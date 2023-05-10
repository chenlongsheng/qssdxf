package cn.cdsoft.modules.maintenance.entity;

import cn.cdsoft.common.persistence.DataEntity;
import cn.cdsoft.common.persistence.MapEntity;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018-12-25.
 */
public class PdfMaintenanceDetail extends DataEntity<PdfMaintenanceDetail> {
    //维保单位id
    private String maintenanceId;
    
    private String orgParentId;//管辖区域id
    
    private String orgParentName;//管辖区域name
    //维保区域id
    private String orgId;
    //维保设备类型 1.门禁 2.风机 3.烟感 4.水浸 5.温湿度计 6.摄像头
    private String typeId;
    //维保起始时间
    private Date timeStart;
    //维保终止时间
    private Date timeStop;
    //维保联系人
    private String contacts;
    //联系电话1
    private String phone;
    //联系电话2
    private String telephone;
    
    private String codeIds;//类型
    
    private List<MapEntity> list;
    

    public PdfMaintenanceDetail() {
		super();
	}

	public PdfMaintenanceDetail(String maintenanceId, String orgParentId, String orgId, String typeId, Date timeStart,
			Date timeStop, String contacts, String phone, String telephone, String codeIds) {
		super();
		this.maintenanceId = maintenanceId;
		this.orgParentId = orgParentId;
		this.orgId = orgId;
		this.typeId = typeId;
		this.timeStart = timeStart;
		this.timeStop = timeStop;
		this.contacts = contacts;
		this.phone = phone;
		this.telephone = telephone;
		this.codeIds = codeIds;
	}

	public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getOrgParentId() {
		return orgParentId;
	}

	public void setOrgParentId(String orgParentId) {
		this.orgParentId = orgParentId;
	}


	public List<MapEntity> getList() {
		return list;
	}

	public void setList(List<MapEntity> list) {
		this.list = list;
	}

	public String getOrgParentName() {
		return orgParentName;
	}

	public void setOrgParentName(String orgParentName) {
		this.orgParentName = orgParentName;
	}

	public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeStop() {
        return timeStop;
    }

    public String getCodeIds() {
		return codeIds;
	}

	public void setCodeIds(String codeIds) {
		this.codeIds = codeIds;
	}

	public void setTimeStop(Date timeStop) {
        this.timeStop = timeStop;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(String maintenanceId) {
        this.maintenanceId = maintenanceId;
    }
}
