package com.yunqi.clientandroid.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.adapter.GridViewPrivinceJianAdapter;
import com.yunqi.clientandroid.entity.AddVehicleEntity;
import com.yunqi.clientandroid.entity.GetProvinceJian;
import com.yunqi.clientandroid.entity.VehicleExist;
import com.yunqi.clientandroid.http.request.AddCompanyVehicleRequest;
import com.yunqi.clientandroid.http.request.AddPersonVehicleRequest;
import com.yunqi.clientandroid.http.request.GetPrivinceJianRequest;
import com.yunqi.clientandroid.http.request.VerifyVehicleRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.AllCapTransformationMethod;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @deprecated:新增车辆验证车牌号
 */
public class StartNewVehicleActivity extends BaseActivity implements
		OnClickListener, RadioGroup.OnCheckedChangeListener {

	private EditText mEtInputCarNumber;
	private ImageView mIvDelete1;
	private Button mBtnNextStep;
	private String vehicleNo;
	private String vehicleMsg;
	private String vehicleCall;
	private RadioGroup rgVehicleType;
	private RadioButton rbVehiclePersonal;
	private RadioButton rbVehicleEnterprise;
	private EditText mEtInputCarCall;
	private LinearLayout mLlChoose;
	private GridView mGvChoose;
	private TextView mTvPrivince;
	private ImageView mIvDelete2;
	private int mVehicleType = -1;// 车辆产权类型--1个人产权、2企业产权、-1为没选中
	private String mVehicleCarNumber;
	private PopupWindow mPrivincePopup; // 省简称的PopupWindow
	private View parentView;
	private RelativeLayout rl_quxiao;

	private ArrayList<GetProvinceJian> mPrivinceJian = new ArrayList<GetProvinceJian>();// 存放简称
	private GridViewPrivinceJianAdapter mGridViewPrivinceJianAdapter;

	// 请求ID
	private final int GET_SHENG_JIAN = 1;
	private final int CAR_NUMBER_DETAIL = 2;
	private final int ADD_PERSON_VEHICLE = 3;
	private final int ADD_COMPANY_VEHICLE = 4;

	// 请求类
	private GetPrivinceJianRequest mGetPrivinceJianRequest;
	private VerifyVehicleRequest mVerifyVehicleRequest;
	private AddPersonVehicleRequest mAddPersonVehicleRequest;
	private AddCompanyVehicleRequest mAddCompanyVehicleRequest;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_startnewvehicle;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		parentView = LayoutInflater.from(StartNewVehicleActivity.this).inflate(
				R.layout.activity_startnewvehicle, null);

		mEtInputCarNumber = (EditText) findViewById(R.id.et_startnewvehicle_inputcarNum);
		mIvDelete1 = (ImageView) findViewById(R.id.iv_et_startnewvehicle_delete1);
		mBtnNextStep = (Button) findViewById(R.id.bt_startnewvehicle_nextstep);
		rgVehicleType = (RadioGroup) findViewById(R.id.rg_vehiclemsg_type);
		rbVehiclePersonal = (RadioButton) findViewById(R.id.rb_vehiclemsg_personal);
		rbVehicleEnterprise = (RadioButton) findViewById(R.id.rb_vehiclemsg_enterprise);
		mIvDelete2 = (ImageView) findViewById(R.id.iv_et_startnewvehicle_delete2);
		mEtInputCarCall = (EditText) findViewById(R.id.et_startnewvehicle_inputcarCall);
		mLlChoose = obtainView(R.id.ll_startnewvehicle_choose); // 选择省的简称
		// mGvChoose = obtainView(R.id.gv_startnewvehicle_sheng);
		mTvPrivince = obtainView(R.id.tv_startnewvehicle_jiancheng);

		// 设置输入框显示的多是大写
		mEtInputCarNumber
				.setTransformationMethod(new AllCapTransformationMethod());
	}

	@Override
	protected void initData() {
		// 获取省份简称的请求
		getProvinceJian();

		// 给适配器适配数据
		// mGridViewPrivinceJianAdapter = new GridViewPrivinceJianAdapter(
		// mPrivinceJian, StartNewVehicleActivity.this);
		// mGvChoose.setAdapter(mGridViewPrivinceJianAdapter);
	}

	// 获取省份简称的请求
	private void getProvinceJian() {
		mGetPrivinceJianRequest = new GetPrivinceJianRequest(
				StartNewVehicleActivity.this);
		mGetPrivinceJianRequest.setRequestId(GET_SHENG_JIAN);
		httpPost(mGetPrivinceJianRequest);
	}

	@Override
	protected void setListener() {
		// 初始化点击事件
		initOnClick();
		// 初始化EditText
		initEditTextListener();
	}

	// 初始化点击事件的方法
	private void initOnClick() {
		mBtnNextStep.setOnClickListener(this);
		mIvDelete2.setOnClickListener(this);
		mIvDelete1.setOnClickListener(this);
		rgVehicleType.setOnCheckedChangeListener(this);
		mLlChoose.setOnClickListener(this);

	}

	// 初始化标题栏的方法
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(
				R.string.tv_newvehicle_titlemsg));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StartNewVehicleActivity.this.finish();
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_startnewvehicle_choose: // 选择省的简称
			// mGvChoose.setVisibility(View.VISIBLE);
			// mBtnNextStep.setVisibility(View.GONE);
			// 弹出选择省的简称
			showChoosePrivince();
			break;

		case R.id.iv_et_startnewvehicle_delete1:
			// 车牌号码输入框的删除按钮
			mEtInputCarNumber.setText("");
			break;
		case R.id.iv_et_startnewvehicle_delete2:
			// 车牌号码输入框的删除按钮
			mEtInputCarCall.setText("");
			break;

		case R.id.bt_startnewvehicle_nextstep:
			String mprivince = mTvPrivince.getText().toString().trim();
			// 验证车牌号码是否可添加
			vehicleNo = mEtInputCarNumber.getText().toString().trim();
			// 验证跟车电话是否正确
			vehicleCall = mEtInputCarCall.getText().toString().trim();
			// 将字符串中的小写转换为大写
			vehicleNo = StringUtils.swapCase(vehicleNo);

			Log.e("TAG", "省份的简称-----" + mprivince);

			if (TextUtils.isEmpty(vehicleNo)) {
				showToast("请输入车牌号码");
				return;
			}

			if (!TextUtils.isEmpty(vehicleCall) && vehicleCall != null) {
				// 检测手机号码
				if (!vehicleCall.matches("^[1][3-8][0-9]{9}$")) {
					showToast("请输入正确的手机号码");
					return;
				}
			}

			// 正则校验车牌号码
			// if (!vehicleNo.matches("[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}"))
			// {
			// showToast("请输入正确的车牌号码");
			// return;
			// }

			// 正则校验车牌号码
			if (!vehicleNo.matches("[A-Z]{1}[A-Z_0-9]{5}")) {
				showToast("请输入正确的车牌号码");
				return;
			}

			// 检测手机号码
			// if (!vehicleCall.matches("^[1][3-8][0-9]{9}$")) {
			// showToast("请输入正确的手机号码");
			// return;
			// }

			if (mVehicleType == -1) {
				showToast("请选择车辆产权");
				return;
			}

			mVehicleCarNumber = mprivince + vehicleNo;

			Log.e("TAG", "--------车牌号-----" + mVehicleCarNumber);

			mVerifyVehicleRequest = new VerifyVehicleRequest(
					StartNewVehicleActivity.this, mVehicleCarNumber);
			mVerifyVehicleRequest.setRequestId(CAR_NUMBER_DETAIL);
			httpPost(mVerifyVehicleRequest);
			// 将下一步按钮置为不可点击
			setViewEnable(false);
			break;

		default:
			break;
		}
	}

	// 设置省份简称框
	private void settingPrivince() {
		mPrivincePopup = new PopupWindow(StartNewVehicleActivity.this);

		View privince_view = StartNewVehicleActivity.this.getLayoutInflater()
				.inflate(R.layout.popupwindow_privince_gridview, null);

		mPrivincePopup.setWidth(LayoutParams.MATCH_PARENT);
		mPrivincePopup.setHeight(LayoutParams.WRAP_CONTENT);
		// mPrivincePopup.setBackgroundDrawable(new BitmapDrawable());
		mPrivincePopup.setContentView(privince_view);
		mPrivincePopup.setBackgroundDrawable(new BitmapDrawable());
		// mPrivincePopup.setOutsideTouchable(true);
		mPrivincePopup.setOutsideTouchable(true);
		mPrivincePopup.setFocusable(true);
		mPrivincePopup.setTouchable(true); // 设置PopupWindow可触摸
		mPrivincePopup
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		mGvChoose = (GridView) privince_view
				.findViewById(R.id.gv_popup_GridView);
		rl_quxiao = (RelativeLayout) privince_view.findViewById(R.id.rl_quxiao);

		// 给适配器适配数据
		mGridViewPrivinceJianAdapter = new GridViewPrivinceJianAdapter(
				mPrivinceJian, StartNewVehicleActivity.this);
		mGvChoose.setAdapter(mGridViewPrivinceJianAdapter);
	}

	/**
	 * @Description:弹出选择省份的简称框
	 * @Title:showChoosePrivince
	 * @return:void
	 * @throws
	 * @Create: 2016-6-2 下午3:31:35
	 * @Author : zhm
	 */
	private void showChoosePrivince() {
		if (mPrivincePopup == null) {
			settingPrivince();
		}

		mPrivincePopup.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

		mGvChoose.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				GetProvinceJian mGetProvinceJian = mPrivinceJian.get(position);

				String mPrivinceText = mGetProvinceJian.ShortName;

				showToast(mPrivinceText);

				mTvPrivince.setText(mPrivinceText);

				// mGvChoose.setVisibility(View.GONE);
				// mBtnNextStep.setVisibility(View.VISIBLE);
				mPrivincePopup.dismiss();
			}
		});

		rl_quxiao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPrivincePopup.dismiss();
			}
		});
	}

	// 输入框有内容监听
	private void initEditTextListener() {
		mEtInputCarNumber.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 输入框输入内容的时候
				setCarNumberChangeEditText();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable enable) {
			}
		});
		mEtInputCarCall.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 输入框输入内容的时候
				setCarNumberChangeEditText();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable enable) {
			}
		});
	}

	// 输入框有内容时执行的方法
	protected void setCarNumberChangeEditText() {
		if (mEtInputCarNumber.getText().toString().length() != 0
				&& mEtInputCarNumber.isFocused()) {
			mIvDelete1.setVisibility(View.VISIBLE);
		} else {
			mIvDelete1.setVisibility(View.GONE);
		}
	}

	// 设置按钮不可重复点击的方法
	private void setViewEnable(boolean bEnable) {
		mBtnNextStep.setEnabled(bEnable);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_vehiclemsg_personal:// 个人
			mVehicleType = 1;

			break;
		case R.id.rb_vehiclemsg_enterprise:// 企业
			mVehicleType = 2;

			break;

		default:
			break;
		}

	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess;

		switch (requestId) {
		case CAR_NUMBER_DETAIL:
			isSuccess = response.isSuccess;
			vehicleMsg = response.message;

			if (isSuccess) {

				VehicleExist vExist = (VehicleExist) response.singleData;

				// 返回结果（0:跳转至车牌号添加页面；1:跳转至发送验证码页面；2:不进行页面跳转，但是根据返回的数值提示相应的Message消息；3:同2；4:同2）
				int vGotoPage = vExist.gotoPage;

				Log.e("TAG", "返回结果--------------------------" + vGotoPage);

				if (vGotoPage == 0) {

					Log.e("TAG", "--------mVehicleType产权--------------"
							+ mVehicleType);
					// 判断是什么产权分别请求相应的接口
					if (mVehicleType == 1) {
						mAddPersonVehicleRequest = new AddPersonVehicleRequest(
								StartNewVehicleActivity.this,
								mVehicleCarNumber, vehicleCall);
						mAddPersonVehicleRequest
								.setRequestId(ADD_PERSON_VEHICLE);
						httpPost(mAddPersonVehicleRequest);
					} else if (mVehicleType == 2) {
						Log.e("TAG", "--------------准备请求------------");
						mAddCompanyVehicleRequest = new AddCompanyVehicleRequest(
								StartNewVehicleActivity.this,
								mVehicleCarNumber, vehicleCall);
						mAddCompanyVehicleRequest
								.setRequestId(ADD_COMPANY_VEHICLE);
						httpPost(mAddCompanyVehicleRequest);
					}

				} else if (vGotoPage == 1) {
					// 说明车辆已经被添加过
					if (!TextUtils.isEmpty(vehicleMsg) && vehicleMsg != null
							&& !TextUtils.isEmpty(vehicleNo)
							&& vehicleNo != null) {
//						VehicleInputCodeActivity.invoke(
//								StartNewVehicleActivity.this, vehicleMsg,
//								vehicleNo);
					}

					// 将下一步按钮置为可点击
					setViewEnable(true);
					// 关闭当前页面
					StartNewVehicleActivity.this.finish();
				} else if (vGotoPage == 2) {
					showToast(vehicleMsg);
				} else if (vGotoPage == 3) {
					showToast(vehicleMsg);
				} else if (vGotoPage == 4) {
					// 说明车辆已经被添加过
					if (!TextUtils.isEmpty(vehicleMsg) && vehicleMsg != null
							&& !TextUtils.isEmpty(vehicleNo)
							&& vehicleNo != null) {
//						VehicleInputCodeActivity.invoke(
//								StartNewVehicleActivity.this, vehicleMsg,
//								vehicleNo);
					}

					// 将下一步按钮置为可点击
					setViewEnable(true);
					// 关闭当前页面
					StartNewVehicleActivity.this.finish();
				}

			} else {
				showToast(vehicleMsg);
			}
			// 将下一步按钮置为可点击
			setViewEnable(true);
			// 关闭当前页面
			// StartNewVehicleActivity.this.finish();

			break;

		case GET_SHENG_JIAN: // 获取省份简称
			isSuccess = response.isSuccess;
			vehicleMsg = response.message;

			if (isSuccess) {
				ArrayList<GetProvinceJian> mPrivince = response.data;

				if (mPrivince.size() != 0) {
					mPrivinceJian.addAll(mPrivince);
				}

			} else {
				showToast(vehicleMsg);
			}

			break;

		case ADD_PERSON_VEHICLE: // 添加个人产权
			isSuccess = response.isSuccess;
			vehicleMsg = response.message;
			if (isSuccess) {
				AddVehicleEntity mAddVehicleEntity = (AddVehicleEntity) response.singleData;

				int mVehicleId = mAddVehicleEntity.Id;
				showToast(vehicleMsg);
				// 跳转到新增车辆界面
				if (!TextUtils.isEmpty(vehicleNo) && vehicleNo != null) {

					// 跳转到添加车辆
					AddPersonalVehicleActivity.invoke(
							StartNewVehicleActivity.this, mVehicleCarNumber,
							mVehicleType, vehicleCall, mVehicleId);
				}

				// 将下一步按钮置为可点击
				setViewEnable(true);
				// 关闭当前页面
				// StartNewVehicleActivity.this.finish();
			} else {
				showToast(vehicleMsg);
			}
			break;

		case ADD_COMPANY_VEHICLE: // 添加企业产权
			isSuccess = response.isSuccess;
			vehicleMsg = response.message;
			if (isSuccess) {
				AddVehicleEntity mAddVehicleEntity = (AddVehicleEntity) response.singleData;

				int mVehicleId = mAddVehicleEntity.Id;
				showToast(vehicleMsg);
				// 跳转到新增车辆界面
				if (!TextUtils.isEmpty(vehicleNo) && vehicleNo != null) {
					Log.e("TAG", "--------------准备跳转------------");
					// 跳转到添加车辆
					AddPersonalVehicleActivity.invoke(
							StartNewVehicleActivity.this, mVehicleCarNumber,
							mVehicleType, vehicleCall, mVehicleId);
				}

				// 将下一步按钮置为可点击
				setViewEnable(true);
				// 关闭当前页面
				// StartNewVehicleActivity.this.finish();
			} else {
				showToast(vehicleMsg);
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		// 将下一步按钮置为可点击
		setViewEnable(true);
		showToast(this.getResources().getString(R.string.net_error_toast));

	}

	/**
	 * 添加车牌号码界面跳转
	 * 
	 * 本界面跳转方法
	 * 
	 * @param activity
	 */
	public static void invoke(Activity activity) {
		Intent intent = new Intent(activity, StartNewVehicleActivity.class);
		activity.startActivity(intent);

	}

	// @Override
	// public void onItemClick(AdapterView<?> parent, View view, int position,
	// long id) {
	// GetProvinceJian mGetProvinceJian = mPrivinceJian.get(position);
	//
	// String mPrivinceText = mGetProvinceJian.ShortName;
	//
	// showToast(mPrivinceText);
	//
	// mTvPrivince.setText(mPrivinceText);
	//
	// mGvChoose.setVisibility(View.GONE);
	// mBtnNextStep.setVisibility(View.VISIBLE);
	//
	// }

}
