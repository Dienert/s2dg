package org.ufpb.s2dg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rg")
public class RG implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1794659432734824839L;

	private long id;
	private String numero;
	private String emissor;
	private String uf;
	
	public RG(){
		
	}
	
	public RG(long id){
		this.id = id;		
	}
	
	public RG(String numero, String emissor, String uf) {
		super();
		this.numero = numero;
		this.emissor = emissor;
		this.uf = uf;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public String getEmissor() {
		return emissor;
	}
	
	public void setEmissor(String emissor) {
		this.emissor = emissor;
	}
	
	public String getUf() {
		return uf;
	}
	
	public void setUf(String uf) {
		this.uf = uf;
	}
	
	@Override
	public String toString() {
		return String.format("%s %s-%s",numero,emissor,uf);
	}
	
}
