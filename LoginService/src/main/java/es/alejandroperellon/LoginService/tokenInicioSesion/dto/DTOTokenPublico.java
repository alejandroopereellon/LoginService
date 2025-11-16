package es.alejandroperellon.LoginService.tokenInicioSesion.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

/**
 * DTO que contiene el token de usuario que se devolverá al cliente tras un
 * inicio de sesión correcto. Este token se utilizará para identificar al
 * usuario en peticiones posteriores.
 * 
 * @author Alejandro Perellón López
 * @version 1.0
 */

public class DTOTokenPublico {

	@NotNull(message = "El token de usuario no puede ser nulo")
	private UUID tokenUsuario;

	private LocalDateTime caducidadToken;

	// Constructor

	public DTOTokenPublico() {
	}

	public DTOTokenPublico(UUID tokenUsuario) {
		super();
		this.tokenUsuario = tokenUsuario;
	}

	// getters && setters

	public UUID getTokenUsuario() {
		return tokenUsuario;
	}

	public void setTokenUsuario(UUID tokenUsuario) {
		this.tokenUsuario = tokenUsuario;
	}

	public LocalDateTime getCaducidadToken() {
		return caducidadToken;
	}

	public void setCaducidadToken(LocalDateTime caducidadToken) {
		this.caducidadToken = caducidadToken;
	}
}
