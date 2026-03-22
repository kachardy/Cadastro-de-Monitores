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
import models.Inscricao; // NOVA ALTERAÇÃO: Importação da classe Inscricao

public class TelaDetalheEditalCoordenador extends JFrame {

    // Botões de Ação do Edital
    private JButton btnEditar;
    private JButton btnEncerrar;
    private JButton btnClonar;
    private JButton btnCalcularResultado;
    private JButton btnVoltar;
    private JButton btnVerPerfil;
    private JButton btnEnviarEmail;

    private JTable tabelaInscritos;
    private DefaultTableModel modeloTabela;
    private JLabel labelStatus;

    public TelaDetalheEditalCoordenador(EditalDeMonitoria edital) {
        // ... (O construtor e a interface gráfica permanecem idênticos)
        setTitle("Detalhes do Edital " + edital.getNumeroEdital());
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);

        try {
            setIconImage(new ImageIcon("ifpblogo.png").getImage());
        } catch (Exception e) {
            // Ignora erro de ícone
        }

        // Cabeçalho
        JLabel labelTitulo = new JLabel("Detalhes do Edital " + edital.getNumeroEdital());
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        labelTitulo.setOpaque(true);
        labelTitulo.setBackground(new Color(0, 128, 0));
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitulo.setBounds(0, 0, 700, 50);
        add(labelTitulo);

        // Informações Básicas
        JLabel labelInfo = new JLabel("Prazo: " + edital.getDataInicio() + " a " + edital.getDataFim());
        labelInfo.setBounds(30, 60, 400, 20);
        add(labelInfo);

        String textoStatus = edital.jaAcabou() ? "Encerrado" : "Aberto";
        labelStatus = new JLabel("Status: " + textoStatus);
        labelStatus.setBounds(30, 85, 200, 20);
        if (edital.jaAcabou()) {
            labelStatus.setForeground(Color.RED);
        }
        add(labelStatus);

        // Tabela de Inscritos
        JLabel labelTab = new JLabel("Alunos Inscritos:");
        labelTab.setBounds(30, 120, 200, 20);
        add(labelTab);

        String[] colunas = {"Disciplina", "Aluno", "Matrícula", "CRE", "Média Disc."};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tabelaInscritos = new JTable(modeloTabela);
        tabelaInscritos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Só seleciona um
        JScrollPane scroll = new JScrollPane(tabelaInscritos);
        scroll.setBounds(30, 150, 630, 250);
        add(scroll);

        // Inicialização dos botões
        btnVerPerfil = new JButton("Ver Perfil do Aluno");
        btnVerPerfil.setBounds(30, 410, 160, 30);
        btnVerPerfil.setFont(new Font("Arial", Font.PLAIN, 12));

        btnEnviarEmail = new JButton("Enviar Email");
        btnEnviarEmail.setBounds(200, 410, 140, 30);
        btnEnviarEmail.setFont(new Font("Arial", Font.PLAIN, 12));

        btnEditar = new JButton("Editar");
        btnEditar.setBounds(30, 460, 100, 35);

        btnEncerrar = new JButton("Encerrar");
        btnEncerrar.setBounds(140, 460, 100, 35);
        btnEncerrar.setBackground(new Color(255, 200, 200));

        btnClonar = new JButton("Clonar");
        btnClonar.setBounds(250, 460, 100, 35);

        btnCalcularResultado = new JButton("Calcular Resultado");
        btnCalcularResultado.setBounds(360, 460, 160, 35);
        btnCalcularResultado.setBackground(new Color(200, 255, 200));

        btnVoltar = new JButton("Voltar");
        btnVoltar.setBounds(540, 460, 120, 35);

        // Regra de Visualização
        if (edital.isResultadoCalculado()) {
            btnEditar.setEnabled(false);
            btnEncerrar.setEnabled(false);
            btnCalcularResultado.setText("Ver Resultado Final");
        } else if (edital.jaAcabou() == false) {
            btnCalcularResultado.setEnabled(false);
            btnCalcularResultado.setToolTipText("Aguarde o fim das inscrições");
        }

        add(btnEditar);
        add(btnEncerrar);
        add(btnClonar);
        add(btnCalcularResultado);
        add(btnVoltar);
        add(btnVerPerfil);
        add(btnEnviarEmail);

        // Chamada do método atualizado
        preencherTabela(edital);

        setVisible(true);
    }

    private void preencherTabela(EditalDeMonitoria edital) {
        for (Disciplina d : edital.getTodasAsDisciplinas()) {

            // NOVA ALTERAÇÃO: Busca apenas a lista única de inscrições da disciplina
            ArrayList<Inscricao> inscricoes = d.getInscricoes();

            // NOVA ALTERAÇÃO: Itera diretamente sobre os objetos Inscricao
            for (Inscricao inscricao : inscricoes) {
                // Extrai o aluno de dentro da inscrição
                Aluno a = inscricao.getCandidato();

                // NOVA ALTERAÇÃO: Monta a linha da tabela extraindo os dados do objeto unificado
                // Eliminada a necessidade de proteção contra falha de sincronia (índices fora de alcance)
                Object[] linha = {
                        d.getNome(),
                        a.getNome(),
                        a.getMatricula(),
                        inscricao.getCre(),
                        inscricao.getMedia()
                };
                modeloTabela.addRow(linha);
            }
        }
    }

    // Método auxiliar para saber qual aluno foi selecionado na tabela
    public String getMatriculaAlunoSelecionado() {
        int linha = tabelaInscritos.getSelectedRow();
        if (linha == -1) return null;
        // A matrícula está na coluna 2 (índice 2)
        return (String) modeloTabela.getValueAt(linha, 2);
    }

    public void adicionarAcaoEditar(ActionListener acao) {
        btnEditar.addActionListener(acao);
    }

    public void adicionarAcaoEncerrar(ActionListener acao) {
        btnEncerrar.addActionListener(acao);
    }

    public void adicionarAcaoClonar(ActionListener acao) {
        btnClonar.addActionListener(acao);
    }

    public void adicionarAcaoCalcular(ActionListener acao) {
        btnCalcularResultado.addActionListener(acao);
    }

    public void adicionarAcaoVoltar(ActionListener acao) {
        btnVoltar.addActionListener(acao);
    }

    // Novos Listeners
    public void adicionarAcaoVerPerfil(ActionListener acao) {
        btnVerPerfil.addActionListener(acao);
    }

    public void adicionarAcaoEnviarEmail(ActionListener acao) {
        btnEnviarEmail.addActionListener(acao);
    }
}