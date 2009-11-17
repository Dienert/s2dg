package org.ufpb.s2dg.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.NotNull;

/**
 * 
 * @deprecated Esta classe está sendo substituida por {@link org.ufpb.s2dg.entity.Evento}
 *
 */
@Entity
@Table(name = "calendario")
@Deprecated
public class Calendario implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Temporal(TemporalType.DATE)
	private Date inicioMatricula, fimMatricula, 
	ultimoDiaTrancamento, fimImplantacaoNotas;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	private Date inicioPeriodo, fimPeriodo;
	
	@NotNull
	@OneToOne(fetch = FetchType.LAZY, cascade= {CascadeType.ALL})
	@JoinColumn(name = "id_periodo")
	private Periodo periodo;
	
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
}

/*inicio do periodo, inicio das matriculas, fim das matriculas, 
 * ultimo dia de trancamento, termino periodo letivo, ultimo dia implantacao
 * de notas
 * @temporal*/

 