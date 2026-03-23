package models;

import java.util.ArrayList;
import java.util.stream.Collectors;
import utils.ComparadorDeNotas;

public class GerenciadorDeInscricoes {

    private ArrayList<Inscricao> todasAsInscricoes = new ArrayList<>();

    public void realizarInscricao(Inscricao nova) {
        // Validação: Aluno já inscrito nesta disciplina específica?
        for (Inscricao i : todasAsInscricoes) {
            if (i.getCandidato().getMatricula().equals(nova.getCandidato().getMatricula()) &&
                    i.getDisciplina().getNome().equals(nova.getDisciplina().getNome())) {
                return;
            }
        }
        todasAsInscricoes.add(nova);
    }

    // Remove as inscrições do aluno em todas as disciplinas deste edital
    public void removerTodasInscricoesDoAluno(Aluno aluno) {
        todasAsInscricoes.removeIf(i -> i.getCandidato().getMatricula().equals(aluno.getMatricula()));
    }

    // Filtra as inscrições de uma disciplina específica para ranking ou exibição
    public ArrayList<Inscricao> getInscricoesPorDisciplina(Disciplina d) {
        ArrayList<Inscricao> filtradas = new ArrayList<>();
        for (Inscricao i : todasAsInscricoes) {
            if (i.getDisciplina().getNome().equals(d.getNome())) {
                filtradas.add(i);
            }
        }
        return filtradas;
    }

    public ArrayList<Disciplina> getDisciplinasPorAluno(String matricula) {
        ArrayList<Disciplina> resultado = new ArrayList<>();

        for (Inscricao i : todasAsInscricoes) {
            if (i.getCandidato().getMatricula().equals(matricula)) {
                resultado.add(i.getDisciplina());
            }
        }
        return resultado;
    }

    // O ranking agora é calculado pelo Gerenciador
    public void ordenarRanking(Disciplina d, double pesoCRE, double pesoMedia) {
        ArrayList<Inscricao> inscricoesDaDisciplina = getInscricoesPorDisciplina(d);
        inscricoesDaDisciplina.sort(new ComparadorDeNotas(pesoCRE, pesoMedia));
    }

    public ArrayList<Inscricao> getTodasAsInscricoes() {
        return todasAsInscricoes;
    }
}