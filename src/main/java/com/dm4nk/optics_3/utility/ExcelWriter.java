package com.dm4nk.optics_3.utility;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelWriter {
    //public static final int n = 200;

    public static void write(String filename, List<List<Double>> z) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(filename);

        XSSFRow row;
        Cell cell;

        int firstNum = 0;

        for (int i = firstNum; i < z.size(); ++i) {
            row = sheet.createRow(i + 1);

            for (int j = 0; j < z.size(); ++j) {
                cell = row.createCell(j);
                cell.setCellValue(z.get(i).get(j));
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream("src/main/resources/" + filename + ".xlsx")) {
            workbook.write(outputStream);
        }
    }
}
