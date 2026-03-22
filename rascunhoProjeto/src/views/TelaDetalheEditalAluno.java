package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import models.Disciplina;
import models.EditalDeMonitoria;

public class TelaDetalheEditalAluno extends JFrame {

    private JTable tabelaDisciplinas;
    private DefaultTableModel modeloTabela;
    private JTextField tfCRE;
    private JTextField tfMedia;
    private JButton btnInscrever;
    private JButton btnVoltar;

    private List<Disciplina> listaDisciplinas;

    public TelaDetalheEditalAluno(EditalDeMonitoria edital) {
        setTitle("Inscrição - Edital " + edital.getNumeroEdital());
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);

        try {
            setIconImage(new ImageIcon("ifpblogo.png").getImage());
        } catch (Exception e) {}

        JLabel labelTitulo = new JLabel("Inscrição");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        labelTitulo.setOpaque(true);
        labelTitulo.setBackground(new Color(0, 100, 200));
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitulo.setBounds(0, 0, 600, 50);
        add(labelTitulo);

        String status = edital.isResultadoCalculado() ? "RESULTADO DISPONÍVEL" : "Inscrições Abertas";
        JLabel labelInfo = new JLabel(status + " | Encerra em: " + edital.getDataFim());
        labelInfo.setBounds(30, 60, 500, 20);
        labelInfo.setFont(new Font("Arial", Font.BOLD, 14));
        add(labelInfo);

        JLabel labelTab = new JLabel("Selecione uma Disciplina:");
        labelTab.setBounds(30, 90, 200, 20);
        add(labelTab);

        String[] colunas = {"Disciplina", "Vagas Rem.", "Vagas Vol."};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tabelaDisciplinas = new JTable(modeloTabela);
        tabelaDisciplinas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tabelaDisciplinas);
        scroll.setBounds(30, 120, 520, 200);
        add(scroll);

        // Preenchimento da tabela com Disciplinas
        this.listaDisciplinas = edital.getTodasAsDisciplinas();
        if (listaDisciplinas != null) {
            for (Disciplina d : listaDisciplinas) {
                Object[] linha = {d.getNome(), d.getVagasRemuneradas(), d.getVagasVoluntarias()};
                modeloTabela.addRow(linha);
            }
        }

        JLabel labelDados = new JLabel("Seus Dados Acadêmicos:");
        labelDados.setFont(new Font("Arial", Font.BOLD, 14));
        labelDados.setBounds(30, 340, 200, 20);
        add(labelDados);

        JLabel labelCRE = new JLabel("Seu CRE Geral:");
        labelCRE.setBounds(30, 370, 100, 30);
        tfCRE = new JTextField();
        tfCRE.setBounds(130, 370, 80, 30);

        JLabel labelMedia = new JLabel("Média na Disciplina:");
        labelMedia.setBounds(230, 370, 130, 30);
        tfMedia = new JTextField();
        tfMedia.setBounds(360, 370, 80, 30);

        add(labelCRE); add(tfCRE);
        add(labelMedia); add(tfMedia);

        btnInscrever = new JButton("Inscrever-se");
        btnInscrever.setBounds(30, 430, 150, 40);
        btnInscrever.setBackground(new Color(200, 255, 200));

        btnVoltar = new JButton("Voltar");
        btnVoltar.setBounds(220, 490, 140, 30);

        add(btnInscrever);
        add(btnVoltar);

        if (edital.jaAcabou() || edital.isResultadoCalculado()) {
            btnInscrever.setEnabled(false);
            tfCRE.setEditable(false);
            tfMedia.setEditable(false);
        }
    }

    // MÉTODOS QUE ESTAVAM FALTANDO (CORREÇÃO DO ERRO EM VERMELHO):

    public Disciplina getDisciplinaSelecionada() {
        int linha = tabelaDisciplinas.getSelectedRow();
        if (linha == -1) return null;
        return listaDisciplinas.get(linha);
    }

    public String getCRE() {
        return tfCRE.getText();
    }

    public String getMedia() {
        return tfMedia.getText();
    }

    public void adicionarAcaoInscrever(ActionListener acao) {
        btnInscrever.addActionListener(acao);
    }

    public void adicionarAcaoVoltar(ActionListener acao) {
        btnVoltar.addActionListener(acao);
    }
}