package io.github.clovisgargione.msautenticacao.infra.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.github.clovisgargione.msautenticacao.domain.usuario.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("select u from Usuario u where u.credenciais.usuario = ?1")
    Optional<Usuario> findByUsuario(String usuario);
}
