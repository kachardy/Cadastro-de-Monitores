package views;

import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.awt.Cursor;

public class TelaListagemAluno extends TelaListagem {
	
	private JButton botaoInscrever;
	private JButton botaoDesistir;
	private JButton botaoResultado;
	
	public TelaListagemAluno() {
		super(); 
		setTitle("Inscrição em Monitoria");
		
		botaoInscrever = new JButton("Inscrever-se");
		botaoInscrever.setBounds(190, 400, 160, 40);
		botaoInscrever.setBackground(new Color(100, 200, 255)); 
		
		botaoDesistir = new JButton("Desistir");
		botaoDesistir.setBounds(365, 400, 140, 40); 
		botaoDesistir.setBackground(new Color(255, 220, 200)); 
		
		botaoResultado = new JButton("Ver Resultado"); 
		botaoResultado.setBounds(530, 50, 120, 25); // Botão pequeno
		botaoResultado.setForeground(new Color(0, 100, 200)); 
		botaoResultado.setOpaque(false);
		botaoResultado.setContentAreaFilled(false); // Sem preencimento
		botaoResultado.setBorderPainted(false);	// Sem borda
		botaoResultado.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		add(botaoInscrever);
		add(botaoDesistir);
		add(botaoResultado);
		
		setVisible(true);
	}
	
	public void adicionarAcaoInscrever(ActionListener acao) {
		botaoInscrever.addActionListener(acao);
	}
	
	public void adicionarAcaoDesistir(ActionListener acao) {
		botaoDesistir.addActionListener(acao);
	}
	
	public void adicionarAcaoResultado(ActionListener acao) {
		botaoResultado.addActionListener(acao);
	}
}