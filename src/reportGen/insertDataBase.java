/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reportGen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author ViduraDan
 */
public class insertDataBase {
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    
    
    public  void insertDatabaseProdPack(JTable table, JComboBox combo){
    try {
            table.getCellEditor().stopCellEditing();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date today = Calendar.getInstance().getTime(); 
            String reportDate = df.format(today);

            int emptyRows = 0;
            rowSearch:
            for (int row = 0; row < table.getRowCount(); row++) { //Iterate through all the rows
                for (int col = 0; col < table.getColumnCount(); col++) { //Iterate through all the columns in the row
                    if (table.getValueAt(row, col) != null) { //Check if the box is empty
                        continue rowSearch; //If the value is not null, the row contains stuff so go onto the next row
                    }
                }
                emptyRows++; //Conditional never evaluated to true so none of the columns in the row contained anything
            }
            
            //System.out.println(emptyRows);

            int rows = table.getRowCount();
            //System.out.println(rows);
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reportgen?useSSL=true", "vidura", "vidura");
            String queryco = "Insert into production(SO_Num,Customer,Date,Shift,PartNumber,prod_qty,pack_qty) values (?,?,?,?,?,?,?);";
            pst = connection.prepareStatement(queryco);
            
            int fullRows= rows-emptyRows;

            for (int row = 0; row < fullRows; row++) {
                String SOnumber = (String) table.getValueAt(row, 1);
                //System.out.println(SOnumber);
                String customer = (String) table.getValueAt(row, 2);
                String shift = combo.getSelectedItem().toString();
                String partNumber = (String) table.getValueAt(row, 3);
                String qty = (String) table.getValueAt(row, 4);
                int int_gty = Integer.parseInt(qty);
                String pack_qty = (String) table.getValueAt(row, 5);
                int int_pack_qty= Integer.parseInt(pack_qty);
                
                pst.setString(1, SOnumber);
                pst.setString(2, customer);
                pst.setString(3, reportDate);
                pst.setString(4, shift);
                pst.setString(5, partNumber);
                pst.setInt(6, int_gty);
                pst.setInt(7, int_pack_qty);

                pst.addBatch();
                
            }
            //productionTable.getSelectionModel().clearSelection();
            
            pst.executeBatch();
            //productionTable.setEnabled(false);
            //prodTableInsert.setEnabled(false);
            //prodTableUpdate.setEnabled(rootPaneCheckingEnabled);
            JOptionPane.showMessageDialog(null, "Saved Entries in the Database", "Successful!", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    
    }
}
