package es.alejandroperellon.LoginService.usuarios.excepciones.registerException;

public class CorreoExistenteException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CorreoExistenteException() {
		super("EMAIL_DUPLICADO");
	}
}
