package cn.cdsoft.modules.warm.dao;

import cn.cdsoft.common.persistence.CrudDao;
import cn.cdsoft.modules.warm.entity.PdfOrderRecorder;
import cn.cdsoft.common.persistence.annotation.MyBatisDao;

import java.util.List;
import java.util.Map;

/**
 * Created by ZZUSER on 2018/12/7.
 */
@MyBatisDao
public interface PdfOrderRecorderDao extends CrudDao<PdfOrderRecorder> {

    void addOrderRecorder(PdfOrderRecorder pdfOrderRecorder);

    List<Map> getRecorderList(PdfOrderRecorder pdfOrderRecorder);
}
