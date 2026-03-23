package models;

public class Inscricao {

    private Aluno candidato;
    private Disciplina disciplina;
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
}