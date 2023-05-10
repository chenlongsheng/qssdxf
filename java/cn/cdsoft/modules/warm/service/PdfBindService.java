package cn.cdsoft.modules.warm.service;

import cn.cdsoft.modules.warm.entity.PdfBind;
import cn.cdsoft.common.service.CrudService;
import cn.cdsoft.modules.warm.dao.PdfBindDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by ZZUSER on 2018/12/12.
 */
@Service
public class PdfBindService extends CrudService<PdfBindDao,PdfBind> {
    @Autowired
    PdfBindDao pdfBindDao;

    @Transactional(readOnly = false)
    public void addBind(PdfBind pdfBind){
        pdfBindDao.addBind(pdfBind);
    }

    @Transactional(readOnly = false)
    public void updateBind(PdfBind pdfBind){
        pdfBindDao.updateBind(pdfBind);
    }

    public List<PdfBind> findBind(PdfBind pdfBind){
        return pdfBindDao.findBind(pdfBind);
    }

    public List<Map> getUserProject(String[] arr){
        return pdfBindDao.getUserProject(arr);
    }
}
