package es.alejandroperellon.LoginService.usuarios.excepciones;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import es.alejandroperellon.LoginService.usuarios.excepciones.loginException.CredencialesInvalidasException;
import es.alejandroperellon.LoginService.usuarios.excepciones.loginException.CuentaEliminadaException;
import es.alejandroperellon.LoginService.usuarios.excepciones.loginException.CuentaSuspendidaException;
import es.alejandroperellon.LoginService.usuarios.excepciones.registerException.CorreoExistenteException;
import es.alejandroperellon.LoginService.usuarios.excepciones.registerException.UsuarioExistenteException;

@RestControllerAdvice
/**
 * Manejador global de excepciones del servicio de usuarios.
 * 
 * Captura y traduce excepciones de negocio a respuestas HTTP con una estructura unificada
 * ({@link ApiResponse}) usando claves i18n y códigos técnicos para que el cliente pueda
 * presentar los mensajes en su idioma.
 * 
 * Controla tanto los errores de login como los de registro.
 * 
 * Códigos de estado:
 * <ul>
 *   <li>401 → Credenciales inválidas</li>
 *   <li>403 → Cuenta suspendida o eliminada</li>
 *   <li>409 → Conflictos de registro (correo o usuario existente)</li>
 * </ul>
 */

public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(CredencialesInvalidasException.class)
	public ResponseEntity<ApiResponse> handleCredencialesInvalidas(CredencialesInvalidasException ex) {
		logger.warn("Credenciales inválidas: {}", ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(ApiResponse.error("login.error.credenciales_invalidas", "CREDENCIALES_INVALIDAS"));
	}

	@ExceptionHandler(CuentaEliminadaException.class)
	public ResponseEntity<ApiResponse> handleCuentaEliminada(CuentaEliminadaException ex) {
		logger.warn("Cuenta eliminada: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(ApiResponse.error("login.error.cuenta_eliminada", "CUENTA_ELIMINADA"));
	}

	@ExceptionHandler(CuentaSuspendidaException.class)
	public ResponseEntity<ApiResponse> handleCuentaSuspendida(CuentaSuspendidaException ex) {
		logger.warn("Cuenta suspendida: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(ApiResponse.error("login.error.cuenta_suspendida", "CUENTA_SUSPENDIDA"));
	}

	@ExceptionHandler(UsuarioExistenteException.class)
	public ResponseEntity<ApiResponse> handleUsuarioExistente(UsuarioExistenteException ex) {
		logger.warn("Usuario existente: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(ApiResponse.error("registro.error.usuario_existente", "USUARIO_DUPLICADO"));
	}

	@ExceptionHandler(CorreoExistenteException.class)
	public ResponseEntity<ApiResponse> handleCorreoExistente(CorreoExistenteException ex) {
		logger.warn("Correo existente: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(ApiResponse.error("registro.error.correo_existente", "EMAIL_DUPLICADO"));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
		logger.error("Error no controlado: ", ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ApiResponse.error("error.desconocido", "ERROR_INTERNO"));
	}
}
