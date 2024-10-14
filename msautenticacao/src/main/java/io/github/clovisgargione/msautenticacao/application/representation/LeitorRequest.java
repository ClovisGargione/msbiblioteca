package io.github.clovisgargione.msautenticacao.application.representation;


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
    
    public LeitorRequest(String nome, Integer idUsuario) {
	super();
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

}
