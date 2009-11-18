package org.ufpb.s2dg.session.util;

import java.util.Comparator;

import org.ufpb.s2dg.entity.Turma;

public class TurmaComparator implements Comparator<Turma> {

	public int compare(Turma t1, Turma t2) {
		int result = t1.getDisciplina().getNome().compareTo(t2.getDisciplina().getNome());
		if(result != 0)
			return result;
		else {
			return t1.getNumero().compareTo(t2.getNumero());
		}
	}

}
