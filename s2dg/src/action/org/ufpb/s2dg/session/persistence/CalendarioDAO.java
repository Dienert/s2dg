package org.ufpb.s2dg.session.persistence;

import java.util.ArrayList;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.ufpb.s2dg.entity.Calendario;
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
			.setParameter("periodo", periodo).getResultList();
		if (result.size() > 0)
			return result.get(0);
		return null;
	}

}
