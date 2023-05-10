package cn.cdsoft.modules.homepage.vo;


/**
 * Created by ZZUSER on 2019/2/13.
 */
public class ChangeSwitchVO {

    private Integer value;//0代表开1代表关

    private Integer codeId;//类型id

    private Integer typeId;//类型主id

    private String floors;//楼层id


    //****************************代码中用到************************************************

    private String[] array;

    public Integer getCodeId() {
        return codeId;
    }

    public void setCodeId(Integer codeId) {
        this.codeId = codeId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getFloors() {
        return floors;
    }

    public void setFloors(String floors) {
        this.floors = floors;
    }

    public String[] getArray() {
        return array;
    }

    public void setArray(String[] array) {
        this.array = array;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
