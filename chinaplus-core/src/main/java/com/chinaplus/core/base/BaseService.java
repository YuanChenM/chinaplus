/**
 * BaseService.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.base;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.chinaplus.common.consts.CodeConst.BatchStatus;
import com.chinaplus.common.entity.TntBatchJob;
import com.chinaplus.common.util.ConfigManager;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * <p>
 * The base class of the service class.
 * </p>
 */
public class BaseService {

    /** the default sql id: find all datas */
    protected static final String SQLID_FIND_ALL = "findAllList";
    /** the default sql id: find all datas count */
    protected static final String SQLID_FIND_COUNT = "findPageListCount";
    /** the default sql id: find all paging datas */
    protected static final String SQLID_FIND_PAGING = "findPageList";

    /** logger */
    // private static Logger logger = LoggerFactory.getLogger(BaseService.class);

    /** hibernate dao */
    @Autowired
    protected BaseHibernateDao baseDao;
    /** mybatis mapper */
    @Autowired
    protected BaseMybatisMapper baseMapper;

    /**
     * Get one data by ID.
     * 
     * @param c the entity class type
     * @param id the entity id
     * @param <T> the entity class type
     * @return the entity
     */
    public <T extends BaseEntity> T getOneById(Class<T> c, Serializable id) {
        T result = baseDao.findOne(c, id);
        return result;
    }

    /**
     * Get one data by parameter.
     * 
     * @param sqlID the sql id for get page datas
     * @param param the query parameter
     * @param <T> the entity class type
     * @return the entity
     */
    public <T extends BaseEntity> T getOneByParam(String sqlID, BaseParam param) {
        T result = baseMapper.findOne(this.getSqlId(sqlID), param);
        return result;
    }

    /**
     * Get one data by parameter entity.
     * 
     * @param param the query parameter entity
     * @param <T> the entity class type
     * @return the entity
     */
    public <T extends BaseEntity> T getOneByEntity(T param) {
        T result = baseDao.findOne(param);
        return result;
    }

    /**
     * Query the data list with paging by default sql.
     *
     * @param param the query parameter
     * @param <T> the entity class type
     * @return the result datas
     * @throws BusinessException the exception
     */
    public <T extends BaseEntity> PageResult<T> getAllList(BaseParam param) throws BusinessException {
        return getAllList(SQLID_FIND_ALL, param);
    }

    /**
     * Query the data list with paging by spectified sql.
     * 
     * @param sqlID the sql id for get page datas
     * @param param the query parameter
     * @param <T> the entity class type
     * @return the result datas
     * @throws BusinessException the exception
     */
    public <T extends BaseEntity> PageResult<T> getAllList(String sqlID, BaseParam param) throws BusinessException {

        // data result
        PageResult<T> result = new PageResult<T>();
        List<T> datas = baseMapper.select(getSqlId(sqlID), param);

        // set query result
        result.setTotalCount(datas == null ? 0 : datas.size());
        result.setDatas(datas);

        return result;
    }

    /**
     * Re set the start for query condition by actual count.
     *
     * @param count the actual count
     * @param start tha start No.
     * @param limit tha datas size in page
     * @return new start No.
     * @throws BusinessException the exception
     */
    protected int resetStart(int count, int start, int limit) throws BusinessException {
        return start < count ? start : (count == 0 ? 0 : count - (count % limit == 0 ? limit : count % limit));
    }

    /**
     * Query the data list with paging by default sql.
     *
     * @param param the query parameter
     * @param <T> the entity class type
     * @return the result datas
     * @throws BusinessException the exception
     */
    public <T extends BaseEntity> PageResult<T> getPageList(PageParam param) throws BusinessException {
        return getPageList(SQLID_FIND_COUNT, SQLID_FIND_PAGING, param);
    }

    /**
     * Query the data list with paging by spectified sql.
     * 
     * @param countSql the sql id for get datas count
     * @param listSql the sql id for get page datas
     * @param param the query parameter
     * @param <T> the entity class type
     * @return the result datas
     * @throws BusinessException the exception
     */
    public <T extends BaseEntity> PageResult<T> getPageList(String countSql, String listSql, PageParam param)
        throws BusinessException {

        // data result
        PageResult<T> result = new PageResult<T>();
        List<T> datas = new ArrayList<T>();

        // count data
        int count = this.getDatasCount(countSql, param);

        // refresh begin data
        if (count > 0) {
            int newstart = this.resetStart(count, param.getStart(), param.getLimit());
            param.setStart(newstart);
            param.setEnd(newstart + param.getLimit());
            datas = baseMapper.select(getSqlId(listSql), param);
        }

        // set the actual query conditions to result
        result.setLimit(param.getLimit());
        result.setStart(param.getStart());
        // set query result
        result.setTotalCount(count);
        result.setDatas(datas);

        return result;
    }

