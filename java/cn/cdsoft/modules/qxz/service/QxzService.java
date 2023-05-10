package cn.cdsoft.modules.qxz.service;

import cn.cdsoft.modules.qxz.dao.QxzDao;
import cn.cdsoft.modules.qxz.wx.util.PropertiesUtil;
import cn.cdsoft.modules.settings.dao.TOrgDao;
import cn.cdsoft.modules.settings.entity.TDevice;
import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.common.service.CrudService;
import cn.cdsoft.modules.qxz.dao.GzhUserDao;
import cn.cdsoft.modules.qxz.entity.GzhUser;
import cn.cdsoft.modules.qxz.entity.QxzFocus;
import cn.cdsoft.modules.settings.dao.TDeviceDao;
import cn.cdsoft.modules.settings.entity.TOrg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by ZZUSER on 2018/10/25.
 */
@Service
public class QxzService extends CrudService<QxzDao,TOrg> {
    @Autowired
    QxzDao qxzDao;
    @Autowired
    TOrgDao tOrgDao;
    @Autowired
    TDeviceDao tDeviceDao;

    @Autowired
    GzhUserDao gzhUserDao;

    public TOrg findOrgByName(TOrg tOrg){
        return qxzDao.findOrgByName(tOrg);
    }

    public List<Map> findQxzListByOrgId(TOrg tOrg){
        List<Map> list2 = new ArrayList<>();
        List<TOrg> list = qxzDao.findQxzListByOrgId(tOrg);
        for(int i=0;i<list.size();i++){
            List<Map> list1 = qxzDao.getNewDataByOrgId(list.get(i).getId());
            Map map = new HashMap();
            map.put("addr",list.get(i).getName());
            map.put("channel",list1);
            list2.add(map);
        }
        return list2;
    }

    public List<TOrg> getFocusList(String user){
        return qxzDao.getFocusList(user);
    }


    public void addFocus(QxzFocus qxzFocus){
        qxzDao.addFocus(qxzFocus);
    }

    public List<Map> getData(GzhUser gzhUser){
        Map map = new HashMap();
        List resultList = new ArrayList();
        GzhUser gzhUser1 = gzhUserDao.findGzhUser(gzhUser).get(0);
        String[] orgIds = gzhUser1.getOrgIds().split(",");
        List<Map> orgList = qxzDao.getOrgList(orgIds);
        TOrg tOrg = new TOrg();
        for(int i=0;i<orgList.size();i++){
            List<Map> typeList = new ArrayList();
            tOrg.setId(String.valueOf(orgList.get(i).get("id")));
            typeList = qxzDao.getDevTypeList(tOrg);
            List<Map> resultTypeList = new ArrayList();

            //气象站
            Map stationMap = new HashMap();
            stationMap.put("name","自动气象站");
            stationMap.put("id","qxz");
            List<Map> stationList = new ArrayList();
//            stationList = qxzDao.findStation(tOrg);
            for(int s=0;s<stationList.size();s++){
                List<Map> newDataList = new ArrayList();
                TOrg torg1 = new TOrg();
                torg1.setId(String.valueOf(stationList.get(s).get("id")));
                newDataList = qxzDao.getNewData(torg1);
                stationList.get(s).put("data",newDataList);
            }
            stationMap.put("data",stationList);
            resultTypeList.add(stationMap);

            int weatherFlg = 0; int pileFlag = 0;
            for(int y=0;y<typeList.size();y++){
                String id = String.valueOf(typeList.get(y).get("id"));
                if(id.equals("163") ||id.equals("164")||id.equals("165") || id.equals("166") || id.equals("167")){
                    weatherFlg = 1;
                }else if(id.equals("151") || id.equals("152")){
                    pileFlag = 1;
                }else {
                    Map resultMap = new HashMap();
                    resultMap.put("name",typeList.get(y).get("typeName"));
                    resultMap.put("id",typeList.get(y).get("id"));
                    String[] arr= {id};
                    Map selectMap = new HashMap();
                    selectMap.put("typeList",arr);
                    selectMap.put("id",tOrg.getId());
                    List<Map> devList = new ArrayList();
                    devList = qxzDao.findDevByTypeList(selectMap);
                    for(int r = 0;r<devList.size();r++){
                        List<Map> data = new ArrayList();
                        data = qxzDao.getNewDataByDevId(String.valueOf(devList.get(r).get("devId")));
                        devList.get(r).put("data",data);
                    }
                    resultMap.put("data",devList);
                    resultTypeList.add(resultMap);
                }
            }
            if(pileFlag==1){
                //充电桩
                Map pileMap = new HashMap();
                pileMap.put("name","充电桩");
                pileMap.put("id","cdz");
                List<Map> pileList = new ArrayList();
                Map findMap = new HashMap();
                String[] type = {"151","152"};
                findMap.put("typeList",type);
                findMap.put("id",tOrg.getId());
                pileList = qxzDao.findDevByTypeList(findMap);
                for(int p=0;p<pileList.size();p++){
                    List<Map> newDataList1 = new ArrayList();
                    newDataList1 = qxzDao.getNewDataByDevId(String.valueOf(pileList.get(p).get("devId")));
                    pileList.get(p).put("data",newDataList1);
                }
                pileMap.put("data",pileList);
                resultTypeList.add(pileMap);
            }
            Map orgMap = new HashMap();
            orgMap.put("name",orgList.get(i).get("name"));
            orgMap.put("id",orgList.get(i).get("id"));
            orgMap.put("data",resultTypeList);
            resultList.add(orgMap);
        }
        return resultList;
    }

