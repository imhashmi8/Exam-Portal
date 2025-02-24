package com.snort.helper;

import com.snort.entities.Question;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Helper {

    // Check if the file is in Excel format
    public static boolean checkExcelFormat(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    // Convert Excel to a list of Question objects
    public static List<Question> convertExcelToListOfQuestions(InputStream is) {
        List<Question> list = new ArrayList<>();

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheet("data");

            Iterator<Row> iterator = sheet.iterator();
            int rowNumber = 0;

            while (iterator.hasNext()) {
                Row row = iterator.next();

                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cells = row.iterator();
                int cid = 0;
                Question question = new Question();

                while (cells.hasNext()) {
                    Cell cell = cells.next();

                    switch (cid) {
                        case 0:
                            question.setId((long) cell.getNumericCellValue());
                            break;
                        case 1:
                            question.setQuestionType(cell.getStringCellValue());
                            break;
                        case 2:
                            question.setOption1(cell.getStringCellValue());
                            break;
                        case 3:
                            question.setOption2(cell.getStringCellValue());
                            break;
                        case 4:
                            question.setOption3(cell.getStringCellValue());
                            break;
                        case 5:
                            question.setOption4(cell.getStringCellValue());
                            break;
                        case 6:
                            question.setTitle(cell.getStringCellValue());
                            break;
                        case 7:
                            question.setTotalMarks((int) cell.getNumericCellValue());
                            break;
                        case 8:
                            question.setCategory(cell.getStringCellValue());
                            break;
                        case 9:
                            question.setLevel(cell.getStringCellValue());
                            break;
                        case 10:
                            question.setQuestionDescription(cell.getStringCellValue());
                            break;
                        case 11:
                            question.setSetNumber((int) cell.getNumericCellValue());
                            break;
                        case 12:
                            question.setCorrectAnswer(cell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    cid++;
                }

                list.add(question);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}
