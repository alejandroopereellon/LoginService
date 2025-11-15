package es.alejandroperellon.LoginService.usuarios.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

/**
 * DTO enviada por el cliente que contiene la informacion de alta de un nuevo
 * usuario
 * 
 * @author Alejandro Perellón López
 * @version 1.0
 */
public class DTOUsuarioRegistro {

	@NotBlank
	@Email
	private String correoElectronico;

	@NotBlank
	@Size(min = 8, max = 64)
	private String contrasena;

	@NotBlank
	@Size(min = 4, max = 20)
	private String nombreUsuario;

	@NotNull
	@Past
	private LocalDate fechaNacimiento;

	@NotBlank
	@Size(max = 50)
	private String nombre;

	@NotBlank
	@Size(max = 50)
	private String apellido;

	// Constructor

	public DTOUsuarioRegistro() {
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

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

}
