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
import cn.cdsoft.modules.settings.entity.CpCarpark;
import cn.cdsoft.modules.settings.entity.TChannel;
import cn.cdsoft.modules.settings.entity.TDevice;
import cn.cdsoft.modules.settings.entity.TOrgExecUnitBind;
import cn.cdsoft.modules.settings.service.TOrgExecUnitBindService;
import cn.cdsoft.modules.sys.entity.Area;

/**
 * 运营单元管理Controller
 * @author long
 * @version 2018-08-17
 */
@Controller
@RequestMapping(value = "${adminPath}/settings/tOrgExec")
public class TOrgExecController extends BaseController {

	@Autowired
	private TOrgExecUnitBindService tOrgExecUnitBindService;
	
	
	/**
	 * 运营单元管理列表页面
	 */
	@RequiresPermissions("settings:tOrgExec:list")
	@RequestMapping(value = {"list", ""})
	public String list(String orgName,String execUnitId,String signLogo, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (signLogo == null) {
			request.getSession().removeAttribute("orgName");
			request.getSession().removeAttribute("execUnitId");
		}
		if (orgName != null || execUnitId != null ) {
			request.getSession().setAttribute("orgName", orgName);
			request.getSession().setAttribute("execUnitId", execUnitId);
		}
		if (signLogo != null) {
			orgName = (String) request.getSession().getAttribute("orgName");
			execUnitId = (String) request.getSession().getAttribute("execUnitId");
		}
		model.addAttribute("orgName",orgName);
		model.addAttribute("execUnitId",execUnitId);
		TOrgExecUnitBind entity = new TOrgExecUnitBind();
		entity.setOrgName(orgName);
		entity.setExecUnitId(execUnitId);
		Page<TOrgExecUnitBind> page = tOrgExecUnitBindService.findPage(new Page<TOrgExecUnitBind>(request, response), entity); 
		model.addAttribute("page", page);
		return "modules/settings/tOrgExecList";
	}
	

	
	/**
	 * 查看，增加，编辑运营单元管理表单页面
	 */
	@RequiresPermissions(value={"settings:tOrgExec:view","settings:tOrgExec:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(@RequestParam(required=false) String orgId,String id, Model model) {
		TOrgExecUnitBind entity = null;
		
		if (StringUtils.isNotBlank(id)){
			entity = tOrgExecUnitBindService.get(id);
			
		}if (StringUtils.isBlank(id)&&StringUtils.isNotBlank(orgId)) {
			entity = tOrgExecUnitBindService.getTorg(id,orgId);			
		}
		if (entity == null){
			entity = new TOrgExecUnitBind();
		}
		model.addAttribute("tOrgExecUnitBind", entity);
		return "modules/settings/tOrgExecForm";
	}

	/**
	 * 保存运营单元管理
	 */
	@RequiresPermissions(value={"settings:tOrgExec:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TOrgExecUnitBind tOrgExecUnitBind, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tOrgExecUnitBind)){
			return form(tOrgExecUnitBind.getId(),tOrgExecUnitBind.getOrgId(), model);
		}
		if(!tOrgExecUnitBind.getIsNewRecord()){//编辑表单保存
			TOrgExecUnitBind t = tOrgExecUnitBindService.get(tOrgExecUnitBind.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tOrgExecUnitBind, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tOrgExecUnitBindService.save(t);//保存
		}else{//新增表单保存
			tOrgExecUnitBindService.save(tOrgExecUnitBind);//保存
		}
		addMessage(redirectAttributes, "保存运营单元管理成功");
		return "redirect:"+Global.getAdminPath()+"/settings/tOrgExec/?repage&signLogo=0";
	}
	
	/**
	 * 删除运营单元管理
	 */
	@RequiresPermissions("settings:tOrgExec:del")
	@RequestMapping(value = "delete")
	public String delete(TOrgExecUnitBind tOrgExecUnitBind, RedirectAttributes redirectAttributes) {
		tOrgExecUnitBindService.delete(tOrgExecUnitBind);
		addMessage(redirectAttributes, "删除运营单元管理成功");
		return "redirect:"+Global.getAdminPath()+"/settings/tOrgExec/?repage&signLogo=0";
	}
	
	/**
	 * 批量删除运营单元管理
	 */
	@RequiresPermissions("settings:tOrgExec:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tOrgExecUnitBindService.delete(tOrgExecUnitBindService.get(id));
		}
		addMessage(redirectAttributes, "删除运营单元管理成功");
		return "redirect:"+Global.getAdminPath()+"/settings/tOrgExec/?repage&signLogo=0";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("settings:tOrgExec:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TOrgExecUnitBind tOrgExecUnitBind, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "运营单元管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TOrgExecUnitBind> page = tOrgExecUnitBindService.findPage(new Page<TOrgExecUnitBind>(request, response, -1), tOrgExecUnitBind);
    		new ExportExcel("运营单元管理", TOrgExecUnitBind.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出运营单元管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/settings/tOrgExec/?repage";
    }

	/**
	 * 导入Excel数据
	 */
//	@RequiresPermissions("settings:tOrgExec:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    @ResponseBody
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
			 
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条运营单元管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入运营单元管理失败！失败信息："+e.getMessage());
		}
		return "";
    }   
    
	
    
	/**
	 * 下载导入运营单元管理数据模板
	 */	
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "运营单元管理数据导入模板.xlsx";
    		List<TOrgExecUnitBind> list = Lists.newArrayList(); 
    		new ExportExcel("运营单元管理数据", TOrgExecUnitBind.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/settings/tOrgExec/?repage";
    }
	
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Area> list = tOrgExecUnitBindService.findAreaList();
		for (int i=0; i<list.size(); i++){
			Area e = list.get(i);			
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				map.put("type", e.getType());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
}