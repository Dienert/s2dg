package org.ufpb.s2dg.session.util;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("MenuAction")
@Scope(ScopeType.SESSION)
@AutoCreate
public class MenuAction implements Serializable {

	/**
	 * 
	 */											
	private static final long serialVersionUID = -396395780804966001L;

	//Menu Discente
	public int Id_Menu = 6;

	String[] lista_opcoes = {"/corpo_do_discente.xhtml", 
								"/configuracoes.xhtml",
							 	"/matricula.xhtml", 
							 	"/paginahistorico.xhtml", 
							 	"/paginahorario.xhtml",
							 	"/trancamento_total.xhtml",
							 	"/bem_vindo.xhtml",
							 	"/confirma_trancamento_parcial.xhtml"};
	//Menu Docente
	public int Id_Menu2 = 4;

	String[] lista_opcoes2 = {"/corpo_do_docente.xhtml", 
								"/configuracoes.xhtml",
								"/alterar_notas_semestre_anterior.xhtml",
								"/alterar_nota_turma_semestre_anterior.xhtml",
								"/bem_vindo.xhtml"};
	
	//Menu Discente
	public String getOption() {
		return lista_opcoes[Id_Menu];
	}
	
	//Menu Docente
	public String getOption2() {
		return lista_opcoes2[Id_Menu2];
	}

	public void setId_Menu(int idMenu) {
		if(idMenu<4){
			this.Id_Menu = idMenu;
			this.Id_Menu2 = idMenu;
		}else {
			this.Id_Menu = idMenu;
			this.Id_Menu2 = 0;
		}
	}
	
	public void setId_MenuAluno(int idMenu){
		this.Id_Menu = idMenu;
	}
	
	public void setId_MenuProfessor(int idMenu){
		this.Id_Menu2 = idMenu;
	}
	
	public void setPaginaBenvindo(){
		this.Id_Menu = 6;
		this.Id_Menu2 = 4;
	} 
	
}