    //第一次进入数据展示页
    public Map getPlots(GzhUser gzhUser){
        Map resultMap = new HashMap();
        int pageNo = gzhUser.getPage().getPageNo();
        int pageSize = gzhUser.getPage().getPageSize();
        GzhUser gzhUser1 = new GzhUser();
        List<GzhUser> list = gzhUserDao.findGzhUser(gzhUser);
        if(list.size()>0){
            gzhUser1 = gzhUserDao.findGzhUser(gzhUser).get(0);
        }else {
            gzhUser1 = null;
        }
        if(gzhUser1==null){
            resultMap.put("orgList",new ArrayList());
            resultMap.put("typeList",new ArrayList());
            resultMap.put("dataList",new ArrayList());
            return resultMap;
        }
        String[] orgIds = gzhUser1.getOrgIds().split(",");
        List<Map> orgList = qxzDao.getOrgList(orgIds);
        List<Map> typeList = new ArrayList();
        Map dataList = new HashMap();
        for(int i=0;i<orgList.size();i++){
            if(i==0){
                typeList = getTypeListByOrg(String.valueOf(orgList.get(0).get("id")));
            }
        }
        for(int j=0;j<typeList.size();j++){
            if(j==0){
                dataList = getDataList(String.valueOf(typeList.get(0).get("id")), String.valueOf(typeList.get(0).get("typeId")), String.valueOf(orgList.get(0).get("id")),pageNo,pageSize);
            }
        }
        resultMap.put("orgList",orgList);
        resultMap.put("typeList",typeList);
        resultMap.put("dataList",dataList);
        return resultMap;
    }

    //切换小区后获取数据
    public Map changePlots(String orgId, int pageNo, int pageSize){
        Map resultMap = new HashMap();
        List<Map> typeList = new ArrayList();
        Map dataList = new HashMap();
        typeList = getTypeListByOrg(orgId);
        for(int j=0;j<typeList.size();j++){
            dataList = getDataList(String.valueOf(typeList.get(0).get("id")), String.valueOf(typeList.get(0).get("typeId")), String.valueOf(orgId),pageNo,pageSize);
        }
        resultMap.put("typeList",typeList);
        resultMap.put("dataList",dataList);
        return resultMap;
    }

    //获取该小区底下设备类型
    public List<Map> getTypeListByOrg(String orgId){
        String qxzList = PropertiesUtil.getProperty("qxzList");
        String cdzList = PropertiesUtil.getProperty("cdzList");
        TOrg tOrg = new TOrg();
        List<Map> typeList = new ArrayList();
        List<Map> resultList = new ArrayList();
        tOrg.setId(orgId);
        typeList = qxzDao.getDevTypeList(tOrg);
        int weatherFlg=0;int pileFlag=0;
        for(int i=0;i<typeList.size();i++){
            String id = String.valueOf(typeList.get(i).get("id"));
            if(qxzList.contains(id)){
                weatherFlg = 1;
            }else if(cdzList.contains(id)){
                pileFlag = 1;
            }else {
                Map resultMap = new HashMap();
                resultMap.put("id",typeList.get(i).get("id"));
                resultMap.put("name",typeList.get(i).get("typeName"));
                resultMap.put("typeId",typeList.get(i).get("typeId"));
                resultList.add(resultMap);
            }
        }
        if(pileFlag==1){
            Map pileMap = new HashMap();
            pileMap.put("name","充电桩");
            pileMap.put("id","cdz");
            resultList.add(pileMap);
        }
        if(weatherFlg==1){
            Map stationMap = new HashMap();
            stationMap.put("name","自动气象站");
            stationMap.put("id","qxz");
            resultList.add(stationMap);
        }
        Collections.reverse(resultList);
        return resultList;
    }

