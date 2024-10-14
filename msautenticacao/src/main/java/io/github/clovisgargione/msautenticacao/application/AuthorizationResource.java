package io.github.clovisgargione.msautenticacao.application;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.clovisgargione.msautenticacao.config.security.JpaOAuth2AuthorizationService;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Token")
@RestController
@RequestMapping
public class AuthorizationResource {

    private JpaOAuth2AuthorizationService authorizationService;

    private PasswordEncoder userPasswordEncoder;

    public AuthorizationResource(JpaOAuth2AuthorizationService authorizationService,
	    PasswordEncoder userPasswordEncoder) {
	super();
	this.authorizationService = authorizationService;
	this.userPasswordEncoder = userPasswordEncoder;
    }

    @GetMapping("secure/info-token")
    public ResponseEntity<?> info(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
	String[] tokenpParts = authorization.split(" ");
	return new ResponseEntity<>(authorizationService.findByToken(tokenpParts[1]), HttpStatus.OK);
    }

    @GetMapping("public/encrypt/{password}")
    public String encrypt(@PathVariable("password") String password) {
	return userPasswordEncoder.encode(password);
    }

}
