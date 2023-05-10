package cn.cdsoft.modules.qxz.wx;

import cn.cdsoft.modules.qxz.wx.util.MessageHandlerUtil;
import cn.cdsoft.modules.qxz.wx.util.PropertiesUtil;
import cn.cdsoft.modules.qxz.wx.util.TemplateUtil;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZZUSER on 2018/10/25.
 */
@Controller
@RequestMapping("/weixin")
public class WeChatController  {
    @Value("${adminPath}")
    private String apiURL;
    private final String TOKEN = "linjinhua";
    @Autowired
    WeChatService wechatService;

    @Autowired
    MessageHandlerUtil messageHandlerUtil;
    @RequestMapping(value="/verify",method= RequestMethod.GET)
    public void index(HttpServletRequest request, HttpServletResponse response){
//        System.out.println("微信接入服务器");
//        String signature = request.getParameter("signature");
//        String timestamp = request.getParameter("timestamp");
//        String nonce = request.getParameter("nonce");
//        String token = "weixin";
//        String echostr = request.getParameter("echostr");
//        if (wechatService.verifyInfo(signature, timestamp, nonce, token)) {
//            System.out.println("echostr为:{}"+ echostr);
//            if (echostr != null) {
//                try {
//                    response.getWriter().write(echostr);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        } else {
//            System.out.println("signature为:{}"+ signature);
//            System.out.println("timestamp为:{}"+ timestamp);
//            System.out.println("nonce为:{}"+nonce);
//            System.out.println("token为:{}"+ token);
//        }
    }

    @RequestMapping(value="/testPost",method= RequestMethod.POST)
    public void postMethod(HttpServletResponse response,HttpServletRequest request) throws IOException {
        // TODO 接收、处理、响应由微信服务器转发的用户发送给公众帐号的消息
        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        System.out.println("请求进入");
        System.out.println(apiURL);
        String responseMessage;
        Map<String,String> map = new HashMap();
        try {
            //解析微信发来的请求,将解析后的结果封装成Map返回
            map = messageHandlerUtil.parseXml(request);
            System.out.println("开始构造响应消息");
            responseMessage = messageHandlerUtil.buildResponseMessage(map);
            System.out.println(responseMessage);
            if(responseMessage.equals("")){
                responseMessage ="未正确响应";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("发生异常："+ e.getMessage());
            responseMessage ="未正确响应";
        }
        //发送响应消息
        response.getWriter().println(responseMessage);
        if(map.get("Event")!=null && map.get("Event").equals("subscribe")){
            TemplateUtil.kefu(map.get("FromUserName"));
        }
    }



    @RequestMapping(value="/testPost",method= RequestMethod.GET)
    public void getMehtod(HttpServletResponse response,HttpServletRequest request) throws IOException {
        System.out.println("开始校验签名");
        /**
         * 接收微信服务器发送请求时传递过来的4个参数
         */
        String signature = request.getParameter("signature");//微信加密签名signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
        String timestamp = request.getParameter("timestamp");//时间戳
        String nonce = request.getParameter("nonce");//随机数
        String echostr = request.getParameter("echostr");//随机字符串
        //排序
        final String token  = PropertiesUtil.getProperty("Token");
        String sortString = sort(token, timestamp, nonce);
        //加密
        String mySignature = sha1(sortString);
        //校验签名,通过检验signature对请求进行校验，若校验成功则原样返回echostr,表示接入成功，否则接入失败
        if (mySignature != null && mySignature != "" && mySignature.equals(signature)) {
            System.out.println("签名校验通过。");
            //如果检验成功输出echostr，微信服务器接收到此输出，才会确认检验完成。
            //response.getWriter().println(echostr);
            response.getWriter().write(echostr);
        } else {
            System.out.println("签名校验失败.");
        }
    }

    @RequestMapping(value="/getToken")
    public void getToken(HttpServletResponse response1,HttpServletRequest request) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {              CookieStore cookieStore = new BasicCookieStore();
            HttpClientContext localContext = HttpClientContext.create();
            localContext.setCookieStore(cookieStore);
            HttpGet httpget = new HttpGet(
                    "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxd2529c6c2aa9234f&secret=14c680cb58f4b835589a21d1b8ff24e1");
            System.out.println("Executing request " + httpget.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httpget,                      localContext);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                InputStream is = response.getEntity().getContent();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is));
                String str = "";
                while ((str = br.readLine()) != null) {
                    System.out.println(str);
                }              }
            finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }



    /**
     * 排序方法
     *
     * @param token
     * @param timestamp
     * @param nonce
     * @return
     */
    public String sort(String token, String timestamp, String nonce) {
        String[] strArray = {token, timestamp, nonce};
        Arrays.sort(strArray);
        StringBuilder sb = new StringBuilder();
        for (String str : strArray) {
            sb.append(str);
        }

        return sb.toString();
    }

    /**
     * 将字符串进行sha1加密
     *
     * @param str 需要加密的字符串
     * @return 加密后的内容
     */
    public String sha1(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}

