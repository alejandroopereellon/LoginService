package es.alejandroperellon.LoginService.usuarios.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.alejandroperellon.LoginService.usuarios.builder.UsuarioNuevoBuilder;
import es.alejandroperellon.LoginService.usuarios.dto.DTOUsuarioRegistro;
import es.alejandroperellon.LoginService.usuarios.excepciones.ApiResponse;
import es.alejandroperellon.LoginService.usuarios.excepciones.registerException.CorreoExistenteException;
import es.alejandroperellon.LoginService.usuarios.excepciones.registerException.UsuarioExistenteException;
import es.alejandroperellon.LoginService.usuarios.model.Usuario;
import es.alejandroperellon.LoginService.usuarios.repository.UsuariosRepository;

@Service
/**
 * Sistema encargado del registro de usuarios
 * 
 * @author Alejandro Perellón López
 * @version 1.0
 */
public class RegisterService {

	private final UsuariosRepository usuariosRepository;
	private final PasswordEncoder passwordEncoder;

	// Constructor
	public RegisterService(UsuariosRepository usuariosRepository, PasswordEncoder passwordEncoder) {
		this.usuariosRepository = usuariosRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * Registra un nuevo usuario:
	 * <ol>
	 *   <li>Valida duplicados (correo, nombreUsuario).</li>
	 *   <li>Hashea la contraseña con el PasswordEncoder configurado.</li>
	 *   <li>Construye la entidad Usuario y la persiste.</li>
	 *   <li>Devuelve una respuesta estandarizada con clave i18n.</li>
	 * </ol>
	 * @param usuarioRegistro datos de alta recibidos del cliente
	 * @return ApiResponse con clave i18n y código lógico
	 * @throws UsuarioExistenteException si el nombre de usuario ya existe
	 * @throws CorreoExistenteException si el correo ya existe
	 */

	@Transactional
	public ApiResponse register(DTOUsuarioRegistro usuarioRegistro) {

		// Comprobamos los campos basicos

		// Comprobamos el correo electronico
		comprobarCorreoElectronico(usuarioRegistro);

		// Comprobamos el nombre de usuario
		comprobarNombreUsuario(usuarioRegistro);

		// Creamos el hash de la contraseña
		String contrasenaHash = passwordEncoder.encode(usuarioRegistro.getContrasena());

		// Creamos el usuario con el builder y rellenamos los campos restantes
		Usuario usuarioNuevo = new UsuarioNuevoBuilder().construirUsuario(usuarioRegistro, contrasenaHash);

		// Almacenamos el usuario en la base de datos
		usuariosRepository.save(usuarioNuevo);

		// Retornamos el mensaje de confirmacion
		return ApiResponse.ok("Se ha registrado el usuario correctamente", "USUARIO_REGISTRADO");
	}

	/**
	 * Comprobacion de si el nombre de usuario no esta ya registrado en el sistema
	 * 
	 * @param usuarioRegistro es el {@link Usuario} que se va a registrar
	 */
	private void comprobarNombreUsuario(DTOUsuarioRegistro usuarioRegistro) {
		// Obtenemos si hay un usuario registrado con el mismo nombre de usuario
		if (usuariosRepository.existsByNombreUsuario(usuarioRegistro.getNombreUsuario())) {
			throw new UsuarioExistenteException();
		}

	}

	/**
	 * Comprobacion de si el correo electronico no esta ya registrado en el sistema
	 * 
	 * @param usuarioRegistro es el {@link Usuario} que se va a registrar
	 */
	private void comprobarCorreoElectronico(DTOUsuarioRegistro usuarioRegistro) {

		// Obtenemos si hay un usuario registrado con el mismo correo electronico
		if (usuariosRepository.existsByCorreo(usuarioRegistro.getCorreoElectronico())) {
			throw new CorreoExistenteException();
		}
	}

}
