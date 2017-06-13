package com.yunqi.clientandroid.employer.request;

import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

public class SendNotNeedAuditPackageRequest extends IRequest {

	/**
	 * 
	 * @Description:
	 * @Title: SendNeedAuditPackageRequest
	 * @param context
	 * @param ContractId
	 *            合同编号
	 * @param TenantId
	 *            发包方
	 * @param PackageType
	 *            包类型：0：一口价；1：竞价；2：定向指派
	 * @param PackageSettlementType
	 *            结算方式：1：实时；2：定期
	 * @param PackageBegin
	 *            包开始地址
	 * @param PackageBeginAddress
	 *            包开始地址
	 * @param PackageEnd
	 *            包结束地址
	 * @param PackageEndAddress
	 *            包结束地址
	 * @param PackagePriceOrigin
	 *            发包方价格 运费
	 * @param PackagePrice
	 *            司机方显示价格 0
	 * @param PackageGoodsCategoryId
	 *            品类
	 * @param PackageCount
	 *            车数
	 * @param PackageWeight
	 *            包重量（吨）
	 * @param PackageGoodsPrice
	 *            货值单价
	 * @param PackageStartTime
	 *            开始时间
	 * @param PackageEndTime
	 *            结束时间
	 * @param TdscPhone
	 *            发货方联系电话
	 * @param TmcPhone1
	 *            驻矿联系电话
	 * @param TmcPhone2
	 *            驻矿联系电话
	 * @param TmcPhone3
	 *            驻矿联系电话
	 * @param StcPhone1
	 *            签收联系电话
	 * @param StcPhone2
	 *            签收联系电话
	 * @param StcPhone3
	 *            签收联系电话
	 * @param InsuranceType
	 *            保险类型0：无保险；1：平台送保险；2：自己购买保险
	 * @param NeedInvoice
	 *            是否需要发票0：不需要；1：需要
	 * @param PackageMemo
	 *            备注
	 * @throws
	 */
	public SendNotNeedAuditPackageRequest(Context context, String ContractId,
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
		mParams.put("ContractId", ContractId);
		mParams.put("PackageType", PackageType);
		mParams.put("PackageSettlementType", PackageSettlementType);
		mParams.put("PackageBegin", PackageBegin);
		mParams.put("PackageBeginAddress", PackageBeginAddress);
		mParams.put("PackageEnd", PackageEnd);
		mParams.put("PackageEndAddress", PackageEndAddress);
		mParams.put("PackagePriceOrigin", PackagePriceOrigin);
		mParams.put("PackagePrice", PackagePrice);
		mParams.put("PackageGoodsCategoryId", PackageGoodsCategoryId);
		mParams.put("PackageCount", PackageCount);
		mParams.put("PackageWeight", PackageWeight);
		mParams.put("PackageGoodsPrice", PackageGoodsPrice);
		mParams.put("PackageStartTime", PackageStartTime);
		mParams.put("PackageEndTime", PackageEndTime);
		mParams.put("TdscPhone", TdscPhone);
		mParams.put("TmcPhone1", TmcPhone1);
		mParams.put("TmcPhone2", TmcPhone2);
		mParams.put("TmcPhone3", TmcPhone3);
		mParams.put("StcPhone1", StcPhone1);
		mParams.put("StcPhone2", StcPhone2);
		mParams.put("StcPhone3", StcPhone3);
		mParams.put("InsuranceType", InsuranceType);
		mParams.put("NeedInvoice", NeedInvoice);
		mParams.put("PackageMemo", PackageMemo);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Package/PulishPackageDoPublished";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<NullObject, NullObject>>() {
		}.getType();
	}

}
