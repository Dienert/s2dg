package org.ufpb.s2dg.session.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.ufpb.s2dg.entity.Calendario;
import org.ufpb.s2dg.entity.Centro;
import org.ufpb.s2dg.entity.Periodo;

@AutoCreate
@Name("calendarioDAO")
public class CalendarioDAO {

	@In
	EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public Calendario getCalendario(Periodo periodo) {
		ArrayList<Calendario> result = 
			(ArrayList<Calendario>) entityManager.createQuery(
			"select cal from Calendario as cal where cal.periodo = :periodo")
			.setParameter("periodo", periodo)
			.getResultList();
		if (result.size() > 0)
			return result.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public Calendario getCalendario(Periodo periodoAtual, Centro centro) {

		ArrayList<Calendario> result = 
			(ArrayList<Calendario>) entityManager.createQuery(
			"select cal from Calendario as cal where cal.periodo = :periodo" +
			" and cal.centro = :centro")
			.setParameter("periodo", periodoAtual)
			.setParameter("centro", centro)
			.getResultList();
		if(result.size() > 0)
			return result.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Calendario> getCalendarios() {
		return (List<Calendario>) entityManager.createQuery(
				"select cal from Calendario as cal")
				.getResultList();
	}

}
