package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

import models.Disciplina;
import models.EditalDeMonitoria;

public class TelaCadastroEdital extends JFrame {
	
	// Atributos
	private JTextField tfNumeroEdital; 
	private JFormattedTextField tfDataInicio;
	private JFormattedTextField tfDataFim;
	private JSpinner spinnerMaxInsc;
	private JSpinner spinnerPesoCRE;
	private JSpinner spinnerPesoMedia;
	private JTextField tfNomeDisc;
	private JSpinner spinVagasRem;
	private JSpinner spinVagasVol;
	private JTextArea areaDisciplinas; 
	private JButton btnAddDisc;
	private JButton btnSalvar;
	private JButton btnCancelar;
	
	public TelaCadastroEdital(EditalDeMonitoria editalBase) {
		
		// Configurações da Janela
	    setTitle("Cadastro de Edital");
	    setSize(500, 700); 
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
	    labelTitulo.setBackground(new Color(0, 128, 0));
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
        
        JLabel labelNumero = new JLabel("Número do Edital:");
        labelNumero.setBounds(30, 70, 120, 30);
        tfNumeroEdital = new JTextField();
        tfNumeroEdital.setBounds(150, 70, 310, 30);
        
        // Datas
        JLabel labelDataInicio = new JLabel("Início Inscrições:");
        labelDataInicio.setBounds(30, 120, 120, 30); 
        tfDataInicio = new JFormattedTextField(mascaraData);
        tfDataInicio.setBounds(150, 120, 100, 30);
        
        JLabel labelDataFim = new JLabel("Fim Inscrições:");
        labelDataFim.setBounds(270, 120, 100, 30);
        tfDataFim = new JFormattedTextField(mascaraData);
        tfDataFim.setBounds(370, 120, 90, 30);
        
        // Max Inscrições
        JLabel labelMaxInsc = new JLabel("Max. Inscrições por Aluno:");
        labelMaxInsc.setBounds(30, 170, 180, 30); 
        SpinnerNumberModel modelMax = new SpinnerNumberModel(1, 1, 10, 1);
        spinnerMaxInsc = new JSpinner(modelMax);
        spinnerMaxInsc.setBounds(200, 170, 50, 30);
        
        // Fórmula (Pesos)
        JLabel labelFormula = new JLabel("--- Fórmula de Ranqueamento (Soma deve ser 1.0) ---");
        labelFormula.setFont(new Font("Arial", Font.BOLD, 12));
        labelFormula.setBounds(30, 210, 400, 20); 
        
        JLabel labelPesoCRE = new JLabel("Peso CRE:");
        labelPesoCRE.setBounds(30, 240, 80, 30); 
        
        SpinnerNumberModel modelCRE = new SpinnerNumberModel(0.5, 0.0, 1.0, 0.1);
        spinnerPesoCRE = new JSpinner(modelCRE);
        spinnerPesoCRE.setBounds(100, 240, 60, 30);
        
        JLabel labelPesoMedia = new JLabel("Peso Média:");
        labelPesoMedia.setBounds(180, 240, 80, 30);
        SpinnerNumberModel modelMedia = new SpinnerNumberModel(0.5, 0.0, 1.0, 0.1);
        spinnerPesoMedia = new JSpinner(modelMedia);
        spinnerPesoMedia.setBounds(260, 240, 60, 30);
        
        // Disciplinas
        
        JSeparator separador = new JSeparator();
        separador.setBounds(20, 290, 440, 10); 
        add(separador);
        
        JLabel labelDisc = new JLabel("Adicionar Disciplina (Vagas):");
        labelDisc.setFont(new Font("Arial", Font.BOLD, 14));
        labelDisc.setBounds(30, 300, 250, 30); 
        
        JLabel labelNomeDisc = new JLabel("Nome:");
        labelNomeDisc.setBounds(30, 335, 50, 30); 
        tfNomeDisc = new JTextField();
        tfNomeDisc.setBounds(80, 335, 200, 30);
        
        JLabel labelVagasRem = new JLabel("Remuneradas:");
        labelVagasRem.setBounds(30, 375, 100, 30); 
        spinVagasRem = new JSpinner(new SpinnerNumberModel(1, 0, 100, 1));
        spinVagasRem.setBounds(120, 375, 50, 30);
        
        JLabel labelVagasVol = new JLabel("Voluntárias:");
        labelVagasVol.setBounds(190, 375, 80, 30);
        spinVagasVol = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        spinVagasVol.setBounds(270, 375, 50, 30);
        
        btnAddDisc = new JButton("+ Adicionar");
        btnAddDisc.setBounds(340, 335, 120, 70); 
        
        // Área para mostrar as disciplinas
        areaDisciplinas = new JTextArea("Disciplinas adicionadas aparecerão aqui...");
        areaDisciplinas.setEditable(false);
        areaDisciplinas.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollDisc = new JScrollPane(areaDisciplinas);
        scrollDisc.setBounds(30, 420, 430, 100); 
        
        // Botões Finais
        
        btnSalvar = new JButton("Salvar Edital");
        btnSalvar.setBackground(new Color(200, 255, 200));
        btnSalvar.setBounds(100, 610, 130, 40); 
        
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(250, 610, 130, 40);

	    // Adicionando tudo
        add(labelNumero);
        add(tfNumeroEdital);
        
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
	    
	    // Se o editalBase não for null, preenche os campos automaticamente
	    if (editalBase != null) {
	    	preencherDados(editalBase);
	    }
	    
	    setVisible(true);
	}
		
