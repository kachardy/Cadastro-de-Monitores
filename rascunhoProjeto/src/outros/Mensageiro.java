package outros;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class Mensageiro {

    public static boolean enviarEmail(String destinatario) {
        String remetente = "kauesegundario2@gmail.com";
        String senha = "hgluzlsflircleiq";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // força TLS 1.2

        try {
            Session sessao = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(remetente, senha);
                }
            });

            Message msg = new MimeMessage(sessao);
            msg.setFrom(new InternetAddress(remetente));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            msg.setSubject("Inscrição no Edital");
            msg.setText("Olá, sua inscrição no edital foi bem-sucedida!");

            Transport.send(msg);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
    }

}
