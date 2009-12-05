package org.ufpb.s2dg.entity;

import static javax.persistence.FetchType.LAZY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.NotNull;

@Entity
@Table(name = "calendario")
public class Calendario implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Temporal(TemporalType.DATE)
	private Date inicioMatricula, fimMatricula, 
	ultimoDiaTrancamento, fimImplantacaoNotas;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	private Date inicioPeriodo, fimPeriodo;
	
	@NotNull
	@JoinColumn(name = "id_periodo")
	@ManyToOne(fetch=LAZY)
	private Periodo periodo;
	@ManyToOne
	private Centro centro;
	
	public Calendario () {}
	
	public Calendario(long id, Date inicioPeriodo, Date fimPeriodo,
			Periodo periodo) {
		this.id = id;
		this.inicioPeriodo = inicioPeriodo;
		this.fimPeriodo = fimPeriodo;
		this.periodo = periodo;
	}

	public Calendario(long id, Date inicioMatricula,
			Date inicioPeriodo, Date fimMatricula,
			Date ultimoDiaTrancamento, Date fimPeriodo,
			Date fimImplantacaoNotas, Periodo periodo) {
		this.id = id;
		this.inicioMatricula = inicioMatricula;
		this.inicioPeriodo = inicioPeriodo;
		this.fimMatricula = fimMatricula;
		this.ultimoDiaTrancamento = ultimoDiaTrancamento;
		this.fimPeriodo = fimPeriodo;
		this.fimImplantacaoNotas = fimImplantacaoNotas;
		this.periodo = periodo;
	}
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	private long id;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getInicioMatricula() {
		return inicioMatricula;
	}

	public void setInicioMatricula(Date inicioMatricula) {
		this.inicioMatricula = inicioMatricula;
	}

	public Date getInicioPeriodo() {
		return inicioPeriodo;
	}

	public void setInicioPeriodo(Date inicioPeriodo) {
		this.inicioPeriodo = inicioPeriodo;
	}

	public Date getFimMatricula() {
		return fimMatricula;
	}

	public void setFimMatricula(Date fimMatricula) {
		this.fimMatricula = fimMatricula;
	}

	public Date getUltimoDiaTrancamento() {
		return ultimoDiaTrancamento;
	}

	public void setUltimoDiaTrancamento(Date ultimoDiaTrancamento) {
		this.ultimoDiaTrancamento = ultimoDiaTrancamento;
	}

	public Date getFimPeriodo() {
		return fimPeriodo;
	}

	public void setFimPeriodo(Date fimPeriodo) {
		this.fimPeriodo = fimPeriodo;
	}

	public Date getFimImplantacaoNotas() {
		return fimImplantacaoNotas;
	}

	public void setFimImplantacaoNotas(Date fimImplantacaoNotas) {
		this.fimImplantacaoNotas = fimImplantacaoNotas;
	}

	public Periodo getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

	public void setCentro(Centro centro) {
		this.centro = centro;
	}

	public Centro getCentro() {
		return centro;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((centro == null) ? 0 : centro.hashCode());
		result = prime * result + ((periodo == null) ? 0 : periodo.hashCode());
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
		Calendario other = (Calendario) obj;
		if (centro == null) {
			if (other.centro != null)
				return false;
		} else if (!centro.equals(other.centro))
			return false;
		if (periodo == null) {
			if (other.periodo != null)
				return false;
		} else if (!periodo.equals(other.periodo))
			return false;
		return true;
	}
	
	
}
 