package org.ufpb.s2dg.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;

@Entity
@Table(name = "avaliacao")
public class Avaliacao implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private String nome;
	private int peso;
	private boolean publicado;
	private Turma turma;
	private Set<AlunoTurmaAvaliacao> alunoTurmaAvaliacoes;
	private EventoCalendarioTurma dataEvento;
	
	public Avaliacao(){
		this.peso = 1;
	}
	
	public Avaliacao(long id, String nome, int peso, Turma turma) {
		this.id = id;
		this.nome = nome;
		this.peso = peso;
		this.turma = turma;
		this.publicado = false;
	}
	
	public Avaliacao(long id, String nome, int peso, Turma turma, Set<AlunoTurmaAvaliacao> alunoTurmaAvaliacoes) {
		this.id = id;
		this.nome = nome;
		this.peso = peso;
		this.turma = turma;
		this.alunoTurmaAvaliacoes = alunoTurmaAvaliacoes;
		this.publicado = false;
	}


	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name = "nome", length = 50, nullable=false)
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Column(name = "peso", nullable=false)
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	
	@Column(name = "publicado", nullable = true)
	public boolean getPublicado() {
		return publicado;
	}
	
	public void setPublicado(boolean publicado) {
		this.publicado = publicado;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_turma", nullable = false, updatable = false)
	@NotNull
	public Turma getTurma() {
		return turma;
	}
	public void setTurma(Turma turma) {
		this.turma = turma;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "avaliacao")
	public Set<AlunoTurmaAvaliacao> getAlunoTurmaAvaliacoes() {
		return alunoTurmaAvaliacoes;
	}

	public void setAlunoTurmaAvaliacoes(Set<AlunoTurmaAvaliacao> alunoTurmaAvaliacoes) {
		this.alunoTurmaAvaliacoes = alunoTurmaAvaliacoes;
	}

	public String geraNomeTruncado(){
        String nomeTruncado = null;
        if(nome.length()>12){
            nomeTruncado = nome.substring(0, 11) + "...";
            return nomeTruncado;
        }
        return nome;
    }

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="id_dataevento", nullable=true, updatable=true, insertable=true)
	public EventoCalendarioTurma getDataEvento() {
		return dataEvento;
	}

	public void setDataEvento(EventoCalendarioTurma dataEvento) {
		this.dataEvento = dataEvento;
	}
	
}
