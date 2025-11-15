package es.alejandroperellon.LoginService.usuarios.dto;

import es.alejandroperellon.LoginService.tokenInicioSesion.dto.DTOTokenPublico;

/**
 * Clase que contiene todos los datos publicos de un usuario, para ello a traves
 * del {@link DTOTokenPublico} se va a solicitar los datos de un usuario y se va
 * a retornar los datos publicos del usuario
 * 
 * @author Alejandro Perellón López
 * @version 1.0
 */
public class DTOUsuarioPublico {

	private String correoElectronico;

	private String nombreUsuario;

	private String nombre;

	private String apellido;

	private String apodo;

	// Constructor
	public DTOUsuarioPublico() {
	}

	// Getters && setters

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getApodo() {
		return apodo;
	}

	public void setApodo(String apodo) {
		this.apodo = apodo;
	}

}
