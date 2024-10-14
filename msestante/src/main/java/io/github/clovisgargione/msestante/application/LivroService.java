package io.github.clovisgargione.msestante.application;

import static java.util.Objects.nonNull;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import feign.FeignException.FeignClientException;
import io.github.clovisgargione.msestante.application.exception.EstanteException;
import io.github.clovisgargione.msestante.application.exception.LeitorException;
import io.github.clovisgargione.msestante.application.exception.LivroNotFoundException;
import io.github.clovisgargione.msestante.application.representation.LeitorResponse;
import io.github.clovisgargione.msestante.application.representation.LivroRequest;
import io.github.clovisgargione.msestante.domain.Estante;
import io.github.clovisgargione.msestante.domain.Livro;
import io.github.clovisgargione.msestante.infra.clients.LeitorResourceClient;
import io.github.clovisgargione.msestante.infra.repository.LivroRepository;

@Service
public class LivroService {

    private LivroRepository repository;

    private EstanteService estanteService;
    
    private LeitorResourceClient leitorResourceClient;

    public LivroService(LivroRepository repository, EstanteService estanteService, LeitorResourceClient leitorResourceClient) {
	super();
	this.repository = repository;
	this.estanteService = estanteService;
	this.leitorResourceClient = leitorResourceClient;
    }

    public Livro cadastrar(LivroRequest livroRequest) throws EstanteException, LeitorException {
	Optional<Estante> estanteOpt = Optional.empty();
	Estante estante = new Estante();
	try {
	    estanteOpt = buscarEstante(livroRequest);
	    if (estanteOpt.isEmpty()) {
		throw new EstanteException("O leitor não possui estante cadastrada.");
	    }
	    estante = estanteOpt.get();
	} catch (EstanteException e) {
	    estante = estanteService.criar(livroRequest.getIdLeitor());
	}
	Livro livro = livroRequest.toModelSave();
	if (!estante.temLivros()) {
	    estante.setLivros(Arrays.asList(livro));
	} else {
	    estante.getLivros().add(livro);
	}
	livro = repository.save(livro);
	estanteService.atualizar(estante);
	return livro;
    }

    public Livro atualizar(LivroRequest livrorequest) throws LivroNotFoundException {
	Optional<Livro> livroOpt = repository.findById(livrorequest.getId());
	if (livroOpt.isEmpty()) {
	    throw new LivroNotFoundException("Livro não cadastrado id:" + livrorequest.getId());
	}
	Livro livro = livrorequest.toModelUpdate();
	return repository.save(livro);
    }

    public Livro buscar(Integer id) throws LivroNotFoundException {
	Optional<Livro> livro = repository.findById(id);
	if (livro.isEmpty()) {
	    throw new LivroNotFoundException("Livro não cadastrado.");
	}
	return livro.get();
    }

    public void remover(Integer id) throws LivroNotFoundException {
	Optional<Livro> livro = repository.findById(id);
	if (livro.isEmpty()) {
	    throw new LivroNotFoundException("Livro não cadastrado.");
	}
	repository.delete(livro.get());
    }

    private Optional<Estante> buscarEstante(LivroRequest livroRequest) throws EstanteException, LeitorException {
	Optional<Estante> estante = Optional.empty();
	try {
	    ResponseEntity<LeitorResponse> response = leitorResourceClient.buscarPorId(livroRequest.getIdLeitor());
	} catch(FeignClientException fce) {
	    if(fce.status() == 404) {
		throw new LeitorException("O leitor informado não está cadastrado.");
	    }
	}
	if (nonNull(livroRequest.getIdEstante())) {
	    estante = Optional.ofNullable(estanteService.buscarPorId(livroRequest.getIdEstante()));
	}
	if (estante.isEmpty() && nonNull(livroRequest.getIdLeitor())) {
	    estante = Optional.ofNullable(estanteService.buscarEstanteDeLivros(livroRequest.getIdLeitor()));
	}
	return estante;
    }
}
