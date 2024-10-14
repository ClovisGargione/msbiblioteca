package io.github.clovisgargione.msautenticacao.domain.usuario;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Embedded
    private Credenciais credenciais;

    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
    @JoinColumn(name = "usuario_id")
    private List<Authority> authorities;

    public Usuario() {
	super();
    }

    public Usuario(String nome, Credenciais credenciais, List<Authority> authorities) {
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

    public List<Authority> getAuthorities() {
	return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
	this.authorities = authorities;
    }

    public List<GrantedAuthority> mapToGrantedAuthorities() {
	return authorities.stream().map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
		.collect(Collectors.toList());
    }
}
