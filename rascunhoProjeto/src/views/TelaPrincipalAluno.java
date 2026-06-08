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

import models.Aluno;
import views.builders.BotaoBuilder;
import views.tema.Tema;

public class TelaPrincipalAluno extends TelaPrincipalPadrao {

    private JLabel labelBemVindo;
    private JButton botaoListarEditais;
    private JButton botaoPerfil;
    private JButton botaoSair;

    public TelaPrincipalAluno(Aluno aluno) {
        // 1. O super constrói a janela e chama inicializarComponentes()
        super("Área do Aluno", 500, 400);

        // 2. Só agora que a janela existe, injetamos o nome do aluno logado
        if (aluno != null) {
            labelBemVindo.setText("Bem-vindo(a), " + aluno.getNome());
        }

        setVisible(true);
    }

    @Override
    protected void inicializarComponentes() {
        setLayout(new BorderLayout());
        adicionarCabecalho("Área do Aluno", 500);

        JPanel painelCentro = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0; // Todos ficarão na mesma coluna (empilhados)

        // --- Instanciação ---
        labelBemVindo = criarLabelBemVindo();

        botaoListarEditais = new BotaoBuilder()
                .comTexto("Ver Editais Disponíveis")
                .comCorFundo(new Color(220, 255, 255))
                .comFonte(Tema.FONTE_DESTAQUE)
                .build();
        botaoListarEditais.setPreferredSize(new Dimension(300, 50));

        botaoPerfil = new BotaoBuilder()
                .comTexto("Meu Perfil")
                .comFonte(Tema.FONTE_DESTAQUE)
                .build();
        botaoPerfil.setPreferredSize(new Dimension(300, 50));

        botaoSair = new BotaoBuilder()
                .comTexto("Sair")
                .comCorFundo(new Color(255, 200, 200))
                .build();
        botaoSair.setPreferredSize(new Dimension(140, 35));

        // --- Adicionando à Grelha com Margens ---
        gbc.gridy = 0; gbc.insets = new Insets(10, 10, 20, 10);
        painelCentro.add(labelBemVindo, gbc);

        gbc.gridy = 1; gbc.insets = new Insets(0, 10, 15, 10);
        painelCentro.add(botaoListarEditais, gbc);

        gbc.gridy = 2; gbc.insets = new Insets(0, 10, 30, 10);
        painelCentro.add(botaoPerfil, gbc);

        gbc.gridy = 3; gbc.insets = new Insets(0, 10, 10, 10);
        painelCentro.add(botaoSair, gbc);

        add(painelCentro, BorderLayout.CENTER);
    }

    // --- Getters / Listeners intactos ---
    public void adicionarAcaoListarEditais(ActionListener acao) { botaoListarEditais.addActionListener(acao); }
    public void adicionarAcaoVerPerfil(ActionListener acao) { botaoPerfil.addActionListener(acao); }
    public void adicionarAcaoSair(ActionListener acao) { botaoSair.addActionListener(acao); }
}