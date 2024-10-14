package io.github.clovisgargione.msautenticacao.domain.usuario;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class Credenciais {

    @NotNull
    private String usuario;

    @NotNull
    private String senha;

    @NotNull
    private boolean habilitado;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date dataUltimaRedefinicaoDeSenha;

    @NotNull
    private boolean contaNaoExpirada;

    @NotNull
    private boolean contaNaoBloqueada;

    @NotNull
    private boolean credencialNaoExpirada;

    public Credenciais() {
	super();
    }

    public Credenciais(@NotNull String usuario, @NotNull String senha, @NotNull Boolean habilitado,
	    @NotNull Date dataUltimaRedefinicaoDeSenha, @NotNull boolean contaNaoExpirada,
	    @NotNull boolean contaNaoBloqueada, @NotNull boolean credencialNaoExpirada) {
	super();
	this.usuario = usuario;
	this.senha = senha;
	this.habilitado = habilitado;
	this.dataUltimaRedefinicaoDeSenha = dataUltimaRedefinicaoDeSenha;
	this.contaNaoExpirada = contaNaoExpirada;
	this.contaNaoBloqueada = contaNaoBloqueada;
	this.credencialNaoExpirada = credencialNaoExpirada;
    }

    public String getUsuario() {
	return usuario;
    }

    public void setEmail(String usuario) {
	this.usuario = usuario;
    }

    public String getSenha() {
	return senha;
    }

    public void setSenha(String senha) {
	this.senha = senha;
    }

    public Boolean isHabilitado() {
	return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
	this.habilitado = habilitado;
    }

    public Date getDataUltimaRedefinicaoDeSenha() {
	return dataUltimaRedefinicaoDeSenha;
    }

    public void setDataUltimaRedefinicaoDeSenha(Date dataUltimaRedefinicaoDeSenha) {
	this.dataUltimaRedefinicaoDeSenha = dataUltimaRedefinicaoDeSenha;
    }

    public boolean isContaNaoExpirada() {
	return contaNaoExpirada;
    }

    public void setContaNaoExpirada(boolean contaNaoExpirada) {
	this.contaNaoExpirada = contaNaoExpirada;
    }

    public boolean isContaNaoBloqueada() {
	return contaNaoBloqueada;
    }

    public void setContaNaoBloqueada(boolean contaNaoBloqueada) {
	this.contaNaoBloqueada = contaNaoBloqueada;
    }

    public boolean isCredencialNaoExpirada() {
	return credencialNaoExpirada;
    }

    public void setCredencialNaoExpirada(boolean credencialNaoExpirada) {
	this.credencialNaoExpirada = credencialNaoExpirada;
    }
}
