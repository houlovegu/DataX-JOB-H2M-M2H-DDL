package com.tlwl.datax.job.v2.template.toHive.temp;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @author houjiawei
 * @date 2024/8/1 15:05
 */

public class OrcTemp {

    public static String ORC_TEMP = "{\n" +
            "        \"job\": {\n" +
            "                \"content\": [{\n" +
            "                        \"reader\": {\n" +
            "                                \"name\": \"mysqlreader\",\n" +
            "                                \"parameter\": {\n" +
            "                                        \"username\": \"{username}\",\n" +
            "                                        \"password\": \"{password}\",\n" +
            "                                        \"connection\": [{\n" +
            "                                                \"querySql\": {querySql},\n" +
            "                                                \"jdbcUrl\": [\n" +
            "                                                        \"jdbc:mysql://{jdbcUrl}?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowLoadLocalInfile=false&autoDeserialize=false&allowLocalInfile=false&allowUrlInLocalInfile=false\"\n" +
            "                                                ]\n" +
            "                                        }]\n" +
            "                                }\n" +
            "                        },\n" +
            "\n" +
            "                        \"writer\": {\n" +
            "                                \"name\": \"hdfswriter\",\n" +
            "                                \"parameter\": {\n" +
            "                                        \"column\": {column},\n" +
            "                                        \"defaultFS\": \"hdfs://{defaultFS}\",\n" +
            "                                        \"fieldDelimiter\": \"{fieldDelimiter}\",\n" +
            "                                        \"fileName\": \"{fileName}\",\n" +
            "                                        \"fileType\": \"{fileType}\",\n" +
            "                                        \"path\": \"/user/hive/warehouse/{dbName}/{fileName}\",\n" +
            "                                        \"writeMode\": \"{writeMode}\"\n" +
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

    public static final String READ_USER_NAME = "root";

    public static final String READ_PASSWORD = "Ssjy@2019";

    public static final String READ_JDBC_URL = "192.168.60.160:3306/tlwl_bu";

    public static final String WRITER_HDFS_URL = "hadoop1:9000";

    public static final String WRITER_FIELD_DELIMITER = "\\t";

    public static final String WRITER_FIELD_TYPE = "orc";

    public static final String WRITER_HDFS_DBNAME = "dw_tlwl_bu.db";

    public static final String WRITER_WRITE_MODE = "truncate";

    public static final String SETTING_SPEED_CHANNEL = "1";

    public static final String SETTING_SPEED_TPS = "1000";

    public static final String SETTING_ERROR_LIMIT_RECORD = "10";

    public static final String SETTING_ERROR_LIMIT_PERCENTAGE = "0.1";

    public static Map<String,String> initCommonParams() {
        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("username", READ_USER_NAME);
        objectObjectHashMap.put("password", READ_PASSWORD);
        objectObjectHashMap.put("jdbcUrl", READ_JDBC_URL);
        objectObjectHashMap.put("defaultFS", WRITER_HDFS_URL);
        objectObjectHashMap.put("fieldDelimiter", WRITER_FIELD_DELIMITER);
        objectObjectHashMap.put("fileType", WRITER_FIELD_TYPE);
        objectObjectHashMap.put("dbName", WRITER_HDFS_DBNAME);
        objectObjectHashMap.put("writeMode", WRITER_WRITE_MODE);
        objectObjectHashMap.put("channel", SETTING_SPEED_CHANNEL);
        objectObjectHashMap.put("tps", SETTING_SPEED_TPS);
        objectObjectHashMap.put("record", SETTING_ERROR_LIMIT_RECORD);
        objectObjectHashMap.put("percentage", SETTING_ERROR_LIMIT_PERCENTAGE);
        return objectObjectHashMap;
    }
}
