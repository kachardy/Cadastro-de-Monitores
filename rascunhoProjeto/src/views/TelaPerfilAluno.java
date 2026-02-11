package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import models.Aluno;

public class TelaPerfilAluno extends JFrame {
	
	// Atributos
	private JTextField tfNome;
	private JTextField tfMatricula;
	private JTextField tfEmail;
	private JPasswordField tfSenha;
	
	private JButton botaoSalvar;
	private JButton botaoVoltar;
	
	public TelaPerfilAluno(Aluno aluno, boolean coordenadorLogado) {
		
		// Configurações da Janela
		setTitle("Perfil do Aluno");
		setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        
        // Ícone
        try {
            setIconImage(new ImageIcon("ifpblogo.png").getImage()); 
        } catch (Exception e) {
        	
        }

        // Cabeçalho
        JLabel labelTitulo = new JLabel("Editar Perfil");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        labelTitulo.setOpaque(true);
        labelTitulo.setBackground(new Color(0, 128, 0)); 
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitulo.setBounds(0, 0, 500, 50); 
        add(labelTitulo);
    
        
        // Campo: Nome
        JLabel labelNome = new JLabel("Nome: ");
        labelNome.setBounds(50, 100, 80, 30);
        tfNome = new JTextField(); 
        tfNome.setBounds(130, 100, 280, 30);
        
        // Campo: Matrícula
        JLabel labelMatricula = new JLabel("Matrícula: ");
        labelMatricula.setBounds(50, 160, 80, 30);
        tfMatricula = new JTextField();
        tfMatricula.setBounds(130, 160, 280, 30);
        tfMatricula.setEditable(false); // Bloqueia matrícula
        tfMatricula.setBackground(new Color(230, 230, 230)); // Cinza pra o block
        
        // Campo: Email
        JLabel labelEmail = new JLabel("Email: ");
        labelEmail.setBounds(50, 220, 80, 30);
        tfEmail = new JTextField();
        tfEmail.setBounds(130, 220, 280, 30);
        
        // Campo: Senha
        JLabel labelSenha = new JLabel("Senha: ");
        labelSenha.setBounds(50, 280, 80, 30);
        tfSenha = new JPasswordField(); 
        tfSenha.setBounds(130, 280, 280, 30);
        tfSenha.setEchoChar('*');
        
        //  Preenchimento automático
        if (aluno != null) {
        	tfNome.setText(aluno.getNome());
        	tfMatricula.setText(aluno.getMatricula());
        	tfEmail.setText(aluno.getEmail());
        	tfSenha.setText(aluno.getSenha());
        }
        
       // Botões
        
        botaoSalvar = new JButton("Salvar Alterações");
        botaoSalvar.setBounds(100, 360, 150, 40);
        botaoSalvar.setBackground(new Color(200, 255, 200)); 
        
        botaoVoltar = new JButton("Voltar");
        botaoVoltar.setBounds(270, 360, 130, 40);
        
        // Adicionando tudo na tela
        add(labelNome); add(tfNome);
        add(labelMatricula); add(tfMatricula);
        add(labelEmail); add(tfEmail);
        add(labelSenha); add(tfSenha);
        
        add(botaoSalvar);
        add(botaoVoltar);
        
        if (coordenadorLogado) {

            JLabel labelHist = new JLabel("Histórico de Monitorias:");
            labelHist.setBounds(50, 330, 300, 30);
            labelHist.setFont(new Font("Arial", Font.BOLD, 14));
            add(labelHist);

            DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Monitorias"}, 0);
            JTable tabela = new JTable(modelo);

            for (String h : aluno.getHistoricoMonitorias()) {
                modelo.addRow(new Object[]{h});
            }

            JScrollPane scroll = new JScrollPane(tabela);
            scroll.setBounds(50, 370, 380, 120);
            add(scroll);

            // Bota pra baixo os botão
            botaoSalvar.setBounds(100, 510, 150, 40);
            botaoVoltar.setBounds(270, 510, 130, 40);

            setSize(500, 620);
        }

        
        setVisible(true);
	}
	
	// Getters
	public String getNome() { 
		return tfNome.getText(); 
	}
	
	public String getEmail() { 
		return tfEmail.getText(); 
	}
	
	public String getSenha() { 
		return new String(tfSenha.getPassword()); 
	}
	
	// Ações
	public void adicionarAcaoSalvar(ActionListener acao) {
		botaoSalvar.addActionListener(acao);
	}
	
	public void adicionarAcaoVoltar(ActionListener acao) {
		botaoVoltar.addActionListener(acao);
	}
}