package cn.cdsoft.common.page;


import java.io.Serializable;
import java.util.List;

public class Pager<E> implements Serializable {
	
	private static final long serialVersionUID = -8443066664431485704L;

	public static final Integer DEFAULTPAGENUMBER = 1;
	
	public static final Integer DEFAULTPAGESIZE = 20;
	
	public static final Integer MAX_PAGE_SIZE = 500;// 每页最大记录数限制

	private Integer pageNumber = null;// 当前页码
	
	private Integer pageSize = null;// 每页记录数
	
	private Long total= 0L;// 总记录数
	
	private Integer pageCount = 0;// 总页数
	
	@SuppressWarnings("rawtypes")
	private List rows = null;// 数据List
	
	@SuppressWarnings("rawtypes")
	public List getRows() {
		return rows;
	}

	@SuppressWarnings("rawtypes")
	public void setRows(List rows) {
		this.rows = rows;
	}

	public Pager(){
		this.pageNumber = DEFAULTPAGENUMBER;
		this.pageSize = DEFAULTPAGESIZE;
	}

	public Pager(Integer pageNumber,Integer pageSize){
		if (pageNumber < 1) {
			pageNumber = DEFAULTPAGENUMBER;
		}
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
	}
	
	public Pager(Integer pageNumber,Integer pageSize,Long totalCount){
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.total = totalCount;
	}
	
	@SuppressWarnings("rawtypes")
	public Pager(Page page){
		this.pageNumber = page.getPageNum();
		this.pageSize = page.getPageSize();
		this.total = page.getTotal();
		this.rows = page.getList();
	}
	
	@SuppressWarnings("rawtypes")
	public void setPage(Page page){
		this.pageNumber = page.getPageNum();
		this.pageSize = page.getPageSize();
		this.total = page.getTotal();
		this.rows = page.getList();
	}
	
	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		if (pageNumber < 1) {
			pageNumber = DEFAULTPAGENUMBER;
		}
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if (pageSize < 1) {
			pageSize = DEFAULTPAGENUMBER;
		} else if(pageSize > MAX_PAGE_SIZE) {
			pageSize = MAX_PAGE_SIZE;
		}
		this.pageSize = pageSize;
	}
	

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Integer getPageCount() {
		pageCount = total.intValue() / pageSize;
		if (total % pageSize > 0) {
			pageCount ++;
		}
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public Integer getBeginCount() {
		
		if(pageNumber ==null||pageNumber <0){
			pageNumber = DEFAULTPAGENUMBER;
		}
		
		if(pageSize==null||pageSize<0){
			pageSize = DEFAULTPAGESIZE;
		}
		
		Integer begin = (pageNumber-1)*pageSize;
		
		return begin;
	}


	public Boolean getFirst() {
		return pageNumber==1;
	}

	public Boolean getLast() {
		return pageNumber==pageCount;
	}

}