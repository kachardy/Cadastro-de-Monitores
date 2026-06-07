package views.builders;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.JButton;

    public class BotaoBuilder {

        // O botão que está sendo "fabricado"
        private JButton botao;

        // Construtor: Já inicia com um botão vazio
        public BotaoBuilder() {
            this.botao = new JButton();
        }

        // --- Métodos Fluentes ---

        public BotaoBuilder comTexto(String texto) {
            this.botao.setText(texto);
            return this; // Retorna a própria classe para permitir encadeamento
        }

        public BotaoBuilder comPosicao(int x, int y, int largura, int altura) {
            this.botao.setBounds(x, y, largura, altura);
            return this;
        }

        public BotaoBuilder comCorFundo(Color cor) {
            this.botao.setBackground(cor);
            return this;
        }

        public BotaoBuilder comFonte(Font fonte) {
            this.botao.setFont(fonte);
            return this;
        }

        // Metodo extra para já atrelar uma ação de clique logo na criação, se quiser

        public BotaoBuilder comAcao(ActionListener acao) {
            this.botao.addActionListener(acao);
            return this;
        }

        // --- Metodo Finalizador ---

        // Devolve a peça pronta e finalizada
        public JButton build() {
            return this.botao;
        }

    }

