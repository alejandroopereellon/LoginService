package es.alejandroperellon.LoginService.usuarios.builder;

import java.time.LocalDateTime;

import es.alejandroperellon.LoginService.usuarios.dto.DTOUsuarioRegistro;
import es.alejandroperellon.LoginService.usuarios.model.EstadoCuenta;
import es.alejandroperellon.LoginService.usuarios.model.Usuario;

/**
 * Construye un {@link Usuario} listo para persistir a partir del DTO de
 * registro y del hash de contraseña ya calculado.
 * 
 * No realiza validaciones ni persistencia.
 * 
 * @author Alejandro Perellón López
 * @version 1.0
 */
public class UsuarioNuevoBuilder {

	/**
	 * Builder de un nuevo usuario
	 * 
	 * @param usuarioRegistro es el {@link Usuario} que se va a registrar
	 * @param contrasenaHash  es la contraseña hasheada que se va a generar para el
	 *                        usuario
	 * @return {@link Usuario} listo para insertar en la base de datos
	 */
	public Usuario construirUsuario(DTOUsuarioRegistro usuarioRegistro, String contrasenaHash) {
		Usuario usuario = new Usuario();

		// Creamos los datos basicos del usuario a traves del usuarioRegistro
		usuario.setApellido(usuarioRegistro.getApellido());
		usuario.setNombre(usuarioRegistro.getNombre());
		usuario.setCorreo(usuarioRegistro.getCorreoElectronico());
		usuario.setNombreUsuario(usuarioRegistro.getNombreUsuario());
		usuario.setFechaNacimiento(usuarioRegistro.getFechaNacimiento());
		usuario.setContrasenaHash(contrasenaHash);

		// Creamos los datos adicionales
		usuario.setEstadoCuenta(EstadoCuenta.ACTIVA);
		usuario.setFechaCambioContrasena(LocalDateTime.now());
		usuario.setFechaCreacionCuenta(LocalDateTime.now());

		// Retornamos el usuario con los campos completados
		return usuario;

	}

}
