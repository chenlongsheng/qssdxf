/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.settings.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.drew.lang.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.cdsoft.common.config.Global;
import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.common.utils.DateUtils;
import cn.cdsoft.common.utils.MyBeanUtils;
import cn.cdsoft.common.utils.StringUtils;
import cn.cdsoft.common.utils.excel.ExportExcel;
import cn.cdsoft.common.utils.excel.ImportExcel;
import cn.cdsoft.common.web.BaseController;
import cn.cdsoft.modules.settings.dao.TCodeDao;
import cn.cdsoft.modules.settings.entity.TAlarmPolicy;
import cn.cdsoft.modules.settings.entity.TChannel;
import cn.cdsoft.modules.settings.entity.TCode;
import cn.cdsoft.modules.settings.entity.TCodeType;
import cn.cdsoft.modules.settings.service.TAlarmPolicyService;
import cn.cdsoft.modules.settings.service.TChannelService;
import cn.cdsoft.modules.settings.service.TCodeService;
import cn.cdsoft.modules.settings.service.TCodeTypeService;
import cn.cdsoft.modules.settings.service.TDeviceService;
import cn.cdsoft.modules.sys.entity.Area;
import cn.cdsoft.modules.sys.entity.Office;
import cn.cdsoft.modules.sys.service.AreaService;
import cn.cdsoft.modules.sys.utils.UserUtils;

/**
 * 数据配置Controller
 * 
 * @author long
 * @version 2018-10-23
 */
@Controller
@RequestMapping(value = "${adminPath}/settings/tAlarmPolicy")
public class TAlarmPolicyController extends BaseController {

	@Autowired
	private TAlarmPolicyService tAlarmPolicyService;
	@Autowired
	private TChannelService tChannelService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private TCodeDao tCodeDao;

