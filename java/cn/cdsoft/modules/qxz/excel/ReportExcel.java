package cn.cdsoft.modules.qxz.excel;

/**
 * Created by ZZUSER on 2018/12/25.
 */
public class ReportExcel {
    private String alarmAddr;//报警区域

    private String pdfName;//配电房名称

    private String alarmType;//报警类型

    private String alarmSource;//报警来源

    private int total;//报警总次数

    private int state0;//待确认次数

    private int state1;//已确认次数

    private int state2;//已派单次数

    private int state3;//已接单次数

    private int state4;//处理中次数

    private int state5;//报警解除次数

    private String over;//报警处理完成率

    public String getAlarmAddr() {
        return alarmAddr;
    }

    public void setAlarmAddr(String alarmAddr) {
        this.alarmAddr = alarmAddr;
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmSource() {
        return alarmSource;
    }

    public void setAlarmSource(String alarmSource) {
        this.alarmSource = alarmSource;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getState0() {
        return state0;
    }

    public void setState0(int state0) {
        this.state0 = state0;
    }

    public int getState1() {
        return state1;
    }

    public void setState1(int state1) {
        this.state1 = state1;
    }

    public int getState2() {
        return state2;
    }

    public void setState2(int state2) {
        this.state2 = state2;
    }

    public int getState3() {
        return state3;
    }

    public void setState3(int state3) {
        this.state3 = state3;
    }

    public int getState4() {
        return state4;
    }

    public void setState4(int state4) {
        this.state4 = state4;
    }

    public int getState5() {
        return state5;
    }

    public void setState5(int state5) {
        this.state5 = state5;
    }

    public String getOver() {
        return over;
    }

    public void setOver(String over) {
        this.over = over;
    }
}
