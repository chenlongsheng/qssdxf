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
import cn.cdsoft.common.utils.ServletUtils;
import cn.cdsoft.common.utils.StringUtils;
import cn.cdsoft.common.utils.excel.ExportExcel;
import cn.cdsoft.common.utils.excel.ImportExcel;
import cn.cdsoft.common.web.BaseController;
import cn.cdsoft.modules.settings.entity.TChannel;
import cn.cdsoft.modules.settings.entity.TCode;
import cn.cdsoft.modules.settings.entity.TDevice;
import cn.cdsoft.modules.settings.entity.TIdLinkId;
import cn.cdsoft.modules.settings.service.TChannelService;
import cn.cdsoft.modules.settings.service.TCodeService;
import cn.cdsoft.modules.settings.service.TIdLinkIdService;
import cn.cdsoft.modules.sys.entity.Area;

/**
 * 联动管理Controller
 * @author long
 * @version 2018-08-08
 */
@Controller
@RequestMapping(value = "${adminPath}/settings/tIdLinkId")
public class TIdLinkIdController extends BaseController {

	@Autowired
	private TIdLinkIdService tIdLinkIdService;
	@Autowired
	private TChannelService tChannelService;
	@Autowired
	private TCodeService tCodeService;
	
	@ModelAttribute("tIdLinkId")
	public TIdLinkId get(@RequestParam(required=false) String id) {
		TIdLinkId entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tIdLinkIdService.get(id);
		}
		if (entity == null){
			entity = new TIdLinkId();
		}
		return entity;
	}
	
	@RequestMapping(value = {"getLinkList"})
	@ResponseBody
	public String getVideoChannelLinkList(String channelSrcId) {
		
		List<MapEntity> linkChannelList = tIdLinkIdService.getVideoChannelLinkList(channelSrcId);
		return ServletUtils.buildRs(true, "", linkChannelList);
	}
	
	/**
	 * 联动配置列表页面
	 */
	@RequiresPermissions("settings:tIdLinkId:list")
	@RequestMapping(value = {"list", ""})
	public String list(String tChannelSrcId,String tChannelDestId,
			String linkType,String notUse,String signLogo,HttpServletRequest request, HttpServletResponse response, Model model) {
	    
		
		/*
		if(signLogo == null) {
        	request.getSession().removeAttribute("tChannelName");
			request.getSession().removeAttribute("tChannelDestName");
			request.getSession().removeAttribute("linkType");
			request.getSession().removeAttribute("linkNotUse");
        }
		if (tChannelName != null || tChannelDestName != null || linkType != null||notUse!=null) {			
			request.getSession().setAttribute("tChannelName", tChannelName);
			request.getSession().setAttribute("tChannelDestName", tChannelDestName);
			request.getSession().setAttribute("linkType", linkType);
			request.getSession().setAttribute("linkNotUse", notUse);
		}
		if (signLogo != null) {
			tChannelName = (String) request.getSession().getAttribute("tChannelName");
			tChannelDestName = (String) request.getSession().getAttribute("tChannelDestName");
			linkType = (String) request.getSession().getAttribute("linkType");
			notUse = (String) request.getSession().getAttribute("linkNotUse");
		}
		*/
		
		if(tChannelSrcId != null && !tChannelSrcId.equals("")) {
			TChannel srcChannel = tChannelService.get(tChannelSrcId);
			model.addAttribute("tChannelSrcName",srcChannel.getName());
		}

		if(tChannelDestId != null && !tChannelDestId.equals("")) {
			TChannel destChannel = tChannelService.get(tChannelDestId);
			model.addAttribute("tChannelDestName",destChannel.getName());
		}
		
		model.addAttribute("tChannelSrcId",tChannelSrcId);
		model.addAttribute("tChannelDestId",tChannelDestId);

		model.addAttribute("linkType",linkType);
		model.addAttribute("notUse",notUse);
		
		List<MapEntity> codeList = tIdLinkIdService.codeList();
		model.addAttribute("codeList", codeList);	
		
		MapEntity entity = new MapEntity();
		entity.put("tChannelSrcId",tChannelSrcId);
		entity.put("tChannelDestId",tChannelDestId);
		entity.put("linkType",linkType);
		entity.put("notUse",notUse);
		Page<MapEntity> page = tIdLinkIdService.findPage(new Page<MapEntity>(request, response), entity); 
		model.addAttribute("page", page);
		return "modules/settings/tIdLinkIdList";
	}

	/**
	 * 查看，增加，编辑联动配置表单页面
	 */
	@RequiresPermissions(value={"settings:tIdLinkId:view","settings:tIdLinkId:add","settings:tIdLinkId:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(@ModelAttribute("tIdLinkId")TIdLinkId tIdLinkId, Model model) {
		
		List<MapEntity> codeList = tIdLinkIdService.codeList();
		model.addAttribute("codeList", codeList);	
		model.addAttribute("tIdLinkId", tIdLinkId);
		return "modules/settings/tIdLinkIdForm";
	}
    
	/**
	 * 保存联动配置
	 */
	@RequiresPermissions(value={"settings:tIdLinkId:add","settings:tIdLinkId:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TIdLinkId tIdLinkId, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tIdLinkId)){
			return form(tIdLinkId, model);
		} 
		if(!tIdLinkId.getIsNewRecord()){//编辑表单保存
			TIdLinkId t = tIdLinkIdService.get(tIdLinkId.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tIdLinkId, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tIdLinkIdService.save(t);//保存
		}else{//新增表单保存			
			tIdLinkIdService.save(tIdLinkId);//保存
		}
		addMessage(redirectAttributes, "保存联动配置成功");
		return "redirect:"+Global.getAdminPath()+"/settings/tIdLinkId/?repage&signLogo=0";
	}
	
	
	@RequestMapping(value = "saveUse")
	public String saveUse(TIdLinkId tIdLinkId, Model model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws Exception {
		
		int t = tIdLinkIdService.saveUse(tIdLinkId);// 从数据库取出记录的值
		System.out.println(t + "-----价格");
		return "redirect:" + Global.getAdminPath() + "/settings/tIdLinkId/list/?repage&signLogo=0";
	}
	/**
	 * 删除联动配置
	 */
	@RequiresPermissions("settings:tIdLinkId:del")
	@RequestMapping(value = "delete")
	public String delete(TIdLinkId tIdLinkId, RedirectAttributes redirectAttributes) {
		tIdLinkIdService.delete(tIdLinkId);
		addMessage(redirectAttributes, "删除联动配置成功");
		return "redirect:"+Global.getAdminPath()+"/settings/tIdLinkId/?repage&signLogo=0";
	}
	
	/**
	 * 批量删除联动配置
	 */
	@RequiresPermissions("settings:tIdLinkId:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tIdLinkIdService.delete(tIdLinkIdService.get(id));
		}
		addMessage(redirectAttributes, "删除联动配置成功");
		return "redirect:"+Global.getAdminPath()+"/settings/tIdLinkId/?repage&signLogo=0";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("settings:tIdLinkId:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TIdLinkId tIdLinkId, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "联动配置"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TIdLinkId> page = tIdLinkIdService.findPage(new Page<TIdLinkId>(request, response, -1), tIdLinkId);
    		new ExportExcel("联动配置", TIdLinkId.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出联动配置记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/settings/tIdLinkId/?repage";
    }

	/**
	 * 导入Excel数据
	 */
	@RequiresPermissions("settings:tIdLinkId:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TIdLinkId> list = ei.getDataList(TIdLinkId.class);
			for (TIdLinkId tIdLinkId : list){
				try{
					tIdLinkIdService.save(tIdLinkId);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条联动配置记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条联动配置记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入联动配置失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/settings/tIdLinkId/?repage";
    }
	
	/**
	 * 下载导入联动配置数据模板
	 */
	@RequiresPermissions("settings:tIdLinkId:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "联动配置数据导入模板.xlsx";
    		List<TIdLinkId> list = Lists.newArrayList(); 
    		new ExportExcel("联动配置数据", TIdLinkId.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/settings/tIdLinkId/?repage";
    }
//	
//	@RequiresPermissions("user")
//	@ResponseBody
//	@RequestMapping(value = "treeData")
//	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, 
//			@RequestParam(required=false) String channelName, HttpServletResponse response) {
//		List<Map<String, Object>> mapList = Lists.newArrayList();
//		System.out.println(channelName+"====channelName");
//		System.out.println(extId+"---extId");
//		//查询
//		TChannel tChannel = new TChannel();
//		tChannel.setName(channelName);		
//		List<TChannel> list = tChannelService.findList(tChannel);
//		for (int i=0; i<list.size(); i++){
//			TChannel e = list.get(i);
//			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()))){
//				Map<String, Object> map = Maps.newHashMap();
//				map.put("id", e.getId());
//				map.put("name", e.getName());				
//				mapList.add(map);
//			}
//		}
//		return mapList;
//	}
//	
	//区域的通道底下----最新
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId,
			@RequestParam(required=false) String channelName,HttpServletResponse response) {
		System.out.println(extId+"------extId");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Area> list = tIdLinkIdService.findAreaList(channelName);
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