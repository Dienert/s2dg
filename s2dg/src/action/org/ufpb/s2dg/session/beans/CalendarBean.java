package org.ufpb.s2dg.session.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.ufpb.s2dg.entity.EventoCalendarioTurma;
import org.ufpb.s2dg.session.Fachada;

@Name("calendarBean")
@Scope(ScopeType.SESSION)
@AutoCreate
public class CalendarBean implements Serializable {

	private static final long serialVersionUID = -5077367133202512506L;
	
	@In
	private Fachada fachada;
	private Date selectedDate;
	private EventoCalendarioTurma eventoCalendarioTurmaSelecionado;
	
	public void init() {
		Calendar c = Calendar.getInstance();
		selectedDate = c.getTime();
	}

	public Date getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
		
		setEventoCalendarioTurmaSelecionado(
				fachada.getEventoCalendarioTurma(selectedDate));
	}

	public EventoCalendarioTurma getEventoCalendarioTurmaSelecionado() {
		return eventoCalendarioTurmaSelecionado;
	}

	public void setEventoCalendarioTurmaSelecionado(
			EventoCalendarioTurma eventoCalendarioTurmaSelecionado) {
		this.eventoCalendarioTurmaSelecionado = eventoCalendarioTurmaSelecionado;
	}

	public void confirmarPlanejamento(){
		eventoCalendarioTurmaSelecionado.setPlanejamento(eventoCalendarioTurmaSelecionado.getPlanejamento());
	}
	
	public void atualzaEventoCalendarioTurmaSelecionado(){
		fachada.persisteEventoCalendarioTurma(eventoCalendarioTurmaSelecionado);
	}
	
}
