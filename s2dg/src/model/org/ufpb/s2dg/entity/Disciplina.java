package org.ufpb.s2dg.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "disciplina")
public class Disciplina implements Serializable {
	
	public enum Tipo {OBRIGATORIA, OPTATIVA,COMPLEMENTAR};
	
	private static final long serialVersionUID = 1L;
	private String codigo;
	private String nome;
	private int creditos;
	private Set<Turma> turmas = new HashSet<Turma>(0);
	private Set<Curriculo> curriculos;
	private Tipo tipo;
	private Departamento departamento;

	public Disciplina() {
	}

	public Disciplina(String codigo, String nome, int creditos) {
		this.codigo = codigo;
		this.nome = nome;
		this.creditos = creditos;
	}

	public Disciplina(String codigo, String nome, int creditos,
			Set<Turma> turmas, Set<Curriculo> curriculos, Set<Disciplina> pre_requisitos, Set<Disciplina> co_requisitos,
			Tipo tipo) {
		this.codigo = codigo;
		this.nome = nome;
		this.creditos = creditos;
		this.turmas = turmas;
		this.curriculos = curriculos;
		this.tipo = tipo;
	}

	@Id
	@Column(name = "codigo", unique = true, nullable = false, length = 7)
	@NotNull
	@Length(max = 7)
	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Column(name = "nome", nullable = false, length = 100)
	@NotNull
	@Length(max = 100)
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "creditos", nullable = false)
	public int getCreditos() {
		return this.creditos;
	}
	
	/*public int getCargaHoraria() {
		return creditos*15;
	}*/

	public void setCreditos(int creditos) {
		this.creditos = creditos;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "disciplina")
	public Set<Turma> getTurmas() {
		return this.turmas;
	}

	public void setTurmas(Set<Turma> turmas) {
		this.turmas = turmas;
	}

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "curriculo_disciplina", schema = "public", joinColumns = { @JoinColumn(name = "codigo_disciplina", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "id_curriculo", nullable = false, updatable = false) })
	public Set<Curriculo> getCurriculos() {
		return curriculos;
	}

	public void setCurriculos(Set<Curriculo> curriculos) {
		this.curriculos = curriculos;
	}

	@Column(name = "tipo")
	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	@ManyToOne
	public Departamento getDepartamento() {
		return departamento;
	}

}
