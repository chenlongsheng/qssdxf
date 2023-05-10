/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.settings.web;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import cn.cdsoft.modules.settings.dao.TDeviceDao;
import cn.cdsoft.modules.settings.entity.TAlarmPolicy;
import cn.cdsoft.modules.settings.entity.TChannel;
import cn.cdsoft.modules.settings.entity.TCode;
import cn.cdsoft.modules.settings.entity.TDevice;
import cn.cdsoft.modules.settings.entity.TDeviceConfig;
import cn.cdsoft.modules.settings.entity.TDeviceDetail;
import cn.cdsoft.modules.settings.service.TChannelService;
import cn.cdsoft.modules.settings.service.TDeviceConfigService;
import cn.cdsoft.modules.settings.service.TDeviceDetailService;
import cn.cdsoft.modules.settings.service.TDeviceService;
import cn.cdsoft.modules.sys.entity.Area;
import cn.cdsoft.modules.sys.entity.Dict;
import cn.cdsoft.modules.sys.service.AreaService;
import cn.cdsoft.modules.sys.utils.UserUtils;

/**
 * 设备管理Controller
 * 
 * @author long
 * @version 2018-07-24
 */
@Controller
@RequestMapping(value = "${adminPath}/settings/tDevice")
public class TDeviceController extends BaseController {

	@Autowired
	private TDeviceService tDeviceService;
	@Autowired
	private TDeviceDetailService tDeviceDetailService;
	@Autowired
	private TDeviceConfigService tDeviceConfigService;
	@Autowired
	private TChannelService tChannelService;
	@Autowired
	private AreaService areaService;
	// @ModelAttribute("tDevice")
	// public MapEntity get(@RequestParam(required=false) String id) {
	// MapEntity entity = null;
	// if (StringUtils.isNotBlank(id)){
	// entity = iDeviceService.getDeviceEn(id);
	// }
	// if (entity == null){
	// entity = new MapEntity();
	// }
	// return entity;
	// }

	@ModelAttribute("tDevice")
	public TDevice get(@RequestParam(required = false) String id) {
		TDevice entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = tDeviceService.get(id);
		}
		if (entity == null) {
			entity = new TDevice();
		}
		return entity;
	}

//	@RequiresPermissions("settings:tDevice:index")
	@RequestMapping(value = { "index" })
	public String index(TDevice tDevice, Model model) {
		System.out.println("进啦了");
		// model.addAttribute("list", officeService.findAll());
		return "modules/settings/tDeviceIndex";
	}

	/**
	 * 设备管理列表页面
	 */
//	@RequiresPermissions("settings:tDevice:index")
	@RequestMapping(value = { "list", "" })
	public String list(String name, String codeName, String notUse, String projectName, String orgTreeId,
			String signLogo, HttpServletRequest request, HttpServletResponse response, Model model) {

		if (signLogo == null) {
			if (StringUtils.isBlank(projectName)) {
				projectName = "停车场";
			}
		}
		String pageNo = request.getParameter("pageNo");
		String pageSize = request.getParameter("pageSize");
		System.out.println(pageNo + "---pageNo");
		String orgId = UserUtils.getUser().getArea().getId();
		if (signLogo == null) {
			request.getSession().removeAttribute("deviceName");
			request.getSession().removeAttribute("deviceCodeName");
			request.getSession().removeAttribute("deviceNotUse");
			request.getSession().removeAttribute("deviceProjectName");
			request.getSession().removeAttribute("deviceOrgTreeId");
			request.getSession().removeAttribute("pageNo");
			request.getSession().removeAttribute("pageSize");
		}
		if (name != null || codeName != null || notUse != null || orgTreeId != null) {
			request.getSession().setAttribute("pageNo", pageNo);
			request.getSession().setAttribute("pageSize", pageSize);
			request.getSession().setAttribute("deviceName", name);
			request.getSession().setAttribute("deviceCodeName", codeName);
			request.getSession().setAttribute("deviceNotUse", notUse);
			request.getSession().setAttribute("deviceOrgTreeId", orgTreeId);
		}
		if (signLogo == null) {
			request.getSession().setAttribute("deviceProjectName", projectName);
		}
		if (signLogo != null) {
			name = (String) request.getSession().getAttribute("deviceName");
			codeName = (String) request.getSession().getAttribute("deviceCodeName");
			notUse = (String) request.getSession().getAttribute("deviceNotUse");
			projectName = (String) request.getSession().getAttribute("deviceProjectName");
			orgTreeId = (String) request.getSession().getAttribute("deviceOrgTreeId");

			System.out.println(projectName + "------signLogo != null");
		}
		if (orgTreeId != null) {
			orgId = orgTreeId;
		}
		List<TDeviceConfig> configList = tDeviceConfigService.configList("停车场");
		model.addAttribute("configList", configList);
		String nameConfig = nameConfig(configList);
		MapEntity entity = new MapEntity();
		entity.put("projectName", projectName);
		entity.put("name", name);
		entity.put("codeName", codeName);
		entity.put("notUse", notUse);
		entity.put("orgId", orgId);
		entity.put("nameConfig", nameConfig);

		List<MapEntity> codeList = tDeviceService.codeList();
		model.addAttribute("codeList", codeList);

		Page<MapEntity> page = tDeviceService.findPage(new Page<MapEntity>(request, response), entity);
		List<MapEntity> pageList = page.getList();

		page.setList(pageList);
		List<String> configNames = tDeviceConfigService.configName();// 项目名
		model.addAttribute("configNames", configNames);
		model.addAttribute("page", page);
		model.addAttribute("name", name);
		model.addAttribute("codeName", codeName);
		model.addAttribute("notUse", notUse);
		model.addAttribute("projectName", projectName);
		model.addAttribute("orgTreeId", orgId);
		return "modules/settings/tDeviceList";
	}

	/**
	 * 查看，增加，编辑设备管理表单页面
	 */
