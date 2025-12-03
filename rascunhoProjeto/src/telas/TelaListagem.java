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
		
		setTitle("Lista de Editais");
	    setSize(600, 500); 
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setResizable(false);
	    setLocationRelativeTo(null);
	    setLayout(null);
	    
	    // Ícone da Janela
	    try {
	        ImageIcon imagemIcone = new ImageIcon("ifpblogo.png");
	        setIconImage(imagemIcone.getImage()); 
	    } catch (Exception e) {}
	    
	    JLabel labelTitulo = new JLabel("Editais Publicados");
	    labelTitulo.setFont(new Font("Arial", Font.BOLD, 26));
	    labelTitulo.setOpaque(true);
	    labelTitulo.setBackground(new Color(0, 128, 0));
	    labelTitulo.setForeground(Color.WHITE);
	    labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
	    labelTitulo.setBounds(0, 0, 600, 50);
	    add(labelTitulo);
	    
	    // Tabela
	    // Definição das Colunas
	    String[] colunas = {"Número", "Início", "Fim", "Status"};
	    
	    // Criação do Modelo (Bloqueando edição de celulas)
	    modeloTabela = new DefaultTableModel(colunas, 0) {
	    	public boolean isCellEditable(int row, int column) {
	    		return false; // Impede que o usuário edite a tabela manualmente
	    	}
	    };
	    
	    // Criação da Tabela visual
	    tabelaEditais = new JTable(modeloTabela);
	    tabelaEditais.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Só pode selecionar 1 por vez
	    
	    // ScrollPane 
	    JScrollPane scroll = new JScrollPane(tabelaEditais);
	    scroll.setBounds(30, 80, 520, 300); 
	    add(scroll);
	    
	    // Botões
	    
	    botaoDetalhar = new JButton("Ver Detalhes");
	    botaoDetalhar.setBounds(30, 400, 150, 40);
	    botaoDetalhar.setBackground(new Color(200, 255, 200));
	    add(botaoDetalhar);
	    
	    botaoVoltar = new JButton("Voltar");
	    botaoVoltar.setBounds(400, 400, 150, 40);
	    add(botaoVoltar);
	    
	    setVisible(true);
	}
	
	
	// Recebe a lista de editais da Central e preenche a tabela.
	public void preencherTabela(List<EditalDeMonitoria> listaEditais) {
		// Limpa a tabela antes de adicionar (para não duplicar)
		modeloTabela.setRowCount(0);
		
		if (listaEditais == null || listaEditais.isEmpty()) {
			return;
		}
		
		DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		for (EditalDeMonitoria edital : listaEditais) {
			
			// Define o status
			String status = "Aberto";
			if (edital.jaAcabou()) {
				status = "Encerrado";
			}
			
			// Cria a linha
			Object[] linha = {
				edital.getNumeroEdital(),
				edital.getDataInicio().format(formatador),
				edital.getDataFim().format(formatador),
				status
			};
			
			// Adiciona no modelo
			modeloTabela.addRow(linha);
		}
	}
	
	
	 //Retorna o número do edital que o usuário selecionou na tabela.
	 
	public String getNumeroEditalSelecionado() {
		int linhaSelecionada = tabelaEditais.getSelectedRow();
		
		if (linhaSelecionada == -1) {
			return null; // Nenhuma linha selecionada
		}
		
		// Pega o valor da coluna 0 (Número) da linha selecionada
		return (String) modeloTabela.getValueAt(linhaSelecionada, 0);
	}
	
	// Métodos de ação
	
	public void adicionarAcaoDetalhar(ActionListener acao) {
		botaoDetalhar.addActionListener(acao);
	}
	
	public void adicionarAcaoVoltar(ActionListener acao) {
		botaoVoltar.addActionListener(acao);
	}
	
	public static void main(String[] args) {
		new TelaListagem();
	}
	
}