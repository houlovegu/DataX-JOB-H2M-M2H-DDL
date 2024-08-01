package com.tlwl.datax.job.v2.template.toMysql.factory.impl;

import cn.hutool.core.lang.Dict;
import com.tlwl.datax.job.v2.template.toMysql.enums.TMTempEnum;
import com.tlwl.datax.job.v2.template.toMysql.factory.ProcessFactory;
import com.tlwl.datax.job.v2.template.toMysql.strategy.impl.SimpleStrategy;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * @Description
 * @author houjiawei
 * @date 2024/8/1 15:54
 */

public class SimpleFactory implements ProcessFactory {

    private SimpleStrategy strategy;

    public SimpleFactory(SimpleStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public Dict readExcel(XSSFSheet sheetAt, String extraSql, TMTempEnum tempEnum) {
        return strategy.initJobAndDDL(sheetAt, extraSql, tempEnum);
    }
}
