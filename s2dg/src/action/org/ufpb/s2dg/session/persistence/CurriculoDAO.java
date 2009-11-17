package org.ufpb.s2dg.session.persistence;

import java.util.ArrayList;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.ufpb.s2dg.entity.Aluno;
import org.ufpb.s2dg.entity.Curriculo;

@Name("curriculoDAO")
@AutoCreate
public class CurriculoDAO {

	@In
	EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public Curriculo getCurriculo(Aluno aluno) {
		Query query = entityManager.createQuery(
			"select curriculo from Curriculo as curriculo where :aluno member of curriculo.alunos")
			.setParameter("aluno", aluno);
		ArrayList<Curriculo> result = (ArrayList<Curriculo>)query.getResultList();
		if(result.size() > 0)
			return result.get(0);
		return null;
	}
	
}
