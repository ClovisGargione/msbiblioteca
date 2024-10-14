package io.github.clovisgargione.msleitor.application.representation;

import io.github.clovisgargione.msleitor.domain.Leitor;

public class LeitorRequest {

    private Integer id;

    private String nome;

    private Integer idUsuario;

    public LeitorRequest(Integer id, String nome, Integer idUsuario) {
	super();
	this.id = id;
	this.nome = nome;
	this.idUsuario = idUsuario;
    }

    public LeitorRequest() {
	super();
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

    public Leitor toModelSave() {
	return new Leitor(nome, idUsuario);
    }
    
    public Leitor toModelUpdate() {
	return new Leitor(id, nome, idUsuario);
    }
}
