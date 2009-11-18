package org.ufpb.s2dg.session.util;

import java.util.Iterator;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;
import org.ufpb.s2dg.entity.Role;
import org.ufpb.s2dg.entity.Usuario;
import org.ufpb.s2dg.session.Fachada;

@Name("authenticator")
public class Authenticator
{

    @In 
    Identity identity;
    
    @In 
    Credentials credentials;

    @In
    Fachada fachada;
    
    @In
	FacesContext facesContext;
    
    public boolean authenticate()
    {
    	credentials.setUsername(fachada.getCPF());
    	String cpfDado = credentials.getUsername().replaceAll("[.]", "").replaceAll("[-]", "");
        
        Usuario usuario = fachada.getUsuarioDoBanco(cpfDado);

        if(usuario != null) {
        	if (Utils.validatePasswordPlana(credentials.getPassword(), usuario)) {
        		Set<Role> roles = usuario.getRoles();
        		if(roles != null) {
        			Iterator<Role> rolesIt = roles.iterator();
        			while(rolesIt.hasNext()) {
        				Role role = rolesIt.next();
        				identity.addRole(role.getNome());
        			}
        		}
        	}
        	else {
            	facesContext.addMessage("login:username", new FacesMessage(FacesMessage.SEVERITY_ERROR,"CPF ou senha invalidos!",null));
            	return false;
            }
        } else {
        	facesContext.addMessage("login:username", new FacesMessage(FacesMessage.SEVERITY_ERROR,"CPF ou senha invalidos!",null));
        	return false;
        }
        
        fachada.setUsuario(usuario);
        if(usuario.getAluno() != null)
        	fachada.initTurmasMatriculadas();
        
        return true;
    }

}
