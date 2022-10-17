package cr.ac.ucr.ecci.proyecto_arce_mall.mail;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailApi extends AsyncTask<Void,Void,Void>  {

    private Context context;
    private Session session;
    private String email;
    private String message;
    private String subject;

    private ProgressDialog progressDialog;


    public JavaMailApi(Context context, String email, String message, String subject) {
        this.context = context;
        this.email = email;
        this.message = message;
        this.subject = subject;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.progressDialog = ProgressDialog.show(this.context,
                "Sending message",
                "Please wait...",
                false,
                false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        this.progressDialog.dismiss();

        Toast.makeText(this.context, "Message Sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        this.session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Utils.EMAIL, Utils.PASSWORD);
            }
        });

        try {

            MimeMessage mimeMessage = new MimeMessage(this.session);

            mimeMessage.setFrom(new InternetAddress(Utils.EMAIL));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(this.email));
            mimeMessage.setSubject(this.subject);
            mimeMessage.setContent(this.message, "text/html");

            Transport.send(mimeMessage);

        } catch (MessagingException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}