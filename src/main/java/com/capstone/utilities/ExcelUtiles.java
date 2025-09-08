package com.capstone.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtiles {

    static String projectpath = System.getProperty("user.dir");

    public static Object[][] getData(String excelpath, String sheetname) throws IOException {
        File file1 = new File(excelpath);
        FileInputStream fs = new FileInputStream(file1);
        XSSFWorkbook workbook = new XSSFWorkbook(fs);
        XSSFSheet worksheet = workbook.getSheet(sheetname);

        int rowcount = worksheet.getPhysicalNumberOfRows();
        int colcount = worksheet.getRow(0).getPhysicalNumberOfCells();

        System.out.println("rows: " + rowcount + ", cols: " + colcount);

        String[][] data = new String[rowcount][colcount];

        for (int i = 0; i < rowcount; i++) {
            for (int j = 0; j < colcount; j++) {
                data[i][j] = worksheet.getRow(i).getCell(j).getStringCellValue();
            }
        }

        workbook.close();
        fs.close();
        return data;
    }
}
