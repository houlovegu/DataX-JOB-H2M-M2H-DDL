package com.tlwl.datax.job.init.model.m2h;

import lombok.Data;

import java.util.List;

/**
 * @Description
 * @author houjiawei
 * @date 2024/7/30 16:14
 */
@Data
public class ReaderParameter {

    private String username="root";

    private String password= "Ssjy@2019";

    private List<Connection> connection;
}
