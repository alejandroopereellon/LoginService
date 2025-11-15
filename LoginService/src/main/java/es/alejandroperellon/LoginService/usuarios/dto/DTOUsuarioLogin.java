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

	@NotBlank(message = "El correo no puede estar en blanco")
	@Email(message = "El correo debe tener un formato válido")
	private String correoElectronico;

	@NotBlank
	@Size(min = 8, max = 64)
	private String contrasena;

	// Constructores

	public DTOUsuarioLogin(String correoElectronico, String contrasena) {
		this.setCorreoElectronico(correoElectronico);
		this.setContrasena(contrasena);
	}

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
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

}
