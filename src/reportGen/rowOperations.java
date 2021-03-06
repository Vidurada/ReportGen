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

    public static void addRowAboveMD(JTable table) {
        int rows = table.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Vector row = new Vector();
        for (int i = 0; i < 7; i++) {
            row.add("");
        }
        model.insertRow(rows, row);
    }

    public static void addRowAboveReject(JTable table) {
        int rows = table.getSelectedRow();
        int columns = table.getColumnCount();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Vector row = new Vector();
        for (int i = 0; i < columns; i++) {
            row.add("");
        }
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

    public static void addRowbottomOee(JTable table) {
        int Rows = table.getRowCount();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Vector row = new Vector();
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        row.add("");
        model.insertRow(Rows, row);
    }

    public static void addColumn(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int cols = table.getColumnCount();
        System.out.println(cols);
        model.addColumn("Item" + (cols - 1));
        table.moveColumn(table.getColumnModel().getColumnIndex("Total"), table.getColumnCount() - 1);

    }

    public static void setRowNumber2(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int rowCount = table.getRowCount();
        model.setRowCount(rowCount);

    }
    
    public static void tableEmpty(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        //int rowCount = table.getRowCount();
        model.setRowCount(0);

    }

    public static void setRowNumber(JTable table, JTable table2, int fullRows, int n) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int realRows = productionFullRows(table2, fullRows, n);
        System.out.println("here" + realRows);
        model.setRowCount(realRows);

    }

    public static int productionFullRows(JTable table, int fullRows, int n) {
        int count = 0;
        for (int i = 0; i < fullRows; i++) {
            if (table.getValueAt(i, n) != null) {
                String val = table.getValueAt(i, n).toString();
                if (!val.isEmpty()) {
                    if (!val.equals("0")) {
                        count = count + 1;
                    }
                }
            }
        }

        return count;

    }

    public static void setRowNumberMachineRunTime(JTable table, int fullRows) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int rowCount = table.getRowCount();
        int dev = fullRows - rowCount;
        for (int i = 0; i < dev; i++) {
            Vector row = new Vector();
            row.add("");
            row.add("");
            row.add("");
            row.add("");
            row.add("");
            row.add("");
            model.insertRow(i, row);
        }

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
        String b = "B";

        for (int counter = 0; counter < rows.length; counter++) {
            table.setValueAt(b, counter, 6);
        }

        //System.out.println(max);
        //table.isCellEditable(max, 2);
    }

    public static void addRowBelowReject(JTable table) {
        int rows = table.getSelectedRow();
        int columns = table.getColumnCount();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Vector row = new Vector();
        for (int i = 0; i < columns; i++) {
            row.add("");
        }

        model.insertRow(rows + 1, row);
    }

    public static void addRowBelowMachine(JTable table) {
        int rows = table.getSelectedRow();

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Vector row = new Vector();
        for (int i = 0; i < 7; i++) {
            row.add("");
        }

        model.insertRow(rows + 1, row);
    }

}
