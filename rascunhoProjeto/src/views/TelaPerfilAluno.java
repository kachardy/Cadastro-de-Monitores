package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import models.Aluno;
import views.builders.BotaoBuilder;
import views.factories.FabricaDeComponentes;
import views.tema.Tema;

public class TelaPerfilAluno extends TelaBase {

    private JTextField tfNome;
    private JTextField tfMatricula;
    private JTextField tfEmail;
    private JPasswordField tfSenha;

    private JButton botaoSalvar;
    private JButton botaoVoltar;

    private JPanel painelCentro;

    public TelaPerfilAluno(Aluno aluno, boolean coordenadorLogado) {
        super("Perfil do Aluno", 500, 600);

        // Preenchimento de dados
        if (aluno != null) {
            tfNome.setText(aluno.getNome());
            tfMatricula.setText(aluno.getMatricula());
            tfEmail.setText(aluno.getEmail());
            tfSenha.setText(aluno.getSenha());
        }

        // Montagem Dinâmica: Se for coordenador, injetamos a tabela e aumentamos a tela
        if (coordenadorLogado) {
            setSize(500, 680);
            adicionarTabelaHistorico(aluno);
        }

        setVisible(true);
    }

    @Override
    protected void inicializarComponentes() {
        setLayout(new BorderLayout());
        adicionarCabecalho("Editar Perfil", 500);

        // 1. Painel Central (Vai conter o form e opcionalmente a tabela)
        painelCentro = new JPanel(new BorderLayout());

        // 2. Formulário Principal
        JPanel painelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel labelNome = FabricaDeComponentes.criarLabel("Nome: ");
        tfNome = FabricaDeComponentes.criarTextField();
        tfNome.setPreferredSize(new Dimension(280, 35));

        JLabel labelMatricula = FabricaDeComponentes.criarLabel("Matrícula: ");
        tfMatricula = FabricaDeComponentes.criarTextField();
        tfMatricula.setPreferredSize(new Dimension(280, 35));

        JLabel labelEmail = FabricaDeComponentes.criarLabel("Email: ");
        tfEmail = FabricaDeComponentes.criarTextField();
        tfEmail.setPreferredSize(new Dimension(280, 35));

        JLabel labelSenha = FabricaDeComponentes.criarLabel("Senha: ");
        tfSenha = FabricaDeComponentes.criarPasswordField();
        tfSenha.setPreferredSize(new Dimension(280, 35));

        gbc.gridx = 0; gbc.gridy = 0; painelForm.add(labelNome, gbc);
        gbc.gridx = 1; gbc.gridy = 0; painelForm.add(tfNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; painelForm.add(labelMatricula, gbc);
        gbc.gridx = 1; gbc.gridy = 1; painelForm.add(tfMatricula, gbc);

        gbc.gridx = 0; gbc.gridy = 2; painelForm.add(labelEmail, gbc);
        gbc.gridx = 1; gbc.gridy = 2; painelForm.add(tfEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 3; painelForm.add(labelSenha, gbc);
        gbc.gridx = 1; gbc.gridy = 3; painelForm.add(tfSenha, gbc);

        painelCentro.add(painelForm, BorderLayout.NORTH);
        add(painelCentro, BorderLayout.CENTER);

        // 3. Botões Inferiores
        botaoSalvar = new BotaoBuilder()
                .comTexto("Salvar")
                .comCorFundo(Tema.COR_FUNDO_BOTAO_ACAO)
                .build();
        botaoSalvar.setPreferredSize(new Dimension(150, 40));

        botaoVoltar = new BotaoBuilder().comTexto("Voltar").build();
        botaoVoltar.setPreferredSize(new Dimension(130, 40));

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoVoltar);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    // Método privado auxiliar para montar a tabela sem poluir o inicializarComponentes()
    private void adicionarTabelaHistorico(Aluno aluno) {
        JPanel painelHist = new JPanel(new BorderLayout(0, 10));
        painelHist.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        JLabel labelHist = FabricaDeComponentes.criarLabel("Histórico de Monitorias:");
        labelHist.setFont(Tema.FONTE_DESTAQUE);
        painelHist.add(labelHist, BorderLayout.NORTH);

        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Monitorias"}, 0);
        JTable tabela = new JTable(modelo);
        tabela.setFont(Tema.FONTE_PADRAO);
        tabela.setRowHeight(25);

        if (aluno != null && aluno.getHistoricoMonitorias() != null) {
            for (String h : aluno.getHistoricoMonitorias()) {
                modelo.addRow(new Object[]{h});
            }
        }

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setPreferredSize(new Dimension(380, 120));
        painelHist.add(scroll, BorderLayout.CENTER);

        painelCentro.add(painelHist, BorderLayout.CENTER);
    }

    public String getNome() { return tfNome.getText(); }
    public String getMatricula() { return tfMatricula.getText(); }
    public String getEmail() { return tfEmail.getText(); }
    public String getSenha() { return new String(tfSenha.getPassword()); }

    public void adicionarAcaoSalvar(ActionListener acao) { botaoSalvar.addActionListener(acao); }
    public void adicionarAcaoVoltar(ActionListener acao) { botaoVoltar.addActionListener(acao); }
}