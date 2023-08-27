package com.org.stock.repository.base;

import com.org.stock.base.PageReq;
import com.org.stock.base.PageResultVO;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.*;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.cri.SqlExpression;
import org.nutz.dao.util.cri.SqlExpressionGroup;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 抽象的实体仓储实现
 *
 * @author hanjie.l
 *
 * @param <T>
 *            实体类
 * @param <K>
 *            实体类的主键
 */
public abstract class AbstractEntityRepository<T extends IEntity<K>, K extends Serializable>
		extends AbstractSqlRepository implements EntityRepository<T, K> {

	/** 实体类 */
	protected Class<T> entityClass;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AbstractEntityRepository() {
		this.entityClass = (Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@Override
	public List<T> listAll() {
		return getDao().query(entityClass, Cnd.NEW());
	}

	@Override
	public T get(K id) {
		if (id instanceof Number) {
			return getDao().fetch(entityClass, ((Number) id).longValue());
		} else {
			return getDao().fetch(entityClass, (String) id);
		}
	}

	@Override
	public int update(T entity) {
		return getDao().update(entity);
	}

	@Override
	public int update(List<T> entitys) {
		return getDao().update(entitys);
	}

	@Override
	public int updateIgnoreNull(T entity) {
		return getDao().updateIgnoreNull(entity);
	}

	@Override
	public int updateIgnoreNull(List<T> entitys) {
		return getDao().updateIgnoreNull(entitys);
	}

	@Override
	public int update(Chain chain, Condition cnd) {
		return getDao().update(entityClass, chain, cnd);
	}

	@Override
	public T insert(T entity) {
		return getDao().insert(entity);
	}

	@Override
	public List<T> insert(List<T> entitys) {
		return getDao().insert(entitys);
	}

	@Override
	public T insertOrUpdate(T entity) {
		return getDao().insertOrUpdate(entity);
	}

	@Override
	public int delete(T entity) {
		return getDao().delete(entity);
	}

	@Override
	public int deleteById(Serializable id) {
		if (id instanceof Number) {
			return getDao().delete(entityClass, ((Number) id).longValue());
		} else {
			return getDao().delete(entityClass, (String)id);
		}
	}

	@Override
	public int deleteByCnd(Cnd cnd) {
		return getDao().clear(entityClass, cnd);
	}

	/**
	 * 条件查询查询结果
	 *
	 * @param cnd
	 * @return
	 */
	@Override
	public List<T> list(Cnd cnd) {
		List<T> list = getDao().query(entityClass, cnd);
		return list == null? new ArrayList<>() : list;
	}

	/**
	 * 条件查询查询结果
	 *
	 * @param cnd
	 * @return
	 */
	@Override
	public T getByCnd(Cnd cnd) {
		List<T> list = getDao().query(entityClass, cnd);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 条件查询查询结果数量
	 *
	 * @param cnd
	 * @return
	 */
	public int countByCnd(Cnd cnd) {
		return getDao().count(entityClass, cnd);
	}

	/**
	 * 分页搜索
	 * @param pageReq
	 * @param fields
	 * @return
	 */
	@Override
	public PageResultVO<T> search(PageReq pageReq, String... fields) {
		return search(Cnd.NEW(), pageReq, fields);
	}

	/**
	 * 条件分页搜索
	 * @param cnd
	 * @param pageReq
	 * @param fields
	 * @return
	 */
	@Override
	public PageResultVO<T> search(Cnd cnd, PageReq pageReq, String... fields) {
		if(!StringUtils.isBlank(pageReq.getSearch()) && fields != null && fields.length > 0){
			SqlExpressionGroup expressionGroup = new SqlExpressionGroup();

			for (String field : fields) {
				SqlExpression exp1 = Cnd.likeEX(field, pageReq.getSearch());
				expressionGroup.or(exp1);
			}
			cnd.and(expressionGroup);
		}
		return list(cnd, pageReq);
	}

	/**
	 * 分页查询结果
	 *
	 * @param cnd
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageResultVO<T> list(Cnd cnd, int pageNumber, int pageSize) {
		Pager pager = getDao().createPager(pageNumber, pageSize);
		List<T> list = getDao().query(entityClass, cnd, pager);
		pager.setRecordCount(getDao().count(entityClass, cnd));
		return new PageResultVO<T>(list == null? new ArrayList<>() : list, pager);
	}

	/**
	 * 分页查询结果
	 *
	 * @param cnd
	 * @param pageReq
	 * @return
	 */
	@Override
	public PageResultVO<T> list(Cnd cnd, PageReq pageReq) {
		return list(cnd, pageReq.getPageNum(), pageReq.getPageSize());
	}

	/**
	 * 自定义sql分页查询
	 * @param sql
	 * @param params
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	protected PageResultVO<T> selectPage(String sql, Map<String, Object> params, int pageNumber, int pageSize){
		PageResultVO<T> tPageResultVO = selectPage(entityClass, sql, params, pageNumber, pageSize);
		return tPageResultVO;
	}

	public boolean exitsKbDatabase(Dao dao, String database) {
		//判断数据库是否存在
		Sql sql = Sqls.create("SELECT count(*) num FROM pg_database where datname = @name");
		sql.setParam("name", database);
		sql.setCallback(Sqls.callback.integer());
		Sql execute = dao.execute(sql);
		Object result = execute.getResult();
		return result == null || Integer.parseInt(result.toString()) > 0;
	}
}
