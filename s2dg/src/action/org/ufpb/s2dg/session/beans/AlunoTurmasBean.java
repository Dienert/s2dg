package org.ufpb.s2dg.session.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.ufpb.s2dg.entity.Aluno;
import org.ufpb.s2dg.entity.AlunoTurma;
import org.ufpb.s2dg.entity.AlunoTurmaAvaliacao;
import org.ufpb.s2dg.entity.Avaliacao;
import org.ufpb.s2dg.entity.Turma;
import org.ufpb.s2dg.entity.AlunoTurma.Situacao;
import org.ufpb.s2dg.session.Fachada;
import org.ufpb.s2dg.session.util.AlunoTurmaComparator;

@Name("alunoTurmasBean")
@Scope(ScopeType.SESSION)
@AutoCreate
public class AlunoTurmasBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5845872799300607390L;

	private List<AlunoTurma> alunoTurmas;
	
	@In
	private Fachada fachada;
	@In
	FacesContext facesContext;
	
	@In
	TimestampBean TimestampBean;

	public void init() {
		Turma turmaAtual = fachada.getTurma();
		if(turmaAtual != null) {
			alunoTurmas = fachada.getAlunoTurmasDoBanco();
			if(alunoTurmas != null) {
				for(int i = 0; i < alunoTurmas.size(); i++) {
					alunoTurmas.get(i).getAluno().setUsuario(fachada.getUsuarioAluno(alunoTurmas.get(i).getAluno().getMatricula()));
				}
				Collections.sort(alunoTurmas, new AlunoTurmaComparator());
			}
		}
	}
	
	public List<AlunoTurma> getAlunoTurmas() {
		fachada.setAlunoTurmaAvaliacoes(new ArrayList<AlunoTurmaAvaliacao>());
		return alunoTurmas;
	}

	public void setAlunoTurmas(List<AlunoTurma> alunoTurmas) {
		this.alunoTurmas = alunoTurmas;
	}
	
	public void atualizaAlunoTurmas() {
		Turma turmaAtual = fachada.getTurma();
		if(turmaAtual != null) {
			fachada.atualizaTurma();
			List<AlunoTurmaAvaliacao> alunoTurmaAvaliacoes = fachada.getAlunoTurmaAvaliacoes();
			if(alunoTurmaAvaliacoes != null) {
				for(int i = 0; i < alunoTurmaAvaliacoes.size(); i++)
					fachada.atualizaAlunoTurmaAvaliacao(alunoTurmaAvaliacoes.get(i));
			}
			if((turmaAtual.isCalcularMediaAutomaticamente())&&(alunoTurmas != null))
				if(alunoTurmas.size() > 0)
					calculaMedias();
			if(alunoTurmas != null) {
				for(int i = 0; i < alunoTurmas.size(); i++)
					fachada.atualizaAlunoTurma(alunoTurmas.get(i));
			}
			String time = TimestampBean.getHour();
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,time+" - Informacoes salvas com sucesso.",null);
			facesContext.addMessage("corpo2",facesMessage);
		}
	}
	
	private void calculaMedias() {
		if(alunoTurmas != null) {
			for(int i = 0; i < alunoTurmas.size(); i++) {
				float somaNota = 0;
				float somaPeso = 0;
				List<Avaliacao> avaliacoes = fachada.getAvaliacoes();
				for(int j = 0; j < avaliacoes.size(); j++) {
					somaNota += fachada.getAlunoTurmaAvaliacao(alunoTurmas.get(i), avaliacoes.get(j)).getNota()
					* avaliacoes.get(j).getPeso();
					somaPeso += avaliacoes.get(j).getPeso();
				}
				alunoTurmas.get(i).setMedia(somaNota/somaPeso);
			}
		}
	}
	
	public int numeroDeDisciplinasAtivas(){
		return ((alunoTurmas == null) ? 0 : alunoTurmas.size());
	}
	
	public void trancamentoTotal(){
		for(AlunoTurma alunoTurma : alunoTurmas){
			if(alunoTurma.getSituacao() == Situacao.EM_CURSO)
				alunoTurma.setSituacao(Situacao.TRANCADO);
		}
		System.out.print("Pegou geral total.");
	}
	
	public boolean fazerTrancamentoTotal(){
		Aluno aluno = fachada.getAluno();
		int trancamentoTotais = aluno.getTracamentosTotais();
		if (trancamentoTotais >= aluno.getCurriculo().getMaximoTrancamentosTotais())
			return false;
		
		List<AlunoTurma> alunoTurmas = fachada.getAlunoTurmasEmCurso();
		if(alunoTurmas != null) {
			for(int i = 0; i < alunoTurmas.size(); i++) {
				AlunoTurma alunoTurma = alunoTurmas.get(i);
				if (alunoTurma.getSituacao() == AlunoTurma.Situacao.EM_CURSO){
					alunoTurma.setSituacao(AlunoTurma.Situacao.TRANCADO_TOTAL);
					fachada.persisteSituacaoTurma(alunoTurma);
				}
			}
			aluno.setTracamentosTotais(++trancamentoTotais);
			fachada.persisteAluno(aluno);
		}
		return true;
	}
}
