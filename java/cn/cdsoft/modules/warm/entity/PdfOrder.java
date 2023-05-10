package cn.cdsoft.modules.warm.entity;

import cn.cdsoft.common.persistence.DataEntity;

/**
 * Created by ZZUSER on 2018/12/6.
 */
public class PdfOrder extends DataEntity<PdfOrder> {
    private String devId;//设备id

    private String devType;//设备类型

    private String typeId;//类型id

    private Integer alarmType;//报警类型

    private String prec;//问题描述

    private Integer alarmLevel;//报警级别

    private String alarmTime;//报警时间

    private String alarmAddr;//报警地址

    private String sendOrderUser;//派单人

    private String suggestion;//处理建议

    private String principal;//负责人

    private Integer state;//状态

    private String alarmCancelTime;//报警解除时间

    private String confirmUser;//确认人

    private String localOrgId;//当前登录账号所属区域id

    private String alarmStartTime;//报警时间开始

    private String alarmEndTime;//报警时间结束

    private String alarmCancelStart;//报警解除时间开始

    private String alarmCancelEnd;//报警解除时间结束

    private String[] arr;

    private int alarmSource;//报警来源（1系统报警2人工报警）

    private String pdfName;//配电房名称（用于查询用）

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public Integer getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(Integer alarmType) {
        this.alarmType = alarmType;
    }

    public String getPrec() {
        return prec;
    }

    public void setPrec(String prec) {
        this.prec = prec;
    }

    public Integer getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(Integer alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getAlarmAddr() {
        return alarmAddr;
    }

    public void setAlarmAddr(String alarmAddr) {
        this.alarmAddr = alarmAddr;
    }

    public String getSendOrderUser() {
        return sendOrderUser;
    }

    public void setSendOrderUser(String sendOrderUser) {
        this.sendOrderUser = sendOrderUser;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getAlarmCancelTime() {
        return alarmCancelTime;
    }

    public void setAlarmCancelTime(String alarmCancelTime) {
        this.alarmCancelTime = alarmCancelTime;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getLocalOrgId() {
        return localOrgId;
    }

    public void setLocalOrgId(String localOrgId) {
        this.localOrgId = localOrgId;
    }

    public String getAlarmStartTime() {
        return alarmStartTime;
    }

    public void setAlarmStartTime(String alarmStartTime) {
        this.alarmStartTime = alarmStartTime;
    }

    public String getAlarmEndTime() {
        return alarmEndTime;
    }

    public void setAlarmEndTime(String alarmEndTime) {
        this.alarmEndTime = alarmEndTime;
    }

    public String getAlarmCancelStart() {
        return alarmCancelStart;
    }

    public void setAlarmCancelStart(String alarmCancelStart) {
        this.alarmCancelStart = alarmCancelStart;
    }

    public String getAlarmCancelEnd() {
        return alarmCancelEnd;
    }

    public void setAlarmCancelEnd(String alarmCancelEnd) {
        this.alarmCancelEnd = alarmCancelEnd;
    }

    public String[] getArr() {
        return arr;
    }

    public void setArr(String[] arr) {
        this.arr = arr;
    }

    public String getConfirmUser() {
        return confirmUser;
    }

    public void setConfirmUser(String confirmUser) {
        this.confirmUser = confirmUser;
    }

    public int getAlarmSource() {
        return alarmSource;
    }

    public void setAlarmSource(int alarmSource) {
        this.alarmSource = alarmSource;
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }
}
