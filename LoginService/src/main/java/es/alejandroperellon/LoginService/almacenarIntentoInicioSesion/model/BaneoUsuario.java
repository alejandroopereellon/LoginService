package es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.model;

import java.time.LocalDateTime;

import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.Auxiliar.MotivosBaneo;
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
import jakarta.persistence.Table;

@Entity
@Table(name = "baneo_usuario")
/**
 * Entidad que registra los baneos provocados a los clientes y permite comprobar
 * si un usuario esta baneado o no
 * 
 * @author Alejandro Perellón López
 * @version 1.0
 */
public class BaneoUsuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_baneo")
	private long id;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario", nullable = true)
	private Usuario usuario;

	@Column(name = "motivo", nullable = false, length = 30)
	@Enumerated(EnumType.STRING)
	private MotivosBaneo motivo;

	@Column(name = "momento_baneo", nullable = false)
	private LocalDateTime momentoBaneo;

	@Column(name = "momento_fin_baneo", nullable = false)
	private LocalDateTime momentoFinBaneo;

	// Constructor
	public BaneoUsuario() {

	}

	// Getters && setters
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public MotivosBaneo getMotivo() {
		return motivo;
	}

	public void setMotivo(MotivosBaneo motivo) {
		this.motivo = motivo;
	}

	public LocalDateTime getMomentoBaneo() {
		return momentoBaneo;
	}

	public void setMomentoBaneo(LocalDateTime momentoBaneo) {
		this.momentoBaneo = momentoBaneo;
	}

	public LocalDateTime getMonentoFinBaneo() {
		return momentoFinBaneo;
	}

	public void setMonentoFinBaneo(LocalDateTime monentoFinBaneo) {
		this.momentoFinBaneo = monentoFinBaneo;
	}

}
