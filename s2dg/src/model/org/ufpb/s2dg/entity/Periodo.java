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
		try{
			Integer.parseInt(ano);
		} catch(NumberFormatException e){
			throw new IllegalArgumentException("Filha da puta, nao bota isso aqui, e so numero!");
		}
		this.ano = ano;
	}

	@Column(name = "semestre", nullable = false, length = 1)
	public char getSemestre() {
		return this.semestre;
	}

	public void setSemestre(char semestre) {
		try{
			Integer.parseInt(new Character(semestre).toString());
		} catch(NumberFormatException e){
			throw new IllegalArgumentException("Filha da puta, nao bota isso aqui, e so numero!");
		}
		this.semestre = semestre;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "periodo")
	public Set<Turma> getTurmas() {
		return this.turmas;
	}

	public void setTurmas(Set<Turma> turmas) {
		this.turmas = turmas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ano == null) ? 0 : ano.hashCode());
		result = prime * result + semestre;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Periodo other = (Periodo) obj;
		if (ano == null) {
			if (other.ano != null)
				return false;
		} else if (!ano.equals(other.ano))
			return false;
		if (semestre != other.semestre)
			return false;
		return true;
	}

	
}
