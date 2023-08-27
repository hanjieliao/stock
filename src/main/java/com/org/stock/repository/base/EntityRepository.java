package com.org.stock.repository.base;

import com.org.stock.base.PageReq;
import com.org.stock.base.PageResultVO;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;

import java.io.Serializable;
import java.util.List;

/**
 * 实体仓储接口
 * @author hanjie.l
 *
 */
public interface EntityRepository<T extends IEntity<K>, K extends Serializable> {

	/**
	 * 查询表里所有结果
	 * @return
	 */
	List<T> listAll();

	/**
	 * 根据id获取对应的实体
	 * @param id
	 * @return
	 */
	T get(K id);

	/**
	 * 条件查询查询结果
	 *  例如
	 *  Cnd.where("name", "=", "peter").and("mobile", "=", 13724010194L);
	 *  Cnd.where("name", "=", "peter").or("mobile", "=", 13724010194L).orderBy("id", "DESC");
	 * @param cnd
	 * @return
	 */
	T getByCnd(Cnd cnd);

	/**
	 * 条件查询查询结果数量
	 *
	 * @param cnd
	 * @return
	 */
	int countByCnd(Cnd cnd);


	/**
	 * 分页搜索
	 * @param pageReq
	 * @return
	 */
	PageResultVO<T> search(PageReq pageReq, String ... fields);

	/**
	 * 带条件分页搜索
	 * @param pageReq
	 * @return
	 */
	PageResultVO<T> search(Cnd cnd, PageReq pageReq, String ... fields);

	/**
	 * 条件查询查询结果
	 *
	 * @param cnd
	 * @return
	 */
	List<T> list(Cnd cnd);

	/**
	 * 分页查询
	 * @param cnd
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	PageResultVO<T> list(Cnd cnd, int pageNumber, int pageSize);

	/**
	 * 分页查询
	 * @param cnd
	 * @param pageReq
	 * @param pageReq
	 * @return
	 */
	PageResultVO<T> list(Cnd cnd, PageReq pageReq);

	/**
	 * 更新实体
	 * @param entity
	 */
	int update(T entity);

	/**
	 * 批量更新实体
	 * @param entitys
	 */
	int update(List<T> entitys);

	/**
	 * 更新实体(不更新空字段)
	 * @param entity
	 */
	int updateIgnoreNull(T entity);

	/**
	 * 批量更新实体(不更新空字段)
	 * @param entitys
	 */
	int updateIgnoreNull(List<T> entitys);

	/**
	 * 通过cnd来更新
	 * 示例: update(Chain.make("status", 1), Cnd.where("id", "=", 1));
	 * @param chain
	 * @param cnd
	 * @return
	 */
	int update(Chain chain, Condition cnd);

	/**
	 * 添加实体
	 * @param entity
	 */
	T insert(T entity);

	/**
	 * 批量添加实体
	 * @param entitys
	 */
	List<T> insert(List<T> entitys);

	/**
	 * 存在则更新，否则插入
	 * @param entity
	 * @return
	 */
	T insertOrUpdate(T entity);

	/**
	 * 移除实体
	 * @param entity
	 */
	int delete(T entity);

	/**
	 * 通过主键id删除
	 * @param id
	 */
	int deleteById(Serializable id);

	/**
	 * 条件删除
	 * @param cnd
	 */
	int deleteByCnd(Cnd cnd);
}
