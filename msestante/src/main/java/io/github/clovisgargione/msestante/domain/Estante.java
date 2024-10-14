package io.github.clovisgargione.msestante.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity(name = "estante")
public class Estante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer idLeitor;

    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
    @JoinColumn(name = "estante_id")
    private List<Livro> livros = new ArrayList<>();

    public Estante(Integer id, Integer idLeitor, List<io.github.clovisgargione.msestante.domain.Livro> livros) {
	super();
	this.id = id;
	this.idLeitor = idLeitor;
	this.livros = livros;
    }

    public Estante() {
	super();
    }

    public Estante(Integer idLeitor) {
	this.idLeitor = idLeitor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdLeitor() {
        return idLeitor;
    }

    public void setIdLeitor(Integer idLeitor) {
        this.idLeitor = idLeitor;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }
    
    public boolean temLivros() {
	return livros.size() > 0;
}
}
