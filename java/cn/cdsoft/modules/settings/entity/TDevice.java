/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.settings.entity;


import cn.cdsoft.common.persistence.DataEntity;
import cn.cdsoft.common.utils.excel.annotation.ExcelField;
import cn.cdsoft.modules.sys.entity.Area;

/**
 * 设备管理Entity
 * @author long
 * @version 2018-07-24
 */
public class TDevice extends DataEntity<TDevice> {
	
	private static final long serialVersionUID = 1L;
	private String orgId;		// 区域id
	private Long parentId;
	private String parentName;
	private String picturePath;
	private Long devType;		// 设备类型，t_code-id
	private Long typeId;		// 设备大类；t_code_type-id
	private String name;		// 设备名称
	private String addr;		// 设备地址
	private String unitType;
	private String firmwareVersion;
	private String vendorInformation;
	
	private Double coordsX;		// x坐标
	private Double coordsY;		// y坐标
	private String sn;		// 设备序列号
	private Long orderNo;		// 排序号
	private Long notUse;		// 状态(1-停用)
	private Area area;

	public TDevice() {
		super();
	}

//	public AudTDevice(String id){
//		super(id);
//	}
    
	
	
	@ExcelField(title="区域id", align=2, sort=1)
	public String getOrgId() {
		return orgId;
	}

	public String getPicturePath() {
		return picturePath;
	}

	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public String getFirmwareVersion() {
		return firmwareVersion;
	}

	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}

	public String getVendorInformation() {
		return vendorInformation;
	}

	public void setVendorInformation(String vendorInformation) {
		this.vendorInformation = vendorInformation;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@ExcelField(title="设备类型，t_code-id", dictType="", align=2, sort=2)
	public Long getDevType() {
		return devType;
	}

	public void setDevType(Long devType) {
		this.devType = devType;
	}
	
	@ExcelField(title="设备大类；t_code_type-id", align=2, sort=3)
	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	
	@ExcelField(title="设备名称", align=2, sort=4)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="设备地址", align=2, sort=5)
	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}
	
	@ExcelField(title="x坐标", align=2, sort=6)
	public Double getCoordsX() {
		return coordsX;
	}

	public void setCoordsX(Double coordsX) {
		this.coordsX = coordsX;
	}
	
	@ExcelField(title="y坐标", align=2, sort=7)
	public Double getCoordsY() {
		return coordsY;
	}

	public void setCoordsY(Double coordsY) {
		this.coordsY = coordsY;
	}
	
	@ExcelField(title="设备序列号", align=2, sort=8)
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
	
	@ExcelField(title="排序号", align=2, sort=9)
	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}
	
	@ExcelField(title="状态(1-停用)", dictType="", align=2, sort=10)
	public Long getNotUse() {
		return notUse;
	}

	public void setNotUse(Long notUse) {
		this.notUse = notUse;
	}
	
}