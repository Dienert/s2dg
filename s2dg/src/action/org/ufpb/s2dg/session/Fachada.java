package org.ufpb.s2dg.session;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.ufpb.s2dg.entity.Aluno;
import org.ufpb.s2dg.entity.AlunoTurma;
import org.ufpb.s2dg.entity.AlunoTurmaAvaliacao;
import org.ufpb.s2dg.entity.Avaliacao;
import org.ufpb.s2dg.entity.Calendario;
import org.ufpb.s2dg.entity.Centro;
import org.ufpb.s2dg.entity.Curriculo;
import org.ufpb.s2dg.entity.Curso;
import org.ufpb.s2dg.entity.Disciplina;
import org.ufpb.s2dg.entity.EventoCalendarioTurma;
import org.ufpb.s2dg.entity.Oferta;
import org.ufpb.s2dg.entity.Periodo;
import org.ufpb.s2dg.entity.Professor;
import org.ufpb.s2dg.entity.Sala;
import org.ufpb.s2dg.entity.Turma;
import org.ufpb.s2dg.entity.Usuario;
import org.ufpb.s2dg.session.beans.AlunoTurmaAvaliacoesBean;
import org.ufpb.s2dg.session.beans.AlunoTurmaBean;
import org.ufpb.s2dg.session.beans.AlunoTurmasBean;
import org.ufpb.s2dg.session.beans.AvaliacaoBean;
import org.ufpb.s2dg.session.beans.AvaliacoesBean;
import org.ufpb.s2dg.session.beans.CalendarBean;
import org.ufpb.s2dg.session.beans.CalendarioBean;
import org.ufpb.s2dg.session.beans.GlobalBean;
import org.ufpb.s2dg.session.beans.TurmaBean;
import org.ufpb.s2dg.session.beans.UsuarioBean;
import org.ufpb.s2dg.session.beans.UsuarioBean.TipoAbaAtiva;
import org.ufpb.s2dg.session.persistence.AlunoDAO;
import org.ufpb.s2dg.session.persistence.AlunoTurmaAvaliacaoDAO;
import org.ufpb.s2dg.session.persistence.AlunoTurmaDAO;
import org.ufpb.s2dg.session.persistence.AvaliacaoDAO;
import org.ufpb.s2dg.session.persistence.CalendarioDAO;
import org.ufpb.s2dg.session.persistence.CurriculoDAO;
import org.ufpb.s2dg.session.persistence.CursoDAO;
import org.ufpb.s2dg.session.persistence.DataEventoDAO;
import org.ufpb.s2dg.session.persistence.DisciplinaDAO;
import org.ufpb.s2dg.session.persistence.EventoCalendarioTurmaDAO;
import org.ufpb.s2dg.session.persistence.GlobalDAO;
import org.ufpb.s2dg.session.persistence.LogDAO;
import org.ufpb.s2dg.session.persistence.OfertaDAO;
import org.ufpb.s2dg.session.persistence.PeriodoDAO;
import org.ufpb.s2dg.session.persistence.ProfessorDAO;
import org.ufpb.s2dg.session.persistence.SalaDAO;
import org.ufpb.s2dg.session.persistence.TurmaDAO;
import org.ufpb.s2dg.session.persistence.UsuarioDAO;
import org.ufpb.s2dg.session.util.EmailAction;
import org.ufpb.s2dg.session.util.TurmasMatriculadasBean;

