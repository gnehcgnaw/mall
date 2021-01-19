package com.beatshadow.mall.thirdparty.controller;

import com.alibaba.excel.EasyExcel;
import com.beatshadow.common.utils.R;
import com.beatshadow.mall.thirdparty.entity.easyexcel.DownloadData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/1/13 13:59
 */
@RestController
@RequestMapping("/easy-excel")
public class EasyExcelController {
    @GetMapping("/download")
    public R download(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), DownloadData.class).sheet("模板").doWrite(data());
        return R.ok() ;
    }


    private List<DownloadData> data() {
        List<DownloadData> list = new ArrayList<DownloadData>();
        for (int i = 0; i < 10; i++) {
            DownloadData data = new DownloadData();
            data.setString("字符串" + 0);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            ArrayList<DownloadData> arrayList = new ArrayList<>();
            arrayList.add(data);
            arrayList.add(data);
            data.setList(arrayList);
            list.add(data);
        }
        return list;
    }

}
