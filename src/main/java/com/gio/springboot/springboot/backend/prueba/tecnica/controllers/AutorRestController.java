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
import com.gio.springboot.springboot.backend.prueba.tecnica.services.IAutorService;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/tecnica")
public class AutorRestController {
	
	@Autowired
	private IAutorService autorService;
	
	@GetMapping("/autores")
	public ResponseEntity<?> index(){
		Map<String, Object>  response = new HashMap<>();
		List<Autor> autores = null;
		
		try {
			
			 autores = autorService.findAll();
			
		}catch(DataAccessException e) {
			response.put("mensaje", "error al realizar la consulta en la base de datos");
			response.put("ok", false);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		
		}
		
		response.put("ok", true);
		response.put("autores",autores);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK); 
	
	}
	
	@GetMapping("/autores/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Autor autor = null;
		
		Map<String, Object>  response = new HashMap<>();
		try {
			autor = autorService.findById(id);
			
		}catch(DataAccessException e) {
			response.put("mensaje", "error al realizar la consulta en la base de datos");
			response.put("ok", false);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			
		}
		
		if(autor == null) {
			response.put("ok", false);
			response.put("mensaje", "El libro con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		response.put("autor", autor);
		response.put("ok", true);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@PostMapping("/autores")
	public ResponseEntity<?> create(@Valid @RequestBody Autor autor, BindingResult result ) {
		
		Autor autorNew = null;
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
			autorNew = autorService.save(autor);
			 
		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("mensaje", "error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			response.put("especifico", e.getMostSpecificCause().getMessage() );
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND); 
		}
		
		response.put("mensaje", "el autor ha sido creado con éxito!");
		response.put("autor",autorNew);
		return new ResponseEntity<Map<String, Object>>(response,HttpStatus.CREATED);
	}
	
	@GetMapping("/autores/filtrar/{term}")
	public ResponseEntity<?> filtro(@PathVariable String term) {
		List<Autor> autores = null;
		
		
		
		Map<String, Object>  response = new HashMap<>();
		try {
			autores = autorService.findByNombre(term);
			
		}catch(DataAccessException e) {
			response.put("mensaje", "error al realizar la consulta en la base de datos");
			response.put("ok", false);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			
		}
		
	 

		
		if(autores == null) {
			response.put("ok", false);
			response.put("mensaje", "no hay autores para el filtro aplicado");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			
		}
		
		response.put("autores", autores);
		response.put("ok", true);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@PutMapping("/autores/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Autor autor, BindingResult result, @PathVariable Long id) {
		Autor autorActual = autorService.findById(id);
		
		Autor autorActualizado = null;
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
		
		if(autorActual == null) {
			response.put("ok", false);
			response.put("mensaje", "Error: no se pudo editar el autor con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			autorActual.setApellido(autor.getApellido());
			autorActual.setLibros(autor.getLibros());
			autorActual.setNombre(autor.getNombre());
		

			
			autorActualizado = autorService.save(autorActual);
		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("mensaje", "error al actualizar en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND); 
		}
		response.put("mensaje", "El autor ha sido actualizado con éxito!");
		response.put("autor",autorActualizado );
		response.put("ok", true);
		 return new ResponseEntity<Map<String, Object>>(response,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/autores/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object>  response = new HashMap<>();
		
		try {
			
			autorService.delete(id);
			
		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("mensaje", "error al eliminar el autor de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND); 
		}
		response.put("mensaje", "autor eliminado con exito!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	

}
