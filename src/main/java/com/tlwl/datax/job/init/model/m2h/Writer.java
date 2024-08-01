package com.tlwl.datax.job.init.model.m2h;

import lombok.Data;

/**
 * @Description
 * @author houjiawei
 * @date 2024/7/30 16:12
 */
@Data
public class Writer {

    private String name="hdfswriter";

    private WriterParameter parameter;
}
