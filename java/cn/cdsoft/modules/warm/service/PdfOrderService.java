package cn.cdsoft.modules.warm.service;

import cn.cdsoft.common.service.ServiceResult;
import cn.cdsoft.modules.homepage.dao.HomepageDao;
import cn.cdsoft.modules.settings.entity.TDevice;
import cn.cdsoft.modules.settings.service.TDeviceService;
import cn.cdsoft.modules.warm.dao.*;
import cn.cdsoft.modules.warm.entity.PdfBind;
import cn.cdsoft.modules.websocket.MyWebSocketHandler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import cn.cdsoft.common.mq.rabbitmq.DevAlarm;
import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.service.CrudService;
import cn.cdsoft.common.utils.OrgUtil;
import cn.cdsoft.modules.qxz.wx.entry.Moban;
import cn.cdsoft.modules.qxz.wx.util.HttpUtils;
import cn.cdsoft.modules.qxz.wx.util.WeChatApiUtil;
import cn.cdsoft.modules.settings.entity.TOrg;
import cn.cdsoft.modules.sys.dao.UserDao;
import cn.cdsoft.modules.sys.entity.User;
import cn.cdsoft.modules.warm.dao.*;
import cn.cdsoft.modules.warm.entity.PdfOrder;
import cn.cdsoft.modules.warm.entity.PdfOrderDeal;
import cn.cdsoft.modules.warm.entity.PdfOrderRecorder;
import cn.cdsoft.modules.warm.util.PdfTemplateUtil;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ZZUSER on 2018/12/6.
 */
@Service
public class PdfOrderService extends CrudService<PdfOrderDao,TOrg> {
    @Autowired
    PdfOrderDao pdfOrderDao;

    @Autowired
    PdfOrderRecorderDao pdfOrderRecorderDao;

    @Autowired
    PdfOrderDealDao pdfOrderDealDao;

    @Autowired
    PdfSchedulingRuleDao pdfSchedulingRuleDao;

    @Autowired
    UserDao userDao;

    @Autowired
    PdfBindDao pdfBindDao;

    @Autowired
    HomepageDao homepageDao;

    @Autowired
    MyWebSocketHandler myWebSocketHandler;


    public Map findOrder(PdfOrder pdfOrder){
        if(pdfOrder.getAlarmAddr() !=null && pdfOrder.getAlarmAddr()!=""){
            TOrg tOrg = new TOrg();
            tOrg.setName(pdfOrder.getAlarmAddr());
//          tOrg.setParentId(233993942926888973l);
            List<TOrg> list = pdfOrderDao.getOrgByName(tOrg);
            List<TOrg> allList = new ArrayList();
            for(int i=0;i<list.size();i++){
                List<TOrg> pdfList = new ArrayList<>();
                pdfList = pdfOrderDao.getPdfByOrg(list.get(i));
                allList.addAll(pdfList);
            }
            String ids = "";
            for(int j=0;j<allList.size();j++){
                if(j==0){
                    ids = allList.get(j).getId();
                }else {
                    ids = ids+","+allList.get(j).getId();
                }
            }
            String[] arr = ids.split(",");
            pdfOrder.setArr(arr);
        }
        List<Map> resultList = pdfOrderDao.findOrder(pdfOrder);
        for(int i=0;i<resultList.size();i++){
            PdfOrderDeal pdfOrderDeal = new PdfOrderDeal();
            pdfOrderDeal.setType(0);
            pdfOrderDeal.setOrderId(String.valueOf(resultList.get(i).get("id")));
            int num = pdfOrderDealDao.countUnRead(pdfOrderDeal);
            if(num>0){
                resultList.get(i).put("hasUnRead",true);
            }
            List<User> userList = new ArrayList<>();
            List<User> sendUser = new ArrayList();
            List<User> confirmUser = new ArrayList<>();
            String ids = "";
            String[] arr = {};
            if(resultList.get(i).get("principal")!= null){
                ids = String.valueOf(resultList.get(i).get("principal"));
                arr = ids.split(",");
                userList = pdfOrderDao.getUserByIds(arr);
                resultList.get(i).put("principals",userList);
            }
            if(resultList.get(i).get("sendOrderUser")!= null){
                ids = String.valueOf(resultList.get(i).get("sendOrderUser"));
                arr = ids.split(",");
                sendUser = pdfOrderDao.getUserByIds(arr);
                resultList.get(i).put("userName",sendUser.get(0).getName());
                resultList.get(i).put("phone",sendUser.get(0).getPhone());
            }
            if(resultList.get(i).get("confirmUser")!= null){
                ids = String.valueOf(resultList.get(i).get("confirmUser"));
                arr = ids.split(",");
                confirmUser = pdfOrderDao.getUserByIds(arr);
                resultList.get(i).put("confirmUser",confirmUser.get(0).getName());
            }
        }
        Map<String,List> map =new LinkedHashMap<>();
        for(int i=0;i<resultList.size();i++){
            String time = String.valueOf(resultList.get(i).get("alarmTime"));
            time = time.substring(0,10);
            List<Map> list = map.get(time);
            if(list !=null){
                list.add(resultList.get(i));
                map.put(time,list);
            }else {
                list = new ArrayList<>();
                list.add(resultList.get(i));
                map.put(time,list);
            }
        }
        pdfOrder.setPage(null);
        List list = pdfOrderDao.findOrder(pdfOrder);
        Map map1 = new HashMap();
        map1.put("data",map);
        map1.put("length",list.size());
        return map1;
    }


