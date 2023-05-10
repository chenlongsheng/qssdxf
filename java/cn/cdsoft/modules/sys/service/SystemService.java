/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.sys.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.cdsoft.common.service.ServiceException;
import cn.cdsoft.modules.sys.dao.RoleDao;
import cn.cdsoft.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.common.security.Digests;
import cn.cdsoft.common.service.BaseService;
import cn.cdsoft.common.utils.CacheUtils;
import cn.cdsoft.common.utils.Encodes;
import cn.cdsoft.common.utils.HttpUtils;
import cn.cdsoft.common.utils.IdGenSnowFlake;
import cn.cdsoft.common.utils.RSAEncrypUtil;
import cn.cdsoft.common.utils.StringUtils;
import cn.cdsoft.modules.sys.dao.MenuDao;
import cn.cdsoft.modules.sys.dao.UserDao;
import cn.cdsoft.modules.sys.entity.Menu;
import cn.cdsoft.modules.sys.entity.Office;
import cn.cdsoft.modules.sys.entity.Role;
import cn.cdsoft.modules.sys.entity.User;
import cn.cdsoft.modules.sys.utils.LogUtils;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * 
 * @author jeeplus
 * @version 2013-12-05
 */
@Service
@Transactional(readOnly = true)
public class SystemService extends BaseService implements InitializingBean {

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	@Value("${userPath}")
	private String userPath;
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private MenuDao menuDao;
	// @Autowired
	// private SessionDAO sessionDao;

	// @Autowired
	// private SystemAuthorizingRealm systemRealm;

	private JSONObject JSON = new JSONObject();

	// public SessionDAO getSessionDao() {
	// return sessionDao;
	// }

	// -- User Service --//

	/**
	 * 获取用户
	 * 
	 * @param id
	 * @return
	 */
	public User getUser(String id) {
		return UserUtils.get(id);
	}

	/**
	 * 根据登录名获取用户
	 * 
	 * @param loginName
	 * @return
	 */
	public User getUserByLoginName(String loginName) {
		return UserUtils.getByLoginName(loginName);
	}

