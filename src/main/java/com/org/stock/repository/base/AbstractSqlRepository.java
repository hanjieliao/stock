package com.org.stock.repository.base;

import com.org.stock.base.PageResultVO;
import org.nutz.castor.Castors;
import org.nutz.dao.Dao;
import org.nutz.dao.FieldFilter;
import org.nutz.dao.FieldMatcher;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.impl.sql.SqlTemplate;
import org.nutz.dao.impl.sql.callback.QueryEntityCallback;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.dao.util.Daos;
import org.nutz.lang.Lang;
import org.springframework.beans.factory.annotation.Autowired;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 抽象的sql仓储实现
 * @author hanjie.l
 *
 */
public abstract class AbstractSqlRepository {

	@Autowired
	protected SqlTemplate sqlTemplate;

	protected Dao getDao(){
		return sqlTemplate.dao();
	}

	/**
	 * 通过sql查询单个对象
	 * @param targetClass
	 * @param sql  例如 SELECT * FROM player WHERE age=@1 AND name=@2;
	 *             这里@num是个编号，从1开始, 表示 params数组对应坐标的参数
	 * @param params 参数
	 * @return
	 */
	protected <P> P selectOneBySql(Class<P> targetClass, String sql, Object... params) {
		List<P> resultList = selectListBySql(targetClass, sql, params);
		if(resultList != null && !resultList.isEmpty()){
			return resultList.get(0);
		}
		return null;
	}


	/**
	 * 自定义sql分页查询
	 * @param targetClass
	 * @param sqlStr
	 * @param params
	 * @param pageNumber
	 * @param pageSize
	 * @param <P>
	 * @return
	 */
	protected <P> PageResultVO<P> selectPage(Class<P> targetClass, String sqlStr, Map<String, Object> params, int pageNumber, int pageSize) {
		Pager pager = getDao().createPager(pageNumber, pageSize);
		Sql sql = Sqls.create(sqlStr);
		sql.setPager(pager);
		sql.setCallback(Sqls.callback.entities());
		sql.setEntity(getDao().getEntity(targetClass));
		if(params != null){
			sql.params().putAll(params);
		}
		pager.setRecordCount((int) Daos.queryCount(getDao(), sql));// 记录数需手动设置
		if(pager.getRecordCount() <= 0){
			return new PageResultVO<>(new ArrayList<>(), pager);
		}
		getDao().execute(sql);
		List<P> list = sql.getList(targetClass);
		return new PageResultVO<>(list, pager);
	}

	/**
	 * 通过sql查询单个对象
	 * @param targetClass
	 * @param sql  例如 SELECT * FROM player WHERE age=@age AND name=@name;
	 * @param params 例如 Map<String, Object> params = new HashMap<>();
	 *                  params.put("age",100);
	 *                  params.put("name", "peter");
	 * @return
	 */
	protected <P> P selectOneBySql(Class<P> targetClass, String sql, Map<String, Object> params) {
		List<P> resultList = selectListBySql(targetClass, sql, params);
		if(resultList != null && !resultList.isEmpty()){
			return resultList.get(0);
		}
		return null;
	}

	/**
	 * 通过sql查询对象List
	 * @param targetClass
	 * @param sql  例如 SELECT * FROM player WHERE age=@1 AND name=@2;
	 *             这里@num是个编号，从1开始, 表示 params数组对应坐标的参数
	 * @param params 参数
	 * @return
	 */
	protected <P> List<P> selectListBySql(Class<P> targetClass, String sql, Object... params) {
		Map<String, Object> buildParams = buildParams(params);
		return selectListBySql(targetClass, sql, buildParams);
	}

	/**
	 * 通过sql查询对象List
	 * @param targetClass
	 * @param sql  例如 SELECT * FROM player WHERE age=@age AND name=@name;
	 * @param params 例如 Map<String, Object> params = new HashMap<>();
	 *                  params.put("age",100);
	 *                  params.put("name", "peter");
	 * @return
	 */
	protected <P> List<P> selectListBySql(Class<P> targetClass, String sql, Map<String, Object> params) {
		if(Number.class.isAssignableFrom(targetClass) || targetClass.equals(String.class) || targetClass.equals(Date.class) || targetClass.isEnum()){
			return sqlTemplate.queryForList(sql, null, params, targetClass);
		}else{
			return sqlTemplate.query(sql, params, targetClass);
		}
	}

