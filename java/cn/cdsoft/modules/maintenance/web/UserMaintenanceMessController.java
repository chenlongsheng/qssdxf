package cn.cdsoft.modules.maintenance.web;

import cn.cdsoft.common.utils.ServletUtils;
import cn.cdsoft.modules.maintenance.entity.PdfUserMaintenanceMess;
import cn.cdsoft.modules.maintenance.service.PdfMaintenanceDetailService;
import cn.cdsoft.modules.maintenance.service.PdfUserService;
import cn.cdsoft.modules.sys.utils.UserUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.modules.maintenance.service.PdfMaintenanceService;
import cn.cdsoft.modules.maintenance.service.PdfUserMaintenanceMessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2018-12-25.
 */
@Controller
@RequestMapping(value = "${adminPath}/maintain/userMaintenanceMess")
public class UserMaintenanceMessController {
	
	@Autowired
	private PdfMaintenanceService pdfMaintenanceService;
	@Autowired
	private PdfMaintenanceDetailService pdfMaintenanceDetailService;
	@Autowired
	private PdfUserService pdfUserService;
	@Autowired
	private PdfUserMaintenanceMessService pdfUserMaintenanceMessService;

	// 查询分页前条件回调
	@RequestMapping(value = { "message" })
	@ResponseBody
	public String message(String orgName) {
		
		String orgId = UserUtils.getUser().getArea().getId();
		MapEntity entity = new MapEntity();
		List<MapEntity> tcodeList = pdfUserService.tcodeList(null);
		Set<MapEntity> orgList = pdfUserService.orgList(orgName, orgId);
		entity.put("tcodeList", tcodeList);// 设备类型烟感等
		entity.put("orgList", orgList);// 配电房的区域list
		return ServletUtils.buildRs(true, "查询条件需求", entity);
	}
	
    //维保单位的分页查询
	@RequestMapping(value = { "list", ""})
	@ResponseBody
	public String list(@ModelAttribute("PdfUserMaintenanceMess") PdfUserMaintenanceMess pdfUserMaintenanceMess,
			HttpServletRequest request, HttpServletResponse response) {

		System.out.println(pdfUserMaintenanceMess.toString());
		String codeIds = pdfUserMaintenanceMess.getCodeIds();
		if (codeIds != null && codeIds != "") {
			String[] codes = codeIds.split(",");
			if (codes.length > 0) {
				pdfUserMaintenanceMess.setCodeId(codes[0]);
				pdfUserMaintenanceMess.setTypeId(codes[1]);
			}
		}
		Page<PdfUserMaintenanceMess> page = pdfUserMaintenanceMessService.findPage(new Page<PdfUserMaintenanceMess>(request, response),
				pdfUserMaintenanceMess);
		
		return ServletUtils.buildRs(true, "维保信息分页查询", page);
	}

	//详情
	@RequestMapping(value = { "detail" })
	@ResponseBody
	public String Detail(PdfUserMaintenanceMess pdfUserMaintenanceMess) {
		
		MapEntity entity = new MapEntity();
		List<MapEntity> userDetailList = pdfUserMaintenanceMessService.userDetail(pdfUserMaintenanceMess);
		List<MapEntity> maintenanceDetailList = pdfUserMaintenanceMessService.maintenanceDetail(pdfUserMaintenanceMess);
		entity.put("userDetailList", userDetailList);
		entity.put("maintenanceDetailList", maintenanceDetailList);
		return ServletUtils.buildRs(true, "详情", entity);
	}

	// 修改我方排序
	@RequestMapping(value = { "changeUserOrder" })
	@ResponseBody
	public String changeUserOrder(String orderIds) {

		System.out.println(orderIds);
		String list = orderIds.replace("&quot;", "'");
		JSONArray ja = JSONArray.parseArray(list);
		try {
			for (int i = 0; i < ja.size(); i++) {
				MapEntity entity = JSONObject.parseObject(ja.get(i).toString(), MapEntity.class);
				pdfUserMaintenanceMessService.changeUserOrder(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ServletUtils.buildRs(false, "修改失败", "");
		}
		return ServletUtils.buildRs(true, "修改成功", "");
	}

	// 修改维保方排序
	@RequestMapping(value = { "changeMainOrder" })
	@ResponseBody
	public String changeMainOrder(String orderIds) {
        
		System.out.println(orderIds+"----维保排序json");
		String list = orderIds.replace("&quot;", "'");
		JSONArray ja = JSONArray.parseArray(list);
		try {
			for (int i = 0; i < ja.size(); i++) {
				MapEntity entity = JSONObject.parseObject(ja.get(i).toString(), MapEntity.class);
				pdfUserMaintenanceMessService.changeMainOrder(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ServletUtils.buildRs(false, "修改失败", "");
		}
		return ServletUtils.buildRs(true, "修改成功", "");
	}

}
