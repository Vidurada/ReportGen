/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reportGen;

import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author ViduraDan
 */
public class excelfiletest {

    public static void main(String args[]) {

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
        

        try {
            //template.close();
            FileOutputStream output_file = new FileOutputStream("C:\\Users\\ViduraDan\\Desktop\\test.xlsx");
            workbook.write(output_file);
            output_file.close();
        } catch (IOException e) {

        }

    }

}
