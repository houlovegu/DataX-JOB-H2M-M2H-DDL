package com.tlwl.datax.job.init.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.tlwl.datax.job.init.model.ExtraEntity;
import com.tlwl.datax.job.init.model.PartitionEntity;
import com.tlwl.datax.job.init.model.h2m.*;
import com.tlwl.datax.job.init.model.m2h.*;
import com.tlwl.datax.job.init.model.m2h.Reader;
import com.tlwl.datax.job.init.model.m2h.Writer;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author houjiawei
 * @Description
 * @date 2024/7/30 17:22
 */

public class ExcelUtil {

    /**
     * 从excel中读取数据
     *
     * @param inputStream 文件输入流
     * @return 数据封装到对象
     */
    public static Dict getDataFromExcel(InputStream inputStream, ExtraEntity extra) {
        XSSFWorkbook workbook = null;
        List<Result> jobs = new ArrayList<>();
        List<String> HCTs = new ArrayList<>();
        Dict dict = Dict.of("jobs", jobs, "hcts", HCTs);
        try {
            workbook = new XSSFWorkbook(inputStream);
            int numberOfSheets = workbook.getNumberOfSheets();
            if (numberOfSheets < 1) {
                System.out.println("SHEET NUMBER IS ERROR");
                return null;
            }
            for (int i = 1; i < numberOfSheets; i++) {
                Result result = new Result();
                Job job = new Job();
                result.setJob(job);
                jobs.add(result);
                job.setSetting(Setting.builder()
                        .speed(new Speed())
                        .errorLimit(new ErrorLimit())
                        .build());
                XSSFSheet sheetAt = workbook.getSheetAt(i);
                List<Content> contents = new ArrayList<>();
                Content content = new Content();
                contents.add(content);
                job.setContent(contents);
                Reader reader = new Reader();
                ReaderParameter parameter = new ReaderParameter();
                if (ObjectUtil.isNotNull(extra.getDbUsername())) {
                    parameter.setUsername(extra.getDbUsername());
                }
                if (ObjectUtil.isNotNull(extra.getDbPassword())) {
                    parameter.setPassword(extra.getDbPassword());
                }
                List<Connection> connections = new ArrayList<>();
                Connection connection = new Connection();
                if (ObjectUtil.isNotNull(extra.getDbUrl())) {
                    connection.setJdbcUrl(List.of(MessageFormat.format(connection.getJdbcUrl().get(0), extra.getDbUrl(), extra.getDbname())));
                }
                connections.add(connection);
                parameter.setConnection(connections);
                reader.setParameter(parameter);
                content.setReader(reader);
                StringBuffer sb = new StringBuffer();
                sb.append("select").append(" ");
                Writer writer = new Writer();
                content.setWriter(writer);
                // mysql的表名
                String mysqlTName = sheetAt.getRow(0).getCell(0).getStringCellValue();

                WriterParameter writerParameter = new WriterParameter();
                List<Column> columns = new ArrayList<>();
                writerParameter.setColumn(columns);
                writer.setParameter(writerParameter);
                int lastRowNum = sheetAt.getLastRowNum();
                // hive的业务表名
                String hiveTName = sheetAt.getRow(1).getCell(0).getStringCellValue();
                writerParameter.setFileName(hiveTName);
                writerParameter.setPath(writerParameter.getPath() + extra.getHiveDbName() + "/" + hiveTName);
                // HIVE 建表语句
                StringBuffer HTSql = new StringBuffer();
                HTSql.append("create table ").append(hiveTName).append("(");
                String tableDesc = sheetAt.getRow(2).getCell(0).getStringCellValue();
                // 判断是否要分区,如果要分区则去掉create括号中的相同的字段
                List<PartitionEntity> partitionEntities = null;
                List<String> existFields = null;
                if (MapUtil.isNotEmpty(extra.getPartitionedTables()) && extra.getPartitionedTables().containsKey(hiveTName)) {
                    partitionEntities = extra.getPartitionedTables().get(hiveTName);
                    existFields = partitionEntities.stream().map(PartitionEntity::getColumnName).collect(Collectors.toList());
                }
                Column column = null;
                XSSFRow row = null;
                for (int j = 4; j <= lastRowNum; j++) {
                    row = sheetAt.getRow(j);
                    XSSFCell cell = row.getCell(0);
                    if (cell == null || cell.getCellType() == CellType.BLANK) {
                        continue;
                    }
                    column = new Column();

                    // 获取字段名
                    String fieldName = row.getCell(0).getStringCellValue();

                    // 获取字段类型
                    String fieldType = row.getCell(1).getStringCellValue();
                    // 字段描述
                    String fieldDesc = row.getCell(2).getStringCellValue();
                    column.setName(fieldName);
                    if (StrUtil.contains(fieldType,"decimal")) {
                        column.setType("double");
                    } else {
                        column.setType(fieldType);
                    }
                    columns.add(column);

                    if (j == lastRowNum) {
                        sb.append(fieldName);
                        // 组装HIVE建表语句
                    } else {
                        sb.append(fieldName).append(",");
                    }
                    if (CollUtil.isNotEmpty(existFields) && existFields.contains(fieldName)) {
                        continue;
                    }
                    if (j == lastRowNum) {
//                        sb.append(fieldName);
                        // 组装HIVE建表语句
                        HTSql.append(fieldName).append(" ").append(fieldType).append(" comment ").append("'").append(fieldDesc).append("'");
                    } else {
//                        sb.append(fieldName).append(",");
                        HTSql.append(fieldName).append(" ").append(fieldType).append(" comment ").append("'").append(fieldDesc).append("'").append(",");
                    }

                }
                if (ObjectUtil.equals(HTSql.lastIndexOf(","), HTSql.length()-1)) {
                    HTSql.delete(HTSql.length()-1, HTSql.length());
                    sb.delete(sb.length()-1, sb.length());
                }
                HTSql.append(")").append(" comment '").append(tableDesc).append("'");
                if (CollUtil.isNotEmpty(existFields)) {
                    HTSql.append(" ").append("partitioned by (");
                    int size = partitionEntities.size();
                    int startNum = 1;
                    for (PartitionEntity partition:partitionEntities) {
                        HTSql.append(partition.getColumnName()).append(" ").append(partition.getColumnType());
                        if (StrUtil.isNotBlank(partition.getColumnComment())) {
                            HTSql.append(" comment '").append(partition.getColumnComment()).append("'");
                        }
                        if (startNum!=size) {
                            HTSql.append(",");
                        }
                        startNum++;
                    }
                    HTSql.append(")");
                }
                HTSql.append(" row format delimited fields terminated by '\\t'");
                if (CollUtil.isNotEmpty(extra.getOrcTables()) && extra.getOrcTables().contains(hiveTName)) {
                    HTSql.append(" ").append("stored as orc");
                    writerParameter.setFileType("orc");
                }
                HTSql.append(";");
                HCTs.add(HTSql.toString());
                sb.append(" from ").append(mysqlTName);
                List<String> querySqls = new ArrayList<>();
                querySqls.add(sb.toString());
                connection.setQuerySql(querySqls);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return dict;
    }

    public static void importExcel(Dict dataFromExcel, String importPath) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet job = workbook.createSheet("job");
        XSSFSheet ddl = workbook.createSheet("ddl");
        List<Job> jobs = (List<Job>) dataFromExcel.get("jobs");
        List<String> ddls = (List<String>) dataFromExcel.get("hcts");
        for (int i = 0; i < jobs.size(); i++) {
            XSSFRow jobRow = job.createRow(i);
            XSSFRow ddlRow = ddl.createRow(i);
            XSSFCell jobCell = jobRow.createCell(0);
            XSSFCell ddlCell = ddlRow.createCell(0);
            jobCell.setCellValue(JSONUtil.formatJsonStr(JSONUtil.toJsonStr(jobs.get(i))));
            ddlCell.setCellValue(JSONUtil.toJsonStr(ddls.get(i)));
        }

        FileOutputStream fileOutputStream = new FileOutputStream(importPath);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        System.out.println("excel导出成功");
    }


    /**
     * 从excel中读取数据
     *
     * @param inputStream 文件输入流
     * @return 数据封装到对象
     */
    public static Dict getOutFromExcel(InputStream inputStream) {
        XSSFWorkbook workbook = null;
        List<OutputResult> jobs = new ArrayList<>();
        List<String> HCTs = new ArrayList<>();
        Dict dict = Dict.of("jobs", jobs, "hcts", HCTs);
        try {
            workbook = new XSSFWorkbook(inputStream);
            int numberOfSheets = workbook.getNumberOfSheets();
            if (numberOfSheets < 1) {
                System.out.println("SHEET NUMBER IS ERROR");
                return null;
            }
            for (int i = 1; i < numberOfSheets; i++) {
                OutputResult outputResult = new OutputResult();
                OutPutJob job = new OutPutJob();
                outputResult.setJob(job);
                jobs.add(outputResult);
                job.setSetting(OutputSetting.builder()
                        .speed(new OutputSpeed())
                        .errorLimit(new OutputErrorLimit())
                        .build());
                XSSFSheet sheetAt = workbook.getSheetAt(i);
                List<OutputContent> contents = new ArrayList<>();
                OutputContent content = new OutputContent();
                contents.add(content);
                job.setContent(contents);
                //reader
                OutputReader reader = new OutputReader();
                OutputRParameter rParameter = new OutputRParameter();
                List<OutputRConnection> rConnections = new ArrayList<>();
                OutputRConnection rConnection = new OutputRConnection();
                rConnections.add(rConnection);
                // querySqls封装sql
                List<String> querySqls = new ArrayList<>();
                // reader querysql
                StringBuffer querySql = new StringBuffer();
                rConnection.setQuerySql(querySqls);
                rParameter.setConnection(rConnections);
                reader.setParameter(rParameter);
                content.setReader(reader);
                //writer
                OutputWriter writer = new OutputWriter();
                OutputWParameter wParameter = new OutputWParameter();
                // wColumns添加字段名
                List<String> wColumns = new ArrayList<>();
                List<OutputWConnection> wConnections = new ArrayList<>();
                OutputWConnection wConnection = new OutputWConnection();
                // 添加表名
                List<String> wTables = new ArrayList<>();

                wConnection.setTable(wTables);
                wConnections.add(wConnection);
                wParameter.setConnection(wConnections);
                wParameter.setColumn(wColumns);
                writer.setParameter(wParameter);
                content.setWriter(writer);

                // hive的表名
                String hiveTName = sheetAt.getRow(0).getCell(0).getStringCellValue();

                int lastRowNum = sheetAt.getLastRowNum();

                // mysql的业务表名
                String mysqlTName = sheetAt.getRow(1).getCell(0).getStringCellValue();
                String mysqlTDesc = sheetAt.getRow(2).getCell(0).getStringCellValue();
                wTables.add(mysqlTName);
                querySql.append("select ");
                // HIVE 建表语句
                StringBuffer MySql = new StringBuffer();
                MySql.append("create table `").append(mysqlTName).append("` (");
                XSSFRow row = null;
                Boolean primaryKey = false;
                for (int j = 4; j <= lastRowNum; j++) {
                    row = sheetAt.getRow(j);
                    XSSFCell cell = row.getCell(0);
                    if (cell == null || cell.getCellType() == CellType.BLANK) {
                        continue;
                    }
                    // 获取字段名
                    String fieldName = row.getCell(0).getStringCellValue();
                    wColumns.add(fieldName);
                    // 获取字段类型
                    String fieldType = row.getCell(1).getStringCellValue();
                    // 字段描述
                    String fieldDesc = row.getCell(2).getStringCellValue();
                    querySql.append(fieldName);
                    MySql.append("`").append(fieldName).append("`").append(" ").append(fieldType);
                    if (StrUtil.equalsIgnoreCase("id", fieldName)) {
                        primaryKey = true;
                        MySql.append(" ").append("auto_increment");
                    }
                    MySql.append(" comment ").append("'").append(fieldDesc).append("'");
                    if (j != lastRowNum) {
                        MySql.append(",");
                        querySql.append(",");
//                        querySql.append(fieldName);
//                        // 组装HIVE建表语句
//                        MySql.append(fieldName).append(" ").append(fieldType).append(" comment ").append("'").append(fieldDesc).append("'");
                    }
//                    else {
//                        querySql.append(fieldName).append(",");
//                        MySql.append(fieldName).append(" ").append(fieldType).append(" comment ").append("'").append(fieldDesc).append("'").append(",");
//                    }
                }
                if (ObjectUtil.equals(MySql.lastIndexOf(","), MySql.length()-1)) {
                    MySql.delete(MySql.length()-1, MySql.length());
                    querySql.delete(querySql.length()-1, querySql.length());
                }
                if (primaryKey) {
                    MySql.append(", PRIMARY KEY (`id`)");
                    MySql.append(") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='");
                } else {
                    MySql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='");
                }
                MySql.append(mysqlTDesc).append("';");
                HCTs.add(MySql.toString());
                querySql.append(" ").append(hiveTName);
                querySqls.add(querySql.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dict;
    }

    /**
     * 读取信息生成建表DDL
     * @param
     * @return
     */
    public static Dict readFieldFromExcel(FileInputStream inputStream) {
        List<String> ddlsList = new ArrayList<>();
        List<String> dddsList = new ArrayList<>();
        Dict dict = Dict.of("ddl", ddlsList, "ddd", dddsList);
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(inputStream);
            int numberOfSheets = workbook.getNumberOfSheets();
            if (numberOfSheets < 1) {
                System.out.println("SHEET NUMBER IS ERROR");
                return null;
            }
            StringBuffer ddl = null;
            for (int i = 1; i < numberOfSheets; i++) {
                XSSFSheet sheetAt = workbook.getSheetAt(i);
                // source 的表名
                String hiveSName = sheetAt.getRow(0).getCell(0).getStringCellValue();

                int lastRowNum = sheetAt.getLastRowNum();

                // target 的表名
                String hiveTName = sheetAt.getRow(1).getCell(0).getStringCellValue();
                String hiveTDesc = sheetAt.getRow(2).getCell(0).getStringCellValue();
                XSSFRow row = null;
                Boolean primaryKey = false;
                ddl = new StringBuffer();
//                StringBuffer ddd = new StringBuffer();
                ddl.append("create table ").append(hiveTName).append(" (");
                for (int j = 4; j <= lastRowNum; j++) {
                    row = sheetAt.getRow(j);
                    XSSFCell cell = row.getCell(0);
                    if (cell == null || cell.getCellType() == CellType.BLANK) {
                        continue;
                    }
                    // 获取字段名
                    String fieldName = row.getCell(0).getStringCellValue();
                    // 获取字段类型
                    String fieldType = row.getCell(1).getStringCellValue();
                    // 字段描述
                    String fieldDesc = row.getCell(2).getStringCellValue();
                    ddl.append(fieldName).append(" ").append(fieldType).append(" comment ").append("'").append(fieldDesc).append("'");
                    if (j != lastRowNum) {
                        ddl.append(",");
                    }
                }
                ddl.append(")").append(" comment '").append(hiveTDesc).append("' row format delimited fields terminated by '\\t';");
                ddlsList.add(ddl.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dict;
    }

    public static void initDDL(Dict dict, String path) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet ddl = workbook.createSheet("ddl");
        List<String> ddls = (List<String>) dict.get("ddl");

        for (int i = 0; i < ddls.size(); i++) {
            XSSFRow ddlRow = ddl.createRow(i);
            XSSFCell ddlCell = ddlRow.createCell(0);
            ddlCell.setCellValue(JSONUtil.toJsonStr(ddls.get(i)));
        }
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        System.out.println("excel导出成功");
    }

    public static Dict readFieldFromExcelV2(FileInputStream inputStream) {
        Dict dict = Dict.of();
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(inputStream);
            int numberOfSheets = workbook.getNumberOfSheets();
            if (numberOfSheets < 1) {
                System.out.println("SHEET NUMBER IS ERROR");
                return null;
            }
            StringBuffer ddl = null;
            for (int i = 1; i < numberOfSheets; i++) {
                XSSFSheet sheetAt = workbook.getSheetAt(i);
                // source 的表名
                String hiveSName = sheetAt.getRow(0).getCell(0).getStringCellValue();

                int lastRowNum = sheetAt.getLastRowNum();

                // target 的表名
                String hiveTName = sheetAt.getRow(1).getCell(0).getStringCellValue();
                String hiveTDesc = sheetAt.getRow(2).getCell(0).getStringCellValue();
                XSSFRow row = null;
                Boolean primaryKey = false;
                ddl = new StringBuffer();
//                StringBuffer ddd = new StringBuffer();
                ddl.append("create table ").append(hiveTName).append(" (");
                for (int j = 4; j <= lastRowNum; j++) {
                    row = sheetAt.getRow(j);
                    XSSFCell cell = row.getCell(0);
                    if (cell == null || cell.getCellType() == CellType.BLANK) {
                        continue;
                    }
                    // 获取字段名
                    String fieldName = row.getCell(0).getStringCellValue();
                    // 获取字段类型
                    String fieldType = row.getCell(1).getStringCellValue();
                    // 字段描述
                    String fieldDesc = row.getCell(2).getStringCellValue();
                    ddl.append(fieldName).append(" ").append(fieldType).append(" comment ").append("'").append(fieldDesc).append("'");
                    if (j != lastRowNum) {
                        ddl.append(",");
                    }
                }

                ddl.append(")").append(" comment '").append(hiveTDesc).append("' row format delimited fields terminated by '\\t';");

            }
            if (ObjectUtil.equals(ddl.lastIndexOf(","), ddl.length()-1)) {
                ddl.delete(ddl.length()-1, ddl.length());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dict;
    }
}
