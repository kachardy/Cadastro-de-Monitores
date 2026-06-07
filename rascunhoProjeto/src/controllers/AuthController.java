package controllers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

import models.*;
import services.CentralFacade;
import utils.Validador;
import views.TelaCadastroAluno;
import views.TelaCadastroCoordenador;
import views.TelaLogin;

public class AuthController {
    private CentralFacade central;

    public AuthController(CentralFacade central) {
        this.central = central;
    }

    public void iniciar() {
        if (central.getCoordenador() == null) {
            exibirCadastroCoordenador();
        } else {
            exibirLogin();
        }
    }

    public void exibirLogin() {
        TelaLogin tela = new TelaLogin();

        // Ação do Botão Login (Método adicionarAcaoSalvar na TelaLogin)
        tela.adicionarAcaoSalvar(e -> {
            String email = tela.getEmail();
            String senha = tela.getSenha();

            // Validação
            if (!Validador.validarEmail(email) || !Validador.validarSenha(senha)) {
                JOptionPane.showMessageDialog(tela, "E-mail ou senha inválidos!");
                return;
            }

            // Busca no banco/central
            Pessoa pessoa = central.recuperarPessoaPorEmail(email);

            // Verifica se a pessoa existe e a senha bate
            if (pessoa != null && pessoa.getSenha().equals(senha)) {
                tela.dispose();

                // Redireciona para o painel correto dependendo do tipo de usuário
                if (pessoa instanceof Coordenador) {
                    new CoordenadorController((Coordenador) pessoa, central).exibirMenuPrincipal();
                } else if (pessoa instanceof Aluno) {
                    new AlunoController((Aluno) pessoa, central).exibirMenu();
                }
                return;
            }

            JOptionPane.showMessageDialog(tela, "Credenciais inválidas!");
        });

        // Ação do Botão Cancelar
        tela.adicionarAcaoCancelar(e -> {
            tela.dispose();
            System.exit(0); // Fecha o sistema
        });

        // Ação do Link de Cadastro (Método adicionarAcaoLinkCadastro na TelaLogin)
        tela.adicionarAcaoLinkCadastro(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
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
                central.adicionarAluno(novo);

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
            central.adicionarCoordenador(c);

            JOptionPane.showMessageDialog(tela, "Configuração inicial concluída!");
            tela.dispose();
            exibirLogin();
        });
        tela.setVisible(true);
    }
}