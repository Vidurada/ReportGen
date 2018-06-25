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
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.JTable;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
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

    public void productionOEEExcel(JTable table, File file) throws FileNotFoundException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet1 = workbook.createSheet("OEE");
        //sheet1.setColumnWidth(0,2);

        //firts title font style
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 18);
        font.setFontName("Arial");
        font.setBold(true);
        font.setUnderline((byte) 1);

        //other color row style
        XSSFCellStyle other_row_style = (XSSFCellStyle) workbook.createCellStyle();
        other_row_style.setFillForegroundColor(new XSSFColor(new java.awt.Color(154, 236, 241)));
        other_row_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //subtitle font style
        XSSFFont subtitle_font = (XSSFFont) workbook.createFont();
        subtitle_font.setFontHeightInPoints((short) 18);
        subtitle_font.setFontName("Arial");
        subtitle_font.setBold(true);
        subtitle_font.setUnderline((byte) 1);
        subtitle_font.setColor(new XSSFColor(new java.awt.Color(255, 0, 0)));

        //normal text font 
        XSSFFont normal_font = (XSSFFont) workbook.createFont();
        normal_font.setFontHeightInPoints((short) 8);
        normal_font.setFontName("Arial");

        //normal text style
        XSSFCellStyle table_head_style = (XSSFCellStyle) workbook.createCellStyle();
        table_head_style.setFont(normal_font);
        table_head_style.setFillForegroundColor(new XSSFColor(new java.awt.Color(231, 227, 227)));
        table_head_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        table_head_style.setAlignment(HorizontalAlignment.CENTER);
        table_head_style.setWrapText(true);

        //style for title
        XSSFCellStyle title_style = (XSSFCellStyle) workbook.createCellStyle();
        title_style.setFillForegroundColor(new XSSFColor(new java.awt.Color(154, 236, 241)));
        title_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        title_style.setFont(font);

        //style for table heading
        XSSFCellStyle head_style = (XSSFCellStyle) workbook.createCellStyle();
        head_style.setFillForegroundColor(new XSSFColor(new java.awt.Color(231, 227, 227)));
        head_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //style for subtitle
        XSSFCellStyle sub_title_style = (XSSFCellStyle) workbook.createCellStyle();
        sub_title_style.setFillForegroundColor(new XSSFColor(new java.awt.Color(154, 236, 241)));
        sub_title_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        sub_title_style.setFont(subtitle_font);
        sub_title_style.setAlignment(HorizontalAlignment.CENTER);
        sub_title_style.setWrapText(true);

        Row firstrow = sheet1.createRow(0);
        for (int i = 0; i < 17; i++) {
            Cell color_row = firstrow.createCell(i);
            color_row.setCellStyle(title_style);
        }
        Cell title = firstrow.createCell(3);
        firstrow.setHeightInPoints(30);
        title.setCellStyle(title_style);
        title.setCellValue("Overall Equipment Effectiveness Calculator Spreadsheet");
        Row secondrow = sheet1.createRow(1);
        for (int i = 0; i < 17; i++) {
            Cell color_row = secondrow.createCell(i);
            color_row.setCellStyle(title_style);
        }
        //secondrow.setRowStyle(title_style);
        secondrow.setHeightInPoints(30);
        sheet1.addMergedRegion(new CellRangeAddress(
                1, //first row (0-based)
                1, //last row  (0-based)
                3, //first column (0-based)
                9 //last column  (0-based)
        ));
        Cell sub_title = secondrow.createCell(3);
        sub_title.setCellStyle(sub_title_style);
        sub_title.setCellValue("Extrusion PROCESS");
        Row thirdrow = sheet1.createRow(2);
        for (int i = 0; i < 15; i++) {
            Cell color_row = thirdrow.createCell(i);
            color_row.setCellStyle(other_row_style);
        }
        Row forthrow = sheet1.createRow(3);
        for (int i = 0; i < 15; i++) {
            Cell color_row = forthrow.createCell(i);
            color_row.setCellStyle(other_row_style);
        }
        Row fifthrow = sheet1.createRow(4);
        for (int i = 0; i < 15; i++) {
            Cell color_row = fifthrow.createCell(i);
            color_row.setCellStyle(other_row_style);
        }

        secondrow.setHeightInPoints(30);

        Row table_head_row = sheet1.createRow(5);
        table_head_row.setHeightInPoints(35);

        //no cell
        Cell no = table_head_row.createCell(0);
        no.setCellStyle(table_head_style);
        no.setCellValue("No");

        //item cell
        Cell item = table_head_row.createCell(1);
        item.setCellStyle(table_head_style);
        item.setCellValue("Item");

        //so_num cell
        Cell so_num = table_head_row.createCell(2);
        so_num.setCellStyle(table_head_style);
        so_num.setCellValue("SO NO");

        //standardWinder cell
        Cell standardWinder = table_head_row.createCell(3);
        standardWinder.setCellStyle(table_head_style);
        standardWinder.setCellValue("Standard Winder Output(kg/hr)");

        //reject cell
        Cell reject = table_head_row.createCell(4);
        reject.setCellStyle(table_head_style);
        reject.setCellValue("Total Reject/Rework");

        //output cell
        Cell output = table_head_row.createCell(5);
        output.setCellStyle(table_head_style);
        output.setCellValue("Total Output Qty(kg)");

        //available time cell
        Cell a_time = table_head_row.createCell(6);
        a_time.setCellStyle(table_head_style);
        a_time.setCellValue("Total Available Time");

        //planned downtime cell
        Cell p_time = table_head_row.createCell(7);
        p_time.setCellStyle(table_head_style);
        p_time.setCellValue("Planned down time");

        //downtime cell
        Cell d_time = table_head_row.createCell(8);
        d_time.setCellStyle(table_head_style);
        d_time.setCellValue("Downtime- (Machine)");

        //operating time cell
        Cell o_time = table_head_row.createCell(9);
        o_time.setCellStyle(table_head_style);
        o_time.setCellValue("Operating Time");

        //ideal time rate cell
        Cell i_rate = table_head_row.createCell(10);
        i_rate.setCellStyle(table_head_style);
        i_rate.setCellValue("Ideal Run Rate");

        //available rate cell
        Cell a_rate = table_head_row.createCell(11);
        a_rate.setCellStyle(table_head_style);
        a_rate.setCellValue("Availability Rate");

        //performance rate cell
        Cell p_rate = table_head_row.createCell(12);
        p_rate.setCellStyle(table_head_style);
        p_rate.setCellValue("Performance Rate");

        //quality rate cell
        Cell q_rate = table_head_row.createCell(13);
        q_rate.setCellStyle(table_head_style);
        q_rate.setCellValue("Quality Rate");

        //oee rate cell
        Cell p_oee = table_head_row.createCell(14);
        p_oee.setCellStyle(table_head_style);
        p_oee.setCellValue("Plant OEE");

        //table.getCellEditor().stopCellEditing();
        int emptyRows = 0;
        rowSearch:
        for (int row = 0; row < table.getRowCount(); row++) { //Iterate through all the rows
            for (int col = 0; col < table.getColumnCount(); col++) { //Iterate through all the columns in the row
                if (table.getValueAt(row, col) != null) { //Check if the box is empty
                    String qty = table.getValueAt(row, col).toString();
                    if (!qty.isEmpty()) {

                        continue rowSearch; //If the value is not null, the row contains stuff so go onto the next row
                    }
                }
            }
            emptyRows++; //Conditional never evaluated to true so none of the columns in the row contained anything
        }

        int rows = table.getRowCount();
        int fullRows = rows - emptyRows;

        Row row = null;
        Cell cell = null;
        for (int i = 0; i < table.getRowCount(); i++) {
            row = sheet1.createRow(i + 6);
            for (int j = 0; j < table.getColumnCount(); j++) {

                cell = row.createCell(j);
                cell.setCellValue(table.getValueAt(i, j).toString());

            }
        }

        /*for (int j = 0; j < table.getColumnCount(); j++) {
            sheet1.autoSizeColumn(j);
        }*/
        sheet1.setColumnWidth(0, 5 * 256);
        sheet1.setColumnWidth(1, 30 * 256);
        sheet1.setColumnWidth(2, 10 * 256);
        sheet1.setColumnWidth(3, 13 * 256);
        sheet1.setColumnWidth(4, 13 * 256);
        sheet1.setColumnWidth(5, 13 * 256);
        sheet1.setColumnWidth(6, 13 * 256);
        sheet1.setColumnWidth(7, 13 * 256);
        sheet1.setColumnWidth(8, 13 * 256);
        sheet1.setColumnWidth(9, 13 * 256);
        sheet1.setColumnWidth(10, 13 * 256);
        sheet1.setColumnWidth(11, 13 * 256);
        sheet1.setColumnWidth(12, 13 * 256);
        sheet1.setColumnWidth(13, 13 * 256);

        try {
            //template.close();
            FileOutputStream output_file = new FileOutputStream(file);
            workbook.write(output_file);
            output_file.close();
        } catch (Exception e) {

        }

    }

    public void packagingOEEExcel(JTable table, File file) throws FileNotFoundException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet1 = workbook.createSheet("OEE");
        //sheet1.setColumnWidth(0,2);

        //firts title font style
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 18);
        font.setFontName("Arial");
        font.setBold(true);
        font.setUnderline((byte) 1);

        //other color row style
        XSSFCellStyle other_row_style = (XSSFCellStyle) workbook.createCellStyle();
        other_row_style.setFillForegroundColor(new XSSFColor(new java.awt.Color(154, 236, 241)));
        other_row_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //subtitle font style
        XSSFFont subtitle_font = (XSSFFont) workbook.createFont();
        subtitle_font.setFontHeightInPoints((short) 18);
        subtitle_font.setFontName("Arial");
        subtitle_font.setBold(true);
        subtitle_font.setUnderline((byte) 1);
        subtitle_font.setColor(new XSSFColor(new java.awt.Color(255, 0, 0)));

        //normal text font 
        XSSFFont normal_font = (XSSFFont) workbook.createFont();
        normal_font.setFontHeightInPoints((short) 8);
        normal_font.setFontName("Arial");

        //normal text style
        XSSFCellStyle table_head_style = (XSSFCellStyle) workbook.createCellStyle();
        table_head_style.setFont(normal_font);
        table_head_style.setFillForegroundColor(new XSSFColor(new java.awt.Color(231, 227, 227)));
        table_head_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        table_head_style.setAlignment(HorizontalAlignment.CENTER);
        table_head_style.setWrapText(true);

        //style for title
        XSSFCellStyle title_style = (XSSFCellStyle) workbook.createCellStyle();
        title_style.setFillForegroundColor(new XSSFColor(new java.awt.Color(154, 236, 241)));
        title_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        title_style.setFont(font);

        //style for table heading
        XSSFCellStyle head_style = (XSSFCellStyle) workbook.createCellStyle();
        head_style.setFillForegroundColor(new XSSFColor(new java.awt.Color(231, 227, 227)));
        head_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //style for subtitle
        XSSFCellStyle sub_title_style = (XSSFCellStyle) workbook.createCellStyle();
        sub_title_style.setFillForegroundColor(new XSSFColor(new java.awt.Color(154, 236, 241)));
        sub_title_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        sub_title_style.setFont(subtitle_font);
        sub_title_style.setAlignment(HorizontalAlignment.CENTER);
        sub_title_style.setWrapText(true);

        Row firstrow = sheet1.createRow(0);
        for (int i = 0; i < 17; i++) {
            Cell color_row = firstrow.createCell(i);
            color_row.setCellStyle(title_style);
        }
        Cell title = firstrow.createCell(3);
        firstrow.setHeightInPoints(30);
        title.setCellStyle(title_style);
        title.setCellValue("Overall Equipment Effectiveness Calculator Spreadsheet");
        Row secondrow = sheet1.createRow(1);
        for (int i = 0; i < 17; i++) {
            Cell color_row = secondrow.createCell(i);
            color_row.setCellStyle(title_style);
        }
        //secondrow.setRowStyle(title_style);
        secondrow.setHeightInPoints(30);
        sheet1.addMergedRegion(new CellRangeAddress(
                1, //first row (0-based)
                1, //last row  (0-based)
                3, //first column (0-based)
                9 //last column  (0-based)
        ));
        Cell sub_title = secondrow.createCell(3);
        sub_title.setCellStyle(sub_title_style);
        sub_title.setCellValue("PACKING PROCESS");
        Row thirdrow = sheet1.createRow(2);
        for (int i = 0; i < 15; i++) {
            Cell color_row = thirdrow.createCell(i);
            color_row.setCellStyle(other_row_style);
        }
        Row forthrow = sheet1.createRow(3);
        for (int i = 0; i < 15; i++) {
            Cell color_row = forthrow.createCell(i);
            color_row.setCellStyle(other_row_style);
        }
        Row fifthrow = sheet1.createRow(4);
        for (int i = 0; i < 15; i++) {
            Cell color_row = fifthrow.createCell(i);
            color_row.setCellStyle(other_row_style);
        }

        secondrow.setHeightInPoints(30);

        Row table_head_row = sheet1.createRow(5);
        table_head_row.setHeightInPoints(35);

        //no cell
        Cell no = table_head_row.createCell(0);
        no.setCellStyle(table_head_style);
        no.setCellValue("No");

        //item cell
        Cell item = table_head_row.createCell(1);
        item.setCellStyle(table_head_style);
        item.setCellValue("Item");

        //so_num cell
        Cell cut_length = table_head_row.createCell(2);
        cut_length.setCellStyle(table_head_style);
        cut_length.setCellValue("Cut Length");

        //standardWinder cell
        Cell standardPack = table_head_row.createCell(3);
        standardPack.setCellStyle(table_head_style);
        standardPack.setCellValue("Standard Packing Output(kg/hr)");

        //reject cell
        Cell reject = table_head_row.createCell(4);
        reject.setCellStyle(table_head_style);
        reject.setCellValue("Total Reject/Rework");

        //output cell
        Cell output = table_head_row.createCell(5);
        output.setCellStyle(table_head_style);
        output.setCellValue("Total Packed Qty(kg)");

        //available time cell
        Cell a_time = table_head_row.createCell(6);
        a_time.setCellStyle(table_head_style);
        a_time.setCellValue("Total Available Time");

        //planned downtime cell
        Cell p_time = table_head_row.createCell(7);
        p_time.setCellStyle(table_head_style);
        p_time.setCellValue("Planned down time");

        //downtime cell
        Cell d_time = table_head_row.createCell(8);
        d_time.setCellStyle(table_head_style);
        d_time.setCellValue("Downtime - (Packing)");

        //operating time cell
        Cell o_time = table_head_row.createCell(9);
        o_time.setCellStyle(table_head_style);
        o_time.setCellValue("Operating Time");

        //ideal time rate cell
        Cell i_rate = table_head_row.createCell(10);
        i_rate.setCellStyle(table_head_style);
        i_rate.setCellValue("Ideal Run Rate");

        //available rate cell
        Cell a_rate = table_head_row.createCell(11);
        a_rate.setCellStyle(table_head_style);
        a_rate.setCellValue("Availability Rate");

        //performance rate cell
        Cell p_rate = table_head_row.createCell(12);
        p_rate.setCellStyle(table_head_style);
        p_rate.setCellValue("Performance Rate");

        //quality rate cell
        Cell q_rate = table_head_row.createCell(13);
        q_rate.setCellStyle(table_head_style);
        q_rate.setCellValue("Quality Rate");

        //oee rate cell
        Cell p_oee = table_head_row.createCell(14);
        p_oee.setCellStyle(table_head_style);
        p_oee.setCellValue("Plant OEE");

        //table.getCellEditor().stopCellEditing();
        int emptyRows = 0;
        rowSearch:
        for (int row = 0; row < table.getRowCount(); row++) { //Iterate through all the rows
            for (int col = 0; col < table.getColumnCount(); col++) { //Iterate through all the columns in the row
                if (table.getValueAt(row, col) != null) { //Check if the box is empty
                    String qty = table.getValueAt(row, col).toString();
                    if (!qty.isEmpty()) {

                        continue rowSearch; //If the value is not null, the row contains stuff so go onto the next row
                    }
                }
            }
            emptyRows++; //Conditional never evaluated to true so none of the columns in the row contained anything
        }

        int rows = table.getRowCount();
        int fullRows = rows - emptyRows;

        Row row = null;
        Cell cell = null;
        for (int i = 0; i < table.getRowCount(); i++) {
            row = sheet1.createRow(i + 6);
            for (int j = 0; j < table.getColumnCount(); j++) {

                cell = row.createCell(j);
                cell.setCellValue(table.getValueAt(i, j).toString());

            }
        }

        /*for (int j = 0; j < table.getColumnCount(); j++) {
            sheet1.autoSizeColumn(j);
        }*/
        sheet1.setColumnWidth(0, 5 * 256);
        sheet1.setColumnWidth(1, 30 * 256);
        sheet1.setColumnWidth(2, 10 * 256);
        sheet1.setColumnWidth(3, 13 * 256);
        sheet1.setColumnWidth(4, 13 * 256);
        sheet1.setColumnWidth(5, 13 * 256);
        sheet1.setColumnWidth(6, 13 * 256);
        sheet1.setColumnWidth(7, 13 * 256);
        sheet1.setColumnWidth(8, 13 * 256);
        sheet1.setColumnWidth(9, 13 * 256);
        sheet1.setColumnWidth(10, 13 * 256);
        sheet1.setColumnWidth(11, 13 * 256);
        sheet1.setColumnWidth(12, 13 * 256);
        sheet1.setColumnWidth(13, 13 * 256);

        try {
            //template.close();
            FileOutputStream output_file = new FileOutputStream(file);
            workbook.write(output_file);
            output_file.close();
        } catch (Exception e) {

        }

    }

    public void shiftReport(JTable prodTable, JTable rejectTable, JTable downtimeTable, JTable packageTimeTable,String reportDate,String reportShift, String Supervisor, String Technician, String NoW, File file) {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet1 = workbook.createSheet("OEE");

        //firts title title_font style
        Font title_font = workbook.createFont();
        title_font.setFontHeightInPoints((short) 16);
        title_font.setFontName("Arial");
        title_font.setBold(true);

        //normal font style
        Font normal_font = workbook.createFont();
        normal_font.setFontHeightInPoints((short) 11);
        normal_font.setFontName("Arial");

        //normal bold font style
        Font normal_bold_font = workbook.createFont();
        normal_bold_font.setFontHeightInPoints((short) 11);
        normal_bold_font.setFontName("Arial");
        normal_bold_font.setBold(true);

        //style for title
        XSSFCellStyle title_style = (XSSFCellStyle) workbook.createCellStyle();
        title_style.setAlignment(HorizontalAlignment.CENTER);
        title_style.setFont(title_font);

        //style for normal
        XSSFCellStyle normal_style = (XSSFCellStyle) workbook.createCellStyle();
        normal_style.setAlignment(HorizontalAlignment.CENTER);
        normal_style.setFont(normal_font);
        normal_style.setWrapText(true);

        //style for normal left alignment
        XSSFCellStyle normal_style_left = (XSSFCellStyle) workbook.createCellStyle();
        normal_style_left.setAlignment(HorizontalAlignment.LEFT);
        normal_style_left.setFont(normal_font);

        //style for normal with bottom boarder
        XSSFCellStyle normal_style_b_border = (XSSFCellStyle) workbook.createCellStyle();
        normal_style_b_border.setAlignment(HorizontalAlignment.CENTER);
        normal_style_b_border.setFont(normal_font);
        normal_style_b_border.setBorderBottom(BorderStyle.MEDIUM);

        //style for normal with full boarder
        XSSFCellStyle normal_style_f_border = (XSSFCellStyle) workbook.createCellStyle();
        normal_style_f_border.setAlignment(HorizontalAlignment.LEFT);
        normal_style_f_border.setFont(normal_font);
        //normal_style_f_border.setBorderTop(BorderStyle.THIN);
        // normal_style_f_border.setBorderBottom(BorderStyle.THIN);
        // normal_style_f_border.setBorderLeft(BorderStyle.THIN);
        // normal_style_f_border.setBorderRight(BorderStyle.THIN);
        normal_style_f_border.setWrapText(true);

        //style for normal bold
        XSSFCellStyle normal_bold_style = (XSSFCellStyle) workbook.createCellStyle();
        normal_bold_style.setAlignment(HorizontalAlignment.CENTER);
        normal_bold_style.setFont(normal_bold_font);

        //first row of document
        Row firstrow = sheet1.createRow(0);
        firstrow.setHeightInPoints(30);
        sheet1.addMergedRegion(new CellRangeAddress(
                0, //first row (0-based)
                0, //last row  (0-based)
                0, //first column (0-based)
                14 //last column  (0-based)
        ));
        Cell title = firstrow.createCell(0);
        title.setCellStyle(title_style);
        title.setCellValue("Beira Enviro Solutions ( Pvt.) Limited");

        //second row of document
        Row secondrow = sheet1.createRow(1);
        //firstrow.setHeightInPoints(10);
        sheet1.addMergedRegion(new CellRangeAddress(
                1, //first row (0-based)
                1, //last row  (0-based)
                0, //first column (0-based)
                14 //last column  (0-based)
        ));
        Cell document = secondrow.createCell(0);
        document.setCellStyle(normal_style);
        document.setCellValue("Document");

        //third Row
        Row thirdrow = sheet1.createRow(2);
        //firstrow.setHeightInPoints(10);
        sheet1.addMergedRegion(new CellRangeAddress(
                2, //first row (0-based)
                2, //last row  (0-based)
                0, //first column (0-based)
                14 //last column  (0-based)
        ));
        Cell shiftreport = thirdrow.createCell(0);
        shiftreport.setCellStyle(normal_bold_style);
        shiftreport.setCellValue("Shift Report");

        Row fourthrow = sheet1.createRow(3);
        //firstrow.setHeightInPoints(10);
        sheet1.addMergedRegion(new CellRangeAddress(
                3, //first row (0-based)
                3, //last row  (0-based)
                0, //first column (0-based)
                4 //last column  (0-based)
        ));
        //issue number cell
        Cell issueNumber = fourthrow.createCell(0);
        issueNumber.setCellStyle(normal_style_left);
        issueNumber.setCellValue("Issue Number: 01");
        sheet1.addMergedRegion(new CellRangeAddress(
                3, //first row (0-based)
                3, //last row  (0-based)
                5, //first column (0-based)
                9 //last column  (0-based)
        ));
        //revision Number cell
        Cell revisionNumber = fourthrow.createCell(5);
        revisionNumber.setCellStyle(normal_style_left);
        revisionNumber.setCellValue("Revision Number: 01");
        sheet1.addMergedRegion(new CellRangeAddress(
                3, //first row (0-based)
                3, //last row  (0-based)
                10, //first column (0-based)
                14 //last column  (0-based)
        ));
        //document Number cell
        Cell documentNumber = fourthrow.createCell(10);
        documentNumber.setCellStyle(normal_style_left);
        documentNumber.setCellValue("Document Number: BES-PRD-03");

        Row fifthrow = sheet1.createRow(4);
        //firstrow.setHeightInPoints(10);
        sheet1.addMergedRegion(new CellRangeAddress(
                4, //first row (0-based)
                4, //last row  (0-based)
                0, //first column (0-based)
                4 //last column  (0-based)
        ));
        //issue number cell
        Cell date_of_issue = fifthrow.createCell(0);
        date_of_issue.setCellStyle(normal_style_left);
        date_of_issue.setCellValue("Date of Issue: 15.05.2013");
        sheet1.addMergedRegion(new CellRangeAddress(
                4, //first row (0-based)
                4, //last row  (0-based)
                5, //first column (0-based)
                9 //last column  (0-based)
        ));
        //revision Number cell
        Cell date_of_revision = fifthrow.createCell(5);
        date_of_revision.setCellStyle(normal_style_left);
        date_of_revision.setCellValue("Date of Revision:03-13-2018");
        sheet1.addMergedRegion(new CellRangeAddress(
                4, //first row (0-based)
                4, //last row  (0-based)
                10, //first column (0-based)
                14 //last column  (0-based)
        ));
        //document Number cell
        Cell page = fifthrow.createCell(10);
        page.setCellStyle(normal_style_left);
        page.setCellValue("Page: 01");

        Row sixthrow = sheet1.createRow(5);
        sheet1.addMergedRegion(new CellRangeAddress(
                5, //first row (0-based)
                5, //last row  (0-based)
                0, //first column (0-based)
                1 //last column  (0-based)
        ));
        //reportDate,String reportShift, String Supervior, String Technician, String NoW
        Cell date = sixthrow.createCell(0);
        date.setCellStyle(normal_style_left);
        date.setCellValue("Date:");

        sheet1.addMergedRegion(new CellRangeAddress(
                5, //first row (0-based)
                5, //last row  (0-based)
                2, //first column (0-based)
                3 //last column  (0-based)
        ));

        Cell date_input = sixthrow.createCell(2);
        date_input.setCellStyle(normal_style_b_border);
        date_input.setCellValue(reportDate);

        Cell date_input2 = sixthrow.createCell(3);
        date_input2.setCellStyle(normal_style_b_border);
        date_input2.setCellValue("");

        sheet1.addMergedRegion(new CellRangeAddress(
                5, //first row (0-based)
                5, //last row  (0-based)
                12, //first column (0-based)
                13 //last column  (0-based)
        ));

        Cell tech = sixthrow.createCell(10);
        tech.setCellStyle(normal_style_left);
        tech.setCellValue("Technician:");

        sheet1.addMergedRegion(new CellRangeAddress(
                5, //first row (0-based)
                5, //last row  (0-based)
                10, //first column (0-based)
                11 //last column  (0-based)
        ));

        Cell tech_input = sixthrow.createCell(12);
        tech_input.setCellStyle(normal_style_b_border);
        tech_input.setCellValue(Technician);

        Cell tech_input2 = sixthrow.createCell(13);
        tech_input2.setCellStyle(normal_style_b_border);
        tech_input2.setCellValue("");

        Row seventhrow = sheet1.createRow(6);
        sheet1.addMergedRegion(new CellRangeAddress(
                6, //first row (0-based)
                6, //last row  (0-based)
                0, //first column (0-based)
                1 //last column  (0-based)
        ));

        Cell shift = seventhrow.createCell(0);
        shift.setCellStyle(normal_style_left);
        shift.setCellValue("Shift: ");

        sheet1.addMergedRegion(new CellRangeAddress(
                6, //first row (0-based)
                6, //last row  (0-based)
                2, //first column (0-based)
                3 //last column  (0-based)
        ));

        Cell shift_input = seventhrow.createCell(2);
        shift_input.setCellStyle(normal_style_b_border);
        shift_input.setCellValue(reportShift);

        Cell shift_input2 = seventhrow.createCell(3);
        shift_input2.setCellStyle(normal_style_b_border);
        shift_input2.setCellValue("");

        sheet1.addMergedRegion(new CellRangeAddress(
                6, //first row (0-based)
                6, //last row  (0-based)
                12, //first column (0-based)
                13 //last column  (0-based)
        ));

        Cell no_workers = seventhrow.createCell(10);
        no_workers.setCellStyle(normal_style_left);
        no_workers.setCellValue("No. of Workers ");

        sheet1.addMergedRegion(new CellRangeAddress(
                6, //first row (0-based)
                6, //last row  (0-based)
                10, //first column (0-based)
                11 //last column  (0-based)
        ));

        Cell no_workers_input = seventhrow.createCell(12);
        no_workers_input.setCellStyle(normal_style_b_border);
        no_workers_input.setCellValue(NoW);

        Cell no_workers_input2 = seventhrow.createCell(13);
        no_workers_input2.setCellStyle(normal_style_b_border);
        no_workers_input2.setCellValue("");

        Row eighthrow = sheet1.createRow(7);
        sheet1.addMergedRegion(new CellRangeAddress(
                7, //first row (0-based)
                7, //last row  (0-based)
                0, //first column (0-based)
                1 //last column  (0-based)
        ));

        Cell supervisor = eighthrow.createCell(0);
        supervisor.setCellStyle(normal_style_left);
        supervisor.setCellValue("Supervisor");
        

        sheet1.addMergedRegion(new CellRangeAddress(
                7, //first row (0-based)
                7, //last row  (0-based)
                2, //first column (0-based)
                3 //last column  (0-based)
        ));

        Cell supervisor_input = eighthrow.createCell(2);
        supervisor_input.setCellStyle(normal_style_b_border);
        supervisor_input.setCellValue(Supervisor);

        Cell supervisor_input2 = eighthrow.createCell(3);
        supervisor_input2.setCellStyle(normal_style_b_border);
        supervisor_input2.setCellValue("");

        Row ninethrow = sheet1.createRow(8);
        ninethrow.setHeightInPoints(20);
        sheet1.addMergedRegion(new CellRangeAddress(
                8, //first row (0-based)
                8, //last row  (0-based)
                0, //first column (0-based)
                14 //last column  (0-based)
        ));
        Cell production = ninethrow.createCell(0);
        production.setCellStyle(normal_style_left);
        production.setCellValue("Production");

        Row tenthrow = sheet1.createRow(9);
        sheet1.addMergedRegion(new CellRangeAddress(
                9, //first row (0-based)
                9, //last row  (0-based)
                0, //first column (0-based)
                7 //last column  (0-based)
        ));
        Cell items = tenthrow.createCell(0);
        items.setCellStyle(normal_style_f_border);
        items.setCellValue("ITEMS");
        sheet1.addMergedRegion(new CellRangeAddress(
                9, //first row (0-based)
                9, //last row  (0-based)
                8, //first column (0-based)
                11 //last column  (0-based)
        ));
        Cell kg = tenthrow.createCell(8);
        kg.setCellStyle(normal_style_f_border);
        kg.setCellValue("Kg");

        sheet1.addMergedRegion(new CellRangeAddress(
                9, //first row (0-based)
                9, //last row  (0-based)
                12, //first column (0-based)
                13 //last column  (0-based)
        ));
        Cell cartons = tenthrow.createCell(12);
        cartons.setCellStyle(normal_style_f_border);
        cartons.setCellValue("No. of Cartons");

        sheet1.addMergedRegion(new CellRangeAddress(
                9, //first row (0-based)
                10, //last row  (0-based)
                14, //first column (0-based)
                14 //last column  (0-based)
        ));
        Cell pp = tenthrow.createCell(14);
        pp.setCellStyle(normal_style_f_border);
        pp.setCellValue("PP/PE Bags");

        Row eleventhrow = sheet1.createRow(10);
        Cell no = eleventhrow.createCell(0);
        no.setCellStyle(normal_style);
        no.setCellValue("No");

        Cell so_no = eleventhrow.createCell(1);
        so_no.setCellStyle(normal_style);
        so_no.setCellValue("SO Number");

        Cell cust = eleventhrow.createCell(2);
        cust.setCellStyle(normal_style);
        cust.setCellValue("Customer");

        sheet1.addMergedRegion(new CellRangeAddress(
                10, //first row (0-based)
                10, //last row  (0-based)
                3, //first column (0-based)
                7 //last column  (0-based)
        ));

        Cell part_no = eleventhrow.createCell(3);
        part_no.setCellStyle(normal_style);
        part_no.setCellValue("Part Number");

        sheet1.addMergedRegion(new CellRangeAddress(
                10, //first row (0-based)
                10, //last row  (0-based)
                8, //first column (0-based)
                9 //last column  (0-based)
        ));

        Cell prod_qty = eleventhrow.createCell(8);
        prod_qty.setCellStyle(normal_style);
        prod_qty.setCellValue("Prod Qty");
        sheet1.addMergedRegion(new CellRangeAddress(
                10, //first row (0-based)
                10, //last row  (0-based)
                10, //first column (0-based)
                11 //last column  (0-based)
        ));

        Cell pack_qty = eleventhrow.createCell(10);
        pack_qty.setCellStyle(normal_style);
        pack_qty.setCellValue("Pack Qty");

        Cell newc = eleventhrow.createCell(11);
        newc.setCellStyle(normal_style);
        newc.setCellValue("New");

        Cell used = eleventhrow.createCell(12);
        used.setCellStyle(normal_style);
        used.setCellValue("Used");

        int emptyRows = 0;
        rowSearch:
        for (int row = 0; row < prodTable.getRowCount(); row++) { //Iterate through all the rows
            for (int col = 0; col < prodTable.getColumnCount()-1; col++) { //Iterate through all the columns in the row
                if (prodTable.getValueAt(row, col) != null) { //Check if the box is empty
                    String qty = prodTable.getValueAt(row, col).toString();
                    if (!qty.isEmpty()) {

                        continue rowSearch; //If the value is not null, the row contains stuff so go onto the next row
                    }
                }
            }
            emptyRows++; //Conditional never evaluated to true so none of the columns in the row contained anything
        }

        Row row = null;
        Cell cell = null;
        for (int i = 0; i < (prodTable.getRowCount() - emptyRows); i++) {
            row = sheet1.createRow(i + 11);

            sheet1.addMergedRegion(new CellRangeAddress(
                    11 + i, //first row (0-based)
                    11 + i, //last row  (0-based)
                    8, //first column (0-based)
                    9 //last column  (0-based)
            ));

            sheet1.addMergedRegion(new CellRangeAddress(
                    11 + i, //first row (0-based)
                    11 + i, //last row  (0-based)
                    10, //first column (0-based)
                    11 //last column  (0-based)
            ));

            sheet1.addMergedRegion(new CellRangeAddress(
                    11 + i, //first row (0-based)
                    11 + i, //last row  (0-based)
                    3, //first column (0-based)
                    7 //last column  (0-based)
            ));

            Cell shift_no = row.createCell(0);
            shift_no.setCellValue(prodTable.getValueAt(i, 0).toString());

            Cell shift_so_no = row.createCell(1);
            shift_so_no.setCellValue(prodTable.getValueAt(i, 1).toString());

            Cell shift_custa = row.createCell(2);
            shift_custa.setCellValue(prodTable.getValueAt(i, 2).toString());

            Cell shift_pno = row.createCell(3);
            shift_pno.setCellValue(prodTable.getValueAt(i, 3).toString());

            Cell shift_prodq = row.createCell(8);
            if (prodTable.getValueAt(i, 4) == null) {
                shift_prodq.setCellValue("");
            } else {
                shift_prodq.setCellValue(prodTable.getValueAt(i, 4).toString());
            }

            Cell shift_packq = row.createCell(10);
            if (prodTable.getValueAt(i, 5) == null) {
                shift_packq.setCellValue("");
            } else {
                shift_packq.setCellValue(prodTable.getValueAt(i, 5).toString());
            }

            Cell shift_new = row.createCell(12);
            if (prodTable.getValueAt(i, 6) == null) {
                shift_new.setCellValue("");
            } else {
                shift_new.setCellValue(prodTable.getValueAt(i, 6).toString());
            }

            Cell shift_used = row.createCell(13);
            if (prodTable.getValueAt(i, 7) == null) {
                shift_used.setCellValue("");
            } else {
                shift_used.setCellValue(prodTable.getValueAt(i, 7).toString());
            }

            Cell shift_bags = row.createCell(14);
            if (prodTable.getValueAt(i, 8) == null) {
                shift_bags.setCellValue("");
            } else {
                shift_bags.setCellValue(prodTable.getValueAt(i, 8).toString());
            }

        }

        int total_row = 11 + (prodTable.getRowCount() - emptyRows);

        main mn = new main();
        float prod_total = mn.columnSum(prodTable, 4);
        Float truncated_prod_total = BigDecimal.valueOf(prod_total)
                .setScale(2, RoundingMode.HALF_UP)
                .floatValue();
        
        System.out.println(truncated_prod_total);
        
        float pack_total = mn.columnSum(prodTable, 5);
        Float truncated_pack_total = BigDecimal.valueOf(pack_total)
                .setScale(2, RoundingMode.HALF_UP)
                .floatValue();

        float prod_new = mn.columnSum(prodTable, 6);
        Float truncated_prod_new = BigDecimal.valueOf(prod_new)
                .setScale(2, RoundingMode.HALF_UP)
                .floatValue();

        float prod_used = mn.columnSum(prodTable, 7);
        Float truncated_prod_used = BigDecimal.valueOf(prod_used)
                .setScale(2, RoundingMode.HALF_UP)
                .floatValue();

        float prod_bags = mn.columnSum(prodTable, 8);
        Float truncated_prod_bags = BigDecimal.valueOf(prod_bags)
                .setScale(2, RoundingMode.HALF_UP)
                .floatValue();

        Row prod_total_row = sheet1.createRow(total_row);
        sheet1.addMergedRegion(new CellRangeAddress(
                total_row, //first row (0-based)
                total_row, //last row  (0-based)
                3, //first column (0-based)
                7 //last column  (0-based)
        ));

        Cell total_label = prod_total_row.createCell(3);
        total_label.setCellValue("Total");

        sheet1.addMergedRegion(new CellRangeAddress(
                total_row, //first row (0-based)
                total_row, //last row  (0-based)
                8, //first column (0-based)
                9 //last column  (0-based)
        ));

        sheet1.addMergedRegion(new CellRangeAddress(
                total_row, //first row (0-based)
                total_row, //last row  (0-based)
                10, //first column (0-based)
                11 //last column  (0-based)
        ));

        Cell prod_total_label = prod_total_row.createCell(8);
        prod_total_label.setCellValue(truncated_prod_total);

        Cell pack_total_label = prod_total_row.createCell(10);
        pack_total_label.setCellValue(truncated_pack_total);

        Cell new_total_label = prod_total_row.createCell(12);
        new_total_label.setCellValue(truncated_prod_new);

        Cell used_total_label = prod_total_row.createCell(13);
        used_total_label.setCellValue(truncated_prod_used);

        Cell bags_total_label = prod_total_row.createCell(14);
        bags_total_label.setCellValue(truncated_prod_bags);
        
        
        
        

        int ra_label = total_row + 1;
        Row ra_label_row = sheet1.createRow(ra_label);
        Cell ral = ra_label_row.createCell(0);
        ral.setCellValue("REJECT ANALYSIS");

        Row reject_base_row = sheet1.createRow(ra_label + 1);
        sheet1.addMergedRegion(new CellRangeAddress(
                ra_label + 1, //first row (0-based)
                ra_label + 2, //last row  (0-based)
                0, //first column (0-based)
                2 //last column  (0-based)
        ));

        sheet1.addMergedRegion(new CellRangeAddress(
                ra_label + 1, //first row (0-based)
                ra_label + 1, //last row  (0-based)
                3, //first column (0-based)
                14 //last column  (0-based)
        ));

        Cell base_label = reject_base_row.createCell(0);
        base_label.setCellValue("BASE");
        Cell base_label2 = reject_base_row.createCell(3);
        base_label2.setCellValue("Quantity(Kg)");

        Row reject_base_row2 = sheet1.createRow(ra_label + 2);
        int columnCount = rejectTable.getColumnCount() - 2;

        for (int i = 1; i < columnCount + 1; i++) {
            Cell table_head = reject_base_row2.createCell(i + 2);
            String head = "Item " + i;
            table_head.setCellValue(head);
        }

        int emptyRows2 = 0;
        rowSearch2:
        for (int rows = 0; rows < rejectTable.getRowCount(); rows++) { //Iterate through all the rows
            for (int col = 0; col < rejectTable.getColumnCount(); col++) { //Iterate through all the columns in the row
                if (rejectTable.getValueAt(rows, col) != null) { //Check if the box is empty
                    String qty = rejectTable.getValueAt(rows, col).toString();
                    if (!qty.isEmpty()) {

                        continue rowSearch2; //If the value is not null, the row contains stuff so go onto the next row
                    }
                }
            }
            emptyRows2++; //Conditional never evaluated to true so none of the columns in the row contained anything
        }

        int rows = rejectTable.getRowCount();
        int fullRows = rows - emptyRows2;

        Cell table_head_total = reject_base_row2.createCell(3 + columnCount);
        table_head_total.setCellValue("TOTAL");
        int row_count = 0;
        for (int i = 0; i < rejectTable.getRowCount(); i++) {
            float if_zero = (float) rejectTable.getValueAt(i, rejectTable.getColumnCount() - 1);
            if (if_zero != 0) {
                row_count++;
                int start_of_reject_data = ra_label + 3 + row_count - 1;
                Row rowd = sheet1.createRow(start_of_reject_data);
                sheet1.addMergedRegion(new CellRangeAddress(
                        start_of_reject_data, //first row (0-based)
                        start_of_reject_data, //last row  (0-based)
                        0, //first column (0-based)
                        2 //last column  (0-based)
                ));
                String base = rejectTable.getValueAt(i, 0).toString();
                Cell base_reason = rowd.createCell(0);
                base_reason.setCellValue(base);

                for (int j = 3; j < rejectTable.getColumnCount() + 2; j++) {
                    cell = rowd.createCell(j);
                    if (rejectTable.getValueAt(i, j - 2) == null) {
                        cell.setCellValue("");
                    } else {
                        cell.setCellValue(rejectTable.getValueAt(i, j - 2).toString());
                    }
                }

            }
        }

        int reject_total_row = ra_label + 3 + row_count;
        Row reject_total_row_string = sheet1.createRow(reject_total_row);
        sheet1.addMergedRegion(new CellRangeAddress(
                reject_total_row, //first row (0-based)
                reject_total_row, //last row  (0-based)
                0, //first column (0-based)
                2 //last column  (0-based)
        ));
        Cell reject_total = reject_total_row_string.createCell(0);
        reject_total.setCellValue("Total");

        for (int j = 1; j < rejectTable.getColumnCount(); j++) {

            cell = reject_total_row_string.createCell(j + 2);
            float tot = mn.columnSum(rejectTable, j);
            cell.setCellValue(tot);

        }

        int downtime_table_starts_here = reject_total_row + 1;
        Row downtime_summary_line = sheet1.createRow(downtime_table_starts_here);
        sheet1.addMergedRegion(new CellRangeAddress(
                downtime_table_starts_here, //first row (0-based)
                downtime_table_starts_here, //last row  (0-based)
                0, //first column (0-based)
                2 //last column  (0-based)
        ));
        Cell downtime_summary = downtime_summary_line.createCell(0);
        downtime_summary.setCellValue("DOWN TIME SUMMARY");

        Row reject_table_time_header = sheet1.createRow(downtime_table_starts_here + 1);
        sheet1.addMergedRegion(new CellRangeAddress(
                downtime_table_starts_here + 1, //first row (0-based)
                downtime_table_starts_here + 1, //last row  (0-based)
                0, //first column (0-based)
                1 //last column  (0-based)
        ));
        Cell reject_table_time = reject_table_time_header.createCell(0);
        reject_table_time.setCellValue("TIME");

        sheet1.addMergedRegion(new CellRangeAddress(
                downtime_table_starts_here + 1, //first row (0-based)
                downtime_table_starts_here + 2, //last row  (0-based)
                2, //first column (0-based)
                2 //last column  (0-based)
        ));

        Cell no_of_minutes = reject_table_time_header.createCell(2);
        no_of_minutes.setCellValue("No of Minutes");

        sheet1.addMergedRegion(new CellRangeAddress(
                downtime_table_starts_here + 1, //first row (0-based)
                downtime_table_starts_here + 2, //last row  (0-based)
                3, //first column (0-based)
                10 //last column  (0-based)
        ));

        Cell reason = reject_table_time_header.createCell(3);
        reason.setCellValue("REASON");

        Row from_to_time = sheet1.createRow(downtime_table_starts_here + 2);
        Cell from = from_to_time.createCell(0);
        from.setCellValue("From");

        Cell to = from_to_time.createCell(1);
        to.setCellValue("To");

        // machineDowntimeTable
        int machine_downtime_table_rows = downtimeTable.getRowCount();

        int emptyRows3 = 0;
        rowSearch3:
        for (int rowss = 0; rowss < downtimeTable.getRowCount(); rowss++) { //Iterate through all the rows
            for (int colss = 0; colss < downtimeTable.getColumnCount(); colss++) { //Iterate through all the columns in the row
                if (downtimeTable.getValueAt(rowss, colss) != null) { //Check if the box is empty
                    String qty = downtimeTable.getValueAt(rowss, colss).toString();
                    if (!qty.isEmpty()) {

                        continue rowSearch3; //If the value is not null, the row contains stuff so go onto the next row
                    }
                }
            }
            emptyRows3++; //Conditional never evaluated to true so none of the columns in the row contained anything
        }

        for (int i = 0; i < machine_downtime_table_rows - emptyRows3; i++) {

            String downtime_from = downtimeTable.getValueAt(i, 2).toString();
            String downtime_to = downtimeTable.getValueAt(i, 3).toString();
            String downtime_mins = downtimeTable.getValueAt(i, 4).toString();
            String downtime_reason = downtimeTable.getValueAt(i, 5).toString();
            Object val = downtimeTable.getValueAt(i, 6);
            String downtime_comment = null;
            if (val == null){
            downtime_comment = "";
            }
            else{
            downtime_comment = downtimeTable.getValueAt(i, 6).toString();
            }

            Row row_input = sheet1.createRow(downtime_table_starts_here + 3 + i);
            Cell input_from = row_input.createCell(0);
            input_from.setCellValue(downtime_from);

            Cell input_to = row_input.createCell(1);
            input_to.setCellValue(downtime_to);

            Cell input_mins = row_input.createCell(2);
            input_mins.setCellValue(downtime_mins);

            sheet1.addMergedRegion(new CellRangeAddress(
                    downtime_table_starts_here + 3 + i, //first row (0-based)
                    downtime_table_starts_here + 3 + i, //last row  (0-based)
                    3, //first column (0-based)
                    10 //last column  (0-based)
            ));

            Cell input_comment_reason = row_input.createCell(3);
            input_comment_reason.setCellValue(downtime_reason + ". " + downtime_comment);
        }

        int down_summaty_total_starts_here = downtime_table_starts_here + 3 + machine_downtime_table_rows - emptyRows3;
        sheet1.addMergedRegion(new CellRangeAddress(
                down_summaty_total_starts_here, //first row (0-based)
                down_summaty_total_starts_here, //last row  (0-based)
                0, //first column (0-based)
                1 //last column  (0-based)
        ));
        Row down_summaty_total_string = sheet1.createRow(down_summaty_total_starts_here);
        Cell down_summaty_total = down_summaty_total_string.createCell(0);
        down_summaty_total.setCellValue("TOTAL");

        float sumtotal = mn.columnSum(downtimeTable, 4);

        Cell down_summaty_total_total = down_summaty_total_string.createCell(2);
        down_summaty_total_total.setCellValue(sumtotal);

        sheet1.addMergedRegion(new CellRangeAddress(
                down_summaty_total_starts_here, //first row (0-based)
                down_summaty_total_starts_here, //last row  (0-based)
                3, //first column (0-based)
                10 //last column  (0-based)
        ));

        //packageTimeTable
        int emptyRows4 = 0;
        rowSearch4:
        for (int rowss = 0; rowss < packageTimeTable.getRowCount(); rowss++) { //Iterate through all the rows
            for (int colss = 0; colss < packageTimeTable.getColumnCount(); colss++) { //Iterate through all the columns in the row
                if (packageTimeTable.getValueAt(rowss, colss) != null) { //Check if the box is empty
                    String qty = packageTimeTable.getValueAt(rowss, colss).toString();
                    if (!qty.isEmpty()) {

                        continue rowSearch4; //If the value is not null, the row contains stuff so go onto the next row
                    }
                }
            }
            emptyRows4++; //Conditional never evaluated to true so none of the columns in the row contained anything
        }
        
        
        
        for (int i = 0; i < (packageTimeTable.getRowCount() - emptyRows4); i++) {
            System.out.println(i+"run");
             String name2 = packageTimeTable.getValueAt(i, 1).toString();
                String state = "Packing(Item " + (i+1) + ") =";
                    String pack_from = packageTimeTable.getValueAt(i, 2).toString();
                    String pack_to = packageTimeTable.getValueAt(i, 3).toString();

                    String tea1 = "";
                    String tea2 = "";
                    String meal = "";
            String name1 = prodTable.getValueAt(i, 3).toString();
            for (int j = 0; j < packageTimeTable.getRowCount(); j++) {
               

                if (name1.equals(name2)) {
                    
                    if (packageTimeTable.getValueAt(i, 5).equals(true)) {
                        tea1 = " Tea = 10.00 am to 10.15 am";
                    }
                    if (packageTimeTable.getValueAt(i, 6).equals(true)) {
                        tea2 = " Tea = 3.00 pm to 3.15 pm";
                    }
                    if (packageTimeTable.getValueAt(i, 7).equals(true)) {
                        
                        float result = Float.parseFloat(pack_from);
                        if (result < 19) {
                            meal = " Lunch = 12.00 pm to 1.00 pm";
                        } else {
                            meal = " Dinner = 7.00 pm to 8.00 pm";
                        }

                    }

                    

                }

            }
            
                    String fullState = state + " " + pack_from + " to " + pack_to + " (" + tea1 + tea2 + meal + ")";
                    int newRow = down_summaty_total_starts_here + 2+i;
                    Row PackingRow = sheet1.createRow(newRow);
                    
                    Cell packing_time_label = PackingRow.createCell(0);
                    packing_time_label.setCellValue("PACKING TIME");
                    
                    Cell packing_time_comment = PackingRow.createCell(2);
                    packing_time_comment.setCellValue(fullState);
                    

        }

        try {
            //template.close();
            FileOutputStream output_file = new FileOutputStream(file);
            workbook.write(output_file);
            output_file.close();
        } catch (Exception e) {

        }
    }

    public void test(JTable prodtable, File file) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet1 = workbook.createSheet("OEE");

        Row row = null;
        Cell cell = null;
        for (int i = 0; i < prodtable.getRowCount(); i++) {
            row = sheet1.createRow(i + 12);

            for (int j = 0; j < prodtable.getColumnCount(); j++) {

                cell = row.createCell(j);
                cell.setCellValue(prodtable.getValueAt(i, j).toString());

            }
        }

        try {
            //template.close();
            FileOutputStream output_file = new FileOutputStream(file);
            workbook.write(output_file);
            output_file.close();
        } catch (Exception e) {

        }

    }

}