@Name("fachada")
@Scope(ScopeType.SESSION)
@AutoCreate
public class Fachada implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8622239731631410022L;
	
	@SuppressWarnings("unused")
	@Logger
	private Log log;
	
	@In
	private UsuarioDAO usuarioDAO;
	@In
	private AlunoTurmaDAO alunoTurmaDAO;
	@In
	private AlunoDAO alunoDAO;
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
	private CursoDAO cursoDAO;
	@In
	private OfertaDAO ofertaDAO;
	@In
	private CurriculoDAO curriculoDAO;
	@In
	private DataEventoDAO dataEventoDAO;
	@In
	private DisciplinaDAO disciplinaDAO;
	@In
	private SalaDAO salaDAO;
	@In
	private LogDAO logDAO;
	@In
	private EventoCalendarioTurmaDAO eventoCalendarioTurmaDAO;
	@In
	private PeriodoDAO periodoDAO;
	
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
	@In
	private CalendarioBean calendarioBean;
	@In
	private TurmasMatriculadasBean turmasMatriculadasBean;
	
	@In
	private CalendarBean calendarBean;
	
	@In
	private EmailAction emailAction;
	
	@Create
	public void init() {
		atualizarPeriodosAtuais(getGlobalDoBanco());
	}

	private void atualizarPeriodosAtuais(Map<Centro, Periodo> globalDoBanco) {
		globalBean.setPeriodosAtuais(globalDoBanco);
	}

	public Usuario getUsuarioDoBanco(String username) {
		return usuarioDAO.getUsuario(username);
	}
	
	public List<Turma> getTurmasDoBanco() {
		Usuario usuario = usuarioBean.getUsuario();
		if((usuario != null)) {
			Professor professor = usuario.getProfessor();
			if(professor == null)
				professor = professorDAO.getProfessor(usuario);
			if(professor != null){
				Centro c = professor.getDepartamento().getCentro();
				return turmaDAO.getTurmas(professor,getPeriodoAtual(c));
			}
				
		}
		return new ArrayList<Turma>();
	}
	
	public List<Turma> getTurmasDoBanco(boolean finalizada) {
		Usuario usuario = usuarioBean.getUsuario();
		if((usuario != null)) {
			Professor professor = usuario.getProfessor();
			if(professor == null)
				professor = professorDAO.getProfessor(usuario);
			if(professor != null){
				Centro c = professor.getDepartamento().getCentro();
				return turmaDAO.getTurmas(professor,getPeriodoAtual(c), finalizada);
			}
				
		}
		return new ArrayList<Turma>();
	}
	
	public List<Turma> getTurmasDoBancoPeriodoAnterior() {
		Usuario usuario = usuarioBean.getUsuario();
		if((usuario != null)) {
			Professor professor = usuario.getProfessor();
			if(professor == null)
				professor = professorDAO.getProfessor(usuario);
			if(professor != null){
				Centro c = professor.getDepartamento().getCentro();
				return turmaDAO.getTurmas(professor,getPeriodoAnterior(c));
			}
				
		}
		return new ArrayList<Turma>();
	}

	public void cancelarEdicaoDeAvaliacao() {
		avaliacaoBean.cancelarEdicao();
	}
	
	public Usuario getUsuarioAluno(String matricula) {
		return usuarioDAO.getUsuarioAluno(matricula);
	}
	
	public List<Avaliacao> getAvaliacoesDoBanco() {
		return avaliacaoDAO.getAvaliacoes(turmaBean.getTurma());
	}

	public Periodo getPeriodoAtual(Centro centro) {
		if(globalBean != null)
			return globalBean.getPeriodoAtual(centro);
		else
			return null;
	}
	
	public Periodo getPeriodoAnterior(Centro centro) {
		if(globalBean != null){
			Periodo atual = globalBean.getPeriodoAtual(centro);
			Periodo anterior = globalBean.AchaPeriodoAnterior(atual);
			return periodoDAO.getPeriodo(anterior.getSemestre(), anterior.getAno());
		}
		else
			return null;
	}

	public Map<Centro, Periodo> getGlobalDoBanco(){
		return globalDAO.getRelacaoPeriodosAtuais();
	}

	public void setTurma(Turma turma) {
		turmaBean.setTurma(turma);		
	}

	public Calendario getCalendarioDoBanco(Centro c) {
		return calendarioDAO.getCalendario(getPeriodoAtual(c));
	}

	public List<AlunoTurma> getAlunoTurmaDoBanco() {
		try {
			Usuario usuario = usuarioBean.getUsuario();
			if(usuario != null) {
				Aluno aluno = usuario.getAluno();
				if(aluno != null){
					Centro c = aluno.getCurriculo().getCurso().getCentro();
					return alunoTurmaDAO.getAlunoTurmas(aluno, getPeriodoAtual(c));
				}
			}
			return new ArrayList<AlunoTurma>();
		} catch (NullPointerException e) {
			return new ArrayList<AlunoTurma>();
		}
	}
	
	public List<AlunoTurma> getAlunosPorTurma(Turma turma) {		
		return alunoTurmaDAO.getAlunoTurmas(turma);
	}

	public void setAlunoTurma(AlunoTurma alunoTurma) {
		alunoTurmaBean.setAlunoTurma(alunoTurma);
	}

	public void atualizaAvaliacao(Avaliacao avaliacao) {
		avaliacaoDAO.atualiza(avaliacao);
	}

	public void atualizaAlunoTurmas(){
		alunoTurmasBean.atualizaAlunoTurmas();
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
	
	public void publicaNotas(Avaliacao avaliacao) {
		avaliacaoDAO.publicaNotas(avaliacao, turmaBean.getTurma());		
	}

	public List<AlunoTurma> getAlunoTurmasDoBanco() {
		return alunoTurmaDAO.getAlunoTurmas(turmaBean.getTurma());
	}
	
	public List<AlunoTurma> getAlunoTurmasDoBanco(Turma turma) {
		return alunoTurmaDAO.getAlunoTurmas(turma);
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
	
	public List<AlunoTurmaAvaliacao> getAvaliacoesPorAluno(AlunoTurma alunoturma) {
		return alunoTurmaAvaliacaoDAO.getAvaliacoesPorAluno(alunoturma);	
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

	public AlunoTurmaAvaliacao getAlunoTurmaAvaliacaoPublicada(
			AlunoTurma alunoTurmaAtual, Avaliacao avaliacao) {
		return alunoTurmaAvaliacaoDAO.getAlunoTurmaAvaliacaoPublicada(alunoTurmaAtual,avaliacao);
	}
	
	public Turma getTurmaDisciplinaSelecionada(){
		try {
			return alunoTurmaBean.getAlunoTurma().getTurma();
		} catch (NullPointerException npe) {
			//log.warn("getTurmaDisciplinaSelecionada - NullPointerException: {0}", npe.getMessage());
			return null;
		}
	}
	
	public void initAvaliacoes() {
		avaliacoesBean.init();
	}

	public void initAlunoTurmas() {
		alunoTurmasBean.init();
	}
	
	public void atualizaUsuario(Usuario usuario) {
		usuarioDAO.updateUsuario(usuario);
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
	
	public void setSenha(String CPF, byte [] senha){
		usuarioDAO.alteraSenha(CPF, senha);
	}
	
	public void setAlunoTurmas(List<AlunoTurma> alunoTurmas) {
		alunoTurmasBean.setAlunoTurmas(alunoTurmas);
	}
	
	public void setAvaliacoes(List<Avaliacao> avaliacoes) {
		avaliacoesBean.setAvaliacoes(avaliacoes);
	}

	public Calendario getCalendarioAluno() {
		return calendarioBean.getCalendarioAluno();
	}
	
	public Calendario getCalendarioProfessor(){
		return calendarioBean.getCalendarioProfessor();
	}

	public Aluno getAluno() {
		Usuario usuario = usuarioBean.getUsuario();
		if(usuario != null)
			return usuario.getAluno();
		else
			return null;
	}

	public Oferta getOferta(Curriculo curriculo, Turma turma) {
		Curso curso = cursoDAO.getCurso(curriculo);
		return ofertaDAO.getOferta(curso, turma);
	}

	public void criaAlunoTurma(AlunoTurma alunoTurma) {
		alunoTurmaDAO.cria(alunoTurma);
	}

	public Curriculo getCurriculoDoBanco(Aluno aluno) {
		return curriculoDAO.getCurriculo(aluno);
	}

	public void atualizaOferta(Oferta oferta) {
		ofertaDAO.atualiza(oferta);
	}

	public String getCPF() {
		return emailAction.getCPF();
	}

	public ArrayList<EventoCalendarioTurma> getDataEvento(int i, int j) {
		return dataEventoDAO.getDataEvento(i, j);
	}

	public List<EventoCalendarioTurma> getDataEvento(Turma turma) {
		return dataEventoDAO.getDataEvento(turma);
	}

	public void criaDataEvento(EventoCalendarioTurma dataEvento) {
		dataEventoDAO.cria(dataEvento);
	}

	public EventoCalendarioTurma getDataEvento(Avaliacao avaliacao) {
		return dataEventoDAO.getDataEvento(avaliacao);
	}

	public void atualizaDataEvento(EventoCalendarioTurma dataEvento) {
		dataEventoDAO.atualiza(dataEvento);
	}

	public void excluiDataEvento(EventoCalendarioTurma dataEvento) {
		dataEventoDAO.exclui(dataEvento);
	}

	public List<Disciplina> getDisciplinasDoBanco(Curriculo curriculo) {
		return disciplinaDAO.getDisciplinas(curriculo);
	}

	public List<AlunoTurma> getTodosAlunoTurma(Aluno aluno) {
		return alunoTurmaDAO.getAlunoTurmas(aluno);
	}

	public Turma getTurmaDoBanco(AlunoTurma at) {
		return turmaDAO.getTurma(at);
	}

	public Disciplina getDisciplinaDoBanco(Turma t) {
		return disciplinaDAO.getDisciplinas(t);
	}

	public List<Turma> getTurmasDoBanco(Disciplina d) {
		try {
			Centro c = d.getDepartamento().getCentro();
			return turmaDAO.getTurmas(d, getPeriodoAtual(c));
		} catch (NullPointerException e) {
			return new ArrayList<Turma>();
		}
	}

	public Curso getCursoDoBanco(Curriculo curriculo) {
		return cursoDAO.getCurso(curriculo);
	}

	public Oferta getOfertaDoBanco(Curso curso, Turma t) {
		return ofertaDAO.getOferta(curso, t);
	}

	public List<Disciplina> getCoRequisitosDoBanco(Curriculo c, Disciplina d) {
		return disciplinaDAO.getCoRequisitos(c, d);
	}

	public Set<Disciplina> getPreRequisitosDoBanco(Curriculo c, Disciplina d) {
		return disciplinaDAO.getPreRequisitos(c, d);
	}
	
	public void initTurmasMatriculadas() {
		turmasMatriculadasBean.init();
	}

	public void setUsuario(Usuario usuario) {
		usuarioBean.setUsuario(usuario);
	}
	
	public List<Sala> getSalaDoBanco(long id) {
		return salaDAO.getSalas(id);
	}

	public int getNumeroDeTrancamentosTotaisEfetuados(){
		return usuarioBean.getUsuario().getAluno().getTracamentosTotais();
	}
	
	public int getMaximoDeTrancamentosTotaisPossiveis(){
		return usuarioBean.getUsuario().getAluno().getCurriculo().getMaximoTrancamentosTotais();
	}
	
	public void fazLog(String log) {
		logDAO.cria(log, usuarioBean.getUsuario());
	}

	public void trancamentoParcial(AlunoTurma alunoTurma) {
		alunoTurmaDAO.atualizaSituacaoTrancamento(alunoTurma);
		
	}
	
	public List<AlunoTurma> getAlunoTurmaCursadasAnteriormente() {
		
		return alunoTurmaDAO.getAlunoTurmas(usuarioBean.getUsuario().getAluno(), alunoTurmaBean.getAlunoTurma().getTurma().getDisciplina());
	}

	public int getNumeroDeVezesAlunoTurmaCursadasAnteriormente() {
		return alunoTurmaDAO.getAlunoTurmas(usuarioBean.getUsuario().getAluno(), alunoTurmaBean.getAlunoTurma().getTurma().getDisciplina()).size();
	}

	public int numeroDeDisciplinasAtivas() {
		return alunoTurmasBean.numeroDeDisciplinasAtivas();
	}

	public Calendario getCalendarioDoBanco(Periodo periodoAtual, Centro centro) {
		return calendarioDAO.getCalendario(periodoAtual, centro);
	}

	public void setCalendarioAluno(Calendario calendarioAluno) {
		calendarioBean.setCalendarioAluno(calendarioAluno);
	}

	public void setCalendarioProfessor(Calendario calendarioProfessor) {
		calendarioBean.setCalendarioProfessor(calendarioProfessor);
	}

	public List<AlunoTurma> getAlunoTurmaIrregulares() {
		return alunoTurmaDAO.getAlunoTurmaInvalidos();
	}

	public List<Calendario> getCalendariosDoBanco() {
		return calendarioDAO.getCalendarios();
	}

	public List<AlunoTurma> getAlunoTurmasDoBanco(Periodo periodo, Centro centro) {
		return alunoTurmaDAO.getAlunoTurmas(periodo, centro);
	}
	
	public List<AlunoTurma> getAlunoTurmasEmCurso(){
		return turmasMatriculadasBean.getAlunoTurmasEmCurso();
	} 
	
	public void persisteSituacaoTurma(AlunoTurma at) {
		alunoTurmaDAO.atualizaSituacaoTrancamento(at);
	}
	
	public void persisteAluno(Aluno aluno) {
		alunoDAO.atualiza(aluno);
	}
	
	public void confirmarPlanejamentoEventoCalendarioTurma(long idEventoCalendarioTurma){
		turmaBean.confirmarPlanejamentoEventoCalendarioTurma(idEventoCalendarioTurma);
	}
	
	public void setarPlanejamentoEventoCalendarioTurma(long idEventoCalendarioTurma, String planejamentoExecutado){
		turmaBean.setPlanejamentoCalendarioTurmaTurma(idEventoCalendarioTurma, planejamentoExecutado);
	}
	
	public void atribuirPresencaAlunoEventoCalendarioTurma(long idEventoCalendarioTurma, Aluno aAluno, boolean presenca){
		turmaBean.atribuirPresencaAlunoEventoCalendarioTurma(idEventoCalendarioTurma, aAluno, presenca);
	}
	
	public void confirmarPlanejamentoEventoCalendarioTurma(Date dataEventoCalendarioTurma){
		turmaBean.confirmarPlanejamentoEventoCalendarioTurma(dataEventoCalendarioTurma);
	}
	
	public void setarPlanejamentoEventoCalendarioTurma(Date dataEventoCalendarioTurma, String planejamentoExecutado){
		turmaBean.setPlanejamentoCalendarioTurmaTurma(dataEventoCalendarioTurma, planejamentoExecutado);
	}
	
	public void atribuirPresencaAlunoEventoCalendarioTurma(Date dataEventoCalendarioTurma, Aluno aAluno, boolean presenca){
		turmaBean.atribuirPresencaAlunoEventoCalendarioTurma(dataEventoCalendarioTurma, aAluno, presenca);
	}
	
	public void persisteEventoCalendarioTurma(EventoCalendarioTurma eventoCalendarioTurma){
		eventoCalendarioTurmaDAO.atualiza(eventoCalendarioTurma);
	}
	
	public List<Aluno> getAlunos(Turma turma){
		return alunoTurmaDAO.getAlunos(turma);
	}
	
	public void persiteTurma(Turma turma){
		turmaDAO.atualiza(turma);
	}

	public void setAbaAtivaDocente() {
		usuarioBean.alterar(TipoAbaAtiva.DOCENTE);
		
	}

	public void setAbaAtivaDiscente() {
		usuarioBean.alterar(TipoAbaAtiva.DISCENTE);
		
	}
	
	public TipoAbaAtiva getAbaAtiva(){
		return usuarioBean.getAbaAtiva();
		
	}

	public List<EventoCalendarioTurma> getEventosCalendarioTurma(Turma turma) {
		return eventoCalendarioTurmaDAO.getEventoCalendarioTurma(turma);
	}
	
	public boolean isDiaDeAulaDaTurma(Date dataEscolhida){
		return turmaBean.isDiaDeAula(dataEscolhida);
	}
	
	public EventoCalendarioTurma getEventoCalendarioTurma(Date data){
		return turmaBean.getEventoCalendarioTurma(data);
	}
	
	public void fazOperacaoCriticaParaPersistir(EventoCalendarioTurma eventoCalendarioTurma){
		eventoCalendarioTurmaDAO.fazOperacaoCriticaParaPersistir(eventoCalendarioTurma);
	}
	
	public List<AlunoTurma> getAlunos(long id){
		return alunoDAO.getAlunos(id);
	}
	
	public Aluno getAluno(long id) {
		return alunoDAO.getAluno(id);
	}
	
	public Date getSelectedDate() {
		return (Date) calendarBean.getSelectedDate();
	}
}
