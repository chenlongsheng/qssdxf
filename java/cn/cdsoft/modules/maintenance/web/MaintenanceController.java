package cn.cdsoft.modules.maintenance.web;

import cn.cdsoft.modules.maintenance.service.PdfMaintenanceDetailService;
import com.alibaba.fastjson.JSONArray;
import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.common.utils.ServletUtils;
import cn.cdsoft.common.utils.StringUtils;
import cn.cdsoft.modules.maintenance.entity.PdfMaintenance;
import cn.cdsoft.modules.maintenance.entity.PdfMaintenanceDetail;
import cn.cdsoft.modules.maintenance.service.PdfMaintenanceService;
import cn.cdsoft.modules.maintenance.service.PdfUserService;
import cn.cdsoft.modules.sys.utils.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Administrator on 2018-12-25.
 */
@Controller
@RequestMapping(value = "${adminPath}/maintain/maintenance")
public class MaintenanceController {
	@Value("${ipFile}")
	private String ipFile;
	@Autowired
	private PdfMaintenanceService pdfMaintenanceService;
	@Autowired
	private PdfMaintenanceDetailService pdfMaintenanceDetailService;
	@Autowired
	private PdfUserService pdfUserService;
	
	// 分页查询前的查询条件
	@ResponseBody
	@RequestMapping(value = "/message")
	public String message(String orgName, HttpServletRequest request, HttpServletResponse response) {
		MapEntity entity = new MapEntity();

		String orgId = UserUtils.getUser().getArea().getId();
		System.out.println(orgId + "---用户区域id");

		Set<MapEntity> orgList = pdfUserService.orgEditList(orgName, orgId);
		List<MapEntity> tcodeList = pdfUserService.tcodeList(null);
		entity.put("orgList", orgList);
		entity.put("tcodeList", tcodeList);
		return ServletUtils.buildRs(true, "维保人员分页查询条件", entity);
	}

	// 维保分页显示
	@ResponseBody
	@RequestMapping(value = "/list")
	public String list(PdfMaintenance pdfMaintenance, HttpServletRequest request, HttpServletResponse response) {
		// System.out.println(pdfMaintenance.toString());
		Page<PdfMaintenance> page = pdfMaintenanceService.findPage(new Page<PdfMaintenance>(request, response),
				pdfMaintenance);
		return ServletUtils.buildRs(true, "维保人员分页", page);
	}

	// 维保人员详情
	@ResponseBody
	@RequestMapping(value = "/maintenanceDetail")
	public String maintenanceDetail(String maintenanceId) {
		List<PdfMaintenanceDetail> list = pdfMaintenanceDetailService.selectMaintenanceDetail(maintenanceId);
		return ServletUtils.buildRs(true, "维保人员详情", list);
	}

