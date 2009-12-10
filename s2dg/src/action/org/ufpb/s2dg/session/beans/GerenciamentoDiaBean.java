package org.ufpb.s2dg.session.beans;

import java.io.Serializable;

import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.ufpb.s2dg.entity.Avaliacao;
import org.ufpb.s2dg.entity.EventoCalendarioTurma;
import org.ufpb.s2dg.session.Fachada;
import org.ufpb.s2dg.session.util.ItemDeCalendario.TipoData;

@Name("gerenciaDia")
@Scope(ScopeType.PAGE)
public class GerenciamentoDiaBean implements Serializable {
	
	private static final long serialVersionUID = 6489975404995636320L;
	
	@In
    Fachada fachada;
	@In
	FacesContext facesContext;;
	
	private Object data;
	private int day;
	private TipoData tipo;
	
	public GerenciamentoDiaBean(){
		super();		
	}
	
	public void setDiaParaEdicao(Object data, int day, TipoData tipo) {
		this.data = data;
		this.day = day;
		this.tipo = tipo;
	}	

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public TipoData getTipo() {
		return tipo;
	}

	public void setTipo(TipoData tipo) {
		this.tipo = tipo;
	}
	
	
	
	
	
}
