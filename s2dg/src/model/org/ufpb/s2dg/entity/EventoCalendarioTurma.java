package org.ufpb.s2dg.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class EventoCalendarioTurma extends EventoCalendario implements Serializable {
	private static final long serialVersionUID = 1L;
	private Set<Avaliacao> avaliacoes;
	private Set<AlunoPresenca> presenca; //AQUI /*Como preencher a lista com os ids dos alunos?*/
	private boolean presencaPublicada;
	private String planejamento;
	private String execucao;
	
	public EventoCalendarioTurma() {}
	
	public EventoCalendarioTurma(Date date) {
		super(date, EventoCalendario.TipoData.DIA_AULA_DISCIPLINA.toString());
	}
	
	@Deprecated
	public EventoCalendarioTurma(Date date, String event) {
		super(date, event);
	}

	@OneToMany(mappedBy="dataEvento")
	public Set<Avaliacao> getAvaliacoes() {
		return avaliacoes;
	}

	public void setAvaliacoes(Set<Avaliacao> avaliacoes) {
		this.avaliacoes = avaliacoes;
	}
	
	public void atribuirPresenca(Aluno aluno, boolean presente){
		presenca.add(new AlunoPresenca(aluno, presente));
	}
	
	public void atribuirPresenca(AlunoPresenca alunoPresenca){
		presenca.add(alunoPresenca);
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

	public boolean isPresencaPublicada() {
		return presencaPublicada;
	}

	public void setPresencaPublicada(boolean presencaPublicada) {
		this.presencaPublicada = presencaPublicada;
	}
	
	
	@OneToMany
	public Set<AlunoPresenca> getPresenca() {
		return presenca;
	}

	public void setPresenca(Set<AlunoPresenca> presenca) {
		this.presenca = presenca;
	}

	
	
	
}
