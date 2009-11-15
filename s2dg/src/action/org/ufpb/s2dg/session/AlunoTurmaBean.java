package org.ufpb.s2dg.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.ufpb.s2dg.entity.AlunoTurma;
import org.ufpb.s2dg.entity.Disciplina;
import org.ufpb.s2dg.entity.Turma;
import org.ufpb.s2dg.entity.AlunoTurma.Situacao;

@Name("alunoTurmaBean")
@Scope(ScopeType.SESSION)
@AutoCreate
public class AlunoTurmaBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8355040116692086316L;

	private AlunoTurma alunoTurma;
	
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
		boolean condicao2 = !maximoTrancamentoParciaisPermitido();
		boolean condicao3 = !minimoDisciplinasCursando();
		return condicao1 && condicao2 && condicao3;
	}
	
	private boolean periodoTrancamentoParcialAberto() {
		return matriculaBean.podeFazerTrancamentoParcial();
	}

	private boolean maximoTrancamentoParciaisPermitido() {
		// TODO falta testar casos de sucesso
		/* TODO falta corrigir problemas na tabela de aluno_turma, pois nao se pode
		 * matricular duas vezes na mesma cadeira, problema de PK!
		 */
		if (fachada.getNumeroDeVezesAlunoTurmaCursadasAnteriormente() < 2)
			return true;
		return false;
	}

	private boolean minimoDisciplinasCursando() {
		// TODO casos de sucesso testados, faltam testes minuciosos
		if (fachada.numeroDeDisciplinasAtivas() > 1)
			return true;
		else
			return false;
	}
	
}
