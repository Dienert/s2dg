package org.ufpb.s2dg.session.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.ufpb.s2dg.entity.Aluno;
import org.ufpb.s2dg.entity.Calendario;
import org.ufpb.s2dg.entity.Centro;
import org.ufpb.s2dg.entity.EventoCalendarioTurma;
import org.ufpb.s2dg.entity.Horario;
import org.ufpb.s2dg.entity.Professor;
import org.ufpb.s2dg.entity.Turma;
import org.ufpb.s2dg.session.Fachada;
import org.ufpb.s2dg.session.util.MenuAction;
import org.ufpb.s2dg.session.util.ProfessorComparator;

@Name("turmaBean")
@Scope(ScopeType.SESSION)
@AutoCreate
public class TurmaBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Turma turma;
	//@Logger
	//private Log log;
	@In
	Fachada fachada;
	@In
	FacesContext facesContext;
	@In
	MenuAction MenuAction;
	@In
	TimestampBean TimestampBean;

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		MenuAction.setId_Menu(0);
		this.turma = turma;
		fachada.cancelarEdicaoDeAvaliacao();
		fachada.initAvaliacoes();
		fachada.initAlunoTurmas();
		try{
		carregaEventosCalendarioTurma();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void switchCalcMediaAuto() {
		if (turma.isCalcularMediaAutomaticamente())
			turma.setCalcularMediaAutomaticamente(false);
		else {
			turma.setCalcularMediaAutomaticamente(true);
			fachada.atualizaAlunoTurmas();
		}
	}
	
	public void atualiza() {
		fachada.atualizaTurma();
	}
	
	public void atualizaPlanoDeCurso() {
		atualiza();
		String time = TimestampBean.getHour();
		facesContext.addMessage("corpo2", new FacesMessage(FacesMessage.SEVERITY_INFO,time+" - Plano de curso salvo com sucesso.", null));
	}
	
	public List<Professor> listaProfessores() {
		if(turma != null) {
			Set<Professor> setProfessores = turma.getProfessores();
			if(setProfessores != null) {
				List<Professor> listProfessores = new ArrayList<Professor>(setProfessores);
				for(Professor professor: listProfessores) {
					professor.setUsuario(fachada.getUsuarioProfessor(professor.getMatricula()));
				}
				Collections.sort(listProfessores,new ProfessorComparator());
				return listProfessores;
			}
		}
		return null;
	}
	
	public Date getDataAtual() {  
		Calendar calendar = new GregorianCalendar();  
		Date date = new Date();  
		calendar.setTime(date);  
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public boolean podeFinalizarTurma() {
		return true;
	/*	Calendario calendario = fachada.getCalendarioDoBanco();
		if(calendario != null) {
			Date inicioImplantacao = calendario.getFimPeriodo();
			inicioImplantacao.setDate(inicioImplantacao.getDate()+1);
			Date fimImplantacao = calendario.getFimImplantacaoNotas();
			Date hoje = getDataAtual();
			if((hoje.compareTo(inicioImplantacao) >= 0)&&(hoje.compareTo(fimImplantacao) <= 0))
				return true;
			else
				return false;
		}
    	return false;
    	*/
	}
	
	public EventoCalendarioTurma getEventoCalendarioTurma(long idEventoCalendarioTurma){
		Iterator<EventoCalendarioTurma> eventosCalendarioTurma = turma.getEventosCalendarioTurma().iterator();
		
		while(eventosCalendarioTurma.hasNext()){
			EventoCalendarioTurma eventoCalendarioTurma = eventosCalendarioTurma.next();
			if(eventoCalendarioTurma.getId() == idEventoCalendarioTurma)
				return eventoCalendarioTurma;
		}
		
		return null;
	}
	
	public EventoCalendarioTurma getEventoCalendarioTurma(Date dataEventoCalendarioTurma){
		Iterator<EventoCalendarioTurma> eventosCalendarioTurma = turma.getEventosCalendarioTurma().iterator();
		
		while(eventosCalendarioTurma.hasNext()){
			EventoCalendarioTurma eventoCalendarioTurma = eventosCalendarioTurma.next();
			if(eventoCalendarioTurma.getData().equals(dataEventoCalendarioTurma))
				return eventoCalendarioTurma;
		}
		
		return null;
	}
	
	public void confirmarPlanejamentoEventoCalendarioTurma(long idEventoCalendarioTurma){
		EventoCalendarioTurma eventoCalendarioTurma = getEventoCalendarioTurma(idEventoCalendarioTurma);
		
		if(eventoCalendarioTurma != null)
			eventoCalendarioTurma.setPlanejamento(eventoCalendarioTurma.getPlanejamento());
	}
	
	public void confirmarPlanejamentoEventoCalendarioTurma(Date dataEventoCalendarioTurma) {
		try {
			EventoCalendarioTurma eventoCalendarioTurma = getEventoCalendarioTurma(dataEventoCalendarioTurma);
			eventoCalendarioTurma.setPlanejamento(eventoCalendarioTurma.getPlanejamento());
		} catch (NullPointerException e) {
			//log.error(String.format("publicarPresencaEventoCalendarioTurma() : EventoCalendarioTurma %s nao encontrado.", 
				//	dataEventoCalendarioTurma.toString()));
		}
	}
	
	public void setPlanejamentoCalendarioTurmaTurma(long idEventoCalendarioTurma, String planejamentoExecutado){
		EventoCalendarioTurma eventoCalendarioTurma = getEventoCalendarioTurma(idEventoCalendarioTurma);
		
		if(eventoCalendarioTurma != null)
			eventoCalendarioTurma.setPlanejamento(planejamentoExecutado);
	}
	
	public void setPlanejamentoCalendarioTurmaTurma(Date dataEventoCalendarioTurma,
			String planejamentoExecutado) {
		try{
			getEventoCalendarioTurma(dataEventoCalendarioTurma).setPlanejamento(planejamentoExecutado);
		} catch (NullPointerException e) {
			//log.error(String.format("setPlanejamentoCalendarioTurmaTurma() : EventoCalendarioTurma %s nao encontrado.", 
				//	dataEventoCalendarioTurma.toString()));
		}
	}
	
	public List<String> exportarPlanejamentoAulas(){
		ArrayList<String> planejamentos = new ArrayList<String>();
		Set<EventoCalendarioTurma> eventosCalendarioTurma = turma.getEventosCalendarioTurma();
		
		for(EventoCalendarioTurma eventoCalendarioTurma : eventosCalendarioTurma)
			planejamentos.add(eventoCalendarioTurma.getPlanejamento());
		
		return planejamentos;
	}
	
	public void importartPlanejamentoAulas(List<String> planejamentos){
		Iterator<EventoCalendarioTurma> eventosCalendarioTurma = turma.getEventosCalendarioTurma().iterator();
		
		for(int i = 0; i < planejamentos.size() && eventosCalendarioTurma.hasNext(); i++)
			eventosCalendarioTurma.next().setPlanejamento(planejamentos.get(i));
	}
	
	public void atribuirPresencaAlunoEventoCalendarioTurma(long idEventoCalendarioTurma, Aluno aluno, boolean presenca){
		try{
			getEventoCalendarioTurma(idEventoCalendarioTurma)
			.atribuirPresenca(aluno, presenca);
		} catch (NullPointerException e) {
		//	log.error(String.format("atribuirPresencaAlunoEventoCalendarioTurma() : EventoCalendarioTurma %l nao encontrado.", 
			//		idEventoCalendarioTurma));
		}
	}
	
	public void atribuirPresencaAlunoEventoCalendarioTurma(Date dataEventoCalendarioTurma, Aluno aluno,
			boolean presenca) {
		try{
			getEventoCalendarioTurma(dataEventoCalendarioTurma).atribuirPresenca(aluno, presenca);
		} catch (NullPointerException e) {
			//log.error(String.format("atribuirPresencaAlunoEventoCalendarioTurma() : EventoCalendarioTurma %s nao encontrado.", 
				//	dataEventoCalendarioTurma.toString()));
		}
	}
	
	public void publicarPresencaEventoCalendarioTurma(long idEventoCalendarioTurma, boolean publicarPresenca){
		try {
			getEventoCalendarioTurma(idEventoCalendarioTurma)
					.setPresencaPublicada(publicarPresenca);
		} catch (NullPointerException e) {
			//log.error(String.format("publicarPresencaEventoCalendarioTurma() : EventoCalendarioTurma %l nao encontrado.", 
				//	idEventoCalendarioTurma));
		}
	}

	public void publicarPresencaEventoCalendarioTurma(Date dataEventoCalendarioTurma, boolean publicarPresenca){
		try {
			getEventoCalendarioTurma(dataEventoCalendarioTurma)
					.setPresencaPublicada(publicarPresenca);
		} catch (NullPointerException e) {
			//log.error(String.format("publicarPresencaEventoCalendarioTurma() : EventoCalendarioTurma %l nao encontrado.", 
				//	dataEventoCalendarioTurma.toString()));
		}
	}	

	private void carregaEventosCalendarioTurma(){
		if(turma.getEventosCalendarioTurma() == null || turma.getEventosCalendarioTurma().size() == 0){
			// TODO Elenilson: ao atualizar getPeriodoAtual(), atualizar aqui!
			Centro centro = turma.getDisciplina().getDepartamento().getCentro();
			Calendario calendario = fachada.getCalendarioDoBanco(fachada.getPeriodoAtual(centro), centro);
			Set<EventoCalendarioTurma> eventosCalendarioTurma = new HashSet<EventoCalendarioTurma>();
			
			Calendar c = Calendar.getInstance();
			
			//Para extrair o ultimo dia de periodo + 1
			c.setTime(calendario.getFimPeriodo());
			c.roll(Calendar.DAY_OF_MONTH, 1);
			Date fimDePeriodo = c.getTime();
			
			//Setando para o inicio do periodo
			c.setTime(calendario.getInicioPeriodo());
			Date dataAtual = c.getTime();
			
			Set<Horario> horarios = turma.getHorarios();
			
			while(dataAtual.compareTo(fimDePeriodo) < 0){
				
				//Verifica se o dia atual e' um dia correspondente a um dia de aula e adiciona na lista
				for(Horario horario : horarios)
					if(c.get(Calendar.DAY_OF_WEEK) == horario.parseDiaDaSemanaFormatoCalendar()){
						eventosCalendarioTurma.add(new EventoCalendarioTurma(dataAtual));
						break;
					}
					
				c.roll(Calendar.DAY_OF_YEAR, 1);
				if(c.get(Calendar.DAY_OF_YEAR) == 1)
					c.roll(Calendar.YEAR, 1);
				dataAtual = c.getTime();
			}
			
			turma.setEventosCalendarioTurma(eventosCalendarioTurma);
			fachada.persiteTurma(turma);
		}
	}

	public boolean isDiaDeAula(Date data){
		Iterator<EventoCalendarioTurma> diasDeAula = turma.getEventosCalendarioTurma().iterator();
		
		while(diasDeAula.hasNext())
			if(diasDeAula.next().getData().equals(data))
				return true;
		
		return false;
	}


}
