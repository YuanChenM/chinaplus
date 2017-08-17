/**
 * BaseMybatisMapper.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.base;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.exception.BusinessException;

/**
 * 
 * The common dao for Mybatis.
 */
@Component
public class BaseMybatisMapper extends SqlSessionDaoSupport {

    /** update type:insert */
    private static final int UPDATE_FLG_INSERT = 1;
    /** update type:update */
    private static final int UPDATE_FLG_UPDATE = 2;
    /** update type:delete */
    private static final int UPDATE_FLG_DELETE = 3;
    /** batch commit */
    private static final int BATCH_COMMIT_COUNT = 5;

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(BaseMybatisMapper.class);

    /**
     * Set the session factory for mybatis.
     * 
     * @param sqlSessionFactory the session factory defined in root-context.xml
     */
    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    /**
     * Do select query sql.
     *
     *
     * @param sqlID the ID of sql
     * @param param the conditions of query
     * @param <T> the Entity Type of result
     * @return the query result
     * @throws BusinessException the exception
     */
    public <T extends BaseEntity> List<T> select(String sqlID, BaseParam param) throws BusinessException {
        try {
            return this.getSqlSession().selectList(sqlID, param);
        } catch (Exception e) {
            logger.debug("Error in select.", e);
            throw new BusinessException("Database Error", e);
        }
    }

    /**
     * Do select query sql.
     *
     *
     * @param sqlID the ID of sql
     * @param entity the condition entity of query
     * @param <T> the Entity Type of result
     * @return the query result
     * @throws BusinessException the exception
     */
    public <T extends BaseEntity> List<T> select(String sqlID, T entity) throws BusinessException {
        return this.getSqlSession().selectList(sqlID, entity);
    }

    /**
     * Do select query sql.
     *
     *
     * @param sqlID the ID of sql
     * @param entity the condition entity of query
     * @param <T> the Entity Type of parameter
     * @param <R> the Entity Type of result
     * @return the query result
     * @throws BusinessException the exception
     */
    public <T extends BaseEntity, R extends BaseEntity> List<R> selectList(String sqlID, T entity) throws BusinessException {
        return this.getSqlSession().selectList(sqlID, entity);
    }

    /**
     * 
     * Insert data to database.
     * 
     * @param sqlID the ID of sql
     * @param param the conditions of query
     * @param <T> the Entity Type
     * @return the count of updated datas
     * @throws BusinessException the exception
     */
    public <T extends BaseEntity> int insert(String sqlID, BaseParam param) throws BusinessException {
        return doUpdate(sqlID, param, UPDATE_FLG_INSERT);
    }

    /**
     * 
     * Insert List<T> to database.
     * 
     * @param sqlID the ID of sql
     * @param EntityList EntityList
     * @param <T> the Entity Type
     * @throws BusinessException the exception
     */
    public <T extends BaseEntity> void insert(String sqlID, List<T> EntityList) throws BusinessException {
        this.getSqlSession().insert(sqlID, EntityList);
    }

    /**
     * 
     * Insert data to database.
     * 
     * @param sqlID the ID of sql
     * @param entity the insert data
     * @param <T> the Entity Type
     * @return the count of updated datas
     * @throws BusinessException the exception
     */
    public <T extends BaseEntity> int insert(String sqlID, T entity) throws BusinessException {
        return doUpdate(sqlID, entity, UPDATE_FLG_INSERT);
    }

    /**
     * 
     * Update data to database.
     * 
     * @param sqlID the ID of sql
     * @param entity the update data
     * @param <T> the Entity Type
     * @return the count of updated datas
     * @throws BusinessException the exception
     */
    public <T extends BaseEntity> int update(String sqlID, T entity) throws BusinessException {
        return doUpdate(sqlID, entity, UPDATE_FLG_UPDATE);
    }

    /**
     * 
     * Update data to database.
     * 
     * @param sqlID the ID of sql
     * @param param the update data param
     * @return the count of updated datas
     * @throws BusinessException the exception
     */
    public int update(String sqlID, BaseParam param) throws BusinessException {
        return doUpdate(sqlID, param, UPDATE_FLG_UPDATE);
    }

    /**
     * 
     * Delete data from database.
     * 
     * @param sqlID the ID of sql
     * @param entity the delete data
     * @param <T> the Entity Type
     * @return the count of deleted datas
     * @throws BusinessException the exception
     */
    public <T extends BaseEntity> int delete(String sqlID, T entity) throws BusinessException {
        return doUpdate(sqlID, entity, UPDATE_FLG_DELETE);
    }

    /**
     * 
     * Delete data from database.
     * 
     * @param sqlID the ID of sql
     * @param param the delete param
     * @return the count of deleted datas
     * @throws BusinessException the exception
     */
    public int delete(String sqlID, BaseParam param) throws BusinessException {
        return doUpdate(sqlID, param, UPDATE_FLG_DELETE);
    }

