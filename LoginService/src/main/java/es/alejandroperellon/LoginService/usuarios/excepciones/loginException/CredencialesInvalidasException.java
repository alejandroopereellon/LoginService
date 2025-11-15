package es.alejandroperellon.LoginService.usuarios.excepciones.loginException;

/**
 * Aparece cuando las credenciales del usuario son invalida (usuario o correo y
 * contraseña erronea)
 * 
 * @author Alejandro Perellón López
 * @version 1.0
 */
public class CredencialesInvalidasException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CredencialesInvalidasException(String mensaje) {
		super(mensaje);
	}
}
