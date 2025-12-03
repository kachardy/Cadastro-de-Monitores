package telas;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.*; 

public class TelaPrincipalPadrao extends JFrame {
	
	public TelaPrincipalPadrao() {
		
        // Ícone da Janela
        try {
            ImageIcon imagemIcone = new ImageIcon("ifpblogo.png");
            setIconImage(imagemIcone.getImage()); 
        } catch (Exception e) {
            System.out.println("Logo não encontrada");
        }
        
		setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        
	}	
	
	public void adicionarCabecalho(String titulo) {
		JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        labelTitulo.setOpaque(true);
        labelTitulo.setBackground(new Color(0, 100, 0)); 
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitulo.setBounds(0, 30, 500, 40); 
        add(labelTitulo);
	}
	
	public static void main(String[] args) {
		new TelaPrincipalPadrao();
	}
}