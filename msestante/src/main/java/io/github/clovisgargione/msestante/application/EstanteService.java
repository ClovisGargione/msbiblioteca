package io.github.clovisgargione.msestante.application;

import java.util.Optional;

import org.springframework.stereotype.Service;

import io.github.clovisgargione.msestante.application.exception.EstanteException;
import io.github.clovisgargione.msestante.domain.Estante;
import io.github.clovisgargione.msestante.infra.repository.EstanteRepository;

@Service
public class EstanteService {

    private EstanteRepository repository;

    public EstanteService(EstanteRepository repository) {
	super();
	this.repository = repository;
    }

    public Estante criar(Integer idLeitor) throws EstanteException {
	Optional<Estante> estanteOpt = repository.findByIdLeitor(idLeitor);
	if (estanteOpt.isEmpty()) {
	    Estante estante = new Estante(idLeitor);
	    return repository.save(estante);
	}
	throw new EstanteException("O leitor já possui estante cadastrada.");
    }
    
    public Estante atualizar(Estante estante) throws EstanteException {
	Optional<Estante> estanteOpt = repository.findById(estante.getId());
	if (estanteOpt.isPresent()) {
	    return repository.save(estante);
	}
	throw new EstanteException("O leitor não possui estante cadastrada.");
    }

    public Estante buscarEstanteDeLivros(Integer idLeitor) throws EstanteException {
	Optional<Estante> estanteOpt = repository.findByIdLeitor(idLeitor);
	if (estanteOpt.isPresent()) {
	    return estanteOpt.get();
	}
	throw new EstanteException("O leitor não possui estante cadastrada.");
    }
    
    public Estante buscarPorId(Integer id) throws EstanteException {
	Optional<Estante> estanteOpt = repository.findById(id);
	if (estanteOpt.isPresent()) {
	    return estanteOpt.get();
	}
	throw new EstanteException("O leitor não possui estante cadastrada.");
    }

    public void remover(Integer idLeitor) throws EstanteException {
	Optional<Estante> estanteOpt = repository.findByIdLeitor(idLeitor);
	if(estanteOpt.isEmpty()) {
	    throw new EstanteException("O leitor não possui estante cadastrada.");
	}
	repository.delete(estanteOpt.get());
    }

}
