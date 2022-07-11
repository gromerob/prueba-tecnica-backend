package com.gio.springboot.springboot.backend.prueba.tecnica.services;

import java.util.List;


import com.gio.springboot.springboot.backend.prueba.tecnica.entity.Libro;


public interface ILibroService {
	
	public List<Libro> findAll();
	
	public Libro save(Libro tratamiento);
	
	public Libro findById(Long id);
	
	public void delete(Long id);
	
	public List<Libro> findByNombre(String term);
		

}
