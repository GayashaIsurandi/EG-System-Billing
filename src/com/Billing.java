package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;


public class Billing {
	
	private Connection connect() {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Provide the correct details: DBServer/DBName, username, password
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/egelectrio?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public String insertBilling(String bAccountNo, String bUnits, String bUnitPrice, String bAmount, String bRemark)  
	{   
		String output = ""; 	 
		try   
		{    
			Connection con = connect(); 
	 
			if (con == null)    
			{return "Error while connecting to the database for inserting."; } 
	 
			// create a prepared statement 
			String query = " insert into bill(`billID`,`bAccountNo`,`bUnits`,`bUnitPrice`,`bAmount`,`bRemark`)" + " values (?, ?, ?, ?, ?, ?)"; 
			PreparedStatement preparedStmt = con.prepareStatement(query); 
	 
			// binding values    
			 preparedStmt.setInt(1, 0);
			 preparedStmt.setString(2, bAccountNo);
			 preparedStmt.setString(3, bUnits);
			 preparedStmt.setString(4, bUnitPrice);
			 preparedStmt.setString(5, bAmount);
			 preparedStmt.setString(6, bRemark);
			
			// execute the statement    
			preparedStmt.execute();    
			con.close(); 
	   
			String newBilling = readBilling(); 
			output =  "{\"status\":\"success\", \"data\": \"" + newBilling + "\"}";    
		}   
		catch (Exception e)   
		{    
			output =  "{\"status\":\"error\", \"data\": \"Error while inserting the billing.\"}";  
			System.err.println(e.getMessage());   
		} 		
	  return output;  
	} 	
	
	public String readBilling()  
	{   
		String output = ""; 
		try   
		{    
			Connection con = connect(); 
		
			if (con == null)    
			{
				return "Error while connecting to the database for reading."; 
			} 
	 
			// Prepare the html table to be displayed    
			output = "<table border=\'1\'><tr><th>Account No</th><th>Unit</th><th>Unit Price</th><th>Amount</th><th>Remark</th><th>Update</th><th>Remove</th></tr>";
	 
			String query = "select * from bill";    
			Statement stmt = (Statement) con.createStatement();
			ResultSet rs = ((java.sql.Statement) stmt).executeQuery(query);
	 
			// iterate through the rows in the result set    
			while (rs.next())    
			{     
				 String billID = Integer.toString(rs.getInt("billID"));
				 String bAccountNo = rs.getString("bAccountNo");
				 String bUnits = rs.getString("bUnits");
				 String bUnitPrice = rs.getString("bUnitPrice");
				 String bAmount = rs.getString("bAmount");
				 String bRemark = rs.getString("bRemark");
				 
				// Add into the html table 
				output += "<tr><td><input id=\'hidBillingIDUpdate\' name=\'hidBillingIDUpdate\' type=\'hidden\' value=\'" + billID + "'>" 
							+ bAccountNo + "</td>"; 
				output += "<td>" + bUnits + "</td>";
				output += "<td>" + bUnitPrice + "</td>";
				output += "<td>" + bAmount + "</td>";
				output += "<td>" + bRemark + "</td>";
	 
				// buttons     
				output +="<td><input name='btnUpdate' type='button' value='Update' class='btnUpdate btn btn-secondary'></td>"       
						+ "<td><input name='btnRemove' type='button' value='Remove' class='btnRemove btn btn-danger' data-billid='" + billID + "'>" + "</td></tr>"; 
			
			}
			con.close(); 
	   
			output += "</table>";   
		}   
		catch (Exception e)   
		{    
			output = "Error while reading the billing.";    
			System.err.println(e.getMessage());   
		} 	 
		return output;  
	}
	
	public String updateBilling(String billID, String bAccountNo, String bUnits, String bUnitPrice, String bAmount, String bRemark)  
	{   
		String output = "";  
		try   
		{    
			Connection con = connect(); 
	 
			if (con == null)    
			{return "Error while connecting to the database for updating."; } 
	 
			// create a prepared statement    
			String query = "UPDATE bill SET bAccountNo=?,bUnits=?,bUnitPrice=?,bAmount=?,bRemark=?"  + "WHERE billID=?";  	 
			PreparedStatement preparedStmt = con.prepareStatement(query); 
	 
			// binding values    
			preparedStmt.setString(1, bAccountNo);
			 preparedStmt.setString(2, bUnits);
			 preparedStmt.setString(3, bUnitPrice);
			 preparedStmt.setString(4, bAmount);
			 preparedStmt.setString(5, bRemark);
			 preparedStmt.setInt(6, Integer.parseInt(billID)); 
	 
			// execute the statement    
			preparedStmt.execute();    
			con.close();  
			String newBilling = readBilling();    
			output = "{\"status\":\"success\", \"data\": \"" + newBilling + "\"}";    
		}   
		catch (Exception e)   
		{    
			output =  "{\"status\":\"error\", \"data\": \"Error while updating the billing.\"}";   
			System.err.println(e.getMessage());   
		} 	 
	  return output;  
	} 
	
	public String deleteBilling(String billID)   
	{   
		String output = ""; 
	 
		try   
		{    
			Connection con = connect(); 
	 
			if (con == null)    
			{
				return "Error while connecting to the database for deleting."; 			
			} 
	 
			// create a prepared statement    
			String query = "delete from bill where billID=?"; 
			PreparedStatement preparedStmt = con.prepareStatement(query); 
	 
			// binding values    
			preparedStmt.setInt(1, Integer.parseInt(billID)); 
	 
			// execute the statement    
			preparedStmt.execute();    
			con.close(); 
	 
			String newBilling = readBilling();    
			output = "{\"status\":\"success\", \"data\": \"" +  newBilling + "\"}";    
		}   
		catch (Exception e)   
		{    
			output = "Error while deleting the billing.";    
			System.err.println(e.getMessage());   
		} 
	 
		return output;  
	}
	
}
