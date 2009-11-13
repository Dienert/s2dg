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

@Name("alunoTurmaAvaliacoesBean")
@Scope(ScopeType.PAGE)
@AutoCreate
public class AlunoTurmaAvaliacoesBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 236571474233568675L;

	private List<AlunoTurmaAvaliacao> alunoTurmaAvaliacoes;
	
	@In
	private Fachada fachada;

	public List<AlunoTurmaAvaliacao> getAlunoTurmaAvaliacoes() {
		return alunoTurmaAvaliacoes;
	}

	public void setAlunoTurmaAvaliacoes(
			List<AlunoTurmaAvaliacao> alunoTurmaAvaliacoes) {
		this.alunoTurmaAvaliacoes = alunoTurmaAvaliacoes;
	}
	
	public AlunoTurmaAvaliacao getAlunoTurmaAvaliacao(AlunoTurma alunoTurma,Avaliacao avaliacao) {
		if((avaliacao != null)&&(alunoTurma != null)) {
			AlunoTurmaAvaliacao alunoTurmaAvaliacao = fachada.getAlunoTurmaAvaliacaoDoBanco(alunoTurma, avaliacao);
			if(alunoTurmaAvaliacao == null)
				alunoTurmaAvaliacao = fachada.criaAlunoTurmaAvaliacao(alunoTurma, avaliacao);
			alunoTurmaAvaliacoes.add(alunoTurmaAvaliacao);
			return alunoTurmaAvaliacao;
		}
		return new AlunoTurmaAvaliacao();
	}
	
}
