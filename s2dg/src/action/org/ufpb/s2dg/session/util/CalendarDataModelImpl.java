package org.ufpb.s2dg.session.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ValueChangeEvent;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.richfaces.model.CalendarDataModel;
import org.richfaces.model.CalendarDataModelItem;
import org.ufpb.s2dg.entity.AlunoTurma;
import org.ufpb.s2dg.entity.Avaliacao;
import org.ufpb.s2dg.entity.Calendario;
import org.ufpb.s2dg.entity.EventoCalendarioTurma;
import org.ufpb.s2dg.entity.Turma;
import org.ufpb.s2dg.session.Fachada;
import org.ufpb.s2dg.session.beans.CalendarBean;
import org.ufpb.s2dg.session.beans.UsuarioBean;
import org.ufpb.s2dg.session.util.ItemDeCalendario.TipoData;


@Name("calendarDataModel")
@AutoCreate
public class CalendarDataModelImpl implements CalendarDataModel {

	@In
	Fachada fachada;

	@In
	CalendarBean calendarBean;

	private CalendarDataModelItem[] items;
	private String currentDescription;
	private String currentShortDescription;
	private Date currentDate;
	private boolean currentDisabled;   

	private List<AlunoTurma> alunoTurmas;	

	public CalendarDataModelItem[] getData(Date[] dateArray) {
		if (dateArray == null) {
			return null;
		}
		if (items == null) {  
			items = new CalendarDataModelItem[dateArray.length];
			for (int i = 0; i < dateArray.length; i++) {
				items[i] = createDataModelItem(dateArray[i]);
			}
		}
		return items;
	}

	protected CalendarDataModelItem createDataModelItem(Date date) {
		Calendario calendario = fachada.getCalendarioProfessor();
		String eventos = "";		
		TipoData tipoData = null;
		CalendarDataModelItemImpl item = new CalendarDataModelItemImpl();
		if(equals(date, calendario.getFimImplantacaoNotas())) {
			eventos += "Fim de Implantação de Notas\n";
			tipoData = TipoData.EVENTO_PERIODO;			
		}
		if(equals(date, calendario.getFimMatricula())) {
			eventos += "Fim do Período de Matrícula\n";
			tipoData = TipoData.EVENTO_PERIODO;
		}
		if(equals(date, calendario.getFimPeriodo())) {
			eventos += "Fim do Período\n";
			tipoData = TipoData.EVENTO_PERIODO;
		}
		if(equals(date, calendario.getInicioMatricula())) {
			eventos += "Início do Período de Matrícula\n";
			tipoData = TipoData.EVENTO_PERIODO;
		}
		if(equals(date, calendario.getInicioPeriodo())) {
			eventos += "Início do Período\n";
			tipoData = TipoData.EVENTO_PERIODO;
		}
		if(equals(date, calendario.getUltimoDiaTrancamento())) {
			eventos += "Último Dia de Trancamento\n";
			tipoData = TipoData.EVENTO_PERIODO;			
		}

		List<EventoCalendarioTurma> eventosTurma = fachada.getTurma().getEventosCalendarioTurma();
		ArrayList<EventoCalendarioTurma> datasEEventos = new ArrayList<EventoCalendarioTurma>();
		for (int i = 0; i < eventosTurma.size(); i++) {
			EventoCalendarioTurma evento_dia = eventosTurma.get(i);
			datasEEventos.add(evento_dia);										
		}
		
		Boolean diaEvento = false;	
		if(datasEEventos != null) {
			for (int j = 0; j < datasEEventos.size(); j++) {
				EventoCalendarioTurma evento = datasEEventos.get(j);
				if (equals(date, evento.getData())) {
					if (evento.getTipoData() == org.ufpb.s2dg.entity.EventoCalendario.TipoData.AVALIACAO) {
						tipoData = TipoData.AVALIACAO;
						if (!eventos.contains(datasEEventos.get(j).getEvento())) {
							eventos += datasEEventos.get(j).getEvento() + "\n";	
						}																
						diaEvento = true;	
						datasEEventos.remove(j);
					} else {
						tipoData = TipoData.DIA_AULA_DISCIPLINA;
						if (!eventos.contains(datasEEventos.get(j).getEvento())) {
							eventos += datasEEventos.get(j).getEvento() + "\n";	
						}					
						diaEvento = true;
						datasEEventos.remove(j);
					}
				}					
			}
		}

		Calendar c = Calendar.getInstance();	
		c.setTime(date);
		Map<String, String> conteudoItem = new HashMap<String, String>();		
		item.setDay(c.get(Calendar.DAY_OF_MONTH));	
		if (!(eventos.equals(""))) {			
			item.setData(eventos);
			item.setDay(c.get(Calendar.DAY_OF_MONTH));			
			item.setToolTip(tipoData);
			item.setEnabled(diaEvento);
			conteudoItem.put("shortDescription", eventos);
			conteudoItem.put("description", "");			
		}		
		if (tipoData != null) {
			item.setStyleClass("ic_" + tipoData.toString().toLowerCase());
		}
		item.setData(conteudoItem); 
		return item;
	} 

