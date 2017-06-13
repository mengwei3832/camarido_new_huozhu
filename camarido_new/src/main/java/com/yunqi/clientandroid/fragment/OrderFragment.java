package com.yunqi.clientandroid.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.MainActivity;
import com.yunqi.clientandroid.entity.CatoryInfo;
import com.yunqi.clientandroid.http.request.GetCatoryRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.FilterManager;
import com.yunqi.clientandroid.utils.PreManager;
import com.yunqi.clientandroid.utils.StringUtils;
import com.yunqi.clientandroid.view.FlowRadioGroup;
import com.yunqi.clientandroid.view.wheel.ChangeAddressPopWin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author zhangwb
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 订单
 * @date 15/11/20
 */
public class OrderFragment extends BaseFragment implements
		View.OnClickListener, CompoundButton.OnCheckedChangeListener,
		RadioGroup.OnCheckedChangeListener {
	/**
	 * 排序，筛选的状态
	 */
	private final int TEXTVIEW_CHANGE_NOMAL = 1;
	private final int TEXTVIEW_CHANGE_CHECKED = 2;
	private final int TEXTVIEW_CHANGE_NO = 3;

	/**
	 * 3种排序常量
	 */
	public static final int SORT_ATTENTION = 1;
	public static final int SORT_SAME_CITY = 2;
	public static final int SORT_PRICE = 3;

	// PopuWindow 弹出框
	private PopupWindow mSortPopupWindow;
	// 筛选弹出框
	private PopupWindow mFilterPopuWindow = null;// 弹出框

	// 排序
	private CheckBox cbSort;
	private ImageView ivSort;
	private ImageView ivLine;
	// 筛选
	private CheckBox cbFilter;
	private ImageView ivFilter;
	// 切换
	private CheckBox cbChange;
	/**
	 * 解决焦点过小
	 */
	private LinearLayout llSort;
	private LinearLayout llFilter;
	private LinearLayout llChange;

	private FlowRadioGroup rgCatory;
	private FrameLayout flContent;

	// 筛选里面的view
	private RadioGroup rgOrderPrice;
	private RadioButton rbTender;// 竞价
	private RadioButton rbOnePrice;// 一口价
	private TextView tvStartProvince;
	private TextView tvStarCity;
	private TextView tvEndProvince;
	private TextView tvEndCity;
	private EditText etPriceMin;
	private EditText etPriceMax;
	private ImageButton ivReset;
	private Button btnSure;

	private OrderDetailListFragment mOrderDetailListFragment;
	private List<CatoryInfo> mCatoryList;
	private HashMap<String, RadioButton> mFilterCatoryMap = new HashMap<String, RadioButton>();

	// 货品种类选择的id
	private int mCatoryCheckId;
	private RadioButton mCatoryCheckButton;
	// 抢单类型
	private int mOrderType;
	// 起点和终点 城市省份id
	private int mPackageBeginProvinceId;
	private int mPackageBeginCityId;
	private String mStartProvince = "";
	private String mStartCity = "";
	private int mPackageEndProvinceId;
	private int mPackageEndCityId;
	private String mEndProvince = "";
	private String mEndCity = "";

	// 价格区间
	private long mBeginPrice;
	private long mEndPrice;

	// 获取筛选种类接口
	private GetCatoryRequest mGetCatoryRequest;
	private final int GET_CATORY_REQUEST = 1;

	private PreManager mPreManager;
	private FilterManager mFilterManager;

	@Override
	protected void initData() {
		mPreManager = PreManager.instance(getActivity());
		mFilterManager = FilterManager.instance(getActivity());
		mOrderDetailListFragment = new OrderDetailListFragment();
		FragmentTransaction transaction = getActivity()
				.getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fl_order_detail, mOrderDetailListFragment,
				"ODER_DETAIL_PAGE").commit();

		mCatoryList = new ArrayList<CatoryInfo>();
		mGetCatoryRequest = new GetCatoryRequest(getActivity());
		mGetCatoryRequest.setRequestId(GET_CATORY_REQUEST);
		httpPost(mGetCatoryRequest);

	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_order;
	}

	@Override
	protected void initView(View _rootView) {
		createSortPopuwindow();
		createFilterPopWindow();

		cbSort = (CheckBox) _rootView.findViewById(R.id.cb_sort);
		cbFilter = (CheckBox) _rootView.findViewById(R.id.cb_filter);
		cbChange = (CheckBox) _rootView.findViewById(R.id.cb_change);

		/**
		 * 解决焦点过小
		 */
		llSort = (LinearLayout) _rootView.findViewById(R.id.ll_sort);
		llFilter = (LinearLayout) _rootView.findViewById(R.id.ll_filter);
		llChange = (LinearLayout) _rootView.findViewById(R.id.ll_change);

		ivSort = (ImageView) _rootView.findViewById(R.id.iv_sort);
		ivLine = (ImageView) _rootView.findViewById(R.id.iv_line);
		ivFilter = (ImageView) _rootView.findViewById(R.id.iv_filter);
		flContent = (FrameLayout) _rootView.findViewById(R.id.fl_order_detail);
	}

	@Override
	protected void setListener() {
		cbFilter.setOnClickListener(this);
		cbSort.setOnCheckedChangeListener(this);
		cbFilter.setOnCheckedChangeListener(this);
		cbChange.setOnCheckedChangeListener(this);
		mSortPopupWindow
				.setOnDismissListener(new PopupWindow.OnDismissListener() {
					@Override
					public void onDismiss() {
						changeTextStyle(TEXTVIEW_CHANGE_NOMAL, cbSort, ivSort);
					}
				});
		mFilterPopuWindow
				.setOnDismissListener(new PopupWindow.OnDismissListener() {
					@Override
					public void onDismiss() {
						changeTextStyle(TEXTVIEW_CHANGE_NOMAL, cbFilter,
								ivFilter);
					}
				});

		rgCatory.setOnCheckedChangeListener(this);

		tvStartProvince.setOnClickListener(this);
		tvStarCity.setOnClickListener(this);
		tvEndCity.setOnClickListener(this);
		tvEndProvince.setOnClickListener(this);
		ivReset.setOnClickListener(this);
		btnSure.setOnClickListener(this);
		rgOrderPrice.setOnCheckedChangeListener(this);

		/**
		 * 解决焦点过小问题
		 */
		llSort.setOnClickListener(this);
		llFilter.setOnClickListener(this);
		llChange.setOnClickListener(this);
	}

	/**
	 * 初始化titileBar
	 */
	private void initActionBar() {
		MainActivity activity = (MainActivity) getActivity();
		activity.getActionBar().show();
		activity.setActionBarTitle(getString(R.string.order_list));
		activity.setActionBarLeft(0);
		activity.setActionBarRight(false, 0, "");
		activity.setOnActionBarLeftClickListener(null);
		activity.setOnActionBarRightClickListener(true, null);
	}

	/**
	 * 创建排序Pupwindow
	 */
	private void createSortPopuwindow() {
		View popupView = getActivity().getLayoutInflater().inflate(
				R.layout.order_popuwindow, null);

		mSortPopupWindow = new PopupWindow(popupView,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		mSortPopupWindow.setFocusable(true);
		mSortPopupWindow.setOutsideTouchable(true);
		mSortPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupView.findViewById(R.id.tv_attention_first)
				.setOnClickListener(this);
		popupView.findViewById(R.id.tv_city_first).setOnClickListener(this);
		popupView.findViewById(R.id.tv_price_sort).setOnClickListener(this);
	}

	/**
	 * 弹出框消失
	 */
	private void dismissPupWindows() {
		if (null != mSortPopupWindow) {
			mSortPopupWindow.dismiss();
		}
	}

	/**
	 * 显示弹出框
	 */
	private void showPupWindow() {
		mSortPopupWindow.showAsDropDown(ivLine, 0, 0);
	}

	@Override
	public void onResume() {
		super.onResume();
		// 初始化titileBar
		initActionBar();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_attention_first: // 关注优先
			mPreManager.setOrderSortType(SORT_ATTENTION);
			mOrderDetailListFragment.changeSort(SORT_ATTENTION);
			dismissPupWindows();
			break;
		case R.id.tv_city_first: // 同城优先
			mPreManager.setOrderSortType(SORT_SAME_CITY);
			mOrderDetailListFragment.changeSort(SORT_SAME_CITY);
			dismissPupWindows();
			break;
		case R.id.tv_price_sort: // 价格排序
			mPreManager.setOrderSortType(SORT_PRICE);
			mOrderDetailListFragment.changeSort(SORT_PRICE);
			dismissPupWindows();
			break;
		case R.id.tv_province_start_check: // 选择起点省份
		case R.id.tv_city_start_check: // 选择起点市区
			hideKeyboard();
			ChangeAddressPopWin changeAddressPopWin = new ChangeAddressPopWin(
					getActivity());
			changeAddressPopWin.setAddress("不限", "不限");
			changeAddressPopWin.showPopWin(getActivity());
			changeAddressPopWin
					.setAddresskListener(new ChangeAddressPopWin.OnAddressCListener() {
						@Override
						public void onClick(String province, String city,
								int provinceId, int cityId) {
							mPackageBeginProvinceId = provinceId;
							mPackageBeginCityId = cityId;
							mStartProvince = province;
							mStartCity = city;

							if (!TextUtils.isEmpty(province)
									&& province != null) {
								tvStartProvince.setText(province);
							}

							if (!TextUtils.isEmpty(city) && city != null) {
								tvStarCity.setText(city);
							}
						}
					});
			break;

		case R.id.tv_province_end_check: // 选择终点省份
		case R.id.tv_city_end_check: // 选择终点市区
			hideKeyboard();
			ChangeAddressPopWin changeAddressPopWiEnd = new ChangeAddressPopWin(
					getActivity());
			changeAddressPopWiEnd.setAddress("不限", "不限");
			changeAddressPopWiEnd.showPopWin(getActivity());
			changeAddressPopWiEnd
					.setAddresskListener(new ChangeAddressPopWin.OnAddressCListener() {
						@Override
						public void onClick(String province, String city,
								int provinceId, int cityId) {
							mPackageEndProvinceId = provinceId;
							mPackageEndCityId = cityId;
							mEndProvince = province;
							mEndCity = city;

							if (!TextUtils.isEmpty(province)
									&& province != null) {
								tvEndProvince.setText(province);
							}

							if (!TextUtils.isEmpty(city) && city != null) {
								tvEndCity.setText(city);
							}
						}
					});
			break;

		case R.id.iv_reset:// 重置
			filterReset();
			break;
		case R.id.btn_sure: // 确认
			if (mFilterPopuWindow != null) {
				mFilterPopuWindow.dismiss();
			}

			if (etPriceMin.getText().toString().equals("")) {
				mBeginPrice = 0;
			} else {
				mBeginPrice = Long.parseLong(etPriceMin.getText().toString());
			}
			if (etPriceMax.getText().toString().equals("")) {
				mEndPrice = 0;
			} else {
				mEndPrice = Long.parseLong(etPriceMax.getText().toString());
			}

			mOrderDetailListFragment.requestFilter(mCatoryCheckId, mOrderType,
					mPackageBeginProvinceId, mPackageBeginCityId,
					mPackageEndProvinceId, mPackageEndCityId, mBeginPrice,
					mEndPrice);
			mFilterManager.setStartProvince(mStartProvince + "_"
					+ mPackageBeginProvinceId);
			mFilterManager.setStartCity(mStartCity + "_" + mPackageBeginCityId);
			mFilterManager.setEndProvince(mEndProvince + "_"
					+ mPackageEndProvinceId);
			mFilterManager.setEndCity(mEndCity + "_" + mPackageEndCityId);
			mFilterManager.setStartPrice(mBeginPrice);
			mFilterManager.setEndPrice(mEndPrice);
			mFilterManager.setOrderType(mOrderType);
			mFilterManager.setCatoryType(mCatoryCheckId);
			// filterReset();
			break;
		/**
		 * 解决焦点过小问题
		 */
		case R.id.ll_sort:
			onCheckedChanged(cbSort, cbSort.isChecked());
			break;
		case R.id.ll_filter:
			onCheckedChanged(cbFilter, cbFilter.isChecked());
			break;
		case R.id.ll_change:
			onCheckedChanged(cbChange, cbChange.isChecked());
			break;
		}
	}

	/**
	 * 筛选状态重置
	 */
	private void filterReset() {
		// 起点重置
		tvStartProvince.setText("省份");
		mStartProvince = "省份";
		mPackageBeginProvinceId = 0;
		tvStarCity.setText("城市");
		mStartCity = "城市";
		mPackageBeginCityId = 0;

		// 终点重置
		tvEndProvince.setText("省份");
		mEndProvince = "省份";
		mPackageEndProvinceId = 0;
		tvEndCity.setText("城市");
		mEndCity = "城市";
		mPackageEndCityId = 0;

		// 价格区间重置
		etPriceMin.setText("");
		mBeginPrice = 0;
		etPriceMax.setText("");
		mEndPrice = 0;
		// 抢单类型重置
		rbTender.setChecked(false);
		rbOnePrice.setChecked(false);
		rbTender.setBackgroundResource(R.drawable.order_check_background);
		rbOnePrice.setBackgroundResource(R.drawable.order_check_background);
		rbOnePrice.setTextColor(CamaridoApp.instance.getResources().getColor(
				R.color.order_from_color));
		rbTender.setTextColor(CamaridoApp.instance.getResources().getColor(
				R.color.order_from_color));
		mOrderType = -1;
		// 货品种类重置
		setCatoryStyle("");
		mCatoryCheckId = 0;

		mFilterManager.clearSp();
	}

	/**
	 * 排序和筛选的状态选择
	 * 
	 * @param checkedCode
	 * @param textView
	 */
	private void changeTextStyle(int checkedCode, CheckBox textView,
			ImageView imageView) {
		if (checkedCode == TEXTVIEW_CHANGE_NOMAL) {
			textView.setTextColor(getActivity().getResources().getColor(
					R.color.textTitleColor));
			imageView.setImageResource(R.drawable.order_triangle_nomal);
		} else if (checkedCode == TEXTVIEW_CHANGE_CHECKED) {
			textView.setTextColor(getActivity().getResources().getColor(
					R.color.color_229AEE));
			imageView.setImageResource(R.drawable.order_triangle_nomal_click);
		} else if (checkedCode == TEXTVIEW_CHANGE_NO) {
			textView.setTextColor(getActivity().getResources().getColor(
					R.color.textNomalColor));
			imageView.setImageResource(R.drawable.order_triangle_nomal_change);
		}
	}

	/**
	 * checkbox选择 方法
	 * 
	 * @param isCheck
	 */
	private void changeCheckBox(boolean isCheck) {
		if (isCheck) {
			cbChange.setBackgroundResource(R.drawable.order_change_selected);
			changeTextStyle(TEXTVIEW_CHANGE_NO, cbSort, ivSort);
			changeTextStyle(TEXTVIEW_CHANGE_NO, cbFilter, ivFilter);
			cbSort.setOnCheckedChangeListener(null);
			cbFilter.setOnCheckedChangeListener(null);
			llSort.setOnClickListener(null);
			llFilter.setOnClickListener(null);

			getActivity()
					.getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.fl_order_detail,
							new OrderSimpleListFragment()).commit();
		} else {
			cbChange.setBackgroundResource(R.drawable.order_change_nomal);
			changeTextStyle(TEXTVIEW_CHANGE_NOMAL, cbSort, ivSort);
			changeTextStyle(TEXTVIEW_CHANGE_NOMAL, cbFilter, ivFilter);
			cbSort.setOnCheckedChangeListener(this);
			cbFilter.setOnCheckedChangeListener(this);
			llSort.setOnClickListener(this);
			llFilter.setOnClickListener(this);

			getActivity()
					.getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.fl_order_detail,
							new OrderDetailListFragment()).commit();
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.cb_sort:
			changeTextStyle(TEXTVIEW_CHANGE_NOMAL, cbFilter, ivFilter);
			if (isChecked) {
				changeTextStyle(TEXTVIEW_CHANGE_CHECKED, cbSort, ivSort);
				showPupWindow();
			} else {
				changeTextStyle(TEXTVIEW_CHANGE_NOMAL, cbSort, ivSort);
			}
			break;
		case R.id.cb_filter:
			if (isChecked) {
				changeTextStyle(TEXTVIEW_CHANGE_NOMAL, cbSort, ivSort);
				changeTextStyle(TEXTVIEW_CHANGE_CHECKED, cbFilter, ivFilter);
				showFilterPopuWindow();
				filterReset();
			} else {
				changeTextStyle(TEXTVIEW_CHANGE_NOMAL, cbSort, ivSort);
				changeTextStyle(TEXTVIEW_CHANGE_NOMAL, cbFilter, ivFilter);
			}
			break;
		case R.id.cb_change:
			changeCheckBox(isChecked);
			break;
		}
	}

	/**
	 * 显示筛选弹窗
	 */
	private void showFilterPopuWindow() {
		if (mFilterPopuWindow == null) {
			createFilterPopWindow();
		}
		tvStartProvince.setText(StringUtils.getOrderCity(mFilterManager
				.getStartProvince())[0]);
		tvStarCity.setText(StringUtils.getOrderCity(mFilterManager
				.getStartCity())[0]);
		tvEndProvince.setText(StringUtils.getOrderCity(mFilterManager
				.getEndProvince())[0]);
		tvEndCity
				.setText(StringUtils.getOrderCity(mFilterManager.getEndCity())[0]);
		etPriceMin.setText(mFilterManager.getStartPrice() == 0 ? ""
				: mFilterManager.getStartPrice() + "");
		etPriceMax.setText(mFilterManager.getEndPrice() == 0 ? ""
				: mFilterManager.getEndPrice() + "");
		mFilterPopuWindow.showAsDropDown(ivLine, 50, 0);
	}

	/**
	 * 初始化筛选弹窗
	 */
	private void createFilterPopWindow() {
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.order_filter_dialog, null);

		mFilterPopuWindow = new PopupWindow(view,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		mFilterPopuWindow.setFocusable(true);
		mFilterPopuWindow.setOutsideTouchable(false);
		mFilterPopuWindow.setBackgroundDrawable(new BitmapDrawable());
		mFilterPopuWindow.setAnimationStyle(android.R.style.Animation_Dialog);

		rgCatory = (FlowRadioGroup) view.findViewById(R.id.rg_filter_catory);

		rgOrderPrice = (RadioGroup) view.findViewById(R.id.rg_order_type);
		rbTender = (RadioButton) view.findViewById(R.id.rb_price_tender);
		rbOnePrice = (RadioButton) view.findViewById(R.id.rb_price_one);

		tvStartProvince = (TextView) view
				.findViewById(R.id.tv_province_start_check);
		tvStarCity = (TextView) view.findViewById(R.id.tv_city_start_check);
		tvEndProvince = (TextView) view
				.findViewById(R.id.tv_province_end_check);
		tvEndCity = (TextView) view.findViewById(R.id.tv_city_end_check);

		etPriceMin = (EditText) view.findViewById(R.id.et_price_min);
		etPriceMax = (EditText) view.findViewById(R.id.et_price_max);

		ivReset = (ImageButton) view.findViewById(R.id.iv_reset);
		btnSure = (Button) view.findViewById(R.id.btn_sure);

	}

	/**
	 * 筛选框 check
	 * 
	 * @param group
	 * @param checkedId
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_price_tender: // 竞价
			mOrderType = 1;
			rbTender.setTextColor(Color.WHITE);
			rbOnePrice.setTextColor(CamaridoApp.instance.getResources()
					.getColor(R.color.order_from_color));
			rbTender.setBackgroundResource(R.drawable.order_checked);
			rbOnePrice.setBackgroundResource(R.drawable.order_check_background);
			break;
		case R.id.rb_price_one: // 一口价
			mOrderType = 0;
			rbOnePrice.setTextColor(Color.WHITE);
			rbTender.setTextColor(CamaridoApp.instance.getResources().getColor(
					R.color.order_from_color));
			rbTender.setBackgroundResource(R.drawable.order_check_background);
			rbOnePrice.setBackgroundResource(R.drawable.order_checked);
			break;
		default:
			break;
		}
	}

	/**
	 * 改变detail列表接口
	 */
	public interface IChangeDetailList {
		/**
		 * 改变排序
		 * 
		 * @param sortType
		 */
		void changeSort(int sortType);

		/**
		 * 筛选刷新
		 * 
		 * @param catoryCheckId
		 * @param orderType
		 * @param packageBeginProvinceId
		 * @param packageBeginCityId
		 * @param packageEndProvinceId
		 * @param packageEndCityId
		 * @param beginPrice
		 * @param endPrice
		 */
		void requestFilter(int catoryCheckId, int orderType,
				int packageBeginProvinceId, int packageBeginCityId,
				int packageEndProvinceId, int packageEndCityId,
				long beginPrice, long endPrice);

	}

	@Override
	public void onStart(int requestId) {
		super.onStart(requestId);
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess;
		String message;
		switch (requestId) {
		case GET_CATORY_REQUEST: // 获取种类接口
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				mCatoryList = response.data;
				if (mCatoryList.size() > 0) {
					for (final CatoryInfo catoryInfo : mCatoryList) {
						RadioButton tempButton = new RadioButton(
								CamaridoApp.instance);
						tempButton
								.setBackgroundResource(R.drawable.order_check_background); // 设置RadioButton的背景图片
						tempButton.setButtonDrawable(new ColorDrawable(
								Color.TRANSPARENT)); // 设置按钮的样式
						tempButton.setGravity(Gravity.CENTER);
						tempButton.setText(catoryInfo.name);
						tempButton.setTextSize(15);
						tempButton.setTextColor(CamaridoApp.instance
								.getResources().getColor(
										R.color.order_from_color));
						rgCatory.addView(tempButton,
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);
						mFilterCatoryMap.put(catoryInfo.name, tempButton);

						tempButton
								.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
									@Override
									public void onCheckedChanged(
											CompoundButton buttonView,
											boolean isChecked) {
										if (isChecked) {
											mCatoryCheckId = catoryInfo.id;
											setCatoryStyle(catoryInfo.name);
										}
									}
								});
					}
				}
			} else {

			}
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		switch (requestId) {
		case GET_CATORY_REQUEST:
			showToast("连接超时,请检查网络");
			break;
		}

	}

	/**
	 * 改变种类状态
	 * 
	 * @param key
	 */
	private void setCatoryStyle(String key) {
		RadioButton radioButton = null;
		for (String catoryName : mFilterCatoryMap.keySet()) {
			if (catoryName.equals(key)) {
				radioButton = mFilterCatoryMap.get(catoryName);
				radioButton.setChecked(true);
				radioButton.setTextColor(Color.WHITE);
				radioButton.setBackgroundResource(R.drawable.order_checked);
			} else {
				radioButton = mFilterCatoryMap.get(catoryName);
				radioButton.setChecked(false);
				radioButton.setTextColor(CamaridoApp.instance.getResources()
						.getColor(R.color.order_from_color));
				radioButton
						.setBackgroundResource(R.drawable.order_check_background);
			}
		}
	}

	/**
	 * 隐藏软键盘
	 */
	private void hideKeyboard() {
		InputMethodManager im = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(etPriceMin.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
		im.hideSoftInputFromWindow(etPriceMax.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

}
