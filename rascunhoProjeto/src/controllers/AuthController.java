package controllers;

import javax.swing.JOptionPane;
import models.*;
import views.*;
import utils.Validador; // Certifique-se de importar o Validador

public class AuthController {
    private CentralDeInformacoes central;
    private Persistencia persistencia;

    public AuthController(CentralDeInformacoes central, Persistencia persistencia) {
        this.central = central;
        this.persistencia = persistencia;
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

        tela.adicionarAcaoSalvar(e -> {
            String email = tela.getEmail();
            String senha = tela.getSenha();

            // USO DO VALIDADOR: Login
            if (!Validador.validarEmail(email)) {
                JOptionPane.showMessageDialog(tela, "E-mail inválido ou vazio!");
                return;
            }

            if (!Validador.validarSenha(senha)) {
                JOptionPane.showMessageDialog(tela, "A senha deve ter no mínimo 6 caracteres!");
                return;
            }

            Coordenador coord = central.getCoordenador();
            if (coord != null && coord.getEmail().equals(email) && coord.getSenha().equals(senha)) {
                tela.dispose();
                new CoordenadorController(coord, central, persistencia).exibirMenuPrincipal();
                return;
            }

            for (Aluno a : central.getTodosOsAlunos()) {
                if (a.getEmail().equals(email) && a.getSenha().equals(senha)) {
                    tela.dispose();
                    new AlunoController(a, central, persistencia).exibirMenu();
                    return;
                }
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

            // USO DO VALIDADOR: Cadastro de Aluno
            if (!Validador.validarNome(nome) ||
                    !Validador.validarMatricula(matricula) ||
                    !Validador.validarEmail(email) ||
                    !Validador.validarSenha(senha)) {

                // Mensagem genérica caso algum dos outros (exceto nome) falhe
                JOptionPane.showMessageDialog(tela, "Verifique os campos! Matricula (apenas números), E-mail válido e Senha (mín. 6)");
                return;
            }

            Aluno novo = new Aluno(nome, matricula, email, senha);
            try {
                central.adicionarAluno(novo);
                persistencia.salvarCentral(central, "central.xml");
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

            // Cadastro de Coordenador
            if (!Validador.validarNome(nome) ||
                    !Validador.validarMatricula(matricula) ||
                    !Validador.validarEmail(email) ||
                    !Validador.validarSenha(senha)) {

                JOptionPane.showMessageDialog(tela, "Preencha todos os dados corretamente para o Coordenador!");
                return;
            }

            Coordenador c = new Coordenador(nome, matricula, email, senha);
            central.setCoordenador(c);
            persistencia.salvarCentral(central, "central.xml");
            JOptionPane.showMessageDialog(tela, "Configuração inicial concluída!");
            tela.dispose();
            exibirLogin();
        });
        tela.setVisible(true);
    }
}