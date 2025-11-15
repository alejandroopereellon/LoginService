package es.alejandroperellon.LoginService.tokenInicioSesion.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.alejandroperellon.LoginService.tokenInicioSesion.model.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

	/**
	 * Busca un {@link Token} por su identificador público.
	 *
	 * @param tokenUsuario identificador del token (UUID enviado al cliente).
	 * @return {@link Optional} con el token si existe, o vacío si no se encuentra.
	 */

	public Optional<Token> findByTokenUsuario(UUID tokenUsuario);
}
