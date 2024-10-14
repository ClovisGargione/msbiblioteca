package io.github.clovisgargione.msleitor.application;

import java.util.Optional;

import org.springframework.stereotype.Service;

import io.github.clovisgargione.msleitor.application.exception.LeitorException;
import io.github.clovisgargione.msleitor.application.representation.LeitorRequest;
import io.github.clovisgargione.msleitor.domain.Leitor;
import io.github.clovisgargione.msleitor.infra.repository.LeitorRepository;

@Service
public class LeitorService {

    private LeitorRepository repository;

    public LeitorService(LeitorRepository repository) {
	super();
	this.repository = repository;
    }

    public Leitor criar(LeitorRequest leitorRequest) throws LeitorException {
	Optional<Leitor> leitorOpt = repository.findByIdUsuario(leitorRequest.getIdUsuario());
	if(leitorOpt.isPresent()) {
	    throw new LeitorException("O leitor % já foi cadastrado.".replace("%", leitorRequest.getNome()));
	}
	Leitor leitor = leitorRequest.toModelSave();
	return repository.save(leitor);
    }

    public Leitor atualizar(LeitorRequest leitorRequest) throws LeitorException {
	Optional<Leitor> leitorOpt = repository.findById(leitorRequest.getId());
	if (leitorOpt.isEmpty()) {
	    throw new LeitorException("Leitor não econtrado");
	}
	Leitor leitor = leitorRequest.toModelUpdate();
	return repository.save(leitor);
    }

    public Leitor buscarPorId(Integer id) throws LeitorException {
	Optional<Leitor> leitor = repository.findById(id);
	if (leitor.isEmpty()) {
	    throw new LeitorException("Leitor não econtrado");
	}
	return leitor.get();
    }

    public Leitor buscarPorIdUsuario(Integer id) throws LeitorException {
	Optional<Leitor> leitor = repository.findByIdUsuario(id);
	if (leitor.isEmpty()) {
	    throw new LeitorException("Leitor não econtrado");
	}
	return leitor.get();
    }

    public void remover(Integer id) throws LeitorException {
	Optional<Leitor> leitor = repository.findById(id);
	if (leitor.isEmpty()) {
	    throw new LeitorException("Leitor não econtrado");
	}
	// precisa remover o usuário e a estante de livros também
	repository.delete(leitor.get());
    }
    
    public void removerPorUsuario(Integer id) throws LeitorException {
	Optional<Leitor> leitor = repository.findByIdUsuario(id);
	if (leitor.isEmpty()) {
	    throw new LeitorException("Leitor não econtrado");
	}
	repository.delete(leitor.get());
    }
}
