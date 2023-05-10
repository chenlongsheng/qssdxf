package cn.cdsoft.modules.warm.dao;

import cn.cdsoft.common.persistence.CrudDao;
import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.annotation.MyBatisDao;
import cn.cdsoft.modules.settings.entity.TDevice;
import cn.cdsoft.modules.settings.entity.TOrg;
import cn.cdsoft.modules.sys.entity.User;
import cn.cdsoft.modules.warm.entity.PdfOrder;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * Created by ZZUSER on 2018/12/6.
 */
@MyBatisDao
public interface PdfOrderDao extends CrudDao<TOrg> {

	List<Map> findOrder(PdfOrder pdfOrder);// 工单列表查询

	List<TOrg> getOrgByName(TOrg tOrg);

	List<TOrg> getPdfByOrg(TOrg tOrg);

	long addOrder(PdfOrder pdfOrder);// 新增工单

	List<User> getUserByIds(String[] arr);

	Map findOrderById(String id);

	TDevice getDevById(String devId);

	void deleteOrderByIds(String[] ids);

	List<Map> getFirstData(PdfOrder pdfOrder);// 已接单或处理中

	List<Map> getUnRecieve(PdfOrder pdfOrder);// 未接工单

	List<Map> getHistoryOrder(PdfOrder pdfOrder);// 历史工单

	List<Map> getDevByOrg(TOrg tOrg);// 根据区域获取设备

	List<Map> getSendUserList(Map map);// 根据区域获取报警人集合

	List<Map> countFirstPageAlarm();// 統計首頁實時報警

	List<Map> findFirstPageOrder(int type);

	List<JSONObject> getUserByDev(String devId);// 根据设备获取对应的人员

	String getBuildByDev(String devId);// 根据设备id获取楼栋

	List<JSONObject> getAlarmByBuild(TOrg tOrg);// 根据楼栋获取报警
	
	void updateOrder(PdfOrder pdfOrder);

	MapEntity getChannel(@Param("chId") String chId);
}
