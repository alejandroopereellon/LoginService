package es.alejandroperellon.LoginService.usuarios.excepciones.loginException;

/**
 * Aparece cuando un usuario tiene la cuenta suspendida
 * 
 * @author Alejandro Perellón López
 * @version 1.0
 */
public class CuentaSuspendidaException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CuentaSuspendidaException(String mensaje) {
		super(mensaje);
	}
}
