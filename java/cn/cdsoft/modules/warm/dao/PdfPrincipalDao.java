package cn.cdsoft.modules.warm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface PdfPrincipalDao {

//	String getPrincipalPhoneByPdfId(@Param("pdfId") String pdfId); 

	List<MapEntity> getPrincipalPhoneByTime();

	// 潮安发送短信
	List<String> getPrincipalPhoneByPdfId(@Param("pdfId") String pdfId, @Param("chId") String chId,
			@Param("devId") String devId);
	
	
	void insertMessageLog(MapEntity entity);
	
	Integer checkExpirationTime(@Param("status") String status, @Param("chId") String chId, @Param("level") String level);
	
	String checkExpirationByChId(@Param("chId") String chId);
	
	Integer getCodeCount(@Param("chId") String chId);
	
	
 

}
