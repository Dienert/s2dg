package org.ufpb.s2dg.session;

import java.awt.HeadlessException;
import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.ufpb.s2dg.entity.Usuario;

@Name("emailAction")
@AutoCreate
@Scope(ScopeType.PAGE)
public class EmailAction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3475934514143309586L;
	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
    private static final String SMTP_AUTH_USER = "es.s2dg";
    private static final String SMTP_AUTH_PWD  = "engenharia";
    private static final String SMTP_PORT  = "465";

    private static final String EMAIL = "es.s2dg@gmail.com"; 
    private static final String TITULO = "Sistema Academico da Graduacao - UFPB";
    private static final String MSG = "Oi!\nUma nova senha foi gerada automaticamente para voce.\n"+
    								  "Voce deve muda-la assim que logar novamente.\nSua nova senha e: ";
    
    private String CPF;
    private boolean clicou = false;
    private String email;
    private String resposta;
    
    @In
    Fachada fachada;
    @In
    FacesContext facesContext;

    public EmailAction(){
    }
    
    public String enviar(){
    	Usuario usuario = fachada.getUsuarioDoBanco(CPF);
    	if(usuario != null) {
    		if(usuario.getResposta().equals(resposta)) {
    			String novaSenha = geraSenha();
    			//usuario.setSenha(Utils.generateHash(novaSenha));
    			usuario.setSenha(novaSenha);
    			fachada.atualizaUsuario(usuario);

    			try 
    			{
    				if(isConnected()) {
    					sendMail(usuario.getEmail(), EMAIL, TITULO, MSG+novaSenha);
    					facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Uma nova senha foi enviada para o seu e-mail.",null));
    				}
    				else
    					System.err.println("Connection-fault");
    			} 
    			catch (HeadlessException e) 
    			{
    				System.err.println(e.getMessage());
    			} 
    			catch (IOException e) 
    			{
    				System.err.println(e.getMessage());
    			}
    		}
    		else {
    			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Resposta incorreta.",null));
    		}
    	}
    	else {
    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Usuario nao encontrado.",null));
    	}
    	clicou = false;
    	return null;
    }
    
    private void sendMail(String to, String from, String titulo, String message)
    {
        try
        {
            Properties props = new Properties();
            
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.starttls.enable","true");
            props.put("mail.smtp.host", SMTP_HOST_NAME);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.socketFactory.port", SMTP_PORT);
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");
                        
            Authenticator auth = new SMTPAuthenticator();
            
            Session session = Session.getInstance(props, auth);
            session.setDebug(false);
            
            Message msg = new MimeMessage(session);
            
            InternetAddress addressFrom = new InternetAddress(from);
            msg.setFrom(addressFrom);
            
            InternetAddress []addressTo = new InternetAddress[1];
            
            addressTo[0] = new InternetAddress(to);
	
            msg.setRecipients(Message.RecipientType.TO, addressTo);
            
            msg.setText(message);
            msg.setSubject(titulo);
            msg.setContent(message, "text/plain");
	
            Transport.send(msg);
        }
        catch(MessagingException e)
        {
            System.err.println(e.getMessage());
        }
    }
    
    private boolean isConnected() throws IOException 
    {         
        HttpURLConnection httpConn = null;
        try 
        {
            URL mandarMail = new URL("http://www.google.com");  
            URLConnection conn = mandarMail.openConnection(); 
            httpConn = (java.net.HttpURLConnection) conn;  
            httpConn.connect();  
            if(httpConn.getResponseCode() == 200)  
                return true;  
            return false;
        } 
        catch (MalformedURLException e) 
        {
            System.err.println(e.getMessage());
        }  
        return false;
    }
    
    private class SMTPAuthenticator extends Authenticator
    {
        public PasswordAuthentication getPasswordAuthentication()
        {
            String username = SMTP_AUTH_USER;
            String password = SMTP_AUTH_PWD;
            
            return new PasswordAuthentication(username, password);
        }
    }
    
    /**
     * Gera uma senha de 6 digitos e atualiza no banco
     */
    public String geraSenha(){
    	String aux = "";
    	
    	while(aux.length() <= 6){
    		int n = (int)(Math.random() * 9);
    		
    		aux += n;
    	}
    	
    	return aux;
    	
    }

	public String getCPF() {
		return CPF;
	}

	public void setCPF(String cpf) {
		CPF = cpf.replaceAll("[.]", "").replaceAll("[-]", "");
	}
	
	public String botaoPressionado() {
		if(fachada.getUsuarioDoBanco(CPF) != null) {
			clicou = true;
			return null;
		}
		else {
			facesContext.addMessage("login:username", new FacesMessage(FacesMessage.SEVERITY_ERROR,"CPF não cadastrado!",null));
			return "";
		}
	}

	public boolean isClicou() {
		return clicou;
	}

	public void setClicou(boolean clicou) {
		this.clicou = clicou;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String cancelar() {
		clicou = false;
		return null;
	}
	
	public String getPergunta(){
		Usuario usuario = fachada.getUsuarioDoBanco(CPF);
		if(usuario != null)
			return usuario.getPergunta();
		else
			return "";
	}
	
	public String getRespostaBanco(){
		Usuario usuario = fachada.getUsuarioDoBanco(CPF);
		if(usuario != null)
			return usuario.getResposta();
		else
			return "";
	}

	public String getResposta() {
		return this.resposta;
	}
	
	public void setResposta(String resposta) {
		this.resposta = resposta;
	}
	
}
