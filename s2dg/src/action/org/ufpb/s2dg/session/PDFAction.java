package org.ufpb.s2dg.session;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.ufpb.s2dg.entity.AlunoTurma;
import org.ufpb.s2dg.entity.Avaliacao;
import org.ufpb.s2dg.session.persistence.AlunoDAO;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Name("pdfAction")
@AutoCreate
@Scope(ScopeType.PAGE)
public class PDFAction implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In(create=true)
	AlunoTurmaAvaliacoesBean alunoTurmaAvaliacoesBean;
	
	@In
    FacesContext facesContext;
	

	@In(create=true)
	ReportGenerator reportGenerator;
	
	@In
	AlunoDAO alunoDAO;
	
	@In
	UsuarioBean usuarioBean;
	
	@In
	TurmasMatriculadasBean turmasMatriculadasBean;
	
	@In
	TurmaBean turmaBean;
	
	@In
	Fachada fachada;
	
	private Document doc;
	
	public PDFAction(){
		
	}

	public void geraPdfDiario(String nome, List<AlunoTurma> list, List<Avaliacao> avaliacoes){		
		
		
		Rectangle pageSize = new Rectangle(PageSize.A4);
 
		pageSize.setBackgroundColor(Color.WHITE);
		
		doc = new Document(pageSize);
		
		try {
			PdfWriter.getInstance(doc, new FileOutputStream(nome));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		this.doc.open();
				
		geraTabelaRelatorioDeNotas(list, avaliacoes);
        
		//geraCabecalho("Test");
		
        this.doc.close(); 
        
        try {
        	reportGenerator.generate(nome);
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Diário de Notas impresso com sucesso!",null));
		} catch (IOException e) {
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Não foi possível imprimir o documento!",null));
			e.printStackTrace();
		}
        
	}
	
	public void geraPdf(String nome, ArrayList<HashMap<String, String>> informacoes){
		Rectangle pageSize = new Rectangle(PageSize.A4);
		 
		pageSize.setBackgroundColor(Color.WHITE);
		
		doc = new Document(pageSize);
		
		try {
			PdfWriter.getInstance(doc, new FileOutputStream(nome));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		this.doc.open();
		
		geraTabelaHoratio(informacoes);
        
		//geraCabecalho();

        this.doc.close(); 

        try {
        	reportGenerator.generate(nome);
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Horário Individual impresso com sucesso!",null));
		} catch (IOException e) {
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Não foi possível imprimir o documento!",null));
			e.printStackTrace();
		}        
        
	}
	
	public void geraPdfHistorico(){
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_hh:mm:ss");
		sdf.format(new Date(System.currentTimeMillis()));
		
		String nome = "Historico_"+fachada.getCPF()+"_"+ sdf + ".pdf";
		
		Rectangle pageSize = new Rectangle(PageSize.A4);
		 
		pageSize.setBackgroundColor(Color.WHITE);
		
		doc = new Document(pageSize);
		
		doc.setMargins(60, 50, 30, 30);
		
		try {
			PdfWriter.getInstance(doc, new FileOutputStream(nome));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		this.doc.open();
		
		geraTabelaHistorico();

        this.doc.close(); 
        
        try {
        	reportGenerator.generate(nome);
        	facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Horário Individual impresso com sucesso!",null));
		} catch (IOException e) {
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Não foi possível imprimir o documento!",null));
			e.printStackTrace();
		}   
	}
	
	public void addParagrafo(Paragraph p){
		try
        {
            this.doc.add(p);
        }
        catch(DocumentException e)
        {
            e.printStackTrace();
        }
	}
	
	public void addParagrafoCentralizado(Paragraph p){
		try
        {
			p.setAlignment(PdfPCell.ALIGN_CENTER);
            this.doc.add(p);
        }
        catch(DocumentException e)
        {
            e.printStackTrace();
        }
	}
	
	public void addParagrafo(String text){
        try
        {
            this.doc.add(new Paragraph(text));
        }
        catch(DocumentException e)
        {
            e.printStackTrace();
        }
    }
	
	public void addImage(String name){
        Image image = null;
        try 
        {        
            image = Image.getInstance(name);
                           
            this.doc.add(image);
        }
        catch(MalformedURLException e) 
        {
            e.printStackTrace();
        } 
        catch(BadElementException e) 
        {
            e.printStackTrace();
        } 
        catch(IOException e) 
        {
            e.printStackTrace();
        }
        catch(DocumentException e)
        {
            e.printStackTrace();
        }
    }
	
	public void geraCabecalho(String titulo){
		ServletContext servletContext = (ServletContext)facesContext.getExternalContext().getContext();
		servletContext.getRealPath("/img");
		
		Image image = null;
		PdfPTable table = null;
		try {
			
			//image = Image.getInstance("C:\\ambiente\\workspace\\s2dg\\WebContent\\img\\logo_ufpb2.png");
			
			String caminhoImagem =  ".." + File.separator + ".." + File.separator + "workspace" + File.separator + "s2dg" + File.separator + "WebContent" + File.separator + "img" + File.separator + "logo_ufpb2.png"; 
			
			image = Image.getInstance(caminhoImagem);
			
			image.scaleAbsolute(31, 45);
			table = new PdfPTable(2);
			float [] proporcao = new float[]{1f, 2f};
			table.setWidths(proporcao);
			
		} catch (BadElementException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		Font f1 = new Font(); f1.setStyle(Font.NORMAL); f1.setSize(8);
		Font f2 = new Font(); f2.setStyle(Font.NORMAL); f2.setSize(10);
		
		PdfPCell cell0 = new PdfPCell(image);
        PdfPCell cell1 = new PdfPCell(new Paragraph("UNIVERSIDADE FEDERAL DA PARAÍBA\n\n"
        		+ titulo, f2));
        
        cell0.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        cell1.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        
        cell0.setBorderColor(Color.WHITE);
        cell1.setBorderColor(Color.WHITE);
		
        table.addCell(cell0);
		table.addCell(cell1);
		
		table.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		try 
        {
            this.doc.add(table);
        } 
        catch (DocumentException ex) {ex.printStackTrace();}
        
        addParagrafo("\n");
	}
	
	public void geraTabelaHoratio(ArrayList<HashMap<String, String>> informacoes){				
		geraCabecalho("HORÁRIO INDIVIDUAL");
		
		Font f1 = new Font(); f1.setStyle(Font.BOLD); f1.setSize(12);
        Font f2 = new Font(); f2.setStyle(Font.ITALIC); f2.setSize(8);
		
        PdfPTable table = new PdfPTable(7);
        PdfPCell cell0 = new PdfPCell(new Paragraph("Horário Individual", f1));
        PdfPCell cell1 = new PdfPCell(new Paragraph("Código", f2));
        PdfPCell cell2 = new PdfPCell(new Paragraph("Nome", f2));
        PdfPCell cell3 = new PdfPCell(new Paragraph("Turma", f2));
        PdfPCell cell4 = new PdfPCell(new Paragraph("CR", f2));
        PdfPCell cell5 = new PdfPCell(new Paragraph("CH", f2));
        PdfPCell cell6 = new PdfPCell(new Paragraph("Horário", f2));
        PdfPCell cell7 = new PdfPCell(new Paragraph("Sala", f2));
        
        cell0.setBackgroundColor(Color.BLUE);
        cell0.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell0.setColspan(7);
        cell0.setBorderWidthBottom(1);
        cell0.setBorderWidthTop(1);
        
        cell1.setBackgroundColor(Color.LIGHT_GRAY);
        cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

        cell2.setBackgroundColor(Color.LIGHT_GRAY);
        cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        
        cell3.setBackgroundColor(Color.LIGHT_GRAY);
        cell3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        
        cell4.setBackgroundColor(Color.LIGHT_GRAY);
        cell4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        
        cell5.setBackgroundColor(Color.LIGHT_GRAY);
        cell5.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        
        cell6.setBackgroundColor(Color.LIGHT_GRAY);
        cell6.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        
        cell7.setBackgroundColor(Color.LIGHT_GRAY);
        cell7.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        
        //table.addCell(cell0);
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        table.addCell(cell5);
        table.addCell(cell6);
        table.addCell(cell7);
        
        for(HashMap<String, String> hash : informacoes){
        	
        	cell1 = new PdfPCell(new Paragraph(hash.get("Codigo"), f2));
        	cell2 = new PdfPCell(new Paragraph(hash.get("Nome"), f2));
        	cell3 = new PdfPCell(new Paragraph(hash.get("Turma"), f2));
            cell4 = new PdfPCell(new Paragraph(hash.get("Creditos"), f2));
            cell5 = new PdfPCell(new Paragraph(hash.get("Carga Horaria"), f2));
            cell6 = new PdfPCell(new Paragraph(hash.get("Horarios"), f2));
            cell7 = new PdfPCell(new Paragraph(hash.get("Sala"), f2));
            
            cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

            cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            
            cell3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            
            cell4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            
            cell5.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            
            cell6.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            
            cell7.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            
        	table.addCell(cell1);
        	table.addCell(cell2);
        	table.addCell(cell3);
        	table.addCell(cell4);
        	table.addCell(cell5);
        	table.addCell(cell6);
        	table.addCell(cell7);
        }
        
        try 
        {
            this.doc.add(table);
        } 
        catch (DocumentException ex) {ex.printStackTrace();}
	}
	
	public void geraTabelaRelatorioDeNotas(List<AlunoTurma> list, List<Avaliacao> avaliacoes){
		geraCabecalho("DIÁRIO DE CLASSE");
		System.out.println("Entrou¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬");				
        Font f1 = new Font(); f1.setStyle(Font.BOLD); f1.setSize(12);
        Font f2 = new Font(); f2.setStyle(Font.ITALIC); f2.setSize(8);
        
        addParagrafo(new Paragraph("Disciplina: " + turmaBean.getTurma().getDisciplina().getCodigo() +"-"+ turmaBean.getTurma().getDisciplina().getNome(), f2));
        addParagrafo(new Paragraph("Numero da Turma: " + turmaBean.getTurma().getNumero(), f2));
        //String professores = "";
        //Iterator<Professor> it = turmaBean.getTurma().getProfessores().iterator();
        //while(it.hasNext()){
        	//nao ta funcionando
        	//professores += it.next().getUsuario().getNome() + " ";
        //}
        //addParagrafo(new Paragraph("Professores: " + professores + "\n\n", f2));
        
        addParagrafo("\n");
        
        int size = 3;
        if (avaliacoes != null)
         size = 4 + avaliacoes.size();
        
        System.out.println("***********************************"+size);

        PdfPTable titulo = new PdfPTable(1);
        PdfPTable table = new PdfPTable(size);
        
        PdfPCell [] cell = new PdfPCell[size+1];
        
        cell[0] = new PdfPCell(new Paragraph("Relatorio de Notas", f1));
        cell[1] = new PdfPCell(new Paragraph("Aluno", f2));
        cell[2] = new PdfPCell(new Paragraph("Matrícula", f2));
        cell[3] = new PdfPCell(new Paragraph("Faltas", f2));
        
        cell[0].setBackgroundColor(Color.BLUE);
        cell[0].setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell[0].setColspan(7);
        cell[0].setBorderWidthBottom(1);
        cell[0].setBorderWidthTop(1);    
        
        cell[1].setBackgroundColor(Color.LIGHT_GRAY);
        cell[1].setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        
        cell[2].setBackgroundColor(Color.LIGHT_GRAY);
        cell[2].setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        
        cell[3].setBackgroundColor(Color.LIGHT_GRAY);
        cell[3].setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        
        for(int i = 0; i < size - 4; i++){
        	Avaliacao avaliacao = avaliacoes.get(i);
        	
        	//Pegar a data nao ta funcionando
        	cell[i+4] = new PdfPCell(new Paragraph(avaliacao.getNome() + "\nPeso: " + avaliacao.getPeso() /*+ " Data: "*/ /*+avaliacao.getDataEvento().getData().toString()*/, f2));
        	cell[i+4].setBackgroundColor(Color.LIGHT_GRAY);
            cell[i+4].setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            
            System.out.println("¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬*****i: "+i);
        }
        
        cell[cell.length - 1] = new PdfPCell(new Paragraph("Média Final", f2));

        cell[cell.length - 1].setBackgroundColor(Color.LIGHT_GRAY);
        cell[cell.length - 1].setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

        //titulo.addCell(cell[0]);
        for(int i = 1; i < cell.length; i++){
        	System.out.println("¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬"+i);
        	table.addCell(cell[i]);
        }

        
        for(AlunoTurma aluno : list){
        	cell[1] = new PdfPCell(new Paragraph(aluno.getAluno().getUsuario().getNome(), f2));
        	cell[2] = new PdfPCell(new Paragraph(aluno.getAluno().getMatricula(), f2));
        	cell[3] = new PdfPCell(new Paragraph(aluno.getFaltas()+"", f2));
        	
        	cell[1].setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        	cell[2].setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        	cell[3].setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        	
            table.addCell(cell[1]);
            table.addCell(cell[2]);
            table.addCell(cell[3]);
            
            if (avaliacoes != null) {
            
            for(Avaliacao avaliacao : avaliacoes){
            	PdfPCell cell0 = new PdfPCell(new Paragraph(alunoTurmaAvaliacoesBean.getAlunoTurmaAvaliacao(aluno, avaliacao).getNota()+"", f2));
            	cell0.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            	table.addCell(cell0);
            }
            
            }
            
            cell[4] = new PdfPCell(new Paragraph(aluno.getMedia()+"", f2));
            cell[4].setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            
            table.addCell(cell[4]);
        }
        
        try 
        {
        	this.doc.add(titulo);
            this.doc.add(table);
        } 
        catch (DocumentException ex) {ex.printStackTrace();}
        
    }
	
	public void geraTabelaHistorico(){
		LinkedList<String> list = turmasMatriculadasBean.geraHistorico();
		
		Font f2 = new Font(Font.COURIER);
		f2.setStyle(Font.NORMAL); 
		f2.setSize(10);
		
		for(String s : list){
			if(s.length() == 0) s = "\n";
			addParagrafo(new Paragraph(s.replaceAll("&nbsp;", " "), f2));
		}		
		
	}
	
	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

}