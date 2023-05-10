/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.settings.entity;


import cn.cdsoft.common.persistence.DataEntity;
import cn.cdsoft.common.utils.excel.annotation.ExcelField;

/**
 * 数据配置Entity
 * @author long
 * @version 2018-10-24
 */
public class TDeviceConfig extends DataEntity<TDeviceConfig> {
	
	private static final long serialVersionUID = 1L;
	private String projectName;		// 工程名
	private String tableName;		// table_name
	private String prefix;
	private String rowName;		// 字段名
	private String modelName;
	private String chineseName;		// chinese_name
	private String mustChoose;
	private Long showField;		// 可显示否
	private Long onlyRead;		// 可操作否
	private Long orderNo;		// order_no
	
	public TDeviceConfig() {
		super();
	}

	public TDeviceConfig(String id){
		super(id);
	}

	
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@ExcelField(title="工程名", align=2, sort=1)
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	@ExcelField(title="table_name", align=2, sort=2)
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	@ExcelField(title="字段名", align=2, sort=3)
	public String getRowName() {
		return rowName;
	}

	public void setRowName(String rowName) {
		this.rowName = rowName;
	}
	
	public String getMustChoose() {
		return mustChoose;
	}

	public void setMustChoose(String mustChoose) {
		this.mustChoose = mustChoose;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	@ExcelField(title="chinese_name", align=2, sort=4)
	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}
	
	
	

	public Long getShowField() {
		return showField;
	}

	public void setShowField(Long showField) {
		this.showField = showField;
	}

	@ExcelField(title="可操作否", align=2, sort=6)
	public Long getOnlyRead() {
		return onlyRead;
	}

	public void setOnlyRead(Long onlyRead) {
		this.onlyRead = onlyRead;
	}
	
	@ExcelField(title="order_no", align=2, sort=7)
	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}
	
}