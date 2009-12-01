package org.ufpb.s2dg.session.beans;

import java.util.Calendar;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.ufpb.s2dg.entity.AlunoTurma;
import org.ufpb.s2dg.entity.Calendario;
import org.ufpb.s2dg.entity.Global;
import org.ufpb.s2dg.entity.AlunoTurma.Situacao;
import org.ufpb.s2dg.session.Fachada;

@Name("globalBean")
@Scope(ScopeType.APPLICATION)
@AutoCreate
public class GlobalBean {

	@Logger
	private Log log;
	
	private Global global;
	@In
	private Fachada fachada;
	private Thread checarDataLimiteImplantacaoNotas = new Thread(){
		public void run(){
			while(true){
				List<Calendario> calendarios = fachada.getCalendariosDoBanco();
				for (Calendario c: calendarios) {
					if (atingiuDataLimiteImplantacao(c)) {	
						List<AlunoTurma> turmas = fachada.getAlunoTurmasDoBanco(c.getPeriodo(), c.getCentro());
						for(AlunoTurma at: turmas){
							if (at.getSituacao() == Situacao.EM_CURSO)
								registraNotaZero(at);
						}
					}
				}
				try {
					sleep(86400000); // Atualiza a cada dia
				} catch (InterruptedException e) {
					log.warn("	Thread checarDataLimiteImplantacaoNotas.run():" +
							 "\n			Thread de atualização de turmas matriculadas" +
						   	 "\n			irregulares interrompida.");
				}
			}
		}

		private void registraNotaZero(AlunoTurma atAberto) {
			atAberto.setMedia(0.0f);
			atAberto.setSituacao(Situacao.REPROVADO_POR_MEDIA);
			fachada.atualizaAlunoTurma(atAberto);
		}

		private boolean atingiuDataLimiteImplantacao(Calendario c) {
			return c.getFimImplantacaoNotas().before(Calendar.getInstance().getTime());
			/* TODO Mostrar alteração - que refatoracao é essa?
			 * if(c.getFimImplantacaoNotas().before(Calendar.getInstance().getTime())){
			 * 	return true;
			 * return false;
			 */
		}
	};
	
	@Create
	public void init(){
		//checarDataLimiteImplantacaoNotas.start();
	}
	
	public GlobalBean(){
		super();
	}

	public Global getGlobal() {
		return global;
	}

	public void setGlobal(Global global) {
		this.global = global;
	}
	
}
