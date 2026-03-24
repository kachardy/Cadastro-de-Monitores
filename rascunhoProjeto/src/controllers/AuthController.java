package controllers;

import javax.swing.JOptionPane;
import models.*;
import views.*;

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

            // Verifica se é nulo OU se está vazio (removendo espaços)
            if (isVazio(email) || isVazio(senha)) {
                JOptionPane.showMessageDialog(tela, "Por favor, preencha e-mail e senha!");
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
            // Validação múltipla de campos vazios
            if (isVazio(tela.getNome()) || isVazio(tela.getMatricula()) || isVazio(tela.getEmail()) || isVazio(tela.getSenha())) {
                JOptionPane.showMessageDialog(tela, "Todos os campos de cadastro são obrigatórios!");
                return;
            }

            Aluno novo = new Aluno(tela.getNome(), tela.getMatricula(), tela.getEmail(), tela.getSenha());
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

        tela.adicionarAcaoLinkLogin(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                tela.dispose();
                exibirLogin();
            }
        });

        tela.setVisible(true);
    }

    private void exibirCadastroCoordenador() {
        TelaCadastroCoordenador tela = new TelaCadastroCoordenador();
        tela.adicionarAcaoSalvar(e -> {
            // Validação de campos vazios para Coordenador
            if (isVazio(tela.getNome()) || isVazio(tela.getMatricula()) || isVazio(tela.getEmail()) || isVazio(tela.getSenha())) {
                JOptionPane.showMessageDialog(tela, "Preencha todos os dados do Coordenador!");
                return;
            }

            Coordenador c = new Coordenador(tela.getNome(), tela.getMatricula(), tela.getEmail(), tela.getSenha());
            central.setCoordenador(c);
            persistencia.salvarCentral(central, "central.xml");
            JOptionPane.showMessageDialog(tela, "Coordenador mestre configurado!");
            tela.dispose();
            exibirLogin();
        });
        tela.setVisible(true);
    }

    // Para manter o código limpo e evitar repetição
    private boolean isVazio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}