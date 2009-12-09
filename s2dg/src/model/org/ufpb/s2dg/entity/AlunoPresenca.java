package org.ufpb.s2dg.entity;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "alunopresenca")
public class AlunoPresenca implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -117699208848997358L;
	private long id;
	private Aluno aluno;
	private boolean presenca;
	private EventoCalendarioTurma eventocalendarioturma;
	
	public AlunoPresenca() {}
	
	@Deprecated
	public AlunoPresenca(Aluno aluno, boolean presenca) {
		super();
		this.aluno = aluno;
		this.presenca = presenca;
	}

	public AlunoPresenca(Aluno aluno, boolean presenca, EventoCalendarioTurma eventoCalendarioTurma) {
		super();
		this.aluno = aluno;
		this.presenca = presenca;
		this.eventocalendarioturma = eventoCalendarioTurma;
	}
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy=IDENTITY)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	@ManyToOne(cascade=ALL, optional = true)
	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public boolean isPresenca() {
		return presenca;
	}

	public void setPresenca(boolean presenca) {
		this.presenca = presenca;
	}

	public void setEventocalendarioturma(EventoCalendarioTurma eventocalendarioturma) {
		this.eventocalendarioturma = eventocalendarioturma;
	}

	@ManyToOne(cascade=ALL, optional = false)
	@JoinColumn(name="eventocalendarioturma_id", referencedColumnName = "id")
	public EventoCalendarioTurma getEventocalendarioturma() {
		return eventocalendarioturma;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aluno == null) ? 0 : aluno.hashCode());
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
		AlunoPresenca other = (AlunoPresenca) obj;
		if (aluno == null) {
			if (other.aluno != null)
				return false;
		} else if (!aluno.equals(other.aluno))
			return false;
		return true;
	}	
	
	public String toString(){
		return String.format(
				"Aluno: %s\n" +
				"Presente? %s", 
				aluno.getMatricula(), presenca);
	}
}
