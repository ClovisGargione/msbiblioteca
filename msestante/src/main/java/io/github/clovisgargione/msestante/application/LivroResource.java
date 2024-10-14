package io.github.clovisgargione.msestante.application;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.clovisgargione.msestante.application.exception.EstanteException;
import io.github.clovisgargione.msestante.application.exception.LeitorException;
import io.github.clovisgargione.msestante.application.exception.LivroNotFoundException;
import io.github.clovisgargione.msestante.application.representation.LivroRequest;
import io.github.clovisgargione.msestante.domain.Livro;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Livro")
@RestController
@RequestMapping("secure/livro")
public class LivroResource {

    private LivroService livroService;

    public LivroResource(LivroService livroService) {
	super();
	this.livroService = livroService;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody @Validated LivroRequest livroRequest) {
	try {
	    Livro livro = livroService.cadastrar(livroRequest);
	    return ResponseEntity.status(HttpStatus.CREATED).body(livro);
	} catch (EstanteException | LeitorException e) {
	    return ResponseEntity.internalServerError().body(e.getMessage());
	}
    }

    @PutMapping
    public ResponseEntity<?> atualizar(@RequestBody @Validated LivroRequest livroRequest) {
	try {
	    Livro livro = livroService.atualizar(livroRequest);
	    return ResponseEntity.ok(livro);
	} catch (LivroNotFoundException e) {
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
    }

    @GetMapping(params = "id")
    public ResponseEntity<?> buscar(@RequestParam("id") Integer id) {
	try {
	    Livro livro = livroService.buscar(id);
	    return ResponseEntity.ok(livro);
	} catch (LivroNotFoundException e) {
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
    }

    @DeleteMapping(params = "id")
    public ResponseEntity<?> remover(@RequestParam("id") Integer id) {
	try {
	    livroService.remover(id);
	    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	} catch (LivroNotFoundException e) {
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
    }

}
