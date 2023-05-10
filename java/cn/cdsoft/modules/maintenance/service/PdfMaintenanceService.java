package cn.cdsoft.modules.maintenance.service;

import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.common.utils.DateUtils;
import cn.cdsoft.common.utils.IdGenSnowFlake;
import cn.cdsoft.common.utils.StringUtils;
import cn.cdsoft.modules.maintenance.dao.PdfMaintenanceAptitudeDao;
import cn.cdsoft.modules.maintenance.dao.PdfMaintenanceDao;
import cn.cdsoft.modules.maintenance.dao.PdfMaintenanceDetailDao;
import cn.cdsoft.modules.maintenance.entity.PdfMaintenance;
import cn.cdsoft.modules.maintenance.entity.PdfMaintenanceAptitude;
import cn.cdsoft.modules.maintenance.entity.PdfMaintenanceCode;
import cn.cdsoft.modules.maintenance.entity.PdfMaintenanceDetail;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.cdsoft.common.service.CrudService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2018-12-25.
 */
@Service
@Transactional(readOnly = true)
public class PdfMaintenanceService extends CrudService<PdfMaintenanceDao, PdfMaintenance> {
	@Autowired
	PdfMaintenanceDao pdfMaintenanceDao;
	@Autowired
    PdfMaintenanceDetailDao pdfMaintenanceDetailDao;
	@Autowired
    PdfMaintenanceAptitudeDao pdfMaintenanceAptitudeDao;

	public PdfMaintenance get(String id) {

		return dao.get(id);
	}

	// 修改维保单位
	@Transactional(readOnly = false)
	public void editMaintenance(PdfMaintenance maintenance, JSONArray ja, HttpServletRequest request) {
		// 添加维保单位
		System.out.println(maintenance.toString());
		maintenance.preUpdate();
		pdfMaintenanceDao.update(maintenance);

		System.out.println("公司id======================" + maintenance.getId());

		// 删除已经存在单位详细再添加
		pdfMaintenanceDetailDao.delByMaintId(maintenance.getId());
		// 添加维保单位详细
		String newDate = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
		for (int i = 0; i < ja.size(); i++) {
			PdfMaintenanceDetail detail = JSONObject.parseObject(ja.get(i).toString(), PdfMaintenanceDetail.class);
			// String oldDetailId = detail.getId();
			// pdfMaintenanceDetailDao.delByMaintId(oldDetailId);//
			if (StringUtils.isBlank(detail.getId())) {
				detail.preInsert();
			}
			System.out.println(detail.getId() + "===id");
			// System.out.println("datail=====" + detail.toString());
			// MapEntity en = pdfMaintenanceDetailDao.selectDetail(detail.getId());
			detail.setMaintenanceId(maintenance.getId());
			pdfMaintenanceDetailDao.insert(detail);
			// 获取管辖所有[配电房
			List<String> orgList = pdfMaintenanceDetailDao.selectOrgIds(detail.getOrgParentId());

			String codeIds = detail.getCodeIds();
			String[] IdsList = codeIds.split(",");// code类型和主类型
			for (String org : orgList) {
				for (String ids : IdsList) {
					String[] codes = ids.split("_");
					String codeId = codes[0];
					String typeId = codes[1];
					String main = pdfMaintenanceDetailDao.maxOrderNo(codeId, typeId, detail.getOrgParentId(),
							maintenance.getId());// 判断是否本公司已经有值
					System.out.println(main + "===main");
					String maxOrderNo = pdfMaintenanceDetailDao.maxOrderNo(codeId, typeId, detail.getOrgParentId(),
							null);

					System.out.println(maxOrderNo + "==========maxOrderNo");
					if (maxOrderNo == null) {
						maxOrderNo = "0";
					}
					int result = pdfMaintenanceDetailDao.updateMaintainDate(newDate, codeId, typeId,
							detail.getOrgParentId(), detail.getId());//

					System.out.println(result + "======jieguo");
					if (result <= 0) {
						int orderNo = Integer.parseInt(maxOrderNo);// 获取最大排序
						if (main == null) {
							orderNo = orderNo + 1;
						}
						System.out.println(orderNo + "===order");
						PdfMaintenanceCode pdfMaintenanceCode = new PdfMaintenanceCode(IdGenSnowFlake.uuid().toString(),
								maintenance.getId(), detail.getId(), detail.getOrgParentId(), org, codeId, typeId,
								orderNo + "", newDate, null);
						pdfMaintenanceDetailDao.saveMaintenanceCode(pdfMaintenanceCode);
					}
				}
			}
		}

		pdfMaintenanceDetailDao.deleteDetail(maintenance.getId(), newDate);
		if (maintenance.getUrl() != null) {

			deletePicUrl(request, maintenance.getId());// 删除物理图片
			// 删除已经存在资质重新添加
			pdfMaintenanceAptitudeDao.delByMaintId(maintenance.getId());

			// 添加资质
			List<String> urls = maintenance.getUrl();
			for (String url : urls) {
				System.out.println("url=========" + url);
				PdfMaintenanceAptitude aptitude = new PdfMaintenanceAptitude();
				aptitude.setUrl(url);
				aptitude.setMaintenanceId(maintenance.getId());
				aptitude.preInsert();
				pdfMaintenanceAptitudeDao.insert(aptitude);
			}
		}

	}

	public void deletePicUrl(HttpServletRequest request, String id) {
		// 删除物理文件
		String path = request.getSession().getServletContext().getRealPath("");
		List<String> list = this.findPicList(id);
		System.out.println("删除文件这个====");
		System.out.println(list.toString());
		for (String str : list) {
			str = path + str;
			System.out.println("物理文件信息===" + str);
			File file = new File(str);
			file.delete();
			System.out.println(str);
		}

	}

