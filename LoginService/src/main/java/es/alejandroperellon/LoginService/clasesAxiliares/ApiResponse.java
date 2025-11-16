package es.alejandroperellon.LoginService.clasesAxiliares;

import java.time.LocalDateTime;

/**
 * Respuesta estándar para operaciones sin payload de dominio. Incluye una clave
 * i18n (mensajeClave), un código lógico (codigo) y un instante.
 * 
 * @author Alejandro Perellón López
 * @version 1.0
 */
public class ApiResponse {

	private boolean success;
	private String codigoMensaje;
	private String messageKey;
	private LocalDateTime timestamp = LocalDateTime.now();

	// Constructor
	public ApiResponse(boolean success, String messageKey, String code) {
		this.success = success;
		this.messageKey = messageKey;
		this.codigoMensaje = code;
	}

	// Metodos estaticos
	public static ApiResponse ok(String mensaje, String codigoMensaje) {
		return new ApiResponse(true, mensaje, codigoMensaje);
	}

	public static ApiResponse error(String mensaje, String codigoMensaje) {
		return new ApiResponse(false, mensaje, codigoMensaje);
	}

	// Getters && setters
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getCode() {
		return codigoMensaje;
	}

	public void setCode(String code) {
		this.codigoMensaje = code;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

}
