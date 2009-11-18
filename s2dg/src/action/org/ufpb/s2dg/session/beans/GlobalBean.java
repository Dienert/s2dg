package org.ufpb.s2dg.session.beans;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.ufpb.s2dg.entity.Global;

@Name("globalBean")
@Scope(ScopeType.APPLICATION)
@AutoCreate
public class GlobalBean {

	Global global;

	public Global getGlobal() {
		return global;
	}

	public void setGlobal(Global global) {
		this.global = global;
	}
	
}
