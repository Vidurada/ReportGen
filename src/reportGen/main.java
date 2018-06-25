package reportGen;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ViduraDan
 */
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.DriverManager;
import java.sql.Connection;
import static java.sql.JDBCType.NULL;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.*;
import net.proteanit.sql.DbUtils;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.Date;
import java.text.*;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import static java.lang.Math.toIntExact;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import static reportGen.rowOperations.addColumn;
import static reportGen.rowOperations.addRowbottomOee;
import static reportGen.rowOperations.removeRowAbove;
import static reportGen.rowOperations.removeRowBelow;
import static reportGen.rowOperations.removeThisRow;
import static reportGen.rowOperations.setRowNumber;
import static reportGen.rowOperations.setRowNumber2;
import static reportGen.rowOperations.setRowNumberMachineRunTime;
import static reportGen.rowOperations.tableEmpty;

public class main extends javax.swing.JFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    /**
     * Creates new form main
     */
    public main() {
        initComponents();
        conn = dbcon.dbconnector();
        databaseOperations cellFill = new databaseOperations();
        cellFill.setItemNumberColumn(productionTable, productionTable.getColumnModel().getColumn(3));
        cellFill.setDowntimeReasonColumn(machineDowntimeTable, machineDowntimeTable.getColumnModel().getColumn(5));;
        setBaseColumn(rejectAnalysisTable, rejectAnalysisTable.getColumnModel().getColumn(0));
        hidPanel.setVisible(false);
        totalDownTable.setVisible(true);
        downTimeColumn(machineDowntimeTable, machineDowntimeTable.getColumnModel().getColumn(0));
        productionTable.setRowSelectionAllowed(true);
        jDateChooser1.setDateFormatString("yyyy-MM-dd");

    }

    public final void setPackPackingTime() {

        int fullRows = filledRows(packageTimeTable);
        for (int row = 0; row < fullRows; row++) {
            packageTimeTable.setValueAt(false, row, 5);
            packageTimeTable.setValueAt(false, row, 6);
            packageTimeTable.setValueAt(false, row, 7);
        }
    }

    public void productionPreDatabaseInsert() {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date today = Calendar.getInstance().getTime();
            String reportDate = df.format(today);
            String shift = shiftCombo.getSelectedItem().toString();
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reportgen?useSSL=true", "vidura", "vidura");
            String queryco = "DELETE FROM production WHERE InsertDate = ? AND Shift = ?";
            pst = connection.prepareStatement(queryco);
            pst.setString(1, reportDate);
            pst.setString(2, shift);
            pst.execute();

        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void prodOEEPreDatabaseInsert() {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date today = Calendar.getInstance().getTime();
            String reportDate = df.format(today);
            String shift = shiftCombo.getSelectedItem().toString();
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reportgen?useSSL=true", "vidura", "vidura");
            String queryco = "DELETE FROM prodOEE WHERE InsertDate = ? AND Shift = ?";
            pst = connection.prepareStatement(queryco);
            pst.setString(1, reportDate);
            pst.setString(2, shift);
            pst.execute();

        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void packOEEPreDatabaseInsert() {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date today = Calendar.getInstance().getTime();
            String reportDate = df.format(today);
            String shift = shiftCombo.getSelectedItem().toString();
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reportgen?useSSL=true", "vidura", "vidura");
            String queryco = "DELETE FROM packOEE WHERE InsertDate = ? AND Shift = ?";
            pst = connection.prepareStatement(queryco);
            pst.setString(1, reportDate);
            pst.setString(2, shift);
            pst.execute();

        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    

    public void machineDownPreDatabaseInsert() {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date today = Calendar.getInstance().getTime();
            String reportDate = df.format(today);
            String shift = shiftCombo.getSelectedItem().toString();
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reportgen?useSSL=true", "vidura", "vidura");
            String queryco = "DELETE FROM productiondowntime WHERE InsertDate = ? AND Shift = ?";
            pst = connection.prepareStatement(queryco);
            pst.setString(1, reportDate);
            pst.setString(2, shift);
            pst.execute();

        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void rejectPreDatabaseInsert() {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date today = Calendar.getInstance().getTime();
            String reportDate = df.format(today);
            String shift = shiftCombo.getSelectedItem().toString();
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reportgen?useSSL=true", "vidura", "vidura");
            String queryco = "DELETE FROM reject WHERE InsertDate = ?  AND Shift = ? ";
            pst = connection.prepareStatement(queryco);
            pst.setString(1, reportDate);
            pst.setString(2, shift);
            pst.execute();

        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void machineTimePreDatabaseInsert() {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date today = Calendar.getInstance().getTime();
            String reportDate = df.format(today);
            String shift = shiftCombo.getSelectedItem().toString();
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reportgen?useSSL=true", "vidura", "vidura");
            String queryco = "DELETE FROM productionmachinetime WHERE InsertDate = ?  AND Shift = ? ";
            pst = connection.prepareStatement(queryco);
            pst.setString(1, reportDate);
            pst.setString(2, shift);
            pst.execute();

        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void productionDatabaseInsert() {
        try {

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date today = Calendar.getInstance().getTime();
            String dataInsertedDay = df.format(today);

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
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reportgen?useSSL=true", "vidura", "vidura");
            int rows = productionTable.getRowCount();
            String queryco = "Insert IGNORE into production(Prodid,SO_Num,Customer,InsertDate,ReportDate,Shift,PartNumber,prod_qty,pack_qty,CartNew,CartUsed,Bags,Supervisor,Technician) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
            pst = connection.prepareStatement(queryco);

            int fullRows = rows - emptyRows;

            for (int row = 0; row < fullRows; row++) {

                if (productionTable.getValueAt(row, 3) != null) {
                    String partNumber = (String) productionTable.getValueAt(row, 3);
                    //proceed if the cell is not empty
                    if (!partNumber.isEmpty()) {

                        String SOnumber = (String) productionTable.getValueAt(row, 1);

                        String customer = (String) productionTable.getValueAt(row, 2);
                        String shift = shiftCombo.getSelectedItem().toString();

                        float int_qty = 0;

                        if (productionTable.getValueAt(row, 4) == null) {

                            int_qty = 0;
                        } else if (productionTable.getValueAt(row, 4).toString().isEmpty()) {
                            int_qty = 0;

                        } else {
                            String prod_qty = (String) productionTable.getValueAt(row, 4);
                            int_qty = Float.parseFloat(prod_qty);
                        }

                        float int_pack_qty = 0;
                        if (productionTable.getValueAt(row, 5) == null) {

                            int_pack_qty = 0;
                        } else if (productionTable.getValueAt(row, 5).toString().isEmpty()) {
                            int_pack_qty = 0;

                        } else {
                            String pack_qty = (String) productionTable.getValueAt(row, 5);
                            int_pack_qty = Float.parseFloat(pack_qty);
                        }

                        Date reportDate = jDateChooser1.getDate();
                        String reportDateString = df.format(reportDate);
                        String sup = supervisor.getText();
                        String tech = technician.getText();
                        String id = partNumber + "-" + reportDateString + "-" + shift;

                        String new_car = (String) productionTable.getValueAt(row, 6);
                        String used_car = (String) productionTable.getValueAt(row, 7);
                        String bags = (String) productionTable.getValueAt(row, 8);

                        pst.setString(1, id);
                        pst.setString(2, SOnumber);
                        pst.setString(3, customer);
                        pst.setString(4, dataInsertedDay);
                        pst.setString(5, reportDateString);
                        pst.setString(6, shift);
                        pst.setString(7, partNumber);
                        pst.setFloat(8, int_qty);
                        pst.setFloat(9, int_pack_qty);
                        pst.setString(10, new_car);
                        pst.setString(11, used_car);
                        pst.setString(12, bags);
                        pst.setString(13, sup);
                        pst.setString(14, tech);

                        pst.addBatch();

                    }
                    //productionTable.getSelectionModel().clearSelection();

                    pst.executeBatch();
                    //productionTable.setEnabled(false);
                    //prodTableInsert.setEnabled(false);
                    //prodTableUpdate.setEnabled(rootPaneCheckingEnabled);

                }
            }
            JOptionPane.showMessageDialog(null, "Saved Entries in the Database", "Successful!", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }

    private void rejectAnalysisDatabaseInsert() {
        try {

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date today = Calendar.getInstance().getTime();
            String InsertDate = df.format(today);

            int emptyRows = 0;
            rowSearch:
            for (int row = 0; row < rejectAnalysisTable.getRowCount(); row++) { //Iterate through all the rows
                for (int col = 0; col < rejectAnalysisTable.getColumnCount(); col++) { //Iterate through all the columns in the row
                    if (rejectAnalysisTable.getValueAt(row, col) != null) { //Check if the box is empty
                        continue rowSearch; //If the value is not null, the row contains stuff so go onto the next row
                    }
                }
                emptyRows++; //Conditional never evaluated to true so none of the columns in the row contained anything
            }
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reportgen?useSSL=true", "vidura", "vidura");
            int rows = rejectAnalysisTable.getRowCount();
            String queryco = "Insert IGNORE into reject(Id,InsertDate,ReportDate,Shift,Base,PartNumber,value,Supervisor,Technician) values (?,?,?,?,?,?,?,?,?);";
            pst = connection.prepareStatement(queryco);

            int fullRows = rows - emptyRows;
            int cols = rejectAnalysisTable.getColumnCount();

            for (int row = 0; row < fullRows; row++) {
                for (int col = 1; col < cols - 1; col++) {

                    if (rejectAnalysisTable.getValueAt(row, col) != null) {
                        //float value =  (float) rejectAnalysisTable.getValueAt(row,col);
                        String val = rejectAnalysisTable.getValueAt(row, col).toString();
                        if (!val.isEmpty()) {

                            String base = (String) rejectAnalysisTable.getValueAt(row, 0);
                            String partNumber = (String) productionTable.getValueAt(col - 1, 3);
                            String amount = rejectAnalysisTable.getValueAt(row, col).toString();
                            String shift = shiftCombo.getSelectedItem().toString();
                            Date reportDate = jDateChooser1.getDate();
                            String reportDateString = df.format(reportDate);
                            String sup = supervisor.getText();
                            String tech = technician.getText();
                            String id = partNumber + "-" + base + "-" + shift;

                            pst.setString(1, id);
                            pst.setString(2, InsertDate);
                            pst.setString(3, reportDateString);
                            pst.setString(4, shift);
                            pst.setString(5, base);
                            pst.setString(6, partNumber);
                            pst.setString(7, amount);
                            pst.setString(8, sup);
                            pst.setString(9, tech);

                            pst.addBatch();
                        }
                    }
                }

            }
            //productionTable.getSelectionModel().clearSelection();

            pst.executeBatch();
            //productionTable.setEnabled(false);
            //prodTableInsert.setEnabled(false);
            //prodTableUpdate.setEnabled(rootPaneCheckingEnabled);
            JOptionPane.showMessageDialog(null, "Saved Entries in the Database", "Successful!", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }
    
     private void prodOEEDatabaseInsert() {
        try {

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date today = Calendar.getInstance().getTime();
            String InsertDate = df.format(today);

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reportgen?useSSL=true", "vidura", "vidura");
            int rows = productionOeeTable.getRowCount();
            String queryco = "INSERT IGNORE into prodoee (Id,SOnumber,SWO,Reject,Output,aTime,pdTime,dTime,oTime,irRate,aRate,pRate,qRate,Oee,reportDate,insertDate,Shift,Supervisor,Technician) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
            pst = connection.prepareStatement(queryco);

            
            int cols = productionOeeTable.getColumnCount();

            for (int row = 0; row < rows-1; row++) {
                for (int col = 1; col < cols; col++) {

                    if (productionOeeTable.getValueAt(row, col) != null) {
                        //float value =  (float) rejectAnalysisTable.getValueAt(row,col);
                        String val = productionOeeTable.getValueAt(row, col).toString();
                        if (!val.isEmpty()) {

                            String Item = (String) productionOeeTable.getValueAt(row, 1).toString();
                            String SONumber = (String) productionOeeTable.getValueAt(row, 2).toString();
                            String SWO = productionOeeTable.getValueAt(row, 3).toString();
                            String reject = (String) productionOeeTable.getValueAt(row, 4).toString();
                            String output = (String) productionOeeTable.getValueAt(row, 5).toString();
                            String a_time = productionOeeTable.getValueAt(row, 6).toString();
                            String pd_time = (String) productionOeeTable.getValueAt(row, 7).toString();
                            String d_time = (String) productionOeeTable.getValueAt(row, 8).toString();
                            String o_time = productionOeeTable.getValueAt(row, 9).toString();
                            String ir_rate = (String) productionOeeTable.getValueAt(row, 10).toString();
                            String a_rate = (String) productionOeeTable.getValueAt(row, 11).toString();
                            String p_rate = productionOeeTable.getValueAt(row, 12).toString();
                            String q_rate = (String) productionOeeTable.getValueAt(row, 13).toString();
                            String oee = (String) productionOeeTable.getValueAt(row, 14).toString();
                            
                            
                            
                            String shift = shiftCombo.getSelectedItem().toString();
                            Date reportDate = jDateChooser1.getDate();
                            String reportDateString = df.format(reportDate);
                            String sup = supervisor.getText();
                            String tech = technician.getText();
                            
                            String id = Item + "-" + reportDateString + "-" + shift;

                            pst.setString(1, id);
                            pst.setString(2, SONumber);
                            pst.setString(3, SWO);
                            pst.setString(4, reject);
                            pst.setString(5, output);
                            pst.setString(6, a_time);
                            pst.setString(7, pd_time);
                            pst.setString(8, d_time);
                            pst.setString(9, o_time);
                            pst.setString(10, ir_rate);
                            pst.setString(11, a_rate);
                            pst.setString(12, p_rate);
                            pst.setString(13, q_rate);
                            pst.setString(14, oee);
                            pst.setString(15, reportDateString);
                            pst.setString(16, InsertDate);
                            pst.setString(17, shift);
                            pst.setString(18, sup);
                            pst.setString(19, tech);
                            

                            pst.addBatch();
                        }
                    }
                }

            }
            //productionTable.getSelectionModel().clearSelection();

            pst.executeBatch();
            //productionTable.setEnabled(false);
            //prodTableInsert.setEnabled(false);
            //prodTableUpdate.setEnabled(rootPaneCheckingEnabled);
            JOptionPane.showMessageDialog(null, "Saved Entries in the Database", "Successful!", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }
     
     private void packOEEDatabaseInsert() {
        try {

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date today = Calendar.getInstance().getTime();
            String InsertDate = df.format(today);

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reportgen?useSSL=true", "vidura", "vidura");
            int rows = packingOeeTable.getRowCount();
            String queryco = "INSERT IGNORE into packoee (Id,CutLength,SPO,Reject,Output,aTime,pdTime,dTime,oTime,irRate,aRate,pRate,qRate,Oee,reportDate,insertDate,Shift,Supervisor,Technician) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
            pst = connection.prepareStatement(queryco);

            
            int cols = packingOeeTable.getColumnCount();

            for (int row = 0; row < rows-1; row++) {
                for (int col = 1; col < cols; col++) {

                    if (packingOeeTable.getValueAt(row, col) != null) {
                        //float value =  (float) rejectAnalysisTable.getValueAt(row,col);
                        String val = packingOeeTable.getValueAt(row, col).toString();
                        if (!val.isEmpty()) {

                            String Item = (String) packingOeeTable.getValueAt(row, 1).toString();
                            String cutLength = (String) packingOeeTable.getValueAt(row, 2).toString();
                            String SPO = packingOeeTable.getValueAt(row, 3).toString();
                            String reject = (String) packingOeeTable.getValueAt(row, 4).toString();
                            String output = (String) packingOeeTable.getValueAt(row, 5).toString();
                            String a_time = packingOeeTable.getValueAt(row, 6).toString();
                            String pd_time = (String) packingOeeTable.getValueAt(row, 7).toString();
                            String d_time = (String)packingOeeTable.getValueAt(row, 8).toString();
                            String o_time = packingOeeTable.getValueAt(row, 9).toString();
                            String ir_rate = (String) packingOeeTable.getValueAt(row, 10).toString();
                            String a_rate = (String) packingOeeTable.getValueAt(row, 11).toString();
                            String p_rate = packingOeeTable.getValueAt(row, 12).toString();
                            String q_rate = (String) packingOeeTable.getValueAt(row, 13).toString();
                            String oee = (String) packingOeeTable.getValueAt(row, 14).toString();
                            
                            
                            
                            String shift = shiftCombo.getSelectedItem().toString();
                            Date reportDate = jDateChooser1.getDate();
                            String reportDateString = df.format(reportDate);
                            String sup = supervisor.getText();
                            String tech = technician.getText();
                            
                            String id = Item + "-" + reportDateString + "-" + shift;

                            pst.setString(1, id);
                            pst.setString(2, cutLength);
                            pst.setString(3, SPO);
                            pst.setString(4, reject);
                            pst.setString(5, output);
                            pst.setString(6, a_time);
                            pst.setString(7, pd_time);
                            pst.setString(8, d_time);
                            pst.setString(9, o_time);
                            pst.setString(10, ir_rate);
                            pst.setString(11, a_rate);
                            pst.setString(12, p_rate);
                            pst.setString(13, q_rate);
                            pst.setString(14, oee);
                            pst.setString(15, reportDateString);
                            pst.setString(16, InsertDate);
                            pst.setString(17, shift);
                            pst.setString(18, sup);
                            pst.setString(19, tech);
                            

                            pst.addBatch();
                        }
                    }
                }

            }
            //productionTable.getSelectionModel().clearSelection();

            pst.executeBatch();
            //productionTable.setEnabled(false);
            //prodTableInsert.setEnabled(false);
            //prodTableUpdate.setEnabled(rootPaneCheckingEnabled);
            JOptionPane.showMessageDialog(null, "Saved Entries in the Database", "Successful!", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }
    
    
    
    
    
    

    private void rejectAnalysisTotal() {

        int fullRows = filledRows(rejectAnalysisTable);

        int last_row = rejectAnalysisTable.getColumnCount();
        for (int i = 0; i < fullRows; i++) {
            float tot = rowSum(rejectAnalysisTable, 1, i);
            rejectAnalysisTable.setValueAt(tot, i, last_row - 1);

        }

    }

    private float rowSum(JTable table, int start, int n) {
        int colsCount = table.getColumnCount();

        float sum = 0;
        for (int i = start; i < colsCount - 1; i++) {
            Object value = table.getValueAt(n, i);
            //float qty = (float) table.getValueAt(i, n);

            //String st_qty = (String) table.getValueAt(n, i);
            if (value != null) {
                // if (!st_qty.isEmpty()) {
                sum = sum + Float.parseFloat(table.getValueAt(n, i).toString());
                // }
            }
        }
        return sum;

    }

    private void machineRuntimeDatabaseInstert() {

        try {

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date today = Calendar.getInstance().getTime();
            String InsertDate = df.format(today);

            int emptyRows = 0;
            rowSearch:
            for (int row = 0; row < machineWorkedTable.getRowCount(); row++) { //Iterate through all the rows
                for (int col = 0; col < machineWorkedTable.getColumnCount(); col++) { //Iterate through all the columns in the row
                    if (machineWorkedTable.getValueAt(row, col) != null) { //Check if the box is empty
                        continue rowSearch; //If the value is not null, the row contains stuff so go onto the next row
                    }
                }
                emptyRows++; //Conditional never evaluated to true so none of the columns in the row contained anything
            }
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reportgen?useSSL=true", "vidura", "vidura");
            int rows = machineWorkedTable.getRowCount();
            String queryco = "Insert IGNORE into productionmachinetime(Id,PartNumber,Shift,InsertDate,ReportDate,TimeFrom,TimeTo,TotalTime,Supervisor,Technician) values (?,?,?,?,?,?,?,?,?,?);";
            pst = connection.prepareStatement(queryco);

            int fullRows = rows - emptyRows;

            for (int row = 0; row < fullRows; row++) {

                if (machineWorkedTable.getValueAt(row, 1) != null) {
                    //float value =  (float) rejectAnalysisTable.getValueAt(row,col);
                    String val = machineWorkedTable.getValueAt(row, 1).toString();
                    if (!val.isEmpty()) {

                        String partNumber = (String) machineWorkedTable.getValueAt(row, 1);
                        String from = (String) machineWorkedTable.getValueAt(row, 2);
                        String to = machineWorkedTable.getValueAt(row, 3).toString();
                        String total = machineWorkedTable.getValueAt(row, 4).toString();
                        String shift = shiftCombo.getSelectedItem().toString();
                        Date reportDate = jDateChooser1.getDate();
                        String reportDateString = df.format(reportDate);
                        String sup = supervisor.getText();
                        String tech = technician.getText();
                        String id = partNumber + "-" + reportDateString + "-" + shift;

                        pst.setString(1, id);
                        pst.setString(2, partNumber);
                        pst.setString(3, shift);
                        pst.setString(4, InsertDate);
                        pst.setString(5, reportDateString);
                        pst.setString(6, from);
                        pst.setString(7, to);
                        pst.setString(8, total);
                        pst.setString(9, sup);
                        pst.setString(10, tech);

                        pst.addBatch();
                    }
                }

            }
            //productionTable.getSelectionModel().clearSelection();

            pst.executeBatch();
            //productionTable.setEnabled(false);
            //prodTableInsert.setEnabled(false);
            //prodTableUpdate.setEnabled(rootPaneCheckingEnabled);
            JOptionPane.showMessageDialog(null, "Saved Entries in the Database", "Successful!", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }

    private void machineDowntimeDatabaseInstert() {

        try {

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date today = Calendar.getInstance().getTime();
            String InsertDate = df.format(today);

            int emptyRows = 0;
            rowSearch:
            for (int row = 0; row < machineDowntimeTable.getRowCount(); row++) { //Iterate through all the rows
                for (int col = 0; col < machineDowntimeTable.getColumnCount(); col++) { //Iterate through all the columns in the row
                    if (machineDowntimeTable.getValueAt(row, col) != null) { //Check if the box is empty
                        continue rowSearch; //If the value is not null, the row contains stuff so go onto the next row
                    }
                }
                emptyRows++; //Conditional never evaluated to true so none of the columns in the row contained anything
            }
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reportgen?useSSL=true", "vidura", "vidura");
            int rows = machineDowntimeTable.getRowCount();
            String queryco = "Insert IGNORE into productiondowntime(Status,Attribute,TimeFrom,TimeTo,TotalTime,Reason,Comment,InsertDate,ReportDate,Shift,Supervisor,Technician) values (?,?,?,?,?,?,?,?,?,?,?,?);";
            pst = connection.prepareStatement(queryco);

            int fullRows = rows - emptyRows;

            for (int row = 0; row < fullRows; row++) {

                if (machineDowntimeTable.getValueAt(row, 1) != null) {
                    //float value =  (float) rejectAnalysisTable.getValueAt(row,col);
                    String val = machineDowntimeTable.getValueAt(row, 1).toString();
                    if (!val.isEmpty()) {

                        String status = (String) machineDowntimeTable.getValueAt(row, 0);
                        String attribute2 = (String) machineDowntimeTable.getValueAt(row, 1);
                        String[] splitStr = attribute2.split("\\s+");
                        String pno = splitStr[1];
                        int p_no = Integer.parseInt(pno);
                        String partNumber = productionTable.getValueAt(p_no - 1, 3).toString();

                        String from = machineDowntimeTable.getValueAt(row, 2).toString();
                        String to = machineDowntimeTable.getValueAt(row, 3).toString();
                        int total = (int) machineDowntimeTable.getValueAt(row, 4);
                        String reason = machineDowntimeTable.getValueAt(row, 5).toString();
                        Object value = machineDowntimeTable.getValueAt(row, 6);
                        String comment = null;
                        if (value == null) {
                            comment = "";
                        } else {
                            comment = machineDowntimeTable.getValueAt(row, 6).toString();
                        }
                        Date reportDate = jDateChooser1.getDate();
                        String reportDateString = df.format(reportDate);
                        String sup = supervisor.getText();
                        String tech = technician.getText();

                        String shift = shiftCombo.getSelectedItem().toString();

                        pst.setString(1, status);
                        pst.setString(2, partNumber);
                        pst.setString(3, from);
                        pst.setString(4, to);
                        pst.setInt(5, total);
                        pst.setString(6, reason);
                        pst.setString(7, comment);
                        pst.setString(8, InsertDate);
                        pst.setString(9, reportDateString);
                        pst.setString(10, shift);
                        pst.setString(11, sup);
                        pst.setString(12, tech);

                        pst.addBatch();
                    }
                }

            }
            //productionTable.getSelectionModel().clearSelection();

            pst.executeBatch();
            //productionTable.setEnabled(false);
            //prodTableInsert.setEnabled(false);
            //prodTableUpdate.setEnabled(rootPaneCheckingEnabled);
            JOptionPane.showMessageDialog(null, "Saved Entries in the Database", "Successful!", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }

    private void packtimeDatabaseInstert() {

        try {

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date today = Calendar.getInstance().getTime();
            String InsertDate = df.format(today);

            int emptyRows = 0;
            rowSearch:
            for (int row = 0; row < packageTimeTable.getRowCount(); row++) { //Iterate through all the rows
                for (int col = 0; col < packageTimeTable.getColumnCount(); col++) { //Iterate through all the columns in the row
                    if (packageTimeTable.getValueAt(row, col) != null) { //Check if the box is empty
                        continue rowSearch; //If the value is not null, the row contains stuff so go onto the next row
                    }
                }
                emptyRows++; //Conditional never evaluated to true so none of the columns in the row contained anything
            }
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reportgen?useSSL=true", "vidura", "vidura");
            int rows = packageTimeTable.getRowCount();
            String queryco = "Insert IGNORE into productiondowntime(Status,Attribute,TimeFrom,TimeTo,TotalTime,Reason,Comment,InsertDate,ReportDate,Shift,Supervisor,Technician) values (?,?,?,?,?,?,?,?,?,?,?,?);";
            pst = connection.prepareStatement(queryco);

            int fullRows = rows - emptyRows;

            for (int row = 0; row < fullRows; row++) {

                if (packageTimeTable.getValueAt(row, 1) != null) {
                    //float value =  (float) rejectAnalysisTable.getValueAt(row,col);
                    String val = packageTimeTable.getValueAt(row, 1).toString();
                    if (!val.isEmpty()) {

                        String status = (String) machineDowntimeTable.getValueAt(row, 0);
                        String attribute2 = (String) machineDowntimeTable.getValueAt(row, 1);
                        String[] splitStr = attribute2.split("\\s+");
                        String pno = splitStr[1];
                        int p_no = Integer.parseInt(pno);
                        String partNumber = productionTable.getValueAt(p_no - 1, 3).toString();

                        String from = machineDowntimeTable.getValueAt(row, 2).toString();
                        String to = machineDowntimeTable.getValueAt(row, 3).toString();
                        int total = (int) machineDowntimeTable.getValueAt(row, 4);
                        String reason = machineDowntimeTable.getValueAt(row, 5).toString();
                        String comment = machineDowntimeTable.getValueAt(row, 6).toString();
                        Date reportDate = jDateChooser1.getDate();
                        String reportDateString = df.format(reportDate);
                        String sup = supervisor.getText();
                        String tech = technician.getText();

                        String shift = shiftCombo.getSelectedItem().toString();

                        pst.setString(1, status);
                        pst.setString(2, partNumber);
                        pst.setString(3, from);
                        pst.setString(4, to);
                        pst.setInt(5, total);
                        pst.setString(6, reason);
                        pst.setString(7, comment);
                        pst.setString(8, InsertDate);
                        pst.setString(9, reportDateString);
                        pst.setString(10, shift);
                        pst.setString(11, sup);
                        pst.setString(12, tech);

                        pst.addBatch();
                    }
                }

            }
            //productionTable.getSelectionModel().clearSelection();

            pst.executeBatch();
            //productionTable.setEnabled(false);
            //prodTableInsert.setEnabled(false);
            //prodTableUpdate.setEnabled(rootPaneCheckingEnabled);
            JOptionPane.showMessageDialog(null, "Saved Entries in the Database", "Successful!", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }

    private void packOEEReject() {

        int rows = rejectAnalysisTable.getRowCount();
        int cols = rejectAnalysisTable.getColumnCount();
        int fuCols = filledCols(rejectAnalysisTable);
        //iterete through reject analysis table
        for (int i = 0; i < rows - 1; i++) {
            //get the base 
            String base = (String) rejectAnalysisTable.getValueAt(i, 0);
            //check whether the base is cutter wastage
            if ("Cutter wastage".equals(base)) {
                //iterate through colums to get values

                for (int j = 1; j < fuCols; j++) {
                    //make sure no null and empty values taken to calculation
                    if (productionTable.getValueAt(j - 1, 5) != null) {
                        String pack_qty = (String) productionTable.getValueAt(j - 1, 5);
                        String part_name = (String) productionTable.getValueAt(j - 1, 3);
                        if (!pack_qty.isEmpty()) {
                            if (rejectAnalysisTable.getValueAt(i, j) != null) {
                                String rej = (String) rejectAnalysisTable.getValueAt(i, j).toString();

                                //proceed if the cell is not empty
                                if (!rej.isEmpty()) {
                                    float float_rej = Float.parseFloat(rej);

                                    //get the row number of each part from park interim table
                                    int count = 0;
                                    for (int k = 0; k < packinterimTable.getRowCount(); k++) {

                                        String part_name_2 = (String) packinterimTable.getValueAt(k, 0);
                                        if (part_name.equals(part_name_2)) {
                                            count = k;
                                        }

                                    }

                                    packinterimTable.setValueAt(float_rej, count, 3);

                                }
                            }

                        }
                    }

                }
            }
        }

    }

    private void createKeybindingsEnter(JTable table) {
        table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        table.getActionMap().put("Enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                //do something on JTable enter pressed
                int click = productionTable.getEditingRow();

                if (click == -1) {
                    prodOEE();

                } else {
                    productionTable.getCellEditor().stopCellEditing();
                    prodOEE();

                }

            }
        });
    }

    public void packOEE() {

        try {
            tableEmpty(packageTimeTable);

            int emptyRows = 0;
            rowSearch:
            for (int row = 0; row < productionTable.getRowCount(); row++) { //Iterate through all the rows
                for (int col = 0; col < productionTable.getColumnCount(); col++) { //Iterate through all the columns in the row
                    if (productionTable.getValueAt(row, col) != null) { //Check if the box is empty
                        String qty = productionTable.getValueAt(row, col).toString();
                        if (!qty.isEmpty()) {

                            continue rowSearch; //If the value is not null, the row contains stuff so go onto the next row
                        }
                    }
                }
                emptyRows++; //Conditional never evaluated to true so none of the columns in the row contained anything
            }

            //System.out.println(emptyRows);
            int rows = productionTable.getRowCount();
            //System.out.println(rows);

            int fullRows = rows - emptyRows;
            //this variable defines the rows and columns in hidden tables
            int bb = -1;
            for (int row = 0; row < fullRows; row++) {
                // check weather the 5th column is not null
                if (productionTable.getValueAt(row, 5) != null) {
                    String pack_qty = (String) productionTable.getValueAt(row, 5);

                    //proceed if the cell is not empty
                    if (!pack_qty.isEmpty()) {
                        if (!pack_qty.equals("0")) {
                            bb = bb + 1;
                            //get values form the production table

                            String partNumber = (String) productionTable.getValueAt(row, 3);

                            setRowNumber(packageTimeTable, productionTable, fullRows, 5);
                            setRowNumber(packinterimTable, productionTable, fullRows, 5);
                            setRowNumber(packingOeeTable, productionTable, fullRows, 5);

                            packinterimTable.setValueAt(partNumber, bb, 0);
                            float z = 0;
                            packinterimTable.setValueAt(z, bb, 3);

                            float float_pack_qty = Float.parseFloat(pack_qty);

                            packinterimTable.setValueAt(float_pack_qty, bb, 1);
                            packingOeeTable.setValueAt(float_pack_qty, bb, 5);
                            packageTimeTable.setValueAt(partNumber, bb, 1);
                            packageTimeTable.setValueAt(bb + 1, bb, 0);

                            packinterimTable.setValueAt(partNumber, bb, 0);
                            //itemFill(packinterimTable, 2);

                            totalDownTable.setValueAt(partNumber, row, 0);

                            //add the values to the productionOEEExcel table
                            packingOeeTable.setValueAt(bb + 1, bb, 0);
                            packingOeeTable.setValueAt(partNumber, bb, 1);

                            String[] parts = partNumber.split("-");
                            String cut_length = parts[2];
                            packingOeeTable.setValueAt(cut_length, bb, 2);

                            String database_column = null;
                            boolean ifSingle = (boolean) productionTable.getValueAt(row, 9);

                            if (ifSingle) {
                                database_column = "SPOSingle";
                            } else {
                                database_column = "SPOBoth";
                            }

                            //get the swo value from the database for each part
                            java.sql.PreparedStatement preparedStatement = null;
                            String query = "select " + database_column + " from part2 where Item=?";
                            preparedStatement = conn.prepareStatement(query);
                            preparedStatement.setString(1, partNumber);
                            ResultSet rs = preparedStatement.executeQuery();
                            String season = null;

                            if (rs.next()) {
                                season = rs.getString(database_column);
                                if (season != null) {
                                    packingOeeTable.setValueAt(season, bb, 3);
                                }
                            }

                        }
                    }
                }

            }

        } catch (Exception e) {
        }

    }

    public void prodOEE() {

        try {
            
            tableEmpty(machineWorkedTable);
            
            int emptyRows = 0;
            rowSearch:
            for (int row = 0; row < productionTable.getRowCount(); row++) { //Iterate through all the rows
                for (int col = 0; col < productionTable.getColumnCount(); col++) { //Iterate through all the columns in the row
                    if (productionTable.getValueAt(row, col) != null) { //Check if the box is empty
                        String qty = productionTable.getValueAt(row, col).toString();
                        if (!qty.isEmpty()) {

                            continue rowSearch; //If the value is not null, the row contains stuff so go onto the next row
                        }
                    }
                }
                emptyRows++; //Conditional never evaluated to true so none of the columns in the row contained anything
            }

            //System.out.println(emptyRows);
            int rows = productionTable.getRowCount();
            //System.out.println(rows);

            int fullRows = rows - emptyRows;
            //this variable defines the rows and columns in hidden tables
            int bb = -1;
            for (int row = 0; row < fullRows; row++) {
                // check weather the 5th column is not null
                if (productionTable.getValueAt(row, 4) != null) {
                    String qty = (String) productionTable.getValueAt(row, 4);
                    //proceed if the cell is not empty
                    if (!qty.isEmpty()) {
                        if (!qty.equals("0")) {
                            bb = bb + 1;
                            //get values form the production table
                            String SOnumber = (String) productionTable.getValueAt(row, 1);
                            String customer = (String) productionTable.getValueAt(row, 2);
                            String shift = shiftCombo.getSelectedItem().toString();
                            String partNumber = (String) productionTable.getValueAt(row, 3);
                            String second_qty = (String) productionTable.getValueAt(row, 4);
                            float float_qty = Float.parseFloat(second_qty);
                            // String pack_qty = (String) productionTable.getValueAt(row, 5);
                            // float int_pack_qty = Float.parseFloat(pack_qty);

                            intermTable.setValueAt(partNumber, bb, 0);
                            itemFill(intermTable, 2);

                            //set index and partnumber in machine worked table
                            setRowNumber(machineWorkedTable, productionTable, fullRows, 4);
                            machineWorkedTable.setValueAt(bb + 1, bb, 0);
                            machineWorkedTable.setValueAt(partNumber, bb, 1);

                            //add the partnumber record in 'hidden' totaDownTable
                            setRowNumber2(productionTable);
                            setRowNumber(plannedDownTable, productionTable, fullRows, 4);
                            setRowNumber(intermTable, productionTable, fullRows, 4);
                            totalDownTable.setValueAt(partNumber, row, 0);

                            //add the values to the oee table
                            setRowNumber(productionOeeTable, productionTable, fullRows, 4);
                            productionOeeTable.setValueAt(bb + 1, bb, 0);
                            productionOeeTable.setValueAt(partNumber, bb, 1);
                            productionOeeTable.setValueAt(SOnumber, bb, 2);
                            productionOeeTable.setValueAt(float_qty, bb, 5);

                            //check if product in single die
                            String database_column = null;
                            boolean ifSingle = (boolean) productionTable.getValueAt(row, 9);

                            if (ifSingle) {
                                database_column = "SWOSingle";
                            } else {
                                database_column = "SWOBoth";
                            }

                            //get the swo value from the database for each part
                            java.sql.PreparedStatement preparedStatement = null;

                            String query = "select " + database_column + " from part2 where Item=?";
                            preparedStatement = conn.prepareStatement(query);
                            preparedStatement.setString(1, partNumber);
                            ResultSet rs = preparedStatement.executeQuery();
                            String season = null;

                            if (rs.next()) {
                                season = rs.getString(database_column);
                                //System.out.println(season);
                                if (season != null) {
                                    productionOeeTable.setValueAt(season, bb, 3);
                                }

                            }

                        }
                    }
                }

            }

            if (fullRows > 6) {
                int columns = fullRows - 6;
                for (int i = 0; i < columns; i++) {
                    addColumn(rejectAnalysisTable);
                    setBaseColumn(rejectAnalysisTable, rejectAnalysisTable.getColumnModel().getColumn(0));

                }
            }

        } catch (Exception e) {
        }

    }

    public void oeeTotal(JTable table) {

        int rows = table.getRowCount();

        String total = table.getValueAt(rows - 1, 1).toString();

        if (!total.equals("TOTAL")) {
            addRowbottomOee(table);
            float total_reject = columnSumOEE(table, 4);
            float total_output = columnSumOEE(table, 5);
            float total_a_time = columnSumOEE(table, 6);
            float total_p_down = columnSumOEE(table, 7);
            float total_down = columnSumOEE(table, 8);
            float total_o_time = columnSumOEE(table, 9);
            float avg_ideal_run_rate = (columnSumOEE(table, 10) / (rows));

            table.setValueAt("TOTAL", rows, 1);
            table.setValueAt(total_reject, rows, 4);
            table.setValueAt(total_output, rows, 5);
            table.setValueAt(total_a_time, rows, 6);
            table.setValueAt(total_p_down, rows, 7);
            table.setValueAt(total_down, rows, 8);
            table.setValueAt(total_o_time, rows, 9);
            table.setValueAt(avg_ideal_run_rate, rows, 10);

            //calculate availability rate
            float avl_rate = (total_o_time / (total_a_time - total_p_down)) * 100;
            Float truncated_avl_rate = BigDecimal.valueOf(avl_rate)
                    .setScale(2, RoundingMode.HALF_UP)
                    .floatValue();
            table.setValueAt(truncated_avl_rate, rows, 11);

            //calulate performance rate
            float performance_rate = (columnSumOEE(table, 12) / (rows));
            Float truncated_performance_rate = BigDecimal.valueOf(performance_rate)
                    .setScale(2, RoundingMode.HALF_UP)
                    .floatValue();
            table.setValueAt(truncated_performance_rate, rows, 12);

            //calculate quality rate
            float quality_rate = ((total_output - (total_reject)) / total_output) * 100;
            Float truncated_quality_rate = BigDecimal.valueOf(quality_rate)
                    .setScale(2, RoundingMode.HALF_UP)
                    .floatValue();
            table.setValueAt(truncated_quality_rate, rows, 13);

            //oee calculation
            float oee = avl_rate * performance_rate * quality_rate / 10000;
            Float truncated_oee = BigDecimal.valueOf(oee)
                    .setScale(2, RoundingMode.HALF_UP)
                    .floatValue();
            table.setValueAt(truncated_oee, rows, 14);
        }
    }

    public void itemColumn(JTable table,
            TableColumn sportColumn) {

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

        //Set up the editor for the sport cells.
        String[] petStrings = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7", "Item 8"};
        String[] subString = Arrays.copyOfRange(petStrings, 0, fullRows);

        JComboBox itemNumberComboBox = new JComboBox(subString);
        sportColumn.setCellEditor(new DefaultCellEditor(itemNumberComboBox));

        //Set up tool tips for the sport cells.
        DefaultTableCellRenderer renderer
                = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click to select base");
        sportColumn.setCellRenderer(renderer);
    }

    public void updateRows() {

        try {

            productionTable.getCellEditor().stopCellEditing();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date today = Calendar.getInstance().getTime();
            String reportDate = df.format(today);

            int row = productionTable.getSelectedRow();

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reportgen?useSSL=true", "vidura", "vidura");
            String queryco = "Update production Set SO_Num=?,Customer=?,Shift=?,prod_qty=?,pack_qty=? where PartNumber=?";
            pst = connection.prepareStatement(queryco);
            //Update production set 
            String SOnumber = (String) productionTable.getValueAt(row, 1);
            //System.out.println(SOnumber);
            String customer = (String) productionTable.getValueAt(row, 2);
            String shift = shiftCombo.getSelectedItem().toString();
            String partNumber = (String) productionTable.getValueAt(row, 3);
            String qty = (String) productionTable.getValueAt(row, 4);
            int int_gty = Integer.parseInt(qty);
            String pack_qty = (String) productionTable.getValueAt(row, 5);
            int int_pack_qty = Integer.parseInt(pack_qty);
            System.out.println(partNumber);
            //downTimeTable.setValueAt(row+1, row, 0);
            //downTimeTable.setValueAt(partNumber, row, 1);
            //String PartNumber = (String) productionTable.getValueAt(row, 3);

            pst.setString(1, SOnumber);
            pst.setString(2, customer);
            //pst.setString(3, reportDate);
            pst.setString(3, shift);
            pst.setInt(4, int_gty);
            pst.setInt(5, int_pack_qty);
            pst.setString(6, partNumber);
            //pst.setString(7, reportDate);

            pst.addBatch();
            JOptionPane.showMessageDialog(null, "Saved Entries in the Database", "Successful!", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }

    public void timeCalculatePacking() {
        try {
            int fullRows = filledRows(packageTimeTable);
            for (int row = 0; row < fullRows; row++) {
                String st_time = (String) packageTimeTable.getValueAt(row, 2);
                String end_time = (String) packageTimeTable.getValueAt(row, 3);

                SimpleDateFormat format = new SimpleDateFormat("HH.mm");
                Date date1 = format.parse(st_time);
                Date date2 = format.parse(end_time);

                long difference = (date2.getTime() - date1.getTime()) / (1000 * 60);

                if (difference < 0) {
                    difference = difference + 1440;
                }

                int bar = toIntExact(difference);
                packageTimeTable.setValueAt(bar, row, 4);

            }

        } catch (Exception e) {

        }

    }

    public void timeCalculate() {
        try {

            itemColumn(machineDowntimeTable, machineDowntimeTable.getColumnModel().getColumn(1));

            //machineWorkedTable.getCellEditor().stopCellEditing();
            int emptyRows = 0;
            rowSearch:
            for (int row = 0; row < machineWorkedTable.getRowCount(); row++) { //Iterate through all the rows
                for (int col = 0; col < machineWorkedTable.getColumnCount(); col++) { //Iterate through all the columns in the row
                    if (machineWorkedTable.getValueAt(row, col) != null) { //Check if the box is empty
                        continue rowSearch; //If the value is not null, the row contains stuff so go onto the next row
                    }
                }
                emptyRows++; //Conditional never evaluated to true so none of the columns in the row contained anything
            }

            //System.out.println(emptyRows);
            int rows = machineWorkedTable.getRowCount();
            //System.out.println(rows);

            int fullRows = rows - emptyRows;
            //System.out.println(fullRows);

            for (int row = 0; row < fullRows; row++) {
                //String PartNumber = (String) intermTable.getValueAt(row, 3);
                String st_time = (String) machineWorkedTable.getValueAt(row, 2);
                String end_time = (String) machineWorkedTable.getValueAt(row, 3);

                System.out.println(st_time);
                SimpleDateFormat format = new SimpleDateFormat("HH.mm");
                Date date1 = format.parse(st_time);
                Date date2 = format.parse(end_time);

                long difference = (date2.getTime() - date1.getTime()) / (1000 * 60);

                if (difference < 0) {
                    difference = difference + 1440;
                }

                System.out.println(difference);
                int bar = toIntExact(difference);
                machineWorkedTable.setValueAt(bar, row, 4);

            }
            //productionTable.getSelectionModel().clearSelection();
            //itemColumn(machineDowntimeTable, machineDowntimeTable.getColumnModel().getColumn(2));

        } catch (Exception e) {
            //JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }

    public void timeCalculateDown() {
        try {
            //downtimeTable.getCellEditor().stopCellEditing();
            int emptyRows = 0;
            rowSearch:
            for (int row = 0; row < machineDowntimeTable.getRowCount(); row++) { //Iterate through all the rows
                for (int col = 0; col < machineDowntimeTable.getColumnCount(); col++) { //Iterate through all the columns in the row
                    if (machineDowntimeTable.getValueAt(row, col) != null) { //Check if the box is empty
                        continue rowSearch; //If the value is not null, the row contains stuff so go onto the next row
                    }
                }
                emptyRows++; //Conditional never evaluated to true so none of the columns in the row contained anything
            }

            //System.out.println(emptyRows);
            int rows = machineDowntimeTable.getRowCount();
            //System.out.println(rows);

            int fullRows = rows - emptyRows;

            for (int row = 0; row < fullRows; row++) {
                //String PartNumber = (String) productionTable.getValueAt(row, 3);
                String st_time = (String) machineDowntimeTable.getValueAt(row, 2);
                String end_time = (String) machineDowntimeTable.getValueAt(row, 3);

                //System.out.println(SOnumber);
                SimpleDateFormat format = new SimpleDateFormat("HH.mm");
                Date date1 = format.parse(st_time);
                Date date2 = format.parse(end_time);
                long difference = (date2.getTime() - date1.getTime()) / (1000 * 60);
                int bar = toIntExact(difference);
                machineDowntimeTable.setValueAt(bar, row, 4);

            }
            //productionTable.getSelectionModel().clearSelection();

        } catch (Exception e) {
            //JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }

    public void setBaseColumn(JTable table,
            TableColumn sportColumn) {
        //Set up the editor for the sport cells.
        JComboBox itemNumberComboBox = new JComboBox();
        sportColumn.setCellEditor(new DefaultCellEditor(itemNumberComboBox));

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reportgen?useSSL=true", "vidura", "vidura");
            Statement st = connection.createStatement();
            String query = "SELECT DISTINCT(Base) FROM base";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                itemNumberComboBox.addItem(rs.getString("Base"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Set up tool tips for the sport cells.
        DefaultTableCellRenderer renderer
                = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click to select base");
        sportColumn.setCellRenderer(renderer);
    }

    public void downTimeColumn(JTable table,
            TableColumn sportColumn) {
        //Set up the editor for the sport cells.
        String[] petStrings = {"Planned", "Unplanned"};

        JComboBox itemNumberComboBox = new JComboBox(petStrings);
        sportColumn.setCellEditor(new DefaultCellEditor(itemNumberComboBox));

        //Set up tool tips for the sport cells.
        DefaultTableCellRenderer renderer
                = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click to select base");
        sportColumn.setCellRenderer(renderer);
    }

    public void insertDatabaseReject() {
        try {
            rejectAnalysisTable.getCellEditor().stopCellEditing();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date today = Calendar.getInstance().getTime();
            String reportDate = df.format(today);

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
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reportgen?useSSL=true", "vidura", "vidura");
            String queryco = "Update production set wastage = ? where PartNumber=? and Date= ? and Shift =?";
            pst = connection.prepareStatement(queryco);

            int fullRows = rows - emptyRows;

            for (int row = 0; row < fullRows; row++) {

                String partNumber = (String) productionTable.getValueAt(row, 3);
                float int_gty = columnSum(rejectAnalysisTable, row + 1);
                //String pack_qty = (String) productionTable.getValueAt(row, 5);

                //pst.setString(1, partNumber);
                String shift = shiftCombo.getSelectedItem().toString();
                pst.setFloat(1, int_gty);
                pst.setString(2, partNumber);
                pst.setString(3, reportDate);
                pst.setString(4, shift);

                pst.addBatch();

            }
            //productionTable.getSelectionModel().clearSelection();

            pst.executeBatch();
            //productionTable.setEnabled(false);
            //prodTableInsert.setEnabled(false);
            //prodTableUpdate.setEnabled(rootPaneCheckingEnabled);
            JOptionPane.showMessageDialog(null, "Saved Entries in the Database", "Successful!", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }

    public void hiddenTable() {
        //downtimeTable.getCellEditor().stopCellEditing();

        int emptyRows = 0;
        rowSearch:
        for (int row = 0; row < intermTable.getRowCount(); row++) { //Iterate through all the rows
            for (int col = 0; col < intermTable.getColumnCount(); col++) { //Iterate through all the columns in the row
                if (intermTable.getValueAt(row, col) != null) { //Check if the box is empty
                    continue rowSearch; //If the value is not null, the row contains stuff so go onto the next row
                }
            }
            emptyRows++; //Conditional never evaluated to true so none of the columns in the row contained anything
        }

        int emptyRowss = 0;
        rowSearch:
        for (int row = 0; row < machineDowntimeTable.getRowCount(); row++) { //Iterate through all the rows
            for (int col = 0; col < machineDowntimeTable.getColumnCount(); col++) { //Iterate through all the columns in the row
                if (machineDowntimeTable.getValueAt(row, col) != null) { //Check if the box is empty
                    continue rowSearch; //If the value is not null, the row contains stuff so go onto the next row
                }
            }
            emptyRowss++; //Conditional never evaluated to true so none of the columns in the row contained anything
        }

        //System.out.println(emptyRows);
        int rows = intermTable.getRowCount();
        //System.out.println(rows);

        int fullRows = rows - emptyRows;
        int rowers = machineDowntimeTable.getRowCount();

        int rower = rowers - emptyRowss;

        for (int row = 0; row < fullRows; row++) {
            totalDownTable.setValueAt(0, row, 1);

        }

        for (int row = 0; row < fullRows; row++) {
            plannedDownTable.setValueAt(0, row, 1);

        }

        System.out.println(rower);
        for (int row = 0; row < fullRows; row++) {

            String partNumber = (String) intermTable.getValueAt(row, 2);

            System.out.println(partNumber);
            for (int roww = 0; roww < rower; roww++) {

                String partNumberd = (String) machineDowntimeTable.getValueAt(roww, 1);
                System.out.println(partNumber);

                if (partNumber.equals(partNumberd)) {
                    String status = (String) machineDowntimeTable.getValueAt(roww, 0);
                    if ("Unplanned".equals(status)) {
                        int qty = (int) totalDownTable.getValueAt(row, 1);
                        //int add_gty = Integer.parseInt(qty);
                        System.out.println("addy" + qty);

                        int addy = (int) machineDowntimeTable.getValueAt(roww, 4);
                        //int add = Integer.parseInt(addy);
                        System.out.println(addy);

                        int summ = addy + qty;
                        System.out.println("summ" + summ);

                        totalDownTable.setValueAt(summ, row, 1);
                    } else {
                        int qty = (int) plannedDownTable.getValueAt(row, 1);
                        //int add_gty = Integer.parseInt(qty);
                        System.out.println(qty);

                        int addy = (int) machineDowntimeTable.getValueAt(roww, 4);
                        //int add = Integer.parseInt(addy);
                        System.out.println(addy);

                        int summ = addy + qty;
                        System.out.println(summ);

                        plannedDownTable.setValueAt(summ, row, 1);

                    }

                }

            }

        }

        for (int row = 0; row < fullRows; row++) {
            int addy = (int) totalDownTable.getValueAt(row, 1);
            //int add = Integer.parseInt(addy);
            productionOeeTable.setValueAt(addy, row, 8);

        }

        //machineWorkedTable
        for (int row = 0; row < fullRows; row++) {
            int addy = (int) plannedDownTable.getValueAt(row, 1);
            //int add = Integer.parseInt(addy);
            productionOeeTable.setValueAt(addy, row, 7);

        }

        for (int row = 0; row < fullRows; row++) {
            int n1 = (int) plannedDownTable.getValueAt(row, 1);
            int n2 = (int) totalDownTable.getValueAt(row, 1);
            int n3 = (int) machineWorkedTable.getValueAt(row, 4);
            //int add = Integer.parseInt(addy);
            int num_sum = n3 - (n2 + n1);
            productionOeeTable.setValueAt(num_sum, row, 9);
            productionOeeTable.setValueAt(n3, row, 6);

        }

    }

    public void itemFill(JTable table, int column) {

        //String[] itemArray = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7", "Item 8"};
        //int row = filledRows(table);
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

        int fullRows = rows - emptyRows;

        for (int i = 0; i < fullRows; i++) {
            int item_no = i + 1;
            table.setValueAt("Item " + item_no, i, column);

        }

    }

    public int filledRows(JTable table) {
        int emptyRows = 0;
        rowSearch:
        for (int row = 0; row < table.getRowCount(); row++) { //Iterate through all the rows
            for (int col = 0; col < table.getColumnCount(); col++) { //Iterate through all the columns in the row
                if (table.getValueAt(row, col) != null) {
                    String demi = table.getValueAt(row, col).toString();
                    if (!demi.isEmpty()) {

                        continue rowSearch; //If the value is not null, the row contains stuff so go onto the next row
                    }
                }
            }
            emptyRows++; //Conditional never evaluated to true so none of the columns in the row contained anything
        }
        return table.getRowCount() - emptyRows;

    }

    public int filledCols(JTable table) {
        int emptyCols = 0;
        rowSearch:
        for (int col = 0; col < table.getColumnCount(); col++) { //Iterate through all the rows
            for (int row = 0; row < table.getRowCount(); row++) { //Iterate through all the columns in the row
                if (table.getValueAt(row, col) != null) {
                    String demi = table.getValueAt(row, col).toString();
                    if (!demi.isEmpty()) {

                        continue rowSearch; //If the value is not null, the row contains stuff so go onto the next row
                    }
                }
            }
            emptyCols++; //Conditional never evaluated to true so none of the columns in the row contained anything
        }
        return table.getColumnCount() - emptyCols;

    }

    public void packagingOeeCalculation() throws SQLException {
        
        

        int fullRows = filledRows(packageTimeTable);
        for (int row = 0; row < fullRows; row++) {

            int deduct = 0;
            int no_min = 0;

            no_min = Integer.parseInt(packageTimeTable.getValueAt(row, 4).toString());

            boolean tea_time_1 = (boolean) packageTimeTable.getValueAt(row, 5);
            boolean tea_time_2 = (boolean) packageTimeTable.getValueAt(row, 6);
            boolean lunch_dinner = (boolean) packageTimeTable.getValueAt(row, 7);

            if (tea_time_1) {

                deduct = deduct + 15;

            }

            if (tea_time_2) {

                deduct = deduct + 15;

            }
            if (lunch_dinner) {

                deduct = deduct + 60;

            }

            packingOeeTable.setValueAt(no_min, row, 6);
            packingOeeTable.setValueAt(deduct, row, 7);
            packingOeeTable.setValueAt(no_min - deduct, row, 9);

            float downtime = 0;
            packingOeeTable.setValueAt(downtime, row, 8);

            String Item = (String) packingOeeTable.getValueAt(row, 1);

            java.sql.PreparedStatement preparedStatement = null;
            String query = "select CWPre from part2 where Item=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, Item);
            ResultSet rs = preparedStatement.executeQuery();
            String standard_wastages = null;

            if (rs.next()) {
                standard_wastages = rs.getString("CWPre");

            }

            float cutter_wastage_float = (float) packinterimTable.getValueAt(row, 3);
            float standard_wastages_float = Float.parseFloat(standard_wastages);

            float packed_Qty_float = (float) packingOeeTable.getValueAt(row, 5);

            float deviation = cutter_wastage_float - ((packed_Qty_float / (1 - standard_wastages_float)) - packed_Qty_float);
            Float truncated_deviation = BigDecimal.valueOf(deviation)
                    .setScale(2, RoundingMode.HALF_UP)
                    .floatValue();
            if (truncated_deviation > 0) {
                packingOeeTable.setValueAt(truncated_deviation, row, 4);
            } else {
                float zero = 0;
                packingOeeTable.setValueAt(zero, row, 4);
            }

            //calculate the ideal run rate
            String swo = (String) packingOeeTable.getValueAt(row, 3);
            float swo_int = Float.parseFloat(swo);
            float ideal_run_rate;
            ideal_run_rate = swo_int / 60;

            Float tobe_i_rate = ideal_run_rate * 100;

            Float truncated_i_rate = BigDecimal.valueOf(tobe_i_rate)
                    .setScale(2, RoundingMode.HALF_UP)
                    .floatValue();

            packingOeeTable.setValueAt(truncated_i_rate, row, 10);

            //calculate availability rate
            int o_time = (int) packingOeeTable.getValueAt(row, 9);
            int t_available = (int) packingOeeTable.getValueAt(row, 6);
            int pd_time = (int) packingOeeTable.getValueAt(row, 7);

            float o_time_float = o_time;
            float t_available_float = t_available;
            float pd_time_float = pd_time;

            float a_rate = o_time_float / (t_available_float - pd_time_float);
            float a_rate_percent = a_rate * 100;
            Float tobe_a_rate = a_rate_percent;

            Float truncatedFloat = BigDecimal.valueOf(tobe_a_rate)
                    .setScale(2, RoundingMode.HALF_UP)
                    .floatValue();

            packingOeeTable.setValueAt(truncatedFloat, row, 11);

            //calculate performance rate
            float t_output = (float) packingOeeTable.getValueAt(row, 5);
            float t_output_float = t_output;
            float c_rate = t_output_float / o_time_float;

            if (ideal_run_rate <= c_rate) {
                float hundred = 100;
                packingOeeTable.setValueAt(hundred, row, 12);

            } else {
                float p_rate = c_rate / ideal_run_rate;

                Float tobe_p_rate = p_rate * 100;

                Float truncated_p_rate = BigDecimal.valueOf(tobe_p_rate)
                        .setScale(2, RoundingMode.HALF_UP)
                        .floatValue();

                packingOeeTable.setValueAt(truncated_p_rate, row, 12);

            }

            //calculate quality rate
            float t_reject = (float) packingOeeTable.getValueAt(row, 4);
            float t_reject_float = t_reject;

            float q_rate = (t_output_float - t_reject_float) / t_output_float;
            Float tobe_q_rate = q_rate * 100;

            Float truncated_q_rate = BigDecimal.valueOf(tobe_q_rate)
                    .setScale(2, RoundingMode.HALF_UP)
                    .floatValue();

            packingOeeTable.setValueAt(truncated_q_rate, row, 13);

            //calculate productionOEEExcel rate
            float pro_rate = (float) packingOeeTable.getValueAt(row, 11);
            Float tobe_pro_rate = pro_rate * 100;
            //double pro_rate_double = pro_rate;
            //double productionOEEExcel = q_rate * pro_rate * a_rate;
            float num1 = (float) packingOeeTable.getValueAt(row, 11);
            float num2 = (float) packingOeeTable.getValueAt(row, 12);
            float num3 = (float) packingOeeTable.getValueAt(row, 13);

            float oee = num1 * num2 * num3 / (100 * 100);
            Float tobe_oee = oee;
            Float truncated_oee = BigDecimal.valueOf(tobe_oee)
                    .setScale(2, RoundingMode.HALF_UP)
                    .floatValue();

            packingOeeTable.setValueAt(truncated_oee, row, 14);

        }

    }

    public void productionOeeCalculation() {

        //rejectAnalysisTable.getCellEditor().stopCellEditing();
       
        int emptyRows = 0;
        rowSearch:
        for (int row = 0; row < productionOeeTable.getRowCount(); row++) { //Iterate through all the rows
            for (int col = 0; col < productionOeeTable.getColumnCount(); col++) { //Iterate through all the columns in the row
                if (productionOeeTable.getValueAt(row, col) != null) { //Check if the box is empty
                    continue rowSearch; //If the value is not null, the row contains stuff so go onto the next row
                }
            }
            emptyRows++; //Conditional never evaluated to true so none of the columns in the row contained anything
        }

        int rows = productionOeeTable.getRowCount();
        //System.out.println(rows);

        int fullRows = rows - emptyRows;

        for (int row = 0; row < fullRows; row++) {

            //calculate the ideal run rate
            String swo = (String) productionOeeTable.getValueAt(row, 3);
            float swo_int = Float.parseFloat(swo);
            float ideal_run_rate;
            ideal_run_rate = swo_int / 60;

            Float tobe_i_rate = ideal_run_rate * 100;

            Float truncated_i_rate = BigDecimal.valueOf(tobe_i_rate)
                    .setScale(2, RoundingMode.HALF_UP)
                    .floatValue();

            productionOeeTable.setValueAt(truncated_i_rate, row, 10);

            //calculate availability rate
            int o_time = (int) productionOeeTable.getValueAt(row, 9);
            int t_available = (int) productionOeeTable.getValueAt(row, 6);
            int pd_time = (int) productionOeeTable.getValueAt(row, 7);

            float o_time_float = o_time;
            float t_available_float = t_available;
            float pd_time_float = pd_time;

            float a_rate = o_time_float / (t_available_float - pd_time_float);
            float a_rate_percent = a_rate * 100;
            Float tobe_a_rate = a_rate_percent;

            Float truncatedFloat = BigDecimal.valueOf(tobe_a_rate)
                    .setScale(2, RoundingMode.HALF_UP)
                    .floatValue();

            productionOeeTable.setValueAt(truncatedFloat, row, 11);

            //calculate performance rate
            float t_output = (float) productionOeeTable.getValueAt(row, 5);
            float t_output_float = t_output;
            float c_rate = t_output_float / o_time_float;

            if (ideal_run_rate <= c_rate) {
                float hundred = 100;
                productionOeeTable.setValueAt(hundred, row, 12);

            } else {
                float p_rate = c_rate / ideal_run_rate;

                Float tobe_p_rate = p_rate * 100;

                Float truncated_p_rate = BigDecimal.valueOf(tobe_p_rate)
                        .setScale(2, RoundingMode.HALF_UP)
                        .floatValue();

                productionOeeTable.setValueAt(truncated_p_rate, row, 12);

            }

            //calculate quality rate
            float t_reject = (float) productionOeeTable.getValueAt(row, 4);
            float t_reject_float = t_reject;

            float q_rate = (t_output_float - t_reject_float) / t_output_float;
            Float tobe_q_rate = q_rate * 100;

            Float truncated_q_rate = BigDecimal.valueOf(tobe_q_rate)
                    .setScale(2, RoundingMode.HALF_UP)
                    .floatValue();

            productionOeeTable.setValueAt(truncated_q_rate, row, 13);

            //calculate productionOEEExcel rate
            float pro_rate = (float) productionOeeTable.getValueAt(row, 11);
            Float tobe_pro_rate = pro_rate * 100;
            //double pro_rate_double = pro_rate;
            //double productionOEEExcel = q_rate * pro_rate * a_rate;
            float num1 = (float) productionOeeTable.getValueAt(row, 11);
            float num2 = (float) productionOeeTable.getValueAt(row, 12);
            float num3 = (float) productionOeeTable.getValueAt(row, 13);

            float oee = num1 * num2 * num3 / (100 * 100);
            Float tobe_oee = oee;
            Float truncated_oee = BigDecimal.valueOf(tobe_oee)
                    .setScale(2, RoundingMode.HALF_UP)
                    .floatValue();

            productionOeeTable.setValueAt(truncated_oee, row, 14);

        }

    }

    public void oeeReject() {
        try {
            //rejectAnalysisTable.getCellEditor().stopCellEditing();

            int emptyRows = 0;
            rowSearch:
            for (int row = 0; row < intermTable.getRowCount(); row++) { //Iterate through all the rows
                for (int col = 0; col < intermTable.getColumnCount(); col++) { //Iterate through all the columns in the row
                    if (intermTable.getValueAt(row, col) != null) { //Check if the box is empty
                        continue rowSearch; //If the value is not null, the row contains stuff so go onto the next row
                    }
                }
                emptyRows++; //Conditional never evaluated to true so none of the columns in the row contained anything
            }

            //System.out.println(emptyRows);
            int rows = intermTable.getRowCount();
            //System.out.println(rows);

            int fullRows = rows - emptyRows;

            for (int row = 0; row < fullRows; row++) {

                float wastage = columnSum(rejectAnalysisTable, row + 1);
                //String pack_qty = (String) productionTable.getValueAt(row, 5);
                Float truncated_wastage = BigDecimal.valueOf(wastage)
                        .setScale(2, RoundingMode.HALF_UP)
                        .floatValue();

                productionOeeTable.setValueAt(truncated_wastage, row, 4);
                //pst.setString(1, partNumber);
                ;

            }
            //productionTable.getSelectionModel().clearSelection();

            //productionTable.setEnabled(false);
            //prodTableInsert.setEnabled(false);
            //prodTableUpdate.setEnabled(rootPaneCheckingEnabled);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }

    public float columnSum(JTable table, int n) {

        int rowsCount = table.getRowCount();

        float sum = 0;
        for (int i = 0; i < rowsCount; i++) {
            Object value = table.getValueAt(i, n);
            //float qty = (float) table.getValueAt(i, n);

            // String st_qty = (String)table.getValueAt(i, n);
            //String st_qty = String.valueOf(qty);
            if (value != null) {
                if (!value.toString().isEmpty()) {
                    sum = sum + Float.parseFloat(table.getValueAt(i, n).toString());
                }
            }
        }
        return sum;

    }

    public float columnSumOEE(JTable table, int n) {

        int rowsCount = table.getRowCount();

        float sum = 0;
        for (int i = 0; i < rowsCount - 1; i++) {

            float item = Float.parseFloat(table.getValueAt(i, n) + "");

            sum = sum + item;

        }
        return sum;

    }

    /*public void partnumberComboBox()
	{
		try{
			Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/reportgen?useSSL=true","vidura","vidura");
			Statement st=connection.createStatement();
			String query="SELECT DISTINCT(PartNumber) FROM parts";
			ResultSet rs=st.executeQuery(query);
			
			while(rs.next()){
				itemNumberComboBox.addItem(rs.getString("PartNumber"));
			}
			}
		catch(Exception e){
			e.printStackTrace();
		}
	}*/

 /*public void baseComboBox()
	{
		try{
			Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/reportgen?useSSL=true","vidura","vidura");
			Statement st=connection.createStatement();
			String query="SELECT DISTINCT(Base) FROM Base";
			ResultSet rs=st.executeQuery(query);
			
			while(rs.next()){
				base.addItem(rs.getString("Base"));
			}
			}
		catch(Exception e){
			e.printStackTrace();
		}
	}*/
 /*public void rejectPartNumberComboBox()
	{
		try{
			Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/reportgen?useSSL=true","vidura","vidura");
			Statement st=connection.createStatement();
			String query="SELECT DISTINCT(PartNumber) FROM parts";
			ResultSet rs=st.executeQuery(query);
			
			while(rs.next()){
				rejectPartNumber.addItem(rs.getString("PartNumber"));
			}
			}
		catch(Exception e){
			e.printStackTrace();
		}
	}*/
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rowAdderDeleter = new javax.swing.JPopupMenu();
        removeThisRow = new javax.swing.JMenuItem();
        addAbove = new javax.swing.JMenuItem();
        addBelow = new javax.swing.JMenuItem();
        removeAbove = new javax.swing.JMenuItem();
        removeBelow = new javax.swing.JMenuItem();
        rejectAnalysis = new javax.swing.JPopupMenu();
        removeThisRowReject = new javax.swing.JMenuItem();
        addAboveReject = new javax.swing.JMenuItem();
        addBelowReject = new javax.swing.JMenuItem();
        removeAboveReject = new javax.swing.JMenuItem();
        removeBelowReject = new javax.swing.JMenuItem();
        machineDowntime = new javax.swing.JPopupMenu();
        removeThisRowMD = new javax.swing.JMenuItem();
        addAboveMD = new javax.swing.JMenuItem();
        addBelowMD = new javax.swing.JMenuItem();
        removeAboveMD = new javax.swing.JMenuItem();
        removeBelowMD = new javax.swing.JMenuItem();
        jDialog1 = new javax.swing.JDialog();
        shiftCombo = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        supervisor = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        technician = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        now = new javax.swing.JTextField();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        productionTable = new javax.swing.JTable(){
            public void changeSelection(final int row, final int column, boolean toggle, boolean extend)
            {
                super.changeSelection(row, column, toggle, extend);
                productionTable.editCellAt(row, column);
                productionTable.transferFocus();
                // productionTable.setRowSelectionAllowed(true);

                //productionTable.setRowSelectionAllowed(true);
                //productionTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            }

        };
        jScrollPane2 = new javax.swing.JScrollPane();
        rejectAnalysisTable = new javax.swing.JTable(){
            public void changeSelection(final int row, final int column, boolean toggle, boolean extend)
            {
                super.changeSelection(row, column, toggle, extend);
                rejectAnalysisTable.editCellAt(row, column);
                rejectAnalysisTable.transferFocus();
            }
        };
        jButton5 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        machineWorkedTable = new javax.swing.JTable(){
            public void changeSelection(final int row, final int column, boolean toggle, boolean extend)
            {
                super.changeSelection(row, column, toggle, extend);
                machineWorkedTable.editCellAt(row, column);
                machineWorkedTable.transferFocus();
            }

            public boolean isCellEditable(int row,int column){
                if(column == 4) return false;//the 4th column is not editable
                return true;
            }

        };
        hidPanel = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        intermTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        totalDownTable = new javax.swing.JTable();
        jScrollPane12 = new javax.swing.JScrollPane();
        packinterimTable = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        plannedDownTable = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        machineDowntimeTable = new javax.swing.JTable(){
            public void changeSelection(final int row, final int column, boolean toggle, boolean extend)
            {
                super.changeSelection(row, column, toggle, extend);
                machineDowntimeTable .editCellAt(row, column);
                machineDowntimeTable .transferFocus();
            }

            public boolean isCellEditable(int row,int column){
                if(column == 4) return false;//the 4th column is not editable
                return true;
            }

        };
        jButton8 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        packageTimeTable = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        productionOeeTable = new javax.swing.JTable(){
            public void changeSelection(final int row, final int column, boolean toggle, boolean extend)
            {
                super.changeSelection(row, column, toggle, extend);
                productionOeeTable.editCellAt(row, column);
                productionOeeTable.transferFocus();
            }
        };
        jScrollPane11 = new javax.swing.JScrollPane();
        packingOeeTable = new javax.swing.JTable(){
            public void changeSelection(final int row, final int column, boolean toggle, boolean extend)
            {
                super.changeSelection(row, column, toggle, extend);
                productionOeeTable.editCellAt(row, column);
                productionOeeTable.transferFocus();
            }
        };
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        removeThisRow.setText("Remove This Row");
        removeThisRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeThisRowActionPerformed(evt);
            }
        });
        rowAdderDeleter.add(removeThisRow);

        addAbove.setText("Add a Row Above");
        addAbove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAboveActionPerformed(evt);
            }
        });
        rowAdderDeleter.add(addAbove);

        addBelow.setText("Add a Row Below");
        addBelow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBelowActionPerformed(evt);
            }
        });
        rowAdderDeleter.add(addBelow);

        removeAbove.setText("Remove Above Row ");
        removeAbove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAboveActionPerformed(evt);
            }
        });
        rowAdderDeleter.add(removeAbove);

        removeBelow.setText("Remove Below Row");
        removeBelow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeBelowActionPerformed(evt);
            }
        });
        rowAdderDeleter.add(removeBelow);

        rejectAnalysis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rejectAnalysisMouseClicked(evt);
            }
        });

        removeThisRowReject.setText("Remove this Row");
        removeThisRowReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeThisRowRejectActionPerformed(evt);
            }
        });
        rejectAnalysis.add(removeThisRowReject);

        addAboveReject.setText("Add a Row Above");
        addAboveReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAboveRejectActionPerformed(evt);
            }
        });
        rejectAnalysis.add(addAboveReject);

        addBelowReject.setText("Add a Row Below");
        addBelowReject.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addBelowRejectMouseClicked(evt);
            }
        });
        addBelowReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBelowRejectActionPerformed(evt);
            }
        });
        rejectAnalysis.add(addBelowReject);

        removeAboveReject.setText("Remove Above Row ");
        removeAboveReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAboveRejectActionPerformed(evt);
            }
        });
        rejectAnalysis.add(removeAboveReject);

        removeBelowReject.setText("Remove Below Row");
        removeBelowReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeBelowRejectActionPerformed(evt);
            }
        });
        rejectAnalysis.add(removeBelowReject);

        removeThisRowMD.setText("Remove this row");
        machineDowntime.add(removeThisRowMD);

        addAboveMD.setText("Add row above");
        addAboveMD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAboveMDActionPerformed(evt);
            }
        });
        machineDowntime.add(addAboveMD);

        addBelowMD.setText("Add row below");
        addBelowMD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBelowMDActionPerformed(evt);
            }
        });
        machineDowntime.add(addBelowMD);

        removeAboveMD.setText("Romove row above");
        removeAboveMD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAboveMDActionPerformed(evt);
            }
        });
        machineDowntime.add(removeAboveMD);

        removeBelowMD.setText("Remove row below");
        removeBelowMD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeBelowMDActionPerformed(evt);
            }
        });
        machineDowntime.add(removeBelowMD);

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(102, 102, 102));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        shiftCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Day", "Night" }));
        shiftCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shiftComboActionPerformed(evt);
            }
        });
        getContentPane().add(shiftCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 20, 60, -1));

        jLabel7.setText("Shift");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, 80, 40));
        getContentPane().add(supervisor, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 20, 140, -1));

        jLabel11.setText("Date");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 40, 20));

        jLabel14.setText("Supervisor");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 20, -1, 20));

        technician.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                technicianActionPerformed(evt);
            }
        });
        getContentPane().add(technician, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 20, 160, -1));

        jLabel16.setText("Technician");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 10, 110, 50));

        jLabel17.setText("No. of Workers");
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 10, 80, 50));

        now.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nowActionPerformed(evt);
            }
        });
        getContentPane().add(now, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 20, 60, -1));

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabbedPane1.setMaximumSize(new java.awt.Dimension(1277, 997));
        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseReleased(evt);
            }
        });

        jPanel5.setAutoscrolls(true);
        jPanel5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel5.setMaximumSize(new java.awt.Dimension(1272, 966));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jScrollPane9KeyPressed(evt);
            }
        });

        productionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null,  new Boolean(false)},
                {null, null, null, null, null, null, null, null, null,  new Boolean(false)},
                {null, null, null, null, null, null, null, null, null,  new Boolean(false)},
                {null, null, null, null, null, null, null, null, null,  new Boolean(false)},
                {null, null, null, null, null, null, null, null, null,  new Boolean(false)}
            },
            new String [] {
                "No", "SO Number", "Customer", "Part Number", "Produced Qty", "Packed Qty", "No of Cart. New", "No of Cart. Used", "PP/PE Bags", "Single Die"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        productionTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                productionTableMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                productionTableMouseReleased(evt);
            }
        });
        productionTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                productionTableKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                productionTableKeyReleased(evt);
            }
        });
        jScrollPane9.setViewportView(productionTable);
        if (productionTable.getColumnModel().getColumnCount() > 0) {
            productionTable.getColumnModel().getColumn(0).setMaxWidth(35);
            productionTable.getColumnModel().getColumn(6).setMaxWidth(100);
            productionTable.getColumnModel().getColumn(7).setMaxWidth(100);
            productionTable.getColumnModel().getColumn(8).setMaxWidth(100);
            productionTable.getColumnModel().getColumn(9).setMaxWidth(60);
        }

        jPanel5.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 52, 1182, 149));

        rejectAnalysisTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Line Setup", null, null, null, null, null, null, null},
                {"Metal Detector", null, null, null, null, null, null, null},
                {"Back Flush", null, null, null, null, null, null, null},
                {"Winder Wastage", null, null, null, null, null, null, null},
                {"Wrapper Wastage", null, null, null, null, null, null, null},
                {"Diameter Variation", null, null, null, null, null, null, null},
                {"Colour Variation", null, null, null, null, null, null, null},
                {"Crimp Variation", null, null, null, null, null, null, null},
                {"Material Quality", null, null, null, null, null, null, null},
                {"Process Issue", null, null, null, null, null, null, null},
                {"Technical Fault", null, null, null, null, null, null, null},
                {"Workers Fault", null, null, null, null, null, null, null},
                {"Technicians Fault", null, null, null, null, null, null, null},
                {"Supervisory Fault", null, null, null, null, null, null, null},
                {"Planning Fault", null, null, null, null, null, null, null},
                {"Cutter wastage", null, null, null, null, null, null, null},
                {"Rewrape", null, null, null, null, null, null, null},
                {"Colour variation", null, null, null, null, null, null, null},
                {"Diameter variation", null, null, null, null, null, null, null},
                {"Other", null, null, null, null, null, null, null}
            },
            new String [] {
                "Base", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        rejectAnalysisTable.setColumnSelectionAllowed(true);
        rejectAnalysisTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                rejectAnalysisTableMouseReleased(evt);
            }
        });
        rejectAnalysisTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                rejectAnalysisTableKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(rejectAnalysisTable);
        rejectAnalysisTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jPanel5.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 274, 1182, 160));

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icon.png"))); // NOI18N
        jButton5.setText("Insert Data");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 440, -1, -1));

        machineWorkedTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Item No", "Part Number", "From", "To", "No of Mins"
            }
        ));
        machineWorkedTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                machineWorkedTableKeyPressed(evt);
            }
        });
        jScrollPane6.setViewportView(machineWorkedTable);

        jPanel5.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 510, 1180, 163));

        intermTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Item ", "Qty", "ItemNo"
            }
        ));
        jScrollPane7.setViewportView(intermTable);

        totalDownTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Item ", "Down"
            }
        ));
        jScrollPane3.setViewportView(totalDownTable);

        packinterimTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ItemName", "Qty", "Ito", "Rej"
            }
        ));
        jScrollPane12.setViewportView(packinterimTable);

        plannedDownTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Item", "Down"
            }
        ));
        jScrollPane5.setViewportView(plannedDownTable);

        javax.swing.GroupLayout hidPanelLayout = new javax.swing.GroupLayout(hidPanel);
        hidPanel.setLayout(hidPanelLayout);
        hidPanelLayout.setHorizontalGroup(
            hidPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hidPanelLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(hidPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(hidPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        hidPanelLayout.setVerticalGroup(
            hidPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hidPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(hidPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.add(hidPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 280, 390, 170));

        machineDowntimeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Status", "Attribute To", "From", "To", "No of Mins", "Reason", "Comment"
            }
        ));
        machineDowntimeTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                machineDowntimeTableMouseReleased(evt);
            }
        });
        machineDowntimeTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                machineDowntimeTableKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                machineDowntimeTableKeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(machineDowntimeTable);
        if (machineDowntimeTable.getColumnModel().getColumnCount() > 0) {
            machineDowntimeTable.getColumnModel().getColumn(0).setMaxWidth(300);
            machineDowntimeTable.getColumnModel().getColumn(1).setMaxWidth(150);
            machineDowntimeTable.getColumnModel().getColumn(2).setMaxWidth(75);
            machineDowntimeTable.getColumnModel().getColumn(3).setMaxWidth(75);
            machineDowntimeTable.getColumnModel().getColumn(4).setMaxWidth(75);
            machineDowntimeTable.getColumnModel().getColumn(5).setMinWidth(200);
            machineDowntimeTable.getColumnModel().getColumn(5).setMaxWidth(300);
        }

        jPanel5.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 760, 1180, 150));

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/plus-and-minus.png"))); // NOI18N
        jButton8.setText("Insert Data");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 920, -1, 30));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Production");
        jPanel5.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 24, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("Reject Analysis");
        jPanel5.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 246, -1, -1));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icon.png"))); // NOI18N
        jButton3.setText("Insert Data");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 220, -1, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("Machine Run Time");
        jPanel5.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 480, -1, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("Packing Time");
        jPanel5.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 960, -1, -1));

        jButton6.setText("Prod OEE");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 1240, -1, 20));

        jButton7.setText("Prod OEE Excel");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 1240, -1, -1));

        packageTimeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null,  new Boolean(false),  new Boolean(false),  new Boolean(false)},
                {null, null, null, null, null,  new Boolean(false),  new Boolean(false),  new Boolean(false)},
                {null, null, null, null, null,  new Boolean(false),  new Boolean(false),  new Boolean(false)},
                {null, null, null, null, null,  new Boolean(false),  new Boolean(false),  new Boolean(false)}
            },
            new String [] {
                "No", "Part Number", "From", "To", "Number of Mins", "Tea 1", "Tea 2", "Lunch/Dinner"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        packageTimeTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                packageTimeTableKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(packageTimeTable);

        jPanel5.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 1000, 1190, 200));

        jButton2.setText("Pack OEE");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 1240, 80, -1));

        jButton1.setText("Pack OEE Excel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 1240, -1, -1));

        jButton12.setText("Shift Excel");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 1240, -1, -1));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setText("Machine Down Time");
        jPanel5.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 730, -1, -1));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/plus-and-minus.png"))); // NOI18N
        jButton4.setText("Insert Data");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 700, -1, -1));

        jTabbedPane1.addTab("Production", jPanel5);

        productionOeeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "Item", "SO Number", "SWO", "Total Reject/Rework", "Total Output", "Total Available Time", "Planned Downtime", "Downtime", "Operating Time", "Ideal Run Rate", "Avialability Rate", "Performance Rate", "Quality Rate", "Plant OEE"
            }
        ));
        jScrollPane8.setViewportView(productionOeeTable);
        if (productionOeeTable.getColumnModel().getColumnCount() > 0) {
            productionOeeTable.getColumnModel().getColumn(0).setMaxWidth(60);
            productionOeeTable.getColumnModel().getColumn(1).setMinWidth(250);
            productionOeeTable.getColumnModel().getColumn(1).setMaxWidth(250);
            productionOeeTable.getColumnModel().getColumn(2).setMinWidth(100);
            productionOeeTable.getColumnModel().getColumn(2).setMaxWidth(100);
            productionOeeTable.getColumnModel().getColumn(3).setMinWidth(100);
            productionOeeTable.getColumnModel().getColumn(3).setMaxWidth(100);
            productionOeeTable.getColumnModel().getColumn(4).setMinWidth(100);
            productionOeeTable.getColumnModel().getColumn(4).setMaxWidth(100);
            productionOeeTable.getColumnModel().getColumn(5).setMinWidth(100);
            productionOeeTable.getColumnModel().getColumn(5).setMaxWidth(100);
            productionOeeTable.getColumnModel().getColumn(6).setMinWidth(100);
            productionOeeTable.getColumnModel().getColumn(6).setMaxWidth(100);
            productionOeeTable.getColumnModel().getColumn(7).setMinWidth(100);
            productionOeeTable.getColumnModel().getColumn(7).setMaxWidth(100);
            productionOeeTable.getColumnModel().getColumn(8).setMinWidth(100);
            productionOeeTable.getColumnModel().getColumn(8).setMaxWidth(100);
            productionOeeTable.getColumnModel().getColumn(9).setMinWidth(100);
            productionOeeTable.getColumnModel().getColumn(9).setMaxWidth(100);
            productionOeeTable.getColumnModel().getColumn(10).setMinWidth(100);
            productionOeeTable.getColumnModel().getColumn(10).setMaxWidth(100);
            productionOeeTable.getColumnModel().getColumn(11).setMinWidth(100);
            productionOeeTable.getColumnModel().getColumn(11).setMaxWidth(100);
            productionOeeTable.getColumnModel().getColumn(12).setMinWidth(100);
            productionOeeTable.getColumnModel().getColumn(12).setMaxWidth(100);
            productionOeeTable.getColumnModel().getColumn(13).setMinWidth(100);
            productionOeeTable.getColumnModel().getColumn(13).setMaxWidth(100);
            productionOeeTable.getColumnModel().getColumn(14).setMinWidth(100);
            productionOeeTable.getColumnModel().getColumn(14).setMaxWidth(100);
        }

        packingOeeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "Item", "Cut Length", "SPO", "Total Reject/Rework", "Total Output", "Total Available Time", "Planned Downtime", "Downtime", "Operating Time", "Ideal Run Rate", "Avialability Rate", "Performance Rate", "Quality Rate", "Plant OEE"
            }
        ));
        jScrollPane11.setViewportView(packingOeeTable);
        if (packingOeeTable.getColumnModel().getColumnCount() > 0) {
            packingOeeTable.getColumnModel().getColumn(0).setMaxWidth(60);
            packingOeeTable.getColumnModel().getColumn(1).setMinWidth(250);
            packingOeeTable.getColumnModel().getColumn(1).setMaxWidth(250);
            packingOeeTable.getColumnModel().getColumn(2).setMinWidth(100);
            packingOeeTable.getColumnModel().getColumn(2).setMaxWidth(100);
            packingOeeTable.getColumnModel().getColumn(3).setMinWidth(100);
            packingOeeTable.getColumnModel().getColumn(3).setMaxWidth(100);
            packingOeeTable.getColumnModel().getColumn(4).setMinWidth(100);
            packingOeeTable.getColumnModel().getColumn(4).setMaxWidth(100);
            packingOeeTable.getColumnModel().getColumn(5).setMinWidth(100);
            packingOeeTable.getColumnModel().getColumn(5).setMaxWidth(100);
            packingOeeTable.getColumnModel().getColumn(6).setMinWidth(100);
            packingOeeTable.getColumnModel().getColumn(6).setMaxWidth(100);
            packingOeeTable.getColumnModel().getColumn(7).setMinWidth(100);
            packingOeeTable.getColumnModel().getColumn(7).setMaxWidth(100);
            packingOeeTable.getColumnModel().getColumn(8).setMinWidth(100);
            packingOeeTable.getColumnModel().getColumn(8).setMaxWidth(100);
            packingOeeTable.getColumnModel().getColumn(9).setMinWidth(100);
            packingOeeTable.getColumnModel().getColumn(9).setMaxWidth(100);
            packingOeeTable.getColumnModel().getColumn(10).setMinWidth(100);
            packingOeeTable.getColumnModel().getColumn(10).setMaxWidth(100);
            packingOeeTable.getColumnModel().getColumn(11).setMinWidth(100);
            packingOeeTable.getColumnModel().getColumn(11).setMaxWidth(100);
            packingOeeTable.getColumnModel().getColumn(12).setMinWidth(100);
            packingOeeTable.getColumnModel().getColumn(12).setMaxWidth(100);
            packingOeeTable.getColumnModel().getColumn(13).setMinWidth(100);
            packingOeeTable.getColumnModel().getColumn(13).setMaxWidth(100);
            packingOeeTable.getColumnModel().getColumn(14).setMinWidth(100);
            packingOeeTable.getColumnModel().getColumn(14).setMaxWidth(100);
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 1563, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 1563, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 35, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(917, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("OEE", jPanel4);

        jScrollPane10.setViewportView(jTabbedPane1);

        jScrollPane10.getVerticalScrollBar().setUnitIncrement(16);

        getContentPane().add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 1335, 560));
        getContentPane().add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 130, -1));

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addAboveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAboveActionPerformed
        // TODO add your handling code here:
        rowOperations.addRowAbove(productionTable);
    }//GEN-LAST:event_addAboveActionPerformed

    private void addBelowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBelowActionPerformed
        // TODO add your handling code here:
        rowOperations.addRowBelow(productionTable);
    }//GEN-LAST:event_addBelowActionPerformed

    private void removeAboveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAboveActionPerformed
        // TODO add your handling code here:
        rowOperations.removeRowAbove(productionTable);
    }//GEN-LAST:event_removeAboveActionPerformed

    private void removeBelowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeBelowActionPerformed
        // TODO add your handling code here:
        rowOperations.removeRowBelow(productionTable);
    }//GEN-LAST:event_removeBelowActionPerformed

    private void removeThisRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeThisRowActionPerformed
        // TODO add your handling code here:
        rowOperations.removeThisRow(productionTable);
    }//GEN-LAST:event_removeThisRowActionPerformed


    private void rejectAnalysisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rejectAnalysisMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_rejectAnalysisMouseClicked

    private void addBelowRejectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addBelowRejectMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_addBelowRejectMouseClicked

    private void addBelowRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBelowRejectActionPerformed
        // TODO add your handling code here:
        rowOperations.addRowBelowReject(rejectAnalysisTable);
    }//GEN-LAST:event_addBelowRejectActionPerformed

    private void removeThisRowRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeThisRowRejectActionPerformed
        // TODO add your handling code here:
        rowOperations.removeThisRow(rejectAnalysisTable);
    }//GEN-LAST:event_removeThisRowRejectActionPerformed

    private void removeAboveRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAboveRejectActionPerformed
        // TODO add your handling code here:
        removeRowAbove(rejectAnalysisTable);
    }//GEN-LAST:event_removeAboveRejectActionPerformed

    private void removeBelowRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeBelowRejectActionPerformed
        // TODO add your handling code here:
        removeRowBelow(rejectAnalysisTable);
    }//GEN-LAST:event_removeBelowRejectActionPerformed

    private void addAboveRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAboveRejectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addAboveRejectActionPerformed

    private void addBelowMDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBelowMDActionPerformed
        // TODO add your handling code here:
        rowOperations.addRowBelowMachine(machineDowntimeTable);
    }//GEN-LAST:event_addBelowMDActionPerformed

    private void removeAboveMDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAboveMDActionPerformed
        // TODO add your handling code here:
        rowOperations.removeRowAbove(machineDowntimeTable);
    }//GEN-LAST:event_removeAboveMDActionPerformed

    private void removeBelowMDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeBelowMDActionPerformed
        // TODO add your handling code here:
        rowOperations.removeRowBelow(machineDowntimeTable);
    }//GEN-LAST:event_removeBelowMDActionPerformed

    private void addAboveMDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAboveMDActionPerformed
        // TODO add your handling code here:
        rowOperations.addRowAboveMD(machineDowntimeTable);
    }//GEN-LAST:event_addAboveMDActionPerformed

    private void shiftComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shiftComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_shiftComboActionPerformed

    private void jTabbedPane1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPane1MouseReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        int click = machineWorkedTable.getEditingRow();

        if (click == -1) {
            timeCalculate();
            machineTimePreDatabaseInsert();
            machineRuntimeDatabaseInstert();

        } else {
            machineWorkedTable.getCellEditor().stopCellEditing();
            timeCalculate();
            machineTimePreDatabaseInsert();
            machineRuntimeDatabaseInstert();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        int click_1 = productionTable.getEditingRow();
        if (click_1 != -1) {
            productionTable.getCellEditor().stopCellEditing();
        }

        int click_2 = rejectAnalysisTable.getEditingRow();
        if (click_2 != -1) {
            rejectAnalysisTable.getCellEditor().stopCellEditing();
        }

        int click_3 = machineDowntimeTable.getEditingRow();
        if (click_3 != -1) {
            machineDowntimeTable.getCellEditor().stopCellEditing();
        }

        int click_4 = packageTimeTable.getEditingRow();
        if (click_4 != -1) {
            packageTimeTable.getCellEditor().stopCellEditing();
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Date reportDate = jDateChooser1.getDate();
        String reportDateString = df.format(reportDate);
        String sup = supervisor.getText();
        String tech = technician.getText();
        String reportShift = shiftCombo.getSelectedItem().toString();
        String now_string = now.getText();

        JFileChooser fileChooser = new JFileChooser();
        // apply filter
        FileNameExtensionFilter sdfFilter = new FileNameExtensionFilter(
                "excel files (*.xlsx)", "xlsx");
        fileChooser.setFileFilter(sdfFilter);

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            //add .xls extention
            file = new File(file.toString() + ".xlsx");
            excelCreator shift = new excelCreator();
            //JTable prodTable, JTable rejectTable, JTable downtimeTable, JTable packageTimeTable,String reportDate,String reportShift, String Supervior, String Technician, String NoW, File file
            shift.shiftReport(productionTable, rejectAnalysisTable, machineDowntimeTable, packageTimeTable, reportDateString, reportShift, sup, tech, now_string, file);

        }


    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        // apply filter
        FileNameExtensionFilter sdfFilter = new FileNameExtensionFilter(
                "excel files (*.xlsx)", "xlsx");
        fileChooser.setFileFilter(sdfFilter);

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            //add .xls extention
            file = new File(file.toString() + ".xlsx");
            excelCreator excaly = new excelCreator();
            try {
                excaly.packagingOEEExcel(packingOeeTable, file);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        timeCalculatePacking();
        try {
            packagingOeeCalculation();

        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        oeeTotal(packingOeeTable);
        packOEEPreDatabaseInsert();
        packOEEDatabaseInsert();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void packageTimeTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_packageTimeTableKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int click = packageTimeTable.getEditingRow();

            if (click == -1) {
                timeCalculatePacking();

            } else {
                packageTimeTable.getCellEditor().stopCellEditing();
                timeCalculatePacking();
            }

        }
    }//GEN-LAST:event_packageTimeTableKeyPressed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code her

        JFileChooser fileChooser = new JFileChooser();
        // apply filter
        FileNameExtensionFilter sdfFilter = new FileNameExtensionFilter(
                "excel files (*.xlsx)", "xlsx");
        fileChooser.setFileFilter(sdfFilter);

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            //add .xls extention
            file = new File(file.toString() + ".xlsx");
            excelCreator excaly = new excelCreator();
            try {
                excaly.productionOEEExcel(productionOeeTable, file);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        prodOEE();
        productionOeeCalculation();
        oeeTotal(productionOeeTable);
        prodOEEPreDatabaseInsert();
        prodOEEDatabaseInsert();

        //oeeTotal(productionOeeTable);
        //excal.production(productionTable);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:

        // databaseOperations calculate = new databaseOperations();
        int click = productionTable.getEditingRow();

        if (click == -1) {

            prodOEE();
            packOEE();
            productionPreDatabaseInsert();
            productionDatabaseInsert();
            setPackPackingTime();

        } else {

            productionTable.getCellEditor().stopCellEditing();
            prodOEE();
            packOEE();
            productionPreDatabaseInsert();
            productionDatabaseInsert();
            setPackPackingTime();

        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        int click = machineDowntimeTable.getEditingRow();

        if (click == -1) {
            timeCalculateDown();
            hiddenTable();
            machineDownPreDatabaseInsert();
            machineDowntimeDatabaseInstert();

        } else {
            machineDowntimeTable.getCellEditor().stopCellEditing();
            timeCalculateDown();
            hiddenTable();
            machineDownPreDatabaseInsert();
            machineDowntimeDatabaseInstert();
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void machineDowntimeTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_machineDowntimeTableKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_machineDowntimeTableKeyReleased

    private void machineDowntimeTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_machineDowntimeTableKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int click = machineWorkedTable.getEditingRow();

            if (click == -1) {
                timeCalculateDown();
                hiddenTable();

            } else {
                machineWorkedTable.getCellEditor().stopCellEditing();
                timeCalculateDown();
                hiddenTable();
            }

        }

        int rowCount = machineWorkedTable.getRowCount();
        if (evt.getKeyCode() == KeyEvent.VK_INSERT) {

            int selectedRow = machineWorkedTable.getSelectedRow();

            int filledRows = filledRows(machineWorkedTable);
            if (filledRows == rowCount) {

                rowOperations.addRowBelow(machineWorkedTable);
            }

        }
    }//GEN-LAST:event_machineDowntimeTableKeyPressed

    private void machineDowntimeTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_machineDowntimeTableMouseReleased
        // TODO add your handling code here:
        if (evt.isPopupTrigger()) {
            int row = machineDowntimeTable.getSelectedRow();

            if (row >= 0) {
                machineDowntime.show(this, evt.getX() + 50, evt.getY() + 220);
            }

        }
    }//GEN-LAST:event_machineDowntimeTableMouseReleased

    private void machineWorkedTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_machineWorkedTableKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int click = machineWorkedTable.getEditingRow();

            if (click == -1) {
                timeCalculate();

            } else {
                machineWorkedTable.getCellEditor().stopCellEditing();
                timeCalculate();
            }

        }
    }//GEN-LAST:event_machineWorkedTableKeyPressed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        int click = rejectAnalysisTable.getEditingRow();

        if (click == -1) {

            oeeReject();
            packOEEReject();
            rejectAnalysisTotal();
            rejectPreDatabaseInsert();
            rejectAnalysisDatabaseInsert();

        } else {

            rejectAnalysisTable.getCellEditor().stopCellEditing();
            oeeReject();
            packOEEReject();
            rejectAnalysisTotal();
            rejectPreDatabaseInsert();
            rejectAnalysisDatabaseInsert();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void rejectAnalysisTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rejectAnalysisTableKeyPressed
        // TODO add your handling code here:

        int click = rejectAnalysisTable.getEditingRow();

        if (click == -1) {
            if (evt.getKeyCode() == KeyEvent.VK_INSERT) {
                int rowCount = rejectAnalysisTable.getRowCount();
                System.out.println(rowCount);

                int selectedRow = rejectAnalysisTable.getSelectedRow();
                System.out.println(selectedRow);

                if (rowCount > 0) {

                    rowOperations.addRowbottom(rejectAnalysisTable);
                }

            }
            //calculate.prodOEEClone(productionTable, machineWorkedTable, totalDownTable, oeeTable);

        } else {
            rejectAnalysisTable.getCellEditor().stopCellEditing();
            if (evt.getKeyCode() == KeyEvent.VK_INSERT) {
                int rowCount = rejectAnalysisTable.getRowCount();
                System.out.println(rowCount);

                int selectedRow = rejectAnalysisTable.getSelectedRow();
                System.out.println(selectedRow);

                if (rowCount > 0) {
                    //rejectAnalysisTable.getCellEditor().stopCellEditing();
                    rowOperations.addRowbottom(rejectAnalysisTable);
                }

            }
            //calculate.prodOEEClone(productionTable, machineWorkedTable, totalDownTable, oeeTable);
        }

    }//GEN-LAST:event_rejectAnalysisTableKeyPressed

    private void rejectAnalysisTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rejectAnalysisTableMouseReleased
        // TODO add your handling code here:
        //if (evt.isPopupTrigger()) {
        // int row = rejectAnalysisTable.getSelectedRow();

        //  if (row >= 0) {
        //       rejectAnalysis.show(this, evt.getX() + 50, evt.getY() + 400);
        //    }
        //   }
    }//GEN-LAST:event_rejectAnalysisTableMouseReleased

    private void jScrollPane9KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jScrollPane9KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane9KeyPressed

    private void productionTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_productionTableKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_productionTableKeyReleased

    private void productionTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_productionTableKeyPressed
        // TODO add your handling code here:

        createKeybindingsEnter(productionTable);
        int rowCount = productionTable.getRowCount();
        if (evt.getKeyCode() == KeyEvent.VK_INSERT) {

            int selectedRow = productionTable.getSelectedRow();

            int filledRows = filledRows(productionTable);
            if (filledRows == rowCount) {
                //productionTable.getCellEditor().stopCellEditing();
                rowOperations.addRowbottom(productionTable);
            }

        }

        /* else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int click = productionTable.getEditingRow();

            if (click == -1) {
                prodOEE();
            } else {
                productionTable.getCellEditor().stopCellEditing();
                prodOEE();

            }

        }*/
    }//GEN-LAST:event_productionTableKeyPressed

    private void productionTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productionTableMouseReleased
        // TODO add your handling code here:
        if (evt.isPopupTrigger()) {
            int row = productionTable.getSelectedRow();
            productionTable.getCellEditor().stopCellEditing();

            if (row >= 0) {
                rowAdderDeleter.show(this, evt.getX() + 50, evt.getY() + 200);
            }

        }
    }//GEN-LAST:event_productionTableMouseReleased

    private void productionTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productionTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_productionTableMouseClicked

    private void technicianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_technicianActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_technicianActionPerformed

    private void nowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nowActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nowActionPerformed
    private void itemNumberComboBoxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent e) {
        databaseOperations dops = new databaseOperations();

        newItem ni = new newItem();
        ni.setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    //javax.swing.UIManager.setLookAndFeel("com.alee.laf.WebLookAndFeel");
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new main().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem addAbove;
    private javax.swing.JMenuItem addAboveMD;
    private javax.swing.JMenuItem addAboveReject;
    private javax.swing.JMenuItem addBelow;
    private javax.swing.JMenuItem addBelowMD;
    private javax.swing.JMenuItem addBelowReject;
    private javax.swing.JPanel hidPanel;
    private javax.swing.JTable intermTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPopupMenu machineDowntime;
    private javax.swing.JTable machineDowntimeTable;
    private javax.swing.JTable machineWorkedTable;
    private javax.swing.JTextField now;
    private javax.swing.JTable packageTimeTable;
    private javax.swing.JTable packingOeeTable;
    private javax.swing.JTable packinterimTable;
    private javax.swing.JTable plannedDownTable;
    private javax.swing.JTable productionOeeTable;
    public static javax.swing.JTable productionTable;
    private javax.swing.JPopupMenu rejectAnalysis;
    private javax.swing.JTable rejectAnalysisTable;
    private javax.swing.JMenuItem removeAbove;
    private javax.swing.JMenuItem removeAboveMD;
    private javax.swing.JMenuItem removeAboveReject;
    private javax.swing.JMenuItem removeBelow;
    private javax.swing.JMenuItem removeBelowMD;
    private javax.swing.JMenuItem removeBelowReject;
    private javax.swing.JMenuItem removeThisRow;
    private javax.swing.JMenuItem removeThisRowMD;
    private javax.swing.JMenuItem removeThisRowReject;
    private javax.swing.JPopupMenu rowAdderDeleter;
    private javax.swing.JComboBox<String> shiftCombo;
    private javax.swing.JTextField supervisor;
    private javax.swing.JTextField technician;
    private javax.swing.JTable totalDownTable;
    // End of variables declaration//GEN-END:variables

}
