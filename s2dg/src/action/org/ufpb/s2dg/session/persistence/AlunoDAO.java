package org.ufpb.s2dg.session.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.ufpb.s2dg.entity.Aluno;
import org.ufpb.s2dg.entity.AlunoTurma;

@AutoCreate
@Name("alunoDAO")
public class AlunoDAO {
	@In
	EntityManager entityManager;
	
	@Deprecated
	public List<AlunoTurma> getAlunos(String matricula) {
		Aluno aluno = (Aluno) entityManager.createQuery("select a from Aluno as a where a.matricula = :matricula")
		.setParameter("matricula", matricula).getSingleResult();
		
		Set<AlunoTurma> turmas = aluno.getAlunoTurmas();
		
		ArrayList<AlunoTurma> lista = new ArrayList<AlunoTurma>(turmas);
		
		return lista;
	}
	
	public List<AlunoTurma> getAlunos(long id) {
		Aluno aluno = (Aluno) entityManager.createQuery("select a from Aluno as a where a.id = :id")
		.setParameter("id", id).getSingleResult();
		
		Set<AlunoTurma> turmas = aluno.getAlunoTurmas();
		
		ArrayList<AlunoTurma> lista = new ArrayList<AlunoTurma>(turmas);
		
		return lista;
	}
	
	public void atualiza(Aluno aluno) {
		entityManager.merge(aluno);		
	}
	
}