	// 保存图片资质
	public String saveFile(HttpServletRequest request, MultipartFile file, PdfMaintenance maintenance) {
		String path = request.getSession().getServletContext().getRealPath("");
		// 修改图片的uuid
		String image = UUID.randomUUID()
				+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		String filePath = "/upload/" + image;
		// 拼接图片的路径
		System.out.println("url=====" + path + filePath);
		File saveDir = new File(path + filePath);
		if (!saveDir.getParentFile().exists()) {
			saveDir.getParentFile().mkdirs();
		}
		try {
			file.transferTo(saveDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;
	}

	// 添加/修改回调
	@ResponseBody
	@RequestMapping(value = "/form")
	public String form(String id, String orgName) {
		String orgId = UserUtils.getUser().getArea().getId();
		MapEntity entity = new MapEntity();
		PdfMaintenance pdfMaintenance = new PdfMaintenance();
		if (StringUtils.isNotBlank(id)) {
			pdfMaintenance = pdfMaintenanceService.get(id);
			List<PdfMaintenanceDetail> mainDetailList = pdfMaintenanceDetailService.selectMaintenanceDetail(id);
			// List<MapEntity> mainDetailList =
			// pdfMaintenanceDetailService.getMainDetailList(id);
			entity.put("mainDetailList", mainDetailList);
		}
		List<MapEntity> tcodeList = pdfUserService.tcodeList(null);
		Set<MapEntity> orgList = pdfUserService.orgEditList(orgName, orgId);
		entity.put("tcodeList", tcodeList);
		entity.put("orgList", orgList);
		entity.put("PdfMaintenance", pdfMaintenance);
		return ServletUtils.buildRs(true, "添加/修改回调成功", entity);
	}

	// 添加维保单位
	@ResponseBody
	@RequestMapping(value = "/addMaintenance")
	public String addMaintenance(@RequestParam(value = "images", required = false) MultipartFile[] images,
			String mainUserList, PdfMaintenance maintenance, HttpServletRequest request) {
		System.out.println("添加维保单位进来的===========");
		System.out.println(maintenance.toString());
		JSONArray ja = null;
		try {
			if (StringUtils.isNotBlank(mainUserList)) {
				String list = mainUserList.replace("&quot;", "'");// 替换json的乱码
				System.out.println(mainUserList);
				ja = JSONArray.parseArray(list);
			}
			if (images != null && images.length > 0) {// 遍历文件
				for (int i = 0; i < images.length; i++) {
					MultipartFile file = images[i];
					// 保存文件
					String url = saveFile(request, file, maintenance);
					maintenance.getUrl().add(url);
				}
			}
			if (maintenance.getId() == null) {
				pdfMaintenanceService.addMaintenance(maintenance, ja);// 添加
			} else {
				pdfMaintenanceService.editMaintenance(maintenance, ja, request);// 修改
			}
		} catch (Exception e) {
			return ServletUtils.buildRs(false, "添加/修改失败", null);
		}
		return ServletUtils.buildRs(true, "添加/修改成功", null);
	}

	// 下载公司图片资质地址
	@ResponseBody
	@RequestMapping(value = "/getPic")
	public String getPic(String id) {
		List<String> li = new ArrayList<>();
		List<String> list = pdfMaintenanceService.findPicList(id);
		for (String str : list) {
			str = ipFile + str;
			li.add(str);
			System.out.println(str);// 获取图片全部路径
		}
		return ServletUtils.buildRs(true, "获取资质集合成功", li);
	}

	// 分页中的维保详情
	@ResponseBody
	@RequestMapping(value = "/maintenDetail")
	public String maintenDetail(String maintenanceId) {
		System.out.println("维保详情中的公司id===" + maintenanceId);
		List<PdfMaintenanceDetail> 	list = pdfMaintenanceDetailService.selectMaintenanceDetail(maintenanceId);
		
		System.out.println("结束");
		return ServletUtils.buildRs(true, "维保公司详情", list);
	}
	
	// 详情中的维保人员管辖的类型集合----------------------------
	@ResponseBody
	@RequestMapping(value = "/selectCodeList")
	public String selectCodeList(String maintenDetailId) {
		// maintenDetailId改成是维保人姓名
		// 维保联系人详情---按名字查询
		List<MapEntity> list = pdfMaintenanceDetailService.selectCodeList(maintenDetailId);		
		return ServletUtils.buildRs(true, "维保公司详情", list);
	}
    

	// 删除维保单位
	@ResponseBody
	@RequestMapping(value = "/delMaintenanceById")
	public String delMaintenanceById(String id, HttpServletRequest request) {
		System.out.println("删除维保单位接口===");
		String path = request.getSession().getServletContext().getRealPath("");
		
		try {
			pdfMaintenanceService.delMaintenanceById(id, request);			
		} catch (Exception e) {
			e.printStackTrace();
			return ServletUtils.buildRs(false, "删除失败", null);
		}
		return ServletUtils.buildRs(true, "删除成功", null);
	}

}
