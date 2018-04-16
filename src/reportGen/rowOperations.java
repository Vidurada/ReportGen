/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reportGen;

import java.util.Arrays;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ViduraDan
 */
public class rowOperations {

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
