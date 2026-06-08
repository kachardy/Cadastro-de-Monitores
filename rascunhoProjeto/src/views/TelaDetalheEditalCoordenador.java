package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import models.Aluno;
import models.Disciplina;
import models.EditalDeMonitoria;
import models.Inscricao;
import models.estados.FaseDoEdital;
import views.builders.BotaoBuilder;
import views.factories.FabricaDeComponentes;
import views.tema.Tema;

public class TelaDetalheEditalCoordenador extends TelaBase {

    private JButton btnEditar;
    private JButton btnEncerrar;
    private JButton btnClonar;
    private JButton btnCalcularResultado;
    private JButton btnVerPerfil;
    private JButton btnEnviarEmail;
    private JButton btnVoltar;

    private JTable tabelaInscritos;
    private DefaultTableModel modeloTabela;
    private JLabel labelStatus;
    private JLabel labelInfo;

    public TelaDetalheEditalCoordenador(EditalDeMonitoria edital) {
        super("Detalhes do Edital " + (edital != null ? edital.getNumeroEdital() : ""), 700, 600);

        if (edital != null) {
            // Preencher informações básicas com as regras do seu modelo
            labelInfo.setText("Prazo: " + edital.getDataInicio() + " a " + edital.getDataFim());

            String textoStatus = edital.jaAcabou() ? "Encerrado" : "Aberto";
            labelStatus.setText("Status: " + textoStatus);
            if (edital.jaAcabou()) {
                labelStatus.setForeground(Color.RED);
            } else {
                labelStatus.setForeground(new Color(0, 100, 200));
            }

            // Regras de negócio de ativação dos botões baseadas no seu código original
            if (edital.getFase() == FaseDoEdital.CALCULADO) {
                btnEditar.setEnabled(false);
                btnEncerrar.setEnabled(false);
                btnCalcularResultado.setText("Ver Resultado Final");
            } else if (!edital.jaAcabou()) {
                btnCalcularResultado.setEnabled(false);
                btnCalcularResultado.setToolTipText("Aguarde o fim das inscrições");
            }

            preencherTabela(edital);
        }

        setVisible(true);
    }

    @Override
    public void inicializarComponentes() {
        setLayout(new BorderLayout());
        adicionarCabecalho("Gerenciamento de Edital", 700);

        JPanel painelCentral = new JPanel(new BorderLayout(0, 10));
        painelCentral.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // 1. Informações Superiores (Norte do Painel Central)
        JPanel painelInfo = new JPanel(new GridLayout(2, 1));
        labelInfo = FabricaDeComponentes.criarLabel("Prazo: ");
        labelStatus = FabricaDeComponentes.criarLabel("Status: ");
        labelStatus.setFont(Tema.FONTE_DESTAQUE);
        painelInfo.add(labelInfo);
        painelInfo.add(labelStatus);
        painelCentral.add(painelInfo, BorderLayout.NORTH);

        // 2. Tabela de Inscritos (Centro)
        String[] colunas = {"Disciplina", "Aluno", "Matrícula", "CRE", "Média Disc."};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tabelaInscritos = new JTable(modeloTabela);
        tabelaInscritos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaInscritos.setFont(Tema.FONTE_PADRAO);
        tabelaInscritos.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(tabelaInscritos);
        painelCentral.add(scroll, BorderLayout.CENTER);

        add(painelCentral, BorderLayout.CENTER);

        // 3. Painel de Botões (Sul da Janela - 2 Linhas acomodando os 7 botões)
        JPanel painelBotoesPrincipal = new JPanel(new GridLayout(2, 1, 0, 10));
        painelBotoesPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JPanel linha1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JPanel linha2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        btnEditar = new BotaoBuilder().comTexto("Editar").comCorFundo(new Color(255, 255, 200)).build();
        btnEditar.setPreferredSize(new Dimension(100, 35));

        btnEncerrar = new BotaoBuilder().comTexto("Encerrar").comCorFundo(new Color(255, 200, 200)).build();
        btnEncerrar.setPreferredSize(new Dimension(100, 35));

        btnClonar = new BotaoBuilder().comTexto("Clonar").comCorFundo(new Color(200, 200, 255)).build();
        btnClonar.setPreferredSize(new Dimension(100, 35));

        btnCalcularResultado = new BotaoBuilder().comTexto("Calcular Resultado").comCorFundo(new Color(200, 255, 200)).build();
        btnCalcularResultado.setPreferredSize(new Dimension(160, 35));

        btnVerPerfil = new BotaoBuilder().comTexto("Ver Perfil do Aluno").build();
        btnVerPerfil.setPreferredSize(new Dimension(160, 35));

        // Novo botão restaurado
        btnEnviarEmail = new BotaoBuilder().comTexto("Enviar Email").build();
        btnEnviarEmail.setPreferredSize(new Dimension(140, 35));

        btnVoltar = new BotaoBuilder().comTexto("Voltar").build();
        btnVoltar.setPreferredSize(new Dimension(100, 35));

        // Montagem das Linhas
        linha1.add(btnEditar);
        linha1.add(btnEncerrar);
        linha1.add(btnClonar);
        linha1.add(btnCalcularResultado);

        linha2.add(btnVerPerfil);
        linha2.add(btnEnviarEmail);
        linha2.add(btnVoltar);

        painelBotoesPrincipal.add(linha1);
        painelBotoesPrincipal.add(linha2);

        add(painelBotoesPrincipal, BorderLayout.SOUTH);
    }

    public void preencherTabela(EditalDeMonitoria edital) {
        modeloTabela.setRowCount(0);
        if (edital == null) return;

        for (Disciplina d : edital.getTodasAsDisciplinas()) {
            // Resgatando as inscrições através do gerenciador (Código exato do seu modelo original)
            List<Inscricao> inscricoes = edital.getGerenciador().getInscricoesPorDisciplina(edital.getInscricoesRealizadas(), d);

            for (Inscricao inscricao : inscricoes) {
                // Buscando o candidato da forma correta
                Aluno a = inscricao.getCandidato();

                Object[] linha = { d.getNome(), a.getNome(), a.getMatricula(), inscricao.getCre(), inscricao.getMedia() };
                modeloTabela.addRow(linha);
            }
        }
    }

    public String getMatriculaAlunoSelecionado() {
        int linha = tabelaInscritos.getSelectedRow();
        if (linha == -1) return null;
        return (String) modeloTabela.getValueAt(linha, 2);
    }

    // Todos os listeners intactos!
    public void adicionarAcaoEditar(ActionListener acao) { btnEditar.addActionListener(acao); }
    public void adicionarAcaoEncerrar(ActionListener acao) { btnEncerrar.addActionListener(acao); }
    public void adicionarAcaoClonar(ActionListener acao) { btnClonar.addActionListener(acao); }
    public void adicionarAcaoCalcular(ActionListener acao) { btnCalcularResultado.addActionListener(acao); }
    public void adicionarAcaoVerPerfil(ActionListener acao) { btnVerPerfil.addActionListener(acao); }
    public void adicionarAcaoEnviarEmail(ActionListener acao) { btnEnviarEmail.addActionListener(acao); }
    public void adicionarAcaoVoltar(ActionListener acao) { btnVoltar.addActionListener(acao); }
}