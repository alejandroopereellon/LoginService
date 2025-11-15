package es.alejandroperellon.LoginService.usuarios.builder;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.alejandroperellon.LoginService.usuarios.dto.DTOUsuarioRegistro;
import es.alejandroperellon.LoginService.usuarios.model.EstadoCuenta;
import es.alejandroperellon.LoginService.usuarios.model.Usuario;

/**
 * Construye un {@link Usuario} listo para persistir a partir del DTO de
 * registro y del hash de contraseña ya calculado.
 * 
 * @author Alejandro Perellón López
 * @version 1.0
 */
public class BuilderUsuarioNuevo {

	private static final Logger logger = LoggerFactory.getLogger(BuilderUsuarioNuevo.class);

	/**
	 * Builder de un nuevo usuario
	 * 
	 * @param usuarioRegistro es el {@link Usuario} que se va a registrar
	 * @param contrasenaHash  es la contraseña hasheada que se va a generar para el
	 *                        usuario
	 * @return {@link Usuario} listo para insertar en la base de datos
	 */
	public Usuario construirUsuario(DTOUsuarioRegistro usuarioRegistro, String contrasenaHash) {
		logger.debug("Se va a contruir el objeto usuario con los datos del registro");

		Usuario usuario = new Usuario();

		// Creamos los datos basicos del usuario a traves del usuarioRegistro
		usuario.setApellido(usuarioRegistro.getApellido());
		usuario.setNombre(usuarioRegistro.getNombre());
		usuario.setCorreo(usuarioRegistro.getCorreoElectronico());
		usuario.setNombreUsuario(usuarioRegistro.getNombreUsuario());
		usuario.setFechaNacimiento(usuarioRegistro.getFechaNacimiento());
		usuario.setContrasenaHash(contrasenaHash);
		logger.debug("Se han creado los datos basicos del usuario desde el objeto usuario registrado");

		// Creamos los datos adicionales
		usuario.setEstadoCuenta(EstadoCuenta.ACTIVA);
		LocalDateTime ahora = LocalDateTime.now();
		usuario.setFechaCambioContrasena(ahora);
		usuario.setFechaCreacionCuenta(ahora);
		logger.debug("Se han creado los datos adicionales del nuevo usuario");

		// Retornamos el usuario con los campos completados
		logger.info("Se creado el objeto usuario {} desde los datos de registro ", usuario);
		return usuario;

	}

}
