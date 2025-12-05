package telas;

import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class TelaListagemAluno extends TelaListagem {
	
	private JButton botaoInscrever;
	private JButton botaoDesistir;
	
	public TelaListagemAluno() {
		super(); 
		setTitle("Inscrição em Monitoria");
		
		botaoInscrever = new JButton("Inscrever-se");
		botaoInscrever.setBounds(190, 400, 160, 40);
		botaoInscrever.setBackground(new Color(100, 200, 255)); 
		
		botaoDesistir = new JButton("D/R");
		botaoDesistir.setBounds(365, 400, 140, 40); 
		botaoDesistir.setBackground(new Color(255, 220, 200)); 
		botaoDesistir.setToolTipText("Ver Resultados ou Desistir da vaga");
		
		add(botaoInscrever);
		add(botaoDesistir);
		
		repaint(); 
	}
	
	public void adicionarAcaoInscrever(ActionListener acao) {
		botaoInscrever.addActionListener(acao);
	}
	
	public void adicionarAcaoDesistir(ActionListener acao) {
		botaoDesistir.addActionListener(acao);
	}
}