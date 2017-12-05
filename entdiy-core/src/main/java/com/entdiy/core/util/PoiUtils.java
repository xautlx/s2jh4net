package com.entdiy.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class PoiUtils {

    private final static Logger logger = LoggerFactory.getLogger(PoiUtils.class);

    /**
     * <pre>
     *    指定获取某列所有数据
     * </pre>
     * 
     * @return
     */
    public static List<Map<String, String>> readExcelSpecifyColNum(InputStream is, Integer readFromRowNum, Integer specifyColNum) {

        return readExcelSpecifyColNum(is, null, readFromRowNum, specifyColNum);
    }

    public static List<Map<String, String>> readExcelContent(MultipartFile excelFile, Integer sheetIndex, Integer readFromRowNum,
            Integer readFromColNum) {
        return readExcelContent(excelFile, sheetIndex, null, readFromRowNum, readFromColNum);
    }

    public static List<Map<String, String>> readExcelContent(MultipartFile excelFile, String sheetName, Integer readFromRowNum, Integer readFromColNum) {
        return readExcelContent(excelFile, null, sheetName, readFromRowNum, readFromColNum);
    }

    /**
     * <pre>
     * 读取Excel数据内容
     * 约定格式要求：第一行为标题行，之后为数据行
     * 返回结构为Map结构的List集合：每行的key=第一行的标题，value=单元格值，统一为字符串，根据需要自行转换数据类型
     * </pre>
     * @param InputStream
     * @return Map 包含单元格数据内容的Map对象
     */
    public static List<Map<String, String>> readExcelContent(MultipartFile excelFile, String sheetName) {
        return readExcelContent(excelFile, sheetName, 0, 0);
    }

    /**
     * <pre>
     * 读取Excel数据内容
     * 约定格式要求：第readFromRowNum行为标题行，之后为数据行
     * 返回结构为Map结构的List集合：每行的key=第一行的标题，value=单元格值，统一为字符串，根据需要自行转换数据类型
     * </pre>
     * @param InputStream
     * @return Map 包含单元格数据内容的Map对象
     */
    public static List<Map<String, String>> readExcelContent(MultipartFile excelFile, String sheetName, Integer readFromRowNum) {
        return readExcelContent(excelFile, sheetName, readFromRowNum, 0);
    }

    /**
     * <pre>
     * 读取Excel数据内容
     * 约定格式要求：第readFromRowNum行为标题行，之后为数据行
     * 返回结构为Map结构的List集合：每行的key=第一行的标题，value=单元格值，统一为字符串，根据需要自行转换数据类型
     * </pre>
     * @param InputStream
     * @return Map 包含单元格数据内容的Map对象
     */
    public static List<Map<String, String>> readExcelContent(MultipartFile excelFile, Integer sheetIndex, Integer readFromRowNum) {
        return readExcelContent(excelFile, sheetIndex, null, readFromRowNum, 0);
    }

    /**
     * <pre>
     * 读取Excel数据内容
     * 约定格式要求：第(titleStartRowNum+1)行为标题行，之后为数据行
     * 返回结构为Map结构的List集合：每行的key=第(titleStartRowNum+1)行的标题，
     * value=单元格值，统一为字符串，根据需要自行转换数据类型
     * </pre>
     * 
     * @param excelName                  表格名称
     * @param sheetName                  读取工作标签项名称
     * @param readFromRowNum             以readFromRowNum作为标题开始读取
     * @param readFromColNum             从readFromColNum列开始读取
     * @return Map                       包含单元格数据内容的Map对象
     */
    public static List<Map<String, String>> readExcelContent(MultipartFile excelFile, Integer sheetIndex, String sheetName, Integer readFromRowNum,
            Integer readFromColNum) {
        List<Map<String, String>> rows = Lists.newArrayList();
        if (excelFile.isEmpty()) {
            return rows;
        }
        InputStream is = null;
        String excelName = excelFile.getOriginalFilename();
        try {
            //读取Excel文件
            is = excelFile.getInputStream(); //this.getClass().getResourceAsStream(excelName);
            if (excelName.toLowerCase().endsWith(".xls")) {

                Workbook wb = new HSSFWorkbook(is);
                Sheet sheet = null;
                if (StringUtils.isNotBlank(sheetName)) {
                    logger.debug("Excel: {}, Sheet: {}", excelName, sheetName);
                    sheet = wb.getSheet(sheetName);
                } else {

                    if (null == sheetIndex) {
                        sheetIndex = 0;
                    }
                    sheet = wb.getSheetAt(sheetIndex);
                    sheetName = sheet.getSheetName();
                }
                int colNum = readFromColNum;
                Row row0 = sheet.getRow(readFromRowNum);
                // 标题总列数
                List<String> titleList = Lists.newArrayList();
                while (true) {
                    Cell cell = row0.getCell(colNum);
                    if (cell == null) {
                        break;
                    }
                    String title = getCellFormatValue(cell);
                    if (StringUtils.isBlank(title)) {
                        break;
                    }
                    titleList.add(title);
                    colNum++;
                    logger.debug(" - Title : {} = {}", colNum, title);
                }
                logger.debug("Excel: {}, Sheet: {}, Column Num: {}", excelName, sheetName, colNum);
                String[] titles = titleList.toArray(new String[titleList.size()]);

                // 正文内容应该从第readFromRowNum + 1行开始,第readFromRowNum行为表头的标题
                int rowNum = readFromRowNum + 1;
                while (rowNum > readFromRowNum) {
                    Row row = sheet.getRow(rowNum++);
                    if (row == null) {
                        break;
                    }
                    Map<String, String> rowMap = Maps.newHashMap();
                    rowMap.put("sheetName", sheetName);

                    Cell firstCell = row.getCell(readFromColNum);
                    //假如第colNum列并且为空则终止行项数据处理
                    if (firstCell == null) {
                        logger.info("End as first cell is Null at row: {}", rowNum);
                        break;
                    }

                    int j = readFromColNum;
                    int titleCnt = 0;
                    while (j < colNum) {
                        Cell cell = row.getCell(j);
                        if (cell != null) {
                            String cellValue = getCellFormatValue(cell);
                            if (StringUtils.isNotBlank(cellValue)) {
                                rowMap.put(titles[titleCnt], cellValue);
                            }
                        }
                        titleCnt++;
                        j++;
                    }
                    if (rowNum > readFromRowNum) {
                        rows.add(rowMap);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        logger.debug("Row Map Data: {}", rows);
        return rows;
    }

    /**
     * 读取Excel数据内容
     * 约定格式要求：第一行为标题行，之后为数据行
     * 返回结构为Map结构的List集合：每行的key=第一行的标题，value=单元格值，统一为字符串，根据需要自行转换数据类型
     * 
     * @param InputStream
     * @return Map 包含单元格数据内容的Map对象
     */
    public static List<Map<String, String>> readExcelContent(InputStream is, String excelName, String sheetName) {
        List<Map<String, String>> rows = Lists.newArrayList();
        try {
            Workbook wb = new HSSFWorkbook(is);
            logger.debug("Excel: {}, Sheet: {}", excelName, sheetName);
            Sheet sheet = wb.getSheet(sheetName);
            Row row0 = sheet.getRow(0);
            // 标题总列数
            int colNum = 0;
            List<String> titleList = Lists.newArrayList();
            while (true) {
                Cell cell = row0.getCell(colNum);
                if (cell == null) {
                    break;
                }
                String title = getCellFormatValue(cell);
                if (StringUtils.isBlank(title)) {
                    break;
                }
                titleList.add(title);
                colNum++;
                logger.debug(" - Title : {} = {}", colNum, title);
            }
            logger.debug("Excel: {}, Sheet: {}, Column Num: {}", excelName, sheetName, colNum);
            String[] titles = titleList.toArray(new String[titleList.size()]);

            // 正文内容应该从第二行开始,第一行为表头的标题
            int rowNum = 1;
            while (rowNum > 0) {
                Row row = sheet.getRow(rowNum++);
                if (row == null) {
                    break;
                }
                Map<String, String> rowMap = Maps.newHashMap();
                rowMap.put("sheetName", sheetName);

                Cell firstCell = row.getCell(0);
                //假如第一列并且为空则终止行项数据处理
                if (firstCell == null) {
                    logger.info("End as firt cell is Null at row: {}", rowNum);
                    break;
                }

                int j = 0;
                while (j < colNum) {
                    Cell cell = row.getCell(j);
                    if (cell != null) {
                        String cellValue = getCellFormatValue(cell);
                        rowMap.put(titles[j], cellValue);
                    }
                    j++;
                }
                if (rowNum > 0) {
                    rows.add(rowMap);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        logger.debug("Row Map Data: {}", rows);
        return rows;
    }

    /**
     * 根据HSSFCell类型设置数据
     * 
     * @param cell
     * @return
     */
    private static String getCellFormatValue(Cell cell) {
        String cellvalue = null;
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
            // 如果当前Cell的Type为NUMERIC
            case Cell.CELL_TYPE_NUMERIC:
            case Cell.CELL_TYPE_FORMULA: {
                // 判断当前的cell是否为Date
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // 如果是Date类型则，转化为Data格式

                    //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                    //cellvalue = cell.getDateCellValue().toLocaleString();

                    //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    cellvalue = sdf.format(date);

                }
                // 如果是纯数字
                else {
                    // 取得当前Cell的数值
                    DecimalFormat df = new DecimalFormat("#.####");
                    cellvalue = df.format(cell.getNumericCellValue());
                }
                break;
            }
            // 如果当前Cell的Type为STRIN
            case Cell.CELL_TYPE_STRING:
                // 取得当前的Cell字符串
                cellvalue = cell.getRichStringCellValue().getString();
                break;
            }
        }
        if (cellvalue == null) {
            logger.warn("NULL cell value [{}, {}]", cell.getRowIndex(), cell.getColumnIndex());
        } else {
            cellvalue = cellvalue.trim();
        }
        return cellvalue;
    }

    /**
     * <pre>
     *   读取Excel指定列内容
     * </pre>
     * 
     * @param excelName                  表格名称
     * @param sheetName                  读取工作标签项名称
     * @param readFromRowNum             以readFromRowNum作为标题开始读取
     * @param readFromColNum             从readFromColNum列开始读取
     * @return Map                       包含单元格数据内容的Map对象
     */
    public static List<Map<String, String>> readExcelSpecifyColNum(InputStream is, Integer sheetIndex, Integer readFromRowNum, Integer specifyColNum) {
        List<Map<String, String>> rows = Lists.newArrayList();
        String sheetName = "defaultSheet";
        try {
            //读取Excel文件
            Workbook wb = new HSSFWorkbook(is);
            Sheet sheet = null;
            if (null == sheetIndex) {
                sheetIndex = 0;
            }

            if (null == readFromRowNum) {
                readFromRowNum = 0;
            }
            sheet = wb.getSheetAt(sheetIndex);
            int colNum = 0;
            Row row0 = sheet.getRow(readFromRowNum);
            // 标题总列数
            List<String> titleList = Lists.newArrayList();
            while (colNum <= specifyColNum) {

                Cell cell = row0.getCell(colNum);
                if (cell == null) {
                    break;
                }
                String title = getCellFormatValue(cell);
                if (StringUtils.isBlank(title)) {
                    break;
                }
                titleList.add(title);
                logger.debug(" - Title : {} = {}", colNum, title);
                colNum++;
            }
            logger.debug("Sheet: {}, Column Num: {}", sheetIndex, colNum);
            String[] titles = titleList.toArray(new String[titleList.size()]);

            // 正文内容应该从第readFromRowNum + 1行开始,第readFromRowNum行为表头的标题
            int rowNum = readFromRowNum + 1;
            while (rowNum > readFromRowNum) {
                Row row = sheet.getRow(rowNum++);
                if (row == null) {
                    break;
                }
                Map<String, String> rowMap = Maps.newHashMap();
                rowMap.put("sheetName", sheetName);

                Cell firstCell = row.getCell(specifyColNum);
                //假如第colNum列并且为空则终止行项数据处理
                if (firstCell == null) {
                    logger.info("End as first cell is Null at row: {}", rowNum);
                    break;
                }

                int j = 0;
                int titleCnt = 0;
                while (j < colNum) {
                    Cell cell = row.getCell(j);
                    if (cell != null) {
                        String cellValue = getCellFormatValue(cell);
                        if (StringUtils.isNotBlank(cellValue)) {
                            rowMap.put(titles[titleCnt], cellValue);
                        }
                    }
                    titleCnt++;
                    j++;
                }
                if (rowNum > readFromRowNum) {
                    rows.add(rowMap);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        logger.debug("Row Map Data: {}", rows);
        return rows;
    }

    public static void main(String[] args) {

    }

}
