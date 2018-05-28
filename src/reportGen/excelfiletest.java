/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reportGen;

import java.io.FileOutputStream;
import java.io.IOException;
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
        issueNumber.setCellStyle(normal_style);
        issueNumber.setCellValue("Issue Number");
        sheet1.addMergedRegion(new CellRangeAddress(
                3, //first row (0-based)
                3, //last row  (0-based)
                5, //first column (0-based)
                9 //last column  (0-based)
        ));
        //revision Number cell
        Cell revisionNumber = fourthrow.createCell(5);
        revisionNumber.setCellStyle(normal_style);
        revisionNumber.setCellValue("Revision Number");
        sheet1.addMergedRegion(new CellRangeAddress(
                3, //first row (0-based)
                3, //last row  (0-based)
                10, //first column (0-based)
                14 //last column  (0-based)
        ));
        //document Number cell
        Cell documentNumber = fourthrow.createCell(10);
        documentNumber.setCellStyle(normal_style);
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
        date_of_issue.setCellStyle(normal_style);
        date_of_issue.setCellValue("Date of Issue");
        sheet1.addMergedRegion(new CellRangeAddress(
                4, //first row (0-based)
                4, //last row  (0-based)
                5, //first column (0-based)
                9 //last column  (0-based)
        ));
        //revision Number cell
        Cell date_of_revision = fifthrow.createCell(5);
        date_of_revision.setCellStyle(normal_style);
        date_of_revision.setCellValue("Date of Revision");
        sheet1.addMergedRegion(new CellRangeAddress(
                4, //first row (0-based)
                4, //last row  (0-based)
                10, //first column (0-based)
                14 //last column  (0-based)
        ));
        //document Number cell
        Cell page= fifthrow.createCell(10);
        page.setCellStyle(normal_style);
        page.setCellValue("Page: 01");
        
        
        
        
        
        
        try {
            //template.close();
            FileOutputStream output_file = new FileOutputStream("C:\\Users\\ViduraDan\\Desktop\\test.xlsx");
            workbook.write(output_file);
            output_file.close();
        } catch (IOException e) {

        }

    }
    
    

}
