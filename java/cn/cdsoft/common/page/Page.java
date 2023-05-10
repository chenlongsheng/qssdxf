package cn.cdsoft.common.page;


import org.apache.ibatis.session.RowBounds;

import java.util.ArrayList;
import java.util.List;

/**
 * Mybatis - 分页对象
 *
 * @author liuzh/abel533/isea533
 * @version 3.2.1
 * @url http://git.oschina.net/free/Mybatis_PageHelper
 */
public class Page<E> extends ArrayList<E> {
    
	private static final long serialVersionUID = -3851184514722940844L;
	/**
     * 不进行count查询
     */
    public static final int NO_SQL_COUNT = -1;
    public static final long SQL_COUNT = 0;
    
    private static final int DEFAULTPAGENUM = 0;
    
    private static final int DEFAULTPAGESIZE = 30;
    /**
     * 页数
     */
    private Integer pageNum;
    /**
     * 每页显示的条数
     */
    private Integer pageSize;
    /**
     * 起始数
     */
    private Integer startRow;
    /**
     * 终止数
     */
    private Integer endRow;
    /**
     * 总条数
     */
    private Long total;
    /**
     * 总页数
     */
    private Integer pages;

    public Page(){
    	this.pageNum = DEFAULTPAGENUM;
    	this.pageSize = DEFAULTPAGESIZE;
    }
    
    public Page(Integer pageNum, Integer pageSize) {
        this(pageNum, pageSize, SQL_COUNT);
    }

    public Page(Integer pageNum, Integer pageSize, Long total) {
        super(pageSize);
        this.pageNum = pageNum==null?DEFAULTPAGENUM:pageNum;
        this.pageSize = pageSize==null?DEFAULTPAGESIZE:pageSize;
        this.total = total;
        this.startRow = pageNum > 0 ? (pageNum - 1) * pageSize : 0;
        this.endRow = pageNum * pageSize;
    }

    public Page(RowBounds rowBounds, long total) {
        super(rowBounds.getLimit());
        this.pageSize = rowBounds.getLimit();
        this.startRow = rowBounds.getOffset();
        //RowBounds方式默认不求count总数，如果想求count,可以修改这里为SQL_COUNT
        this.total = total;
        this.endRow = this.startRow + this.pageSize;
    }

    public List<E> getList() {
        return this;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public Integer getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getStartRow() {
        return startRow;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", startRow=" + startRow +
                ", endRow=" + endRow +
                ", total=" + total +
                ", pages=" + pages +
                '}';
    }
}