package org.ufpb.s2dg.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.ufpb.s2dg.entity.AlunoTurma;
import org.ufpb.s2dg.entity.Disciplina;
import org.ufpb.s2dg.entity.Horario;
import org.ufpb.s2dg.entity.Sala;
import org.ufpb.s2dg.entity.Turma;
import org.ufpb.s2dg.entity.Aluno.FormaIngresso;
import org.ufpb.s2dg.entity.Aluno.SituacaoAcademica;
import org.ufpb.s2dg.entity.AlunoTurma.Situacao;
import org.ufpb.s2dg.entity.Disciplina.Tipo;
import org.ufpb.s2dg.session.persistence.AlunoDAO;

@Name("turmasMatriculadasBean")
@Scope(ScopeType.SESSION)
@AutoCreate
public class TurmasMatriculadasBean {

	private List<AlunoTurma> alunoTurmas;
	
	@In
	Fachada fachada;

	@In
	MatriculaBean matriculaBean;
	
	@In
	TurmasMatriculadasBean turmasMatriculadasBean;
	
	@In
	PDFAction pdfAction;
	
	@In
	AlunoDAO alunoDAO;
	
	@In
	UsuarioBean usuarioBean;
		
	public void init() {
		List<AlunoTurma> ats = fachada.getAlunoTurmaDoBanco();
		if(ats != null) {
			if (ats.size() > 0) {
				fachada.setAlunoTurma(ats.get(0));
				this.alunoTurmas = ats;
				
				for(AlunoTurma alunoTurma : alunoTurmas) {
					Turma t = alunoTurma.getTurma();
					if(t == null)
						t = fachada.getTurmaDoBanco(alunoTurma);
					Disciplina d = t.getDisciplina();
					if(d == null)
						d = fachada.getDisciplinaDoBanco(t);
					t.setDisciplina(d);
					alunoTurma.setTurma(t);
				}
				
			}
		}
	}

	public List<AlunoTurma> getAlunoTurmas() {
		return alunoTurmas;
	}

	public List<AlunoTurma> getAlunoTurmasEmCurso() {
		
		if(alunoTurmas == null || alunoTurmas.size() == 0)
			return alunoTurmas;
		
		List<AlunoTurma> alunos = new LinkedList<AlunoTurma>();
		
		for(int i = 0; i < alunoTurmas.size(); i++){
			if(alunoTurmas.get(i).getSituacao() == Situacao.EM_CURSO){
				alunos.add(alunoTurmas.get(i));
			}
		}
		return alunos;
	}
	
	public void setAlunoTurmas(List<AlunoTurma> alunoTurmas) {
		this.alunoTurmas = alunoTurmas;
	}
	
	public int geraCreditosIntegralizadosObrigatorias(List<AlunoTurma> ats){

		
		int creditosIntegralizados = 0;
		
		if(ats != null){
			for(int i=0; i < ats.size(); i++){
				if((ats.get(i).getSituacao()==Situacao.APROVADO) ||( ats.get(i).getSituacao()==Situacao.DISPENSADO) ){
					
						creditosIntegralizados += ats.get(i).getTurma().getDisciplina().getCreditos();
					
				}
			}
		}
		return creditosIntegralizados;
	}
	
public int geraCreditosIntegralizadosOptativas(List<AlunoTurma> ats){

		
		int creditosIntegralizados = 0;
		
		if(ats != null){
			for(int i=0; i < ats.size(); i++){
				if((ats.get(i).getSituacao()==Situacao.APROVADO) ||( ats.get(i).getSituacao()==Situacao.DISPENSADO) ){
					
						creditosIntegralizados += ats.get(i).getTurma().getDisciplina().getCreditos();
					
				}
			}
		}
		return creditosIntegralizados;
	}

public int geraCreditosIntegralizadosComplementares(List<AlunoTurma> ats){

	
	int creditosIntegralizados = 0;
	
	if(ats != null){
		for(int i=0; i < ats.size(); i++){
			if((ats.get(i).getSituacao()==Situacao.APROVADO) ||( ats.get(i).getSituacao()==Situacao.DISPENSADO) ){
				
					creditosIntegralizados += ats.get(i).getTurma().getDisciplina().getCreditos();
				
			}
		}
	}
	return creditosIntegralizados;
}
	public int geraCreditosIntegralizados(List<AlunoTurma> ats){

		int creditosIntegralizados = 0;
		if(ats != null){
			for(int i=0; i < ats.size(); i++){
				if((ats.get(i).getSituacao()==Situacao.APROVADO) ||( ats.get(i).getSituacao()==Situacao.DISPENSADO) ){
						creditosIntegralizados += ats.get(i).getTurma().getDisciplina().getCreditos();
				}
			}
		}
		return creditosIntegralizados;
	}

