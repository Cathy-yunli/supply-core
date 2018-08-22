package com.lingshou.supply.common.excel;

import com.lingshou.supply.contract.exception.ServiceException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yunli on 2017/10/26
 */
public class ExcelUtil {


    //行数
    private static final int ROW = 65535;

    //列数
    private static final int COLUMN = 255;

    private static Logger LOG = Logger.getLogger(ExcelUtil.class);

    /**
     * @description: 生成Excel
     * @params: rows 要导出的数据
     * @params: headers 头信息
     * @params: sheetName sheet名称
     * @params: outputStream 输出流
     * @return:
     */

    public static <T> void generateExcel(List<T> rows, List<String> headers, String sheetName, OutputStream outputStream) {

        HSSFWorkbook workbook = initWorkbook(headers, sheetName);

        try {

            if (headers.size() > COLUMN || rows.size() > ROW) {
                throw new ServiceException("行数或列数超过限制");
            }
            List<List<String>> finalRows = dealWithRows(rows);


            if (!CollectionUtils.isEmpty(finalRows) && headers.size() != finalRows.get(0).size()) {
                throw new ServiceException("标题个数与属性个数不一致");
            }

            fillDateToExcel(workbook, finalRows);

            workbook.write(outputStream);

        } catch (Exception e) {
            if(e instanceof ServiceException){
                throw (ServiceException)e;
            }
            throw new ServiceException(e.getMessage(), e);

        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
            } catch (IOException ie) {
                LOG.error("workbook close error", ie);
            }


        }
    }


    private static HSSFWorkbook initWorkbook(List<String> headers, String title) {

        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 15);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        // 把字体应用到当前的样式
        style.setFont(font);

        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for (short i = 0; i < headers.size(); i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers.get(i));
            cell.setCellValue(text);
        }
        return workbook;
    }

    private static void fillDateToExcel(HSSFWorkbook workbook, List<List<String>> finalRows) throws Exception {

        HSSFSheet sheet = workbook.getSheetAt(0);

        for (int i = 0; i < finalRows.size(); i++) {
            HSSFRow hssfRow = sheet.createRow(i + 1);

            List<String> row = finalRows.get(i);
            for (int j = 0; j < row.size(); j++) {
                HSSFRichTextString richString = new HSSFRichTextString(row.get(j));
                hssfRow.createCell(j).setCellValue(richString);
            }
        }
    }

    private static <T> List<List<String>> dealWithRows(List<T> rows) throws Exception {

        List<List<String>> finalRows = new ArrayList<>();
        Iterator<T> it = rows.iterator();
        while (it.hasNext()) {
            T t = it.next();

            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            Field[] fields = t.getClass().getDeclaredFields();

            List<String> row = new ArrayList<>();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), t.getClass());
                Method getMethod = pd.getReadMethod();
                Object value = getMethod.invoke(t);

                String realValue = "";
                if (value != null) {
                    realValue = transData(value);
                }

                row.add(realValue);
            }
            finalRows.add(row);
        }
        return finalRows;
    }

    private static String transData(Object value) {

        if (value instanceof Date) {
            Date date = (Date) value;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(date);
        } else {
            return value.toString();
        }
    }
}
