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

        // Ação de Inscrever
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

        // Ação de Desistir
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
                telaLista.preencherTabela(central.getTodosOsEditais()); // Atualiza a tabela
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
        
        // Se for apenas leitura (coordenador vendo), podemos desabilitar o botão salvar na tela
        // ou apenas tratar aqui no controller
        tela.adicionarAcaoSalvar(e -> {
            String novoNome = tela.getNome();
            String novoEmail = tela.getEmail();
            String novaSenha = tela.getSenha();
            
            if (novoNome.isEmpty() || novoEmail.isEmpty() || novaSenha.isEmpty()) {
                JOptionPane.showMessageDialog(tela, "Preencha todos os campos!");
                return;
            }
            
            // Atualiza os dados do objeto Aluno
            aluno.setNome(novoNome);
            aluno.setEmail(novoEmail);
            aluno.setSenha(novaSenha);
            
            // Sincroniza com os editais
            for (EditalDeMonitoria ed : central.getTodosOsEditais()) {
                for (Disciplina d : ed.getTodasAsDisciplinas()) {
                    for (Aluno aInscrito : d.getAlunosInscritos()) {
                        if (aInscrito.getMatricula().equals(aluno.getMatricula())) {
                            aInscrito.setNome(novoNome);
                            aInscrito.setEmail(novoEmail);
                            aInscrito.setSenha(novaSenha);
                        }
                    }
                }
            }
            
            // Persistência
            persistencia.salvarCentral(central, "central.xml");
            JOptionPane.showMessageDialog(tela, "Perfil atualizado com sucesso!");
            
            // Inteligência de Navegação
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