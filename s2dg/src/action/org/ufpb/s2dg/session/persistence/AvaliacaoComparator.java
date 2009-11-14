package org.ufpb.s2dg.session.persistence;

import java.util.Comparator;

import org.ufpb.s2dg.entity.Avaliacao;
import org.ufpb.s2dg.entity.EventoCalendarioTurma;

public class AvaliacaoComparator implements Comparator<Avaliacao> {

	public int compare(Avaliacao arg0, Avaliacao arg1) {
		EventoCalendarioTurma d1 = arg0.getDataEvento();
		EventoCalendarioTurma d2 = arg1.getDataEvento();
		if((d1 != null)&&(d2 != null))
			return d1.getData().compareTo(d2.getData());
		else {
			if(d1 == null)
				return 1;
			else if(d2 == null)
				return -1;
			else {
				return arg1.getNome().compareTo(arg0.getNome());
			}
		}
	}

}
