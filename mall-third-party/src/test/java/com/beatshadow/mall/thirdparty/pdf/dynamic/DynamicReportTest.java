/*
package com.beatshadow.mall.thirdparty.pdf.dynamic;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

*/
/**
 * @author lay
 * @date 2018/10/26 18:00
 *//*

public class DynamicReportTest {

    private DynamicReport dynamicReport;

    private JasperReport jasperReport;

    private JRDataSource jrDataSource;

    private JasperPrint jasperPrint;

    public static void main(String[] args) throws Exception {
        new DynamicReportTest().buildDynamicReport()
                .generateJasperReport()
                .fillDataSource()
                .export();
    }

    */
/**
     * 构建DynamicJasper
     * @return
     * @throws ClassNotFoundException
     *//*

    public DynamicReportTest buildDynamicReport() throws ClassNotFoundException {
        FastReportBuilder builder = new FastReportBuilder();
        Style style = new Style();
        style.setHorizontalAlign(HorizontalAlign.CENTER);
        builder.addColumn("中文", "id", Integer.class.getName(), 30, style)
                .addColumn("姓名", "name", String.class.getName(), 30, style)
                .addColumn("年龄", "age", Integer.class.getName(), 30, style)
                .setTitle("DynamicReport测试")
                .setUseFullPageWidth(true)
                .setPrintBackgroundOnOddRows(true);

        dynamicReport = builder.build();
        return this;
    }

    */
/**
     * 生成JasperReport
     * @return
     * @throws JRException
     *//*

    public DynamicReportTest generateJasperReport() throws JRException {
        jasperReport = DynamicJasperHelper.generateJasperReport(dynamicReport, new ClassicLayoutManager(), null);
        return this;
    }

    */
/**
     * 填充数据源
     * @return
     * @throws Exception
     *//*

    public DynamicReportTest fillDataSource() throws Exception {
        // 模拟数据
        List<Map<String, Object>> persons = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> person = new HashMap<>();
            person.put("id", 1);
            person.put("name", "张三1｜张三2");
          //  person.put("name", "张三2");
            person.put("age", 25);
            persons.add(person);
        }

        // 创建数据源
        jrDataSource = new JRBeanCollectionDataSource(persons);
        // 填充数据
        jasperPrint = JasperFillManager.fillReport(jasperReport, null, jrDataSource);
        return this;
    }

    */
/**
     * 导出为Html文件
     * @throws Exception
     *//*

    public void export() throws Exception {
        // html
        HtmlExporter htmlExporter = new HtmlExporter();
        htmlExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        htmlExporter.setExporterOutput(new SimpleHtmlExporterOutput(new FileOutputStream(new File("/Users/gnehcgnaw/Desktop/testHtml.html"))));
        htmlExporter.exportReport();
    }
}*/
