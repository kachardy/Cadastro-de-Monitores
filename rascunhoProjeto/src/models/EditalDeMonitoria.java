package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class EditalDeMonitoria implements Serializable {
    private long id;
    private String numeroEdital;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private int maxInscricoesPorAluno; // Novo campo
    private double pesoCRE;
    private double pesoMedia;
    private boolean resultadoCalculado = false;
    private boolean resultadoFinal = false; // Novo campo para homologação
    private ArrayList<Disciplina> todasAsDisciplinas = new ArrayList<>();

    public EditalDeMonitoria(long id, String numeroEdital, LocalDate dataInicio, LocalDate dataFim, int maxInscricoes, double pesoCRE, double pesoMedia) {
        this.id = id;
        this.numeroEdital = numeroEdital;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.maxInscricoesPorAluno = maxInscricoes;
        this.pesoCRE = pesoCRE;
        this.pesoMedia = pesoMedia;
    }

    public EditalDeMonitoria clonar() {
        EditalDeMonitoria novoEdital = new EditalDeMonitoria(
                System.currentTimeMillis(),
                "Cópia de " + this.numeroEdital,
                this.dataInicio,
                this.dataFim,
                this.maxInscricoesPorAluno,
                this.pesoCRE,
                this.pesoMedia
        );

        for (Disciplina d : this.todasAsDisciplinas) {
            novoEdital.adicionarDisciplina(new Disciplina(d.getNome(), d.getVagasRemuneradas(), d.getVagasVoluntarias()));
        }
        return novoEdital;
    }

    public void calcularResultadoFinal() {
        for (Disciplina d : todasAsDisciplinas) {
            d.ordenarRanking(pesoCRE, pesoMedia);
        }
        this.resultadoCalculado = true;
    }

    public boolean jaAcabou() {
        return LocalDate.now().isAfter(dataFim);
    }

    // --- Getters e Setters Necessários para o Controller ---

    public long getId() { return id; }
    public String getNumeroEdital() { return numeroEdital; }
    public void setNumeroEdital(String numeroEdital) { this.numeroEdital = numeroEdital; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public int getMaxInscricoesPorAluno() { return maxInscricoesPorAluno; }
    public void setMaxInscricoesPorAluno(int max) { this.maxInscricoesPorAluno = max; }

    public double getPesoCRE() { return pesoCRE; }
    public void setPesoCRE(double pesoCRE) { this.pesoCRE = pesoCRE; }

    public double getPesoMedia() { return pesoMedia; }
    public void setPesoMedia(double pesoMedia) { this.pesoMedia = pesoMedia; }

    public boolean isResultadoCalculado() { return resultadoCalculado; }
    public void setResultadoCalculado(boolean res) { this.resultadoCalculado = res; }

    public boolean isResultadoFinal() { return resultadoFinal; }
    public void setResultadoFinal(boolean res) { this.resultadoFinal = res; }

    public ArrayList<Disciplina> getTodasAsDisciplinas() { return todasAsDisciplinas; }
    public void setTodasAsDisciplinas(ArrayList<Disciplina> disc) { this.todasAsDisciplinas = disc; }

    public void adicionarDisciplina(Disciplina d) { todasAsDisciplinas.add(d); }

    public boolean inscrever(Aluno aluno, Disciplina disciplina, double cre, double media) {
        if (jaAcabou() || resultadoCalculado) return false;
        // (Sua lógica de verificação de duplicatas aqui...)
        disciplina.adicionarAluno(aluno, cre, media);
        return true;
    }

    public boolean desistirDoEdital(Aluno aluno) {
        if (resultadoFinal) return false;
        for (Disciplina d : todasAsDisciplinas) d.removerAluno(aluno);
        return true;
    }
}