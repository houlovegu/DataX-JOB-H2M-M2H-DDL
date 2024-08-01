package com.tlwl.datax.job.v2.template.toMysql.temp;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description HIVE TO MYSQL 默认模板
 * @author houjiawei
 * @date 2024/8/1 15:45
 */

public class DefaultTemp {

    public static String DEFAULT_TEMP = "{\n" +
            "        \"job\": {\n" +
            "                \"content\": [{\n" +
            "                        \"reader\": {\n" +
            "                                \"name\": \"rdbmsreader\",\n" +
            "                                \"parameter\": {\n" +
            "                                        \"username\": \"{rUsername}\",\n" +
            "                                        \"password\": \"{rPassword}\",\n" +
            "                                        \"connection\": [{\n" +
            "                                                \"querySql\": {querySql},\n" +
            "                                                \"jdbcUrl\": [\n" +
            "                                                        \"jdbc:hive2://{rJdbcUrl}\"\n" +
            "                                                ]\n" +
            "                                        }]\n" +
            "                                }\n" +
            "                        },\n" +
            "                        \"writer\": {\n" +
            "                                \"name\": \"mysqlwriter\",\n" +
            "                                \"parameter\": {\n" +
            "                                        \"username\": \"{wUsername}\",\n" +
            "                                        \"password\": \"{wPassword}\",\n" +
            "                                        \"column\": {column},\n" +
            "                                        \"connection\": [{\n" +
            "                                                \"table\": {table}\n" +
            "                                                \"jdbcUrl\": \"jdbc:mysql://{wJdbcUrl}?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowLoadLocalInfile=false&autoDeserialize=false&allowLocalInfile=false&allowUrlInLocalInfile=false\"\n" +
            "                                        }]\n" +
            "                                }\n" +
            "                        }\n" +
            "                }],\n" +
            "                \"setting\": {\n" +
            "                        \"speed\": {\n" +
            "                                \"channel\": {channel},\n" +
            "                                \"tps\": {tps}\n" +
            "                        },\n" +
            "                        \"errorLimit\": {\n" +
            "                                \"record\": {record},\n" +
            "                                \"percentage\": {percentage}\n" +
            "                        }\n" +
            "                }\n" +
            "        }\n" +
            "}";

    private static String READ_USER_NAME = "hive";

    private static String READ_PASSWORD = "hive";

    private static String READ_JDBC_URL = "10.10.9.119:10000/dw_tlwl_bu";

    private static String WRITER_USER_NAME = "root";

    private static String WRITER_PASSWORD = "Ssjy@2019";

    private static String WRITER_JDBC_URL = "192.168.60.160:3306/tlwl_bu";

    private static final String SETTING_SPEED_CHANNEL = "1";

    private static final String SETTING_SPEED_TPS = "1000";

    private static final String SETTING_ERROR_LIMIT_RECORD = "10";

    private static final String SETTING_ERROR_LIMIT_PERCENTAGE = "0.1";

    // TODO: 暂时没有用此字段
    private static String WRITE_MODE = "replace";

    public static Map<String,String> initCommonParams() {
        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("rUsername", READ_USER_NAME);
        objectObjectHashMap.put("rPassword", READ_PASSWORD);
        objectObjectHashMap.put("rJdbcUrl", READ_JDBC_URL);
        objectObjectHashMap.put("wUsername", WRITER_USER_NAME);
        objectObjectHashMap.put("wPassword", WRITER_PASSWORD);
        objectObjectHashMap.put("wJdbcUrl", WRITER_JDBC_URL);
        objectObjectHashMap.put("channel", SETTING_SPEED_CHANNEL);
        objectObjectHashMap.put("tps", SETTING_SPEED_TPS);
        objectObjectHashMap.put("record", SETTING_ERROR_LIMIT_RECORD);
        objectObjectHashMap.put("percentage", SETTING_ERROR_LIMIT_PERCENTAGE);
        return objectObjectHashMap;
    }
}
