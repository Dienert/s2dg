package org.ufpb.s2dg.session.beans;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;


@Name("TimestampBean")
@Scope(ScopeType.SESSION)
@AutoCreate
public class TimestampBean implements Serializable {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5787745374984405106L;

	public String getHour(){
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
	    return format.format(Calendar.getInstance().getTime()).toString();
	}
	
}
