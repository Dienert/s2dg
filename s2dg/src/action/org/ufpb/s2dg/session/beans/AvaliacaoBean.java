package org.ufpb.s2dg.session.beans;


import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.ufpb.s2dg.entity.Avaliacao;
import org.ufpb.s2dg.entity.EventoCalendarioTurma;
import org.ufpb.s2dg.entity.Turma;
import org.ufpb.s2dg.session.Fachada;
import org.ufpb.s2dg.session.persistence.AvaliacaoDAO;

@Name("avaliacaoBean")
@Scope(ScopeType.PAGE)
@AutoCreate
public class AvaliacaoBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1505454885943337059L;
	private Avaliacao avaliacao;
	private boolean criarOuEditar;
	private boolean avaliacaoVazia;
	private Avaliacao avaliacaoParaExclusao;
	private Avaliacao avaliacaoParaPublicacao;
	
	@In
	Fachada fachada;
	@In
	FacesContext facesContext;
	@In
	TimestampBean TimestampBean;

	public AvaliacaoBean() {
		avaliacao = new Avaliacao();
		avaliacao.setDataEvento(new EventoCalendarioTurma());
		criarOuEditar = true;
		setAvaliacaoVazia(false);
	}
	
	public Avaliacao getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(Avaliacao avaliacao) {
		this.avaliacao = avaliacao;
	}

	public boolean isCriarOuEditar() {
		return criarOuEditar;
	}
	
	public void setCriarOuEditar(boolean criarOuEditar) {
		this.criarOuEditar = criarOuEditar;
	}
	
	public boolean isAvaliacaoVazia() {
		return avaliacaoVazia;
	}

	public void setAvaliacaoVazia(boolean avaliacaoVazia) {
		this.avaliacaoVazia = avaliacaoVazia;
	}
	
	
	public void setAvaliacaoParaEdicao(Avaliacao avaliacao) {
		this.avaliacao = avaliacao;
		criarOuEditar = false;
		if(avaliacao.getDataEvento() == null)
			avaliacao.setDataEvento(new EventoCalendarioTurma());
	}	
	
	public void cancelarEdicao() {
		this.avaliacao = new Avaliacao();
		avaliacao.setDataEvento(new EventoCalendarioTurma());
		criarOuEditar = true;
	}
	
	public Avaliacao getAvaliacaoParaExclusao() {
		return avaliacaoParaExclusao;
	}

	public void setAvaliacaoParaExclusao(Avaliacao avaliacaoParaExclusao) {
		this.avaliacaoParaExclusao = avaliacaoParaExclusao;
	}
	
	public Avaliacao getAvaliacaoParaPublicacao() {
		return avaliacaoParaPublicacao;
	}

	public void setAvaliacaoParaPublicacao(Avaliacao avaliacaoParaPublicacao) {
		this.avaliacaoParaPublicacao= avaliacaoParaPublicacao;
	}	
	
	public void atualizarAvaliacao() {
		if(avaliacao.getDataEvento().getData() == null)
			avaliacao.setDataEvento(null);
		else
			avaliacao.getDataEvento().setEvento("Avaliação: "+avaliacao.getNome());
		fachada.atualizaAvaliacao(avaliacao);
		if((fachada.getTurma().isCalcularMediaAutomaticamente())){
			fachada.atualizaAlunoTurmas();
		}
		this.avaliacao = new Avaliacao();
		avaliacao.setDataEvento(new EventoCalendarioTurma());
		criarOuEditar = true;
//		setAvaliacaoVazia(false);
		fachada.initAvaliacoes();
	}
	
	public void cancelaExclusao() {
		this.avaliacao = new Avaliacao();
		avaliacao.setDataEvento(new EventoCalendarioTurma());
	}
	
	public void excluiAvaliacao() {
		if(avaliacaoParaExclusao != null) {
			avaliacaoParaExclusao.setTurma(fachada.getTurma());
			fachada.excluiAvaliacao(avaliacaoParaExclusao);
			if(avaliacao.getId() == avaliacaoParaExclusao.getId()) {
				avaliacao = new Avaliacao();
				avaliacao.setDataEvento(new EventoCalendarioTurma());
				criarOuEditar = true;
			}
			avaliacaoParaExclusao = null;
			if((fachada.getAvaliacoes().size()==1) && (fachada.getTurma().isCalcularMediaAutomaticamente())){
				fachada.getTurma().setCalcularMediaAutomaticamente(false);
				setAvaliacaoVazia(true);
				fachada.atualizaTurma();
			}
		}
		fachada.initAvaliacoes();
	}
	
	public void criarAvaliacao() {
		Turma turmaAtual = fachada.getTurma();
		if((avaliacao != null)&&(turmaAtual != null)) {			
			if(fachada.getAvaliacoes().size()<8){
				if(avaliacao.getDataEvento().getData() == null)
					avaliacao.setDataEvento(null);
				else
					avaliacao.getDataEvento().setEvento("Avaliação: "+avaliacao.getNome());
				fachada.criaAvaliacao(avaliacao);
				
				String log = "Criação com sucesso da avaliação \""+avaliacao.getNome()
					+"\" para a turma "+turmaAtual.getNumero()
					+" (id:"+turmaAtual.getId()
					+") da disciplina "+turmaAtual.getDisciplina().getNome()
					+"(código:"+turmaAtual.getDisciplina().getCodigo()
					+")";
				
				fachada.fazLog(log);
				fachada.atualizaAlunoTurmas();
				avaliacao = new Avaliacao();
				avaliacao.setDataEvento(new EventoCalendarioTurma());
				turmaAtual.setCalcularMediaAutomaticamente(true);
				setAvaliacaoVazia(false);
			}
			else{
				FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Número máximo de avaliações atingido.",null);
				facesContext.addMessage("form_avaliacao:nome_avaliacao",facesMessage);

			}
		}
		fachada.initAvaliacoes();
		
	}
	
	public void publicarNotas() {
		Avaliacao avaliacao = avaliacaoParaPublicacao;
		Turma turmaAtual = fachada.getTurma();
		if((avaliacao != null)&&(turmaAtual != null)) {		
			avaliacao.setPublicado(true);
			String log = "Publicando com sucesso a avaliacao \"" + avaliacao.getNome()
			+ "\" para a turma " + turmaAtual.getNumero()
			+ " (id:" + turmaAtual.getId()
			+ ") da disciplina " + turmaAtual.getDisciplina().getNome()
			+ "(código:" + turmaAtual.getDisciplina().getCodigo()
			+ ")";		
		    fachada.fazLog(log);
			fachada.atualizaAvaliacao(avaliacao);
		}			
		
		
		fachada.initAvaliacoes();
	}
	
}
