package telas;

import java.awt.TextField;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class TelaCadastro extends JFrame{
	

	public TelaCadastro() {
        setTitle("Cadastro de Coordenador");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        
        JLabel labelMatricula = new JLabel("Matrícula: ");
        labelMatricula.setBounds(50, 50, 80, 30);
        
        JLabel labelEmail = new JLabel("Email: ");
        labelEmail.setBounds(50, 90, 80, 30);
        
        JLabel labelSenha = new JLabel("Senha: ");
        labelSenha.setBounds(50, 130, 80, 30);
        
        TextField tfMatricula = new TextField();
        tfMatricula.setBounds(140, 50, 100, 30);
        
        TextField tfEmail = new TextField();
        tfEmail.setBounds(140, 90, 100, 30);
        
        TextField tfSenha = new TextField();
        tfSenha.setBounds(140, 130, 100, 30);
        
        JButton botaoCadastrar = new JButton("Cadastrar");
        botaoCadastrar.setBounds(50,170,90,30);
        
        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setBounds(210,170,90,30);
        
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
	
	public static void main (String[] args) {
		new TelaCadastro();
	}
}
