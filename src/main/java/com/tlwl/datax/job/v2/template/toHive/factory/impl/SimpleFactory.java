package com.tlwl.datax.job.v2.template.toHive.factory.impl;

import cn.hutool.core.lang.Dict;
import com.tlwl.datax.job.v2.template.toHive.enums.TemplateEnum;
import com.tlwl.datax.job.v2.template.toHive.factory.ProcessFactory;
import com.tlwl.datax.job.v2.template.toHive.strategy.impl.SimpleStrategy;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.List;
import java.util.Map;


/**
 * @Description
 * @author houjiawei
 * @date 2024/8/1 9:40
 */

public class SimpleFactory implements ProcessFactory {

    private SimpleStrategy strategy;

    public SimpleFactory(SimpleStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public Dict readExcel(XSSFSheet sheetAt, List<String> fields, String extraSql, TemplateEnum templateEnum) {
        Dict dict = strategy.initJobsAndDDL(sheetAt, null, extraSql, templateEnum);
        return dict;
    }
}
