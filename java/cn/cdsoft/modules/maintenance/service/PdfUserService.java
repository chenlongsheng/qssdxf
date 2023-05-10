/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.maintenance.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.cdsoft.common.utils.DateUtils;
import cn.cdsoft.modules.maintenance.dao.PdfUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.common.service.CrudService;
import cn.cdsoft.common.utils.IdGenSnowFlake;
import cn.cdsoft.modules.maintenance.entity.PdfUser;
import cn.cdsoft.modules.maintenance.entity.PdfUserOrg;

/**
 * 我方人员管理Service
 * 
 * @author long
 * @version 2019-01-09
 */
@Service
@Transactional(readOnly = true)
public class PdfUserService extends CrudService<PdfUserDao, PdfUser> {
	@Autowired
	private PdfUserDao pdfUserDao;

	public PdfUser get(String id) {
		return super.get(id);
	}

	public List<PdfUser> findList(PdfUser pdfUser) {
		return super.findList(pdfUser);
	}

	public Page<PdfUser> findPage(Page<PdfUser> page, PdfUser pdfUser) {
		String codeIds = pdfUser.getCodeId();
		//分解前端codeIds为codeId和typeId类型
		if (codeIds != null && codeIds != "") {
			String[] codes = codeIds.split(",");
			pdfUser.setCodeId(codes[0]);
			pdfUser.setTypeId(codes[1]);
		}
		pdfUser.setPage(page);
		List<PdfUser> list = dao.findList(pdfUser);
		
		for (PdfUser user : list) {
			user.setDepartOrgName("");
//			user.setDepartOrgName(this.orgName(user.getDepartOrgId()));
		}
		page.setList(list);
		return page;
	}

	public String orgName(String orgId) {// 转化org中文名字

		MapEntity en = pdfUserDao.parentList(orgId);
//		System.out.println(en);
		Long id = (Long) en.get("id");
		String name = (String) en.get("name");
		en = pdfUserDao.parentList(id + "");
		Long parentId = (Long) en.get("id");
		String parentName = (String) en.get("name");
		en = pdfUserDao.parentList(parentId + "");
		String pParentName = (String) en.get("name");

		return pParentName + parentName + name;
	}

	public void saveElecCode(PdfUserOrg pdfUserOrg, String ourUserId) {
        
		String order = pdfUserOrg.getOrderNo();//获取用户的排序
		String orgIds = pdfUserOrg.getOrgIds();//得到用户拼接配电房的所有ids
		String orgParentId = pdfUserOrg.getOrgParentId();
		String codeIds = pdfUserOrg.getCodeIds();
		String[] idsList = codeIds.split(",");// code类型和主类型	//todo
		System.out.println(codeIds + "==设备类型的");
		MapEntity en = new MapEntity();
		String[] str = orgIds.split(",");//配电房
		en.put("ourUserId", ourUserId);
		en.put("orgParentId", orgParentId);
		en.put("orderNo", order);
		for (int i = 0; i < str.length; i++) {
			String id = IdGenSnowFlake.uuid().toString();
			en.put("id", id);
			en.put("orgId", str[i]);
//			System.out.println(id + "=====xuehuaid");

			int key = pdfUserDao.saveUserOrg(en);
			System.out.println(en.get("id") + "========jiaruhou ");
			if (key > 0) {// 添加配电房设备类型责任人
				for (String ids : idsList) {
					String[] codes = ids.split("_");

					String codeId = codes[0];
					String typeId = codes[1];
					String maxOrderNo = pdfUserDao.maxOrderNo(codeId, typeId, str[i]);
					System.out.println(maxOrderNo + "entity");
					System.out.println(maxOrderNo + "==========maxOrderNo");
					if (maxOrderNo == null) {
						maxOrderNo = "0";
					}
					String newDate = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
					int result = pdfUserDao.updateDate(newDate, codeId, typeId, str[i], ourUserId);
					System.out.println(result + "======jieguo");
					if (result <= 0) {
						int orderNo = Integer.parseInt(maxOrderNo) + 1;
						pdfUserDao.insertCodeOrg(IdGenSnowFlake.uuid().toString(), codeId, typeId, ourUserId, str[i],
								orderNo, newDate);
					}
				}
			}
		}
	}

