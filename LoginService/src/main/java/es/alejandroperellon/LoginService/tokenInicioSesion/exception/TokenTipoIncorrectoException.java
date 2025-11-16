package es.alejandroperellon.LoginService.tokenInicioSesion.exception;

/**
 * Indica que el token corto esta caducado
 * 
 * @author Alejandro Perellón López
 * @version 1.0
 */
public class TokenTipoIncorrectoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TokenTipoIncorrectoException() {
		super("TOKEN_AVISO_TIPO_INCORRECTO");
	}
}
