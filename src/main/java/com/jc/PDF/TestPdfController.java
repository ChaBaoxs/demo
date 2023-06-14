package com.jc.PDF;

import com.jc.PDF.PdfUtil;
import com.jc.common.Result;
import com.jc.entity.Plan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/example")
public class TestPdfController {

    @PostMapping("/createPdfLocal")
    public Result create(@RequestBody Plan plan) {
        PdfUtil.createPdfPage(plan,"D:\\Desktop\\"+plan.getName()+"计划报告.pdf");
        return Result.success();
    }

//    @GetMapping("/mergePdf")
//    public Boolean mergePdf() {
//        String[] files = {"D:\\test\\1.pdf", "D:\\test\\2.pdf"};
//        String newFile = "D:\\test\\xxf-merge.pdf";
//        return PdfUtil.mergePdfFiles(files, newFile);
//    }

//    @GetMapping("/exportMergePdf")
//    public void createPdf(HttpServletResponse response) {
//        // 设置response参数，可以打开下载页面
//        response.reset();
//        response.setCharacterEncoding("UTF-8");
//        // 定义输出类型
//        response.setContentType("application/PDF;charset=utf-8");
//        // 设置名称
//        response.setHeader("Content-Disposition", "attachment; filename=" + "xiaoxiaofeng.pdf");
//        try (ServletOutputStream out = response.getOutputStream()) {
//            String[] files = {"D:\\test\\1.pdf", "D:\\test\\2.pdf"};
//            // 生成为临时文件，转换为流后，再删除该文件
//            String newFile = "src\\main\\resources\\templates\\" + UUID.randomUUID() + ".pdf";
//            boolean isOk = PdfUtil.mergePdfFiles(files, newFile);
//            if (isOk) {
//                PdfUtil.readDeletePdf(newFile, out);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}