	public int geraCreditosPeriodoAtual(List<AlunoTurma> alunoTurmas){
		int anoAtual = Integer.parseInt(alunoTurmas.get(alunoTurmas.size()-1).getTurma().getPeriodo().getAno());
		int semestreAtual = (int)alunoTurmas.get(alunoTurmas.size()-1).getTurma().getPeriodo().getSemestre();
		int creditosPeriodoAtual = 0;
		for(int i=0; i < alunoTurmas.size(); i++){
			int anoLista = Integer.parseInt(alunoTurmas.get(i).getTurma().getPeriodo().getAno());
			int semestreLista = (int)alunoTurmas.get(i).getTurma().getPeriodo().getSemestre();
			System.out.println("--------------------"+ anoLista+"." +semestreLista +"  "+ alunoTurmas.get(i).getSituacao() +"   " + alunoTurmas.get(i).getTurma().getDisciplina().getCreditos());
			if((((anoLista == anoAtual)&&(semestreLista == semestreAtual))&&((alunoTurmas.get(i).getSituacao()==Situacao.EM_CURSO))) ){
				creditosPeriodoAtual += alunoTurmas.get(i).getTurma().getDisciplina().getCreditos();
			}

		}
		return creditosPeriodoAtual;
	}
	public float geraCRE(List<AlunoTurma> ats){
		float somaMedias = 0;
		float somaCreditos = (float)geraCreditosIntegralizados(ats);
		
		
		if(ats != null){
			for(int i=0; i < ats.size(); i++){
				if((ats.get(i).getSituacao()==Situacao.APROVADO) ||( ats.get(i).getSituacao()==Situacao.DISPENSADO) 
						|| (ats.get(i).getSituacao()==Situacao.REPROVADO_POR_MEDIA)){
					somaMedias += (ats.get(i).getTurma().getDisciplina().getCreditos())*(ats.get(i).getMedia());
					if(ats.get(i).getSituacao()==Situacao.REPROVADO_POR_MEDIA){
						somaCreditos += (ats.get(i).getTurma().getDisciplina().getCreditos());
					}
				}	
			}
		}
		return (somaMedias/somaCreditos);
	}
	
	public int cargaHoraria(int creditos) {
		return creditos*15;
	}
	
	public String getTextoSituacao(Situacao situacao){
		switch(situacao){
			case APROVADO: return "APROVADO";
			case DISPENSADO: return "DISPENSA";
			case EM_CURSO: return "EM CURSO";
			case REPROVADO_POR_FALTA: return "REP FALTA";
			case REPROVADO_POR_MEDIA: return "REP MEDIA";
			default: return "TRANCADO";
		}
	}
	
	public int geraDisciplinasIntegralizadas(List<AlunoTurma> ats){
		
		int disciplinasIntegralizadas = 0;
		if(ats != null){
			for(int i=0; i < ats.size(); i++){
				if((ats.get(i).getSituacao()==Situacao.APROVADO) ||( ats.get(i).getSituacao()==Situacao.DISPENSADO) ){
					disciplinasIntegralizadas++;
				}
			}
		}
		return disciplinasIntegralizadas;
	}

	public int geraDisciplinasIntegralizadasObrigatorias(List<AlunoTurma> ats){
		int disciplinasIntegralizadas = 0;
		
		if(ats != null){
			for(int i=0; i < ats.size(); i++){
				if((ats.get(i).getSituacao()==Situacao.APROVADO) ||( ats.get(i).getSituacao()==Situacao.DISPENSADO) ){
						disciplinasIntegralizadas++;
				}
			}
		}
		return disciplinasIntegralizadas;
	}

	public int geraDisciplinasIntegralizadasOptativas(List<AlunoTurma> ats){
		int disciplinasIntegralizadas = 0;
		
		if(ats != null){
			for(int i=0; i < ats.size(); i++){
				if((ats.get(i).getSituacao()==Situacao.APROVADO) ||( ats.get(i).getSituacao()==Situacao.DISPENSADO) ){
						disciplinasIntegralizadas++;
				}
			}
		}
		return disciplinasIntegralizadas;
	}

