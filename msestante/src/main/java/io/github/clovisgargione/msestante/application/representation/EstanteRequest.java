package io.github.clovisgargione.msestante.application.representation;

import io.github.clovisgargione.msestante.domain.Estante;

public class EstanteRequest {

    private Integer id;

    private Integer idLeitor;

    public EstanteRequest(Integer id, Integer idLeitor) {
	super();
	this.id = id;
	this.idLeitor = idLeitor;
    }

    public EstanteRequest() {
	super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdLeitor() {
        return idLeitor;
    }

    public void setIdLeitor(Integer idLeitor) {
        this.idLeitor = idLeitor;
    }
    
    public Estante toModel() {
	return new Estante(idLeitor); 
    }
}
