package com.tlwl.datax.job.init;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import com.tlwl.datax.job.init.model.DataExtraEntity;
import com.tlwl.datax.job.init.model.ExtraEntity;
import com.tlwl.datax.job.init.utils.ExcelUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @Description  ods_output
 * @author houjiawei
 * @date 2024/7/30 20:43
 */

public class ToMysqlConfInit {


    public static void main(String[] args) {
        try (FileInputStream file = new FileInputStream(new File("D:\\workspace-local\\Dxjconfinit\\src\\main\\resources\\ods_output.xlsx"))) {
            DataExtraEntity.builder()
                    .sourceDbUsername("hive")
                    .sourceDbPassword("hive")
                    .sourceDbUrl("10.10.9.119:10000/default")
                    .targetDbUsername("root")
                    .targetDbPassword("Ssjy@2019")
                    .targetDbUrl("192.168.60.160:3306/lwx-bu-test")
                    .build();
            Dict dataFromExcel = ExcelUtil.getOutFromExcel(file);
            if (ObjectUtil.isNotNull(dataFromExcel)) {
                ExcelUtil.importExcel(dataFromExcel, "D:\\workspace-local\\Dxjconfinit\\src\\main\\resources\\ods_output_detail.xlsx");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
