package org.ufpb.s2dg.session.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.ufpb.s2dg.entity.AlunoTurma;
import org.ufpb.s2dg.entity.Disciplina;
import org.ufpb.s2dg.entity.Turma;
import org.ufpb.s2dg.entity.AlunoTurma.Situacao;
import org.ufpb.s2dg.session.Fachada;
import org.ufpb.s2dg.session.util.MenuAction;

@Name("alunoTurmaBean")
@Scope(ScopeType.SESSION)
@AutoCreate
public class AlunoTurmaBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8355040116692086316L;

	private AlunoTurma alunoTurma;
	
	@Logger
	private Log log;
	
	@In
	private Fachada fachada;

	@In
	MenuAction MenuAction;
	
	//TODO Clodoaldo: isso pode ser perigoso, caso der pau, checar!
	@In
	MatriculaBean matriculaBean;
	
	public AlunoTurma getAlunoTurma() {
		return alunoTurma;
	}

	public void setAlunoTurma(AlunoTurma alunoTurma) {
		MenuAction.setId_Menu(0);
		this.alunoTurma = alunoTurma;
		if(alunoTurma != null) {
			Turma t = alunoTurma.getTurma();
			if(t == null)
				t = fachada.getTurmaDoBanco(alunoTurma);
			Disciplina d = t.getDisciplina();
			if(d == null)
				d = fachada.getDisciplinaDoBanco(t);
			t.setDisciplina(d);
			fachada.setTurma(t);
		}
	}
	
	public List<AlunoTurma> getAlunoTurmaAsList() {
		if(alunoTurma != null) {
			List<AlunoTurma> list = new ArrayList<AlunoTurma>();
			list.add(alunoTurma);
			return list;
		}
		else return null;
	}
	
	public void trancamentoParcial(){
		System.out.print("Pegou geral entrando no metodo. Id="+alunoTurma.getId() +"Id_turma ="+alunoTurma.getTurma() +"id aluno="+ alunoTurma.getAluno()+ "SItuacao ="+alunoTurma.getSituacao());
		alunoTurma.setSituacao(Situacao.TRANCADO);
		fachada.trancamentoParcial(alunoTurma);
		System.out.print("Pegou geral na saida. SItuacao ="+alunoTurma.getSituacao());
	}
	
	public boolean checaCondicoesTrancamentoParcial(){
		boolean condicao1 = periodoTrancamentoParcialAberto();
		log.info(" periodoTrancamentoParcialAberto() - {0}", condicao1);
		boolean condicao2 = !maximoTrancamentoParciaisPermitido();
		log.info(" !maximoTrancamentoParciaisPermitido() - {0}", condicao2);
		boolean condicao3 = !minimoDisciplinasCursando();
		log.info(" !minimoDisciplinasCursando() - {0}", condicao3);
		return condicao1 && condicao2 && condicao3;
	}
	
	private boolean periodoTrancamentoParcialAberto() {
		return matriculaBean.podeFazerTrancamentoParcial();
	}

	private boolean maximoTrancamentoParciaisPermitido() {
		if (fachada.getNumeroDeVezesAlunoTurmaCursadasAnteriormente() < 2)
			return false;
		return true;
	}

	private boolean minimoDisciplinasCursando() {
		log.info("fachada.numeroDeDisciplinasAtivas() - {0}", fachada.numeroDeDisciplinasAtivas());
		if (fachada.numeroDeDisciplinasAtivas() > 1)
			return false;
		else
			return true;
	}
	
}