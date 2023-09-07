package com.example.test;

import org.apache.fontbox.ttf.OTFParser;
import org.apache.fontbox.ttf.OpenTypeFont;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Objects;

/**
 * Created  on 2022/5/23 16:16:21
 *
 * @author wmz
 */
public final class PDFUtil {

    /**
     * 添加水印
     * 暂时未对坐标，字体大小等参数扩展，后期有需要，再进行扩展
     * 需要自行捕获异常，处理异常
     */
    public static void addWaterMark(InputStream inputStream, OutputStream outputStream, String watermark) {
        baseCheck(inputStream, outputStream, watermark);
        try (InputStream fontInputStream = ResourceUtils.getURL("classpath:otf/SourceHanSerifSC-Bold.otf").openStream();) {
            //打开pdf文件
            PDDocument doc = PDDocument.load(inputStream);
            doc.setAllSecurityToBeRemoved(true);
            // 选择字体
            OTFParser otfParser = new OTFParser();
            OpenTypeFont otf = otfParser.parse(fontInputStream);
            //遍历pdf所有页
            for (PDPage page : doc.getPages()) {
                PDPageContentStream cs = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);

                //引入字体文件 解决中文汉字乱码问题
                PDFont font = PDType0Font.load(doc, otf, false);

                float fontSize = 25;
                PDExtendedGraphicsState r0 = new PDExtendedGraphicsState();
                // 水印透明度
                r0.setNonStrokingAlphaConstant(0.2f);
                r0.setAlphaSourceFlag(true);
                cs.setGraphicsStateParameters(r0);
                // 设置水印颜色
                cs.setNonStrokingColor(Color.blue);
                cs.beginText();
                cs.setFont(font, fontSize);
                //根据水印文字大小长度计算横向坐标需要渲染几次水印
                float h = watermark.length() * fontSize;

                int x = 100; // 水印的横向间距
                int theta = -150;

                // 获取旋转实例
                cs.setTextMatrix(Matrix.getRotateInstance(theta, 100, 100));
                cs.showText(watermark);

                //                for (int i = 0; i <= 10; i++) {
                //                    // 获取旋转实例
                //                    cs.setTextMatrix(Matrix.getRotateInstance(theta, i * x, 0));
                //                    cs.showText(watermark);
                //                    for (int j = 0; j < 20; j++) {
                //                        cs.setTextMatrix(Matrix.getRotateInstance(theta, i * x, j * h));
                //                        cs.showText(watermark);
                //                    }
                //                }
                cs.endText();
                cs.restoreGraphicsState();
                cs.close();
            }
            doc.save(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("PDF 添加水印失败！异常原因： " + e.getMessage());
        }

    }

    /**
     * 添加水印
     * 暂时未对坐标，字体大小等参数扩展，后期有需要，再进行扩展
     * 需要自行捕获异常，处理异常
     */
    public static void addWaterMark(URL url, OutputStream outputStream, String watermark) {
        baseCheck(url, outputStream, watermark);
        InputStream pdfInputStream;
        try {
            pdfInputStream = url.openStream();
        } catch (IOException e) {
            throw new RuntimeException("PDF 添加水印，文件流打开失败：" + e.getMessage());
        }

        addWaterMark(pdfInputStream, outputStream, watermark);

    }

    static void baseCheck(InputStream inputStream, OutputStream outputStream, String waterMark) {
        Assert.isTrue(Objects.nonNull(inputStream), "PDF 文件输入流不能为空");
        Assert.isTrue(Objects.nonNull(outputStream), "文件输出流 不能为空");
        Assert.isTrue(StringUtils.hasText(waterMark), "水印不能为空");
    }

    static void baseCheck(URL url, OutputStream outputStream, String waterMark) {
        Assert.isTrue(Objects.nonNull(url), "PDF 地址URL不能为空");
        Assert.isTrue(Objects.nonNull(outputStream), "文件输出流 不能为空");
        Assert.isTrue(StringUtils.hasText(waterMark), "水印不能为空");
    }

}
