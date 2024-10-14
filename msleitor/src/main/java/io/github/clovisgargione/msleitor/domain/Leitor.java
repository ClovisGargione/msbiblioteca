package io.github.clovisgargione.msleitor.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "leitor")
public class Leitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    private Integer idUsuario;
    
    public Leitor(Integer id, String nome, Integer idUsuario) {
	super();
	this.id = id;
	this.nome = nome;
	this.idUsuario = idUsuario;
    }

    public Leitor(String nome, Integer idUsuario) {
	super();
	this.nome = nome;
	this.idUsuario = idUsuario;
    }

    public Leitor() {
	super();
    }

    public Leitor(Builder builder) {
	this.id = builder.id;
	this.nome = builder.nome;
	this.idUsuario = builder.idUsuario;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getNome() {
	return nome;
    }

    public void setNome(String nome) {
	this.nome = nome;
    }

    public Integer getIdUsuario() {
	return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
	this.idUsuario = idUsuario;
    }

    public static class Builder {

	private Integer id;

	private String nome;

	private Integer idUsuario;
	
	public Builder id(Integer id) {
	    this.id = id;
	    return this;
	}
	
	public Builder nome(String nome) {
	    this.nome = nome;
	    return this;
	}
	
	public Builder idUsuario(Integer idUsuario) {
	    this.idUsuario = idUsuario;
	    return this;
	}
	
	public Leitor build() {
	    return new Leitor(this);
	}
    }

}