	public Page<User> findUser(Page<User> page, User user) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
		// 设置分页参数
		user.setPage(page);
		// 执行分页查询
		page.setList(userDao.findList(user));
		return page;
	}

	public Page<Role> findPage(Page<Role> page, Role role) {
		role.setPage(page);
		List<Role> list = findRole(role);
		page.setList(list);
		return page;
	}

	/**
	 * 无分页查询人员列表
	 * 
	 * @param user
	 * @return
	 */
	public List<User> findUser(User user) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
		List<User> list = userDao.findList(user);
		return list;
	}

	/**
	 * 通过部门ID获取用户列表，仅返回用户id和name（树查询用户时用）
	 * 
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<User> findUserByOfficeId(String officeId) {
		List<User> list = (List<User>) CacheUtils.get(UserUtils.USER_CACHE,
				UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + officeId);
		if (list == null) {
			User user = new User();
			user.setOffice(new Office(officeId));
			list = userDao.findUserByOfficeId(user);
			CacheUtils.put(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + officeId, list);
		}
		return list;
	}

	@Transactional(readOnly = false)
	public void saveUser(User user) {
		if (StringUtils.isBlank(user.getId())) {
			user.preInsert();
			userDao.insert(user);

		} else {
			// 清除原用户机构用户缓存
			User oldUser = userDao.get(user.getId());
			if (oldUser.getOffice() != null && oldUser.getOffice().getId() != null) {
				CacheUtils.remove(UserUtils.USER_CACHE,
						UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + oldUser.getOffice().getId());
			}
			// 更新用户数据
			user.preUpdate();
			userDao.update(user);
		}
		if (StringUtils.isNotBlank(user.getId())) {
			// 更新用户与角色关联
			int key = userDao.deleteUserRole(user);

			if (user.getRoleList() != null && user.getRoleList().size() > 0) {
				int result = userDao.insertUserRole(user);
				System.out.println(result + "几回");
			}

			// if (user.getRole()!=null){
			// userDao.saveUserRole(null,user.getRole().getId());
			// }
			else {
				throw new ServiceException(user.getLoginName() + "没有设置角色！");
			}
			// 清除用户缓存
			UserUtils.clearCache(user);
			// // 清除权限缓存
			// systemRealm.clearAllCachedAuthorizationInfo();
		}
	}

	@Transactional(readOnly = false)
	public void updateUserInfo(User user) {
		user.preUpdate();
		userDao.updateUserInfo(user);
		// 清除用户缓存
		UserUtils.clearCache(user);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public void deleteUser(User user) {
		userDao.delete(user);
		// 清除用户缓存
		UserUtils.clearCache(user);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public void updatePasswordById(String id, String loginName, String newPassword) {
		User user = new User(id);
		user.setPassword(entryptPassword(newPassword));
		userDao.updatePasswordById(user);
		// 清除用户缓存
		user.setLoginName(loginName);
		UserUtils.clearCache(user);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public Integer resetPassword(String userId, String password) {

		int key = userDao.resetPassword(userId, password);
		return key;

	}

	@Transactional(readOnly = false)
	public void updateUserLoginInfo(User user) {
		// 保存上次登录信息
		user.setOldLoginIp(user.getLoginIp());
		user.setOldLoginDate(user.getLoginDate());
		// 更新本次登录信息
		user.setLoginIp(UserUtils.getSession().getHost());
		user.setLoginDate(new Date());
		userDao.updateLoginInfo(user);
	}

	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword) {
	    
	    
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword);
	}

	/**
	 * 验证密码
	 * 
	 * @param plainPassword
	 *            明文密码
	 * @param password
	 *            密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password) {	    
	   	    
		byte[] salt = Encodes.decodeHex(password.substring(0, 16));
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return password.equals(Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword));
	}

	/**
	 * 获得活动会话
	 * 
	 * @return
	 */
	// public Collection<Session> getActiveSessions() {
	// return sessionDao.getActiveSessions(false);
	// }

	// -- Role Service --//

	public Role getRole(String id) {
		return roleDao.get(id);
	}

	public Role getRoleByName(String name, String officeId) {
		Role r = new Role();
		r.setName(name);
		System.out.println(officeId + "======service");
		// Office office = new Office();
		// office.setId(officeId);
		//
		// r.setOffice(office);
		return roleDao.getByName(r);
	}

	public Role getRoleByEnname(String enname, String officeId) {
		Role r = new Role();
		r.setEnname(enname);
		Office office = new Office();
		office.setId(officeId);

		r.setOffice(office);
		return roleDao.getByEnname(r);
	}

	public List<Role> findRole(Role role) {
		return roleDao.findList(role);
	}

	public List<Role> findAllRole() {
		return UserUtils.getRoleList();
	}

	@Transactional(readOnly = false)
	public void saveAuth(Role role) {
		// 更新角色与菜单关联
		roleDao.deleteRoleMenu(role);
		if (role.getMenuList().size() > 0) {
			roleDao.insertRoleMenu(role);
		}
		// 清除用户角色缓存
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
	}

	@Transactional(readOnly = false)
	public void saveRole(Role role) {
		if (StringUtils.isBlank(role.getId())) {
			role.preInsert();
			roleDao.insert(role);
		} else {
			role.preUpdate();
			roleDao.update(role);
		}
		// 更新角色与菜单关联
		 roleDao.deleteRoleMenu(role);
		 if (role.getMenuList().size() > 0) {
		 roleDao.insertRoleMenu(role);
		 }
//		 更新角色与部门关联
		 roleDao.deleteRoleOffice(role);
//		 if (role.getOfficeList().size() > 0) {
		 roleDao.insertRoleOffice(role);
//		 }
		// 清除用户角色缓存
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
		 // 清除权限缓存
//		 systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public void deleteRole(Role role) {
		roleDao.delete(role);
		roleDao.deleteRoleOffice(role);
		// 清除用户角色缓存
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public Boolean outUserInRole(Role role, User user) throws Exception {
		List<Role> roles = user.getRoleList();
		for (Role e : roles) {
			if (e.getId().equals(role.getId())) {
				roles.remove(e);
				saveUser(user);
				return true;
			}
		}
		return false;
	}

	@Transactional(readOnly = false)
	public User assignUserToRole(Role role, User user) throws Exception {
		if (user == null) {
			return null;
		}
		List<String> roleIds = user.getRoleIdList();
		if (roleIds.contains(role.getId())) {
			return null;
		}
		user.getRoleList().add(role);
		saveUser(user);
		return user;
	}

	// -- Menu Service --//

	public Menu getMenu(String id) {
		return menuDao.get(id);
	}

	public List<Menu> findAllMenu() {
		return UserUtils.getMenuList();
	}

	@Transactional(readOnly = false)
	public void saveMenu(Menu menu) {

		// 获取父节点实体
		menu.setParent(this.getMenu(menu.getParent().getId()));

		// 获取修改前的parentIds，用于更新子节点的parentIds
		String oldParentIds = menu.getParentIds();

		// 设置新的父节点串
		menu.setParentIds(menu.getParent().getParentIds() + menu.getParent().getId() + ",");

		// 保存或更新实体
		if (StringUtils.isBlank(menu.getId())) {
			menu.preInsert();
			menuDao.insert(menu);
		} else {
			menu.preUpdate();
			menuDao.update(menu);
		}

		// 更新子节点 parentIds
		Menu m = new Menu();
		m.setParentIds("%," + menu.getId() + ",%");
		List<Menu> list = menuDao.findByParentIdsLike(m);
		for (Menu e : list) {
			e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
			menuDao.updateParentIds(e);
		}
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}

	@Transactional(readOnly = false)
	public void updateMenuSort(Menu menu) {
		menuDao.updateSort(menu);
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}

	@Transactional(readOnly = false)
	public void deleteMenu(Menu menu) {
		menuDao.delete(menu);
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}

	/**
	 * 获取Key加载信息
	 */
	public static boolean printKeyLoadMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n======================================================================\r\n");
		sb.append("\r\ncatalina.base = " + System.getProperty("catalina.base") + "\r\n");
		sb.append("\r\n======================================================================\r\n");
		System.out.println(sb.toString());
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	// 配电房所有集合
	public List<MapEntity> orgElecList(String orgId) {

		return userDao.orgElecList(orgId);

	}// 用户底下的可见配电房

	public List<MapEntity> selectElecList(String userId) {

		return userDao.selectElecList(userId);
	}

	// 分页展示的配电房id
	public List<MapEntity> userElecList(String userId) {

		return userElecList(userId);
	}

	@Transactional(readOnly = false)
	public Integer userType(String userType, String userId, String token) throws Exception {

		int key = userDao.userType(userType, userId);
		if (key > 0) {

			Map<String, Object> params = new HashMap<>();
			MapEntity entity = new MapEntity();
			String foreignId = userDao.get(userId).getForeignId();
			params.put("userId", foreignId);
			params.put("flag", userType);
			entity.put("token", token);
			String resultText = HttpUtils.sendPost(userPath + "/user/editEnableUser", params, entity);
			JSONObject resultData = JSON.parseObject(resultText);
			String result = resultData.getString("success");
			if (result.equals("false")) {
				throw new RuntimeException();
			}
			System.out.println(result + "===启用禁用==");
		} else {
			throw new RuntimeException();
		}
		return key;
	}

	public List<MapEntity> roleNameList() {

		return userDao.roleNameList();
	}

	public List<MapEntity> userAreaList(String orgId) {

		return userDao.userAreaList(orgId);
	}

	// 获取userId的树形配电房集合
	// public List<MapEntity> parentIds(String userId) {
	// String s = "";
	// Set<String> set = new HashSet<String>();
	//
	// List<String> parentIdList = userDao.parentIds(userId);
	// for (String strs : parentIdList) {
	// System.out.println(strs);
	// String[] str = strs.split(",");
	// for (int i = 0; i < str.length; i++) {
	// System.out.println(str);
	// if (str[i] != null) {
	// set.add(str[i]);
	// }
	// }
	// }
	// for (String string : set) {
	// s += string + ",";
	// }
	// if (s == "") {
	// return null;
	// }
	// s = s.substring(0, s.length() - 1);
	//
	// List<MapEntity> list = userDao.orgList(s);
	// for (int i = 0; i < list.size(); i++) {
	// System.out.println(list.get(i).toString());
	// }
	// return list;
	// }

	// 获取修改的模糊查询区域===配电房查询
	public Set<MapEntity> parentIds(String userId) {

		List<String> list = userDao.userOrgList(userId);// 用户的
		Set<MapEntity> set = new HashSet<MapEntity>();

		Iterator it = list.iterator();

		while (it.hasNext()) {
			String id = (String) it.next();
			// System.out.println(id+"====所有orgId");
			String orgIds = userDao.parentIds(id);
			System.out.println(orgIds + "===orgIds");
			Set<MapEntity> set1 = userDao.orgEditList(orgIds);
			set.addAll(set1);
		}

		Iterator its = set.iterator();
		while (its.hasNext()) {
			MapEntity entity = (MapEntity) its.next();
			int type = (int) entity.get("type");
			System.out.println(type);
			if (type == 6) {
				String parentId = (String) entity.get("parentId");
				parentId = userDao.orgParentId(parentId);
				entity.put("parentId", parentId);
			}
		}
		return set;
	}

	// 所有区域集合
	public List<MapEntity> orgAll(String orgId) {
		List<MapEntity> list = userDao.orgAll(orgId);
		Iterator it = list.iterator();
		while (it.hasNext()) {
			MapEntity entity = (MapEntity) it.next();
			int type = (int) entity.get("type");
			if (type == 6) {
				String parentId = (String) entity.get("parentId");
				parentId = userDao.orgParentId(parentId);
				entity.put("parentId", parentId);
			}
		}
		return list;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		JSONObject JSON = new JSONObject();
		Map<String, Object> params = new HashMap<>();
		MapEntity entity = new MapEntity();
		params.put("userName", "a231");
		params.put("password", "123456");

		entity.put("token", "6680d61903ea4fb1a6ae8f04efb03ad8");
		//
		params.put("loginName", "admin");
		params.put("password", URLEncoder.encode(
				"AGjIegBSONwKDgYOY126znZWhvRxY+SLO0PtIsixg415GyHjJLE37+G5DuSGdwyxTKSWPhrNZjDR4vCfNqo2k4H1VYEnnOY/W/YetmQYzfrY/sAg1sBnoDskU23MDFHbnr1BXB9YdpyVP7zL7+b9FMFECBCZOy3ko0xNP7PIkpU=",
				"utf-8"));

		String resultText = HttpUtils.sendPost("http://10.199.2.199:8081/passport/user/saveUser", params, entity);
		// String resultText =
		// HttpUtils.sendPost("http://10.199.2.199:8081/passport/login", params, null);
		System.out.println(resultText);
		JSONObject resultData = JSON.parseObject(resultText);
		System.out.println(resultData);
	 

	}

}
