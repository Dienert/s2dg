package org.ufpb.s2dg.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.ufpb.s2dg.entity.Aluno;
import org.ufpb.s2dg.entity.AlunoTurma;
import org.ufpb.s2dg.entity.Calendario;
import org.ufpb.s2dg.entity.Curriculo;
import org.ufpb.s2dg.entity.Curso;
import org.ufpb.s2dg.entity.Disciplina;
import org.ufpb.s2dg.entity.DisciplinaTurmas;
import org.ufpb.s2dg.entity.Horario;
import org.ufpb.s2dg.entity.Oferta;
import org.ufpb.s2dg.entity.Professor;
import org.ufpb.s2dg.entity.Turma;
import org.ufpb.s2dg.entity.Aluno.SituacaoAcademica;
import org.ufpb.s2dg.entity.AlunoTurma.Situacao;

@Name("matriculaBean")
@Scope(ScopeType.PAGE)
@AutoCreate
public class MatriculaBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<Turma> turmasAptas = new ArrayList<Turma>();
	List<Turma> turmasSelecionadas = new ArrayList<Turma>();
	HashMap<Turma, Oferta> ofertas = new HashMap<Turma, Oferta>();
	List<DisciplinaTurmas> turmasAptasPorDisciplina;
	List<DisciplinaTurmas> temporario = new ArrayList<DisciplinaTurmas>();
	Aluno aluno;
	
	@In
	Fachada fachada;
	@In
	FacesContext facesContext;
	
	@Create
	public void init() {
		aluno = fachada.getAluno();
		listaTurmasAptas();
	}
	
	public void listaTurmasAptas() {
		aluno.setCurriculo(fachada.getCurriculoDoBanco(aluno));
		/* todas as disciplinas do curriculo: */
		List<Disciplina> disciplinasDoCurriculo = fachada.getDisciplinasDoBanco(aluno.getCurriculo());
		/* todas as turmas cursadas: */
		List<AlunoTurma> alunoTurmas = fachada.getTodosAlunoTurma(aluno);				
		/* remover as disciplinas já cursadas: */
		removerDisciplinasCursadas(disciplinasDoCurriculo, alunoTurmas);
		/* remover disciplinas que não atendem aos pre-requisitos */
		removerDisciplinasSemPreRequisitos(disciplinasDoCurriculo, alunoTurmas);
		/* remover disciplinas já matriculadas */
		removerDisciplinasJaMatriculadas(disciplinasDoCurriculo, alunoTurmas);
		/* listar turmas aptas, removendo disciplinas que não possuem oferta de turma */
		listarTurmasAptas(disciplinasDoCurriculo);
		geraTurmasAptasPorDisciplina();
		
	}

	private void geraTurmasAptasPorDisciplina() {
		Collections.sort(turmasAptas, new TurmaComparator());
		turmasAptasPorDisciplina = DisciplinaTurmas.geraTurmasPorDisciplina(turmasAptas);
	}

	private void removerDisciplinasJaMatriculadas( List<Disciplina> disciplinasDoCurriculo, List<AlunoTurma> alunoTurmas) {
		if(alunoTurmas != null) {
			for(AlunoTurma at : alunoTurmas) {
				Turma t = at.getTurma();
				if(t == null) {
					t = fachada.getTurmaDoBanco(at);
					at.setTurma(t);
				}
				Disciplina d = t.getDisciplina();
				if(d == null) {
					d = fachada.getDisciplinaDoBanco(t);
					t.setDisciplina(d);
				}
				if(at.getSituacao() == Situacao.EM_CURSO) {
					if (disciplinasDoCurriculo != null) {
						for(Disciplina disc : disciplinasDoCurriculo) {
							if(disc.getCodigo().equals(d.getCodigo())) {
								int i = disciplinasDoCurriculo.indexOf(disc);
								disciplinasDoCurriculo.remove(i);
								break;
							}
						}
					}					
				}
			}
		}
	}

	private void listarTurmasAptas(
			List<Disciplina> disciplinasDoCurriculo) {
		if(disciplinasDoCurriculo != null) {
			for(Disciplina d : disciplinasDoCurriculo) {
				Curso curso = fachada.getCursoDoBanco(aluno.getCurriculo());
				List<Turma> turmas = fachada.getTurmasDoBanco(d);
				if(turmas != null) {
					for(Turma t : turmas) {
						Oferta o = fachada.getOfertaDoBanco(curso, t);
						if(o != null) {
							boolean jaSelecionada = false;
							for(Turma turma : turmasSelecionadas)
								if(turma.getId() == t.getId())
									 jaSelecionada = true;
							if(!jaSelecionada){
								for(int i = 0; i < turmasAptas.size(); i++){
									if(turmasAptas.get(i).getId() == t.getId())
										turmasAptas.remove(i--);
								}
								turmasAptas.add(t);
								ofertas.put(t, o);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * @param disciplinasDoCurriculo
	 * @param alunoTurmas
	 */
	private void removerDisciplinasSemPreRequisitos(List<Disciplina> disciplinasDoCurriculo, List<AlunoTurma> alunoTurmas) {
		
		if(disciplinasDoCurriculo != null) {			
			for(int i = 0; i < disciplinasDoCurriculo.size(); i++) {				
				Disciplina d1 = disciplinasDoCurriculo.get(i);
				Set<Disciplina> preRequisitos = fachada.getPreRequisitosDoBanco(aluno.getCurriculo(), d1);				
				if(preRequisitos != null) {					
					for(Disciplina preReq : preRequisitos) {					
						if(!aprovadoNaDisciplina(alunoTurmas, preReq)){
							disciplinasDoCurriculo.remove(i--);
						}					
					}
				}
			}
		}
	}
	
	private boolean aprovadoNaDisciplina(List<AlunoTurma> alunoTurmas, Disciplina disciplina){
		for(int i = 0; i < alunoTurmas.size(); i++) {
			AlunoTurma at = alunoTurmas.get(i);
			Situacao sit = at.getSituacao();
			if(sit == null || (sit != Situacao.APROVADO && sit != Situacao.DISPENSADO)){
				continue; 
			}
			Turma t = at.getTurma();
			if(t == null) {
				t = fachada.getTurmaDoBanco(at);
				at.setTurma(t);
			}
			Disciplina d2 = t.getDisciplina();
			if(d2 == null) {
				d2 = fachada.getDisciplinaDoBanco(t);
				t.setDisciplina(d2);
			}
			if(d2.getCodigo().equals(disciplina.getCodigo())){
				return true;
			}
		}
		return false;
	}
	

	private void removerDisciplinasCursadas(
			List<Disciplina> disciplinasDoCurriculo,
			List<AlunoTurma> alunoTurmas) {
		if(alunoTurmas != null) {
			for(AlunoTurma at : alunoTurmas) {
				Turma t = at.getTurma();
				if(t == null) {
					t = fachada.getTurmaDoBanco(at);
					at.setTurma(t);
				}
				Disciplina d = t.getDisciplina();
				if(d == null) {
					d = fachada.getDisciplinaDoBanco(t);
					t.setDisciplina(d);
				}
				if((at.getSituacao() == Situacao.APROVADO)||(at.getSituacao() == Situacao.DISPENSADO)) {
					int i = disciplinasDoCurriculo.indexOf(d);
					disciplinasDoCurriculo.remove(i);
				}
			}
		}
	}

	public String fazMatricula(MenuAction menuAction) {
		MenuAction.setId_Menu(2);
		if(isCoRequisitosPresentes()) {
			if(isCreditosCorretos()) {
				if(!isChoqueDeHorario()) {
					if(isVagasSuficientes()) {
						for(Turma turma : turmasSelecionadas) {
							AlunoTurma novoAlunoTurma = new AlunoTurma();
							novoAlunoTurma.setAluno(aluno);
							novoAlunoTurma.setTurma(turma);
							novoAlunoTurma.setFaltas(0);
							fachada.criaAlunoTurma(novoAlunoTurma);
							String log = "Matrícula realizada com sucesso na disciplina "+turma.getDisciplina().getNome()
								+" (código:"+turma.getDisciplina().getCodigo()
								+"), turma "+turma.getNumero()
								+" (id:"+turma.getId()
								+")";
							fachada.fazLog(log);
						}
						turmasSelecionadas.clear();
						fachada.initTurmasMatriculadas();
						temporario.clear();
						geraTurmasAptasPorDisciplina();
						menuAction.setId_Menu(4);
						return "/home.seam";
					}
				}
			}
		}
		return null;
	}

	private boolean isCoRequisitosPresentes() {
		for(Turma turma : turmasSelecionadas) {
			Disciplina d = turma.getDisciplina();;
			List<Disciplina> coRequisitos = fachada.getCoRequisitosDoBanco(aluno.getCurriculo(), d);
			if(coRequisitos != null) {
				for(Disciplina coReq : coRequisitos) {
					for(int i = 0; i < turmasSelecionadas.size(); i++) {
						Turma t = turmasSelecionadas.get(i);
						if(t.getId() != turma.getId()) {
							if(t.getDisciplina().getCodigo().equals(coReq.getCodigo())) {
								break;
							}
						}
						else if(i == turmasSelecionadas.size()-1) {
							String msg = "Você deve selecionar as disciplinas "+d.getNome()+" e "+coReq.getNome()+" conjuntamente, pois possuem co-requisitos entre si";
							facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,msg, null));
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	@Transactional
	private boolean isVagasSuficientes() {
		List<Oferta> ofertas = new ArrayList<Oferta>();
		for(int i = 0; i < turmasSelecionadas.size(); i++) {
			Turma turma = turmasSelecionadas.get(i);
			ofertas.add(fachada.getOferta(aluno.getCurriculo(),turma));
			if(ofertas.get(i).getNumeroDeVagasDisponiveis() == 0) {
				String msg = turma.getDisciplina().getNome()+", Turma "+turma.getNumero()+" não possui mais vagas.";
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,msg, null));
				return false;
			}
		}
		for(int i = 0; i < ofertas.size(); i++) {
			ofertas.get(i).decrementaVaga();
			fachada.atualizaOferta(ofertas.get(i));
		}
		return true;
	}

	private boolean isChoqueDeHorario() {
		List<AlunoTurma> alunoTurmas = fachada.getAlunoTurmaDoBanco();
		List<Turma> matriculadas = new ArrayList<Turma>();
		if((alunoTurmas != null)&&(alunoTurmas.size() > 0)) {
			for(AlunoTurma at : alunoTurmas) {
				Turma t = at.getTurma(); 
				if(t == null) {
					t = fachada.getTurmaDoBanco(at);
					at.setTurma(t);
				}
				matriculadas.add(t);
			}
		}
		for(int i = 0; i < turmasSelecionadas.size(); i++) {
			/*turmas já matriculadas*/
			for(int j = 0; j < matriculadas.size(); j++) {
				Turma t1 = turmasSelecionadas.get(i);
				Turma t2 = matriculadas.get(j);
				List<Horario> horario1 = new ArrayList<Horario>(t1.getHorarios());
				List<Horario> horario2 = new ArrayList<Horario>(t2.getHorarios());
				for(Horario h1: horario1) {
					for(Horario h2: horario2) {
						if(h1.temChoque(h2)) {
							String msg = t1.getDisciplina().getNome()+", Turma "+t1.getNumero()+" possui choque de horário com "+t2.getDisciplina().getNome()+", Turma "+t2.getNumero()+", matriculada anteriormente.";
							facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,msg, null));
							return true;
						}
					}
				}
			}
			/* turmas selecionadas */
			for(int j = 0; j < turmasSelecionadas.size(); j++) {
				if(i != j) {
					Turma t1 = turmasSelecionadas.get(i);
					Turma t2 = turmasSelecionadas.get(j);
					List<Horario> horario1 = new ArrayList<Horario>(t1.getHorarios());
					List<Horario> horario2 = new ArrayList<Horario>(t2.getHorarios());
					for(Horario h1: horario1) {
						for(Horario h2: horario2) {
							if(h1.temChoque(h2)) {
								String msg = t1.getDisciplina().getNome()+", Turma "+t1.getNumero()+" possui choque de horário com "+t2.getDisciplina().getNome()+", Turma "+t2.getNumero()+".";
								facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,msg, null));
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean isCreditosCorretos() {
		Curriculo curriculo = aluno.getCurriculo();
		if(curriculo != null) {
			int minimo = curriculo.getMinimoCreditos();
			int maximo = curriculo.getMaximoCreditos();
			int atual = 0;
			/* turmas já matriculadas */
			List<AlunoTurma> matriculadas = fachada.getAlunoTurmaDoBanco();
			if((matriculadas != null)&&(matriculadas.size() > 0)) {
				for(AlunoTurma at : matriculadas) {
					Turma t = at.getTurma(); 
					if(t == null) {
						t = fachada.getTurmaDoBanco(at);
						at.setTurma(t);
					}
					Disciplina d = t.getDisciplina();
					if(d == null) {
						d = fachada.getDisciplinaDoBanco(t);
						t.setDisciplina(d);
					}
					atual += d.getCreditos();
				}
			}
			/* turmas selecionadas */
			if((turmasSelecionadas != null)&&(turmasSelecionadas.size() > 0)) {
				for(Turma turma: turmasSelecionadas) {
					atual += turma.getDisciplina().getCreditos();
				}
				if(atual < minimo) {
					String msg = "Quantidade de créditos ("+atual+") está abaixo do mínimo ("+minimo+").";
					facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,msg, null));
					return false;
				}
				else if(atual > maximo) {
					String msg = "Quantidade de créditos ("+atual+") está acima do máximo ("+maximo+").";
					facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,msg, null));
					return false;
				}
				else
					return true;
			}
			else {
				String msg = "Nenhuma turma selecionada.";
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,msg, null));
				return false;
			}
		}
		String msg = "Currículo não encontrado.";
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,msg, null));
		return false;
	}

	public boolean podeFazerMatricula() {
		return true;
//		if(fachada.getAluno().getSituacaoAcademica() != SituacaoAcademica.REGULAR)
//			return false;
//		Calendario calendario = fachada.getCalendario();
//		if(calendario != null) {
//			Date inicioMatricula = calendario.getInicioMatricula();
//			Date fimMatricula = calendario.getFimMatricula();
//			Date hoje = getDataAtual();
//			if((hoje.compareTo(inicioMatricula) >= 0)&&(hoje.compareTo(fimMatricula) <= 0))
//				return true;
//			else
//				return false;
//		}
//		return false;
	}
	
	public boolean podeFazerTrancamentoParcial(){
		return true;
		/*
		if(fachada.getAluno().getSituacaoAcademica() != SituacaoAcademica.REGULAR)
			return false;
		Calendario calendario = fachada.getCalendario();
		if(calendario != null) {
			Date inicioPeriodo = calendario.getInicioPeriodo();
			Date fimPeriodoTrancamento = calendario.getUltimoDiaTrancamento();
			Date hoje = getDataAtual();
			if((hoje.compareTo(inicioPeriodo) >= 0)&&(hoje.compareTo(fimPeriodoTrancamento) <= 0))
				return true;
			else
				return false;
		}
		return false;
		*/
	}
	
	
	public boolean podeFazerTrancamentoTotal(){
		return true;
		/*
		if(fachada.getAluno().getSituacaoAcademica() != SituacaoAcademica.REGULAR)
			return false;
		Calendario calendario = fachada.getCalendario();
		if(calendario != null) {
			Date inicioPeriodo = calendario.getInicioPeriodo();
			Date fimPeriodoTrancamento = calendario.getUltimoDiaTrancamento();
			Date hoje = getDataAtual();
			if((hoje.compareTo(inicioPeriodo) >= 0)&&(hoje.compareTo(fimPeriodoTrancamento) <= 0))
				return true;
			else
				return false;
		}
		return false;
		*/
	}
	
	
	public Date getDataAtual() {  
		Calendar calendar = new GregorianCalendar();  
		Date date = new Date();  
		calendar.setTime(date);  
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}  

	public List<Turma> getTurmasAptas() {
		return turmasAptas;
	}

	public void setTurmasAptas(List<Turma> turmasAptas) {
		this.turmasAptas = turmasAptas;
	}

	public List<Turma> getTurmasSelecionadas() {
		return turmasSelecionadas;
	}

	public void setTurmasSelecionadas(List<Turma> turmasSelecionadas) {
		this.turmasSelecionadas = turmasSelecionadas;
	}
	
	public List<Professor> listaProfessores(Turma turma) {
		if(turma != null) {
			Set<Professor> setProfessores = turma.getProfessores();
			if(setProfessores != null) {
				List<Professor> listProfessores = new ArrayList<Professor>(setProfessores);
				for(Professor professor: listProfessores) {
					professor.setUsuario(fachada.getUsuarioProfessor(professor.getMatricula()));
				}
				Collections.sort(listProfessores, new ProfessorComparator());
				return listProfessores;
			}
		}
		return null;
	}
	
	@In
	MenuAction MenuAction;
	
	public void selecionaTurma(Turma turma, DisciplinaTurmas dts) {
		MenuAction.setId_Menu(2);
		turmasSelecionadas.add(turma);
		if(isChoqueDeHorario()) {
			turmasSelecionadas.remove(turma);
			return;
		}
		for(Turma t : dts.getTurmas()) {
			turmasAptas.remove(t);
		}
		temporario.add(dts);
		geraTurmasAptasPorDisciplina();
	}
	
	public void deselecionaTurma(Turma turma) {
		MenuAction.setId_Menu(2);
		turmasSelecionadas.remove(turma);
		for(DisciplinaTurmas dts : temporario) {
			if(dts.getDisciplina().getCodigo().equals(turma.getDisciplina().getCodigo())) {
				for(Turma t : dts.getTurmas()) {
					turmasAptas.add(t);
				}
				temporario.remove(dts);
				break;
			}
		}
		geraTurmasAptasPorDisciplina();
	}
	
	public List<Horario> getHorariosOrdenados(Set<Horario> horarios) {
		if(horarios != null) {
			List<Horario> list = new ArrayList<Horario>(horarios);
			Collections.sort(list, new HorarioComparator());
			return list;
		}
		return null;
	}
	
	public Oferta getOferta(Turma t) {
		return ofertas.get(t);
	}

	public List<DisciplinaTurmas> getTurmasAptasPorDisciplina() {
		return turmasAptasPorDisciplina;
	}

	public void setTurmasAptasPorDisciplina(
			List<DisciplinaTurmas> turmasAptasPorDisciplina) {
		this.turmasAptasPorDisciplina = turmasAptasPorDisciplina;
	}
	
}
