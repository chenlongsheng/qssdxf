/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.settings.entity;


import cn.cdsoft.common.persistence.DataEntity;
import cn.cdsoft.common.utils.excel.annotation.ExcelField;

/**
 * 数据配置Entity
 * @author long
 * @version 2018-10-23
 */
public class TAlarmPolicy extends DataEntity<TAlarmPolicy> {
	
	private static final long serialVersionUID = 1L;
	private Long chId;		// 通道iD
	private Long typeId;		// 类型id
	private Long chType;		// t_code的id
	private Long orgId;		// 区域id
	private Long level;		// level
	private Double lowValue;		// 低值
	private Double highValue;		// 高值
	private String chName;
	private String orgName;
	private String codeName;
	private String typeName;
	
	private String orgTreeId;  //树的id
	
	
	public TAlarmPolicy() {
		super();
	}

	public TAlarmPolicy(String id){
		super(id);
	}

	@ExcelField(title="通道iD", align=2, sort=1)
	public Long getChId() {
		return chId;
	}

	public void setChId(Long chId) {
		this.chId = chId;
	}
	
	@ExcelField(title="类型id", align=2, sort=2)
	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	
	public String getOrgTreeId() {
		return orgTreeId;
	}

	public void setOrgTreeId(String orgTreeId) {
		this.orgTreeId = orgTreeId;
	}

	@ExcelField(title="t_code的id", align=2, sort=3)
	public Long getChType() {
		return chType;
	}

	public void setChType(Long chType) {
		this.chType = chType;
	}
	
	@ExcelField(title="区域id", align=2, sort=4)
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	
	@ExcelField(title="level", align=2, sort=5)
	public Long getLevel() {
		return level;
	}

	public void setLevel(Long level) {
		this.level = level;
	}
	
	@ExcelField(title="低值", align=2, sort=6)
	public Double getLowValue() {
		return lowValue;
	}

	public void setLowValue(Double lowValue) {
		this.lowValue = lowValue;
	}
	
	@ExcelField(title="高值", align=2, sort=7)
	public Double getHighValue() {
		return highValue;
	}

	public void setHighValue(Double highValue) {
		this.highValue = highValue;
	}

	public String getChName() {
		return chName;
	}

	public void setChName(String chName) {
		this.chName = chName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	
	
}