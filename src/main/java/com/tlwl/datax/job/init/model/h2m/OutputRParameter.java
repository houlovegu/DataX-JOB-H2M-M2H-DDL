package com.tlwl.datax.job.init.model.h2m;

import lombok.Data;

import java.util.List;

/**
 * @Description
 * @author houjiawei
 * @date 2024/7/30 20:58
 */

@Data
public class OutputRParameter {

    private String username="hive";

    private String password="hive";

    private List<OutputRConnection> connection;

}
