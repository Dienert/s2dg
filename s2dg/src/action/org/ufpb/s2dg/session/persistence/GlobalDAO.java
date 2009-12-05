package org.ufpb.s2dg.session.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.ufpb.s2dg.entity.Centro;
import org.ufpb.s2dg.entity.Global;
import org.ufpb.s2dg.entity.Periodo;


@AutoCreate
@Name("globalDAO")
public class GlobalDAO {

	@In
	EntityManager entityManager;
	
	public GlobalDAO() {}
	
	public GlobalDAO(EntityManager em) {
		entityManager = em;
	}

	@SuppressWarnings("unchecked")
	public Map<Centro, Periodo> getRelacaoPeriodosAtuais() {
		Query query = entityManager.createQuery(
		"select global from Global global");
		List<Global> result = (List<Global>) query.getResultList();
		Map<Centro, Periodo> periodosAtuais = new HashMap<Centro, Periodo>();
		for (Global global: result){
			periodosAtuais.put(global.getCentro(), global.getPeriodoAtual());
		}
		return periodosAtuais;
	}

}
