package com.hj.tj.gohome.utils;

import lombok.Data;

import java.io.Serializable;


/**
 * 封装了请求中的分页信息.
 * <p/>
 */
@Data
public class Page implements Serializable {
	/**
	 * 当前页。
	 */
	private int page;

	/**
	 * 每页显示记录数。
	 */
	private int size;

	/**
	 * 排序列标识。
	 */
	private String order;

	public Page() {
		this.page = 1;
		this.size = 10;
	}

	public Page(int page, int size) {
		this.page = page;
		this.size = size;
	}
}
