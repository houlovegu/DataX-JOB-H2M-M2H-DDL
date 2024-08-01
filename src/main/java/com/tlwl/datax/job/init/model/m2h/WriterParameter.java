package com.tlwl.datax.job.init.model.m2h;

import lombok.Data;

import java.util.List;

/**
 * @Description
 * @author houjiawei
 * @date 2024/7/30 16:21
 */
@Data
public class WriterParameter {

    private List<Column> column;

    private String defaultFS="hdfs://hadoop1:9000";

    private String fieldDelimiter="\t";

    private String fileName;

    private String fileType="text";

    private String path = "/user/hive/warehouse/";

    private String writeMode = "truncate";
}
