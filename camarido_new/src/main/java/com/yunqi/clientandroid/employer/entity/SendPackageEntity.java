package com.yunqi.clientandroid.employer.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @Description:
 * @ClassName: SendPackageEntity
 * @author: chengtao
 * @date: 2016年6月25日 下午5:59:26
 * 
 */
public class SendPackageEntity implements Serializable {
	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 8431794442464645287L;
	private Long startTime;
	private Long endTime;
	private int zhuangProvinceId;
	private int zhuangCityId;
	private int zhuangAreaId;
	private String zhuangCutomName;
	private String shipAddress;
	private int xieProvinceId;
	private int xieCityId;
	private int xieAreaId;
	private String xieCutomName;
	private String desChargeAddress;
	private float unitPrice;
	private String coalName;
	private int coalNameId;
	private int vehicleNumberId;
	private int carList[];
	private String carTypeStr;
	private int packageSettlementType;
	private int insuranceType;
	private int needInvoice;
	private float loading;
	private float unLoading;
	private String remark;
	private String tuSunLv;
	private String sumDun;
	private String vehicleNumberText;
	private int shortFallType;
	private String mTuSunDun;

	public String getTuSunDun() {
		return mTuSunDun;
	}

	public void setTuSunDun(String tuSunDun) {
		mTuSunDun = tuSunDun;
	}

	public int getShortFallType() {
		return shortFallType;
	}

	public void setShortFallType(int shortFallType) {
		this.shortFallType = shortFallType;
	}

	public int getVehicleNumberId() {
		return vehicleNumberId;
	}

	public void setVehicleNumberId(int vehicleNumberId) {
		this.vehicleNumberId = vehicleNumberId;
	}

	public String getSumDun() {
		return sumDun;
	}

	public void setSumDun(String sumDun) {
		this.sumDun = sumDun;
	}

	public String getVehicleNumberText() {
		return vehicleNumberText;
	}

	public void setVehicleNumberText(String vehicleNumberText) {
		this.vehicleNumberText = vehicleNumberText;
	}

	public String getTuSunLv() {
		return tuSunLv;
	}

	public void setTuSunLv(String tuSunLv) {
		this.tuSunLv = tuSunLv;
	}

