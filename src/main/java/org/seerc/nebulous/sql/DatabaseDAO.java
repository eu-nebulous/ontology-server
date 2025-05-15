package org.seerc.nebulous.sql;

public class DatabaseDAO extends Database{
	public static DatabaseDAO instance = null;
	
	private DatabaseDAO(String url, String username, String password) {
		super(url, username, password);
	}
	
	public static DatabaseDAO getInstance(String url, String username, String password)  {
		if(instance == null) 
			instance = new DatabaseDAO(url, username, password);

		return instance;
	}
		
	public static DatabaseDAO getInstance() {	
			return instance;
	}
	
}
