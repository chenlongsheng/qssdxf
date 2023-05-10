/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.maintenance.dao;

import java.util.List;
import java.util.Set;

import cn.cdsoft.common.persistence.CrudDao;
import org.apache.ibatis.annotations.Param;

import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.annotation.MyBatisDao;
import cn.cdsoft.modules.maintenance.entity.PdfUser;

/**
 * 我方人员管理DAO接口
 * 
 * @author long
 * @version 2019-01-09
 */
@MyBatisDao
public interface PdfUserDao extends CrudDao<PdfUser> {
	// 查询的配电房配电局集合
	Set<MapEntity> orgList(@Param(value = "name") String name,@Param(value = "code") String code,@Param(value ="orgId") String orgId);
	//电业局集合
	Set<MapEntity> orgDepartList(@Param(value = "name") String name,@Param(value = "code") String code,@Param(value ="orgId") String orgId);
	//添加修改中的区域集合
	Set<MapEntity> orgEditList(@Param(value ="orgIds") String orgIds); 
	// 我方员职位集合
	List<MapEntity> posiList();

	// 所有设备通道集合
	List<MapEntity> tcodeList(@Param(value = "name") String name);

	// 删除职位
	Integer deletePosition(@Param(value = "id") String id);

	// 添加职位
	Integer insertPosition(MapEntity entity);

	// 插入用户的所属电业局
	Integer saveElec(MapEntity entity);

	// 辖区下的配电房
	List<MapEntity> elceList(@Param(value = "orgId") String orgId, @Param(value = "name") String name);

	// 删除用户第一,二责任人,当用户删除时候
//	Integer deleteCodeUser(@Param(value = "userId") String userId);

	// 添加用户的配电房
	Integer saveUserOrg(MapEntity entity);

	// 添加设备类型责任人
	Integer insertCodeOrg(@Param(value = "id") String id, @Param(value = "codeId") String codeId,
			@Param(value = "typeId") String typeId, @Param(value = "userId") String userId,
			@Param(value = "OrgId") String OrgId, @Param(value = "orderNo") Integer orderNo,
			 @Param(value = "updateDate") String updateDate);

	// 删除用户下所有配电房
	Integer deleteUserOrg(@Param(value = "userId") String userId);
	
	

	// 删除设备类型责任人或者按时间
	Integer deleteCodeOrg(@Param(value = "userId") String userId,@Param(value = "time") String time);

	// 获取最大责任人no
	String maxOrderNo(@Param(value = "codeId") String codeId, @Param(value = "typeId") String typeId,
			@Param(value = "orgId") String orgId);

	// 获取父id和name
	MapEntity parentList(@Param(value = "orgId") String orgId);
	//详情
	List<MapEntity> details(@Param(value = "userId") String userId);
	
	Integer updateDate(@Param(value = "newDate") String newDate,@Param(value = "codeId") String codeId, @Param(value = "typeId") String typeId,
			@Param(value = "orgId") String orgId,@Param(value = "userId") String userId);
    //配电房的的typeid=6集合
	List<MapEntity> elecList(@Param(value = "code") String code);
	//父级的父级id
	String orgParentId(@Param(value = "parentId") String parentId);
	//所有配电房父级的id
	String parentIds(@Param(value = "orgId") String orgId);
	//
	String parentId(@Param(value = "orgId") String orgId);
	
	//删除中修改第二维保人为第一维保人
	List<MapEntity> selectCodeOrgList(@Param(value = "userId") String userId);
	
	List<MapEntity> secondOrder(@Param(value = "codeId") Integer codeId, @Param(value = "typeId") Integer typeId,
			@Param(value = "orgId") Long orgId);
	
	Integer updateOrder(@Param(value = "id") String id);
	
	
	
	
	
	
	
	
	
	
}