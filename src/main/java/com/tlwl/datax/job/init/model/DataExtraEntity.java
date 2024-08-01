package com.tlwl.datax.job.init.model;

import lombok.Builder;
import lombok.Data;

/**
 * @Description
 * @author houjiawei
 * @date 2024/7/31 16:40
 */
@Data
@Builder
public class DataExtraEntity {

    /**
     * 源库用户名
     */
    private String sourceDbUsername;

    /**
     * 源库用户密码
     */
    private String sourceDbPassword;

    /**
     * 源库url
     */
    private String sourceDbUrl;

    /**
     * 目标库用户名
     */
    private String targetDbUsername;

    /**
     * 目标用户密码
     */
    private String targetDbPassword;

    /**
     * 目标url
     */
    private String targetDbUrl;

    /**
     * 同步模式 insert/replace/update
     */
    private String writeMode;

}
