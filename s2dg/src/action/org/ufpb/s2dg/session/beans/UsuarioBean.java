package org.ufpb.s2dg.session.beans;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.ufpb.s2dg.entity.AlunoTurma;
import org.ufpb.s2dg.entity.Calendario;
import org.ufpb.s2dg.entity.Centro;
import org.ufpb.s2dg.entity.Periodo;
import org.ufpb.s2dg.entity.Professor;
import org.ufpb.s2dg.entity.Role;
import org.ufpb.s2dg.entity.Usuario;
import org.ufpb.s2dg.session.Fachada;

@Name("usuarioBean")
@AutoCreate
@Scope(ScopeType.SESSION)
public class UsuarioBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Logger
	private Log log;

	private Usuario usuario;
	
	public enum TipoAbaAtiva { DOCENTE,	DISCENTE }
	
	private TipoAbaAtiva abaAtiva;
	
	@In
	Fachada fachada;
	
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
		defineCalendarios();	
	}
	
	private void defineCalendarios() {
		for(Role r: usuario.getRoles()){
        	defineCalendario(r);
        }
	}
	
	public Usuario getUsuarioProfessor() {
		AlunoTurma alunoTurmaAtual = fachada.getAlunoTurma();
		if (alunoTurmaAtual == null)
			return null;
		Professor professor = (Professor)alunoTurmaAtual.getTurma().getProfessores().toArray()[0];
		return fachada.getUsuarioProfessor(professor.getMatricula());
	}
	
	public void defineCalendario(Role tipoUsuario){
		if(tipoUsuario.getNome().equals("professor"))
			defineCalendarioProfessor();
		else
			defineCalendarioAluno();
	}
	
	/* TODO Clodoaldo: Adicionar método para pegar fornecer calendario ativo - professor ou aluno.
	 * Ver classe ModeloCalendario para mais detalhes.
	 */
	
	private void defineCalendarioAluno() {
		Centro centroAluno = getCentroAluno();
		Periodo periodoAtual = fachada.getPeriodoAtual(centroAluno);
		Calendario calendarioAluno = fachada.getCalendarioDoBanco(periodoAtual, centroAluno);
		fachada.setCalendarioAluno(calendarioAluno);
	}
	
	private void defineCalendarioProfessor() {
		Centro centroProfessor = getCentroProfessor();
		Periodo periodoAtual = fachada.getPeriodoAtual(centroProfessor);
		Calendario calendarioProfessor = fachada.getCalendarioDoBanco(periodoAtual, centroProfessor);
		fachada.setCalendarioProfessor(calendarioProfessor);
	}
	
	private Centro getCentroAluno() {
		// TODO Clodoaldo: Remove Middle Man?
		return usuario.getAluno().getCurriculo().getCurso().getCentro();
	}
	
	private Centro getCentroProfessor() {
		// TODO Clodoaldo: Remove Middle Man?
		return usuario.getProfessor().getDepartamento().getCentro();
	}
	
	public void alterar(TipoAbaAtiva nova){
		abaAtiva = nova;
		log.info("alterar(TipoAbaAtiva) - novo valor: {0}", abaAtiva);
	}
	
	public void defineAbaAtivaDocente() {
		alterar(TipoAbaAtiva.DOCENTE);
	}

	public void defineAbaAtivaDiscente() {
		alterar(TipoAbaAtiva.DISCENTE);
	}
	
	public TipoAbaAtiva getAbaAtiva() {
		return abaAtiva;
	}
	
}
