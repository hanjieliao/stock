package com.org.stock.base;

import org.nutz.dao.pager.Pager;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询返回对象
 *
 */
public class PageResultVO<T> {

	/** 当前页数据 */
	private List<T> list;
	/** 当前页码 */
	private int pageNum;
	/** 一页最多显示几个数据 */
	private int pageSize;
	/** 一共几页 */
	private int pageCount;
	/** 一共几条记录 */
	private int total;

	/** 分页对象 */
	private transient Pager pager;

	public PageResultVO() {
	}

	/**
	 * 一个分页查询的结果集合
	 *
	 */
	public PageResultVO(List<T> list, Pager pager) {
		this.list = list;
		this.pageNum = pager.getPageNumber();
		this.pageSize = pager.getPageSize();
		this.pageCount = pager.getPageCount();
		this.total = pager.getRecordCount();
		this.pager = pager;
	}

	public PageResultVO(List<T> list, int pageNum, int pageSize, int total){
		this.list = list;
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.pageCount = (total/pageSize) + (total%pageSize > 0? 1 : 0);
		this.total = total;
	}


	/**
	 * 转成vo分页对象
	 * @param v
	 * @param <V>
	 * @return
	 */
	public <V> PageResultVO<V> toVO(Class<V> v){
		ArrayList<V> vs = new ArrayList<>();
		if(list != null){
			for (T t : list) {
				try {
					V vo = v.getConstructor().newInstance();
					BeanUtils.copyProperties(t, vo);
					vs.add(vo);
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		return new PageResultVO<>(vs, this.pager);
	}


	/**
	 * 是否已经是尾页了
	 * @return
	 */
	public boolean isEndPage(){
		return pageNum >= pageCount;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

}
