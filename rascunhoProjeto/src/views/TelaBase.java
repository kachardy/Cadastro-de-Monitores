package views;

import java.awt.Color;
import java.awt.Font;
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
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        labelTitulo.setOpaque(true);
        labelTitulo.setBackground(new Color(0, 128, 0)); // Verde padrão do IFPB
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitulo.setBounds(0, 0, largura, 50);
        add(labelTitulo);
    }

    // Metodo abstrato que toda tela filha será obrigada a fornecer
    protected abstract void inicializarComponentes();
}