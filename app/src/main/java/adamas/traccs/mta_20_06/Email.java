/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adamas.traccs.mta_20_06;

/**
 *
 * @author arshad
 */

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Properties;



public class Email extends javax.mail.Authenticator {
    private final String mailhost2 ;
    private final String user;
    private final String password;
    private final Session session;
    private final String port2;


    static {
        Security.addProvider(new JSSEProvider());
    }


    public Email(String user, String password,String mailhost, String port) {
        this.user = user;
        this.password = password;
        this.mailhost2=mailhost;
        this.port2=port;


        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", this.mailhost2);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", this.port2);
        props.put("mail.smtp.socketFactory.port", this.port2);
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "true");
        props.put("mail.smtp.starttls.enable", "true");

        session = Session.getDefaultInstance(props, this);

    }


    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(this.user, this.password);
    }
    public synchronized void sendMail(String subject, String body, String sender, String recipients) throws Exception {
      String Exception_message="";
        try{
            MimeMessage message = new MimeMessage(session);
            DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
            message.setSender(new InternetAddress(sender));
            message.setSubject(subject,"UTF-8");
            message.setDataHandler(handler);
            if (recipients.indexOf(',') > 0)
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
            else
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));

            message.setFrom(sender);

            message.setContent(body, " text/plain");
            try  {
                Transport transport = session.getTransport();
                transport.connect(mailhost2, user, password);
                transport.sendMessage(message, message.getAllRecipients());
                transport.close();
            }catch(Exception ex){
              throw  ex;
            }



        }catch(Exception e){
            throw  e;
        }
    }

    public class ByteArrayDataSource implements DataSource {
        private final byte[] data;
        private String type;

        public ByteArrayDataSource(byte[] data, String type) {
            super();
            this.data = data;
            this.type = type;
        }

        public ByteArrayDataSource(byte[] data) {
            super();
            this.data = data;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContentType() {
            if (type == null)
                return "application/octet-stream";
            else
                return type;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        public String getName() {
            return "ByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }
    }
}

