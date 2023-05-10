package cn.cdsoft.modules.warm.web;

import cn.cdsoft.common.utils.SpringContextHolder;
import cn.cdsoft.common.web.BaseController;
import cn.cdsoft.modules.qxz.entity.GzhUser;
import cn.cdsoft.modules.sys.entity.User;
import cn.cdsoft.modules.sys.service.SystemService;
import cn.cdsoft.modules.warm.entity.PdfBind;
import cn.cdsoft.modules.warm.entity.PdfOrder;
import cn.cdsoft.modules.warm.service.PdfBindService;
import cn.cdsoft.modules.warm.service.PdfOrderService;
import cn.cdsoft.modules.websocket.MyWebSocketHandler;
import com.alibaba.fastjson.JSONObject;
import cn.cdsoft.modules.qxz.service.GzhUserService;
import cn.cdsoft.modules.warm.entity.PdfOrderDeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZZUSER on 2018/12/11.
 */
@Controller
@RequestMapping("/warm/wx")
public class WexinController extends BaseController {

    @Autowired
    PdfOrderService pdfOrderService;

    @Autowired
    PdfBindService pdfBindService;

    @Autowired
    GzhUserService gzhUserService;

    @Autowired
    MyWebSocketHandler myWebSocketHandler;

    /**
     * 工单项目账号绑定
     * @param name
     * @param password
     * @param openId
     * @return
     */
    @RequestMapping("/bingding")
    @ResponseBody
    public JSONObject login(String name, String password, String openId){
        JSONObject jsonObject = new JSONObject();
        User user = new User();
        user = getSystemService().getUserByLoginName(name);
        if(user ==null){
            jsonObject.put("success",false);
            jsonObject.put("msg","账号或密码错误");
            return jsonObject;
        }
        boolean b = SystemService.validatePassword(password,user.getPassword());
        if(b){
            PdfBind pdfBind = new PdfBind();
            pdfBind.setOpenId(openId);
            pdfBind.setUserId(user.getId());
            pdfBindService.addBind(pdfBind);
            jsonObject.put("success",true);
            jsonObject.put("msg","绑定成功");
        }else {
            jsonObject.put("success",false);
            jsonObject.put("msg","账号或密码错误");
        }
        return jsonObject;
//        if(b){
//            PdfBind pdfBind = new PdfBind();
//            pdfBind.setOpenId(openId);
//            pdfBind.setUserId(user.getId());
//            PdfBind pdfBind1 = pdfBindService.findBind(pdfBind);
//            if(pdfBind1 ==null){
//                pdfBindService.addBind(pdfBind);
//            }else {
//                pdfBind.setId(pdfBind1.getId());
//                pdfBindService.updateBind(pdfBind);
//            }
//            JedisUtils.setObject("token"+name,name,3600);
//            jsonObject.put("success",true);
//            jsonObject.put("msg","登录成功");
//            return jsonObject;
//        }else {
//            jsonObject.put("success",false);
//            jsonObject.put("msg","登录失败");
//            return jsonObject;
//        }
    }
    private SystemService systemService;

    /**
     * 设备故障报警项目绑定关键字
     * @param keyword
     * @param openId
     * @return
     */
    @RequestMapping("/bingdAlarm")
    @ResponseBody
    public JSONObject bingdAlarm(String keyword, String openId){
        JSONObject jsonObject = new JSONObject();
        GzhUser gzhUser = new GzhUser();
        gzhUser.setKeyword(keyword);
        List<GzhUser> list = gzhUserService.findGzhUser(gzhUser);
        gzhUser.setOpenId(openId);
        gzhUser.setKeyword(null);
        List<GzhUser> list1 = gzhUserService.findGzhUser(gzhUser);
        if(list.size()==0){
            jsonObject.put("success",false);
            jsonObject.put("msg","请输入正确关键字");
            jsonObject.put("type",1);
        }else {
            GzhUser gzhUser1 = list.get(0);
            if(gzhUser1.getOpenId()==null || gzhUser1.getOpenId().length()==0){
                if(list1.size()==0){
                    gzhUser1.setOpenId(openId);
                    gzhUserService.updateUser(gzhUser1);
                    jsonObject.put("success",true);
                    jsonObject.put("msg","绑定成功");
                    jsonObject.put("type",3);
                }else {
                    if(list1.size()==1){
                        if(list1.get(0).getType()==list.get(0).getType()){
                            jsonObject.put("success",false);
                            jsonObject.put("msg","您已绑定过关键字");
                            jsonObject.put("type",2);
                        }else {
                            gzhUser1.setOpenId(openId);
                            gzhUserService.updateUser(gzhUser1);
                            jsonObject.put("success",true);
                            jsonObject.put("msg","绑定成功");
                            jsonObject.put("type",3);
                        }
                    }else {
                        jsonObject.put("success",false);
                        jsonObject.put("msg","您已绑定过关键字");
                        jsonObject.put("type",2);
                    }
                }

            }else {
                if(gzhUser1.getOpenId().equals(openId)){
                    jsonObject.put("success",false);
                    jsonObject.put("msg","请勿重复绑定");
                    jsonObject.put("type",2);
                }else {
                    jsonObject.put("success",false);
                    jsonObject.put("msg","该关键字已被绑定");
                    jsonObject.put("type",2);
                }
            }
        }
        return jsonObject;
//        if(gzhUser.getOpenId()==null || gzhUser.getOpenId().length()==0){
//            gzhUser.setOpenId(fromUserName);
//            gzhUserDao.updateGzhUser(gzhUser);
//            Map map1 = new HashMap();
//            map1.put("openId",fromUserName);
//            map1.put("keyword",content);
//            responseMessage = buildNewsMessage(map,map1);
//        }else {
//            System.out.println(1111111);
//            System.out.println(gzhUser.getOpenId());
//            System.out.println(content);
//            if(gzhUser.getOpenId().equals(fromUserName)){
//                Map map1 = new HashMap();
//                map1.put("openId",fromUserName);
//                map1.put("keyword",content);
//                responseMessage = buildNewsMessage(map,map1);
//            }else {
//                String msgText = "该关键字已被绑定，如有问题请联系管理员";
//                responseMessage = buildTextMessage(map,msgText);
//            }
//        }
    }


