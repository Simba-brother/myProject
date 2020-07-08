package cn.wps.controller;

import cn.wps.model.FileModel;
import cn.wps.model.UserModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;

@RestController
public class WebOfficeController {

    @RequestMapping(value="/v1/3rd/file/info", method = RequestMethod.GET)
    @ResponseBody
    public Object fileInfo(@RequestParam("_w_fname") String filename) {
        JSONObject jsonObject = new JSONObject();
        JSONObject file = new JSONObject();
        JSONObject user = new JSONObject();
        try {
            FileModel fileModel = new FileModel();
            File localFile = new File(filename);
            fileModel.size = localFile.length();
            file.put("id", filename.substring(0,1));
            file.put("name", filename);
            file.put("version", fileModel.version);
            file.put("size", fileModel.size);
            file.put("creator", fileModel.creator);
            file.put("modifier", fileModel.modifier);
            file.put("download_url", fileModel.download_url + filename);
            jsonObject.put("file", file);
            UserModel userModel = new UserModel();
            user.put("id", userModel.id);
            user.put("name", userModel.name);
            user.put("permission", "write");
            user.put("avatar_url", userModel.avatar_url);
            jsonObject.put("user", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @RequestMapping(value="/v1/3rd/file/version/{version}", method = RequestMethod.GET)
    @ResponseBody
    public Object fileVersionInfo(@PathVariable("version") Long version, @RequestParam("_w_fname") String filename) {
        JSONObject jsonObject = new JSONObject();
        JSONObject file = new JSONObject();
        JSONObject user = new JSONObject();
        try {
            FileModel fileModel = new FileModel();
            File localFile = new File(filename);
            fileModel.size = localFile.length();
            file.put("id", fileModel.id);
            file.put("name", filename);
            file.put("version", fileModel.version);
            file.put("size", fileModel.size);
            file.put("creator", fileModel.creator);
            file.put("modifier", fileModel.modifier);
            file.put("download_url", fileModel.download_url + filename);
            jsonObject.put("file", file);
            UserModel userModel = new UserModel();
            user.put("id", userModel.id);
            user.put("name", userModel.name);
            user.put("permission", "write");
            user.put("avatar_url", userModel.avatar_url);
            jsonObject.put("user", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @RequestMapping(value="/v1/3rd/user/info", method = RequestMethod.POST)
    @ResponseBody
    public Object userInfo() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject user = new JSONObject();
        UserModel userModel = new UserModel();
        try {
            user.put("id", userModel.id);
            user.put("name", userModel.name);
            user.put("permission", userModel.permission);
            user.put("avatar_url", userModel.avatar_url);
            jsonArray.put(user);
            jsonObject.put("users", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @RequestMapping(value="/v1/3rd/file/online", method = RequestMethod.POST)
    @ResponseBody
    public void online() {
    }

    @RequestMapping(value = "/v1/3rd/file/save", method = RequestMethod.POST)
    @ResponseBody
    public Object save(@RequestParam("file") MultipartFile file, @RequestParam("_w_fname") String filename) {
        if (!file.isEmpty()) {
            try {
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(new File(filename)));
                out.write(file.getBytes());
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new FileModel();
    }

    @GetMapping("/weboffice/getFile")
    public ResponseEntity<byte[]> getFile(@RequestParam("_w_fname") String filename) throws Exception {
        File file = new File(filename);
        InputStream inputStream = new FileInputStream(file);
        byte[] body = new byte[inputStream.available()];
        HttpHeaders headers=new HttpHeaders();
        headers.add("Content-Disposition", "attachment;filename="+filename);
        inputStream.read(body);
        return new ResponseEntity(body, headers, HttpStatus.OK);
    }
}
