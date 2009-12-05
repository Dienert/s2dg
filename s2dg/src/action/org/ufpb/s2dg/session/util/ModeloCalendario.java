package org.ufpb.s2dg.session.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.richfaces.model.CalendarDataModel;
import org.richfaces.model.CalendarDataModelItem;
import org.ufpb.s2dg.entity.Avaliacao;
import org.ufpb.s2dg.entity.Calendario;
import org.ufpb.s2dg.entity.EventoCalendarioTurma;
import org.ufpb.s2dg.entity.Horario;
import org.ufpb.s2dg.entity.Turma;
import org.ufpb.s2dg.session.Fachada;
import org.ufpb.s2dg.session.beans.UsuarioBean.TipoAbaAtiva;
import org.ufpb.s2dg.session.util.ItemDeCalendario.TipoData;

@Name("modeloCalendario")
@AutoCreate
public class ModeloCalendario implements CalendarDataModel, Serializable {
	ItemDeCalendario[] itens;
	private static final long serialVersionUID = 1L;
	
	@In
	Fachada fachada;
	
	public ModeloCalendario(){
		
	}
	
	public CalendarDataModelItem[] getData(Date[] datas) {
		if (datas == null) return null;
		
		/* TODO Clodoaldo: 
		 * Adicionar escolha de calendario de acordo com usuario logado.
		 * É preciso checar qual é o perfil ativo - professor ou aluno?
		 * Para teste, está sendo pego calendario de aluno.
		 * Se usar usuario professor vai dar pau, deve-se trocar aqui antes.
		*/
		Calendario calendario = null;
		if(fachada.getAbaAtiva() == TipoAbaAtiva.DISCENTE)
			calendario = fachada.getCalendarioAluno();
		if(fachada.getAbaAtiva() == TipoAbaAtiva.DOCENTE)
			calendario = fachada.getCalendarioProfessor();
		
		
		itens = new ItemDeCalendario[datas.length];
		Calendar c = Calendar.getInstance();
		c.setTime(datas[0]);
		
		// Pegando dias da semana com aula	
		// datasComAula(datas);
		
		
		List<EventoCalendarioTurma> datasEEventos = new ArrayList<EventoCalendarioTurma>();
		List<Avaliacao> avaliacoes = fachada.getAvaliacoes();
		
		if(avaliacoes != null) {
			for(Avaliacao a : avaliacoes) {
				EventoCalendarioTurma de = a.getDataEvento(); 
				if(de != null)
					datasEEventos.add(de);
			}
		}
		
		String eventos = "";
		TipoData tipoData = null;
		for (int i = 0; i < datas.length; i++) {
			if (datas[i] != null) {
				
				if(equals(datas[i], calendario.getFimImplantacaoNotas())) {
					eventos += "Fim de Implantação de Notas\n";
					tipoData = TipoData.EVENTO_PERIODO;
				}
				if(equals(datas[i], calendario.getFimMatricula())) {
					eventos += "Fim do Período de Matrícula\n";
					tipoData = TipoData.EVENTO_PERIODO;
				}
				if(equals(datas[i], calendario.getFimPeriodo())) {
					eventos += "Fim do Período\n";
					tipoData = TipoData.EVENTO_PERIODO;
				}
				if(equals(datas[i], calendario.getInicioMatricula())){
					eventos += "Início do Período de Matrícula\n";
					tipoData = TipoData.EVENTO_PERIODO;
				}
				if(equals(datas[i], calendario.getInicioPeriodo())){
					eventos += "Início do Período\n";
					tipoData = TipoData.EVENTO_PERIODO;
				}
				if(equals(datas[i], calendario.getUltimoDiaTrancamento())) {
					eventos += "Último Dia de Trancamento\n";
					tipoData = TipoData.EVENTO_PERIODO;
				}
				
				if(datasEEventos != null) {
					for (int j = 0; j < datasEEventos.size(); j++) {
						if (datas[i].equals(datasEEventos.get(j).getData())) {
							eventos += datasEEventos.remove(j--).getEvento() + "\n";
							tipoData = TipoData.AVALIACAO;
						}
					}
				}
				c.setTime(datas[i]);
				
				if (eventos.equals("")) {
					itens[i] = new ItemDeCalendario(c.get(Calendar.DAY_OF_MONTH));
				}
				else {
					itens[i] = new ItemDeCalendario(eventos, c.get(Calendar.DAY_OF_MONTH), tipoData);
					eventos = "";
					tipoData = null;
				}
			}
		}
		datasComAula(datas);
		return itens;
	}


	private void datasComAula(Date[] datas) {
		
		Calendar c = Calendar.getInstance();
		// TODO Clodoaldo: Analisar porque fachada está toda nula aqui!
		Turma t = fachada.getTurmaDisciplinaSelecionada();
		Set<Horario> horariosDeAula = null;
		if (t != null)
			horariosDeAula = t.getHorarios();
		else
			horariosDeAula = new HashSet<Horario>();
		
		for(int i = 0; i < datas.length; i++){
			
			c.setTime(datas[i]);
			for (Horario h: horariosDeAula){
				if(c.get(Calendar.DAY_OF_WEEK) == h.getDia().ordinal()+1){
					String evento = (String) itens[i].getData() + h.horarioFormatado();
					evento += "\nEditar dia";
					itens[i].setData(evento);
					if(itens[i].getTipo() != null && 
							itens[i].getTipo().ordinal() < TipoData.DIA_AULA_DISCIPLINA.ordinal() ||
							itens[i].getTipo() == null)
						itens[i].setTipo(TipoData.DIA_AULA_DISCIPLINA);
				}
			}
		}
		
	}


	public Object getToolTip(Date arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean equals(Date d1, Date d2){
		long dif = d1.getTime() - d2.getTime();
		return dif >= 0 && dif < 24*60*60*1000;
	}
	
	/*private ItemDeCalendario criarItemDeCalendario(Date data) {
		ItemDeCalendario item = new ItemDeCalendario();
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		int dia = c.get(Calendar.DAY_OF_MONTH);
		item.setDay(dia);
		
		//puxar do banco
		ArrayList<DataEvento> datasEEventos = new DataEventoDAO().getDataEvento(c.get(Calendar.MONTH) + 1, 
				c.get(Calendar.YEAR) + 1969);
		String eventos = "";
        
        for (DataEvento de : datasEEventos) {
        	eventos += de.getEvento() + "\n"; //dado puxado do banco
        }
        item.setData(eventos);
        return item;
	}*/
}
