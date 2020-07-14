
package com.example.demo.controller;

import com.example.demo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;


@Controller
public class FileController {
    @Autowired
    FileService fileService;
    @RequestMapping("/getLabel")
    @ResponseBody
    public String getLable() throws IOException {
        String html = fileService.getLabel();
//        response.setCharacterEncoding("utf-8");
//        PrintWriter respWritter = response.getWriter();
//        respWritter.write(html);
        return html;
    }

    @RequestMapping("/replaceLabel")
    //@ResponseBody
    /**
     * map: <书签1：新值1， 标签2：新值2>
     */
    public String replaceLabel(@RequestBody TreeMap<String, String> map) {
        fileService.replaceLabel(map);
        return "success";
    }
}