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
    
    // Garante que as listas não sejam nulas
    
    private void validarListas() {
        if (alunosInscritos == null) alunosInscritos = new ArrayList<>();
        if (listaCREs == null) listaCREs = new ArrayList<>();
        if (listaMedias == null) listaMedias = new ArrayList<>();
    }
    
    public void adicionarAluno(Aluno aluno, double cre, double media) {
    	validarListas();
        
        // Valida a matrícula
        for(Aluno a : alunosInscritos) {
            if(a.getMatricula().equals(aluno.getMatricula())) {
                return; // Já está inscrito, não faz nada
            }
        }
        
        alunosInscritos.add(aluno);
        listaCREs.add(cre);
        listaMedias.add(media);
    }
    
    public void removerAluno(Aluno aluno) {
        validarListas();
        for (int i = 0; i < alunosInscritos.size(); i++) {
            if (alunosInscritos.get(i).getMatricula().equals(aluno.getMatricula())) {
                alunosInscritos.remove(i);
                listaCREs.remove(i);
                listaMedias.remove(i);
                return;
            }
        }
    }
    
    public void ordenarRanking(double pesoCRE, double pesoMedia) {
        // Algoritmo Bubble Sort
        // Ordena as 3 listas simultaneamente baseada na Nota Final
        for (int i = 0; i < alunosInscritos.size(); i++) {
            for (int j = 0; j < alunosInscritos.size() - 1; j++) {
                
                double notaAtual = (listaCREs.get(j) * pesoCRE) + (listaMedias.get(j) * pesoMedia);
                double notaProxima = (listaCREs.get(j+1) * pesoCRE) + (listaMedias.get(j+1) * pesoMedia);
                
                // Se a nota atual for menor que a outra, troca rapaz
                if (notaAtual < notaProxima) {
                    trocar(j, j+1);
                }
            }
        }
    }
    
    // Trocar posição do aluno
    private void trocar(int i, int j) {
        Aluno tempA = alunosInscritos.get(i);
        alunosInscritos.set(i, alunosInscritos.get(j));
        alunosInscritos.set(j, tempA);
        
        Double tempC = listaCREs.get(i);
        listaCREs.set(i, listaCREs.get(j));
        listaCREs.set(j, tempC);
        
        Double tempM = listaMedias.get(i);
        listaMedias.set(i, listaMedias.get(j));
        listaMedias.set(j, tempM);
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
    
    public int getTotalVagas() {
        return vagasRemuneradas + vagasVoluntarias;
    }
    
    
    public ArrayList<Aluno> getAlunosInscritos() {
    	validarListas();
        return alunosInscritos;
    }
    
    public ArrayList<Double> getListaCREs() {
    	validarListas();
    	return listaCREs;
    }
    
    public ArrayList<Double> getListaMedias() {
    	validarListas();
    	return listaMedias;
    }
    
    public String toString() {
    	return nome + " (Rem: " + vagasRemuneradas + ", Vol: " + vagasVoluntarias + ")";
    }
}