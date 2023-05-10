package cn.cdsoft.modules.qxz.wx.util;


import cn.cdsoft.modules.qxz.dao.GzhUserDao;
import cn.cdsoft.modules.qxz.dao.QxzDao;
import cn.cdsoft.modules.qxz.entity.GzhUser;
import cn.cdsoft.modules.qxz.wx.Common.MessageType;
import cn.cdsoft.modules.warm.dao.PdfBindDao;
import cn.cdsoft.modules.warm.entity.PdfBind;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息处理工具类
 * Created by xdp on 2016/1/26.
 */
@Service
public class MessageHandlerUtil {
    @Autowired
    QxzDao qxzDao;

    @Autowired
    GzhUserDao gzhUserDao;

    @Autowired
    PdfBindDao pdfBindDao;

    /**
     * 解析微信发来的请求（XML）
     *
     * @param request 封装了请求信息的HttpServletRequest对象
     * @return map 解析结果
     * @throws Exception
     */
    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();
        // 从request中取得输入流
        InputStream inputStream = request.getInputStream();
       String result = XmlUtil.getBodyString(inputStream);
System.out.println(result);
//        // 读取输入流
//        SAXReader reader = new SAXReader();
//        Document document = reader.read(inputStream);
//        // 得到xml根元素
//        Element root = document.getRootElement();
//        // 得到根元素的所有子节点
//        List<Element> elementList = root.elements();
//
//        // 遍历所有子节点
//        for (Element e : elementList) {
//            System.out.println(e.getName() + "|" + e.getText());
//            map.put(e.getName(), e.getText());
//        }
//
//        // 释放资源
//        inputStream.close();
//        inputStream = null;
//        return map;
        Map<String, String> data = new HashMap<String, String>();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        InputStream stream = new ByteArrayInputStream(result.getBytes("UTF-8"));
        org.w3c.dom.Document doc = documentBuilder.parse(stream);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getDocumentElement().getChildNodes();
        for (int idx = 0; idx < nodeList.getLength(); ++idx) {
            Node node = nodeList.item(idx);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                data.put(element.getNodeName(), element.getTextContent());
            }
        }
        try {
            inputStream.close();
        } catch (Exception ex) {
            // do nothing
        }
        System.out.println(JSONObject.toJSONString(data));
        return data;
    }

    /**
     * 根据消息类型构造返回消息
     * @param map 封装了解析结果的Map
     * @return responseMessage(响应消息)
     */
    public String buildResponseMessage(Map map) {
        //响应消息
        String responseMessage = "";
        //得到消息类型
        String msgType = map.get("MsgType").toString();
//        String MsgId = map.get("MsgId").toString();
        System.out.println("MsgType:" + msgType);
        //消息类型
        MessageType messageEnumType = MessageType.valueOf(MessageType.class, msgType.toUpperCase());
        switch (messageEnumType) {
            case TEXT:
                //处理文本消息
                responseMessage = handleTextMessage(map);
                break;
            case IMAGE:
                //处理图片消息
                responseMessage = handleImageMessage(map);
                break;
            case VOICE:
                //处理语音消息
                responseMessage = handleVoiceMessage(map);
                break;
            case VIDEO:
                //处理视频消息
                responseMessage = handleVideoMessage(map);
                break;
            case SHORTVIDEO:
                //处理小视频消息
                responseMessage = handleSmallVideoMessage(map);
                break;
            case LOCATION:
                //处理位置消息
                responseMessage = handleLocationMessage(map);
                break;
            case LINK:
                //处理链接消息
                responseMessage = handleLinkMessage(map);
                break;
            case EVENT:
                //处理事件消息,用户在关注与取消关注公众号时，微信会向我们的公众号服务器发送事件消息,开发者接收到事件消息后就可以给用户下发欢迎消息
                responseMessage = handleEventMessage(map);
            default:
                break;
        }
        //返回响应消息
        return responseMessage;
    }

    /**
     * 接收到文本消息后处理
     * @param map 封装了解析结果的Map
     * @return
     */
    private String handleTextMessage(Map<String, String> map) {
        //响应消息
        String responseMessage="";
        // 消息内容
        String content = map.get("Content");
        String fromUserName = map.get("FromUserName");//用户openId
        GzhUser gzhUser = new GzhUser();
        gzhUser.setOpenId(fromUserName);
        List<GzhUser> list = gzhUserDao.findGzhUser(gzhUser);
        if(list.size()==0){

            String msgText = "未查询到对应业务";
            responseMessage = buildTextMessage(map,msgText);
        }else {
            try{
                int i = Integer.parseInt(content);
                if(i==1){
                    gzhUser.setType(0);
                    list = gzhUserDao.findGzhUser(gzhUser);
                    if(list.size()==0){
                        String msgText = "未查询到对应业务";
                        responseMessage = buildTextMessage(map,msgText);
                    }else {
                        Map map1 = new HashMap();
                        map1.put("openId",fromUserName);
                        map1.put("keyword",content);
                        map1.put("type",0);
                        responseMessage = buildNewsMessage(map,map1);
                    }
                }else if(i==2){
                    gzhUser.setType(1);
                    list = gzhUserDao.findGzhUser(gzhUser);
                    if(list.size()==0){
                        String msgText = "未查询到对应业务";
                        responseMessage = buildTextMessage(map,msgText);
                    }else {
                        Map map1 = new HashMap();
                        map1.put("openId",fromUserName);
                        map1.put("keyword",content);
                        map1.put("type",1);
                        responseMessage = buildNewsMessage(map,map1);
                    }
                }else {
                    String msgText = "未查询到对应业务";
                    responseMessage = buildTextMessage(map,msgText);
                }
            }catch (Exception e){
                String msgText = "未查询到对应业务";
                responseMessage = buildTextMessage(map,msgText);
            }

//            gzhUser = list.get(0);
//            if(gzhUser.getOpenId()==null || gzhUser.getOpenId().length()==0){
//                gzhUser.setOpenId(fromUserName);
//                gzhUserDao.updateGzhUser(gzhUser);
//                Map map1 = new HashMap();
//                map1.put("openId",fromUserName);
//                map1.put("keyword",content);
//                responseMessage = buildNewsMessage(map,map1);
//            }else {
//                System.out.println(1111111);
//                System.out.println(gzhUser.getOpenId());
//                System.out.println(content);
//                if(gzhUser.getOpenId().equals(fromUserName)){
//                    Map map1 = new HashMap();
//                    map1.put("openId",fromUserName);
//                    map1.put("keyword",content);
//                    responseMessage = buildNewsMessage(map,map1);
//                }else {
//                    String msgText = "该关键字已被绑定，如有问题请联系管理员";
//                    responseMessage = buildTextMessage(map,msgText);
//                }
//            }
        }
//        gzhUser = gzhUserDao.findGzhUser(gzhUser).get(0);

//        TOrg tOrg = new TOrg();
//        tOrg.setName(content);
//        TOrg tOrg1 = qxzDao.findOrgByName(tOrg);
//        if(tOrg1 ==null){
//            String msgText = "未查询到该小区的气象站信息";
//            responseMessage = buildTextMessage(map, msgText);
//        }else {
//            responseMessage = buildNewsMessage(map,tOrg1.getName());
//        }
//        switch (content) {
//
//            case "文本":
//                String msgText = "孤傲苍狼又要开始写博客总结了,欢迎朋友们访问我在博客园上面写的博客\n" +
//                        "<a href='http://www.cnblogs.com/xdp-gacl'></a>孤傲苍狼的博客";
//                responseMessage = buildTextMessage(map, msgText);
//                break;
//            case "图片":
//                //通过素材管理接口上传图片时得到的media_id
//                String imgMediaId = "cjKj5Qv_91t9m_0hXwJAZjMMS1_PTKlsFIIsuGeXqis5VssUwSCf391dR2Wue23f";
//                responseMessage = buildImageMessage(map, imgMediaId);
//                break;
//            case "语音":
//                //通过素材管理接口上传语音文件时得到的media_id
//                String voiceMediaId = "hiWJwkkf0EbhXTzDuZkW_PB-LkZdmv_bXyDY4l_NocIQlDEKoCJ1YGIR6TjFNF8O";
//                responseMessage = buildVoiceMessage(map,voiceMediaId);
//                break;
//            case "图文":
//                responseMessage = buildNewsMessage(map);
//                break;
//            case "音乐":
//                Music music = new Music();
//                music.title = "赵丽颖、许志安 - 乱世俱灭";
//                music.description = "电视剧《蜀山战纪》插曲";
//                music.musicUrl = "http://gacl.ngrok.natapp.cn/media/music/music.mp3";
//                music.hqMusicUrl = "http://gacl.ngrok.natapp.cn/media/music/music.mp3";
//                responseMessage = buildMusicMessage(map, music);
//                break;
//            case "视频":
//                Video video = new Video();
//                video.mediaId = "XvTZr2W-uFe9jlNCHD-4MISoV55C956HFayrNUjcxCoG768XDkb0UgOOItiyBVtg";
//                video.title = "小苹果";
//                video.description = "小苹果搞笑视频";
//                responseMessage = buildVideoMessage(map, video);
//                break;
//            default:
//                responseMessage = buildWelcomeTextMessage(map);
//                break;
//
//        }
        //返回响应消息
        return responseMessage;
    }

    /**
     * 生成消息创建时间 （整型）
     * @return 消息创建时间
     */
    private static String getMessageCreateTime() {
        Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时间
        DateFormat df = new SimpleDateFormat("yyyyMMddhhmm");// 设置显示格式
        String nowTime = df.format(dt);
        long dd = (long) 0;
        try {
            dd = df.parse(nowTime).getTime();
        } catch (Exception e) {

        }
        return String.valueOf(dd);
    }


    /**
     * 构建提示消息
     * @param map 封装了解析结果的Map
     * @return responseMessageXml
     */
    private static String buildWelcomeTextMessage(Map<String, String> map) {
        String responseMessageXml;
        String fromUserName = map.get("FromUserName");
        // 开发者微信号
        String toUserName = map.get("ToUserName");
        responseMessageXml = String
                .format(
                        "<xml>" +
                                "<ToUserName><![CDATA[%s]]></ToUserName>" +
                                "<FromUserName><![CDATA[%s]]></FromUserName>" +
                                "<CreateTime>%s</CreateTime>" +
                                "<MsgType><![CDATA[text]]></MsgType>" +
                                "<Content><![CDATA[%s]]></Content>" +
                                "</xml>",
                        fromUserName, toUserName, getMessageCreateTime(),
                        "欢迎关注川大公众号~\n在这您可以第一时间收到业务提醒\n输入关键字进入名下业务");
//                        "感谢您关注我的个人公众号，请回复如下关键词来使用公众号提供的服务：\n文本\n图片\n语音\n视频\n音乐\n图文");
        return responseMessageXml;
    }

    /**
     * 构造文本消息
     * @param map 封装了解析结果的Map
     * @param content 文本消息内容
     * @return 文本消息XML字符串
     */
    private static String buildTextMessage(Map<String, String> map, String content) {
        //发送方帐号
        String fromUserName = map.get("FromUserName");
        // 开发者微信号
        String toUserName = map.get("ToUserName");
        /**
         * 文本消息XML数据格式
         * <xml>
         <ToUserName><![CDATA[toUser]]></ToUserName>
         <FromUserName><![CDATA[fromUser]]></FromUserName>
         <CreateTime>1348831860</CreateTime>
         <MsgType><![CDATA[text]]></MsgType>
         <Content><![CDATA[this is a test]]></Content>
         <MsgId>1234567890123456</MsgId>
         </xml>
         */
        return String.format(
                "<xml>" +
                        "<ToUserName><![CDATA[%s]]></ToUserName>" +
                        "<FromUserName><![CDATA[%s]]></FromUserName>" +
                        "<CreateTime>%s</CreateTime>" +
                        "<MsgType><![CDATA[text]]></MsgType>" +
                        "<Content>%s</Content>" +
                        "</xml>",
                fromUserName, toUserName, getMessageCreateTime(), content);
    }

    /**
     * 构造图片消息
     * @param map 封装了解析结果的Map
     * @param mediaId 通过素材管理接口上传多媒体文件得到的id
     * @return 图片消息XML字符串
     */
    private static String buildImageMessage(Map<String, String> map, String mediaId) {
        //发送方帐号
        String fromUserName = map.get("FromUserName");
        // 开发者微信号
        String toUserName = map.get("ToUserName");
        String MsgId = map.get("MsgId");
        /**
         * 图片消息XML数据格式
         *<xml>
         <ToUserName><![CDATA[toUser]]></ToUserName>
         <FromUserName><![CDATA[fromUser]]></FromUserName>
         <CreateTime>12345678</CreateTime>
         <MsgType><![CDATA[image]]></MsgType>
         <Image>
         <MediaId><![CDATA[media_id]]></MediaId>
         </Image>
         </xml>
         */
//        String Content =  "<img src=\"http://132.232.105.167/media/image/%E6%88%91.jpg\">";
        String PicUrl="http://132.232.105.167/media/image/%E6%88%91.jpg";
        return String.format(
                "<xml>"+
       "<ToUserName><![CDATA[%s]]></ToUserName>"+
       "<FromUserName><![CDATA[%s]]></FromUserName>"+
       "<CreateTime>%s</CreateTime>"+
       "<MsgType><![CDATA[image]]></MsgType>"+
        "<Image>" +
       "<MediaId><![CDATA[%s]]></MediaId>"+
        "</Image>" +
"</xml>",
                fromUserName, toUserName, getMessageCreateTime(),mediaId);
    }

    /**
     * 构造音乐消息
     * @param map 封装了解析结果的Map
     * @param music 封装好的音乐消息内容
     * @return 音乐消息XML字符串
     */
    private static String buildMusicMessage(Map<String, String> map, Music music) {
        //发送方帐号
        String fromUserName = map.get("FromUserName");
        // 开发者微信号
        String toUserName = map.get("ToUserName");
        /**
         * 音乐消息XML数据格式
         *<xml>
         <ToUserName><![CDATA[toUser]]></ToUserName>
         <FromUserName><![CDATA[fromUser]]></FromUserName>
         <CreateTime>12345678</CreateTime>
         <MsgType><![CDATA[music]]></MsgType>
         <Music>
         <Title><![CDATA[TITLE]]></Title>
         <Description><![CDATA[DESCRIPTION]]></Description>
         <MusicUrl><![CDATA[MUSIC_Url]]></MusicUrl>
         <HQMusicUrl><![CDATA[HQ_MUSIC_Url]]></HQMusicUrl>
         <ThumbMediaId><![CDATA[media_id]]></ThumbMediaId>
         </Music>
         </xml>
         */
        return String.format(
                "<xml>" +
                        "<ToUserName><![CDATA[%s]]></ToUserName>" +
                        "<FromUserName><![CDATA[%s]]></FromUserName>" +
                        "<CreateTime>%s</CreateTime>" +
                        "<MsgType><![CDATA[music]]></MsgType>" +
                        "<Music>" +
                        "   <Title><![CDATA[%s]]></Title>" +
                        "   <Description><![CDATA[%s]]></Description>" +
                        "   <MusicUrl><![CDATA[%s]]></MusicUrl>" +
                        "   <HQMusicUrl><![CDATA[%s]]></HQMusicUrl>" +
                        "</Music>" +
                        "</xml>",
                fromUserName, toUserName, getMessageCreateTime(), music.title, music.description, music.musicUrl, music.hqMusicUrl);
    }

    /**
     * 构造视频消息
     * @param map 封装了解析结果的Map
     * @param video 封装好的视频消息内容
     * @return 视频消息XML字符串
     */
    private static String buildVideoMessage(Map<String, String> map, Video video) {
        //发送方帐号
        String fromUserName = map.get("FromUserName");
        // 开发者微信号
        String toUserName = map.get("ToUserName");
        /**
         * 音乐消息XML数据格式
         *<xml>
         <ToUserName><![CDATA[toUser]]></ToUserName>
         <FromUserName><![CDATA[fromUser]]></FromUserName>
         <CreateTime>12345678</CreateTime>
         <MsgType><![CDATA[video]]></MsgType>
         <Video>
         <MediaId><![CDATA[media_id]]></MediaId>
         <Title><![CDATA[title]]></Title>
         <Description><![CDATA[description]]></Description>
         </Video>
         </xml>
         */
        return String.format(
                "<xml>" +
                        "<ToUserName><![CDATA[%s]]></ToUserName>" +
                        "<FromUserName><![CDATA[%s]]></FromUserName>" +
                        "<CreateTime>%s</CreateTime>" +
                        "<MsgType><![CDATA[video]]></MsgType>" +
                        "<Video>" +
                        "   <MediaId><![CDATA[%s]]></MediaId>" +
                        "   <Title><![CDATA[%s]]></Title>" +
                        "   <Description><![CDATA[%s]]></Description>" +
                        "</Video>" +
                        "</xml>",
                fromUserName, toUserName, getMessageCreateTime(), video.mediaId, video.title, video.description);
    }

    /**
     * 构造语音消息
     * @param map 封装了解析结果的Map
     * @param mediaId 通过素材管理接口上传多媒体文件得到的id
     * @return 语音消息XML字符串
     */
    private static String buildVoiceMessage(Map<String, String> map, String mediaId) {
        //发送方帐号
        String fromUserName = map.get("FromUserName");
        // 开发者微信号
        String toUserName = map.get("ToUserName");
        /**
         * 语音消息XML数据格式
         *<xml>
         <ToUserName><![CDATA[toUser]]></ToUserName>
         <FromUserName><![CDATA[fromUser]]></FromUserName>
         <CreateTime>12345678</CreateTime>
         <MsgType><![CDATA[voice]]></MsgType>
         <Voice>
         <MediaId><![CDATA[media_id]]></MediaId>
         </Voice>
         </xml>
         */
        return String.format(
                "<xml>" +
                        "<ToUserName><![CDATA[%s]]></ToUserName>" +
                        "<FromUserName><![CDATA[%s]]></FromUserName>" +
                        "<CreateTime>%s</CreateTime>" +
                        "<MsgType><![CDATA[voice]]></MsgType>" +
                        "<Voice>" +
                        "   <MediaId><![CDATA[%s]]></MediaId>" +
                        "</Voice>" +
                        "</xml>",
                fromUserName, toUserName, getMessageCreateTime(), mediaId);
    }

    /**
     * 构造图文消息
     * @param map 封装了解析结果的Map
     * @return 图文消息XML字符串
     */
    private static String buildNewsMessage(Map<String, String> map, Map map2) {
        int type = (int) map2.get("type");
        String content = "";
        if(type==0){
            String fromUserName = map.get("FromUserName");
            // 开发者微信号
            String toUserName = map.get("ToUserName");
            String openId = String.valueOf(map2.get("openId"));
            String keyword = String.valueOf(map2.get("keyword"));
            NewsItem item = new NewsItem();
            item.Title = "小区设备数据详情";
            item.Description = "";
            String path = PropertiesUtil.getProperty("localPath");
            System.out.println("path:"+path);
            item.PicUrl = path+"/static_modules/gzh/plots.png";
            item.Url = path+"/page.html?openId="+openId+"&keyword="+keyword+"&type=0";
//            Map map1 = new HashMap();
//            map1.put("appid","xiaochengxuappid12345");
//            map1.put("pagepath","index?openId="+openId+"&keyword="+keyword);
//            item.Miniprogram = map1;
            System.out.println("url:"+ item.Url);
            String itemContent1 = buildSingleItem(item);
            content = String.format("<xml>\n" +
                    "<ToUserName><![CDATA[%s]]></ToUserName>\n" +
                    "<FromUserName><![CDATA[%s]]></FromUserName>\n" +
                    "<CreateTime>%s</CreateTime>\n" +
                    "<MsgType><![CDATA[news]]></MsgType>\n" +
                    "<ArticleCount>%s</ArticleCount>\n" +
                    "<Articles>\n" + "%s" +
                    "</Articles>\n" +
                    "</xml> ", fromUserName, toUserName, getMessageCreateTime(), 1, itemContent1);
        }else if(type ==1){
            String fromUserName = map.get("FromUserName");
            // 开发者微信号
            String toUserName = map.get("ToUserName");
            String openId = String.valueOf(map2.get("openId"));
            String keyword = String.valueOf(map2.get("keyword"));
            System.out.println("openId:"+openId);
            System.out.println("keyword:"+keyword);
            NewsItem item = new NewsItem();
            item.Title = "小区设备数据详情";
            item.Description = "";
            String path = PropertiesUtil.getProperty("localPath");
            System.out.println("path:"+path);
            item.PicUrl = path+"/static_modules/gzh/plots.png";
            item.Url = path+"/page.html?openId="+openId+"&keyword="+keyword+"&type=0";
            System.out.println("url:"+ item.Url);
            String itemContent1 = buildSingleItem(item);
            content = String.format("<xml>\n" +
                    "<ToUserName><![CDATA[%s]]></ToUserName>\n" +
                    "<FromUserName><![CDATA[%s]]></FromUserName>\n" +
                    "<CreateTime>%s</CreateTime>\n" +
                    "<MsgType><![CDATA[news]]></MsgType>\n" +
                    "<ArticleCount>%s</ArticleCount>\n" +
                    "<Articles>\n" + "%s" +
                    "</Articles>\n" +
                    "</xml> ", fromUserName, toUserName, getMessageCreateTime(), 1, itemContent1);
        }else if(type ==2){
            String fromUserName = map.get("FromUserName");
            // 开发者微信号
            String toUserName = map.get("ToUserName");
            String openId = String.valueOf(map2.get("openId"));
            NewsItem item = new NewsItem();
            item.Title = "工单详情查看";
            item.Description = "";
            String path = PropertiesUtil.getProperty("localPath");
            System.out.println("path:"+path);
            item.PicUrl = path+"/static_modules/gzh/plots.png";
//            item.Url = path+"/page.html?openId="+openId+"&keyword="+keyword+"&type=0";
            Map map1 = new HashMap();
            map1.put("appid","xiaochengxuappid12345");
            map1.put("pagepath","index?openId="+openId);
            item.Miniprogram = map1;
            System.out.println("url:"+ item.Url);
            String itemContent1 = buildSingleItem(item);
            content = String.format("<xml>\n" +
                    "<ToUserName><![CDATA[%s]]></ToUserName>\n" +
                    "<FromUserName><![CDATA[%s]]></FromUserName>\n" +
                    "<CreateTime>%s</CreateTime>\n" +
                    "<MsgType><![CDATA[news]]></MsgType>\n" +
                    "<ArticleCount>%s</ArticleCount>\n" +
                    "<Articles>\n" + "%s" +
                    "</Articles>\n" +
                    "</xml> ", fromUserName, toUserName, getMessageCreateTime(), 1, itemContent1);
        }else if(type ==3){
            String fromUserName = map.get("FromUserName");
            // 开发者微信号
            String toUserName = map.get("ToUserName");
            String openId = String.valueOf(map2.get("openId"));
            System.out.println("openId:"+openId);
            NewsItem item = new NewsItem();
            item.Title = "点击进入绑定页";
            item.Description = "";
            String path = PropertiesUtil.getProperty("localPath");
            System.out.println("path:"+path);
            item.PicUrl = path+"/static_modules/gzh/plots.png";
            item.Url = path+"/bingding.html?openId="+openId;
            System.out.println("url:"+ item.Url);
            String itemContent1 = buildSingleItem(item);
            content = String.format("<xml>\n" +
                    "<ToUserName><![CDATA[%s]]></ToUserName>\n" +
                    "<FromUserName><![CDATA[%s]]></FromUserName>\n" +
                    "<CreateTime>%s</CreateTime>\n" +
                    "<MsgType><![CDATA[news]]></MsgType>\n" +
                    "<ArticleCount>%s</ArticleCount>\n" +
                    "<Articles>\n" + "%s" +
                    "</Articles>\n" +
                    "</xml> ", fromUserName, toUserName, getMessageCreateTime(), 1, itemContent1);
        }

        return content;

    }

    /**
     * 生成图文消息的一条记录
     *
     * @param item
     * @return
     */
    private static String buildSingleItem(NewsItem item) {
        String itemContent = String.format("<item>\n" +
                "<Title><![CDATA[%s]]></Title> \n" +
                "<Description><![CDATA[%s]]></Description>\n" +
                "<PicUrl><![CDATA[%s]]></PicUrl>\n" +
                "<Url><![CDATA[%s]]></Url>\n" +
                "</item>", item.Title, item.Description, item.PicUrl, item.Url);
        return itemContent;
    }


    /**
     * 处理接收到图片消息
     *
     * @param map
     * @return
     */
    private static String handleImageMessage(Map<String, String> map) {
        String picUrl = map.get("PicUrl");
        String mediaId = map.get("MediaId");
        System.out.print("picUrl:" + picUrl);
        System.out.print("mediaId:" + mediaId);
        String result = String.format("已收到您发来的图片，图片Url为：%s\n图片素材Id为：%s", picUrl, mediaId);
        return buildTextMessage(map, result);
    }

    /**
     * 处理接收到语音消息
     * @param map
     * @return
     */
    private static String handleVoiceMessage(Map<String, String> map) {
        String format = map.get("Format");
        String mediaId = map.get("MediaId");
        System.out.print("format:" + format);
        System.out.print("mediaId:" + mediaId);
        String result = String.format("已收到发来的语音，语音格式为：%s\n语音素材Id为：%s", format, mediaId);
        return buildTextMessage(map, result);
    }

    /**
     * 处理接收到的视频消息
     * @param map
     * @return
     */
    private static String handleVideoMessage(Map<String, String> map) {
        String thumbMediaId = map.get("ThumbMediaId");
        String mediaId = map.get("MediaId");
        System.out.print("thumbMediaId:" + thumbMediaId);
        System.out.print("mediaId:" + mediaId);
        String result = String.format("已收到您发来的视频，视频中的素材ID为：%s\n视频Id为：%s", thumbMediaId, mediaId);
        return buildTextMessage(map, result);
    }

    /**
     * 处理接收到的小视频消息
     * @param map
     * @return
     */
    private static String handleSmallVideoMessage(Map<String, String> map) {
        String thumbMediaId = map.get("ThumbMediaId");
        String mediaId = map.get("MediaId");
        System.out.print("thumbMediaId:" + thumbMediaId);
        System.out.print("mediaId:" + mediaId);
        String result = String.format("已收到您发来的小视频，小视频中素材ID为：%s,\n小视频Id为：%s", thumbMediaId, mediaId);
        return buildTextMessage(map, result);
    }

    /**
     * 处理接收到的地理位置消息
     * @param map
     * @return
     */
    private static String handleLocationMessage(Map<String, String> map) {
        String latitude = map.get("Location_X");  //纬度
        String longitude = map.get("Location_Y");  //经度
        String label = map.get("Label");  //地理位置精度
        String result = String.format("纬度：%s\n经度：%s\n地理位置：%s", latitude, longitude, label);
        return buildTextMessage(map, result);
    }

    /**
     * 处理接收到的链接消息
     * @param map
     * @return
     */
    private static String handleLinkMessage(Map<String, String> map) {
        String title = map.get("Title");
        String description = map.get("Description");
        String url = map.get("Url");
        String result = String.format("已收到您发来的链接，链接标题为：%s,\n描述为：%s\n,链接地址为：%s", title, description, url);
        return buildTextMessage(map, result);
    }

    /**
     * 处理消息Message
     * @param map 封装了解析结果的Map
     * @return
     */
    private String handleEventMessage(Map<String, String> map) {
        String responseMessage = "";
        System.out.println("EventKey:"+map.get("EventKey"));
        if(String.valueOf(map.get("EventKey")).equals("devAlarm")){
            GzhUser gzhUser = new GzhUser();
            String openId = map.get("FromUserName");
            gzhUser.setOpenId(openId);
//            gzhUser = gzhUserDao.findGzhUser(gzhUser);
            List<GzhUser> list = gzhUserDao.findGzhUser(gzhUser);
            if(list.size()==0){
                String msgText = "未查询到对应业务";
                responseMessage = buildTextMessage(map,msgText);
            }else if(list.size()==1) {
                gzhUser = list.get(0);
                Map map1 = new HashMap();
                map1.put("openId",openId);
                map1.put("keyword",gzhUser.getKeyword());
                if(gzhUser.getType()==1){
                    map1.put("type",1);

                }else {
                    map1.put("type",0);
                }
                responseMessage = buildNewsMessage(map,map1);
            }else {
                String msgText = "系统检测到您名下有多种项目形态\n\n回复数字编号进入对应项目形态:\n1.小程序形态\n2.公众号形态";
                responseMessage = buildTextMessage(map,msgText);
            }
            return responseMessage;
        }else if(String.valueOf(map.get("EventKey")).equals("scheduling")){
            String openId = map.get("FromUserName");
            PdfBind pdfBind = new PdfBind();
            pdfBind.setOpenId(openId);
            List<PdfBind> list = pdfBindDao.findBind(pdfBind);
            if(list.size()==0){
                String msgText = "未查询到对应业务";
                responseMessage = buildTextMessage(map,msgText);
            }else {
                Map map1 = new HashMap();
                map1.put("openId",openId);
                map1.put("type",2);
                responseMessage = buildNewsMessage(map,map1);
            }
            return responseMessage;
        }else if(String.valueOf(map.get("EventKey")).equals("compment")){
            String openId = map.get("FromUserName");
            TemplateUtil.sendCompment(openId);
            responseMessage = "success";
            return responseMessage;
        }else if(String.valueOf(map.get("EventKey")).equals("newProject")){
            String openId = map.get("FromUserName");
            Map map1 = new HashMap();
            map1.put("openId",openId);
            map1.put("type",3);
            responseMessage = buildNewsMessage(map,map1);
            return responseMessage;
        }
        responseMessage = buildWelcomeTextMessage(map);
        return responseMessage;
    }

}

/**
 * 图文消息
 */
class NewsItem {
    public String Title;

    public String Description;

    public String PicUrl;

    public String Url;

    public Map Miniprogram;
}

/**
 * 音乐消息
 */
class Music {
    public String title;
    public String description;
    public String musicUrl;
    public String hqMusicUrl;
}

/**
 * 视频消息
 */
class Video {
    public String title;
    public String description;
    public String mediaId;
}
