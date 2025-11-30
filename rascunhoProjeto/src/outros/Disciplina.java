package outros;

import java.util.ArrayList;
import pessoas.Aluno;

public class Disciplina {
    private String nome;
    private int qtdVagas;
    private ArrayList<Aluno> alunosInscritos = new ArrayList<>();

    public Disciplina(String nome, int qtdVagas) {
        this.nome = nome;
        this.qtdVagas = qtdVagas;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;;
    }
    
    public int getQtdVagas() {
    	return qtdVagas;
    }
    
    public void setQtdVagas(int qtdVagas) {
        this.qtdVagas = qtdVagas;
    }

    public ArrayList<Aluno> getAlunosInscritos() {
        return alunosInscritos;
    }

    public void adicionarAluno(Aluno aluno) {
        if (!alunosInscritos.contains(aluno)) {
            alunosInscritos.add(aluno);
        }
    }
}
