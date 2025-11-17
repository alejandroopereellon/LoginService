package es.alejandroperellon.LoginService.usuarios.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.Auxiliar.ResultadoIntentoInicioSesion;
import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.model.IntentoInicioSesion;
import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.repository.IntentoInicioSesionRepository;
import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.utils.ComprobarBaneo;
import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.utils.GestionBaneo;
import es.alejandroperellon.LoginService.tokenInicioSesion.builder.TokenBuilder;
import es.alejandroperellon.LoginService.tokenInicioSesion.model.TipoToken;
import es.alejandroperellon.LoginService.tokenInicioSesion.model.Token;
import es.alejandroperellon.LoginService.tokenInicioSesion.repository.TokenRepository;
import es.alejandroperellon.LoginService.usuarios.builder.BuilderDTOUsuarioPublico;
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
public class LoginService {

	private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

	private final UsuariosRepository usuariosRepository;
	private final TokenRepository tokenRepository;
	private final IntentoInicioSesionRepository intentoRepository;
	private final PasswordEncoder passwordEncoder;
	private final GestionBaneo gestionBaneo;
	private final ComprobarBaneo comprobarBaneo;

	public LoginService(UsuariosRepository usuariosRepository, TokenRepository tokenRepository,
			IntentoInicioSesionRepository intentoRepository, PasswordEncoder passwordEncoder, GestionBaneo gestionBaneo,
			ComprobarBaneo comprobarBaneo) {
		this.usuariosRepository = usuariosRepository;
		this.tokenRepository = tokenRepository;
		this.intentoRepository = intentoRepository;
		this.passwordEncoder = passwordEncoder;
		this.gestionBaneo = gestionBaneo;
		this.comprobarBaneo = comprobarBaneo;
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

	public DTOUsuarioPublico login(DTOUsuarioLogin datosInicioSesion, String direccionIp) {
		logger.debug("Se ha iniciado el metodo de login con el usuario {}", datosInicioSesion.getCorreoElectronico());

		// Registramos los datos introducidos por el usuario
		String usuarioIntento = datosInicioSesion.getCorreoElectronico();
		String contrasenaIntento = datosInicioSesion.getContrasena();
		logger.debug("Se han almacenado los datos del usuario");

		// Registramos el dato de usuario introducido por el cliente
		IntentoInicioSesion intento = new IntentoInicioSesion();
		intento.setCorreoIntroducido(usuarioIntento);

		Usuario usuario = null;

		try {
			// Primero de todo se va a comprobar que la direccion IP no este bloqueada
			comprobarBaneo.baneoActivoIp(direccionIp, intento);
			
			// Obtenemos el usuario de la peticion del cliente
			usuario = obtenerUsuarioLogin(usuarioIntento, intento);

			// Comprobamos la contaseña del usuario
			comprobarContrasenaUsuario(contrasenaIntento, usuario, intento);

			// Comprobamos las restricciones del usuario
			comprobarRestriccionCuenta(usuario, intento, direccionIp);

			// Si ha pasado todos los controles ponemos intento correcto
			intento.setResultado(ResultadoIntentoInicioSesion.CORRECTO);

			// Generamos token corto del usuario
			Token tokenCorto = new TokenBuilder().nuevoToken(usuario, TipoToken.CORTO);

			// Almacenamos el token en la base de datos
			tokenRepository.save(tokenCorto);

			// Generamos token corto del usuario
			Token tokenLargo = new TokenBuilder().nuevoToken(usuario, TipoToken.LARGO);

			// Almacenamos el token en la base de datos
			tokenRepository.save(tokenLargo);

			logger.info("Se ha almacenado el token del usuario {} en la BBDD", usuarioIntento);

			// Retornamos el usuario con los datos del token
			return new BuilderDTOUsuarioPublico(usuario, tokenCorto, tokenLargo).construirUsuarioPublico();
		} catch (RuntimeException e) {
			// Retornamos el error al cliente
			logger.warn("Ha ocurrido un error en el login del usuario");
			throw e;
		} finally {
			intentoRepository.save(intento);
			logger.debug("Se ha almacenado el intento de inicio de sesion: {} en la BBDD", intento);

			gestionBaneo.comprobarCandidaturaBaneo(usuario, direccionIp);
		}
	}

	/**
	 * Metodo encargado de comprobar las restricciones de la cuenta, en caso de que
	 * tenga alguna restriccion se almacena el motivo de la restriccion en el
	 * intento de inicio y se
	 * 
	 * @param usuario, es el {@link Usuario} que se va a comprobar las restricciones
	 */
	private void comprobarRestriccionCuenta(Usuario usuario, IntentoInicioSesion intento, String direccionIp) {
		logger.debug("Se va a comprobar las restricciones de la cuenta");

		// Comprobamos si el usuario tiene algun tipo de suspension en la cuenta
		if (usuario.getEstadoCuenta().equals(EstadoCuenta.SUSPENDIDA_TEMPORALMENTE)
				|| usuario.getEstadoCuenta().equals(EstadoCuenta.SUSPENDIDA_PERMANENTEMENTE)) {
			logger.info("La cuenta del usuario {} esta SUSPENDIDA", usuario);
			intento.setResultado(ResultadoIntentoInicioSesion.CUENTA_SUSPENDIDA);

			logger.debug("Se va a marcar el intento de inicio de sesion como CUENTA_SUSPENDIDA");
			throw new CuentaSuspendidaException("CUENTA_USUARIO_SUSPENDIDO");
		} else

		// Comprobamos si el usuario tiene la cuenta eliminada
		if (usuario.getEstadoCuenta().equals(EstadoCuenta.ELIMINADA)) {
			logger.info("La cuenta del usuario {} esta ELIMINADA", usuario);
			intento.setResultado(ResultadoIntentoInicioSesion.CUENTA_ELIMINADA);

			logger.debug("Se va a marcar el intento de inicio de sesion como CUENTA_ELIMINADA");
			throw new CuentaEliminadaException("CUENTA_USUARIO_ELIMINADO");
		}

		logger.info("El usuario {} no tiene ningun tipo de resrtriccion en la cuenta", usuario);
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
		logger.debug("Se va a comprobar la contraseña del usuario {}", usuario);

		boolean passwordMatches = passwordEncoder.matches(contrasenaIntento, usuario.getContrasenaHash());
		logger.debug("Se ha creado el hash de la contraseña y se va a comprar");

		if (!passwordMatches) {
			logger.info("La contraseña introducida para el usuario {} no coincide con la base de datos", usuario);
			intento.setResultado(ResultadoIntentoInicioSesion.CONTRASENA_INCORRECTA);
			logger.debug("Se ha marcado el intento de inicio de sesion del usuario {} con CONTRASEÑA_INCORRECTA",
					usuario);
			throw new CredencialesInvalidasException("CUENTA_USUARIO_CONTRASENA_ERRONEA");
		}

		logger.info("La contraseña introducida para el usuario {} coincide con la BBDD", usuario);
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
		logger.debug("Se van a obtener los datos del usuario");

		// Buscamos el usuario en la base de datos
		Optional<Usuario> usuario = usuariosRepository.findByCorreoOrNombreUsuario(usuarioIntento, usuarioIntento);

		// Comprobamos si el usuario esta vacio
		if (usuario.isEmpty()) {
			logger.info("El usuario {} no existe en la base de datos", usuarioIntento);
			intento.setResultado(ResultadoIntentoInicioSesion.USUARIO_INEXISTENTE);
			logger.debug("Se ha marcado el intento de inicio de sesion como USUARIO_INEXISTENTE");
			throw new CredencialesInvalidasException("CUENTA_USUARIO_NOMBREUSUARIO_CORREO_INCORRECTOS");
		} else {
			logger.info("El usuario {} existe en la base de datos", usuario.get());
			// Si el usuario existe en el sistema se va a añadir al intento de inicio de
			// sesion
			intento.setUsuario(usuario.get());
			logger.debug("Se ha añadido el usuario {} al sistema", usuario.get().getId());
			// Se va comprobar si el usuario esta baneado
			comprobarBaneo.baneoActivoUsuario(usuario.get(), intento);
		}

		return usuario.get();
	}
}
