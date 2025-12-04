package telas;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import pessoas.Aluno;

public class TelaListaAlunos extends JFrame {
    
    private JTable tabelaAlunos;
    private DefaultTableModel modeloTabela;
    private JTextField tfFiltroNome;
    private JButton btnBuscar;
    private JButton btnVoltar;
    
    public TelaListaAlunos() {
        setTitle("Lista de Alunos Cadastrados");
        setSize(700, 500); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        
        // Ícone
        try {
            setIconImage(new ImageIcon("ifpblogo.png").getImage()); 
        } catch (Exception e) {}
        
        // Cabeçalho
        JLabel labelTitulo = new JLabel("Alunos Cadastrados");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        labelTitulo.setOpaque(true);
        labelTitulo.setBackground(new Color(0, 128, 0));
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitulo.setBounds(0, 0, 700, 50);
        add(labelTitulo);
        
        // Filtro
        JLabel labelFiltro = new JLabel("Filtrar por Nome:");
        labelFiltro.setBounds(30, 70, 120, 30);
        add(labelFiltro);
        
        tfFiltroNome = new JTextField();
        tfFiltroNome.setBounds(140, 70, 350, 30);
        add(tfFiltroNome);
        
        btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(500, 70, 100, 30);
        btnBuscar.setBackground(new Color(200, 255, 200));
        add(btnBuscar);
        
        // Tabela
        String[] colunas = {"Nome", "Matrícula", "E-mail"};
        
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        tabelaAlunos = new JTable(modeloTabela);
        tabelaAlunos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scroll = new JScrollPane(tabelaAlunos);
        scroll.setBounds(30, 120, 620, 280);
        add(scroll);
        
        // Botão Voltar
        btnVoltar = new JButton("Voltar");
        btnVoltar.setBounds(500, 410, 150, 40);
        add(btnVoltar);
        
        setVisible(true);
    }
    
    // Preenche a tabela com uma lista de alunos
    public void preencherTabela(List<Aluno> listaAlunos) {
        modeloTabela.setRowCount(0); // Limpa tabela
        
        if (listaAlunos == null || listaAlunos.isEmpty()) {
            return;
        }
        
        for (Aluno a : listaAlunos) {
            Object[] linha = {
                a.getNome(),
                a.getMatricula(),
                a.getEmail()
            };
            modeloTabela.addRow(linha);
        }
    }
    
    public String getTextoFiltro() {
        return tfFiltroNome.getText();
    }
    
    // Ações
    public void adicionarAcaoBuscar(ActionListener acao) {
        btnBuscar.addActionListener(acao);
    }
    
    public void adicionarAcaoVoltar(ActionListener acao) {
        btnVoltar.addActionListener(acao);
    }
}