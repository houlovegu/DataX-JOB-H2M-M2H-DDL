package com.tlwl.datax.job.init.model.h2m;

import lombok.Data;

/**
 * @Description
 * @author houjiawei
 * @date 2024/7/30 20:57
 */
@Data
public class OutputWriter {

    private String name="mysqlwriter";

    private OutputWParameter parameter;
}