    public Map findOrderById(String id){
        return  pdfOrderDao.findOrderById(id);
    }

    @Transactional(readOnly = false)
    public void deleteOrderByIds(String ids){
        String[] arr = ids.split(",");
        pdfOrderDao.deleteOrderByIds(arr);
    }

    /**
     * 消息队列发送告警过来
     * @param devAlarm
     */
    @Transactional(readOnly = false)
    public void addOrder(JSONObject devAlarm){
        TDevice tDevice = pdfOrderDao.getDevById(devAlarm.getString("dev_id"));
        PdfOrder pdfOrder = new PdfOrder();
        pdfOrder.setDevId(devAlarm.getString("dev_id"));
        pdfOrder.setAlarmType(1);      
        
        JSONArray array = devAlarm.getJSONArray("items");
        
        pdfOrder.setPrec(array.getJSONObject(0).getString("alarm_info"));
        pdfOrder.setAlarmLevel(array.getJSONObject(0).getInteger("alarm_level"));
        pdfOrder.setAlarmTime(array.getJSONObject(0).getString("occur_time"));
        pdfOrder.setAlarmAddr(tDevice.getOrgId());
        pdfOrder.setState(0);
        pdfOrder.setAlarmSource(1);
        pdfOrderDao.addOrder(pdfOrder);
        //websocket通知前端

        Map map = new HashMap();
        map.put("devStatus",2);
        map.put("devId",tDevice.getId());
        homepageDao.updateDevStatus(map);
    }

    @Transactional(readOnly = false)
    public long addOrder(PdfOrder pdfOrder){
//        SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal) UserUtils.getPrincipal();
//        String userId = principal.getId();
//        User user = UserUtils.get(userId);
        pdfOrder.setSendOrderUser(OrgUtil.getUserId());
//        pdfOrder.setSendOrderUser("277802135813361664");
        pdfOrder.setAlarmSource(2);
        pdfOrderDao.addOrder(pdfOrder);
        Map map = new HashMap();
        if(pdfOrder.getAlarmType()==1){
            //数据异常报警
            map.put("devStatus",2);
        }else {
            //设备故障报警
            map.put("devStatus",1);
        }
        map.put("devId",pdfOrder.getDevId());
        homepageDao.updateDevStatus(map);
        return Long.parseLong(pdfOrder.getId());
    }

