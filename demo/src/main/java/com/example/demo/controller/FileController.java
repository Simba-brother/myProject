
package com.example.demo.controller;

import com.example.demo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class FileController {
    @Autowired
    FileService fileService;

    @RequestMapping("/fileJson")
    @ResponseBody
    public String getJson(HttpServletResponse response) throws IOException {
        String html = fileService.getLabel();
//        response.setCharacterEncoding("utf-8");
//        PrintWriter respWritter = response.getWriter();
//        respWritter.write(html);
        return html;
    }
}