package com.tlwl.datax.job.init.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Description 额外配置项
 * @author houjiawei
 * @date 2024/7/31 14:03
 */
@Data
@Builder
public class ExtraEntity {

    /**
     * 数据库用户名
     */
    private String dbUsername;

    /**
     * 数据库用户密码
     */
    private String dbPassword;

    /**
     * 数据库url
     */
    private String dbUrl;

    /**
     * 数据库
     */
    private String dbname;

    /**
     * hive数据库名字
     */
    private String hiveDbName;

    /**
     * 要使用Orc的表名
     */
    private List<String> orcTables;

    /**
     * 要使用分区的表名，以及表字段,类型,描述
     *  Map key 表名, value 根据哪些字段分区
     */
    private Map<String,List<PartitionEntity>> partitionedTables;

}
