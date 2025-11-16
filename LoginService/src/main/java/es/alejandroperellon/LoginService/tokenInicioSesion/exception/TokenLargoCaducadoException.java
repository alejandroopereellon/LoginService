package es.alejandroperellon.LoginService.tokenInicioSesion.exception;

/**
 * Indica que el token largo esta caducado
 * 
 * @author Alejandro Perellón López
 * @version 1.0
 */
public class TokenLargoCaducadoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TokenLargoCaducadoException() {
		super("TOKEN_LARGO_CADUCADO");
	}
}
