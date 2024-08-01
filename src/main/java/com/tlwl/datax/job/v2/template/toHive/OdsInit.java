package com.tlwl.datax.job.v2.template.toHive;


import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import com.tlwl.datax.job.v2.template.toHive.enums.TemplateEnum;
import com.tlwl.datax.job.v2.template.toHive.factory.impl.OrcFactory;
import com.tlwl.datax.job.v2.template.toHive.factory.impl.SimpleFactory;
import com.tlwl.datax.job.v2.template.toHive.strategy.impl.OrcStrategy;
import com.tlwl.datax.job.v2.template.toHive.strategy.impl.SimpleStrategy;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  目前ODS模板只分为textfile和 orc模板，支持再这两个模板上扩展动态job和自定义sql
 *  支持自定义Job模板：
 *      1.在temp包下创建自定义模板，并配置初始化的公共值不变的值
 *      2.在TemplateEnum中添加枚举类型
 *      3.在jobTempMap中将表和自定义模板绑定
 *      4.在strategy代码的switch块中添加对应的逻辑即可
 * @author houjiawei
 * @Description
 * @date 2024/8/1 9:10
 */

public class OdsInit {

//    private static List<String> SPECIAL = List.of();

    /**
     * 分区表对应分区字段
     */
    private static Map<String, List<String>> partitions = Map.of();

    /**
     * 额外的sql
     */
    private static Map<String, String> extraSql = Map.of();

    /**
     * 自定义job模板，默认是Simple,如果 Map<String, List<String>> partitions 不为空,则默认使用ORC模板
     * "bc_business", TemplateEnum.SIMPLE
     */
    private static Map<String, TemplateEnum> jobTempMap = Map.of();

    public static void main(String[] args) {

        Map<String, Dict> rMap = new HashMap<>();
        SimpleStrategy strategy = new SimpleStrategy();
        SimpleFactory factory = new SimpleFactory(strategy);
        OrcStrategy orcStrategy = new OrcStrategy();
        OrcFactory orcFactory = new OrcFactory(orcStrategy);
        String filePath = "D:\\workspace-local\\Dxjconfinit\\src\\main\\resources\\ods_input.xlsx";
        String importPath = "D:\\workspace-local\\Dxjconfinit\\src\\main\\resources\\ods_input_detail.xlsx";
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
                if (partitions.containsKey(mysqlTName)) {
                    dict = orcFactory.readExcel(sheetAt, partitions.get(mysqlTName), extraSql.get(mysqlTName), jobTempMap.get(mysqlTName));
                } else {
                    // 默认使用简单模板：简单sql和简单建表语句
                    dict = factory.readExcel(sheetAt, null, extraSql.get(mysqlTName), jobTempMap.get(mysqlTName));
                }
                rMap.put(mysqlTName, dict);
            }
            // 生成Excel
            workbook = new XSSFWorkbook();
            for (String key : rMap.keySet()) {
                Dict res = rMap.get(key);
                String job = res.getStr("job");
                String ddl = res.getStr("ddl");
                XSSFSheet sheet = workbook.createSheet(key);
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
