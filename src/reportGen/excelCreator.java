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

    public void shiftReport(JTable prodtable, File file) {

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
        issueNumber.setCellValue("Issue Number");
        sheet1.addMergedRegion(new CellRangeAddress(
                3, //first row (0-based)
                3, //last row  (0-based)
                5, //first column (0-based)
                9 //last column  (0-based)
        ));
        //revision Number cell
        Cell revisionNumber = fourthrow.createCell(5);
        revisionNumber.setCellStyle(normal_style_left);
        revisionNumber.setCellValue("Revision Number");
        sheet1.addMergedRegion(new CellRangeAddress(
                3, //first row (0-based)
                3, //last row  (0-based)
                10, //first column (0-based)
                14 //last column  (0-based)
        ));
        //document Number cell
        Cell documentNumber = fourthrow.createCell(10);
        documentNumber.setCellStyle(normal_style_left);
        documentNumber.setCellValue("Document Number");

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
        date_of_issue.setCellValue("Date of Issue");
        sheet1.addMergedRegion(new CellRangeAddress(
                4, //first row (0-based)
                4, //last row  (0-based)
                5, //first column (0-based)
                9 //last column  (0-based)
        ));
        //revision Number cell
        Cell date_of_revision = fifthrow.createCell(5);
        date_of_revision.setCellStyle(normal_style_left);
        date_of_revision.setCellValue("Date of Revision");
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

        Cell date = sixthrow.createCell(0);
        date.setCellStyle(normal_style_left);
        date.setCellValue("Date");

        sheet1.addMergedRegion(new CellRangeAddress(
                5, //first row (0-based)
                5, //last row  (0-based)
                2, //first column (0-based)
                3 //last column  (0-based)
        ));

        Cell date_input = sixthrow.createCell(2);
        date_input.setCellStyle(normal_style_b_border);
        date_input.setCellValue("");

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
        tech.setCellValue("Technician");

        sheet1.addMergedRegion(new CellRangeAddress(
                5, //first row (0-based)
                5, //last row  (0-based)
                10, //first column (0-based)
                11 //last column  (0-based)
        ));

        Cell tech_input = sixthrow.createCell(12);
        tech_input.setCellStyle(normal_style_b_border);
        tech_input.setCellValue("");

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
        shift.setCellValue("Shift");

        sheet1.addMergedRegion(new CellRangeAddress(
                6, //first row (0-based)
                6, //last row  (0-based)
                2, //first column (0-based)
                3 //last column  (0-based)
        ));

        Cell shift_input = seventhrow.createCell(2);
        shift_input.setCellStyle(normal_style_b_border);
        shift_input.setCellValue("");

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
        no_workers.setCellValue("No. of Workers");

        sheet1.addMergedRegion(new CellRangeAddress(
                6, //first row (0-based)
                6, //last row  (0-based)
                10, //first column (0-based)
                11 //last column  (0-based)
        ));

        Cell no_workers_input = seventhrow.createCell(12);
        no_workers_input.setCellStyle(normal_style_b_border);
        no_workers_input.setCellValue("");

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
        supervisor_input.setCellValue("");

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
        
         int emptyRows = 0;
        rowSearch:
        for (int row = 0; row < prodtable.getRowCount(); row++) { //Iterate through all the rows
            for (int col = 0; col < prodtable.getColumnCount(); col++) { //Iterate through all the columns in the row
                if (prodtable.getValueAt(row, col) != null) { //Check if the box is empty
                    String qty = prodtable.getValueAt(row, col).toString();
                    if (!qty.isEmpty()) {

                        continue rowSearch; //If the value is not null, the row contains stuff so go onto the next row
                    }
                }
            }
            emptyRows++; //Conditional never evaluated to true so none of the columns in the row contained anything
        }
      

        Row row = null;
        Cell cell = null;
        for (int i = 0; i < (prodtable.getRowCount()- emptyRows); i++) {
            row = sheet1.createRow(i + 11);
            
            sheet1.addMergedRegion(new CellRangeAddress(
                11+i, //first row (0-based)
                11+i, //last row  (0-based)
                8, //first column (0-based)
                9 //last column  (0-based)
        ));
        
        sheet1.addMergedRegion(new CellRangeAddress(
                11+i, //first row (0-based)
                11+i, //last row  (0-based)
                10, //first column (0-based)
                11 //last column  (0-based)
        ));
        
        sheet1.addMergedRegion(new CellRangeAddress(
                11+i, //first row (0-based)
                11+i, //last row  (0-based)
                3, //first column (0-based)
                7 //last column  (0-based)
        ));
            
            
         Cell shift_no = row.createCell(0);
         shift_no.setCellValue(prodtable.getValueAt(i, 0).toString());
         
         Cell shift_so_no = row.createCell(1);
         shift_so_no.setCellValue(prodtable.getValueAt(i, 1).toString());
         
         Cell shift_custa = row.createCell(2);
         shift_custa.setCellValue(prodtable.getValueAt(i, 2).toString());
         
         Cell shift_pno = row.createCell(3);
         shift_pno.setCellValue(prodtable.getValueAt(i, 3).toString());
         
         Cell shift_prodq = row.createCell(8);
         shift_prodq.setCellValue(prodtable.getValueAt(i, 4).toString());
         
         Cell shift_packq = row.createCell(10);
         shift_packq.setCellValue(prodtable.getValueAt(i, 5).toString());
         
         Cell shift_new = row.createCell(12);
         shift_new.setCellValue(prodtable.getValueAt(i, 6).toString());
         
         Cell shift_used = row.createCell(13);
         shift_used.setCellValue(prodtable.getValueAt(i, 7).toString());
         
         Cell shift_bags = row.createCell(14);
         shift_bags.setCellValue(prodtable.getValueAt(i, 8).toString());
        
            
        }
        
        
        
        
        
        
        

        try {
            //template.close();
            FileOutputStream output_file = new FileOutputStream(file);
            workbook.write(output_file);
            output_file.close();
        } catch (Exception e) {

        }

    }
    
    public void test(JTable prodtable, File file){
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
