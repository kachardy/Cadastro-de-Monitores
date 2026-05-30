package models;

public class Inscricao {


    private Long id;


    private Aluno candidato;

    private Disciplina disciplina;

    private EditalDeMonitoria edital;

    private double cre;
    private double media;

    // A INSCRIÇÃO RECEBE UM ALUNO , UM CRE E UMA MÉDIA, ALÉM DE RECEBER TAMBÉM UMA DISCIPLINA
    // PARA IDENTIFICAR DE QUAL DISCIPLINA SE TRATA .
    public Inscricao(Aluno candidato, Disciplina disciplina, double cre, double media) {
        this.candidato = candidato;
        this.disciplina = disciplina;
        this.cre = cre;
        this.media = media;
    }

    public Inscricao () {}

    public Aluno getCandidato() {
        return candidato;
    }

    public void setCandidato(Aluno candidato) {
        this.candidato = candidato;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public double getCre() {
        return cre;
    }

    public void setCre(double cre) {
        this.cre = cre;
    }

    public double getMedia() {
        return media;
    }

    public void setMedia(double media) {
        this.media = media;
    }

    public EditalDeMonitoria getEdital() {
        return edital;
    }

    public void setEdital(EditalDeMonitoria edital) {
        this.edital = edital;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}