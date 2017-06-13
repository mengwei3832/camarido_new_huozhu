package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.employer.entity.SendPackageEntity;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 
 * @Description:需要审核的发包请求
 * @ClassName: SendNeedAuditPackageRequest
 * @author: chengtao
 * @date: 2016年6月27日 上午11:09:36
 * 
 */
public class SendNeedAuditPackageRequest extends IRequest {

	/**
	 * 
	 * @Description:
	 * @Title: SendNeedAuditPackageRequest
	 * @param context
	 * @param entity
	 *            发包信息实体
	 * @throws
	 */
	public SendNeedAuditPackageRequest(Context context, SendPackageEntity entity) {
		super(context);
		Log.v("TAG", entity.toString());
		// 发货时限
		mParams.put("PackageStartTime", entity.getStartTime());
		mParams.put("PackageEndTime", entity.getEndTime());
		Log.v("TAG", entity.getStartTime() + "");
		Log.v("TAG", entity.getEndTime() + "");
		// 装货地址
		mParams.put("BeginProvinceId", entity.getZhuangProvinceId());
		mParams.put("BeginCityId", entity.getZhuangCityId());
		mParams.put("BeginRegionId", entity.getZhuangAreaId());
		mParams.put("PackageBegin", entity.getZhuangCutomName());
		mParams.put("PackageBeginAddress", entity.getShipAddress());
		// 卸货地址
		mParams.put("EndProvinceId", entity.getXieProvinceId());
		mParams.put("EndCityId", entity.getXieCityId());
		mParams.put("EndRegionId", entity.getXieAreaId());
		mParams.put("PackageEnd", entity.getXieCutomName());
		mParams.put("PackageEndAddress", entity.getDesChargeAddress());
		// 用车信息
		mParams.put("VehicleList", entity.intArrayToString(entity.getCarList()));
		mParams.put("PackageCount", entity.getVehicleNumberId());
		// 货物信息
		mParams.put("PackageGoodsPrice", entity.getUnitPrice());
		mParams.put("PackageGoodsCategoryId", entity.getCoalNameId());
		// 结算方式
		mParams.put("PackageSettlementType", entity.getPackageSettlementType());
		// 保险
		mParams.put("InsuranceType", entity.getInsuranceType());
		// 发票
		mParams.put("NeedInvoice", entity.getNeedInvoice());
		// 装车费
		mParams.put("LoadingFee", entity.getLoading());
		// 卸车费
		mParams.put("UnloadingFee", entity.getUnLoading());
		// 备注
		mParams.put("PackageMemo", entity.getRemark());
		// 总吨数
		mParams.put("PackageWeight", entity.getSumDun());
		// 车次范围
		mParams.put("CarRange", entity.getVehicleNumberText());
		// 途损率
		mParams.put("ShortFallType",entity.getShortFallType());
		if (entity.getShortFallType() == 10){
			mParams.put("PackageLoseRate", entity.getTuSunLv());
		} else {
			mParams.put("PackageLoseRate", entity.getTuSunDun());
		}
	}

	public SendNeedAuditPackageRequest(Context context, String ContractId,
			String PackageType, int PackageSettlementType, int PackageBegin,
			String PackageBeginAddress, int PackageEnd,
			String PackageEndAddress, float PackagePriceOrigin,
			float PackagePrice, int PackageGoodsCategoryId, int PackageCount,
			float PackageWeight, float PackageGoodsPrice,
			String PackageStartTime, String PackageEndTime, String TdscPhone,
			String TmcPhone1, String TmcPhone2, String TmcPhone3,
			String StcPhone1, String StcPhone2, String StcPhone3,
			int InsuranceType, String NeedInvoice, String PackageMemo) {
		super(context);

	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/PulishPackageDoPendingAudit";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
