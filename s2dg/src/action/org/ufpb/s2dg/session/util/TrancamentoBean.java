package org.ufpb.s2dg.session.util;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.ufpb.s2dg.session.Fachada;

@Name("trancamentoBean")
@Scope(ScopeType.SESSION)
@AutoCreate
public class TrancamentoBean implements Serializable {

	/**
	 * 
	 */											
	private static final long serialVersionUID = -396395780804966001L;

	private boolean requiredBox = false;
	private boolean trancadoTotal = false;
	private boolean trancadoParcial = false;
	
	
	@In
	Fachada fachada;
	
	
	public boolean getTrancadoParcial() {
		return trancadoParcial;
	}


	public void setTrancadoParcial(boolean trancadoParcial) {
		this.trancadoParcial = trancadoParcial;
	}


	public boolean getTrancadoTotal() {
		return trancadoTotal;
	}


	public void setTrancadoTotal(boolean trancadoTotal) {
		this.trancadoTotal = trancadoTotal;
	}


	public void setRequiredBox(boolean requiredBox) {
		this.requiredBox = requiredBox;
	}

	
	public boolean getRequiredBox() {
		return requiredBox;
	}

	
	public boolean numeroMaximoDeTrancamentosTotaisAtingido(){

		if (fachada.getAluno().getTracamentosTotais() >= fachada.getAluno().getCurriculo().getMaximoTrancamentosTotais())
			return true;
		return false;
		
	}
	
}
