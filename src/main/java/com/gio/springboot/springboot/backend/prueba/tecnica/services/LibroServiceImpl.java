package com.gio.springboot.springboot.backend.prueba.tecnica.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gio.springboot.springboot.backend.prueba.tecnica.entity.Libro;
import com.gio.springboot.springboot.backend.prueba.tecnica.repository.ILibroRepository;

@Service
public class LibroServiceImpl implements ILibroService {

	
	@Autowired
	ILibroRepository libroRepository;
	
	@Override
	public List<Libro> findAll() {
		// TODO Auto-generated method stub
		return (List<Libro>) libroRepository.findAll();
	}

	@Override
	public Libro save(Libro libro) {
		// TODO Auto-generated method stub
		return libroRepository.save(libro);
	}

	@Override
	public Libro findById(Long id) {
		// TODO Auto-generated method stub
		return libroRepository.findById(id).orElse(null);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		libroRepository.deleteById(id);
		
	}

	@Override
	public List<Libro> findByNombre(String term) {
		// TODO Auto-generated method stub
		return null;
	}

}
