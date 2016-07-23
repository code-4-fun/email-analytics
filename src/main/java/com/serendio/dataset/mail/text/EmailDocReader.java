package com.serendio.dataset.mail.text;

import com.serendio.Utils.Utils;
import com.serendio.dataset.domain.EmailDoc;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public class EmailDocReader {
	private String PathForDoc;

	public EmailDocReader() {
		setPathForDoc(null);
	}

	public EmailDocReader(String Path) {
		setPathForDoc(Path);
	}

	public EmailDoc processDoc() throws MessagingException, IOException {
		if (getPathForDoc().equals(null)) {
			System.out.println("Please set the EmailDocPath");
			return null;
		}
		File mailFile = new File(getPathForDoc());
		String host = "host.com";
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		Session session = Session.getDefaultInstance(properties);
		FileInputStream fileStream = new FileInputStream(mailFile);
		MimeMessage email = new MimeMessage(session, fileStream);

		EmailDoc mailDoc = new EmailDoc();
		mailDoc.setName(getNameFromHeader(email));
		mailDoc.setFrom(getFrom(email));
		mailDoc.setSubject(email.getSubject());
		mailDoc.setMessage_ID(email.getMessageID());
		mailDoc.setDate(email.getSentDate().toString());
		System.err.println(getContent(email));
		mailDoc.setContent(getContent(email));
		mailDoc.setTo(Utils.addressArrayToHashset(email
				.getRecipients(Message.RecipientType.TO)));
		mailDoc.setBcc(Utils.addressArrayToHashset(email
				.getRecipients(Message.RecipientType.BCC)));
		mailDoc.setCc(Utils.addressArrayToHashset(email
				.getRecipients(Message.RecipientType.CC)));
		return mailDoc;
	}
	
	private String getContent(MimeMessage email) {
		try {
		Object content = email.getContent();
		if (content instanceof String) return content.toString().trim();
		if (content instanceof Multipart) {
            Part messagePart = ((Multipart) content).getBodyPart(0);
            String contentType = messagePart.getContentType().toUpperCase();
            if (contentType.startsWith("TEXT/PLAIN") || contentType.startsWith("TEXT/HTML")) {
				Scanner scanner = new Scanner(messagePart.getInputStream()).useDelimiter("\\Z");
				String result = scanner.next();
				return result.trim(); // todo replace html
          	}
        }
        } catch(Exception e) {
	      throw new RuntimeException("Error getting content from email "+email,e);
        }
		return null;
	}

	private String getFrom(MimeMessage email) {
        try {
		   return email.getFrom()[0].toString();
	    } catch(javax.mail.internet.AddressException ae) {
           ae.printStackTrace();
           String msg = ae.getMessage();
		   if (msg.contains("``")) {
		       return msg.substring(msg.indexOf("``")+2,msg.indexOf("''"));
		   }
	    }
		return null;
	}
	private String getNameFromHeader(MimeMessage email)
			throws MessagingException {
		// TODO Auto-generated method stub
		Enumeration headers = email.getAllHeaders();
		String Name = null;
		while (headers.hasMoreElements()) {
			Header h = (Header) headers.nextElement();
			if (h.getName().contains("X-From")
					|| h.getName().contains("Sender"))
				Name = h.getValue();
			if (h.getName().contains("From:")) {
				String listName[] = h.getName().split("<");
				if (listName[0].length() > 1)
					Name = listName[0];
			}
		}
		return Name;
	}

	public String getPathForDoc() {
		return PathForDoc;
	}

	public void setPathForDoc(String pathForDoc) {
		PathForDoc = pathForDoc;
	}

}
