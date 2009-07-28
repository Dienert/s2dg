package org.ufpb.s2dg.session;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.ufpb.s2dg.entity.Horario;
import org.ufpb.s2dg.entity.Oferta;
import org.ufpb.s2dg.entity.Professor;
import org.ufpb.s2dg.entity.Turma;
import org.ufpb.s2dg.entity.AlunoTurma.Situacao;

@Name("matriculaBean")
@Scope(ScopeType.PAGE)
@AutoCreate
public class MatriculaBean {

	List<Turma> turmasAptas = new ArrayList<Turma>();
	List<Turma> turmasSelecionadas = new ArrayList<Turma>();
	Aluno aluno;
	boolean sucesso;
	String msg;
	
	@In
	Fachada fachada;
	
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
		litarTurmasAptas(disciplinasDoCurriculo);
		ordenarHorarios();
	}
	
	private void ordenarHorarios() {
		for(Turma t : turmasAptas) {
			ArrayList<Horario> list = new ArrayList<Horario>(t.getHorarios());
			Collections.sort(list,new HorarioComparator());
			t.setHorarios(new HashSet<Horario>(list));
		}
	}

	private void removerDisciplinasJaMatriculadas(
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
				if(at.getSituacao() == Situacao.EM_CURSO) {
					for(Disciplina disc : disciplinasDoCurriculo) {
						if(disc.getCodigo() == d.getCodigo()) {
							int i = disciplinasDoCurriculo.indexOf(disc);
							disciplinasDoCurriculo.remove(i);
							break;
						}
					}
				}
			}
		}
		
	}

	private void litarTurmasAptas(
			List<Disciplina> disciplinasDoCurriculo) {
		if(disciplinasDoCurriculo != null) {
			for(Disciplina d : disciplinasDoCurriculo) {
				Curso curso = fachada.getCursoDoBanco(aluno.getCurriculo());
				List<Turma> turmas = fachada.getTurmasDoBanco(d);
				if(turmas != null) {
					for(Turma t : turmas) {
						Oferta o = fachada.getOfertaDoBanco(curso, t);
						if(o != null) {
							turmasAptas.add(t);
						}
					}
				}
			}
		}
	}

	private void removerDisciplinasSemPreRequisitos(
			List<Disciplina> disciplinasDoCurriculo,
			List<AlunoTurma> alunoTurmas) {
		if(disciplinasDoCurriculo != null) {
			for(Disciplina d1 : disciplinasDoCurriculo) {
				Set<Disciplina> preRequisitos = d1.getPre_requisitos();
				if(preRequisitos != null) {
					loop: for(Disciplina preReq : preRequisitos) {
						for(int i = 0; i < alunoTurmas.size(); i++) {
							AlunoTurma at = alunoTurmas.get(i);
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
							if(d2.getCodigo() == preReq.getCodigo())
								break;
							else if(i == alunoTurmas.size()-1) {
								int j = disciplinasDoCurriculo.indexOf(d1);
								disciplinasDoCurriculo.remove(j);
								break loop;
							}
						}
					}
				}
			}
		}
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

	public void fazMatricula() {
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
						}
						turmasSelecionadas.clear();
						sucesso = true;
						msg = "Matrícula realizada com sucesso.";
						fachada.initTurmasMatriculadas();
					}
				}
			}
		}
	}

	private boolean isCoRequisitosPresentes() {
		for(Turma turma : turmasSelecionadas) {
			Disciplina d = turma.getDisciplina();
			List<Disciplina> coRequisitos = fachada.getCoRequisitosDoBanco(d);
			if(coRequisitos != null) {
				for(Disciplina coReq : coRequisitos) {
					for(int i = 0; i < turmasSelecionadas.size(); i++) {
						Turma t = turmasSelecionadas.get(i);
						if(t.getId() != turma.getId()) {
							if(t.getDisciplina().getCodigo() == coReq.getCodigo()) {
								break;
							}
						}
						else if(i == turmasSelecionadas.size()-1) {
							sucesso = false;
							msg = "Você deve selecionar as disciplinas "+d.getNome()+" e "+coReq.getNome()+" conjuntamente, pois possuem co-requisitos entre si";
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
				sucesso = false;
				msg = turma.getDisciplina().getNome()+", Turma "+turma.getNumero()+" não possui mais vagas.";
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
		for(int i = 0; i < turmasSelecionadas.size(); i++) {
			for(int j = 0; j < turmasSelecionadas.size(); j++) {
				if(i != j) {
					Turma t1 = turmasSelecionadas.get(i);
					Turma t2 = turmasSelecionadas.get(j);
					List<Horario> horario1 = new ArrayList<Horario>(t1.getHorarios());
					List<Horario> horario2 = new ArrayList<Horario>(t2.getHorarios());
					for(Horario h1: horario1) {
						for(Horario h2: horario2) {
							if(h1.temChoque(h2)) {
								sucesso = false;
								msg = t1.getDisciplina().getNome()+", Turma "+t1.getNumero()+" possui choque de horário com "+t2.getDisciplina().getNome()+", Turma "+t2.getNumero()+".";
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
			if((turmasSelecionadas != null)&&(turmasSelecionadas.size() > 0)) {
				for(Turma turma: turmasSelecionadas) {
					atual += turma.getDisciplina().getCreditos();
				}
				if(atual < minimo) {
					sucesso = false;
					msg = "Quantidade de créditos ("+atual+") está abaixo do mínimo ("+minimo+").";
					return false;
				}
				else if(atual > maximo) {
					sucesso = false;
					msg = "Quantidade de créditos ("+atual+") está acima do máximo ("+maximo+").";
					return false;
				}
				else
					return true;
			}
			else {
				sucesso = false;
				msg = "Nenhuma turma selecionada.";
				return false;
			}
		}
		sucesso = false;
		msg = "Currículo não encontrado.";
		return false;
	}

	public boolean isPeriodoDeMatricula() {
		Calendario calendario = fachada.getCalendario();
		if(calendario != null) {
			Date inicioMatricula = calendario.getInicioMatricula();
			Date fimMatricula = calendario.getFimMatricula();
			Date hoje = getDataAtual();
			if((hoje.compareTo(inicioMatricula) >= 0)&&(hoje.compareTo(fimMatricula) <= 0))
				return true;
			else
				return false;
		}
		return false;
	}
	
	public Date getDataAtual() {  
		Calendar calendar = new GregorianCalendar();  
		Date date = new Date();  
		calendar.setTime(date);  
		return calendar.getTime();
	}  

	public List<Turma> getTurmasAptas() {
		return turmasAptas;
	}

	public void setTurmasAptas(List<Turma> turmasAptas) {
		this.turmasAptas = turmasAptas;
	}

	public boolean isSucesso() {
		return sucesso;
	}

	public void setSucesso(boolean sucesso) {
		this.sucesso = sucesso;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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
				return listProfessores;
			}
		}
		return null;
	}
	
	public void selecionaTurma(Turma turma) {
		turmasSelecionadas.add(turma);
		int i = turmasAptas.indexOf(turma);
		if(i > -1)
			turmasAptas.remove(i);
	}
	
	public void deselecionaTurma(Turma turma) {
		int i = turmasSelecionadas.indexOf(turma);
		if(i > -1)
			turmasSelecionadas.remove(i);
		turmasAptas.add(turma);
	}
	
}