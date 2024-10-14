package io.github.clovisgargione.msleitor.application;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.clovisgargione.msleitor.application.exception.LeitorException;
import io.github.clovisgargione.msleitor.application.representation.LeitorRequest;
import io.github.clovisgargione.msleitor.domain.Leitor;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name="Leitor")
@RestController
@RequestMapping("secure/leitor")
public class LeitorResource {
    
    private LeitorService service;

    public LeitorResource(LeitorService service) {
	super();
	this.service = service;
    }
    
    @PostMapping
    public ResponseEntity<?> criarLeitor(@RequestBody LeitorRequest leitorRequest) {
	Leitor leitor;
	try {
	    leitor = service.criar(leitorRequest);
	} catch (LeitorException e) {
	    return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}
	return ResponseEntity.status(HttpStatus.CREATED).body(leitor);
    }
    
    @PutMapping
    public ResponseEntity<?> atualizarLeitor(@RequestBody LeitorRequest leitorRequest) {
	Leitor leitor = service.atualizar(leitorRequest);
	return ResponseEntity.ok(leitor);
    }
    
    @GetMapping(params = "id")
    public ResponseEntity<?> buscarPorId(@RequestParam("id") Integer id){
	try {
	    Leitor leitor = service.buscarPorId(id);
	    return ResponseEntity.ok(leitor);
	} catch (LeitorException e) {
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
    }
    
    @GetMapping(value = "usuario", params="idUsuario")
    public ResponseEntity<?> buscarPorUsuario(@RequestParam("idUsuario") Integer idUsuario){
	try {
	    Leitor leitor = service.buscarPorIdUsuario(idUsuario);
	    return ResponseEntity.ok(leitor);
	} catch (LeitorException e) {
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
    }
    
    @DeleteMapping(params = "id")
    public ResponseEntity<?> removerLeitor(@RequestParam("id") Integer id){
	try {
	    service.remover(id);
	    return ResponseEntity.noContent().build();
	} catch (LeitorException e) {
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
    }
    
    @DeleteMapping(value = "usuario", params = "id")
    public ResponseEntity<?> removerLeitorPorUsuario(@RequestParam("id") Integer id){
	try {
	    service.removerPorUsuario(id);
	    return ResponseEntity.noContent().build();
	} catch (LeitorException e) {
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
    }
    
    @GetMapping("server")
    @ResponseBody
    public String server(HttpServletRequest request) {
	return request.getServerName() + ":" + request.getServerPort();
    }
}
