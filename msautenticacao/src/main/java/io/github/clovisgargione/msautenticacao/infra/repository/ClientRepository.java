package io.github.clovisgargione.msautenticacao.infra.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.clovisgargione.msautenticacao.domain.autenticacao.Client;

public interface ClientRepository extends JpaRepository<Client, String> {

    Optional<Client> findByClientId(String clientId);
}
