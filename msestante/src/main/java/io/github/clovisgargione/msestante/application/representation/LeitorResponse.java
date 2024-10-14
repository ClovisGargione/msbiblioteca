package io.github.clovisgargione.msestante.application.representation;

public class LeitorResponse {

    private Integer id;

    private String nome;

    private Integer idUsuario;

    public LeitorResponse() {
	super();
    }

    public LeitorResponse(Integer id, String nome, Integer idUsuario) {
	super();
	this.id = id;
	this.nome = nome;
	this.idUsuario = idUsuario;
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

}
