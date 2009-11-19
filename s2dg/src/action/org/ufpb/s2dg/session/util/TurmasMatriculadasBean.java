package org.ufpb.s2dg.session.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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
import org.ufpb.s2dg.entity.AlunoTurma.Situacao;
import org.ufpb.s2dg.entity.Disciplina.Tipo;
import org.ufpb.s2dg.session.Fachada;
import org.ufpb.s2dg.session.beans.AlunoBean;
import org.ufpb.s2dg.session.beans.MatriculaBean;
import org.ufpb.s2dg.session.beans.UsuarioBean;
import org.ufpb.s2dg.session.persistence.AlunoDAO;

@Name("turmasMatriculadasBean")
@Scope(ScopeType.SESSION)
@AutoCreate
public class TurmasMatriculadasBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	
	@In
	AlunoBean alunoBean;
	
	@In
	MenuAction MenuAction;
		
	public void init() {
		List<AlunoTurma> ats = fachada.getAlunoTurmaDoBanco();
		if(ats != null) {
			if (ats.size() > 0) {
				fachada.setAlunoTurma(ats.get(0));
				this.alunoTurmas = ats;
				
				for(AlunoTurma alunoTurma : alunoTurmas) {
					if (alunoTurma != null) {
						Turma t = alunoTurma.getTurma();
						if (t == null)
							t = fachada.getTurmaDoBanco(alunoTurma);
						Disciplina d = t.getDisciplina();
						if (d == null)
							d = fachada.getDisciplinaDoBanco(t);
						t.setDisciplina(d);
						alunoTurma.setTurma(t);
					}
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
			if (alunoTurmas.get(i) != null) {
				//if (alunoTurmas.get(i).getSituacao() == Situacao.EM_CURSO) {
				if (alunoTurmas.get(i).getSituacao().compareTo(Situacao.EM_CURSO)==0) {
					alunos.add(alunoTurmas.get(i));
				}
			}
		}
		return alunos;
	}
	
	public void setAlunoTurmas(List<AlunoTurma> alunoTurmas) {
		this.alunoTurmas = alunoTurmas;
	}
	
	public List<AlunoTurma> getAluno(){
		List<AlunoTurma> aluno = alunoDAO.getAlunos(usuarioBean.getUsuario().getAluno().getMatricula());
		return aluno;
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
		int anoAtual = Integer.parseInt(fachada.getGlobalDoBanco().getPeriodoAtual().getAno());
        int semestreAtual = (int)(fachada.getGlobalDoBanco().getPeriodoAtual().getSemestre());
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
	public String geraCRE(List<AlunoTurma> ats){
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
		if(somaCreditos == 0){
			return "-----";
		}
		return (String.format("%.2f",(somaMedias/somaCreditos)).replace('.', ','));
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
			return (String.format("%.2f",alunoTurma.getMedia())).replace('.', ',');
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
	public String geraMediaVestibular(){

		float media = 0;
		int cont = 0;
		try{
			media += fachada.getAluno().getValorProva1();
			cont++;
			media += fachada.getAluno().getValorProva2();
			cont++;
			media += fachada.getAluno().getValorProva3();
			cont++;
			media += fachada.getAluno().getValorProva4();
			cont++;
			media += fachada.getAluno().getValorProva5();
			cont++;
			media += fachada.getAluno().getValorProva6();
			cont++;
			media += fachada.getAluno().getValorProva7();
			cont++;
			media += fachada.getAluno().getValorProva8();
			cont++;
			media += fachada.getAluno().getValorProva9();
			cont++;
		} catch (Exception e) {}
		
		if(cont == 0)
			return "------";
		else
			return (String.format("%.2f", media/cont)).replace('.', ',');
	}
	
	private String getEspacos(int nEspacos){
		String espaco = "&nbsp;", retorno = "";
		while(nEspacos-- > 0){
			retorno += espaco;
		}
		return retorno;		
	}
	
		
	public LinkedList<String> geraHistorico(){
		
		LinkedList<String> lista = new LinkedList<String>();
		
		lista = addCabecalhoHistorico(lista);	
		lista = addObrigatorias(lista);
		lista = addOptativas(lista);
		lista = addComplementares(lista);
				
		lista.add("");
		lista.add("=============================================================================");
		lista.add("");		
		
		try {
			lista = addDadosDeIntegralizacaoDisciplinas(lista);
			lista = addDadosDeIntegralizacaoSemestres(lista);
			lista = addDadosVestibular(lista);
		} catch(Exception ex){
			lista.add("");
			lista.add("Banco inconsistente, favor corrigir!");
			lista.add("");			
		}
		
		lista.add("---------------------------------------------------------------------------");
		
		lista.add("A MATRICULA E OBRIGATÓRIA EM TODO PERÍODO LETIVO,EVITE SITUAÇÃO DE ABANDONO");
		lista.add("");
		lista.add("");
		lista.add("HISTORICO EMITIDO PARA SIMPLES CONFERENCIA. NAO VALE COMO DOCUMENTO OFICIAL");
		
		return lista;
	}
	
	private LinkedList<String> addCabecalhoHistorico(LinkedList<String> lista){

		String historico = getEspacos(25) + "H I S T O R I C O"+ getEspacos(3)+"E S C O L A R";//25 espacos
        lista.add(historico);
        lista.add(""); // linha em branco
        try{
	        historico = "ALUNO: "+fachada.getAluno().getMatricula()+"--"+fachada.getUsuario().getNome();
	        lista.add(historico.toUpperCase());
			historico = "CURSO: "+fachada.getAluno().getCurriculo().getCurso().getCodigo()+"--"+fachada.getAluno().getCurriculo().getCurso().getNome();
			historico += " CURRICULO: "+fachada.getAluno().getCurriculo().getNumero();
			lista.add(historico.toUpperCase());
			historico = "RECONHECIMENTO: "+fachada.getAluno().getCurriculo().getCurso().getCur_ato_criacao()+"  "+"RG: "+fachada.getUsuario().getRg();
			lista.add(historico.toUpperCase());
        }catch(Exception ex){
        	lista.add("Banco inconsistente, favor corrigir!");
        }
        lista.add("");
		historico = String.format("CODIGO%sNOME DA DISCIPLINA%sCR CH. PERIOD MEDIA SITUACAO", getEspacos(2), getEspacos(23));
		lista.add(historico);
				
		return lista;
	}
	
	private LinkedList<String> addObrigatorias(LinkedList<String> lista){
		String historico = "=====================     DISCIPLINAS OBRIGATORIAS     ======================";
		lista.add(historico.replaceAll(" ", getEspacos(1)));		
		List<AlunoTurma> ats = getObrigatoriasOrdenadas(getAluno());
		return addListaDisciplinas(lista, ats);
	}
	
	private LinkedList<String> addOptativas(LinkedList<String> lista){
		String historico = "=====================      DISCIPLINAS OPTATIVAS      =======================";
		lista.add(historico.replaceAll(" ", getEspacos(1)));		
		List<AlunoTurma> ats = getOptativasOrdenadas(getAluno());
		return addListaDisciplinas(lista, ats);
	}

	private LinkedList<String> addComplementares(LinkedList<String> lista){
		String historico = "=====================    DISCIPLINAS COMPLEMENTARES   =======================";
		lista.add(historico.replaceAll(" ", getEspacos(1)));		
		List<AlunoTurma> ats = getComplementaresOrdenadas(getAluno());
		return addListaDisciplinas(lista, ats);
	}
	
	private LinkedList<String> addListaDisciplinas(LinkedList<String> lista, List<AlunoTurma> ats){
		String linha, codigo, nome, ano, media, situacao;
		int creditos, cargaHoraria, semestre;
		for(int i = 0; i < ats.size(); i++){
			codigo = ats.get(i).getTurma().getDisciplina().getCodigo();
			nome = ats.get(i).getTurma().getDisciplina().getNome();
			creditos = ats.get(i).getTurma().getDisciplina().getCreditos();
			cargaHoraria = cargaHoraria(creditos);
			ano = ats.get(i).getTurma().getPeriodo().getAno();
			semestre = ats.get(i).getTurma().getPeriodo().getSemestre();
			media = geraMedia(ats.get(i));
			situacao = getTextoSituacao(ats.get(i).getSituacao());
			linha = String.format("%s %-41s %d %3d %s %c %5s %s", codigo, nome, creditos, cargaHoraria, ano, semestre, media, situacao);
			lista.add(linha.toUpperCase().replaceAll(" ", getEspacos(1)));
		}
		return lista;
	}
	
	private LinkedList<String> addDadosDeIntegralizacaoDisciplinas(LinkedList<String> lista){
		
		String historico = "DADOS INERENTES A INTEGRALZAÇÃO CURRICULAR";
		lista.add(historico);
		lista.add("");
		historico = "                             -CARGA HORARIA-  ---CREDITOS---  -DISCIPLINAS-";
		lista.add(historico.replaceAll(" ", getEspacos(1)));
		historico = "INTEGRALIZACAO CURRICULAR     Minimo Integr.  Minima Integr.  Minimo Integr";
		lista.add(historico.replaceAll(" ", getEspacos(1)));
		historico = "---------------------------------------------------------------------------";
		lista.add(historico);
		
		int chMin = cargaHoraria(fachada.getAluno().getCurriculo().getMinimoCreditosObrigatorias());
		int chInt = cargaHoraria(geraCreditosIntegralizadosObrigatorias(getObrigatoriasOrdenadas(getAluno())));
		int crMin = fachada.getAluno().getCurriculo().getMinimoCreditosObrigatorias();
		int crInt = geraCreditosIntegralizadosObrigatorias(getObrigatoriasOrdenadas(getAluno()));
		int dsMin = fachada.getAluno().getCurriculo().getMinimoDisciplinasObrigatorias();
		int dsInt = geraDisciplinasIntegralizadasObrigatorias(getObrigatoriasOrdenadas(getAluno()));
		
		historico = String.format("Disciplinas Obrigatorias...... %4d %7d %7d %7d %7d %6d", chMin, chInt, crMin, crInt, dsMin, dsInt);		
		lista.add(historico.replaceAll(" ", getEspacos(1)));
		
		chMin = cargaHoraria(fachada.getAluno().getCurriculo().getMinimoCreditosOptativas());
		chInt = cargaHoraria(geraCreditosIntegralizadosOptativas(getOptativasOrdenadas(getAluno())));
		crMin = fachada.getAluno().getCurriculo().getMinimoCreditosOptativas();
		crInt = geraCreditosIntegralizadosOptativas(getOptativasOrdenadas(getAluno()));
		dsMin = fachada.getAluno().getCurriculo().getMinimoDisciplinasOptativas();
		dsInt = geraDisciplinasIntegralizadasOptativas(getOptativasOrdenadas(getAluno()));
		
		historico = String.format("Disciplinas Optativas......... %4d %7d %7d %7d %7d %6d", chMin, chInt, crMin, crInt, dsMin, dsInt);
		lista.add(historico.replaceAll(" ", getEspacos(1)));
				
		historico = "Disciplinas Eletivas.......... ----       0    ----       0    ----      0";
		lista.add(historico.replaceAll(" ", getEspacos(1)));
		
		chMin = cargaHoraria(fachada.getAluno().getCurriculo().getMinimoCreditosComplementares());
		chInt = cargaHoraria(geraCreditosIntegralizadosComplementares(getComplementaresOrdenadas(getAluno())));
		crMin = fachada.getAluno().getCurriculo().getMinimoCreditosComplementares();
		crInt = geraCreditosIntegralizadosComplementares(getComplementaresOrdenadas(getAluno()));
		dsMin = fachada.getAluno().getCurriculo().getMinimoDisciplinasComplementares();
		dsInt = geraDisciplinasIntegralizadasComplementares(getComplementaresOrdenadas(getAluno()));
		
		historico = String.format("Disciplinas Complementares.... %4d %7d %7d %7d %7d %6d", chMin, chInt, crMin, crInt, dsMin, dsInt);
		lista.add(historico.replaceAll(" ", getEspacos(1)));
		
		historico = "TOTAIS DO CURRICULO =========> ";
		
		chMin = cargaHoraria(fachada.getAluno().getCurriculo().getMinimoCreditosCurriculo());
		chInt = cargaHoraria(geraCreditosIntegralizados(getDisciplinasOrdenadas(getAluno())));
		crMin = geraMinimoCreditosCurriculo();
		crInt = geraCreditosIntegralizados(getDisciplinasOrdenadas(getAluno()));
		dsMin = geraMinimoDisciplinasCurriculo();
		dsInt = geraDisciplinasIntegralizadas(getDisciplinasOrdenadas(getAluno()));
		
		historico = String.format("TOTAIS DO CURRICULO =========> %4d %7d %7d %7d %7d %6d", chMin, chInt, crMin, crInt, dsMin, dsInt);
		lista.add(historico.replaceAll(" ", getEspacos(1)));
		
		historico = "Disciplinas Extra-Curriculares ----       0    ----       0    ----      0";
		lista.add(historico.replaceAll(" ", getEspacos(1)));
		historico = "---------------------------------------------------------------------------";
		lista.add(historico);
		
		return lista;
	}

	private LinkedList<String> addDadosDeIntegralizacaoSemestres(LinkedList<String> lista){
		
		int semCursados = geraSemestresCursados(getDisciplinasOrdenadas(getAluno()));
		int minSemestres = fachada.getAluno().getCurriculo().getMinimoSemestres();
		int maxSemestres = fachada.getAluno().getCurriculo().getMaximoSemestres();
		int ativos = geraSemestresAtivos(getDisciplinasOrdenadas(getAluno()));
		String historico = String.format("Numero de semestres cursados.. %4d (Minimo: %2d, Maximo: %2d) de %d ativos", semCursados, minSemestres, maxSemestres, ativos);
		lista.add(historico.replaceAll(" ", getEspacos(1)));
		
		int trancTotais = geraTrancamentosTotais(getDisciplinasOrdenadas(getAluno()));
		int maxTrancs = fachada.getAluno().getCurriculo().getMaximoTrancamentosTotais();
		historico = String.format("Trancamentos Totais efetuados. %4d (Max: %d sem)", trancTotais, maxTrancs);
		lista.add(historico.replaceAll(" ", getEspacos(1)));
		
		
		int matInst = fachada.getAluno().getMatriculasInstitucionais();
		int maxInst = fachada.getAluno().getCurriculo().getMaximoMatriculaInstitucional();
		historico = String.format("Matriculas Institucionais .... %4d (Max: %d sem)", matInst, maxInst);
		lista.add(historico.replaceAll(" ", getEspacos(1)));
		
		
		int trancParc = geraTrancamentosParciais(getDisciplinasOrdenadas(getAluno()));
		maxTrancs = fachada.getAluno().getCurriculo().getMaximoTrancamentosParciais();
		historico = String.format("Trancamentos Parciais efetuad. %4d (Minimo: --, Maximo: %2d)", trancParc, maxTrancs);
		lista.add(historico.replaceAll(" ", getEspacos(1)));
		
		int crAtual = geraCreditosPeriodoAtual(getDisciplinasOrdenadas(getAluno()));
		int minCr = fachada.getAluno().getCurriculo().getMinimoCreditos();
		int maxCr = fachada.getAluno().getCurriculo().getMaximoCreditos();
		historico = String.format("Matriculado atualmente em .... %4d Creditos (Minimo: %4d, Maximo: %4d)", crAtual, minCr, maxCr);
		lista.add(historico.replaceAll(" ", getEspacos(1)));

		historico = "---------------------------------------------------------------------------";
		lista.add(historico);
		
		String sitAcad = fachada.getAluno().getSituacaoAcademica().toString();
		String cre = geraCRE(getDisciplinasOrdenadas(getAluno()));
		if(alunoBean.situacaoAcademicaGraduado())
			sitAcad += String.format("(em %s %s) ", ultimoPeriodoCursado(getDisciplinasOrdenadas(getAluno())), fachada.getAluno().getDataConclusao());
				
		historico = String.format("Situacao academica............ %-33s CRE: %5s", sitAcad, cre);
		lista.add(historico.replaceAll(" ", getEspacos(1)));
				
		historico = String.format("Forma de ingresso............. %s (em %s.%c)", fachada.getAluno().getFormaIngresso(), getDisciplinasOrdenadas(getAluno()).get(0).getTurma().getPeriodo().getAno(), getDisciplinasOrdenadas(getAluno()).get(0).getTurma().getPeriodo().getSemestre());
		lista.add(historico.replaceAll(" ", getEspacos(1)));
		
		return lista;		
	}
	
	private LinkedList<String> addDadosVestibular(LinkedList<String> lista){
		
		if(alunoBean.formaIngresso()){
			String historico = "----------------------- PROVAS E NOTAS DO VESTIBULAR ----------------------";
			lista.add(historico);
			for(int i = 0; i < 3; i++){
				historico = "";
				for(int j = 3*i+1; j <= 3*i+3; j++){
					historico = historico + nota(j);
				}
				lista.add(historico.toUpperCase().replaceAll(" ", getEspacos(1)));
			}			
			
			historico = "MEDIA GERAL......."+geraMediaVestibular();
			lista.add(historico);			
		}
		
		return lista;		
	}
	
	private String nota(int indice){
		String nome;
		int nota;
		switch(indice){
			case 1: nome = fachada.getAluno().getNomeProva1(); break; 
			case 2: nome = fachada.getAluno().getNomeProva2(); break;
			case 3: nome = fachada.getAluno().getNomeProva3(); break;
			case 4: nome = fachada.getAluno().getNomeProva4(); break;
			case 5: nome = fachada.getAluno().getNomeProva5(); break;
			case 6: nome = fachada.getAluno().getNomeProva6(); break;
			case 7: nome = fachada.getAluno().getNomeProva7(); break;
			case 8: nome = fachada.getAluno().getNomeProva8(); break;
			case 9: nome = fachada.getAluno().getNomeProva9(); break;
			default: nome = ""; break;
		}
		
		if(nome == null || nome.length() == 0)
			return "";
		
		switch(indice){
			case 1: nota = fachada.getAluno().getValorProva1(); break; 
			case 2: nota = fachada.getAluno().getValorProva2(); break;
			case 3: nota = fachada.getAluno().getValorProva3(); break;
			case 4: nota = fachada.getAluno().getValorProva4(); break;
			case 5: nota = fachada.getAluno().getValorProva5(); break;
			case 6: nota = fachada.getAluno().getValorProva6(); break;
			case 7: nota = fachada.getAluno().getValorProva7(); break;
			case 8: nota = fachada.getAluno().getValorProva8(); break;
			case 9: nota = fachada.getAluno().getValorProva9(); break;
			default: nota = 0; break;
		}
		return String.format("%-20s %3d ", nome, nota); 
	}
		
	public boolean naoVazio(List<AlunoTurma> at) {
		if (at == null) {
			MenuAction.setId_Menu(4);
			return false;
		}
		
		return true;
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
}
