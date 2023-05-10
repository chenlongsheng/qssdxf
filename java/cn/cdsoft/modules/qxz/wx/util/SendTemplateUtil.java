package cn.cdsoft.modules.qxz.wx.util;

import cn.cdsoft.modules.qxz.dao.GzhUserDao;
import cn.cdsoft.modules.qxz.dao.QxzDao;
import cn.cdsoft.modules.qxz.entity.GzhUser;
import cn.cdsoft.modules.qxz.wx.entry.Moban;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import cn.cdsoft.common.mq.rabbitmq.DevAlarm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZZUSER on 2018/11/23.
 * 模板消息发送工具类
 */
@Service
public class SendTemplateUtil {
    @Autowired
    QxzDao qxzDao;
    @Autowired
    GzhUserDao gzhUserDao;

    public void sendAlarmTemplate(DevAlarm devAlarm){
        Long alarmType = devAlarm.getItems().get(0).getAlarm_type();
        Map map = qxzDao.getDevById(String.valueOf(devAlarm.getDev_id()));
        Moban moban = new Moban();
        String parentIds = qxzDao.getParentIds(String.valueOf(devAlarm.getDev_id()));
        String[] arr = parentIds.split(",");
        Map paramMap = new HashMap();
        paramMap.put("orgList",arr);
        paramMap.put("id", String.valueOf(devAlarm.getDev_id()));
        String orgId = qxzDao.getPlotsBydevId(paramMap);
        GzhUser gzhUser = new GzhUser();
        gzhUser.setParentOrgIds(orgId);
        gzhUser.getPage().setPageSize(1000);
        gzhUser.getPage().setPageNo(1);
        List<Map> list = gzhUserDao.findListByPage(gzhUser);
        for(int i=0;i<list.size();i++){
            if(list.get(i).get("openId") !=null && String.valueOf(list.get(i).get("openId")).length()!=0){
//                if(alarmType ==3){
                    moban= TemplateUtil.dataAlarmTemplate(devAlarm,map,orgId, String.valueOf(list.get(i).get("openId")));
//                }else if(alarmType==4){
//
//                }
//                String accessToken = (String) JedisUtils.getObject("WxAccessToken");
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

    }
}
