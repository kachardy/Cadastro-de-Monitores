package views;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Cursor;
import javax.swing.SwingConstants;

public class TelaCadastroAluno extends TelaPadraoCadastro {

	private JLabel labelLinkLogin;

	public TelaCadastroAluno() {
		super(); // Chama o construtor da TelaPadraoCadastro

		// Usando o método herdado da TelaBase com a largura correta (500)
		adicionarCabecalho("Cadastro de Aluno", 500);

		labelLinkLogin = new JLabel("Já tem conta? Faça login aqui.");
		labelLinkLogin.setForeground(new Color(0, 128, 0));
		labelLinkLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
		labelLinkLogin.setBounds(0, 420, 500, 30);
		labelLinkLogin.setHorizontalAlignment(SwingConstants.CENTER); // Centraliza

		add(labelLinkLogin);
	}
}