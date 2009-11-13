package org.ufpb.s2dg.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Aula implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8161266557144004043L;
	@Id
	private long id;
	private String planejamento;
	private String execucao;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPlanejamento() {
		return planejamento;
	}
	public void setPlanejamento(String planejamento) {
		this.planejamento = planejamento;
	}
	public String getExecucao() {
		return execucao;
	}
	public void setExecucao(String execucao) {
		this.execucao = execucao;
	}

}
