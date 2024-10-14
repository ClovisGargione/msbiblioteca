package io.github.clovisgargione.msautenticacao.application.representation;

import io.github.clovisgargione.msautenticacao.domain.usuario.AuthorityName;
import jakarta.validation.constraints.NotNull;

public class AuthorityDTO {

    private AuthorityName name;

    public AuthorityDTO() {
	super();
    }

    public AuthorityDTO(@NotNull AuthorityName name) {
	super();
	this.name = name;
    }

    public AuthorityName getName() {
	return name;
    }

    public void setName(AuthorityName name) {
	this.name = name;
    }
}
