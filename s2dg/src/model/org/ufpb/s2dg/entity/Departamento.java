package org.ufpb.s2dg.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Departamento implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2181974333456922304L;
	
	@Id
	@GeneratedValue(strategy=IDENTITY)
	private long id;
	private String nome;
	private String sigla;
	@ManyToOne
	private Centro centro;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSigla() {
		return sigla;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	public Centro getCentro() {
		return centro;
	}
	public void setCentro(Centro centro) {
		this.centro = centro;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}
