package models.estados;

import models.Aluno;
import models.Disciplina;
import models.EditalDeMonitoria;
import models.Inscricao;

public enum FaseDoEdital {

    ABERTO {
        @Override
        public boolean inscrever(EditalDeMonitoria edital, Aluno aluno, Disciplina disc, double cre, double media) {
            // Se o prazo passou, fazemos uma "auto-transição" para ENCERRADO e negamos a inscrição
            if (edital.jaAcabou()) {
                edital.setFase(ENCERRADO);
                return false;
            }

            Inscricao nova = new Inscricao(aluno, disc, cre, media);
            if (!edital.getGerenciador().validarNovaInscricao(edital.getInscricoesRealizadas(), nova)) {
                return false;
            }

            edital.getInscricoesRealizadas().add(nova);
            return true;
        }

        @Override
        public boolean desistir(EditalDeMonitoria edital, Aluno aluno) {
            return edital.getInscricoesRealizadas().removeIf(i -> i.getCandidato().equals(aluno));
        }

        @Override
        public void calcularResultado(EditalDeMonitoria edital) {
            throw new IllegalStateException("Não podes calcular o resultado enquanto o edital recebe inscrições.");
        }

        @Override
        public void homologar(EditalDeMonitoria edital) {
            throw new IllegalStateException("O edital precisa de ser calculado antes de ser homologado.");
        }
    },

    ENCERRADO {
        @Override
        public boolean inscrever(EditalDeMonitoria edital, Aluno aluno, Disciplina disc, double cre, double media) {
            return false; // Edital fechado, logo não aceita inscrições!
        }

        @Override
        public boolean desistir(EditalDeMonitoria edital, Aluno aluno) {
            return edital.getInscricoesRealizadas().removeIf(i -> i.getCandidato().equals(aluno));
        }

        @Override
        public void calcularResultado(EditalDeMonitoria edital) {
            for (Disciplina d : edital.getTodasAsDisciplinas()) {
                edital.getGerenciador().ordenarRanking(edital.getInscricoesRealizadas(), d, edital.getPesoCRE(), edital.getPesoMedia());
            }
            // Transição de Estado
            edital.setFase(CALCULADO);
        }

        @Override
        public void homologar(EditalDeMonitoria edital) {
            throw new IllegalStateException("O resultado precisa de ser calculado primeiro.");
        }
    },

    CALCULADO {
        @Override
        public boolean inscrever(EditalDeMonitoria edital, Aluno aluno, Disciplina disc, double cre, double media) { return false; }

        @Override
        public boolean desistir(EditalDeMonitoria edital, Aluno aluno) {
            return edital.getInscricoesRealizadas().removeIf(i -> i.getCandidato().equals(aluno));
        }

        @Override
        public void calcularResultado(EditalDeMonitoria edital) {
            // Se o coordenador quiser recalcular por algum motivo
            for (Disciplina d : edital.getTodasAsDisciplinas()) {
                edital.getGerenciador().ordenarRanking(edital.getInscricoesRealizadas(), d, edital.getPesoCRE(), edital.getPesoMedia());
            }
        }

        @Override
        public void homologar(EditalDeMonitoria edital) {
            // Transição Final
            edital.setFase(HOMOLOGADO);
        }
    },

    HOMOLOGADO {
        @Override
        public boolean inscrever(EditalDeMonitoria edital, Aluno aluno, Disciplina disc, double cre, double media) { return false; }

        @Override
        public boolean desistir(EditalDeMonitoria edital, Aluno aluno) { return false; } // Não se desiste de resultado final

        @Override
        public void calcularResultado(EditalDeMonitoria edital) { throw new IllegalStateException("Edital finalizado e selado."); }

        @Override
        public void homologar(EditalDeMonitoria edital) { throw new IllegalStateException("Edital já se encontra homologado."); }
    };

    // Toda a fase é obrigada a dizer como reage a estas 4 ações
    public abstract boolean inscrever(EditalDeMonitoria edital, Aluno aluno, Disciplina disc, double cre, double media);
    public abstract boolean desistir(EditalDeMonitoria edital, Aluno aluno);
    public abstract void calcularResultado(EditalDeMonitoria edital);
    public abstract void homologar(EditalDeMonitoria edital);
}