package com.tlwl.datax.job.v2.template.toMysql;

import cn.hutool.core.lang.Dict;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.tlwl.datax.job.v2.template.toMysql.enums.TMTempEnum;
import com.tlwl.datax.job.v2.template.toMysql.factory.impl.SimpleFactory;
import com.tlwl.datax.job.v2.template.toMysql.strategy.impl.SimpleStrategy;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @author houjiawei
 * @date 2024/8/1 15:51
 */

public class AdsInit {

    /**
     * 额外的sql
     */
    private static Map<String, String> extraSql = Map.of("ads_income_consume_refund_course_sy", "where create_time > '2024-05-01 00:00:00' and create_time<'2024-08-01 23:59:59'");

    private static Map<String, TMTempEnum> jobTempMap = Map.of();

    public static void main(String[] args) {

        SimpleStrategy strategy = new SimpleStrategy();
        SimpleFactory factory = new SimpleFactory(strategy);

        Map<String, Dict> rMap = new HashMap<>();
        String filePath = "D:\\workspace-local\\Dxjconfinit\\src\\main\\resources\\ods_output.xlsx";
        String importPath = "D:\\workspace-local\\Dxjconfinit\\src\\main\\resources\\ods_output_detail.xlsx";
        try (FileInputStream file = new FileInputStream(new File(filePath))) {
            XSSFWorkbook workbook = null;
            workbook = new XSSFWorkbook(file);
            int numberOfSheets = workbook.getNumberOfSheets();
            if (numberOfSheets < 1) {
                System.out.println("SHEET NUMBER IS ERROR");
                return;
            }
            Dict dict = null;
            for (int i = 1; i < numberOfSheets; i++) {
                XSSFSheet sheetAt = workbook.getSheetAt(i);
                String mysqlTName = sheetAt.getRow(0).getCell(0).getStringCellValue();
                if (sheetAt.getRow(0).getCell(0).getCellStyle().getFont().getStrikeout()) {
                    continue;
                }
                dict = factory.readExcel(sheetAt, extraSql.get(mysqlTName), jobTempMap.get(mysqlTName));
                rMap.put(mysqlTName, dict);
            }
            // 生成Excel
            workbook = new XSSFWorkbook();
            for (String key : rMap.keySet()) {
                // sheet限制字段长度
                String md5 = SecureUtil.md5(key);
                Dict res = rMap.get(key);
                String job = res.getStr("job");
                String ddl = res.getStr("ddl");
                XSSFSheet sheet = workbook.createSheet(md5);
                XSSFRow jobRow = sheet.createRow(0);
                XSSFCell jobCell = jobRow.createCell(0);
                jobCell.setCellValue(JSONUtil.formatJsonStr(job));
                XSSFRow ddlRow = sheet.createRow(1);
                XSSFCell ddlCell = ddlRow.createCell(0);
                ddlCell.setCellValue(ddl);
            }
            FileOutputStream fileOutputStream = new FileOutputStream(importPath);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            System.out.println("excel导出成功");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
