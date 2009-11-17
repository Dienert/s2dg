package org.ufpb.s2dg.session.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.ufpb.s2dg.entity.Curriculo;
import org.ufpb.s2dg.entity.Curso;

@AutoCreate
@Name("cursoDAO")
public class CursoDAO {

	@In
	EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public Curso getCurso(Curriculo curriculo) {
		Query query = entityManager.createQuery(
    		"select curso from Curso as curso where :curriculo member of curso.curriculos")
    		.setParameter("curriculo", curriculo);
		List<Curso> result = query.getResultList();
		if (result.size() > 0)
			return result.get(0);
		return null;
	}
	
}
