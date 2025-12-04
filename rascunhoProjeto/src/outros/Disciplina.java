package outros;

import java.util.ArrayList;
import pessoas.Aluno;

public class Disciplina {
    
    private String nome;
    private int vagasRemuneradas;
    private int vagasVoluntarias;
    
    // Mantemos os dados sincronizados pelo índice (posição na lista)
    private ArrayList<Aluno> alunosInscritos = new ArrayList<>();
    private ArrayList<Double> listaCREs = new ArrayList<>();
    private ArrayList<Double> listaMedias = new ArrayList<>();

    public Disciplina(String nome, int vagasRemuneradas, int vagasVoluntarias) {
        this.nome = nome;
        this.vagasRemuneradas = vagasRemuneradas;
        this.vagasVoluntarias = vagasVoluntarias;
    }
    
    public void adicionarAluno(Aluno aluno, double cre, double media) {
        if (!alunosInscritos.contains(aluno)) {
            alunosInscritos.add(aluno);
            listaCREs.add(cre);
            listaMedias.add(media);
        }
    }
    
    public int getTotalVagas() {
        return vagasRemuneradas + vagasVoluntarias;
    }

    // Getters e Setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getVagasRemuneradas() {
        return vagasRemuneradas;
    }

    public void setVagasRemuneradas(int vagasRemuneradas) {
        this.vagasRemuneradas = vagasRemuneradas;
    }

    public int getVagasVoluntarias() {
        return vagasVoluntarias;
    }

    public void setVagasVoluntarias(int vagasVoluntarias) {
        this.vagasVoluntarias = vagasVoluntarias;
    }
    
    public ArrayList<Aluno> getAlunosInscritos() {
        return alunosInscritos;
    }
    
    public ArrayList<Double> getListaCREs() {
        return listaCREs;
    }
    
    public ArrayList<Double> getListaMedias() {
        return listaMedias;
    }
    
    public String toString() {
    	return nome + " (Rem: " + vagasRemuneradas + ", Vol: " + vagasVoluntarias + ")";
    }
}