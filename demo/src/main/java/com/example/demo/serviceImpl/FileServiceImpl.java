
package com.example.demo.serviceImpl;
import com.example.demo.service.FileService;
import com.spire.doc.collections.BookmarkCollection;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.documents.TextBodyPart;
import com.spire.doc.fields.TextRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.spire.doc.*;
import com.spire.doc.documents.BookmarksNavigator;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


@Service
public class FileServiceImpl implements FileService{

    //    //将DAO注入Service层
//    @Autowired
//    private UserMapper userMapper;
//    @Autowired
    private Document doc;

    public FileServiceImpl() {
        doc = new Document();
    }

    @Override
    public String getLabel() {
        /*
        //json串
        String jsonStr = "{\"标签1\":\"default1\", \"标签2\": \"default2\"}";
        //得到json对象
        JSONObject jsonObject = new JSONObject().parseObject(jsonStr, Feature.OrderedField);// 将json格式的字符串转换成json
        //拼接html
        StringBuffer html = new StringBuffer();

        for (String key:jsonObject.keySet()) {
            html.append(key+"<input value = \"" + jsonObject.get(key) + "\"/><br>");
        }
        */

        //装载文件
        doc.loadFromFile("/Users/mml/repositories/myProject/demo/testLable.docx");
        //获得书签对象集合
        BookmarkCollection bookmarkCollection =  doc.getBookmarks();
        //获得书签数量
        int count = bookmarkCollection.getCount();
        //获得书签导航
        BookmarksNavigator bookmarksNavigator = new BookmarksNavigator(doc);
        //声明书签对象
        Bookmark bookmark = null;
        //存储书签名称集合
        TreeMap<String, String> bookmark_map = new TreeMap<>();

        for (int i = 0; i < count; i++) {
            //得到书签
            bookmark = bookmarkCollection.get(i);
            //得到书签名称
            String name = bookmark.getName();
            //获得书签内容
            bookmarksNavigator.moveToBookmark(name);
            TextBodyPart textBodyPart = bookmarksNavigator.getBookmarkContent();
            String content = "";
            //遍历书签内容的项目
            for (Object item : textBodyPart.getBodyItems()) {
                //判断项目是否为段落
                if (item instanceof Paragraph) {
                    Paragraph paragraph = (Paragraph) item;
                    //遍历段落中的子对象
                    for (Object childObj : paragraph.getChildObjects()) {
                        //判断子对象是否为TextRange
                        if (childObj instanceof TextRange) {
                            //获取TextRange中的文本
                            TextRange textRange = (TextRange) childObj;
                            content = content + textRange.getText();
                        }
                    }
                }
            }
            //标签与内容映射
            bookmark_map.put(name, content);
        }
        //拼接前端html
        StringBuffer html = new StringBuffer();
        int id = 0;
        for (String key:bookmark_map.keySet()) {
            html.append(key+"<input id = \""+id+"\" value = \"" + bookmark_map.get(key) + "\" name = \""+key+"\"/><br>");
            id++;
        }
        return html.toString();
    }

    /**
     * 根据前端传过来的标签，替换标签对应的文本值
     * @param map  <书签1：新值1> <书签2：新值2>
     */
    @Override
    public void replaceLabel(TreeMap<String, String> map) {
        doc.loadFromFile("/Users/mml/repositories/myProject/demo/testLable.docx");
        for (String lable:map.keySet()) {
            //定位到指定书签位置
            BookmarksNavigator bookmarksNavigator = new BookmarksNavigator(doc);
            bookmarksNavigator.moveToBookmark(lable);
            //用文本内容替换原有书签位置的文本，新替换的内容与原文格式一致
            bookmarksNavigator.replaceBookmarkContent(map.get(lable),true);
        }
        //保存文档

        doc.saveToFile("/Users/mml/repositories/myProject/demo/newFile"+new Date() +".docx",FileFormat.Docx_2013);
        
        System.out.println("另存成功");
        doc.dispose();
    }
}