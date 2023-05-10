/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.cdsoft.modules.settings.entity;


import cn.cdsoft.common.persistence.DataEntity;
import cn.cdsoft.common.utils.excel.annotation.ExcelField;

/**
 * 数据配置Entity
 * @author long
 * @version 2018-07-24
 */
public class TDeviceDetail extends DataEntity<TDeviceDetail> {
	
	private static final long serialVersionUID = 1L;
	private String ip;		// 设备ip
	private Long port;		// port
	private String uname;		// 登入名
	private String upwd;		// 登入密码
	private String mac;		// mac，格式：00ff95f98cbc，没有符号，小写
	private String gateway;		// 网关
	private String mask;		// 网络掩码
	private String imei;		// 国际移动设备识别码
	private String outerId;		// 外部系统的设备ID
	private String extParam;		// 扩展参数
	private String online;
	private String commChannel;
	private String freqBand;
	private String busAddr;
	private int deviceId;
	public TDeviceDetail() {
		super();
	}

//	public TDeviceDetail(String id){
//		super(id);
//	}

	
    
	@ExcelField(title="设备ip", align=2, sort=1)
	public String getIp() {
		return ip;
	}



	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}

	public String getCommChannel() {
		return commChannel;
	}

	public void setCommChannel(String commChannel) {
		this.commChannel = commChannel;
	}

	public String getFreqBand() {
		return freqBand;
	}

	public void setFreqBand(String freqBand) {
		this.freqBand = freqBand;
	}

	public String getBusAddr() {
		return busAddr;
	}

	public void setBusAddr(String busAddr) {
		this.busAddr = busAddr;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@ExcelField(title="port", align=2, sort=2)
	public Long getPort() {
		return port;
	}

	public void setPort(Long port) {
		this.port = port;
	}
	
	@ExcelField(title="登入名", align=2, sort=3)
	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}
	
	@ExcelField(title="登入密码", align=2, sort=4)
	public String getUpwd() {
		return upwd;
	}

	public void setUpwd(String upwd) {
		this.upwd = upwd;
	}
	
	@ExcelField(title="mac，格式：00ff95f98cbc，没有符号，小写", align=2, sort=5)
	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}
	
	@ExcelField(title="网关", align=2, sort=6)
	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	
	@ExcelField(title="网络掩码", align=2, sort=7)
	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}
	
	@ExcelField(title="国际移动设备识别码", align=2, sort=8)
	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}
	
	@ExcelField(title="外部系统的设备ID", align=2, sort=9)
	public String getOuterId() {
		return outerId;
	}

	public void setOuterId(String outerId) {
		this.outerId = outerId;
	}
	
	@ExcelField(title="扩展参数", align=2, sort=10)
	public String getExtParam() {
		return extParam;
	}

	public void setExtParam(String extParam) {
		this.extParam = extParam;
	}
	
}