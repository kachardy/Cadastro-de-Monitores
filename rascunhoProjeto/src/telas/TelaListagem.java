package telas;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import outros.EditalDeMonitoria;

public class TelaListagem extends JFrame {
	
	private JTable tabelaEditais;
	private DefaultTableModel modeloTabela;
	private JButton botaoDetalhar;
	private JButton botaoVoltar;
	
	public TelaListagem() {
		// Configurações da Janela
		setTitle("Lista de Editais");
	    setSize(700, 500); 
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setResizable(false);
	    setLocationRelativeTo(null);
	    setLayout(null);
	    
	    try {
	        ImageIcon imagemIcone = new ImageIcon("ifpblogo.png");
	        setIconImage(imagemIcone.getImage()); 
	    } catch (Exception e) {}
	    
	    // Cabeçalho
	    JLabel labelTitulo = new JLabel("Editais Publicados");
	    labelTitulo.setFont(new Font("Arial", Font.BOLD, 26));
	    labelTitulo.setOpaque(true);
	    labelTitulo.setBackground(new Color(0, 128, 0));
	    labelTitulo.setForeground(Color.WHITE);
	    labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
	    labelTitulo.setBounds(0, 0, 700, 50);
	    add(labelTitulo);
	    
	    String[] colunas = {"ID", "Início", "Fim", "Status"};
	    
	    modeloTabela = new DefaultTableModel(colunas, 0) {
	    	@Override
	    	public boolean isCellEditable(int row, int column) {
	    		return false; 
	    	}
	    };
	    
	    tabelaEditais = new JTable(modeloTabela);
	    tabelaEditais.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    
	    tabelaEditais.getColumnModel().getColumn(0).setPreferredWidth(50); 
	    
	    JScrollPane scroll = new JScrollPane(tabelaEditais);
	    scroll.setBounds(30, 80, 620, 300); 
	    add(scroll);
	    
	    // Botões
	    botaoDetalhar = new JButton("Ver Detalhes");
	    botaoDetalhar.setBounds(30, 400, 150, 40);
	    botaoDetalhar.setBackground(new Color(200, 255, 200));
	    add(botaoDetalhar);
	    
	    botaoVoltar = new JButton("Voltar");
	    botaoVoltar.setBounds(500, 400, 150, 40);
	    add(botaoVoltar);
	    
	    setVisible(true);
	}
	
	public void preencherTabela(List<EditalDeMonitoria> listaEditais) {
		modeloTabela.setRowCount(0);
		
		if (listaEditais == null || listaEditais.isEmpty()) {
			return;
		}
		
		DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		for (EditalDeMonitoria edital : listaEditais) {
			
			String status = "Aberto";
			if (edital.jaAcabou()) {
				status = "Encerrado";
			}
			
			Object[] linha = {
				edital.getId(), 
				edital.getDataInicio().format(formatador),
				edital.getDataFim().format(formatador),
				status
			};
			
			modeloTabela.addRow(linha);
		}
	}
	
	// Retorna o ID (long) da linha selecionada (Coluna 0)
	public Long getIdEditalSelecionado() {
		int linhaSelecionada = tabelaEditais.getSelectedRow();
		
		if (linhaSelecionada == -1) {
			return null; 
		}
		
		return (Long) modeloTabela.getValueAt(linhaSelecionada, 0);
	}
	
	// Ações
	public void adicionarAcaoDetalhar(ActionListener acao) {
		botaoDetalhar.addActionListener(acao);
	}
	
	public void adicionarAcaoVoltar(ActionListener acao) {
		botaoVoltar.addActionListener(acao);
	}
}