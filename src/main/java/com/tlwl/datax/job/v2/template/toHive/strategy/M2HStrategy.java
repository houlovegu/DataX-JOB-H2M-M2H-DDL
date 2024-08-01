package com.tlwl.datax.job.v2.template.toHive.strategy;

import cn.hutool.core.lang.Dict;
import com.tlwl.datax.job.v2.template.toHive.enums.TemplateEnum;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @author houjiawei
 * @date 2024/8/1 9:22
 */

public interface M2HStrategy {

    Dict initJobsAndDDL(XSSFSheet sheetAt, List<String> fields, String extraSql, TemplateEnum templateEnum);
}
