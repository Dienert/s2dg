package org.ufpb.s2dg.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "eventocalendario")
public class EventoCalendario implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3040333643404387685L;
	private long id;
	private Date data;
	private String evento;
	
	public EventoCalendario() {}
	
	public EventoCalendario(Date date, String event) {
		data = date;
		evento = event;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name = "id", unique = true, nullable = false)
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	@NotNull
	@Temporal(TemporalType.DATE)
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	@NotNull
	@Length(min=5, message="A descrição do evento deve ter no mínimo 5 caracteres")
	public String getEvento() {
		return evento;
	}
	
	public void setEvento(String evento) {
		this.evento = evento;
	}
}
