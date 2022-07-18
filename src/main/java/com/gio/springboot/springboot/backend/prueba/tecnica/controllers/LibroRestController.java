package com.gio.springboot.springboot.backend.prueba.tecnica.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gio.springboot.springboot.backend.prueba.tecnica.entity.Autor;
import com.gio.springboot.springboot.backend.prueba.tecnica.entity.Libro;
import com.gio.springboot.springboot.backend.prueba.tecnica.services.ILibroService;


@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/tecnica")
public class LibroRestController {
	
	
	@Autowired
	private ILibroService libroService;
	
	@GetMapping("/libros")
	public ResponseEntity<?> index(){
		Map<String, Object>  response = new HashMap<>();
		List<Libro> libros = null;
		
		try {
			
			 libros = libroService.findAll();
			
		}catch(DataAccessException e) {
			response.put("mensaje", "error al realizar la consulta en la base de datos");
			response.put("ok", false);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		
		}
		
		
		if(libros == null) {
			response.put("ok", false);
			response.put("mensaje", "No se encontraron libros en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		
		response.put("ok", true);
		response.put("libros",libros);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK); 
	
	}
	
	@GetMapping("/libros/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Libro libro = null;
		
		Map<String, Object>  response = new HashMap<>();
		try {
			libro = libroService.findById(id);
			
		}catch(RuntimeException e) {
			response.put("mensaje", "error al realizar la consulta en la base de datos");
			response.put("ok", false);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			
		}
		
		if(libro == null) {
			response.put("ok", false);
			response.put("mensaje", "El libro con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		response.put("libro", libro);
		response.put("ok", true);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@GetMapping("/libros/filtrar/{term}")
	public ResponseEntity<?> filtro(@PathVariable String term) {
		List<Libro> libros = null;
		
		
		
		Map<String, Object>  response = new HashMap<>();
		try {
			libros = libroService.findByNombre(term);
			
		}catch(DataAccessException e) {
			response.put("mensaje", "error al realizar la consulta en la base de datos");
			response.put("ok", false);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			
		}
		
	 

		
		if(libros == null) {
			response.put("ok", false);
			response.put("mensaje", "no hay libros para el filtro aplicado");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			
		}
		
		response.put("libros", libros);
		response.put("ok", true);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@PostMapping("/libros")
	public ResponseEntity<?> create(@Valid @RequestBody Libro libro, BindingResult result ) {
		
		Libro libroNew = null;
		Map<String, Object>  response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
									.stream()
									.map(err -> "El campo '"+ err.getField() + "' "+err.getDefaultMessage())
									.collect(Collectors.toList());
			response.put("errors",errors );
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST); 
		}
		
		try {
			libroNew = libroService.save(libro);
			 
		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("mensaje", "error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			response.put("especifico", e.getMostSpecificCause().getMessage() );
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND); 
		}
		
		response.put("mensaje", "el libro ha sido creado con éxito!");
		response.put("libro",libroNew);
		return new ResponseEntity<Map<String, Object>>(response,HttpStatus.CREATED);
	}
	
	@PutMapping("/libro/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Libro libro, BindingResult result, @PathVariable Long id) {
		Libro libroActual = libroService.findById(id);
		
		Libro libroActualizado = null;
		Map<String, Object>  response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
									.stream()
									.map(err -> "El campo '"+ err.getField() + "' "+err.getDefaultMessage())
									.collect(Collectors.toList());
			response.put("errors",errors );
			response.put("ok", false);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST); 
		}
		
		if(libroActual == null) {
			response.put("ok", false);
			response.put("mensaje", "Error: no se pudo editar el libro con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			libroActual.setPrecio(libro.getPrecio());
			libroActual.setAutor(libro.getAutor());
			libroActual.setPrecio(libro.getPrecio());

			
			libroActualizado = libroService.save(libroActual);
		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("mensaje", "error al actualizar en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND); 
		}
		response.put("mensaje", "El Libro ha sido actualizado con éxito!");
		response.put("libro",libroActualizado );
		response.put("ok", true);
		 return new ResponseEntity<Map<String, Object>>(response,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/libros/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object>  response = new HashMap<>();
		
		try {
			
			libroService.delete(id);
			
		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("mensaje", "error al eliminar el tratamiento de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND); 
		}
		response.put("mensaje", "libro eliminado con exito!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	
	

}
