package org.ufpb.s2dg.session.util;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.richfaces.model.CalendarDataModelItem;
import org.ufpb.s2dg.session.util.ItemDeCalendario.TipoData;

@Name("calendarAlunoDataModelItem")
@AutoCreate
public class CalendarAlunoDataModelItemImpl implements CalendarDataModelItem {

    private Object data;
    private String styleClass;
    private Object toolTip;
    private int day;
    private boolean enabled = false;
    private TipoData tipo;                    

	public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Object getData() {
        return data;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public Object getToolTip() {
        return toolTip;
    }

    public boolean hasToolTip() {
        return getToolTip() != null;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public void setToolTip(Object toolTip) {
        this.toolTip = toolTip;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public TipoData getTipo() {
		return tipo;
	}

	public void setTipo(TipoData tipo) {
		this.tipo = tipo;
	}
    
}