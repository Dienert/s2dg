package org.ufpb.s2dg.session.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.ufpb.s2dg.entity.Aluno;
import org.ufpb.s2dg.entity.AlunoTurma;
import org.ufpb.s2dg.entity.Disciplina;
import org.ufpb.s2dg.entity.Periodo;
import org.ufpb.s2dg.entity.Turma;
import org.ufpb.s2dg.session.Fachada;

@AutoCreate
@Name("alunoTurmaDAO")
public class AlunoTurmaDAO {

	@In
	Fachada fachada;
	@In
	EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<AlunoTurma> getAlunoTurmas(Aluno aluno, Periodo periodo) {
		return (List<AlunoTurma>) entityManager.createQuery(
    	"select alunoTurma from AlunoTurma as alunoTurma where alunoTurma.aluno = :aluno and alunoTurma.turma.periodo = :periodo order by alunoTurma.turma.disciplina.nome")
    	.setParameter("aluno", aluno)
    	.setParameter("periodo",periodo)
    	.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<AlunoTurma> getAlunoTurmas(Turma turma) {
		return (List<AlunoTurma>) entityManager.createQuery(
    	"Select at from AlunoTurma as at where at.turma = :turma")
    	.setParameter("turma", turma)
    	.getResultList();
	}


	
	public void atualiza(AlunoTurma alunoTurma) {
		
		Integer maxFaltas = Math.round(((fachada.getTurma().getDisciplina().getCreditos())*10/8)+new Float(0.4));
		if (alunoTurma.getFaltas() > maxFaltas){
			alunoTurma.setMedia(new Float(0));
		}
		entityManager.merge(alunoTurma);
		
	}

	public void cria(AlunoTurma alunoTurma) {
		AlunoTurma alunoTurma_ = entityManager.find(AlunoTurma.class, alunoTurma.getId());
		if(alunoTurma_ != null)
			entityManager.merge(alunoTurma);
		else
			entityManager.persist(alunoTurma);
	}

	@SuppressWarnings("unchecked")
	public List<AlunoTurma> getAlunoTurmas(Aluno aluno) {
		Query q = entityManager.createQuery("Select at from AlunoTurma as at where at.aluno = :aluno")
		.setParameter("aluno", aluno);
		List<AlunoTurma> list = q.getResultList();
		if(list.size() > 0)
			return list;
		return null;
	}
	
	public void atualizaSituacaoTrancamento(AlunoTurma at) {
		AlunoTurma alunoTurma = entityManager.find(AlunoTurma.class, at.getId());
		alunoTurma.setSituacao(at.getSituacao());
		if (alunoTurma != null) {
			System.out.print("Pegou geral na entrada do PERSIST. Situacao alunoturma ="+alunoTurma.getSituacao()+"sistuacao at = "+at.getSituacao());
			entityManager.persist(alunoTurma);
			System.out.print("Pegou geral na saida do persist. SItuacao ="+alunoTurma.getSituacao());
			System.out.println("Atualizou");
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<AlunoTurma> getAlunoTurmas(Aluno aluno, Disciplina disciplina) {
		List<AlunoTurma> relacaoDeDisciplinaPorAluno = new ArrayList<AlunoTurma>();
		if(aluno != null && disciplina != null){
			List<AlunoTurma> turmasDoAluno = (List<AlunoTurma>)entityManager.createQuery("from AlunoTurma as at where at.aluno = :aluno").setParameter("aluno", aluno).getResultList();
			if (turmasDoAluno != null){
				for(AlunoTurma at: turmasDoAluno){
					if(at.getTurma().getDisciplina() == disciplina)
						relacaoDeDisciplinaPorAluno.add(at);
				}
				System.out.println("Relacao de alunos: "+relacaoDeDisciplinaPorAluno);
				return relacaoDeDisciplinaPorAluno;
			}
		}
		return null;
	}
	
}
