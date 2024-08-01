package com.tlwl.datax.job.init.model.h2m;

import lombok.Builder;
import lombok.Data;

/**
 * @Description
 * @author houjiawei
 * @date 2024/7/30 16:05
 */
@Data
@Builder
public class OutputSetting {

    private OutputSpeed speed;

    private OutputErrorLimit errorLimit;
}
