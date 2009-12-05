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
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private Aluno aluno;
	private Professor professor;
	private String senha;
	private String nome;
	private String cpf;
	private Set<Role> roles = new HashSet<Role>(0);
	private String email;
	private String pergunta;
	private String resposta;
	private RG rg;
	

	public Usuario() {
	}

	public Usuario(long id) {
		this.id = id;
	}

	public Usuario(long id, Aluno aluno, Professor professor, String senha,
			String nome, String cpf, Set<Role> roles, RG rg) {
		this.id = id;
		this.aluno = aluno;
		this.professor = professor;
		this.senha = senha;
		this.nome = nome;
		this.cpf = cpf;
		this.roles = roles;
		this.rg = rg;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@OneToOne(fetch = FetchType.EAGER, cascade= {CascadeType.ALL})
	@JoinColumn(name = "aluno_id")
	public Aluno getAluno() {
		return this.aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	@OneToOne(fetch = FetchType.EAGER, cascade= {CascadeType.ALL})
	@JoinColumn(name = "usuario_rg")
	public RG getRg() {
		return this.rg;
	}

	
	public void setRg(RG rg) {
		this.rg = rg;
	}

	@OneToOne(fetch = FetchType.EAGER, cascade= {CascadeType.ALL})
	@JoinColumn(name = "professor_matricula")
	public Professor getProfessor() {
		return this.professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public String getSenha() {
		return this.senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Column(name = "nome", length = 50)
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return this.cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "usuario_role", schema = "public", joinColumns = { @JoinColumn(name = "usuario_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "role_id", nullable = false, updatable = false) })
	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Column(name="email")
	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	@Column(name="pergunta")
	public void setPergunta(String pergunta) {
		this.pergunta = pergunta;
	}

	public String getPergunta() {
		return pergunta;
	}

	@Column(name="resposta")
	public void setResposta(String resposta) {
		this.resposta = resposta;
	}

	public String getResposta() {
		return resposta;
	}
	
	public boolean usuarioIsAluno(){
		return (aluno != null);
	}
	
	public boolean usuarioIsProfessor(){
		return (professor !=null);
	}

	/*@Column(name="rg")
	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}*/

}
