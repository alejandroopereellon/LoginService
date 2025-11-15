package es.alejandroperellon.LoginService.usuarios.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.Auxiliar.ResultadoIntentoInicioSesion;
import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.model.IntentoInicioSesion;
import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.repository.IntentoInicioSesionRepository;
import es.alejandroperellon.LoginService.tokenInicioSesion.builder.TokenBuilder;
import es.alejandroperellon.LoginService.tokenInicioSesion.model.Token;
import es.alejandroperellon.LoginService.tokenInicioSesion.repository.TokenRepository;
import es.alejandroperellon.LoginService.usuarios.dto.BuilderDTOUsuarioPublico;
import es.alejandroperellon.LoginService.usuarios.dto.DTOUsuarioLogin;
import es.alejandroperellon.LoginService.usuarios.dto.DTOUsuarioPublico;
import es.alejandroperellon.LoginService.usuarios.excepciones.loginException.CredencialesInvalidasException;
import es.alejandroperellon.LoginService.usuarios.excepciones.loginException.CuentaEliminadaException;
import es.alejandroperellon.LoginService.usuarios.excepciones.loginException.CuentaSuspendidaException;
import es.alejandroperellon.LoginService.usuarios.model.EstadoCuenta;
import es.alejandroperellon.LoginService.usuarios.model.Usuario;
import es.alejandroperellon.LoginService.usuarios.repository.UsuariosRepository;

@Service
/**
 * Manejo del sistema de inicio de sesion
 * 
 * @author Alejandro Perellón López
 * @version 1.0
 */
public class AuthService {

	private final UsuariosRepository usuariosRepository;
	private final TokenRepository tokenRepository;
	private final IntentoInicioSesionRepository intentoRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthService(UsuariosRepository usuariosRepository, TokenRepository tokenRepository,
			IntentoInicioSesionRepository intento, PasswordEncoder passwordEncoder) {
		this.usuariosRepository = usuariosRepository;
		this.tokenRepository = tokenRepository;
		this.intentoRepository = intento;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * Procesa un intento de inicio de sesión:
	 * <ol>
	 * <li>Busca al usuario por correo o nombre de usuario.</li>
	 * <li>Comprueba la contraseña introducida.</li>
	 * <li>Verifica el estado de la cuenta (suspensiones, eliminaciones, etc.).</li>
	 * <li>Genera un nuevo token de sesión y lo persiste.</li>
	 * <li>Devuelve un {@link DTOUsuarioPublico} con los datos del usuario y su
	 * token.</li>
	 * </ol>
	 *
	 * En todos los casos registra un {@link IntentoInicioSesion} en la base de
	 * datos.
	 *
	 * @param datosInicioSesion datos introducidos por el cliente (usuario/correo y
	 *                          contraseña).
	 * @return DTO con los datos públicos del usuario autenticado.
	 * @throws RuntimeException si las credenciales son incorrectas o la cuenta
	 *                          tiene restricciones.
	 */

	public DTOUsuarioPublico login(DTOUsuarioLogin datosInicioSesion) {

		// Registramos los datos introducidos por el usuario
		String usuarioIntento = datosInicioSesion.getCorreoElectronico();
		String contrasenaIntento = datosInicioSesion.getContrasena();

		// Registramos el dato de usuario introducido por el cliente
		IntentoInicioSesion intento = new IntentoInicioSesion();
		intento.setCorreoIntroducido(usuarioIntento);

		try {
			// Obtenemos el usuario de la peticion del cliente
			Usuario usuario = obtenerUsuarioLogin(usuarioIntento, intento);

			// Comprobamos la contaseña del usuario
			comprobarContrasenaUsuario(contrasenaIntento, usuario, intento);

			// Comprobamos las restricciones del usuario
			comprobarRestriccionCuenta(usuario, intento);

			// Si ha pasado todos los controles ponemos intento correcto
			intento.setResultado(ResultadoIntentoInicioSesion.CORRECTO);

			// Generamos token del usuario
			Token token = new TokenBuilder().nuevoToken(usuario);
			// Almacenamos el token en la base de datos
			tokenRepository.save(token);

			// Retornamos el usuario con los datos del token
			return new BuilderDTOUsuarioPublico(usuario, token).construirUsuarioPublico();
		} catch (RuntimeException e) {
			// Retornamos el error al cliente
			throw e;
			// TODO Añadir los logs de error proximamente
		} finally {
			intentoRepository.save(intento);
		}
	}

	/**
	 * Metodo encargado de comprobar las restricciones de la cuenta, en caso de que
	 * tenga alguna restriccion se almacena el motivo de la restriccion en el
	 * intento de inicio y se
	 * 
	 * @param usuario, es el {@link Usuario} que se va a comprobar las restricciones
	 */
	private void comprobarRestriccionCuenta(Usuario usuario, IntentoInicioSesion intento) {
		if (usuario.getEstadoCuenta().equals(EstadoCuenta.SUSPENDIDA_TEMPORALMENTE)
				|| usuario.getEstadoCuenta().equals(EstadoCuenta.SUSPENDIDA_PERMANENTEMENTE)) {
			intento.setResultado(ResultadoIntentoInicioSesion.CUENTA_SUSPENDIDA);
			throw new CuentaSuspendidaException("Usuario suspendido, contacta con soporte para mas informacion");
		} else if (usuario.getEstadoCuenta().equals(EstadoCuenta.ELIMINADA)) {
			intento.setResultado(ResultadoIntentoInicioSesion.CUENTA_ELIMINADA);
			throw new CuentaEliminadaException("El usuario introducido ha sido eliminado recientemente");
		}
	}

	/**
	 * Metodo encargado de comprobar si la contraseña introducida por el usuario
	 * coindice con el usuario obtenido en la base de datos, en caso de coincidir se
	 * va a permitir el acceso a la cuenta
	 * 
	 * @param contrasenaIntento es el contrasena introducida por el usuario
	 * @param usuario           es el objeto {@link Usuario} que contiene la
	 *                          informacion del usuario, se extrae el hash de la
	 *                          contraseña para comprobar si coinciden
	 */
	private void comprobarContrasenaUsuario(String contrasenaIntento, Usuario usuario, IntentoInicioSesion intento) {

		boolean passwordMatches = passwordEncoder.matches(contrasenaIntento, usuario.getContrasenaHash());

		if (!passwordMatches) {
			intento.setResultado(ResultadoIntentoInicioSesion.CONTRASENA_INCORRECTA);
			throw new CredencialesInvalidasException("Usuario o contraseña incorrectos");
		}
	}

	/**
	 * Metodo encargado de obtener el usuario y registrar sus datos en el inicio de
	 * sesion
	 * 
	 * @param usuarioIntento es el nombre de usuario o correo que ha introducido el
	 *                       cliente
	 * @param intento        es el {@link IntentoInicioSesion} que se genera en cada
	 *                       peticion de inicio de sesion
	 */
	private Usuario obtenerUsuarioLogin(String usuarioIntento, IntentoInicioSesion intento) {
		// Buscamos el usuario en la base de datos
		Optional<Usuario> usuario = usuariosRepository.findByCorreoOrNombreUsuario(usuarioIntento, usuarioIntento);

		// Comprobamos si el usuario esta vacio
		if (usuario.isEmpty()) {
			intento.setResultado(ResultadoIntentoInicioSesion.USUARIO_INEXISTENTE);
			throw new CredencialesInvalidasException("Usuario o contraseña incorrectos");
		} else {
			// Si el usuario existe en el sistema se va a añadir al sistema
			intento.setUsuario(usuario.get());
		}

		return usuario.get();
	}
}
