package controllers;

import javax.swing.JOptionPane;
import models.*;
import models.estados.FaseDoEdital;
import services.CentralDeInformacoes;
import views.*;

public class AlunoController {
    private Aluno aluno;
    private CentralDeInformacoes central;

    public AlunoController(Aluno aluno, CentralDeInformacoes central) {
        this.aluno = aluno;
        this.central = central;
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
            new AuthController(central).exibirLogin();
        });

        tela.setVisible(true);
    }

    private void exibirListagemEditais() {
        TelaListagemAluno telaLista = new TelaListagemAluno();
        telaLista.preencherTabela(central.getTodosOsEditais());

        telaLista.adicionarAcaoInscrever(e -> {
            Long id = telaLista.getIdEditalSelecionado();
            if (id == null) {
                JOptionPane.showMessageDialog(telaLista, "Selecione um edital.");
                return;
            }
            EditalDeMonitoria edital = central.recuperarEditalPeloId(id);
            if (edital.jaAcabou()) {
                JOptionPane.showMessageDialog(telaLista, "As inscrições já encerraram!");
                return;
            }
            telaLista.dispose();
            exibirInscricaoEdital(edital);
        });

        telaLista.adicionarAcaoDesistir(e -> {
            Long id = telaLista.getIdEditalSelecionado();
            if (id == null) {
                JOptionPane.showMessageDialog(telaLista, "Selecione um edital.");
                return;
            }
            EditalDeMonitoria edital = central.recuperarEditalPeloId(id);
            if (edital.getFase() == FaseDoEdital.HOMOLOGADO) {
                JOptionPane.showMessageDialog(telaLista, "O resultado final já saiu. Impossível desistir.");
                return;
            }

            // Se o edital removeu o aluno da lista com sucesso...
            if (edital.desistirDoEdital(aluno)) {
                // ...eu chamo o salvarEdital. Como o GenericDao usa o merge,
                // ele entende que é uma atualização e sincroniza a remoção no banco.
                central.salvarEdital(edital);

                JOptionPane.showMessageDialog(telaLista, "Desistência realizada!");
                telaLista.preencherTabela(central.getTodosOsEditais());
            } else {
                JOptionPane.showMessageDialog(telaLista, "Você não está inscrito neste edital.");
            }
        });

        telaLista.adicionarAcaoDetalhar(e -> {
            Long id = telaLista.getIdEditalSelecionado();
            if (id == null) return;
            EditalDeMonitoria edital = central.recuperarEditalPeloId(id);
            if (edital != null) {
                telaLista.dispose();
                exibirDetalhesEdital(edital);
            }
        });

        telaLista.adicionarAcaoResultado(e -> {
            Long id = telaLista.getIdEditalSelecionado();
            if (id == null) return;
            EditalDeMonitoria edital = central.recuperarEditalPeloId(id);
            if (edital.getFase() == FaseDoEdital.CALCULADO || edital.getFase() == FaseDoEdital.HOMOLOGADO) {
                TelaResultadoEdital telaRes = new TelaResultadoEdital(edital);
                telaRes.adicionarAcaoFechar(ev -> telaRes.dispose());
                telaRes.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(telaLista, "Resultado ainda não disponível.");
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
            try {
                double cre = Double.parseDouble(telaInscricao.getCRE());
                double media = Double.parseDouble(telaInscricao.getMedia());

                // Se a inscrição passou pelas validações do edital...
                if (edital.inscrever(aluno, disc, cre, media)) {
                    // ...eu mando salvar o edital. O JPA vai ver que tem uma nova linha
                    // na tabela de inscrições e vai fazer o INSERT automático pra mim.
                    central.salvarEdital(edital);

                    JOptionPane.showMessageDialog(telaInscricao, "Inscrito com sucesso!");
                    telaInscricao.dispose();
                    exibirListagemEditais();
                } else {
                    JOptionPane.showMessageDialog(telaInscricao, "Inscrição negada.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(telaInscricao, "Dados inválidos.");
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
            aluno.setNome(tela.getNome());
            aluno.setEmail(tela.getEmail());
            aluno.setSenha(tela.getSenha());

            // Aqui eu também poderia ter um central.salvarAluno(aluno) seguindo
            // a mesma lógica do edital para garantir que o perfil seja atualizado no banco.

            JOptionPane.showMessageDialog(tela, "Perfil atualizado!");
            tela.dispose();
            if (voltaPara != null) voltaPara.exibirMenuPrincipal();
            else exibirMenu();
        });
        tela.adicionarAcaoVoltar(e -> {
            tela.dispose();
            if (voltaPara != null) voltaPara.exibirMenuPrincipal();
            else exibirMenu();
        });
        tela.setVisible(true);
    }

    private void exibirDetalhesEdital(EditalDeMonitoria edital) {
        TelaDetalheEditalAluno telaDetalhes = new TelaDetalheEditalAluno(edital);
        telaDetalhes.configurarModoConsulta();
        telaDetalhes.adicionarAcaoVoltar(e -> {
            telaDetalhes.dispose();
            exibirListagemEditais();
        });
        telaDetalhes.setVisible(true);
    }
}