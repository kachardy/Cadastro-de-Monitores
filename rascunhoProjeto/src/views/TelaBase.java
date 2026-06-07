package views;

import views.tema.Tema;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public abstract class TelaBase extends JFrame {

    // O construtor dita o "Template" de como uma janela deve ser montada
    public TelaBase(String titulo, int largura, int altura) {
        setTitle(titulo);
        setSize(largura, altura);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);

        // Centralização do carregamento do logotipo
        try {
            ImageIcon imagemIcone = new ImageIcon("ifpblogo.png");
            setIconImage(imagemIcone.getImage());
        } catch (Exception e) {
            System.out.println("Logo não encontrada");
        }

        // O "Gancho" (Hook) do Template Method:
        // Executa a montagem específica dos componentes que cada tela filha definir
        inicializarComponentes();
    }

    // Metodo utilitário herdável para adicionar o cabeçalho verde padronizado
    public void adicionarCabecalho(String titulo, int largura) {
        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(Tema.FONTE_CABECALHO);
        labelTitulo.setOpaque(true);
        labelTitulo.setBackground(Tema.COR_PRIMARIA_VERDE);
        labelTitulo.setForeground(Tema.COR_TEXTO_BRANCO);
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        labelTitulo.setBounds(0, 0, largura, 50);

        // Define um tamanho preferencial para as telas novas e responsivas
        labelTitulo.setPreferredSize(new Dimension(largura, 50));

        // Magia da Arquitetura: A TelaBase descobre se a tela filha é moderna ou legada
        if (getLayout() instanceof java.awt.BorderLayout) {
            add(labelTitulo, java.awt.BorderLayout.NORTH); // Cola no topo
        } else {
            add(labelTitulo); // Adiciona solto (Modo antigo)
        }
    }

    // Metodo abstrato que toda tela filha será obrigada a fornecer
    protected abstract void inicializarComponentes();
}