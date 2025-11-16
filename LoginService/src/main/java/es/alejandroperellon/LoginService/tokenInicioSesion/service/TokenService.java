package es.alejandroperellon.LoginService.tokenInicioSesion.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.alejandroperellon.LoginService.clasesAxiliares.ApiResponse;
import es.alejandroperellon.LoginService.tokenInicioSesion.builder.TokenBuilder;
import es.alejandroperellon.LoginService.tokenInicioSesion.builder.TokenPublicoBuilder;
import es.alejandroperellon.LoginService.tokenInicioSesion.dto.DTOTokenPublico;
import es.alejandroperellon.LoginService.tokenInicioSesion.exception.TokenCortoCaducadoException;
import es.alejandroperellon.LoginService.tokenInicioSesion.exception.TokenCortoVigenteException;
import es.alejandroperellon.LoginService.tokenInicioSesion.exception.TokenDiferenteUsuarioException;
import es.alejandroperellon.LoginService.tokenInicioSesion.exception.TokenLargoCaducadoException;
import es.alejandroperellon.LoginService.tokenInicioSesion.exception.TokenNoEncontradoException;
import es.alejandroperellon.LoginService.tokenInicioSesion.exception.TokenRevocadoException;
import es.alejandroperellon.LoginService.tokenInicioSesion.exception.TokenTipoIncorrectoException;
import es.alejandroperellon.LoginService.tokenInicioSesion.model.TipoToken;
import es.alejandroperellon.LoginService.tokenInicioSesion.model.Token;
import es.alejandroperellon.LoginService.tokenInicioSesion.repository.TokenRepository;
import jakarta.validation.Valid;

@Service
public class TokenService {

	private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

	private final TokenRepository tokenRepository;

	public TokenService(TokenRepository tokenRepository) {
		super();
		this.tokenRepository = tokenRepository;
	}

	/**
	 * Renueva el token publico que utiliza el cliente para las consultas, comprueba
	 * si no esta caducado y realiza la renovacion del token para futuras consultas
	 * 
	 * @param tokenPublico es el {@link DTOTokenPublico} que se va a renovar
	 * @return nuevo {@link DTOTokenPublico} con la informacion nueva
	 */
	@Transactional
	public DTOTokenPublico actualizarToken(@Valid DTOTokenPublico tokenPublico) {
		logger.info("Se va a actualizar el token del usuario");

		// Obtenemos el token original del usuario
		Token token = obtenerTokenOriginal(tokenPublico.getTokenUsuario());

		// Comprobamos la fecha de caducidad
		validarTokenNoCaducado(token);

		// Generamos un nuevo token publico y lo retornamos
		DTOTokenPublico tokenPublicoNuevo = generarAlmacenarNuevoToken(token);

		logger.info("Se ha renovado el token del usuario");
		return tokenPublicoNuevo;
	}

	/**
	 * Fuerza la renovacion de un token corto caducado a traves del uso de un token
	 * largo, se utiliza cuando el sistema no ha podido renovar anteriormente el
	 * token (EJ equipo sin conexion, servidor caido etc...)
	 * 
	 * @param tokenPublicoCorto es el {@link DTOTokenPublico} corto, debe estar
	 *                          caducado
	 * @param tokenPublicoLargo es el {@link DTOTokenPublico} largo, debe esta
	 *                          vigente
	 * @return {@link DTOTokenPublico} corto renovado y vigente
	 */
	@Transactional
	public DTOTokenPublico forzarActualizarToken(@Valid DTOTokenPublico tokenPublicoCorto,
			@Valid DTOTokenPublico tokenPublicoLargo) {
		logger.info("Se va a forzar la renovacion de un token corto a traves de el token largo");

		// Obtenemos el token largo y corto desde la BBDD
		Token tokenCorto = obtenerTokenOriginal(tokenPublicoCorto.getTokenUsuario());
		Token tokenLargo = obtenerTokenOriginal(tokenPublicoLargo.getTokenUsuario());
		logger.debug("Se han obtenido el token corto id {} y el token largo id {}", tokenCorto.getIdToken(),
				tokenLargo.getIdToken());

		// Comprobamos si el token pertenece al mismo usuario
		comprobarTokenPertenecenMismoUsuario(tokenCorto, tokenLargo);

		// Comprobamos que el token largo no este caducado
		validarTokenNoCaducado(tokenLargo);

		// Si el token corto sigue vigente, no se puede renovar
		logger.debug("Se va a comprobar la caducidad del token id {}", tokenCorto.getIdToken());
		if (tokenCorto.getCaducidadToken().isAfter(LocalDateTime.now())) {
			logger.info("El token corto con id {} esta vigente, no se puede renovar", tokenCorto.getIdToken());
			throw new TokenCortoVigenteException();
		}

		// Renovamos el token corto
		logger.info("Se va a proceder a generar el token nuevo");
		return generarAlmacenarNuevoToken(tokenCorto);
	}

