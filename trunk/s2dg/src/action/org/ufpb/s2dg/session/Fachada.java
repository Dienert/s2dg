package org.ufpb.s2dg.session;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.ufpb.s2dg.entity.Aluno;
import org.ufpb.s2dg.entity.AlunoTurma;
import org.ufpb.s2dg.entity.AlunoTurmaAvaliacao;
import org.ufpb.s2dg.entity.Avaliacao;
import org.ufpb.s2dg.entity.Calendario;
import org.ufpb.s2dg.entity.Global;
import org.ufpb.s2dg.entity.Periodo;
import org.ufpb.s2dg.entity.Professor;
import org.ufpb.s2dg.entity.Turma;
import org.ufpb.s2dg.entity.Usuario;
import org.ufpb.s2dg.session.persistence.AlunoTurmaAvaliacaoDAO;
import org.ufpb.s2dg.session.persistence.AlunoTurmaDAO;
import org.ufpb.s2dg.session.persistence.AvaliacaoDAO;
import org.ufpb.s2dg.session.persistence.CalendarioDAO;
import org.ufpb.s2dg.session.persistence.GlobalDAO;
import org.ufpb.s2dg.session.persistence.ProfessorDAO;
import org.ufpb.s2dg.session.persistence.TurmaDAO;
import org.ufpb.s2dg.session.persistence.UsuarioDAO;

@Name("fachada")
@Scope(ScopeType.APPLICATION)
@AutoCreate
public class Fachada {
	
	@In
	private UsuarioDAO usuarioDAO;
	@In
	private AlunoTurmaDAO alunoTurmaDAO;
	@In
	private TurmaDAO turmaDAO;
	@In
	private AvaliacaoDAO avaliacaoDAO;
	@In
	private AlunoTurmaAvaliacaoDAO alunoTurmaAvaliacaoDAO;
	@In
	private GlobalDAO globalDAO;
	@In
	private CalendarioDAO calendarioDAO;
	@In
	private ProfessorDAO professorDAO;
	
	@In
	private UsuarioBean usuarioBean;
	@In
	private GlobalBean globalBean;
	@In
	private TurmaBean turmaBean;
	@In
	private AlunoTurmaBean alunoTurmaBean;
	@In
	private AvaliacaoBean avaliacaoBean;
	@In
	private AlunoTurmaAvaliacoesBean alunoTurmaAvaliacoesBean;
	@In
	private AvaliacoesBean avaliacoesBean;
	@In
	private AlunoTurmasBean alunoTurmasBean;
	
	@Create
	public void init() {
		globalBean.setGlobal(getGlobalDoBanco());
	}
	
	public Usuario getUsuarioDoBanco(String username, String password) {
		return usuarioDAO.getUsuario(username,password);
	}
	
	public List<Turma> getTurmasDoBanco() {
		Usuario usuario = usuarioBean.getUsuario();
		Periodo periodo = getPeriodoAtual();
		if((usuario != null)&&(periodo != null)) {
			Professor professor = usuario.getProfessor();
			if(professor == null)
				professor = professorDAO.getProfessor(usuario);
			if(professor != null)
				return turmaDAO.getTurmas(professor,periodo);
		}
		return null;
	}

	public void cancelarEdicaoDeAvaliacao() {
		avaliacaoBean.cancelarEdicao();
	}
	
	public Usuario getUsuarioAluno(String matricula) {
		return usuarioDAO.getUsuarioAluno(matricula);
	}

	/* ESSES M�TODOS AINDA S�O NECESS�RIOS??
	//Criado por Julio e Rennan
	public String getEmail(String cpf) {
		if (cpf.equals("") || cpf == null) {
			System.err.println("usuario:"+cpf);
			return "dienertalencar@gmail.com";
		}
		System.err.println("Chamando getUsuario");
		Usuario user = usuarioDAO.getUsuario(cpf);
		if (user == null)
			return null;
		return user.getEmail();
	}
	
	//Criado por Rennan
	public boolean existeCpf(String cpf) {
		if (cpf.equals("") || cpf == null) {
			return false;
		}
		System.err.println("existe cpf");
		Usuario user = usuarioDAO.getUsuario(cpf);
		return user != null;		
	}*/
	
	public List<Avaliacao> getAvaliacoesDoBanco() {
		return avaliacaoDAO.getAvaliacoes(turmaBean.getTurma());
	}