    /**
     * Return true when data exists, Otherwise throw an exception.
     * 
     * @param param the query parameter
     * @return Return true when data exists
     * @throws BusinessException no data found exception
     */
    public boolean existData(BaseParam param) throws BusinessException {
        return this.existData(SQLID_FIND_COUNT, param);
    }

    /**
     * Return true when data exists, Otherwise throw an exception.
     * 
     * @param sqlID the sql id for get datas count
     * @param param the query parameter
     * @return Return true when data exists
     * @throws BusinessException no data found exception
     */
    public boolean existData(String sqlID, BaseParam param) throws BusinessException {
        int dataCount = this.getDatasCount(sqlID, param);
        if (dataCount <= 0) {
            throw new BusinessException(MessageCodeConst.W1012_001);
        }
        return true;
    }

    /**
     * Find the datas count by condition with defalut sql id.
     * 
     * @param param the query parameter
     * @return the datas count
     */
    public int getDatasCount(BaseParam param) {
        return this.getDatasCount(SQLID_FIND_COUNT, param);
    }

    /**
     * Find the datas count by condition.
     * 
     * @param sqlID the sql id for get datas count
     * @param param the query parameter
     * @return the datas count
     */
    public int getDatasCount(String sqlID, BaseParam param) {
        return baseMapper.count(getSqlId(sqlID), param);
    }

    // /**
    // * Find the datas count by condition.
    // *
    // * @param sqlID the sql id for get datas count
    // * @param param the query parameter
    // * @param <T> the entity class type
    // * @return the datas of this page
    // */
    // protected <T extends BaseEntity> List<T> findByPaging(String sqlID, PageParam param) {
    // return baseMapper.findByPaging(getSqlId(sqlID), param);
    // }

    /**
     * Get the mybatis sql id.
     * 
     * @param sqlId the sql id without namespace
     * @return the sql id with namespace
     */
    protected String getSqlId(String sqlId) {

        StringBuffer sb = new StringBuffer();

        if (!StringUtil.isEmpty(sqlId) && sqlId.indexOf(StringConst.DOT) < 0) {

            // the namespace of sql is the service class's full path
            String namespace = this.getClass().getName();

            sb.append(namespace);
            sb.append(StringConst.DOT);
            sb.append(sqlId);
        } else if (sqlId.indexOf(StringConst.DOT) > 0){
            sb.append(sqlId);
        }

        return sb.toString();
    }

    /**
     * Save the data by hibernate dao.
     * 
     * 
     * @param entity the entity data
     * @param <T> the Entity Type
     * @return the generated identifier
     */
    public <T extends BaseEntity> Serializable doInsert(T entity) {
        return baseDao.insert(entity);
    }

    /**
     * Update the data by hibernate dao.
     * 
     * 
     * @param entity the entity data
     * @param <T> the Entity Type
     */
    public <T extends BaseEntity> void doUpdate(T entity) {
        baseDao.update(entity);
    }

    /**
     * Delete the data by hibernate dao.
     * 
     * 
     * @param entity the entity data
     * @param <T> the Entity Type
     */
    public <T extends BaseEntity> void doDelete(T entity) {
        baseDao.delete(entity);
    }

    /**
     * Delete the data by hibernate dao.
     * 
     * 
     * @param c the entity class type
     * @param id the entity id
     * @param <T> the entity class type
     */
    public <T extends BaseEntity> void doDelete(Class<T> c, Serializable id) {
        T entity = baseDao.findOne(c, id);
        if (entity != null) {
            baseDao.delete(entity);
        }
    }

    /**
     * Get current time from database.
     * 
     * @return current time of database
     */
    public Timestamp getDBDateTimeByDefaultTimezone() {
        return baseMapper.getCurrentTime();
    }

    /**
     * Get current time from database.
     * 
     * @param officeTimezone the timezone of current office
     * @return current time of database
     */
    public Timestamp getDBDateTime(String officeTimezone) {
        if (!StringUtil.isEmpty(officeTimezone)) {
            String sourceTimeZone = ConfigManager.getSystemDbTimezone();
            return DateTimeUtil.convertTimezone(baseMapper.getCurrentTime(), sourceTimeZone, officeTimezone);
        } else {
            return getDBDateTimeByDefaultTimezone();
        }
    }

    /**
     * Get next sequence value from sequence.
     * 
     * @param seqName the sequence
     * @return next sequence value
     */
    public Integer getNextSequence(String seqName) {
        return baseMapper.getNextSequence(seqName);
    }

    /**
     * Check batch is running or not.
     *
     * @param officeId office id
     * @return is running or not
     */
    public boolean checkBatchIsRun(Integer officeId) {

        // String sql
        String hql = "FROM TNT_BATCH_JOB A WHERE (A.officeId = ? OR A.officeId IS NULL) AND A.status = ?";
        Object[] param = new Object[] { officeId, BatchStatus.RUNNING };

        // get from database
        List<TntBatchJob> tbjList = baseDao.select(hql, param);
        // check is exist or not
        if (tbjList != null && !tbjList.isEmpty()) {
            return true;
        }

        // does not has
        return false;
    }

}
