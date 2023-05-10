/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.settings.web;

import java.util.List;

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

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;

import cn.cdsoft.common.config.Global;
import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.common.utils.DateUtils;
import cn.cdsoft.common.utils.MyBeanUtils;
import cn.cdsoft.common.utils.StringUtils;
import cn.cdsoft.common.utils.excel.ExportExcel;
import cn.cdsoft.common.utils.excel.ImportExcel;
import cn.cdsoft.common.web.BaseController;
import cn.cdsoft.modules.settings.entity.TDeviceConfig;
import cn.cdsoft.modules.settings.service.TDeviceConfigService;
import cn.cdsoft.modules.sys.utils.UserUtils;

/**
 * 数据配置Controller
 * 
 * @author long
 * @version 2018-10-24
 */
@Controller
@RequestMapping(value = "${adminPath}/settings/tDeviceConfig")
public class TDeviceConfigController extends BaseController {

	@Autowired
	private TDeviceConfigService tDeviceConfigService;

	@ModelAttribute("tDeviceConfig")
	public TDeviceConfig get(@RequestParam(required = false) String id) {
		TDeviceConfig entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = tDeviceConfigService.get(id);
		}
		if (entity == null) {
			entity = new TDeviceConfig();
		}
		return entity;
	}

	/**
	 * 配置参数列表页面
	 */
	@RequiresPermissions("settings:tDeviceConfig:list")
	@RequestMapping(value = { "list", "" })
	public String list(String projectName, String signLogo, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		String orgId = UserUtils.getUser().getArea().getId();
		if (signLogo == null) {
			request.getSession().removeAttribute("projectName");
		}
		if (projectName != null) {
			request.getSession().setAttribute("projectName", projectName);
		}
		if (signLogo != null) {
			projectName = (String) request.getSession().getAttribute("projectName");
		}
		TDeviceConfig tDeviceConfig = new TDeviceConfig();
		tDeviceConfig.setProjectName(projectName);

		Page<TDeviceConfig> page = tDeviceConfigService.findPage(new Page<TDeviceConfig>(request, response),
				tDeviceConfig);
		List<String> configNames = tDeviceConfigService.configName();
		model.addAttribute("configNames", configNames);
		model.addAttribute("page", page);
		model.addAttribute("projectName", projectName);
		return "modules/settings/tDeviceConfigList";
	}

	/**
	 * 查看，增加，编辑配置参数表单页面
	 */
	@RequiresPermissions(value = { "settings:tDeviceConfig:view", "settings:tDeviceConfig:add",
			"settings:tDeviceConfig:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(@ModelAttribute("tDeviceConfig") TDeviceConfig tDeviceConfig, Model model) {
		model.addAttribute("tDeviceConfig", tDeviceConfig);
		return "modules/settings/tDeviceConfigForm";
	}

	/**
	 * 保存配置参数
	 */
	@RequiresPermissions(value = { "settings:tDeviceConfig:add", "settings:tDeviceConfig:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(TDeviceConfig tDeviceConfig, Model model, RedirectAttributes redirectAttributes)
			throws Exception {
		if (!beanValidator(model, tDeviceConfig)) {
			return form(tDeviceConfig, model);
		}
		if (!tDeviceConfig.getIsNewRecord()) {// 编辑表单保存
			TDeviceConfig t = tDeviceConfigService.get(tDeviceConfig.getId());// 从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tDeviceConfig, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值
			tDeviceConfigService.save(t);// 保存
		} else {// 新增表单保存
			tDeviceConfigService.save(tDeviceConfig);// 保存
		}
		addMessage(redirectAttributes, "保存配置参数成功");
		return "redirect:" + Global.getAdminPath() + "/settings/tDeviceConfig/?repage&signLogo=0";
	}

	/**
	 * 删除配置参数
	 */
	@RequiresPermissions("settings:tDeviceConfig:del")
	@RequestMapping(value = "delete")
	public String delete(TDeviceConfig tDeviceConfig, RedirectAttributes redirectAttributes) {
		tDeviceConfigService.delete(tDeviceConfig);
		addMessage(redirectAttributes, "删除配置参数成功");
		return "redirect:" + Global.getAdminPath() + "/settings/tDeviceConfig/?repage&signLogo=0";
	}

	/**
	 * 批量删除配置参数
	 */
	@RequiresPermissions("settings:tDeviceConfig:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		System.out.println(ids + "===ids");
		String idArray[] = ids.split(",");
		// for(String id : idArray){
		// tDeviceConfigService.delete(tDeviceConfigService.get(id));
		// }
		addMessage(redirectAttributes, "删除配置参数成功");
		return "redirect:" + Global.getAdminPath() + "/settings/tDeviceConfig/?repage&signLogo=0";
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("settings:tDeviceConfig:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(TDeviceConfig tDeviceConfig, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "配置参数" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<TDeviceConfig> page = tDeviceConfigService.findPage(new Page<TDeviceConfig>(request, response, -1),
					tDeviceConfig);
			new ExportExcel("配置参数", TDeviceConfig.class).setDataList(page.getList()).write(response, fileName)
					.dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出配置参数记录失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/settings/tDeviceConfig/?repage";
	}

	/**
	 * 导入Excel数据
	 * 
	 */
	@RequiresPermissions("settings:tDeviceConfig:import")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TDeviceConfig> list = ei.getDataList(TDeviceConfig.class);
			for (TDeviceConfig tDeviceConfig : list) {
				try {
					tDeviceConfigService.save(tDeviceConfig);
					successNum++;
				} catch (ConstraintViolationException ex) {
					failureNum++;
				} catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条配置参数记录。");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条配置参数记录" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入配置参数失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/settings/tDeviceConfig/?repage";
	}

	/**
	 * 下载导入配置参数数据模板
	 */
	@RequiresPermissions("settings:tDeviceConfig:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "配置参数数据导入模板.xlsx";
			List<TDeviceConfig> list = Lists.newArrayList();
			new ExportExcel("配置参数数据", TDeviceConfig.class, 1).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/settings/tDeviceConfig/?repage";
	}
    //提交所有的信息
	@ResponseBody
	@RequestMapping(value = "editConfig")
	public String editConfig(String ids, String shows, String onlyReads, RedirectAttributes redirectAttributes) {
		int key = 0;
		TDeviceConfig tDeviceConfig = new TDeviceConfig();
		String[] idr = ids.split(",");
		String[] showr = shows.split(",");
		String[] onlyReadr = onlyReads.split(",");
		for (int i = 0; i < idr.length; i++) {
			System.out.println(idr[i]);
			System.out.println(showr[i]);
			System.out.println(onlyReadr[i]);
			tDeviceConfig.setId(idr[i]);
			tDeviceConfig.setShowField(Long.parseLong(showr[i]));
			tDeviceConfig.setOnlyRead(Long.parseLong(onlyReadr[i]));
			int result = tDeviceConfigService.updateConfig(tDeviceConfig);
			key += result;
		}
        System.out.println(key);
		return key+"";
	}

}