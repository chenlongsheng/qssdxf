package cn.cdsoft.modules.qxz.wx.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import cn.cdsoft.common.utils.JedisUtils;
import cn.cdsoft.modules.qxz.wx.entry.Moban;
import cn.cdsoft.modules.qxz.wx.entry.TemplateData;
import cn.cdsoft.modules.qxz.wx.util.WeChatApiUtil;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZZUSER on 2018/11/2.
 */
@Controller
@RequestMapping("/qxz")
public class TestKefuController {
    @RequestMapping(value="/testKefu")
    @ResponseBody
    public void testKefu(){
//        TextObject textObject = new TextObject();
//        textObject.setToUser("oa9Na0_cnFnyb7Lc-MJDCc2vB8YA");
//        textObject.setMsgtype("text");
//        Map map = new HashMap();
//        map.put("content","你好");
//        textObject.setText(map);
//        String json = JSONObject.toJSONString(textObject);
//
//        String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
//        String accessToken = (String) JedisUtils.getObject("WxAccessToken");
//        url = url.replaceAll("ACCESS_TOKEN", accessToken);
//        String httpsRequest = WeChatApiUtil.httpsRequestToString(url,"POST",json);
//        System.out.println("***httpsRequest>"+httpsRequest);

//        String appId ="wx8b8638def0231eff";
//        String appSecret = "9f15318dab99e670be4138fc8339fe7e";
//        String accessToken = (String) JedisUtils.getObject("WxAccessToken");
//        String jsonTextMsg = AdvancedUtil.makeTextCustomMessage("oa9Na0_cnFnyb7Lc-MJDCc2vB8YA", "测试客服消息！小子，你厉害");//发送文本信息 到1用户
////        String jsonTextMsg2 = AdvancedUtil.makeTextCustomMessage("oO5Cbs-KyEXBcLGM4tW_7QS0EJ2Y", "测试客服消息！"); //客服消息 组装文本
////         CommonUtil.sendCustomMessage(accessToken,jsonTextMsg);
//        String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
//        url = url.replaceAll("ACCESS_TOKEN", accessToken);
//        String httpsRequest = WeChatApiUtil.httpsRequestToString(url,"POST",jsonTextMsg);
//        System.out.println(httpsRequest);


        String accessToken = (String) JedisUtils.getObject("WxAccessToken");
        Moban moban = new Moban();
        Map map = new HashMap();
        TemplateData templateData = new TemplateData();
        templateData.setColor("#1E90FF");
        templateData.setValue("监测到数据异常，请及时查看");
        map.put("title",templateData);

        templateData = new TemplateData();
        templateData.setColor("#1E90FF");
        templateData.setValue("2018-11-11");
        map.put("time",templateData);

        templateData = new TemplateData();
        templateData.setColor("#1E90FF");
        templateData.setValue("\r\n     设备名称:"+"sdsds\r\n     设备类型:"+"ddd\r\n     设备位置:"+"打发打发");

//        Map map1 = new HashMap();
//        map1.put("设备类型","wew");
//        map1.put("设备型号","dfdfd");
        map.put("dev",templateData);
        moban.setData(map);
        moban.setTemplate_id("fvF6dKVk813UxHWalPD-GNkn2CoSgm9vc5vhTBCiH0U");
        moban.setTouser("oa9Na0_cnFnyb7Lc-MJDCc2vB8YA");
        moban.setUrl("www.baidu.com");

        String json = JSONObject.toJSONString(moban);
        System.out.println(json);
        Gson gson=new Gson();
        String json1 = gson.toJson(moban);
        System.out.println(json1);
        String url="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
        url = url.replaceAll("ACCESS_TOKEN", accessToken);
        String httpsRequest = WeChatApiUtil.httpsRequestToString(url,"POST",json1);
        System.out.println(httpsRequest);
    }


    public static void sendWechatMessage(String touser, String template_id, String url, String miniprogram, Map<String,Object> data) throws IOException {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost("http://wx.cdsoft.cn/index.php/templatemessage?appid=wx98841a0227941381");
                Map<String,Object> bodyMap = new HashMap<String,Object>();
        bodyMap.put("touser",touser);
        bodyMap.put("template_id",template_id);
        bodyMap.put("url",url);
        bodyMap.put("miniprogram",miniprogram);
        bodyMap.put("data",data);
        Gson gson=new Gson();
        String json = gson.toJson(bodyMap);
        System.out.println(json);
        // 创建参数队列
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("message", json));
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    System.out.println("--------------------------------------");
                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
                    System.out.println("--------------------------------------");
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
