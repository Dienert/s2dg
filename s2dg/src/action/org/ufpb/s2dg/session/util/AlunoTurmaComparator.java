package org.ufpb.s2dg.session.util;

import java.util.Comparator;

import org.ufpb.s2dg.entity.AlunoTurma;

public class AlunoTurmaComparator implements Comparator<AlunoTurma> {

	public int compare(AlunoTurma at1, AlunoTurma at2) {
		String nome1 = at1.getAluno().getUsuario().getNome();
		String nome2 = at2.getAluno().getUsuario().getNome();
		return nome1.compareTo(nome2);
	}
	
}
