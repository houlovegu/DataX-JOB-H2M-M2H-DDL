package com.tlwl.datax.job.init.model.h2m;

import lombok.Data;

/**
 * @Description
 * @author houjiawei
 * @date 2024/7/30 20:56
 */
@Data
public class OutputReader {

    private String name = "rdbmsreader";

    private OutputRParameter parameter;
}
