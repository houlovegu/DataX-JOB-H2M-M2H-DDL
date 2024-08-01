package com.tlwl.datax.job.init.model.h2m;

import lombok.Data;

/**
 * @Description
 * @author houjiawei
 * @date 2024/7/30 16:07
 */
@Data
public class OutputErrorLimit {

    private int record=10;

    private double percentage=0.1;
}
