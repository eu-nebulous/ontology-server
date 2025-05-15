package org.seerc.nebulous.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.seerc.nebulous.rest.CreateDataPropertyPostBody;
import org.seerc.nebulous.rest.CreateIndividualPostBody;
import org.seerc.nebulous.rest.CreateObjectPropertyPostBody;

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;

public class Database {
	
	protected Connection conn;
//	
//	String sql = "SELECT uri FROM classes";

	
	public Database(String url, String username, String password) {
		
		Properties props = new Properties();
		props.setProperty("user", username); //postgres
		props.setProperty("password", password); //pass
		try {
			conn = DriverManager.getConnection(url, props); // "jdbc:postgresql://localhost/semantic_models";
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public List<List<String>> getSLAClassAssertions(String slaName) {
		List<List<String>> res = new ArrayList<List<String>>();
		try {
			PreparedStatement s = conn.prepareStatement("SELECT * FROM class_assertions WHERE individual_uri LIKE ?;");
			s.setString(1, "%" + slaName + "%");
			
		
			ResultSet rs = s.executeQuery();
			while(rs.next())
				res.add(List.of(rs.getString(1),  rs.getString(2)));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	public List<List<String>> getSLAObjectPropertyAssertions(String slaName) {
		List<List<String>> res = new ArrayList<List<String>>();
		try {
			PreparedStatement s = conn.prepareStatement("SELECT * FROM object_property_assertions WHERE domain_uri LIKE ? OR range_uri LIKE ?;");;
			s.setString(1, "%" + slaName + "%");
			s.setString(2, "%" + slaName + "%");
		
			ResultSet rs = s.executeQuery();
			while(rs.next())
				res.add(List.of(rs.getString(1),  rs.getString(2), rs.getString(3)));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	public List<List<String>> getSLADataPropertyAssertions(String slaName) {
		List<List<String>> res = new ArrayList<List<String>>();
		try {
			PreparedStatement s = conn.prepareStatement("SELECT * FROM data_property_assertions WHERE individual_uri LIKE ?;");;
			s.setString(1, "%" + slaName + "%");
		
			ResultSet rs = s.executeQuery();
			while(rs.next())
				res.add(List.of(rs.getString(1),  rs.getString(2), rs.getString(3), rs.getString(4)));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	public void createIndividual(CreateIndividualPostBody postBody) {
		try {

			PreparedStatement s = conn.prepareStatement(
					"INSERT INTO individuals (uri) VALUES (?) ON CONFLICT DO NOTHING;" +
					"INSERT INTO class_assertions (individual_uri, class_uri) VALUES (?, ?) ON CONFLICT DO NOTHING;"
			);

			s.setString(1, postBody.getIndividualURI());
			s.setString(2, postBody.getIndividualURI());
			s.setString(3, postBody.getClassURI());
 
			s.execute();
			
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void createObjectPropertyAssertion(CreateObjectPropertyPostBody postBody) {
		try {
			PreparedStatement s = conn.prepareStatement(
					"INSERT INTO object_property_assertions (domain_uri, object_property_uri, range_uri) VALUES (?, ?, ?) ON CONFLICT DO NOTHING;"
			);

			s.setString(1, postBody.getDomainURI());
			s.setString(2, postBody.getObjectPropertyURI());
			s.setString(3, postBody.getRangeURI());

			s.execute();
			
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createDataPropertyAssertion(CreateDataPropertyPostBody postBody) {
		try {
			PreparedStatement s = conn.prepareStatement(
					"INSERT INTO data_property_assertions (individual_uri, data_property_uri, value, datatype) VALUES (?, ?, ?, ?) ON CONFLICT DO NOTHING;"
			);

			s.setString(1, postBody.getDomainURI());
			s.setString(2, postBody.getDataPropertyURI());
			s.setString(3, postBody.getValue());
			s.setString(4, postBody.getType());;

			s.execute();
			
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void createEntities(List<String> entityURIs, String tableName) throws SQLException {
		Statement  s = conn.createStatement();
		
		for(String e : entityURIs)
			s.addBatch("INSERT INTO " + tableName + " (uri) VALUES ('" + e + "') ON CONFLICT DO NOTHING" );
		
		System.out.println(s.executeBatch());

	}
	
}
