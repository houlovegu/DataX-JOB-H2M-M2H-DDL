package com.tlwl.datax.job.init.model.m2h;

import lombok.Data;

import java.util.List;

/**
 * @Description
 * @author houjiawei
 * @date 2024/7/30 15:57
 */
@Data
public class Job {

    private List<Content> content;

    private Setting setting;
}
