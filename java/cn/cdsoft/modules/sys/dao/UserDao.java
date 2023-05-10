/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.sys.dao;

import java.util.List;
import java.util.Set;

import cn.cdsoft.modules.sys.entity.User;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import cn.cdsoft.common.persistence.CrudDao;
import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.annotation.MyBatisDao;

/**
 * 用户DAO接口
 * 
 * @author jeeplus
 * @version 2014-05-16
 */
@MyBatisDao
public interface UserDao extends CrudDao<User> {

	/**
	 * 根据登录名称查询用户
	 * 
	 * @param loginName
	 * @return
	 */
	public User getByLoginName(User user);

	/**
	 * 通过OfficeId获取用户列表，仅返回用户id和name（树查询用户时用）
	 * 
	 * @param user
	 * @return
	 */
	public List<User> findUserByOfficeId(User user);

	/**
	 * 查询全部用户数目
	 * 
	 * @return
	 */
	public long findAllCount(User user);

	/**
	 * 更新用户密码
	 * 
	 * @param user
	 * @return
	 */
	public int updatePasswordById(User user);

	/**
	 * 更新登录信息，如：登录IP、登录时间
	 * 
	 * @param user
	 * @return
	 */
	public int updateLoginInfo(User user);

	/**
	 * 删除用户角色关联数据
	 * 
	 * @param user
	 * @return
	 */
	public int deleteUserRole(User user);

	/**
	 * 插入用户角色关联数据
	 * 
	 * @param user
	 * @return
	 */
	public int insertUserRole(User user);

	// 改造的
	public int saveUserRole(@Param(value = "userId") String userId, @Param(value = "roleId") String roleId);

	/**
	 * 更新用户信息
	 * 
	 * @param user
	 * @return
	 */
	public int updateUserInfo(User user);

	/**
	 * 插入好友
	 */
	public int insertFriend(@Param("id") String id, @Param("userId") String userId, @Param("friendId") String friendId);

	/**
	 * 查找好友
	 */
	public User findFriend(@Param("userId") String userId, @Param("friendId") String friendId);

	/**
	 * 删除好友
	 */
	public void deleteFriend(@Param("userId") String userId, @Param("friendId") String friendId);

	/**
	 * 
	 * 获取我的好友列表
	 * 
	 */
	public List<User> findFriends(User currentUser);

	/**
	 * 
	 * 查询用户-->用来添加到常用联系人
	 * 
	 */
	public List<User> searchUsers(User user);

	/**
	 * 
	 */

	public List<User> findListByOffice(User user);

	// 用户获取的role的id
	public String userRole(@Param(value = "userId") String userId);

	MapEntity login(@Param(value = "loginName") String loginName, @Param(value = "password") String password);

	// 配电房所有list
	List<MapEntity> orgElecList(@Param(value = "orgId") String orgId);

	Integer insertUserOrg(MapEntity mapEntity);

	Integer deleteUserElec(@Param(value = "userId") String userId);

	// 用户底下的可见配电房
	List<MapEntity> selectElecList(@Param(value = "userId") String userId);

	// 分页展示的配电房id
	List<MapEntity> userElecList(@Param(value = "userId") String userId);

	// 更新外键id
	Integer foreignId(@Param(value = "foreignId") String foreignId, @Param(value = "userId") String userId);

	Integer userType(@Param(value = "userType") String userType, @Param(value = "userId") String userId);

	// 获取userorg表orgId集合
	List<String> userOrgList(@Param(value = "userId") String userId);

	// 所有配电房父级的id
	String parentIds(@Param(value = "orgId") String orgId);

	List<MapEntity> orgList(@Param(value = "parentIds") String parentIds);

	// 所有角色
	List<MapEntity> roleNameList();

	// 所有区域的list
	List<MapEntity> orgAll(@Param(value = "orgId") String orgId);

	// 父级的父级id
	String orgParentId(@Param(value = "parentId") String parentId);

	// 添加修改中的区域集合
	Set<MapEntity> orgEditList(@Param(value = "orgIds") String orgIds);

	// 分页加入集合显示
	List<String> OrgNameList(@Param(value = "userId") String userId);

	Integer resetPassword(@Param(value = "userId") String userId, @Param(value = "password") String password);

	List<MapEntity> userAreaList(@Param(value = "orgId") String orgId);

	/**
	 * 根据用户查询权限列表
	 * @param userId
	 * @return
	 */
    List<JSONObject> findMenuListByUser(@Param("userId") String userId);
}
