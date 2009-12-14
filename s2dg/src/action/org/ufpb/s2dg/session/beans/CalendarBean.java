package org.ufpb.s2dg.session.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("calendarBean")
@Scope(ScopeType.SESSION)
@AutoCreate
public class CalendarBean implements Serializable {

	private static final long serialVersionUID = -5077367133202512506L;
	
	private Date selectedDate;
	
	public void init() {
		Calendar c = Calendar.getInstance();
		selectedDate = c.getTime();
	}

	public Date getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}

	
}
