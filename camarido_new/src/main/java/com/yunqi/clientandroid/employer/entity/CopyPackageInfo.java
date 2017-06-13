package com.yunqi.clientandroid.employer.entity;

import java.util.Arrays;

import com.yunqi.clientandroid.entity.IDontObfuscate;

public class CopyPackageInfo extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 3949144379496993746L;

	//
	public String ContractId;//
	public String TenantId;//
	public String IsPayFor;//
	public String IsPayIn;//
	public String InsuranceType;//
	public String PakcageCode;//
	public String PackageTenantCode;//
	public String PackageRequireCode;//
	public String PackageStatus;//
	public String PackagePubTime;//
	public String PackageSettlementType;//
	public String PackageBegin;//
	public String PackageBeginName;//
	public String PackageBeginLongitude;//
	public String PackageBeginLatitude;//
	public String PackageBeginAddress;//
	public String PackageEnd;//
	public String PackageEndName;//
	public String PackageEndLongitude;//
	public String PackageEndLatitude;//
	public String PackageEndAddress;//
	public String PackageType;//
	public String PackageGoodsPrice;//
	public String PackagePriceOrigin;//
	public String PackageDistance;//
	public String PackagePrice;//
	public String PackagePriceType;//
	public String PackageWeight;//
	public String Subsidy;//
	public String PackageRoadToll;//
	public String PackageRecommendPath;//
	public String PackageGoodsType;//
	public String PackageGoodsTypeText;//
	public String PackageGoodsKind;//
	public String PackageGoodsKindText;//
	public String PackageGoodsCategoryId;//
	public String PackageGoodsCalorific;//
	public String PackageCount;//
	public String OrderCount;//
	public String PackageAutoPrice;//
	public String PackageAutoTimespan;//
	public String BAddressName;//
	public String EAddressName;//
	public String PackageStartTime;//
	public String PackageEndTime;//
	public String PackageMemo;//
	public String CategoryId;//
	public String CategoryName;//
	public String FocusedTimes;//
	public String TenantName;//
	public String TenantShortName;//
	public String TmcPhone;//
	public String StcPhone;//
	public String LoadingFee;//
	public String UnloadingFee;//
	public String TdscPhone;//
	public String TlscPhone;//
	public String PcsPhone;//
	public boolean NeedInvoice;//
	public String ContractCode;//
	public String PackageBeginProvince;//
	public String PackageBeginCity;//
	public String PackageBeginCityText;//
	public String PackageEndProvince;//
	public String PackageEndCity;//
	public String PackageEndCityText;//
	public String PackageBeginCountry;//
	public String PackageBeginProvinceText;//
	public String PackageBeginCountryText;//
	public String PackageEndCountry;//
	public String PackageEndProvinceText;//
	public String PackageEndCountryText;//
	public String OrderAllCount;//
	public String OrderExecutingCount;//
	public String OrderPendingCount;//
	public String OrderExecutedCount;//
	public String Quotationcount;//
	public String ConfirmQuotationcount;//
	public String OrderSettledCount;//
	public String OrderBeforeSettleCount;//
	public String[] VehicleList;//
	public String Id;//
	public String CreateTime;//
	public String CreateBy;//
	public String UpdateTime;//
	public String UpdateBy;//
	public String IsDeleted;//
	public String PackageLoseRate;
	public String CarRange;
	public int ShortFallType;

	@Override
	public String toString() {
		return "CopyPackageInfo [ContractId=" + ContractId + ", TenantId="
				+ TenantId + ", IsPayFor=" + IsPayFor + ", IsPayIn=" + IsPayIn
				+ ", InsuranceType=" + InsuranceType + ", PakcageCode="
				+ PakcageCode + ", PackageTenantCode=" + PackageTenantCode
				+ ", PackageRequireCode=" + PackageRequireCode
				+ ", PackageStatus=" + PackageStatus + ", PackagePubTime="
				+ PackagePubTime + ", PackageSettlementType="
				+ PackageSettlementType + ", PackageBegin=" + PackageBegin
				+ ", PackageBeginName=" + PackageBeginName
				+ ", PackageBeginLongitude=" + PackageBeginLongitude
				+ ", PackageBeginLatitude=" + PackageBeginLatitude
				+ ", PackageBeginAddress=" + PackageBeginAddress
				+ ", PackageEnd=" + PackageEnd + ", PackageEndName="
				+ PackageEndName + ", PackageEndLongitude="
				+ PackageEndLongitude + ", PackageEndLatitude="
				+ PackageEndLatitude + ", PackageEndAddress="
				+ PackageEndAddress + ", PackageType=" + PackageType
				+ ", PackageGoodsPrice=" + PackageGoodsPrice
				+ ", PackagePriceOrigin=" + PackagePriceOrigin
				+ ", PackageDistance=" + PackageDistance + ", PackagePrice="
				+ PackagePrice + ", PackagePriceType=" + PackagePriceType
				+ ", PackageWeight=" + PackageWeight + ", Subsidy=" + Subsidy
				+ ", PackageRoadToll=" + PackageRoadToll
				+ ", PackageRecommendPath=" + PackageRecommendPath
				+ ", PackageGoodsType=" + PackageGoodsType
				+ ", PackageGoodsTypeText=" + PackageGoodsTypeText
				+ ", PackageGoodsKind=" + PackageGoodsKind
				+ ", PackageGoodsKindText=" + PackageGoodsKindText
				+ ", PackageGoodsCategoryId=" + PackageGoodsCategoryId
				+ ", PackageGoodsCalorific=" + PackageGoodsCalorific
				+ ", PackageCount=" + PackageCount + ", OrderCount="
				+ OrderCount + ", PackageAutoPrice=" + PackageAutoPrice
				+ ", PackageAutoTimespan=" + PackageAutoTimespan
				+ ", BAddressName=" + BAddressName + ", EAddressName="
				+ EAddressName + ", PackageStartTime=" + PackageStartTime
				+ ", PackageEndTime=" + PackageEndTime + ", PackageMemo="
				+ PackageMemo + ", CategoryId=" + CategoryId
				+ ", CategoryName=" + CategoryName + ", FocusedTimes="
				+ FocusedTimes + ", TenantName=" + TenantName
				+ ", TenantShortName=" + TenantShortName + ", TmcPhone="
				+ TmcPhone + ", StcPhone=" + StcPhone + ", LoadingFee="
				+ LoadingFee + ", UnloadingFee=" + UnloadingFee
				+ ", TdscPhone=" + TdscPhone + ", TlscPhone=" + TlscPhone
				+ ", PcsPhone=" + PcsPhone + ", NeedInvoice=" + NeedInvoice
				+ ", ContractCode=" + ContractCode + ", PackageBeginProvince="
				+ PackageBeginProvince + ", PackageBeginCity="
				+ PackageBeginCity + ", PackageBeginCityText="
				+ PackageBeginCityText + ", PackageEndProvince="
				+ PackageEndProvince + ", PackageEndCity=" + PackageEndCity
				+ ", PackageEndCityText=" + PackageEndCityText
				+ ", PackageBeginCountry=" + PackageBeginCountry
				+ ", PackageBeginProvinceText=" + PackageBeginProvinceText
				+ ", PackageBeginCountryText=" + PackageBeginCountryText
				+ ", PackageEndCountry=" + PackageEndCountry
				+ ", PackageEndProvinceText=" + PackageEndProvinceText
				+ ", PackageEndCountryText=" + PackageEndCountryText
				+ ", OrderAllCount=" + OrderAllCount + ", OrderExecutingCount="
				+ OrderExecutingCount + ", OrderPendingCount="
				+ OrderPendingCount + ", OrderExecutedCount="
				+ OrderExecutedCount + ", Quotationcount=" + Quotationcount
				+ ", ConfirmQuotationcount=" + ConfirmQuotationcount
				+ ", OrderSettledCount=" + OrderSettledCount
				+ ", OrderBeforeSettleCount=" + OrderBeforeSettleCount
				+ ", VehicleList=" + Arrays.toString(VehicleList) + ", Id="
				+ Id + ", CreateTime=" + CreateTime + ", CreateBy=" + CreateBy
				+ ", UpdateTime=" + UpdateTime + ", UpdateBy=" + UpdateBy
				+ ", IsDeleted=" + IsDeleted + "]";
	}

}
