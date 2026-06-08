package views;

import views.factories.FabricaDeComponentes;
import javax.swing.JLabel;

public abstract class TelaPrincipalPadrao extends TelaBase {

    // Agora ela repassa a responsabilidade de tamanho e título para a TelaBase
    public TelaPrincipalPadrao(String titulo, int largura, int altura) {
        super(titulo, largura, altura);
    }

    // Método utilitário herdado pelas telas filhas para padronizar a mensagem
    protected JLabel criarLabelBemVindo() {
        JLabel label = FabricaDeComponentes.criarLabel("");
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }
}