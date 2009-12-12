package org.ufpb.s2dg.session.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.ufpb.s2dg.entity.Aluno;
import org.ufpb.s2dg.entity.AlunoPresenca;
import org.ufpb.s2dg.entity.AlunoTurma;
import org.ufpb.s2dg.entity.Avaliacao;
import org.ufpb.s2dg.entity.EventoCalendarioTurma;
import org.ufpb.s2dg.entity.Turma;
import org.ufpb.s2dg.session.Fachada;

@AutoCreate
@Name("eventoCalendarioTurmaDAO")
public class EventoCalendarioTurmaDAO {
	@In
	EntityManager entityManager;
	
	@In
	private Fachada fachada;
	
	private static final long ID_DO_ALUNO_FAKE = 13L;
	
	public void atualiza(EventoCalendarioTurma eventoCalendarioTurma){
		fazOperacaoCriticaParaPersistir(eventoCalendarioTurma);
		entityManager.merge(eventoCalendarioTurma);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<EventoCalendarioTurma> getDataEvento(int mes, int ano) {
		try {
			return (ArrayList<EventoCalendarioTurma>) entityManager.createQuery(
					"select de from EventoCalendarioTurma as de where EXTRACT(MONTH FROM de.data) = :mes and " +
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
	public List<EventoCalendarioTurma> getEventoCalendarioTurma(Turma turma) {
		List<EventoCalendarioTurma> eventosDaTurma = entityManager.createQuery("select de from EventoCalendarioTurma as de where de.turma = :turma")
		.setParameter("turma", turma).getResultList();
		
		for (EventoCalendarioTurma evento : eventosDaTurma) {
			List<AlunoPresenca> alunosPresenca = entityManager.createQuery("select ap from AlunoPresenca as ap where ap.eventocalendarioturma = :evento")
			.setParameter("evento", evento).getResultList();
			evento.setPresencas(alunosPresenca);
		}		
		
		return eventosDaTurma;
	}

	public void cria(EventoCalendarioTurma dataEvento) {
		entityManager.persist(dataEvento);
	}

	public void fazOperacaoCriticaParaPersistir(EventoCalendarioTurma eventoCalendarioTurma){
		if(eventoCalendarioTurma.getPresencas() == null || eventoCalendarioTurma.getPresencas().size() == 0){
			AlunoPresenca alunoPresenca = new AlunoPresenca();
			alunoPresenca.setEventocalendarioturma(eventoCalendarioTurma);
			//List<AlunoTurma> alunosTurma = fachada.getAlunos(ID_DO_ALUNO_FAKE);
			Aluno aluno = fachada.getAluno(ID_DO_ALUNO_FAKE);
			alunoPresenca.setAluno(aluno);
			eventoCalendarioTurma.getPresencas().add(alunoPresenca);
		}
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

	public void exclui(EventoCalendarioTurma dataEvento) {
		dataEvento = entityManager.find(EventoCalendarioTurma.class, dataEvento.getId());
		entityManager.remove(dataEvento);
	}
}
