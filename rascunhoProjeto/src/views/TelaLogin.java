package views;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.*; // Importando Swing para usar JTextField e JPasswordField

public class TelaLogin extends JFrame {
	
	// Atributos
	private JTextField tfEmail;
	private JPasswordField tfSenha;
	private JButton botaoLogin;
	private JButton botaoCancelar;
	private JLabel labelLinkCadastro;
	
	public TelaLogin() {
		
		// Ícone da Janela
	    try {
	        ImageIcon imagemIcone = new ImageIcon("ifpblogo.png");
	        setIconImage(imagemIcone.getImage()); 
	    } catch (Exception e) {
	        System.out.println("Logo não encontrada");
	    }
	    
	    // Cabeçalho 
	    JLabel labelTitulo = new JLabel("Login");
	    labelTitulo.setFont(new Font("Arial", Font.BOLD, 26));
	    labelTitulo.setOpaque(true);
	    labelTitulo.setBackground(new Color(0, 128, 0)); 
	    labelTitulo.setForeground(Color.WHITE);
	    labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
	    labelTitulo.setBounds(0, 30, 500, 40);
	    
	    // Dimensões
		setSize(500, 600);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setResizable(false);
	    setLocationRelativeTo(null);
	    setLayout(null);
	    
	    // Labels
	    
	    JLabel labelEmail = new JLabel("Email: ");
	    labelEmail.setBounds(50, 150, 80, 30);
	    
	    JLabel labelSenha = new JLabel("Senha: ");
	    labelSenha.setBounds(50, 200, 80, 30);
	    
	    
	    // TextFields 
	    
	    tfEmail = new JTextField();
	    tfEmail.setBounds(130, 150, 280, 30);
	    
	    tfSenha = new JPasswordField(); 
	    tfSenha.setBounds(130, 200, 280, 30);
	    tfSenha.setEchoChar('*'); 
	    
	    // Botões
	    
	    botaoLogin = new JButton("Login");
	    botaoLogin.setBounds(110, 330, 110, 35);
	    botaoLogin.setBackground(new Color(200, 255, 200));
	    
	    botaoCancelar = new JButton("Cancelar");
	    botaoCancelar.setBounds(260, 330, 110, 35);
	    
	    // Link pra cadastro
	    labelLinkCadastro = new JLabel("Não tem conta? Faça o cadastro aqui.");
        labelLinkCadastro.setForeground(new Color(0, 128, 0));
        
        labelLinkCadastro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        labelLinkCadastro.setBounds(0, 420, 500, 30);
        labelLinkCadastro.setHorizontalAlignment(SwingConstants.CENTER);
        
	    
	    // Adicionando tudo na tela
	    
	    add(labelEmail);
	    add(tfEmail);
	    
	    add(labelSenha);
	    add(tfSenha);
	    
	    add(botaoLogin);
	    add(botaoCancelar);
	    
	    add(labelTitulo);
	    add(labelLinkCadastro);
	    
	    setVisible(true);
	    
	}
	
	// Getters
	public String getEmail() { 
		return tfEmail.getText(); 
	}
	public String getSenha() { 
		return new String(tfSenha.getPassword()); 
	}
	
	// Ação
	public void adicionarAcaoSalvar(ActionListener acao) {
		botaoLogin.addActionListener(acao);
	}
	
	public void adicionarAcaoCancelar(ActionListener acao) {
		botaoCancelar.addActionListener(acao);
	}
	
	public void adicionarAcaoLinkCadastro(MouseListener acao) {
        labelLinkCadastro.addMouseListener(acao);
	 }

	public static void main(String[] args) {
		new TelaLogin();
	}
}