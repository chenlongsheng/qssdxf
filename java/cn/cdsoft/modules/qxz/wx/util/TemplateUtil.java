package cn.cdsoft.modules.qxz.wx.util;

import cn.cdsoft.common.mq.rabbitmq.DevAlarm;
import cn.cdsoft.modules.qxz.wx.controller.AdvancedUtil;
import cn.cdsoft.modules.qxz.wx.entry.Moban;
import cn.cdsoft.modules.qxz.wx.entry.TemplateData;
import cn.cdsoft.modules.qxz.wx.entry.TextObject;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZZUSER on 2018/11/23.
 * 模板消息工具类
 */
public class TemplateUtil {

    //数据告警模板
    public static Moban dataAlarmTemplate(DevAlarm devAlarm, Map map1, String plotsId, String openId){
        Moban moban = new Moban();
        Map map = new HashMap();
        TemplateData templateData = new TemplateData();
        templateData.setColor("#DC143C");
        templateData.setValue("               监测到数据异常，请及时查看\r\n");
        map.put("first",templateData);

        templateData = new TemplateData();
        templateData.setColor("#1E90FF");
        templateData.setValue(devAlarm.getItems().get(0).getOccur_time());
        map.put("keyword1","  "+templateData);

        templateData = new TemplateData();
        templateData.setColor("#1E90FF");
        templateData.setValue("\r\n     设备类型:  "+map1.get("typeName")+"\r\n     设备名称:  "+map1.get("name")+"\r\n     设备型号:  "+map1.get("unitType")+"\r\n     设备位置:  "+map1.get("addr"));
        map.put("keyword2",templateData);

        moban.setData(map);
        moban.setTemplate_id("n--HgHMMlK5iUxdvwOXolPrr3PpcXVCPxXFS1e_cM0U");
        moban.setTouser(openId);
        String path = PropertiesUtil.getProperty("localPath");
        String devType = String.valueOf(map1.get("devType"));
//        moban.setUrl(path+"/page.html?type=1");
        moban.setUrl(path+"/page.html?plotsId="+plotsId+"&devTypeId="+devType+"&devId="+devAlarm.getDev_id()+"&type=1");
        System.out.println("url:"+moban.getUrl());
        moban.setEmphasis_keyword("keyword1.DATA");
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


    public static void sendKefu(String toUser){
        TextObject textObject = new TextObject();
        textObject.setToUser("oa9Na0_cnFnyb7Lc-MJDCc2vB8YA");
        textObject.setMsgtype("text");
        Map map = new HashMap();
        map.put("content","你好");
        textObject.setText(map);
        String json = JSONObject.toJSONString(textObject);

//        String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
//        String accessToken = (String) JedisUtils.getObject("WxAccessToken");
//        url = url.replaceAll("ACCESS_TOKEN", accessToken);
//        String httpsRequest = WeChatApiUtil.httpsRequestToString(url,"POST",json);
//        System.out.println("***httpsRequest>"+httpsRequest);

        String api = "http://wx.cdsoft.cn/index.php/accesstoken";
        String token = HttpUtils.sendGet(api,null,null);
        Map tokenMap = JSONObject.parseObject(token);
        String accessToken = String.valueOf(tokenMap.get("access_token"));
        String jsonTextMsg = AdvancedUtil.makeImageCustomMessage(toUser, "ckQHwqSDD08SJ5XObc7xHOPUuBcJk1aFbjoeoI_iZ9c");//发送文本信息 到1用户
        String jsonTextMsg2 = AdvancedUtil.makeTextCustomMessage(toUser, "测试客服消息！"); //客服消息 组装文本
        String jsonTextMsg3 = AdvancedUtil.makeNews(toUser);
        System.out.println("sssssssssss:"+jsonTextMsg3);
//         CommonUtil.sendCustomMessage(accessToken,jsonTextMsg);
        String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
        url = url.replaceAll("ACCESS_TOKEN", accessToken);
        String httpsRequest = WeChatApiUtil.httpsRequestToString(url,"POST",jsonTextMsg3);
        System.out.println(httpsRequest);
    }


    public static void sendCompment(String toUser){
        String api = "http://wx.cdsoft.cn/index.php/accesstoken";
        String token = HttpUtils.sendGet(api,null,null);
        Map tokenMap = JSONObject.parseObject(token);
        String accessToken = String.valueOf(tokenMap.get("access_token"));
        String jsonTextMsg3 = AdvancedUtil.makeMpNews(toUser);
        String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
        url = url.replaceAll("ACCESS_TOKEN", accessToken);
        String httpsRequest = WeChatApiUtil.httpsRequestToString(url,"POST",jsonTextMsg3);
        System.out.println(httpsRequest);
    }

    static class PrintThread extends Thread {
        private String toUser;
        public PrintThread(String toUser) {
            this.toUser = toUser;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(500);
                sendKefu(toUser);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void kefu(String toUser){
        new PrintThread(toUser).start();
    }

    public static void main(String[] args){
//        sendKefu();
        kefu("o6bdvwAlR83a_P4tL-4q4gDhFnnM");
    }

}
