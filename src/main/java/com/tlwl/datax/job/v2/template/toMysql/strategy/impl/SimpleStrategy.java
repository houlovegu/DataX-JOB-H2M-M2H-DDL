package com.tlwl.datax.job.v2.template.toMysql.strategy.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.tlwl.datax.job.v2.template.toHive.temp.OrcTemp;
import com.tlwl.datax.job.v2.template.toHive.temp.TextTempV2;
import com.tlwl.datax.job.v2.template.toMysql.enums.TMTempEnum;
import com.tlwl.datax.job.v2.template.toMysql.strategy.H2MStrategy;
import com.tlwl.datax.job.v2.template.toMysql.temp.DefaultTemp;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author houjiawei
 * @Description
 * @date 2024/8/1 15:57
 */

public class SimpleStrategy implements H2MStrategy {
    @Override
    public Dict initJobAndDDL(XSSFSheet sheetAt, String extraSql, TMTempEnum tempEnum) {
        String job = null;
        Map<String, String> commonParams = null;
        if (ObjectUtil.isNull(tempEnum)) {
            job = DefaultTemp.DEFAULT_TEMP;
            commonParams = DefaultTemp.initCommonParams();
        } else {
            switch (tempEnum) {
                case DEFAULT:
                    job = DefaultTemp.DEFAULT_TEMP;
                    commonParams = DefaultTemp.initCommonParams();
                    break;
                default:
            }
        }
        Dict dict = new Dict();
        try {
            // querySqls封装sql
            List<String> querySqls = new ArrayList<>();
            // reader querysql
            StringBuffer querySql = new StringBuffer();
            // wColumns添加字段名
            List<String> wColumns = new ArrayList<>();
            // 添加表名
            List<String> wTables = new ArrayList<>();
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
                }
            }
            if (ObjectUtil.equals(MySql.lastIndexOf(","), MySql.length() - 1)) {
                MySql.delete(MySql.length() - 1, MySql.length());
                querySql.delete(querySql.length() - 1, querySql.length());
            }
            if (primaryKey) {
                MySql.append(", PRIMARY KEY (`id`)");
                MySql.append(") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='");
            } else {
                MySql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='");
            }
            MySql.append(mysqlTDesc).append("';");
            querySql.append(" ").append(hiveTName);
            if (StrUtil.isNotBlank(extraSql)) {
                querySql.append(" ").append(extraSql);
            }
            querySqls.add(querySql.toString());
            commonParams.put("querySql",JSONUtil.formatJsonStr(JSONUtil.toJsonStr(querySqls)));
            commonParams.put("column",JSONUtil.formatJsonStr(JSONUtil.toJsonStr(wColumns)));
            commonParams.put("table",JSONUtil.formatJsonStr(JSONUtil.toJsonStr(wTables)));
            job = StrUtil.format(job, commonParams);
//            job = String.format(job,
//                    READ_USER_NAME,
//                    READ_PASSWORD,
//                    JSONUtil.formatJsonStr(JSONUtil.toJsonStr(querySqls)),
//                    READ_JDBC_URL,
//                    WRITER_USER_NAME,
//                    WRITER_PASSWORD,
//                    JSONUtil.formatJsonStr(JSONUtil.toJsonStr(wColumns)),
//                    JSONUtil.formatJsonStr(JSONUtil.toJsonStr(wTables)),
//                    WRITER_JDBC_URL,
//                    SETTING_SPEED_CHANNEL,
//                    SETTING_SPEED_TPS,
//                    SETTING_ERROR_LIMIT_RECORD,
//                    SETTING_ERROR_LIMIT_PERCENTAGE
//            );
            dict.set("job", job).set("ddl", querySql.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dict;
    }
}
