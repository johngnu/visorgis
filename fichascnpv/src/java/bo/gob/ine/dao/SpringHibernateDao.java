/*
 * ICG SRL - International Consulting Group 2011
 */
package bo.gob.ine.dao;

import com.google.gson.Gson;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.heyma.db.utils.StatementUtil;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Dao implement wiht hibernate and PostgreSQL
 *
 * @author Johnston Castillo Valencia email: john.gnu@gmail.com
 * @since 01-03-2011
 */
@Repository
public class SpringHibernateDao implements Dao {

    @Autowired
    SessionFactory sessionFactory;

    @Transactional
    public void persist(Object entity) {
        this.sessionFactory.getCurrentSession().saveOrUpdate(entity);
    }

    @Transactional
    public void update(Object entity) {
        this.sessionFactory.getCurrentSession().update(entity);
    }

    @Transactional
    public void persist(Object[] entities) {
        for (int i = 0; i < entities.length; i++) {
            persist(entities[i]);
        }
    }

    @Transactional(readOnly = true)
    public <T> List<T> find(Class<T> entityClass) {
        return this.sessionFactory.getCurrentSession().createQuery("from " + entityClass.getName()).list();
    }

    @Transactional(readOnly = true)
    public <T> T load(Class<T> entityClass, Serializable id) {
        return (T) this.sessionFactory.getCurrentSession().load(entityClass, id);
    }

    @Transactional(readOnly = true)
    public <T> T get(Class<T> entityClass, Serializable id) {
        return (T) this.sessionFactory.getCurrentSession().get(entityClass, id);
    }

    @Transactional(readOnly = true)
    public <T> List<T> find(String hql) {
        Query q = this.sessionFactory.getCurrentSession().createQuery(hql);
        return q.list();
    }

    @Transactional
    public void remove(Object entity) {
        this.sessionFactory.getCurrentSession().delete(entity);
    }

    @Transactional
    public <T> List<T> pagination(Class<T> entityClass, int start, int limit) {
        Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(entityClass);
        criteria.setFirstResult(start);
        criteria.setMaxResults(limit);
        return criteria.list();
    }

    @Transactional
    public void execute(String sql) {
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
        query.executeUpdate();
    }

    @Transactional
    public void execute(String sql, Long entity, Long chlid) {
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
        query.setParameter(0, entity);
        query.setParameter(1, chlid);
        query.executeUpdate();
    }

    @Transactional
    public void execute(String sql, Object[] params) {
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
        int i = 0;
        Object obs = new Object[]{};
        for (Object o : params) {
            query.setParameter(i, o);
            i++;
        }
        query.executeUpdate();
    }

    @Transactional(readOnly = true)
    public boolean existTable(String schema, String tablename) {
        String st = String.format("SELECT table_name FROM information_schema.tables WHERE table_schema = '%s' AND table_name='%s';", schema, tablename);
        List tables = this.sessionFactory.getCurrentSession().createSQLQuery(st).list();
        return !tables.isEmpty();
    }

    @Transactional
    public void persist(String SQL, Map<String, Object> values) {
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(SQL);
        String[] params = query.getNamedParameters();
        for (String param : params) {
            query.setParameter(param, values.get(param));
        }
        query.executeUpdate();
    }

    @Transactional(readOnly = true)
    public <T> List<T> selectSQL(String SQL) {
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(SQL);
        return query.list();
    }

    @Transactional
    public <T> List<T> selectSQLRW(String SQL) {
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(SQL);
        return query.list();
    }

    @Transactional
    public void selectSQLDelMapResult(String SQL, String id) {
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(SQL);
        query.executeUpdate();
    }

    @Transactional(readOnly = true)
    public <T> List<T> selectSQL(Class<T> javaClass, String SQL) {
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(SQL);
        query.setResultTransformer(Transformers.aliasToBean(javaClass));
        return query.list();
    }

    @Transactional(readOnly = true)
    public <T> T selectUniqueSQLResult(String SQL) {
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(SQL);
        return (T) query.uniqueResult();
    }

    @Transactional
    public <T> List<T> selectSQL(String SQL, Map values) {
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(SQL);
        int i = 0;
        for (Object ob : values.entrySet()) {
            Entry en = (Entry) ob;
            query.setParameter(i, en.getValue());
            i++;
        }
        return query.list();
    }

    @Transactional
    public void remove(String SQL, Long id) {
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(SQL);
        query.setParameter(0, id);
        query.executeUpdate();
    }

