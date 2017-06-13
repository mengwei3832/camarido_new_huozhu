package com.yunqi.clientandroid.employer.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.R.id;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.adapter.CarTypeAdapter;
import com.yunqi.clientandroid.employer.adapter.GoodsTypeAdapter;
import com.yunqi.clientandroid.employer.entity.AddressBeginAnd;
import com.yunqi.clientandroid.employer.entity.CarType;
import com.yunqi.clientandroid.employer.entity.CopyPackageInfo;
import com.yunqi.clientandroid.employer.entity.GetProvince;
import com.yunqi.clientandroid.employer.entity.PackagePickersInfo;
import com.yunqi.clientandroid.employer.entity.SendPackageEntity;
import com.yunqi.clientandroid.employer.entity.SenumListInfo;
import com.yunqi.clientandroid.employer.entity.TuSunLvBean;
import com.yunqi.clientandroid.employer.request.CarTypeRequest;
import com.yunqi.clientandroid.employer.request.CopyPackageRequest;
import com.yunqi.clientandroid.employer.request.GetCityRequest;
import com.yunqi.clientandroid.employer.request.GetPakagePickersRequest;
import com.yunqi.clientandroid.employer.request.GetProvinceRequest;
import com.yunqi.clientandroid.employer.request.GetSenumListRequest;
import com.yunqi.clientandroid.employer.request.GetTuSunLvRequest;
import com.yunqi.clientandroid.employer.util.SaveSetUtils;
import com.yunqi.clientandroid.employer.util.SpManager;
import com.yunqi.clientandroid.employer.util.UmengStatisticsUtils;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.FilterManager;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.ProgressWheel;
import com.yunqi.clientandroid.utils.StringUtils;
import com.yunqi.clientandroid.utils.T;
import com.yunqi.clientandroid.view.wheel.NewChangeTimePopWin;
import com.yunqi.clientandroid.view.wheel.NewChangeTimePopWin.OnChangeTimeListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @Description:新版发包界面
 * @ClassName: NewSendPackage
 * @author: chengtao
 * @date: 2016年6月21日 下午2:01:22
 */
