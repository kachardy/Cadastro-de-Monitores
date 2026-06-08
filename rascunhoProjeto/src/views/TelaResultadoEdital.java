package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import models.Aluno;
import models.Disciplina;
import models.EditalDeMonitoria;
import models.Inscricao;
import views.builders.BotaoBuilder;
import views.tema.Tema;

public class TelaResultadoEdital extends TelaBase {

    private JButton btnGerarPdf;
    private JButton btnFechar;
    private JTable tabelaResultado;
    private DefaultTableModel modeloTabela;

    public TelaResultadoEdital(EditalDeMonitoria edital) {
        super("Resultado Final - Edital " + (edital != null ? edital.getNumeroEdital() : ""), 750, 550);

        // MAGIA: Como esta é uma tela secundária, sobrescrevemos o comportamento de fecho
        // da TelaBase para que não encerre o programa todo ao clicar no "X"
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        if (edital != null) {
            preencherTabela(edital);
        }

        setVisible(true);
    }

    @Override
    protected void inicializarComponentes() {
        setLayout(new BorderLayout());

        // 1. Cabeçalho Padronizado
        adicionarCabecalho("Resultado Final de Monitoria", 750);

        // 2. Painel Central (Tabela de Resultados)
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20)); // Margens

        String[] colunas = {"Disciplina", "Aluno", "Matrícula", "Nota Final", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaResultado = new JTable(modeloTabela);
        tabelaResultado.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaResultado.setFont(Tema.FONTE_PADRAO);
        tabelaResultado.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(tabelaResultado);
        painelCentral.add(scroll, BorderLayout.CENTER);

        add(painelCentral, BorderLayout.CENTER);

        // 3. Painel de Botões (Rodapé)
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));

        // Mantemos o padrão do Builder com o prefixo "com" para as propriedades
        btnGerarPdf = new BotaoBuilder()
                .comTexto("Gerar PDF")
                .comCorFundo(new Color(255, 200, 200)) // Cor de destaque suave
                .build();
        btnGerarPdf.setPreferredSize(new Dimension(150, 40));

        btnFechar = new BotaoBuilder()
                .comTexto("Fechar")
                .build();
        btnFechar.setPreferredSize(new Dimension(150, 40));

        painelBotoes.add(btnGerarPdf);
        painelBotoes.add(btnFechar);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    // A lógica de negócio permaneceu 100% intacta para não afetar os cálculos e controllers
    public void preencherTabela(EditalDeMonitoria edital) {
        modeloTabela.setRowCount(0);
        if (edital == null) return;

        for (Disciplina d : edital.getTodasAsDisciplinas()) {

            List<Inscricao> inscricoes = edital.getGerenciador().getInscricoesPorDisciplina(edital.getInscricoesRealizadas(), d);

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

    public void adicionarAcaoGerarPdf(ActionListener acao) { btnGerarPdf.addActionListener(acao); }
    public void adicionarAcaoFechar(ActionListener acao) { btnFechar.addActionListener(acao); }
}