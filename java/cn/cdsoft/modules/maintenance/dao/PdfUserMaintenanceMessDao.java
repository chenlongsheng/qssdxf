package cn.cdsoft.modules.maintenance.dao;

import cn.cdsoft.common.persistence.CrudDao;
import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.annotation.MyBatisDao;
import cn.cdsoft.modules.maintenance.entity.PdfUserMaintenanceMess;

import java.util.List;

/**
 * Created by Administrator on 2018-12-25.
 */
@MyBatisDao
public interface PdfUserMaintenanceMessDao  extends CrudDao<PdfUserMaintenanceMess>   {
    //分页查询
	List<PdfUserMaintenanceMess> messagelist(PdfUserMaintenanceMess pdfUserMaintenanceMess);
	
	List<MapEntity> userDetail(PdfUserMaintenanceMess pdfUserMaintenanceMess);
	
	List<MapEntity> maintenanceDetail(PdfUserMaintenanceMess pdfUserMaintenanceMess);

//改变我方的排序
	Integer changeUserOrder(MapEntity entity);
	//改变维保排序
	Integer changeMainOrder(MapEntity entity);
	
	
}
