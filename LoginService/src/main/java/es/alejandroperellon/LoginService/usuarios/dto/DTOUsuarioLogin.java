package es.alejandroperellon.LoginService.usuarios.dto;

import es.alejandroperellon.LoginService.tokenInicioSesion.dto.DTOTokenPublico;
import jakarta.validation.constraints.*;

/**
 * DTO enviada por el cliente que contiene la informacion de inicio de sesion,
 * el objetivo es que una vez comprobado los datos de inicio de sesion se
 * retornaria un {@link DTOTokenPublico}
 * 
 * @author Alejandro Perellón López
 * @version 1.0
 */
public class DTOUsuarioLogin {

	@NotBlank(message = "El usuario o correo no puede estar en blanco")
	private String usuario;

	@NotBlank
	@Size(min = 8, max = 64)
	private String contrasena;

	// Constructores

	public DTOUsuarioLogin() {
	}

	// Getters && setters
	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public String getCorreoElectronico() {
		return usuario;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.usuario = correoElectronico;
	}

}
