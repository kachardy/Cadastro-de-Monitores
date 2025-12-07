package outros;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import pessoas.Aluno;

public class GeradorDeRelatorio {

    public static void gerarPdfResultado(EditalDeMonitoria edital) {

        Document doc = new Document();

        try {
            String arquivo = "resultado_edital_" + edital.getId() + ".pdf";

            PdfWriter.getInstance(doc, new FileOutputStream(arquivo));

            doc.open();

            doc.add(new Paragraph("Resultado do Edital " + edital.getNumeroEdital()));
            doc.add(new Paragraph(" "));

            double pesoCRE = edital.getPesoCRE();
            double pesoMedia = edital.getPesoMedia();

            for (Disciplina d : edital.getTodasAsDisciplinas()) {

                doc.add(new Paragraph("Disciplina: " + d.getNome()));
                doc.add(new Paragraph("-----------------------------"));

                ArrayList<Aluno> alunos = d.getAlunosInscritos();
                ArrayList<Double> cres = d.getListaCREs();
                ArrayList<Double> medias = d.getListaMedias();

                for (int i = 0; i < alunos.size(); i++) {

                    double cre = (i < cres.size()) ? cres.get(i) : 0;
                    double media = (i < medias.size()) ? medias.get(i) : 0;

                    double pont = (cre * pesoCRE) + (media * pesoMedia);
                    String pontStr = String.format("%.2f", pont);

                    doc.add(new Paragraph((i + 1) + "º  - " + alunos.get(i).getNome() + "   | Pontuação: " + pontStr));
                }

                doc.add(new Paragraph(" ")); // Espacin entre as disciplinas hahay
            }

            doc.close();
            System.out.println("PDF gerado: " + arquivo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
