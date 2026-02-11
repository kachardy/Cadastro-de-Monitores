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

    // Inicia o fluxo do programa
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
            
            // Lógica de Autenticação
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
            Aluno novo = new Aluno(tela.getNome(), tela.getMatricula(), tela.getEmail(), tela.getSenha());
            try {
                central.adicionarAluno(novo);
                persistencia.salvarCentral(central, "central.xml");
                JOptionPane.showMessageDialog(tela, "Aluno cadastrado!");
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
            Coordenador c = new Coordenador(tela.getNome(), tela.getMatricula(), tela.getEmail(), tela.getSenha());
            central.setCoordenador(c);
            persistencia.salvarCentral(central, "central.xml");
            tela.dispose();
            exibirLogin();
        });
        tela.setVisible(true);
    }
}