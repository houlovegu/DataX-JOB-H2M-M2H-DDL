package com.tlwl.datax.job.init.model.h2m;

import lombok.Data;

import java.util.List;

/**
 * @Description
 * @author houjiawei
 * @date 2024/7/30 20:53
 */
@Data
public class OutPutJob {

    private List<OutputContent> content;

    private OutputSetting setting;
}
