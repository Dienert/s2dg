package org.ufpb.s2dg.session.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.event.ValueChangeEvent;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.richfaces.model.CalendarDataModel;
import org.richfaces.model.CalendarDataModelItem;
import org.ufpb.s2dg.entity.Avaliacao;
import org.ufpb.s2dg.entity.Calendario;
import org.ufpb.s2dg.entity.EventoCalendarioTurma;
import org.ufpb.s2dg.entity.Horario;
import org.ufpb.s2dg.entity.Turma;
import org.ufpb.s2dg.session.Fachada;
import org.ufpb.s2dg.session.util.CalendarDataModelItemImpl.TipoData;


@Name("calendarDataModel")
@AutoCreate
public class CalendarDataModelImpl implements CalendarDataModel {

	@In
	Fachada fachada;

	private CalendarDataModelItem[] items;

	private String currentDescription;
	private String currentShortDescription;
	private Date currentDate;
	private boolean currentDisabled;    

	public CalendarDataModelItem[] getData(Date[] dateArray) {
		if (dateArray == null) {
			return null;
		}
		if (items==null) {  
			items = new CalendarDataModelItem[dateArray.length];
			for (int i = 0; i < dateArray.length; i++) {
				items[i] = createDataModelItem(dateArray[i]);
			}
		}
		return items;
	}

	private void datasComAula(Date[] datas) {
		
		
		
	}

	protected CalendarDataModelItem createDataModelItem(Date date) {
		Calendario calendario = fachada.getCalendarioAluno();    	    	
		Calendar c = Calendar.getInstance();
		CalendarDataModelItemImpl item = new CalendarDataModelItemImpl();
		ArrayList<EventoCalendarioTurma> datasEEventos = new ArrayList<EventoCalendarioTurma>();
		List<Avaliacao> avaliacoes = fachada.getAvaliacoes();
		Map data = new HashMap();
		

		if(avaliacoes != null) {
			for(Avaliacao a : avaliacoes) {
				EventoCalendarioTurma de = a.getDataEvento(); 
				if(de != null)
					datasEEventos.add(de);
			}
		}

		String eventos = "";
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
		data.put("shortDescription", eventos);

		if(datasEEventos != null) {
			for (int j = 0; j < datasEEventos.size(); j++) {
				if (date.equals(datasEEventos.get(j).getData())) {
					eventos += datasEEventos.remove(j--).getEvento() + "\n";
					tipoData = TipoData.AVALIACAO;
				}
			}
		}
		
		if (eventos.equals("")) {			
			item = new CalendarDataModelItemImpl();
			item.setDay(c.get(Calendar.DAY_OF_MONTH));			
		}
		else {
			item = new CalendarDataModelItemImpl();
			item.setData(eventos);
			item.setDay(c.get(Calendar.DAY_OF_MONTH));
			item.setToolTip(tipoData);
			eventos = "";
			tipoData = null;
		}
		/*
		data.put("shortDescription", "Nothing planned");
		data.put("description", "");
		
		


		//Map data = new HashMap();
		data.put("shortDescription", "Nothing planned");
		data.put("description", "");
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		item.setDay(c.get(Calendar.DAY_OF_MONTH));
		item.setEnabled(true);
		item.setStyleClass("rel-hol");
		item.setData(data); */
		return item;
	} 

	/* (non-Javadoc)
	 * @see org.richfaces.model.CalendarDataModel#getToolTip(java.util.Date)
	 */
	public Object getToolTip(Date date) {

		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return items
	 */
	public CalendarDataModelItem[] getItems() {
		return items;
	}

	/**
	 * @param setter for items
	 */
	public void setItems(CalendarDataModelItem[] items) {
		this.items = items;
	}

	/**
	 * @param valueChangeEvent handling
	 */
	public void valueChanged(ValueChangeEvent event) {
		System.out.println(event.getNewValue()+"selected");
		setCurrentDate((Date)event.getNewValue());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getCurrentDate());
		setCurrentDescription((String)((HashMap)items[calendar.get(Calendar.DAY_OF_MONTH)-1].getData()).get("description"));
		setCurrentShortDescription((String)((HashMap)items[calendar.get(Calendar.DAY_OF_MONTH)-1].getData()).get("shortDescription"));
	}

	/**
	 * Storing changes action
	 */
	public void storeDayDetails() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getCurrentDate());
		((HashMap)items[calendar.get(Calendar.DAY_OF_MONTH)-1].getData()).put("shortDescription", getCurrentShortDescription());
		((HashMap)items[calendar.get(Calendar.DAY_OF_MONTH)-1].getData()).put("description", getCurrentDescription());
	}

	/**
	 * @return currentDescription
	 */
	public String getCurrentDescription() {
		return currentDescription;
	}

	/**
	 * @param currentDescription
	 */
	public void setCurrentDescription(String currentDescription) {
		this.currentDescription = currentDescription;
	}

	/**
	 * @return currentDisabled
	 */
	public boolean isCurrentDisabled() {
		return currentDisabled;
	}

	/**
	 * @param currentDisabled
	 */
	public void setCurrentDisabled(boolean currentDisabled) {
		this.currentDisabled = currentDisabled;
	}

	/**
	 * @return currentShortDescription
	 */
	public String getCurrentShortDescription() {
		return currentShortDescription;
	}

	/**
	 * @param currentShortDescription
	 */
	public void setCurrentShortDescription(String currentShortDescription) {
		this.currentShortDescription = currentShortDescription;
	}

	/**
	 * @return currentDate
	 */
	public Date getCurrentDate() {
		return currentDate;
	}

	/**
	 * @param currentDate
	 */
	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public boolean equals(Date d1, Date d2){
		long dif = d1.getTime() - d2.getTime();
		return dif >= 0 && dif < 24*60*60*1000;
	}

}