package org.ufpb.s2dg.session.beans;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.ufpb.s2dg.entity.Calendario;
import org.ufpb.s2dg.session.Fachada;

@Name("calendarioBean")
@Scope(ScopeType.SESSION)
@AutoCreate
public class CalendarioBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1351222147190710878L;

	Calendario calendario;
	
	@In
	Fachada fachada;

	public Calendario getCalendario() {
		return calendario;
	}

	public void setCalendario(Calendario calendario) {
		this.calendario = calendario;
	}
	
}
