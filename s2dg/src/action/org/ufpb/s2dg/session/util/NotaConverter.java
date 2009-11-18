package org.ufpb.s2dg.session.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;

@Name("notaConverter")
@AutoCreate
public class NotaConverter implements Converter {

	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		try {
			int virgula = arg2.indexOf(',');
			Float nota;
			if(virgula != -1) {
				nota = new Float(Float.parseFloat(arg2.substring(0, virgula)));
				int ultimaPos = arg2.length()-1;
				if(virgula == ultimaPos);
				else if(virgula == ultimaPos-1) {
					float ultimoDigito = Float.parseFloat(arg2.substring(ultimaPos,ultimaPos+1));
					ultimoDigito *= 0.1;
					nota += ultimoDigito;
				}
				else if(virgula <= ultimaPos-2) {
					float ultimosDigitos = Float.parseFloat(arg2.substring(virgula+1,virgula+3));
					ultimosDigitos *= 0.01;
					nota += ultimosDigitos;
				}
			}
			else {
				nota = new Float(Float.parseFloat(arg2));
			}
			if(nota > 10.0)
				nota = 10.0f;
			else if(nota < 0.0)
				nota = 0.0f;
			return nota;
		}
		catch (NumberFormatException e) {
			return new Float(0.0f);
		}
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		float nota = (Float)arg2;
		if(nota < 0.0)
			nota = 0.0f;
		else if(nota > 10.0)
			nota = 10.0f;
		String nota_str = String.format("%.02f", nota);
		nota_str = nota_str.replace('.', ',');		
		return nota_str;
	}

}
