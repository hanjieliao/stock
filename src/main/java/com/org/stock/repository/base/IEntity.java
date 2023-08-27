package com.org.stock.repository.base;

import java.io.Serializable;

/**
 * 实体接口
 * @author hanjie.l
 *
 */
public interface IEntity<K extends Serializable>{

	/**
	 * 获取id
	 * @return
	 */
	K getId();
}
