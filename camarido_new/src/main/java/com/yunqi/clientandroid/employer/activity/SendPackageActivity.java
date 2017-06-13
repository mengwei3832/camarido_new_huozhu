package com.yunqi.clientandroid.employer.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.entity.CopyPackageInfo;
import com.yunqi.clientandroid.employer.entity.GetContractsInfo;
import com.yunqi.clientandroid.employer.entity.GetLastPackageInfo;
import com.yunqi.clientandroid.employer.entity.HeTongShenHe;
import com.yunqi.clientandroid.employer.entity.IsXuShenHe;
import com.yunqi.clientandroid.employer.entity.PackagePickersInfo;
import com.yunqi.clientandroid.employer.entity.PinZhongId;
import com.yunqi.clientandroid.employer.request.CopyPackageRequest;
import com.yunqi.clientandroid.employer.request.GetContractsRequest;
import com.yunqi.clientandroid.employer.request.GetLastPackageRequest;
import com.yunqi.clientandroid.employer.request.GetPakagePickersRequest;
import com.yunqi.clientandroid.employer.request.GetPlatfromServiceOptionsRequest;
import com.yunqi.clientandroid.employer.request.IsEixtsPackageRequest;
import com.yunqi.clientandroid.employer.request.SendNeedAuditPackageRequest;
import com.yunqi.clientandroid.employer.request.SendNotNeedAuditPackageRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.PreManager;
import com.yunqi.clientandroid.utils.StringUtils;
import com.yunqi.clientandroid.utils.TimeDefaultUtils;
import com.yunqi.clientandroid.view.wheel.ChangeTimePopWin;
import com.yunqi.clientandroid.view.wheel.ChangeTimePopWin.OnChangeTimePopWinListener;
import com.yunqi.clientandroid.view.wheel.adapters.AbstractWheelTextAdapter;
import com.yunqi.clientandroid.view.wheel.views.OnWheelChangedListener;
import com.yunqi.clientandroid.view.wheel.views.OnWheelScrollListener;
import com.yunqi.clientandroid.view.wheel.views.WheelView;

/**
 * 
 * @Description:发包界面
 * @ClassName: SendPackageActivity
 * @author: chengtao
 * @date: 2016年5月11日 下午6:01:23
 * 
 */