	/**
	 * Cierra los {@link Token}s largos y cortos asignados al usuario impidiendo el
	 * acceso a la aplicacion
	 * 
	 * @param tokenPublicoCorto token corto que se va a forzar caducidad
	 * @param tokenPublicoLargo token largo que se va a forzar caducidad
	 * @return {@link ApiResponse} con el aviso de cierre de sesion exitoso
	 */
	@Transactional
	public ApiResponse cerrarSesion(@Valid DTOTokenPublico tokenPublicoCorto,
			@Valid DTOTokenPublico tokenPublicoLargo) {
		logger.info("Se va a proceder a cerrar la sesion del usuario");

		// Obtenemos el token largo y corto desde la BBDD
		Token tokenCorto = obtenerTokenOriginal(tokenPublicoCorto.getTokenUsuario());
		Token tokenLargo = obtenerTokenOriginal(tokenPublicoLargo.getTokenUsuario());
		logger.debug("Se han obtenido el token corto id {} y el token largo id {}", tokenCorto.getIdToken(),
				tokenLargo.getIdToken());

		// Comprobamos que el tipo de token pertence a lo previso
		if (!tokenCorto.getTipoToken().equals(TipoToken.CORTO) || !tokenLargo.getTipoToken().equals(TipoToken.LARGO)) {
			logger.info("Unos de los tokens introducidos no son del tipo esperado");
			throw new TokenTipoIncorrectoException();
		}

		// Comprobamos que los tokens pertenecen al mismo usuario
		comprobarTokenPertenecenMismoUsuario(tokenCorto, tokenLargo);

		// Actualizamos las caducidades de los tokens a este momento
		LocalDateTime instante = LocalDateTime.now();
		tokenCorto.setRevocado(instante);
		tokenLargo.setRevocado(instante);
		logger.debug("Se ha revocado el token corto id {} y el token largo id {} a fecha de {}",
				tokenCorto.getIdToken(), tokenLargo.getIdToken(), instante);

		// Almacenamos las actualizaciones de los tokens en la BBDD
		tokenRepository.save(tokenCorto);
		tokenRepository.save(tokenLargo);
		logger.debug("Se han actualizado el token corto id {} y el token largo id {} en la BBDD",
				tokenCorto.getIdToken(), tokenLargo.getIdToken());

		// Confirmamos el cierre de la sesion
		logger.info("Se han cerrado los tokens asignados al usuario");
		return new ApiResponse(true, "TOKEN_AVISO_SESION_CERRADA", "LOGOUT_SUCCESSFUL");

	}

	/**
	 * Obtiene el token original de la BBDD a traves de los datos del token publico
	 * 
	 * @param uuidToken es el {@link UUID} del token que el usuario ha pedido
	 * @return {@link Token} con los datos originales de la base de datos
	 */
	private Token obtenerTokenOriginal(UUID uuidToken) {
		logger.debug("Se va a obtener el token original desde el UUID original del usuario");
		// Recuperamos el token original de la BBDD
		Token token = tokenRepository.findByTokenUsuario(uuidToken).orElseThrow(() -> new TokenNoEncontradoException());
		logger.debug("Se va a retornar el token id {}", token.getIdToken());
		return token;
	}

	/**
	 * Genera un nuevo objeto {@link Token} a traves de otro {@link Token} diferente
	 * 
	 * @param tokenOriginal es el {@link Token} viejo que se va a renovar
	 * @return {@link DTOTokenPublico} con la informacion del nuevo token ya
	 *         generado
	 */
	private DTOTokenPublico generarAlmacenarNuevoToken(Token tokenOriginal) {
		logger.info("Se va a crear un nuevo token para el usuario");

		// Generamos en primera instancia un nuevo token
		Token tokenNuevo = new TokenBuilder().nuevoToken(tokenOriginal.getUsuario(), tokenOriginal.getTipoToken());
		logger.debug("Se ha generado un nuevo token");

		// Almacenamos el token nuevo en la base de datos
		tokenRepository.save(tokenNuevo);
		logger.debug("Se ha almacenado el token");

		// Revocamos el token antiguo y lo almacenamos en BBDD
		tokenOriginal.setRevocado(LocalDateTime.now());
		tokenRepository.save(tokenOriginal);
		logger.debug("Se ha revocado el token");

		// Generamos un nuevo token publico a partir del token nuevo
		DTOTokenPublico tokenPublicoNuevo = new TokenPublicoBuilder().construirTokenPublico(tokenNuevo);
		logger.info("Se ha generado un nuevo token publico");
		return tokenPublicoNuevo;
	}

	/**
	 * Comprueba si el token introducido esta caducado, en caso de estar caducado
	 * retorna un TokenCaducadoException
	 * 
	 * @param token es el {@link Token} que se va a comprobar si esta caducado
	 */
	private void validarTokenNoCaducado(Token token) {
		logger.debug("Se va a comprobar la caducidad del token");

		if (token.getRevocado() != null) {
			logger.info("El token id {} esta revocado a fecha de {}", token.getIdToken(), token.getRevocado());
			throw new TokenRevocadoException();
		}

		// Comprobamos si el token esta caducado
		if (token.getCaducidadToken().isBefore(LocalDateTime.now())) {

			// Segun el tipo de token caducado se va a notificar al usuario
			if (token.getTipoToken().equals(TipoToken.CORTO)) {
				logger.info("El token corto id {} esta caducado", token.getIdToken());
				throw new TokenCortoCaducadoException();
			} else if (token.getTipoToken().equals(TipoToken.LARGO)) {
				logger.info("El token largo id {} esta caducado", token.getIdToken());
				throw new TokenLargoCaducadoException();
			}
		}
		logger.info("El token esta vigente");
	}

	/**
	 * Comprueba que el token largo y el token corto pertenecen al mismo usuario
	 * 
	 * @param tokenCorto es el {@link Token} corto
	 * @param tokenLargo es el {@link Token} largo
	 */
	private void comprobarTokenPertenecenMismoUsuario(Token tokenCorto, Token tokenLargo) {
		// Comprobamos si el token largo y corto pertenecen al mismo usuario
		if (tokenCorto.getUsuario().equals(tokenLargo.getUsuario())) {
			logger.info("El token corto id {} y el token largo id {} no pertenecen al mismo usuario",
					tokenCorto.getIdToken(), tokenLargo.getIdToken());
			throw new TokenDiferenteUsuarioException();
		}
		logger.info("El token corto id {} y el token largo id {} pertenecen al mismo usuario", tokenCorto.getIdToken(),
				tokenLargo.getIdToken());
	}
}
