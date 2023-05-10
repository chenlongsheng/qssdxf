/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.settings.entity;


import cn.cdsoft.common.persistence.DataEntity;
import cn.cdsoft.common.utils.excel.annotation.ExcelField;

/**
 * 联动管理Entity
 * @author long
 * @version 2018-08-08
 */
public class TIdLinkId extends DataEntity<TIdLinkId> {
	
	private static final long serialVersionUID = 1L;
	private Long srcId;		// 源通道
	private Long destId;		// 目标通道
	//private Long srcType;		// 源通道类型
	//private Long destType;		// 目标通道类型
	//private Long destCodeId;		// dest_code_id
//	private Long codeId;		// codeId
	private Long linkType;		// 联动类型
	private Long param;		// 联动参数
	private Long notUse;		// 1-停用
	
	private String coName;
	private String decoName;
	private String linkName;
	private TChannel tChannel;
	public TIdLinkId() {
		super();
	}

//	public TIdLinkId(String id){
//		super(id);
//	}
	
	

	@ExcelField(title="源通道", align=2, sort=1)
	public Long getSrcId() {
		return srcId;
	}

	

//	public Long getCodeId() {
//		return codeId;
//	}
//
//	public void setCodeId(Long codeId) {
//		this.codeId = codeId;
//	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getCoName() {
		return coName;
	}

	public void setCoName(String coName) {
		this.coName = coName;
	}

	public String getDecoName() {
		return decoName;
	}

	public void setDecoName(String decoName) {
		this.decoName = decoName;
	}

	public TChannel gettChannel() {
		return tChannel;
	}

	public void settChannel(TChannel tChannel) {
		this.tChannel = tChannel;
	}

	public void setSrcId(Long srcId) {
		this.srcId = srcId;
	}
	
//	@ExcelField(title="源通道类型", align=2, sort=2)
//	public Long getSrcType() {
//		return srcType;
//	}
//
//	public void setSrcType(Long srcType) {
//		this.srcType = srcType;
//	}
//	
//	@ExcelField(title="codeId", align=2, sort=3)
//	public Long getSrcCodeId() {
//		return srcCodeId;
//	}
//
//	public void setSrcCodeId(Long srcCodeId) {
//		this.srcCodeId = srcCodeId;
//	}
	
	@ExcelField(title="目标通道", align=2, sort=4)
	public Long getDestId() {
		return destId;
	}

	public void setDestId(Long destId) {
		this.destId = destId;
	}
	
//	@ExcelField(title="目标通道类型", align=2, sort=5)
//	public Long getDestType() {
//		return destType;
//	}
//
//	public void setDestType(Long destType) {
//		this.destType = destType;
//	}
	
//	@ExcelField(title="dest_code_id", align=2, sort=6)
//	public Long getDestCodeId() {
//		return destCodeId;
//	}
//
//	public void setDestCodeId(Long destCodeId) {
//		this.destCodeId = destCodeId;
//	}

	@ExcelField(title="联动类型", align=2, sort=7)
	public Long getLinkType() {
		return linkType;
	}

	public void setLinkType(Long linkType) {
		this.linkType = linkType;
	}
	
	@ExcelField(title="联动参数", align=2, sort=8)
	public Long getParam() {
		return param;
	}

	public void setParam(Long param) {
		this.param = param;
	}
	
	@ExcelField(title="1-停用", align=2, sort=9)
	public Long getNotUse() {
		return notUse;
	}

	public void setNotUse(Long notUse) {
		this.notUse = notUse;
	}
	
}