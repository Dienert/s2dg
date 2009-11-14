package org.ufpb.s2dg.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;


/**
 * EventoCalendarioTurma.java DEVERIA herdar de EventoCalendario
 * 
 * Representa um evento de calendário de uma turma. Foi mal ae, eu tentei usar heranca, mas o Seam nao deixou!
 */
@Entity
@Table(name = "eventocalendarioturma")
public class EventoCalendarioTurma /*extends EventoCalendario */implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private Date data;
	private String evento;
	private Set<Avaliacao> avaliacoes;
	
	public EventoCalendarioTurma() {}
	
	public EventoCalendarioTurma(Date date, String event) {
		setData(data);
		setEvento(evento);
	}

	@OneToMany(mappedBy="dataEvento")
	public Set<Avaliacao> getAvaliacoes() {
		return avaliacoes;
	}

	public void setAvaliacoes(Set<Avaliacao> avaliacoes) {
		this.avaliacoes = avaliacoes;
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
