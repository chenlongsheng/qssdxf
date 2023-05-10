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
import cn.cdsoft.modules.settings.entity.CdzChargePot;
import cn.cdsoft.modules.settings.entity.CpCarpark;
import cn.cdsoft.modules.settings.entity.TChannel;
import cn.cdsoft.modules.settings.entity.TDevice;
import cn.cdsoft.modules.settings.entity.TOrgExecUnitBind;
import cn.cdsoft.modules.settings.service.TOrgExecUnitBindService;

/**
 * 运营管理Controller
 * @author long
 * @version 2018-08-17
 */
@Controller
@RequestMapping(value = "${adminPath}/settings/tOrgExecUnitBind")
public class TOrgExecUnitBindController extends BaseController {

	@Autowired
	private TOrgExecUnitBindService tOrgExecUnitBindService;
	
//	@ModelAttribute("tOrgExecUnitBind")
//	public MapEntity get(@RequestParam(required=false) String id) {
//		MapEntity entity = null;
//		if (StringUtils.isNotBlank(id)){
//			entity = tOrgExecUnitBindService.getExecUnit(id);
//		}
//		if (entity == null){
//			entity = new MapEntity();
//		}
//		return entity;
//	}
//	
	/**
	 * 运营单元绑定列表页面
	 */
	@RequiresPermissions("settings:tOrgExecUnitBind:list")
	@RequestMapping(value = {"list", ""})
	public String list(String id ,String orgName,String execUnitId,String execUnitName,String signLogo, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (signLogo == null) {
			request.getSession().removeAttribute("TOid");
			request.getSession().removeAttribute("TOorgName");
			request.getSession().removeAttribute("TOexecUnitId");
			request.getSession().removeAttribute("TOexecUnitName");
		}
		if (orgName != null || execUnitId != null ) {
			request.getSession().setAttribute("TOid", id);
			request.getSession().setAttribute("TOorgName", orgName);
			request.getSession().setAttribute("TOexecUnitId", execUnitId);
			request.getSession().setAttribute("TOexecUnitName", execUnitName);
		}
		if (signLogo != null) {
			id = (String) request.getSession().getAttribute("TOid");
			orgName = (String) request.getSession().getAttribute("TOorgName");
			execUnitId = (String) request.getSession().getAttribute("TOexecUnitId");
			execUnitName = (String) request.getSession().getAttribute("TOexecUnitName");
		}
		model.addAttribute("id",id);
		model.addAttribute("orgName",orgName);
		model.addAttribute("execUnitId",execUnitId);
		model.addAttribute("execUnitName",execUnitName);
		MapEntity entity = new MapEntity();
		entity.put("id",id);
		entity.put("orgName", orgName);
		entity.put("execUnitId", execUnitId);
		entity.put("execUnitName", execUnitName);
		Page<MapEntity> page = tOrgExecUnitBindService.findBindPage(new Page<MapEntity>(request, response), entity); 
		model.addAttribute("page", page);
		return "modules/settings/tOrgExecUnitBindList";
	}