	// Getters
	public String getNumeroEdital() { 
		return tfNumeroEdital.getText(); 
	}
	
	public String getDataInicio() { 
		return tfDataInicio.getText(); 
	}
	public String getDataFim() { 
		return tfDataFim.getText(); 
	}
	
	public int getMaxInscricoes() { 
		return (int) spinnerMaxInsc.getValue(); 
	}
	
	public double getPesoCRE() { 
		return (double) spinnerPesoCRE.getValue(); 
	}
	
	public double getPesoMedia() { 
		return (double) spinnerPesoMedia.getValue(); 
	}
	
	public String getNomeDisciplina() { 
		return tfNomeDisc.getText(); 
	}
	
	public int getVagasRem() { 
		return (int) spinVagasRem.getValue(); 
	}
	
	public int getVagasVol() { 
		return (int) spinVagasVol.getValue(); 
	}
	
	public void limparCamposDisciplina() {
		tfNomeDisc.setText("");
		spinVagasRem.setValue(1);
		spinVagasVol.setValue(0);
	}
	
	public void adicionarTextoDisciplina(String texto) {
		if (areaDisciplinas.getText().startsWith("Disciplinas")) {
			areaDisciplinas.setText("");
		}
		areaDisciplinas.append(texto + "\n");
	}
	
	public void preencherDados(EditalDeMonitoria edital) {
		if (edital == null) return;
		
		tfNumeroEdital.setText(edital.getNumeroEdital());
		
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		if (edital.getDataInicio() != null) 
			tfDataInicio.setText(edital.getDataInicio().format(fmt));
		
		if (edital.getDataFim() != null) 
			tfDataFim.setText(edital.getDataFim().format(fmt));
		
		spinnerMaxInsc.setValue(edital.getMaxInscricoesPorAluno());
		spinnerPesoCRE.setValue(edital.getPesoCRE());
		spinnerPesoMedia.setValue(edital.getPesoMedia());
		
		areaDisciplinas.setText("");
		for (Disciplina d : edital.getTodasAsDisciplinas()) {
			adicionarTextoDisciplina(" - " + d.toString());
		}
	}
	
	// Ações
	
	public void adicionarAcaoSalvar(ActionListener acao) {
		btnSalvar.addActionListener(acao);
	}
	
	public void adicionarAcaoCancelar(ActionListener acao) {
		btnCancelar.addActionListener(acao);
	}
	
	public void adicionarAcaoAddDisciplina(ActionListener acao) {
		btnAddDisc.addActionListener(acao);
	}
	
	public static void main(String[] args) {
		new TelaCadastroEdital(null);
	}
}