package org.ufpb.s2dg.session.beans;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.ufpb.s2dg.entity.AlunoTurma;
import org.ufpb.s2dg.entity.DisciplinaTurmas;
import org.ufpb.s2dg.entity.Turma;
import org.ufpb.s2dg.session.Fachada;
import org.ufpb.s2dg.session.util.AlunoTurmaComparator;
import org.ufpb.s2dg.session.util.MenuAction;

@Name("alterarNotasSemestreAnteriorBean")
@Scope(ScopeType.SESSION)
@AutoCreate
public class AlterarNotasSemestreAnteriorBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8426420865543668893L;
	private List<DisciplinaTurmas> turmasPorDisciplina;
	@In
	Fachada fachada;
	@In
	MenuAction MenuAction;
	
	private Turma turma;
	private AlunoTurma alunoTurma;
	private List<AlunoTurma> alunoTurmas;
	
	@Create
	public void init() {
		List<Turma> turmas = fachada.getTurmasDoBancoPeriodoAnterior();
		if(turmas != null) {
			List<DisciplinaTurmas> disciplinaTurmas = DisciplinaTurmas.geraTurmasPorDisciplina(turmas);
			turmasPorDisciplina = disciplinaTurmas;
		}
	}
	
	public String atualizaMedia(){
		for(AlunoTurma alunoT : alunoTurmas)
			fachada.atualizaAlunoTurma(checaValoresDoAlunoTurma(alunoT));
		
		MenuAction.setId_MenuProfessor(2);
		
		return "/home.seam";
	}
	
	public void setTurmasPorDisciplina(List<DisciplinaTurmas> turmasPorDisciplina) {
		this.turmasPorDisciplina = turmasPorDisciplina;
	}
	
	public List<DisciplinaTurmas> getTurmasPorDisciplina() {
		return turmasPorDisciplina;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
		MenuAction.setId_Menu(3);;
	}

	public AlunoTurma getAlunoTurma() {
		return alunoTurma;
	}

	public void setAlunoTurma(AlunoTurma alunoTurma) {
		this.alunoTurma = alunoTurma;
		System.out.println("SETANDO ALUNO TURMA " + alunoTurma.getMedia());
	}

	public List<AlunoTurma> getAlunoTurmas() {
		if(turma != null) {
			alunoTurmas = fachada.getAlunoTurmasDoBanco(turma);
			if(alunoTurmas != null) {
				Collections.sort(alunoTurmas, new AlunoTurmaComparator());
			}
		}
		return alunoTurmas;
	}

	public void setAlunoTurmas(List<AlunoTurma> alunoTurmas) {
		this.alunoTurmas = alunoTurmas;
	}
	
	public AlunoTurma checaValoresDoAlunoTurma(AlunoTurma alunoTurma){
		if(alunoTurma.getSituacao() == AlunoTurma.Situacao.REPROVADO_POR_FALTA && 
				(alunoTurma.getMedia() == null || alunoTurma.getMedia() == 0))
			return alunoTurma;
		
		if(alunoTurma.getMedia() == null)
			alunoTurma.setMedia(0.0f);
		
		if(alunoTurma.getMedia() > 10)
			alunoTurma.setMedia(10.0f);

		if(alunoTurma.getMedia() < 0)
			alunoTurma.setMedia(0.0f);
		
		alunoTurma.setSituacao(alunoTurma.getMedia() >= AlunoTurmaBean.MEDIA_MINIMA_PARA_APROVACAO ? 
				AlunoTurma.Situacao.APROVADO : AlunoTurma.Situacao.REPROVADO_POR_MEDIA);
		
		alunoTurma.getTurma().setFinalizada(true);
		return alunoTurma;
	}
	
	public void teste(){
		System.out.println("TESTE");
	}
}
