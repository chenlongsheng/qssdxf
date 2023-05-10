package cn.cdsoft.modules.homepage.service;

import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.service.ServiceResult;
import cn.cdsoft.modules.homepage.dao.DataViewDao;
import cn.cdsoft.modules.homepage.dao.HomepageDao;
import cn.cdsoft.modules.homepage.vo.HourDataFindVO;
import cn.cdsoft.modules.sys.entity.User;
import cn.cdsoft.modules.sys.utils.UserUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ZZUSER on 2019/2/14.
 */
@Service
public class DataViewService {
	@Autowired
	DataViewDao dataViewDao;

	@Autowired
	HomepageDao homepageDao;

	public ServiceResult getWaterDataByBuild(String orgId) {
		ServiceResult result = new ServiceResult();
		List<JSONObject> list = dataViewDao.getWaterDevByBuild(orgId);
		for (int i = 0; i < list.size(); i++) {
			List<JSONObject> list1 = homepageDao.getNewDataByDev(String.valueOf(list.get(i).get("id")));
			list.get(i).put("newData", list1);
			if (list1.size() == 0) {
				list.get(i).put("state", "1");
			}
			for (int k = 0; k < list1.size(); k++) {
				String warn = String.valueOf(list1.get(k).get("warn"));
				Integer historyValue = Integer.parseInt(String.valueOf(list1.get(k).get("historyValue")));
				int param0 = Integer.parseInt(String.valueOf(list1.get(k).get("param0")));
				int param1 = Integer.parseInt(String.valueOf(list1.get(k).get("param1")));
				int param2 = Integer.parseInt(String.valueOf(list1.get(k).get("param2")));

				double key = (float) (historyValue - param2) / param1;
				System.out.println("double------" + key);
				BigDecimal bg = new BigDecimal(key);
				double history = bg.setScale(param0, BigDecimal.ROUND_HALF_UP).doubleValue();

				list1.get(k).put("historyValue", history);
				
				
				list.get(i).put("state", String.valueOf(list1.get(k).get("online")));
			}
		}
		List<JSONObject> devList1 = new ArrayList<JSONObject>();
		// 按state排序
		for (JSONObject json : list) {
			String state = String.valueOf(json.get("state"));
			if (!state.equals("0")) {
				devList1.add(json);
			}
		}
		for (JSONObject json : list) {
			String state = String.valueOf(json.get("state"));
			if (state.equals("0")) {
				devList1.add(json);
			}
		}
		result.setData(devList1);
		result.setSuccess(true);
		return result;
	}

	public ServiceResult getHistoryData(HourDataFindVO vo) {
		ServiceResult result = new ServiceResult();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		if (StringUtils.isBlank(vo.getStartDate()) && StringUtils.isBlank(vo.getEndDate())) { // 日期都为空
			Date date = new Date();
			vo.setStartDate(formatter.format(date));
			List<JSONObject> list = dataViewDao.getHourDataByDev(vo);
			Map map = changeList(list);
			result.setData(map);
		} else if (!StringUtils.isBlank(vo.getStartDate()) && StringUtils.isBlank(vo.getEndDate())) { // 结束日期为空
			List<JSONObject> list = dataViewDao.getHourDataByDev(vo);
			Map map = changeList(list);
			result.setData(map);
		} else if (StringUtils.isBlank(vo.getStartDate()) && !StringUtils.isBlank(vo.getEndDate())) { // 开始日期为空
			vo.setStartDate(vo.getEndDate());
			List<JSONObject> list = dataViewDao.getHourDataByDev(vo);
			Map map = changeList(list);
			result.setData(map);
		} else if (!StringUtils.isBlank(vo.getStartDate()) && !StringUtils.isBlank(vo.getEndDate())
				&& vo.getStartDate().equals(vo.getEndDate())) { // 日期相同
			List<JSONObject> list = dataViewDao.getHourDataByDev(vo);
			Map map = changeList(list);
			result.setData(map);
		} else if (!StringUtils.isBlank(vo.getStartDate()) && !StringUtils.isBlank(vo.getEndDate())
				&& !vo.getStartDate().equals(vo.getEndDate())) { // 日期不同
			List<JSONObject> list = dataViewDao.getAvgData(vo);
			Map map = new HashMap();
			List xData = new ArrayList();
			List yData = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				xData.add(list.get(i).get("historyTime"));
				yData.add(list.get(i).get("historyValue"));
			}
			map.put("xData", xData);
			map.put("yData", yData);
			result.setData(map);
		}
		result.setSuccess(true);
		return result;
	}

	public ServiceResult getAllBuildData() {// 无用
		ServiceResult result = new ServiceResult();
		User user = UserUtils.getUser();
		// 获取所有可见楼栋
		List<JSONObject> orgLsit = homepageDao.getVisibleOrg(user.getId());
		for (int i = 0; i < orgLsit.size(); i++) {
			List<JSONObject> devList = dataViewDao.getWaterDevByBuild(String.valueOf(orgLsit.get(i).get("id")));

			for (int j = 0; j < devList.size(); j++) {
				List<JSONObject> list1 = homepageDao.getNewDataByDev(String.valueOf(devList.get(j).get("id")));
				devList.get(j).put("newData", list1);
				if (list1.size() == 0) {
					devList.get(j).put("state", "1");
				}
				for (int k = 0; k < list1.size(); k++) {
					String warn = String.valueOf(list1.get(k).get("warn"));
					String online = String.valueOf(list1.get(k).get("online"));
					Integer historyValue = Integer.parseInt(String.valueOf(list1.get(k).get("historyValue")));

					int param0 = Integer.parseInt(String.valueOf(list1.get(k).get("param0")));
					int param1 = Integer.parseInt(String.valueOf(list1.get(k).get("param1")));
					int param2 = Integer.parseInt(String.valueOf(list1.get(k).get("param2")));
					double key = (float) (historyValue - param2) / param1;
					System.out.println("double------" + key);
					BigDecimal bg = new BigDecimal(key);
					double history = bg.setScale(param0, BigDecimal.ROUND_HALF_UP).doubleValue();

					list1.get(k).put("historyValue", historyValue);
					devList.get(j).put("state", warn);
					devList.get(j).put("online", online);

				}
			}
			orgLsit.get(i).put("dataList", devList);
		}
		result.setData(orgLsit);
		result.setSuccess(true);

		return result;
	}

	public MapEntity getAllWater() {

		MapEntity entity = new MapEntity();
		List<MapEntity> getWarterOrg = homepageDao.getWarterOrg();
		List<MapEntity> allWater = dataViewDao.getAllWater();
		entity.put("getWarterOrg", getWarterOrg);
		entity.put("waterList", allWater);
		return entity;

	}

	@Transactional(readOnly = false)
	public void deleteHistoryData() {

		dataViewDao.deleteHistoryData();

	}

	private Map changeList(List<JSONObject> list) {
		String xData[] = { "0:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00", "10:00",
				"11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00",
				"22:00", "23:00" };
		double yData[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		for (int i = 0; i < list.size(); i++) {
			int index = (int) list.get(i).get("historyTime");
			Object ob = list.get(i).get("historyValue");
			yData[index] = (double) ob;
		}
		Map map = new HashMap();
		map.put("xData", xData);
		map.put("yData", yData);
		return map;
	}
}
