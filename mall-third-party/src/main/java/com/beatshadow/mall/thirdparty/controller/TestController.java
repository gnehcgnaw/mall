package com.beatshadow.mall.thirdparty.controller;

import cn.hutool.http.server.HttpServerResponse;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.beatshadow.common.utils.R;
import net.sf.jasperreports.engine.JasperFillManager;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/3/5 09:19
 */
@RestController
public class TestController {
    @GetMapping("/test")
    @SentinelResource("hello")
    public String test1(){
        return "test" ;
    }


    public R downLoadPDF(HttpServletResponse httpServletResponse) throws FileNotFoundException {
        //1，获取模版文件
        //2，准备数据
            //2，1,准备数据库连接
            //2、2、
        //3，
        File rootFile = new File(ResourceUtils.getURL("classpath:").getPath());
        File templateFile = new File(rootFile, "/pdf_template/");
       // JasperFillManager.fillReport(new FileInputStream(templateFile),new HashMap<>(),null);
        return null ;

    }
}