	/**
	 * 查看，增加，编辑运营单元绑定表单页面
	 */
	@RequiresPermissions(value={"settings:tOrgExecUnitBind:view","settings:tOrgExecUnitBind:add"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(@RequestParam(required=false) String id, Model model) {
		TOrgExecUnitBind entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tOrgExecUnitBindService.get(id);
		}
		if (entity == null){
			entity = new TOrgExecUnitBind();
		}
		model.addAttribute("tOrgExecUnitBind", entity);
		return "modules/settings/tOrgExecUnitBindForm";
	}

	@RequiresPermissions(value={"settings:tOrgExecUnitBind:edit"},logical=Logical.OR)
	@RequestMapping(value = "formEdit")
	public String formEdit(@RequestParam(required=false) String id, Model model) {
		TOrgExecUnitBind entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tOrgExecUnitBindService.get(id);
		}
		if (entity == null){
			entity = new TOrgExecUnitBind();
		}
		model.addAttribute("tOrgExecUnitBind", entity);
		return "modules/settings/tOrgExecUnitBindFormEdit";
	}
	/**
	 * 保存运营单元绑定
	 */
	@RequiresPermissions(value={"settings:tOrgExecUnitBind:add","settings:tOrgExecUnitBind:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TOrgExecUnitBind tOrgExecUnitBind, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tOrgExecUnitBind)){
			return form(tOrgExecUnitBind.getId(), model);
		}
		if(!tOrgExecUnitBind.getIsNewRecord()){//编辑表单保存
			TOrgExecUnitBind t = tOrgExecUnitBindService.get(tOrgExecUnitBind.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tOrgExecUnitBind, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tOrgExecUnitBindService.save(t);//保存
		}else{//新增表单保存
			tOrgExecUnitBindService.save(tOrgExecUnitBind);//保存
		}
		addMessage(redirectAttributes, "保存运营单元绑定成功");
		return "redirect:"+Global.getAdminPath()+"/settings/tOrgExecUnitBind/?repage&signLogo=0";
	}
	
	/**
	 * 删除运营单元绑定
	 */
	@RequiresPermissions("settings:tOrgExecUnitBind:del")
	@RequestMapping(value = "delete")
	public String delete(TOrgExecUnitBind tOrgExecUnitBind, RedirectAttributes redirectAttributes) {
		tOrgExecUnitBindService.delete(tOrgExecUnitBind);
		addMessage(redirectAttributes, "删除运营单元绑定成功");
		return "redirect:"+Global.getAdminPath()+"/settings/tOrgExecUnitBind/?repage&signLogo=0";
	}
	
	/**
	 * 批量删除运营单元绑定
	 */
	@RequiresPermissions("settings:tOrgExecUnitBind:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tOrgExecUnitBindService.delete(tOrgExecUnitBindService.get(id));
		}
		addMessage(redirectAttributes, "删除运营单元绑定成功");
		return "redirect:"+Global.getAdminPath()+"/settings/tOrgExecUnitBind/?repage&signLogo=0";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("settings:tOrgExecUnitBind:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TOrgExecUnitBind tOrgExecUnitBind, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "运营单元绑定"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TOrgExecUnitBind> page = tOrgExecUnitBindService.findPage(new Page<TOrgExecUnitBind>(request, response, -1), tOrgExecUnitBind);
    		new ExportExcel("运营单元绑定", TOrgExecUnitBind.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出运营单元绑定记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/settings/tOrgExecUnitBind/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("settings:tOrgExecUnitBind:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TOrgExecUnitBind> list = ei.getDataList(TOrgExecUnitBind.class);
			for (TOrgExecUnitBind tOrgExecUnitBind : list){
				try{
					tOrgExecUnitBindService.save(tOrgExecUnitBind);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条运营单元绑定记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条运营单元绑定记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入运营单元绑定失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/settings/tOrgExecUnitBind/?repage";
    }
	
	/**
	 * 下载导入运营单元绑定数据模板
	 */
	@RequiresPermissions("settings:tOrgExecUnitBind:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "运营单元绑定数据导入模板.xlsx";
    		List<TOrgExecUnitBind> list = Lists.newArrayList(); 
    		new ExportExcel("运营单元绑定数据", TOrgExecUnitBind.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/settings/tOrgExecUnitBind/?repage";
    }
	
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, 
			HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		System.out.println(extId+"---extId");
//添加停车场
		List<CpCarpark> list = tOrgExecUnitBindService.carParkList();
		CpCarpark cpCarpark = new CpCarpark();
		cpCarpark.setId("0");
		cpCarpark.setName("停车场");		
	    list.add(cpCarpark);
		for (int i=0; i<list.size(); i++){
			CpCarpark e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()))){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());			
				map.put("pId", "0");
//				map.put("pIds", "0,"+e.getId());
				map.put("name", e.getName());	
//				map.put("isParent", true);
				mapList.add(map);
			}
		}
		//添加充电点
		List<CdzChargePot> changeList = tOrgExecUnitBindService.cdzChargePotList();
		CdzChargePot cdzChargePot = new CdzChargePot();
		cdzChargePot.setId("1");
		cdzChargePot.setName("充电点");
		changeList.add(cdzChargePot);
		for (int i = 0; i < changeList.size(); i++) {
			CdzChargePot  e = changeList.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()))){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());			
				map.put("pId", "1");				
				map.put("name", e.getName());		
				mapList.add(map);
			}
		}
		return mapList;
	}
	
}