//	@RequiresPermissions(value = { "settings:tDevice:view", "settings:tDevice:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(@RequestParam(required = false) String id, Model model, HttpServletRequest request) {
		String projectName = (String) request.getSession().getAttribute("deviceProjectName");
		TDevice tDevice = tDeviceService.get(id);
		TDeviceDetail tDeviceDetail = tDeviceDetailService.get(id);
		List<MapEntity> codeList = tDeviceService.codeList();
		List<TDeviceConfig> deviceFromList = tDeviceConfigService.deviceFromList(projectName);
		model.addAttribute("deviceFromList", deviceFromList);
		String nameConfig = nameConfig(deviceFromList);
		if (nameConfig != "") {
			System.out.println("进来");
			MapEntity entity = tDeviceService.getDeviceFrom(id, nameConfig);
			model.addAttribute("entity", entity);
		}
		model.addAttribute("codeList", codeList);
		model.addAttribute("tDevice", tDevice);
		model.addAttribute("tDeviceDetail", tDeviceDetail);
		return "modules/settings/tDeviceForm";
	}

	// 添加的from
//	@RequiresPermissions(value = { "settings:tDevice:add" }, logical = Logical.OR)
	@RequestMapping(value = "formAdd")
	public String formAdd(@RequestParam(required = false) String id, Model model, HttpServletRequest request) {
		String projectName = (String) request.getSession().getAttribute("deviceProjectName");

		TDevice tDevice = tDeviceService.get(id);
		TDeviceDetail tDeviceDetail = tDeviceDetailService.get(id);
		List<MapEntity> codeList = tDeviceService.codeList();
		List<TDeviceConfig> deviceFromList = tDeviceConfigService.deviceFromList(projectName);
		model.addAttribute("deviceFromList", deviceFromList);
		String nameConfig = nameConfig(deviceFromList);
		if (nameConfig != "") {
			System.out.println("进来");
			MapEntity entity = tDeviceService.getDeviceFrom(id, nameConfig);
			model.addAttribute("entity", entity);
		}
		if (tDevice == null) {
			tDevice = new TDevice();
			String orgId = UserUtils.getUser().getArea().getId();
			String orgTreeId = (String) request.getSession().getAttribute("deviceOrgTreeId");
			if (orgTreeId != null) {
				orgId = orgTreeId;
			}
			tDevice.setOrgId(orgId);
			tDevice.setArea(areaService.get(orgId));
		}
		model.addAttribute("codeList", codeList);
		model.addAttribute("tDevice", tDevice);
		model.addAttribute("tDeviceDetail", tDeviceDetail);
		return "modules/settings/tDeviceFormAdd";
	}

	/**
	 * 保存设备管理
	 */
