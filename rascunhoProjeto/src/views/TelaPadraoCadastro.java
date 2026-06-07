package views;

import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import views.builders.BotaoBuilder; // Importando nosso construtor de botões

public class TelaPadraoCadastro extends TelaBase {

    private JTextField tfNome;
    private JTextField tfMatricula;
    private JTextField tfEmail;
    private JPasswordField tfSenha;
    private JButton botaoCadastrar;
    private JButton botaoCancelar;

    public TelaPadraoCadastro() {
        // Repassa o nome genérico da janela e o tamanho para a classe mãe
        super("Cadastro", 500, 600);
    }

    @Override
    protected void inicializarComponentes() {
        // NOTA: O adicionarCabecalho foi retirado daqui para permitir
        // que as telas filhas decidam qual cabeçalho vão desenhar!

        // Labels
        JLabel labelNome = new JLabel("Nome: ");
        labelNome.setBounds(50, 100, 80, 30);

        JLabel labelMatricula = new JLabel("Matrícula: ");
        labelMatricula.setBounds(50, 150, 80, 30);

        JLabel labelEmail = new JLabel("Email: ");
        labelEmail.setBounds(50, 200, 80, 30);

        JLabel labelSenha = new JLabel("Senha: ");
        labelSenha.setBounds(50, 250, 80, 30);

        // TextFields
        tfNome = new JTextField();
        tfNome.setBounds(130, 100, 280, 30);

        tfMatricula = new JTextField();
        tfMatricula.setBounds(130, 150, 280, 30);

        tfEmail = new JTextField();
        tfEmail.setBounds(130, 200, 280, 30);

        tfSenha = new JPasswordField();
        tfSenha.setEchoChar('*');
        tfSenha.setBounds(130, 250, 280, 30);

        // Criação dos botões usando o nosso Builder
        botaoCadastrar = new BotaoBuilder()
                .comTexto("Cadastrar")
                .comPosicao(110, 330, 110, 35)
                .comCorFundo(new Color(200, 255, 200))
                .build();

        botaoCancelar = new BotaoBuilder()
                .comTexto("Cancelar")
                .comPosicao(260, 330, 110, 35)
                .build();

        // Adicionando tudo na tela
        add(labelNome); add(tfNome);
        add(labelMatricula); add(tfMatricula);
        add(labelEmail); add(tfEmail);
        add(labelSenha); add(tfSenha);
        add(botaoCadastrar); add(botaoCancelar);

        setVisible(true);
    }

    // Getters
    public String getNome() { return tfNome.getText(); }
    public String getMatricula() { return tfMatricula.getText(); }
    public String getEmail() { return tfEmail.getText(); }
    public String getSenha() { return new String(tfSenha.getPassword()); }

    // Listeners
    public void adicionarAcaoSalvar(ActionListener acao) { botaoCadastrar.addActionListener(acao); }
    public void adicionarAcaoCancelar(ActionListener acao) { botaoCancelar.addActionListener(acao); }
}