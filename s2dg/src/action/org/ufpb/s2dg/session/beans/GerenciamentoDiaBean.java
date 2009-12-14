package org.ufpb.s2dg.session.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.ufpb.s2dg.entity.AlunoTurma;
import org.ufpb.s2dg.entity.AlunoTurmaAvaliacao;
import org.ufpb.s2dg.entity.EventoCalendarioTurma;
import org.ufpb.s2dg.entity.Turma;
import org.ufpb.s2dg.session.Fachada;
import org.ufpb.s2dg.session.util.AlunoTurmaComparator;
import org.ufpb.s2dg.session.util.CalendarDataModelImpl;
import org.ufpb.s2dg.session.util.CalendarDataModelItemImpl;
import org.ufpb.s2dg.session.util.ItemDeCalendario.TipoData;

@Name("gerenciaDia")
@Scope(ScopeType.SESSION)
@AutoCreate
public class GerenciamentoDiaBean implements Serializable {
	
	private static final long serialVersionUID = 6489975404995636320L;
	
	@In
    private Fachada fachada;
	
	@In
	FacesContext facesContext;
	
	@In
	CalendarBean calendarBean;
	
	private List<AlunoTurma> alunoTurmas;
	private EventoCalendarioTurma eventoCalendarioTurma;	
	
	public GerenciamentoDiaBean(){
		super();		
	}
	
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
			List<EventoCalendarioTurma> list = fachada.getEventosCalendarioTurma(fachada.getTurma());
			for (int i = 0; i< list.size(); i++) {
				EventoCalendarioTurma evento_dia = list.get(i);
				if (evento_dia.getData().compareTo(fachada.getSelectedDate()) == 0) {
					eventoCalendarioTurma = evento_dia;
				}
			}
		}
		
	}
	
	public List<AlunoTurma> getAlunoTurmas() {
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
		return alunoTurmas;
	}

	public void setAlunoTurmas(List<AlunoTurma> alunoTurmas) {
		this.alunoTurmas = alunoTurmas;
	}	
	
	public EventoCalendarioTurma getEventoCalendarioTurma() {	
		
		List<EventoCalendarioTurma> list = fachada.getEventosCalendarioTurma(fachada.getTurma());
		for (int i = 0; i< list.size(); i++) {
			EventoCalendarioTurma evento_dia = list.get(i);				       
			java.util.Date selectedDate = calendarBean.getSelectedDate();
			if (evento_dia != null && selectedDate.compareTo(evento_dia.getData()) == 0) {
				return list.get(i);
			}
		}
		return new EventoCalendarioTurma();
	}

	public void setEventoCalendarioTurma(EventoCalendarioTurma eventoCalendarioTurma) {
		this.eventoCalendarioTurma = eventoCalendarioTurma;
	}
	
}
