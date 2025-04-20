package hospital_fct.QR_Email;
import javax.mail.*;
import javax.mail.internet.*;
import java.nio.file.Files;
import java.util.Properties;

public class EmailSender {

    public static void enviarCorreoConQR(String destinatario, String asunto, String mensajeTexto, byte[] qrBytes) {
        final String remitente = "haendel.tee.2013@gmail.com";
        final String password = "mmww ogeb gwon suay";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);

            // Parte del mensaje de texto
            MimeBodyPart textoPart = new MimeBodyPart();
            textoPart.setText(mensajeTexto);

            // Parte del QR adjunto
            MimeBodyPart qrPart = new MimeBodyPart();
            qrPart.attachFile(Files.write(Files.createTempFile("qr", ".png"), qrBytes).toFile());

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textoPart);
            multipart.addBodyPart(qrPart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Correo enviado correctamente con QR adjunto.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
