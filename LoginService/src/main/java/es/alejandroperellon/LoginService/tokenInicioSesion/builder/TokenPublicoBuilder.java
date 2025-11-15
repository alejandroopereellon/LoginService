package es.alejandroperellon.LoginService.tokenInicioSesion.builder;

import es.alejandroperellon.LoginService.tokenInicioSesion.dto.DTOTokenPublico;
import es.alejandroperellon.LoginService.tokenInicioSesion.model.Token;

/**
 * Construye un {@link DTOTokenPublico} a partir de un {@link Token} interno.
 * Este DTO se envía al cliente y contiene únicamente la información pública del
 * token.
 * 
 * @author Alejandro Perellón López
 * @version 1.0
 */
public class TokenPublicoBuilder {

	/**
	 * Crea el {@link DTOTokenPublico} copiando la caducidad y el identificador del
	 * {@link Token}.
	 *
	 * @return {@link DTOTokenPublico} con los datos públicos del {@link Token}.
	 */

	public DTOTokenPublico construirTokenPublico(Token token) {
		DTOTokenPublico dto = new DTOTokenPublico();

		// Establecemos los datos del token
		dto.setCaducidadToken(token.getCaducidadToken());
		dto.setTokenUsuario(token.getTokenUsuario());

		return dto;
	}
}
