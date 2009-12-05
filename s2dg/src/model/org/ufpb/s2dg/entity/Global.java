package org.ufpb.s2dg.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "global")
public class Global implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	private long id;
	
	@OneToOne(fetch = FetchType.EAGER, cascade= {CascadeType.ALL})
	@JoinColumn(name="periodo_atual")
	private Periodo periodoAtual;
	
	@OneToOne
	private Centro centro;
	
	public Periodo getPeriodoAtual() {
		return periodoAtual;
	}

	public void setPeriodoAtual(Periodo periodoAtual) {
		this.periodoAtual = periodoAtual;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setCentro(Centro centro) {
		this.centro = centro;
	}

	public Centro getCentro() {
		return centro;
	}
	
}