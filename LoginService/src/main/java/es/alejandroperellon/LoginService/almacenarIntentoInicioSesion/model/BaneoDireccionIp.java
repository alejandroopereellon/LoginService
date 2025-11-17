package es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.model;

import java.time.LocalDateTime;

import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.Auxiliar.MotivosBaneo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "baneo_direccion_ip")
/**
 * Entidad que registra los baneos provocados a los clientes y permite comprobar
 * si un usuario esta baneado o no
 * 
 * @author Alejandro Perellón López
 * @version 1.0
 */
public class BaneoDireccionIp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_baneo")
	private long id;

	@Column(name = "direccion_ip", nullable = false)
	private String ip;

	@Column(name = "motivo", nullable = false, length = 30)
	@Enumerated(EnumType.STRING)
	private MotivosBaneo motivo;

	@Column(name = "momento_baneo", nullable = false)
	private LocalDateTime momentoBaneo;

	@Column(name = "momento_fin_baneo", nullable = false)
	private LocalDateTime momentoFinBaneo;

	// Constructor
	public BaneoDireccionIp() {

	}

	// Getters && setters
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
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
