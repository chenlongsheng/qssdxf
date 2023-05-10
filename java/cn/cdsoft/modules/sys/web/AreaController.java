/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.sys.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.cdsoft.common.config.Global;
import cn.cdsoft.common.utils.StringUtils;
import cn.cdsoft.common.web.BaseController;
import cn.cdsoft.modules.sys.entity.Area;
import cn.cdsoft.modules.sys.service.AreaService;
import cn.cdsoft.modules.sys.utils.UserUtils;

/**
 * 区域Controller
 * 
 * @author jeeplus
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/area")
public class AreaController extends BaseController {

	@Autowired
	private AreaService areaService;

	@ModelAttribute("area")
	public Area get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return areaService.get(id);
		} else {
			return new Area();
		}
	}

	@RequiresPermissions("sys:area:list")
	@RequestMapping(value = { "list", "" })
	public String list(Area area, Model model) {
//		List<Area> list = areaService.findAll();
		model.addAttribute("list", areaService.findAll());
		return "modules/sys/areaList";
	}

	@RequiresPermissions(value = { "sys:area:view", "sys:area:add", "sys:area:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(Area area, Model model) {
		if (area.getParent() == null || area.getParent().getId() == null) {
        // area.setParent(UserUtils.getUser().getOffice().getArea());
		} else {
			Area a=areaService.get(area.getParent().getId());
			if(a==null) {
				a = new Area();
				a.setName("无");
			}
			area.setParent(a);
		}
		// 自动获取排序号
		// if (StringUtils.isBlank(area.getId())){
		// int size = 0;
		// List<Area> list = areaService.findAll();
		// for (int i=0; i<list.size(); i++){
		// Area e = list.get(i);
		// if (e.getParent()!=null && e.getParent().getId()!=null
		// && e.getParent().getId().equals(area.getParent().getId())){
		// size++;
		// }
		// }
		// area.setCode(area.getParent().getCode() +
		// StringUtils.leftPad(String.valueOf(size > 0 ? size : 1), 4, "0"));
		// }
		model.addAttribute("area", area);
		return "modules/sys/areaForm";
	}

	@RequiresPermissions(value = { "sys:area:add", "sys:area:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(Area area, Model model, RedirectAttributes redirectAttributes) {
		System.out.println(area.toString());
		// 获取区域底下最大code
		// 获取区域底下最大code
		String addCode = "";
		String str = "";
		String code = "10";
		String maxCode = "10";
		if (!"0".equals(area.getParentId())) {
			code = areaService.selectCode(area.getParentId());
			maxCode = areaService.maxCode(area.getParentId());
		}
		System.out.println(code);
		System.out.println(maxCode);
		if (maxCode != null) {
			str = maxCode.substring(maxCode.length() - 2);
			System.out.println(str);
		}
		if (maxCode == null) {
			addCode = code + "01";
		} else if (str.equals("99")) {
			// addMessage(redirectAttributes, "保存区域'" + area.getName() + "'失败");
			return "redirect:" + adminPath + "/sys/area/";
		} else {
			addCode = code + String.format("%02d", (Long.parseLong(str) + 1));
		}
		System.out.println(addCode + "------addcode");
		// 修改区域时候变更父id改变code
		Area parentArea = areaService.get(area.getParentId());
		String type = (Integer.parseInt(parentArea.getType()) + 1) + "";
		area.setType(type);
		if (StringUtils.isNotBlank(area.getId())) {
			Area a = areaService.get(area.getId());
			if (!a.getParentId().equals(area.getParentId())) {
				area.setCode(addCode);				
			}
		} else {
			area.setCode(addCode);
		}
		areaService.save(area);
		addMessage(redirectAttributes, "保存区域'" + area.getName() + "'成功");
		return "redirect:" + adminPath + "/sys/area/";
	}

	
	@RequiresPermissions("sys:area:del")
	@RequestMapping(value = "delete")
	public String delete(Area area, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/area";
		}
		// if (Area.isRoot(id)){
		// addMessage(redirectAttributes, "删除区域失败, 不允许删除顶级区域或编号为空");
		// }else{
		areaService.delete(area);
		addMessage(redirectAttributes, "删除区域成功");
		// }
		return "redirect:" + adminPath + "/sys/area/";
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId,
			HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Area> list = areaService.findAll();
		for (int i = 0; i < list.size(); i++) {
			Area e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId())
					&& e.getParentIds().indexOf("," + extId + ",") == -1)) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeDataUser")
	public List<Map<String, Object>> treeDataUser(@RequestParam(required = false) String extId,
			HttpServletResponse response) {
		String orgId = UserUtils.getUser().getArea().getId();		
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Area> list = areaService.userOrgList(orgId);
		for (int i = 0; i < list.size(); i++) {
			Area e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId())
					&& e.getParentIds().indexOf("," + extId + ",") == -1)) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
}
