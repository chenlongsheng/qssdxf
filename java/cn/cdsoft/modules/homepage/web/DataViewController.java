package cn.cdsoft.modules.homepage.web;

import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.service.ServiceResult;
import cn.cdsoft.common.utils.ServletUtils;
import cn.cdsoft.modules.homepage.service.DataViewService;
import cn.cdsoft.modules.homepage.vo.HourDataFindVO;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ZZUSER on 2019/2/14.
 */
@Controller
@RequestMapping("${adminPath}/dataView")
//${adminPath}/
public class DataViewController {
	@Autowired
	DataViewService dataViewService;

	/**
	 * 获取某一楼栋水位水压实时数据
	 * 
	 * @param orgId
	 * @return
	 */
	@RequestMapping("getWaterDataByBuild")
	@ResponseBody
	public JSONObject getWaterDataByBuild(String orgId) { 
		// 已修改
		ServiceResult serviceResult = dataViewService.getWaterDataByBuild(orgId);
		return ServletUtils.buildJsonRs(serviceResult.isSuccess(), serviceResult.getMessage(), serviceResult.getData());
	}

	/**
	 * 获取某一楼栋某一水位水压设备历史数据
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping("/getHistoryData")
	@ResponseBody
	public JSONObject getHistoryData(HourDataFindVO vo) {
		
		ServiceResult serviceResult = dataViewService.getHistoryData(vo);
		return ServletUtils.buildJsonRs(serviceResult.isSuccess(), serviceResult.getMessage(), serviceResult.getData());
	}

	// 水压所有数据
	@RequestMapping("/getAllBuildData")
	@ResponseBody
	public JSONObject getAllBuildData() {
		ServiceResult serviceResult = dataViewService.getAllBuildData();
		return ServletUtils.buildJsonRs(serviceResult.isSuccess(), serviceResult.getMessage(), serviceResult.getData());
	}

	@RequestMapping("/getAllWater") // 新写需要前端渲染 上面
	@ResponseBody
	public String getAllWater() {
		
		return ServletUtils.buildRs(true, "数据总览", dataViewService.getAllWater());
	}
}
