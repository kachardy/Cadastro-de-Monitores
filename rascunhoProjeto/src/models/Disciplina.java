package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Disciplina {

    private Long id;

    private String nome;

    private int vagasRemuneradas;
    private int vagasVoluntarias;

    public Disciplina() {
    }

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

    @JsonIgnore
    public int getTotalVagas() {
        return vagasRemuneradas + vagasVoluntarias;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return nome;
    }
}