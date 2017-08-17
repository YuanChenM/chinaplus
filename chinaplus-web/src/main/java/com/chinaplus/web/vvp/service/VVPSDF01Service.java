/**
 * Common logic for prepare Stock Status.
 * 
 * @screen common
 * @author liu_yinchuan
 */
package com.chinaplus.web.vvp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.ComboData;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.vvp.entity.VVPSDF01Entity;

/**
 * Common logic for Stock Status File.
 */
@Service
public class VVPSDF01Service extends BaseService {

	/**
	 * Find the datas count by condition.
	 * 
	 * @param param
	 *            the query parameter
	 * @return the datas count
	 */
	public int getSelectDatasCount(BaseParam param) {
		return super.getDatasCount(SQLID_FIND_COUNT, param);
	}

	/**
	 * Get Supplier list by condition
	 *
	 * @param param
	 *            filters
	 * @return Supplier list
	 */
	public VVPSDF01Entity getSupplierById(BaseParam param) {

		return (VVPSDF01Entity) super.baseMapper.select(
				getSqlId("findSupplierBySupplierId"), param).get(0);
	}

	/**
	 * Get Supplier list by condition
	 *
	 * @param param
	 *            filters
	 * @return Supplier list
	 */
	public List<VVPSDF01Entity> getAllSupplierList(BaseParam param) {

		return super.baseMapper.select(getSqlId("findAllSupplier"), param);
	}

	/**
	 * Replace the like conditon in parameter.
	 *
	 * @param param
	 *            PageParam
	 */
	public void makeLikeCondition(BaseParam param) {
		// build like condition
		StringUtil.buildLikeCondition(param, "ttcPartsNo");
		StringUtil.buildLikeCondition(param, "partsNameEn");
	}

	/**
	 * getOfficesByUser
	 * 
	 * @param param
	 *            param
	 * @return Map<String, Object>
	 */
	public List<ComboData> getOffices() {
		BaseParam param = new BaseParam();
		return baseMapper.select(getSqlId("getOffices"), param);
	}

	/**
	 * get RegionCode
	 * 
	 * @return List<ComboData> comboDataList
	 */
	public List<ComboData> getRegionCode() {
		BaseParam param = new BaseParam();
		List<ComboData> comboDataList = baseMapper.select(
				this.getSqlId("getRegionCode"), param);
		return comboDataList;
	}

}
