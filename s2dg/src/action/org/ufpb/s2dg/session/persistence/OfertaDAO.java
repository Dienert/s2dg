package org.ufpb.s2dg.session.persistence;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.ufpb.s2dg.entity.Curso;
import org.ufpb.s2dg.entity.Oferta;
import org.ufpb.s2dg.entity.Turma;

@AutoCreate
@Name("ofertaDAO")
public class OfertaDAO {

	@In
	EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public Oferta getOferta(Curso curso, Turma turma) {
		Query query = entityManager.createQuery(
		"select oferta from Oferta as oferta where oferta.curso = :curso and oferta.turma = :turma")
		.setParameter("curso", curso)
		.setParameter("turma", turma);
		ArrayList<Oferta> result = (ArrayList<Oferta>) query.getResultList();
		if (result.size() > 0)
			return result.get(0);
		return null;
	}

	public void atualiza(Oferta oferta) {
		entityManager.merge(oferta);
	}
	
}
