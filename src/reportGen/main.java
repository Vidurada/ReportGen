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
import static reportGen.rowOperations.setRowNumber;

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
        //partnumberComboBox();
        //baseComboBox();
        //rejectPartNumberComboBox();
        databaseOperations cellFill = new databaseOperations();
        cellFill.setItemNumberColumn(productionTable, productionTable.getColumnModel().getColumn(3));
        cellFill.setDowntimeReasonColumn(machineDowntimeTable, machineDowntimeTable.getColumnModel().getColumn(5));
        //cellFill.itemNumberComboBoxPopupMenuWillBecomeInvisible();
        //cellFill.setItemNumberColumn(downtimeTable, downtimeTable.getColumnModel().getColumn(2));
        setBaseColumn(rejectAnalysisTable, rejectAnalysisTable.getColumnModel().getColumn(0));
        hidPanel.setVisible(false);
        totalDownTable.setVisible(true);
        //jPanel1.setVisible(false);
        //hitTab(productionTable);
        downTimeColumn(machineDowntimeTable, machineDowntimeTable.getColumnModel().getColumn(0));
        //jPanel4.setVisible(false);
        productionTable.setRowSelectionAllowed(true);
        //timeCalculate();

    }

    /*
    public void hitTab(JTable table) {
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        InputMap im = table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        im.put(enter, im.get(tab));

    }*/
    public void nothing() {

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reportgen?useSSL=true", "vidura", "vidura");
            String queryco = "Insert into parts(PartNumber,swo) values (?,?);";
            pst = connection.prepareStatement(queryco);

            //String partNumber = (String) newItem.itemNameText.getText();
            String swo = (String) newItem.swoText.getText();

//            pst.setString(1, partNumber);
            pst.setString(2, swo);

            pst.execute();
            pst.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }

    public void insertDatabaseProdPack() {
        try {
            productionTable.getCellEditor().stopCellEditing();
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
            String queryco = "Insert into production(SO_Num,Customer,Date,Shift,PartNumber,prod_qty,pack_qty) values (?,?,?,?,?,?,?);";
            pst = connection.prepareStatement(queryco);

            int fullRows = rows - emptyRows;

            for (int row = 0; row < fullRows; row++) {
                String SOnumber = (String) productionTable.getValueAt(row, 1);
                //System.out.println(SOnumber);
                String customer = (String) productionTable.getValueAt(row, 2);
                String shift = shiftCombo.getSelectedItem().toString();
                String partNumber = (String) productionTable.getValueAt(row, 3);
                String qty = (String) productionTable.getValueAt(row, 4);
                int int_gty = Integer.parseInt(qty);
                String pack_qty = (String) productionTable.getValueAt(row, 5);
                int int_pack_qty = Integer.parseInt(pack_qty);

                machineWorkedTable.setValueAt(row + 1, row, 0);
                machineWorkedTable.setValueAt(partNumber, row, 1);
                //String PartNumber = (String) productionTable.getValueAt(row, 3);

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
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }

    private void packOEEReject() {

        int rows = rejectAnalysisTable.getRowCount();
        int cols = rejectAnalysisTable.getColumnCount();

        //iterete through reject analysis table
        for (int i = 0; i < rows - 1; i++) {
            //get the base 
            String base = (String) rejectAnalysisTable.getValueAt(i, 0);
            //check whether the base is cutter wastage
            if ("Cutter wastage".equals(base)) {
                //iterate through colums to get values
                for (int j = 1; j < cols - 1; j++) {
                    //make sure no null and empty values taken to calculation
                    if (productionTable.getValueAt(j - 1, 5) != null) {
                        String pack_qty = (String) productionTable.getValueAt(j - 1, 5);
                        String part_name = (String) productionTable.getValueAt(j - 1, 3);
                        if (!pack_qty.isEmpty()) {
                            if (rejectAnalysisTable.getValueAt(i, j) != null) {
                                String rej = (String) rejectAnalysisTable.getValueAt(i, j);

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

            int fullRows = filledRows(productionTable);
            //this variable defines the rows and columns in hidden tables
            int bb = -1;
            for (int row = 0; row < fullRows; row++) {
                // check weather the 5th column is not null
                if (productionTable.getValueAt(row, 5) != null) {
                    String pack_qty = (String) productionTable.getValueAt(row, 5);

                    //proceed if the cell is not empty
                    if (!pack_qty.isEmpty()) {
                        bb = bb + 1;
                        //get values form the production table

                        String partNumber = (String) productionTable.getValueAt(row, 3);

                        float float_pack_qty = Float.parseFloat(pack_qty);
                        packinterimTable.setValueAt(float_pack_qty, bb, 1);
                        packingOeeTable.setValueAt(float_pack_qty, bb, 5);
                        packageTimeTable.setValueAt(partNumber, bb, 1);
                        packageTimeTable.setValueAt(bb + 1, bb, 0);

                        packinterimTable.setValueAt(partNumber, bb, 0);
                        itemFill(packinterimTable, 2);

                        setRowNumber(packinterimTable, fullRows);
                        totalDownTable.setValueAt(partNumber, row, 0);

                        //add the values to the productionOEEExcel table
                        setRowNumber(packingOeeTable, fullRows);
                        packingOeeTable.setValueAt(bb + 1, bb, 0);
                        packingOeeTable.setValueAt(partNumber, bb, 1);

                        String[] parts = partNumber.split("-");
                        String cut_length = parts[2];
                        packingOeeTable.setValueAt(cut_length, bb, 2);

                        //get the swo value from the database for each part
                        java.sql.PreparedStatement preparedStatement = null;
                        String query = "select swo from parts where PartNumber=?";
                        preparedStatement = conn.prepareStatement(query);
                        preparedStatement.setString(1, partNumber);
                        ResultSet rs = preparedStatement.executeQuery();
                        String season = null;

                        if (rs.next()) {
                            season = rs.getString("swo");
                            System.out.println(season);
                            packingOeeTable.setValueAt(season, bb, 3);

                        }

                    }

                }

            }

        } catch (Exception e) {
        }

    }

    public void prodOEE() {
        try {

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
                        bb = bb + 1;
                        //get values form the production table
                        String SOnumber = (String) productionTable.getValueAt(row, 1);
                        String customer = (String) productionTable.getValueAt(row, 2);
                        String shift = shiftCombo.getSelectedItem().toString();
                        String partNumber = (String) productionTable.getValueAt(row, 3);

                        //insert new items to database
                        databaseOperations dop = new databaseOperations();
                        dop.insertIfNotExist(productionTable, partNumber, row);

                        String second_qty = (String) productionTable.getValueAt(row, 4);
                        float float_qty = Float.parseFloat(second_qty);
                        System.out.println(float_qty);

                        intermTable.setValueAt(partNumber, bb, 0);
                        itemFill(intermTable, 2);

                        //set index and partnumber in machine worked table
                        setRowNumber(machineWorkedTable, fullRows);
                        machineWorkedTable.setValueAt(bb + 1, bb, 0);
                        machineWorkedTable.setValueAt(partNumber, bb, 1);

                        //add the partnumber record in 'hidden' totaDownTable
                        setRowNumber(totalDownTable, fullRows);
                        setRowNumber(plannedDownTable, fullRows);
                        setRowNumber(intermTable, fullRows);
                        totalDownTable.setValueAt(partNumber, row, 0);

                        //add the values to the productionOEEExcel table
                        setRowNumber(productionOeeTable, fullRows);
                        productionOeeTable.setValueAt(bb + 1, bb, 0);
                        productionOeeTable.setValueAt(partNumber, bb, 1);
                        productionOeeTable.setValueAt(SOnumber, bb, 2);
                        productionOeeTable.setValueAt(float_qty, bb, 5);

                        //packagetime table
                        packageTimeTable.setValueAt(bb + 1, bb, 0);
                        packageTimeTable.setValueAt(partNumber, bb, 1);

                        //get the swo value from the database for each part
                        java.sql.PreparedStatement preparedStatement = null;
                        String query = "select swo from parts where PartNumber=?";
                        preparedStatement = conn.prepareStatement(query);
                        preparedStatement.setString(1, partNumber);
                        ResultSet rs = preparedStatement.executeQuery();
                        String season = null;

                        if (rs.next()) {
                            season = rs.getString("swo");
                            System.out.println(season);
                            productionOeeTable.setValueAt(season, bb, 3);

                        }

                    }

                }

            }

            if (fullRows > 5) {
                addColumn(rejectAnalysisTable);
            }

        } catch (Exception e) {
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
                float int_gty = columnSum(row + 1);
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

        //System.out.println(rower);
        for (int row = 0; row < fullRows; row++) {

            String partNumber = (String) intermTable.getValueAt(row, 2);

            //System.out.println(partNumber);
            for (int roww = 0; roww < rower; roww++) {

                String partNumberd = (String) machineDowntimeTable.getValueAt(roww, 1);
                //System.out.println(partNumber);

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

        String[] itemArray = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7", "Item 8"};
        int row = filledRows(table);
        for (int i = 0; i < row; i++) {
            table.setValueAt(itemArray[i], i, column);

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

    public void packagingOeeCalculation() throws SQLException {
        int deduct = 0;
        int no_min = 0;
        int fullRows = filledRows(packageTimeTable);
        for (int row = 0; row < fullRows; row++) {

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

            String cutlength = (String) packingOeeTable.getValueAt(row, 2);

            java.sql.PreparedStatement preparedStatement = null;
            String query = "select Wastage from standardwastages where CutLength=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, cutlength);
            ResultSet rs = preparedStatement.executeQuery();
            String standard_wastages = null;

            if (rs.next()) {
                standard_wastages = rs.getString("Wastage");

            }

            float cutter_wastage_float = (float) packinterimTable.getValueAt(row, 3);
            float standard_wastages_float = Float.parseFloat(standard_wastages) / 100;

            float packed_Qty_float = (float) packingOeeTable.getValueAt(row, 5);

            float deviation = cutter_wastage_float - ((packed_Qty_float / (1 - standard_wastages_float)) - packed_Qty_float);

            if (deviation > 0) {
                packingOeeTable.setValueAt(deviation, row, 4);
            } else {
                float zero= 0;
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

            float o_time_double = o_time;
            float t_available_double = t_available;
            float pd_time_double = pd_time;

            float a_rate = o_time_double / (t_available_double - pd_time_double);
            float a_rate_percent = a_rate * 100;
            Float tobe_a_rate = a_rate_percent;

            Float truncatedDouble = BigDecimal.valueOf(tobe_a_rate)
                    .setScale(2, RoundingMode.HALF_UP)
                    .floatValue();

            packingOeeTable.setValueAt(truncatedDouble, row, 11);

            //calculate performance rate
            float t_output = (float) packingOeeTable.getValueAt(row, 5);
            float t_output_double = t_output;
            float c_rate = t_output_double / o_time_double;

            if (ideal_run_rate <= c_rate) {

                packingOeeTable.setValueAt(100.00, row, 12);

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
            float t_reject_double = t_reject;

            float q_rate = (t_output_double - t_reject_double) / t_output_double;
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

            float o_time_double = o_time;
            float t_available_double = t_available;
            float pd_time_double = pd_time;

            float a_rate = o_time_double / (t_available_double - pd_time_double);
            float a_rate_percent = a_rate * 100;
            Float tobe_a_rate = a_rate_percent;

            Float truncatedDouble = BigDecimal.valueOf(tobe_a_rate)
                    .setScale(2, RoundingMode.HALF_UP)
                    .floatValue();

            productionOeeTable.setValueAt(truncatedDouble, row, 11);

            //calculate performance rate
            float t_output = (float) productionOeeTable.getValueAt(row, 5);
            float t_output_double = t_output;
            float c_rate = t_output_double / o_time_double;

            if (ideal_run_rate <= c_rate) {

                productionOeeTable.setValueAt(100.00, row, 12);

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
            float t_reject_double = t_reject;

            float q_rate = (t_output_double - t_reject_double) / t_output_double;
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

                float wastage = columnSum(row + 1);
                //String pack_qty = (String) productionTable.getValueAt(row, 5);

                productionOeeTable.setValueAt(wastage, row, 4);
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

    public float columnSum(int n) {

        int rowsCount = rejectAnalysisTable.getRowCount();

        float sum = 0;
        for (int i = 0; i < rowsCount; i++) {
            //int amount = integer.parseInt(rejectAnalysisTable.getValueAt(i, 2).toString());
            Object value = rejectAnalysisTable.getValueAt(i, n);
            String qty = (String) rejectAnalysisTable.getValueAt(i, n);
            if (value != null) {
                if (!qty.isEmpty()) {
                    sum = sum + Float.parseFloat(rejectAnalysisTable.getValueAt(i, n).toString());
                }
            }
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
        biProduct = new javax.swing.JMenuItem();
        reportArea = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
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
        jButton4 = new javax.swing.JButton();
        hidPanel = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        plannedDownTable = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        intermTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        totalDownTable = new javax.swing.JTable();
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
        jScrollPane12 = new javax.swing.JScrollPane();
        packinterimTable = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
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
        shiftCombo = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
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

        biProduct.setText("Bi Product");
        biProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                biProductActionPerformed(evt);
            }
        });
        rowAdderDeleter.add(biProduct);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(102, 102, 102));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        reportArea.setForeground(new java.awt.Color(51, 51, 51));
        reportArea.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

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
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "SO Number", "Customer", "Part Number", "Produced Qty", "Packed Qty", ""
            }
        ));
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
            productionTable.getColumnModel().getColumn(6).setPreferredWidth(20);
            productionTable.getColumnModel().getColumn(6).setMaxWidth(20);
        }

        jPanel5.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 52, 1182, 149));

        rejectAnalysisTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Base", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Total"
            }
        ));
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

        jPanel5.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 274, 1182, 125));

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icon.png"))); // NOI18N
        jButton5.setText("OEE Insert");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 410, -1, -1));

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

        jPanel5.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 476, 1180, 163));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/plus-and-minus.png"))); // NOI18N
        jButton4.setText("Calculate");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 650, -1, -1));

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

        javax.swing.GroupLayout hidPanelLayout = new javax.swing.GroupLayout(hidPanel);
        hidPanel.setLayout(hidPanelLayout);
        hidPanelLayout.setHorizontalGroup(
            hidPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hidPanelLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(129, Short.MAX_VALUE))
        );
        hidPanelLayout.setVerticalGroup(
            hidPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hidPanelLayout.createSequentialGroup()
                .addGroup(hidPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(hidPanelLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(hidPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(hidPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(74, Short.MAX_VALUE))
        );

        jPanel5.add(hidPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 470, 380, 170));

        machineDowntimeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
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

        jPanel5.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 728, 1180, 238));

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/plus-and-minus.png"))); // NOI18N
        jButton8.setText("Calculate");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 980, -1, 30));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Production");
        jPanel5.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 24, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("Reject Analysis");
        jPanel5.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 246, -1, -1));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icon.png"))); // NOI18N
        jButton3.setText("OEE Insert");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 212, -1, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("Machine Run Time");
        jPanel5.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 448, -1, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("Machine Down Time");
        jPanel5.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 695, -1, -1));

        jButton6.setText("OEE Create");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 1260, -1, -1));

        jButton7.setText("Prod OEE Excel");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 1260, -1, -1));

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

        jPanel5.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 1020, 1190, 200));

        jButton2.setText("Test");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 1260, -1, -1));

        packinterimTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
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

        jPanel5.add(jScrollPane12, new org.netbeans.lib.awtextra.AbsoluteConstraints(1280, 350, 210, 210));

        jButton1.setText("Pack OEE Excel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 1260, -1, -1));

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
                .addGap(0, 458, Short.MAX_VALUE))
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 1335, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 486, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(262, Short.MAX_VALUE))
        );

        reportArea.addTab("Shift Report", jPanel2);

        getContentPane().add(reportArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 1340, 790));
        reportArea.getAccessibleContext().setAccessibleName("Report01");

        shiftCombo.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        shiftCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Day", "Night" }));
        getContentPane().add(shiftCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(1260, 20, 80, 30));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Shift");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 20, 50, 30));

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


    private void biProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_biProductActionPerformed
        // TODO add your handling code here:
        rowOperations.biProducts(productionTable);
    }//GEN-LAST:event_biProductActionPerformed

    private void jTabbedPane1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPane1MouseReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:

        // databaseOperations calculate = new databaseOperations();
        int click = productionTable.getEditingRow();

        if (click == -1) {
            prodOEE();
            packOEE();

        } else {
            productionTable.getCellEditor().stopCellEditing();
            prodOEE();
            packOEE();

        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        int click = machineDowntimeTable.getEditingRow();

        if (click == -1) {
            timeCalculateDown();
            hiddenTable();

        } else {
            machineDowntimeTable.getCellEditor().stopCellEditing();
            timeCalculateDown();
            hiddenTable();
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        int click = machineWorkedTable.getEditingRow();

        if (click == -1) {
            timeCalculate();

        } else {
            machineWorkedTable.getCellEditor().stopCellEditing();
            timeCalculate();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        int click = rejectAnalysisTable.getEditingRow();

        if (click == -1) {
            oeeReject();
            packOEEReject();

        } else {
            rejectAnalysisTable.getCellEditor().stopCellEditing();
            oeeReject();
            packOEEReject();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void productionTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_productionTableKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_productionTableKeyReleased

    private void productionTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productionTableMouseReleased
        // TODO add your handling code here:
        if (evt.isPopupTrigger()) {
            int row = productionTable.getSelectedRow();

            if (row >= 0) {
                rowAdderDeleter.show(this, evt.getX() + 50, evt.getY() + 200);
            }

        }
    }//GEN-LAST:event_productionTableMouseReleased

    private void productionTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productionTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_productionTableMouseClicked

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

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        prodOEE();
        productionOeeCalculation();

        //excal.production(productionTable);
    }//GEN-LAST:event_jButton6ActionPerformed

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

    private void jScrollPane9KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jScrollPane9KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane9KeyPressed

    private void machineDowntimeTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_machineDowntimeTableKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_machineDowntimeTableKeyReleased

    private void machineDowntimeTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_machineDowntimeTableMouseReleased
        // TODO add your handling code here:
        if (evt.isPopupTrigger()) {
            int row = productionTable.getSelectedRow();

            if (row >= 0) {
                rowAdderDeleter.show(this, evt.getX() + 50, evt.getY() + 200);
            }

        }
    }//GEN-LAST:event_machineDowntimeTableMouseReleased

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
        if (evt.isPopupTrigger()) {
            int row = productionTable.getSelectedRow();

            if (row >= 0) {
                rowAdderDeleter.show(this, evt.getX() + 50, evt.getY() + 200);
            }

        }
    }//GEN-LAST:event_rejectAnalysisTableMouseReleased

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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        timeCalculatePacking();
        try {
            packagingOeeCalculation();
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

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
    private javax.swing.JMenuItem addBelow;
    private javax.swing.JMenuItem biProduct;
    private javax.swing.JPanel hidPanel;
    private javax.swing.JTable intermTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel2;
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
    private javax.swing.JTable machineDowntimeTable;
    private javax.swing.JTable machineWorkedTable;
    private javax.swing.JTable packageTimeTable;
    private javax.swing.JTable packingOeeTable;
    private javax.swing.JTable packinterimTable;
    private javax.swing.JTable plannedDownTable;
    private javax.swing.JTable productionOeeTable;
    public static javax.swing.JTable productionTable;
    private javax.swing.JTable rejectAnalysisTable;
    private javax.swing.JMenuItem removeAbove;
    private javax.swing.JMenuItem removeBelow;
    private javax.swing.JMenuItem removeThisRow;
    private javax.swing.JTabbedPane reportArea;
    private javax.swing.JPopupMenu rowAdderDeleter;
    private javax.swing.JComboBox<String> shiftCombo;
    private javax.swing.JTable totalDownTable;
    // End of variables declaration//GEN-END:variables
}
