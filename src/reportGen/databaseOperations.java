


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
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author ViduraDan
 */
public class databaseOperations {
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
    
    
    public void setItemNumberColumn(JTable table,
            TableColumn sportColumn) {
        //Set up the editor for the sport cells.
        JComboBox itemNumberComboBox = new JComboBox();
        //temNumberComboBox.setSelectedIndex(0);
        sportColumn.setCellEditor(new DefaultCellEditor(itemNumberComboBox));

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reportgen?useSSL=true", "vidura", "vidura");
            Statement st = connection.createStatement();
            String query = "SELECT DISTINCT(PartNumber) FROM parts";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                itemNumberComboBox.addItem(rs.getString("PartNumber"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Set up tool tips for the sport cells.
        DefaultTableCellRenderer renderer
                = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        sportColumn.setCellRenderer(renderer);
    }
    
    
    public void setDowntimeReasonColumn(JTable table,
            TableColumn sportColumn) {
        //Set up the editor for the sport cells.
        JComboBox itemNumberComboBox = new JComboBox();
        //temNumberComboBox.setSelectedIndex(0);
        sportColumn.setCellEditor(new DefaultCellEditor(itemNumberComboBox));

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reportgen?useSSL=true", "vidura", "vidura");
            Statement st = connection.createStatement();
            String query = "SELECT DISTINCT(Reason) FROM downtimes";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                itemNumberComboBox.addItem(rs.getString("Reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Set up tool tips for the sport cells.
        DefaultTableCellRenderer renderer
                = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        sportColumn.setCellRenderer(renderer);
    }
    
    public void prodOEEClone(JTable productionTable, JTable machineWorkedTable, JTable totalDownTable, JTable oeeTable ) {
        try {
            

            int emptyRows = 0;
            rowSearch:
            for (int row = 0; row < productionTable.getRowCount(); row++) { //Iterate through all the rows
                for (int col = 0; col < productionTable.getColumnCount(); col++) { //Iterate through all the columns in the row
                    if (productionTable.getValueAt(row, col) != null) { //Check if the box is empty
                        continue rowSearch; //If the value is not null, the row contains stuff so go onto the next row
                    }
                }
                emptyRows++; //Conditional never evaluated to true so none of the columns in the row contained anything
            }
            
            

            //System.out.println(emptyRows);
            int rows = productionTable.getRowCount();
            //System.out.println(rows);

            int fullRows = rows - emptyRows;

            for (int row = 0; row < fullRows; row++) {
                
                
                
                String SOnumber = (String) productionTable.getValueAt(row, 1);
                //System.out.println(SOnumber);
                String customer = (String) productionTable.getValueAt(row, 2);
                //String shift = shiftCombo.getSelectedItem().toString();
                String partNumber = (String) productionTable.getValueAt(row, 3);
                String qty = (String) productionTable.getValueAt(row, 4);
                int int_gty = Integer.parseInt(qty);
                String pack_qty = (String) productionTable.getValueAt(row, 5);
                int int_pack_qty = Integer.parseInt(pack_qty);

                machineWorkedTable.setValueAt(row + 1, row, 0);
                machineWorkedTable.setValueAt(partNumber, row, 1);
                
                totalDownTable.setValueAt(partNumber, row, 0);
                totalDownTable.setValueAt(0, row, 1);

                oeeTable.setValueAt(partNumber, row, 0);
                oeeTable.setValueAt(SOnumber, row, 1);
                oeeTable.setValueAt(int_gty, row, 4);
                //String PartNumber = (String) productionTable.getValueAt(row, 3);
                
                
                java.sql.PreparedStatement preparedStatement = null;
                String query = "select swo from parts where PartNumber=?";

                preparedStatement = conn.prepareStatement(query);
                //String partNumber = (String) productionTable.getValueAt(row, 3);
                //System.out.println(partNumber);
                preparedStatement.setString(1, partNumber);
                ResultSet rs = preparedStatement.executeQuery();

                String season = null;

                if (rs.next()) {
                    season = rs.getString("swo");
                    System.out.println(season);
                    oeeTable.setValueAt(season, row, 2);
                }
                
                
                
                
                
                
                
                
                
                
                

            }
            //productionTable.getSelectionModel().clearSelection();

            
            //productionTable.setEnabled(false);

            //prodTableUpdate.setEnabled(rootPaneCheckingEnabled);
            JOptionPane.showMessageDialog(null, "Saved Entries in the Database", "Successful!", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        
    }
    
    
    
    
    
}
