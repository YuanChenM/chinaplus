/**
 * BaseDao.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.base;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.StringUtil;

/**
 * The base DAO for hibernate.
 */
@Component
public class BaseHibernateDao {

    // private static final int DEFAULT_PAGE = 1;
    /** the default data size in one page */
    private static final int DEFAULT_LIMIT = 10;

    /** the hibernate session factory */
    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Get the current session by session factory.
     * 
     * @return the current session
     */
    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Flush this session.
     */
    public void flush() {
        this.getCurrentSession().flush();
    }

    /**
     * Flush this session.
     */
    public void clear() {
        this.getCurrentSession().clear();
    }

    /**
     * lock this session.
     * 
     * @param o object
     * @param <T> BaseEntity
     */
    public <T extends BaseEntity> void lock(T o) {
        // this.lock(o, LockMode.UPGRADE_NOWAIT);
    }

    /**
     * lock this session.
     * 
     * @param o object
     * @param lockMode lockMode
     * @param <T> BaseEntity
     */
    public <T extends BaseEntity> void lock(T o, LockMode lockMode) {
        this.getCurrentSession().buildLockRequest(new LockOptions(lockMode))
            .setTimeOut(NumberConst.IntDef.INT_TIMEOUTS).lock(o);
    }

    /**
     * Save the data.
     * 
     * @param o the entity data
     * @param <T> the Entity Type
     * @return the generated identifier
     */
    public <T extends BaseEntity> Serializable insert(T o) {
        return this.getCurrentSession().save(o);
    }

    /**
     * Save the data.
     * 
     * @param l the entity datas
     * @param comCount comCount
     * @param <T> the Entity Type
     */
    public <T extends BaseEntity> void insertWithBatch(List<T> l, int comCount) {

        // loop
        int totalSize = l.size();
        for (int i = 0; i < l.size(); i++) {

            // save
            this.getCurrentSession().save(l.get(i));

            // commit
            if ((i + 1) % comCount == 0 || i == totalSize - 1) {

                // clear
                this.getCurrentSession().flush();
                this.getCurrentSession().clear();
            }
        }
    }

    /**
     * Update the data.
     * 
     * @param o the entity data
     * @param <T> the Entity Type
     */
    public <T extends BaseEntity> void update(T o) {
        this.getCurrentSession().update(o);
    }

    /**
     * Delete the data.
     * 
     * @param o the entity data
     * @param <T> the Entity Type
     */
    public <T extends BaseEntity> void delete(T o) {
        this.getCurrentSession().delete(o);
    }

    /**
     * Save or update the data.
     * 
     * @param o the entity data
     * @param <T> the Entity Type
     */
    public <T extends BaseEntity> void saveOrUpdate(T o) {
        this.getCurrentSession().saveOrUpdate(o);
    }

    /**
     * Find the data list by HQL.
     * 
     * @param entity the parameter entity
     * @param <T> the Entity Type
     * @return the result data list
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> List<T> select(T entity) {
        Criteria criteria = this.getCurrentSession().createCriteria(entity.getClass());
        List<T> result = criteria.add(Example.create(entity)).list();
        return result;
    }

    /**
     * Find the data list by HQL.
     * 
     * @param hql the HQL
     * @param <T> the Entity Type
     * @return the result data list
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> List<T> select(String hql) {
        return this.getCurrentSession().createQuery(hql).list();
    }

    /**
     * Find the data list by HQL and conditions array.
     * 
     * @param hql the HQL
     * @param param the query conditions
     * @param <T> the Entity Type
     * @return the result data list
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> List<T> select(String hql, Object[] param) {
        Query q = this.getCurrentSession().createQuery(hql);
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                q.setParameter(i, param[i]);
            }
        }
        return q.list();
    }

    /**
     * Find the data list by HQL and conditions list.
     * 
     * @param hql the HQL
     * @param param the query conditions
     * @param <T> the Entity Type
     * @return the result data list
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> List<T> select(String hql, List<Object> param) {
        Query q = this.getCurrentSession().createQuery(hql);
        if (param != null && param.size() > 0) {
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
        }
        return q.list();
    }

    /**
     * Find the data list with paging by HQL and conditions array.
     * 
     * @param hql the HQL
     * @param param the query conditions
     * @param page the start page No.
     * @param rows the size of page datas
     * @param <T> the Entity Type
     * @return the result data list
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> List<T> select(String hql, Object[] param, Integer page, Integer rows) {

        int start = 0;
        int limit = DEFAULT_LIMIT;

        if (rows != null && rows > 0) {
            limit = rows.intValue();
        }
        if (page != null && page > 0) {
            start = (page - 1) * limit;
        }

        Query q = this.getCurrentSession().createQuery(hql);
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                q.setParameter(i, param[i]);
            }
        }
        return q.setFirstResult(start).setMaxResults(limit).list();
    }

    /**
     * Find the data list by HQL and conditions list.
     * 
     * @param hql the HQL
     * @param param the query conditions
     * @param page the start page No.
     * @param rows the size of page datas
     * @param <T> the Entity Type
     * @return the result data list
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> List<T> select(String hql, List<Object> param, Integer page, Integer rows) {

        int start = 0;
        int limit = DEFAULT_LIMIT;

        if (rows != null && rows > 0) {
            limit = rows.intValue();
        }
        if (page != null && page > 0) {
            start = (page - 1) * limit;
        }

        Query q = this.getCurrentSession().createQuery(hql);
        if (param != null && param.size() > 0) {
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
        }
        return q.setFirstResult(start).setMaxResults(limit).list();
    }

    /**
     * Find the data list by HQL.
     * 
     * @param entity the parameter entity
     * @param sortPropertyName the sort field name
     * @param <T> the Entity Type
     * @return the result data list
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> List<T> selectWithSort(T entity, String sortPropertyName) {
        Criteria criteria = this.getCurrentSession().createCriteria(entity.getClass());
        List<T> result = criteria.add(Example.create(entity)).addOrder(Order.asc(sortPropertyName)).list();
        return result;
    }

    /**
     * Get the entity by id.
     * 
     * @param c the entity class type
     * @param id the entity id
     * @param <T> the Entity Type
     * @return the entity
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> T findOne(Class<T> c, Serializable id) {
        T entity = null;
        Object result = this.getCurrentSession().get(c, id);
        if (result != null && result.getClass() == c) {
            entity = (T) result;
        }
        return entity;
    }

    /**
     * Get the entity by id.
     * 
     * @param entity the entity object
     * @param <T> the Entity Type
     * @return the entity
     */
    public <T extends BaseEntity> T findOne(T entity) {
        T resultEntity = null;
        List<T> results = select(entity);
        if (results != null && !results.isEmpty()) {
            resultEntity = results.get(0);
        }
        return resultEntity;
    }

