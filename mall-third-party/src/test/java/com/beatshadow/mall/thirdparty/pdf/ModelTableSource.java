package com.beatshadow.mall.thirdparty.pdf;

import lombok.Data;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/4/30 17:16
 */
@Data
public class ModelTableSource {
    /**
     * 注入table组件的数据源
     */
    private JRBeanCollectionDataSource tableData;

    private String deviceNameAndCode  = "111";

    private String productNameAndCode= "111" ;

    private String processName = "111";

    private String batchNumber = "111";

    private String shiftNameAndTime = "111";

    private String formNumber = "111";
}
