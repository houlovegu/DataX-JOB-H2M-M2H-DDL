package com.tlwl.datax.job.init.model.h2m;
/**
 * @Description
 * @author houjiawei
 * @date 2024/7/30 21:00
 */

import lombok.Data;

import java.util.List;

@Data
public class OutputWConnection {


    private List<String> table;

    // TODO: MessageFormat.format(String msg, format...)
    private String jdbcUrl="jdbc:mysql://{0}?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowLoadLocalInfile=false&autoDeserialize=false&allowLocalInfile=false&allowUrlInLocalInfile=false";
}