	public int geraDisciplinasIntegralizadasComplementares(List<AlunoTurma> ats){
		int disciplinasIntegralizadas = 0;
		
		if(ats != null){
			for(int i=0; i < ats.size(); i++){
				if((ats.get(i).getSituacao()==Situacao.APROVADO) ||( ats.get(i).getSituacao()==Situacao.DISPENSADO) ){
						disciplinasIntegralizadas++;
				}
			}
		}
		return disciplinasIntegralizadas;
	}

	public int geraMinimoCreditosCurriculo(){
		int minimoCreditosCurriculo = fachada.getAluno().getCurriculo().getMinimoCreditosObrigatorias()
									+fachada.getAluno().getCurriculo().getMinimoCreditosOptativas()
									+fachada.getAluno().getCurriculo().getMinimoCreditosComplementares();
		return minimoCreditosCurriculo;
	}
	
	public int geraMinimoDisciplinasCurriculo(){
		int minimoDisciplinasCurriculo = fachada.getAluno().getCurriculo().getMinimoDisciplinasObrigatorias()
									+fachada.getAluno().getCurriculo().getMinimoDisciplinasOptativas()
									+fachada.getAluno().getCurriculo().getMinimoDisciplinasComplementares();
		return minimoDisciplinasCurriculo;
	}
	
	public List<Sala> getSalasDoBanco(long id) {
		return fachada.getSalaDoBanco(id);}
	
	public String geraMedia(AlunoTurma alunoTurma){
		if((alunoTurma.getSituacao()==Situacao.APROVADO) ||( alunoTurma.getSituacao()==Situacao.DISPENSADO) 
			|| (alunoTurma.getSituacao()==Situacao.REPROVADO_POR_MEDIA)){
			return String.valueOf(alunoTurma.getMedia());
		}
		return "-----";
	}
	
	public int geraTrancamentosParciais(List<AlunoTurma> ats){
		int trancamentosParciais = 0;
		if(ats != null){
			for(int i=0; i < ats.size(); i++){
				System.out.println("situacao----------------------"+ats.get(i).getSituacao());
				if(ats.get(i).getSituacao()==Situacao.TRANCADO){
					trancamentosParciais++;
				}
			}
			
		}
		return trancamentosParciais;
	}
		
	public List<AlunoTurma> getObrigatoriasOrdenadas(List<AlunoTurma> alunoTurma){
		
		List<AlunoTurma> disciplinas = getDisciplinasOrdenadas(alunoTurma);
		
		for(int i = 0; i < disciplinas.size(); i++){
			if(!isDisciplinaObrigatoria(disciplinas.get(i).getTurma().getDisciplina())){
				disciplinas.remove(i--);
			}
		}
		
		return disciplinas;
	}
	
	public List<AlunoTurma> getOptativasOrdenadas(List<AlunoTurma> alunoTurma){
		
		List<AlunoTurma> disciplinas = getDisciplinasOrdenadas(alunoTurma);
		
		for(int i = 0; i < disciplinas.size(); i++){
			if(!isDisciplinaOptativa(disciplinas.get(i).getTurma().getDisciplina())){
				disciplinas.remove(i--);
			}
		}
		
		return disciplinas;
	}
	
	public List<AlunoTurma> getComplementaresOrdenadas(List<AlunoTurma> alunoTurma){
		
		List<AlunoTurma> disciplinas = getDisciplinasOrdenadas(alunoTurma);
		
		for(int i = 0; i < disciplinas.size(); i++){
			if(!isDisciplinaComplementar(disciplinas.get(i).getTurma().getDisciplina())){
				disciplinas.remove(i--);
			}
		}
		
		return disciplinas;
	}
	
	
	public List<AlunoTurma> getDisciplinasOrdenadas(List<AlunoTurma> alunoTurma){
		ArrayList<AlunoTurma> lista  = new ArrayList<AlunoTurma>(alunoTurma);
		LinkedList<AlunoTurma> ordenada = new LinkedList<AlunoTurma>();
		
		for (AlunoTurma at : lista) {
			if (ordenada.size() == 0) {
				ordenada.add(at);
			}
			else {
				int anoNaoOrdenado = Integer.parseInt(at.getTurma().getPeriodo().getAno()), 
				periodoNaoOrdenado = (int) at.getTurma().getPeriodo().getSemestre(), 
				anoOrdenado, periodoOrdenado;
				boolean entrou = false;
				
				int anoAtual = Integer.parseInt(ordenada.get(0).getTurma().getPeriodo().getAno());
				for (int i = 0; i < ordenada.size(); i++) {
					anoOrdenado = Integer.parseInt(ordenada.get(i).getTurma().getPeriodo().getAno()); 
					periodoOrdenado = (int) ordenada.get(i).getTurma().getPeriodo().getSemestre(); 
					if(anoAtual < anoOrdenado){
						anoAtual = anoOrdenado;
					}
					if (anoNaoOrdenado < anoOrdenado) {
						ordenada.add(i, at);
						entrou = true;
						break;
					}
					else if (anoNaoOrdenado == anoOrdenado) {
						if (periodoNaoOrdenado == periodoOrdenado) {
							if (anoAtual == anoOrdenado) {
								ordenada.add(i, at);
								entrou = true;
								break;
							}
						}
						else {
							if (periodoNaoOrdenado > periodoOrdenado) {
								ordenada.add(i + 1, at);
								entrou = true;
								break;
							}
							else {
								ordenada.add(i, at);
								entrou = true;
								break;
							}
						}
					}
					
				}
				if (!entrou) {
					ordenada.add(at);
				}
			}
		}
		return ordenada;
	}
	
