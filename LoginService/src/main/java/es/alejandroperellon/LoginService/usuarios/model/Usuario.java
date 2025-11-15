package es.alejandroperellon.LoginService.usuarios.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(unique = true, nullable = false, length = 254)
	@NotBlank(message = "El correo no puede estar en blanco")
	@Email(message = "El correo debe tener un formato válido")
	@Size(max = 254, message = "El correo no puede exceder los 254 caracteres")

	private String correo;

	@Column(name = "contrasena_hash", nullable = false, length = 100)
	@NotBlank(message = "La contraseña no puede estar en blanco")
	@Size(min = 60, max = 100, message = "El hash debe tener entre 60 y 100 caracteres")
	private String contrasenaHash;

	@Column(name = "nombre_usuario", unique = true, nullable = false, length = 20)
	@NotBlank(message = "El nombre de usuario no puede estar en blanco")
	@Size(min = 4, max = 20, message = "El nombre de usuario debe tener entre 4 y 20 caracteres")
	private String nombreUsuario;

	@Column(name = "nombre", nullable = false)
	@NotBlank(message = "El nombre no puede estar en blanco")
	@Size(min = 1, max = 50, message = "El nombre debe tener entre 1 y 50 caracteres")
	private String nombre;

	@Column(name = "apellido", nullable = true)
	@Size(min = 1, max = 50, message = "Los apellidos debe tener entre 1 y 50 caracteres")
	private String apellido;

	@Column(name = "apodo", unique = false, nullable = true, length = 30)
	@Size(min = 1, max = 50, message = "El apodo debe tener entre 1 y 50 caracteres")
	private String apodo;

	@Column(name = "fecha_nacimiento", nullable = false)
	@NotNull(message = "La fecha de nacimiento no puede estar en blanco")
	@Past(message = "La fecha de nacimiento debe ser una fecha pasada")
	private LocalDate fechaNacimiento;

	@Column(name = "fecha_creacion_cuenta", nullable = false)
	@PastOrPresent(message = "La fecha de creación de la cuenta no puede ser una fecha futura")
	@NotNull(message = "La fecha de creación de la cuenta no puede ser nula")
	private LocalDateTime fechaCreacionCuenta;

	@Column(name = "fechacambio_contrasena")
	@PastOrPresent(message = "La fecha de cambio de contraseña no puede ser una fecha futura")
	private LocalDateTime fechaCambioContrasena;

	@Column(name = "ultima_conexion")
	private LocalDateTime ultimaConexion;

	@Column(name = "estado_cuenta", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	@NotNull(message = "El estado de la cuenta no puede ser nulo")
	private EstadoCuenta estadoCuenta;

	@Column(name = "informacion_adicional", length = 1000)
	@Size(max = 1000, message = "La información adicional no puede exceder los 1000 caracteres")
	private String informacionAdicional;

	// Constructors
	public Usuario() {
	}

	// Prepersist
	@PrePersist
	protected void onCreate() {
		// Establecemos la creacion de la cuenta en AHORA
		if (fechaCreacionCuenta == null) {
			fechaCreacionCuenta = LocalDateTime.now();
		}
		// Establecemos el estado de la cuenta en Activa
		if (estadoCuenta == null) {
			estadoCuenta = EstadoCuenta.ACTIVA;
		}
		// Establecemos la ultima conexion en 0
		if (ultimaConexion == null) {
			ultimaConexion = null;
		}

	}

	// Getters and Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getContrasenaHash() {
		return contrasenaHash;
	}

	public void setContrasenaHash(String contrasenaHash) {
		this.contrasenaHash = contrasenaHash;
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

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public LocalDateTime getFechaCreacionCuenta() {
		return fechaCreacionCuenta;
	}

	public void setFechaCreacionCuenta(LocalDateTime fechaCreacionCuenta) {
		this.fechaCreacionCuenta = fechaCreacionCuenta;
	}

	public LocalDateTime getFechaCambioContrasena() {
		return fechaCambioContrasena;
	}

	public void setFechaCambioContrasena(LocalDateTime fechaCambioContrasena) {
		this.fechaCambioContrasena = fechaCambioContrasena;
	}

	public LocalDateTime getUltimaConexion() {
		return ultimaConexion;
	}

	public void setUltimaConexion(LocalDateTime ultimaConexion) {
		this.ultimaConexion = ultimaConexion;
	}

	public EstadoCuenta getEstadoCuenta() {
		return estadoCuenta;
	}

	public void setEstadoCuenta(EstadoCuenta estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}

	public String getInformacionAdicional() {
		return informacionAdicional;
	}

	public void setInformacionAdicional(String informacionAdicional) {
		this.informacionAdicional = informacionAdicional;
	}

}
