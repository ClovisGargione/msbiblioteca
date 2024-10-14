package io.github.clovisgargione.msautenticacao.application.representation;

public class DadosSolicitacaoExcluirLeitor {

    private Integer idUsuario;

    public DadosSolicitacaoExcluirLeitor() {
	super();
    }

    public DadosSolicitacaoExcluirLeitor(Integer idUsuario) {
	super();
	this.idUsuario = idUsuario;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
}
