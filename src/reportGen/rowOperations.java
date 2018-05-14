/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reportGen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author ViduraDan
 */
public class rowOperations {
    
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    
    
    

    public static void addRowAbove(JTable table) {
        int rows = table.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Vector row = new Vector();
        row.add(null);
        row.add(null);
        row.add(null);
        row.add(null);
        row.add(null);
        row.add(null);
        model.insertRow(rows, row);
    }
    
    public static void addRowbottom(JTable table) {
        int Rows = table.getRowCount();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Vector row = new Vector();
        row.add(null);
        row.add(null);
        row.add(null);
        row.add(null);
        row.add(null);
        row.add(null);
        model.insertRow(Rows, row);
    }
    
     public static void addColumn(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int cols = table.getColumnCount();
        System.out.println(cols);      
        model.addColumn("Item"+(cols-1));
        table.moveColumn(table.getColumnModel().getColumnIndex("Total"), table.getColumnCount()-1);
        
    }
    
    public static void setRowNumber(JTable table,int fullRows){
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        //int rowCount = table.getRowCount();
        model.setRowCount(fullRows);

    }
    
    
    public static void addRowBelow(JTable table) {
        int rows = table.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Vector row = new Vector();
        row.add(null);
        row.add(null);
        row.add(null);
        row.add(null);
        row.add(null);
        row.add(null);
        model.insertRow(rows + 1, row);
    }

    public static void removeRowAbove(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        //int removeRow=1;
        int rows = table.getSelectedRow();
        model.removeRow(rows - 1);

    }

    public static void removeRowBelow(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        table.getSelectionModel().clearSelection();

        int rows = table.getSelectedRow();
        //System.out.println(rows);
        int allRows = table.getRowCount();
        //System.out.println(allRows);
        if (rows < allRows - 1) {
            model.removeRow(rows + 1);
        }

    }

    public static void removeThisRow(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        //int removeRow=1;
        int rows = table.getSelectedRow();
        model.removeRow(rows);
    }

    public static void removeFromBottom(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        //int removeRow=1;
        int removeRows = table.getRowCount();
        model.removeRow(removeRows - 1);
    }

      
    
    
    public static void biProducts(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        //int removeRow=1;

        
        
        int[] rows = table.getSelectedRows();
        int max = rows[0];
        String b= "B";
        
        for (int counter = 0; counter < rows.length; counter++) {
            table.setValueAt(b, counter, 6);
        }
        
        //System.out.println(max);
        //table.isCellEditable(max, 2);
    }

    

    }
    
    
    
    
    
    
    

