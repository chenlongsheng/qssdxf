/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.settings.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import cn.cdsoft.common.persistence.CrudDao;
import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.annotation.MyBatisDao;
import cn.cdsoft.modules.settings.entity.TChannel;
import cn.cdsoft.modules.settings.entity.TDevice;
import cn.cdsoft.modules.settings.entity.TDeviceDetail;
import cn.cdsoft.modules.sys.entity.Area;

/**
 * 设备管理DAO接口
 * @author long
 * @version 2018-07-24
 */
@MyBatisDao
public interface TDeviceDao extends CrudDao<TDevice> {
	
	public MapEntity getDeviceEn(String id);

	public List<MapEntity> deviceList(MapEntity entity);
	
	public List<TDevice> deviceAllList();
	
    
	//t_code中的name集合==1
	public List<MapEntity> codeList();
	
   //	删除t_device_detail
	public int deleteDetail(TDevice tDevice);
	
    public int insert(TDeviceDetail tDeviceDetail);
	
	public int update(TDeviceDetail tDeviceDetail);
	//更新t_channel的orgId
	public int updateChannel(@Param(value ="orgId")String orgId,@Param(value ="devId")String devId);
//	public List<MapEntity> nameList(@Param(value ="nameList")String nameList);
	//获取from表单字段
	public MapEntity getDeviceFrom(@Param(value ="id")String id,@Param(value ="deviceFromList")String deviceFromList);
	//更改启用禁用
	public Integer saveUse(TDevice tDevice);
	//修改设备图片
	public Integer updatePic(@Param(value ="id")String id,@Param(value ="picturePath")String picturePath);
	//获取区域底下设备小图标
	public List<MapEntity> devicePic(@Param(value ="orgId")String orgId,@Param(value ="coldId")String coldId);
	
	Integer updateCoords(@Param(value ="id")String id,@Param(value ="coordX")String coordX,@Param(value ="coordY")String coordY);
	

}