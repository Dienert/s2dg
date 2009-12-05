package org.ufpb.s2dg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;

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
	
	
	
	public AlunoPresenca(Aluno aluno, boolean presenca) {
		super();
		this.aluno = aluno;
		this.presenca = presenca;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
