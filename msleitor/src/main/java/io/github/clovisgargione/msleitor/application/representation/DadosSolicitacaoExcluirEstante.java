package io.github.clovisgargione.msleitor.application.representation;

public class DadosSolicitacaoExcluirEstante {

    private Integer idLeitor;

    public DadosSolicitacaoExcluirEstante(Integer idLeitor) {
	super();
	this.idLeitor = idLeitor;
    }

    public DadosSolicitacaoExcluirEstante() {
	super();
    }

    public Integer getIdLeitor() {
        return idLeitor;
    }

    public void setIdLeitor(Integer idLeitor) {
        this.idLeitor = idLeitor;
    }
}
