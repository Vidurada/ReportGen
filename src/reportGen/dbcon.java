/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reportGen;

import java.sql.*;

import javax.swing.*;
public class dbcon {
	Connection conn=null;
	public static Connection dbconnector()
	{
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/reportgen?useSSL=true","vidura","vidura");
			return connection;
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,e);
			return null;
		}
	}

}

    
    