    /**
     * Update data to database.
     * 
     * @param sqlID the ID of sql
     * @param entity the update data
     * @param updateFlg the update type
     * @param <T> the Entity Type
     * @return the count of updated datas
     * @throws BusinessException the exception
     */
    private <T extends Object> int doUpdate(String sqlID, T entity, Integer updateFlg) throws BusinessException {

        int cnt = 0;
        switch (updateFlg) {
            case UPDATE_FLG_INSERT:
                cnt = this.getSqlSession().insert(sqlID, entity);
                break;
            case UPDATE_FLG_UPDATE:
                cnt = this.getSqlSession().update(sqlID, entity);
                break;
            case UPDATE_FLG_DELETE:
                cnt = this.getSqlSession().delete(sqlID, entity);
                break;
            default:
                break;
        }
        return cnt;
    }

    /**
     * 
     * find a BaseEntity from database
     * 
     * @param sqlID the ID of sql
     * @param param the query condition
     * @param <T> the Entity Type
     * @return the query result
     * @throws BusinessException the exception
     */
    public <T extends BaseEntity> T findOne(String sqlID, BaseParam param) throws BusinessException {
        return this.getSqlSession().selectOne(sqlID, param);
    }

    /**
     *
     * find a BaseEntity from database
     *
     * @param sqlID the ID of sql
     * @param entity the query condition entity
     * @param <T> the Entity Type
     * @return the query result entity
     * @throws BusinessException the exception
     */
    public <T extends BaseEntity> T findOne(String sqlID, T entity) throws BusinessException {
        return this.getSqlSession().selectOne(sqlID, entity);
    }

    /**
     * find count of datas.
     * 
     * @param sqlID the ID of sql
     * @param param the query condition
     * @return the count of result
     * @throws BusinessException the exception
     */
    public int count(String sqlID, BaseParam param) throws BusinessException {
        int cnt = 0;
        Integer obj = (Integer) this.getSqlSession().selectOne(sqlID, param);
        if (obj != null) {
            cnt = obj.intValue();
        }
        return cnt;
    }

    // /**
    // *
    // * find a list from database with paging
    // *
    // * @param sqlID the ID of sql
    // * @param param the query condition
    // * @param <T> the Entity Type
    // * @return the data result in this specified page
    // * @throws BusinessException the exception
    // */
    // protected <T extends BaseEntity> List<T> findByPaging(String sqlID, PageParam param) throws BusinessException {
    //
    // RowBounds rowBounds = new RowBounds(param.getStart(), param.getLimit());
    // return this.getSqlSession().selectList(sqlID, param, rowBounds);
    // }

    // /**
    // *
    // * find a list from database with paging
    // *
    // * @param sqlID the ID of sql
    // * @param param the query condition
    // * @param <T> the Entity Type
    // * @return the data result in this specified page
    // * @throws BusinessException the exception
    // */
    // public <T extends BaseEntity> List<T> findByPagingForComplex(String sqlID, PageParam param)
    // throws BusinessException {
    //
    // return this.getSqlSession().selectList(sqlID, param);
    // }

    /**
     * 
     * find data with loop result.
     * 
     * @param sqlID the ID of sql
     * @param param the query condition
     * @param <T> the Entity Type
     * @param resultHandler the handler for result
     * @throws BusinessException the exception
     */
    public <T extends BaseEntity> void findWithResultHandler(String sqlID, BaseParam param,
        ResultHandler<T> resultHandler) throws BusinessException {
        this.getSqlSession().select(sqlID, param, resultHandler);
    }

    /**
     * insert data to database with batch.
     * 
     * @param sqlID the ID of sql
     * @param entitys the update datas
     * @param <T> the Entity Type
     * @return result
     * @throws BusinessException the exception
     */
    public <T extends BaseEntity> boolean insertWithBatch(String sqlID, List<T> entitys) throws BusinessException {
        return insertWithBatch(sqlID, entitys, BATCH_COMMIT_COUNT);
    }

    /**
     * 
     * insert data to database with batch
     * 
     * @param sqlID the ID of sql
     * @param entitys the update datas
     * @param commitCount the count of batch commit
     * @param <T> the Entity Type
     * @return result
     * @throws BusinessException the exception
     */
    public <T extends BaseEntity> boolean insertWithBatch(String sqlID, List<T> entitys, int commitCount)
        throws BusinessException {

        int totalSize = entitys.size();
        try {

            for (int i = 0; i < totalSize; i++) {
                this.getSqlSession().insert(sqlID, entitys.get(i));
                if ((i + 1) % commitCount == 0 || i == totalSize - 1) {
                    this.getSqlSession().commit();
                    this.getSqlSession().clearCache();
                }
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

        return true;
    }

    /**
     * get current time from database.
     * 
     * @return current time of database
     */
    public Timestamp getCurrentTime() {
        
        Integer diffDay = this.getSqlSession().selectOne("com.chinaplus.common.service.CommonService.getDiffSystemDay");
        
        if (diffDay == null) {
            diffDay = 0;
        }
        
        BaseParam param = new BaseParam();
        param.setSwapData("diffDay", diffDay);
        return this.getSqlSession().selectOne("com.chinaplus.common.service.CommonService.getCurrentTime", param);
    }

    /**
     * get next sequence value from sequence.
     * 
     * @param seqName sequence name
     * @return next sequence value
     */
    public Integer getNextSequence(String seqName) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("seqName", seqName);
        return this.getSqlSession().selectOne("com.chinaplus.common.service.CommonService.getNextSequence", params);
    }

}
