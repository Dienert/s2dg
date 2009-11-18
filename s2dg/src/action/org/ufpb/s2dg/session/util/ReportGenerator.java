package org.ufpb.s2dg.session.util;

import java.io.File;
import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Manager;
import org.jboss.seam.document.ByteArrayDocumentData;
import org.jboss.seam.document.DocumentData;
import org.jboss.seam.document.DocumentStore;
import org.jboss.seam.faces.RedirectException;
import org.jboss.seam.international.StatusMessages;

@Name("reportGenerator")
@AutoCreate
public class ReportGenerator
{
    @In StatusMessages statusMessages;
    
    @In Manager manager;
    
	public void generate(String name) throws IOException {
		String separator = "/";
		
		String path = System.getProperty("user.dir");
		
		if (path.contains("\\"))
			separator = "\\";
		
		File file = new File(path+separator+name);
		
		byte[] binaryData = Utils.getBytesFromFile(file);
	    
	    DocumentData data = new ByteArrayDocumentData(name,
	        new DocumentData.DocumentType("pdf", "application/pdf"), binaryData
	    );
	    DocumentStore documentStore = DocumentStore.instance();
	    String docId = documentStore.newId();
	    documentStore.saveData(docId, data);                            
	    String documentUrl = documentStore.preferredUrlForContent(      
	        data.getBaseName(), data.getDocumentType().getExtension(), docId);
	    redirect(documentUrl);
	}
	
	@SuppressWarnings("deprecation")
	protected void redirect(String url) {
	      FacesContext context = FacesContext.getCurrentInstance();
	      ExternalContext externalContext = context.getExternalContext();
	      try {
	          externalContext.redirect(
	              manager.encodeConversationId(url));                   
	      }
	      catch (IOException ioe) {
	          throw new RedirectException(ioe);
	      }
	}

}