	// 添加维保单位
	@Transactional(readOnly = false)
	public void addMaintenance(PdfMaintenance maintenance, JSONArray ja) {
		// 添加维保单位
		System.out.println(maintenance.toString());
		maintenance.preInsert();
		pdfMaintenanceDao.saveMaintenance(maintenance);    
		// 删除已经存在单位详细再添加
		pdfMaintenanceDetailDao.delByMaintId(maintenance.getId());
		if (ja != null) {
			// 添加维保单位详细
			for (int i = 0; i < ja.size(); i++) {
				PdfMaintenanceDetail detail = JSONObject.parseObject(ja.get(i).toString(), PdfMaintenanceDetail.class);
				System.out.println("====nihao==" + detail.toString());
				if (StringUtils.isBlank(detail.getId())) {
					detail.preInsert();
					detail.setMaintenanceId(maintenance.getId());
				}
				System.out.println(detail.getId() + "===id");
				// System.out.println("datail=====" + detail.toString());
				String newDate = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");

				pdfMaintenanceDetailDao.insert(detail);
				// 获取管辖所有[配电房
				List<String> orgList = pdfMaintenanceDetailDao.selectOrgIds(detail.getOrgParentId());
				System.out.println("集合的size=======" + orgList.size());
				String codeIds = detail.getCodeIds();
				String[] IdsList = codeIds.split(",");// code类型和主类型
				for (String org : orgList) {
					System.out.println("org集合=====" + org.toString());

					for (String ids : IdsList) {
						String[] codes = ids.split("_");
						String codeId = codes[0];
						String typeId = codes[1];
						String main = pdfMaintenanceDetailDao.maxOrderNo(codeId, typeId, detail.getOrgParentId(),
								maintenance.getId());// 判断是否本公司已经有值
						System.out.println(main + "===main");
						String maxOrderNo = pdfMaintenanceDetailDao.maxOrderNo(codeId, typeId, detail.getOrgParentId(),
								null);
						System.out.println(maxOrderNo + "==========maxOrderNo");
						if (maxOrderNo == null) {
							maxOrderNo = "0";
						}
						// String newDate = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");

						// int result = pdfMaintenanceDetailDao.updateMaintainDate(newDate, codeId,
						// typeId,
						// detail.getOrgParentId(), detail.getId());
						// System.out.println(result + "======jieguo");
						//
						// if (result <= 0) {
						//
						int orderNo = Integer.parseInt(maxOrderNo);// 获取最大排序
						if (main == null) {
							orderNo = orderNo + 1;
						}
						System.out.println(orderNo + "===order");
						PdfMaintenanceCode pdfMaintenanceCode = new PdfMaintenanceCode(IdGenSnowFlake.uuid().toString(),
								maintenance.getId(), detail.getId(), detail.getOrgParentId(), org, codeId, typeId,
								orderNo + "", newDate, null);
						pdfMaintenanceDetailDao.saveMaintenanceCode(pdfMaintenanceCode);
						// }
					}
				}
			}
		}
		// 删除已经存在资质重新添加
		// pdfMaintenanceAptitudeDao.delByMaintId(maintenance.getId());
		// 添加资质
		List<String> urls = maintenance.getUrl();
		for (String url : urls) {
			System.out.println("url=========" + url);
			PdfMaintenanceAptitude aptitude = new PdfMaintenanceAptitude();
			aptitude.setUrl(url);
			aptitude.setMaintenanceId(maintenance.getId());
			aptitude.preInsert();
			pdfMaintenanceAptitudeDao.insert(aptitude);
		}
	}

	public Page<PdfMaintenance> findPage(Page<PdfMaintenance> page, PdfMaintenance entity) {
		String codeIds = entity.getCodeIds();
		if (codeIds != null && codeIds != "") {
			System.out.println(codeIds);
			String[] strs = codeIds.split(",");
			entity.setCodeId(strs[0]);
			entity.setTypeId(strs[1]);
		}

		entity.setPage(page);
		page.setList(pdfMaintenanceDao.findMaintenList(entity));
		return page;
	}

	public List<String> findPicList(String maintenanceId) {
		return pdfMaintenanceDao.findPicList(maintenanceId);

	}

	// 删除维保单位
	@Transactional(readOnly = false)
	public void delMaintenanceById(String id, HttpServletRequest request) {

		// 删除维保详细
		pdfMaintenanceDetailDao.delByMaintId(id);
		List<MapEntity> list = pdfMaintenanceDao.selectMainCodeList(id);
		for (MapEntity mapEntity : list) {
			int order = (int) mapEntity.get("orderNo");
			if (order == 1) {
				int codeId = (int) mapEntity.get("codeId");
				int typeId = (int) mapEntity.get("typeId");
				Long orgId = (Long) mapEntity.get("orgId");

				List<MapEntity> li = pdfMaintenanceDao.secondOrder(codeId, typeId, orgId);
				if (li != null) {
					for (MapEntity entity : li) {
						if (entity != null) {
							String mainId = (String) entity.get("id");
							int key = pdfMaintenanceDao.updateMainOrder(mainId);
							System.out.println(key);
						}
					}
				}
			}
		}
		// 删除维保设备类型
		pdfMaintenanceDetailDao.deleteDetail(id, null);
		this.deletePicUrl(request, id);
		// 删除维保信息
		pdfMaintenanceDao.deleteById(id);
		// 删除资质信息
		pdfMaintenanceAptitudeDao.delByMaintId(id);

	}

}