    /**
     * 获取系统业务对象
     */
    public SystemService getSystemService() {
        if (systemService == null){
            systemService = SpringContextHolder.getBean(SystemService.class);
        }
        return systemService;
    }

    /**
     * 小程序端获取首页数据
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @RequestMapping("/getData")
    @ResponseBody
    public JSONObject getData(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JSONObject jsonObject =new JSONObject();
        String token = httpServletRequest.getParameter("token");
        String state = httpServletRequest.getParameter("state");
        String orgId = httpServletRequest.getParameter("orgId");
        PdfBind pdfBind = new PdfBind();
        pdfBind.setOpenId(token);
        List<PdfBind> list= pdfBindService.findBind(pdfBind);
        String ids = "";
        for(int i=0;i<list.size();i++){
            if(i==0){
                ids = list.get(i).getUserId();
            }else {
                ids = ids +","+list.get(i).getUserId();
            }
        }
        String[] arr = ids.split(",");
        List<Map> list1 = pdfBindService.getUserProject(arr);
        String id = "";
        String name = "";
        if(orgId ==null || orgId.length()==0){
            id = String.valueOf(list1.get(0).get("id"));
            name = String.valueOf(list1.get(0).get("name"));
            orgId = String.valueOf(list1.get(0).get("orgId"));
        }else {
            for(int i = 0;i<list1.size();i++){
                String orgId1 = String.valueOf(list1.get(i).get("orgId"));
                if(orgId1.equals(orgId)){
                    id = String.valueOf(list1.get(i).get("id"));
                    name = String.valueOf(list1.get(i).get("name"));
                }
            }
        }
        try{
            Map map = pdfOrderService.getWxData(id,name,orgId,state);
            Map resultMap = new HashMap();
            resultMap.put("list",list1);
            resultMap.put("data",map);
            jsonObject.put("data",resultMap);
            jsonObject.put("success",true);

        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e);
            e.printStackTrace();
        }
        return jsonObject;
    }


    /**
     * 接单
     * @param pdfOrder
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("/recieveOrder")
    @ResponseBody
    public JSONObject recieveOrder(PdfOrder pdfOrder, HttpServletRequest httpServletRequest){
        JSONObject jsonObject = new JSONObject();
        String token = httpServletRequest.getParameter("principal");
//        User user = getSystemService().getUserByLoginName(token);
//        pdfOrder.setConfirmUser(token);
        try {
            pdfOrderService.recieveOrder(pdfOrder);
            jsonObject.put("success",true);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e);
            e.printStackTrace();
        }
        return jsonObject;
    }


    /**
     * 点击处理按钮
     * @param pdfOrder
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("/dealOrder")
    @ResponseBody
    public JSONObject dealOrder(PdfOrder pdfOrder, HttpServletRequest httpServletRequest){
        JSONObject jsonObject = new JSONObject();
//        String token = httpServletRequest.getParameter("userId");
//        User user = getSystemService().getUserByLoginName(token);
//        pdfOrder.setConfirmUser(token);
        try {
            pdfOrderService.dealOrder(pdfOrder);
            jsonObject.put("success",true);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e);
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 维修人员提交工单处理
     * @param pdfOrderDeal
     */
    @RequestMapping("/submitOrder")
    @ResponseBody
    public JSONObject submitOrder(PdfOrderDeal pdfOrderDeal, HttpServletRequest httpServletRequest){
        JSONObject jsonObject = new JSONObject();
//        String token = httpServletRequest.getParameter("userId");
//        User user = getSystemService().getUserByLoginName(token);
//        pdfOrderDeal.setSendUser(token);
        try {
            pdfOrderService.submitOrder(pdfOrderDeal);
            jsonObject.put("success",true);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e);
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 维修人员获取工单处理记录
     * @param pdfOrder
     */
    @RequestMapping("/getOrderDeal")
    @ResponseBody
    public JSONObject getOrderDeal(PdfOrder pdfOrder, HttpServletRequest httpServletRequest){
        JSONObject jsonObject = new JSONObject();
//        String token = httpServletRequest.getParameter("token");
//        User user = getSystemService().getUserByLoginName(token);
        try {
//            pdfOrder.setPrincipal(user.getId());
            List<Map> list = pdfOrderService.getOrderDeal(pdfOrder);
            jsonObject.put("success",true);
            jsonObject.put("data",list);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e);
            e.printStackTrace();
        }
        return jsonObject;
    }

    @RequestMapping("/addOrderByWx")
    @ResponseBody
    public JSONObject addOrderByWx(PdfOrder pdfOrder){
        JSONObject jsonObject = new JSONObject();
        try{
            pdfOrderService.addOrderByWx(pdfOrder);
            String buildingId = pdfOrderService.getBuildByDev(pdfOrder.getDevId());
            TextMessage textMessage = new TextMessage(buildingId);
            List<JSONObject> list = pdfOrderService.getUserByDev(pdfOrder.getDevId());
            for(int i=0;i<=list.size();i++){
                myWebSocketHandler.sendMessageToUser(String.valueOf(list.get(i).get("user_id")),textMessage);
            }
            jsonObject.put("success",true);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e.getMessage());
            e.printStackTrace();
        }
        return jsonObject;
    }
}
