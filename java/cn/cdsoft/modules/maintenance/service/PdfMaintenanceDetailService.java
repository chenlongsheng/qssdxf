package cn.cdsoft.modules.maintenance.service;

import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.service.CrudService;
import cn.cdsoft.modules.maintenance.dao.PdfMaintenanceAptitudeDao;
import cn.cdsoft.modules.maintenance.dao.PdfMaintenanceDao;
import cn.cdsoft.modules.maintenance.dao.PdfMaintenanceDetailDao;
import cn.cdsoft.modules.maintenance.entity.PdfMaintenanceDetail;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2018-12-25.
 */
@Service
@Transactional(readOnly = true)
public class PdfMaintenanceDetailService extends CrudService<PdfMaintenanceDetailDao, PdfMaintenanceDetail> {

	@Autowired
	PdfMaintenanceDao pdfMaintenanceDao;
	@Autowired
	PdfMaintenanceDetailDao pdfMaintenanceDetailDao;
	@Autowired
	PdfMaintenanceAptitudeDao pdfMaintenanceAptitudeDao;
	@Autowired
	private PdfUserService pdfUserService;

	// 公司明细
	public List<PdfMaintenanceDetail> selectMaintenanceDetail(String maintenanceId) {

		List<PdfMaintenanceDetail> list = pdfMaintenanceDetailDao.selectMaintenanceDetail(maintenanceId);
		for (PdfMaintenanceDetail pdfMaintenanceDetail : list) {
			
			List<MapEntity> strList = pdfMaintenanceDetailDao.codeMainList(pdfMaintenanceDetail.getOrgParentId(),
					pdfMaintenanceDetail.getId());
			String orgParentName = pdfUserService.orgName(pdfMaintenanceDetail.getOrgParentId());
			pdfMaintenanceDetail.setOrgParentName(orgParentName);
			pdfMaintenanceDetail.setList(strList);
		}
		return list;
	}

	// 公司明细
	public List<MapEntity> selectCodeList(String mainDatailId) {
		System.out.println(mainDatailId);
		List<MapEntity> list = pdfMaintenanceDetailDao.selectCodeList(mainDatailId);
		for (MapEntity mapEntity : list) {
			String orgParentId = (String) mapEntity.get("orgParentId");
//			System.out.println("===orgParentId===" + orgParentId);
			String orgParentName = pdfUserService.orgName(orgParentId);
			mapEntity.put("orgParentName", orgParentName);
		}
		return list;
	}
	//公司明细集合
	public List<MapEntity> getMainDetailList(String id){
		
		return pdfMaintenanceDetailDao.getMainDetailList(id);
		
	}

}
