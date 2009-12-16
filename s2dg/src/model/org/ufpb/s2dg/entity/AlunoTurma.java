package org.ufpb.s2dg.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;

@Entity
@Table(name = "aluno_turma")
public class AlunoTurma implements Serializable {

	public enum Situacao {APROVADO, DISPENSADO, TRANCADO, REPROVADO_POR_FALTA, REPROVADO_POR_MEDIA, EM_CURSO};
	
	private static final long serialVersionUID = 1L;
	private long id;
	private Turma turma;
	private Aluno aluno;
	private Float media;
	private Integer faltas;
	private Situacao situacao;

	public AlunoTurma() {
		//situacao = Situacao.EM_CURSO;
	}

	public AlunoTurma(long id, Turma turma, Aluno aluno, Situacao situacao) {
		this.id = id;
		this.turma = turma;
		this.aluno = aluno;
		this.situacao = situacao;
	}

	public AlunoTurma(long id, Turma turma, Aluno aluno, Float media,
			Integer frequencia, Situacao situacao) {
		this.id = id;
		this.turma = turma;
		this.aluno = aluno;
		this.media = media;
		this.faltas = frequencia;
		this.situacao = situacao;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name = "id", unique = true, nullable = false)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_turma", nullable = false)
	@NotNull
	public Turma getTurma() {
		return this.turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_aluno", nullable = false)
	@NotNull
	public Aluno getAluno() {
		return this.aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	@Column(name = "media", precision = 8, scale = 8)
	public Float getMedia() {
		return this.media;
	}

	@Deprecated
	public void setMedia(Float media) {
		this.media = media;
	}

	@Deprecated
	@Column(name = "faltas")
	public Integer getFaltas() {
		return this.faltas;
	}

	public void setFaltas(Integer faltas) {
		this.faltas = faltas;
	}

	@Column(name = "situacao")
	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}
	
	public int numeroDeFaltas(){
		int faltas = 0;
		List<EventoCalendarioTurma> eventosCalendarioTurma = turma.getEventosCalendarioTurma();
		
		//Busca os eventos calendario da turma
		for(EventoCalendarioTurma eventoCalendarioTurma : eventosCalendarioTurma){
			//Pega a lista de presencas
			List<AlunoPresenca> alunosPresenca = eventoCalendarioTurma.getPresencas();
			
			//Itera na lista de presencas
			for(AlunoPresenca alunoPresenca : alunosPresenca)
				//Encontra o aluno
				if(alunoPresenca.getAluno().equals(alunoPresenca)){ 
					//Se o aluno nao estava presente incrementa as faltas e da o break no for de AlunoPresenca
					if(!alunoPresenca.isPresenca())
						faltas++;
					break;
				}
						
		}
		
		return faltas;
	}

}
