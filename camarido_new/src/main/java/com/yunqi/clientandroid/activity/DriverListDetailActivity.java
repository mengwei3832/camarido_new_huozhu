package com.yunqi.clientandroid.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.DriverDetailInfo;
import com.yunqi.clientandroid.http.request.DeleteDriverRequest;
import com.yunqi.clientandroid.http.request.GetDriverDetailRequest;
import com.yunqi.clientandroid.http.request.SetDefaultDriverRequest;
import com.yunqi.clientandroid.http.request.SetOpRoleRequest;
import com.yunqi.clientandroid.http.request.SetSettleRoleRequest;
import com.yunqi.clientandroid.http.request.SetTicketRoleRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CacheUtils;
import com.yunqi.clientandroid.utils.ImageLoaderOptions;
import com.yunqi.clientandroid.view.CircleImageView;

/**
 * @deprecated:司机详情信息
 */
public class DriverListDetailActivity extends BaseActivity implements
		OnClickListener {

	private String vehicleDriverId;// 车辆司机的id
	private String headPortraitUrl;// 司机图像URL
	private ImageButton mBack;
	private TextView mSave;
	private TextView mPhone;
	private TextView mName;
	private TextView mStatus;
	private TextView mTime;
	private ImageView mPhotoimg;
	private CheckBox cb_default;
	private CheckBox cb_mGrap;
	private CheckBox cb_mSettlement;
	private CheckBox cb_perform;
	private Button bt_delete;
	private String format;// 存放当前时间
	private LinearLayout mLlGlobal;
	private ProgressBar mProgress;
	private CircleImageView mCivDriverHeader;

	// SP存放的key
	private final String CB_DEFAULT = "CB_DEFAULT";
	private final String CB_MGRAP = "CB_MGRAP";
	private final String CB_MSETTLEMENT = "CB_MSETTLEMENT";
	private final String CB_PERFORM = "CB_PERFORM";

	// 本页面请求
	private SetOpRoleRequest mSetOpRoleRequest;
	private SetSettleRoleRequest mSetSettleRoleRequest;
	private SetTicketRoleRequest mSetTicketRoleRequest;
	private SetDefaultDriverRequest mSetDefaultDriverRequest;
	private GetDriverDetailRequest mGetDriverDetailRequest;
	private DeleteDriverRequest mDeleteDriverRequest;

	// 本页面请求id
	private final int SET_OP_ROLE_REQUEST = 3;
	private final int SET_SETTLE_ROLE_REQUEST = 4;
	private final int SET_TICKET_ROLE_REQUEST = 5;
	private final int SET_DEFAULT_DRIVER_REQUEST = 6;
	private final int GET_DRIVER_DETAIL_REQUEST = 7;
	private final int DELETE_DRIVER_REQUEST = 8;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_yunqidriverdetail;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();
		// 获取从司机列表传过来的图像和车辆司机id
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		if (bundle != null && bundle.containsKey("vehicleDriverId")) {
			vehicleDriverId = bundle.getString("vehicleDriverId");
		}
		if (bundle != null && bundle.containsKey("headPortraitUrl")) {
			headPortraitUrl = bundle.getString("headPortraitUrl");
		}

		mBack = (ImageButton) findViewById(R.id.ib_drivermsg_back);
		mSave = (TextView) findViewById(R.id.tv_drivermsg_save);
		mPhone = (TextView) findViewById(R.id.tv_drivermsg_phone);
		mName = (TextView) findViewById(R.id.tv_drivermsg_name);
		mStatus = (TextView) findViewById(R.id.tv_drivermsg_status);
		mTime = (TextView) findViewById(R.id.tv_drivermsg_time);
		mPhotoimg = (ImageView) findViewById(R.id.iv_drivermsg_photoimg);
		cb_default = (CheckBox) findViewById(R.id.cb_drivermsg_default);
		cb_mGrap = (CheckBox) findViewById(R.id.cb_drivermsg_grap);
		cb_mSettlement = (CheckBox) findViewById(R.id.cb_drivermsg_settlement);
		cb_perform = (CheckBox) findViewById(R.id.cb_drivermsg_perform);
		bt_delete = (Button) findViewById(R.id.bt_drivermsg_delete);
		mCivDriverHeader = (CircleImageView) findViewById(R.id.iv_drivermsg_driverHeader);

		mLlGlobal = (LinearLayout) findViewById(R.id.ll_drivermsg_global);
		mProgress = (ProgressBar) findViewById(R.id.pb_drivermsg_progress);

		// 显示图像
		if (!TextUtils.isEmpty(headPortraitUrl) && headPortraitUrl != null) {
			ImageLoader.getInstance().displayImage(headPortraitUrl,
					mCivDriverHeader, ImageLoaderOptions.options);
		}

	}

	@Override
	protected void initData() {
		// 初始化时间
		initTime();
		// 从服务器获取数据
		if (!TextUtils.isEmpty(vehicleDriverId) && vehicleDriverId != null) {
			getDataFromServiceDriDetail(vehicleDriverId);
		}
	}

	// 初始化标题栏的方法
	private void initActionBar() {
		// 隐藏ActionBar
		ActionBar bar = getActionBar();
		bar.hide();
	}

	@Override
	protected void setListener() {
		// 初始化点击事件
		initOnClick();
		// 监听多选框
		initChecked();

	}

	// 初始化点击事件的方法
	private void initOnClick() {
		mBack.setOnClickListener(this);
		bt_delete.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_drivermsg_back:
			// 结束当前页面
			DriverListDetailActivity.this.finish();

			break;

		case R.id.bt_drivermsg_delete:
			// 删除司机
			deleteDriver(vehicleDriverId);

			break;

		default:
			break;
		}
	}

	// 初始化时间的方法
	@SuppressLint("SimpleDateFormat")
	private void initTime() {
		// 1.创建一个Date对象.
		Date date = new Date();
		// 2.得到 2015.03.30 16:50:25
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		format = sdf.format(date);

	}

	// 删除司机的接口
	private void deleteDriver(String VehicleDriverId) {
		mDeleteDriverRequest = new DeleteDriverRequest(this, VehicleDriverId);
		mDeleteDriverRequest.setRequestId(DELETE_DRIVER_REQUEST);
		httpPost(mDeleteDriverRequest);
	}

	// 设置司机执行权限
	protected void SetOpRole(String VehicleDriverId, boolean HasRole) {
		mSetOpRoleRequest = new SetOpRoleRequest(this, VehicleDriverId, HasRole);
		mSetOpRoleRequest.setRequestId(SET_OP_ROLE_REQUEST);
		httpPost(mSetOpRoleRequest);

	}

	// 设置司机结算权限
	protected void SetSettleRole(String VehicleDriverId, boolean HasRole) {
		mSetSettleRoleRequest = new SetSettleRoleRequest(this, VehicleDriverId,
				HasRole);
		mSetSettleRoleRequest.setRequestId(SET_SETTLE_ROLE_REQUEST);
		httpPost(mSetSettleRoleRequest);
	}

	// 设置司机抢单权限
	protected void SetTicketRole(String VehicleDriverId, boolean HasRole) {
		mSetTicketRoleRequest = new SetTicketRoleRequest(this, VehicleDriverId,
				HasRole);
		mSetTicketRoleRequest.setRequestId(SET_TICKET_ROLE_REQUEST);
		httpPost(mSetTicketRoleRequest);
	}

	// 设置默认司机的方法
	protected void SetDefaultDriver(String VehicleDriverId, boolean HasRole) {
		mSetDefaultDriverRequest = new SetDefaultDriverRequest(this,
				VehicleDriverId, HasRole);
		mSetDefaultDriverRequest.setRequestId(SET_DEFAULT_DRIVER_REQUEST);
		httpPost(mSetDefaultDriverRequest);
	}

	// 从服务器获取数据
	private void getDataFromServiceDriDetail(String vehicleDriverId) {
		mLlGlobal.setVisibility(View.INVISIBLE);
		mProgress.setVisibility(View.VISIBLE);
		mGetDriverDetailRequest = new GetDriverDetailRequest(this,
				vehicleDriverId);
		mGetDriverDetailRequest.setRequestId(GET_DRIVER_DETAIL_REQUEST);
		httpPost(mGetDriverDetailRequest);
	}

	// 监听多选框的方法
	private void initChecked() {
		// 默认司机点击监听
		cb_default.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 设置不可点击
				cb_default.setEnabled(false);
				// 从缓存中获取默认司机是否选中
				boolean default_cb = CacheUtils.getBoolean(
						getApplicationContext(), CB_DEFAULT, false);
				if (default_cb) {
					// 原来是选中状态
					SetDefaultDriver(vehicleDriverId, false);
				} else {
					// 原来是没选中状态
					SetDefaultDriver(vehicleDriverId, true);
				}

			}
		});

		// 监听抢单点击
		cb_mGrap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 设置不可点击
				cb_mGrap.setEnabled(false);
				// 从缓存中获取抢单是否已选中
				boolean mgrap_cb = CacheUtils.getBoolean(
						getApplicationContext(), CB_MGRAP, false);
				if (mgrap_cb) {
					// 原来是选中状态
					SetTicketRole(vehicleDriverId, false);
				} else {
					// 原来是没选中状态
					SetTicketRole(vehicleDriverId, true);
				}

			}
		});

		// 监听结算点击
		cb_mSettlement.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 设置不可点击
				cb_mSettlement.setEnabled(false);
				// 从缓存中获取结算是否已选中
				boolean mSettlement_cb = CacheUtils.getBoolean(
						getApplicationContext(), CB_MSETTLEMENT, false);
				if (mSettlement_cb) {
					// 原来是选中状态
					SetSettleRole(vehicleDriverId, false);
				} else {
					// 原来是没选中状态
					SetSettleRole(vehicleDriverId, true);
				}

			}
		});

		// 监听执行点击
		cb_perform.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 设置不可点击
				cb_perform.setEnabled(false);
				// 从缓存中获取执行是否已选中
				boolean perform_cb = CacheUtils.getBoolean(
						getApplicationContext(), CB_PERFORM, false);
				if (perform_cb) {
					// 原来是选中状态
					SetOpRole(vehicleDriverId, false);
				} else {
					// 原来是没选中状态
					SetOpRole(vehicleDriverId, true);
				}

			}
		});

	}

	@Override
	public void onSuccess(int requestId, Response response) {
		boolean isSuccess;
		String message;
		switch (requestId) {
		case SET_OP_ROLE_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			// 设置执行
			if (isSuccess) {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 从缓存中获取执行是否已选中
				boolean perform_cb = CacheUtils.getBoolean(
						getApplicationContext(), CB_PERFORM, false);
				if (perform_cb) {
					// 原来是选中状态
					cb_perform.setButtonDrawable(R.drawable.drivercheck);
					// 缓存是否拥有执行权限
					CacheUtils.putBoolean(getApplicationContext(), CB_PERFORM,
							false);
				} else {
					// 原来是没选中状态
					cb_perform
							.setButtonDrawable(R.drawable.drivercheckbox_check);
					// 缓存是否拥有执行权限
					CacheUtils.putBoolean(getApplicationContext(), CB_PERFORM,
							true);
				}

			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}

			// 设置可点击
			cb_perform.setEnabled(true);

			break;
		case SET_SETTLE_ROLE_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			// 设置结算
			if (isSuccess) {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 从缓存中获取结算是否已选中
				boolean mSettlement_cb = CacheUtils.getBoolean(
						getApplicationContext(), CB_MSETTLEMENT, false);
				if (mSettlement_cb) {
					// 原来是选中状态
					cb_mSettlement.setButtonDrawable(R.drawable.drivercheck);
					// 缓存是否拥有结算权限
					CacheUtils.putBoolean(getApplicationContext(),
							CB_MSETTLEMENT, false);
				} else {
					// 原来是没选中状态
					cb_mSettlement
							.setButtonDrawable(R.drawable.drivercheckbox_check);
					// 缓存是否拥有结算权限
					CacheUtils.putBoolean(getApplicationContext(),
							CB_MSETTLEMENT, true);
				}

			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}

			// 设置可点击
			cb_mSettlement.setEnabled(true);

			break;
		case SET_TICKET_ROLE_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			// 设置抢单
			if (isSuccess) {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 从缓存中获取抢单是否已选中
				boolean mgrap_cb = CacheUtils.getBoolean(
						getApplicationContext(), CB_MGRAP, false);
				if (mgrap_cb) {
					// 原来是选中状态
					cb_mGrap.setButtonDrawable(R.drawable.drivercheck);
					// 缓存是否有抢单权限
					CacheUtils.putBoolean(getApplicationContext(), CB_MGRAP,
							false);

				} else {
					// 原来是没选中状态
					cb_mGrap.setButtonDrawable(R.drawable.drivercheckbox_check);
					// 缓存是否有抢单权限
					CacheUtils.putBoolean(getApplicationContext(), CB_MGRAP,
							true);
				}

			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}

			// 设置可点击
			cb_mGrap.setEnabled(true);

			break;
		case SET_DEFAULT_DRIVER_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			// 设置默认司机
			if (isSuccess) {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
				// 从缓存中获取默认司机是否选中
				boolean default_cb = CacheUtils.getBoolean(
						getApplicationContext(), CB_DEFAULT, false);
				if (default_cb) {
					// 原来是选中状态
					cb_default.setButtonDrawable(R.drawable.drivercheck);
					// 缓存默认司机状态
					CacheUtils.putBoolean(getApplicationContext(), CB_DEFAULT,
							false);
				} else {
					// 原来是没选中状态
					cb_default
							.setButtonDrawable(R.drawable.drivercheckbox_check);
					// 缓存默认司机状态
					CacheUtils.putBoolean(getApplicationContext(), CB_DEFAULT,
							true);
				}

			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}

			// 设置可点击
			cb_default.setEnabled(true);

			break;
		case GET_DRIVER_DETAIL_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				// 获取信息成功
				DriverDetailInfo driverInfo = (DriverDetailInfo) response.singleData;
				String userName = driverInfo.userName;// 用户名
				String name = driverInfo.name;// 真实姓名
				String isDriver = driverInfo.isDriver;// 是否司机认证：0：未认证，1：认证中，2：已认证，3：认证失败
				String licenceImgUrl = driverInfo.licenceImgUrl;// 驾驶证图片Url
				Boolean isDefaultDriver = driverInfo.isDefaultDriver;// 是否是默认司机
				Boolean ticketRole = driverInfo.ticketRole;// 是否抢单抢单权限
				Boolean settleRole = driverInfo.settleRole;// 是否拥有结算权限
				Boolean opRole = driverInfo.opRole;// 是否拥有执行权限
				int driverType = driverInfo.driverType;// 0：司机，1：车主

				// 设置用户名
				if (!TextUtils.isEmpty(userName) && userName != null) {
					mPhone.setText(userName);
				}

				// 设置真实姓名
				if (!TextUtils.isEmpty(name) && name != null) {
					mName.setText(name);
				}

				// 设置司机认证情况
				if (isDriver != null && isDriver.equals("0")) {
					mStatus.setText("V 未认证");
				} else if (isDriver != null && isDriver.equals("1")) {
					mStatus.setText("V 认证中");
				} else if (isDriver != null && isDriver.equals("2")) {
					mStatus.setText("V 已认证");
				} else if (isDriver != null && isDriver.equals("3")) {
					mStatus.setText("V 认证失败");
				}

				// 设置时间
				if (!TextUtils.isEmpty(format) && format != null) {
					mTime.setText(format.split(" ")[0] + "");
				}

				// 设置行驶证照片
				if (!TextUtils.isEmpty(licenceImgUrl) && licenceImgUrl != null) {
					ImageLoader.getInstance().displayImage(licenceImgUrl,
							mPhotoimg, ImageLoaderOptions.options);
				}

				// 设置默认司机
				if (isDefaultDriver) {
					cb_default
							.setButtonDrawable(R.drawable.drivercheckbox_check);
					// 缓存默认司机状态
					CacheUtils.putBoolean(getApplicationContext(), CB_DEFAULT,
							true);
				} else {
					cb_default.setButtonDrawable(R.drawable.drivercheck);
					// 缓存默认司机状态
					CacheUtils.putBoolean(getApplicationContext(), CB_DEFAULT,
							false);
				}

				// 设置是否具有抢单权限
				if (ticketRole) {
					cb_mGrap.setButtonDrawable(R.drawable.drivercheckbox_check);
					// 缓存是否有抢单权限
					CacheUtils.putBoolean(getApplicationContext(), CB_MGRAP,
							true);
				} else {
					cb_mGrap.setButtonDrawable(R.drawable.drivercheck);
					// 缓存是否有抢单权限
					CacheUtils.putBoolean(getApplicationContext(), CB_MGRAP,
							false);
				}

				// 设置是否拥有结算权限
				if (settleRole) {
					cb_mSettlement
							.setButtonDrawable(R.drawable.drivercheckbox_check);
					// 缓存是否拥有结算权限
					CacheUtils.putBoolean(getApplicationContext(),
							CB_MSETTLEMENT, true);
				} else {
					cb_mSettlement.setButtonDrawable(R.drawable.drivercheck);
					// 缓存是否拥有结算权限
					CacheUtils.putBoolean(getApplicationContext(),
							CB_MSETTLEMENT, false);
				}

				// 设置是否拥有执行权限
				if (opRole) {
					cb_perform
							.setButtonDrawable(R.drawable.drivercheckbox_check);
					// 缓存是否拥有执行权限
					CacheUtils.putBoolean(getApplicationContext(), CB_PERFORM,
							true);
				} else {
					cb_perform.setButtonDrawable(R.drawable.drivercheck);
					// 缓存是否拥有执行权限
					CacheUtils.putBoolean(getApplicationContext(), CB_PERFORM,
							false);
				}

			} else {
				// 获取信息失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			mLlGlobal.setVisibility(View.VISIBLE);
			mProgress.setVisibility(View.INVISIBLE);
			break;
		case DELETE_DRIVER_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				// 删除司机成功
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
				// 关闭当前界面
				DriverListDetailActivity.this.finish();
			} else {
				// 删除司机失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(this.getResources().getString(R.string.net_error_toast));
		switch (requestId) {
		case SET_DEFAULT_DRIVER_REQUEST:
			// 设置可点击
			cb_default.setEnabled(true);
			break;
		case SET_TICKET_ROLE_REQUEST:
			// 设置可点击
			cb_mGrap.setEnabled(true);
			break;
		case SET_SETTLE_ROLE_REQUEST:
			// 设置可点击
			cb_mSettlement.setEnabled(true);
			break;
		case SET_OP_ROLE_REQUEST:
			// 设置可点击
			cb_perform.setEnabled(true);
			break;

		default:
			break;
		}
	}

	// 本界面跳转的方法
	public static void invoke(Context activity, String vehicleDriverId,
			String headPortraitUrl) {
		Intent intent = new Intent();
		intent.setClass(activity, DriverListDetailActivity.class);
		intent.putExtra("vehicleDriverId", vehicleDriverId);
		intent.putExtra("headPortraitUrl", headPortraitUrl);// 头像URL
		activity.startActivity(intent);
	}

}
