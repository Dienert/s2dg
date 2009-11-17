package org.ufpb.s2dg.session.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.ufpb.s2dg.entity.AlunoTurma;
import org.ufpb.s2dg.entity.AlunoTurmaAvaliacao;
import org.ufpb.s2dg.entity.Avaliacao;

@AutoCreate
@Name("alunoTurmaAvaliacaoDAO")
public class AlunoTurmaAvaliacaoDAO {

	@In
	EntityManager entityManager;

	public AlunoTurmaAvaliacao getAlunoTurmaAvaliacao(AlunoTurma alunoTurma, Avaliacao avaliacao) {
		try {
			if (alunoTurma != null) {
				Object result = entityManager.createQuery(
				"select alunoTurmaAvaliacao from AlunoTurmaAvaliacao as alunoTurmaAvaliacao where alunoTurmaAvaliacao.alunoTurma =:alunoTurma and alunoTurmaAvaliacao.avaliacao =:avaliacao")
				.setParameter("alunoTurma", alunoTurma)
				.setParameter("avaliacao", avaliacao)
				.getSingleResult();
				return (AlunoTurmaAvaliacao) result; 
			} else return null;
		} catch(NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<AlunoTurmaAvaliacao> getAlunoTurmaAvaliacao(Avaliacao avaliacao) {
		try {
			if (avaliacao != null) {
				Avaliacao avaliacaox = entityManager.find(Avaliacao.class, avaliacao.getId());
				avaliacaox.setTurma(avaliacao.getTurma());
				if(avaliacaox != null){
					Object result = entityManager.createQuery(
					"select alunoTurmaAvaliacao from AlunoTurmaAvaliacao as alunoTurmaAvaliacao where alunoTurmaAvaliacao.avaliacao =:avaliacao")
					.setParameter("avaliacao", avaliacaox)
					.getResultList();
					return (List<AlunoTurmaAvaliacao>) result;
				}
				 
			} else return null;
		} catch(NoResultException e) {
			return null;
		}
		return null;
	}

	public AlunoTurmaAvaliacao cria(AlunoTurma alunoTurma, Avaliacao avaliacao) {
		AlunoTurmaAvaliacao novoAlunoTurmaAvaliacao = new AlunoTurmaAvaliacao();
		novoAlunoTurmaAvaliacao.setAlunoTurma(alunoTurma);
		novoAlunoTurmaAvaliacao.setAvaliacao(avaliacao);
		novoAlunoTurmaAvaliacao.setNota(0);
		entityManager.persist(novoAlunoTurmaAvaliacao);
		entityManager.flush();
		return novoAlunoTurmaAvaliacao;
	}

	public void atualiza(AlunoTurmaAvaliacao alunoTurmaAvaliacao) {
		entityManager.merge(alunoTurmaAvaliacao);
		entityManager.flush();
	}

	public void remove(AlunoTurmaAvaliacao ata) {
		if(ata != null) {
			ata = entityManager.find(AlunoTurmaAvaliacao.class,ata.getId());
			entityManager.remove(ata);
			entityManager.flush();
		}
	}
	
}
