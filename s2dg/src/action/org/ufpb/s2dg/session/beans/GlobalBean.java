package org.ufpb.s2dg.session.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.ufpb.s2dg.entity.AlunoTurma;
import org.ufpb.s2dg.entity.Calendario;
import org.ufpb.s2dg.entity.Centro;
import org.ufpb.s2dg.entity.Periodo;
import org.ufpb.s2dg.entity.AlunoTurma.Situacao;

@Name("globalBean")
@Scope(ScopeType.APPLICATION)
@AutoCreate
@SuppressWarnings("unused")
public class GlobalBean {

	@Logger
	private Log log;
	
	private Map<Centro, Periodo> periodosAtuais;
	
	@In
	private EntityManagerFactory entityManagerFactory;
	
	private EntityManager entityManager;
	
	private Thread checarDataLimiteImplantacaoNotas = new Thread(){
		public void run(){
			while(true){
				try {
					List<Calendario> calendarios = getCalendarios();
					for (Calendario c: calendarios) {
						if (atingiuDataLimiteImplantacao(c)) {	
							List<AlunoTurma> turmas = getAlunoTurmas(c.getPeriodo(), c.getCentro());
							for(AlunoTurma at: turmas){
								if (at.getSituacao() == Situacao.EM_CURSO)
									registraNotaZero(at);
							}
						}
					}
					sleep(10000); // Atualiza a cada dia - 86400000ms
				} catch (InterruptedException e) {
					log.warn("	Thread checarDataLimiteImplantacaoNotas.run():" +
							 "\n			Thread de atualização de turmas matriculadas" +
						   	 "\n			irregulares interrompida.");
				} catch (NullPointerException e) {
					e.printStackTrace();
					try {
						sleep(10000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					continue;
				}
			}
		}

		
		private void registraNotaZero(AlunoTurma atAberto) {
			atAberto.setMedia(0.0f);
			atAberto.setSituacao(Situacao.REPROVADO_POR_MEDIA);
			entityManager.merge(atAberto);
			log.info("registraNotaZero() \n" +
					 "  {0} reprovado por media em {1} (turma {2}) com zero. Omissao do professor.",
					 atAberto.getAluno().getMatricula(),
					 atAberto.getTurma().getDisciplina().getCodigo(),
					 atAberto.getTurma().getNumero());
		}
		
		private boolean atingiuDataLimiteImplantacao(Calendario c) {
			return c.getFimImplantacaoNotas().before(Calendar.getInstance().getTime());
			/* TODO Mostrar alteração - que refatoracao é essa?
			 * if(c.getFimImplantacaoNotas().before(Calendar.getInstance().getTime())){
			 * 	return true;
			 * return false;
			 */
		}
	};
	
	@Create
	public void init(){
		/*entityManager = entityManagerFactory.createEntityManager();
		checarDataLimiteImplantacaoNotas.start();*/
	}
	
	public GlobalBean(){
		super();
	}
	
	/*
	 * Exceção à arquitetura: Este Bean nao pode usar fachada. usando
	 * entity manager diretamente. isso é uma gambiarra documentada.
	 */
	
	public List<AlunoTurma> getAlunoTurmas(Periodo periodo, Centro centro) {

		return new ArrayList<AlunoTurma>();
		/*return (List<AlunoTurma>)
		((org.hibernate.ejb.EntityManagerImpl) entityManager).getSession()
		.createCriteria(AlunoTurma.class)
		.createCriteria(Turma.class)
		.createCriteria(Disciplina.class)
		.createCriteria()*/
		// TODO terminar agora
		
		/*return (List<AlunoTurma>)entityManager.createQuery(
				"select at from AlunoTurma as at "
						+ "where at.turma.disciplina.curriculos.curso.centro = :centro "
						+ "and at.turma.periodo = :periodo")
		.setParameter("centro", centro)
		.setParameter("periodo", periodo)
		.getResultList();*/
	}

	@SuppressWarnings("unchecked")
	public List<Calendario> getCalendarios() {
		return (List<Calendario>) entityManager.createQuery(
				"select cal from Calendario as cal")
				.getResultList();
	}

	public Periodo getPeriodoAtual(Centro centro) {
		return periodosAtuais.get(centro);
	}
	
	public List<Centro> getCentrosNoPeriodo(Periodo periodo){
		List<Centro> result = new ArrayList<Centro>();
		for(Entry<Centro, Periodo> registro: periodosAtuais.entrySet()){
			if(registro.getValue().equals(periodo))
				result.add(registro.getKey());
		}
		return result;
	}

	public void setPeriodosAtuais(Map<Centro, Periodo> globalDoBanco) {
		this.periodosAtuais = globalDoBanco;
	}

}
