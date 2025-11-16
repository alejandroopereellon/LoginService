package es.alejandroperellon.LoginService.tokenInicioSesion.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import es.alejandroperellon.LoginService.clasesAxiliares.ApiResponse;

@Order(1)
@RestControllerAdvice(basePackages = "es.alejandroperellon.LoginService.tokenInicioSesion")
public class TokenExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(TokenExceptionHandler.class);

	@ExceptionHandler(TokenCortoCaducadoException.class)
	public ResponseEntity<ApiResponse> handleTokenCortoCaducado(TokenCortoCaducadoException ex) {
		log.warn("Token corto caducado");
		ApiResponse res = new ApiResponse(false, ex.getMessage(), "TOKEN_SHORT_EXPIRED");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
	}

	@ExceptionHandler(TokenCortoVigenteException.class)
	public ResponseEntity<ApiResponse> handleTokenRevocado(TokenCortoVigenteException ex) {
		log.warn("Token corto vigente");
		ApiResponse res = new ApiResponse(false, ex.getMessage(), "TOKEN_SHORT_CURRENT");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
	}

	@ExceptionHandler(TokenDiferenteUsuarioException.class)
	public ResponseEntity<ApiResponse> handleTokenRevocado(TokenDiferenteUsuarioException ex) {
		log.warn("Token de diferentes usuarios");
		ApiResponse res = new ApiResponse(false, ex.getMessage(), "TOKENS_DIFERENT_USER");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
	}

	@ExceptionHandler(TokenLargoCaducadoException.class)
	public ResponseEntity<ApiResponse> handleTokenLargoCaducado(TokenLargoCaducadoException ex) {
		log.warn("Token largo caducado");
		ApiResponse res = new ApiResponse(false, ex.getMessage(), "TOKEN_LONG_EXPIRED");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
	}

	@ExceptionHandler(TokenNoEncontradoException.class)
	public ResponseEntity<ApiResponse> handleTokenNoEncontrado(TokenNoEncontradoException ex) {
		log.warn("Token no encontrado o inválido");
		ApiResponse res = new ApiResponse(false, ex.getMessage(), "TOKEN_NOT_FOUND");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
	}

	@ExceptionHandler(TokenRevocadoException.class)
	public ResponseEntity<ApiResponse> handleTokenRevocado(TokenRevocadoException ex) {
		log.warn("Token revocado manualmente");
		ApiResponse res = new ApiResponse(false, ex.getMessage(), "TOKEN_REVOKED");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
	}

	@ExceptionHandler(TokenTipoIncorrectoException.class)
	public ResponseEntity<ApiResponse> handleTokenRevocado(TokenTipoIncorrectoException ex) {
		log.warn("Token de tipo incorrecto");
		ApiResponse res = new ApiResponse(false, ex.getMessage(), "TOKEN_INCORRECT_TYPE");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
	}

}
