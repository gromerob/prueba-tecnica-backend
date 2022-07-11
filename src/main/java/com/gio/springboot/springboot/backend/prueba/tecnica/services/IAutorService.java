package com.gio.springboot.springboot.backend.prueba.tecnica.services;

import java.util.List;

import com.gio.springboot.springboot.backend.prueba.tecnica.entity.Autor;


public interface IAutorService {
	
	public List<Autor> findAll();
	
	public Autor save(Autor tratamiento);
	
	public Autor findById(Long id);
	
	public void delete(Long id);
	
	public List<Autor> findByNombre(String term);
		

}
