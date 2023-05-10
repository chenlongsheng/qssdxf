package cn.cdsoft.modules.warm.dao;

import cn.cdsoft.common.persistence.CrudDao;
import cn.cdsoft.common.persistence.annotation.MyBatisDao;
import cn.cdsoft.modules.warm.entity.PdfOrderDeal;

import java.util.List;
import java.util.Map;

/**
 * Created by ZZUSER on 2018/12/7.
 */
@MyBatisDao
public interface PdfOrderDealDao extends CrudDao<PdfOrderDeal> {

    void addOrderDeal(PdfOrderDeal pdfOrderDeal);

    List<Map> getDealList(String orderId);

    void setReaded(PdfOrderDeal pdfOrderDeal);

    int countUnRead(PdfOrderDeal pdfOrderDeal);

    Map getUnRead(PdfOrderDeal pdfOrderDeal);

    List<Map> getOrderDeal(PdfOrderDeal pdfOrderDeal);

    List<String> getMaxDate(String id);

    List<Map> getDealByReplyId(String id);

}
