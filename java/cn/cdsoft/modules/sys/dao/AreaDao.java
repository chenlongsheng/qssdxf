/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.cdsoft.common.persistence.TreeDao;
import cn.cdsoft.common.persistence.annotation.MyBatisDao;
import cn.cdsoft.modules.sys.entity.Area;

/**
 * 区域DAO接口
 * 
 * @author jeeplus
 * @version 2014-05-16
 */
@MyBatisDao
public interface AreaDao extends TreeDao<Area> {

	public int saveImage(Area area);

	// 区域user下的集合
	public List<Area> userOrgList(@Param(value = "orgId") String orgId);

	// 区域code获取
	public String selectCode(@Param(value = "orgId") String orgId);

	// 区域底下子集的最大code获取
	public String maxCode(@Param(value = "orgId") String orgId);
	
	public Integer updateSonOrg(@Param(value = "type") Integer type, @Param(value = "parentId") String parentId);


}
