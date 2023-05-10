package cn.cdsoft.modules.warm.entity;

import java.util.List;

/**
 * Created by ZZUSER on 2018/12/12.
 */
public class PdfSchedulings {
    private PdfSchedulingRule pdfSchedulingRule;

    private List<PdfScheduling> list;

    public List<PdfScheduling> getList() {
        return list;
    }

    public void setList(List<PdfScheduling> list) {
        this.list = list;
    }

    public PdfSchedulingRule getPdfSchedulingRule() {
        return pdfSchedulingRule;
    }

    public void setPdfSchedulingRule(PdfSchedulingRule pdfSchedulingRule) {
        this.pdfSchedulingRule = pdfSchedulingRule;
    }
}
