package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Customer { // A common method to connect to the DB
	private Connection connect() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");

			// Provide the correct details: DBServer/DBName, username, password
			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/customer", "root", "1999sana@");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public String insertCustomer(String name, String nic, String address, String birth, String contact) {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for inserting.";
			}
			// create a prepared statement
			String query = " insert into customer (`empID`,`empName`,`empNIC`,`empAddress`,`empDOB`, `empContact`)"
					+ " values (?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			// binding values
			preparedStmt.setInt(1, 0);
			preparedStmt.setString(2, name);
			preparedStmt.setString(3, nic);
			preparedStmt.setString(4, address);
			preparedStmt.setString(5, birth);
			preparedStmt.setString(6, contact);
			// execute the statement
			preparedStmt.execute();
			con.close();
			String newCustomer = readCustomer();
			output = "{\"status\":\"success\", \"data\": \"" + newCustomer + "\"}";
		} catch (Exception e) {
			output = "{\"status\":\"error\", \"data\":\"Error while inserting the employee.\"}";
			System.err.println(e.getMessage());
		}
		return output;
	}

	public String readCustomer() {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for reading.";
			}
			// Prepare the html table to be displayed
			output = "<div class='w-100'><table class='table table-dark table-hover table-bordered'><tr><th>Name</th>"
					+ "<th>NIC</th><th>Address</th>" + "<th>Date of Birth</th>" + "<th>Contact Number</th>"
					+ "<th>Update</th><th>Remove</th></tr></div>";
			String query = "select * from employee";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			// iterate through the rows in the result set
			while (rs.next()) {
				String empID = Integer.toString(rs.getInt("empID"));
				String empName = rs.getString("empName");
				String empNIC = rs.getString("empNIC");
				String empAddress = rs.getString("empAddress");
				String empDOB = rs.getString("empDOB");
				String empContact = rs.getString("empContact");

				// Add into the html table
				output += "<tr><td>" + empName + "</td>";
				output += "<td>" + empNIC + "</td>";
				output += "<td>" + empAddress + "</td>";
				output += "<td>" + empDOB + "</td>";
				output += "<td>" + empContact + "</td>";
				// buttons
				output += "<td><input name='btnUpdate' type='button' value='Update' "
						+ "class='btnUpdate btn btn-primary badge-pill' data-itemid='" + empID + "'></td>"
						+ "<td><input name='btnRemove' type='button' value='Remove' "
						+ "class='btnRemove btn btn-danger badge-pill' data-itemid='" + empID + "'></td></tr>";
			}
			con.close();
			// Complete the html table
			output += "</table>";
		}

		catch (Exception e) {
			output = "Error while reading the employee.";
			System.err.println(e.getMessage());
		}
		return output;
	}

	public String updateEmployee(String empID, String empName, String empNIC, String empAddress, String empDOB,
			String empContact)

	{
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for updating.";
			}
			// create a prepared statement
			String query = "UPDATE customer SET empName=?,empNIC=?,empAddress=?,empDOB=?,empContact=? WHERE empID=?";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			// binding values
			preparedStmt.setString(1, empName);
			preparedStmt.setString(2, empNIC);
			preparedStmt.setString(3, empAddress);
			preparedStmt.setString(4, empDOB);
			preparedStmt.setString(5, empContact);
			preparedStmt.setInt(6, Integer.parseInt(empID));
			// execute the statement
			preparedStmt.execute();
			con.close();
			String newCustomer = readCustomer();
			output = "{\"status\":\"success\", \"data\": \"" + insertCustomer(null, null, null, null, null) + "\"}";
		} catch (Exception e) {
			output = "{\"status\":\"error\", \"data\":\"Error while updating the Customer.\"}";
			System.err.println(e.getMessage());
		}
		return output;
	}

	public String deleteCustomer(String empID) {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for deleting.";
			}
			// create a prepared statement
			String query = "delete from customer where empID=?";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			// binding values
			preparedStmt.setInt(1, Integer.parseInt(empID));
			// execute the statement
			preparedStmt.execute();
			con.close();
			String newCustomer = readCustomer();
			output = "{\"status\":\"success\", \"data\": \"" + insertCustomer(null, null, null, null, null) + "\"}";
		} catch (Exception e) {
			output = "{\"status\":\"error\", \"data\":\"Error while deleting the employee.\"}";
			System.err.println(e.getMessage());
		}
		return output;
	}
}
