package controllers;

import javax.swing.JOptionPane;
import models.*;
import views.*;

public class AlunoController {
    private Aluno aluno;
    private CentralDeInformacoes central;
    private Persistencia persistencia;

    public AlunoController(Aluno aluno, CentralDeInformacoes central, Persistencia persistencia) {
        this.aluno = aluno;
        this.central = central;
        this.persistencia = persistencia;
    }

    public void exibirMenu() {
        TelaPrincipalAluno tela = new TelaPrincipalAluno(aluno);

        tela.adicionarAcaoListarEditais(e -> {
            tela.dispose();
            exibirListagemEditais();
        });

        tela.adicionarAcaoVerPerfil(e -> {
            tela.dispose();
            exibirPerfil(false, null);
        });

        tela.adicionarAcaoSair(e -> {
            tela.dispose();
            new AuthController(central, persistencia).exibirLogin();
        });

        tela.setVisible(true);
    }

    private void exibirListagemEditais() {
        TelaListagemAluno telaLista = new TelaListagemAluno();
        telaLista.preencherTabela(central.getTodosOsEditais());

        telaLista.adicionarAcaoInscrever(e -> {
            Long id = telaLista.getIdEditalSelecionado();
            EditalDeMonitoria edital = central.recuperarEditalPeloId(id);

            if (edital == null) {
                JOptionPane.showMessageDialog(telaLista, "Selecione um edital.");
                return;
            }
            if (edital.jaAcabou()) {
                JOptionPane.showMessageDialog(telaLista, "As inscrições já encerraram!");
                return;
            }

            telaLista.dispose();
            exibirInscricaoEdital(edital);
        });

        telaLista.adicionarAcaoDesistir(e -> {
            Long id = telaLista.getIdEditalSelecionado();
            EditalDeMonitoria edital = central.recuperarEditalPeloId(id);

            if (edital == null) {
                JOptionPane.showMessageDialog(telaLista, "Selecione um edital.");
                return;
            }
            if (edital.isResultadoFinal()) {
                JOptionPane.showMessageDialog(telaLista, "O resultado final já saiu. Impossível desistir.");
                return;
            }

            boolean ok = edital.desistirDoEdital(aluno);
            if (ok) {
                persistencia.salvarCentral(central, "central.xml");
                JOptionPane.showMessageDialog(telaLista, "Desistência realizada!");
                telaLista.preencherTabela(central.getTodosOsEditais());
            } else {
                JOptionPane.showMessageDialog(telaLista, "Você não está inscrito neste edital.");
            }
        });

        telaLista.adicionarAcaoVoltar(e -> {
            telaLista.dispose();
            exibirMenu();
        });

        telaLista.setVisible(true);
    }

    private void exibirInscricaoEdital(EditalDeMonitoria edital) {
        TelaDetalheEditalAluno telaInscricao = new TelaDetalheEditalAluno(edital);

        telaInscricao.adicionarAcaoInscrever(e -> {
            Disciplina disc = telaInscricao.getDisciplinaSelecionada();
            String creStr = telaInscricao.getCRE();
            String mediaStr = telaInscricao.getMedia();

            if (disc == null || creStr.isEmpty() || mediaStr.isEmpty()) {
                JOptionPane.showMessageDialog(telaInscricao, "Preencha todos os campos e selecione a disciplina.");
                return;
            }

            try {
                double cre = Double.parseDouble(creStr);
                double media = Double.parseDouble(mediaStr);

                boolean sucesso = edital.inscrever(aluno, disc, cre, media);
                if (sucesso) {
                    persistencia.salvarCentral(central, "central.xml");
                    JOptionPane.showMessageDialog(telaInscricao, "Inscrito com sucesso em " + disc.getNome());
                    telaInscricao.dispose();
                    exibirListagemEditais();
                } else {
                    JOptionPane.showMessageDialog(telaInscricao, "Inscrição negada (Verifique vagas ou limite de editais).");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(telaInscricao, "CRE e Média devem ser números.");
            }
        });

        telaInscricao.adicionarAcaoVoltar(e -> {
            telaInscricao.dispose();
            exibirListagemEditais();
        });

        telaInscricao.setVisible(true);
    }

    public void exibirPerfil(boolean modoLeitura, CoordenadorController voltaPara) {
        TelaPerfilAluno tela = new TelaPerfilAluno(aluno, modoLeitura);

        tela.adicionarAcaoSalvar(e -> {
            String novoNome = tela.getNome();
            String novoEmail = tela.getEmail();
            String novaSenha = tela.getSenha();

            if (novoNome.isEmpty() || novoEmail.isEmpty() || novaSenha.isEmpty()) {
                JOptionPane.showMessageDialog(tela, "Preencha todos os campos!");
                return;
            }

            aluno.setNome(novoNome);
            aluno.setEmail(novoEmail);
            aluno.setSenha(novaSenha);

            // NOVA ALTERAÇÃO: Sincronização com os editais através da classe Inscricao
            for (EditalDeMonitoria ed : central.getTodosOsEditais()) {
                for (Disciplina d : ed.getTodasAsDisciplinas()) {
                    // Percorre a lista única de inscrições para encontrar e atualizar o aluno
                    for (Inscricao insc : ed.getGerenciador().getTodasAsInscricoes()) {
                        if (insc.getCandidato().getMatricula().equals(aluno.getMatricula())) {
                            insc.getCandidato().setNome(novoNome);
                            insc.getCandidato().setEmail(novoEmail);
                            insc.getCandidato().setSenha(novaSenha);
                        }
                    }
                }
            }

            persistencia.salvarCentral(central, "central.xml");
            JOptionPane.showMessageDialog(tela, "Perfil atualizado com sucesso!");

            tela.dispose();
            if (voltaPara != null) {
                voltaPara.exibirMenuPrincipal();
            } else {
                exibirMenu();
            }
        });

        tela.adicionarAcaoVoltar(e -> {
            tela.dispose();
            if (voltaPara != null) voltaPara.exibirMenuPrincipal();
            else exibirMenu();
        });

        tela.setVisible(true);
    }
}