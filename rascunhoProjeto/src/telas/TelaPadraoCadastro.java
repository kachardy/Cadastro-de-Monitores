package telas;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*; 

public class TelaPadraoCadastro extends JFrame {
	
	public TelaPadraoCadastro() {
		
        // Ícone da Janela
        try {
            ImageIcon imagemIcone = new ImageIcon("ifpblogo.png");
            setIconImage(imagemIcone.getImage()); 
        } catch (Exception e) {
            System.out.println("Logo não encontrada");
        }
        
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
        
        
        JTextField tfNome = new JTextField();
        tfNome.setBounds(130, 100, 280, 30);
        
        JTextField tfMatricula = new JTextField();
        tfMatricula.setBounds(130, 150, 280, 30);
        
        JTextField tfEmail = new JTextField();
        tfEmail.setBounds(130, 200, 280, 30);
        
        JPasswordField tfSenha = new JPasswordField();
        tfSenha.setEchoChar('*');
        tfSenha.setBounds(130, 250, 280, 30);
        
        // Botões
        
        JButton botaoCadastrar = new JButton("Cadastrar");
        botaoCadastrar.setBounds(110, 330, 110, 35);
        botaoCadastrar.setBackground(new Color(200, 255, 200));
        
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
        
        setVisible(true);
	}
	
	public void adicionarCabecalho(String titulo) {
		JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        labelTitulo.setOpaque(true);
        labelTitulo.setBackground(new Color(0, 128, 0)); // Verde Padrão
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitulo.setBounds(0, 30, 500, 40); 
        add(labelTitulo);
	}
	
	public static void main(String[] args) {
		new TelaPadraoCadastro();
	}
}