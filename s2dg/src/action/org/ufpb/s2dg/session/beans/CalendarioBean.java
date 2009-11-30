package org.ufpb.s2dg.session.beans;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.ufpb.s2dg.entity.Calendario;

@Name("calendarioBean")
@Scope(ScopeType.SESSION)
@AutoCreate
public class CalendarioBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1351222147190710878L;

	private Calendario calendarioAluno;
	
	private Calendario calendarioProfessor;
	
	public Calendario getCalendarioAluno() {
		return calendarioAluno;
	}

	public void setCalendarioAluno(Calendario calendarioAluno) {
		this.calendarioAluno = calendarioAluno;
	}

	public Calendario getCalendarioProfessor() {
		return calendarioProfessor;
	}

	public void setCalendarioProfessor(Calendario calendarioProfessor) {
		this.calendarioProfessor = calendarioProfessor;
	}
	
}