//	@RequiresPermissions(value = { "settings:tDevice:add", "settings:tDevice:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(TDevice tDevice, TDeviceDetail tDeviceDetail, String org, String channelUse, Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
		System.out.println(channelUse);
		System.out.println(org);
		System.out.println(tDevice.getOrgId());
		System.out.println(tDevice.getId());
		int key = -1;
		TChannel tChannel = new TChannel();
		if (!beanValidator(model, tDevice)) {
			return form(tDevice.getId(), model, request);
		}
		if (!tDevice.getIsNewRecord()) {// 编辑表单保存
			System.out.println("更新界面");
			TDevice t = tDeviceService.get(tDevice.getId());// 从数据库取出记录的值
			TDeviceDetail td = tDeviceDetailService.get(tDeviceDetail.getId());
			MyBeanUtils.copyBeanNotNull2Bean(tDevice, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值
			MyBeanUtils.copyBeanNotNull2Bean(tDeviceDetail, td);
			tDeviceService.saveDevice(t, td);// 保存
		} else {// 新增表单保存
			String id = tDeviceService.saveDevice(tDevice, tDeviceDetail);// 保存
			tDevice.setId(id);
		}
		if (channelUse.equals("1")) {
			key = tChannelService.updateOrg(tDevice.getOrgId(), tDevice.getId());
			addMessage(redirectAttributes, "同步设备下通道" + key + "条");
		}
		addMessage(redirectAttributes, "保存设备管理成功");
		if (key >= 0) {
			addMessage(redirectAttributes, "保存设备管理成功,通道同步" + key + "条");
		}
		String pageNo = (String) request.getSession().getAttribute("pageNo");
		String pageSize = (String) request.getSession().getAttribute("pageSize");
		return "redirect:" + Global.getAdminPath() + "/settings/tDevice/list/?repage&signLogo=0&pageNo=" + pageNo
				+ "&pageSize=" + pageSize;
	}

	@RequestMapping(value = "saveUse")
	public String saveUse(TDevice tDevice, Model model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws Exception {

		int t = tDeviceService.saveUse(tDevice);// 从数据库取出记录的值
		String pageNo = (String) request.getSession().getAttribute("pageNo");
		String pageSize = (String) request.getSession().getAttribute("pageSize");
		return "redirect:" + Global.getAdminPath() + "/settings/tDevice/list/?repage&signLogo=0&pageNo=" + pageNo
				+ "&pageSize=" + pageSize;
	}

	/**
	 * 删除设备管理
	 */
//	@RequiresPermissions("settings:tDevice:del")
	@RequestMapping(value = "delete")
	public String delete(TDevice tDevice, HttpServletRequest request, RedirectAttributes redirectAttributes) {

		tDeviceService.delete(tDevice);
		addMessage(redirectAttributes, "删除设备管理成功");
		String pageNo = (String) request.getSession().getAttribute("pageNo");
		String pageSize = (String) request.getSession().getAttribute("pageSize");
		return "redirect:" + Global.getAdminPath() + "/settings/tDevice/list/?repage&signLogo=0&pageNo=" + pageNo
				+ "&pageSize=" + pageSize;
	}

	/**
	 * 批量删除设备管理
	 */
//	@RequiresPermissions("settings:tDevice:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String idArray[] = ids.split(",");
		for (String id : idArray) {
			tDeviceService.delete(tDeviceService.get(id));
		}
		addMessage(redirectAttributes, "删除设备管理成功");
		String pageNo = (String) request.getSession().getAttribute("pageNo");
		String pageSize = (String) request.getSession().getAttribute("pageSize");
		return "redirect:" + Global.getAdminPath() + "/settings/tDevice/list/?repage&signLogo=0&pageNo=" + pageNo
				+ "&pageSize=" + pageSize;
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("settings:tDevice:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(TDevice tDevice, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "设备管理" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<TDevice> page = tDeviceService.findPage(new Page<TDevice>(request, response, -1), tDevice);
			new ExportExcel("设备管理", TDevice.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出设备管理记录失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/settings/tDevice/?repage";
	}

	/**
	 * 导入Excel数据
	 */
	@RequiresPermissions("settings:tDevice:import")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TDevice> list = ei.getDataList(TDevice.class);
			for (TDevice tDevice : list) {
				try {
					tDeviceService.save(tDevice);
					successNum++;
				} catch (ConstraintViolationException ex) {
					failureNum++;
				} catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条设备管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条设备管理记录" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入设备管理失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/settings/tDevice/?repage";
	}

	/**
	 * 下载导入设备管理数据模板
	 */
	@RequiresPermissions("settings:tDevice:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "设备管理数据导入模板.xlsx";
			List<TDevice> list = Lists.newArrayList();
			new ExportExcel("设备管理数据", TDevice.class, 1).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/settings/tDevice/?repage";
	}

	// 方法
	public String nameConfig(List<TDeviceConfig> deviceFromList) {
		String nameConfig = "";
		for (int i = 0; i < deviceFromList.size(); i++) {
			if (i != 0) {
				nameConfig += ",";
			}
			nameConfig += deviceFromList.get(i).getPrefix() + deviceFromList.get(i).getRowName() + " as "
					+ deviceFromList.get(i).getModelName();
		}
		return nameConfig;
	}

	// 上传图片
	@RequestMapping(value = { "upLoadPic" })
	@ResponseBody
	public JSONObject upLoadPic(String id, String status, HttpServletRequest request, MultipartFile imgFile,
			HttpServletResponse response) throws Exception {
		System.out.println("上传设备图标");
		JSONObject json = new JSONObject();
		// 获取文件原始名称
		String originalFilename = imgFile.getOriginalFilename();
		// 上传图片
		if (imgFile != null && originalFilename != null && originalFilename.length() > 0) {
			// 存储图片的物理路径
			String pic_path = request.getSession().getServletContext().getRealPath("/");
			String path = "static_modules/device/";

			System.out.println(pic_path + path);
			File file = new File(pic_path + path);
			// 如果文件夹不存在则创建
			if (!file.exists() && !file.isDirectory()) {
				System.out.println("//不存在");
				file.mkdir();
			} else {
				System.out.println("//目录存在");
			}

			String newFileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
			// 新图片
			System.out.println(pic_path + path + newFileName);
			File newFile = new File(pic_path + path + newFileName);
			// 将内存中的数据写入磁盘
			imgFile.transferTo(newFile);

			try {
				tDeviceService.updatePic(id, path + newFileName);// 保存
				json.put("suc", "成功上传");
			} catch (Exception e) {
				json.put("suc", "成功失败");
			}
		}
		return json;

	}

	// 下载tcode小图标
	@RequestMapping(value = { "getPic" }, method = RequestMethod.POST)
	@ResponseBody
	public String getpic(String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println(id + "getPic");
		TDevice tDevice = tDeviceService.get(id);
		String image = tDevice.getPicturePath();
		String pic_path = request.getSession().getServletContext().getRealPath("/") + image;
		System.out.println(pic_path);

		File file = new File(pic_path);
		if (StringUtils.isBlank(tDevice.getPicturePath())) {
			return "false";
		}
		return image;
	}

	// 小图片回调
	@RequestMapping(value = "codeRedirect")
	public String codeRedirect(HttpServletRequest request) throws Exception {
		String pageNo = (String) request.getSession().getAttribute("pageNo");
		String pageSize = (String) request.getSession().getAttribute("pageSize");
		return "redirect:" + Global.getAdminPath() + "/settings/tDevice/?repage&signLogo=0&pageNo=" + pageNo
				+ "&pageSize=" + pageSize;
	}

	@RequestMapping(value = "eMapForm")
	public String form(String orgId, String coldId, String typeId, String coordX, String coordY,
			HttpServletRequest request, Model model) {
		System.out.println(orgId + "----哈哈哈");
		System.out.println(coldId);
		// System.out.println(typeId);
		String[] str = coldId.split(",");

		List<MapEntity> deviceList = tDeviceService.devicePic(orgId, str[1]);

		model.addAttribute("length", deviceList.size());
		model.addAttribute("deviceList", deviceList);
		request.getSession().setAttribute("coordX", coordX);
		request.getSession().setAttribute("coordY", coordY);
		System.out.println(coordX);
		System.out.println(coordY);
		return "modules/settings/eMapForm";
	}

	@RequestMapping(value = "eMapSave")
	@ResponseBody
	public String eMapSave(String id, Model model, HttpServletRequest request) {
		System.out.println(id + "----哈哈哈");
		String coordX = (String) request.getSession().getAttribute("coordX");
		String coordY = (String) request.getSession().getAttribute("coordY");
		System.out.println(coordX);
		System.out.println(coordY);
		int key = tDeviceService.updateCoords(id, coordX, coordY);
		System.out.println(key);
		return "1";
	}

}