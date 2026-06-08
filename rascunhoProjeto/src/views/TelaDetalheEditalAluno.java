package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import models.Disciplina;
import models.EditalDeMonitoria;
import views.builders.BotaoBuilder;
import views.factories.FabricaDeComponentes;
import views.tema.Tema;

public class TelaDetalheEditalAluno extends TelaBase {

    private JTable tabelaDisciplinas;
    private DefaultTableModel modeloTabela;
    private JTextField tfCRE;
    private JTextField tfMedia;
    private JButton btnInscrever;
    private JButton btnVoltar;

    private JLabel labelDados;
    private JLabel labelCRE;
    private JLabel labelMedia;

    private List<Disciplina> listaDisciplinas;

    public TelaDetalheEditalAluno(EditalDeMonitoria edital) {
        super("Inscrição - Edital " + (edital != null ? edital.getNumeroEdital() : ""), 600, 600);

        if (edital != null) {
            listaDisciplinas = edital.getTodasAsDisciplinas();
            preencherTabela();
        }

        setVisible(true);
    }

    @Override
    public void inicializarComponentes() {
        setLayout(new BorderLayout());
        adicionarCabecalho("Inscrição de Monitoria", 600);

        JPanel painelCentral = new JPanel(new BorderLayout(0, 10));
        painelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        // 1. Tabela de Disciplinas (Centro)
        String[] colunas = {"Disciplina", "Vagas Remuneradas", "Vagas Voluntárias"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tabelaDisciplinas = new JTable(modeloTabela);
        tabelaDisciplinas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaDisciplinas.setFont(Tema.FONTE_PADRAO);
        tabelaDisciplinas.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(tabelaDisciplinas);
        painelCentral.add(scroll, BorderLayout.CENTER);

        // 2. Formulário de Inscrição (Sul do Painel Central)
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        labelDados = FabricaDeComponentes.criarLabel("Preencha suas notas para ranqueamento:");
        labelDados.setFont(Tema.FONTE_DESTAQUE);

        labelCRE = FabricaDeComponentes.criarLabel("Seu CRE:");
        tfCRE = FabricaDeComponentes.criarTextField();
        tfCRE.setPreferredSize(new Dimension(80, 30));

        labelMedia = FabricaDeComponentes.criarLabel("Média na Disciplina:");
        tfMedia = FabricaDeComponentes.criarTextField();
        tfMedia.setPreferredSize(new Dimension(80, 30));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        painelFormulario.add(labelDados, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; painelFormulario.add(labelCRE, gbc);
        gbc.gridx = 1; gbc.gridy = 1; painelFormulario.add(tfCRE, gbc);
        gbc.gridx = 2; gbc.gridy = 1; painelFormulario.add(labelMedia, gbc);
        gbc.gridx = 3; gbc.gridy = 1; painelFormulario.add(tfMedia, gbc);

        painelCentral.add(painelFormulario, BorderLayout.SOUTH);
        add(painelCentral, BorderLayout.CENTER);

        // 3. Botões Inferiores (Rodapé da Janela)
        btnInscrever = new BotaoBuilder()
                .comTexto("Confirmar Inscrição")
                .comCorFundo(new Color(200, 255, 200))
                .build();
        btnInscrever.setPreferredSize(new Dimension(180, 40));

        btnVoltar = new BotaoBuilder().comTexto("Voltar").build();
        btnVoltar.setPreferredSize(new Dimension(140, 40));

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        painelBotoes.add(btnInscrever);
        painelBotoes.add(btnVoltar);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void preencherTabela() {
        modeloTabela.setRowCount(0);
        if (listaDisciplinas != null) {
            for (Disciplina d : listaDisciplinas) {
                modeloTabela.addRow(new Object[]{ d.getNome(), d.getVagasRemuneradas(), d.getVagasVoluntarias() });
            }
        }
    }

    public void configurarModoConsulta() {
        btnInscrever.setVisible(false);
        tfCRE.setVisible(false);
        tfMedia.setVisible(false);
        labelCRE.setVisible(false);
        labelMedia.setVisible(false);

        labelDados.setText("Consulte as vagas disponíveis acima.");
        setTitle("Detalhes do Edital");
    }

    public Disciplina getDisciplinaSelecionada() {
        int linha = tabelaDisciplinas.getSelectedRow();
        return (linha == -1) ? null : listaDisciplinas.get(linha);
    }

    public double getCRE() {
        try { return Double.parseDouble(tfCRE.getText().replace(",", ".")); } catch (Exception e) { return -1; }
    }

    public double getMedia() {
        try { return Double.parseDouble(tfMedia.getText().replace(",", ".")); } catch (Exception e) { return -1; }
    }

    public void adicionarAcaoInscrever(ActionListener acao) { btnInscrever.addActionListener(acao); }
    public void adicionarAcaoVoltar(ActionListener acao) { btnVoltar.addActionListener(acao); }
}