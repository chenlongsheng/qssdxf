package cn.cdsoft.modules.qxz.web;

import cn.cdsoft.common.json.AjaxJson;
import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.common.web.BaseController;
import cn.cdsoft.modules.qxz.entity.GzhUser;
import cn.cdsoft.modules.qxz.entity.QxzFocus;
import cn.cdsoft.modules.qxz.service.QxzService;
import cn.cdsoft.modules.settings.entity.TOrg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by ZZUSER on 2018/10/25.
 */
@Controller
@RequestMapping("/qxz")
public class QxzController extends BaseController {
	@Autowired
	QxzService qxzService;

	@RequestMapping("/findOrgByName")
	@ResponseBody
	public AjaxJson findOrgByName(TOrg tOrg) {
		AjaxJson ajaxJson = new AjaxJson();
		try {
			TOrg data = qxzService.findOrgByName(tOrg);
			ajaxJson.setSuccess(true);
			ajaxJson.put("data", data);
			ajaxJson.setErrorCode("0");
		} catch (Exception e) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg(e.getMessage());
		}
		return ajaxJson;
	}

	@RequestMapping("/findQxzListByOrgId")
	@ResponseBody
	public AjaxJson findQxzListByOrgId(TOrg tOrg) {
		AjaxJson ajaxJson = new AjaxJson();
		try {
			List<Map> data = qxzService.findQxzListByOrgId(tOrg);
			ajaxJson.setSuccess(true);
			ajaxJson.put("data", data);
			ajaxJson.setErrorCode("0");
		} catch (Exception e) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg(e.getMessage());
		}
		return ajaxJson;
	}

	@RequestMapping("/getFocusList")
	@ResponseBody
	public AjaxJson getFocusList(String user) {
		AjaxJson ajaxJson = new AjaxJson();
		try {
			List<TOrg> data = qxzService.getFocusList(user);
			ajaxJson.setSuccess(true);
			ajaxJson.put("data", data);
			ajaxJson.setErrorCode("0");
		} catch (Exception e) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg(e.getMessage());
		}
		return ajaxJson;
	}

	@RequestMapping("/addFocus")
	@ResponseBody
	public AjaxJson addFocus(QxzFocus qxzFocus) {
		AjaxJson ajaxJson = new AjaxJson();
		try {
			qxzService.addFocus(qxzFocus);
			ajaxJson.setSuccess(true);
			ajaxJson.setErrorCode("0");
		} catch (Exception e) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg(e.getMessage());
		}
		return ajaxJson;
	}

	@RequestMapping("/getData")
	@ResponseBody
	public List<Map> getData(GzhUser gzhUser) {
		return qxzService.getData(gzhUser);
	}

	@RequestMapping("/getPlots")
	@ResponseBody
	public Map getPlots(GzhUser gzhUser, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		Page page = new Page<GzhUser>(httpServletRequest, httpServletResponse);
		gzhUser.setPage(page);
		return qxzService.getPlots(gzhUser);
	}

	@RequestMapping("/changePlots")
	@ResponseBody
	public Map changePlots(String orgId, int pageNo, int pageSize) {
		return qxzService.changePlots(orgId, pageNo, pageSize);
	}

	@RequestMapping("/getTypeListByOrg")
	@ResponseBody
	public List<Map> getTypeListByOrg(String id) {
		return qxzService.getTypeListByOrg(id);
	}

	@RequestMapping("/getDataList")
	@ResponseBody
	public Map getDataList(String id, String typeId, String orgId, int pageNo, int pageSize) {
		return qxzService.getDataList(id, typeId, orgId, pageNo, pageSize);
	}

	@RequestMapping("/getAlarmData")
	@ResponseBody
	public Map getAlarmData(String plotsId, String devTypeId, String devId) {
		return qxzService.getAlarmData(plotsId, devTypeId, devId);
	}
}
