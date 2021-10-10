/*
package com.beatshadow.mall.thirdparty.pdf;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

*/
/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/4/30 00:49
 *//*

public class PdfDemo {
    public static void main(String[] args) throws FileNotFoundException, JRException {
        //模版文件
        String filePath = "/Users/gnehcgnaw/Desktop/first_inspection_items_table.jasper" ;
        //转换为输入流
        FileInputStream fileInputStream = new FileInputStream(filePath);
        Map<String, Object> paramters = new HashMap<>();
        paramters.put("deviceNameAndCode","张三");
        paramters.put("productNameAndCode","测试测测(030912121)");
        paramters.put("processName","一条工序一条产线");
        paramters.put("batchNumber","批次号188888——1");
        paramters.put("shiftNameAndTime","张三");
        paramters.put("formNumber","15104455502");
        */
/**
         * <parameter name="deviceNameAndCode" class="java.lang.String"/>
         * 	<parameter name="productNameAndCode" class="java.lang.String"/>
         * 	<parameter name="processName" class="java.lang.String"/>
         * 	<parameter name="batchNumber" class="java.lang.String"/>
         * 	<parameter name="shiftNameAndTime" class="java.lang.String"/>
         * 	<parameter name="formNumber" class="java.lang.String"/>
         *//*

        List<Fields> list = new ArrayList<Fields>();
        for(int i = 1 ; i <= 20; i++) {
            Fields fields = new Fields("field"+i,"field"+(i+1),"field"+(i+2),"field"+(i+3),"field"+(i+4));
            list.add(fields);
        }
        ModelTableSource mts = new ModelTableSource();
        mts.setTableData(new JRBeanCollectionDataSource(list));
        List<ModelTableSource> mlist = new ArrayList<ModelTableSource>();
        mlist.add(mts);
        JasperPrint jasperPrint = JasperFillManager.fillReport(fileInputStream, paramters, new JRBeanCollectionDataSource(mlist));
        JasperExportManager.exportReportToPdfStream(jasperPrint,new FileOutputStream("/Users/gnehcgnaw/Desktop/first_inspection_items_table.pdf"));
    }
}
*/
