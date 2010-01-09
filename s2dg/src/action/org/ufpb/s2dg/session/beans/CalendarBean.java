package org.ufpb.s2dg.session.beans;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.ufpb.s2dg.entity.EventoCalendarioTurma;
import org.ufpb.s2dg.session.Fachada;
import org.ufpb.s2dg.session.beans.UsuarioBean.TipoAbaAtiva;

@Name("calendarBean")
@Scope(ScopeType.SESSION)
@AutoCreate
public class CalendarBean implements Serializable {

	private static final long serialVersionUID = -5077367133202512506L;
	
	@In
	private Fachada fachada;
	
	private Date selectedDate;
	private EventoCalendarioTurma eventoCalendarioTurmaSelecionado;
	private Date prDateRangeBegin;
	private Date prDateRangeEnd;

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
		eventoCalendarioTurmaSelecionado.setExecucao(eventoCalendarioTurmaSelecionado.getPlanejamento());
		atualizaEventoCalendarioTurmaSelecionado();
	}
	
	public void atualizaEventoCalendarioTurmaSelecionado(){
		fachada.persisteEventoCalendarioTurma(eventoCalendarioTurmaSelecionado);
	}
	
	public String dataSelecionada(){
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("pt"));
		return getSelectedDate() != null ? dateFormat.format(getSelectedDate()) : "";
	}
	
	public Date getPrDateRangeBegin() {
		Calendar c = Calendar.getInstance();	
		Date date = new Date();
		date = c.getTime();		
		return new Date(date.getYear()-1, date.getMonth(), date.getDate());
	}

	public void setPrDateRangeBegin(Date prDateRangeBegin) {
		this.prDateRangeBegin = prDateRangeBegin;
	}

	public Date getPrDateRangeEnd() {
		Calendar c = Calendar.getInstance();	
		Date date = new Date();
		date = c.getTime();		
		return new Date(date.getYear()+1, date.getMonth(), date.getDate());
	}

	public void setPrDateRangeEnd(Date prDateRangeEnd) {
		this.prDateRangeEnd = prDateRangeEnd;
	}
	
	public boolean isProfessorTab(){
		TipoAbaAtiva abaAtiva = fachada.getAbaAtiva();
		if (abaAtiva == TipoAbaAtiva.DOCENTE) {
			return true;
		} else {
			return false;
		}				
	}


	
}
