package org.ufpb.s2dg.session;

import java.io.Serializable;

import org.richfaces.model.CalendarDataModelItem;

public class ItemDeCalendario implements CalendarDataModelItem, Serializable {
	private Object data;
	private int day;
	private TipoData tipo;

	private static final long serialVersionUID = 1L;
	public enum TipoData implements Serializable { DIA_LETIVO, DIA_AULA_DISCIPLINA ,  EVENTO_PERIODO, AVALIACAO, FERIADO }
	
	public ItemDeCalendario(int dia) {
		data = "";
		day = dia;
	}
	
	public ItemDeCalendario(String eventos, int dia) {
		data = eventos;
		day = dia;
	}
	
	public ItemDeCalendario(String eventos, int dia, TipoData tipo) {
		data = eventos;
		day = dia;
		this.tipo = tipo;
	}
	
	public Object getData() {
		// TODO Auto-generated method stub
		return data;
	}
	
	public void setData(String informacao) {
		data = informacao; 
	}

	public int getDay() {
		// TODO Auto-generated method stub
		return day;
	}
		
	public void setDay(int x) {
		day = x;
	}

	public String getStyleClass() {
		if (tipo == null) return null;
		return "ic_" + tipo.toString().toLowerCase();
	}

	public Object getToolTip() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasToolTip() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setTipo(TipoData tipo){
		this.tipo = tipo;
	}
	
	public TipoData getTipo() {
		return tipo;
	}
}
