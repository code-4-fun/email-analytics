package serendio.graphdb.neo4j;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.cypher.ExecutionResult;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

public class Neo4jGraphCreation {

	public DBConnection connection;
	static Label label = DynamicLabel.label("User");
	public void init(String Path)
	{
		connection = new DBConnection();
		connection.createEmbDb(Path);
	}
	
	public void init()
	{
		String Path = ConstantVariables.getDbPath();
		connection = new DBConnection();
		connection.createEmbDb(Path);
	}
	public void closeDatabase()
	{
		connection.getDbService().shutdown();
	}
	
	public void createEmailNode(String Message_ID, String Date, long EpochTimestamp, String Subject, String Content)
	{
		
		String queryString = "MERGE (n:Email {Message_ID: {Message_ID} , Date: {Date} , EpochTimestamp: {EpochTimestamp}, Subject: {Subject} , Content: {Content}}) RETURN n";
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("Message_ID", Message_ID);
		parameters.put("Date", Date);
		parameters.put("EpochTimestamp", EpochTimestamp);
		if(Subject!=null){
			parameters.put("Subject", Subject);
		}
		else{
			parameters.put("Subject", " ");
		}
		if(Content!=null){
			parameters.put("Content", Content);
		}
		else{
			parameters.put("Content", " ");
		}
		
		connection.getDbService().execute(queryString, parameters).columnAs("n");	
		/* //if(isEmailNodeExist(Message_ID))
			//return;
		
		Node myNode = null;
		try(Transaction tx = connection.getDbService().beginTx())
		{
			myNode = connection.getDbService().createNode(ConstantVariables.NodeLabel.EMAIL);
			myNode.setProperty("Message_ID", Message_ID);
			myNode.setProperty("Date", Date);
			myNode.setProperty("EpochTimestamp", EpochTimestamp);
			if(Subject != null)
				myNode.setProperty("Subject", Subject);
			else
				myNode.setProperty("Subject", "");
			if(Content != null)
				myNode.setProperty("Content", Content);
			else
				myNode.setProperty("Content", "");
			tx.success();
			
		} */
	}
	
	/*private boolean isEmailNodeExist(String Message_ID) 
	{
		boolean exist = false;
		try(Transaction tx = connection.getDbService().beginTx())
		{
			try(ResourceIterator<Node> emailNode = connection.getDbService().findNodes(ConstantVariables.NodeLabel.EMAIL, "Message_ID", Message_ID))
			{
				exist = emailNode.hasNext()?true:false;
			}
	}
		return exist;
	}
	*/
	public void createUniqueLink(String SourceEmail,String DestinationMail_ID, String Direction, String Relation)
	{
		/*Node userNode = getUserNode(SourceEmail);
		Node mailNode = getEmailNode(DestinationMail_ID);
		System.out.println(userNode);
		System.out.println(mailNode);
		try(Transaction tx = connection.getDbService().beginTx())
		{
			Relationship relationship = userNode.createRelationshipTo(mailNode, ConstantVariables.RelationType.FROM);
			relationship.setProperty("MailHeader", Relation);
			tx.success();
		}*/
	//	@SuppressWarnings("deprecation")
	//	ExecutionEngine engine = new ExecutionEngine(connection.getDbService());
		String query = null;
		if(Direction.equals(ConstantVariables.EdgeDirection.FORWARD.toString()))
		  query = "match (a:USER),(b:EMAIL) Where a.Email='"+SourceEmail+"'AND b.Message_ID='"+DestinationMail_ID+"' merge (a)-[r:Link {Relation: '"+Relation+"'}]->(b)";
		else if(Direction.equals(ConstantVariables.EdgeDirection.BACKWORD.toString()))
		  query = "match (a:USER),(b:EMAIL) Where a.Email='"+SourceEmail+"'AND b.Message_ID='"+DestinationMail_ID+"' merge (a)<-[r:Link {Relation: '"+Relation+"'}]-(b)";
		//ExecutionResult result =  engine.execute(query);
		connection.getDbService().execute(query);

	}
	
	public Node getUserNode(String Email)
	{
		try(Transaction tx = connection.getDbService().beginTx())
		{
			try(ResourceIterator<Node> user = connection.getDbService().findNodes(ConstantVariables.NodeLabel.USER, "Email", Email))
			{
				return user.hasNext()?user.next():null;
			}
		}
	}
	
	public Node getEmailNode(String Email_ID)
	{
		try(Transaction tx = connection.getDbService().beginTx())
		{
			try(ResourceIterator<Node> mailNode = connection.getDbService().findNodes(ConstantVariables.NodeLabel.EMAIL, "Message_ID", Email_ID))
			{
				return mailNode.hasNext()?mailNode.next():null;
			}
		}
	}

	public void createUserNode(String Name,String Email)
	{
		
		
		String queryString = "MERGE (n:USER {Email: {Email} , Name: {Name}}) RETURN n";
		Map<String, Object> parameters = new HashMap<>();
		if(Name!=null){
			parameters.put("Name", Name);
		}
		else{
			parameters.put("Name", " ");
		}
		parameters.put("Email", Email);
		connection.getDbService().execute(queryString, parameters).columnAs("n");																																				
		
		/*if(isUserNodeExist(Email))
			return;
		Node myNode = null;
		try(Transaction tx = connection.getDbService().beginTx())
		{
			myNode = connection.getDbService().createNode(ConstantVariables.NodeLabel.USER);
			myNode.setProperty("Email", Email);
			if(Name != null)
			myNode.setProperty("Name", Name);
			System.out.println("Name+Email:"+Name+Email);
			tx.success();
		}*/
	}
	public Node mergeUserNode(String Name, String Email)
	{
		Node result = null;
		ResourceIterator<Node> resultIterator = null;
		try( Transaction tx = connection.getDbService().beginTx())
		{
			String queryString = "MERGE (n:User {Email: {Email}}) RETURN n";
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("Email", Email);
			resultIterator = connection.getDbService().execute(queryString, parameters).columnAs("n");
			result = resultIterator.next();
			tx.success();
			return result;
		}
	}
	/*public boolean isUserNodeExist(String Email)
	{
		boolean exist = false;
		try(Transaction tx = connection.getDbService().beginTx())
		{
			try(ResourceIterator<Node> user = connection.getDbService().findNodes(ConstantVariables.NodeLabel.USER, "Email", Email))
			{
				exist = user.hasNext()?true:false;
			}
		}
		return exist;
	} */
} 