@SuppressLint("InflateParams")
public class NewSendPackageActivity extends BaseActivity implements
        OnClickListener, OnCheckedChangeListener, OnChangeTimeListener {
    // 包裹发包界面
    private RelativeLayout rlContainer;
    // 进度
    private ProgressWheel progressBar;
    // 是否复制发包
    private boolean isCopyPackage = false;
    // 发货时限
    private RelativeLayout rlTime;
    private TextView tvTime;
    // 装卸地址
    private RelativeLayout rlZhuang;
    private TextView tvZhuang;
    private RelativeLayout rlXie;
    private TextView tvXie;
    // 货品单价
    private EditText etSendPrice;
    // 货品种类
    private TextView tvGoods;
    // 需求车次
    private TextView tvVehicleNumber;
    // 车型需求
    private TextView tvVehicleType;
    // 结算方式
    private TextView tvJieSuanText;
    private CheckBox cbFaPiao;
    // 备注
    private TextView tvNote;
    // 下一步
    private Button btnNext;
    // 货品总吨
    private EditText etZongdun;
    // 途损比率
    private TextView tvTusun;
    private ImageView ivTusun;
    //发票
    private RelativeLayout rlInvoice;

    private RelativeLayout rlVehicle;

    private RelativeLayout rlTuSun;
    //途损方式
    private RelativeLayout rlTuSunType;
    private TextView tvTusunType;
    //途损吨数
    private EditText etTusunDun;
    private TextView tvTusunDanwei;

    // 请求Code
    private int ZHUANG_CODE = 1;
    private int XIE_CODE = 2;
    private final static int NOTE_CODE = 5;

    // 弹窗父级View
    private View parentView;

    // 时间弹窗
    private NewChangeTimePopWin timPop;
    // 车型弹窗
    private PopupWindow carTypePop;
    private TextView tvCancle;
    private TextView tvSure;
    private ListView lvCarTypeView;
    private RelativeLayout rlPopCarType;
    // 货品类型弹窗
    private PopupWindow goodsPop;
    private ListView lvPopGoodsView;
    private RelativeLayout rlPopGoodsLayout;
    private String goodsTypeId;
    private String goodsTypeText;
    // 结算方式弹框(途损方式)
    private PopupWindow settleMentPop;
    private RelativeLayout rlSettleMentLayout;
    private RelativeLayout rlSettleMentTop;
    private RelativeLayout rlSettleMentBottom;
    private int insuranceType;// 保险
    private String packageSettlementType;// 结算方式
    private int needInvoice;// 是否需要发票0：不需要；1：需要
    private TextView tvTJtop1, tvTJtop2, tvTJbottom;
    private int shortFallType = 10;//途损方式，10表示按比率，20表示按吨数

    // 需求车次弹出框
    private PopupWindow vehicleNumberPop;
    private RelativeLayout rlPopVehicleNumber;
    private TextView tvVehicleShi;
    private TextView tvVehicleBai;
    private TextView tvVehicleDuo;

    // 传递的包的ID
    private String packageId;

    // 复制发包的字段
    private long startTime;// 开始时间
    private long endTime;// 结束时间
    private String shipAddress;// 装货地址
    private String dischargeAddress;// 卸货地址
    private String companyShipAddress;// 装货地址别名
    private String companyDischargeAddress;// 卸货地址别名
    private String loading;// 装车费
    private String unLoading;// 卸车费
    private String demand;// 备注
    private int zhuangProvinceId, zhuangCityId, zhuangCountyId;
    private String zhuangProvinceName, zhuangCityName, zhuangCountyName;
    private int xieProvinceId, xieCityId, xieCountyId;
    private String xieProvinceName, xieCityName, xieCountyName;
    private String huodun;// 货品总吨
    private String vehicleNumberTextString;
    private String mTuSunLv;
    private String mTuSunDun;
    // 请求类
    private GetPakagePickersRequest getPakagePickersRequest;
    private CarTypeRequest carTypeRequest;
    private CopyPackageRequest copyPackageRequest;
    private GetSenumListRequest getSenumListRequest;
    private GetTuSunLvRequest getTuSunLvRequest;
    private GetProvinceRequest mGetProvinceRequest;//请求省份
    private GetCityRequest mGetCityRequest;//请求市区

    // 请求ID
    private final int GET_GOODS_TYPE = 1;
    private final int GET_CAR_TYPE = 2;
    private final int COPY_PACKAGE_REQUEST = 3;
    private final int GET_SENUM_LIST_REQUES = 4;
    private final int GET_TUSUNLV = 5;
    private final int GET_PROVINCE = 6;
    private final int GET_CITY = 7;
    private final int GET_DISTRICT = 8;

    // 货品种类集合
    private ArrayList<PackagePickersInfo> goodsTypeList = new ArrayList<PackagePickersInfo>();
    private GoodsTypeAdapter goodsTypeAdapter;

    // 车型需求集合
    private ArrayList<CarType> carTypelist = new ArrayList<CarType>();
    private ArrayList<String> carIdList = new ArrayList<String>();
    private ArrayList<String> carNamelist = new ArrayList<String>();
    private List<GetProvince> mProvinceList = new ArrayList<>();
    private List<GetProvince> mCityList = new ArrayList<>();
    private List<GetProvince> mDistrictList = new ArrayList<>();
    private CarTypeAdapter carTypeAdapter;

    private String priceText;
    private String goodsType;
    private String vehicleNumber;
    private String SettlementType;

    // 复制发包信息
    private CopyPackageInfo packageInfo;
    // 获取复制发包车型信息是否成功
    private boolean isGetCarList = false;

    // 装车费和卸车费临时值
    private String loadingText, unLoadingText;

    // 途损率
    private double tusunNumber = 0;

    //区分途损方式和结算方式弹窗标记,0表示途损方式，1表示结算方式
    private int isTJ = 0;
    //要发票点击的标记
    private boolean isInvoice = false;

    private FilterManager mFilterManager;
    private SpManager mSpManager;

    /* 省市区相关参数 */
    private int i = 0;//标记省份
    private int mProvinceId;//省Id
    private int mCityId;//市Id
    private int mDistrictId;//区Id

    /* 友盟统计 */
    private UmengStatisticsUtils mUmeng;

    @Override
    protected int getLayoutId() {
        return R.layout.new_employer_acitvity_send_package;
    }

    @Override
    protected void initView() {
        mFilterManager = FilterManager.instance(mContext);
        mSpManager = SpManager.instance(mContext);
        mUmeng = UmengStatisticsUtils.instance(mContext);

        CamaridoApp.addDestoryActivity(NewSendPackageActivity.this,
                "NewSendPackageActivity");
        // 初始化ActionBar
        initActionBar();
        // 传递过来的包的ID
        packageId = getIntent().getStringExtra("packageId");

        parentView = NewSendPackageActivity.this.getLayoutInflater().inflate(
                R.layout.new_employer_acitvity_send_package, null);

        // 包裹发包界面
        rlContainer = obtainView(R.id.rl_container);
        // 进度
        progressBar = obtainView(R.id.progress_send_bao);
        // 发货时限
        rlTime = obtainView(R.id.rl_new_time);
        tvTime = obtainView(R.id.tv_time);
        // 装卸地址
        rlZhuang = obtainView(R.id.rl_zhuang);
        tvZhuang = obtainView(R.id.tv_zhuang);
        rlXie = obtainView(R.id.rl_xie);
        tvXie = obtainView(R.id.tv_xie);
        // 货品单价
        etSendPrice = obtainView(R.id.et_new_send_price);
        // 货品种类
        tvGoods = obtainView(R.id.tv_ship_type);
        // 需求车次
        tvVehicleNumber = obtainView(R.id.tv_vehicle_number);
        // 车型要求
        tvVehicleType = obtainView(R.id.tv_vehicle_type);
        // 结算方式
        tvJieSuanText = obtainView(R.id.tv_jiesuan_fanshi);
        // 要发票
        cbFaPiao = obtainView(R.id.cb_fa_piao_true);
        rlInvoice = obtainView(id.rl_fa_piao_true);
        // 备注
        tvNote = obtainView(R.id.tv_note);
        // 下一步
        btnNext = obtainView(R.id.btn_next);
        etZongdun = obtainView(R.id.et_ship_dun_type);

        rlVehicle = obtainView(R.id.rl_vehicle);
        //途损
        tvTusun = obtainView(R.id.tv_tusun_lv_dun);
        ivTusun = obtainView(R.id.iv_tusun_iv_t);
        etTusunDun = obtainView(id.et_tusun_input_dunshu);
        tvTusunDanwei = obtainView(R.id.tv_tusun_dun_danwei);
        rlTuSun = obtainView(R.id.rl_tusun);
        rlTuSunType = obtainView(id.rl_tusun_type);
        tvTusunType = obtainView(R.id.tv_tusui_type);

        // 创建弹框
        timPop = new NewChangeTimePopWin(mContext, this, true);

        // 初始化界面
        rlContainer.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        // 判断是否复制发包请求接口
        if (packageId != null && !TextUtils.isEmpty(packageId)) {
            // 是复制发包
            isCopyPackage = true;
        } else {
            getTuSunLvRequest = new GetTuSunLvRequest(mContext);
            getTuSunLvRequest.setRequestId(GET_TUSUNLV);
            httpGet(getTuSunLvRequest);
        }
        carTypeAdapter = new CarTypeAdapter(carTypelist, mContext, carIdList);
    }

    /**
     * @throws
     * @Description:初始化ActionBar
     * @Title:initActionBar
     * @return:void
     * @Create: 2016年6月21日 下午2:37:45
     * @Author : chengtao
     */
    private void initActionBar() {
        setActionBarTitle("发货信息");
        setActionBarLeft(R.drawable.fanhui);
        setOnActionBarLeftClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void setListener() {
        // 发货时限
        rlTime.setOnClickListener(this);
        // 装货地址
        rlZhuang.setOnClickListener(this);
        // 卸货地址
        rlXie.setOnClickListener(this);
        // 货品种类
        tvGoods.setOnClickListener(this);
        // 车型要求
        tvVehicleType.setOnClickListener(this);
        // 备注
        tvNote.setOnClickListener(this);
        // 结算方式
        tvJieSuanText.setOnClickListener(this);
        // 索要发票
        cbFaPiao.setOnCheckedChangeListener(this);
        rlInvoice.setOnClickListener(this);
        // 下一步
        btnNext.setOnClickListener(this);
        // 需求车次
        rlVehicle.setOnClickListener(this);
        // 途损比率
        tvTusun.setOnClickListener(this);
        //途损方式
        rlTuSunType.setOnClickListener(this);
        //途损吨数保留两位小数
        etTusunDun.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String temp = s.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0)
                    return;
                if (temp.length() - posDot - 1 > 2) {
                    s.delete(posDot + 3, posDot + 4);
                }
            }
        });
        //总吨数的控制
        etZongdun.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String zongDun = etZongdun.getText().toString().trim();

                if (zongDun == "." || zongDun.equals(".")) {
                    showToast("请不要以小数点开头");
                    return;
                }

                if (StringUtils.isStrNotNull(zongDun)) {
                    // 获取总吨数
                    double sumDunnum = Double.valueOf(zongDun);
                    if (sumDunnum <= 400) {
                        tvVehicleNumber.setText("10车以内");
                    } else if (sumDunnum <= 4000 && sumDunnum > 400) {
                        tvVehicleNumber.setText("100车以内");
                    } else if (sumDunnum > 4000) {
                        tvVehicleNumber.setText("大量上车");
                    }
                }

            }
        });

        //根据总吨数显示车数
        etZongdun.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String zongDun = etZongdun.getText().toString().trim();
                    if (StringUtils.isStrNotNull(zongDun)) {
                        // 获取总吨数
                        double sumDunnum = Double.valueOf(zongDun);
                        if (sumDunnum <= 400) {
                            tvVehicleNumber.setText("10车以内");
                        } else if (sumDunnum <= 4000 || sumDunnum > 400) {
                            tvVehicleNumber.setText("100车以内");
                        } else if (sumDunnum > 4000) {
                            tvVehicleNumber.setText("大量上车");
                        }
                    }
                }
            }
        });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void onSuccess(int requestId, Response response) {
        super.onSuccess(requestId, response);
        boolean isSuccess;
        String message;

        switch (requestId) {
            case GET_PROVINCE://省份
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    List<GetProvince> proList = response.data;

                    if (proList != null) {
                        mProvinceList.addAll(proList);
                        try {
                            mSpManager.setProvince("");
                            String str = SaveSetUtils.SceneList2String(mProvinceList);
                            mSpManager.setProvince(str);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (mProvinceList.size() != 0) {
                        mProvinceId = mProvinceList.get(i).id;
                        getCityRequest(mProvinceId);
                    }

                }
                break;
            case GET_CITY://市
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    List<GetProvince> cityList = response.data;

                    if (cityList != null) {
                        mCityList.addAll(cityList);
                        try {
                            mSpManager.setCity(String.valueOf(mProvinceId),"");
                            String str = SaveSetUtils.SceneList2String(mCityList);
                            L.e("----------要保存的省Id------------" + mProvinceId);
                            mSpManager.setCity(String.valueOf(mProvinceId), str);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (i < 6) {

                        L.e("--------i的值------" + i);

                        mProvinceId = mProvinceList.get(i).id;
                        getCityRequest(mProvinceId);
                    }
                }
                break;
            case GET_TUSUNLV:// 途损率
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    TuSunLvBean tuSunLvBean = (TuSunLvBean) response.singleData;

                    tusunNumber = Double.valueOf(tuSunLvBean.PackageLoseRate);

                    if (tusunNumber != 0) {
                        tvTusun.setText(StringUtils.sanToQianFenHao(tusunNumber));
                    } else {
                        tvTusun.setText("0‰");
                    }

                }
                break;

            case GET_GOODS_TYPE: // 货品种类
                isSuccess = response.isSuccess;
                message = response.message;

                if (isSuccess) {
                    ArrayList<PackagePickersInfo> goodsList = response.data;

                    if (goodsList != null) {
                        goodsTypeList.addAll(goodsList);
                    }
                }

                break;

            case GET_CAR_TYPE: // 车型需求
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    ArrayList<CarType> ctlis = response.data;
                    if (ctlis != null) {
                        carTypelist.addAll(ctlis);
                    }
                    //获取省的请求
//                    getProvinceRequest();
                }
                break;
            case COPY_PACKAGE_REQUEST:// 复制发包
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    packageInfo = (CopyPackageInfo) response.singleData;
                    if (packageInfo != null) {
                        setPackageInfo(packageInfo);
                    }
                } else {
                    showToast(message);
                }
                break;
            case GET_SENUM_LIST_REQUES:// 复制发包车型
                isSuccess = response.isSuccess;
                message = response.message;
                ArrayList<SenumListInfo> info = response.data;
                if (isSuccess) {
                    isGetCarList = true;
                    if (info != null) {
                        setCarTypeInfo(info);
                    }
                } else {
                    showToast(message);
                }
                break;
            default:
                break;
        }
        if (isCopyPackage) {// 复制发包
            if (packageInfo != null && goodsTypeList.size() > 0
                    && carTypelist.size() > 0 && isGetCarList) {
                rlContainer.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                carTypeAdapter.notifyDataSetChanged();
            }
        } else {// 普通发包
            if (goodsTypeList.size() > 0 && carTypelist.size() > 0) {
                rlContainer.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    /**
     * @param info
     * @throws
     * @Description:设置车辆信息
     * @Title:setCarTypeInfo
     * @return:void
     * @Create: 2016年6月29日 下午2:10:39
     * @Author : chengtao
     */
    private void setCarTypeInfo(ArrayList<SenumListInfo> info) {
        // 车型要求
        for (SenumListInfo senumListInfo : info) {
            carIdList.add(senumListInfo.Id);
            carNamelist.add(senumListInfo.EnumName);
        }
        if (carNamelist.size() > 0) {
            for (String s : carNamelist) {
                tvVehicleType.setText(tvVehicleType.getText().toString() + s
                        + " ");
            }
        }
    }

    /**
     * @param packageInfo
     * @throws
     * @Description:复制发包设置包信息
     * @Title:setPackageInfo
     * @return:void
     * @Create: 2016年6月27日 下午6:52:44
     * @Author : chengtao
     */
    private void setPackageInfo(CopyPackageInfo packageInfo) {
        // 发货时限
        Log.v("TAG", packageInfo.toString());
        Log.v("TAG", packageInfo.PackageStartTime);
        Log.v("TAG", packageInfo.PackageEndTime);
        startTime = Long.parseLong(packageInfo.PackageStartTime);
        endTime = Long.parseLong(packageInfo.PackageEndTime);
        // 装货地址
        zhuangProvinceId = Integer.parseInt(packageInfo.PackageBeginProvince);
        zhuangCityId = Integer.parseInt(packageInfo.PackageBeginCity);
        zhuangCountyId = Integer.parseInt(packageInfo.PackageBeginCountry);
        zhuangProvinceName = packageInfo.PackageBeginProvinceText;
        zhuangCityName = packageInfo.PackageBeginCityText;
        zhuangCountyName = packageInfo.PackageBeginCountryText;
        companyShipAddress = packageInfo.PackageBeginName;
        shipAddress = packageInfo.PackageBeginAddress;
        // 卸货地址
        xieProvinceId = Integer.parseInt(packageInfo.PackageEndProvince);
        xieCityId = Integer.parseInt(packageInfo.PackageEndCity);
        xieCountyId = Integer.parseInt(packageInfo.PackageEndCountry);
        xieProvinceName = packageInfo.PackageEndProvinceText;
        xieCityName = packageInfo.PackageEndCityText;
        xieCountyName = packageInfo.PackageEndCountryText;
        companyDischargeAddress = packageInfo.PackageEndName;
        dischargeAddress = packageInfo.PackageEndAddress;
        // 货品单价
        priceText = packageInfo.PackageGoodsPrice;
        // 货物种类
        goodsType = packageInfo.CategoryName;
        goodsTypeId = packageInfo.CategoryId;
        // 需求车数
        vehicleNumber = packageInfo.PackageCount;
        //途损方式
        shortFallType = packageInfo.ShortFallType;
        // 结算方式
        packageSettlementType = packageInfo.PackageSettlementType;
        // 保险
        insuranceType = Integer.parseInt(packageInfo.InsuranceType);
        // 途损率
        mTuSunLv = packageInfo.PackageLoseRate;
        // 车次范围
        vehicleNumberTextString = packageInfo.CarRange;
        // 货品总吨
        huodun = packageInfo.PackageWeight;


        // 发票
        if (packageInfo.NeedInvoice) {
            needInvoice = 1;
        } else {
            needInvoice = 0;
        }
        // 装车费
        loading = packageInfo.LoadingFee;
        // 卸车费
        unLoading = packageInfo.UnloadingFee;
        // 备注
        demand = packageInfo.PackageMemo;

        // 设置信息
        // 发货时限
        if (startTime > 0 && endTime > 0) {
            tvTime.setText(StringUtils.formatModify(startTime + "") + " ~ "
                    + StringUtils.formatModify(endTime + ""));
        }
        // 装货地址
        if (companyShipAddress != null
                && !TextUtils.isEmpty(companyShipAddress)) {
            tvZhuang.setText(companyShipAddress);
        }
        // 卸货地址
        if (companyDischargeAddress != null
                && !TextUtils.isEmpty(companyDischargeAddress)) {
            tvXie.setText(companyDischargeAddress);
        }
        // 货品单价
        if (priceText != null && !TextUtils.isEmpty(priceText)) {
            etSendPrice.setText(priceText);
        }
        // 货品种类
        if (goodsType != null && !TextUtils.isEmpty(goodsType)) {
            tvGoods.setText(goodsType);
        }

        if (StringUtils.isStrNotNull(huodun)) {
            etZongdun.setText(huodun);
        }

        // 需车次数
        // if (vehicleNumber != null && !TextUtils.isEmpty(vehicleNumber)) {
        // etSendVehicleNumber.setText(vehicleNumber);
        // }

        if (StringUtils.isStrNotNull(vehicleNumberTextString)) {
            tvVehicleNumber.setText(vehicleNumberTextString);
        }

        // 途损比率
        if (shortFallType == 10) {
            if (StringUtils.isStrNotNull(mTuSunLv)) {
                tusunNumber = Double.valueOf(mTuSunLv);
                tvTusun.setText(StringUtils.sanToQianFenHao(tusunNumber));
                tvTusunType.setText(getString(R.string._tusun_lv_calculation));
            }
        } else if (shortFallType == 20) {
            tvTusun.setVisibility(View.GONE);
            ivTusun.setVisibility(View.GONE);
            etTusunDun.setVisibility(View.VISIBLE);
            tvTusunDanwei.setVisibility(View.VISIBLE);
            etTusunDun.setText(mTuSunLv);
            tvTusunType.setText(getString(R.string._tusun_dun_calculation));
        }

        // 结算模式
        if (packageSettlementType == "0") {
            tvJieSuanText.setText("线上结算");
            cbFaPiao.setChecked(true);
            cbFaPiao.setClickable(true);
            cbFaPiao.setButtonDrawable(R.drawable.cb_send_package_bg);
        } else if (packageSettlementType == "1") {
            tvJieSuanText.setText("线下结算");
            cbFaPiao.setChecked(false);
            cbFaPiao.setClickable(false);
            cbFaPiao.setButtonDrawable(R.drawable.xuanzezhihui);
        }
        // 发票
        if (needInvoice == 1) {
            cbFaPiao.setChecked(true);
        } else if (needInvoice == 0) {
            cbFaPiao.setChecked(false);
        }
        // 装车费
        if (loading != null && !TextUtils.isEmpty(loading)) {
            if (Float.parseFloat(loading) != 0.0) {
                tvNote.setText("装车费:" + loading + "元");
                loadingText = loading;
            } else {
                loading = "";
                // loadingText = "0";
            }
        }
        // 卸车费
        if (unLoading != null && !TextUtils.isEmpty(unLoading)) {
            if (Float.parseFloat(unLoading) != 0.0) {
                tvNote.setText(tvNote.getText().toString() + " 卸车费:"
                        + unLoading + "元");
                unLoadingText = unLoading;
            } else {
                unLoading = "";
                // unLoadingText = "0";
            }
        }
        // 备注信息
        if (demand != null && !TextUtils.isEmpty(demand)) {
            tvNote.setText(tvNote.getText().toString() + " " + demand);
        }
    }

    @Override
    public void onFailure(int requestId, int httpCode, Throwable error) {
        super.onFailure(requestId, httpCode, error);
        showToast(getString(R.string.net_error_toast));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_new_time:// 发货时限
                //友盟统计发货时间点击
                mUmeng.setCalculateEvents("ship_select_date");

                timPop.showPopWin(NewSendPackageActivity.this);
                break;
            case R.id.rl_zhuang:// 装货地址
                //友盟统计进入装货地址
                mUmeng.setCalculateEvents("ship_begin_address_enter");

                mFilterManager.setZhuangXieAddress(true);
                ShipAddressActivity.invoke(NewSendPackageActivity.this,
                        ZHUANG_CODE, shipAddress, zhuangProvinceId, zhuangCityId,
                        zhuangCountyId, companyShipAddress, zhuangProvinceName,
                        zhuangCityName, zhuangCountyName, isCopyPackage);
                break;
            case R.id.rl_xie:// 卸货地址
                //友盟统计进入卸货地址
                mUmeng.setCalculateEvents("ship_end_address_enter");

                mFilterManager.setZhuangXieAddress(false);
                ShipAddressActivity.invoke(NewSendPackageActivity.this, XIE_CODE,
                        dischargeAddress, xieProvinceId, xieCityId, xieCountyId,
                        companyDischargeAddress, xieProvinceName, xieCityName,
                        xieCountyName, isCopyPackage);
                break;
            case R.id.tv_ship_type:// 货品种类
                //友盟统计进入装货地址
                mUmeng.setCalculateEvents("ship_click_coal");
                // 弹出货品种类弹出框
                showGoodsPop();
                break;
            case R.id.rl_vehicle:// 选择需求车次
                // 获取货品总吨数
                String sumDun = etZongdun.getText().toString().trim();
                //点击需求车次统计
                mUmeng.setCalculateEvents("ship_click_demand_trips");

                if (isVehicleNumber(sumDun)) {
                    // 弹出需求车次框
                    showXuQiuCarPopup();
                }
                break;
            case id.rl_tusun_type://途损方式
                //友盟统计途损方式
                mUmeng.setCalculateEvents("ship_click_wayof_damage");

                isTJ = 0;
                showSettleMent();
                break;
            case R.id.tv_tusun_lv_dun:// 途损率
                //友盟统计合理途损
                mUmeng.setCalculateEvents("ship_click_reasonable_damage");

                // 显示途损弹出框
                showTuSunDialog();
                break;

            case R.id.rl_vehicle_number_pop:// 点击取消需求车次弹出框
                vehicleNumberPop.dismiss();
                break;
            case R.id.tv_pop_vehicle_shi:// 点击10
                //友盟统计需求车次
                mUmeng.setCalculateEvents("ship_select_demand_trips");

                tvVehicleNumber.setText(tvVehicleShi.getText().toString().trim());
                vehicleNumberPop.dismiss();
                break;
            case R.id.tv_pop_vehicle_bai:// 点击100
                //友盟统计需求车次
                mUmeng.setCalculateEvents("ship_select_demand_trips");

                tvVehicleNumber.setText(tvVehicleBai.getText().toString().trim());
                vehicleNumberPop.dismiss();
                break;
            case R.id.tv_pop_vehicle_duo:// 大量上车
                //友盟统计需求车次
                mUmeng.setCalculateEvents("ship_select_demand_trips");

                tvVehicleNumber.setText(tvVehicleDuo.getText().toString().trim());
                vehicleNumberPop.dismiss();
                break;

            case R.id.tv_vehicle_type:// 车型需求
                //友盟统计需求车次
                mUmeng.setCalculateEvents("ship_click_models");

                createCarTypePop();
                break;
            case R.id.tv_jiesuan_fanshi:// 结算方式
                //友盟统计
                mUmeng.setCalculateEvents("ship_click_settlement_type");
                isTJ = 1;
                showSettleMent();
                break;
            case R.id.tv_note:// 备注
                //友盟统计
                mUmeng.setCalculateEvents("ship_click_remarks");

                RemarkActivity.invoke(NewSendPackageActivity.this, NOTE_CODE,
                        loading, unLoading, demand);
                break;
            case R.id.btn_next:// 下一步
                Log.e("TAG", "-------mTuSunDun----------" + mTuSunDun);
                Log.e("TAG", "----------shortFallType------------" + shortFallType);
                if (isCompleted()) {
                    btnNext.setClickable(false);
                    // 跳转到发包预览界面
                    // 获取途损率
                    String tusunBiLv = tvTusun.getText().toString().trim();
                    mTuSunDun = etTusunDun.getText().toString().trim();
                    if (shortFallType == 20) {
                        if (!StringUtils.isStrNotNull(mTuSunDun)) {
                            showToast("请输入每车合理扣减吨数");
                            return;
                        }
                        double tuSunDun = Double.valueOf(mTuSunDun);
                        if (tuSunDun == 0) {
                            showToast("合理扣减吨数不可为0");
                            return;
                        }
                    }
                    // 获取总吨数
                    Log.e("TAG", "-------mTuSunDun----------" + mTuSunDun);
                    Log.e("TAG", "-----徒孙率----" + tusunNumber);

                    Log.e("TAG", "startTime----------" + startTime);
                    Log.e("TAG", "endTime----------" + endTime);
                    Log.e("TAG", "zhuangProvinceId----------" + zhuangProvinceId);
                    Log.e("TAG", "zhuangCityId----------" + zhuangCityId);
                    Log.e("TAG", "zhuangCountyId----------" + zhuangCountyId);
                    Log.e("TAG", "companyShipAddress----------"
                            + companyShipAddress);
                    Log.e("TAG", "shipAddress----------" + shipAddress);
                    Log.e("TAG", "xieProvinceId----------" + xieProvinceId);
                    Log.e("TAG", "xieCityId----------" + xieCityId);
                    Log.e("TAG", "xieCountyId----------" + xieCountyId);
                    Log.e("TAG", "companyDischargeAddress----------"
                            + companyDischargeAddress);
                    Log.e("TAG", "dischargeAddress----------" + dischargeAddress);
                    Log.e("TAG", "priceText----------" + priceText);
                    Log.e("TAG", "goodsType----------" + goodsType);
                    Log.e("TAG", "goodsTypeId----------" + goodsTypeId);
                    Log.e("TAG", "vehicleNumber----------" + vehicleNumber);
                    Log.e("TAG", "carIdList----------" + carIdList);
                    Log.e("TAG", "tvVehicleType.getText().toString()----------"
                            + tvVehicleType.getText().toString());
                    Log.e("TAG", "insuranceType----------" + insuranceType);
                    Log.e("TAG", "needInvoice----------" + needInvoice);
                    Log.e("TAG", "loading----------" + loading);
                    Log.e("TAG", "unLoading----------" + unLoading);
                    Log.e("TAG", "demand----------" + demand);
                    // 获取车辆ID数组
                    int[] carTypeId = new int[carIdList.size()];
                    int i = 0;
                    for (String s : carIdList) {
                        try {
                            carTypeId[i] = Integer.parseInt(s);
                        } catch (NumberFormatException e) {
                            Log.v("TAG", "Exception e---------" + e.toString());
                        }
                        Log.v("TAG", "String s : carIdList---------" + s);
                        i++;
                    }
                    Log.v("TAG", "packageSettlementType---------"
                            + packageSettlementType);
                    Log.v("TAG", "carTypeId---------" + carTypeId.toString());
                    Log.v("TAG",
                            "----------priceText------------"
                                    + Float.parseFloat(priceText));
                    Log.v("TAG",
                            "----------loadingText------------"
                                    + Float.parseFloat(loadingText));
                    Log.v("TAG",
                            "----------unLoadingText------------"
                                    + Float.parseFloat(unLoadingText));

                    //友盟统计下一步点击
                    mUmeng.setCalculateEvents("ship_click_next");

                    SendPackageEntity entity = new SendPackageEntity(startTime,
                            endTime, zhuangProvinceId,
                            zhuangCityId,
                            zhuangCountyId, companyShipAddress,
                            shipAddress, xieProvinceId,
                            xieCityId,
                            xieCountyId, companyDischargeAddress,
                            dischargeAddress, Float.parseFloat(priceText),
                            goodsType, Integer.parseInt(goodsTypeId), carTypeId,
                            tvVehicleType.getText().toString(),
                            Integer.parseInt(packageSettlementType), insuranceType,
                            needInvoice, Float.parseFloat(loadingText),
                            Float.parseFloat(unLoadingText), demand,
                            String.valueOf(tusunNumber), huodun,
                            vehicleNumberTextString, shortFallType, mTuSunDun);
                    EmployerPreViewActivity.invoke(mContext, entity);
                }
                ;
                break;

            case R.id.rl_pop_goods:// 取消货品种类弹框
                goodsPop.dismiss();
                break;

            case R.id.rl_settlement_pop:// 取消结算方式弹框
                settleMentPop.dismiss();
                break;

            case R.id.rl_online_billing:// 线上结算(按比率计算)
                if (isTJ == 0) {
                    //友盟统计途损方式
                    mUmeng.setCalculateEvents("ship_select_wayof_damage");

                    tvTusun.setVisibility(View.VISIBLE);
                    ivTusun.setVisibility(View.VISIBLE);
                    etTusunDun.setVisibility(View.GONE);
                    tvTusunDanwei.setVisibility(View.GONE);
                    settleMentPop.dismiss();
                    tvTusunType.setText(getString(R.string._tusun_lv_calculation));
                    shortFallType = 10;
                } else {
                    //友盟统计结算方式
                    mUmeng.setCalculateEvents("ship_select_settlement_type");

                    packageSettlementType = "0";
                    insuranceType = 1;
                    settleMentPop.dismiss();
                    // cbFaPiao.setChecked(true);
                    cbFaPiao.setClickable(true);
                    cbFaPiao.setButtonDrawable(R.drawable.cb_send_package_bg);
                    tvJieSuanText.setText("线上结算");
                }
                break;

            case R.id.rl_clear_the_line:// 线下结算（按吨数计算）
                if (isTJ == 0) {
                    //友盟统计途损方式
                    mUmeng.setCalculateEvents("ship_select_wayof_damage");

                    tvTusun.setVisibility(View.GONE);
                    ivTusun.setVisibility(View.GONE);
                    etTusunDun.setVisibility(View.VISIBLE);
                    tvTusunDanwei.setVisibility(View.VISIBLE);
                    settleMentPop.dismiss();
                    tvTusunType.setText(getString(R.string._tusun_dun_calculation));
                    shortFallType = 20;
                } else {
                    //友盟统计结算方式
                    mUmeng.setCalculateEvents("ship_select_settlement_type");

                    packageSettlementType = "1";
                    insuranceType = 0;
                    settleMentPop.dismiss();
                    cbFaPiao.setChecked(false);
                    cbFaPiao.setClickable(false);
                    cbFaPiao.setButtonDrawable(R.drawable.xuanzezhihui);
                    needInvoice = 0;
                    tvJieSuanText.setText("线下结算");
                }
                break;

            case R.id.tv_cartype_sure: // 车型确定
                //友盟统计结算方式
                mUmeng.setCalculateEvents("ship_click_models_sure");

                carTypePop.dismiss();
                // 得到所选车型
                String carString = "";
                for (int i = 0; i < carIdList.size(); i++) {
                    carString += carNamelist.get(i) + " ";
                }
                tvVehicleType.setText(carString);
                break;

            case R.id.tv_cartype_cancle:// 车型取消
                //友盟统计结算方式
                mUmeng.setCalculateEvents("ship_click_models_cancel");

                carTypePop.dismiss();
                break;
            case R.id.rl_pop_carType:// 车型弹窗取消
                carTypePop.dismiss();
                break;
            case id.rl_fa_piao_true://选择要发票
                if (StringUtils.isStrNotNull(packageSettlementType)) {
                    if (packageSettlementType.equals("0")) {
                        if (!isInvoice) {
                            needInvoice = 1;
                            isInvoice = true;
                            cbFaPiao.setButtonDrawable(R.drawable.new_xuanze);
                        } else {
                            needInvoice = 0;
                            isInvoice = false;
                            cbFaPiao.setButtonDrawable(R.drawable.new_weixuanze);
                        }
                    }
                } else {
                    showToast("请选择结算方式");
                }
                break;
            default:
                break;
        }
    }

    /**
     * @param zongdun
     * @return
     * @throws
     * @Description:判断货品总吨是否填写正确
     * @Title:isVehicleNumber
     * @return:boolean
     * @Create: 2016-9-14 下午2:28:49
     * @Author : chengtao
     */
    private boolean isVehicleNumber(String zongdun) {
        if (!StringUtils.isStrNotNull(zongdun)) {
            showToast("请填写货品总吨");
            return false;
        }

        double zongDun = Double.valueOf(zongdun);

        if (zongDun == 0) {
            showToast("货品总吨不可为0");
            return false;
        }

        return true;
    }

    /**
     * @Description:设置需求车次弹出框
     */
    private void settingVehicleNumberPop() {
        vehicleNumberPop = new PopupWindow(mContext);
        View vehicleView = NewSendPackageActivity.this.getLayoutInflater()
                .inflate(R.layout.employer_popupwindow_vehicle_number, null);

        vehicleNumberPop.setWidth(LayoutParams.MATCH_PARENT);
        vehicleNumberPop.setHeight(LayoutParams.WRAP_CONTENT);
        vehicleNumberPop.setBackgroundDrawable(new BitmapDrawable());
        vehicleNumberPop.setContentView(vehicleView);
        vehicleNumberPop.setOutsideTouchable(true);
        vehicleNumberPop.setFocusable(true);
        vehicleNumberPop.setTouchable(true); // 设置PopupWindow可触摸
        vehicleNumberPop
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        tvVehicleShi = (TextView) vehicleView
                .findViewById(R.id.tv_pop_vehicle_shi);
        tvVehicleBai = (TextView) vehicleView
                .findViewById(R.id.tv_pop_vehicle_bai);
        tvVehicleDuo = (TextView) vehicleView
                .findViewById(R.id.tv_pop_vehicle_duo);
        rlPopVehicleNumber = (RelativeLayout) vehicleView
                .findViewById(R.id.rl_vehicle_number_pop);
    }

    /**
     * @throws
     * @Description:需求车次框
     * @Title:showXuQiuCarPopup
     * @return:void
     * @Create: 2016-9-14 下午1:40:52
     * @Author : mengwei
     */
    private void showXuQiuCarPopup() {
        // TODO 需求车次框
        if (vehicleNumberPop == null) {
            // 设置需求车次弹出框
            settingVehicleNumberPop();
        }

        vehicleNumberPop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

        rlPopVehicleNumber.setOnClickListener(this);
        tvVehicleShi.setOnClickListener(this);
        tvVehicleBai.setOnClickListener(this);
        tvVehicleDuo.setOnClickListener(this);
    }

    /**
     * @throws
     * @Description:途损率弹窗
     * @Title:showTuSunDialog
     * @return:void
     * @Create: 2016-9-14 上午9:52:05
     * @Author : mengwei
     */
    private void showTuSunDialog() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.employer_dialog_tusun, null);
        RelativeLayout rlJian = (RelativeLayout) view
                .findViewById(R.id.rl_dialog_jian);
        RelativeLayout rlJia = (RelativeLayout) view
                .findViewById(R.id.rl_dialog_jia);
        final TextView tvTuSunLv = (TextView) view
                .findViewById(R.id.tv_dialog_tusunlv);
        final EditText etTuSunNumber = (EditText) view
                .findViewById(R.id.et_dialog_number);
        Button btnSure = (Button) view.findViewById(R.id.bt_dialog_sure);
        AlertDialog.Builder builder = new Builder(mContext);
        final AlertDialog dialog = builder.create();
        dialog.setView(view);
        dialog.setCancelable(true);
        etTuSunNumber.setText(((int) (tusunNumber * 1000)) + "");
        dialog.show();
        // 增加途损率
        rlJia.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "--------jia途损率---------" + tusunNumber);
                String mJiaTxt = etTuSunNumber.getText()
                        .toString().trim();
                if (!StringUtils.isStrNotNull(mJiaTxt)) {
                    T.showShort(mContext, "请先输入数字");
                    return;
                }
                // 获取输入框的值
                int mTuSunJia = Integer.valueOf(mJiaTxt);
                if (mTuSunJia >= 100) {
                    showToast("途损率不能超过100‰");
                    return;
                }

                //友盟统计途损jia
                mUmeng.setCalculateEvents("ship_click_damage_plus");

                mTuSunJia = mTuSunJia + 1;
                tusunNumber = mTuSunJia / 1000;
                etTuSunNumber.setText(mTuSunJia + "");
            }
        });

        // 减少途损率
        rlJian.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String mJianTxt = etTuSunNumber.getText()
                        .toString().trim();
                if (!StringUtils.isStrNotNull(mJianTxt)) {
                    T.showShort(mContext, "请先输入数字");
                    return;
                }
                // 获取输入框的值
                int mTuSunJian = Integer.valueOf(mJianTxt);
                if (mTuSunJian == 0) {
                    showToast("途损率不能为0");
                    return;
                }

                //友盟统计途损jian
                mUmeng.setCalculateEvents("ship_click_damage_less");

                mTuSunJian = mTuSunJian - 1;
                tusunNumber = mTuSunJian / 1000;
                etTuSunNumber.setText(mTuSunJian + "");
            }
        });
        btnSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取输入框的值
                String mTuSunLv = etTuSunNumber.getText()
                        .toString().trim();
                if (StringUtils.isStrNotNull(mTuSunLv)) {
                    int mTuSunNum = Integer.valueOf(mTuSunLv);
                    if (mTuSunNum == 0) {
                        showToast("途损率不能为0");
                        return;
                    } else if (mTuSunNum >= 100) {
                        showToast("途损率不能超过100‰");
                        return;
                    }

                    //友盟统计途损jian
                    mUmeng.setCalculateEvents("ship_click_damage_sure");


                    tusunNumber = (mTuSunNum * 1.000) / 1000;
                    tvTusun.setText(mTuSunNum + "‰");
                    dialog.dismiss();
                } else {
                    showToast("途损率不可为空");
                }
            }
        });
    }

    /**
     * @throws
     * @Description:设置结算方式弹框
     * @Title:setSettleMentPop
     * @return:void
     * @Create: 2016-6-23 下午1:33:56
     * @Author : chengtao
     */
    @SuppressWarnings("deprecation")
    private void setSettleMentPop() {
        settleMentPop = new PopupWindow(mContext);
        View settleMentView = NewSendPackageActivity.this.getLayoutInflater()
                .inflate(R.layout.employer_popupwindow_settlement, null);

        settleMentPop.setWidth(LayoutParams.MATCH_PARENT);
        settleMentPop.setHeight(LayoutParams.WRAP_CONTENT);
        settleMentPop.setBackgroundDrawable(new BitmapDrawable());
        settleMentPop.setContentView(settleMentView);
        settleMentPop.setOutsideTouchable(true);
        settleMentPop.setFocusable(true);
        settleMentPop.setTouchable(true); // 设置PopupWindow可触摸
        settleMentPop
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        rlSettleMentLayout = (RelativeLayout) settleMentView
                .findViewById(R.id.rl_settlement_pop);
        rlSettleMentTop = (RelativeLayout) settleMentView
                .findViewById(R.id.rl_online_billing);
        rlSettleMentBottom = (RelativeLayout) settleMentView
                .findViewById(R.id.rl_clear_the_line);
        tvTJtop1 = (TextView) settleMentView.findViewById(id.tv_tj_top1);
        tvTJtop2 = (TextView) settleMentView.findViewById(id.tv_tj_top2);
        tvTJbottom = (TextView) settleMentView.findViewById(id.tv_tj_bottom);
    }

    /**
     * @throws
     * @Description:显示结算方式弹出框
     * @Title:showSettleMent
     * @return:void
     * @Create: 2016-6-23 下午1:30:56
     * @Author : chengtao
     */
    private void showSettleMent() {
        if (settleMentPop == null) {
            // 设置结算方式弹框
            setSettleMentPop();
        }
        settleMentPop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
        if (isTJ == 0) {//途损方式
            tvTJtop1.setText(getString(R.string._tusun_lv_calculation));
            tvTJtop2.setVisibility(View.GONE);
            tvTJbottom.setText(getString(R.string._tusun_dun_calculation));
        } else if (isTJ == 1) {//结算方式
            tvTJtop1.setText(getString(R.string.cb_online));
            tvTJtop2.setVisibility(View.VISIBLE);
            tvTJtop2.setText(getString(R.string.tv_insurance));
            tvTJbottom.setText(getString(R.string.cb_outline));
        }

        rlSettleMentLayout.setOnClickListener(this);
        rlSettleMentTop.setOnClickListener(this);
        rlSettleMentBottom.setOnClickListener(this);
    }

    /**
     * @throws
     * @Description:设置货品种类弹出框
     * @Title:setGoodsPop
     * @return:void
     * @Create: 2016-6-23 上午11:01:29
     * @Author : mengwei
     */
    @SuppressWarnings("deprecation")
    private void setGoodsPop() {
        goodsPop = new PopupWindow(mContext);
        View goodsView = NewSendPackageActivity.this.getLayoutInflater()
                .inflate(R.layout.employer_popupwindow_goods_type, null);

        goodsPop.setWidth(LayoutParams.MATCH_PARENT);
        goodsPop.setHeight(LayoutParams.WRAP_CONTENT);
        goodsPop.setBackgroundDrawable(new BitmapDrawable());
        goodsPop.setContentView(goodsView);
        goodsPop.setOutsideTouchable(true);
        goodsPop.setFocusable(true);
        goodsPop.setTouchable(true); // 设置PopupWindow可触摸
        goodsPop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        lvPopGoodsView = (ListView) goodsView.findViewById(R.id.lv_pop_goods);
        rlPopGoodsLayout = (RelativeLayout) goodsView
                .findViewById(R.id.rl_pop_goods);

        // 适配数据
        goodsTypeAdapter = new GoodsTypeAdapter(goodsTypeList, mContext);
        lvPopGoodsView.setAdapter(goodsTypeAdapter);
    }

    /**
     * @throws
     * @Description:弹出货品种类弹出框
     * @Title:showGoodsPop
     * @return:void
     * @Create: 2016-6-23 上午10:56:36
     * @Author : mengwei
     */
    private void showGoodsPop() {
        if (goodsPop == null) {
            // 设置货品种类弹出框
            setGoodsPop();
        }

        goodsPop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

        rlPopGoodsLayout.setOnClickListener(this);

        lvPopGoodsView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                PackagePickersInfo goodsType = goodsTypeList.get(position);

                goodsTypeId = String.valueOf(goodsType.Id);
                goodsTypeText = goodsType.CategoryName;

                Log.e("TAG", "-----货品种类名称-----" + goodsTypeText);
                Log.e("TAG", "-----货品种类ID-----" + goodsTypeId);

                showToast(goodsTypeText);

                // 给界面的控件赋值
                tvGoods.setText(goodsTypeText);

                //友盟统计
                mUmeng.setCalculateEvents("ship_select_coal");

                // 关闭弹框
                goodsPop.dismiss();
            }
        });
    }

    /**
     * @throws
     * @Description:设置车型弹窗
     * @Title:setCarTypePop
     * @return:void
     * @Create: 2016-6-24 上午10:13:22
     * @Author : mengwei
     */
    @SuppressWarnings("deprecation")
    private void setCarTypePop() {
        carTypePop = new PopupWindow(mContext);
        View carTypeView = NewSendPackageActivity.this.getLayoutInflater()
                .inflate(R.layout.car_type_poupwindow, null);

        carTypePop.setWidth(LayoutParams.MATCH_PARENT);
        carTypePop.setHeight(LayoutParams.WRAP_CONTENT);
        carTypePop.setBackgroundDrawable(new BitmapDrawable());
        carTypePop.setContentView(carTypeView);
        carTypePop.setOutsideTouchable(true);
        carTypePop.setFocusable(true);
        carTypePop.setTouchable(true); // 设置PopupWindow可触摸
        carTypePop
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        tvCancle = (TextView) carTypeView.findViewById(R.id.tv_cartype_cancle);
        tvSure = (TextView) carTypeView.findViewById(R.id.tv_cartype_sure);
        lvCarTypeView = (ListView) carTypeView.findViewById(R.id.lv_car_type);
        rlPopCarType = (RelativeLayout) carTypeView
                .findViewById(R.id.rl_pop_carType);

        carTypeAdapter = new CarTypeAdapter(carTypelist, mContext, carIdList);
        lvCarTypeView.setAdapter(carTypeAdapter);
        lvCarTypeView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.v("TAG", "onItemClick");
                if (carTypeAdapter.getCarTypeItem().get(
                        String.valueOf(position))) {
                    carIdList.remove(carTypelist.get(position).id);
                    carNamelist.remove(carTypelist.get(position).enumName);
                    Log.v("TAG", "carIdList-----" + carIdList.toString());
                    Log.v("TAG", "carNamelist-----" + carNamelist.toString());
                } else {
                    carIdList.add(carTypelist.get(position).id);
                    carNamelist.add(carTypelist.get(position).enumName);
                    Log.v("TAG", "carIdList-----" + carIdList.toString());
                    Log.v("TAG", "carNamelist-----" + carNamelist.toString());
                }
                carTypeAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * @throws
     * @Description:创建车型弹窗
     * @Title:createCarTypePop
     * @return:void
     * @Create: 2016年6月22日 下午4:41:45
     * @Author : mengwei
     */
    private void createCarTypePop() {
        if (carTypePop == null) {
            // 设置车型弹窗
            setCarTypePop();
        }
        carTypePop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
        rlPopCarType.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        tvCancle.setOnClickListener(this);
    }

    /**
     * @return
     * @throws
     * @Description:判断信息是否填写完整
     * @Title:isCompleted
     * @Create: 2016年6月22日 下午2:43:59
     * @Author : chengtao
     */
    private boolean isCompleted() {
        String time = tvTime.getText().toString().trim();
        String zhuangText = tvZhuang.getText().toString().trim();
        String xieText = tvXie.getText().toString().trim();
        priceText = etSendPrice.getText().toString().trim();
        goodsType = tvGoods.getText().toString().trim();
        // vehicleNumber = etSendVehicleNumber.getText().toString().trim();
        SettlementType = packageSettlementType;
        huodun = etZongdun.getText().toString().trim();
        vehicleNumberTextString = tvVehicleNumber.getText().toString().trim();
        mTuSunLv = tvTusun.getText().toString().trim();
        mTuSunDun = etTusunDun.getText().toString().trim();
        L.e("-------mTuSunDun----------" + mTuSunDun);
        L.e("----------shortFallType------------" + shortFallType);

        // 判断时间
        if (time == null || TextUtils.isEmpty(time)) {
            showToast("请选择发货时间");
            return false;
        }

        // 判断结束时间是否小于开始时间
        if (endTime < startTime) {
            showToast("结束时间大于开始时间");
            return false;
        }

        // 判断装卸地址
        if ((zhuangProvinceId == 0)
                || (zhuangCityId == 0)
                || (zhuangCountyId == 0)) {
            showToast("请填写正确装货地址");
            return false;
        }
        if (zhuangText == null || TextUtils.isEmpty(zhuangText)) {
            showToast("请填写装货地址");
            return false;
        }
        // 卸货地址
        if ((xieProvinceId == 0)
                || (xieCityId == 0)
                || (xieCountyId == 0)) {
            showToast("请填写正确卸货地址");
            return false;
        }
        if (xieText == null || TextUtils.isEmpty(xieText)) {
            showToast("请填写卸货地址");
            return false;
        }
        // 判断单价
        if (priceText == null || TextUtils.isEmpty(priceText)) {
            showToast("请填写货品单价");
            return false;
        }
        if (StringUtils.isStrNotNull(priceText)) {
            if (Float.parseFloat(priceText) <= 0.0) {
                showToast("货品单价要大于0");
                return false;
            }
        }
        // 判断货品种类
        if (goodsTypeId == null || TextUtils.isEmpty(goodsTypeId)) {
            showToast("请选择货品种类");
            return false;
        }
        if (goodsType == null || TextUtils.isEmpty(goodsType)) {
            showToast("请选择货品种类");
            return false;
        }

        if (!StringUtils.isStrNotNull(huodun)) {
            showToast("请填写货品总吨");
            return false;
        }

        double huoDun = Double.valueOf(huodun);
        if (huoDun == 0) {
            showToast("货品总吨不能为0");
            return false;
        }

        // 判断需求车次
        if (!StringUtils.isStrNotNull(vehicleNumberTextString)) {
            showToast("请选择需求车次");
            return false;
        }

        if (shortFallType == 20) {
            if (!StringUtils.isStrNotNull(mTuSunDun)) {
                showToast("请输入每车合理扣减吨数");
                return false;
            }
            double tuSunDun = Double.valueOf(mTuSunDun);
            if (tuSunDun == 0) {
                showToast("合理扣减吨数不可为0");
                return false;
            }
        }

        // 结算方式
        if (SettlementType == null || TextUtils.isEmpty(SettlementType)) {
            showToast("请选择结算方式");
            return false;
        }

        if (!StringUtils.isStrNotNull(packageSettlementType)){
            showToast("请选择结算方式");
            return false;
        }
        // 装车费
        if (StringUtils.isStrNotNull(loading)) {
            if (Float.parseFloat(loading) <= 0.0) {
                showToast("装车费要大于0");
                return false;
            }
        }
        if (loading == null || TextUtils.isEmpty(loading)) {
            loadingText = "0";
        }
        // 卸车费
        if (StringUtils.isStrNotNull(unLoading)) {
            if (Float.parseFloat(unLoading) <= 0.0) {
                showToast("卸车费要大于0");
                return false;
            }
        }
        if (unLoading == null || TextUtils.isEmpty(unLoading)) {
            unLoadingText = "0";
        }
        // 备注信息
        if (demand == null || TextUtils.isEmpty(demand)) {
            demand = "";
        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        AddressBeginAnd beginAnd = (AddressBeginAnd) getIntent().getSerializableExtra("ADDRESS");
        int type = beginAnd.getType();
        if (type == 1) {
            shipAddress = beginAnd.getDetailAddress();
            zhuangProvinceId = beginAnd.getProvinceId();
            zhuangCityId = beginAnd.getCityId();
            zhuangCountyId = beginAnd.getCountyId();
            zhuangProvinceName = beginAnd.getProvinceName();
            zhuangCityName = beginAnd.getCityName();
            zhuangCountyName = beginAnd.getCountyName();
            companyShipAddress = beginAnd.getCompanyName();
        } else if (type == 2) {
            dischargeAddress = beginAnd.getDetailAddress();
            xieProvinceId = beginAnd.getProvinceId();
            xieCityId = beginAnd.getCityId();
            xieCountyId = beginAnd.getCountyId();
            xieProvinceName = beginAnd.getProvinceName();
            xieCityName = beginAnd.getCityName();
            xieCountyName = beginAnd.getCountyName();
            companyDischargeAddress = beginAnd.getCompanyName();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnNext.setClickable(true);

        // 判断返回的数据
        if (companyShipAddress != null
                && !TextUtils.isEmpty(companyShipAddress)) {
            tvZhuang.setText(companyShipAddress);
        }

        if (companyDischargeAddress != null
                && !TextUtils.isEmpty(companyDischargeAddress)) {
            tvXie.setText(companyDischargeAddress);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("TAG", "----------------onActivityResult");
        if (data == null) {
            return;
        } else {
            Log.e("TAG", "----------------data" + data);
            switch (requestCode) {
                //			case ZHUANG_CODE:// 获取装货地址
                //				Log.e("TAG", "----------------data" + data);
                //				shipAddress = data.getStringExtra("detailAddress");
                //				zhuangProvinceId = data.getStringExtra("provinceId");
                //				zhuangCityId = data.getStringExtra("cityId");
                //				zhuangCountyId = data.getStringExtra("countyId");
                //				zhuangProvinceName = data.getStringExtra("provinceName");
                //				zhuangCityName = data.getStringExtra("cityName");
                //				zhuangCountyName = data.getStringExtra("countyName");
                //				companyShipAddress = data.getStringExtra("companyName");
                //				if (companyShipAddress != null
                //						&& !TextUtils.isEmpty(companyDischargeAddress)) {
                //					tvZhuang.setText(companyShipAddress);
                //				}
                //				break;
                //			case XIE_CODE:// 获取卸货地址
                //				Log.e("TAG", "----------------data" + data);
                //				dischargeAddress = data.getStringExtra("detailAddress");
                //				xieProvinceId = data.getStringExtra("provinceId");
                //				xieCityId = data.getStringExtra("cityId");
                //				xieCountyId = data.getStringExtra("countyId");
                //				xieProvinceName = data.getStringExtra("provinceName");
                //				xieCityName = data.getStringExtra("cityName");
                //				xieCountyName = data.getStringExtra("countyName");
                //				companyDischargeAddress = data.getStringExtra("companyName");
                //				if (companyShipAddress != null
                //						&& !TextUtils.isEmpty(companyDischargeAddress)) {
                //					tvXie.setText(companyShipAddress);
                //				}
                //				break;
                case NOTE_CODE:// 获取备注信息
                    Log.e("TAG", "----------------data" + data);
                    loading = data.getStringExtra("loading");
                    unLoading = data.getStringExtra("unLoading");
                    demand = data.getStringExtra("demand");
                    Log.e("TAG", "----------------loading" + loading);
                    Log.e("TAG", "----------------unLoading" + unLoading);
                    Log.e("TAG", "----------------demand" + demand);
                /*
                 * if (loading != null && !TextUtils.isEmpty(loading)) {
				 * tvNote.setText("装车费:"+loading+"元"); } if (unLoading != null
				 * && !TextUtils.isEmpty(unLoading)) {
				 * tvNote.setText(" 装车费:"+tvNote.getText().toString() +
				 * unLoading + "元"); } if (demand != null &&
				 * !TextUtils.isEmpty(demand)) {
				 * tvNote.setText(" "+tvNote.getText().toString() + demand); }
				 */
                    // 备注信息
                    tvNote.setText("");
                    if (StringUtils.isStrNotNull(loading)) {
                        loadingText = loading;
                        tvNote.setText("装车费:" + loading + "元");
                    }
                    if (StringUtils.isStrNotNull(unLoading)) {
                        unLoadingText = unLoading;
                        tvNote.setText(tvNote.getText().toString() + " 卸车费:"
                                + unLoading + "元");
                    }
                    if (StringUtils.isStrNotNull(demand)) {
                        tvNote.setText(tvNote.getText().toString() + " " + demand);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onTime(String startTime, String endTime) {
        tvTime.setText(startTime + " ~ " + endTime);
        this.startTime = Long.parseLong(StringUtils
                .StringDateToDateLong(startTime));
        Log.v("TAG", this.startTime + "");
        this.endTime = Long
                .parseLong(StringUtils.StringDateToDateLong(endTime));
        Log.v("TAG", this.endTime + "");
    }

    /**
     * @param activity
     * @throws
     * @Description:普通发包
     * @Title:invoke
     * @return:void
     * @Create: 2016年6月22日 下午2:01:38
     * @Author : chengtao
     */
    public static void invoke(Activity activity, String packageId) {
        Intent intent = new Intent(activity, NewSendPackageActivity.class);
        intent.putExtra("packageId", packageId);
        activity.startActivity(intent);
    }

    @Override
    protected void initData() {
        // 货品种类的请求
        getGoodsType();
        // 车型的请求
        getCarType();
        if (isCopyPackage) {
            // 获取复制发包信息
            getPackageDetail(packageId);
            getCarTypeList();
        }

    }

    /**
     * 获取省的请求
     */
    private void getProvinceRequest() {
        mGetProvinceRequest = new GetProvinceRequest(mContext);
        mGetProvinceRequest.setRequestId(GET_PROVINCE);
        httpPost(mGetProvinceRequest);
    }

    /**
     * 获取市的请求
     *
     * @param provinceId 选中的省份Id
     */
    private void getCityRequest(int provinceId) {
        i++;
        mCityList.clear();
        if (i < 6) {
            mGetCityRequest = new GetCityRequest(mContext, provinceId);
            mGetCityRequest.setRequestId(GET_CITY);
            httpPost(mGetCityRequest);
        }
    }

    /**
     * @throws
     * @Description:获取复制包的车型
     * @Title:getCarTypeList
     * @return:void
     * @Create: 2016年6月29日 下午2:05:18
     * @Author : chengtao
     */
    private void getCarTypeList() {
        getSenumListRequest = new GetSenumListRequest(mContext, packageId);
        getSenumListRequest.setRequestId(GET_SENUM_LIST_REQUES);
        httpPost(getSenumListRequest);
    }

    /**
     * @param packageId
     * @throws
     * @Description:复制发包的内容
     * @Title:getPackageDetail
     * @return:void
     * @Create: 2016-6-23 上午10:30:51
     * @Author : mengwei
     */
    private void getPackageDetail(String packageId) {
        copyPackageRequest = new CopyPackageRequest(mContext, packageId);
        copyPackageRequest.setRequestId(COPY_PACKAGE_REQUEST);
        httpPost(copyPackageRequest);
    }

    /**
     * @throws
     * @Description:车型的请求
     * @Title:getCarType
     * @return:void
     * @Create: 2016-6-24 上午10:46:56
     * @Author : chengtao
     */
    private void getCarType() {
        carTypeRequest = new CarTypeRequest(mContext);
        carTypeRequest.setRequestId(GET_CAR_TYPE);
        httpGet(carTypeRequest);
    }

    /**
     * @throws
     * @Description:货品种类的请求
     * @Title:getGoodsType
     * @return:void
     * @Create: 2016-6-23 上午11:10:33
     * @Author : mengwei
     */
    private void getGoodsType() {
        getPakagePickersRequest = new GetPakagePickersRequest(mContext);
        getPakagePickersRequest.setRequestId(GET_GOODS_TYPE);
        httpGet(getPakagePickersRequest);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.e("TAG", "onCheckedChanged");

        //友盟统计
        mUmeng.setCalculateEvents("ship_click_invoice");

        if (isChecked) {
            needInvoice = 1;
            Log.e("TAG", "1");
        } else {
            needInvoice = 0;
            Log.e("TAG", "0");
        }
    }
}
