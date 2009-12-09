package org.ufpb.s2dg.session.persistence;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.ufpb.s2dg.entity.Periodo;

@AutoCreate
@Name("periodoDAO")
public class PeriodoDAO {

	@In
	EntityManager entityManager;

	public Periodo getPeriodo(char semestre, String ano) {
		return (Periodo) entityManager.createQuery("select a from Periodo as a where a.semestre = :semestre and a.ano = :ano")
		.setParameter("semestre", semestre)
		.setParameter("ano", ano)
		.getSingleResult();
	}
}