	@ModelAttribute("tAlarmPolicy")
	public TAlarmPolicy get(@RequestParam(required = false) String id) {
		TAlarmPolicy entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = tAlarmPolicyService.get(id);
		}
		if (entity == null) {
			entity = new TAlarmPolicy();
		}
		return entity;
	}

	@RequiresPermissions("settings:tAlarmPolicy:index")
	@RequestMapping(value = { "index" })
	public String index(TAlarmPolicy TAlarmPolicy, Model model) {
		System.out.println("进啦了");
		// model.addAttribute("list", officeService.findAll());
		return "modules/settings/tAlarmPolicyIndex";
	}
    
	/**
	 * 数据配置列表页面
	 */
	@RequiresPermissions("settings:tAlarmPolicy:index")
	@RequestMapping(value = { "list", "" })
	public String list(String chName, String chType, Long typeId, String orgTreeId, Long level, String signLogo,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		String orgId = UserUtils.getUser().getArea().getId();
		System.out.println("list进来了");
		System.out.println(orgTreeId);

		if (signLogo == null) {
			request.getSession().removeAttribute("alarmChName");
			request.getSession().removeAttribute("alarmTypeName");
			request.getSession().removeAttribute("alarmorgTreeId");
			request.getSession().removeAttribute("alarmLevel");
			request.getSession().removeAttribute("alarmTypeId");
		}
		if (chName != null || chType != null || orgTreeId != null || level != null || typeId != null) {
			request.getSession().setAttribute("alarmChName", chName);
			request.getSession().setAttribute("alarmTypeName", chType);
			request.getSession().setAttribute("alarmorgTreeId", orgTreeId);
			request.getSession().setAttribute("alarmLevel", level);
			request.getSession().setAttribute("alarmTypeId", typeId);
		}
		if (signLogo != null) {
			chName = (String) request.getSession().getAttribute("alarmChName");
			chType = (String) request.getSession().getAttribute("alarmTypeName");
			orgTreeId = (String) request.getSession().getAttribute("alarmorgTreeId");
			level = (Long) request.getSession().getAttribute("alarmLevel");
			typeId = (Long) request.getSession().getAttribute("alarmTypeId");
		}
		if (orgTreeId != null) {
            orgId = orgTreeId;
		}
		TAlarmPolicy tAlarmPolicy = new TAlarmPolicy();
		tAlarmPolicy.setChName(chName);
		tAlarmPolicy.setTypeName(chType);
		tAlarmPolicy.setOrgId(Long.parseLong(orgId));
//		tAlarmPolicy.setOrgTreeId(orgTreeId);
		tAlarmPolicy.setLevel(level);
		tAlarmPolicy.setTypeId(typeId);
		Page<TAlarmPolicy> page = tAlarmPolicyService.findPage(new Page<TAlarmPolicy>(request, response), tAlarmPolicy);
		model.addAttribute("page", page);
		model.addAttribute("chName", chName);
		model.addAttribute("chType", chType);
		model.addAttribute("orgTreeId", orgId);
		model.addAttribute("level", level);
		model.addAttribute("typeId", typeId);
		// 获取级别
		List<TCode> levelList = tAlarmPolicyService.codeList("23");
		model.addAttribute("levelList", levelList);
		// 通道主类型集合
		List<TCodeType> codeTypeList = tAlarmPolicyService.codeTypeList();
		model.addAttribute("codeTypeList", codeTypeList);
		// 获取通道子类型表集合
		List<TCode> typeList = tAlarmPolicyService.codeList(null);
		model.addAttribute("typeList", typeList);

		return "modules/settings/tAlarmPolicyList";
	}

	/**
	 * 查看，增加，编辑数据配置表单页面
	 */
	@RequiresPermissions(value = { "settings:tAlarmPolicy:view", "settings:tAlarmPolicy:add",
			"settings:tAlarmPolicy:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(@ModelAttribute("tAlarmPolicy") TAlarmPolicy tAlarmPolicy, Model model) {

		List<TChannel> tChannelList = tChannelService.findAllList();
		model.addAttribute("tChannelList", tChannelList);
		// 区域地区集合
		List<MapEntity> orgList = tChannelService.orgList(null);
		model.addAttribute("orgList", orgList);
		// 通道主类型集合
		List<TCodeType> codeTypeList = tAlarmPolicyService.codeTypeList();
		model.addAttribute("codeTypeList", codeTypeList);

		// 获取通道子类型表集合
		List<TCode> typeList = tAlarmPolicyService.codeList(tAlarmPolicy.getTypeId() + "");
		model.addAttribute("typeList", typeList);

		// 获取级别
		List<TCode> levelList = tAlarmPolicyService.codeList("23");
		
		
		
		model.addAttribute("levelList", levelList);
        
		model.addAttribute("tAlarmPolicy", tAlarmPolicy);
		return "modules/settings/tAlarmPolicyForm";
	}

	@ResponseBody
	@RequestMapping(value = "codeTypeList")
	public JSONObject codeTypeList(String typeId) {
		System.out.println(typeId);
		JSONObject json = new JSONObject();
		List<TCode> typeList = tAlarmPolicyService.codeList(typeId);
		json.put("data", typeList);
		System.out.println(typeList);
		return json;

	}

	/**
	 * 保存数据配置
	 */
	@RequiresPermissions(value = { "settings:tAlarmPolicy:add", "settings:tAlarmPolicy:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(TAlarmPolicy tAlarmPolicy, Model model, RedirectAttributes redirectAttributes) throws Exception {
		System.out.println(tAlarmPolicy.toString());
		tAlarmPolicy.setLevel(Long.valueOf(3));
		tAlarmPolicy.setChId(Long.valueOf(0));
		tAlarmPolicy.setOrgId(Long.valueOf(0));
		
		
		if (!tAlarmPolicy.getIsNewRecord()) {// 编辑表单保存
			TAlarmPolicy t = tAlarmPolicyService.get(tAlarmPolicy.getId());// 从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tAlarmPolicy, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值
			tAlarmPolicyService.save(t);// 保存
		} else {// 新增表单保存
			try {
				tAlarmPolicyService.save(tAlarmPolicy);// 保存				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		addMessage(redirectAttributes, "保存数据配置成功");
		return "redirect:" + Global.getAdminPath() + "/settings/tAlarmPolicy/list/?repage&signLogo=0";
	}

	/**
	 * 删除数据配置
	 */
	@RequiresPermissions("settings:tAlarmPolicy:del")
	@RequestMapping(value = "delete")
	public String delete(TAlarmPolicy tAlarmPolicy, RedirectAttributes redirectAttributes) {
		tAlarmPolicyService.delete(tAlarmPolicy);
		addMessage(redirectAttributes, "删除数据配置成功");
		return "redirect:" + Global.getAdminPath() + "/settings/tAlarmPolicy/list/?repage&signLogo=0";
	}

	/**
	 * 批量删除数据配置
	 */
	@RequiresPermissions("settings:tAlarmPolicy:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] = ids.split(",");
		for (String id : idArray) {
			tAlarmPolicyService.delete(tAlarmPolicyService.get(id));
		}
		addMessage(redirectAttributes, "删除数据配置成功");
		return "redirect:" + Global.getAdminPath() + "/settings/tAlarmPolicy/list/?repage&signLogo=0";
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("settings:tAlarmPolicy:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(TAlarmPolicy tAlarmPolicy, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "数据配置" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<TAlarmPolicy> page = tAlarmPolicyService.findPage(new Page<TAlarmPolicy>(request, response, -1),
					tAlarmPolicy);
			new ExportExcel("数据配置", TAlarmPolicy.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出数据配置记录失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/settings/tAlarmPolicy/?repage";
	}

	/**
	 * 导入Excel数据
	 * 
	 */
	@RequiresPermissions("settings:tAlarmPolicy:import")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TAlarmPolicy> list = ei.getDataList(TAlarmPolicy.class);
			for (TAlarmPolicy tAlarmPolicy : list) {
				try {
					tAlarmPolicyService.save(tAlarmPolicy);
					successNum++;
				} catch (ConstraintViolationException ex) {
					failureNum++;
				} catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条数据配置记录。");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条数据配置记录" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入数据配置失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/settings/tAlarmPolicy/?repage";
	}

	/**
	 * 下载导入数据配置数据模板
	 */
	@RequiresPermissions("settings:tAlarmPolicy:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "数据配置数据导入模板.xlsx";
			List<TAlarmPolicy> list = Lists.newArrayList();
			new ExportExcel("数据配置数据", TAlarmPolicy.class, 1).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/settings/tAlarmPolicy/?repage";
	}

	// @RequiresPermissions("user")
	// @ResponseBody
	// @RequestMapping(value = "treeData")
	// public List<Map<String, Object>> treeData(@RequestParam(required=false)
	// String extId, @RequestParam(required=false) String type,
	// @RequestParam(required=false) Long grade, @RequestParam(required=false)
	// Boolean isAll, HttpServletResponse response) {
	// List<Map<String, Object>> mapList = Lists.newArrayList();
	// List<Office> list = officeService.findList(isAll);
	// for (int i=0; i<list.size(); i++){
	// Office e = list.get(i);
	// if ((StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId())
	// && e.getParentIds().indexOf(","+extId+",")==-1))
	// && (type == null || (type != null && (type.equals("1") ?
	// type.equals(e.getType()) : true)))
	// && (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <=
	// grade.intValue()))
	// && Global.YES.equals(e.getUseable())){
	// Map<String, Object> map = Maps.newHashMap();
	// map.put("id", e.getId());
	// map.put("pId", e.getParentId());
	// map.put("pIds", e.getParentIds());
	// map.put("name", e.getName());
	// if (type != null && "3".equals(type)){
	// map.put("isParent", true);
	// }
	// mapList.add(map);
	// }
	// }
	// return mapList;
	// }

}