	public Periodo getPeriodoAtual() {
		if(globalBean != null)
			return globalBean.getGlobal().getPeriodoAtual();
		else
			return null;
	}

	public Global getGlobalDoBanco() {
		return globalDAO.getGlobal();
	}

	public void setTurma(Turma turma) {
		turmaBean.setTurma(turma);		
	}

	public Calendario getCalendarioDoBanco() {
		return calendarioDAO.getCalendario(getPeriodoAtual());
	}

	public List<AlunoTurma> getAlunoTurmaDoBanco() {
		Usuario usuario = usuarioBean.getUsuario();
		if(usuario != null) {
			Aluno aluno = usuario.getAluno();
			if(aluno != null)
				return alunoTurmaDAO.getAlunoTurmas(aluno, getPeriodoAtual());
		}
		return null;
	}

	public void setAlunoTurma(AlunoTurma alunoTurma) {
		alunoTurmaBean.setAlunoTurma(alunoTurma);
	}

	public void atualizaAvaliacao(Avaliacao avaliacao) {
		avaliacaoDAO.atualiza(avaliacao);
	}

	public Turma getTurma() {
		return turmaBean.getTurma();
	}

	public void excluiAvaliacao(Avaliacao avaliacao) {
		avaliacaoDAO.remove(avaliacao);
	}

	public void criaAvaliacao(Avaliacao avaliacao) {
		avaliacaoDAO.cria(avaliacao, turmaBean.getTurma());
	}

	public List<AlunoTurma> getAlunoTurmasDoBanco() {
		return alunoTurmaDAO.getAlunoTurmas(turmaBean.getTurma());
	}

	public void setAlunoTurmaAvaliacoes(List<AlunoTurmaAvaliacao> list) {
		alunoTurmaAvaliacoesBean.setAlunoTurmaAvaliacoes(list);
	}

	public void atualizaTurma() {
		turmaDAO.atualiza(turmaBean.getTurma());
	}

	public List<AlunoTurmaAvaliacao> getAlunoTurmaAvaliacoes() {
		return alunoTurmaAvaliacoesBean.getAlunoTurmaAvaliacoes();
	}

	public void atualizaAlunoTurmaAvaliacao(
			AlunoTurmaAvaliacao alunoTurmaAvaliacao) {
		alunoTurmaAvaliacaoDAO.atualiza(alunoTurmaAvaliacao);
	}

	public void atualizaAlunoTurma(AlunoTurma alunoTurma) {
		alunoTurmaDAO.atualiza(alunoTurma);
	}

	public AlunoTurma getAlunoTurma() {
		return alunoTurmaBean.getAlunoTurma();
	}

	public Usuario getUsuarioProfessor(String matricula) {
		return usuarioDAO.getUsuarioProfessor(matricula);
	}

	public List<Avaliacao> getAvaliacoes() {
		return avaliacoesBean.getAvaliacoes();
	}

	public AlunoTurmaAvaliacao getAlunoTurmaAvaliacaoDoBanco(AlunoTurma alunoTurma,
			Avaliacao avaliacao) {
		return alunoTurmaAvaliacaoDAO.getAlunoTurmaAvaliacao(alunoTurma, avaliacao);
	}

	public AlunoTurmaAvaliacao criaAlunoTurmaAvaliacao(AlunoTurma alunoTurma,
			Avaliacao avaliacao) {
		return alunoTurmaAvaliacaoDAO.cria(alunoTurma, avaliacao);
	}

	public AlunoTurmaAvaliacao getAlunoTurmaAvaliacao(
			AlunoTurma alunoTurmaAtual, Avaliacao avaliacao) {
		return alunoTurmaAvaliacaoDAO.getAlunoTurmaAvaliacao(alunoTurmaAtual,avaliacao);
	}

	public void initAvaliacoes() {
		avaliacoesBean.init();
	}

	public void initAlunoTurmas() {
		alunoTurmasBean.init();
	}
	
	public void alteraSenha(String CPF, String senhaAtual, String novaSenha){
		usuarioDAO.alteraSenha(CPF, senhaAtual, novaSenha);
	}
	
	public Usuario getUsuario() {
		return usuarioBean.getUsuario();
	}
	
	public String getEmail(String CPF){
		return usuarioDAO.getEmail(CPF);
	}	
	
	public String getSenha(String CPF){
		return usuarioDAO.getSenha(CPF);
	}
}
