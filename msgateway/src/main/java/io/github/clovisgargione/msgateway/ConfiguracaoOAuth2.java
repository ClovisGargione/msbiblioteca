package io.github.clovisgargione.msgateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class ConfiguracaoOAuth2 {

    @Value("${spring.security.oauth2.authorizationserver.issuer}")
    private String issuerLocation;

    @Bean
    SecurityFilterChain oauth2ResourceServerFilterChain(HttpSecurity httpSecurity) throws Exception {
	String[] caminhosPermitidos = new String[] { "/erro", "/home", "/login", "/api/v1/server", "public/encrypt/**",
		"/api/v1/public/**", "/webjars/**", "/js/**", "/jquery*", "/oauth2/authorize**" };
	httpSecurity.securityMatcher("/secure/**", "/api/v1/secure/**")
		.authorizeHttpRequests(req -> req.requestMatchers(caminhosPermitidos).permitAll()
			.requestMatchers("/secure/**", "/api/v1/secure/**").hasAnyAuthority("SCOPE_read", "SCOPE_write")
			.anyRequest().authenticated())
		.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder()))).csrf((c) -> c.disable())
		.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	return httpSecurity.build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
	return NimbusJwtDecoder.withIssuerLocation(issuerLocation).build();
    }
}
