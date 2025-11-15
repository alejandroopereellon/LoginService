package es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.alejandroperellon.LoginService.almacenarIntentoInicioSesion.model.IntentoInicioSesion;

public interface IntentoInicioSesionRepository extends JpaRepository<IntentoInicioSesion, Long> {

}