    /**
     * 小程序端提交工单
     * @param pdfOrder
     */
    @Transactional(readOnly = false)
    public void addOrderByWx(PdfOrder pdfOrder){
        String id =  pdfOrder.getPrincipal();
        pdfOrder.setPrincipal(null);
        pdfOrder.setState(0);
        pdfOrder.setAlarmSource(2);
        pdfOrderDao.addOrder(pdfOrder);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatStr =formatter.format(new Date());
        PdfOrderRecorder pdfOrderRecorder = new PdfOrderRecorder();
        pdfOrderRecorder.setDate(formatStr);
        pdfOrderRecorder.setUserId(id);
        pdfOrderRecorder.setState(0);
        pdfOrderRecorder.setOrderId(pdfOrder.getId());
        pdfOrderRecorderDao.addOrderRecorder(pdfOrderRecorder);
    }

    /**
     * 确认工单
     * @param ids
     */
    @Transactional(readOnly = false)
    public void confirmOrder(String ids){
        PdfOrder pdfOrder = new PdfOrder();
     
        String[] arr = ids.split(",");
        for(int i=0;i<arr.length;i++){
            pdfOrder.setId(arr[i]);
            pdfOrderDao.updateOrder(pdfOrder);           
        }

    }

    /**
     * 派单
     * @param pdfOrder
     */
    @Transactional(readOnly = false)
    public void sendOrder(PdfOrder pdfOrder){
//        Map map = pdfOrderDao.findOrderById(pdfOrder.getId());
//        int state = (int) map.get("state");
        if(pdfOrder.getId()==null){
            long id= addOrder(pdfOrder);
            confirmOrder(String.valueOf(id));
            pdfOrder.setId(String.valueOf(id));
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatStr =formatter.format(new Date());
        PdfOrderRecorder pdfOrderRecorder = new PdfOrderRecorder();
        pdfOrderRecorder.setDate(formatStr);
        pdfOrderRecorder.setOrderId(pdfOrder.getId());
        pdfOrderRecorder.setUserId(OrgUtil.getUserId());
//        pdfOrderRecorder.setUserId("277802135813361664");
        Map map = pdfOrderDao.findOrderById(pdfOrder.getId());
        int state = (int) map.get("state");
        if(state ==0){
            confirmOrder(pdfOrder.getId());
        }
        pdfOrderRecorder.setState(2);
        pdfOrderRecorderDao.addOrderRecorder(pdfOrderRecorder);
        pdfOrder.setState(2);
        pdfOrder.setSendOrderUser(OrgUtil.getUserId());
//        pdfOrder.setSendOrderUser("277802135813361664");
        pdfOrderDao.updateOrder(pdfOrder);

        map = pdfOrderDao.findOrderById(pdfOrder.getId());
        String ids = String.valueOf(map.get("principal"));
        String[] arr = ids.split(",");
        List<PdfBind> list = pdfBindDao.findBindByIds(arr);
        for(int i=0;i<list.size();i++){
            Moban moban = PdfTemplateUtil.dataAlarmTemplate(map,list.get(i).getOpenId());
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


    /**
     * 接单
     * @param pdfOrder
     */
    @Transactional(readOnly = false)
    public void recieveOrder(PdfOrder pdfOrder){
        Map map = pdfOrderDao.findOrderById(pdfOrder.getId());
        int state = (int) map.get("state");
        String confirmUser = pdfOrder.getConfirmUser();
        if(state ==2){
            pdfOrder.setState(3);
            pdfOrder.setConfirmUser(null);
            pdfOrderDao.updateOrder(pdfOrder);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatStr =formatter.format(new Date());
        PdfOrderRecorder pdfOrderRecorder = new PdfOrderRecorder();
        pdfOrderRecorder.setDate(formatStr);
        pdfOrderRecorder.setOrderId(pdfOrder.getId());
        pdfOrderRecorder.setUserId(confirmUser);
        pdfOrderRecorder.setState(3);
        pdfOrderRecorderDao.addOrderRecorder(pdfOrderRecorder);
    }

    /**
     * 处理中
     * @param pdfOrder
     */
    @Transactional(readOnly = false)
    public void dealOrder(PdfOrder pdfOrder){
        Map map = pdfOrderDao.findOrderById(pdfOrder.getId());
        int state = (int) map.get("state");
        String confirmUser = pdfOrder.getConfirmUser();
        if(state ==3){
            pdfOrder.setConfirmUser(confirmUser);
            pdfOrder.setState(4);
            pdfOrderDao.updateOrder(pdfOrder);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatStr =formatter.format(new Date());
        PdfOrderRecorder pdfOrderRecorder = new PdfOrderRecorder();
        pdfOrderRecorder.setDate(formatStr);
        pdfOrderRecorder.setOrderId(pdfOrder.getId());
        pdfOrderRecorder.setUserId(confirmUser);
        pdfOrderRecorder.setState(4);
        pdfOrderRecorderDao.addOrderRecorder(pdfOrderRecorder);
    }

    /**
     * 维修人员提交工单处理
     */
    @Transactional(readOnly = false)
    public void submitOrder(PdfOrderDeal pdfOrderDeal){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatStr =formatter.format(new Date());
        pdfOrderDeal.setSendDate(formatStr);
        pdfOrderDeal.setType(1);
        pdfOrderDealDao.addOrderDeal(pdfOrderDeal);
    }

    /**
     * 管理人员回复
     */
    @Transactional(readOnly = false)
    public void replyOrder(PdfOrderDeal pdfOrderDeal){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatStr =formatter.format(new Date());
        pdfOrderDeal.setSendDate(formatStr);
        pdfOrderDeal.setType(0);
        pdfOrderDealDao.addOrderDeal(pdfOrderDeal);
        PdfBind pdfBind = new PdfBind();
        pdfBind.setUserId(pdfOrderDeal.getRecieveUser());
        List<PdfBind> list = pdfBindDao.findBind(pdfBind);
        Map map = pdfOrderDao.findOrderById(pdfOrderDeal.getOrderId());
        for(int i=0;i<list.size();i++){
            Moban moban = PdfTemplateUtil.order(map,list.get(i).getOpenId());
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

    /**
     * 管理员获取工单处理过程
     */
    public List<Map> getDealList(String orderId){
        List<Map> list = pdfOrderDealDao.getDealList(orderId);
        return list;
    }

    /**
     * 管理员获取工单处理过程
     */
    @Transactional(readOnly = false)
    public Map getOrderRecorders(PdfOrder pdfOrder){
        Map nowOrder= pdfOrderDao.findOrderById(pdfOrder.getId());
        int state = (int) nowOrder.get("state");
        PdfOrderDeal pdfOrderDeal = new PdfOrderDeal();
        pdfOrderDeal.setType(0);
        pdfOrderDeal.setOrderId(pdfOrder.getId());
        pdfOrderDealDao.setReaded(pdfOrderDeal);
        Map resultMap = new HashMap();
        PdfOrderRecorder pdfOrderRecorder = new PdfOrderRecorder();
        pdfOrderRecorder.setOrderId(pdfOrder.getId());
        List<Map> list = pdfOrderRecorderDao.getRecorderList(pdfOrderRecorder);
        List<Map> dealList = pdfOrderDealDao.getDealList(pdfOrder.getId());
        List<String> maxDate = pdfOrderDealDao.getMaxDate(pdfOrder.getId());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=0;i<dealList.size();i++){
            String date = (String) dealList.get(i).get("send_date");
            for(int j=0;j<maxDate.size();j++){
                if(date.equals(maxDate.get(j))){
                    List<Map> list1 = pdfOrderDealDao.getDealByReplyId(String.valueOf(dealList.get(i).get("id")));
                    if(list1.size()==0){
                        dealList.get(i).put("new",true);
                    }
                }
            }
        }
        resultMap.put("recorderList",list);
        resultMap.put("dealList",dealList);
        resultMap.put("state",state);
        return resultMap;
    }

    /**
     * 微信小程序端首页数据获取
     * @return
     */
    public Map getWxData(String id,String name,String orgId,String state){
        PdfOrder pdfOrder = new PdfOrder();
        Map resultMap = new HashMap();
        resultMap.put("name",name);
        pdfOrder.setPrincipal(id);
        pdfOrder.setAlarmAddr(orgId);
        if(state !=null && state.length()!=0){
            pdfOrder.setState(Integer.valueOf(state));
            List list = findOrder1(pdfOrder);
            if(state.equals("2")){
                resultMap.put("unRecieve",list);
                resultMap.put("recieve",new ArrayList());
                resultMap.put("onDeal",new ArrayList());
                resultMap.put("history",new ArrayList());
            }else if(state.equals("3")){
                resultMap.put("unRecieve",new ArrayList());
                resultMap.put("recieve",list);
                resultMap.put("onDeal",new ArrayList());
                resultMap.put("history",new ArrayList());
            }else if(state.equals("4")){
                resultMap.put("unRecieve",new ArrayList());
                resultMap.put("recieve",new ArrayList());
                resultMap.put("onDeal",list);
                resultMap.put("history",new ArrayList());
            } else if(state.equals("5")){
                resultMap.put("unRecieve",new ArrayList());
                resultMap.put("recieve",new ArrayList());
                resultMap.put("onDeal",new ArrayList());
                resultMap.put("history",list);
            }
            return resultMap;
        }

        pdfOrder.setState(2);
        List list = findOrder1(pdfOrder);
        resultMap.put("unRecieve",list);
        pdfOrder.setState(3);
        List list1 =findOrder1(pdfOrder);
        resultMap.put("recieve",list1);
        pdfOrder.setState(4);
        List list2 = findOrder1(pdfOrder);
        resultMap.put("onDeal",list2);
        pdfOrder.setState(5);
        List list3 = findOrder1(pdfOrder);
        resultMap.put("history",list3);
        return resultMap;
    }

    public List<Map> findOrder1(PdfOrder pdfOrder){
        List<Map> resultList = new ArrayList();
        List<Map> unreadList = new ArrayList<>();
        List<Map> readList = new ArrayList();
        if(pdfOrder.getState() ==3 || pdfOrder.getState()==4){
            resultList = pdfOrderDao.getFirstData(pdfOrder);
        }else if(pdfOrder.getState()==2){
            resultList = pdfOrderDao.getUnRecieve(pdfOrder);
        }else if(pdfOrder.getState()==5){
            resultList = pdfOrderDao.getHistoryOrder(pdfOrder);
        }
        for(int i=0;i<resultList.size();i++){
            List<User> userList = new ArrayList<>();
            List<User> sendUser = new ArrayList();
            List<User> confirmUser = new ArrayList<>();
            String ids = "";
            String[] arr = {};
            if(resultList.get(i).get("principal")!= null){
                ids = String.valueOf(resultList.get(i).get("principal"));
                arr = ids.split(",");
                userList = pdfOrderDao.getUserByIds(arr);
                resultList.get(i).put("principals",userList);
            }
            if(resultList.get(i).get("sendOrderUser")!= null){
                ids = String.valueOf(resultList.get(i).get("sendOrderUser"));
                arr = ids.split(",");
                sendUser = pdfOrderDao.getUserByIds(arr);
                resultList.get(i).put("userName",sendUser.get(0).getName());
                resultList.get(i).put("phone",sendUser.get(0).getPhone());
            }
            if(resultList.get(i).get("confirmUser")!= null){
                ids = String.valueOf(resultList.get(i).get("confirmUser"));
                arr = ids.split(",");
                confirmUser = pdfOrderDao.getUserByIds(arr);
                resultList.get(i).put("confirmUser",confirmUser.get(0).getName());
            }
            PdfOrderDeal pdfOrderDeal = new PdfOrderDeal();
            pdfOrderDeal.setOrderId(String.valueOf(resultList.get(i).get("id")));
            pdfOrderDeal.setType(1);
            pdfOrderDeal.setRecieveUser(pdfOrder.getPrincipal());
            int num = pdfOrderDealDao.countUnRead(pdfOrderDeal);
            Map map = pdfOrderDealDao.getUnRead(pdfOrderDeal);
            if(map==null){
                readList.add(resultList.get(i));
            }else {
                Object recieveUser =  map.get("recieve_user");
                if(recieveUser!=null){
                    resultList.get(i).put("hasUnRead",true);
                    resultList.get(i).put("sendDate",map.get("send_date"));
                    unreadList.add(resultList.get(i));
                }else {
                    readList.add(resultList.get(i));
                }
            }

        }
        //返回数据按照出生日期降序排 (我比较懒，方法头就不写了~~)
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Collections.sort(unreadList, new Comparator<Map>(){
            @Override
            public int compare(Map arg0, Map arg1) {
                int mark = 1;
                try {
                    Date date0 = sdf.parse(String.valueOf(arg0.get("sendDate")));
                    Date date1 = sdf.parse(String.valueOf(arg1.get("sendDate")));
                    if(date0.getTime() > date1.getTime()){
                        mark =  -1;
                    }
                    if(arg0.get("sendDate").equals(arg1.get("sendDate"))){
                        mark =  0;
                    }
                } catch (ParseException e) {
                    logger.error("日期转换异常", e);
                    e.printStackTrace();
                }
                return mark;
            } //compare
        });
        unreadList.addAll(readList);
        return unreadList;
    }

    /**
     * 维修工人查看工单处理记录
     * @param pdfOrder
     * @return
     */
    public List<Map> getOrderDeal(PdfOrder pdfOrder){
        PdfOrderDeal pdfOrderDeal = new PdfOrderDeal();
        pdfOrderDeal.setOrderId(pdfOrder.getId());
        pdfOrderDeal.setSendUser(pdfOrder.getPrincipal());
        pdfOrderDeal.setRecieveUser(pdfOrder.getPrincipal());
        List<Map> list = pdfOrderDealDao.getOrderDeal(pdfOrderDeal);
        return list;
    }

    /**
     * 根据区域获取设备列表
     */
    public List<Map> getDevByOrg(TOrg tOrg){
        return pdfOrderDao.getDevByOrg(tOrg);
    }

    /**
     * 获取报警人集合
     */
    public List<Map> getSendUserList(String orgId){
        Map map  = new HashMap();
        map.put("orgId",orgId);
        return pdfOrderDao.getSendUserList(map);
    }

    public List<Map> countFirstPageAlarm(){
        return pdfOrderDao.countFirstPageAlarm();
    }


    public List<Map> findFirstPageOrder(int type){
        return pdfOrderDao.findFirstPageOrder(type);
    }


    public List<JSONObject> getUserByDev(String devId){
        List<JSONObject> list = pdfOrderDao.getUserByDev(devId);
        return list;
    }

    public String getBuildByDev(String devId){
        return pdfOrderDao.getBuildByDev(devId);
    }

    /**
     * 消息队列发送告警过来
     * @param devAlarm
     */
    @Transactional(readOnly = false)
    public void sendAlarm(JSONObject devAlarm){

        PdfOrder pdfOrder = new PdfOrder();
        pdfOrder.setDevId(devAlarm.getString("dev_id"));
        String chId = devAlarm.getString("ch_id");

    	MapEntity channel = pdfOrderDao.getChannel(chId);
    	String name = (String)channel.get("name");
    	String addr = (String)channel.get("addr");
    	String orgName = (String)channel.get("orgName");
    	
        JSONArray array = devAlarm.getJSONArray("items");        
        pdfOrder.setPrec(array.getJSONObject(0).getString("alarm_info"));
        pdfOrder.setAlarmLevel(array.getJSONObject(0).getInteger("alarm_level"));
        pdfOrder.setAlarmTime(array.getJSONObject(0).getString("occur_time"));

        
        
        
        
        
        
        
    }
}
