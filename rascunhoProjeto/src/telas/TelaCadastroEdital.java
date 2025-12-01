package telas;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import javax.swing.text.MaskFormatter;

public class TelaCadastroEdital extends JFrame {
	
	public TelaCadastroEdital() {
		
		// Configurações da Janela
	    setTitle("Cadastro de Edital");
	    setSize(500, 650); 
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setResizable(false);
	    setLocationRelativeTo(null);
	    setLayout(null);
	    
	    // Ícone da Janela
	    try {
	        ImageIcon imagemIcone = new ImageIcon("ifpblogo.png");
	        setIconImage(imagemIcone.getImage()); 
	    } catch (Exception e) {
	        System.out.println("Logo não encontrada");
	    }
	    
	    // Cabeçalho
	    JLabel labelTitulo = new JLabel("Cadastro do Edital");
	    labelTitulo.setFont(new Font("Arial", Font.BOLD, 26));
	    labelTitulo.setOpaque(true);
	    labelTitulo.setBackground(new Color(0, 128, 0)); // Verde mais escuro
	    labelTitulo.setForeground(Color.WHITE);
	    labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
	    labelTitulo.setBounds(0, 0, 500, 50);
	    add(labelTitulo);
	    
	    // Máscaras
	    MaskFormatter mascaraData = null;
        try {
            mascaraData = new MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter('_');
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Datas
        
        JLabel labelDataInicio = new JLabel("Início Inscrições:");
        labelDataInicio.setBounds(30, 70, 120, 30);
        JFormattedTextField tfDataInicio = new JFormattedTextField(mascaraData);
        tfDataInicio.setBounds(150, 70, 100, 30);
        
        JLabel labelDataFim = new JLabel("Fim Inscrições:");
        labelDataFim.setBounds(270, 70, 100, 30);
        JFormattedTextField tfDataFim = new JFormattedTextField(mascaraData);
        tfDataFim.setBounds(370, 70, 90, 30);
        
        // Regras do Edital
        
        // Requisito 4: Max Inscrições
        JLabel labelMaxInsc = new JLabel("Max. Inscrições por Aluno:");
        labelMaxInsc.setBounds(30, 120, 180, 30);
        SpinnerNumberModel modelMax = new SpinnerNumberModel(1, 1, 10, 1);
        JSpinner spinnerMaxInsc = new JSpinner(modelMax);
        spinnerMaxInsc.setBounds(200, 120, 50, 30);
        
        // Requisito 5: Fórmula (Pesos)
        JLabel labelFormula = new JLabel("--- Fórmula de Ranqueamento (Soma deve ser 1.0) ---");
        labelFormula.setFont(new Font("Arial", Font.BOLD, 12));
        labelFormula.setBounds(30, 160, 400, 20);
        
        JLabel labelPesoCRE = new JLabel("Peso CRE:");
        labelPesoCRE.setBounds(30, 190, 80, 30);
        // Spinner para decimais (0.0 a 1.0, passo 0.1)
        SpinnerNumberModel modelCRE = new SpinnerNumberModel(0.5, 0.0, 1.0, 0.1);
        JSpinner spinnerPesoCRE = new JSpinner(modelCRE);
        spinnerPesoCRE.setBounds(100, 190, 60, 30);
        
        JLabel labelPesoMedia = new JLabel("Peso Média:");
        labelPesoMedia.setBounds(180, 190, 80, 30);
        SpinnerNumberModel modelMedia = new SpinnerNumberModel(0.5, 0.0, 1.0, 0.1);
        JSpinner spinnerPesoMedia = new JSpinner(modelMedia);
        spinnerPesoMedia.setBounds(260, 190, 60, 30);
        
        // Disciplinas
        
        JSeparator separador = new JSeparator();
        separador.setBounds(20, 240, 440, 10);
        add(separador);
        
        JLabel labelDisc = new JLabel("Adicionar Disciplina (Vagas):");
        labelDisc.setFont(new Font("Arial", Font.BOLD, 14));
        labelDisc.setBounds(30, 250, 250, 30);
        
        JLabel labelNomeDisc = new JLabel("Nome:");
        labelNomeDisc.setBounds(30, 285, 50, 30);
        JTextField tfNomeDisc = new JTextField();
        tfNomeDisc.setBounds(80, 285, 200, 30);
        
        JLabel labelVagasRem = new JLabel("Remuneradas:");
        labelVagasRem.setBounds(30, 325, 100, 30);
        JSpinner spinVagasRem = new JSpinner(new SpinnerNumberModel(1, 0, 100, 1));
        spinVagasRem.setBounds(120, 325, 50, 30);
        
        JLabel labelVagasVol = new JLabel("Voluntárias:");
        labelVagasVol.setBounds(190, 325, 80, 30);
        JSpinner spinVagasVol = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        spinVagasVol.setBounds(270, 325, 50, 30);
        
        JButton btnAddDisc = new JButton("+ Adicionar");
        btnAddDisc.setBounds(340, 285, 120, 70);
        
        // Área para mostrar as disciplinas
        JTextArea areaDisciplinas = new JTextArea("Disciplinas adicionadas aparecerão aqui...");
        areaDisciplinas.setEditable(false);
        areaDisciplinas.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollDisc = new JScrollPane(areaDisciplinas);
        scrollDisc.setBounds(30, 370, 430, 100);
        
        // Botões Finais
        
        JButton btnSalvar = new JButton("Salvar Edital");
        btnSalvar.setBackground(new Color(200, 255, 200));
        btnSalvar.setBounds(100, 560, 130, 40);
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(250, 560, 130, 40);

	    // Adicionando tudo
	    add(labelDataInicio); 
	    add(tfDataInicio);
	    add(labelDataFim); 
	    add(tfDataFim);
	    
	    add(labelMaxInsc); 
	    add(spinnerMaxInsc);
	    
	    add(labelFormula);
	    add(labelPesoCRE); 
	    add(spinnerPesoCRE);
	    add(labelPesoMedia); 
	    add(spinnerPesoMedia);
	    
	    add(labelDisc); 
	    add(labelNomeDisc); 
	    add(tfNomeDisc);
	    add(labelVagasRem); 
	    add(spinVagasRem);
	    add(labelVagasVol);
	    add(spinVagasVol);
	    add(btnAddDisc); 
	    add(scrollDisc);
	    
	    add(btnSalvar); 
	    add(btnCancelar);
	    
	    setVisible(true);
	}
	
	public static void main(String[] args) {
		new TelaCadastroEdital();
	}
}