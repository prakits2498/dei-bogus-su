package su.android.server.connection;


import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;


/**
 *  Nome: MailClient
 *  Finalidade: Esta classe tem como finalidade o envio de emails
 *  Data de criação: 30/04/2011 
 *  @author: Diogo Mestre
 *
 **/
public class MailClient{
    
    /**
    * Envia um email ao user caso o target se encontre numa zona proibida.
    * 1- Receber os parametros user, pass, mailServer, to, subject e messageBody
    * 2- Enviar o email para o user
    * Data de criacao: 30/04/2011
    * @author: Diogo Mestre
    * @param user - string com o username 
    * @param pass - string com a password 
    * @param mailServer - string com o nome do servidor de email
    * @param to - string com o endereço email do destinatario 
    * @param subject - string com o assunto do email 
    * @param messageBody - string com o corpo do email 
    */
    public void sendMail(String user, String pass, String mailServer, String to, String subject, String messageBody)
            throws MessagingException, AddressException {
        
        // Setup mail server
        Properties props = new Properties();
        props.put("mail.smtps.auth", "true");

        // Get a mail session
        Session session = Session.getDefaultInstance(props, null);

        // Define a new mail message
        MimeMessage message = new MimeMessage(session);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject, "utf-8");
        
        // Create a message part to represent the body text
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(messageBody, "utf-8");
        
        
        //use a MimeMultipart as we need to handle the file attachments
        Multipart multipart = new MimeMultipart();

        //add the message body to the mime message
        multipart.addBodyPart(messageBodyPart);

        // Put all message parts in the message
        message.setContent(multipart);

        // Send the message
        Transport t = session.getTransport("smtps");
        try {
            t.connect(mailServer, user, pass);
            t.sendMessage(message, message.getAllRecipients());
        } finally {
            t.close();
        }
    }
    
    public static void main(String[] args) {
		// TODO Auto-generated method stub
    	try {   
    		MailClient sender = new MailClient();
    		sender.sendMail("durvalp1@gmail.com", "Gozonatuacarinha","smtp.gmail.com","durvalp1@gmail.com","oioi","ioioia");   
    	} catch (Exception e) {   
    		System.out.println(e);   
    	}
    	
    }
 } 