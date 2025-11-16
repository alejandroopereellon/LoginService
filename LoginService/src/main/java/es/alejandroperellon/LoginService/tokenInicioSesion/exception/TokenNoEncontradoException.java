package es.alejandroperellon.LoginService.tokenInicioSesion.exception;

public class TokenNoEncontradoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TokenNoEncontradoException() {
		super("TOKEN_AVISO_NO_ENCONTRADO");
	}
}
