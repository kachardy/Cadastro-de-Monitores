package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
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


    // MUDANÇA: Agora são atributos da classe para serem acessados pelo método configurarModoConsulta
    private JLabel labelDados;
    private JLabel labelCRE;
    private JLabel labelMedia;

    private List<Disciplina> listaDisciplinas;

    public TelaDetalheEditalAluno(EditalDeMonitoria edital) {
        setTitle("Inscrição - Edital " + edital.getNumeroEdital());
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);

        // Cabeçalho
        JLabel labelTitulo = new JLabel("Inscrição de Monitoria");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        labelTitulo.setOpaque(true);
        labelTitulo.setBackground(new Color(0, 100, 200));
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitulo.setBounds(0, 0, 600, 50);
        add(labelTitulo);

        // Status e Data
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = edital.getDataFim().format(df);
        JLabel labelInfo = new JLabel("Inscrições encerram em: " + dataFormatada);
        labelInfo.setBounds(30, 60, 500, 20);
        labelInfo.setFont(new Font("Arial", Font.BOLD, 14));
        add(labelInfo);

        // Tabela
        JLabel labelTab = new JLabel("Disciplinas Ofertadas:");
        labelTab.setBounds(30, 90, 300, 20);
        add(labelTab);

        String[] colunas = {"Disciplina", "Vagas Rem.", "Vagas Vol."};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tabelaDisciplinas = new JTable(modeloTabela);
        JScrollPane scroll = new JScrollPane(tabelaDisciplinas);
        scroll.setBounds(30, 120, 520, 200);
        add(scroll);

        this.listaDisciplinas = edital.getTodasAsDisciplinas();
        if (listaDisciplinas != null) {
            for (Disciplina d : listaDisciplinas) {
                Object[] linha = {d.getNome(), d.getVagasRemuneradas(), d.getVagasVoluntarias()};
                modeloTabela.addRow(linha);
            }
        }

        // --- INICIALIZAÇÃO DOS ATRIBUTOS DE DADOS ---
        labelDados = new JLabel("Dados para Candidatura:");
        labelDados.setFont(new Font("Arial", Font.BOLD, 14));
        labelDados.setBounds(30, 340, 200, 20);
        add(labelDados);

        labelCRE = new JLabel("Seu CRE:");
        labelCRE.setBounds(30, 370, 80, 30);
        tfCRE = new JTextField();
        tfCRE.setBounds(110, 370, 80, 30);

        labelMedia = new JLabel("Média na Disciplina:");
        labelMedia.setBounds(210, 370, 130, 30);
        tfMedia = new JTextField();
        tfMedia.setBounds(340, 370, 80, 30);

        add(labelCRE); add(tfCRE);
        add(labelMedia); add(tfMedia);

        btnInscrever = new JButton("Confirmar Inscrição");
        btnInscrever.setBounds(30, 430, 180, 40);
        btnInscrever.setBackground(new Color(200, 255, 200));

        btnVoltar = new JButton("Voltar");
        btnVoltar.setBounds(410, 430, 140, 40);

        add(btnInscrever);
        add(btnVoltar);
    }

    // MÉTODO DE CONFIGURAÇÃO DINÂMICA
    public void configurarModoConsulta() {
        // Esconde os componentes de entrada
        btnInscrever.setVisible(false);
        tfCRE.setVisible(false);
        tfMedia.setVisible(false);
        labelCRE.setVisible(false);
        labelMedia.setVisible(false);

        // Altera o título da seção para algo mais informativo
        labelDados.setText("Consulte as vagas disponíveis acima.");
        labelDados.setBounds(30, 340, 400, 20);

        // Ajusta o botão voltar para o centro, já que o outro sumiu
        btnVoltar.setBounds(230, 430, 140, 40);

        setTitle("Detalhes do Edital");
    }

    public Disciplina getDisciplinaSelecionada() {
        int linha = tabelaDisciplinas.getSelectedRow();
        return (linha == -1) ? null : listaDisciplinas.get(linha);
    }

    public String getCRE() { return tfCRE.getText(); }
    public String getMedia() { return tfMedia.getText(); }

    public void adicionarAcaoInscrever(ActionListener acao) { btnInscrever.addActionListener(acao); }
    public void adicionarAcaoVoltar(ActionListener acao) { btnVoltar.addActionListener(acao); }
}