package org.ufpb.s2dg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "formaingresso")
public class FormaIngresso implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1586966854926227580L;
	
	public enum Tipo {VESTIBULAR, DECISAO_CONSEPE,DECISAO_JUDICIAL,MOBILIDADE_INTERNA, VESTIBULAR_EAD_DEMANDA_SOCIAL,
		VESTIBULAR_EAD_PROFESSOR,VESTIBULAR_PROESP_UFPB,VESTIBULAR_PROESP_OUTROS_ORG_PUB,REOPCAO_DE_POLO_EAD, EAD_TRANSFERENCIA_PSTV,
		DISCIPLINAS_ISOLADAS_ESPECIAL,TRANSFERENCIA_PSTV, CONVENIO, MUDANCA_DE_CAMPUS_CURSO, GRADUADO, VESTIBULAR_MUDANCA_DE_CURSO,
		TRANSFERENCIA_MUDANCA_DE_CURSO, CONVENIO_MUDANCA_DE_CURSO, TRANSFERENCIA_EX_OFICIO,TRANSFERENCIA_POR_FORCA_LIMINAR,
		TRANSFERENCIA_POR_FORCA_SENTENCA,PEC,REINGRESSO,PROGRAMA_PIANI,VESTIBULAR_2,PROGRAMA_DE_MOBILIDADE_ESTUDANTIL,PEC_MSC,REINGRESSO_POR_DECISAO_DO_CONSEPE,
		REOPCAO_MUDANCA_DE_TURNO
	}
	
	private Tipo tipo;
	private String ano;
	private String semetre;
	private long id;
	
	public FormaIngresso() {
		
	}
		
	public FormaIngresso(Tipo tipo, String ano, String semetre) {
		super();
		this.tipo = tipo;
		this.ano = ano;
		this.semetre = semetre;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemetre() {
		return semetre;
	}

	public void setSemetre(String semetre) {
		this.semetre = semetre;
	}
	
	public String toString(){
		return String.format("%s (em %s.%s)",tipo.toString(),ano,semetre);
	}
	
}
