package io.github.clovisgargione.msestante.application.representation;

import org.hibernate.validator.constraints.Range;

import io.github.clovisgargione.msestante.domain.Livro;
import jakarta.validation.constraints.NotNull;

public class LivroRequest {

    private Integer id;
    
    private Integer idEstante; 
    
    @NotNull(message = "O id do leitor precisa ser informado")
    private Integer idLeitor; 

    private String titulo;

    private String autor;

    @Range(message = "Nota deve ser entre 0 e 10", min = 0, max = 10)
    private Integer nota;
    
    private boolean favorito; 

    public LivroRequest(Integer id, Integer idEstante, Integer idLeitor, String titulo, String autor, Integer nota, boolean favorito) {
	super();
	this.id = id;
	this.idEstante = idEstante;
	this.idLeitor = idLeitor;
	this.titulo = titulo;
	this.autor = autor;
	this.nota = nota;
	this.favorito = favorito;
    }
    
    public LivroRequest(Integer idEstante, Integer idLeitor, String titulo, String autor, Integer nota, boolean favorito) {
	super();
	this.idEstante = idEstante;
	this.idLeitor = idLeitor;
	this.titulo = titulo;
	this.autor = autor;
	this.nota = nota;
	this.favorito = favorito;
    }

    public LivroRequest() {
	super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getIdEstante() {
        return idEstante;
    }

    public void setIdEstante(Integer idEstante) {
        this.idEstante = idEstante;
    }
    
    public Integer getIdLeitor() {
        return idLeitor;
    }

    public void setIdLeitor(Integer idLeitor) {
        this.idLeitor = idLeitor;
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

    public Livro toModelSave() {
	return new Livro(titulo, autor, nota, favorito);
    }
    
    public Livro toModelUpdate() {
	return new Livro(id, titulo, autor, nota, favorito);
    }
}
