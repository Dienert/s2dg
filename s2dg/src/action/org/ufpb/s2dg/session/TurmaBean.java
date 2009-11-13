package org.ufpb.s2dg.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.ufpb.s2dg.entity.Professor;
import org.ufpb.s2dg.entity.Turma;

@Name("turmaBean")
@Scope(ScopeType.SESSION)
@AutoCreate
public class TurmaBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Turma turma;
	@In
	Fachada fachada;
	@In
	FacesContext facesContext;
	@In
	MenuAction MenuAction;
	@In
	TimestampBean TimestampBean;

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		MenuAction.setId_Menu(0);
		this.turma = turma;
		fachada.cancelarEdicaoDeAvaliacao();
		fachada.initAvaliacoes();
		fachada.initAlunoTurmas();
	}
	
	public void switchCalcMediaAuto() {
		if (turma.isCalcularMediaAutomaticamente())
			turma.setCalcularMediaAutomaticamente(false);
		else {
			turma.setCalcularMediaAutomaticamente(true);
			fachada.atualizaAlunoTurmas();
		}
	}
	
	public void atualiza() {
		fachada.atualizaTurma();
	}
	
	public void atualizaPlanoDeCurso() {
		atualiza();
		String time = TimestampBean.getHour();
		facesContext.addMessage("corpo2", new FacesMessage(FacesMessage.SEVERITY_INFO,time+" - Plano de curso salvo com sucesso.", null));
	}
	
	public List<Professor> listaProfessores() {
		if(turma != null) {
			Set<Professor> setProfessores = turma.getProfessores();
			if(setProfessores != null) {
				List<Professor> listProfessores = new ArrayList<Professor>(setProfessores);
				for(Professor professor: listProfessores) {
					professor.setUsuario(fachada.getUsuarioProfessor(professor.getMatricula()));
				}
				Collections.sort(listProfessores,new ProfessorComparator());
				return listProfessores;
			}
		}
		return null;
	}
	
}