	public int geraSemestresCursados (List<AlunoTurma> alunoTurmas){
		
		if(alunoTurmas.size()==0){
			return 0;
		}
		
		int anoAtual = Integer.parseInt(alunoTurmas.get(0).getTurma().getPeriodo().getAno());
		int semestreAtual = (int)(alunoTurmas.get(0).getTurma().getPeriodo().getSemestre());
		int semestresCursados = 1;
		
		for(int i = 0; i < alunoTurmas.size(); i++) {
			int ano = Integer.parseInt(alunoTurmas.get(i).getTurma().getPeriodo().getAno()), 
			semestre = (int) alunoTurmas.get(i).getTurma().getPeriodo().getSemestre();
			
			if(ano != anoAtual || semestre != semestreAtual){
				semestresCursados++;
				anoAtual = ano;
				semestreAtual = semestre;

			}
		}
		return semestresCursados;
	}
	
	public int geraSemestresAtivos(List<AlunoTurma> alunoTurmas){
		
		int semestresAtivos = geraSemestresCursados(alunoTurmas) - geraTrancamentosTotais(alunoTurmas);
		return semestresAtivos;
	}
	
	public int geraTrancamentosTotais(List<AlunoTurma> alunoTurmas){
		
		ArrayList<AlunoTurma> lista  = new ArrayList<AlunoTurma>(alunoTurmas);
		LinkedList<AlunoTurma> listaPeriodo = new LinkedList<AlunoTurma>();
		int trancamentosParciais = 0;
		int trancamentosTotais = 0;
		int anoAtual = Integer.parseInt(alunoTurmas.get(0).getTurma().getPeriodo().getAno());
		int semestreAtual = (int)(alunoTurmas.get(0).getTurma().getPeriodo().getSemestre());
		
		for (int i = 0; i < lista.size(); i++) {
			int anoLista = Integer.parseInt(lista.get(i).getTurma().getPeriodo().getAno()), 
			semestreLista = (int) lista.get(i).getTurma().getPeriodo().getSemestre();
			if(anoLista == anoAtual && semestreLista == semestreAtual){
				listaPeriodo.add(lista.get(i));
				if(lista.get(i).getSituacao()==Situacao.TRANCADO){
					trancamentosParciais++; 
				}
			}
			else {
				anoAtual = anoLista;
				semestreAtual = semestreLista;
				if(trancamentosParciais == listaPeriodo.size()){
					trancamentosTotais++;
				}
				trancamentosParciais = 0;
				listaPeriodo.clear();
				i--;
			}
		}
		return trancamentosTotais;
	}
	public Boolean isDisciplinaObrigatoria(Disciplina disciplina){
		if(disciplina.getTipo()==Tipo.OBRIGATORIA){
			return true;
		}
		else 
			return false;
	}
	
	public Boolean isDisciplinaOptativa(Disciplina disciplina){
		if(disciplina.getTipo()==Tipo.OPTATIVA){
			return true;
		}
		else 
			return false;
	}
	
	public Boolean isDisciplinaComplementar(Disciplina disciplina){
		if(disciplina.getTipo()==Tipo.COMPLEMENTAR){
			return true;
		}
		else 
			return false;
	}
	
