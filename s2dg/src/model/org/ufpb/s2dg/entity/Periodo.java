package org.ufpb.s2dg.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "periodo")
public class Periodo implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private String ano;
	private char semestre;
	private Set<Turma> turmas = new HashSet<Turma>(0);

	public Periodo() {
	}

	public Periodo(long id, String ano, char semestre) {
		this.id = id;
		this.ano = ano;
		this.semestre = semestre;
	}

	public Periodo(long id, String ano, char semestre, Set<Turma> turmas) {
		this.id = id;
		this.ano = ano;
		this.semestre = semestre;
		this.turmas = turmas;
	}

	public Periodo(Periodo p) {
		this.id = p.getId();
		this.ano = p.getAno();
		this.semestre = p.getSemestre();
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "ano", nullable = false, length = 4)
	@NotNull
	@Length(max = 4)
	public String getAno() {
		return this.ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	@Column(name = "semestre", nullable = false, length = 1)
	public char getSemestre() {
		return this.semestre;
	}

	public void setSemestre(char semestre) {
		this.semestre = semestre;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "periodo")
	public Set<Turma> getTurmas() {
		return this.turmas;
	}

	public void setTurmas(Set<Turma> turmas) {
		this.turmas = turmas;
	}

}
