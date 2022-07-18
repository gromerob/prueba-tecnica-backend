package com.gio.springboot.springboot.backend.prueba.tecnica.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="autores")
public class Autor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@Column(nullable = false, unique = false)
	@NotEmpty
	@Size(min = 5, message = "nombre debe tener minimo 6 caracteres")
	private String nombre;
	
	@NotEmpty
	@Size(min = 5, message = "apellido debe tener minimo 8 caracteres")
	@Column(nullable = false, unique = false)
	private String apellido;
	
	@JsonIgnoreProperties(value ={"autor","hibernateLazyInitializer","handler"}, allowSetters = true)
	@OneToMany(mappedBy = "autor")
	private Set<Libro> libros = new HashSet<Libro>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public Set<Libro> getLibros() {
		return libros;
	}

	public void setLibros(Set<Libro> libros) {
		this.libros = libros;
	}

	public Autor(Long id, @NotEmpty String nombre, @NotEmpty String apellido, Set<Libro> libros) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.libros = libros;
	}
	
	public Autor () {}


	
	
	
	
	

}