	/**
	 * 通过sql查询对象List
	 * @param targetClass
	 * @param sql  例如 Sqls.create("select * FROM player").setCondition(Cnd.NEW().and("id", "=", "1"));
	 * @return
	 */
	protected <P> List<P> selectListBySql(Class<P> targetClass, Sql sql) {
		List<P> list = null;
		if(Number.class.isAssignableFrom(targetClass) || targetClass.equals(String.class) || targetClass.equals(Date.class) || targetClass.isEnum()){
			//简单对象list
			sql.setCallback(new SqlCallback() {
				@Override
				public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
					List<P> list = new ArrayList<P>();
					while (rs.next()) {
						P result = Castors.me().castTo(rs.getObject(1), targetClass);
						list.add(result);
					}
					return list;
				}
			});

			getDao().execute(sql);
			list = sql.getList(targetClass);
		}else{
			//javabean对象list
			Entity<P> entity = getDao().getEntity(targetClass);
			sql.setCallback(Sqls.callback.entities());
			sql.setEntity(entity);

			getDao().execute(sql);

			list = sql.getList(entity.getType());
		}
		return list;
	}

	/**
	 * 通过sql对象进行分页查询
	 * @param targetClass
	 * @param sql  例如 Sqls.create("select * FROM player @condition").setCondition(Cnd.NEW().and("id", "=", "1"));
	 * @return
	 */
	protected <P> PageResultVO<P> selectPage(Class<P> targetClass, Sql sql, int pageNumber, int pageSize) {
		Pager pager = getDao().createPager(pageNumber, pageSize);
		sql.setPager(pager);
		//先查总共多少条，是0直接返回了
		pager.setRecordCount((int) Daos.queryCount(getDao(), sql));// 记录数需手动设置
		if(pager.getRecordCount() <= 0){
			return new PageResultVO<>(new ArrayList<>(), pager);
		}

		List<P> list = null;
		if(Number.class.isAssignableFrom(targetClass) || targetClass.equals(String.class) || targetClass.equals(Date.class) || targetClass.isEnum()){
			//简单对象list
			sql.setCallback(new SqlCallback() {
				@Override
				public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
					List<P> list = new ArrayList<P>();
					while (rs.next()) {
						P result = Castors.me().castTo(rs.getObject(1), targetClass);
						list.add(result);
					}
					return list;
				}
			});

			getDao().execute(sql);
			list = sql.getList(targetClass);
		}else{
			//javabean对象list
			Entity<P> entity = getDao().getEntity(targetClass);
			sql.setCallback(new QueryEntityCallback(){
				@Override
				public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
					Entity<?> en = entity;
					FieldMatcher fmh = sql.getContext().getFieldMatcher();
					if (null == fmh)
						sql.getContext().setFieldMatcher(FieldFilter.get(en.getType()));
					return process(rs, en, sql.getContext());
				}
			});
			getDao().execute(sql);
			list = sql.getList(entity.getType());
		}
		return new PageResultVO<>(list, pager);
	}

	/**
	 * 执行更新或删除sql
	 * @param sql  例如 SELECT * FROM player WHERE age=@1 AND name=@2;
	 *             这里@num是个编号，从1开始, 表示 params数组对应坐标的参数
	 * @param params 参数
	 */
	protected int updateOrDeleteSql(String sql, Object... params) {
		Map<String, Object> buildParams = buildParams(params);
		return sqlTemplate.update(sql, buildParams);
	}

	/**
	 * 执行更新或删除sql
	 * @param sql  例如 SELECT * FROM player WHERE age=@age AND name=@name;
	 * @param params 例如 Map<String, Object> params = new HashMap<>();
	 *                  params.put("age",100);
	 *                  params.put("name", "peter");
	 */
	protected int updateOrDeleteSql(String sql, Map<String, Object> params) {
		return sqlTemplate.update(sql, params);
	}
	/**
	 * 批量执行sql
	 * @param sqls
	 * @return
	 */
	public void excSqls(Collection<Sql> sqls){
		getDao().execute(sqls.toArray(new Sql[sqls.size()]));
	}

	/**
	 * 填充参数
	 * @param params
	 */
	private Map<String, Object> buildParams(Object... params){
		Map<String, Object> paramsMap = new HashMap<>();
		if(params != null && params.length > 0){
			for(int i = 0; i < params.length; i++){
				Object param = params[i];
				String name = "" + (i+1);//约定查询参数名 $1 $2 $3..这样子 如:select * from userInfo where name=:$1

				paramsMap.put(name, param);
			}
		}
		return paramsMap;
	}
}
