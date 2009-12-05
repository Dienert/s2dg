package org.ufpb.s2dg.session.beans;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.ufpb.s2dg.entity.Aluno;
import org.ufpb.s2dg.entity.EventoCalendarioTurma;
import org.ufpb.s2dg.session.Fachada;

@Name("eventoCalendarioTurmaBean")
@Scope(ScopeType.SESSION)
@AutoCreate
public class EventoCalendarioTurmaBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1158063557624286711L;

	private EventoCalendarioTurma eventoCalendarioTurma;
	@In
	private Fachada fachada;
	
	public EventoCalendarioTurma getEventoCalendarioTurma() {
		return eventoCalendarioTurma;
	}
	
	public void atribuirPresencaAluno(Aluno aluno, boolean presente){
		eventoCalendarioTurma.atribuirPresenca(aluno, presente);
		fachada.persisteEventoCalendarioTurma(eventoCalendarioTurma);
	}
	
	public void confirmarPlanejamentoDaAula(){
		eventoCalendarioTurma.setExecucao(eventoCalendarioTurma.getPlanejamento());
		fachada.persisteEventoCalendarioTurma(eventoCalendarioTurma);
	}
	
	public void adicionarExecucaoDaAula(String execucao){
		eventoCalendarioTurma.setExecucao(execucao);
		fachada.persisteEventoCalendarioTurma(eventoCalendarioTurma);
	}
	
	public void publicarPresencas(boolean publicar){
		eventoCalendarioTurma.setPresencaPublicada(publicar);
		fachada.persisteEventoCalendarioTurma(eventoCalendarioTurma);
	}
	
}
