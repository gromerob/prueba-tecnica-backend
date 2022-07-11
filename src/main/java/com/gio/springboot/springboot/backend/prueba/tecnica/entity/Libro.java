package com.gio.springboot.springboot.backend.prueba.tecnica.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name="libros")
public class Libro implements Serializable {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	

	@Column(nullable = false, unique = false)
	private String titulo;
	
	
	@Column(nullable = false, unique = false)
	private int  precio;
	
	@JsonIgnoreProperties(value = {"libros","hibernateLazyInitializer","handler"}, allowSetters = true)
	@ManyToOne()
	@JoinColumn(name="autor_id", referencedColumnName="id")
	private Autor autor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public int getPrecio() {
		return precio;
	}

	public void setPrecio(int precio) {
		this.precio = precio;
	}

	public Autor getAutor() {
		return autor;
	}

	public void setAutor(Autor autor) {
		this.autor = autor;
	}

	public Libro(Long id,  String titulo,  int precio, Autor autor) {
		this.id = id;
		this.titulo = titulo;
		this.precio = precio;
		this.autor = autor;
	}
	
	public Libro () {}
	
	
	
	

}