	public SendPackageEntity(Long startTime, Long endTime,
			int zhuangProvinceId, int zhuangCityId, int zhuangAreaId,
			String zhuangCutomName, String shipAddress, int xieProvinceId,
			int xieCityId, int xieAreaId, String xieCutomName,
			String desChargeAddress, float unitPrice, String coalName,
			int coalNameId, int carList[], String carTypeStr,
			int packageSettlementType, int insuranceType, int needInvoice,
			float loading, float unLoading, String remark, String tuSunLv,
			String sumDun, String vehicleNumberText,int shortFallType,String tuSunDun) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.zhuangProvinceId = zhuangProvinceId;
		this.zhuangCityId = zhuangCityId;
		this.zhuangAreaId = zhuangAreaId;
		this.zhuangCutomName = zhuangCutomName;
		this.shipAddress = shipAddress;
		this.xieProvinceId = xieProvinceId;
		this.xieCityId = xieCityId;
		this.xieAreaId = xieAreaId;
		this.xieCutomName = xieCutomName;
		this.desChargeAddress = desChargeAddress;
		this.unitPrice = unitPrice;
		this.coalName = coalName;
		this.coalNameId = coalNameId;
		this.carList = carList;
		this.carTypeStr = carTypeStr;
		this.packageSettlementType = packageSettlementType;
		this.insuranceType = insuranceType;
		this.needInvoice = needInvoice;
		this.loading = loading;
		this.unLoading = unLoading;
		this.remark = remark;
		this.tuSunLv = tuSunLv;
		this.sumDun = sumDun;
		this.vehicleNumberText = vehicleNumberText;
		this.shortFallType = shortFallType;
		this.mTuSunDun = tuSunDun;
	}

	public Long getStartTime() {
		return startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public int getZhuangProvinceId() {
		return zhuangProvinceId;
	}

	public int getZhuangCityId() {
		return zhuangCityId;
	}

	public int getZhuangAreaId() {
		return zhuangAreaId;
	}

	public String getZhuangCutomName() {
		return zhuangCutomName;
	}

	public String getShipAddress() {
		return shipAddress;
	}

	public int getXieProvinceId() {
		return xieProvinceId;
	}

	public int getXieCityId() {
		return xieCityId;
	}

	public int getXieAreaId() {
		return xieAreaId;
	}

	public String getXieCutomName() {
		return xieCutomName;
	}

	public String getDesChargeAddress() {
		return desChargeAddress;
	}

	public float getUnitPrice() {
		return unitPrice;
	}

	public String getCoalName() {
		return coalName;
	}

	public int getCoalNameId() {
		return coalNameId;
	}

	public int[] getCarList() {
		return carList;
	}

	public String getCarTypeStr() {
		return carTypeStr;
	}

	public int getPackageSettlementType() {
		return packageSettlementType;
	}

	public int getInsuranceType() {
		return insuranceType;
	}

	public int getNeedInvoice() {
		return needInvoice;
	}

	public float getLoading() {
		return loading;
	}

	public float getUnLoading() {
		return unLoading;
	}

	public String getRemark() {
		return remark;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public void setZhuangProvinceId(int zhuangProvinceId) {
		this.zhuangProvinceId = zhuangProvinceId;
	}

	public void setZhuangCityId(int zhuangCityId) {
		this.zhuangCityId = zhuangCityId;
	}

	public void setZhuangAreaId(int zhuangAreaId) {
		this.zhuangAreaId = zhuangAreaId;
	}

	public void setZhuangCutomName(String zhuangCutomName) {
		this.zhuangCutomName = zhuangCutomName;
	}

	public void setShipAddress(String shipAddress) {
		this.shipAddress = shipAddress;
	}

	public void setXieProvinceId(int xieProvinceId) {
		this.xieProvinceId = xieProvinceId;
	}

	public void setXieCityId(int xieCityId) {
		this.xieCityId = xieCityId;
	}

	public void setXieAreaId(int xieAreaId) {
		this.xieAreaId = xieAreaId;
	}

	public void setXieCutomName(String xieCutomName) {
		this.xieCutomName = xieCutomName;
	}

	public void setDesChargeAddress(String desChargeAddress) {
		this.desChargeAddress = desChargeAddress;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}

	public void setCoalName(String coalName) {
		this.coalName = coalName;
	}

	public void setCoalNameId(int coalNameId) {
		this.coalNameId = coalNameId;
	}

	public void setCarList(int[] carList) {
		this.carList = carList;
	}

	public void setCarTypeStr(String carTypeStr) {
		this.carTypeStr = carTypeStr;
	}

	public void setPackageSettlementType(int packageSettlementType) {
		this.packageSettlementType = packageSettlementType;
	}

	public void setInsuranceType(int insuranceType) {
		this.insuranceType = insuranceType;
	}

	public void setNeedInvoice(int needInvoice) {
		this.needInvoice = needInvoice;
	}

	public void setLoading(float loading) {
		this.loading = loading;
	}

	public void setUnLoading(float unLoading) {
		this.unLoading = unLoading;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 
	 * @Description:
	 * @Title:intArrayToString
	 * @param array
	 * @return
	 * @return:String
	 * @throws
	 * @Create: 2016年6月27日 下午3:32:31
	 * @Author : chengtao
	 */
	public String intArrayToString(int[] array) {
		String str = "";
		for (int i = 0; i < array.length; i++) {
			if (i != array.length - 1) {
				str += array[i] + ",";
			} else {
				str += array[i] + "";
			}
		}
		return str;
	}

	@Override
	public String toString() {
		return "SendPackageEntity [startTime=" + startTime + ", endTime="
				+ endTime + ", zhuangProvinceId=" + zhuangProvinceId
				+ ", zhuangCityId=" + zhuangCityId + ", zhuangAreaId="
				+ zhuangAreaId + ", zhuangCutomName=" + zhuangCutomName
				+ ", shipAddress=" + shipAddress + ", xieProvinceId="
				+ xieProvinceId + ", xieCityId=" + xieCityId + ", xieAreaId="
				+ xieAreaId + ", xieCutomName=" + xieCutomName
				+ ", desChargeAddress=" + desChargeAddress + ", unitPrice="
				+ unitPrice + ", coalName=" + coalName + ", coalNameId="
				+ coalNameId + ", vehicleNumberId=" + vehicleNumberId
				+ ", carList=" + Arrays.toString(carList) + ", carTypeStr="
				+ carTypeStr + ", packageSettlementType="
				+ packageSettlementType + ", insuranceType=" + insuranceType
				+ ", needInvoice=" + needInvoice + ", loading=" + loading
				+ ", unLoading=" + unLoading + ", remark=" + remark + "]";
	}

}
