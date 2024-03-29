package org.ufpb.s2dg.session.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.ufpb.s2dg.entity.Avaliacao;
import org.ufpb.s2dg.entity.EventoCalendarioTurma;
import org.ufpb.s2dg.entity.Turma;

@AutoCreate
//@Stateless
@Name("dataEventoDAO")
public class DataEventoDAO {
	
	@In//(create = true)
	EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public ArrayList<EventoCalendarioTurma> getDataEvento(int mes, int ano) {
		try {
			return (ArrayList<EventoCalendarioTurma>) entityManager.createQuery(
					"select de from DataEvento as de where EXTRACT(MONTH FROM de.data) = :mes and " +
					"EXTRACT(YEAR FROM de.data) = :ano")
					.setParameter("mes", mes).setParameter("ano", ano).
					getResultList();
		}
		catch(NullPointerException npe) {
			npe.printStackTrace();
			return new ArrayList<EventoCalendarioTurma>();
		}
		
	}

	@SuppressWarnings("unchecked")
	public List<EventoCalendarioTurma> getDataEvento(Turma turma) {
		Query query = entityManager.createQuery("select de from DataEvento as de where de.turma = :turma")
			.setParameter("turma", turma);
		List<EventoCalendarioTurma> list = query.getResultList();
		if(list.size() > 0)
			return list;
		return null;
	}

	public void cria(EventoCalendarioTurma dataEvento) {
		entityManager.persist(dataEvento);
	}

	@SuppressWarnings("unchecked")
	public EventoCalendarioTurma getDataEvento(Avaliacao avaliacao) {
		Query query = entityManager.createQuery("select de from DataEvento as de where de.avaliacao = :avaliacao")
			.setParameter("avaliacao", avaliacao);
		List<EventoCalendarioTurma> list = query.getResultList();
		if(list.size() > 0)
			return list.get(0);
		return null;
	}

	public void atualiza(EventoCalendarioTurma dataEvento) {
		entityManager.merge(dataEvento);
	}

	public void exclui(EventoCalendarioTurma dataEvento) {
		dataEvento = entityManager.find(EventoCalendarioTurma.class, dataEvento.getId());
		entityManager.remove(dataEvento);
	}
}
