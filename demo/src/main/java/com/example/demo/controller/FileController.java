
package com.example.demo.controller;

import com.example.demo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FileController {
    @Autowired
    FileService fileService;

    @RequestMapping("/fileJson")
    public String getJson() {
        fileService.getLabel();
        return "index";
    }
}