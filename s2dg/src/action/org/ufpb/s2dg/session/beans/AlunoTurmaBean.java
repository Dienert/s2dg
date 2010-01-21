package org.ufpb.s2dg.session.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.ufpb.s2dg.entity.Aluno;
import org.ufpb.s2dg.entity.AlunoTurma;
import org.ufpb.s2dg.entity.Disciplina;
import org.ufpb.s2dg.entity.Horario;
import org.ufpb.s2dg.entity.Turma;
import org.ufpb.s2dg.entity.Usuario;
import org.ufpb.s2dg.entity.AlunoTurma.Situacao;
import org.ufpb.s2dg.session.Fachada;
import org.ufpb.s2dg.session.util.MenuAction;
import org.ufpb.s2dg.session.util.PDFAction;
import org.ufpb.s2dg.session.util.TrancamentoBean;
import org.ufpb.s2dg.session.beans.MatriculaBean;

@Name("alunoTurmaBean")
@Scope(ScopeType.SESSION)
@AutoCreate
public class AlunoTurmaBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8355040116692086316L;
	public static float MEDIA_MINIMA_PARA_APROVACAO = 5.0F;

	private AlunoTurma alunoTurma;
	
	@Logger
	private Log log;
	
	@In
	private Fachada fachada;

	@In
	MenuAction MenuAction;
	
	//TODO Clodoaldo: isso pode ser perigoso, caso der pau, checar!
	@In
	MatriculaBean matriculaBean;
	
	@In
	TrancamentoBean trancamentoBean;
	
	@In
	PDFAction pdfAction;
	
	public AlunoTurma getAlunoTurma() {
		return alunoTurma;
	}

	public String setAlunoTurma(AlunoTurma alunoTurma) {
		MenuAction.setId_Menu(0);
		this.alunoTurma = alunoTurma;
		if(alunoTurma != null) {
			Turma t = alunoTurma.getTurma();
			if(t == null)
				t = fachada.getTurmaDoBanco(alunoTurma);
			Disciplina d = t.getDisciplina();
			if(d == null)
				d = fachada.getDisciplinaDoBanco(t);
			t.setDisciplina(d);
			fachada.setTurma(t);
		}
		return "/home.seam";	
	}
	
	public List<AlunoTurma> getAlunoTurmaAsList() {
		if(alunoTurma != null) {
			List<AlunoTurma> list = new ArrayList<AlunoTurma>();
			list.add(alunoTurma);
			return list;
		}
		else return null;
	}
	

	public String trancamentoParcial(){
		alunoTurma.setSituacao(Situacao.TRANCADO);
		fachada.trancamentoParcial(alunoTurma);
		MenuAction.setId_MenuAluno(7);
		trancamentoBean.setRequiredBoxParcial(false);
		return "/home.seam";
	}
	
	public boolean checaCondicoesTrancamentoParcial(){
		boolean condicao1 = periodoTrancamentoParcialAberto();
		log.info(" periodoTrancamentoParcialAberto() - {0}", condicao1);
		boolean condicao2 = !maximoTrancamentoParciaisPermitido();
		log.info(" !maximoTrancamentoParciaisPermitido() - {0}", condicao2);
		boolean condicao3 = !minimoDisciplinasCursando();
		log.info(" !minimoDisciplinasCursando() - {0}", condicao3);
		return condicao1 && condicao2 && condicao3;
	}
	
	private boolean periodoTrancamentoParcialAberto() {
		return matriculaBean.podeFazerTrancamentoParcial();
	}

	private boolean maximoTrancamentoParciaisPermitido() {
		if (fachada.getNumeroDeVezesAlunoTurmaCursadasAnteriormente() < 2)
			return false;
		return true;
	}

	private boolean minimoDisciplinasCursando() {
		log.info("fachada.numeroDeDisciplinasAtivas() - {0}", fachada.numeroDeDisciplinasAtivas());
		if (fachada.numeroDeDisciplinasAtivas() > 1)
			return false;
		else
			return true;
	}

	public void gerarPDFTrancamentoParcial() {
		ArrayList<HashMap<String, String>> mapas = new ArrayList<HashMap<String, String>>();
		//Numero - Codigo - Nome da disciplina - Creditos - CargaHoraria - Horarios - Sala
		AlunoTurma at = alunoTurma;
		HashMap<String, String> mapa = new HashMap<String, String>();
		mapa.put("Aviso","A disciplina foi trancada.");
		mapa.put("NomeAluno", fachada.getUsuario().getNome());
		mapa.put("Numero", at.getTurma().getNumero());
		mapa.put("Codigo", at.getTurma().getDisciplina().getCodigo());
		mapa.put("Nome", at.getTurma().getDisciplina().getNome());
		String horarios = "";
		for (Horario h : matriculaBean.getHorariosOrdenados(at.getTurma().getHorarios())) {
			horarios += h.toString()+ "\n";
		}
		mapa.put("Horarios", horarios);
		String salas = "";
		mapa.put("Turma", at.getTurma().getNumero());
		int creditos = at.getTurma().getDisciplina().getCreditos();
		mapa.put("Creditos", String.valueOf(creditos));
		System.out.println(at.getTurma().getNumero());
		mapa.put("Carga Horaria", String.valueOf(creditos*15));
		System.out.println(at.getTurma().getNumero());
		mapas.add(mapa);
		
		HashMap<String, String> dadosAluno = new HashMap<String, String>();
		
		Usuario usuario = fachada.getUsuario();
		dadosAluno.put("NomeAluno", usuario.getNome());
		dadosAluno.put("MatriculaAluno", usuario.getAluno().getMatricula());
		dadosAluno.put("CursoAluno", "" + usuario.getAluno().getCurriculo().getCurso().getNome());
		dadosAluno.put("CurriculoAluno", "" + usuario.getAluno().getCurriculo().getNumero());

		pdfAction.geraPdfTranca("Comprovante_Trancamento", mapas, dadosAluno);
	}
	
	public void gerarPDFTrancamentoTotal() {

		HashMap<String, String> dadosAluno = new HashMap<String, String>();
		dadosAluno.put("NomeAluno", fachada.getUsuario().getNome());
		dadosAluno.put("MatriculaAluno", fachada.getUsuario().getAluno().getMatricula());
		dadosAluno.put("CursoAluno", "" + fachada.getUsuario().getAluno().getCurriculo().getCurso().getNome());
		dadosAluno.put("CurriculoAluno", "" + fachada.getUsuario().getAluno().getCurriculo().getNumero());	
		dadosAluno.put("PeriodoAluno", "" + fachada.getPeriodoAtual(fachada.getUsuario().getAluno().getCurriculo().getCurso().getCentro()).toString());
		
		pdfAction.geraPdfTrancaTotal("Comprovante_Trancamento_Total", dadosAluno);
	}
}