@SuppressLint("SimpleDateFormat")
@SuppressWarnings("deprecation")
public class SendPackageActivity extends BaseActivity implements
		OnClickListener, OnCheckedChangeListener {
	// 包编号
	private String packageId;
	// 弹窗
	private PopupWindow mPopupWindow;

	private LinearLayout mainContainer;
	private ProgressBar progressBar;
	//
	private PreManager preManager;
	private SharedPreferences mShared;
	private boolean isFirstSendPackage = true;// 判断是否第一次登陆
	// 要保存在本地的字符串
	private final static String IS_FIRST_SEND_PACKAGE = "IS_FIRST_SEND_PACKAGE";
	private final static String EMPLOYER_PHONE = "EMPLOYER_PHONE";

	// 合同编号
	private RelativeLayout rlHeTongId;
	private TextView tvHeTongId;
	private ImageView ivHeTongId;
	private String heTongId;
	private int isNeedAudit = 1;
	private String heTongCode;
	private String tenantId = "";
	// 发包类型
	private RelativeLayout rlPackageType;
	private TextView tvPackageType;
	private ImageView ivPackageType;
	private String packageType;
	// 结算模式
	private RelativeLayout rlJieSuanModle;
	private TextView tvJieSuanModle;
	private ImageView ivJieSuanModle;
	private String jieSuanModle;
	// 索要发票
	private RelativeLayout rlFaPiao;
	private TextView tvFaPiao;
	private ImageView ivFaPiao;
	private String faPiao = "0";
	// 运费
	private EditText etYunFei;
	private String yunFei;
	// 车数
	private EditText etCarNum;
	private String carNum;
	// 货品单价
	private EditText etGoodsAveprice;
	private String goodsAveprice;
	// 货品类型
	private EditText etGoodsType;
	private String goodsType;
	private String goodTypeId;
	// 吨数
	private EditText etGoodsWeight;
	private String goodsWeight;
	// 收发货文字提示
	private TextView tvPlace;
	private int tvPlacePaddingBottom;
	// 发货地点
	private TextView tvStartPlaceHinit;
	private LinearLayout llStartPlace;
	private TextView tvStartPlace;
	private ImageView ivStartPlace;
	private String startPlace;
	private int startAddressId = 0;
	// 收货地点
	private LinearLayout llPlace;
	private TextView tvEndPlaceHinit;
	private LinearLayout llEndPlace;
	private TextView tvEndPlace;
	private ImageView ivEndPlace;
	private String endPlace;
	private int endAddressId = 0;
	// 选择时间提示
	private TextView tvTime;
	// 开始时间
	private LinearLayout llStartTime;
	private TextView tvStartTime;
	private ImageView ivStartTime;
	private String startTime;
	// 截止时间
	private LinearLayout llEndTime;
	private TextView tvEndTime;
	private ImageView ivEndTime;
	private String endTime;
	// 发包方电话
	private EditText etEmployerPhone;
	private String employerPhone;
	// 发包方电话图标
	private ImageView ivEmployerPhone;
	// 矿发电话
	private EditText etKuangFaPhone;
	private String kuangFaPhone;
	// 矿发电话图标
	private ImageView ivKuangFaPhone;
	// 签收电话
	private EditText etQianShouPhone;
	private String qianShouPhone;
	// 签收电话图标
	private ImageView ivQianShouPhone;
	// 备注
	private EditText etNote;
	private String note;
	// 下一步按钮
	private Button btnNext;
	// 平台服务
	private RelativeLayout rlPingTaiService;
	private TextView tvPingTaiService;
	private LinearLayout llAllService;
	private CheckBox cbSendInsurance;
	private boolean isSendInsurance = false;
	private RelativeLayout rlSendInsurance;

	// 存储弹出框数据的集合
	private ArrayList<IsXuShenHe> auditList = new ArrayList<IsXuShenHe>();
	private ArrayList<HeTongShenHe> shenHeList = new ArrayList<HeTongShenHe>();
	private ArrayList<GetContractsInfo> hetongList = new ArrayList<GetContractsInfo>();
	private ArrayList<PackagePickersInfo> pinzhongList = new ArrayList<PackagePickersInfo>();
	private ArrayList<PinZhongId> pinZhongIdList = new ArrayList<PinZhongId>();
	private ArrayList<String> popupList = new ArrayList<String>();
	private String chooseText;
	private View parentView;
	private int itemId = 0;
	private Date startTimeText, endTimeText;

	private List<Map<String, String>> heTongIdList = new ArrayList<Map<String, String>>();// 存放合同编号类型
	private List<Map<String, String>> packageTypeList = new ArrayList<Map<String, String>>();// 存放发包类型
	private List<Map<String, String>> jieSuanList = new ArrayList<Map<String, String>>();// 存放结算类型
	private List<Map<String, String>> goodsTypeList = new ArrayList<Map<String, String>>();// 存放货物种类
	private List<Map<String, String>> faPiaoList = new ArrayList<Map<String, String>>();// 存放发票
	private String HE_TONG_CODE = "heTongCode";
	private String HE_TONG_ID = "heTongId";
	private String IS_NEED_ADUIT = "isNeedAduit";
	private String FA_BAO_FANG_ID = "faBaoFangId";
	private String PACKAGE_TYPE = "packageType";
	private String JIE_SUAN = "jieSuan";
	private String GOOD_TYPE = "goodType";
	private String GOOD_TYPE_ID = "goodTypeId";
	private String FA_PIAO = "faPiao";
	private String SPECIAL = "SPECIAL";

	private int maxsize = 24;// 设置字体的大小
	private int minsize = 12;// 设置字体的大小
	private String selectedHeTongCode;// 选中的合同id
	private String selectedPackageType;// 选中的发包类型
	private String selectedJieSuan;// 选中的结算模式
	private String selectedGoodsType;// 选中的货物种类
	private String selectedFaPiao;// 选中的发票

	// popWindow类型
	private final static int HE_TONG = 1;
	private final static int TYPE = 2;
	private final static int MODLE = 3;
	private final static int GOODS = 4;
	private final static int FAPIAO = 5;
	// 电话类型
	private final static int EMPLOYER = 1;
	private final static int KUANG_FA = 2;
	private final static int QIAN_SHOU = 3;
	// poupwindow中只有一个WheelView的Adapter
	private OneWheelListAdapter adapter = null;

	// 各种请求
	private CopyPackageRequest copyPackageRequest;
	private IsEixtsPackageRequest isEixtsPackageRequest;
	private GetLastPackageRequest getLastPackageRequest;
	private GetContractsRequest getContractsRequest;
	private SendNotNeedAuditPackageRequest sendnotNeedAuditPackageRequest;
	private SendNeedAuditPackageRequest sendNeedAuditPackageRequest;
	private GetPlatfromServiceOptionsRequest getPlatfromServiceOptionsRequest;
	private GetPakagePickersRequest getPakagePickersRequest;

	// 各种请求id
	private final static int GET_CONTRACTS = 1;
	private final static int IS_EIXTS_PACKAGE_REQUEST = 2;
	private final static int GET_LAST_PACKAGE_REQUEST = 3;
	private final static int COPY_PACKAGE_REQUEST = 4;
	private final static int SEND_NEED_AUDIT_PACKAGE_REQUEST = 5;
	private final static int SEND_NOT_NEED_AUTID_PACKAGE_REQUEST = 6;
	private final static int GET_PLATFORM_SERVICE_OPTION_REQUEST = 7;
	private final static int GET_PAKAGE_PICKER_REQUEST = 8;
	// 添加地址的requestCode
	private final static int START_PLCE = 5;
	private final static int EDN_PLACE = 6;

	// 是否点击下一步
	private boolean isNext = false;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_send_package;
	}

	@Override
	protected void initView() {
		parentView = SendPackageActivity.this.getLayoutInflater().inflate(
				R.layout.employer_activity_send_package, null);

		//
		mainContainer = obtainView(R.id.send_package_container);
		progressBar = obtainView(R.id.send_package_progress_bar);
		//
		rlHeTongId = obtainView(R.id.rl_hetong_id);
		tvHeTongId = obtainView(R.id.tv_hetong_id);
		rlPackageType = obtainView(R.id.rl_type);
		tvPackageType = obtainView(R.id.tv_package_type);
		rlJieSuanModle = obtainView(R.id.rl_modle);
		tvJieSuanModle = obtainView(R.id.tv_modle);
		// 索要发票
		rlFaPiao = obtainView(R.id.rl_fa_piao);
		tvFaPiao = obtainView(R.id.tv_fa_piao);

		etYunFei = obtainView(R.id.et_yun_fei);
		etCarNum = obtainView(R.id.et_car_num);
		etGoodsAveprice = obtainView(R.id.et_huo_pin_dan_jia);
		etGoodsType = obtainView(R.id.et_huo_pin_type);
		etGoodsWeight = obtainView(R.id.et_huo_pin_weight);
		llPlace = obtainView(R.id.ll_address);
		tvStartPlace = obtainView(R.id.tv_start_address);
		llStartPlace = obtainView(R.id.ll_start_address);
		tvEndPlace = obtainView(R.id.tv_end_address);
		llEndPlace = obtainView(R.id.ll_end_address);
		tvStartTime = obtainView(R.id.tv_start_time);
		llStartTime = obtainView(R.id.ll_start_time);
		tvEndTime = obtainView(R.id.tv_end_time);
		llEndTime = obtainView(R.id.ll_end_time);
		etEmployerPhone = obtainView(R.id.et_employer_phone);
		ivEmployerPhone = obtainView(R.id.iv_employer);
		etKuangFaPhone = obtainView(R.id.et_kuang_fa_phone);
		ivKuangFaPhone = obtainView(R.id.iv_kuagn_fa);
		etQianShouPhone = obtainView(R.id.et_qian_shou_phone);
		ivQianShouPhone = obtainView(R.id.iv_qian_shou);
		etNote = obtainView(R.id.et_note);
		btnNext = obtainView(R.id.btn_next);

		// 需要隐藏和显示的控件
		ivHeTongId = obtainView(R.id.iv_he_tong);
		ivPackageType = obtainView(R.id.iv_package_type);
		ivJieSuanModle = obtainView(R.id.iv_jie_suan);
		ivFaPiao = obtainView(R.id.iv_fa_piao);
		ivStartPlace = obtainView(R.id.iv_start_address);
		ivEndPlace = obtainView(R.id.iv_end_address);
		ivStartTime = obtainView(R.id.iv_start_time);
		ivEndTime = obtainView(R.id.iv_end_time);
		tvPlace = obtainView(R.id.tv_address);
		tvTime = obtainView(R.id.tv_time);
		tvStartPlaceHinit = obtainView(R.id.tv_start_address_hinit);
		tvEndPlaceHinit = obtainView(R.id.tv_end_address_hinit);

		// 平台服务
		rlPingTaiService = obtainView(R.id.rl_service);
		tvPingTaiService = obtainView(R.id.ctv_service);
		llAllService = obtainView(R.id.ll_all_service);
		cbSendInsurance = obtainView(R.id.cb_send_insurance);
		rlSendInsurance = obtainView(R.id.rl_send_insurance);
		// 包类型
		Map<String, String> packageMap1 = new HashMap<String, String>();
		packageMap1.put(PACKAGE_TYPE + SPECIAL, "一口价");
		packageTypeList.add(packageMap1);
		Map<String, String> packageMap2 = new HashMap<String, String>();
		packageMap2.put(PACKAGE_TYPE + SPECIAL, "竞价");
		packageTypeList.add(packageMap2);
		Map<String, String> packageMap3 = new HashMap<String, String>();
		packageMap3.put(PACKAGE_TYPE + SPECIAL, "定向指派");
		packageTypeList.add(packageMap3);

		Map<String, String> faPiaoMap1 = new HashMap<String, String>();
		faPiaoMap1.put(FA_PIAO + SPECIAL, "是");
		faPiaoList.add(faPiaoMap1);
		Map<String, String> faPiaoMap2 = new HashMap<String, String>();
		faPiaoMap2.put(FA_PIAO + SPECIAL, "否");
		faPiaoList.add(faPiaoMap2);

		Log.v("TAG----------packageTypeList", packageTypeList.toString());

		progressBar.setVisibility(View.VISIBLE);
		mainContainer.setVisibility(View.GONE);
		// 固定结算模式
		tvJieSuanModle.setText("实时结算");
		jieSuanModle = "实时结算";

		//
		tvPlacePaddingBottom = tvPlace.getPaddingBottom();
		//
		TimeDefaultUtils.setDefaultTime(tvStartTime, tvEndTime);

		//
		preManager = PreManager.instance(mContext);
		mShared = preManager.getShare();

		// 获取传过来的包编号
		packageId = this.getIntent().getStringExtra("packageId");
	}

	@Override
	protected void initData() {
		initActionBar();
		if (!TextUtils.isEmpty(packageId) && packageId != null) {// 判断是不是复制发包
			executeCopyPackageRequest();
		} else {
			// 是否第一次发包请求
			if (mShared.getBoolean(IS_FIRST_SEND_PACKAGE, true)) {
				executeIsEixtsPackageRequest();
			} else {
				// 最新的一次发包记录请求
				executeGetLastPackageRequest();
			}
		}
		// 获取公司的相关信息
		executeGetContractsRequest();
		// 获取所有货品种类
		executeGetPakagePickersRequest();
		// 获取平台是否送保险
		executeGetPlatfromServiceOptionsRequest();
	}

	/**
	 * 
	 * @Description:平台是否送保险请求
	 * @Title:executeGetPlatfromServiceOptionsRequest
	 * @return:void
	 * @throws
	 * @Create: 2016年6月17日 下午7:21:40
	 * @Author : chengtao
	 */
	private void executeGetPlatfromServiceOptionsRequest() {
		getPlatfromServiceOptionsRequest = new GetPlatfromServiceOptionsRequest(
				mContext);
		getPlatfromServiceOptionsRequest
				.setRequestId(GET_PLATFORM_SERVICE_OPTION_REQUEST);
		httpGet(getPlatfromServiceOptionsRequest);
	}

	/**
	 * 
	 * @Description:获取所有货品种类
	 * @Title:executeGetPakagePickersRequest
	 * @return:void
	 * @throws
	 * @Create: 2016年6月17日 下午7:01:12
	 * @Author : chengtao
	 */
	private void executeGetPakagePickersRequest() {
		getPakagePickersRequest = new GetPakagePickersRequest(mContext);
		getPakagePickersRequest.setRequestId(GET_PAKAGE_PICKER_REQUEST);
		httpGet(getPakagePickersRequest);
	}

	/**
	 * 
	 * @Description:获取公司信息
	 * @Title:executeGetContractsRequest
	 * @return:void
	 * @throws
	 * @Create: 2016年6月17日 下午6:57:33
	 * @Author : chengtao
	 */
	private void executeGetContractsRequest() {
		getContractsRequest = new GetContractsRequest(mContext);
		getContractsRequest.setRequestId(GET_CONTRACTS);
		httpGet(getContractsRequest);
	}

	/**
	 * 
	 * @Description:最新的一次发包记录请求
	 * @Title:executeGetLastPackageRequest
	 * @return:void
	 * @throws
	 * @Create: 2016年6月17日 下午6:57:37
	 * @Author : chengtao
	 */
	private void executeGetLastPackageRequest() {
		getLastPackageRequest = new GetLastPackageRequest(mContext);
		getLastPackageRequest.setRequestId(GET_LAST_PACKAGE_REQUEST);
		httpPost(getLastPackageRequest);
	}

	/**
	 * 
	 * @Description:是否第一次发包请求
	 * @Title:executeIsEixtsPackageRequest
	 * @return:void
	 * @throws
	 * @Create: 2016年6月17日 下午6:53:58
	 * @Author : chengtao
	 */
	private void executeIsEixtsPackageRequest() {
		isEixtsPackageRequest = new IsEixtsPackageRequest(mContext);
		isEixtsPackageRequest.setRequestId(IS_EIXTS_PACKAGE_REQUEST);
		httpPost(isEixtsPackageRequest);
	}

	/**
	 * 
	 * @Description:复制发包请求
	 * @Title:executeCopyPackageRequest
	 * @return:void
	 * @throws
	 * @Create: 2016年6月17日 下午6:52:54
	 * @Author : chengtao
	 */
	private void executeCopyPackageRequest() {
		copyPackageRequest = new CopyPackageRequest(mContext, packageId);
		copyPackageRequest.setRequestId(COPY_PACKAGE_REQUEST);
		httpPostJson(copyPackageRequest);
	}

	@Override
	protected void setListener() {
		rlHeTongId.setOnClickListener(this);
		rlPackageType.setOnClickListener(this);
		// rlJieSuanModle.setOnClickListener(this);
		rlFaPiao.setOnClickListener(this);
		etGoodsType.setOnClickListener(this);
		llStartPlace.setOnClickListener(this);
		llEndPlace.setOnClickListener(this);
		llStartTime.setOnClickListener(this);
		llEndTime.setOnClickListener(this);
		ivEmployerPhone.setOnClickListener(this);
		ivKuangFaPhone.setOnClickListener(this);
		ivQianShouPhone.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		rlPingTaiService.setOnClickListener(this);
		rlPingTaiService.setTag(true);
		cbSendInsurance.setOnCheckedChangeListener(this);
		etCarNum.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().trim() != null
						&& !TextUtils.isEmpty(s.toString().trim())) {
					etGoodsWeight.setText((Integer.parseInt((s + "").trim()) * 40)
							+ "");
				} else {
					etGoodsWeight.setText("");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});// 监听车数，自动算出货物吨数
	}

	// 初始化titileBar的方法
	private void initActionBar() {
		setActionBarTitle("发包");
		setActionBarLeft(R.drawable.nav_back);
		setActionBarRight(true, 0, "常见问题");
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isNext) {
					reDisplayWedget();
					btnNext.setText("下一步");
					isNext = false;
				} else {
					SendPackageActivity.this.finish();
				}
			}
		});
		setOnActionBarRightClickListener(false, new OnClickListener() {

			@Override
			public void onClick(View v) {
				showToast("常见问题");
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (isNext) {
				reDisplayWedget();
				btnNext.setText("下一步");
				isNext = false;
				return false;
			} else {
				SendPackageActivity.this.finish();
				return true;
			}
		}
		return false;
	}

	// TODO 发包界面弹出框
	private void createFaBaoPopupWindow(final int statusPopup) {
		// 清空集合
		popupList.clear();
		mPopupWindow = new PopupWindow(SendPackageActivity.this);
		View pop_view = SendPackageActivity.this.getLayoutInflater().inflate(
				R.layout.send_package_simple_wheel_pop_window, null);

		TextView tvCancle = (TextView) pop_view.findViewById(R.id.tv_cancle);
		TextView tvSure = (TextView) pop_view.findViewById(R.id.tv_sure);
		// 点击取消
		tvCancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
			}
		});

		tvSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(chooseText) && chooseText != null) {
					switch (statusPopup) {
					case 1: // TODO 合同编号
						tvHeTongId.setText(chooseText);
						break;
					case 2: // 发包类型
						tvPackageType.setText(chooseText);
						break;
					case 3: // 索要发票
						tvFaPiao.setText(chooseText);
						break;
					case 4: // 货品类型
						etGoodsType.setText(chooseText);
						break;
					default:
						break;
					}
				}
				mPopupWindow.dismiss();
			}
		});

		WheelView mvList = (WheelView) pop_view.findViewById(R.id.wv_show);

		// TODO 添加相应数据到集合中
		switch (statusPopup) {
		case 1:
			String hetong = null;
			for (int i = 0; i < hetongList.size(); i++) {
				hetong = hetongList.get(i).ContractCode;
				popupList.add(hetong);
			}
			break;
		case 2:
			popupList.add("一口价");
			popupList.add("竞价");
			popupList.add("定向指派");

			break;
		case 3:
			popupList.add("是");
			popupList.add("否");
			break;
		case 4:
			String pinzhong = null;
			for (int i = 0; i < pinzhongList.size(); i++) {
				pinzhong = pinzhongList.get(i).CategoryName;
				popupList.add(pinzhong);
			}
			break;

		default:
			break;
		}

		final FaBaoWheelAdapter ageTextAdapter = new FaBaoWheelAdapter(
				SendPackageActivity.this, popupList);

		mvList.setVisibleItems(5);
		mvList.setViewAdapter(ageTextAdapter);
		mvList.setCurrentItem(0);
		mvList.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String currentText = (String) ageTextAdapter.getItemText(wheel
						.getCurrentItem());
				// ageTextAdapter.getItemText(index)
				chooseText = currentText;
				setTextviewSizeTwo(currentText, ageTextAdapter);

			}
		});
		mvList.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String currentText = (String) ageTextAdapter.getItemText(wheel
						.getCurrentItem());
				itemId = wheel.getCurrentItem();

				Log.e("TAG", "---------itemID-----------" + itemId);
				setTextviewSizeTwo(currentText, ageTextAdapter);
			}
		});

		mPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
		mPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setContentView(pop_view);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setTouchable(true); // 设置PopupWindow可触摸
		mPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

	}

	class FaBaoWheelAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected FaBaoWheelAdapter(Context context, ArrayList<String> list) {
			super(context, R.layout.item_send_package_wheel_text);
			this.list = list;

			if (list.size() > 0 && popupList.size() > 0) {
				chooseText = list.get(0);
			}

			setItemTextResource(R.id.tv_item_wheel);
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index);
		}

	}

	/**
	 * 设置年龄字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSizeTwo(String curriteItemText,
			FaBaoWheelAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(maxsize);
			} else {
				textvew.setTextSize(minsize);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_hetong_id:
			// TODO 合同编号弹出框
			createFaBaoPopupWindow(1);
			// showSimplePoupWindow(createOneWheelWindow(HE_TONG));
			break;
		case R.id.rl_type:
			createFaBaoPopupWindow(2);
			// showSimplePoupWindow(createOneWheelWindow(TYPE));
			break;
		case R.id.rl_modle:
			showSimplePoupWindow(createOneWheelWindow(MODLE));
		case R.id.rl_fa_piao:
			createFaBaoPopupWindow(3);
			// showSimplePoupWindow(createOneWheelWindow(FAPIAO));
			break;
		case R.id.et_huo_pin_type:
			createFaBaoPopupWindow(4);
			// showSimplePoupWindow(createOneWheelWindow(GOODS));
			break;
		case R.id.ll_start_time:
			ChangeTimePopWin startTimePopWin = new ChangeTimePopWin(this);
			startTimePopWin.showPopWin(SendPackageActivity.this);
			startTimePopWin
					.setOnChangeTimePopWinListener(new OnChangeTimePopWinListener() {

						public void onTimePoupWindowListener(
								String selectedYear, String selectedMonth,
								String selectedDay) {
							tvStartTime.setText(selectedYear + "-"
									+ selectedMonth + "-" + selectedDay);
							startTime = selectedYear + "-" + selectedMonth
									+ "-" + selectedDay;
						}
					});
			break;
		case R.id.ll_end_time:
			ChangeTimePopWin endTimePopWin = new ChangeTimePopWin(this);
			endTimePopWin.showPopWin(SendPackageActivity.this);
			endTimePopWin
					.setOnChangeTimePopWinListener(new OnChangeTimePopWinListener() {

						public void onTimePoupWindowListener(
								String selectedYear, String selectedMonth,
								String selectedDay) {
							tvEndTime.setText(selectedYear + "-"
									+ selectedMonth + "-" + selectedDay);
							endTime = selectedYear + "-" + selectedMonth + "-"
									+ selectedDay;
						}
					});
			break;
		case R.id.ll_start_address:
			GoToChooseAcitivity(START_PLCE);
			break;
		case R.id.ll_end_address:
			GoToChooseAcitivity(EDN_PLACE);
			break;
		case R.id.iv_employer:
			getPhoneNumer(EMPLOYER);
			break;
		case R.id.iv_kuagn_fa:
			getPhoneNumer(KUANG_FA);
			break;
		case R.id.iv_qian_shou:
			getPhoneNumer(QIAN_SHOU);
			break;
		case R.id.tv_cancle:
		case R.id.black_lock:
			mPopupWindow.dismiss();
			adapter = null;
			break;
		case R.id.btn_next:
			// isNeedAudit = auditList.get(itemId).getPackageIsNeedAudit();
			if (isInfoCompleted()) {
				if (isNext) {
					if (isNeedAudit == 1) {
						// 审核
						sendNeedAuditPackage();
					} else if (isNeedAudit == 0) {
						// 不审核
						sendNotNeedAuditPackage();
					}
				} else {
					isNext = true;
					btnNext.setText("发布");
					hideWidget();
				}
			} else {
				showToast("请填写相关信息");
			}
			break;
		case R.id.rl_service:
			if ((Boolean) rlPingTaiService.getTag()) {
				llAllService.setVisibility(View.VISIBLE);
				rlPingTaiService.setTag(false);
				Drawable drawable = getResources().getDrawable(
						R.drawable.shangsanjiao);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				tvPingTaiService.setCompoundDrawables(null, null, drawable,
						null);
			} else {
				llAllService.setVisibility(View.GONE);
				rlPingTaiService.setTag(true);
				Drawable drawable = getResources().getDrawable(
						R.drawable.xiasanjiao);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				tvPingTaiService.setCompoundDrawables(null, null, drawable,
						null);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @Description:发送不需要审核的包
	 * @Title:sendNotNeedAuditPackage
	 * @return:void
	 * @throws
	 * @Create: 2016年5月24日 下午4:09:50
	 * @Author : chengtao
	 */
	private void sendNotNeedAuditPackage() {
		// String contractId = tvHeTongId.getText().toString().trim();
		String contractId = shenHeList.get(itemId).getContractId();
		String packageType = tvPackageType.getText().toString().trim();
		String faPiaoChoose = tvFaPiao.getText().toString().trim();
		String pinZhongText = etGoodsType.getText().toString().trim();
		int packageGoodsCategoryId = pinZhongIdList.get(itemId)
				.getPackageGoodsCategoryId();
		String packageTypeText = null;
		if (packageType.equals("一口价")) {
			packageTypeText = "0";
		} else if (packageType.equals("竞价")) {
			packageTypeText = "1";
		} else if (packageType.equals("定向指派")) {
			packageTypeText = "2";
		}
		// int ContractId = Integer.parseInt(heTongId);
		// int TenantId = Integer.parseInt(tenantId);
		// int PackageType = Integer.parseInt(packageType);
		int PackageSettlementType = Integer.parseInt(jieSuanModle);
		int PackageBegin = startAddressId;
		String PackageBeginAddress = startPlace;
		int PackageEnd = endAddressId;
		String PackageEndAddress = endPlace;
		float PackagePriceOrigin = Float.parseFloat(yunFei);
		float PackagePrice = 0;
		// int PackageGoodsCategoryId = Integer.parseInt(goodTypeId);
		int PackageCount = Integer.parseInt(carNum);
		float PackageWeight = Float.parseFloat(goodsWeight);
		float PackageGoodsPrice = Float.parseFloat(goodsAveprice);
		Log.e("TAG", "-----------startTime-------------" + startTime);
		Log.e("TAG", "-----------endTime-------------" + endTime);

		String PackageStartTime = StringUtils.StringDateToDateLong(startTime);
		String PackageEndTime = StringUtils.StringDateToDateLong(endTime);

		Log.e("TAG", "--------PackageStartTime--------" + PackageStartTime);
		Log.e("TAG", "--------PackageEndTime--------" + PackageEndTime);
		String TdscPhone = employerPhone;
		String TmcPhone1 = kuangFaPhone;
		String TmcPhone2 = "";
		String TmcPhone3 = "";
		String StcPhone1 = qianShouPhone;
		String StcPhone2 = "";
		String StcPhone3 = "";
		int InsuranceType = 0;
		if (isSendInsurance) {
			InsuranceType = Integer.parseInt("1");
		} else {
			InsuranceType = Integer.parseInt("0");
		}
		String needInvoice = null;
		if (faPiaoChoose.equals("是")) {
			needInvoice = "1";
		} else if (faPiaoChoose.equals("否")) {
			needInvoice = "0";
		}

		Log.e("TAG", "---------contractId---------" + contractId);
		Log.e("TAG", "---------packageTypeText---------" + packageTypeText);
		Log.e("TAG", "---------packageGoodsCategoryId---------"
				+ packageGoodsCategoryId);
		Log.e("TAG", "---------needInvoice---------" + needInvoice);
		// int NeedInvoice = Integer.parseInt(faPiao);
		String PackageMemo = note;
		sendnotNeedAuditPackageRequest = new SendNotNeedAuditPackageRequest(
				mContext, contractId, packageTypeText, PackageSettlementType,
				PackageBegin, PackageBeginAddress, PackageEnd,
				PackageEndAddress, PackagePriceOrigin, PackagePrice,
				packageGoodsCategoryId, PackageCount, PackageWeight,
				PackageGoodsPrice, PackageStartTime, PackageEndTime, TdscPhone,
				TmcPhone1, TmcPhone2, TmcPhone3, StcPhone1, StcPhone2,
				StcPhone3, InsuranceType, needInvoice, PackageMemo);
		sendnotNeedAuditPackageRequest
				.setRequestId(SEND_NOT_NEED_AUTID_PACKAGE_REQUEST);
		httpPostJson(sendnotNeedAuditPackageRequest);
	}

	/**
	 * 
	 * @Description:发送需要审核的包
	 * @Title:sendNeedAuditPackage
	 * @return:void
	 * @throws
	 * @Create: 2016年5月24日 下午4:10:14
	 * @Author : chengtao
	 */
	private void sendNeedAuditPackage() {
		// String contractId = tvHeTongId.getText().toString().trim();
		String contractId = shenHeList.get(itemId).getContractId();
		String packageType = tvPackageType.getText().toString().trim();
		String faPiaoChoose = tvFaPiao.getText().toString().trim();
		String pinZhongText = etGoodsType.getText().toString().trim();
		int packageGoodsCategoryId = pinZhongIdList.get(itemId)
				.getPackageGoodsCategoryId();
		String packageTypeText = null;
		if (packageType.equals("一口价")) {
			packageTypeText = "0";
		} else if (packageType.equals("竞价")) {
			packageTypeText = "1";
		} else if (packageType.equals("定向指派")) {
			packageTypeText = "2";
		}
		// int ContractId = Integer.parseInt(heTongId);
		// int TenantId = Integer.parseInt(tenantId);
		// int PackageType = Integer.parseInt(packageType);
		int PackageSettlementType = Integer.parseInt(jieSuanModle);
		int PackageBegin = startAddressId;
		String PackageBeginAddress = startPlace;
		int PackageEnd = endAddressId;
		String PackageEndAddress = endPlace;
		float PackagePriceOrigin = Float.parseFloat(yunFei);
		float PackagePrice = 0;
		// int PackageGoodsCategoryId = Integer.parseInt(goodTypeId);
		int PackageCount = Integer.parseInt(carNum);
		float PackageWeight = Float.parseFloat(goodsWeight);
		float PackageGoodsPrice = Float.parseFloat(goodsAveprice);
		Log.e("TAG", "-----------startTime-------------" + startTime);
		Log.e("TAG", "-----------endTime-------------" + endTime);

		String PackageStartTime = StringUtils.StringDateToDateLong(startTime);
		String PackageEndTime = StringUtils.StringDateToDateLong(endTime);

		Log.e("TAG", "--------PackageStartTime--------" + PackageStartTime);
		Log.e("TAG", "--------PackageEndTime--------" + PackageEndTime);
		String TdscPhone = employerPhone;
		String TmcPhone1 = kuangFaPhone;
		String TmcPhone2 = "";
		String TmcPhone3 = "";
		String StcPhone1 = qianShouPhone;
		String StcPhone2 = "";
		String StcPhone3 = "";
		int InsuranceType = 0;
		if (isSendInsurance) {
			InsuranceType = Integer.parseInt("1");
		} else {
			InsuranceType = Integer.parseInt("0");
		}
		String needInvoice = null;
		if (faPiaoChoose.equals("是")) {
			needInvoice = "1";
		} else if (faPiaoChoose.equals("否")) {
			needInvoice = "0";
		}
		Log.e("TAG", "---------contractId---------" + contractId);
		Log.e("TAG", "---------packageTypeText---------" + packageTypeText);
		Log.e("TAG", "---------packageGoodsCategoryId---------"
				+ packageGoodsCategoryId);
		Log.e("TAG", "---------needInvoice---------" + needInvoice);
		// int NeedInvoice = Integer.parseInt(faPiao);
		String PackageMemo = note;
		sendNeedAuditPackageRequest = new SendNeedAuditPackageRequest(mContext,
				contractId, packageTypeText, PackageSettlementType,
				PackageBegin, PackageBeginAddress, PackageEnd,
				PackageEndAddress, PackagePriceOrigin, PackagePrice,
				packageGoodsCategoryId, PackageCount, PackageWeight,
				PackageGoodsPrice, PackageStartTime, PackageEndTime, TdscPhone,
				TmcPhone1, TmcPhone2, TmcPhone3, StcPhone1, StcPhone2,
				StcPhone3, InsuranceType, needInvoice, PackageMemo);
		sendNeedAuditPackageRequest
				.setRequestId(SEND_NEED_AUDIT_PACKAGE_REQUEST);
		httpPostJson(sendNeedAuditPackageRequest);
	}

	private void GoToChooseAcitivity(int requestCode) {
		Intent intent = new Intent(mContext, ChooseAddressActivity.class);
		this.startActivityForResult(intent, requestCode);
	}

	/**
	 * 
	 * @Description:隐藏一些控件
	 * @Title:hideWidget
	 * @return:void
	 * @throws
	 * @Create: 2016年5月13日 上午12:42:44
	 * @Author : chengtao
	 */
	private void hideWidget() {
		int serviceCount = 0;// 服务item个数
		rlHeTongId.setClickable(false);
		ivHeTongId.setVisibility(View.GONE);
		ivPackageType.setVisibility(View.GONE);
		rlPackageType.setClickable(false);
		ivJieSuanModle.setVisibility(View.GONE);
		rlJieSuanModle.setClickable(false);
		ivFaPiao.setVisibility(View.GONE);
		rlFaPiao.setClickable(false);
		llStartPlace.setClickable(false);
		tvPlace.setVisibility(View.GONE);
		ivStartPlace.setVisibility(View.GONE);
		llEndPlace.setClickable(false);
		ivEndPlace.setVisibility(View.GONE);
		llStartTime.setClickable(false);
		tvTime.setVisibility(View.GONE);
		ivStartTime.setVisibility(View.GONE);
		llEndTime.setClickable(false);
		ivEndTime.setVisibility(View.GONE);
		tvTime.setVisibility(View.GONE);

		tvStartPlaceHinit.setVisibility(View.VISIBLE);
		tvEndPlaceHinit.setVisibility(View.VISIBLE);

		// etYunFei.setBackgroundColor(Color.parseColor("#00000000"));
		etYunFei.setFocusable(false);
		// etCarNum.setBackgroundColor(Color.parseColor("#00000000"));
		etCarNum.setFocusable(false);
		// etGoodsAveprice.setBackgroundColor(Color.parseColor("#00000000"));
		etGoodsAveprice.setFocusable(false);
		// etGoodsType.setBackgroundColor(Color.parseColor("#00000000"));
		etGoodsType.setCompoundDrawables(null, null, null, null);
		etGoodsType.setOnClickListener(null);
		// etGoodsWeight.setBackgroundColor(Color.parseColor("#00000000"));
		etGoodsWeight.setFocusable(false);

		ivEmployerPhone.setVisibility(View.GONE);
		ivKuangFaPhone.setVisibility(View.GONE);
		ivQianShouPhone.setVisibility(View.GONE);
		etEmployerPhone.setFocusable(false);
		etKuangFaPhone.setFocusable(false);
		etQianShouPhone.setFocusable(false);

		etNote.setFocusable(false);
		if (TextUtils.isEmpty(etNote.getText().toString())) {
			etNote.setHint("");
		}

		// 平台服务
		rlPingTaiService.setClickable(false);
		if (cbSendInsurance.isChecked()) {
			serviceCount++;
			rlSendInsurance.setVisibility(View.VISIBLE);
			cbSendInsurance.setClickable(false);
		} else {
			rlSendInsurance.setVisibility(View.GONE);
		}
		if (serviceCount > 0) {
			llAllService.setVisibility(View.VISIBLE);
			Drawable drawable = getResources().getDrawable(
					R.drawable.shangsanjiao);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			tvPingTaiService.setCompoundDrawables(null, null, drawable, null);
		} else {
			llAllService.setVisibility(View.GONE);
		}

		setActionBarTitle("包预览信息");
		setActionBarRight(true, 0, "");
	}

	/**
	 * 
	 * @Description:重新显示被隐藏的控件
	 * @Title:reDisplayWedget
	 * @return:void
	 * @throws
	 * @Create: 2016年5月13日 上午12:39:07
	 * @Author : chengtao
	 */
	protected void reDisplayWedget() {
		rlHeTongId.setClickable(true);
		ivHeTongId.setVisibility(View.VISIBLE);
		ivPackageType.setVisibility(View.VISIBLE);
		rlPackageType.setClickable(true);
		ivJieSuanModle.setVisibility(View.VISIBLE);
		rlJieSuanModle.setClickable(true);
		ivFaPiao.setVisibility(View.VISIBLE);
		rlFaPiao.setClickable(true);
		llStartPlace.setClickable(true);
		tvPlace.setVisibility(View.VISIBLE);
		ivStartPlace.setVisibility(View.VISIBLE);
		llEndPlace.setClickable(true);
		ivEndPlace.setVisibility(View.VISIBLE);
		llStartTime.setClickable(true);
		tvTime.setVisibility(View.VISIBLE);
		ivStartTime.setVisibility(View.VISIBLE);
		llEndTime.setClickable(true);
		ivEndTime.setVisibility(View.VISIBLE);
		tvTime.setVisibility(View.VISIBLE);

		tvStartPlaceHinit.setVisibility(View.GONE);
		tvEndPlaceHinit.setVisibility(View.GONE);

		// etYunFei.setBackgroundResource(R.drawable.package_et_bg);
		etYunFei.setFocusableInTouchMode(true);
		// etCarNum.setBackgroundResource(R.drawable.package_et_bg);
		etCarNum.setFocusableInTouchMode(true);
		// etGoodsAveprice.setBackgroundResource(R.drawable.package_et_bg);
		etGoodsAveprice.setFocusableInTouchMode(true);
		// etGoodsType.setBackgroundResource(R.drawable.package_et_bg);
		Drawable drawable = getResources().getDrawable(R.drawable.xiasanjiao);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		etGoodsType.setCompoundDrawables(null, null, drawable, null);
		etGoodsType.setOnClickListener(this);
		// etGoodsWeight.setBackgroundResource(R.drawable.package_et_bg);
		etGoodsWeight.setFocusableInTouchMode(true);

		ivEmployerPhone.setVisibility(View.VISIBLE);
		ivKuangFaPhone.setVisibility(View.VISIBLE);
		ivQianShouPhone.setVisibility(View.VISIBLE);
		etEmployerPhone.setFocusableInTouchMode(true);
		etKuangFaPhone.setFocusableInTouchMode(true);
		etQianShouPhone.setFocusableInTouchMode(true);

		etNote.setFocusableInTouchMode(true);
		if (TextUtils.isEmpty(etNote.getText().toString())) {
			etNote.setHint(R.string.et_note);
		}

		// 平台服务
		rlPingTaiService.setClickable(true);
		cbSendInsurance.setClickable(true);
		rlSendInsurance.setVisibility(View.VISIBLE);
		if ((Boolean) rlPingTaiService.getTag()) {
			llAllService.setVisibility(View.GONE);
			Drawable drawable1 = getResources().getDrawable(
					R.drawable.xiasanjiao);
			drawable1.setBounds(0, 0, drawable1.getMinimumWidth(),
					drawable1.getMinimumHeight());
			tvPingTaiService.setCompoundDrawables(null, null, drawable1, null);
		} else {
			llAllService.setVisibility(View.VISIBLE);
			Drawable drawable2 = getResources().getDrawable(
					R.drawable.shangsanjiao);
			drawable2.setBounds(0, 0, drawable2.getMinimumWidth(),
					drawable2.getMinimumHeight());
			tvPingTaiService.setCompoundDrawables(null, null, drawable2, null);
		}

		setActionBarTitle("发包");
		setActionBarRight(true, 0, "常见问题");
		setOnActionBarRightClickListener(false, new OnClickListener() {

			@Override
			public void onClick(View v) {
				showToast("常见问题");
			}
		});
	}

	/**
	 * 
	 * @Description:判断信息是否填写完整
	 * @Title:isInfoCompleted
	 * @return
	 * @return:boolean
	 * @throws
	 * @Create: 2016年5月13日 上午12:34:16
	 * @Author : chengtao
	 */
	private boolean isInfoCompleted() {
		// 运费
		if (etYunFei.getText() != null) {
			yunFei = etYunFei.getText().toString().trim();
		} else {
			yunFei = "";
		}
		// 车数
		if (etCarNum.getText() != null) {
			carNum = etCarNum.getText().toString().trim();
		} else {
			carNum = "";
		}
		// 货品单价
		if (etGoodsAveprice.getText() != null) {
			goodsAveprice = etGoodsAveprice.getText().toString().trim();
		} else {
			goodsAveprice = "";
		}
		// 货品类型
		if (etGoodsType.getText() != null) {
			goodsType = etGoodsType.getText().toString().trim();
		} else {
			goodsType = "";
		}
		// 总吨数
		if (etGoodsWeight.getText() != null) {
			goodsWeight = etGoodsWeight.getText().toString().trim();
		} else {
			goodsWeight = "";
		}
		// 备注
		if (etNote.getText() != null) {
			note = etNote.getText().toString();
		} else {
			note = "";
		}
		// 合同编号
		if (tvHeTongId.getText() != null) {
			heTongCode = tvHeTongId.getText().toString().trim();
		} else {
			heTongCode = "";
		}
		// 包类型
		if (tvPackageType.getText() != null) {
			if (tvPackageType.getText() == "一口价") {
				packageType = "0";
			} else if (tvPackageType.getText() == "竞价") {
				packageType = "1";
			} else if (tvPackageType.getText() == "定向指派") {
				packageType = "2";
			}
		} else {
			packageType = "";
		}
		// 结算模式
		if (tvJieSuanModle.getText() != null) {
			if (tvJieSuanModle.getText() == "实时结算") {
				jieSuanModle = "0";
			} else if (tvJieSuanModle.getText() == "定期结算") {
				jieSuanModle = "1";
			}
		} else {
			jieSuanModle = "";
		}
		// 发票
		if (tvFaPiao.getText() != null) {
			if (tvFaPiao.getText() == "否") {
				faPiao = "0";
			} else if (tvFaPiao.getText() == "是") {
				faPiao = "1";
			}
		} else {
			faPiao = "0";
		}
		// 开始地点
		if (tvStartPlace.getText() != null) {
			startPlace = tvStartPlace.getText().toString().trim();
		} else {
			startPlace = "";
		}
		// 结束地点
		if (tvEndPlace.getText() != null) {
			endPlace = tvEndPlace.getText().toString();
		} else {
			endPlace = "";
		}
		// 开始时间
		if (tvStartTime.getText() != null) {
			startTime = tvStartTime.getText().toString();
		} else {
			startTime = "";
		}
		// 结束时间
		if (tvEndTime.getText() != null) {
			endTime = tvEndTime.getText().toString();
		} else {
			endTime = "";
		}
		// 发包方电话
		if (etEmployerPhone.getText() != null) {
			employerPhone = etEmployerPhone.getText().toString();
		} else {
			employerPhone = "";
		}
		// 矿发电话
		if (etKuangFaPhone.getText() != null) {
			kuangFaPhone = etKuangFaPhone.getText().toString();
		} else {
			kuangFaPhone = "";
		}
		// 签收电话
		if (etQianShouPhone.getText() != null) {
			qianShouPhone = etQianShouPhone.getText().toString();
		} else {
			qianShouPhone = "";
		}
		Log.v("TAG", heTongCode + "--\n" + packageType + "--\n" + jieSuanModle
				+ "--\n" + yunFei + "--\n" + carNum + "--\n" + goodsAveprice
				+ "--\n" + goodsType + "--\n" + goodsWeight + "--\n"
				+ startPlace + "--\n" + endPlace + "--\n" + startTime + "--\n"
				+ endTime + "--\n" + employerPhone + "--\n" + kuangFaPhone
				+ "--\n" + qianShouPhone);
		if (TextUtils.isEmpty(heTongCode) || TextUtils.isEmpty(packageType)
				|| TextUtils.isEmpty(jieSuanModle) || TextUtils.isEmpty(yunFei)
				|| TextUtils.isEmpty(carNum)
				|| TextUtils.isEmpty(goodsAveprice)
				|| TextUtils.isEmpty(goodsType)
				|| TextUtils.isEmpty(goodsWeight)
				|| TextUtils.isEmpty(startPlace) || TextUtils.isEmpty(endPlace)
				|| TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)
				|| TextUtils.isEmpty(employerPhone)
				|| TextUtils.isEmpty(kuangFaPhone)
				|| TextUtils.isEmpty(qianShouPhone)
				|| TextUtils.isEmpty(faPiao)) {
			return false;
		} else {
			/*
			 * if (employerPhone.matches(phoneRegex) &&
			 * kuangFaPhone.matches(phoneRegex) &&
			 * qianShouPhone.matches(phoneRegex)) { return true; } else {
			 * showToast("请填写正确的手机号码");
			 */
			return true;
			// }

		}
	}

	/**
	 * 
	 * @Description:显示简单的PoupWindow
	 * @Title:showSimplePoupWindow
	 * @return:void
	 * @throws
	 * @Create: 2016年5月13日 上午12:09:14
	 * @Author : chengtao
	 */
	private void showSimplePoupWindow(PopupWindow mWindow) {
		mWindow.setTouchable(true);
		mWindow.setFocusable(true);
		mWindow.setAnimationStyle(R.style.FadeInPopWin);
		mWindow.showAtLocation(this.getWindow().getDecorView(),
				android.view.Gravity.BOTTOM, 0, 0);
		mWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		mWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
	}

	/**
	 * 
	 * @Description:从本机电话簿获取电话
	 * @Title:getPhoneNumer
	 * @param type
	 * @return:void
	 * @throws
	 * @Create: 2016年5月12日 下午2:20:41
	 * @Author : chengtao
	 */
	private void getPhoneNumer(int type) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setData(ContactsContract.Contacts.CONTENT_URI);
		startActivityForResult(intent, type);
	}

	/**
	 * 
	 * @Description:填写电话号码
	 * @Title:onActivityResult
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 * @throws
	 * @Create: 2016年5月12日 下午2:21:59
	 * @Author : cehngtao
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		} else {
			if (requestCode >= 5) {
				switch (requestCode) {
				case START_PLCE:
					tvStartPlace.setText(data.getStringExtra("address"));
					startPlace = data.getStringExtra("address");
					startAddressId = data.getIntExtra("addressId", 0);
					break;
				case EDN_PLACE:
					tvEndPlace.setText(data.getStringExtra("address"));
					endPlace = data.getStringExtra("address");
					endAddressId = data.getIntExtra("addressId", 0);
					break;
				default:
					break;
				}
				setAddressHeight();
			} else {
				Uri result = data.getData();
				String phone = "";
				String contactId = result.getLastPathSegment();
				String[] projection = new String[] { Contacts.Phones.PERSON_ID,
						Contacts.Phones.NUMBER };
				Cursor cursor = getContentResolver().query(
						Contacts.Phones.CONTENT_URI, projection,
						Contacts.Phones.PERSON_ID + " = ?",
						new String[] { contactId }, Contacts.Phones.NAME);
				if (cursor.moveToFirst()) {
					phone = cursor.getString(cursor
							.getColumnIndex(Contacts.Phones.NUMBER));
				}
				switch (requestCode) {
				case EMPLOYER:
					etEmployerPhone.setText(phone);
					employerPhone = phone;
					break;
				case KUANG_FA:
					etKuangFaPhone.setText(phone);
					kuangFaPhone = phone;
					break;
				case QIAN_SHOU:
					etQianShouPhone.setText(phone);
					qianShouPhone = phone;
					break;
				default:
					break;
				}
			}
		}
	}

	/**
	 * 
	 * @Description:设置地址栏的高度
	 * @Title:setAddressHeight
	 * @return:void
	 * @throws
	 * @Create: 2016年6月18日 下午1:56:12
	 * @Author : chengtao
	 */
	private void setAddressHeight() {
		int tvStartPlaceTextSize = (int) tvStartPlace.getTextSize();
		int tvEndPlaceTextSize = (int) tvEndPlace.getTextSize();
		tvPlace.setPadding(
				tvPlace.getPaddingLeft(),
				tvPlace.getPaddingTop(),
				tvPlace.getPaddingRight(),
				tvPlacePaddingBottom + tvStartPlaceTextSize
						* tvStartPlace.getLineCount()
						+ tvStartPlace.getPaddingBottom() + tvEndPlaceTextSize
						* tvEndPlace.getLineCount()
						+ tvEndPlace.getPaddingBottom());
		llPlace.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
	}

	/**
	 * 
	 * @Description:创建一个WheelView的poupwindow
	 * @Title:createOneWheelWindow
	 * @param type
	 * @return
	 * @return:PopupWindow
	 * @throws
	 * @Create: 2016年5月12日 下午2:22:59
	 * @Author : chengtao
	 */
	@SuppressLint("InflateParams")
	private PopupWindow createOneWheelWindow(final int type) {
		View popView = LayoutInflater.from(this).inflate(
				R.layout.send_package_simple_wheel_pop_window, null);
		TextView cancle = (TextView) popView.findViewById(R.id.tv_cancle);
		TextView sure = (TextView) popView.findViewById(R.id.tv_sure);
		View blackView = popView.findViewById(R.id.black_lock);
		WheelView mListView = (WheelView) popView.findViewById(R.id.wv_show);

		mPopupWindow = new PopupWindow(this);
		mPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
		mPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		mPopupWindow.setContentView(popView);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setTouchable(true); // 设置PopupWindow可触摸
		mPopupWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		switch (type) {
		case HE_TONG:
			adapter = new OneWheelListAdapter(this, HE_TONG, heTongIdList);
			mListView.setVisibleItems(5);
			break;
		case TYPE:
			adapter = new OneWheelListAdapter(this, TYPE, packageTypeList);
			mListView.setVisibleItems(5);
			break;
		case MODLE:
			adapter = new OneWheelListAdapter(this, MODLE, jieSuanList);
			mListView.setVisibleItems(5);
			break;
		case GOODS:
			adapter = new OneWheelListAdapter(this, GOODS, goodsTypeList);
			mListView.setVisibleItems(5);
			break;
		case FAPIAO:
			adapter = new OneWheelListAdapter(this, FAPIAO, faPiaoList);
			mListView.setVisibleItems(5);
			break;
		default:
			break;
		}
		mListView.setViewAdapter(adapter);
		mListView.setCurrentItem(0);
		mListView.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String crrentText = (String) adapter.getItemText(wheel
						.getCurrentItem());
				switch (type) {
				case HE_TONG:
					selectedHeTongCode = crrentText;
					// isNeedAudit =
					// heTongIdList.get(wheel.getCurrentItem()).get(
					// IS_NEED_ADUIT);
					heTongId = heTongIdList.get(wheel.getCurrentItem()).get(
							HE_TONG_ID);
					tenantId = heTongIdList.get(wheel.getCurrentItem()).get(
							FA_BAO_FANG_ID);
					setTextviewSizeTwo(crrentText, adapter);
					break;
				case TYPE:
					selectedPackageType = crrentText;
					setTextviewSizeTwo(crrentText, adapter);
					break;
				case MODLE:
					selectedJieSuan = crrentText;
					setTextviewSizeTwo(crrentText, adapter);
					break;
				case GOODS:
					selectedGoodsType = crrentText;
					goodTypeId = goodsTypeList.get(wheel.getCurrentItem()).get(
							GOOD_TYPE_ID);
					setTextviewSizeTwo(crrentText, adapter);
				case FAPIAO:
					selectedFaPiao = crrentText;
					setTextviewSizeTwo(crrentText, adapter);
					break;
				default:
					break;
				}
			}
		});
		mListView.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				if (adapter.getItemText(wheel.getCurrentItem()) != null) {
					String crrentText = (String) adapter.getItemText(wheel
							.getCurrentItem());
					setTextviewSizeTwo(crrentText, adapter);
				}
			}
		});
		cancle.setOnClickListener(this);
		blackView.setOnClickListener(this);
		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (type) {
				case HE_TONG:
					tvHeTongId.setText(selectedHeTongCode);
					heTongCode = selectedHeTongCode;
					break;
				case TYPE:
					tvPackageType.setText(selectedPackageType);
					packageType = selectedPackageType;
					break;
				case MODLE:
					tvJieSuanModle.setText(selectedJieSuan);
					jieSuanModle = selectedJieSuan;
					break;
				case GOODS:
					etGoodsType.setText(selectedGoodsType);
					goodsType = selectedGoodsType;
					break;
				case FAPIAO:
					tvFaPiao.setText(selectedFaPiao);
					faPiao = selectedFaPiao;
					break;

				default:
					break;
				}
				mPopupWindow.dismiss();
				adapter = null;
			}
		});
		return mPopupWindow;
	}

	/**
	 * 只有一个WheelView的Adapter
	 * 
	 * @Description:
	 * @ClassName: OneWheelListAdapter
	 * @author: chengtao
	 * @date: 2016年5月12日 下午12:59:16
	 * 
	 */
	class OneWheelListAdapter extends AbstractWheelTextAdapter {
		private List<Map<String, String>> list;

		protected OneWheelListAdapter(Context context, int type,
				List<Map<String, String>> list) {
			super(context, R.layout.item_send_package_wheel_text);
			this.list = list;
			if (list.size() > 0) {
				switch (type) {
				case HE_TONG:
					selectedHeTongCode = list.get(0).get(HE_TONG_ID);
					// isNeedAudit = list.get(0).get(IS_NEED_ADUIT);
					tenantId = list.get(0).get(FA_BAO_FANG_ID);
					break;
				case TYPE:
					selectedPackageType = list.get(0).get(PACKAGE_TYPE);
					break;
				case MODLE:
					selectedJieSuan = list.get(0).get(JIE_SUAN);
					break;
				case GOODS:
					selectedGoodsType = list.get(0).get(GOOD_TYPE);
					goodTypeId = list.get(0).get(GOOD_TYPE_ID);
					break;
				case FAPIAO:
					selectedFaPiao = list.get(0).get(FA_PIAO);
					break;
				default:
					break;
				}
			}
			setItemTextResource(R.id.tv_item_wheel);
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			String mKey = null;
			if (index > 0) {
				for (String key : list.get(0).keySet()) {
					if (key.contains(SPECIAL)) {
						mKey = key;
						break;
					}
				}
			}
			if (mKey != null) {
				return list.get(index).get(mKey);
			} else {
				return "";
			}

		}

	}

	/**
	 * 设置字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSizeTwo(String curriteItemText,
			AbstractWheelTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(maxsize);
			} else {
				textvew.setTextSize(minsize);
			}
		}
	}

	/**
	 * 
	 * @Description:本页面跳转
	 * @Title:invoke
	 * @param context
	 * @param packageId
	 *            包Id
	 * @return:void
	 * @throws
	 * @Create: 2016年5月24日 下午4:54:17
	 * @Author : chengtao
	 */
	public static void invoke(Context context, String packageId) {
		Intent intent = new Intent(context, SendPackageActivity.class);
		intent.putExtra("packageId", packageId);
		context.startActivity(intent);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.cb_send_insurance:
			if (isChecked) {
				isSendInsurance = true;
			} else {
				isSendInsurance = false;
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * @Description:请求失败
	 * @Title:onFailure
	 * @param requestId
	 * @param httpCode
	 * @param error
	 * @throws
	 * @Create: 2016年5月16日 上午8:57:56
	 * @Author : zhm
	 */
	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast("连接失败，请检查网络");
		Log.v("TAG------------", requestId + "-----" + httpCode + "-----"
				+ error.toString());
	}

	/**
	 * 
	 * @Description:请求成功
	 * @Title:onSuccess
	 * @param requestId
	 * @param response
	 * @throws
	 * @Create: 2016年5月16日 上午8:58:09
	 * @Author : zhm
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess = response.isSuccess;
		String message = response.message;
		switch (requestId) {
		case GET_CONTRACTS:
			if (isSuccess) {
				ArrayList<GetContractsInfo> getContractsInfo = (ArrayList<GetContractsInfo>) response.data;
				HeTongShenHe heTongShenHe = null;
				IsXuShenHe isXuShenHe = null;
				if (getContractsInfo != null) {
					// for (GetContractsInfo info : getContractsInfo) {
					// Map<String, String> map = new HashMap<String, String>();
					// map.put(HE_TONG_CODE+SPECIAL, info.ContractCode);
					// map.put(IS_NEED_ADUIT, info.PackageIsNeedAudit);
					// map.put(HE_TONG_ID, info.Id);
					// map.put(FA_BAO_FANG_ID, info.TenantId);
					// heTongIdList.add(map);
					// }
					hetongList.addAll(getContractsInfo);

					for (int i = 0; i < getContractsInfo.size(); i++) {
						heTongShenHe = new HeTongShenHe();
						heTongShenHe.setContractId(getContractsInfo.get(i).Id);
						shenHeList.add(heTongShenHe);
					}

					for (int i = 0; i < getContractsInfo.size(); i++) {
						isXuShenHe = new IsXuShenHe(
								getContractsInfo.get(i).PackageIsNeedAudit);
						auditList.add(isXuShenHe);
					}
				}
			}
			break;
		case IS_EIXTS_PACKAGE_REQUEST:
			if (isSuccess) {
				isFirstSendPackage = false;
				mShared.edit().putBoolean(IS_FIRST_SEND_PACKAGE,
						isFirstSendPackage);
			} else {
				isFirstSendPackage = true;
				mShared.edit().putBoolean(IS_FIRST_SEND_PACKAGE,
						isFirstSendPackage);
				etEmployerPhone.setText(mShared.getString(EMPLOYER_PHONE, ""));
			}
			break;
		case GET_LAST_PACKAGE_REQUEST:
			if (isSuccess) {
				GetLastPackageInfo getLastPackage = (GetLastPackageInfo) response.singleData;
				etEmployerPhone.setText(getLastPackage.TlscPhone);
				etKuangFaPhone.setText(getLastPackage.TmcPhone);
				etQianShouPhone.setText(getLastPackage.StcPhone);
			}
			break;
		case COPY_PACKAGE_REQUEST:
			CopyPackageInfo copyPackageInfo = (CopyPackageInfo) response.singleData;
			// 合同编号
			if (copyPackageInfo.ContractId != null
					&& !TextUtils.isEmpty(copyPackageInfo.ContractId)) {
				tvHeTongId.setText(copyPackageInfo.ContractId);
			}
			// 包类型
			if (copyPackageInfo.PackageType != null
					&& !TextUtils.isEmpty(copyPackageInfo.PackageType)) {
				if (copyPackageInfo.PackageType == "0") {
					tvPackageType.setText("一口价");
				} else if (copyPackageInfo.PackageType == "1") {
					tvPackageType.setText("竞价");
				} else if (copyPackageInfo.PackageType == "2") {
					tvPackageType.setText("定向指派");
				}
			}
			// 结算模式
			if (copyPackageInfo.PackageSettlementType != null
					&& !TextUtils
							.isEmpty(copyPackageInfo.PackageSettlementType)) {
				if (copyPackageInfo.PackageSettlementType == "0") {
					tvJieSuanModle.setText("定期结算");
				} else if (copyPackageInfo.PackageSettlementType == "1") {
					tvJieSuanModle.setText("实时结算");
				}
			}
			// 发票
			if (!copyPackageInfo.NeedInvoice) {
				tvFaPiao.setText("否");
			} else if (copyPackageInfo.NeedInvoice) {
				tvFaPiao.setText("是");
			}
			// 运费
			if (copyPackageInfo.PackagePrice != null
					&& !TextUtils.isEmpty(copyPackageInfo.PackagePrice)) {
				etYunFei.setText(copyPackageInfo.PackagePrice);
			}
			// 车数
			if (copyPackageInfo.PackageCount != null
					&& !TextUtils.isEmpty(copyPackageInfo.PackageCount)) {
				etCarNum.setText(copyPackageInfo.PackageCount);
			}
			// 货品单价
			if (copyPackageInfo.PackageGoodsPrice != null
					&& !TextUtils.isEmpty(copyPackageInfo.PackageGoodsPrice)) {
				etGoodsAveprice.setText(copyPackageInfo.PackageGoodsPrice);
			}
			// 货品类型
			if (copyPackageInfo.CategoryName != null
					&& !TextUtils.isEmpty(copyPackageInfo.CategoryName)) {
				etGoodsType.setText(copyPackageInfo.CategoryName);
			}
			// 总吨数
			if (copyPackageInfo.PackageWeight != null
					&& !TextUtils.isEmpty(copyPackageInfo.PackageWeight)) {
				etGoodsWeight.setText(copyPackageInfo.PackageWeight);
			}
			// 包的起始地点
			if (copyPackageInfo.PackageBeginAddress != null
					&& !TextUtils.isEmpty(copyPackageInfo.PackageBeginAddress)) {
				tvStartPlace.setText(copyPackageInfo.PackageBeginAddress);
			}
			// 包的终止地点
			if (copyPackageInfo.PackageEndAddress != null
					&& !TextUtils.isEmpty(copyPackageInfo.PackageEndAddress)) {
				tvEndPlace.setText(copyPackageInfo.PackageEndAddress);
			}
			// 开始时间
			if (copyPackageInfo.PackageStartTime != null
					&& !TextUtils.isEmpty(copyPackageInfo.PackageStartTime)) {
				String time = StringUtils
						.formatModify(copyPackageInfo.PackageStartTime);
				tvStartTime.setText(time);

			}
			// 结束时间
			if (copyPackageInfo.PackageEndTime != null
					&& !TextUtils.isEmpty(copyPackageInfo.PackageEndTime)) {
				String time = StringUtils
						.formatModify(copyPackageInfo.PackageStartTime);
				tvEndTime.setText(time);
			}
			// 发包方电话
			if (copyPackageInfo.TdscPhone != null
					&& !TextUtils.isEmpty(copyPackageInfo.TdscPhone)) {
				etEmployerPhone.setText(copyPackageInfo.TdscPhone);
			}
			// 矿发电话
			if (copyPackageInfo.TmcPhone != null
					&& !TextUtils.isEmpty(copyPackageInfo.TmcPhone)) {
				etKuangFaPhone.setText(copyPackageInfo.TmcPhone);
			}
			// 签收电话
			if (copyPackageInfo.StcPhone != null
					&& !TextUtils.isEmpty(copyPackageInfo.StcPhone)) {
				etQianShouPhone.setText(copyPackageInfo.StcPhone);
			}
			// 备注
			if (copyPackageInfo.PackageMemo != null
					&& !TextUtils.isEmpty(copyPackageInfo.PackageMemo)) {
				etNote.setText(copyPackageInfo.PackageMemo);
			}
			// 保险
			if (copyPackageInfo.InsuranceType != null
					&& !TextUtils.isEmpty(copyPackageInfo.InsuranceType)
					&& copyPackageInfo.InsuranceType == "1") {
				cbSendInsurance.setChecked(true);
			}
			setAddressHeight();
			break;
		case SEND_NEED_AUDIT_PACKAGE_REQUEST:
			showToast(message);
			if (isSuccess) {
				this.finish();
			}
			break;
		case SEND_NOT_NEED_AUTID_PACKAGE_REQUEST:
			showToast(message);
			if (isSuccess) {
				this.finish();
			}
			break;
		case GET_PAKAGE_PICKER_REQUEST:
			if (isSuccess) {
				ArrayList<PackagePickersInfo> lists = response.data;
				PinZhongId pinZhongId = null;
				if (lists != null) {
					// for (PackagePickersInfo packagePickersInfo : lists) {
					// Map<String, String> map = new HashMap<String, String>();
					// map.put(GOOD_TYPE+SPECIAL,
					// packagePickersInfo.CategoryName);
					// map.put(GOOD_TYPE_ID, packagePickersInfo.CategoryNum+"");
					// goodsTypeList.add(map);
					// }
					pinzhongList.addAll(lists);
					for (int i = 0; i < lists.size(); i++) {
						pinZhongId = new PinZhongId();
						pinZhongId.setPackageGoodsCategoryId(lists.get(i).Id);
						pinZhongIdList.add(pinZhongId);
					}

					Log.v("TAG----------String", goodsTypeList.toString());
				} else {
					etGoodsType.setOnClickListener(null);
				}
			} else {
				etGoodsType.setOnClickListener(null);
				showToast(message);
			}
			break;
		case GET_PLATFORM_SERVICE_OPTION_REQUEST:
			if (isSuccess) {
				isSendInsurance = true;
			} else {
				isSendInsurance = false;
			}
			break;
		default:
			break;
		}
		if (heTongIdList != null
				&& etEmployerPhone.getText().toString() != null) {
			progressBar.setVisibility(View.GONE);
			mainContainer.setVisibility(View.VISIBLE);
		}
		if (isSendInsurance) {
			rlSendInsurance.setVisibility(View.VISIBLE);
		} else {
			rlSendInsurance.setVisibility(View.GONE);
		}
	}
}
