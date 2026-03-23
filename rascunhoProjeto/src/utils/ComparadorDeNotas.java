package utils;

import models.Inscricao;
import java.util.Comparator;

public class ComparadorDeNotas implements Comparator<Inscricao> {

    private double pesoCRE;
    private double pesoMedia;

    // Construtor que recebe os pesos definidos no Edital
    public ComparadorDeNotas(double pesoCRE, double pesoMedia) {
        this.pesoCRE = pesoCRE;
        this.pesoMedia = pesoMedia;
    }

    public int compare(Inscricao i1, Inscricao i2) {
        // Calcula a nota final de cada inscrição
        double nota1 = (i1.getCre() * pesoCRE) + (i1.getMedia() * pesoMedia);
        double nota2 = (i2.getCre() * pesoCRE) + (i2.getMedia() * pesoMedia);

        // Retorna a comparação.
        // Usamos nota2 comparada com nota1 para gerar a lista em ordem DECRESCENTE (maior nota no topo)
        return Double.compare(nota2, nota1);
    }
}