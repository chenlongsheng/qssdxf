package cn.cdsoft.modules.maintenance.service;

import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.modules.maintenance.dao.PdfMaintenanceDao;
import cn.cdsoft.modules.maintenance.dao.PdfMaintenanceDetailDao;
import cn.cdsoft.modules.maintenance.dao.PdfUserMaintenanceMessDao;
import cn.cdsoft.modules.maintenance.entity.PdfUserMaintenanceMess;
import cn.cdsoft.common.service.CrudService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018-12-25.
 */
@Service
@Transactional(readOnly = true)
public class PdfUserMaintenanceMessService extends CrudService<PdfUserMaintenanceMessDao, PdfUserMaintenanceMess> {
	@Autowired
    PdfMaintenanceDao pdfMaintenanceDao;
	@Autowired
    PdfMaintenanceDetailDao pdfMaintenanceDetailDao;
	@Autowired
	PdfUserMaintenanceMessDao pdfUserMaintenanceMessDao;
	@Autowired
	private PdfUserService pdfUserService;

	public Page<PdfUserMaintenanceMess> findPage(Page<PdfUserMaintenanceMess> page,
                                                 PdfUserMaintenanceMess pddfUserMaintenanceMess) {

		pddfUserMaintenanceMess.setPage(page);
		List<PdfUserMaintenanceMess> list = pdfUserMaintenanceMessDao.messagelist(pddfUserMaintenanceMess);
		for (PdfUserMaintenanceMess pdfUserMaintenanceMess : list) {

			String orgParentId = pdfUserMaintenanceMess.getOrgParentId();
			String orgParentName = pdfUserService.orgName(orgParentId);
			pdfUserMaintenanceMess.setOrgParentName(orgParentName);
		}
		page.setList(list);
		return page;
	}

	// 我方人员信息
	public List<MapEntity> userDetail(PdfUserMaintenanceMess pdfUserMaintenanceMess) {

		return pdfUserMaintenanceMessDao.userDetail(pdfUserMaintenanceMess);

	}

	// 维保方人员信息
	public List<MapEntity> maintenanceDetail(PdfUserMaintenanceMess pdfUserMaintenanceMess) {

		return pdfUserMaintenanceMessDao.maintenanceDetail(pdfUserMaintenanceMess);

	}

	// 我方排序改变
	@Transactional(readOnly = false)
	public Integer changeUserOrder(MapEntity entity) {

		return pdfUserMaintenanceMessDao.changeUserOrder(entity);
	}

	// 维包方排序改变
	@Transactional(readOnly = false)
	public Integer changeMainOrder(MapEntity entity) {

		return pdfUserMaintenanceMessDao.changeMainOrder(entity);
	}

}
