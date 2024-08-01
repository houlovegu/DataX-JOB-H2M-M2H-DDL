package com.tlwl.datax.job.v2.template.toHive.temp;

/**
 * @Description
 * @author houjiawei
 * @date 2024/8/1 9:20
 */

public class TextTemp {

    public static String TEXT_TEMP = "{\n" +
            "        \"job\": {\n" +
            "                \"content\": [{\n" +
            "                        \"reader\": {\n" +
            "                                \"name\": \"mysqlreader\",\n" +
            "                                \"parameter\": {\n" +
            "                                        \"username\": \"%s\",\n" +
            "                                        \"password\": \"%s\",\n" +
            "                                        \"connection\": [{\n" +
            "                                                \"querySql\": %s,\n" +
            "                                                \"jdbcUrl\": [\n" +
            "                                                        \"jdbc:mysql://%s?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowLoadLocalInfile=false&autoDeserialize=false&allowLocalInfile=false&allowUrlInLocalInfile=false\"\n" +
            "                                                ]\n" +
            "                                        }]\n" +
            "                                }\n" +
            "                        },\n" +
            "\n" +
            "                        \"writer\": {\n" +
            "                                \"name\": \"hdfswriter\",\n" +
            "                                \"parameter\": {\n" +
            "                                        \"column\": %s,\n" +
            "                                        \"defaultFS\": \"hdfs://%s\",\n" +
            "                                        \"fieldDelimiter\": \"%s\",\n" +
            "                                        \"fileName\": \"%s\",\n" +
            "                                        \"fileType\": \"%s\",\n" +
            "                                        \"path\": \"/user/hive/warehouse/%s/%s\",\n" +
            "                                        \"writeMode\": \"%s\"\n" +
            "                                }\n" +
            "                        }\n" +
            "                }],\n" +
            "                \"setting\": {\n" +
            "                        \"speed\": {\n" +
            "                                \"channel\": %s,\n" +
            "                                \"tps\": %s\n" +
            "                        },\n" +
            "                        \"errorLimit\": {\n" +
            "                                \"record\": %s,\n" +
            "                                \"percentage\": %s\n" +
            "                        }\n" +
            "                }\n" +
            "        }\n" +
            "}";

    public static final String READ_USER_NAME = "root";

    public static final String READ_PASSWORD = "Ssjy@2019";

    public static final String READ_JDBC_URL = "192.168.60.160:3306/tlwl_bu";

    public static final String WRITER_HDFS_URL = "hadoop1:9000";

    public static final String WRITER_FIELD_DELIMITER = "\\t";

    public static final String WRITER_FIELD_TYPE = "text";

    public static final String WRITER_HDFS_DBNAME = "dw_tlwl_bu.db";

    public static final String WRITER_WRITE_MODE = "truncate";

    public static final String SETTING_SPEED_CHANNEL = "1";

    public static final String SETTING_SPEED_TPS = "1000";

    public static final String SETTING_ERROR_LIMIT_RECORD = "10";

    public static final String SETTING_ERROR_LIMIT_PERCENTAGE = "0.1";
}
