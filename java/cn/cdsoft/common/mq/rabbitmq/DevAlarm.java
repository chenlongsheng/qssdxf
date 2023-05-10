package cn.cdsoft.common.mq.rabbitmq;

import java.util.List;

public class DevAlarm {
	Long dev_id;
	List<Alarm> items;
	public Long getDev_id() {
		return dev_id;
	}
	public void setDev_id(Long dev_id) {
		this.dev_id = dev_id;
	}
	public List<Alarm> getItems() {
		return items;
	}
	public void setItems(List<Alarm> items) {
		this.items = items;
	}
	
}
