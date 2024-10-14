package io.github.clovisgargione.msautenticacao.config.security;

import static org.springframework.security.config.Customizer.withDefaults;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import io.github.clovisgargione.msautenticacao.config.JwtProperties;
import io.github.clovisgargione.msautenticacao.config.security.customGrantType.CustomPasswordGrantAuthenticationConverter;
import io.github.clovisgargione.msautenticacao.config.security.customGrantType.CustomPasswordGrantAuthenticationProvider;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class ConfiguracaoOAuth2 {

    public static final String ALGORITHM = "RSA";

    /**
     * Local da chave privada no sistema de arquivos.
     */
    public static final String PATH_CHAVE_PRIVADA = "keys/private.key";

    /**
     * Local da chave pública no sistema de arquivos.
     */
    public static final String PATH_CHAVE_PUBLICA = "keys/public.key";

    @Autowired
    private PasswordEncoder userPasswordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtProperties jwtProperties;

    @Bean
    SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
	OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
	http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
		.tokenEndpoint(tokenEndpoint -> tokenEndpoint
			.accessTokenRequestConverter(new CustomPasswordGrantAuthenticationConverter())
			.authenticationProvider(new CustomPasswordGrantAuthenticationProvider(tokenGenerator(),
				userDetailsService, userPasswordEncoder)));
	return http.formLogin(withDefaults()).build();
    }

    @Bean
    OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator() {
	NimbusJwtEncoder jwtEncoder = new NimbusJwtEncoder(jwkSource());
	JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
	jwtGenerator.setJwtCustomizer(tokenCustomizer());
	OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
	OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
	return new DelegatingOAuth2TokenGenerator(jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
    }

    @Bean
    JWKSource<SecurityContext> jwkSource() {
	KeyPair keyPair = null;
	RSAPublicKey publicKey = null;
	RSAPrivateKey privateKey = null;
	if (!verificaSeExisteChavesNoSO()) {
	    keyPair = generateRsaKey();
	    publicKey = (RSAPublicKey) keyPair.getPublic();
	    privateKey = (RSAPrivateKey) keyPair.getPrivate();
	} else {
	    ObjectInputStream inputStream = null;
	    try {
		inputStream = new ObjectInputStream(new FileInputStream(PATH_CHAVE_PUBLICA));
		publicKey = (RSAPublicKey) inputStream.readObject();
		inputStream.close();
		inputStream = new ObjectInputStream(new FileInputStream(PATH_CHAVE_PRIVADA));
		privateKey = (RSAPrivateKey) inputStream.readObject();
		inputStream.close();
	    } catch (IOException | ClassNotFoundException e) {
		e.printStackTrace();
	    }
	}
	RSAKey rsaKey = new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(jwtProperties.getSecretKey())
		.build();
	JWKSet jwkSet = new JWKSet(rsaKey);
	return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
	return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
	return context -> {
	    Authentication principal = context.getPrincipal();
	    if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
		Set<String> authorities = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority)
			.collect(Collectors.toSet());
		context.getClaims().claim("authorities", authorities);
	    }
	};
    }

    private static KeyPair generateRsaKey() {
	KeyPair keyPair;
	try {
	    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
	    keyPairGenerator.initialize(2048);
	    keyPair = keyPairGenerator.generateKeyPair();

	    File chavePrivadaFile = new File(PATH_CHAVE_PRIVADA);
	    File chavePublicaFile = new File(PATH_CHAVE_PUBLICA);

	    // Cria os arquivos para armazenar a chave Privada e a chave Publica
	    if (chavePrivadaFile.getParentFile() != null) {
		chavePrivadaFile.getParentFile().mkdirs();
	    }

	    chavePrivadaFile.createNewFile();

	    if (chavePublicaFile.getParentFile() != null) {
		chavePublicaFile.getParentFile().mkdirs();
	    }

	    chavePublicaFile.createNewFile();

	    // Salva a Chave Pública no arquivo
	    ObjectOutputStream chavePublicaOS = new ObjectOutputStream(new FileOutputStream(chavePublicaFile));
	    chavePublicaOS.writeObject(keyPair.getPublic());
	    chavePublicaOS.close();

	    // Salva a Chave Privada no arquivo
	    ObjectOutputStream chavePrivadaOS = new ObjectOutputStream(new FileOutputStream(chavePrivadaFile));
	    chavePrivadaOS.writeObject(keyPair.getPrivate());
	    chavePrivadaOS.close();
	} catch (Exception ex) {
	    throw new IllegalStateException(ex);
	}
	return keyPair;
    }

    /**
     * Verifica se o par de chaves Pública e Privada já foram geradas.
     */
    public static boolean verificaSeExisteChavesNoSO() {

	File chavePrivada = new File(PATH_CHAVE_PRIVADA);
	File chavePublica = new File(PATH_CHAVE_PUBLICA);

	if (chavePrivada.exists() && chavePublica.exists()) {
	    return true;
	}

	return false;
    }
}
