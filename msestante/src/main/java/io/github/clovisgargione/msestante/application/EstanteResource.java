package io.github.clovisgargione.msestante.application;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.clovisgargione.msestante.application.exception.EstanteException;
import io.github.clovisgargione.msestante.application.representation.EstanteRequest;
import io.github.clovisgargione.msestante.domain.Estante;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Estante")
@RestController
@RequestMapping("secure/estante")
public class EstanteResource {

    private EstanteService service;
    
    public EstanteResource(EstanteService service) {
	super();
	this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody EstanteRequest estanteRequest){
	try {
	    Estante estante = service.criar(estanteRequest.getIdLeitor());
	    return ResponseEntity.status(HttpStatus.CREATED).body(estante);
	} catch (EstanteException e) {
	    return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}
    }
    
    @GetMapping(params = "idLeitor")
    public ResponseEntity<?> mostrarEstanteDeLivros(@RequestParam("idLeitor") Integer idLeitor){
	try {
	    Estante estante = service.buscarEstanteDeLivros(idLeitor);
	    return ResponseEntity.ok(estante);
	} catch (EstanteException e) {
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
    }
    
    @DeleteMapping(params = "idLeitor")
    public ResponseEntity<?> limparEstante(@RequestParam("idLeitor") Integer idLeitor){
	try {
	    service.remover(idLeitor);
	    return ResponseEntity.noContent().build();
	} catch (EstanteException e) {
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
    }
    
}
