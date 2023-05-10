/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.maintenance.web;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.cdsoft.common.utils.MyBeanUtils;
import cn.cdsoft.common.utils.ServletUtils;
import cn.cdsoft.common.utils.StringUtils;
import cn.cdsoft.common.web.BaseController;
import cn.cdsoft.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.modules.maintenance.entity.PdfUser;
import cn.cdsoft.modules.maintenance.service.PdfUserService;

/**
 * 我方人员管理Controller
 * 
 * @author long
 * @version 2019-01-09
 */
@Controller
@RequestMapping(value = "${adminPath}/maintain/pdfUser")
public class PdfUserController extends BaseController {

	@Autowired
	private PdfUserService pdfUserService;

	/**
	 * 我方人员管理列表页面
	 */
	// @RequiresPermissions("maintain:pdfUser:list")
	@RequestMapping(value = { "list", "" })
	@ResponseBody
	public String list(@ModelAttribute("pdfUser") PdfUser pdfUser, HttpServletRequest request,
			HttpServletResponse response, Model model) {

		Page<PdfUser> page = pdfUserService.findPage(new Page<PdfUser>(request, response), pdfUser);
		return ServletUtils.buildRs(true, "我方人员分页", page);
	}

	// 查询分页前条件回调---取消
	@RequestMapping(value = { "orgEditList" })
	@ResponseBody
	public String orgEditList(String name) {
		String orgId = UserUtils.getUser().getArea().getId();
		System.out.println("查询分页前回调函数===");
		Set<MapEntity> set = pdfUserService.orgEditList(name, orgId);
		return ServletUtils.buildRs(true, "查询条件管辖区域", set);
	}

	// 删除职位
	@RequestMapping(value = { "deletePosition" })
	@ResponseBody
	public String deletePosition(String ids) {
		String[] str = ids.split(",");
		try {
			for (int i = 0; i < str.length; i++) {
				pdfUserService.deletePosition(str[i]);
			}
		} catch (Exception e) {
			return ServletUtils.buildRs(false, "删除失败", null);
		}
		return ServletUtils.buildRs(true, "删除成功", null);
	}

	// 添加职位
	@RequestMapping(value = { "insertPosition" })
	@ResponseBody
	public String insertPosition(String name) {
		MapEntity entity = null;
		try {
			entity = pdfUserService.insertPosition(name);
			System.out.println(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return ServletUtils.buildRs(false, "添加失败", null);
		}
		return ServletUtils.buildRs(true, "添加成功", entity);
	}

	// 添加中,选择辖区后,所有配电房显示
	@RequestMapping(value = { "elceListById" })
	@ResponseBody
	public String elceListById(String orgId, String name) {

		List<MapEntity> elceList = pdfUserService.elceList(orgId, name);
		return ServletUtils.buildRs(true, "查询辖区下配电房", elceList);

	}

	// 查询分页前条件回调
	@RequestMapping(value = { "message" })
	@ResponseBody
	public String message(String orgName, String orgDepartName) {
		String orgId = UserUtils.getUser().getArea().getId();
		System.out.println(orgId);
		MapEntity entity = new MapEntity();

		List<MapEntity> tcodeList = pdfUserService.tcodeList(null);// 设备类型
		List<MapEntity> posiList = pdfUserService.posiList();// 职位集合
		
		Set<MapEntity> orgList = pdfUserService.orgList(orgName, orgId);// 配电房集合
//		Set<MapEntity> orgDepartList = pdfUserService.orgDepartList(orgDepartName, orgId);// 电业局集合
        
		entity.put("tcodeList", tcodeList);// 设备类型烟感等
		entity.put("posiList", posiList);// 职位渲染
		entity.put("orgList", orgList);// 配电房的区域list
//		entity.put("orgDepartList", orgDepartList);// 电业局的集合

		return ServletUtils.buildRs(true, "查询条件需求", entity);
	}

	/**
	 * 查看，增加，编辑我方人员管理表单页面
	 */
	// @RequiresPermissions(value = { "maintain:pdfUser:view",
	// "maintain:pdfUser:add",
	// "maintain:pdfUser:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	@ResponseBody
	public String form(String id, String orgDepartName, String orgName) {
		String orgId = UserUtils.getUser().getArea().getId();
		MapEntity entity = new MapEntity();
		List<MapEntity> tcodeList = pdfUserService.tcodeList(null);
		List<MapEntity> posiList = pdfUserService.posiList();
		Set<MapEntity> orgList = pdfUserService.orgEditList(orgName, orgId);
//		Set<MapEntity> orgDepartList = pdfUserService.orgDepartList(orgDepartName, orgId);
		entity.put("tcodeList", tcodeList);// 设备类型烟感等
		entity.put("posiList", posiList);// 职位渲染
		entity.put("orgList", orgList);// 管辖区域list
//		entity.put("orgDepartList", orgDepartList);// 电业局的集合
		PdfUser pdfUser = null;
		if (StringUtils.isNotBlank(id)) {
			pdfUser = pdfUserService.get(id);
			List<MapEntity> list = pdfUserService.details(pdfUser.getId());
			entity.put("userDetails", list);
		} else {
			pdfUser = new PdfUser();
		}
		entity.put("pdfUser", pdfUser);// 用户
		return ServletUtils.buildRs(true, "我方人员回调", entity);
	}

	// 管辖设备类型,按名称搜索
	@RequestMapping(value = "serchCode")
	@ResponseBody
	public String serchCode(String name) {
        
		List<MapEntity> tcodeList = pdfUserService.tcodeList(name);
		return ServletUtils.buildRs(true, "按名称查询设备类型", tcodeList);
        
	}

	/**
	 * 保存我方人员管理
	 */
	// @RequiresPermissions(value = { "maintain:pdfUser:add",
	// "maintain:pdfUser:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	@ResponseBody
	public String save(PdfUser pdfUser, String userOrgList) throws Exception {
		// String orgIds,String ourUserId,String orgParentId
		System.out.println(userOrgList);
		String list = userOrgList.replace("&quot;", "'");
		JSONArray ja = JSONArray.parseArray(list);
		try {
			if (!pdfUser.getIsNewRecord()) {// 编辑表单保存
				PdfUser t = pdfUserService.get(pdfUser.getId());// 从数据库取出记录的值
				MyBeanUtils.copyBeanNotNull2Bean(pdfUser, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值
				pdfUserService.save(t, ja);// 保存
			} else {// 新增表单保存
				pdfUserService.save(pdfUser, ja);// 保存
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ServletUtils.buildRs(false, "保存我方人员管理失败", null);
		}
		return ServletUtils.buildRs(true, "保存我方人员管理成功", null);
	}

	// 用户详情
	@RequestMapping(value = "userDetails")
	@ResponseBody
	public String userDetails(String userId) {
        
		List<MapEntity> list = pdfUserService.details(userId);
		return ServletUtils.buildRs(true, "用户详情", list);
	}

	/**
	 * 删除我方人员管理
	 */
	// @RequiresPermissions("maintain:pdfUser:del")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(PdfUser pdfUser) {
		System.out.println("删除我方人员");
		try {
			pdfUserService.delete(pdfUser);
		} catch (Exception e) {
			e.printStackTrace();
			return ServletUtils.buildRs(false, "删除我方人员管理失败", null);
		}
		return ServletUtils.buildRs(true, "删除我方人员管理成功", null);
	}

}