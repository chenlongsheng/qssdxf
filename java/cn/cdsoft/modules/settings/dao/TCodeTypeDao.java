/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.settings.dao;

import cn.cdsoft.common.persistence.CrudDao;
import cn.cdsoft.common.persistence.annotation.MyBatisDao;
import cn.cdsoft.modules.settings.entity.TCodeType;

/**
 * CodeType管理DAO接口
 * @author ywk
 * @version 2018-06-22
 */
@MyBatisDao
public interface TCodeTypeDao extends CrudDao<TCodeType> {

	
}