package es.alejandroperellon.LoginService.tokenInicioSesion.exception;

/**
 * Indica que el token corto esta vigente
 * 
 * @author Alejandro Perellón López
 * @version 1.0
 */
public class TokenCortoVigenteException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TokenCortoVigenteException() {
		super("TOKEN_CORTO_VIGENTE");
	}
}
