package org.ufpb.s2dg.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "eventocalendario")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class EventoCalendario implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3040333643404387685L;
	public enum TipoData implements Serializable { DIA_LETIVO("Dia Letivo"), DIA_AULA_DISCIPLINA("Dia de Aula") ,  
		EVENTO_PERIODO("Evento de Período"), AVALIACAO("Avaliação"), FERIADO("Feriado"); 
		String string;
	
		TipoData(String string){
			this.string = string;
		}
	
		public String toString(){
			return string;
		}
	}
	
	private long id;
	private Date data;
	private TipoData tipoData;
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

	public TipoData getTipoData() {
		return tipoData;
	}

	public void setTipoData(TipoData tipoData) {
		this.tipoData = tipoData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventoCalendario other = (EventoCalendario) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}
	
}