	public String ultimoPeriodoCursado(List<AlunoTurma> alunoTurmas){
		if(alunoTurmas!=null){
			String periodo = alunoTurmas.get(alunoTurmas.size()-1).getTurma().getPeriodo().getAno()+"."+
							alunoTurmas.get(alunoTurmas.size()-1).getTurma().getPeriodo().getSemestre();
			return periodo;
		}
		else
			return null;
	}
	
	public void exportarPDF() {
		System.out.println("***********************************geraTabelaHoratio");
		ArrayList<HashMap<String, String>> mapas = new ArrayList<HashMap<String, String>>();
		//Numero - Codigo - Nome da disciplina - Creditos - CargaHoraria - Horarios - Sala
		for (AlunoTurma at : alunoTurmas) {
			HashMap<String, String> mapa = new HashMap<String, String>();
			mapa.put("Numero", at.getTurma().getNumero());
			
			mapa.put("Codigo", at.getTurma().getDisciplina().getCodigo());
			
			mapa.put("Nome", at.getTurma().getDisciplina().getNome());

			String horarios = "";
			for (Horario h : matriculaBean.getHorariosOrdenados(at.getTurma().getHorarios())) {
				horarios += h.toString()+ "\n";
			}
			
			mapa.put("Horarios", horarios);
			
			String salas = "";
			
			List<Sala> salas_list = getSalasDoBanco(at.getTurma().getId());
			if(salas_list != null) {
				for (Sala s : salas_list) {
					salas += s.getSala() + "\n";
				}
			}
						
			mapa.put("Sala", salas);
			mapa.put("Turma", at.getTurma().getNumero());

			int creditos = at.getTurma().getDisciplina().getCreditos();
			mapa.put("Creditos", String.valueOf(creditos));
			System.out.println(at.getTurma().getNumero());
			mapa.put("Carga Horaria", String.valueOf(creditos*15));
			System.out.println(at.getTurma().getNumero());
			
			mapas.add(mapa);
		}
		
		pdfAction.geraPdf("Horario_Individual.pdf", mapas);
	}
	
