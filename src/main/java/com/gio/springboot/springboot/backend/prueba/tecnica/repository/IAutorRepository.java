package com.gio.springboot.springboot.backend.prueba.tecnica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gio.springboot.springboot.backend.prueba.tecnica.entity.Autor;


public interface IAutorRepository extends JpaRepository<Autor, Long> {
	
	@Query("select a from Autor a where ((a.nombre like %?1%) or (a.apellido like %?1%))")
	public List<Autor> findByNombre(String term);

}
