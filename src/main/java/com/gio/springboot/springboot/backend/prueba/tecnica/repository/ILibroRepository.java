package com.gio.springboot.springboot.backend.prueba.tecnica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gio.springboot.springboot.backend.prueba.tecnica.entity.Libro;


public interface ILibroRepository extends JpaRepository<Libro, Long>{
	
	@Query("select l from Libro l where (l.titulo like %?1%)")
	public List<Libro> findByNombre(String term);

}
