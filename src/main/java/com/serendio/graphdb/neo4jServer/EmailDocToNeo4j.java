package com.serendio.graphdb.neo4jServer;

import com.serendio.dataset.domain.EmailDoc;
import java.io.FileNotFoundException;
import org.apache.http.client.ClientProtocolException;
import java.io.IOException;
import com.serendio.configuration.AppConfigurations;

public class EmailDocToNeo4j {

	public Neo4jGraphCreation graphi;

	public void pushToNeo4j(EmailDoc emailObject) throws FileNotFoundException,
			ClientProtocolException, IOException {
		this.graphi = new Neo4jGraphCreation(AppConfigurations.getPassword());
		processUserNodes(emailObject);
		processEmailNodes(emailObject);
		processLinks(emailObject);
		System.err.println("EmailDoc Pushed to Neo4j");
		graphi.closeConnection();
	}

	public void processLinks(EmailDoc emailObject) {


		if (emailObject.getFrom() != null)
			graphi.createUniqueLink(emailObject.getFrom(),
					emailObject.getMessage_ID(), "FORWARD", "FROM");

		if (emailObject.getTo() != null)
			for (String toAddress : emailObject.getTo().toArray(
					new String[emailObject.getTo().size()])) {

				graphi.createUniqueLink(toAddress, emailObject.getMessage_ID(),
						"BACKWORD", "TO");
			}

		if (emailObject.getCc() != null)
			for (String ccAddress : emailObject.getCc().toArray(
					new String[emailObject.getCc().size()])) {
				graphi.createUniqueLink(ccAddress, emailObject.getMessage_ID(),
						"BACKWORD", "CC");
			}

		if (emailObject.getBcc() != null)
			for (String bccAddress : emailObject.getBcc().toArray(
					new String[emailObject.getBcc().size()])) {
				graphi.createUniqueLink(bccAddress,
						emailObject.getMessage_ID(), "BACKWORD", "BCC");
			}

		if (emailObject.getReplyMessage_ID() != null) {
			graphi.createUniqueLink(emailObject.getReplyMessage_ID(),
					emailObject.getMessage_ID(), "FORWARD", "RESPONSE");
		}
		System.err.println("EmailDoc Links Processed "+emailObject.getMessage_ID());
	}

	public void processEmailNodes(EmailDoc emailObject) {
		graphi.createEmailNode(emailObject.getMessage_ID(),
				emailObject.getDate(), emailObject.getEpochTimeStamp(),
				emailObject.getSubject(), emailObject.getContent(),
				emailObject.getReplyMessage_ID(), emailObject.getTopic(),
				emailObject.getSentiment());
	}

	public void processUserNodes(EmailDoc emailObject) {
		graphi.createUserNode(emailObject.getName(), emailObject.getFrom());

		if (emailObject.getTo() != null)
			for (String toAddress : emailObject.getTo().toArray(
					new String[emailObject.getTo().size()])) {
				graphi.createUserNode(null, toAddress);
			}

		if (emailObject.getCc() != null)
			for (String toAddress : emailObject.getCc().toArray(
					new String[emailObject.getCc().size()])) {
				graphi.createUserNode(null, toAddress);
			}

		if (emailObject.getBcc() != null)
			for (String toAddress : emailObject.getBcc().toArray(
					new String[emailObject.getBcc().size()])) {
				graphi.createUserNode(null, toAddress);
			}
	}
}
