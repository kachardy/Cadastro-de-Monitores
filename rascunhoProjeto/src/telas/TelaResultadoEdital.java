package telas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import outros.Disciplina;
import pessoas.Aluno;
import outros.EditalDeMonitoria;

public class TelaResultadoEdital extends JFrame {

    private JTable tabela;
    private DefaultTableModel modelo;
    private JButton btnGerarPdf;
    private JButton btnFechar;

    public TelaResultadoEdital(EditalDeMonitoria edital) {

        setTitle("Resultado do Edital " + edital.getId());
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        
        // Ícone da Janela
        try {
            ImageIcon imagemIcone = new ImageIcon("ifpblogo.png");
            setIconImage(imagemIcone.getImage()); 
        } catch (Exception e) {}

        JLabel labelTitulo = new JLabel("Ranking das Disciplinas");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        labelTitulo.setOpaque(true);
        labelTitulo.setBackground(new Color(0, 128, 0)); // Verde
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitulo.setBounds(0, 0, 700, 50);
        add(labelTitulo);
 
        JLabel labelSubtitulo = new JLabel("Edital: " + edital.getNumeroEdital() + " (Classificação Final)");
        labelSubtitulo.setFont(new Font("Arial", Font.BOLD, 14));
        labelSubtitulo.setBounds(30, 60, 400, 20);
        add(labelSubtitulo);

        // Tabela
        String[] colunas = {"Disciplina", "Posição", "Aluno", "Pontuação"};
        modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBounds(30, 90, 620, 400); // Ajustei margens
        add(scroll);

        // Preenche os dados
        preencherTabela(edital);
        
        btnGerarPdf = new JButton("Gerar PDF");
        btnGerarPdf.setBounds(220, 510, 120, 35);

        btnFechar = new JButton("Fechar");
        btnFechar.setBounds(360, 510, 120, 35);
      
        add(btnFechar);

        setVisible(true);
    }

    private void preencherTabela(EditalDeMonitoria edital) {
    	double pesoCRE = edital.getPesoCRE();
        double pesoMedia = edital.getPesoMedia();

        // Percorre todas as disciplinas do edital
        for (Disciplina d : edital.getTodasAsDisciplinas()) {
            
            // Pega as listas paralelas
            ArrayList<Aluno> alunos = d.getAlunosInscritos();
            ArrayList<Double> cres = d.getListaCREs();
            ArrayList<Double> medias = d.getListaMedias();
            
            // Percorre os alunos daquela disciplina
            for (int i = 0; i < alunos.size(); i++) {

                // Proteção contra índice fora
                double cre = (i < cres.size()) ? cres.get(i) : 0.0;
                double media = (i < medias.size()) ? medias.get(i) : 0.0;
                
                double pontuacao = (cre * pesoCRE) + (media * pesoMedia);
                
                // Formata pontuação para 2 casas decimais
                String pontuacaoStr = String.format("%.2f", pontuacao);

                modelo.addRow(new Object[]{
                        d.getNome(),
                        (i + 1) + "º",
                        alunos.get(i).getNome(),
                        pontuacaoStr
                });
            }
        }
    }
    
    public void adicionarAcaoGerarPdf(ActionListener acao) {
		btnGerarPdf.addActionListener(acao);
	}
    
    public void adicionarAcaoFechar(ActionListener acao) {
		btnFechar.addActionListener(acao);
	}
}