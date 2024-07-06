package MarketYonetim;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


	public class EmailSender {

	    public static void sendEmail(String tc, String name, String surname, String email) {
	        Properties props = new Properties();
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.starttls.enable", "true");  
	        props.put("mail.smtp.host", "smtp.gmail.com");   
	        props.put("mail.smtp.port", "587"); 

	        Session session = Session.getInstance(props, new Authenticator() {
	            @Override
	            protected PasswordAuthentication getPasswordAuthentication() {
	              
	                return new PasswordAuthentication("sd.demirrkaya0606@gmail.com", "cpln zjjv xdwd uoqg");
	            }
	        });

	        try {
	            Message message = new MimeMessage(session);
	            session.setDebug(true);

	          
	            message.setFrom(new InternetAddress("sd.demirrkaya0606@gmail.com"));
	            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
	            message.setSubject("İnsan Kaynakları");
	            message.setText("Sayın " + name + " " + surname + ",\n\n" +
	                    "İş akdiniz sonlandırılmıtşır " + tc + "\n\n" +
	                    "İyi günler dileriz.");

	            Transport.send(message);
	       

	            try (Transport transport = session.getTransport("smtp")) {
	                transport.connect();
	                transport.sendMessage(message, message.getAllRecipients());
	            }

	            System.out.println("E-posta gönderildi.");

	        } catch (MessagingException e) {
	            e.printStackTrace();
	        }
	    }

	    public static void main(String[] args) {
	        
	        sendEmail("1234567890", "sefa", "demirkaya", "sf.demirkaya06@gmail.com");
	    }
	}