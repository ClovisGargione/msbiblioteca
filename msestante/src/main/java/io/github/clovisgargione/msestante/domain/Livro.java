package io.github.clovisgargione.msestante.domain;

import org.hibernate.validator.constraints.Range;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "livro")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String titulo;

    private String autor;

    @Range(min = 0, max = 10)
    private Integer nota;
    
    private boolean favorito;

    public Livro() {
	super();
    }

    public Livro(Integer id, String titulo, String autor, @Range(min = 0, max = 10) Integer nota, boolean favorito) {
	super();
	this.id = id;
	this.titulo = titulo;
	this.autor = autor;
	this.nota = nota;
	this.favorito = favorito;
    }

    public Livro(String titulo, String autor, @Range(min = 0, max = 10) Integer nota, boolean favorito) {
	super();
	this.titulo = titulo;
	this.autor = autor;
	this.nota = nota;
	this.favorito = favorito;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }
}
