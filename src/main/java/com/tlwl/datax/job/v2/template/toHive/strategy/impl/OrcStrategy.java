package com.tlwl.datax.job.v2.template.toHive.strategy.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.tlwl.datax.job.init.model.m2h.Column;
import com.tlwl.datax.job.v2.template.toHive.enums.TemplateEnum;
import com.tlwl.datax.job.v2.template.toHive.strategy.M2HStrategy;
import com.tlwl.datax.job.v2.template.toHive.temp.OrcTemp;

import com.tlwl.datax.job.v2.template.toHive.temp.TextTempV2;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author houjiawei
 * @Description 简单模式 - 生成最简单的job和ddl语句
 * @date 2024/8/1 9:24
 */

public class OrcStrategy implements M2HStrategy {

    @Override
    public Dict initJobsAndDDL(XSSFSheet sheetAt, List<String> fields, String extraSql, TemplateEnum templateEnum) {
        String job = null;
        Map<String, String> commonParams = null;
        if (ObjectUtil.isNull(templateEnum)) {
            job = OrcTemp.ORC_TEMP;
            commonParams = OrcTemp.initCommonParams();
        } else {
            switch (templateEnum) {
                case ORC:
                    job = OrcTemp.ORC_TEMP;
                    commonParams = OrcTemp.initCommonParams();
                    break;
                case SIMPLE:
                    job = TextTempV2.TEXT_TEMP;
                    commonParams = TextTempV2.initCommonParams();
                    break;
                default:
            }
        }
        StringBuffer HTSql = new StringBuffer();
        Dict dict = new Dict();
        try {
            StringBuffer sb = new StringBuffer();
            StringBuffer partitionAppand = new StringBuffer();
            partitionAppand.append(" partitioned by (");
            sb.append("select").append(" ");
            // mysql的表名
            String mysqlTName = sheetAt.getRow(0).getCell(0).getStringCellValue();
            List<Column> columns = new ArrayList<>();
            int lastRowNum = sheetAt.getLastRowNum();
            // hive的业务表名
            String hiveTName = sheetAt.getRow(1).getCell(0).getStringCellValue();
            // HIVE 建表语句
            HTSql.append("create table ").append(hiveTName).append("(");
            String tableDesc = sheetAt.getRow(2).getCell(0).getStringCellValue();
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
                if (StrUtil.contains(fieldType, "decimal")) {
                    column.setType("double");
                    // 设置精度
                    fieldType = "decimal(10,2)";
                }
                else {
                    column.setType(fieldType);
                }
                columns.add(column);
                if (j == lastRowNum) {
                    sb.append(fieldName);
                    // 组装HIVE建表语句
                } else {
                    sb.append(fieldName).append(",");
                }
                if (fields.contains(fieldName)) {
                    partitionAppand.append(fieldName).append(" ").append(fieldType).append(" comment'").append(fieldDesc).append("',");
                    continue;
                }
                if (j == lastRowNum) {
                    // 组装HIVE建表语句
                    HTSql.append(fieldName).append(" ").append(fieldType).append(" comment ").append("'").append(fieldDesc).append("'");
                } else {
                    HTSql.append(fieldName).append(" ").append(fieldType).append(" comment ").append("'").append(fieldDesc).append("'").append(",");
                }
            }
            if (ObjectUtil.equals(HTSql.lastIndexOf(","), HTSql.length() - 1)) {
                HTSql.delete(HTSql.length() - 1, HTSql.length());
                sb.delete(sb.length() - 1, sb.length());
            }
            if (ObjectUtil.equals(partitionAppand.lastIndexOf(","), partitionAppand.length() - 1)) {
                partitionAppand.delete(partitionAppand.length() - 1, partitionAppand.length());
            }
            partitionAppand.append(") ");
            HTSql.append(")").append(" comment '").append(tableDesc).append("'");
            HTSql.append(" ").append(partitionAppand)
                    .append(" row format delimited fields terminated by '\\t'")
                    .append(" stored as orc");
            sb.append(" from ").append(mysqlTName);
            if (StrUtil.isNotBlank(extraSql)) {
                sb.append(" ").append(extraSql);
            }
            List<String> querySqls = new ArrayList<>();
            querySqls.add(sb.toString());
            commonParams.put("querySql", JSONUtil.formatJsonStr(JSONUtil.toJsonStr(querySqls)));
            commonParams.put("column",JSONUtil.formatJsonStr(JSONUtil.toJsonStr(columns)));
            job = StrUtil.format(job, commonParams);
//            job = String.format(job,
//                    READ_USER_NAME,
//                    READ_PASSWORD,
//                    JSONUtil.formatJsonStr(JSONUtil.toJsonStr(querySqls)),
//                    READ_JDBC_URL,
//                    JSONUtil.formatJsonStr(JSONUtil.toJsonStr(columns)),
//                    WRITER_HDFS_URL,
//                    WRITER_FIELD_DELIMITER,
//                    hiveTName,
//                    WRITER_FIELD_TYPE,
//                    WRITER_HDFS_DBNAME,
//                    hiveTName,
//                    WRITER_WRITE_MODE,
//                    SETTING_SPEED_CHANNEL,
//                    SETTING_SPEED_TPS,
//                    SETTING_ERROR_LIMIT_RECORD,
//                    SETTING_ERROR_LIMIT_PERCENTAGE
//            );
            dict.set("job", job).set("ddl", HTSql.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dict;
    }
}
