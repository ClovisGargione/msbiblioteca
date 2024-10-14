package io.github.clovisgargione.msestante.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.clovisgargione.msestante.domain.Livro;

public interface LivroRepository extends JpaRepository<Livro, Integer> {

}
