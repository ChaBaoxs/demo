package com.jc.PDF;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.jc.entity.Plan;
import com.jc.service.PlanService;
import org.apache.commons.compress.utils.IOUtils;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.jc.PDF.PdfFontUtil.*;

public class PdfUtil {

    private PdfUtil() {}

    @Resource
    private PlanService planService;

    /**
     * 创建PDF，并保存到指定位置
     *
     * @param filePath 保存路径
     */
    public static void createPdfPage(Plan plan, String filePath) {
        // FileOutputStream 需要关闭，释放资源
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            // 创建文档
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            // 报告标题
            document.add(PdfFontUtil.getParagraph("计划综合报告", TITLE_FONT, 1));

            // 报告内容
            // 段落标题 + 报表图
            document.add(PdfFontUtil.getParagraph("\n计划名称："+plan.getName(), NODE_FONT, -1));
            document.add(PdfFontUtil.getParagraph("\n开始时间："+plan.getBeginTime(), NODE_FONT, -1));
            document.add(PdfFontUtil.getParagraph("\n培训时长："+plan.getPeriod(), NODE_FONT, -1));
            document.add(PdfFontUtil.getParagraph("\n参与人数："+plan.getTrainNumber()+"人", NODE_FONT, -1));
            document.add(PdfFontUtil.getParagraph("\n此次收益："+plan.getTrainNumber()* plan.getPrice()+"元", NODE_FONT, -1));
            document.add(PdfFontUtil.getParagraph("\n学生评分："+plan.getScore()+"分", NODE_FONT, -1));
            document.add(PdfFontUtil.getParagraph("\n计划封面：", NODE_FONT, -1));
            // 设置图片宽高
            float documentWidth = 260;
            float documentHeight = 260;
            document.add(PdfFontUtil.getImage(plan.getCoverUrl()+"", documentWidth - 80, documentHeight - 80));
            //结尾
            document.add(PdfFontUtil.getParagraph("\n\n生成时间："+
                    (LocalDateTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), INFO_FONT, -1));
//            // 数据表格
//            document.add(PdfFontUtil.getParagraph("\n· 数据详情\n\n", BLOCK_FONT, -1));
//            // 生成6列的表格
//            PdfPTable dataTable = PdfFontUtil.getPdfTable(6, 500);
//            // 设置表格
//            List<String> tableHeadList = tableHead();
//            List<List<String>> tableDataList = getTableData();
//            PdfFontUtil.addTableCell(dataTable, CONTENT_FONT, tableHeadList);
//            for (List<String> tableData : tableDataList) {
//                PdfFontUtil.addTableCell(dataTable, CONTENT_FONT, tableData);
//            }
//            document.add(dataTable);
//            document.add(PdfFontUtil.getParagraph("\n· 报表描述\n\n", BLOCK_FONT, -1));
//            document.add(PdfFontUtil.getParagraph("数据报告可以监控每天的推广情况，" +
//                    "可以针对不同的数据表现进行分析，以提升推广效果。", CONTENT_FONT, -1));
            document.newPage();
            document.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 模拟数据
     */
    private static List<String> tableHead() {
        List<String> tableHeadList = new ArrayList<>();
        tableHeadList.add("省份");
        tableHeadList.add("城市");
        tableHeadList.add("数量");
        tableHeadList.add("百分比1");
        tableHeadList.add("百分比2");
        tableHeadList.add("百分比3");
        return tableHeadList;
    }

    /**
     * 模拟数据
     */
    private static List<List<String>> getTableData() {
        List<List<String>> tableDataList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<String> tableData = new ArrayList<>();
            tableData.add("浙江" + i);
            tableData.add("杭州" + i);
            tableData.add("276" + i);
            tableData.add("33.3%");
            tableData.add("34.3%");
            tableData.add("35.3%");
            tableDataList.add(tableData);
        }
        return tableDataList;
    }

//    /**
//     * 合并pdf文件
//     *
//     * @param files   要合并文件数组(绝对路径如{ "D:\\test\\1.pdf", "D:\\test\\2.pdf" , "D:\\test\\3.pdf"})
//     * @param newFile 合并后存放的目录D:\\test\\xxf-merge.pdf
//     * @return boolean 生成功返回true, 否則返回false
//     */
//    public static boolean mergePdfFiles(String[] files, String newFile) {
//        boolean retValue = false;
//        Document document;
//        try (FileOutputStream fileOutputStream = new FileOutputStream(newFile)) {
//            document = new Document(new PdfReader(files[0]).getPageSize(1));
//            PdfCopy copy = new PdfCopy(document, fileOutputStream);
//            document.open();
//            for (String file : files) {
//                PdfReader reader = new PdfReader(file);
//                int n = reader.getNumberOfPages();
//                for (int j = 1; j <= n; j++) {
//                    document.newPage();
//                    PdfImportedPage page = copy.getImportedPage(reader, j);
//                    copy.addPage(page);
//                }
//            }
//            retValue = true;
//            document.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return retValue;
//    }

    /**
     * 读取PDF，读取后删除PDF，适用于生成后需要导出PDF，创建临时文件
     */
    public static void readDeletePdf(String fileName, ServletOutputStream outputStream) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println(fileName + "文件不存在");
        }
        try (InputStream in = new FileInputStream(fileName)) {
            IOUtils.copy(in, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                Files.delete(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
