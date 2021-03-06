package com.serendio.graphdb.neo4jServer;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

import java.util.HashMap;
import java.util.Map;

public class Neo4jGraphCreation {

	public Driver driver;
	public Session session;

	Neo4jGraphCreation(String password) {
		this.driver = GraphDatabase.driver("bolt://localhost",
				AuthTokens.basic("neo4j", password));
		this.session = driver.session();
	}

	public void closeConnection() {
		this.session.close();
	}

	public void createEmailNode(String Message_ID, String Date,
			long EpochTimestamp, String Subject, String Content,
			String ReplyMessage_ID, String Topic, String Sentiment) {
		System.err.println("Creating Email "+Message_ID+" subject "+Subject);
		String queryString = "MERGE (n:Email {Message_ID: {Message_ID}}) ON CREATE SET n.Date={Date} , n.EpochTimestamp={EpochTimestamp}, n.Subject={Subject} , n.Content={Content} , n.Topic={Topic} , n.Sentiment={Sentiment}";
		if (ReplyMessage_ID != null)
			queryString += ", n:Reply";
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("Message_ID", Message_ID);
		parameters.put("Date", Date);
		parameters.put("EpochTimestamp", EpochTimestamp);
		parameters.put("Topic", Topic);
		parameters.put("Sentiment", Sentiment);
		if (Subject != null) {
			parameters.put("Subject", Subject);
		} else {
			parameters.put("Subject", " ");
		}
		if (Content != null) {
			parameters.put("Content", Content);
		} else {
			parameters.put("Content", " ");
		}
		this.session.run(queryString, parameters);
	}

	public void createUniqueLink(String SourceEmail, String DestinationMail_ID,
			String Direction, String Relation) {
		String query = null;
		if (Relation.equals("RESPONSE")) {
			query = "match (a:Email),(b:Email) Where a.Message_ID={SourceEmail} "
					+ " AND b.Message_ID= {DestinationMail_ID}"
					+ " merge (a)-[r:`"+ Relation + "`]->(b)";
		} else {
			if (Direction.equals("FORWARD"))
				query = "match (a:User),(b:Email) Where a.Email={SourceEmail} "
						+ " AND b.Message_ID={DestinationMail_ID} "
						+ " merge (a)-[r:`"+ Relation + "`]->(b)";
			else if (Direction.equals("BACKWORD"))
				query = "match (a:User),(b:Email) Where a.Email={SourceEmail} "
						+ " AND b.Message_ID= {DestinationMail_ID}"
						+ " merge (a)-[r:`"+ Relation + "`]->(b)";
		}
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("SourceEmail", SourceEmail);
		parameters.put("DestinationMail_ID", DestinationMail_ID);
		parameters.put("Relation", Relation);
		this.session.run(query,parameters);
	}

	public void createUserNode(String Name, String Email) {
		System.err.println("Creating User "+Email);

		String queryString = "MERGE (n:User {Email: {Email}}) ON CREATE SET n.Name = {Name}";
		Map<String, Object> parameters = new HashMap<>();
			parameters.put("Email", Email);
			parameters.put("Name", Name);
		this.session.run(queryString, parameters);
	}
}
