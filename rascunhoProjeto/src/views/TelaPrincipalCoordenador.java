package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import models.Coordenador;
import views.builders.BotaoBuilder;
import views.tema.Tema;

public class TelaPrincipalCoordenador extends TelaPrincipalPadrao {

    private JLabel labelBemVindo;
    private JButton botaoCadastrarEdital;
    private JButton botaoListarEditais;
    private JButton botaoListarAlunos;
    private JButton botaoSair;

    public TelaPrincipalCoordenador(Coordenador coordenador) {
        super("Área do Coordenador", 500, 500);

        if (coordenador != null) {
            labelBemVindo.setText("Bem-vindo(a), " + coordenador.getNome());
        }

        setVisible(true);
    }

    @Override
    protected void inicializarComponentes() {
        setLayout(new BorderLayout());
        adicionarCabecalho("Área do Coordenador", 500);

        JPanel painelCentro = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;

        // --- Instanciação ---
        labelBemVindo = criarLabelBemVindo();

        botaoCadastrarEdital = new BotaoBuilder()
                .comTexto("Cadastrar Edital")
                .comCorFundo(new Color(220, 255, 220))
                .comFonte(Tema.FONTE_DESTAQUE)
                .build();
        botaoCadastrarEdital.setPreferredSize(new Dimension(300, 50));

        botaoListarEditais = new BotaoBuilder()
                .comTexto("Listar Editais")
                .comFonte(Tema.FONTE_DESTAQUE)
                .build();
        botaoListarEditais.setPreferredSize(new Dimension(300, 50));

        botaoListarAlunos = new BotaoBuilder()
                .comTexto("Ver Alunos Cadastrados")
                .comFonte(Tema.FONTE_DESTAQUE)
                .build();
        botaoListarAlunos.setPreferredSize(new Dimension(300, 50));

        botaoSair = new BotaoBuilder()
                .comTexto("Sair")
                .comCorFundo(new Color(255, 200, 200))
                .build();
        botaoSair.setPreferredSize(new Dimension(140, 35));

        // --- Adicionando à Grelha com Margens ---
        gbc.gridy = 0; gbc.insets = new Insets(10, 10, 20, 10);
        painelCentro.add(labelBemVindo, gbc);

        gbc.gridy = 1; gbc.insets = new Insets(0, 10, 15, 10);
        painelCentro.add(botaoCadastrarEdital, gbc);

        gbc.gridy = 2; gbc.insets = new Insets(0, 10, 15, 10);
        painelCentro.add(botaoListarEditais, gbc);

        gbc.gridy = 3; gbc.insets = new Insets(0, 10, 30, 10);
        painelCentro.add(botaoListarAlunos, gbc);

        gbc.gridy = 4; gbc.insets = new Insets(0, 10, 10, 10);
        painelCentro.add(botaoSair, gbc);

        add(painelCentro, BorderLayout.CENTER);
    }

    // --- Getters / Listeners ---
    public void adicionarAcaoCadastrarEdital(ActionListener acao) { botaoCadastrarEdital.addActionListener(acao); }
    public void adicionarAcaoListarEditais(ActionListener acao) { botaoListarEditais.addActionListener(acao); }
    public void adicionarAcaoListarAlunos(ActionListener acao) { botaoListarAlunos.addActionListener(acao); }
    public void adicionarAcaoSair(ActionListener acao) { botaoSair.addActionListener(acao); }
}