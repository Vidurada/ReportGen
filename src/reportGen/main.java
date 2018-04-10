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
        setItemNumberColumn(productionTable, productionTable.getColumnModel().getColumn(3));
        setBaseColumn(rejectAnalysisTable, rejectAnalysisTable.getColumnModel().getColumn(0));
        setItemNumberColumn(downtimeTable, downtimeTable.getColumnModel().getColumn(0));
        hiddenPanel.setVisible(false);
        totalDownTable.setVisible(false);
        jPanel1.setVisible(false);
        jPanel4.setVisible(false);
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
            prodTableInsert.setEnabled(false);
            //prodTableUpdate.setEnabled(rootPaneCheckingEnabled);
            JOptionPane.showMessageDialog(null, "Saved Entries in the Database", "Successful!", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }

    public void prodOEE() {
        try {
            productionTable.getCellEditor().stopCellEditing();

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
                String shift = shiftCombo.getSelectedItem().toString();
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
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

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

    public void timeCalculate() {
        try {
            machineWorkedTable.getCellEditor().stopCellEditing();
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

            for (int row = 0; row < fullRows; row++) {
                String PartNumber = (String) productionTable.getValueAt(row, 3);
                String st_time = (String) machineWorkedTable.getValueAt(row, 2);
                String end_time = (String) machineWorkedTable.getValueAt(row, 3);

                //System.out.println(SOnumber);
                SimpleDateFormat format = new SimpleDateFormat("HH.mm");
                Date date1 = format.parse(st_time);
                Date date2 = format.parse(end_time);
                long difference = (date2.getTime() - date1.getTime()) / (1000 * 60);

                machineWorkedTable.setValueAt(difference, row, 4);

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
                int int_gty = columnSum(row + 1);
                //String pack_qty = (String) productionTable.getValueAt(row, 5);

                //pst.setString(1, partNumber);
                String shift = shiftCombo.getSelectedItem().toString();
                pst.setInt(1, int_gty);
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

    public void hiddenTable(){
        downtimeTable.getCellEditor().stopCellEditing();
        
    
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
            
            
            int emptyRowss = 0;
            rowSearch:
            for (int row = 0; row < downtimeTable.getRowCount(); row++) { //Iterate through all the rows
                for (int col = 0; col < downtimeTable.getColumnCount(); col++) { //Iterate through all the columns in the row
                    if (downtimeTable.getValueAt(row, col) != null) { //Check if the box is empty
                        continue rowSearch; //If the value is not null, the row contains stuff so go onto the next row
                    }
                }
                emptyRowss++; //Conditional never evaluated to true so none of the columns in the row contained anything
            }
            
            
            

            //System.out.println(emptyRows);
            int rows = productionTable.getRowCount();
            //System.out.println(rows);

            int fullRows = rows - emptyRows;
            int rowers= downtimeTable.getRowCount();
            
            int rower= rowers - emptyRowss;
            
             for (int row = 0; row < fullRows; row++) {
                totalDownTable.setValueAt(0, row, 1);
             
             }
            
            
            
            //System.out.println(rower);
            for (int row = 0; row < fullRows; row++) {
                
                String partNumber = (String) productionTable.getValueAt(row, 3);
                
                 //System.out.println(partNumber);
                 
                for (int roww = 0; roww < rower; roww++){
                    
                     String partNumberd = (String) downtimeTable.getValueAt(roww, 0);
                     //System.out.println(partNumber);
                     
                     if (partNumber.equals(partNumberd)){
                         
                         int qty = (int) totalDownTable.getValueAt(row, 1);
                         //int add_gty = Integer.parseInt(qty);
                         System.out.println(qty);
                         
                         String addy = (String) downtimeTable.getValueAt(roww, 3);
                         int add = Integer.parseInt(addy);
                         System.out.println(addy);
                         
                         int summ = add + qty ; 
                         System.out.println(summ);
                         
                        
                     totalDownTable.setValueAt(summ, row, 1);
                     
                     
                     }
                
                
                
                
                }
                
                
                
                
                
                
                
    
    
    }
            
            
            for (int row = 0; row < fullRows; row++) {
                int addy = (int) totalDownTable.getValueAt(row, 1);
                //int add = Integer.parseInt(addy);
                oeeTable.setValueAt(addy, row, 7);
             
             }
            
    
    
    }  
    
    
    
    
    
    
    public void oeeReject() {
        try {
            rejectAnalysisTable.getCellEditor().stopCellEditing();

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

                String partNumber = (String) productionTable.getValueAt(row, 3);
                int wastage = columnSum(row + 1);
                //String pack_qty = (String) productionTable.getValueAt(row, 5);

                oeeTable.setValueAt(wastage, row, 3);
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

    public int columnSum(int n) {

        int rowsCount = rejectAnalysisTable.getRowCount();

        int sum = 0;
        for (int i = 0; i < rowsCount; i++) {
            //int amount = integer.parseInt(rejectAnalysisTable.getValueAt(i, 2).toString());
            Object value = rejectAnalysisTable.getValueAt(i, n);
            if (value != null) {
                sum = sum + Integer.parseInt(rejectAnalysisTable.getValueAt(i, n).toString());
            }
        }
        return sum;

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
        reportArea = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        productionTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        prodTableInsert = new javax.swing.JButton();
        addRow = new javax.swing.JButton();
        removeRow = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        prodTableUpdate = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        rejectAnalysisTable = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        item2 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        machineWorkedTable = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        downtimeTable = new javax.swing.JTable();
        jButton8 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        totalDownTable = new javax.swing.JTable();
        hiddenPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        oeeTable = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        reportArea.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseReleased(evt);
            }
        });

        productionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No", "SO Number", "Customer", "Part Number", "Produced Qty", "Packed Qty"
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
        jScrollPane1.setViewportView(productionTable);
        if (productionTable.getColumnModel().getColumnCount() > 0) {
            productionTable.getColumnModel().getColumn(0).setMaxWidth(35);
        }

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Total Packed Qty :");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Total Produced Qty :");

        prodTableInsert.setText("Insert");
        prodTableInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prodTableInsertActionPerformed(evt);
            }
        });

        addRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRowActionPerformed(evt);
            }
        });

        removeRow.setText("jButton3");
        removeRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeRowActionPerformed(evt);
            }
        });

        jButton2.setText("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        prodTableUpdate.setText("Update");
        prodTableUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prodTableUpdateActionPerformed(evt);
            }
        });

        jButton3.setText("OEE Insert");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton3)
                        .addGap(18, 18, 18)
                        .addComponent(prodTableUpdate)
                        .addGap(18, 18, 18)
                        .addComponent(prodTableInsert)
                        .addGap(6, 6, 6))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel2)
                                .addGap(124, 124, 124)
                                .addComponent(jLabel1))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1255, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addRow, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(removeRow, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(addRow, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(removeRow, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(prodTableUpdate)
                    .addComponent(prodTableInsert))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Production", jPanel5);

        rejectAnalysisTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Base", "Item 1", "Item 2", "Item 3", "Item 4/5", "Item 6", "Item 7", "Total"
            }
        ));
        rejectAnalysisTable.setColumnSelectionAllowed(true);
        jScrollPane2.setViewportView(rejectAnalysisTable);
        rejectAnalysisTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jButton1.setText("Sum");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setText("Item 1");

        item2.setText("Item 2");

        jButton5.setText("OEE Insert");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1257, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(228, 228, 228)
                        .addComponent(jLabel4)
                        .addGap(117, 117, 117)
                        .addComponent(item2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jLabel4)
                    .addComponent(item2)
                    .addComponent(jButton5))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Reject Analysis", jPanel6);

        jButton4.setText("Calculate");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

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
        jScrollPane6.setViewportView(machineWorkedTable);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setText("Machine Worked");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setText("Machine Downtime");

        downtimeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "PartNumber", "From", "To", "No of Mins", "Reason"
            }
        ));
        jScrollPane4.setViewportView(downtimeTable);

        jButton8.setText("Insert");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        totalDownTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 424, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(24, 24, 24)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(24, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 277, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(46, 46, 46)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(46, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 685, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(jButton8))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 575, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel9)))
                .addGap(48, 48, 48)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(20, 20, 20)
                    .addComponent(jLabel6)
                    .addContainerGap(1131, Short.MAX_VALUE)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton8)
                .addGap(234, 234, 234))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addComponent(jLabel6)
                    .addContainerGap(494, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Down Time Summery", jPanel7);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setText("Planned Machine Downtime");

        javax.swing.GroupLayout hiddenPanelLayout = new javax.swing.GroupLayout(hiddenPanel);
        hiddenPanel.setLayout(hiddenPanelLayout);
        hiddenPanelLayout.setHorizontalGroup(
            hiddenPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hiddenPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addContainerGap(1040, Short.MAX_VALUE))
        );
        hiddenPanelLayout.setVerticalGroup(
            hiddenPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hiddenPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel7)
                .addContainerGap(502, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Planned Downtime", hiddenPanel);

        oeeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Item", "SO Number", "SWO", "Total Reject/Rework", "Total Output", "Total Available Time", "Planned Downtime", "Downtime", "Operating Time", "Ideal Run Rate", "Avialability Rate", "Performance Rate", "Quality Rate", "Plant OEE"
            }
        ));
        jScrollPane8.setViewportView(oeeTable);

        jButton6.setText("print");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Calculate");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 1284, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(jButton7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("OEE", jPanel3);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1304, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 546, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab6", jPanel4);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1309, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 577, Short.MAX_VALUE)
                .addContainerGap())
        );

        reportArea.addTab("Extrusion Process", jPanel2);

        getContentPane().add(reportArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 1340, 630));
        reportArea.getAccessibleContext().setAccessibleName("Report01");

        shiftCombo.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        shiftCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Day", "Night" }));
        getContentPane().add(shiftCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(1250, 30, -1, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Shift");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1200, 30, -1, -1));

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

    private void jTabbedPane1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPane1MouseReleased

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        timeCalculate();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        oeeReject();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        //item2.setText( Integer.toString(columnSum(3)));
        insertDatabaseReject();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        prodOEE();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void prodTableUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prodTableUpdateActionPerformed
        // TODO add your handling code here:
        updateRows();

    }//GEN-LAST:event_prodTableUpdateActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void removeRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeRowActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) productionTable.getModel();
        //int removeRow=1;
        int removeRows = productionTable.getRowCount();
        model.removeRow(removeRows - 1);
    }//GEN-LAST:event_removeRowActionPerformed

    private void addRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRowActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) productionTable.getModel();

        Vector row = new Vector();
        row.add(null);
        row.add(null);
        row.add(null);
        row.add(null);
        row.add(null);
        row.add(null);
        model.addRow(row);
    }//GEN-LAST:event_addRowActionPerformed

    private void prodTableInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prodTableInsertActionPerformed
        // TODO add your handling code here:
        //insertDatabaseProd();
        //autoUpdateDownTime();
        insertDatabaseProdPack();
    }//GEN-LAST:event_prodTableInsertActionPerformed

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

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        hiddenTable();
    }//GEN-LAST:event_jButton8ActionPerformed

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
    private javax.swing.JButton addRow;
    private javax.swing.JTable downtimeTable;
    private javax.swing.JPanel hiddenPanel;
    private javax.swing.JLabel item2;
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
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable machineWorkedTable;
    private javax.swing.JTable oeeTable;
    private javax.swing.JButton prodTableInsert;
    private javax.swing.JButton prodTableUpdate;
    private javax.swing.JTable productionTable;
    private javax.swing.JTable rejectAnalysisTable;
    private javax.swing.JMenuItem removeAbove;
    private javax.swing.JMenuItem removeBelow;
    private javax.swing.JButton removeRow;
    private javax.swing.JMenuItem removeThisRow;
    private javax.swing.JTabbedPane reportArea;
    private javax.swing.JPopupMenu rowAdderDeleter;
    private javax.swing.JComboBox<String> shiftCombo;
    private javax.swing.JTable totalDownTable;
    // End of variables declaration//GEN-END:variables
}
