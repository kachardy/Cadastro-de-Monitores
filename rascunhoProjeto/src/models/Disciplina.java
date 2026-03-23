package models;

public class Disciplina {
    private String nome;
    private int vagasRemuneradas;
    private int vagasVoluntarias;

    public Disciplina(String nome, int vagasRemuneradas, int vagasVoluntarias) {
        this.nome = nome;
        this.vagasRemuneradas = vagasRemuneradas;
        this.vagasVoluntarias = vagasVoluntarias;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getVagasRemuneradas() {
        return vagasRemuneradas;
    }

    public void setVagasRemuneradas(int vagas) {
        this.vagasRemuneradas = vagas;
    }

    public int getVagasVoluntarias() {
        return vagasVoluntarias;
    }

    public void setVagasVoluntarias(int vagas) {
        this.vagasVoluntarias = vagas;
    }

    public int getTotalVagas() {
        return vagasRemuneradas + vagasVoluntarias;
    }

    @Override
    public String toString() {
        return nome;
    }
}