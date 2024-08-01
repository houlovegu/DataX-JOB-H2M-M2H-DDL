package com.tlwl.datax.job.init.model.m2h;

import lombok.Builder;
import lombok.Data;

/**
 * @Description
 * @author houjiawei
 * @date 2024/7/30 16:05
 */
@Data
@Builder
public class Setting {

    private Speed speed;

    private ErrorLimit errorLimit;
}
