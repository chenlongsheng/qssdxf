package cn.cdsoft.modules.warm.util;

import cn.cdsoft.common.mq.rabbitmq.*;
import cn.cdsoft.modules.qxz.wx.entry.Moban;
import cn.cdsoft.modules.qxz.wx.entry.TemplateData;
import cn.cdsoft.modules.qxz.wx.util.PropertiesUtil;
import cn.cdsoft.modules.qxz.wx.util.WeChatApiUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import cn.cdsoft.modules.qxz.wx.util.HttpUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZZUSER on 2018/12/21.
 */
public class PdfTemplateUtil {
    //数据告警模板
    public static Moban dataAlarmTemplate(Map map, String openId){
        Moban moban = new Moban();
        TemplateData templateData = new TemplateData();
        templateData.setColor("#DC143C");
        templateData.setValue("               监测到数据异常，请及时查看\r\n");
        map.put("first",templateData);

        templateData = new TemplateData();
        templateData.setColor("#1E90FF");
        templateData.setValue(String.valueOf(map.get("alarmTime")));
        map.put("keyword1","  "+map.get("alarmTime"));

        templateData = new TemplateData();
        templateData.setColor("#1E90FF");
        templateData.setValue("\r\n     设备类型:  "+map.get("devTypeName")+"\r\n     设备名称:  "+map.get("devName")+"\r\n     报警级别:  "+map.get("alarmLevel")+"\r\n     问题描述:  "+map.get("prec"));
        map.put("keyword2",templateData);

        moban.setData(map);
        moban.setTemplate_id("n--HgHMMlK5iUxdvwOXolPrr3PpcXVCPxXFS1e_cM0U");
        moban.setTouser(openId);
        String path = PropertiesUtil.getProperty("localPath");
//        moban.setUrl(path+"/page.html?type=1");
//        moban.setUrl(path+"/page.html?plotsId="+plotsId+"&devTypeId="+devType+"&devId="+devAlarm.getDev_id()+"&type=1");
        Map map1 = new HashMap();
        map1.put("appid","wxc735adf03843f28e");
        map1.put("path","index?openId="+openId+"&orderId="+map.get("id"));
        moban.setMiniprogram(map1);
        System.out.println("url:"+moban.getUrl());
        return moban;
    }

    public static Moban devFaultTemplate(DevAlarm devAlarm, Map map1, String openId){
        Moban moban = new Moban();
        Map map = new HashMap();
        TemplateData templateData = new TemplateData();
        templateData.setColor("#1E90FF");
        templateData.setValue("监测到设备故障，请及时查看!");
        map.put("first",templateData);

        templateData = new TemplateData();
        templateData.setColor("#1E90FF");
        templateData.setValue(devAlarm.getItems().get(0).getOccur_time());
        map.put("keyword1",templateData);

        templateData = new TemplateData();
        templateData.setColor("#1E90FF");
        templateData.setValue("\r\n     设备类型:"+map1.get("typeName")+"\r\n     设备名称:"+map1.get("name")+"\r\n     设备型号:"+map1.get("unitType")+"\r\n     设备位置:"+map1.get("addr"));
        map.put("keyword2",templateData);

        moban.setData(map);
        moban.setTemplate_id("hikkgMG8eRzcOEW0rf8QkdaF40EPNqgHqsOCzr8m8FA");
        moban.setTouser(openId);
        moban.setUrl("www.baidu.com");
        return moban;
    }

    public static Moban order(Map map, String openId){
        Moban moban = new Moban();
        TemplateData templateData = new TemplateData();
        templateData.setColor("#000000");
        templateData.setValue("                     工单状态更新提醒\r\n");
        map.put("first",templateData);

        templateData = new TemplateData();
        templateData.setColor("#000000");
        templateData.setValue(String.valueOf(map.get("id")));
        map.put("keyword1",templateData);

        templateData = new TemplateData();
        templateData.setColor("#000000");
        int state = (int) map.get("state");
        if(state==4){
            templateData.setValue("处理中");
        }else if(state==5) {
            templateData.setValue("报警解除");
        }
        map.put("keyword2",templateData);

        templateData = new TemplateData();
        templateData.setColor("#000000");
        templateData.setValue(String.valueOf(map.get("sendName")));
        map.put("keyword3",templateData);

        templateData = new TemplateData();
        templateData.setColor("#1E90FF");
        if(state ==4){
            templateData.setValue("                    \r\n您的工单有新留言");
        }
        if(state ==5){
            templateData.setValue("                    \r\n您的工单已解除报警");
        }
        map.put("remark",templateData);

        moban.setData(map);
        moban.setTemplate_id("a6yt32AL2qZbyKExajYj7uUcrbXfpFIufnMBIsGMqHc");
        moban.setTouser(openId);
        Map map1 = new HashMap();
        map1.put("appid","wxc735adf03843f28e");
        map1.put("path","index?openId="+openId+"&orderId="+map.get("id"));
        moban.setMiniprogram(map1);
        return moban;
    }

    public static void main(String[] args){
        Map map = new HashMap();
        map.put("id","12");
        map.put("state",5);
        map.put("sendName","admin");
        Moban moban = PdfTemplateUtil.order(map,"o6bdvwAlR83a_P4tL-4q4gDhFnnM");
        String api = "http://wx.cdsoft.cn/index.php/accesstoken";
        String token = HttpUtils.sendGet(api,null,null);
        Map tokenMap = JSONObject.parseObject(token);
        String accessToken = String.valueOf(tokenMap.get("access_token"));

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
}
