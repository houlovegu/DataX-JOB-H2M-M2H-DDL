package com.tlwl.datax.job.init.model;

import lombok.Builder;
import lombok.Data;

/**
 * @Description  分区信息记录实体
 * @author houjiawei
 * @date 2024/7/31 14:17
 */
@Data
@Builder
public class PartitionEntity {

    /**
     * 字段名
     */
    private String columnName;

    /**
     * 字段类型
     */
    private String columnType;

    /**
     * 字段描述
     */
    private String columnComment;
}
