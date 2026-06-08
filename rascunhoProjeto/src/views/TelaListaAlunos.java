package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import models.Aluno;
import views.builders.BotaoBuilder;
import views.factories.FabricaDeComponentes;
import views.tema.Tema;

public class TelaListaAlunos extends TelaBase {

    private JTable tabelaAlunos;
    private DefaultTableModel modeloTabela;
    private JTextField tfFiltroNome;
    private JButton btnBuscar;
    private JButton btnPerfil;
    private JButton btnVoltar;

    public TelaListaAlunos() {
        super("Lista de Alunos Cadastrados", 700, 500);
    }

    @Override
    public void inicializarComponentes() {
        setLayout(new BorderLayout());
        adicionarCabecalho("Alunos Cadastrados", 700);

        // Wrapper Central
        JPanel painelCentral = new JPanel(new BorderLayout(0, 10)); // 10px de espaçamento vertical
        painelCentral.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // 1. Painel de Filtro (Fica no topo da área central)
        JPanel painelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JLabel labelFiltro = FabricaDeComponentes.criarLabel("Filtrar por Nome:");
        tfFiltroNome = FabricaDeComponentes.criarTextField();
        tfFiltroNome.setPreferredSize(new Dimension(350, 30));

        btnBuscar = new BotaoBuilder()
                .comTexto("Buscar")
                .comCorFundo(new Color(200, 255, 200))
                .build();
        btnBuscar.setPreferredSize(new Dimension(100, 30));

        painelFiltro.add(labelFiltro);
        painelFiltro.add(tfFiltroNome);
        painelFiltro.add(btnBuscar);

        painelCentral.add(painelFiltro, BorderLayout.NORTH);

        // 2. Configuração da Tabela
        String[] colunas = {"Nome", "Matrícula", "E-mail"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaAlunos = new JTable(modeloTabela);
        tabelaAlunos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaAlunos.setFont(Tema.FONTE_PADRAO);
        tabelaAlunos.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(tabelaAlunos);
        painelCentral.add(scroll, BorderLayout.CENTER);

        add(painelCentral, BorderLayout.CENTER);

        // 3. Painel de Botões Inferior (Um na esquerda, um na direita)
        JPanel painelBotoes = new JPanel(new BorderLayout());
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        btnPerfil = new BotaoBuilder().comTexto("Visualizar perfil").build();
        btnPerfil.setPreferredSize(new Dimension(150, 40));

        btnVoltar = new BotaoBuilder().comTexto("Voltar").build();
        btnVoltar.setPreferredSize(new Dimension(150, 40));

        painelBotoes.add(btnPerfil, BorderLayout.WEST);
        painelBotoes.add(btnVoltar, BorderLayout.EAST);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    // --- Métodos Funcionais Intactos ---
    public void preencherTabela(List<Aluno> listaAlunos) {
        modeloTabela.setRowCount(0);
        if (listaAlunos == null || listaAlunos.isEmpty()) return;
        for (Aluno a : listaAlunos) {
            Object[] linha = { a.getNome(), a.getMatricula(), a.getEmail() };
            modeloTabela.addRow(linha);
        }
    }

    public String getTextoFiltro() { return tfFiltroNome.getText(); }

    public String getMatriculaAlunoSelecionado() {
        int linha = tabelaAlunos.getSelectedRow();
        if (linha == -1) return null;
        return (String) modeloTabela.getValueAt(linha, 1);
    }

    public void adicionarAcaoBuscar(ActionListener acao) { btnBuscar.addActionListener(acao); }
    public void adicionarAcaoPerfil(ActionListener acao) { btnPerfil.addActionListener(acao); }
    public void adicionarAcaoVoltar(ActionListener acao) { btnVoltar.addActionListener(acao); }


}