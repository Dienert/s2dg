package org.ufpb.s2dg.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "eventocalendarioperiodo")
public class EventoCalendarioPeriodo extends EventoCalendario implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5454532553653097257L;
	
	public EventoCalendarioPeriodo() {
		// TODO Auto-generated constructor stub
	}
	
	public EventoCalendarioPeriodo(Date date) {
		super(date, EventoCalendario.TipoData.EVENTO_PERIODO.toString());
	}	
	
}
