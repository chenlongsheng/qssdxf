/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.settings.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import cn.cdsoft.common.persistence.MapEntity;
import cn.cdsoft.common.persistence.Page;
import cn.cdsoft.common.service.CrudService;
import cn.cdsoft.modules.settings.dao.TChannelDao;
import cn.cdsoft.modules.settings.dao.TCodeDao;
import cn.cdsoft.modules.settings.entity.TCode;
import cn.cdsoft.modules.sys.security.exception.NoAccountException;

/**
 * code管理Service
 * 
 * @author long
 * @version 2018-08-09
 */
@Service
@Transactional(readOnly = true)
public class TCodeService extends CrudService<TCodeDao, TCode> {

	@Autowired
	private TCodeDao tCodeDao;
	private List<MapEntity> findCodeList;

	public TCode get(String id, String typeId) {

		return tCodeDao.getCode(id, typeId);

	}

	public List<TCode> findList(TCode tCode) {
		return super.findList(tCode);
	}

	// public Page<TCode> findPage(Page<TCode> page, TCode tCode) {
	// return super.findPage(page, tCode);
	// }

	@Transactional(readOnly = false)
	public void save(TCode tCode) {
		super.save(tCode);
	}

	@Transactional(readOnly = false)
	public void delete(TCode tCode) {
		super.delete(tCode);
	}

	public String checkPic(MultipartFile iconFile, MultipartFile warnFile, MultipartFile offlineFile,
			MultipartFile defenceFile, MultipartFile withdrawingFile, MultipartFile sidewayFile) throws IOException {

		istrue(iconFile);
		istrue(warnFile);
		istrue(offlineFile);
		istrue(defenceFile);
		istrue(withdrawingFile);
		istrue(sidewayFile);

		return null;

	}

	public boolean istrue(MultipartFile iconFile) throws IOException {

		if (iconFile != null) {

			String str = "89504E47 FFD8FF 47494638";
			byte[] byteArr = iconFile.getBytes();
			InputStream is = new ByteArrayInputStream(byteArr);

			byte[] b = new byte[3];
			is.read(b, 0, b.length);
			String xxx = bytesToHexString(b);
			xxx = xxx.toUpperCase();
			System.out.println("头文件是：" + xxx);

			boolean status = str.contains(xxx);
			if (!status) {
				throw new NoAccountException();
			}
		}
		return true;

	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	// --------------------------------------
	// 分页
	public Page<TCode> findPage(Page<TCode> page, TCode entity) {
//        String skin = str;
		entity.setPage(page);
		List<TCode> findCodeList = tCodeDao.findCodeList(entity);
		for (int i = 0; i < findCodeList.size(); i++) {
			TCode tCode = findCodeList.get(i);
//			if(tCode.getIconSkin()=="")tCode.setIconSkin(skin);
			String path = path(tCode.getTypeId() + "") + tCode.getIconSkin();
			tCode.setIconSkin(path);

//			if(tCode.getWarnIconSkin()=="")tCode.setWarnIconSkin(skin);			
			path = path(tCode.getTypeId() + "") + tCode.getWarnIconSkin();
			tCode.setWarnIconSkin(path);

//			if(tCode.getOfflineIconSkin()=="")tCode.setOfflineIconSkin(skin);						
			path = path(tCode.getTypeId() + "") + tCode.getOfflineIconSkin();
			tCode.setOfflineIconSkin(path);

//			if(tCode.getDefenceIconSkin()=="")tCode.setDefenceIconSkin(skin);	
			path = path(tCode.getTypeId() + "") + tCode.getDefenceIconSkin();
			tCode.setDefenceIconSkin(path);

//			if(tCode.getWithdrawingIconSkin()=="")tCode.setWithdrawingIconSkin(skin);	
			path = path(tCode.getTypeId() + "") + tCode.getWithdrawingIconSkin();
			tCode.setWithdrawingIconSkin(path);

//			if(tCode.getSidewayIconSkin()=="")tCode.setSidewayIconSkin(skin);	
			path = path(tCode.getTypeId() + "") + tCode.getSidewayIconSkin();
			tCode.setSidewayIconSkin(path);

		}
		page.setList(findCodeList);
		return page;
	}

	public String path(String typeId) {
		String path = null;
		if (typeId.equals("1")) {
			path = "/static_modules/device/";
		} else {
			path = "/static_modules/channel/";
		}
		return path;
	}

	// 添加t_code
	@Transactional(readOnly = false)
	public void saveAdd(TCode tCode) {
		String sking = "46d78cc6-f888-45ea-9b21-143a244f11cb.png";
		tCode.setIconSkin(sking);
		tCode.setWarnIconSkin(sking);
		tCode.setOfflineIconSkin(sking);
		tCode.setDefenceIconSkin(sking);
		tCode.setWithdrawingIconSkin(sking);
		tCode.setSidewayIconSkin(sking);
		tCode.preUpdate();
		dao.insert(tCode);
	}

}