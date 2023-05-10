package cn.cdsoft.modules.warm.web;

import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.common.utils.OrgUtil;
import cn.cdsoft.common.utils.ServletUtils;
import cn.cdsoft.common.web.BaseController;
import cn.cdsoft.modules.settings.entity.TOrg;
import cn.cdsoft.modules.warm.entity.PdfOrder;
import cn.cdsoft.modules.warm.service.PdfOrderService;
import cn.cdsoft.modules.websocket.MyWebSocketHandler;
import com.alibaba.fastjson.JSONObject;
import cn.cdsoft.modules.settings.entity.TCode;
import cn.cdsoft.modules.settings.service.TCodeService;
import cn.cdsoft.modules.warm.entity.PdfOrderDeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZZUSER on 2018/12/6.
 */
@Controller
@RequestMapping("${adminPath}/warm")
public class PdfOrderConroller extends BaseController {
    @Autowired
    PdfOrderService pdfOrderService;

    @Autowired
    TCodeService tCodeService;

    @Autowired
    MyWebSocketHandler myWebSocketHandler;

    @RequestMapping("/findOrder")
    @ResponseBody
    public JSONObject findOrder(PdfOrder pdfOrder, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        Page page = new Page(httpServletRequest,httpServletResponse);
        pdfOrder.setPage(page);
        JSONObject jsonObject = new JSONObject();
        try {
            System.out.println(1);
            Map map = pdfOrderService.findOrder(pdfOrder);
            jsonObject.put("data",map);
            jsonObject.put("success",true);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject;
    }
    
    
    
    /**
     * 根据id获取工单
     * @param id
     * @return
     */
    @RequestMapping("/findOrderById")
    @ResponseBody
    public JSONObject findOrderById(String id){
        JSONObject jsonObject = new JSONObject();
        try {
            Map map = pdfOrderService.findOrderById(id);
            jsonObject.put("data",map);
            jsonObject.put("success",true);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject;
    }

    @RequestMapping("/addOrder")
    @ResponseBody
    public JSONObject addOrder(PdfOrder pdfOrder, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        try {
            pdfOrderService.addOrder(pdfOrder);
            String buildingId = pdfOrderService.getBuildByDev(pdfOrder.getDevId());
             
            TextMessage textMessage = new TextMessage(buildingId);
            List<JSONObject> list = pdfOrderService.getUserByDev(pdfOrder.getDevId());
            for(int i=0;i<=list.size();i++){
                myWebSocketHandler.sendMessageToUser(String.valueOf(list.get(i).get("user_id")),textMessage);
            }
            return ServletUtils.buildJsonRs(true, "", null);
        }catch (Exception e){
            return ServletUtils.buildJsonRs(false, e.getMessage(), null);
        }
    }

    @RequestMapping("/deleteOrder")
    @ResponseBody
    public JSONObject deleteOrder(String ids, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JSONObject jsonObject = new JSONObject();
        try{
            pdfOrderService.deleteOrderByIds(ids);
            jsonObject.put("success",true);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 确认工单
     * @param ids
     */
    @RequestMapping("/confirmOrder")
    @ResponseBody
    public JSONObject confirmOrder(String ids, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JSONObject jsonObject = new JSONObject();
        try {
            pdfOrderService.confirmOrder(ids);
            jsonObject.put("success",true);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 派单
     * @param pdfOrder
     */
    @RequestMapping("/sendOrder")
    @ResponseBody
    public JSONObject sendOrder(PdfOrder pdfOrder , HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        pdfOrder.setSendOrderUser(OrgUtil.getUserId());
//        pdfOrder.setSendOrderUser("277802135813361664");
//        pdfOrder.setState(2);
        JSONObject jsonObject = new JSONObject();
        try{
            new String(pdfOrder.getSuggestion().getBytes("UTF-8"),"GBK");
            pdfOrderService.sendOrder(pdfOrder);
            jsonObject.put("success",true);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 接单
     * @param pdfOrder
     */
    @RequestMapping("/recieveOrder")
    @ResponseBody
    public JSONObject recieveOrder(PdfOrder pdfOrder){
        JSONObject jsonObject = new JSONObject();
        try{
            pdfOrderService.recieveOrder(pdfOrder);
            jsonObject.put("success",true);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 处理中
     * @param pdfOrder
     */
    @RequestMapping("/dealOrder")
    @ResponseBody
    public void dealOrder(PdfOrder pdfOrder){
        pdfOrderService.dealOrder(pdfOrder);
    }

    /**
     * 维修人员提交工单处理
     * @param pdfOrderDeal
     */
    @RequestMapping("/submitOrder")
    @ResponseBody
    public void submitOrder(PdfOrderDeal pdfOrderDeal){
        pdfOrderService.submitOrder(pdfOrderDeal);
    }

    /**
     * 维修人员提交工单处理
     * @param pdfOrderDeal
     */
    @RequestMapping("/getDealList")
    @ResponseBody
    public void getDealList(PdfOrderDeal pdfOrderDeal){
        pdfOrderService.getDealList(pdfOrderDeal.getId());
    }


    /**
     * 点击处理流程按钮
     * @param pdfOrder
     */
    @RequestMapping("/getOrderRecorders")
    @ResponseBody
    public JSONObject getOrderRecorders(PdfOrder pdfOrder){
        JSONObject jsonObject = new JSONObject();
        try {
            Map map = pdfOrderService.getOrderRecorders(pdfOrder);
            jsonObject.put("success",true);
            jsonObject.put("data",map);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 管理人员回复工单
     */
    @RequestMapping("/replyOrder")
    @ResponseBody
    public JSONObject replyOrder(PdfOrderDeal pdfOrderDeal){
        JSONObject jsonObject = new JSONObject();
        try {
            pdfOrderDeal.setSendUser(OrgUtil.getUserId());
//            pdfOrderDeal.setSendUser("277802135813361664");
            pdfOrderService.replyOrder(pdfOrderDeal);
            jsonObject.put("success",true);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 根据区域获取设备
     * @param orgId
     * @return
     */
    @RequestMapping("/getDevByOrg")
    @ResponseBody
    public JSONObject getDevByOrg(String orgId, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JSONObject jsonObject = new JSONObject();
        try {
            TOrg tOrg = new TOrg();
            tOrg.setId(orgId);
            List<Map> list = pdfOrderService.getDevByOrg(tOrg);
            jsonObject.put("success",true);
            jsonObject.put("data",list);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 获取设备类型列表
     */
    @RequestMapping("/getTypeList")
    @ResponseBody
    public JSONObject getTypeList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JSONObject jsonObject = new JSONObject();
        try {
            TCode tCode = new TCode();
            tCode.setTypeId(1l);
            tCode.setPage(null);
            List<TCode> list = tCodeService.findList(tCode);
            jsonObject.put("data",list);
            jsonObject.put("success",true);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e.getMessage());
        }

        return jsonObject;
    }

    /**
     * 获取报警人集合
     */
    @RequestMapping("/getSendUserList")
    @ResponseBody
    public JSONObject getSendUserList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JSONObject jsonObject = new JSONObject();
        try {
            String orgId = OrgUtil.getOrgId();
//            String orgId = "233993942926888973";
            List<Map> list =   pdfOrderService.getSendUserList(orgId);
            jsonObject.put("data",list);
            jsonObject.put("success",true);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject;
    }

    /**
     *  报警工单页头部 几个下拉控件初始化数据
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @RequestMapping("/initAlarmTop")
    @ResponseBody
    public JSONObject initAlarmTop(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JSONObject jsonObject = new JSONObject();
        try {
            String orgId = OrgUtil.getOrgId();
//            String orgId = "233993942926888973";
            List<Map> list = pdfOrderService.getSendUserList(orgId);

            TCode tCode = new TCode();
            tCode.setTypeId(1l);
            tCode.setPage(null);
            List<TCode> list1 = tCodeService.findList(tCode);

            Map resultMap = new HashMap();
            resultMap.put("typeList",list1);
            resultMap.put("sendUser",list);
            resultMap.put("period",list);
            jsonObject.put("data",resultMap);
            jsonObject.put("success",true);
        }catch (Exception e){
            jsonObject.put("success",true);
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 初始化添加报警弹框中的下拉框
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @RequestMapping("/initAddAlarm")
    @ResponseBody
    public JSONObject initAddAlarm(String orgId, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JSONObject jsonObject = new JSONObject();
        try {
            System.out.println(orgId);
        }catch (Exception e){

        }
        return jsonObject;
    }

    /**
     * 首頁實時報警
     * @return
     */
    @RequestMapping("/countFirstPageAlarm")
    @ResponseBody
    public JSONObject countFirstPageAlarm(){
        JSONObject jsonObject = new JSONObject();
        try {
            String orgId = OrgUtil.getOrgId();
//          String orgId = "233993942926888973";
            List<Map> list =   pdfOrderService.countFirstPageAlarm();
            jsonObject.put("data",list);
            jsonObject.put("success",true);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 首頁實時報警
     * @return
     */
    @RequestMapping("/findFirstPageOrder")
    @ResponseBody
    public JSONObject findFirstPageOrder(int type){
        JSONObject jsonObject = new JSONObject();
        try {
            String orgId = OrgUtil.getOrgId();
//            String orgId = "233993942926888973";
            List<Map> list =   pdfOrderService.findFirstPageOrder(type);
            jsonObject.put("data",list);
            jsonObject.put("success",true);
        }catch (Exception e){
            jsonObject.put("success",false);
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject;
    }
    public static String myMap = "ceshi";
    @RequestMapping("/getMap")
    @ResponseBody
    public JSONObject getMap(String map,HttpServletResponse httpServletResponse){
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
		/*星号表示所有的域都可以接受，*/
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST");
        JSONObject jsonObject = new JSONObject();
        myMap = map;
        System.out.println(map);
        return jsonObject;
    }

    @RequestMapping("/showMap")
    @ResponseBody
    public JSONObject showMap(HttpServletResponse httpServletResponse){
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
		/*星号表示所有的域都可以接受，*/
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data",myMap);
        return jsonObject;
    }
}
