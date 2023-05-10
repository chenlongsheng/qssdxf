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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;

import cn.cdsoft.common.config.Global;
import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.common.utils.DateUtils;
import cn.cdsoft.common.utils.MyBeanUtils;
import cn.cdsoft.common.utils.StringUtils;
import cn.cdsoft.common.utils.excel.ExportExcel;
import cn.cdsoft.common.utils.excel.ImportExcel;
import cn.cdsoft.common.web.BaseController;
import cn.cdsoft.modules.settings.entity.TCodeType;
import cn.cdsoft.modules.settings.service.TCodeTypeService;

/**
 * CodeType管理Controller
 * @author ywk
 * @version 2018-06-22
 */
@Controller
@RequestMapping(value = "${adminPath}/settings/tCodeType")
public class TCodeTypeController extends BaseController {

	@Autowired
	private TCodeTypeService tCodeTypeService;
	
	@ModelAttribute("tCodeType")
	public TCodeType get(@RequestParam(required=false) String id) {
		TCodeType entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tCodeTypeService.get(id);
		}
		if (entity == null){
			entity = new TCodeType();
		}
		return entity;
	}
	
	/**
	 * CodeType列表页面
	 */
	@RequiresPermissions("settings:tCodeType:list")
	@RequestMapping(value = {"list", ""})
	public String list(@ModelAttribute("tCodeType")TCodeType tCodeType, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TCodeType> page = tCodeTypeService.findPage(new Page<TCodeType>(request, response), tCodeType); 
		System.out.println(tCodeType.toString()+"-----jjjj");
		model.addAttribute("page", page);
		return "modules/settings/tCodeTypeList";
	}
    
	/**
	 * 查看，增加，编辑CodeType表单页面
	 */
	@RequiresPermissions(value={"settings:tCodeType:view","settings:tCodeType:add","settings:tCodeType:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(@ModelAttribute("tCodeType")TCodeType tCodeType, Model model) {
		System.out.println(tCodeType.toString()+"-----jjjj");
		model.addAttribute("tCodeType", tCodeType);
		return "modules/settings/tCodeTypeForm";
	}
    
	/**
	 * 保存CodeType
	 */
	@RequiresPermissions(value={"settings:tCodeType:add","settings:tCodeType:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TCodeType tCodeType, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tCodeType)){
			return form(tCodeType, model);
		}
		if(!tCodeType.getIsNewRecord()){//编辑表单保存
			TCodeType t = tCodeTypeService.get(tCodeType.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tCodeType, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tCodeTypeService.save(t);//保存
		}else{//新增表单保存
			tCodeTypeService.save(tCodeType);//保存
		}
		addMessage(redirectAttributes, "保存字典类型成功");
		return "redirect:"+Global.getAdminPath()+"/settings/tCodeType/?repage";
	}
	
	/**
	 * 删除CodeType
	 */
	@RequiresPermissions("settings:tCodeType:del")
	@RequestMapping(value = "delete")
	public String delete(TCodeType tCodeType, RedirectAttributes redirectAttributes) {
		tCodeTypeService.delete(tCodeType);
		addMessage(redirectAttributes, "删除字典类型成功");
		return "redirect:"+Global.getAdminPath()+"/settings/tCodeType/?repage";
	}
	
	/**
	 * 批量删除CodeType
	 */
	@RequiresPermissions("settings:tCodeType:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tCodeTypeService.delete(tCodeTypeService.get(id));
		}
		addMessage(redirectAttributes, "删除字典类型成功");
		return "redirect:"+Global.getAdminPath()+"/settings/tCodeType/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("settings:tCodeType:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TCodeType tCodeType, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "CodeType"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TCodeType> page = tCodeTypeService.findPage(new Page<TCodeType>(request, response, -1), tCodeType);
    		new ExportExcel("CodeType", TCodeType.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出字典类型记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/settings/tCodeType/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("settings:tCodeType:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TCodeType> list = ei.getDataList(TCodeType.class);
			for (TCodeType tCodeType : list){
				try{
					tCodeTypeService.save(tCodeType);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条CodeType记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条CodeType记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入CodeType失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/settings/tCodeType/?repage";
    }
	
	/**
	 * 下载导入CodeType数据模板
	 */
	@RequiresPermissions("settings:tCodeType:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "CodeType数据导入模板.xlsx";
    		List<TCodeType> list = Lists.newArrayList(); 
    		new ExportExcel("CodeType数据", TCodeType.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/settings/tCodeType/?repage";
    }
	
	
	

}