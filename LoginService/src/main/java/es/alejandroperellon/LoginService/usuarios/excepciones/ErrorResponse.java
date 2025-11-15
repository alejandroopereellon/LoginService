package es.alejandroperellon.LoginService.usuarios.excepciones;

import java.time.LocalDateTime;

public class ErrorResponse {

	private String codigo;
	private String mensaje;
	private String ruta;
	private LocalDateTime instante;

	public ErrorResponse() {
	}

	// getters && setters
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public LocalDateTime getInstante() {
		return instante;
	}

	public void setInstante(LocalDateTime instante) {
		this.instante = instante;
	}

}
