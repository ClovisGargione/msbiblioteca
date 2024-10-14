package io.github.clovisgargione.msautenticacao.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import io.github.clovisgargione.msautenticacao.domain.usuario.Usuario;
import io.github.clovisgargione.msautenticacao.infra.repository.UsuarioRepository;

@Component
public class UsuarioLogado {

    private UsuarioRepository repository;

    public UsuarioLogado(UsuarioRepository repository) {
	super();
	this.repository = repository;
    }

    public Usuario jwt() {
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	Jwt usuario = (Jwt) authentication.getPrincipal();
	return repository.findByUsuario(usuario.getClaim("sub")).orElseThrow();
    }
}
