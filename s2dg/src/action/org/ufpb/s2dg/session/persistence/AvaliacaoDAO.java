package org.ufpb.s2dg.session.persistence;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.ufpb.s2dg.entity.Avaliacao;
import org.ufpb.s2dg.entity.Turma;

@AutoCreate
@Name("avaliacaoDAO")
public class AvaliacaoDAO {

	@In
	EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Avaliacao> getAvaliacoes(Turma turma) {
		Query query = entityManager.createQuery(
    	"select avaliacao from Avaliacao as avaliacao where avaliacao.turma = :turma order by avaliacao.nome")
    	.setParameter("turma", turma);
		List<Avaliacao> list = (List<Avaliacao>)query.getResultList();
		if(list.size() > 0) {
			Collections.sort(list, new AvaliacaoComparator());
			return list;
		}
		return new LinkedList<Avaliacao>();
	}

	public void cria(Avaliacao avaliacao, Turma turma) {
		/*
		Avaliacao novaAvaliacao = new Avaliacao();
		novaAvaliacao.setNome(avaliacao.getNome());
		novaAvaliacao.setPeso(avaliacao.getPeso());
		novaAvaliacao.setTurma(turma);
		novaAvaliacao.setDataEvento(avaliacao.getDataEvento());
		*/
		avaliacao.setTurma(turma);
		entityManager.merge(avaliacao);
		//entityManager.flush();
	}
	
	public void publicaNotas(Avaliacao avaliacao, Turma turma) {
		if (avaliacao != null) {
			avaliacao.setPublicado(true);
			entityManager.merge(avaliacao);
			entityManager.flush();			
		}		
	}

	public void atualiza(Avaliacao avaliacao) {
		if(avaliacao != null) {
			entityManager.merge(avaliacao);
//			entityManager.flush();
		}
	}

	public void remove(Avaliacao avaliacao) {
		if(avaliacao != null) {
			Avaliacao avaliacaox = entityManager.find(Avaliacao.class,avaliacao.getId());
			avaliacaox.setTurma(avaliacao.getTurma());
			entityManager.remove(avaliacaox);
			entityManager.flush();
		}
	}

}
