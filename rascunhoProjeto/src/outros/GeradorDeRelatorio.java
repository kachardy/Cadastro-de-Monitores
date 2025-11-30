package outros;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import pessoas.Aluno;

public class GeradorDeRelatorio {
	public static void obterComprovanteDeInscricaoAluno(String matricula, long id, CentralDeInformacoes c) {
		 Document document = new Document();
		 try {

             PdfWriter.getInstance(document, new FileOutputStream(System.getProperty("user.home") + "\\Documents\\relatório.pdf"));
             document.open();
             
             String resultado = "";
             ArrayList<Disciplina> inscricoes = c.recuperarInscricoesDeUmAlunoEmUmEdital(matricula, id);
             if (inscricoes == null) {
            	 return;
             }
             for (Disciplina d: inscricoes) {
            	 resultado += "\n" + d.getNome();
             }
             
             Aluno aluno = c.recuperarAlunoPorMatricula(matricula);
             
             document.add(new Paragraph("Aluno: " + aluno.getNome() +" - Matrícula: " + aluno.getMatricula()));
             document.add(new Paragraph("Disciplinas inscritas: \n"));
             document.add(new Paragraph(resultado));
         }
         catch(DocumentException de) {
            
         }
         catch(IOException ioe) {
       
         }
         document.close();
     
	}
}
