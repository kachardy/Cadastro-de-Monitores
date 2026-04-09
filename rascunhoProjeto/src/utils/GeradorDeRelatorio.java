package utils;

import java.io.FileOutputStream;
import java.util.List; // IMPORTANTE: Trocamos ArrayList por List por conta do JPA

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import models.Disciplina;
import models.EditalDeMonitoria;
import models.Inscricao;

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

                // NOVA ALTERAÇÃO: Pegamos a lista do banco (no edital) e usamos o gerenciador apenas para FILTRAR pela disciplina atual.
                List<Inscricao> inscricoesDaDisciplina = edital.getGerenciador()
                        .getInscricoesPorDisciplina(edital.getInscricoesRealizadas(), d);

                // Adicionei uma validação rápida para o PDF ficar mais elegante caso não haja inscritos
                if (inscricoesDaDisciplina.isEmpty()) {
                    doc.add(new Paragraph("Nenhum inscrito nesta disciplina."));
                } else {
                    for (int i = 0; i < inscricoesDaDisciplina.size(); i++) {
                        // Recupera o objeto da lista filtrada
                        Inscricao insc = inscricoesDaDisciplina.get(i);

                        double cre = insc.getCre();
                        double media = insc.getMedia();

                        double pont = (cre * pesoCRE) + (media * pesoMedia);
                        String pontStr = String.format("%.2f", pont);

                        // Acessa o nome do candidato através da associação na Inscrição
                        doc.add(new Paragraph((i + 1) + "º  - " + insc.getCandidato().getNome() + "   | Pontuação: " + pontStr));
                    }
                }

                doc.add(new Paragraph(" ")); // Espaçamento entre as disciplinas
            }

            doc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}