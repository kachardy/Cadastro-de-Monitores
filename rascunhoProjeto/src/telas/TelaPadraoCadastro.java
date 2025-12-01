package telas;

import java.awt.Color;
import java.awt.Font;
import java.awt.TextField;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class TelaPadraoCadastro extends JFrame{
	
	public TelaPadraoCadastro() {
		
		
		// Ícone da Janela
        ImageIcon imagemIcone = new ImageIcon("ifpblogo.png");
        setIconImage(imagemIcone.getImage()); 
        
		setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        
        // Labels
        
        JLabel labelNome = new JLabel("Nome: ");
        labelNome.setBounds(50, 100, 80, 30);
        
        JLabel labelMatricula = new JLabel("Matrícula: ");
        labelMatricula.setBounds(50, 150, 80, 30);
        
        JLabel labelEmail = new JLabel("Email: ");
        labelEmail.setBounds(50, 200, 80, 30);
        
        JLabel labelSenha = new JLabel("Senha: ");
        labelSenha.setBounds(50, 250, 80, 30);
        
        // TextFields
        
        TextField tfNome = new TextField();
        tfNome.setBounds(130, 100, 280, 30);
        
        TextField tfMatricula = new TextField();
        tfMatricula.setBounds(130, 150, 280, 30);
        
        TextField tfEmail = new TextField();
        tfEmail.setBounds(130, 200, 280, 30);
        
        TextField tfSenha = new TextField();
        tfSenha.setEchoChar('*'); // Deixei como senha para ficar correto
        tfSenha.setBounds(130, 250, 280, 30);
        
        // Botões
        
        JButton botaoCadastrar = new JButton("Cadastrar");
        botaoCadastrar.setBounds(110, 330, 110, 35);
        
        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setBounds(260, 330, 110, 35);
        
        // Adicionando tudo na tela
        
        add(labelNome);
        add(tfNome);
        
        add(labelMatricula);
        add(tfMatricula);
        
        add(labelEmail);
        add(tfEmail);
        
        add(labelSenha);
        add(tfSenha);
        
        add(botaoCadastrar);
        add(botaoCancelar);
        
        // Deixando visível
        
        setVisible(true);
	}
	
	public void adicionarCabecalho(String titulo) {
		JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        labelTitulo.setOpaque(true);
        labelTitulo.setBackground(Color.GREEN);
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitulo.setBounds(0, 30, 500, 40);
        add(labelTitulo);
	}
	
	public static void main(String[] args) {
		new TelaPadraoCadastro();
	}
}