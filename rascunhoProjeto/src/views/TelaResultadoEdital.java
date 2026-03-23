package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import models.Aluno;
import models.Disciplina;
import models.EditalDeMonitoria;
import models.Inscricao; // NOVA ALTERAÇÃO: Importação necessária para a nova estrutura de dados

public class TelaResultadoEdital extends JFrame {

    private JButton btnGerarPdf;
    private JButton btnFechar;
    private JTable tabelaResultado;
    private DefaultTableModel modeloTabela;

    public TelaResultadoEdital(EditalDeMonitoria edital) {
        setTitle("Resultado Final - Edital " + edital.getNumeroEdital());
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);

        // Cabeçalho
        JLabel labelTitulo = new JLabel("Resultado Final de Monitoria");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        labelTitulo.setOpaque(true);
        labelTitulo.setBackground(new Color(70, 130, 180));
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitulo.setBounds(0, 0, 750, 50);
        add(labelTitulo);

        JLabel labelEdital = new JLabel("Edital: " + edital.getNumeroEdital());
        labelEdital.setFont(new Font("Arial", Font.ITALIC, 14));
        labelEdital.setBounds(30, 60, 300, 20);
        add(labelEdital);

        // Tabela de Resultados
        String[] colunas = {"Disciplina", "Aluno", "Matrícula", "Nota Final", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tabelaResultado = new JTable(modeloTabela);
        JScrollPane scroll = new JScrollPane(tabelaResultado);
        scroll.setBounds(30, 100, 680, 300);
        add(scroll);

        // Preenchimento da tabela com a lógica de Inscrição
        preencherTabela(edital);

        // Botões
        btnGerarPdf = new JButton("Gerar Relatório PDF");
        btnGerarPdf.setBounds(30, 430, 180, 40);
        btnGerarPdf.setBackground(new Color(200, 230, 200));

        btnFechar = new JButton("Fechar");
        btnFechar.setBounds(590, 430, 120, 40);

        add(btnGerarPdf);
        add(btnFechar);
    }

    private void preencherTabela(EditalDeMonitoria edital) {
        // Limpando a tabela para evitar duplicatas
        modeloTabela.setRowCount(0);

        for (Disciplina d : edital.getTodasAsDisciplinas()) {

            ArrayList<Inscricao> inscricoes = edital.getGerenciador().getInscricoesPorDisciplina(d);

            for (int i = 0; i < inscricoes.size(); i++) {
                Inscricao insc = inscricoes.get(i);
                Aluno a = insc.getCandidato();

                // Cálculo da nota final
                double notaFinal = (insc.getCre() * edital.getPesoCRE()) + (insc.getMedia() * edital.getPesoMedia());

                String status = "Classificado";
                if (i >= d.getTotalVagas()) {
                    status = "Lista de Espera";
                }

                Object[] linha = {
                        d.getNome(),
                        a.getNome(),
                        a.getMatricula(),
                        String.format("%.2f", notaFinal),
                        status
                };
                modeloTabela.addRow(linha);
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