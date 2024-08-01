package com.tlwl.datax.job.v2.template.toHive.enums;

import lombok.Getter;

/**
 * @Description job模板对应枚举
 *              使用类的全限定名，用于反射找到此类
 * @author houjiawei
 * @date 2024/8/1 16:42
 */
@Getter
public enum TemplateEnum {
    
    SIMPLE,

    ORC,
    ;


    TemplateEnum() {
    }
}