	public void exportarHistoricoPDF() {
		ArrayList<HashMap<String, String>> mapas = new ArrayList<HashMap<String, String>>();
		
		// Matricula - Nome do Aluno - Codigo do Curso - Nome do Curso - Numero do Curriculo - Reconhecimento - RG
		HashMap<String, String> mapaCabecalho = new HashMap<String, String>();
		mapaCabecalho.put("Matricula", fachada.getAluno().getMatricula());
		mapaCabecalho.put("Nome", fachada.getUsuario().getNome());
		mapaCabecalho.put("Curso", fachada.getAluno().getCurriculo().getCurso().getCodigo());
		mapaCabecalho.put("NomeCurso", fachada.getAluno().getCurriculo().getCurso().getNome());
		mapaCabecalho.put("Curriculo", fachada.getAluno().getCurriculo().getNumero());
		mapaCabecalho.put("Reconhecimento", fachada.getAluno().getCurriculo().getCurso().getCur_ato_criacao());
		mapaCabecalho.put("RG", fachada.getUsuario().getRg());
		mapas.add(mapaCabecalho);
		
		List<AlunoTurma> aluno = getDisciplinasOrdenadas(alunoDAO.getAlunos(usuarioBean.getUsuario().getAluno().getMatricula()));		
		//CODIGO - NOME DA DISCIPLINA - CR - CH.- PERIODO - MEDIA -SITUACAO 
		//Disciplinas Obrigatorias
		for (AlunoTurma at : aluno) {
			if(at.getTurma().getDisciplina().getTipo()==Tipo.OBRIGATORIA){
				HashMap<String, String> mapaDisciplinasObrigatorias = new HashMap<String, String>();
				mapaDisciplinasObrigatorias.put("Codigo", at.getTurma().getDisciplina().getCodigo());
				mapaDisciplinasObrigatorias.put("Disciplina", at.getTurma().getDisciplina().getNome());
				int creditos = at.getTurma().getDisciplina().getCreditos();
				mapaDisciplinasObrigatorias.put("Creditos", String.valueOf(creditos));
				mapaDisciplinasObrigatorias.put("Carga Horaria", String.valueOf(creditos*15));			
				mapaDisciplinasObrigatorias.put("Periodo", at.getTurma().getPeriodo().getAno().concat(" "+ at.getTurma().getPeriodo().getSemestre()));
				mapaDisciplinasObrigatorias.put("Media",turmasMatriculadasBean.geraMedia(at)+"");
				mapaDisciplinasObrigatorias.put("Situacao", at.getSituacao()+"");
						
				mapas.add(mapaDisciplinasObrigatorias);
			}
		}
		
		//Disciplinas Optativas
		for (AlunoTurma at : aluno) {
			if(at.getTurma().getDisciplina().getTipo()==Tipo.OPTATIVA){
				HashMap<String, String> mapaDisciplinasOptativas = new HashMap<String, String>();
				mapaDisciplinasOptativas.put("Codigo", at.getTurma().getDisciplina().getCodigo());
				mapaDisciplinasOptativas.put("Disciplina", at.getTurma().getDisciplina().getNome());
				int creditos = at.getTurma().getDisciplina().getCreditos();
				mapaDisciplinasOptativas.put("Creditos", String.valueOf(creditos));
				mapaDisciplinasOptativas.put("Carga Horaria", String.valueOf(creditos*15));			
				mapaDisciplinasOptativas.put("Periodo", at.getTurma().getPeriodo().getAno().concat(" "+ at.getTurma().getPeriodo().getSemestre()));
				mapaDisciplinasOptativas.put("Media",turmasMatriculadasBean.geraMedia(at)+"");
				mapaDisciplinasOptativas.put("Situacao", at.getSituacao()+"");
						
				mapas.add(mapaDisciplinasOptativas);
			}
		}
		
		//Disciplinas Complementares
		for (AlunoTurma at : aluno) {
			if(at.getTurma().getDisciplina().getTipo()==Tipo.COMPLEMENTAR){
				HashMap<String, String> mapaDisciplinasComplementares = new HashMap<String, String>();
				mapaDisciplinasComplementares.put("Codigo", at.getTurma().getDisciplina().getCodigo());
				mapaDisciplinasComplementares.put("Disciplina", at.getTurma().getDisciplina().getNome());
				int creditos = at.getTurma().getDisciplina().getCreditos();
				mapaDisciplinasComplementares.put("Creditos", String.valueOf(creditos));
				mapaDisciplinasComplementares.put("Carga Horaria", String.valueOf(creditos*15));			
				mapaDisciplinasComplementares.put("Periodo", at.getTurma().getPeriodo().getAno().concat(" "+ at.getTurma().getPeriodo().getSemestre()));
				mapaDisciplinasComplementares.put("Media",turmasMatriculadasBean.geraMedia(at)+"");
				mapaDisciplinasComplementares.put("Situacao", at.getSituacao()+"");
						
				mapas.add(mapaDisciplinasComplementares);
			}
		}
		//Integralizacoes Disciplina Obrigatoria
		HashMap<String, String> mapaIntegralizacaoObrigatoria = new HashMap<String, String>();
		mapaIntegralizacaoObrigatoria.put("Carga Horaria Minima", cargaHoraria(fachada.getAluno().getCurriculo().getMinimoCreditosObrigatorias())+"");
		mapaIntegralizacaoObrigatoria.put("Integralizada", cargaHoraria(geraCreditosIntegralizadosObrigatorias(getObrigatoriasOrdenadas(alunoDAO.getAlunos(usuarioBean.getUsuario().getAluno().getMatricula()))))+"");
		mapaIntegralizacaoObrigatoria.put("Creditos Minimo", fachada.getAluno().getCurriculo().getMinimoCreditosObrigatorias()+"");
		mapaIntegralizacaoObrigatoria.put("IntegralizadoCredito", geraCreditosIntegralizadosObrigatorias(getObrigatoriasOrdenadas(alunoDAO.getAlunos(usuarioBean.getUsuario().getAluno().getMatricula())))+"");
		mapaIntegralizacaoObrigatoria.put("Disciplinas Minimo", fachada.getAluno().getCurriculo().getMinimoDisciplinasObrigatorias()+"");
		mapaIntegralizacaoObrigatoria.put("IntegralizadoDisciplina", geraDisciplinasIntegralizadasObrigatorias(getObrigatoriasOrdenadas(alunoDAO.getAlunos(usuarioBean.getUsuario().getAluno().getMatricula())))+"");
		mapas.add(mapaIntegralizacaoObrigatoria);
		
		//Integralizacoes Disciplina Optativa
		HashMap<String, String> mapaIntegralizacaoOptativa = new HashMap<String, String>();
		mapaIntegralizacaoOptativa.put("Carga Horaria Minima", cargaHoraria(fachada.getAluno().getCurriculo().getMinimoCreditosOptativas())+"");
		mapaIntegralizacaoOptativa.put("Integralizada", cargaHoraria(geraCreditosIntegralizadosOptativas(getOptativasOrdenadas(alunoDAO.getAlunos(usuarioBean.getUsuario().getAluno().getMatricula()))))+"");
		mapaIntegralizacaoOptativa.put("Creditos Minimo", fachada.getAluno().getCurriculo().getMinimoCreditosOptativas()+"");
		mapaIntegralizacaoOptativa.put("IntegralizadoCredito", geraCreditosIntegralizadosOptativas(getOptativasOrdenadas(alunoDAO.getAlunos(usuarioBean.getUsuario().getAluno().getMatricula())))+"");
		mapaIntegralizacaoOptativa.put("Disciplinas Minimo", fachada.getAluno().getCurriculo().getMinimoDisciplinasOptativas()+"");
		mapaIntegralizacaoOptativa.put("IntegralizadoDisciplina", geraDisciplinasIntegralizadasOptativas(getOptativasOrdenadas(alunoDAO.getAlunos(usuarioBean.getUsuario().getAluno().getMatricula())))+"");
		mapas.add(mapaIntegralizacaoOptativa);
		
		//Integralizacoes Disciplina Complementar
		HashMap<String, String> mapaIntegralizacaoComplementar = new HashMap<String, String>();
		mapaIntegralizacaoComplementar.put("Carga Horaria Minima", cargaHoraria(fachada.getAluno().getCurriculo().getMinimoCreditosComplementares())+"");
		mapaIntegralizacaoComplementar.put("Integralizada", cargaHoraria(geraCreditosIntegralizadosComplementares(getComplementaresOrdenadas(alunoDAO.getAlunos(usuarioBean.getUsuario().getAluno().getMatricula()))))+"");
		mapaIntegralizacaoComplementar.put("Creditos Minimo", fachada.getAluno().getCurriculo().getMinimoCreditosComplementares()+"");
		mapaIntegralizacaoComplementar.put("IntegralizadoCredito", geraCreditosIntegralizadosComplementares(getComplementaresOrdenadas(alunoDAO.getAlunos(usuarioBean.getUsuario().getAluno().getMatricula())))+"");
		mapaIntegralizacaoComplementar.put("Disciplinas Minimo", fachada.getAluno().getCurriculo().getMinimoDisciplinasComplementares()+"");
		mapaIntegralizacaoComplementar.put("IntegralizadoDisciplina", geraDisciplinasIntegralizadasComplementares(getComplementaresOrdenadas(alunoDAO.getAlunos(usuarioBean.getUsuario().getAluno().getMatricula())))+"");
		mapas.add(mapaIntegralizacaoComplementar);
		
		//Integralizacao Total
		HashMap<String, String> mapaIntegralizacaoTotal = new HashMap<String, String>();
		mapaIntegralizacaoTotal.put("Carga Horaria Minima", cargaHoraria(geraMinimoCreditosCurriculo())+"");
		mapaIntegralizacaoTotal.put("Integralizada", cargaHoraria(geraCreditosIntegralizados(getDisciplinasOrdenadas(alunoDAO.getAlunos(usuarioBean.getUsuario().getAluno().getMatricula()))))+"");
		mapaIntegralizacaoTotal.put("Creditos Minimo", geraMinimoCreditosCurriculo()+"");
		mapaIntegralizacaoTotal.put("IntegralizadoCredito", geraCreditosIntegralizados(getDisciplinasOrdenadas(alunoDAO.getAlunos(usuarioBean.getUsuario().getAluno().getMatricula())))+"");
		mapaIntegralizacaoTotal.put("Disciplinas Minimo", geraMinimoDisciplinasCurriculo()+"");
		mapaIntegralizacaoTotal.put("IntegralizadoDisciplina", geraDisciplinasIntegralizadas(turmasMatriculadasBean.getDisciplinasOrdenadas(alunoDAO.getAlunos(usuarioBean.getUsuario().getAluno().getMatricula())))+"");
		mapas.add(mapaIntegralizacaoTotal);

		//Semestres cursados
		HashMap<String, String> mapaSemestresCursados = new HashMap<String, String>();
		mapaSemestresCursados.put("Cursados", geraSemestresCursados(turmasMatriculadasBean.getDisciplinasOrdenadas(alunoDAO.getAlunos(usuarioBean.getUsuario().getAluno().getMatricula())))+"");
		mapaSemestresCursados.put("Minimo", fachada.getAluno().getCurriculo().getMinimoSemestres()+"");
		mapaSemestresCursados.put("Maximo", fachada.getAluno().getCurriculo().getMaximoSemestres()+"");
		mapaSemestresCursados.put("Ativos", geraSemestresAtivos(turmasMatriculadasBean.getDisciplinasOrdenadas(alunoDAO.getAlunos(usuarioBean.getUsuario().getAluno().getMatricula())))+"");
		mapas.add(mapaSemestresCursados);
		
		//Trancamentos Totais
		HashMap<String, String> mapaTrancamentosTotais = new HashMap<String, String>();
		mapaTrancamentosTotais.put("Trancamentos", geraTrancamentosTotais(getDisciplinasOrdenadas(alunoDAO.getAlunos(usuarioBean.getUsuario().getAluno().getMatricula())))+"");
		mapaTrancamentosTotais.put("Maximo", fachada.getAluno().getCurriculo().getMaximoTrancamentosTotais()+"");
		mapas.add(mapaTrancamentosTotais);
		
		//Matriculas Institucionais
		HashMap<String, String> mapaMatriculasInstitucionais = new HashMap<String, String>();
		mapaMatriculasInstitucionais.put("Matriculas", fachada.getAluno().getMatriculasInstitucionais()+"");
		mapaMatriculasInstitucionais.put("Maximo", fachada.getAluno().getCurriculo().getMaximoMatriculaInstitucional()+"");
		mapas.add(mapaMatriculasInstitucionais);

		//Trancamentos Parciais
		HashMap<String, String> mapaTrancamentosParciais = new HashMap<String, String>();
		mapaTrancamentosParciais.put("Trancamentos", geraTrancamentosParciais(getDisciplinasOrdenadas(alunoDAO.getAlunos(usuarioBean.getUsuario().getAluno().getMatricula())))+"");
		mapaTrancamentosParciais.put("Maximo", fachada.getAluno().getCurriculo().getMaximoTrancamentosParciais()+"");
		mapas.add(mapaTrancamentosParciais);
		
		//Creditos Matriculados
		HashMap<String, String> mapaCreditosMatriculados = new HashMap<String, String>();
		mapaCreditosMatriculados.put("Matriculados", geraCreditosPeriodoAtual(getDisciplinasOrdenadas(alunoDAO.getAlunos(usuarioBean.getUsuario().getAluno().getMatricula())))+"");
		mapaCreditosMatriculados.put("Minimo", fachada.getAluno().getCurriculo().getMinimoCreditos()+"");
		mapaCreditosMatriculados.put("Maximo", fachada.getAluno().getCurriculo().getMaximoCreditos()+"");
		mapas.add(mapaCreditosMatriculados);
		
		//Situacao Academica
		HashMap<String, String> mapaSituacaoAcademica = new HashMap<String, String>();
		mapaSituacaoAcademica.put("Situacao", fachada.getAluno().getSituacaoAcademica()+"");
		if(fachada.getAluno().getSituacaoAcademica()==SituacaoAcademica.GRADUADO){
			mapaSituacaoAcademica.put("Ano de Conclusao", ultimoPeriodoCursado(getDisciplinasOrdenadas(alunoDAO.getAlunos(usuarioBean.getUsuario().getAluno().getMatricula()))).
					  concat(" "+fachada.getAluno().getDataConclusao()));
		}
		mapaSituacaoAcademica.put("CRE", geraCRE(getDisciplinasOrdenadas(alunoDAO.getAlunos(usuarioBean.getUsuario().getAluno().getMatricula())))+"");
		mapaSituacaoAcademica.put("Forma de Ingresso", fachada.getAluno().getFormaIngresso()+"");
		mapaSituacaoAcademica.put("Ano Ingresso", getDisciplinasOrdenadas(alunoDAO.getAlunos(usuarioBean.getUsuario().getAluno().getMatricula())).get(0).getTurma().getPeriodo().getAno().
								  concat(" "+getDisciplinasOrdenadas(alunoDAO.getAlunos(usuarioBean.getUsuario().getAluno().getMatricula())).get(0).getTurma().getPeriodo().getSemestre()));
		mapas.add(mapaSituacaoAcademica);
		
		//Notas do Vestibular
		if(fachada.getAluno().getFormaIngresso()==FormaIngresso.VESTIBULAR){
			
		}
		System.out.println("¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬" + mapas.size());
		pdfAction.geraPdfHistorico("Historico_Escolar.pdf", mapas);
	}

}
