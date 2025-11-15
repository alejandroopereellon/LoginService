package es.alejandroperellon.LoginService.usuarios.excepciones;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import es.alejandroperellon.LoginService.usuarios.excepciones.loginException.CredencialesInvalidasException;
import es.alejandroperellon.LoginService.usuarios.excepciones.loginException.CuentaEliminadaException;
import es.alejandroperellon.LoginService.usuarios.excepciones.loginException.CuentaSuspendidaException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Envia los errores al cliente
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CredencialesInvalidasException.class)
	public ResponseEntity<ErrorResponse> handleCredencialesInvalidas(CredencialesInvalidasException ex,
			HttpServletRequest request) {

		ErrorResponse error = new ErrorResponse();
		error.setCodigo("CREDENCIALES_INVALIDAS");
		error.setMensaje(ex.getMessage());
		error.setRuta(request.getRequestURI());
		error.setInstante(LocalDateTime.now());

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}

	@ExceptionHandler(CuentaEliminadaException.class)
	public ResponseEntity<ErrorResponse> handleCuentaEliminada(CuentaEliminadaException ex,
			HttpServletRequest request) {

		ErrorResponse error = new ErrorResponse();
		error.setCodigo("CUENTA_ELIMINADA");
		error.setMensaje(ex.getMessage());
		error.setRuta(request.getRequestURI());
		error.setInstante(LocalDateTime.now());

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}

	@ExceptionHandler(CuentaSuspendidaException.class)
	public ResponseEntity<ErrorResponse> handleCuentaSuspendida(CuentaSuspendidaException ex,
			HttpServletRequest request) {

		ErrorResponse error = new ErrorResponse();
		error.setCodigo("CREDENCIALES_INVALIDAS");
		error.setMensaje(ex.getMessage());
		error.setRuta(request.getRequestURI());
		error.setInstante(LocalDateTime.now());

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}
}
