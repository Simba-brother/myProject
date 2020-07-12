
package com.example.demo.service;

import java.util.Map;
import java.util.TreeMap;

public interface FileService {

    String getLabel();
    void replaceLabel(TreeMap<String,String> map);
}