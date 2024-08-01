package com.tlwl.datax.job.init.model.h2m;
/**
 * @Description
 * @author houjiawei
 * @date 2024/7/30 21:00
 */

import lombok.Data;
import java.util.List;

@Data
public class OutputRConnection {

    // TODO: 需要动态生成
    private List<String> querySql;

    // TODO: MessageFormat.format(String msg, format...)
    private List<String> jdbcUrl= List.of("jdbc:hive2://{0}");
}
