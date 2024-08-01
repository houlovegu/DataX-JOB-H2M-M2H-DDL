package com.tlwl.datax.job.init.model.m2h;

import lombok.Data;

import java.util.List;

/**
 * @Description
 * @author houjiawei
 * @date 2024/7/30 16:15
 */
@Data
public class Connection {

    // TODO: 需要动态生成
    private List<String> querySql;

    // TODO: MessageFormat.format(String msg, format...)
    // jdbc:mysql://192.168.60.160:3306/lwx-bu-test?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowLoadLocalInfile=false&autoDeserialize=false&allowLocalInfile=false&allowUrlInLocalInfile=false
    private List<String> jdbcUrl= List.of("jdbc:mysql://{0}/{1}?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowLoadLocalInfile=false&autoDeserialize=false&allowLocalInfile=false&allowUrlInLocalInfile=false");
}
