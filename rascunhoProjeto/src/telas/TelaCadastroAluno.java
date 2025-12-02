package telas;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseListener;
import javax.swing.SwingConstants;

public class TelaCadastroAluno extends TelaPadraoCadastro {
	
	private JLabel labelLinkLogin;
	
	public TelaCadastroAluno() {
		super();
		adicionarCabecalho("Cadastro de Aluno");
		
		labelLinkLogin = new JLabel("Já tem conta? Faça login aqui.");
        labelLinkLogin.setForeground(new Color(0, 128, 0));
        
        labelLinkLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        labelLinkLogin.setBounds(0, 420, 500, 30);
        labelLinkLogin.setHorizontalAlignment(SwingConstants.CENTER); // Centraliza
        
        add(labelLinkLogin);
	}
	
	
	 public void adicionarAcaoLinkLogin(MouseListener acao) {
	        labelLinkLogin.addMouseListener(acao);
	 }
	
	public static void main (String[] args) {
		new TelaCadastroAluno();
	}
}
