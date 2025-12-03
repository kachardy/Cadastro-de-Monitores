package telas;

import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class TelaListagemAluno extends TelaListagem {
	
	private JButton botaoInscrever;
	
	public TelaListagemAluno() {
		super(); 
		setTitle("Inscrição em Monitoria");
		
		botaoInscrever = new JButton("Inscrever-se");
		botaoInscrever.setBounds(200, 400, 180, 40);
		botaoInscrever.setBackground(new Color(100, 200, 255)); 
		
		add(botaoInscrever);
		
		repaint(); 
	}
	
	public void adicionarAcaoInscrever(ActionListener acao) {
		botaoInscrever.addActionListener(acao);
	}
}