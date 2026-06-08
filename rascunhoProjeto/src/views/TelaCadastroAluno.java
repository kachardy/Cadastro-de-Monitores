package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class TelaCadastroAluno extends TelaPadraoCadastro {

	private JLabel labelLinkLogin;

	public TelaCadastroAluno() {
		super(); // Chama o construtor da TelaPadraoCadastro, que já monta o formulário
		setTitle("Cadastro de Aluno"); // Atualiza o título da janela (barra superior)

		// Configurando o link de login sem usar setBounds
		labelLinkLogin = new JLabel("Já tem conta? Faça login aqui.");
		labelLinkLogin.setForeground(new Color(0, 128, 0)); // Mantendo o seu verde
		labelLinkLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
		labelLinkLogin.setHorizontalAlignment(SwingConstants.CENTER);

		// Damos uma altura preferencial para dar um respiro no rodapé
		labelLinkLogin.setPreferredSize(new Dimension(500, 50));

		// Como a TelaPadraoCadastro usa BorderLayout, colamos este link no SUL (rodapé)
		add(labelLinkLogin, BorderLayout.SOUTH);
	}
}