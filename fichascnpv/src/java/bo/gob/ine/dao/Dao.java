/*
 * ICG SRL - International Consulting Group 2011
 */
package bo.gob.ine.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author Johnston Castillo Valencia email: john.gnu@gmail.com
 * @since 01-03-2011
 */
public interface Dao {

    public void persist(Object entity);

    public void update(Object entity);

    public void persist(Object[] entities);

    public void remove(Object entity);

    public <T> List<T> find(Class<T> entityClass);

    public <T> T load(Class<T> entityClass, Serializable id);

    public <T> T get(Class<T> entityClass, Serializable id);

    public <T> List<T> find(String hql);

    public <T> List<T> pagination(Class<T> entityClass, int start, int limit);

    public void execute(String sql);

    public void persist(String SQL, Map<String, Object> values);

    public void execute(String sql, Object[] params);

    public boolean existTable(String schema, String tablename);

    public <T> List<T> selectSQL(String SQL);

    public <T> List<T> selectSQLRW(String SQL);

    public <T> List<T> selectSQL(Class<T> javaClass, String SQL);

    public <T> List<T> selectSQL(String SQL, Map values);

    public void remove(String SQL, Long id);

    public void selectSQLDelMapResult(String SQL, String id);

    public void update(String SQL, Map<String, String> tableMap, Map<String, Object> insertable);

    public void persist(String SQL, Object[] values);

    public <T> T selectUniqueSQLResult(String SQL);

    public void update(String SQL, Map<String, Object> input);

    public Map<String, Object> get(String SQL);

    public List<Map<String, Object>> selectSQLMapResult(String SQL);

    public List<Map<String, Object>> selectSQLMapResult(String SQL, Map<String, Object> params);

    public List<Map<String, Object>> selectSQLMapResult(String SQL, Object[] params);

    public List<Map<String, Object>> selectSQLMapResult(String SQL, Object value);

    public void persist(String SQL, Map<String, String> table, Map<String, Object> insertable);

    public Map<String, Object> get(String SQL, Object ID);

    public List<Map<String, Object>> find(String SQL, Map<String, Object> params);

    public boolean existObject(String schema, String entity, String field, Object value);

    public boolean existColumn(String schema, String table, String column);

    public <T> List<T> findHql(String hql);

    public <T> List<T> findHql(String hql, Object o);

    public void updateHql(String hql, Object o);

    public <T> T findHqlForUniqueResult(String hql, Object o);

    public void updateSingleField(String SQL, Object id, Object input);
}
