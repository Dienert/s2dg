package org.ufpb.s2dg.session.util;

import java.util.Comparator;

import org.ufpb.s2dg.entity.Professor;

public class ProfessorComparator implements Comparator<Professor> {

	public int compare(Professor p1, Professor p2) {
		return p1.getUsuario().getNome().compareTo(p2.getUsuario().getNome());
	}

}
