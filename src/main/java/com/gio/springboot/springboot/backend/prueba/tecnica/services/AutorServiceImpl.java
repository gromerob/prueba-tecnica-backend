package com.gio.springboot.springboot.backend.prueba.tecnica.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gio.springboot.springboot.backend.prueba.tecnica.entity.Autor;
import com.gio.springboot.springboot.backend.prueba.tecnica.repository.IAutorRepository;

@Service
public class AutorServiceImpl implements IAutorService {
	
	@Autowired
	IAutorRepository autorRepository;

	@Override
	public List<Autor> findAll() {
		// TODO Auto-generated method stub
		return (List<Autor>)autorRepository.findAll() ;
	}

	@Override
	public Autor save(Autor autor) {
		// TODO Auto-generated method stub
		return autorRepository.save(autor);
	}

	@Override
	public Autor findById(Long id) {
		// TODO Auto-generated method stub
		return autorRepository.findById(id).orElse(null);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		autorRepository.deleteById(id);
	}

	@Override
	public List<Autor> findByNombre(String term) {
		// TODO Auto-generated method stub
		return autorRepository.findByNombre(term);
	}

}
