package cn.cdsoft.modules.qxz.wx.controller;

import cn.cdsoft.modules.qxz.wx.util.PropertiesUtil;
import cn.cdsoft.modules.qxz.wx.util.WeChatApiUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import cn.cdsoft.modules.qxz.wx.util.HttpUtils;
import cn.cdsoft.modules.qxz.wx.util.TemplateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZZUSER on 2018/11/30.
 */
@Controller
@RequestMapping("/wx")
public class Btn {
    @RequestMapping(value="/addBtn")
    @ResponseBody
    public void addBtn(){
        //自定义菜单创建接口
        String api = "http://wx.cdsoft.cn/index.php/accesstoken";
        String token = HttpUtils.sendGet(api,null,null);
        Map map = JSONObject.parseObject(token);
        String accessToken = String.valueOf(map.get("access_token"));
//         accessToken = WeChatApiUtil.getToken("wx8b8638def0231eff","9f15318dab99e670be4138fc8339fe7e");
        String menuUrl="https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+accessToken;

        //button array
        JSONArray btnArray=new JSONArray();
        //button1
//        JSONObject btn1Json=new JSONObject();
//        btn1Json.put("type","click");
//        btn1Json.put("name","我的");
//        btn1Json.put("key","MINE");
        //button2
        JSONObject btn2Json=new JSONObject();
        btn2Json.put("type","click");
        btn2Json.put("name","公司简介");
        String path = PropertiesUtil.getProperty("localPath");
        btn2Json.put("key","compment");

        List list = new ArrayList();
        JSONObject btn4Json=new JSONObject();
        btn4Json.put("type","click");
        btn4Json.put("name","设备故障报警项目");
        btn4Json.put("key","devAlarm");
        JSONObject btn5Json=new JSONObject();
        btn5Json.put("type","click");
        btn5Json.put("name","维修工单推送系统");
        btn5Json.put("key","scheduling");
        JSONObject btn6Json=new JSONObject();
        btn6Json.put("type","click");
        btn6Json.put("name","新项目");
        btn6Json.put("key","newProject");
//        String path1 = PropertiesUtil.getProperty("localPath");
//        btn6Json.put("url",path1+"/bingding.html");
        list.add(btn4Json);
        list.add(btn5Json);
        list.add(btn6Json);

        JSONObject btn3Json = new JSONObject();
        btn3Json.put("name","我的");
        btn3Json.put("sub_button",list);


//        //button3
//        JSONObject btn3Json=new JSONObject();
//        btn3Json.put("type","view");
//        btn3Json.put("name","业务员");
//        btn3Json.put("url","https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx59edf850079b7948&redirect_uri=http://ast.tunnel.qydev.com/wechat/admin/orderBase/getOrderBasePage.action&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
        btnArray.add(btn3Json);
        btnArray.add(btn2Json);
//        btnArray.add(btn3Json);

        JSONObject json=new JSONObject();
        json.put("button",btnArray);
//        JSONObject jsonObject = HttpClientUtil.getInstance().httpPostRequest(menuUrl, json.toString());
        WeChatApiUtil.httpsRequestToString(menuUrl,"POST",json.toString());
        System.out.println("自定义菜单创建接口" );
    }

    @RequestMapping(value="/deleteBtn")
    @ResponseBody
    public void deleteBtn(){
        String api = "http://wx.cdsoft.cn/index.php/accesstoken";
        String token = HttpUtils.sendGet(api,null,null);
        Map map = JSONObject.parseObject(token);
        String accessToken = String.valueOf(map.get("access_token"));
//        accessToken = WeChatApiUtil.getToken("wx8b8638def0231eff","9f15318dab99e670be4138fc8339fe7e");
        String url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token="+accessToken;
        WeChatApiUtil.httpsRequestToString(url,"GET",null);
    }

    @RequestMapping(value="/getAccesstoken")
    @ResponseBody
    public void getAccesstoken(){
        String api = "http://wx.cdsoft.cn/index.php/accesstoken";
        String token = HttpUtils.sendGet(api,null,null);
        Map map = JSONObject.parseObject(token);
        String accessToken = String.valueOf(map.get("access_token"));
        String url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token="+accessToken;
        WeChatApiUtil.httpsRequestToString(url,"GET",null);
    }

    /**
     * 获取素材列表
     */
    @RequestMapping(value="/getSucaiList")
    @ResponseBody
    public void getSucaiList(){
        String api = "http://wx.cdsoft.cn/index.php/accesstoken";
        String token = HttpUtils.sendGet(api,null,null);
        Map map = JSONObject.parseObject(token);
        String accessToken = String.valueOf(map.get("access_token"));
        Map map1 = new HashMap();
        map1.put("type","news");
        map1.put("offset",0);
        map1.put("count",20);
        Gson gson = new Gson();
        String json = gson.toJson(map1);
        String url = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token="+accessToken;
        String result = WeChatApiUtil.httpsRequestToString(url,"GET",json);
        System.out.println(result);
    }

    @RequestMapping(value="/sendCompent")
    @ResponseBody
    public void sendCompent(){
        TemplateUtil.sendCompment("o6bdvwAlR83a_P4tL-4q4gDhFnnM");
    }

    public static void main(String[] args){
//        String api = "http://wx.cdsoft.cn/index.php/accesstoken";
//        String token = HttpUtils.sendGet(api,null,null);
//        Map map = JSONObject.parseObject(token);
//        String accessToken = String.valueOf(map.get("access_token"));
//       System.out.println(accessToken);


    }



}
