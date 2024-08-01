package com.tlwl.datax.job.v2.template.toHive.factory;

import cn.hutool.core.lang.Dict;
import com.tlwl.datax.job.v2.template.toHive.enums.TemplateEnum;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @author houjiawei
 * @date 2024/8/1 9:38
 */

public interface ProcessFactory {

    Dict readExcel(XSSFSheet sheetAt, List<String> fields, String extraSql, TemplateEnum templateEnum);
}
