package org.ufpb.s2dg.session.persistence;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.ufpb.s2dg.entity.Usuario;

@AutoCreate
@Name("usuarioDAO")
public class UsuarioDAO {
	@In
	EntityManager entityManager;
	
	public Usuario getUsuario(String username, String password) {
		return (Usuario) entityManager.createQuery(
    	"select u from Usuario as u where u.cpf =:cpf and u.senha =:senha")
    	.setParameter("cpf", username)
    	.setParameter("senha", password)
    	.getSingleResult();
	}
	
	//Criado por Julio e Rennan
	@SuppressWarnings("unchecked")
	public Usuario getUsuario(String username){
		Query query = entityManager.createQuery("select u from Usuario as u where u.cpf = :cpf")
			.setParameter("cpf", username);
		List<Usuario> result = query.getResultList();
		if(result.size() > 0)
			return (Usuario)result.get(0);
		return null;
	}

	public Usuario getUsuarioProfessor(String matricula) {
		return (Usuario) entityManager.createQuery(
    	"select u from Usuario as u where u.professor.matricula =:matricula")
    	.setParameter("matricula", matricula)
    	.getSingleResult();
	}
	
	public Usuario getUsuarioAluno(String matricula) {
		return (Usuario) entityManager.createQuery(
    	"select u from Usuario as u where u.aluno.matricula =:matricula")
    	.setParameter("matricula", matricula)
    	.getSingleResult();
	}
	
	//Criado por Julio
	public void alteraSenha(String CPF, byte [] senha){
		entityManager.createQuery("update Usuario Set senha = :senha where cpf = :cpf")
		.setParameter("cpf", CPF)
		.setParameter("senha", senha).executeUpdate();
		entityManager.flush();
	}
	
	/**
	 * Capturar o email do usuatio
	 */
	public String getEmail(String CPF){
		return (String) entityManager.createQuery("select email from Usuario where cpf = :cpf")
		.setParameter("cpf", CPF).getSingleResult(); 
	}
	
	/**
	 * Captura o email
	 */
	
	public String getSenha(String CPF){
		return (String) entityManager.createQuery("select senha from Usuario where cpf = :cpf")
		.setParameter("cpf", CPF).getSingleResult(); 
	}
	
	public boolean updateUsuario(Usuario usuario) {
		//Usuario user = (Usuario) entityManager.find(Usuario.class, usuario.getId());
		if (usuario == null)
			return false;
		entityManager.merge(usuario);
		entityManager.flush();
		return true;
	}
	
}
