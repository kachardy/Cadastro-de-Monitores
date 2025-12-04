package telas;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import outros.Disciplina;
import outros.EditalDeMonitoria;
import pessoas.Aluno;

public class TelaDetalheEditalCoordenador extends JFrame {

    private JButton btnEditar;
    private JButton btnEncerrar;
    private JButton btnClonar;
    private JButton btnCalcularResultado;
    private JButton btnVoltar;
    
    private JTable tabelaInscritos;
    private DefaultTableModel modeloTabela;
    private JLabel labelStatus;

    public TelaDetalheEditalCoordenador(EditalDeMonitoria edital) {
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
        JScrollPane scroll = new JScrollPane(tabelaInscritos);
        scroll.setBounds(30, 150, 630, 250);
        add(scroll);
        
        preencherTabela(edital);

        // Botões de Ação
        
        btnEditar = new JButton("Editar");
        btnEditar.setBounds(30, 430, 100, 35);
        
        btnEncerrar = new JButton("Encerrar");
        btnEncerrar.setBounds(140, 430, 100, 35);
        btnEncerrar.setBackground(new Color(255, 200, 200));
        
        btnClonar = new JButton("Clonar");
        btnClonar.setBounds(250, 430, 100, 35);
        
        btnCalcularResultado = new JButton("Calcular Resultado");
        btnCalcularResultado.setBounds(360, 430, 160, 35);
        btnCalcularResultado.setBackground(new Color(200, 255, 200));
        
        btnVoltar = new JButton("Voltar");
        btnVoltar.setBounds(540, 430, 120, 35);

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

        setVisible(true);
    }

    private void preencherTabela(EditalDeMonitoria edital) {
        for (Disciplina d : edital.getTodasAsDisciplinas()) {
            
            // Pega as listas protegidas (agora não vai dar erro se tiver null)
            ArrayList<Aluno> listaAlunos = d.getAlunosInscritos();
            ArrayList<Double> listaCres = d.getListaCREs();
            ArrayList<Double> listaMedias = d.getListaMedias();
            
            for (int i = 0; i < listaAlunos.size(); i++) {
                Aluno a = listaAlunos.get(i);
                
                // Verifica se tem nota salva para esse índice (segurança extra)
                Double cre = (i < listaCres.size()) ? listaCres.get(i) : 0.0;
                Double media = (i < listaMedias.size()) ? listaMedias.get(i) : 0.0;
                
                Object[] linha = {
                    d.getNome(),
                    a.getNome(),
                    a.getMatricula(),
                    cre, 
                    media  
                };
                modeloTabela.addRow(linha);
            }
        }
    }
    
    // Listeners Expandidos
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
}