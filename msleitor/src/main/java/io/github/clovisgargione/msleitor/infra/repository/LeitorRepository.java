package io.github.clovisgargione.msleitor.infra.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.clovisgargione.msleitor.domain.Leitor;

public interface LeitorRepository extends JpaRepository<Leitor, Integer>{

    Optional<Leitor> findByIdUsuario(Integer id);
}
