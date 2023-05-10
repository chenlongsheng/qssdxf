package cn.cdsoft.modules.qxz.wx.controller;

/**
 * Created by ZZUSER on 2018/11/2.
 */

import cn.cdsoft.modules.qxz.wx.util.PropertiesUtil;
import cn.cdsoft.modules.qxz.wx.util.WeChatApiUtil;
import com.google.gson.Gson;
import cn.cdsoft.common.utils.JedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancedUtil {
    private static Logger log = LoggerFactory.getLogger(AdvancedUtil.class);

    /**
     * 组装发送文本消息
     *
     * @return
     */
    public static String makeTextCustomMessage(String openId, String content) {
        content = content.replace("\"", "\\\"");
        String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"text\",\"text\":{\"content\":\"%s\"}}";
        return String.format(jsonMsg, openId, content);
    }

    /**
     * 组装发送图片消息
     *
     * @return
     */
    public static String makeImageCustomMessage(String openId, String mediaId) {
        String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"image\",\"image\":{\"media_id\":\"%s\"}}";
        return String.format(jsonMsg, openId, mediaId);
    }

    /**
     * 组装发送语音消息
     *
     * @return
     */
    public static String makeVoiceCustomMessage(String openId, String mediaId) {
        String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"voice\",\"voice\":{\"media_id\":\"%s\"}}";
        return String.format(jsonMsg, openId, mediaId);
    }

    public static String makeVideoCustomMessage(String openId, String mediaId, String thumbMediaId) {
        String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"video\",\"video\":{\"media_id\":\"%s\",\"thumb_media_id\":\"%s\"}}";
        return String.format(jsonMsg, openId, mediaId, thumbMediaId);
    }

    /**
     * 图文消息
     * @param openId
     * @return
     */
    public static String makeNews(String openId){
        String path = PropertiesUtil.getProperty("localPath");
        System.out.println("path:"+path);
        Map map = new HashMap();
        map.put("touser",openId);
        map.put("msgtype","news");
        Map map1 = new HashMap();
        List list = new ArrayList();
        Map map2 = new HashMap();
        map2.put("title","点击进入绑定页");
        map2.put("url",path+"/bingding.html?openId="+openId);
//        map2.put("url","http:www.baidu.com");
        map2.put("picurl",path+"/static_modules/gzh/plots.png");
        list.add(map2);
        map1.put("articles",list);
        map.put("news",map1);
        Gson gson = new Gson();
        String json = gson.toJson(map);
        return json;
    }

    public static String makeMpNews(String openId){
        Map map = new HashMap();
        map.put("touser",openId);
        map.put("msgtype","mpnews");
        Map map1= new HashMap();
        map1.put("media_id","ckQHwqSDD08SJ5XObc7xHDUuOSP92MFblvQ4BSZltrs");
        map.put("mpnews",map1);
        Gson gson = new Gson();
        String json = gson.toJson(map);
        return json;
    }


//    public static String makeMusicCustomMessage(String openId, Music music) {
//        String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"music\",\"music\":%s}";
//        jsonMsg = String.format(jsonMsg, openId, JSONObject.fromObject(music).toString());
//        jsonMsg = jsonMsg.replace("thumbmediaid", "thumb_media_id");
//        return jsonMsg;
//    }
//
//    public static String makeNewsCustomMessage(String openId, List<Article> articleList) {
//        String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"news\",\"news\":{\"articles\":%s}}";
//        jsonMsg = String.format(jsonMsg, openId, JSONArray.fromObject(articleList).toString().replaceAll("\"","\\\""));
//        jsonMsg = jsonMsg.replace("picUrl", "picurl");
//        return jsonMsg;
//    }
//
//    public static void main(String args[]){
//        System.out.println(makeMusicCustomMessage("xxxxxxxxxx",new Music()));
//        System.out.println(makeVideoCustomMessage("xxxxxxxxxx","m_id","t_m_id"));
//        System.out.println(makeVoiceCustomMessage("xxxxxxxxxx","m_id"));
//        System.out.println(makeImageCustomMessage("xxxxxxxxxx","m_id"));
//    }

    public static void main(String args[]) {
        String appId ="wx8b8638def0231eff";
        String appSecret = "9f15318dab99e670be4138fc8339fe7e";
        String accessToken = (String) JedisUtils.getObject("WxAccessToken");
        AdvancedUtil advancedUtil = new AdvancedUtil();
        String jsonTextMsg = advancedUtil.makeTextCustomMessage("gh_e8355a96d525", "测试客服消息！小子，你厉害");//发送文本信息 到1用户
//        String jsonTextMsg2 = AdvancedUtil.makeTextCustomMessage("oO5Cbs-KyEXBcLGM4tW_7QS0EJ2Y", "测试客服消息！"); //客服消息 组装文本
//         CommonUtil.sendCustomMessage(accessToken,jsonTextMsg);
        String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
        url = url.replaceAll("ACCESS_TOKEN", accessToken);
        String httpsRequest = WeChatApiUtil.httpsRequestToString(url,"post",jsonTextMsg);
        // CommonUtil.sendCustomMessage(at.getAccessToken(),jsonTextMsg2);    //客服消息，发送文本

//        List<Article> articleList = new ArrayList<Article>();
//        Article article = new Article();
//        article.setTitle("信息服务站");
//        article.setDescription("信息服务站提供及时的各种信息，包括招聘，出租，兼职，美食，美女，帅哥，招租等。为你及时就地的服务。");
//        article.setPicUrl("http://7xjjge.com1.z0.glb.clouddn.com/xxfw.jpg");
//        article.setUrl("http://" + R.dns + "/chatman/msg_list.do?fromuser=oO5Cbs-KyEXBcLGM4tW_7QS0EJ2Y");
//        articleList.add(article);
//        String newsMessage = AdvancedUtil.makeNewsCustomMessage("oO5Cbs-KyEXBcLGM4tW_7QS0EJ2Y", articleList);   //客服消息 组装图文  发送图文消息给用户
//
//        CommonUtil.sendCustomMessage(at.getAccessToken(), newsMessage);

    }
}

