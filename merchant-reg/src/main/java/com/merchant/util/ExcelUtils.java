package com.merchant.util;


import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.thymeleaf.util.StringUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @program: muse-pay
 * @description: EXCEL操作类
 * @author: Vincent
 * @create: 2018-11-30 09:05
 **/
public class ExcelUtils {

    public static void main(String[] args) throws IOException {

    }

    /**
     * 读取文档
     *
     * @param excelPath  Excel文件路径
     * @param sheetIndex 工作表索引
     * @param rowIndex   行号
     * @param colAlpha   列字母
     * @return List
     * @throws Exception 异常
     */
    public static List<Object> readExcel(String excelPath, int sheetIndex, int rowIndex, String colAlpha) throws Exception {
        if (StringUtils.isEmpty(excelPath)) {
            throw new Exception("文件[" + excelPath + "]不存在");
        }

        int colIndex = convertIndex(colAlpha);
        FileInputStream in = new FileInputStream(excelPath);
        sheetIndex -= 1;
        rowIndex -= 1;

        // 根据文件类型获取工作簿
        Workbook workbook = null;
        if (excelPath.toUpperCase().endsWith("XLSX")) {
            workbook = new XSSFWorkbook(in);
        } else if (excelPath.toUpperCase().endsWith("XLS")) {
            workbook = new HSSFWorkbook(in);
        }
        if (workbook == null) {
            throw new Exception("获取工作簿异常，工作簿为空");
        }

        // 获取sheet的总数
        int numberOfSheets = workbook.getNumberOfSheets();
        if (sheetIndex >= numberOfSheets) {
            throw new Exception("Sheet序号超出总数[" + sheetIndex + " / " + numberOfSheets + "]");
        }

        // 获取指定的Sheet
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        System.out.println("工作表：" + sheet.getSheetName());

        // 获取每一列的迭代器
        Iterator<Row> rowIterator = sheet.iterator();
        List<Object> resultList = new ArrayList<>();

        // 获取指定的列
        int count = 0;
        while (rowIterator.hasNext()) {
            // 获取行对象
            Row row = rowIterator.next();
            if (count >= rowIndex) {
                if (colIndex == -1) {
                    List<Object> rowValueList = new ArrayList<>();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        rowValueList.add(getCellValue(cell));
                    }
                    resultList.add(rowValueList);
                } else {
                    String target = getCellValue(row, colIndex);
                    resultList.add(target);
                }
            }
            count++;
        }
        in.close();

        return resultList;
    }


    /**
     * 读取单元格内容
     *
     * @param row
     * @param cellIndex
     * @return
     */
    private static String getCellValue(Row row, int cellIndex) {
        //得到单元格对象
        Cell cell = row.getCell(cellIndex);
        if (cell == null) {
            return "";
        }

        Object value = getCellValue(cell);
        if (value == null) {
            return "";
        } else {
            return value.toString();
        }
    }

    /**
     * 获取单元格值
     *
     * @param cell 单元格
     * @return String
     */
    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }

        // 判断数据的类型
        if (cell.getCellType() == CellType.NUMERIC) {// 数字
            if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                SimpleDateFormat sdf;
                if (cell.getCellStyle().getDataFormat() == 14) {
                    sdf = new SimpleDateFormat("yyyy/MM/dd");
                } else if (cell.getCellStyle().getDataFormat() == 21) {
                    sdf = new SimpleDateFormat("HH:mm:ss");
                } else if (cell.getCellStyle().getDataFormat() == 21) {
                    sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
                } else if (cell.getCellStyle().getDataFormat() == 22) {
                    sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                } else {
                    sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                }
                Date date = cell.getDateCellValue();
                cellValue = sdf.format(date);
            } else if (cell.getCellStyle().getDataFormat() == 0) {//处理数值格式
                cell.setCellType(CellType.STRING);
                cellValue = String.valueOf(cell.getRichStringCellValue().getString());
            }
        } else if (cell.getCellType() == CellType.STRING) { // 字符串
            cellValue = String.valueOf(cell.getStringCellValue());
        } else if (cell.getCellType() == CellType.BOOLEAN) { // 布尔值
            cellValue = String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == CellType.FORMULA) { // 公式
            cellValue = String.valueOf(cell.getCellFormula());
        } else if (cell.getCellType() == CellType.BLANK || cell.getCellType() == CellType._NONE) { // 空值
            cellValue = null;
        } else if (cell.getCellType() == CellType.ERROR) { // 异常
            cellValue = "错误";
        } else {
            cellValue = "未知类型";
        }

        return cellValue;
    }


    /**
     * 将字母转换成序号
     *
     * @param alpha
     * @return
     */
    private static int convertIndex(String alpha) {
        if (StringUtils.isEmpty(alpha)) {
            return 0;
        } else if (alpha.length() > 1) {
            return -1;
        }
        final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for (int index = 0; index < alphabet.length(); index++) {
            if (alpha.toUpperCase().toCharArray()[0] == alphabet.charAt(index)) {
                return index;
            }
        }
        return 0;
    }

    /**
     * 写Excel文件
     *
     * @param folder
     * @param fileName
     * @param fileType
     * @param titleList
     * @param contentList
     * @throws Exception
     */
    public static void writeExcel(String folder, String fileName, String fileType, List<String> titleList,
                                  List<Object> contentList) throws Exception {
        // 检查参数
        if (StringUtils.isEmpty(folder) || StringUtils.isEmpty(fileName) || StringUtils.isEmpty(fileType)) {
            throw new Exception("写EXCEL文件，参数异常");
        }

        File outputFile = new File(folder + File.separator + fileName + "." + fileType);

        // 创建Workbook
        Workbook workbook;
        if (fileType.equals("xls")) {
            workbook = new HSSFWorkbook();
        } else if (fileType.equals("xlsx")) {
            workbook = new XSSFWorkbook();
        } else {
            throw new Exception("文件格式[" + fileType + "]不正确");
        }

        Sheet sheet = null;
        // 创建文件
        if (!outputFile.exists()) {
            //创建sheet对象
            sheet = workbook.createSheet("sheet1");
            OutputStream outputStream = new FileOutputStream(outputFile);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        }
        // 创建sheet对象
        if (sheet == null) {
            sheet = workbook.createSheet("sheet1");
        }
        sheet.autoSizeColumn(1, true);

        //添加表头
        int titleRowCount = 0;
        Row row = sheet.createRow(0);
        if (titleList != null && titleList.size() > 0) {
            titleRowCount += 1;
            for (int i = 0; i < titleList.size(); i++) {
                row.createCell(i).setCellValue(titleList.get(i));
            }
        }
        // 循环写入行数据
        for (int i = 0; i < contentList.size(); i++) {
            // 行
            row = sheet.createRow(i + titleRowCount);

            List<Object> rowContent;
            Object item = contentList.get(i);
            if (item instanceof List) {
                rowContent = (List<Object>) item;
            } else {
                throw new Exception("数据内容的类型有误");
            }
            // 行 -> 列
            for (int j = 0; j < rowContent.size(); j++) {
                row.createCell(j).setCellValue((String) rowContent.get(j));
            }
        }
        //创建文件流
        OutputStream stream = new FileOutputStream(outputFile);
        //写入数据
        workbook.write(stream);
        stream.flush();
        stream.close();
        System.out.println("文件地址：" + outputFile.getPath());
    }


}
