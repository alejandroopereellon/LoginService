package es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.model;

import java.time.LocalDateTime;

import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.Auxiliar.ResultadoIntentoInicioSesion;
import es.alejandroperellon.LoginService.usuarios.model.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "intento_inicio_sesion")

/**
 * Entidad que registra cada intento de inicio de sesión. Puede estar asociada a
 * un usuario existente o ser nula si el correo introducido no pertenece a
 * ningún usuario registrado.
 * 
 * @author Alejandro Perellón López
 * @version 1.0
 */
public class IntentoInicioSesion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_intento")
	private long idIntento;

	@Column(name = "momento_intento")
	private LocalDateTime momentoIntento;

	@Column(name = "correo_introducido", nullable = false, length = 254)
	private String correoIntroducido;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario", nullable = true)
	private Usuario usuario;

	@Column(name = "resultado", nullable = false, length = 30)
	@Enumerated(EnumType.STRING)
	private ResultadoIntentoInicioSesion resultado;

	// Constructores
	public IntentoInicioSesion() {
	}
	
	@PrePersist
	protected void onCreate() {
		if (momentoIntento == null) {
			momentoIntento = LocalDateTime.now();
		}
	}

	// Getters && setters
	public long getIdIntento() {
		return idIntento;
	}

	public void setIdIntento(long idIntento) {
		this.idIntento = idIntento;
	}

	public LocalDateTime getMomentoIntento() {
		return momentoIntento;
	}

	public void setMomentoIntento(LocalDateTime momentoIntento) {
		this.momentoIntento = momentoIntento;
	}

	public String getCorreoIntroducido() {
		return correoIntroducido;
	}

	public void setCorreoIntroducido(String correoIntroducido) {
		this.correoIntroducido = correoIntroducido;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public ResultadoIntentoInicioSesion getResultado() {
		return resultado;
	}

	public void setResultado(ResultadoIntentoInicioSesion resultado) {
		this.resultado = resultado;
	}

}
