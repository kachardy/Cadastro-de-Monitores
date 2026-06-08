package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import views.builders.BotaoBuilder;
import views.factories.FabricaDeComponentes;
import views.tema.Tema;

public class TelaPadraoCadastro extends TelaBase {

    private JTextField tfNome;
    private JTextField tfMatricula;
    private JTextField tfEmail;
    private JPasswordField tfSenha;
    private JButton botaoCadastrar;
    private JButton botaoCancelar;

    public TelaPadraoCadastro() {
        super("Cadastro", 500, 600);
    }

    @Override
    public void inicializarComponentes() {
        // 1. Layout principal em blocos (Norte, Sul, Centro...)
        setLayout(new BorderLayout());

        // 2. Cabeçalho vai automaticamente para o topo (NORTH)
        adicionarCabecalho("Novo Cadastro", 500);

        // 3. Preparar a grelha central
        JPanel painelCentro = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margem uniforme
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 4. Instanciar os componentes usando a Fábrica e definir o tamanho responsivo
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

        // Botões usando o Builder e o Tema
        botaoCadastrar = new BotaoBuilder()
                .comTexto("Cadastrar")
                .comCorFundo(Tema.COR_FUNDO_BOTAO_ACAO)
                .build();
        botaoCadastrar.setPreferredSize(new Dimension(110, 35));

        botaoCancelar = new BotaoBuilder()
                .comTexto("Cancelar")
                .build();
        botaoCancelar.setPreferredSize(new Dimension(110, 35));

        // --- 5. Montar o formulário na Grelha ---

        // Linha 0 (Nome)
        gbc.gridx = 0; gbc.gridy = 0; painelCentro.add(labelNome, gbc);
        gbc.gridx = 1; gbc.gridy = 0; painelCentro.add(tfNome, gbc);

        // Linha 1 (Matrícula)
        gbc.gridx = 0; gbc.gridy = 1; painelCentro.add(labelMatricula, gbc);
        gbc.gridx = 1; gbc.gridy = 1; painelCentro.add(tfMatricula, gbc);

        // Linha 2 (Email)
        gbc.gridx = 0; gbc.gridy = 2; painelCentro.add(labelEmail, gbc);
        gbc.gridx = 1; gbc.gridy = 2; painelCentro.add(tfEmail, gbc);

        // Linha 3 (Senha)
        gbc.gridx = 0; gbc.gridy = 3; painelCentro.add(labelSenha, gbc);
        gbc.gridx = 1; gbc.gridy = 3; painelCentro.add(tfSenha, gbc);

        // Linha 4 (Painel de Botões - Agrupados na mesma célula estendida)
        JPanel painelBotoes = new JPanel();
        painelBotoes.add(botaoCadastrar);
        painelBotoes.add(botaoCancelar);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2; // Ocupar duas colunas para centralizar abaixo do formulário
        painelCentro.add(painelBotoes, gbc);

        // 6. Colar o painel completo no centro do ecrã
        add(painelCentro, BorderLayout.CENTER);
    }

    // --- Getters e Listeners permanecem 100% inalterados ---
    public String getNome() { return tfNome.getText(); }
    public String getMatricula() { return tfMatricula.getText(); }
    public String getEmail() { return tfEmail.getText(); }
    public String getSenha() { return new String(tfSenha.getPassword()); }

    public void adicionarAcaoSalvar(ActionListener acao) { botaoCadastrar.addActionListener(acao); }
    public void adicionarAcaoCancelar(ActionListener acao) { botaoCancelar.addActionListener(acao); }
}