    //获取实时数据集合
    public Map getDataList(String id , String typeId, String orgId, int pageNo, int pageSize){
        Map resultMap = new HashMap();
        if(id.equals("qxz")){
            TOrg tOrg = new TOrg();
            tOrg.setId(orgId);
            List<Map> stationList = new ArrayList();
            Page page = new Page();
            page.setPageSize(pageSize-1);
            page.setPageNo(pageNo);
            tOrg.setPage(page);
            Map paramMap = new HashMap();
            String qxzList = PropertiesUtil.getProperty("qxzList");
            String[] type = qxzList.split(",");
            paramMap.put("id",orgId);
            paramMap.put("qxzList",type);
            paramMap.put("pageNo",(pageNo-1)*pageSize);
            paramMap.put("pageSize",pageSize);
            stationList = qxzDao.findStation(paramMap);
            for(int s=0;s<stationList.size();s++){
                List<Map> newDataList = new ArrayList();
                TOrg torg1 = new TOrg();
                torg1.setId(String.valueOf(stationList.get(s).get("id")));
                newDataList = qxzDao.getNewData(torg1);
                for(int j=0;j<newDataList.size();j++){
                    int count = qxzDao.countAlarmByChId(String.valueOf(newDataList.get(j).get("id")));
                    if(count >0){
                        newDataList.get(j).put("alarm",true);
                        stationList.get(s).put("alarm",true);
                    }
                }
                stationList.get(s).put("data",newDataList);
            }
            resultMap.put("data",stationList);
            tOrg.setPage(null);
            paramMap.put("pageNo",null);
            paramMap.put("pageSize",null);
            List list = qxzDao.findStation(paramMap);
            resultMap.put("length",list.size());
            resultMap.put("id",id);
            return resultMap;
        }else if(id.equals("cdz")){
            List<Map> pileList = new ArrayList();
            Map findMap = new HashMap();
            String cdzList = PropertiesUtil.getProperty("cdzList");
            String[] type = cdzList.split(",");
            findMap.put("typeList",type);
            findMap.put("id",orgId);
            findMap.put("pageNo",(pageNo-1)*pageSize);
            findMap.put("pageSize",pageSize);
            pileList = qxzDao.findDevByTypeList(findMap);
            for(int p=0;p<pileList.size();p++){
                List<Map> newDataList1 = new ArrayList();
                newDataList1 = qxzDao.getNewDataByDevId(String.valueOf(pileList.get(p).get("devId")));
                for(int j=0;j<newDataList1.size();j++){
                    if(newDataList1.get(j).get("cId")!=null && String.valueOf(newDataList1.get(j).get("cId")).length()!=0){
                        newDataList1.get(j).put("alarm",true);
                        pileList.get(p).put("alarm",true);
                    }
//                    int count = qxzDao.countAlarmByChId(String.valueOf(newDataList1.get(j).get("id")));
//                    if(count >0){
//                        newDataList1.get(j).put("alarm",true);
//                        pileList.get(p).put("alarm",true);
//                    }
                }
                pileList.get(p).put("data",newDataList1);
            }
            resultMap.put("data",pileList);
            List list = qxzDao.countDevByTypeList(findMap);
            resultMap.put("length",list.size());
            resultMap.put("id",id);
            return resultMap;
        }else {
            String[] arr= {id};
            Map selectMap = new HashMap();
            selectMap.put("typeList",arr);
            selectMap.put("id",orgId);
            selectMap.put("pageNo",(pageNo-1)*pageSize);
            selectMap.put("pageSize",pageSize);
            List<Map> devList = new ArrayList();
            devList = qxzDao.findDevByTypeList(selectMap);
            for(int r = 0;r<devList.size();r++){
                List<Map> data = new ArrayList();
                data = qxzDao.getNewDataByDevId(String.valueOf(devList.get(r).get("devId")));
                for(int j=0;j<data.size();j++){
                    if(data.get(j).get("cId")!=null && String.valueOf(data.get(j).get("cId")).length()!=0){
                        data.get(j).put("alarm",true);
                        data.get(r).put("alarm",true);
                    }
//                    int count = qxzDao.countAlarmByChId(String.valueOf(data.get(j).get("id")));
//                    if(count >0){
//                        data.get(j).put("alarm",true);
//                        devList.get(r).put("alarm",true);
//                    }
                }
                devList.get(r).put("data",data);
            }
            resultMap.put("data",devList);
            List list = qxzDao.countDevByTypeList(selectMap);
            resultMap.put("length",list.size());
            resultMap.put("id",id);
            return resultMap;
        }
    }

