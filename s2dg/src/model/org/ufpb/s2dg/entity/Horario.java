package org.ufpb.s2dg.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "horario")
public class Horario implements Serializable {

	// TODO Se der pau na ordem, alterar de volta para domingo no final
	//public enum Dia { SEG, TER, QUA, QUI, SEX, SAB, DOM}
	public enum Dia {DOM, SEG, TER, QUA, QUI, SEX, SAB}
	private static final long serialVersionUID = 1L;
	private long id;
	private Dia dia;
	private int horaInicio;
	private int horaFim;
	private int minutoInicio;
	private int minutoFim;
	private Set<Turma> turmas;

	public Horario() {
	}

	public Horario(long id) {
		this.id = id;
	}
	
	public Horario(long id, Dia dia, int horaInicio, int horaFim,
			int minutoInicio, int minutoFim, Set<Turma> turmas) {
		this.id = id;
		this.dia = dia;
		this.horaInicio = horaInicio;
		this.horaFim = horaFim;
		this.turmas = turmas;
	}
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "dia")
	public Dia getDia() {
		return dia;
	}

	public void setDia(Dia dia) {
		this.dia = dia;
	}

	@Column(name = "hora_inicio")
	public int getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(int horaInicio) {
		this.horaInicio = horaInicio;
	}

	@Column(name = "hora_fim")
	public int getHoraFim() {
		return horaFim;
	}

	public void setHoraFim(int horaFim) {
		this.horaFim = horaFim;
	}

	@Column(name = "minuto_inicio")
	public int getMinutoInicio() {
		return minutoInicio;
	}

	public void setMinutoInicio(int minutoInicio) {
		this.minutoInicio = minutoInicio;
	}

	@Column(name = "minuto_fim")
	public int getMinutoFim() {
		return minutoFim;
	}

	public void setMinutoFim(int minutoFim) {
		this.minutoFim = minutoFim;
	}

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "turma_horario", schema = "public", joinColumns = { @JoinColumn(name = "id_horario", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "id_turma", nullable = false, updatable = false) })
	public Set<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(Set<Turma> turmas) {
		this.turmas = turmas;
	}

	public boolean temChoque(Horario outroHorario) {
		if(this.dia == outroHorario.getDia()) {
			if(this.horaFim > outroHorario.getHoraInicio()) {
				if(this.horaInicio < outroHorario.getHoraFim())
					return true;
			}
		}
		return false;
	}
	
	public String toString() {
		String dia = this.dia.name();
		String h1 = Integer.toString(this.horaInicio);
		if(h1.length() == 1)
			h1 = '0' + h1;
		String m1 = Integer.toString(this.minutoInicio);
		if(m1.length() == 1)
			m1 = '0' + m1;
		String h2 = Integer.toString(this.horaFim);
		if(h2.length() == 1)
			h2 = '0' + h2;
		String m2 = Integer.toString(this.minutoFim);
		if(m2.length() == 1)
			m2 = '0' + m2;
		return dia+", "+h1+":"+m1+" - "+h2+":"+m2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dia == null) ? 0 : dia.hashCode());
		result = prime * result + horaFim;
		result = prime * result + horaInicio;
		result = prime * result + minutoFim;
		result = prime * result + minutoInicio;
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
		Horario other = (Horario) obj;
		if (dia == null) {
			if (other.dia != null)
				return false;
		} else if (!dia.equals(other.dia))
			return false;
		if (horaFim != other.horaFim)
			return false;
		if (horaInicio != other.horaInicio)
			return false;
		if (minutoFim != other.minutoFim)
			return false;
		if (minutoInicio != other.minutoInicio)
			return false;
		return true;
	}
	
	public String horarioFormatado(){
		return String.format("%d:%d - %d:%d", horaInicio,
				minutoInicio, horaFim, minutoFim);
	}
	
}
