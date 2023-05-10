/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.settings.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.cdsoft.common.persistence.CrudDao;
import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.annotation.MyBatisDao;
import cn.cdsoft.modules.settings.entity.TCode;

/**
 * code管理DAO接口
 * @author long
 * @version 2018-08-09
 */
@MyBatisDao
public interface TCodeDao extends CrudDao<TCode> {

	public List<TCode> findCodeList(TCode entity);
	
	public TCode getCode(@Param(value="id")String id,@Param(value="typeId")String typeId);
	

}