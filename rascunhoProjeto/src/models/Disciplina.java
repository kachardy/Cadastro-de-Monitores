package models;

import java.util.ArrayList;

public class Disciplina {

    private String nome;
    private int vagasRemuneradas;
    private int vagasVoluntarias;

    // NOVA ALTERAÇÃO: Substituição das 3 listas paralelas por uma única lista de Inscrições
    private ArrayList<Inscricao> inscricoes = new ArrayList<>();

    public Disciplina(String nome, int vagasRemuneradas, int vagasVoluntarias) {
        this.nome = nome;
        this.vagasRemuneradas = vagasRemuneradas;
        this.vagasVoluntarias = vagasVoluntarias;
    }

    // NOVA ALTERAÇÃO: Garante que a nova lista não seja nula
    private void validarInscricoes() {
        if (inscricoes == null) inscricoes = new ArrayList<>();
    }

    public void adicionarAluno(Aluno aluno, double cre, double media) {
        validarInscricoes();

        // Valida a matrícula buscando dentro do objeto candidato da Inscrição
        for(Inscricao inscricao : inscricoes) {
            if(inscricao.getCandidato().getMatricula().equals(aluno.getMatricula())) {
                return; // Já está inscrito, não faz nada
            }
        }

        // NOVA ALTERAÇÃO: Cria o objeto Inscrição e adiciona na lista única
        Inscricao novaInscricao = new Inscricao(aluno, this, cre, media);
        inscricoes.add(novaInscricao);
    }

    public void removerAluno(Aluno aluno) {
        validarInscricoes();
        for (int i = 0; i < inscricoes.size(); i++) {
            if (inscricoes.get(i).getCandidato().getMatricula().equals(aluno.getMatricula())) {
                // NOVA ALTERAÇÃO: Remove apenas um objeto da lista
                inscricoes.remove(i);
                return;
            }
        }
    }

    public void ordenarRanking(double pesoCRE, double pesoMedia) {
        this.inscricoes.sort(new utils.ComparadorDeNotas(pesoCRE, pesoMedia));
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

    // NOVA ALTERAÇÃO: Único Getter necessário agora para recuperar os alunos e notas
    public ArrayList<Inscricao> getInscricoes() {
        validarInscricoes();
        return inscricoes;
    }

    public String toString() {
        return nome + " (Rem: " + vagasRemuneradas + ", Vol: " + vagasVoluntarias + ")";
    }
}