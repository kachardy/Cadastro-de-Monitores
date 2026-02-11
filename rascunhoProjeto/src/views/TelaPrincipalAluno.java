package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.*;

import models.Aluno;

public class TelaPrincipalAluno extends TelaPrincipalPadrao {
    
    private JButton botaoListarEditais;
    private JButton botaoPerfil; // NOVO BOTÃO
    private JButton botaoSair;
    
    public TelaPrincipalAluno(Aluno aluno) {
        super();
        adicionarCabecalho("Área do Aluno");
        
        // Mensagem de boas-vindas
        JLabel labelBemVindo = new JLabel("Bem-vindo(a), " + aluno.getNome());
        labelBemVindo.setFont(new Font("Arial", Font.PLAIN, 16));
        labelBemVindo.setBounds(30, 70, 400, 30);
        add(labelBemVindo);
        
    
        
        // Listar
        botaoListarEditais = new JButton("Ver Editais Disponíveis");
        botaoListarEditais.setBounds(100, 130, 300, 50);
        botaoListarEditais.setFont(new Font("Arial", Font.BOLD, 14));
        botaoListarEditais.setBackground(new Color(220, 255, 255)); 
        
        // Perfil 
        botaoPerfil = new JButton("Meu Perfil");
        botaoPerfil.setBounds(100, 200, 300, 50);
        botaoPerfil.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Sair
        botaoSair = new JButton("Sair");
        botaoSair.setBounds(180, 290, 140, 35);
        botaoSair.setBackground(new Color(255, 200, 200)); 
        
        add(botaoListarEditais);
        add(botaoPerfil); 
        add(botaoSair);
        
        setVisible(true);
    }
    
    // Ações
    public void adicionarAcaoListarEditais(ActionListener acao) {
        botaoListarEditais.addActionListener(acao);
    }
    
    public void adicionarAcaoVerPerfil(ActionListener acao) {
        botaoPerfil.addActionListener(acao);
    }
    
    public void adicionarAcaoSair(ActionListener acao) {
        botaoSair.addActionListener(acao);
    }
}