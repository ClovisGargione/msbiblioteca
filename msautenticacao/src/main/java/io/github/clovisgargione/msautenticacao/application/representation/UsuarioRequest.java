package io.github.clovisgargione.msautenticacao.application.representation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.github.clovisgargione.msautenticacao.domain.usuario.Credenciais;

public class UsuarioRequest {

    private Long id;

    private String nome;

    private Credenciais credenciais;

    private List<AuthorityDTO> authorities;

    public UsuarioRequest() {
	super();
    }

    public UsuarioRequest(String nome, Credenciais credenciais, List<AuthorityDTO> authorities) {
	super();
	this.nome = nome;
	this.credenciais = credenciais;
	this.authorities = authorities;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getNome() {
	return nome;
    }

    public void setNome(String nome) {
	this.nome = nome;
    }

    public Credenciais getCredenciais() {
	return credenciais;
    }

    public void setCredenciais(Credenciais credenciais) {
	this.credenciais = credenciais;
    }

    public List<AuthorityDTO> getAuthorities() {
	return authorities;
    }

    public void setAuthorities(List<AuthorityDTO> authorities) {
	this.authorities = authorities;
    }

    public List<GrantedAuthority> mapToGrantedAuthorities() {
	return authorities.stream().map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
		.collect(Collectors.toList());
    }
}
