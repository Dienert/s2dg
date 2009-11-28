package org.ufpb.s2dg.session.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.ufpb.s2dg.entity.Calendario;
import org.ufpb.s2dg.entity.Professor;
import org.ufpb.s2dg.entity.Turma;
import org.ufpb.s2dg.session.Fachada;
import org.ufpb.s2dg.session.util.MenuAction;
import org.ufpb.s2dg.session.util.ProfessorComparator;

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
	
	public Date getDataAtual() {  
		Calendar calendar = new GregorianCalendar();  
		Date date = new Date();  
		calendar.setTime(date);  
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public boolean podeFinalizarTurma() {
		return true;
		/*Calendario calendario = fachada.getCalendario();
		if(calendario != null) {
			Date inicioImplantacao = calendario.getFimPeriodo();
			inicioImplantacao.setDate(inicioImplantacao.getDate()+1);
			Date fimImplantacao = calendario.getFimImplantacaoNotas();
			Date hoje = getDataAtual();
			if((hoje.compareTo(inicioImplantacao) >= 0)&&(hoje.compareTo(fimImplantacao) <= 0))
				return true;
			else
				return false;
		}
    	return false;
    	*/
	}
	
}
