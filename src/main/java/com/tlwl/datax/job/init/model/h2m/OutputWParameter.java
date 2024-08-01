package com.tlwl.datax.job.init.model.h2m;

import lombok.Data;

import java.util.List;

/**
 * @Description
 * @author houjiawei
 * @date 2024/7/30 20:58
 */

@Data
public class OutputWParameter {

    private String username="root";

    private String password="Ssjy@2019";

    // insert/replace/update
    private String writeMode="replace";

    private List<String> column;

    private List<OutputWConnection> connection;

}
