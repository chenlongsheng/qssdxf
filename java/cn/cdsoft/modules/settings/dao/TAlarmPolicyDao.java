/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.settings.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.cdsoft.common.persistence.CrudDao;
import cn.cdsoft.common.persistence.annotation.MyBatisDao;
import cn.cdsoft.modules.settings.entity.TAlarmPolicy;
import cn.cdsoft.modules.settings.entity.TCode;
import cn.cdsoft.modules.settings.entity.TCodeType;

/**
 * 数据配置DAO接口
 * @author long
 * @version 2018-10-23
 */
@MyBatisDao
public interface TAlarmPolicyDao extends CrudDao<TAlarmPolicy> {
    //获取级别和获取通道子类型3
	public List<TCode> codeList(@Param(value="codeTypeId")String codeTypeId);
	//通道主类型集合
	public List<TCodeType> codeTypeList();

	
	
}