package com.tlwl.datax.job.v2.template.toMysql.factory;

import cn.hutool.core.lang.Dict;
import com.tlwl.datax.job.v2.template.toMysql.enums.TMTempEnum;
import org.apache.poi.xssf.usermodel.XSSFSheet;


/**
 * @Description
 * @author houjiawei
 * @date 2024/8/1 9:38
 */

public interface ProcessFactory {

    Dict readExcel(XSSFSheet sheetAt, String extraSql, TMTempEnum tempEnum);
}
