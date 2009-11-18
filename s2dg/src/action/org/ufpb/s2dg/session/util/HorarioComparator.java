package org.ufpb.s2dg.session.util;

import java.util.Comparator;

import org.ufpb.s2dg.entity.Horario;

public class HorarioComparator implements Comparator<Horario> {

	public int compare(Horario h1, Horario h2) {
		int result = h1.getDia().ordinal()-h2.getDia().ordinal();
		if(result != 0)
			return result;
		else {
			result = h1.getHoraInicio()-h2.getHoraInicio();
			if(result != 0)
				return result;
			else {
				return h1.getMinutoInicio()-h2.getMinutoInicio();
			}
		}
	}

}
