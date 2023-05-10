/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.settings.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;


import cn.cdsoft.common.persistence.CrudDao;
import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.annotation.MyBatisDao;
import cn.cdsoft.modules.settings.entity.TDevice;
import cn.cdsoft.modules.settings.entity.TIdLinkId;
import cn.cdsoft.modules.sys.entity.Area;

/**
 * 联动管理DAO接口
 * @author long
 * @version 2018-08-08
 */
@MyBatisDao
public interface TIdLinkIdDao extends CrudDao<TIdLinkId> {

	//t_code中的name集合==5
		public List<MapEntity> codeList();
		
		public List<MapEntity> TIdLinkIdList(MapEntity entity);
		
		//更改启用禁用
		public Integer saveUse(TIdLinkId tIdLinkId);
		
		//区域  通道底下
		public List<Area> findAreaList(@Param(value="name")String name);

		//获取联动通道视频列表
		public List<MapEntity> getVideoChannelLinkList(@Param(value="channelSrcId")String channelSrcId);
		
}