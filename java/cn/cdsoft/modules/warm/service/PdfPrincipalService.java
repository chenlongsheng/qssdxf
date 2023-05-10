package cn.cdsoft.modules.warm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.modules.warm.dao.PdfPrincipalDao;

@Service
public class PdfPrincipalService {

	@Autowired
	PdfPrincipalDao pdfPrincipalDao;

	public List<String> getPrincipalPhoneByPdfId(String pdfId, String chId, String devId) {

		return pdfPrincipalDao.getPrincipalPhoneByPdfId(pdfId, chId, devId);
	}

	@Transactional(readOnly = false)
	public void insertMessageLog(MapEntity entity) {
		try {
			pdfPrincipalDao.insertMessageLog(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer checkExpirationTime(String status, String chId, String level) {
		return pdfPrincipalDao.checkExpirationTime(status, chId, level);
	}

	public String checkExpirationByChId(String chId) {
		return pdfPrincipalDao.checkExpirationByChId(chId);

	}

	public Integer getCodeCount(String chId) {
		return pdfPrincipalDao.getCodeCount(chId);

	}

}
