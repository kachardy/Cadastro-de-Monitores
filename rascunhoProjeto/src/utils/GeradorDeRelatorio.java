package utils;

import java.io.FileOutputStream;
import java.util.ArrayList;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import models.Aluno;
import models.Disciplina;
import models.EditalDeMonitoria;
import models.Inscricao; // NOVA ALTERAÇÃO: Importação para suportar o novo modelo

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

                // NOVA ALTERAÇÃO: Fim das listas paralelas (alunos, cres, medias)
                // Agora percorremos a lista única de inscrições da disciplina
                ArrayList<Inscricao> inscricoes = d.getInscricoes();

                for (int i = 0; i < inscricoes.size(); i++) {
                    // Recupera o objeto unificado
                    Inscricao insc = inscricoes.get(i);

                    // NOVA ALTERAÇÃO: Cálculo da pontuação extraído diretamente do objeto Inscricao
                    double cre = insc.getCre();
                    double media = insc.getMedia();

                    double pont = (cre * pesoCRE) + (media * pesoMedia);
                    String pontStr = String.format("%.2f", pont);

                    // Acessa o nome do candidato através da associação na Inscrição
                    doc.add(new Paragraph((i + 1) + "º  - " + insc.getCandidato().getNome() + "   | Pontuação: " + pontStr));
                }

                doc.add(new Paragraph(" ")); // Espaçamento entre as disciplinas
            }

            doc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}