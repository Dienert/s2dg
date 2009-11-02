package org.ufpb.s2dg.session.persistence;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.ufpb.s2dg.entity.Global;


@AutoCreate
@Name("globalDAO")
public class GlobalDAO {

	@In
	EntityManager entityManager;
	
	public GlobalDAO() {}
	
	public GlobalDAO(EntityManager em) {
		entityManager = em;
	}
	
	public Global getGlobal() {
		Query query = entityManager.createQuery(
    		"select global from Global global");
		ArrayList<Global> result = (ArrayList<Global>) query.getResultList();
		if (result.size() > 0)
			return result.get(0);
		return null;
		

	}

}
