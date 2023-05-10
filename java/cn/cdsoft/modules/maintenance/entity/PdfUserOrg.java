package cn.cdsoft.modules.maintenance.entity;

public class PdfUserOrg {
	
	private static final long serialVersionUID = 1L;
	
	private String orgIds;  //配电房ids
	private String ourUserId; //我方人员id
	private String orgParentId; //配电房区域父id
	private String codeIds;  //配电房责任人==设备类型  第一
	private String orderNo;
	
	
	public PdfUserOrg(String orgIds, String ourUserId, String orgParentId,String codeIds,String orderNo) {
		super();
		this.orgIds = orgIds;
		this.ourUserId = ourUserId;
		this.orgParentId = orgParentId;
		this.codeIds =codeIds;
		this.orderNo = orderNo;
	}

	public PdfUserOrg() {
		super();
	}

	
	public String getCodeIds() {
		return codeIds;
	}

	public void setCodeIds(String codeIds) {
		this.codeIds = codeIds;
	}

	public String getOrgIds() {
		return orgIds;
	}
	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}
	public String getOurUserId() {
		return ourUserId;
	}
	public void setOurUserId(String ourUserId) {
		this.ourUserId = ourUserId;
	}
	public String getOrgParentId() {
		return orgParentId;
	}
	public void setOrgParentId(String orgParentId) {
		this.orgParentId = orgParentId;
	}
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PdfUserOrg [orgIds=");
		builder.append(orgIds);
		builder.append(", ourUserId=");
		builder.append(ourUserId);
		builder.append(", orgParentId=");
		builder.append(orgParentId);
		builder.append(", codeIds=");
		builder.append(codeIds);
		builder.append(", orderNo=");
		builder.append(orderNo);
		builder.append("]");
		return builder.toString();
	}


	

	
	
}
