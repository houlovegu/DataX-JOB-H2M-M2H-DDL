package com.tlwl.datax.job.v2.template.toMysql.strategy;

import cn.hutool.core.lang.Dict;
import com.tlwl.datax.job.v2.template.toMysql.enums.TMTempEnum;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * @Description
 * @author houjiawei
 * @date 2024/8/1 15:56
 */

public interface H2MStrategy {

    Dict initJobAndDDL(XSSFSheet sheetAt, String extraSql, TMTempEnum tempEnum);
}
