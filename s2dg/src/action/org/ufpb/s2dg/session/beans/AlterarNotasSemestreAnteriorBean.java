package org.ufpb.s2dg.session.beans;

import java.io.Serializable;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.ufpb.s2dg.entity.DisciplinaTurmas;
import org.ufpb.s2dg.entity.Turma;
import org.ufpb.s2dg.session.Fachada;

@Name("alterarNotasSemestreAnteriorBean")
@Scope(ScopeType.SESSION)
@AutoCreate
public class AlterarNotasSemestreAnteriorBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8426420865543668893L;
	private List<DisciplinaTurmas> turmasPorDisciplina;
	@In
	Fachada fachada;
	
	@Create
	public void init() {
		List<Turma> turmas = fachada.getTurmasDoBancoPeriodoAnterior();
		if(turmas != null) {
			List<DisciplinaTurmas> disciplinaTurmas = DisciplinaTurmas.geraTurmasPorDisciplina(turmas);
			if(disciplinaTurmas != null) {
				List<Turma> dturmas = disciplinaTurmas.get(0).getTurmas();
				if(dturmas != null)
					fachada.setTurma(dturmas.get(0));
			}
			turmasPorDisciplina = disciplinaTurmas;
		}
		else {
			fachada.setTurma(null);
			fachada.setAlunoTurmas(null);
			fachada.setAvaliacoes(null);
			fachada.setAlunoTurmaAvaliacoes(null);
		}
	}
	
	public void setTurmasPorDisciplina(List<DisciplinaTurmas> turmasPorDisciplina) {
		this.turmasPorDisciplina = turmasPorDisciplina;
	}
	
	public List<DisciplinaTurmas> getTurmasPorDisciplina() {
		return turmasPorDisciplina;
	}
	
}