	public Object getToolTip(Date date) {
		return null;
	}


	public CalendarDataModelItem[] getItems() {
		return items;
	}


	public void setItems(CalendarDataModelItem[] items) {
		this.items = items;
	}

	public void valueChanged(ValueChangeEvent event) {		
		calendarBean.setSelectedDate((Date)event.getNewValue());		
	}

	public void storeDayDetails() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getCurrentDate());
		((HashMap)items[calendar.get(Calendar.DAY_OF_MONTH)-1].getData()).put("shortDescription", getCurrentShortDescription());
		((HashMap)items[calendar.get(Calendar.DAY_OF_MONTH)-1].getData()).put("description", getCurrentDescription());
	}

	public String getCurrentDescription() {
		return currentDescription;
	}

	public void setCurrentDescription(String currentDescription) {
		this.currentDescription = currentDescription;
	}

	public boolean isCurrentDisabled() {
		return currentDisabled;
	}

	public void setCurrentDisabled(boolean currentDisabled) {
		this.currentDisabled = currentDisabled;
	}

	public String getCurrentShortDescription() {
		return currentShortDescription;
	}

	public void setCurrentShortDescription(String currentShortDescription) {
		this.currentShortDescription = currentShortDescription;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public boolean equals(Date d1, Date d2){
		long dif = d1.getTime() - d2.getTime();
		return dif >= 0 && dif < 24*60*60*1000;
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
			java.util.Date selectedDate = getCurrentDate();
			if (evento_dia != null && selectedDate != null) {
				if (selectedDate.compareTo(evento_dia.getData()) == 0) {

				}
				return list.get(i);
			}
		}
		return new EventoCalendarioTurma();
	}

	public void confirmarPlanejamentoEventoCalendarioTurma(){
		Date dataEventoCalendarioTurma = getEventoCalendarioTurma().getData();
		fachada.confirmarPlanejamentoEventoCalendarioTurma(dataEventoCalendarioTurma);
	}		

	public TipoData getTipoEvento(Date date, Calendario calendario, String eventos) {
		TipoData tipoData = null;
		if(equals(date, calendario.getFimImplantacaoNotas())) {
			eventos += "Fim de Implantação de Notas\n";
			tipoData = TipoData.EVENTO_PERIODO;
		}
		if(equals(date, calendario.getFimMatricula())) {
			eventos += "Fim do Período de Matrícula\n";
			tipoData = TipoData.EVENTO_PERIODO;
		}
		if(equals(date, calendario.getFimPeriodo())) {
			eventos += "Fim do Período\n";
			tipoData = TipoData.EVENTO_PERIODO;
		}
		if(equals(date, calendario.getInicioMatricula())){
			eventos += "Início do Período de Matrícula\n";
			tipoData = TipoData.EVENTO_PERIODO;
		}
		if(equals(date, calendario.getInicioPeriodo())){
			eventos += "Início do Período\n";
			tipoData = TipoData.EVENTO_PERIODO;
		}
		if(equals(date, calendario.getUltimoDiaTrancamento())) {
			eventos += "Último Dia de Trancamento\n";
			tipoData = TipoData.EVENTO_PERIODO;			
		}
		return tipoData;		
	}
	
}