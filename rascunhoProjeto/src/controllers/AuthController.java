package controllers;

import javax.swing.JOptionPane;
import models.*;
import services.CentralDeInformacoes;
import utils.Validador;
import views.TelaCadastroAluno;
import views.TelaCadastroCoordenador;
import views.TelaLogin;

public class AuthController {
    private CentralDeInformacoes central;

    // O construtor agora recebe apenas a central, pois a Persistencia é estática
    public AuthController(CentralDeInformacoes central) {
        this.central = central;
    }

    public void iniciar() {
        // A central busca o coordenador no banco via DAO agora
        if (central.getCoordenador() == null) {
            exibirCadastroCoordenador();
        } else {
            exibirLogin();
        }
    }

    public void exibirLogin() {
        TelaLogin tela = new TelaLogin();

        tela.adicionarAcaoSalvar(e -> {
            String email = tela.getEmail();
            String senha = tela.getSenha();

            if (!Validador.validarEmail(email) || !Validador.validarSenha(senha)) {
                JOptionPane.showMessageDialog(tela, "E-mail ou senha inválidos!");
                return;
            }

            // Busca unificada via PessoaDao (que você já criou) através da Central
            Pessoa pessoa = central.recuperarPessoaPorEmail(email);

            if (pessoa != null && pessoa.getSenha().equals(senha)) {
                tela.dispose();
                if (pessoa instanceof Coordenador) {
                    new CoordenadorController((Coordenador) pessoa, central).exibirMenuPrincipal();
                } else if (pessoa instanceof Aluno) {
                    new AlunoController((Aluno) pessoa, central).exibirMenu();
                }
                return;
            }

            JOptionPane.showMessageDialog(tela, "Credenciais inválidas!");
        });

        tela.adicionarAcaoLinkCadastro(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                tela.dispose();
                exibirCadastroAluno();
            }
        });

        tela.setVisible(true);
    }

    private void exibirCadastroAluno() {
        TelaCadastroAluno tela = new TelaCadastroAluno();

        tela.adicionarAcaoSalvar(e -> {
            String nome = tela.getNome();
            String matricula = tela.getMatricula();
            String email = tela.getEmail();
            String senha = tela.getSenha();

            if (!Validador.validarNome(nome) || !Validador.validarMatricula(matricula) ||
                    !Validador.validarEmail(email) || !Validador.validarSenha(senha)) {
                JOptionPane.showMessageDialog(tela, "Dados inválidos!");
                return;
            }

            Aluno novo = new Aluno(nome, matricula, email, senha);
            try {
                // A central.adicionarAluno agora chama o alunoDao.salvar() internamente
                central.adicionarAluno(novo);
                // REMOVIDO: persistencia.salvarCentral(central, "central.xml");

                JOptionPane.showMessageDialog(tela, "Aluno cadastrado com sucesso!");
                tela.dispose();
                exibirLogin();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(tela, ex.getMessage());
            }
        });

        tela.adicionarAcaoCancelar(e -> {
            tela.dispose();
            exibirLogin();
        });

        tela.setVisible(true);
    }

    private void exibirCadastroCoordenador() {
        TelaCadastroCoordenador tela = new TelaCadastroCoordenador();
        tela.adicionarAcaoSalvar(e -> {
            String nome = tela.getNome();
            String matricula = tela.getMatricula();
            String email = tela.getEmail();
            String senha = tela.getSenha();

            if (!Validador.validarNome(nome) || !Validador.validarEmail(email)) {
                JOptionPane.showMessageDialog(tela, "Dados do Coordenador inválidos!");
                return;
            }

            Coordenador c = new Coordenador(nome, matricula, email, senha);
            // A central agora persiste o coordenador no banco imediatamente
            central.adicionarCoordenador(c);

            JOptionPane.showMessageDialog(tela, "Configuração inicial concluída!");
            tela.dispose();
            exibirLogin();
        });
        tela.setVisible(true);
    }
}