    public Map getDataByDevId(String id, String orgId, String devId){
        Map resultMap = new HashMap();
        if(id.equals("qxz")){
            TOrg tOrg = new TOrg();
            tOrg.setId(orgId);
            List<Map> stationList = new ArrayList();
            TDevice tDevice1 = tDeviceDao.get(devId);
//            stationList = qxzDao.findStation(tOrg);

            List<Map> newDataList = new ArrayList();
            TOrg torg1 = new TOrg();
            torg1.setId(String.valueOf(tDevice1.getOrgId()));
            Map stationMap = new HashMap();
            stationMap = qxzDao.getStationBydevId(devId);
            newDataList = qxzDao.getNewData(torg1);
            for(int j=0;j<newDataList.size();j++){
                int count = qxzDao.countAlarmByChId(String.valueOf(newDataList.get(j).get("id")));
                if(count >0){
                    newDataList.get(j).put("alarm",true);
                    stationMap.put("alarm",true);
                }
            }
            stationMap.put("data",newDataList);
            stationList.add(stationMap);
            resultMap.put("data",stationList);
//            tOrg.setPage(null);
//            List list = qxzDao.findStation(tOrg);
//            resultMap.put("length",list.size());
            return resultMap;
        }else if(id.equals("cdz")){
            Map findMap = new HashMap();
            String cdzList = PropertiesUtil.getProperty("cdzList");
            String[] type =cdzList.split(",");
            findMap.put("typeList",type);
            List<Map> pileList = new ArrayList();
            List<Map> newDataList1 = new ArrayList();
            newDataList1 = qxzDao.getNewDataByDevId(devId);
            Map pipleMap = new HashMap();
            pipleMap = qxzDao.getDev(devId);
            for(int j=0;j<newDataList1.size();j++){
                int count = qxzDao.countAlarmByChId(String.valueOf(newDataList1.get(j).get("id")));
                if(count >0){
                    newDataList1.get(j).put("alarm",true);
                    pipleMap.put("alarm",true);
                }
            }
            pipleMap.put("data",newDataList1);
            pileList.add(pipleMap);
            resultMap.put("data",pileList);
//            List list = qxzDao.countDevByTypeList(findMap);
//            resultMap.put("length",list.size());
            return resultMap;
        }else {
//            String[] arr= {id};
            Map selectMap = new HashMap();

//            selectMap.put("pageNo",(pageNo-1)*pageSize);
//            selectMap.put("pageSize",pageSize);
            List<Map> devList = new ArrayList();
            List<Map> data = new ArrayList();
            data = qxzDao.getNewDataByDevId(devId);
            selectMap = qxzDao.getDev(devId);
            for(int j=0;j<data.size();j++){
                int count = qxzDao.countAlarmByChId(String.valueOf(data.get(j).get("id")));
                if(count >0){
                    data.get(j).put("alarm",true);
                    selectMap.put("alarm",true);
                }
            }
            selectMap.put("data",data);
            devList.add(selectMap);
            resultMap.put("data",devList);
//            List list = qxzDao.countDevByTypeList(selectMap);
//            resultMap.put("length",list.size());
            return resultMap;
        }
    }

    //点击告警模板消息后跳转页面数据获取
    public Map getAlarmData(String plotsId, String devTypeId, String devId){
        String qxzList = PropertiesUtil.getProperty("qxzList");
        String cdzList = PropertiesUtil.getProperty("cdzList");
        Map resultMap = new HashMap();
        List<Map> orgList = qxzDao.getOrgById(plotsId);
        List<Map> typeList = new ArrayList<>();
        Map map = new HashMap();
        if(qxzList.contains(devTypeId)){
            map.put("id","qxz");
            map.put("name","自动气象站");
            typeList.add(map);
        }else if(cdzList.contains(devTypeId)){
            map.put("id","cdz");
            map.put("name","充电桩");
            typeList.add(map);
        }else {
            Map codeMap = qxzDao.getCodeById(devTypeId);
            map.put("id",devTypeId);
            map.put("name", String.valueOf(codeMap.get("name")));
            typeList.add(map);
        }
        Map map1 = getDataByDevId(String.valueOf(typeList.get(0).get("id")),plotsId,devId);
        resultMap.put("orgList",orgList);
        resultMap.put("typeList",typeList);
        resultMap.put("dataList",map1);
        return resultMap;
    }



}
