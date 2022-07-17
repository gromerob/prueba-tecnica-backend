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
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gio.springboot.springboot.backend.prueba.tecnica.entity.Autor;
import com.gio.springboot.springboot.backend.prueba.tecnica.entity.Libro;
import com.gio.springboot.springboot.backend.prueba.tecnica.services.IAutorService;

@WebMvcTest(AutorRestController.class)
public class AutorControllerTest {
	
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private IAutorService autorService;
	
	Autor autor1;
	Autor autor2;
	
	ObjectMapper objectMapper;
	
	@BeforeEach
	void setup() {
		 autor1= new Autor(Long.valueOf(1), "Gabriel", "Robles", new HashSet<Libro>());
		 autor2= new Autor(Long.valueOf(2), "Giovanni", "Romero", new HashSet<Libro>());
		 objectMapper = new ObjectMapper();
	}
	
	@Test
	void showTest() throws Exception {
		// given
		when(autorService.findById(Long.valueOf(1))).thenReturn(autor1);
		
		// when
		mvc.perform(get("/tecnica/autores/1").contentType(MediaType.APPLICATION_JSON))
		//then
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.ok").value(true))
		.andExpect(jsonPath("$.autor.nombre").value("Gabriel"));
	}
	
	@Test
	void ShowNoValidoTest() throws Exception {
		
		// given
		when(autorService.findById(Long.valueOf(3))).thenReturn(null);
		
		// when
		mvc.perform(get("/tecnica/autores/3").contentType(MediaType.APPLICATION_JSON))
		//then
		.andExpect(status().isNotFound())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.ok").value(false))
		.andExpect(jsonPath("$.mensaje").isNotEmpty());
		
	}
	
	@Test
	void ShowNoValidoTestConExcepcion() throws Exception {
		
		// given
		doThrow(new RuntimeException()).when(autorService).findById(Long.valueOf(4));
		
		// when
		mvc.perform(get("/tecnica/autores/4").contentType(MediaType.APPLICATION_JSON))
		//then
		.andExpect(status().isNotFound())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.ok").value(false))
		.andExpect(jsonPath("$.mensaje").value("error al realizar la consulta en la base de datos"));
				
	}
	
	@Test
	void indexTest() throws Exception {
		// given
		List<Autor> autores = new ArrayList<Autor>();
		autores.add(autor1);
		autores.add(autor2);
		when(autorService.findAll()).thenReturn(autores);
		
		// when
		mvc.perform(get("/tecnica/autores").contentType(MediaType.APPLICATION_JSON))
		//then
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.ok").value(true))
		.andExpect(jsonPath("$.autores").isNotEmpty());
		
		verify(autorService).findAll();
	}
	
	@Test
	void createAutorTest() throws  Exception {
		
		//Given
		Autor autor = new Autor();
		autor.setNombre("Gonzalo");
		autor.setApellido("Guerrero");
		when(autorService.save(any())).thenReturn(autor);
		
		
		//When
		mvc.perform(post("/tecnica/autores")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(autor)))
		
		//then
		.andExpect(status().isCreated())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.autor.nombre").value("Gonzalo"));
		
	}
	
	@Test
	void createAutorTestNoValido() throws  Exception {
		
		//Given
		Autor autor = new Autor();
		autor.setNombre("Gonzalo");
		
		
		//When
		mvc.perform(post("/tecnica/autores")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(autor)))
		
		//then
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON));
		
	}
	

	@Test
	void FiltroTest() throws Exception {
		// given
		List<Autor> autores = new ArrayList<Autor>();
		autores.add(autor1);
		autores.add(autor2);
		when(autorService.findByNombre(any())).thenReturn(autores);
		
		// when
		mvc.perform(get("/tecnica/autores/filtrar/gio").contentType(MediaType.APPLICATION_JSON))
		//then
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.ok").value(true))
		.andExpect(jsonPath("$.autores").isNotEmpty());
	}
	
	@Test
	void FiltroNoValidoTest() throws Exception {
		
		// given
	
		when(autorService.findByNombre(any())).thenReturn(null);
		
		// when
		mvc.perform(get("/tecnica/autores/filtrar/akjsj").contentType(MediaType.APPLICATION_JSON))
		//then
		.andExpect(status().isNotFound())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.ok").value(false))
		.andExpect(jsonPath("$.mensaje").value("no hay autores para el filtro aplicado"));
		
	}
	
	@Test
	void updateAutorTest() throws  Exception {
		
		//Given
		// datos a modificar del autor1
		Autor autor = new Autor();
		autor.setNombre("Gonzalo");
		autor.setApellido("Guerrero");
		
		when(autorService.findById(Long.valueOf(1))).thenReturn(autor1);
		
		autor1.setNombre(autor.getNombre());
		autor1.setApellido(autor.getApellido());
		
		when(autorService.save(any())).thenReturn(autor1);		
		
		//When
		mvc.perform(put("/tecnica/autores/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(autor)))
		
		//then
		.andExpect(status().isCreated())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.autor.nombre").value("Gonzalo"));
		
	}
	
	@Test
	void updateAutorTestNoValido() throws  Exception {
		
		//Given
		// datos a modificar del autor1
		Autor autor = new Autor();
		autor.setNombre("Gonzalo");
		
		when(autorService.findById(Long.valueOf(1))).thenReturn(autor);
		
		
		//When
		mvc.perform(put("/tecnica/autores/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(autor)))
		
		//then
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON));
		
	}
	
	@Test
	void updateAutorTestNoEncontrado() throws  Exception {
		
		//Given
		// datos a modificar del autor1
		Autor autor = new Autor();
		autor.setNombre("Gonzalo");
		autor.setApellido("Guerrero");
		
		when(autorService.findById(Long.valueOf(1))).thenReturn(null);
			
		
		//When
		mvc.perform(put("/tecnica/autores/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(autor)))
		
		//then
		.andExpect(status().isNotFound())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.ok").value(false));
		
	}
	
	@Test
	void deleteAutorTest() throws Exception {
		
		//Given
		doNothing().when(autorService).delete(any());
		
		//when
		mvc.perform(delete("/tecnica/autores/1")
				.contentType(MediaType.APPLICATION_JSON))
		//then
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.mensaje").value("autor eliminado con exito!"));
		
		
		
	}
	
	
	
	
	
	
	

}
