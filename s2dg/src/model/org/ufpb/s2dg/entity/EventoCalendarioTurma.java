package org.ufpb.s2dg.entity;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class EventoCalendarioTurma extends EventoCalendario implements Serializable {
	private static final long serialVersionUID = 1L;
	private Set<Avaliacao> avaliacoes;
	private List<AlunoPresenca> presencas; //AQUI /*Como preencher a lista com os ids dos alunos?*/
	private boolean presencaPublicada;
	private String planejamento;
	private String execucao;
	private Turma turma;
	
	public EventoCalendarioTurma() {
		super();
		setTipoData(EventoCalendario.TipoData.DIA_AULA_DISCIPLINA);
	}
	
	@Deprecated
	public EventoCalendarioTurma(Date date) {
		super(date, EventoCalendario.TipoData.DIA_AULA_DISCIPLINA.toString());
		setTipoData(EventoCalendario.TipoData.DIA_AULA_DISCIPLINA);
	}
	
	public EventoCalendarioTurma(Date date, Turma turma) {
		super(date, EventoCalendario.TipoData.DIA_AULA_DISCIPLINA.toString());
		setTipoData(EventoCalendario.TipoData.DIA_AULA_DISCIPLINA);
		this.turma = turma;
	}
	
	@Deprecated
	public EventoCalendarioTurma(Date date, String event) {
		super(date, event);
	}
	
	@Column(nullable = true)
	@OneToMany(mappedBy="dataEvento", fetch = EAGER) //TODO CLODOALDO verificar se a remoção foi bem sucedida "cascade = ALL, "
	public Set<Avaliacao> getAvaliacoes() {
		return avaliacoes;
	}
	
	public void adicionarAvaliacao(Avaliacao avaliacao){
		if(avaliacoes == null)
			avaliacoes = new HashSet<Avaliacao>();
		
		avaliacoes.add(avaliacao);
	}
	
	public void setAvaliacoes(Set<Avaliacao> avaliacoes) {
		this.avaliacoes = avaliacoes;
	}
	
	public void atribuirPresenca(Aluno aluno, boolean presente){	
		if(presencas == null)
			presencas = new ArrayList<AlunoPresenca>();
			
		presencas.add(new AlunoPresenca(aluno, presente, this));
	}
	
	public void atribuirPresenca(AlunoPresenca alunoPresenca){	
		if(presencas == null)
			presencas = new ArrayList<AlunoPresenca>();
		
		presencas.add(alunoPresenca);
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
	
	@OneToMany(cascade = ALL, mappedBy = "eventocalendarioturma")
	public List<AlunoPresenca> getPresencas() {
		return presencas;
	}

	@ManyToOne(cascade = ALL, optional = false)
	@JoinColumn(name="turma_id", referencedColumnName = "id")
	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public void setPresencas(List<AlunoPresenca> presencas) {
		this.presencas = presencas;
	}

	public String toString(){
		return String.format(
				"Avaliacoes: %s\n" +
				"Presenca: %s\n" +
				"Presenca publicada? %s\n" +
				"Planejamento: %s\n" +
				"Execucao: %s\n", 
				avaliacoes, presencas, presencaPublicada + "", planejamento, execucao);
	}
	
	
}
