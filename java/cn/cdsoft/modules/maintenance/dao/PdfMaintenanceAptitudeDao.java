package cn.cdsoft.modules.maintenance.dao;

import cn.cdsoft.common.persistence.CrudDao;
import cn.cdsoft.common.persistence.annotation.MyBatisDao;
import cn.cdsoft.modules.maintenance.entity.PdfMaintenanceAptitude;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 2018-12-26.
 */
@MyBatisDao
public interface PdfMaintenanceAptitudeDao extends CrudDao<PdfMaintenanceAptitude> {
    void delByMaintId(@Param(value = "maintenanceId") String maintenanceId);
}
