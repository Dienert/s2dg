package org.ufpb.s2dg.entity;

import java.io.Serializable;
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

@Entity
@Table(name = "curriculo")
public class Curriculo implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private Set<Aluno> alunos;
	private Curso curso;
	private Set<Disciplina> disciplinas;
	private int minimoCreditos;
	private int maximoCreditos;
	private String numero;
	private int minimoCreditosCurriculo;
	private int minimoDisciplinas;
	private int maximoTrancamentosParciais;
	private int minimoSemestres;
	private int maximoSemestres;
	private int maximoTrancamentosTotais;
	private int maximoMatriculaInstitucional;
	private int minimoCreditosObrigatorias;
	private int minimoCreditosOptativas;
	private int minimoCreditosComplementares;
	private int minimoDisciplinasObrigatorias;
	private int minimoDisciplinasOptativas;
	private int minimoDisciplinasComplementares;

	public Curriculo() {
	}

	public Curriculo(long id) {
		this.id = id;
	}
	
	public Curriculo(long id, Set<Aluno> alunos, Curso curso, Set<Disciplina> disciplinas,
			int minimoCreditos, int maximoCreditos, String numero, int minimoCreditosCurriculo,
			int minimoDisciplinas, int maximoTrancamentosParciais, int minimoSemestres, int maximoSemestres,
			int maximoTrancamentosTotais, int maxMatriculaInstitucional,int minimoCreditosObrigatorias,
			int minimoCreditosOptativas, int minimoCreditosComplementares, int minimoDisciplinasObrigatorias,
			int minimoDisciplinasOptativas, int minimoDisciplinasComplementares) {
		this.id = id;
		this.alunos = alunos;
		this.curso = curso;
		this.disciplinas = disciplinas;
		this.minimoCreditos = minimoCreditos;
		this.maximoCreditos = maximoCreditos;
		this.numero = numero;
		this.minimoCreditosCurriculo = minimoCreditosCurriculo;
		this.minimoDisciplinas = minimoDisciplinas;
		this.maximoTrancamentosParciais = maximoTrancamentosParciais;
		this.minimoSemestres = minimoSemestres;
		this.maximoSemestres = maximoSemestres;
		this.maximoTrancamentosTotais = maximoTrancamentosTotais;
		this.maximoMatriculaInstitucional = maxMatriculaInstitucional;
		this.minimoCreditosObrigatorias = minimoCreditosObrigatorias;
		this.minimoCreditosOptativas = minimoCreditosOptativas;
		this.minimoCreditosComplementares = minimoCreditosComplementares;
		this.minimoDisciplinasObrigatorias = minimoDisciplinasObrigatorias;
		this.minimoDisciplinasOptativas = minimoDisciplinasOptativas;
		this.minimoDisciplinasComplementares = minimoDisciplinasComplementares;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@OneToMany(mappedBy="curriculo")
	public Set<Aluno> getAlunos() {
		return alunos;
	}
	
	public void setAlunos(Set<Aluno> alunos) {
		this.alunos = alunos;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade= {CascadeType.ALL})
	@JoinColumn(name = "id_curso")
	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "curriculo_disciplina", schema = "public", joinColumns = { @JoinColumn(name = "id_curriculo", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "codigo_disciplina", nullable = false, updatable = false) })
	public Set<Disciplina> getDisciplinas() {
		return disciplinas;
	}

	public void setDisciplinas(Set<Disciplina> disciplinas) {
		this.disciplinas = disciplinas;
	}

	@Column(name = "min_creditos")
	public int getMinimoCreditos() {
		return minimoCreditos;
	}

	public void setMinimoCreditos(int minimoCreditos) {
		this.minimoCreditos = minimoCreditos;
	}

	@Column(name = "max_creditos")
	public int getMaximoCreditos() {
		return maximoCreditos;
	}

	public void setMaximoCreditos(int maximoCreditos) {
		this.maximoCreditos = maximoCreditos;
	}

	@Column(name = "numero")
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(name = "min_creditos_curriculo")
	public int getMinimoCreditosCurriculo() {
		return minimoCreditosCurriculo;
	}

	public void setMinimoCreditosCurriculo(int minimoCreditosCurriculo) {
		this.minimoCreditosCurriculo = minimoCreditosCurriculo;
	}

	@Column(name = "min_disciplinas")
	public int getMinimoDisciplinas() {
		return minimoDisciplinas;
	}

	public void setMinimoDisciplinas(int minimoDisciplinas) {
		this.minimoDisciplinas = minimoDisciplinas;
	}

	@Column(name = "max_trancamento_parcial")
	public int getMaximoTrancamentosParciais() {
		return maximoTrancamentosParciais;
	}

	public void setMaximoTrancamentosParciais(int maximoTrancamentosParciais) {
		this.maximoTrancamentosParciais = maximoTrancamentosParciais;
	}

	@Column(name = "min_semestres")
	public int getMinimoSemestres() {
		return minimoSemestres;
	}

	public void setMinimoSemestres(int minimoSemestres) {
		this.minimoSemestres = minimoSemestres;
	}

	@Column(name = "max_semestres")
	public int getMaximoSemestres() {
		return maximoSemestres;
	}

	public void setMaximoSemestres(int maximoSemestres) {
		this.maximoSemestres = maximoSemestres;
	}

	@Column(name = "max_trancamentos_totais")
	public int getMaximoTrancamentosTotais() {
		return maximoTrancamentosTotais;
	}

	public void setMaximoTrancamentosTotais(int maximoTrancamentosTotais) {
		this.maximoTrancamentosTotais = maximoTrancamentosTotais;
	}

	@Column(name = "max_matricula_institucional")
	public int getMaximoMatriculaInstitucional() {
		return maximoMatriculaInstitucional;
	}

	public void setMaximoMatriculaInstitucional(int maximoMatriculaInstitucional) {
		this.maximoMatriculaInstitucional = maximoMatriculaInstitucional;
	}

	@Column(name = "min_creditos_obrigatorias")
	public int getMinimoCreditosObrigatorias() {
		return minimoCreditosObrigatorias;
	}

	public void setMinimoCreditosObrigatorias(int minimoCreditosObrigatorias) {
		this.minimoCreditosObrigatorias = minimoCreditosObrigatorias;
	}

	@Column(name = "min_creditos_optativas")
	public int getMinimoCreditosOptativas() {
		return minimoCreditosOptativas;
	}

	public void setMinimoCreditosOptativas(int minimoCreditosOptativas) {
		this.minimoCreditosOptativas = minimoCreditosOptativas;
	}

	
	@Column(name = "min_creditos_complementares")
	public int getMinimoCreditosComplementares() {
		return minimoCreditosComplementares;
	}

	public void setMinimoCreditosComplementares(int minimoCreditosComplementares) {
		this.minimoCreditosComplementares = minimoCreditosComplementares;
	}

	@Column(name = "min_disciplinas_obrigatorias")
	public int getMinimoDisciplinasObrigatorias() {
		return minimoDisciplinasObrigatorias;
	}

	public void setMinimoDisciplinasObrigatorias(int minimoDisciplinasObrigatorias) {
		this.minimoDisciplinasObrigatorias = minimoDisciplinasObrigatorias;
	}

	@Column(name = "min_disciplinas_optativas")
	public int getMinimoDisciplinasOptativas() {
		return minimoDisciplinasOptativas;
	}

	public void setMinimoDisciplinasOptativas(int minimoDisciplinasOptativas) {
		this.minimoDisciplinasOptativas = minimoDisciplinasOptativas;
	}

	@Column(name = "min_disciplinas_complementares")
	public int getMinimoDisciplinasComplementares() {
		return minimoDisciplinasComplementares;
	}

	public void setMinimoDisciplinasComplementares(
			int minimoDisciplinasComplementares) {
		this.minimoDisciplinasComplementares = minimoDisciplinasComplementares;
	}

	
}
