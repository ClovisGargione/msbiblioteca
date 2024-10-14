package io.github.clovisgargione.msestante.application.exception;

public class DadosLeitorNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public DadosLeitorNotFoundException() {
	super("Dados do leitor não econtrados para o usuário logado.");
    }

    
}
