
package com.example.demo.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.example.demo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FileServiceImpl implements FileService{

//    //将DAO注入Service层
//    @Autowired
//    private UserMapper userMapper;
    @Override
    public String getLabel() {
        //json串
        String jsonStr = "{\"标签1\":\"default1\", \"标签2\": \"default2\"}";
        //得到json对象
        JSONObject jsonObject = new JSONObject().parseObject(jsonStr, Feature.OrderedField);// 将json格式的字符串转换成json
        //拼接html
        StringBuffer html = new StringBuffer();

        for (String key:jsonObject.keySet()) {
            html.append(key+"<input value = \"" + jsonObject.get(key) + "\"/><br>");
        }
        return html.toString();
    }

}