package cn.cdsoft.modules.warm.service;

import cn.cdsoft.modules.warm.dao.PdfOrderRecorderDao;
import cn.cdsoft.modules.warm.entity.PdfOrderRecorder;
import cn.cdsoft.common.service.CrudService;
import org.springframework.stereotype.Service;

/**
 * Created by ZZUSER on 2018/12/7.
 */
@Service
public class PdfOrderRecorderService extends CrudService<PdfOrderRecorderDao,PdfOrderRecorder> {
}
