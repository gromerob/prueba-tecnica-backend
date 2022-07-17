package com.gio.springboot.springboot.backend.prueba.tecnica.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gio.springboot.springboot.backend.prueba.tecnica.entity.Autor;
import com.gio.springboot.springboot.backend.prueba.tecnica.entity.Libro;
import com.gio.springboot.springboot.backend.prueba.tecnica.services.ILibroService;

@WebMvcTest(LibroRestController.class)
public class LibroControllerTest {
	
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private ILibroService libroService;
	
	Autor autor1;
	Autor autor2;
	Libro libro1;
	Libro libro2;
	Libro libro3;
	
	ObjectMapper objectMapper;
	
	@BeforeEach
	void setup() {
		 autor1= new Autor(Long.valueOf(1), "Gabriel", "Robles", new HashSet<Libro>());
		 autor2= new Autor(Long.valueOf(2), "Giovanni", "Romero", new HashSet<Libro>());
		 libro1 = new Libro(Long.valueOf(1), "Las aventuras del pollo", 20000, autor1);
		 libro2 = new Libro(Long.valueOf(2), "la gata salvaje", 3000, autor2);
		 libro3 = new Libro(Long.valueOf(3), "La casa de hielo", 40000, autor1);
		 objectMapper = new ObjectMapper();
	}
	
	
	@Test
	void showTest() throws Exception {
		// given
		when(libroService.findById(Long.valueOf(1))).thenReturn(libro1);
		
		// when
		mvc.perform(get("/tecnica/libros/1").contentType(MediaType.APPLICATION_JSON))
		//then
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.ok").value(true))
		.andExpect(jsonPath("$.libro.titulo").value("Las aventuras del pollo"));
	}
	
	@Test
	void ShowNoValidoTest() throws Exception {
		
		// given
		when(libroService.findById(Long.valueOf(3))).thenReturn(null);
		
		// when
		mvc.perform(get("/tecnica/libros/3").contentType(MediaType.APPLICATION_JSON))
		//then
		.andExpect(status().isNotFound())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.ok").value(false))
		.andExpect(jsonPath("$.mensaje").isNotEmpty());
		
	}
	
	@Test
	void ShowNoValidoTestConExcepcion() throws Exception {
		
		// given
		doThrow(new RuntimeException()).when(libroService).findById(Long.valueOf(4));
		
		// when
		mvc.perform(get("/tecnica/libros/4").contentType(MediaType.APPLICATION_JSON))
		//then
		.andExpect(status().isNotFound())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.ok").value(false))
		.andExpect(jsonPath("$.mensaje").value("error al realizar la consulta en la base de datos"));
				
	}
	
	@Test
	void indexTest() throws Exception {
		// given
		List<Libro> libros = new ArrayList<Libro>();
		libros.add(libro1);
		libros.add(libro2);
		libros.add(libro3);
		when(libroService.findAll()).thenReturn(libros);
		
		// when
		mvc.perform(get("/tecnica/libros").contentType(MediaType.APPLICATION_JSON))
		//then
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.ok").value(true))
		.andExpect(jsonPath("$.libros").isNotEmpty());
		
		verify(libroService).findAll();
	}
	
	@Test
	void createLibroTest() throws  Exception {
		
		//Given
		Libro libro = new Libro();
		libro.setTitulo("La cosecha de verano");
		libro.setPrecio(20000);
		when(libroService.save(any())).thenReturn(libro);
		
		
		//When
		mvc.perform(post("/tecnica/libros")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(libro)))
		
		//then
		.andExpect(status().isCreated())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.libro.titulo").value("La cosecha de verano"));
		
	}
	
	@Test
	void createLibroTestNoValido() throws  Exception {
		
		//Given
		Libro libro = new Libro();
		libro.setPrecio(2000);
		
		
		//When
		mvc.perform(post("/tecnica/libros")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(libro)))
		
		//then
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON));
		
	}
	

	@Test
	void FiltroTestLibro() throws Exception {
		// given
		List<Libro> libros = new ArrayList<Libro>();
		libros.add(libro1);
		libros.add(libro2);
		when(libroService.findByNombre(any())).thenReturn(libros);
		
		// when
		mvc.perform(get("/tecnica/libros/filtrar/a").contentType(MediaType.APPLICATION_JSON))
		//then
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.ok").value(true))
		.andExpect(jsonPath("$.libros").isNotEmpty());
	}
	
	@Test
	void FiltroNoValidoLibrosTest() throws Exception {
		
		// given
	
		when(libroService.findByNombre(any())).thenReturn(null);
		
		// when
		mvc.perform(get("/tecnica/libros/filtrar/akjsj").contentType(MediaType.APPLICATION_JSON))
		//then
		.andExpect(status().isNotFound())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.ok").value(false))
		.andExpect(jsonPath("$.mensaje").value("no hay libros para el filtro aplicado"));
		
	}
	
	@Test
	void updateLibroTest() throws  Exception {
		
		//Given
		// datos a modificar del libro1
		Libro libro = new Libro();
		libro.setTitulo("al mas alla");
		libro.setPrecio(20000);
		libro.setAutor(autor1);
		when(libroService.findById(Long.valueOf(1))).thenReturn(libro1);
		
		libro1.setTitulo(libro.getTitulo());
		libro1.setPrecio(libro.getPrecio());
		libro1.setAutor(libro.getAutor());
		
		when(libroService.save(any())).thenReturn(libro1);		
		
		//When
		mvc.perform(put("/tecnica/libro/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(libro)))
		
		//then
		.andExpect(status().isCreated())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.libro.titulo").value("al mas alla"));
		
	}
	
	@Test
	void updateLibroTestNoValido() throws  Exception {
		
		//Given
		// datos a modificar del libro1
		Libro libro = new Libro();
		libro.setPrecio(2000);
		
		when(libroService.findById(Long.valueOf(1))).thenReturn(libro1);
		
		
		//When
		mvc.perform(put("/tecnica/libro/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(libro)))
		
		//then
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON));
		
	}
	
	@Test
	void updateLibroTestNoEncontrado() throws  Exception {
		
		//Given
		// datos a modificar del autor1
		Libro libro = new Libro();
		libro.setPrecio(20000);
		libro.setTitulo("titulo de prueba");
		when(libroService.findById(Long.valueOf(1))).thenReturn(null);
			
		
		//When
		mvc.perform(put("/tecnica/libro/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(libro)))
		
		//then
		.andExpect(status().isNotFound())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.ok").value(false));
		
	}
	
	@Test
	void deleteLibroTest() throws Exception {
		
		//Given
		doNothing().when(libroService).delete(any());
		
		//when
		mvc.perform(delete("/tecnica/libros/1")
				.contentType(MediaType.APPLICATION_JSON))
		//then
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.mensaje").value("libro eliminado con exito!"));
		
		
		
	}
	
	
	
	
	
}
