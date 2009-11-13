package org.ufpb.s2dg.session;

import java.io.Serializable;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.ufpb.s2dg.entity.AlunoTurma;
import org.ufpb.s2dg.entity.AlunoTurmaAvaliacao;
import org.ufpb.s2dg.entity.Avaliacao;
import org.ufpb.s2dg.entity.Turma;

@Name("avaliacoesBean")
@Scope(ScopeType.SESSION)
@AutoCreate
public class AvaliacoesBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3811567638464304533L;

	private List<Avaliacao> avaliacoes;
	
	@In
	private Fachada fachada;
	
	public void init() {
		Turma turmaAtual = fachada.getTurma();
		if(turmaAtual != null) {
			avaliacoes = fachada.getAvaliacoesDoBanco();
		}
	}

	public List<Avaliacao> getAvaliacoes() {
		return avaliacoes;
	}

	public void setAvaliacoes(List<Avaliacao> avaliacoes) {
		this.avaliacoes = avaliacoes;
	}
	
	public float getNota(Avaliacao avaliacao) {
		AlunoTurma alunoTurmaAtual = fachada.getAlunoTurma();
		if((alunoTurmaAtual != null)&&(avaliacao != null)) {
			AlunoTurmaAvaliacao alunoTurmaAvaliacao = fachada.getAlunoTurmaAvaliacao(alunoTurmaAtual,avaliacao);
			if(alunoTurmaAvaliacao != null)
				return alunoTurmaAvaliacao.getNota();
			else
				return 0;
		}
		return 0;
	}
	
	public boolean eNulo(List<AlunoTurma> lista) {
		if (lista == null || lista.isEmpty()) {
			return true;
		}
		return false;
	}
	
}
