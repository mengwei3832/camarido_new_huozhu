package com.yunqi.clientandroid.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.AddPersonalVehicleActivity;
import com.yunqi.clientandroid.activity.HelpActivity;
import com.yunqi.clientandroid.activity.ImageScaleActivity;
import com.yunqi.clientandroid.activity.UploadPictureActivity;
import com.yunqi.clientandroid.activity.VehicleListDetailActivity;
import com.yunqi.clientandroid.activity.VehicleUploadPhotoActivity;
import com.yunqi.clientandroid.entity.VehicleDetailInfo;
import com.yunqi.clientandroid.http.request.AddVehicleAuditRequest;
import com.yunqi.clientandroid.http.request.GetVehicleDetailRequest;
import com.yunqi.clientandroid.http.request.UpdateGencheCallRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.ImageLoaderOptions;

/**
 * @deprecated:车辆详细信息
 */
public class VehicleBasicmsgFragment extends BaseFragment implements
		OnClickListener {

	private String vehicleId;// 车辆ID
	private int imgStatus;// 判断是要上传哪种图片
	private TextView tv_vehicleNo;
	private ImageView iv_withcardlicense;
	private ImageView iv_withcardfied;
	private ImageView iv_driverlicense;
	private ImageView iv_driverlicenseFail;
	private ImageView iv_insuranceimg;
	private ImageView iv_insuranceimgFail;
	private LinearLayout mLlGlobal;
	private ProgressBar mProgress;
	private RadioGroup rgCertificationType;
	private RadioButton rbCertification;
	private RadioButton rbProperty;
	private TextView tvCertificationMsg;
	private TextView tvCertificateOne;
	private LinearLayout ll_carbasic_xingxian;// 根据认证状态显示星级
	private ImageView iv_carbasic_xing1, iv_carbasic_xing2, iv_carbasic_xing3;// 星星的控件对象
	private TextView tv_carbasic_shengxing;// 升星按钮
	private Button bt_carbasic_commit;// 提交按钮通知审核
	private TextView tv_carbasic_geichecall;// 跟车电话
	private TextView tv_carbasic_xiugai;// 修改跟车电话
	private TextView tv_update_carbasic_quxiao, tv_update_carbasic_queding;// 修改电话框上的确定、取消
	private EditText et_update_carbasic_genche_call;// 电话输入框
	private TextView tv_xing_carbasic_quxiao, tv_xing_carbasic_queding;// 升星提示框上的确定、取消
	private TextView tv_xing_tishi;// 提示框说明的文字
	private int mIsSubmit;
	private LinearLayout llCarBasicTishi;
	private TextView tvCarBasicHelp;

	private String vehicleNo;// 车牌号码
	private String vehicleLicenseImgName;
	private String handIdCodeImgName;
	private String businessLicenseImgName;
	private String vehicleImgName;
	private String vehicleStatus;// 车辆认证总状态
	private String vehicleOwnerType;// 车辆所属人类型
	private String vehicleLicenseImgUrl;// 行驶证URL
	private String handIdCodeImgBase64;// 身份证URL
	private String businessLicenseImgBase64;// 营业执照URL
	private String vehicleImgBase64;// 车辆URL
	private String vehicleLicenseImgStatus;// 行驶证认证状态
	private String handIdCodeImgStatus;// 身份证认证状态
	private String businessLicenseImgStatus;// 营业执照认证状态
	private String vehicleImgStatus;// 车辆认证状态
	private String vehicleForceInsuImgUrl800;// 交强险图片Url(大图)
	private String vehicleLicenseImgUrl800; // 行驶证图片Url（大图）
	private String vehicleCertificateImgUrl800;// 营运证图片Url（大图）
	private String vehicleImgBase64800;// 车辆图片大图
	private String handIdCodeImgBase64800;// 手持身份证大图
	private int xStarsLevel;// 星级1，一星 2，二星 3，三星
	private String cVehicleContacts;// 跟车联系方式
	private String yBusinessLicenseImgBase64800;// 营运执照图片Url（大图）

	// 本页请求
	private GetVehicleDetailRequest mGetVehicleDetailRequest;// 获取车辆详情
	private UpdateGencheCallRequest mUpdateGencheCallRequest;// 将修改的电话传到后台
	private AddVehicleAuditRequest mAddVehicleAuditRequest; // 申请车辆审核

	// 本页请求id
	private final int GET_VEHICLE_DETAIL_REQUEST = 25;
	private final int GET_VEHICLE_DETAIL_CALL = 26;
	private final int ADD_VEHICLE_AUDIT = 27;

	private PopupWindow updateGencheCallPopup;// 修改跟车电话
	private PopupWindow updateShengXingPopup;// 升星提示框
	private View parentView;

	private int xType = 0;

	// 上传图片的类型ID
	private final int ID_CODE_STATUS = 1; // 手持身份证
	private final int BUSINESS_LICENSE_STATUS = 2;// 营业执照
	private final int LICENSE_STATUS = 3; // 行驶证
	private final int VEHICLE_STATUS = 4; // 车辆

	@Override
	protected void initData() {
		// 从服务器获取车辆详情信息数据
		if (!TextUtils.isEmpty(vehicleId) && vehicleId != null) {
			getDataFromServiceBasic(vehicleId);
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		if (imgStatus == 1 || imgStatus == 2 || imgStatus == 3
				|| imgStatus == 4 || imgStatus == 5 || imgStatus == 6
				|| imgStatus == 7 || imgStatus == 8) {
			// 从服务器获取车辆详情信息数据
			if (!TextUtils.isEmpty(vehicleId) && vehicleId != null) {
				getDataFromServiceBasic(vehicleId);
			}
		}
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_vehiclebasicmsg;
	}

	@Override
	protected void initView(View carbasicView) {

		parentView = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_vehiclebasicmsg, null);

		vehicleId = ((VehicleListDetailActivity) getActivity()).getId();
		// rgCertificationType = (RadioGroup) carbasicView
		// .findViewById(R.id.rg_carbasic_certificationType);
		// rbCertification = (RadioButton) carbasicView
		// .findViewById(R.id.rb_carbasic_certification);
		ll_carbasic_xingxian = (LinearLayout) carbasicView
				.findViewById(R.id.ll_carbasic_xingxian);
		iv_carbasic_xing1 = (ImageView) carbasicView
				.findViewById(R.id.iv_carbasic_xing1);
		iv_carbasic_xing2 = (ImageView) carbasicView
				.findViewById(R.id.iv_carbasic_xing2);
		iv_carbasic_xing3 = (ImageView) carbasicView
				.findViewById(R.id.iv_carbasic_xing3);
		tv_carbasic_shengxing = (TextView) carbasicView
				.findViewById(R.id.tv_carbasic_shengxing);
		bt_carbasic_commit = (Button) carbasicView
				.findViewById(R.id.bt_carbasic_commit);
		tv_carbasic_geichecall = (TextView) carbasicView
				.findViewById(R.id.tv_carbasic_geichecall);
		tv_carbasic_xiugai = (TextView) carbasicView
				.findViewById(R.id.tv_carbasic_xiugai);
		tvCertificationMsg = (TextView) carbasicView
				.findViewById(R.id.rb_carbasic_certificationMsg);
		tv_vehicleNo = (TextView) carbasicView
				.findViewById(R.id.tv_carbasic_vehicleNo);
		rbProperty = (RadioButton) carbasicView
				.findViewById(R.id.rb_carbasic_property);
		tvCertificateOne = (TextView) carbasicView
				.findViewById(R.id.tv_carbasic_certificateOne);
		iv_withcardlicense = (ImageView) carbasicView
				.findViewById(R.id.iv_carbasic_withcardlicense);
		iv_withcardfied = (ImageView) carbasicView
				.findViewById(R.id.iv_carbasic_withcardfied);
		iv_driverlicense = (ImageView) carbasicView
				.findViewById(R.id.iv_carbasic_driverlicense);
		iv_driverlicenseFail = (ImageView) carbasicView
				.findViewById(R.id.iv_carbasic_driverlicensefied);
		iv_insuranceimg = (ImageView) carbasicView
				.findViewById(R.id.iv_carbasic_insuranceimg);
		iv_insuranceimgFail = (ImageView) carbasicView
				.findViewById(R.id.iv_carbasic_insuranceimgfied);
		mLlGlobal = (LinearLayout) carbasicView
				.findViewById(R.id.ll_carbasic_global);
		mProgress = (ProgressBar) carbasicView
				.findViewById(R.id.pb_carbasic_progress);

		llCarBasicTishi = (LinearLayout) carbasicView
				.findViewById(R.id.ll_carbasic_tishixinxi);
		tvCarBasicHelp = (TextView) carbasicView
				.findViewById(R.id.tv_carbasic_question);
	}

	@Override
	protected void setListener() {
		// 初始化点击事件
		initOnClick();
	}

	// 初始化点击事件的方法
	private void initOnClick() {
		iv_withcardlicense.setOnClickListener(this);
		iv_driverlicense.setOnClickListener(this);
		iv_insuranceimg.setOnClickListener(this);
		tv_carbasic_shengxing.setOnClickListener(this);
		tv_carbasic_xiugai.setOnClickListener(this);
		bt_carbasic_commit.setOnClickListener(this);
		tvCarBasicHelp.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_carbasic_question:
			HelpActivity.invoke(getActivity(), "iscom");
			break;

		case R.id.iv_carbasic_withcardlicense:
			// 重新上传手持身份证或营业执照照片
			if (vehicleOwnerType != null && vehicleOwnerType.equals("1")) {
				// 个人车辆
				if (handIdCodeImgStatus != null
						&& handIdCodeImgStatus.equals("2")) {
					// 手持身份证图片审核中和审核通过--点击放大图片
					// if (!TextUtils.isEmpty(handIdCodeImgBase64)
					// && handIdCodeImgBase64 != null) {
					// ImageScaleActivity.invoke(getActivity(),
					// handIdCodeImgBase64);
					// }
					if (!TextUtils.isEmpty(handIdCodeImgBase64800)
							&& handIdCodeImgBase64800 != null) {
						ImageScaleActivity.invoke(getActivity(),
								handIdCodeImgBase64800);
					}

				} else if ((handIdCodeImgStatus != null && handIdCodeImgStatus
						.equals("1"))
						|| (handIdCodeImgStatus != null && handIdCodeImgStatus
								.equals("4"))
						|| (handIdCodeImgStatus != null && handIdCodeImgStatus
								.equals("5"))
						|| (handIdCodeImgBase64 == null && TextUtils
								.isEmpty(handIdCodeImgBase64))) {
					// 手持身份证图片审核未通过和证件过期--重新上传
					imgStatus = 1;

					UploadPictureActivity.invoke(getActivity(), "",
							ID_CODE_STATUS, Integer.parseInt(vehicleId));

				} else if ((handIdCodeImgStatus != null && handIdCodeImgStatus
						.equals("3"))) {
					if (!TextUtils.isEmpty(handIdCodeImgBase64)
							&& handIdCodeImgBase64 != null) {
						Log.e("TAG", "-------handIdCodeImgBase64---------"
								+ handIdCodeImgBase64);
						imgStatus = 5;
						UploadPictureActivity.invoke(getActivity(),
								handIdCodeImgBase64, ID_CODE_STATUS,
								Integer.parseInt(vehicleId));
					}
				}

			} else if (vehicleOwnerType != null && vehicleOwnerType.equals("2")) {
				// 企业车辆
				if ((businessLicenseImgStatus != null && businessLicenseImgStatus
						.equals("2"))) {
					// 营业执照图片审核中和审核通过--点击放大图片
					// if (!TextUtils.isEmpty(businessLicenseImgBase64)
					// && businessLicenseImgBase64 != null) {
					// ImageScaleActivity.invoke(getActivity(),
					// businessLicenseImgBase64);
					// }
					if (!TextUtils.isEmpty(yBusinessLicenseImgBase64800)
							&& yBusinessLicenseImgBase64800 != null) {
						ImageScaleActivity.invoke(getActivity(),
								yBusinessLicenseImgBase64800);
					}

				} else if ((businessLicenseImgStatus != null && businessLicenseImgStatus
						.equals("1"))
						|| (businessLicenseImgStatus != null && businessLicenseImgStatus
								.equals("4"))
						|| (businessLicenseImgStatus != null && businessLicenseImgStatus
								.equals("5"))
						|| (businessLicenseImgBase64 == null && TextUtils
								.isEmpty(businessLicenseImgBase64))) {
					// 营业执照图片审核未通过和证件过期--重新上传
					imgStatus = 4;

					UploadPictureActivity.invoke(getActivity(), "",
							BUSINESS_LICENSE_STATUS,
							Integer.parseInt(vehicleId));
				} else if ((businessLicenseImgStatus != null && businessLicenseImgStatus
						.equals("3"))) {
					if (!TextUtils.isEmpty(businessLicenseImgBase64)
							&& businessLicenseImgBase64 != null) {
						imgStatus = 6;
						UploadPictureActivity.invoke(getActivity(),
								businessLicenseImgBase64,
								BUSINESS_LICENSE_STATUS,
								Integer.parseInt(vehicleId));
					}
				}
			}

			break;

		case R.id.iv_carbasic_driverlicense:
			// 重新上传行驶证
			if ((vehicleLicenseImgStatus != null && vehicleLicenseImgStatus
					.equals("2"))) {
				// 行驶证图片审核中和审核通过--点击放大图片
				if (!TextUtils.isEmpty(vehicleLicenseImgUrl800)
						&& vehicleLicenseImgUrl800 != null) {
					ImageScaleActivity.invoke(getActivity(),
							vehicleLicenseImgUrl800);
				}

			} else if ((vehicleLicenseImgStatus != null && vehicleLicenseImgStatus
					.equals("1"))
					|| (vehicleLicenseImgStatus != null && vehicleLicenseImgStatus
							.equals("4"))
					|| (vehicleLicenseImgStatus != null && vehicleLicenseImgStatus
							.equals("5"))
					|| (vehicleLicenseImgUrl == null && TextUtils
							.isEmpty(vehicleLicenseImgUrl))) {
				// 行驶证图片审核未通过和证件过期--重新上传
				imgStatus = 2;
				UploadPictureActivity.invoke(getActivity(), "", LICENSE_STATUS,
						Integer.parseInt(vehicleId));
			} else if ((vehicleLicenseImgStatus != null && vehicleLicenseImgStatus
					.equals("3"))) {
				if (!TextUtils.isEmpty(vehicleLicenseImgUrl)
						&& vehicleLicenseImgUrl != null) {
					imgStatus = 7;
					UploadPictureActivity.invoke(getActivity(),
							vehicleLicenseImgUrl, LICENSE_STATUS,
							Integer.parseInt(vehicleId));
				}
			}

			break;

		case R.id.iv_carbasic_insuranceimg:
			// 重新上传车辆照片
			if ((vehicleImgStatus != null && vehicleImgStatus.equals("2"))) {
				// // 车辆图片审核中和审核通过--点击放大图片
				// if (!TextUtils.isEmpty(vehicleImgBase64)
				// && vehicleImgBase64 != null) {
				// ImageScaleActivity.invoke(getActivity(), vehicleImgBase64);
				// }
				if (!TextUtils.isEmpty(vehicleImgBase64800)
						&& vehicleImgBase64800 != null) {
					ImageScaleActivity.invoke(getActivity(),
							vehicleImgBase64800);
				}

			} else if ((vehicleImgStatus != null && vehicleImgStatus
					.equals("1"))
					|| (vehicleImgStatus != null && vehicleImgStatus
							.equals("4"))
					|| (vehicleImgStatus != null && vehicleImgStatus
							.equals("5"))
					|| (vehicleImgBase64 == null && TextUtils
							.isEmpty(vehicleImgBase64))) {
				// 车辆图片审核未通过和证件过期--重新上传
				imgStatus = 3;
				UploadPictureActivity.invoke(getActivity(), "", VEHICLE_STATUS,
						Integer.parseInt(vehicleId));
			} else if ((vehicleImgStatus != null && vehicleImgStatus
					.equals("3"))) {
				if (!TextUtils.isEmpty(vehicleImgBase64)
						&& vehicleImgBase64 != null) {
					imgStatus = 8;
					Log.e("TAG", "--------vehicleImgBase64----------"
							+ vehicleImgBase64);
					UploadPictureActivity.invoke(getActivity(),
							vehicleImgBase64, VEHICLE_STATUS,
							Integer.parseInt(vehicleId));
				}
			}

			break;

		case R.id.tv_carbasic_shengxing:// 进行升星
			// 弹出具体提示框
			showShengJiKuang();
			break;

		case R.id.tv_carbasic_xiugai:// 对跟车电话进行修改
			// 弹出修改跟车电话框
			showUpdateCallPopup();
			break;

		case R.id.bt_carbasic_commit: // 提交车辆申请审核
			if (vehicleOwnerType != null && vehicleOwnerType.equals("1")) {
				if (handIdCodeImgBase64 == null
						|| TextUtils.isEmpty(handIdCodeImgBase64)) {
					showToast("手持身份证图片未上传");
					return;
				}
			} else if (vehicleOwnerType != null && vehicleOwnerType.equals("2")) {
				if (businessLicenseImgBase64 == null
						|| TextUtils.isEmpty(businessLicenseImgBase64)) {
					showToast("营业执照图片未上传");
					return;
				}
			}

			if (vehicleLicenseImgUrl == null
					|| TextUtils.isEmpty(vehicleLicenseImgUrl)) {
				showToast("行驶证图片未上传");
				return;
			}

			if (vehicleImgBase64 == null || TextUtils.isEmpty(vehicleImgBase64)) {
				showToast("车辆图片未上传");
				return;
			}

			mAddVehicleAuditRequest = new AddVehicleAuditRequest(getActivity(),
					Integer.parseInt(vehicleId));
			mAddVehicleAuditRequest.setRequestId(ADD_VEHICLE_AUDIT);
			httpPost(mAddVehicleAuditRequest);

			break;

		default:
			break;
		}
	}

	// 弹出具体提示框
	private void showShengJiKuang() {
		if (updateShengXingPopup == null) {
			// 实例化升星提示框
			updateXing();
		}

		// 弹出框出来的方式
		updateShengXingPopup.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

		// 赋值提示的文字
		if (xStarsLevel == 1) {
			tv_xing_tishi
					.setText(Html
							.fromHtml("等级越高，能抢到的订单种类越多<br/>升级条件：上传车辆的行驶证、车头照和真实车主的身份证照或驾驶证照片"));
		} else if (xStarsLevel == 2) {
			tv_xing_tishi
					.setText(Html
							.fromHtml("只有3星级车辆，才可抢可开发票的订单<br/>升级条件：车辆的信息真实有效，真实车主在平台注册且实名认证通过"));
		}
		// 取消的监听
		tv_xing_carbasic_quxiao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateShengXingPopup.dismiss();
			}
		});

		// 确定的监听
		tv_xing_carbasic_queding.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (xStarsLevel == 1) {
					// 身份证图片状态、为待更新
					// 设置车辆产权
					if (vehicleOwnerType != null
							&& vehicleOwnerType.equals("1")) {
						iv_withcardfied
								.setImageResource(R.drawable.carbasic_in_xing);// 个人
					} else if (vehicleOwnerType != null
							&& vehicleOwnerType.equals("2")) {
						iv_withcardfied
								.setImageResource(R.drawable.carbasic_in_xing);// 企业
					}

					// 行驶证图片状态、为待更新
					iv_driverlicenseFail
							.setImageResource(R.drawable.carbasic_in_xing);

					// 车辆图片状态、为待更新
					iv_insuranceimgFail
							.setImageResource(R.drawable.carbasic_in_xing);

					// 标记可以升星
					// xType = 1;
					handIdCodeImgStatus = "5";

					businessLicenseImgStatus = "5";

					vehicleLicenseImgStatus = "5";

					vehicleImgStatus = "5";

				}
				updateShengXingPopup.dismiss();
			}
		});

	}

	// 实例化升星提示框
	private void updateXing() {
		updateShengXingPopup = new PopupWindow(getActivity());
		View updateXing_view = getActivity().getLayoutInflater().inflate(
				R.layout.popouwindow_firsttosecond, null);
		updateShengXingPopup.setWidth(LayoutParams.MATCH_PARENT);
		updateShengXingPopup.setHeight(LayoutParams.WRAP_CONTENT);
		updateShengXingPopup.setBackgroundDrawable(new BitmapDrawable());
		updateShengXingPopup.setContentView(updateXing_view);
		updateShengXingPopup.setOutsideTouchable(true);
		updateShengXingPopup.setFocusable(true);
		updateShengXingPopup.setTouchable(true); // 设置PopupWindow可触摸
		updateShengXingPopup
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		// 找控件对象
		tv_xing_carbasic_quxiao = (TextView) updateXing_view
				.findViewById(R.id.tv_xing_carbasic_quxiao);
		tv_xing_carbasic_queding = (TextView) updateXing_view
				.findViewById(R.id.tv_xing_carbasic_queding);
		tv_xing_tishi = (TextView) updateXing_view
				.findViewById(R.id.tv_xing_tishi);

	}

	// 修改跟车电话框
	private void showUpdateCallPopup() {
		if (updateGencheCallPopup == null) {
			// 实例化修改跟车电话
			updateCall();
		}
		// 弹出框出来的方式
		updateGencheCallPopup.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

		// 给电话输入框赋值
		et_update_carbasic_genche_call.setText(cVehicleContacts);

		// 取消的监听
		tv_update_carbasic_quxiao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateGencheCallPopup.dismiss();
			}
		});

		// 确定的监听
		tv_update_carbasic_queding.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 获取输入框的值
				cVehicleContacts = et_update_carbasic_genche_call.getText()
						.toString().trim();

				Log.e("TAG", "修改后的电话---++++++" + cVehicleContacts);

				if (TextUtils.isEmpty(cVehicleContacts)
						&& cVehicleContacts == null) {
					showToast("请输入手机号码");
					return;
				}

				// 检测手机号码
				if (!cVehicleContacts.matches("^[1][3-8][0-9]{9}$")) {
					showToast("请输入正确的手机号码");
					return;
				}

				// 对跟车电话重新赋值
				// tv_carbasic_geichecall.setText(cVehicleContacts);

				// TODO 将修改后的跟车电话上传到服务器
				getDateUpdateCall();
			}

		});
	}

	// 将修改后的跟车电话上传到服务器
	private void getDateUpdateCall() {
		Log.e("TAG", "电话----------" + cVehicleContacts);
		Log.e("TAG", "cheID----------" + vehicleId);

		mUpdateGencheCallRequest = new UpdateGencheCallRequest(getActivity(),
				vehicleId, cVehicleContacts);
		mUpdateGencheCallRequest.setRequestId(GET_VEHICLE_DETAIL_CALL);
		httpPost(mUpdateGencheCallRequest);

	}

	// //实例化修改跟车电话
	private void updateCall() {
		updateGencheCallPopup = new PopupWindow(getActivity());
		View updateCall_view = getActivity().getLayoutInflater().inflate(
				R.layout.popouwindow_update_genchecall, null);
		updateGencheCallPopup.setWidth(LayoutParams.MATCH_PARENT);
		updateGencheCallPopup.setHeight(LayoutParams.WRAP_CONTENT);
		updateGencheCallPopup.setBackgroundDrawable(new BitmapDrawable());
		updateGencheCallPopup.setContentView(updateCall_view);
		updateGencheCallPopup.setOutsideTouchable(true);
		updateGencheCallPopup.setFocusable(true);
		updateGencheCallPopup.setTouchable(true); // 设置PopupWindow可触摸
		updateGencheCallPopup
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		// 找控件对象
		tv_update_carbasic_quxiao = (TextView) updateCall_view
				.findViewById(R.id.tv_update_carbasic_quxiao);
		tv_update_carbasic_queding = (TextView) updateCall_view
				.findViewById(R.id.tv_update_carbasic_queding);
		et_update_carbasic_genche_call = (EditText) updateCall_view
				.findViewById(R.id.et_update_carbasic_genche_call);

	}

	// 从服务器获取车辆基本信息数据
	private void getDataFromServiceBasic(String vehicleId) {
		mLlGlobal.setVisibility(View.INVISIBLE);
		mProgress.setVisibility(View.VISIBLE);

		mGetVehicleDetailRequest = new GetVehicleDetailRequest(getActivity(),
				vehicleId);
		mGetVehicleDetailRequest.setRequestId(GET_VEHICLE_DETAIL_REQUEST);
		httpPost(mGetVehicleDetailRequest);

	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess;
		String message;
		switch (requestId) {
		case GET_VEHICLE_DETAIL_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				// 获取车辆信息成功
				VehicleDetailInfo carDetailInfo = (VehicleDetailInfo) response.singleData;
				vehicleNo = carDetailInfo.vehicleNo;// 车牌号
				vehicleStatus = carDetailInfo.vehicleStatus;// 车辆状态显示值
				vehicleOwnerType = carDetailInfo.vehicleOwnerType;// 车辆所属人类型
				handIdCodeImgBase64 = carDetailInfo.handIdCodeImgBase64;// 手持身份证URL
				businessLicenseImgBase64 = carDetailInfo.businessLicenseImgBase64;// 营业执照URL
				vehicleLicenseImgUrl = carDetailInfo.vehicleLicenseImgUrl;// 行驶证图片URL
				vehicleImgBase64 = carDetailInfo.vehicleImgBase64;// 车辆图片URL

				vehicleLicenseImgName = carDetailInfo.vehicleLicenseImgName;
				handIdCodeImgName = carDetailInfo.handIdCodeImgName;
				businessLicenseImgName = carDetailInfo.businessLicenseImgName;
				vehicleImgName = carDetailInfo.vehicleImgName;
				handIdCodeImgStatus = carDetailInfo.handIdCodeImgStatus;// 手持身份证图片状态
				businessLicenseImgStatus = carDetailInfo.businessLicenseImgStatus;// 营业执照图片状态
				vehicleLicenseImgStatus = carDetailInfo.vehicleLicenseImgStatus;// 行驶证图片状态
				vehicleImgStatus = carDetailInfo.vehicleImgStatus;// 车辆图片状态

				// 获取大图
				vehicleForceInsuImgUrl800 = carDetailInfo.vehicleForceInsuImgUrl800;// 交强险图片Url(大图)
				vehicleLicenseImgUrl800 = carDetailInfo.vehicleLicenseImgUrl800; // 行驶证图片Url（大图）
				vehicleCertificateImgUrl800 = carDetailInfo.vehicleCertificateImgUrl800;// 营运证图片Url（大图）
				vehicleImgBase64800 = carDetailInfo.vehicleImgBase64800;// 车辆图片大图
				handIdCodeImgBase64800 = carDetailInfo.handIdCodeImgBase64800;// 手持身份证大图
				yBusinessLicenseImgBase64800 = carDetailInfo.businessLicenseImgBase64800;// 营运执照图片Url（大图）

				// 新增字段
				xStarsLevel = carDetailInfo.starsLevel;// 星级1，一星 2，二星 3，三星
				cVehicleContacts = carDetailInfo.vehicleContacts;// 跟车电话
				mIsSubmit = carDetailInfo.IsSubmit;

				Log.e("TAG", "交强险图片Url(大图)-------" + vehicleForceInsuImgUrl800);
				Log.e("TAG", "行驶证图片Url(大图)-------" + vehicleLicenseImgUrl800);
				Log.e("TAG", "营运证图片Url（大图）-------"
						+ vehicleCertificateImgUrl800);
				Log.e("TAG", "车辆图片Url（大图）-------" + vehicleImgBase64800);

				Log.e("TAG", "跟车电话-----------" + cVehicleContacts);

				// 对跟车电话进行赋值
				if (!TextUtils.isEmpty(cVehicleContacts)
						&& cVehicleContacts != null) {
					tv_carbasic_geichecall.setText(cVehicleContacts);
				}

				// 判断申请按钮是否yincang
				if (mIsSubmit != 0) {
					bt_carbasic_commit.setVisibility(View.GONE);
					llCarBasicTishi.setVisibility(View.GONE);
				} else {
					bt_carbasic_commit.setVisibility(View.VISIBLE);
					llCarBasicTishi.setVisibility(View.VISIBLE);
				}

				// 设置星级1，一星 2，二星 3，三星
				if (xStarsLevel == 1) {
					ll_carbasic_xingxian.setVisibility(View.VISIBLE);
					iv_carbasic_xing1.setVisibility(View.VISIBLE);
					tvCertificationMsg.setText("你已通过一星车辆认证,可以接单了");
					tv_carbasic_shengxing.setText("升级二星认证");
				} else if (xStarsLevel == 2) {
					ll_carbasic_xingxian.setVisibility(View.VISIBLE);
					iv_carbasic_xing1.setVisibility(View.VISIBLE);
					iv_carbasic_xing2.setVisibility(View.VISIBLE);
					tvCertificationMsg.setText("你已通过二星车辆认证");
					tv_carbasic_shengxing.setText("升级三星认证");
				} else if (xStarsLevel == 3) {
					ll_carbasic_xingxian.setVisibility(View.VISIBLE);
					iv_carbasic_xing1.setVisibility(View.VISIBLE);
					iv_carbasic_xing2.setVisibility(View.VISIBLE);
					iv_carbasic_xing3.setVisibility(View.VISIBLE);
					tvCertificationMsg.setText("你已通过三星车辆认证,可抢可开发票订单了");
					tv_carbasic_shengxing.setVisibility(View.GONE);
				}

				// 设置车牌号码
				if (vehicleNo != null && !TextUtils.isEmpty(vehicleNo)) {
					tv_vehicleNo.setText(vehicleNo);
				}

				// 设置车辆产权
				if (vehicleOwnerType != null && vehicleOwnerType.equals("1")) {
					// 个人车辆
					rbProperty.setText("个人产权车辆");
					tvCertificateOne.setText("身份证/驾驶证");
					// 显示身份证照片
					if (!TextUtils.isEmpty(handIdCodeImgBase64)
							&& handIdCodeImgBase64 != null) {
						ImageLoader.getInstance().displayImage(
								handIdCodeImgBase64, iv_withcardlicense,
								ImageLoaderOptions.options);
					} else {
						iv_withcardlicense
								.setImageResource(R.drawable.iv_withcard);
					}

					if (handIdCodeImgStatus != null
							&& handIdCodeImgStatus.equals("1")) {
						// 身份证图片待审核
						iv_withcardfied
								.setImageResource(R.drawable.short_dengdai);
					} else if (handIdCodeImgStatus != null
							&& handIdCodeImgStatus.equals("2")) {
						// 身份证图片审核通过
						iv_withcardfied
								.setImageResource(R.drawable.carbasic_min_ok);
					} else if (handIdCodeImgStatus != null
							&& handIdCodeImgStatus.equals("3")) {
						// 身份证图片审核未通过
						iv_withcardfied
								.setImageResource(R.drawable.short_bohui);
					} else if (handIdCodeImgStatus != null
							&& handIdCodeImgStatus.equals("4")) {
						// 身份证图片已过期
						iv_withcardfied
								.setImageResource(R.drawable.carbasic_min_overdue);
					}

				} else if (vehicleOwnerType != null
						&& vehicleOwnerType.equals("2")) {
					// 企业车辆
					rbProperty.setText("企业人产权车辆");
					tvCertificateOne.setText("营业执照");

					// 显示营业执照照片
					if (!TextUtils.isEmpty(businessLicenseImgBase64)
							&& businessLicenseImgBase64 != null) {
						ImageLoader.getInstance().displayImage(
								businessLicenseImgBase64, iv_withcardlicense,
								ImageLoaderOptions.options);
					} else {
						iv_withcardlicense
								.setImageResource(R.drawable.iv_business);
					}

					if (businessLicenseImgStatus != null
							&& businessLicenseImgStatus.equals("1")) {
						// 营业执照图片待审核
						iv_withcardfied
								.setImageResource(R.drawable.short_dengdai);
					} else if (businessLicenseImgStatus != null
							&& businessLicenseImgStatus.equals("2")) {
						// 营业执照图片审核通过
						iv_withcardfied
								.setImageResource(R.drawable.carbasic_min_ok);
					} else if (businessLicenseImgStatus != null
							&& businessLicenseImgStatus.equals("3")) {
						// 营业执照图片审核未通过
						iv_withcardfied
								.setImageResource(R.drawable.short_bohui);
					} else if (businessLicenseImgStatus != null
							&& businessLicenseImgStatus.equals("4")) {
						// 营业执照图片已过期
						iv_withcardfied
								.setImageResource(R.drawable.carbasic_min_overdue);
					}
				}

				// 显示行驶证照片
				if (!TextUtils.isEmpty(vehicleLicenseImgUrl)
						&& vehicleLicenseImgUrl != null) {
					ImageLoader.getInstance().displayImage(
							vehicleLicenseImgUrl, iv_driverlicense,
							ImageLoaderOptions.options);
				} else {
					iv_driverlicense
							.setImageResource(R.drawable.iv_driverlicense);
				}

				// 显示车辆照片
				if (!TextUtils.isEmpty(vehicleImgBase64)
						&& vehicleImgBase64 != null) {
					ImageLoader.getInstance().displayImage(vehicleImgBase64,
							iv_insuranceimg, ImageLoaderOptions.options);
				} else {
					iv_insuranceimg
							.setImageResource(R.drawable.iv_insuranceimg);
				}

				// 根据图片状态来决定是否要重新上传
				if (vehicleImgStatus != null && vehicleImgStatus.equals("1")) {
					// 车辆图片待审核
					iv_insuranceimgFail
							.setImageResource(R.drawable.long_dengdai);
				} else if (vehicleImgStatus != null
						&& vehicleImgStatus.equals("2")) {
					// 车辆图片审核通过
					iv_insuranceimgFail
							.setImageResource(R.drawable.carbasic_max_ok);
				} else if (vehicleImgStatus != null
						&& vehicleImgStatus.equals("3")) {
					// 车辆图片审核未通过
					iv_insuranceimgFail.setImageResource(R.drawable.long_bohui);
				} else if (vehicleImgStatus != null
						&& vehicleImgStatus.equals("4")) {
					// 车辆图片已过期
					iv_insuranceimgFail
							.setImageResource(R.drawable.carbasic_max_overdue);
				}

				if (vehicleLicenseImgStatus != null
						&& vehicleLicenseImgStatus.equals("1")) {
					// 行驶证图片待审核
					iv_driverlicenseFail
							.setImageResource(R.drawable.short_dengdai);
				} else if (vehicleLicenseImgStatus != null
						&& vehicleLicenseImgStatus.equals("2")) {
					// 行驶证图片审核通过
					iv_driverlicenseFail
							.setImageResource(R.drawable.carbasic_min_ok);
				} else if (vehicleLicenseImgStatus != null
						&& vehicleLicenseImgStatus.equals("3")) {
					// 行驶证图片审核未通过
					iv_driverlicenseFail
							.setImageResource(R.drawable.short_bohui);
				} else if (vehicleLicenseImgStatus != null
						&& vehicleLicenseImgStatus.equals("4")) {
					// 行驶证图片已过期
					iv_driverlicenseFail
							.setImageResource(R.drawable.carbasic_min_overdue);
				}

			} else {
				// 获取车辆信息失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}

			mLlGlobal.setVisibility(View.VISIBLE);
			mProgress.setVisibility(View.INVISIBLE);
			break;

		case GET_VEHICLE_DETAIL_CALL:// 将修改的电话传到服务器
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				Log.e("TAG", "-------------fuzhi-------------");
				updateGencheCallPopup.dismiss();
				Log.e("TAG", "-------------jinlai-------------");
				// 对跟车电话重新赋值
				tv_carbasic_geichecall.setText(cVehicleContacts);
				showToast(message);
			} else {
				showToast(message);
			}
			break;

		case ADD_VEHICLE_AUDIT: // 提交车辆申请审核
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				showToast(message);
				getActivity().finish();
			} else {
				showToast(message);
			}
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast("连接超时,请检查网络");

	}

}
