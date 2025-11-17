package es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.Auxiliar;

/**
 * Motivos por los que se puede banear a un usuario
 */
public enum MotivosBaneo {

	EXCESO_INTENTOS_FALLIDOS, BANEADO_MANUALMENTE, IP_DIFERENTE_MISMO_USUARIO;
}
