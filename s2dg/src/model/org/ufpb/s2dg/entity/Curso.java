package org.ufpb.s2dg.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.ManyToOne;

@Entity
@Table(name = "curso")
public class Curso implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private Set<Curriculo> curriculos;
	private Set<Oferta> ofertas;
	private String nome;
	private String codigo;
	private String cur_ato_criacao;
	private Centro centro;
	
	public Curso() {
	}

	public Curso(long id) {
		this.id = id;
	}
	
	public Curso(long id, Set<Curriculo> curriculos, Set<Oferta> ofertas, String nome, String codigo, String cur_ato_criacao) {
		this.id = id;
		this.curriculos = curriculos;
		this.ofertas = ofertas;
		this.nome = nome;
		this.codigo = codigo;
		this.cur_ato_criacao = cur_ato_criacao;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@OneToMany(mappedBy="curso")
	public Set<Curriculo> getCurriculos() {
		return curriculos;
	}

	public void setCurriculos(Set<Curriculo> curriculos) {
		this.curriculos = curriculos;
	}

	@OneToMany(mappedBy="curso")
	public Set<Oferta> getOfertas() {
		return ofertas;
	}

	public void setOfertas(Set<Oferta> ofertas) {
		this.ofertas = ofertas;
	}

	@Column(name = "nome", length = 50)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	@Column(name = "codigo", length = 9)
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	@Column(name = "cur_ato_criacao", length = 100)
	public String getCur_ato_criacao() {
		return cur_ato_criacao;
	}

	public void setCur_ato_criacao(String cur_ato_criacao) {
		this.cur_ato_criacao = cur_ato_criacao;
	}

	public void setCentro(Centro centro) {
		this.centro = centro;
	}

	
	@ManyToOne
	public Centro getCentro() {
		return centro;
	}


}
