package es.alejandroperellon.LoginService.tokenInicioSesion.model;

import java.time.LocalDateTime;
import java.util.UUID;

import es.alejandroperellon.LoginService.usuarios.model.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Tokens")

/**
 * Entidad que representa un token de inicio de sesión. Cada token identifica de
 * forma única una sesión de un usuario y tiene una fecha de caducidad.
 *
 * El token se genera en cada inicio de sesión correcto y se devuelve al
 * cliente.
 * 
 * @author Alejandro Perellón López
 * 
 * @version 1.0
 */

public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_token")
	private long idToken;

	@Column(name = "token_usuario")
	private UUID tokenUsuario;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario", nullable = false)
	private Usuario usuario;

	@Column(name = "caducidad_token", nullable = false)
	@FutureOrPresent(message = "La caducidad del token no puede ser anterior al presente")
	@NotNull(message = "La caducidad del token no puede ser nula")
	private LocalDateTime caducidadToken;

	// Constructor
	public Token() {
	}

	@PrePersist
	protected void onCreate() {
		/**
		 * Generamos un nuevo token
		 */
		if (tokenUsuario == null) {
			tokenUsuario = UUID.randomUUID();
		}
		/**
		 * Hacemos que la caducidad del token dure un dia completo
		 */
		if (caducidadToken == null) {
			caducidadToken = LocalDateTime.now().plusDays(1);
		}
	}

	// Getters && setters
	public long getIdToken() {
		return idToken;
	}

	public void setIdToken(long idToken) {
		this.idToken = idToken;
	}

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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
