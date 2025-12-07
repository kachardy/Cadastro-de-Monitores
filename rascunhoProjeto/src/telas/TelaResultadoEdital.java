package telas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import outros.Disciplina;
import pessoas.Aluno;
import outros.EditalDeMonitoria;

public class TelaResultadoEdital extends JFrame {

    private JTable tabela;
    private DefaultTableModel modelo;

    public TelaResultadoEdital(EditalDeMonitoria edital) {

        setTitle("Resultado do Edital " + edital.getNumeroEdital());
        setSize(700, 600);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel titulo = new JLabel("Ranking das Disciplinas");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBounds(0, 10, 700, 30);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(titulo);

        modelo = new DefaultTableModel(new Object[]{"Disciplina", "Posição", "Aluno", "Pontuação"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tabela = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBounds(20, 60, 650, 460);
        add(scroll);

        preencherTabela(edital);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.setBounds(280, 530, 120, 30);
        btnFechar.addActionListener(e -> dispose());
        add(btnFechar);

        setVisible(true);
    }

    private void preencherTabela(EditalDeMonitoria edital) {
    	double pesoCRE = edital.getPesoCRE();
        double pesoMedia = edital.getPesoMedia();

        for (Disciplina d : edital.getTodasAsDisciplinas()) {

            List<Aluno> ranking = d.getAlunosInscritos(); 
            
            List<Aluno> alunos = d.getAlunosInscritos();
            List<Double> cres = d.getListaCREs();
            List<Double> medias = d.getListaMedias();
            
            for (int i = 0; i < alunos.size(); i++) {

                double pontuacao = (cres.get(i) * pesoCRE) + (medias.get(i) * pesoMedia);

                modelo.addRow(new Object[]{
                        d.getNome(),
                        (i + 1),
                        alunos.get(i).getNome(),
                        pontuacao
                });
            }
            
        }
    }
}
