package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class EditalDeMonitoria implements Serializable {

    // Atributos de Identificação e Configuração
    private long id;
    private String numeroEdital;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private int maxInscricoesPorAluno;
    private double pesoCRE;
    private double pesoMedia;

    // Atributos de Estado do Processo
    private boolean resultadoCalculado = false;
    private boolean resultadoFinal = false;

    // O Edital possui as vagas e o Gerenciador para as Inscrições)
    private ArrayList<Disciplina> todasAsDisciplinas = new ArrayList<>();
    private GerenciadorDeInscricoes gerenciador = new GerenciadorDeInscricoes();

    public EditalDeMonitoria(long id, String numeroEdital, LocalDate dataInicio, LocalDate dataFim,
                             int maxInscricoes, double pesoCRE, double pesoMedia) {
        this.id = id;
        this.numeroEdital = numeroEdital;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.maxInscricoesPorAluno = maxInscricoes;
        this.pesoCRE = pesoCRE;
        this.pesoMedia = pesoMedia;
    }

    // --- LÓGICA DE NEGÓCIO ---

    /**
     * Realiza uma cópia profunda (Deep Copy) do edital para um novo período.
     * Mantém as disciplinas (Catálogo), mas zera as inscrições (Estado).
     */
    public EditalDeMonitoria clonar() {
        EditalDeMonitoria novoEdital = new EditalDeMonitoria(
                System.currentTimeMillis(), // Novo ID
                "Cópia de " + this.numeroEdital,
                this.dataInicio,
                this.dataFim,
                this.maxInscricoesPorAluno,
                this.pesoCRE,
                this.pesoMedia
        );

        // Clona as disciplinas como objetos novos (sem vínculos antigos)
        for (Disciplina d : this.todasAsDisciplinas) {
            novoEdital.adicionarDisciplina(new Disciplina(
                    d.getNome(),
                    d.getVagasRemuneradas(),
                    d.getVagasVoluntarias()
            ));
        }
        return novoEdital;
    }

    // Delega a inscrição para o Gerenciador de Serviços
    public boolean inscrever(Aluno aluno, Disciplina disc, double cre, double media) {
        if (jaAcabou() || resultadoCalculado) {
            return false; // Regra: Não inscreve se o prazo acabou ou ranking já saiu
        }

        Inscricao nova = new Inscricao(aluno, disc, cre, media);
        gerenciador.realizarInscricao(nova);
        return true;
    }

    // Percorre o catálogo de disciplinas e solicita ao Gerenciador que ordene cada ranking.
    public void calcularResultadoFinal() {
        for (Disciplina d : todasAsDisciplinas) {
            gerenciador.ordenarRanking(d, pesoCRE, pesoMedia);
        }
        this.resultadoCalculado = true;
    }

    // Remove o aluno do processo seletivo através do Gerenciador.
    public boolean desistirDoEdital(Aluno aluno) {
        if (resultadoFinal) {
            return false; // Regra: Após a homologação final, não há mais desistência
        }
        gerenciador.removerTodasInscricoesDoAluno(aluno);
        return true;
    }

    public boolean jaAcabou() {
        return LocalDate.now().isAfter(dataFim);
    }

    // --- GETTERS E SETTERS ---

    public long getId() {
        return id;
    }

    public String getNumeroEdital() {
        return numeroEdital;
    }

    public void setNumeroEdital(String num) {
        this.numeroEdital = num;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate inicio) {
        this.dataInicio = inicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate fim) {
        this.dataFim = fim;
    }

    public int getMaxInscricoesPorAluno() {
        return maxInscricoesPorAluno;
    }

    public void setMaxInscricoesPorAluno(int max) {
        this.maxInscricoesPorAluno = max;
    }

    public double getPesoCRE() {
        return pesoCRE;
    }

    public void setPesoCRE(double peso) {
        this.pesoCRE = peso;
    }

    public double getPesoMedia() {
        return pesoMedia;
    }

    public void setPesoMedia(double peso) {
        this.pesoMedia = peso;
    }

    public boolean isResultadoCalculado() {
        return resultadoCalculado;
    }

    public void setResultadoCalculado(boolean status) {
        this.resultadoCalculado = status;
    }

    public boolean isResultadoFinal() {
        return resultadoFinal;
    }

    public void setResultadoFinal(boolean status) {
        this.resultadoFinal = status;
    }

    public ArrayList<Disciplina> getTodasAsDisciplinas() {
        return todasAsDisciplinas;
    }

    public void setTodasAsDisciplinas(ArrayList<Disciplina> lista) {
        this.todasAsDisciplinas = lista;
    }

    public void adicionarDisciplina(Disciplina d) {
        this.todasAsDisciplinas.add(d);
    }

    public GerenciadorDeInscricoes getGerenciador() {
        return gerenciador;
    }
}