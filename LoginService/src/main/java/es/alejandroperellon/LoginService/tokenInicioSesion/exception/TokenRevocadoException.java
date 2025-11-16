package es.alejandroperellon.LoginService.tokenInicioSesion.exception;

/**
 * Indica que el token corto esta revocado
 * 
 * @author Alejandro Perellón López
 * @version 1.0
 */
public class TokenRevocadoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TokenRevocadoException() {
		super("TOKEN_AVISO_REVOCADO");
	}
}
