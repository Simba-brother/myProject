package cn.wps.controller;

import cn.wps.ApplicationProperties;
import cn.wps.model.UrlModel;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static org.apache.tomcat.util.codec.binary.Base64.encodeBase64String;

@RestController
public class OauthController  {

    @RequestMapping(value="/weboffice/url", method = RequestMethod.GET)
    @ResponseBody
    public Object  getapp_Token(@RequestParam("_w_fname") String filename) throws UnsupportedEncodingException {
        if (filename == null || filename.isEmpty()) {
            return null;
        }
        String url = ApplicationProperties.domain + "/office";
        if (filename.endsWith("xls")) {
            url += "/s/2?";
        } else if (filename.endsWith("ppt")) {
            url += "/p/3?";
        } else if (filename.endsWith("pdf")) {
            url += "/f/4?";
        } else {
            url += "/w/1?";
        }
        Map paramMap= new HashMap<String, String>();
        paramMap.put("_w_appid", ApplicationProperties.appid);
        paramMap.put("_w_fname", filename);
        String signature = getSignature(paramMap, ApplicationProperties.appSecret);
        url += getUrlParam(paramMap) + "&_w_signature=" + signature;
        UrlModel urlModel = new UrlModel();
        urlModel.wpsUrl = url;
        urlModel.token = "1";
        return  urlModel;
    }

    private static String getUrlParam(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (builder.length() > 0) {
                builder.append('&');
            }
            builder.append(URLEncoder.encode(entry.getKey(), "utf-8")).append('=').append(URLEncoder.encode(entry.getValue(), "utf-8"));
        }
        return  builder.toString();
    }

    private static String getSignature(Map<String, String> params, String appSecret) {
        List<String> keys=new ArrayList();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            keys.add(entry.getKey());
        }

         // 将所有参数按key的升序排序
        Collections.sort(keys, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        // 构造签名的源字符串
        StringBuilder contents=new StringBuilder("");
        for (String key : keys) {
            if (key=="_w_signature"){
                continue;
            }
            contents.append(key+"=").append(params.get(key));
            System.out.println("key:"+key+",value:"+params.get(key));
        }
        contents.append("_w_secretkey=").append(appSecret);

        // 进行hmac sha1 签名
        byte[] bytes= hmacSha1(appSecret.getBytes(),contents.toString().getBytes());
        //字符串经过Base64编码
        String sign= encodeBase64String(bytes);
        try {
            sign = URLEncoder.encode( sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(sign);
        return sign;
    }

    public static byte[] hmacSha1(byte[] key, byte[] data) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
            Mac mac = Mac.getInstance(signingKey.getAlgorithm());
            mac.init(signingKey);
            return mac.doFinal(data);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }
}
