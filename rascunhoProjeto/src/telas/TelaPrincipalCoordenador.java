package telas;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.*;
import pessoas.Coordenador;

public class TelaPrincipalCoordenador extends TelaPrincipalPadrao {
    
    private JButton botaoCadastrarEdital;
    private JButton botaoListarEditais;
    private JButton botaoSair;
    
    // Construtor recebe o objeto Coordenador Logado
    public TelaPrincipalCoordenador(Coordenador coordenador) {
        super();
        adicionarCabecalho("Área do Coordenador");
        
        // Mensagem de bem-vindo
        JLabel labelBemVindo = new JLabel("Bem-vindo(a), " + coordenador.getNome());
        labelBemVindo.setFont(new Font("Arial", Font.PLAIN, 16));
        labelBemVindo.setBounds(30, 70, 400, 30);
        add(labelBemVindo);
        
        // Botões
        
        // Cadastrar Edital
        botaoCadastrarEdital = new JButton("Cadastrar Edital");
        botaoCadastrarEdital.setBounds(100, 130, 300, 50);
        botaoCadastrarEdital.setFont(new Font("Arial", Font.BOLD, 14));
        botaoCadastrarEdital.setBackground(new Color(220, 255, 220));
        
        // Listar Editais
        botaoListarEditais = new JButton("Listar Editais Abertos");
        botaoListarEditais.setBounds(100, 200, 300, 50);
        botaoListarEditais.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Sair / Logout
        botaoSair = new JButton("Sair");
        botaoSair.setBounds(180, 290, 140, 35);
        botaoSair.setBackground(new Color(255, 200, 200));
        
        // Adicionando tudo
        add(botaoCadastrarEdital);
        add(botaoListarEditais);
        add(botaoSair);
        
        repaint();
        validate();
        
        setVisible(true);
    }
    
    // Métodos controlar os cliques
    public void adicionarAcaoCadastrarEdital(ActionListener acao) {
        botaoCadastrarEdital.addActionListener(acao);
    }
    
    public void adicionarAcaoListarEditais(ActionListener acao) {
        botaoListarEditais.addActionListener(acao);
    }
    
    public void adicionarAcaoSair(ActionListener acao) {
        botaoSair.addActionListener(acao);
    }
    
    /*
    public static void main(String[] args) {
		new TelaPrincipalCoordenador();
	}
	*/
	
    
}