    @Transactional
    public void update(String SQL, Map<String, String> tableMap, Map<String, Object> insertable) {
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(SQL);
        for (Entry<String, String> c : tableMap.entrySet()) {
            query.setParameter(c.getKey(), insertable.get(c.getKey()));
        }
        query.executeUpdate();
    }

    @Transactional
    public void persist(String SQL, Object[] values) {
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(SQL);
        int i = 0;
        for (Object o : values) {
            query.setParameter(i, o);
            i++;
        }
        query.executeUpdate();
    }

    @Transactional
    public void update(String SQL, Map<String, Object> input) {
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(SQL);
        for (Entry<String, Object> entry : input.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        //query.setParameter(i, id);
        query.executeUpdate();

    }

    @Transactional
    public void updateSingleField(String SQL, Object id, Object input) {
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(SQL);
        query.setParameter("id", id);
        query.setParameter("input", input);
        query.executeUpdate();
    }

    @Transactional
    public Map<String, Object> get(String SQL) {
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(SQL);
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return (HashMap<String, Object>) query.uniqueResult();
    }

    @Transactional
    public Map<String, Object> getObjectMap(String SQL) {
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(SQL);
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return (HashMap<String, Object>) query.uniqueResult();
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> selectSQLMapResult(String SQL) {
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(SQL);
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        List<Map<String, Object>> aliasToValueMapList = query.list();
        return aliasToValueMapList;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> selectSQLMapResult(String SQL, Map<String, Object> params) {
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(SQL);
        for (Entry<String, Object> c : params.entrySet()) {
            query.setParameter(c.getKey(), c.getValue());
        }
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        List<Map<String, Object>> aliasToValueMapList = query.list();
        return aliasToValueMapList;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> selectSQLMapResult(String SQL, Object[] params) {
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(SQL);
        int index = 0;
        for (Object o : params) {
            query.setParameter(index, o);
            index++;
        }
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        List<Map<String, Object>> aliasToValueMapList = query.list();
        return aliasToValueMapList;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> selectSQLMapResult(String SQL, Object value) {
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(SQL);
        String key = query.getNamedParameters()[0];
        query.setParameter(key, value);
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        List<Map<String, Object>> aliasToValueMapList = query.list();
        return aliasToValueMapList;
    }

    @Transactional
    public void persist(String SQL, Map<String, String> table, Map<String, Object> insertable) {
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(SQL);
        System.out.println("setting parameters");
        for (Entry<String, String> c : table.entrySet()) {
            query.setParameter(c.getKey(), insertable.get(c.getKey()));
        }
        query.executeUpdate();
        System.out.println(new Gson().toJson(query.getQueryReturns()));
    }

    @Transactional(readOnly = true)
    public Map<String, Object> get(String SQL, Object ID) {
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(SQL);
        query.setParameter(0, ID);
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return (HashMap<String, Object>) query.uniqueResult();
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> find(String SQL, Map<String, Object> params) {
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(SQL);
        for (Entry<String, Object> p : params.entrySet()) {
            query.setParameter(p.getKey(), p.getValue());
        }
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return query.list();
    }

    @Transactional(readOnly = true)
    public boolean existObject(String schema, String entity, String field, Object value) {
        String SQL = StatementUtil.getCountByFieldFilterStatement(schema, entity, field);
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(SQL);
        query.setParameter(field, value);
        Integer count = new Integer(query.uniqueResult().toString());
        System.out.println("exist object count: " + count);
        return count > 0 ? true : false;
    }

    @Transactional(readOnly = true)
    public boolean existColumn(String schema, String table, String column) {
        SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(StatementUtil.getInfExistColumn(schema, table, column));
        Integer count = new Integer(query.uniqueResult().toString());
        return count > 0 ? true : false;
    }

    @Transactional(readOnly = true)
    public <T> List<T> findHql(String hql) {
        Query q = this.sessionFactory.getCurrentSession().createQuery(hql);
        return q.list();
    }

    @Transactional(readOnly = true)
    public <T> List<T> findHql(String hql, Object o) {
        Query q = this.sessionFactory.getCurrentSession().createQuery(hql);
        q.setProperties(o);
        return q.list();
    }

    @Transactional(readOnly = true)
    public <T> T findHqlForUniqueResult(String hql, Object o) {
        Query q = this.sessionFactory.getCurrentSession().createQuery(hql);
        q.setProperties(o);
        return (T) q.uniqueResult();
    }

    @Transactional
    public void updateHql(String hql, Object o) {
        Query q = this.sessionFactory.getCurrentSession().createQuery(hql);
        q.setProperties(o);
        q.executeUpdate();
    }

}
