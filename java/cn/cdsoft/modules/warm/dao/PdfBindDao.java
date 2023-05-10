package cn.cdsoft.modules.warm.dao;

import cn.cdsoft.common.persistence.CrudDao;
import cn.cdsoft.modules.warm.entity.PdfBind;
import cn.cdsoft.common.persistence.annotation.MyBatisDao;

import java.util.List;
import java.util.Map;

/**
 * Created by ZZUSER on 2018/12/12.
 */
@MyBatisDao
public interface PdfBindDao extends CrudDao<PdfBind> {

    void addBind(PdfBind pdfBind);

    void updateBind(PdfBind pdfBind);

    List<PdfBind> findBind(PdfBind pdfBind);

    List<Map> getUserProject(String[] arr);//获取用户所有项目

    List<PdfBind> findBindByIds(String[] arr);//根据用户id获取到绑定数据
}
