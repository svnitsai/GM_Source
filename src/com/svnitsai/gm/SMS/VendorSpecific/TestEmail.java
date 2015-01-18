package com.svnitsai.gm.SMS.VendorSpecific;

//import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

public class TestEmail {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			sendSimpleMail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void sendSimpleMail() throws Exception {
		
		HtmlEmail email = new HtmlEmail();
		email.setHostName("smtp.gmail.com");
		email.setSmtpPort(465);
		email.setSSL(true);
		
	    email.setAuthentication("username@gmail.com", "password");
	    email.setDebug(false);
	    email.setHostName("smtp.gmail.com");
	    email.setFrom("username@gmail.com");
	    email.setSubject("Hi");
	    email.setMsg("This is a test mail ... :-)");
	    email.addTo("toemail@yahoo.com");
	    email.setTLS(true);
	    email.send();
	    System.out.println("Mail sent!");
	}
}

