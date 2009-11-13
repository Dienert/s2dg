package org.ufpb.s2dg.entity;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "professor")
public class Professor implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "matricula", unique = true, nullable = false, length = 15)
	@NotNull
	@Length(max = 15)
	private String matricula;
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "professor_turma", schema = "public", joinColumns = { @JoinColumn(name = "matricula_docente", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "turma_id", nullable = false, updatable = false) })
	private Set<Turma> turmas = new HashSet<Turma>(0);
	@OneToOne(mappedBy="professor")
	private Usuario usuario;

	public Professor() {
	}

	public Professor(String matricula) {
		this.matricula = matricula;
	}

	public Professor(String matricula, Set<Usuario> usuarios, Set<Turma> turmas) {
		this.matricula = matricula;
		this.turmas = turmas;
	}

	
	public String getMatricula() {
		return this.matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	
	public Set<Turma> getTurmas() {
		return this.turmas;
	}

	public void setTurmas(Set<Turma> turmas) {
		this.turmas = turmas;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
