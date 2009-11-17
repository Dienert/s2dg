package org.ufpb.s2dg.session.persistence;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.ufpb.s2dg.entity.Curriculo;
import org.ufpb.s2dg.entity.Disciplina;
import org.ufpb.s2dg.entity.Turma;

@Name("disciplinaDAO")
@AutoCreate
public class DisciplinaDAO {

	@In
	EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Disciplina> getDisciplinas(Curriculo curriculo) {
		if(curriculo != null) {
			Query q = entityManager.createQuery("select d from Disciplina as d where :curriculo member of d.curriculos order by d.nome")
			.setParameter("curriculo", curriculo);
			List<Disciplina> list = q.getResultList();
			if(list.size() > 0)
				return list;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Disciplina getDisciplinas(Turma t) {
		Query q = entityManager.createQuery("select d from Disciplina as d where :turma member of d.turmas")
		.setParameter("turma", t);
		List<Disciplina> list = q.getResultList();
		if(list.size() > 0)
			return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public Set<Disciplina> getPreRequisitos(Curriculo c, Disciplina d) {
		Query q = entityManager.createQuery("select pr.dependencia from PreRequesito as pr where pr.curriculo = :curriculo and pr.disciplina = :disciplina")
		.setParameter("curriculo", c)
		.setParameter("disciplina", d);
		Set<Disciplina> set = new HashSet<Disciplina>();
		set.addAll(q.getResultList());
		if(set.size() > 0)
			return set;
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Disciplina> getCoRequisitos(Curriculo c, Disciplina d) {
		Query q = entityManager.createQuery("select co.disciplina2 from CoRequesito as co where co.curriculo = :curriculo and co.disciplina1 = :disciplina")
		.setParameter("curriculo", c)
		.setParameter("disciplina", d);
		List<Disciplina> list = q.getResultList();
		q = entityManager.createQuery("select co.disciplina1 from CoRequesito as co where co.curriculo = :curriculo and co.disciplina2 = :disciplina")
		.setParameter("curriculo", c)
		.setParameter("disciplina", d);		
		list.addAll(q.getResultList());
		if(list.size() > 0)
			return list;
		return null;
	}
	
}
