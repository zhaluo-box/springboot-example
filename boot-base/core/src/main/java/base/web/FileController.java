package com.example.boot.base.web;

import com.example.boot.base.common.utils.CsvUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MingZhe on 2021/11/12 09:9:12
 *
 * @author wmz
 */
@RestController
@RequestMapping("/files/")
public class FileController {

    @PostMapping("actions/export-csv/")
    public void exportCsv(HttpServletResponse response) throws IOException {
        var reportName = "aaa.csv";
        response.reset();
        response.setContentType("application/octet-stream; charset=UTF-8");
        response.setHeader("Content-Encoding", "UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + reportName);

        String[] headers = { "名称", "年龄", "地址" };
        CsvUtil.write(response.getOutputStream(), headers, mockData());
    }

    private List<String[]> mockData() {
        var list = new ArrayList<String[]>();

        for (int i = 0; i < 10; i++) {
            String[] arr = { "name" + i, "age" + i, "address" + i };
            list.add(arr);
        }
        return list;
    }
}
