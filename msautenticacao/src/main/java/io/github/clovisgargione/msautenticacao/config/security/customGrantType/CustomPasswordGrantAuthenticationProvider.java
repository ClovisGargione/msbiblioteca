package io.github.clovisgargione.msautenticacao.config.security.customGrantType;

import java.security.Principal;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;

import io.github.clovisgargione.msautenticacao.config.ResourceOwner;
import io.github.clovisgargione.msautenticacao.config.security.JpaOAuth2AuthorizationService;
import io.github.clovisgargione.msautenticacao.domain.autenticacao.Authorization;

public class CustomPasswordGrantAuthenticationProvider implements AuthenticationProvider {

    private static final String CUSTOM_PASSWORD = "password";
    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";
    @Autowired
    private JpaOAuth2AuthorizationService authorizationService;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private String username = new String();
    private String password = new String();
    private Set<String> authorizedScopes = new HashSet<>();

    public CustomPasswordGrantAuthenticationProvider(OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
	    UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
	Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
	this.tokenGenerator = tokenGenerator;
	this.userDetailsService = userDetailsService;
	this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
	CustomPasswordGrantAuthenticationToken customCodeGrantAuthentication = (CustomPasswordGrantAuthenticationToken) authentication;
	OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(
		customCodeGrantAuthentication);
	RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
	username = customCodeGrantAuthentication.getUsername();
	password = customCodeGrantAuthentication.getPassword();
	authorizedScopes = customCodeGrantAuthentication.getScope();
	ResourceOwner user = null;
	try {
	    user = (ResourceOwner) userDetailsService.loadUserByUsername(username);
	} catch (UsernameNotFoundException e) {
	    throw new OAuth2AuthenticationException(OAuth2ErrorCodes.ACCESS_DENIED);
	}
	if (!passwordEncoder.matches(password, user.getPassword()) || !user.getUsername().equals(username)) {
	    throw new OAuth2AuthenticationException(OAuth2ErrorCodes.ACCESS_DENIED);
	}
	if (!registeredClient.getAuthorizationGrantTypes().contains(customCodeGrantAuthentication.getGrantType())) {
	    throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
	}
	authorizedScopes.forEach(scope -> {
	    if (!registeredClient.getScopes().contains(scope)) {
		throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
	    }
	});
	User u = new User(user.getUsername(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(),
		user.isCredentialsNonExpired(), user.isAccountNonLocked(), user.getAuthorities());
	Authentication usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(u, null,
		user.getAuthorities());

	DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
		.registeredClient(registeredClient).principal(usernamePasswordAuthenticationToken)
		.authorizationServerContext(AuthorizationServerContextHolder.getContext())
		.authorizedScopes(authorizedScopes).authorizationGrantType(customCodeGrantAuthentication.getGrantType())
		.authorizationGrant(customCodeGrantAuthentication);

	// ----- Access Token -----
	Authorization authorization_ = this.authorizationService.findByUsernameAndGrantType(user.getUsername(),
		CUSTOM_PASSWORD);

	OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
	OAuth2Token generatedAccessToken;
	if (authorization_ != null) {
	    if (authorization_.getAccessTokenExpiresAt().isBefore(Instant.now())) {
		this.authorizationService
			.remove(OAuth2Authorization.withRegisteredClient(registeredClient).id(authorization_.getId())
				.principalName(clientPrincipal.getName()).authorizedScopes(authorizedScopes)
				.authorizationGrantType(new AuthorizationGrantType(CUSTOM_PASSWORD)).build());
		generatedAccessToken = this.tokenGenerator.generate(tokenContext);
	    } else {
		generatedAccessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
			authorization_.getAccessTokenValue(), Instant.now(),
			authorization_.getAccessTokenExpiresAt(),
			authorizedScopes);
	    }
	} else {
	    generatedAccessToken = this.tokenGenerator.generate(tokenContext);
	}

	if (generatedAccessToken == null) {
	    OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
		    "The token generator failed to generate the access token.", null);
	    throw new OAuth2AuthenticationException(error);
	}
	OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
		generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
		generatedAccessToken.getExpiresAt(), authorizedScopes);

	OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
		.principalName(clientPrincipal.getName())
		.authorizationGrantType(customCodeGrantAuthentication.getGrantType());

	if (generatedAccessToken instanceof ClaimAccessor) {
	    authorizationBuilder.token(accessToken,
		    (metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
			    ((ClaimAccessor) generatedAccessToken).getClaims()));
	} else {
	    authorizationBuilder.accessToken(accessToken);
	}

	// ----- Refresh Token -----
	OAuth2RefreshToken refreshToken = null;
	if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)
		&& !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {
	    tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
	    OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
	    if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
		OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
			"The token generator failed to generate the refresh token.", ERROR_URI);
		throw new OAuth2AuthenticationException(error);
	    }
	    if (authorization_ == null || authorization_.getRefreshTokenExpiresAt().isBefore(Instant.now())) {
		refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
	    } else {
		refreshToken = new OAuth2RefreshToken(authorization_.getRefreshTokenValue(), Instant.now(),
			authorization_.getRefreshTokenExpiresAt());
	    }
	    authorizationBuilder.refreshToken(refreshToken);
	}

	Map<String, Object> additionalParameters = Collections.emptyMap();
	if (authorization_ == null) {
	    OAuth2Authorization authorization = authorizationBuilder.accessToken(accessToken).refreshToken(refreshToken)
		    .authorizedScopes(authorizedScopes)
		    .attribute(Principal.class.getName(), usernamePasswordAuthenticationToken).build();
	    this.authorizationService.save(authorization);
	}

	return new OAuth2AccessTokenAuthenticationToken(registeredClient, usernamePasswordAuthenticationToken,
		accessToken, refreshToken, additionalParameters);
    }

    @Override
    public boolean supports(Class<?> authentication) {
	return CustomPasswordGrantAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private static OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(
	    Authentication authentication) {
	OAuth2ClientAuthenticationToken clientPrincipal = null;
	if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
	    clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
	}
	if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
	    return clientPrincipal;
	}
	throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }

    public void setSessionRegistry(SessionRegistry sessionRegistry) {
	Assert.notNull(sessionRegistry, "sessionRegistry cannot be null");
    }

}
