package com.tlwl.datax.job.init;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import com.tlwl.datax.job.init.model.ExtraEntity;
import com.tlwl.datax.job.init.model.PartitionEntity;
import com.tlwl.datax.job.init.utils.ExcelUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Description 自动生成 datax 转换的job配置
 * @author houjiawei
 * @date 2024/7/30 15:55
 */
public class ToHiveConfInit {

    /**
     * 读取excel生成job配置
     * @param args
     */
    public static void main(String[] args) {
        try (FileInputStream file = new FileInputStream(new File("D:\\workspace-local\\Dxjconfinit\\src\\main\\resources\\ods_input.xlsx"))) {
            // TODO: 添加额外配置项
            ExtraEntity extra = ExtraEntity.builder()
                    .dbUsername("root")
                    .dbPassword("Ssjy@2019")
                    .dbUrl("192.168.60.160:3306")
                    .dbname("tlwl_bu")
                    .hiveDbName("dw_tlwl_bu.db")
//                    .orcTables(List.of("ods_bd_class_semester", "ods_trade_refund", "ods_trade_order_course_item"))
//                    .partitionedTables(Map.of("ods_bd_class_semester", List.of(PartitionEntity.builder().columnName("code").columnType("string").columnComment("业务编码").build()),
//                            "ods_trade_refund", List.of(PartitionEntity.builder().columnName("real_refund_time").columnType("timestamp").columnComment("出账时间").build()),
//                            "ods_trade_order_course_item",List.of(PartitionEntity.builder().columnName("grade_id").columnType("bigint").build(),
//                                    PartitionEntity.builder().columnName("subject_id").columnType("bigint").build(),
//                                    PartitionEntity.builder().columnName("class_code").columnType("string").build())
//                            )
//                    )
                    .build();
            Dict dataFromExcel = ExcelUtil.getDataFromExcel(file, extra);
            if (ObjectUtil.isNotNull(dataFromExcel)) {
                ExcelUtil.importExcel(dataFromExcel, "D:\\workspace-local\\Dxjconfinit\\src\\main\\resources\\ods_input_detail.xlsx");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