	// 添加用户
	@Transactional(readOnly = false)
	public void save(PdfUser pdfUser, JSONArray ja) {
		// pdfUser.preInsert();
		// dao.insert(pdfUser);
		super.save(pdfUser);
		String ourUserId = pdfUser.getId();
		if (ja != null) {
			// 删除用户配电房
			pdfUserDao.deleteUserOrg(ourUserId);
			// pdfUserDao.deleteCodeOrg(ourUserId);
			for (int i = 0; i < ja.size(); i++) {
				PdfUserOrg pdfUserOrg = JSONObject.parseObject(ja.get(i).toString(), PdfUserOrg.class);
				System.out.println(pdfUserOrg.toString());
				pdfUserOrg.getOrgIds();
				pdfUserOrg.getOrgParentId();
				pdfUserOrg.getCodeIds();
				this.saveElecCode(pdfUserOrg, ourUserId);
			}
		}
		// 删除用设备类型当前时间前三秒数据
		pdfUserDao.deleteCodeOrg(ourUserId, DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
	}

	// 详情
	public List<MapEntity> details(String userId) {
		List<MapEntity> list = pdfUserDao.details(userId);
		for (MapEntity mapEntity : list) {
			String orgId = (String) mapEntity.get("orgParentId");
			String orgName = this.orgName(orgId);
			System.out.println(orgName + "---orgName");
			mapEntity.put("areaName", orgName);
		}
		return list;
	}

	// 删除我方用户
	@Transactional(readOnly = false)
	public void delete(PdfUser pdfUser) {
		super.delete(pdfUser);
		// 删除用户第一,二责任人,当用户删除时候
		pdfUserDao.deleteUserOrg(pdfUser.getId());
		// --------------------------------------------------------------
		List<MapEntity> list = pdfUserDao.selectCodeOrgList(pdfUser.getId());
		for (MapEntity entity : list) {
			int orderNo = (int) entity.get("orderNo");
			if (orderNo == 1) {

				int codeId = (int) entity.get("codeId");
				int typeId = (int) entity.get("typeId");
				Long orgId = (Long) entity.get("orgId");
				System.out.println(codeId + "==" + typeId + "===" + orgId);
				List<MapEntity> li = pdfUserDao.secondOrder(codeId, typeId, orgId);
				System.out.println(li + "li==========");
				if (li != null) {
					for (MapEntity mapEntity : li) {
						if (mapEntity != null) {
							String id = (String) mapEntity.get("id");
							int key = pdfUserDao.updateOrder(id);
							System.out.println(key);
						}
					}
				}
			}
		}
		// -----------------------------------------------------------
		pdfUserDao.deleteCodeOrg(pdfUser.getId(), null);

	}

	// =================================

	// 我方员职位集合
	public List<MapEntity> posiList() {

		return pdfUserDao.posiList();
	}

	// 所有设备类型集合
	public List<MapEntity> tcodeList(String name) {

		return pdfUserDao.tcodeList(name);
	}

	// 删除职位
	@Transactional(readOnly = false)
	public Integer deletePosition(String id) {

		return pdfUserDao.deletePosition(id);
	}

	// 添加职位
	@Transactional(readOnly = false)
	public MapEntity insertPosition(String name) {
		MapEntity entity = new MapEntity();
		entity.put("name", name);
		pdfUserDao.insertPosition(entity);
		return entity;
	}

	// 辖区下的配电房

	public List<MapEntity> elceList(String orgId, String name) {

		List<MapEntity> list = pdfUserDao.elceList(orgId, name);
		return list;
	}

	// 添加用户的配电房
	public Integer saveUserOrg(MapEntity entity) {
		return pdfUserDao.saveUserOrg(entity);
	}

	// 查询的电业局配电局集合--- 取消
	public Set<MapEntity> orgDepartList1(String name, String orgId) {
		Set<MapEntity> list = pdfUserDao.orgDepartList(name, null, orgId);
		Set<MapEntity> set = new HashSet<MapEntity>();

		Iterator it = list.iterator();
		while (it.hasNext()) {
			MapEntity entity = (MapEntity) it.next();
			int type = (int) entity.get("type");
			if (type == 1) {
				list = pdfUserDao.orgDepartList(null, null, orgId);// 全部
				set.addAll(list);
			}
			if (type == 2 || type == 3 || type == 4 || type == 5) {
				String code = (String) entity.get("code");
				String id = (String) entity.get("id");
				Set<MapEntity> list1 = pdfUserDao.orgDepartList(null, code, orgId);// 全部
				set.addAll(list1);
				String orgIds = pdfUserDao.parentIds(id);
				System.out.println(orgIds);
				orgIds = orgIds.substring(orgIds.lastIndexOf(orgId));
				Set<MapEntity> set1 = pdfUserDao.orgEditList(orgIds);
				set.addAll(set1);
			}
		}
		return set;
	}

	// 查询的电业局配电局集合----添加修改时候回调
	public Set<MapEntity> orgEditList(String name, String orgId) {
		
		Set<MapEntity> setMap = pdfUserDao.orgList(name, null, orgId);
		
//		Set<MapEntity> set = this.orgList(name, orgId);
//		Set<MapEntity> setMap = new HashSet<>();
//		Iterator its = set.iterator();
//		while (its.hasNext()) {
//			MapEntity entity = (MapEntity) its.next();
//			int type = (int) entity.get("type");
//			if (type != 6) {
//				setMap.add(entity);
//				// its.remove();
//				System.out.println("======dage" + type);
//			}
//		}
		return setMap;
	}

	// 配电房的集合
	// public Set<MapEntity> orgList(String name,String orgId) {
	//
	// Set<MapEntity> list = pdfUserDao.orgList(null, null, orgId);
	// for (MapEntity mapEntity : list) {
	// int type = (int) mapEntity.get("type");
	// if (type == 6) {
	// String id = (String) mapEntity.get("id");
	// String parentId = (String) mapEntity.get("parentId");
	// System.out.println(parentId + "父==id");
	// String parent = pdfUserDao.orgParentId(parentId);
	// // mapEntity.put("parentId", parent);
	// }
	// }
	// return list;
	// }

	// 获取修改的模糊查询区域===配电房查询
	public Set<MapEntity> orgList(String name, String orgId) {

		Set<MapEntity> list = pdfUserDao.orgList(name, null, orgId);
		Set<MapEntity> set = new HashSet<MapEntity>();

		Iterator it = list.iterator();
		while (it.hasNext()) {
			MapEntity entity = (MapEntity) it.next();
			int type = (int) entity.get("type");
			if (type == 1) {
				list = pdfUserDao.orgList(null, null, orgId);// 全部
				set.addAll(list);
			}
			if (type == 2 || type == 3 || type == 4 || type == 6) {
				String code = (String) entity.get("code");
				String id = (String) entity.get("id");
				Set<MapEntity> list1 = pdfUserDao.orgList(null, code, orgId);// 全部
				set.addAll(list1);
				String orgIds = pdfUserDao.parentIds(id);
				// System.out.println(orgIds + "====");
				orgIds = orgIds.substring(orgIds.lastIndexOf(orgId));
				// System.out.println(orgIds + "====orgId=nihao==");
				// System.out.println(orgIds);
				Set<MapEntity> set1 = pdfUserDao.orgEditList(orgIds);
				set.addAll(set1);
			}
		}
		//配电房parentId变成电业局的parentId
		Iterator its = set.iterator();
		while (its.hasNext()) {
			MapEntity entity = (MapEntity) its.next();
			int type = (int) entity.get("type");
//			System.out.println(type);
			if (type == 6) {
				String parentId = (String) entity.get("parentId");
				parentId = pdfUserDao.orgParentId(parentId);
				entity.put("parentId", parentId);
			}
		}
		return set;
	}

}