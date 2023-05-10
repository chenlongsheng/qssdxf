package cn.cdsoft.modules.qxz.dao;

import cn.cdsoft.common.persistence.CrudDao;
import cn.cdsoft.common.persistence.annotation.MyBatisDao;
import cn.cdsoft.modules.qxz.entity.QxzFocus;
import cn.cdsoft.modules.settings.entity.TOrg;

import java.util.List;
import java.util.Map;

/**
 * Created by ZZUSER on 2018/10/25.
 */
@MyBatisDao
public interface QxzDao extends CrudDao<TOrg> {

    TOrg findOrgByName(TOrg tOrg);

    List<TOrg> findQxzListByOrgId(TOrg tOrg);

    List<Map> getNewDataByOrgId(String orgId);

    List<TOrg> getFocusList(String user);

    void addFocus(QxzFocus qxzFocus);

    List<Map> getDevTypeList(TOrg tOrg);

    List<Map> getOrgList(String[] arr);

    List<Map> findStation(Map map);//获取小区气象站集合

    List<Map> getNewData(TOrg tOrg);//获取实时数据集合

    List<Map> findDevByTypeList(Map map);//获取设备集合

    List<Map> getNewDataByDevId(String devId);//根据设备id获取实时数据

    int countAlarmByChId(String chId);

    Map getDevById(String devId);

    String getParentIds(String id);

    String getPlotsBydevId(Map map);

    int countStation(Map map);

    List<Map> countDevByTypeList(Map map);

    List<Map> getOrgById(String id);

    Map getCodeById(String id);

    Map getStationBydevId(String id);

    Map getDev(String id);
}
