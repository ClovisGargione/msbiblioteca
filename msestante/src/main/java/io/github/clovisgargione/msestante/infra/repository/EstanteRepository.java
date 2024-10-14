package io.github.clovisgargione.msestante.infra.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.clovisgargione.msestante.domain.Estante;

public interface EstanteRepository extends JpaRepository<Estante, Integer>{

    Optional<Estante> findByIdLeitor(Integer idLeitor);
}
