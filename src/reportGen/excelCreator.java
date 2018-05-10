/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reportGen;

import java.io.File;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.swing.JTable;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author ViduraDan
 */
public class excelCreator {

    public void production(JTable table, File file) {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet1 = workbook.createSheet("Production");

        table.getCellEditor().stopCellEditing();
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
        Cell n_head = sheet1.createRow(0).createCell(0);
        Cell so_num_head = sheet1.createRow(0).createCell(1);
        Cell cus_head = sheet1.createRow(0).createCell(2);
        Cell p_num_head = sheet1.createRow(0).createCell(3);
        Cell pro_qty_head = sheet1.createRow(0).createCell(4);
        Cell pack_qty_head = sheet1.createRow(0).createCell(5);

        n_head.setCellValue("No");
        so_num_head.setCellValue("SO Number");
        cus_head.setCellValue("Customer");
        p_num_head.setCellValue("Part Number");
        pro_qty_head.setCellValue("Produced");
        pack_qty_head.setCellValue("Packed");

        int rows = table.getRowCount();
        int fullRows = rows - emptyRows;
        for (int row = 1; row < fullRows + 1; row++) {

            Cell n = sheet1.createRow(row).createCell(0);
            Cell so_num = sheet1.createRow(row).createCell(1);
            Cell cus = sheet1.createRow(row).createCell(2);
            Cell p_num = sheet1.createRow(row).createCell(3);
            Cell pro_qty = sheet1.createRow(row).createCell(4);
            Cell pack_qty = sheet1.createRow(row).createCell(5);

            n.setCellValue((String) table.getValueAt(row - 1, 0));
            so_num.setCellValue((String) table.getValueAt(row - 1, 1));
            cus.setCellValue((String) table.getValueAt(row - 1, 2));
            p_num.setCellValue((String) table.getValueAt(row - 1, 3));
            pro_qty.setCellValue((String) table.getValueAt(row - 1, 4));
            pack_qty.setCellValue((String) table.getValueAt(row - 1, 5));
        }
        try {
            FileOutputStream output = new FileOutputStream(file);
            workbook.write(output);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void oee(JTable table, File file) throws FileNotFoundException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet1 = workbook.createSheet("OEE");
        
        
        
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short)18);
        font.setFontName("Arial");
        font.setBold(true);
        font.setUnderline((byte)1);
        
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(font);

        Row firstrow = sheet1.createRow(0);
        Cell title = sheet1.createRow(0).createCell(4);
        title.setCellStyle(style);
        title.setCellValue("Overall Equipment Effectiveness Calculator Spreadsheet");
        Row secondrow = sheet1.createRow(1);
        Row thirdrow = sheet1.createRow(2);
        Row forthrow = sheet1.createRow(3);
        Row fifthrow = sheet1.createRow(4);
        

        firstrow.setRowStyle(style);
        firstrow.setHeightInPoints(30);
        secondrow.setRowStyle(style);
        secondrow.setHeightInPoints(30);
        thirdrow.setRowStyle(style);
        forthrow.setRowStyle(style);
        fifthrow.setRowStyle(style);

        //table.getCellEditor().stopCellEditing();
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

        int rows = table.getRowCount();
        int fullRows = rows - emptyRows;

        Row row = null;
    Cell cell = null;
    for (int i=0;i<table.getRowCount();i++) {
        row = sheet1.createRow(i+8);
        for (int j=0;j<table.getColumnCount();j++) {
             
            cell = row.createCell(j);
            cell.setCellValue(table.getValueAt(i, j).toString());
            
        }
    }

        try {
            //template.close();
            FileOutputStream output = new FileOutputStream(file);
            workbook.write(output);
            output.close();
        } catch (Exception e) {

        }

    }
}
