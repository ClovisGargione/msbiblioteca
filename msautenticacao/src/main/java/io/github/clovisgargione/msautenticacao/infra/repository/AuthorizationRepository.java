package io.github.clovisgargione.msautenticacao.infra.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.clovisgargione.msautenticacao.domain.autenticacao.Authorization;


public interface AuthorizationRepository extends JpaRepository<Authorization, String> {

	Optional<Authorization> findByState(String state);
	Optional<Authorization> findByAuthorizationCodeValue(String authorizationCode);
	Optional<Authorization> findByAccessTokenValue(String accessToken);
	Optional<Authorization> findByRefreshTokenValue(String refreshToken);
	@Query("select a from Authorization a where a.state = :token" +
			" or a.authorizationCodeValue = :token" +
			" or a.accessTokenValue = :token" +
			" or a.refreshTokenValue = :token"
	)
	Optional<Authorization> findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValue(@Param("token") String token);

	@Query("select a from Authorization a where a.username = ?1 and a.authorizationGrantType like ?2")
	Optional<Authorization> findByUsernameAndGrantType(String idUsuario, String grantType);
}
