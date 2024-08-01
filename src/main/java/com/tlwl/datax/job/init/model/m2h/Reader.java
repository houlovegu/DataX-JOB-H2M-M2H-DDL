package com.tlwl.datax.job.init.model.m2h;

import lombok.Data;

/**
 * @Description
 * @author houjiawei
 * @date 2024/7/30 16:11
 */
@Data
public class Reader {

    private String name = "mysqlreader";

    private ReaderParameter parameter;

}
