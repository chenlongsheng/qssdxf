/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.settings.entity;


import cn.cdsoft.common.persistence.DataEntity;
import cn.cdsoft.common.utils.excel.annotation.ExcelField;
import cn.cdsoft.modules.sys.entity.Area;

/**
 * 通道管理Entity
 * @author ywk
 * @version 2018-06-22
 */
public class TChannel extends DataEntity<TChannel> {
	
	private static final long serialVersionUID = 1L;
	private Long devId;		// 设备id
	private String logicOrgId;		// 区域id
	private Long chNo;		// 通道号
	private Long chType;		// 通道类型
	private Long typeId;		// 通道类型2
	private String name;		// 通道名称	
	private Double coordsX;		// x坐标
	private Double coordsY;		// y坐标
	private Long param0;		// 参数0
	private Long param1;		// 参数1
	private Long notUse;		// 状态(1-停用)
	private String extParams;		// 扩展参数
	private String devName;
	private Long param2;  //参数2
	private String addr;
	private String chAddr;
	private String devType;
	
	private String orgCode; //区域Code
	
	private String destName;
	private Area area;		// 归属区域
	public TChannel() {
		super();
	}

//	public TChannel(Long id){
//		super(id);
//	}
    
    

	public Area getArea() {
		return area;
	}



	

	/**
	 * @return the addr
	 */
	public String getAddr() {
		return addr;
	}

	/**
	 * @param addr the addr to set
	 */
	public void setAddr(String addr) {
		this.addr = addr;
	}
	
	
	

	/**
	 * @return the chAddr
	 */
	public String getChAddr() {
		return chAddr;
	}

	/**
	 * @param chAddr the chAddr to set
	 */
	public void setChAddr(String chAddr) {
		this.chAddr = chAddr;
	}

	/**
	 * @return the devType
	 */
	public String getDevType() {
		return devType;
	}

	/**
	 * @param devType the devType to set
	 */
	public void setDevType(String devType) {
		this.devType = devType;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getDestName() {
		return destName;
	}

	public void setDestName(String destName) {
		this.destName = destName;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	@ExcelField(title="设备id", align=2, sort=1)
	public Long getDevId() {
		return devId;
	}

	public void setDevId(Long devId) {
		this.devId = devId;
	}
	
	@ExcelField(title="区域id", align=2, sort=2)
	public String getLogicOrgId() {
		return logicOrgId;
	}

	public void setLogicOrgId(String logicOrgId) {
		this.logicOrgId = logicOrgId;
	}
	
	@ExcelField(title="通道号", align=2, sort=3)
	public Long getChNo() {
		return chNo;
	}

	public void setChNo(Long chNo) {
		this.chNo = chNo;
	}
	
	@ExcelField(title="通道类型", align=2, sort=4)
	public Long getChType() {
		return chType;
	}

	public void setChType(Long chType) {
		this.chType = chType;
	}
	
	@ExcelField(title="通道类型2", align=2, sort=5)
	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	
	@ExcelField(title="通道名称", align=2, sort=6)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="x坐标", align=2, sort=7)
	public Double getCoordsX() {
		return coordsX;
	}

	public void setCoordsX(Double coordsX) {
		this.coordsX = coordsX;
	}
	
	@ExcelField(title="y坐标", align=2, sort=8)
	public Double getCoordsY() {
		return coordsY;
	}

	public void setCoordsY(Double coordsY) {
		this.coordsY = coordsY;
	}
	
	@ExcelField(title="参数0", align=2, sort=9)
	public Long getParam0() {
		return param0;
	}

	public void setParam0(Long param0) {
		this.param0 = param0;
	}
	
	@ExcelField(title="参数1", align=2, sort=10)
	public Long getParam1() {
		return param1;
	}

	public void setParam1(Long param1) {
		this.param1 = param1;
	}
	
	@ExcelField(title="状态(1-停用)", align=2, sort=11)
	public Long getNotUse() {
		return notUse;
	}

	public void setNotUse(Long notUse) {
		this.notUse = notUse;
	}
	
	@ExcelField(title="扩展参数", align=2, sort=13)
	public String getExtParams() {
		return extParams;
	}

	public void setExtParams(String extParams) {
		this.extParams = extParams;
	}
	@ExcelField(title="参数2", align=2, sort=10)
	public Long getParam2() {
		return param2;
	}

	public void setParam2(Long param2) {
		this.param2 = param2;
	}
}