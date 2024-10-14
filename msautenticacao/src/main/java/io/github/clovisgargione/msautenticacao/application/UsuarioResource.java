package io.github.clovisgargione.msautenticacao.application;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.clovisgargione.msautenticacao.application.exception.ErroComunicacaoMicroservicesException;
import io.github.clovisgargione.msautenticacao.application.exception.UsuarioException;
import io.github.clovisgargione.msautenticacao.application.representation.UsuarioRequest;
import io.github.clovisgargione.msautenticacao.domain.usuario.Usuario;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Usuário")
@RestController
@RequestMapping
public class UsuarioResource {

    private UsuarioService usuarioService;

    public UsuarioResource(UsuarioService usuarioService) {
	super();
	this.usuarioService = usuarioService;
    }

    @PostMapping("/public/usuario")
    public ResponseEntity<?> registrar(@RequestBody UsuarioRequest usuarioRequest) {
	Usuario usuario;
	try {
	    usuario = usuarioService.cadastrar(usuarioRequest);
	} catch (UsuarioException e) {
	    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Usuário já cadastrado!");
	} catch (ErroComunicacaoMicroservicesException e) {
	    return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
	}
	return ResponseEntity.ok(usuario);
    }

    @GetMapping(value = "/secure/usuario", params = "login")
    public ResponseEntity<?> buscar(@RequestParam(name = "login") String login) {
	Usuario usuario;
	try {
	    usuario = usuarioService.buscarPorLogin(login);
	} catch (UsuarioException e) {
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	return ResponseEntity.ok(usuario);
    }

    @DeleteMapping(value = "/secure/usuario", params = "id")
    public ResponseEntity<?> remover(@RequestParam(name = "id") Long id) {
	try {
	    usuarioService.removerUsuario(id);
	} catch (UsuarioException e) {
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}
	return ResponseEntity.noContent().build();
    }
}