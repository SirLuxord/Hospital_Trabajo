package hospital_fct.QR_Email;

public class TestQRMail {
    public static void main(String[] args) {
        String contenidoQR = "Operación: Apendicectomía\nFecha: 25/04/2025\nHora: 10:30\nSector: Quirófano 2";
        String destinatario = "usuario@gmail.com";
        String asunto = "Detalles de tu operación";
        String texto = "Adjunto encontrarás el código QR con los detalles de tu operación.";

        try {
            byte[] qr = QRGenerator.generarQR(contenidoQR, 250, 250);
            EmailSender.enviarCorreoConQR(destinatario, asunto, texto, qr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
