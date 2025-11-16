package es.alejandroperellon.LoginService.tokenInicioSesion.builder;

import java.time.LocalDateTime;
import java.util.UUID;

import es.alejandroperellon.LoginService.tokenInicioSesion.model.TipoToken;
import es.alejandroperellon.LoginService.tokenInicioSesion.model.Token;
import es.alejandroperellon.LoginService.usuarios.model.Usuario;

/**
 * Crea un nuevo {@link Token} asociado a un {@link Usuario}, con un UUID
 * aleatorio y fecha de caducidad a 2 horas desde el momento actual.
 * 
 * @author Alejandro Perellón López
 * @version 1.0
 */
public class TokenBuilder {
	/**
	 * Metodo que genera un nuevo token y lo almacena en la base de datos
	 * 
	 * * Genera un nuevo {@link Token} para el usuario indicado y lo inicializa
	 * 
	 * 
	 * @param usuario es el objeto {@link Usuario} que se va a añadir en la
	 *                informacion del token
	 * @return {@link Token} con la informacion del token
	 */
	public Token nuevoToken(Usuario usuario, TipoToken tipo) {
		// Creamos el token del usuario
		Token token = new Token();

		// Establecemos los datos del token
		// Usuario
		token.setUsuario(usuario);
		// Establecemos la caducidad del token
		if (tipo.equals(TipoToken.LARGO)) {
			token.setCaducidadToken(LocalDateTime.now().plusWeeks(1));
		} else {
			token.setCaducidadToken(LocalDateTime.now().plusHours(2));
		}

		// Generamos el numero de token para enviar al usuario
		token.setTokenUsuario(UUID.randomUUID());
		// Añadimos el tipo de token
		token.setTipoToken(tipo);

		// Retornamos el token
		return token;
	}
}
