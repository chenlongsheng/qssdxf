/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.settings.dao;

import cn.cdsoft.common.persistence.CrudDao;
import cn.cdsoft.common.persistence.annotation.MyBatisDao;
import cn.cdsoft.modules.settings.entity.TDevice;
import cn.cdsoft.modules.settings.entity.TDeviceDetail;

/**
 * 数据配置DAO接口
 * @author long
 * @version 2018-07-24
 */
@MyBatisDao
public interface TDeviceDetailDao extends CrudDao<TDeviceDetail> {

	
}