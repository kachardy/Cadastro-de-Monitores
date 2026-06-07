package views.factories;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import views.tema.Tema;

public class FabricaDeComponentes {

    public static JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(Tema.FONTE_PADRAO);
        return label;
    }

    public static JTextField criarTextField() {
        JTextField textField = new JTextField();
        textField.setFont(Tema.FONTE_PADRAO);
        return textField;
    }

    public static JPasswordField criarPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(Tema.FONTE_PADRAO);
        passwordField.setEchoChar('*');
        return passwordField;
    }
}