    /**
     * Find one data by HQL and conditions array.
     * 
     * @param hql the HQL
     * @param param the query conditions
     * @param <T> the Entity Type
     * @return one data
     */
    public <T extends BaseEntity> T findOne(String hql, Object[] param) {
        List<T> l = this.select(hql, param);
        if (l != null && l.size() > 0) {
            return l.get(0);
        } else {
            return null;
        }
    }

    /**
     * Find one data by HQL and conditions list.
     * 
     * @param hql the HQL
     * @param param the query conditions
     * @param <T> the Entity Type
     * @return one data
     */
    public <T extends BaseEntity> T findOne(String hql, List<Object> param) {
        List<T> l = this.select(hql, param);
        if (l != null && l.size() > 0) {
            return l.get(0);
        } else {
            return null;
        }
    }

    /**
     * find count of datas.
     * 
     * @param hql the HQL
     * @return the count of datas
     */
    public int count(String hql) {
        int count = 0;

        Object result = this.getCurrentSession().createQuery(hql).uniqueResult();
        if (result != null && result instanceof Long) {
            count = ((Long) result).intValue();
        } else {
            throw new BusinessException("Get count is fail.");
        }

        return count;
    }

    /**
     * find count of datas by condition array.
     * 
     * @param hql the HQL
     * @param param the query conditions
     * @return the count of datas
     */
    public int count(String hql, Object[] param) {
        Query q = this.getCurrentSession().createQuery(hql);
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                q.setParameter(i, param[i]);
            }
        }

        // return
        return StringUtil.toInteger(q.uniqueResult()) == null ? 0 : StringUtil.toInteger(q.uniqueResult());
    }

    /**
     * find count of datas by condition list.
     * 
     * @param hql the HQL
     * @param param the query conditions
     * @return the count of datas
     */
    public int count(String hql, List<Object> param) {
        Query q = this.getCurrentSession().createQuery(hql);
        if (param != null && param.size() > 0) {
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
        }
        return StringUtil.toInteger(q.uniqueResult()) == null ? 0 : StringUtil.toInteger(q.uniqueResult());
    }

    /**
     * Execute the update or delete hql.
     * 
     * @param hql the HQL
     * @return The number of entities updated or deleted
     */
    public int executeHql(String hql) {
        return this.getCurrentSession().createQuery(hql).executeUpdate();
    }

    /**
     * Execute the update or delete hql with condition array.
     * 
     * @param hql the HQL
     * @param param the query conditions
     * @return The number of entities updated or deleted
     */
    public int executeHql(String hql, Object[] param) {
        Query q = this.getCurrentSession().createQuery(hql);
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                q.setParameter(i, param[i]);
            }
        }
        return q.executeUpdate();
    }

    /**
     * Execute the update or delete hql with condition list.
     * 
     * @param hql the HQL
     * @param param the query conditions
     * @return The number of entities updated or deleted
     */
    public int executeHql(String hql, List<Object> param) {
        Query q = this.getCurrentSession().createQuery(hql);
        if (param != null && param.size() > 0) {
            for (int i = 0; i < param.size(); i++) {
                q.setParameter(i, param.get(i));
            }
        }
        return q.executeUpdate();
    }

}
