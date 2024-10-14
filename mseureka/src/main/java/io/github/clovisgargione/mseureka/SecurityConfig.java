package io.github.clovisgargione.mseureka;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain authFilterChain(HttpSecurity httpSecurity) throws Exception {
	httpSecurity
		.authorizeHttpRequests(
			req -> req.anyRequest().authenticated())
		.httpBasic(withDefaults())
		.csrf((c) -> c.disable());
	return httpSecurity.build();
    }
}
