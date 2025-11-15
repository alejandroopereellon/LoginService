package es.alejandroperellon.LoginService.usuarios.excepciones.registerException;

public class UsuarioExistenteException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UsuarioExistenteException() {
		super("CUENTA_USUARIO_NOMBRE_USUARIO_DUPLICADO");
	}
}
