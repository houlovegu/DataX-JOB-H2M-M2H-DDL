package com.tlwl.datax.job.init;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import com.tlwl.datax.job.init.utils.ExcelUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @Description 自动生成建表语句
 * @author houjiawei
 * @date 2024/7/31 11:41
 */
public class AutoCreateTableDDL {

    public static void main(String[] args) {
        try (FileInputStream file = new FileInputStream(new File("D:\\workspace-local\\Dxjconfinit\\src\\main\\resources\\dim_transform.xlsx"))) {
            Dict dict = ExcelUtil.readFieldFromExcel(file);
            if (ObjectUtil.isNotNull(dict)) {
                ExcelUtil.initDDL(dict, "D:\\workspace-local\\Dxjconfinit\\src\\main\\resources\\dim_transform_ddl.xlsx");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
