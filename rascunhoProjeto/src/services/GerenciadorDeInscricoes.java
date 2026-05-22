package services;

import java.util.ArrayList;
import java.util.List;

import models.Disciplina;
import models.Inscricao;
import utils.ComparadorDeNotas;

// Removemos as anotações @Embeddable, @Entity ou @Table.
// Esta classe agora é um utilitário puro de regras de negócio.
public class GerenciadorDeInscricoes {

    // Recebe a lista oficial do banco e verifica se o aluno já está naquela disciplina
    public boolean validarNovaInscricao(List<Inscricao> todasAsInscricoes, Inscricao nova) {
        for (Inscricao i : todasAsInscricoes) {
            if (i.getCandidato().getMatricula().equals(nova.getCandidato().getMatricula()) &&
                    i.getDisciplina().getNome().equals(nova.getDisciplina().getNome())) {
                return false; // Retorna falso se já estiver inscrito
            }
        }
        return true; // Retorna verdadeiro se puder inscrever
    }

    // Filtra as inscrições de uma disciplina específica para ranking ou exibição
    public List<Inscricao> getInscricoesPorDisciplina(List<Inscricao> todasAsInscricoes, Disciplina d) {
        List<Inscricao> filtradas = new ArrayList<>();
        for (Inscricao i : todasAsInscricoes) {
            if (i.getDisciplina().getNome().equals(d.getNome())) {
                filtradas.add(i);
            }
        }
        return filtradas;
    }

    // Retorna as disciplinas nas quais um aluno específico se inscreveu
    public List<Disciplina> getDisciplinasPorAluno(List<Inscricao> todasAsInscricoes, String matricula) {
        List<Disciplina> resultado = new ArrayList<>();
        for (Inscricao i : todasAsInscricoes) {
            if (i.getCandidato().getMatricula().equals(matricula)) {
                resultado.add(i.getDisciplina());
            }
        }
        return resultado;
    }

    // O ranking agora recebe a lista do edital, filtra pela disciplina e ordena
    public void ordenarRanking(List<Inscricao> todasAsInscricoes, Disciplina d, double pesoCRE, double pesoMedia) {
        List<Inscricao> inscricoesDaDisciplina = getInscricoesPorDisciplina(todasAsInscricoes, d);
        inscricoesDaDisciplina.sort(new ComparadorDeNotas(pesoCRE, pesoMedia));
    }
}