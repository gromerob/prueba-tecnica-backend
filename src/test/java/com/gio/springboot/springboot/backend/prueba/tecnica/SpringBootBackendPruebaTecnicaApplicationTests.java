package com.gio.springboot.springboot.backend.prueba.tecnica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.gio.springboot.springboot.backend.prueba.tecnica.entity.Autor;
import com.gio.springboot.springboot.backend.prueba.tecnica.entity.Libro;
import com.gio.springboot.springboot.backend.prueba.tecnica.repository.IAutorRepository;
import com.gio.springboot.springboot.backend.prueba.tecnica.repository.ILibroRepository;
import com.gio.springboot.springboot.backend.prueba.tecnica.services.AutorServiceImpl;
import com.gio.springboot.springboot.backend.prueba.tecnica.services.LibroServiceImpl;

@SpringBootTest
class SpringBootBackendPruebaTecnicaApplicationTests {
	
	// Probaremos nuestros servicios
	
	@Mock
	ILibroRepository libroRepository;
	
	@Mock
	IAutorRepository autorRepository;
	

	@InjectMocks
	AutorServiceImpl autorService;
	
	@InjectMocks
	LibroServiceImpl libroService ;
	
	
	
	Autor autor1;
	Autor autor2;
	
	Libro libro1;
	Libro libro2;
	Libro libro3;
	
	@BeforeEach
	void setup() {		
		
		 autor1= new Autor(Long.valueOf(1), "Gabriel", "Robles", new HashSet<Libro>());
		 autor2= new Autor(Long.valueOf(2), "Giovanni", "Romero", new HashSet<Libro>());
		 
		 libro1 = new Libro (Long.valueOf(1), "Las aventuras de gabriel",20000, autor1 );
		 libro2 = new Libro (Long.valueOf(1), "La casa de los horrores",30000, autor2 );
		 libro3 = new Libro (Long.valueOf(1), "El amanecer",30000, autor2 );
		 
	}
	
	// desde aca comienza el test de autorService.
	
	@Test
	void findAutorByIdTest() {
		when(autorRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(autor1));
		
		Autor buscado = autorService.findById(Long.valueOf(1));
		
		assertEquals("Gabriel", buscado.getNombre());
		assertEquals("Robles", buscado.getApellido());
		assertEquals(Long.valueOf(1), buscado.getId());
		assertEquals(0, buscado.getLibros().size());
		
		verify(autorRepository).findById(Long.valueOf(1));
		
	}
	
	@Test
	void saveAutorTest() {
		
		//given
		
		Autor pablo = new Autor(null, "Pablo", "Alvarez",  new HashSet<Libro>());
		when(autorRepository.save(any())).then(invocation -> {
			Autor a = invocation.getArgument(0);
			// se setea el id del autor nuevo que venga en este caso pablo
			a.setId(Long.valueOf(3));
			return a;		
		});
		
		//when
		Autor autorPrueba = autorService.save(pablo);
		//then
		assertEquals(Long.valueOf(3), autorPrueba.getId());
		assertEquals("Pablo", autorPrueba.getNombre());				
		
		verify(autorRepository).save(any());
	}
	
	@Test
	void findAllAutorTest() {
		// Given
		List<Autor> autores = new ArrayList<Autor>();
		autores.add(autor1);
		autores.add(autor2);
		when(autorRepository.findAll()).thenReturn(autores);
		
		//when
		List<Autor> autoresPrueba = autorService.findAll();
		
		//then
		assertFalse(autoresPrueba.isEmpty());
		assertEquals(2,autoresPrueba.size());
		verify(autorRepository).findAll();
		
	}
	
	@Test
	void findAByNombreAutorTest() {
		
		List<Autor> autores = new ArrayList<Autor>();
		autores.add(autor1);
		when(autorRepository.findByNombre(any())).thenReturn(autores);
		
		List<Autor> autoresBuscados = autorService.findByNombre("gio");
		
		assertEquals(1, autoresBuscados.size());		
		
		verify(autorRepository).findByNombre(any());
	}
	
	@Test
	void DeleleAutorTest() {
		
		doNothing().when(autorRepository).deleteById(any());
		
		autorService.delete(Long.valueOf(1));
		
		verify(autorRepository).deleteById(any());
	}
	
	// pruebas LibroService.
	
	@Test
	void findLibroByIdTest() {
		when(libroRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(libro1));
		
		Libro buscado = libroService.findById(Long.valueOf(1));
		
		assertEquals("Las aventuras de gabriel", buscado.getTitulo());
		assertEquals(20000, buscado.getPrecio());
		assertEquals(Long.valueOf(1), buscado.getId());
		
		verify(libroRepository).findById(Long.valueOf(1));
		
	}
	
	
	
	
	@Test
	void saveLibroTest() {
		
		//given
		
		Libro libro = new Libro(null, "Libro de giovanni",2000, new Autor() );
		when(libroRepository.save(any())).then(invocation -> {
			Libro l = invocation.getArgument(0);
			// se setea el id del libro nuevo que venga en este caso libro de giovanni
			l.setId(Long.valueOf(4));
			l.setAutor(autor2);
			return l;		
		});
		
		//when
		Libro libroPrueba = libroService.save(libro);
		//then
		assertEquals(Long.valueOf(4), libroPrueba.getId());
		assertEquals("Giovanni", libroPrueba.getAutor().getNombre());				
		
		verify(libroRepository).save(any());
	}
	
	@Test
	void findAllLibrosTest() {
		// Given
		List<Libro> libros = new ArrayList<Libro>();
		libros.add(libro1);
		libros.add(libro2);
		when(libroRepository.findAll()).thenReturn(libros);
		
		//when
		List<Libro> librosPrueba = libroService.findAll();
		
		//then
		assertFalse(librosPrueba.isEmpty());
		assertEquals(2,librosPrueba.size());
		verify(libroRepository).findAll();
		
	}
	
	@Test
	void findAByNombreLibroTest() {
		
		List<Libro> libros = new ArrayList<Libro>();
		libros.add(libro1);
		when(libroRepository.findByNombre(any())).thenReturn(libros);
		
		List<Libro> librosBuscados = libroService.findByNombre("las");
		
		assertEquals(1, librosBuscados.size());		
		
		verify(libroRepository).findByNombre(any());
	}
	
	@Test
	void DeleleLibroTest() {
		
		doNothing().when(libroRepository).deleteById(any());
		
		libroService.delete(Long.valueOf(1));
		
		verify(libroRepository).deleteById(any());
	}
	
	
	
	
	

}
