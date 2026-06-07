package views;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import views.builders.BotaoBuilder;
import views.factories.FabricaDeComponentes;
import views.tema.Tema;

public class TelaLogin extends TelaBase {

    private JTextField tfEmail;
    private JPasswordField tfSenha;
    private JButton botaoLogin;
    private JButton botaoCancelar;
    private JLabel labelLinkCadastro;

    public TelaLogin() {
        super("Login", 500, 600);
    }

    @Override
    protected void inicializarComponentes() {
        // 1. MUDANÇA DE PARADIGMA: Sobrescrevemos o layout nulo para um layout em blocos!
        setLayout(new BorderLayout());

        // 2. Cabeçalho (O nosso método inteligente vai enviá-lo logo para o Topo/NORTH)
        adicionarCabecalho("Login", 500);

        // 3. Criar a "Grelha" (GridBagLayout) para o centro do ecrã
        JPanel painelCentro = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margem de segurança de 10px entre cada item
        gbc.fill = GridBagConstraints.HORIZONTAL; // Esticar sempre na horizontal

        // 4. Instanciar os componentes com a Fábrica Responsiva (Zero coordenadas!)
        JLabel labelEmail = FabricaDeComponentes.criarLabel("Email: ");
        tfEmail = FabricaDeComponentes.criarTextField();
        tfEmail.setPreferredSize(new Dimension(280, 35)); // Definimos o tamanho ideal que queremos

        JLabel labelSenha = FabricaDeComponentes.criarLabel("Senha: ");
        tfSenha = FabricaDeComponentes.criarPasswordField();
        tfSenha.setPreferredSize(new Dimension(280, 35));

        botaoLogin = new BotaoBuilder()
                .comTexto("Login")
                .comCorFundo(Tema.COR_FUNDO_BOTAO_ACAO)
                .build();
        botaoLogin.setPreferredSize(new Dimension(110, 35));

        botaoCancelar = new BotaoBuilder()
                .comTexto("Cancelar")
                .build();
        botaoCancelar.setPreferredSize(new Dimension(110, 35));

        labelLinkCadastro = new JLabel("Não tem conta? Faça o cadastro aqui.");
        labelLinkCadastro.setForeground(Tema.COR_PRIMARIA_VERDE);
        labelLinkCadastro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        labelLinkCadastro.setHorizontalAlignment(SwingConstants.CENTER);

        // --- 5. Montar o Puzzle na Grelha ---

        // Linha 0 (Email) - Coluna 0 e Coluna 1
        gbc.gridx = 0; gbc.gridy = 0; painelCentro.add(labelEmail, gbc);
        gbc.gridx = 1; gbc.gridy = 0; painelCentro.add(tfEmail, gbc);

        // Linha 1 (Senha)
        gbc.gridx = 0; gbc.gridy = 1; painelCentro.add(labelSenha, gbc);
        gbc.gridx = 1; gbc.gridy = 1; painelCentro.add(tfSenha, gbc);

        // Linha 2 (Painel auxiliar para empacotar os dois botões juntos)
        JPanel painelBotoes = new JPanel();
        painelBotoes.add(botaoLogin);
        painelBotoes.add(botaoCancelar);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2; // Dizemos que os botões vão ocupar o espaço de 2 colunas!
        painelCentro.add(painelBotoes, gbc);

        // Linha 3 (Link de Cadastro)
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        painelCentro.add(labelLinkCadastro, gbc);

        // 6. Colamos o painel inteiro exatamente no centro geométrico do ecrã
        add(painelCentro, BorderLayout.CENTER);
    }

    // --- Getters e Listeners permanecem intactos! ---
    public String getEmail() { return tfEmail.getText(); }
    public String getSenha() { return new String(tfSenha.getPassword()); }

    public void adicionarAcaoSalvar(ActionListener acao) { botaoLogin.addActionListener(acao); }
    public void adicionarAcaoCancelar(ActionListener acao) { botaoCancelar.addActionListener(acao); }
    public void adicionarAcaoLinkCadastro(MouseListener acao) { labelLinkCadastro.addMouseListener(acao); }
}