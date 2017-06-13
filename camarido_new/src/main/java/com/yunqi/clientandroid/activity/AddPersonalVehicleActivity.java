package com.yunqi.clientandroid.activity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.VehicleDetailInfo;
import com.yunqi.clientandroid.http.request.AddEnterpriseVehicleRequest;
import com.yunqi.clientandroid.http.request.AddPersonalVehicleRequest;
import com.yunqi.clientandroid.http.request.AddVehicleAuditRequest;
import com.yunqi.clientandroid.http.request.GetVehicleDetailRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.FileUtils;
import com.yunqi.clientandroid.utils.ImageLoaderOptions;
import com.yunqi.clientandroid.utils.ImageUtil;
import com.yunqi.clientandroid.utils.PickImage;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 新增车辆界面
 * @date 15/03/30
 */
public class AddPersonalVehicleActivity extends BaseActivity implements
		OnClickListener {

	private View parentView;
	private String vehicleNo;
	private ProgressBar progressBarHandle;
	private TextView myProssBarhandleText;
	private Button mBtnCommit;
	private EditText mEtVehicleNo;
	private EditText etNewVehileInputCarPhone;
	private TextView tvQuestion;
	private ImageView mIvWithcardlicense;
	private ImageView mIvDrivinglicense;
	private ImageView mIvInsuranceimg;
	private ImageView mIvWithcardStatus;
	private ImageView mIvDrivingStatus;
	private ImageView mIvInsuranceStatus;
	private PopupWindow withcardPopupWindow;
	private PopupWindow drivingPopupWindow;
	private PopupWindow insurancePopupWindow;
	private PopupWindow businessPopupWindow;
	private RadioButton rbPersonal;
	private TextView tvCertificateOne;
	private int mVehicleType;// 车辆产权类型--1个人产权、2企业产权
	private TextView tv_newvehicle_xinxibiangeng;// 车辆照片下的文本变更
	private String mVvehicleCall; // 跟车电话

	// 存放图片文件夹的统一路径
	private String personalVehicleTempFileDir = Environment
			.getExternalStorageDirectory() + "/";
	// 手持身份证存放图片文件夹的路径
	private String withCardImgName = "WithCardlicense.jpg";// 手持身份证图片名
	private File withCardimageFile = null;
	private String imgWithCardBase64;// 存放手持身份证图片的字符串

	// 存放行驶证图片文件夹的路径
	private String drivingImgName = "Drivinglicense.jpg";// 行驶证图片名
	private File drivingimageFile = null;
	private String imgDrivingBase64;// 存放行驶证图片的字符串

	// 车辆存放图片文件夹的路径
	private String vehicleImgName = "Vehiclelicense.jpg";// 车辆图片名
	private File vehicleimageFile = null;
	private String imgInsuranceBase64;// 存放车辆图片的字符串

	// 营业执照存放图片文件夹的路径
	private String businessImgName = "Businesslicense.jpg";// 营业执照图片名
	private File businessimageFile = null;
	private String imgBusinessBase64;// 存放营业执照图片的字符串

	private String vehicleCall;// 跟车电话
	private int mVehicleId; // 车ID

	private final int MAX_PROGRESS = 100;
	private final int NINE_PROGRESS = 96;
	private final int MIN_PROGRESS = 0;
	private final int PRO = 10;
	private final int ALL = 20;
	private final int FAIL = 30;
	private int progress = 1;

	private String vehicleStatus;// 车辆状态显示值
	private String vehicleOwnerType;// 车辆所属人类型
	private String handIdCodeImgBase64;// 手持身份证URL
	private String businessLicenseImgBase64;// 营业执照URL
	private String vehicleLicenseImgUrl;// 行驶证图片URL
	private String vehicleImgBase64;// 车辆图片URL

	private String handIdCodeImgStatus;// 手持身份证图片状态
	private String businessLicenseImgStatus;// 营业执照图片状态
	private String vehicleLicenseImgStatus;// 行驶证图片状态
	private String vehicleImgStatus;// 车辆图片状态

	// 本页请求
	private AddPersonalVehicleRequest mAddVehicleRequest;
	private AddEnterpriseVehicleRequest mAddEnterpriseVehicleRequest;
	private GetVehicleDetailRequest mGetVehicleDetailRequest;
	private AddVehicleAuditRequest mAddVehicleAuditRequest;

	// 本页请求id
	private final int ADD_VEHICLE_REQUEST = 19;
	private final int ADD_ENTERPRISEVEHICLE_REQUEST = 20;
	private final int GET_VEHICLE_DETAIL_REQUEST = 21;
	private final int ADD_VEHICLE_AUDIT = 22;

	// 上传图片的类型ID
	private final int ID_CODE_STATUS = 1; // 手持身份证
	private final int BUSINESS_LICENSE_STATUS = 2;// 营业执照
	private final int LICENSE_STATUS = 3; // 行驶证
	private final int VEHICLE_STATUS = 4; // 车辆

	// handler处理进度条
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case PRO:
				if (progress < NINE_PROGRESS) {
					progress++;
					progressBarHandle.setProgress(progress);
					float num = (float) progressBarHandle.getProgress()
							/ (float) progressBarHandle.getMax();
					int result = (int) (num * 100);
					myProssBarhandleText.setText("上传进度" + result + "%");
					// 延迟发送消息
					handler.sendEmptyMessageDelayed(PRO, 100);
				}

				break;
			case ALL:
				// 删除消息队列
				handler.removeMessages(PRO);
				progressBarHandle.setProgress(MAX_PROGRESS);
				myProssBarhandleText.setText("上传进度" + 100 + "%");
				break;
			case FAIL:
				// 删除消息队列
				handler.removeMessages(PRO);
				progress = 1;
				progressBarHandle.setProgress(MIN_PROGRESS);
				myProssBarhandleText.setText("上传进度" + 0 + "%");
				progressBarHandle.setVisibility(View.GONE);
				myProssBarhandleText.setVisibility(View.GONE);
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected int getLayoutId() {
		return R.layout.activity_personalvehicle;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		parentView = getLayoutInflater().inflate(
				R.layout.activity_personalvehicle, null);
		// 获取从验证车牌号码页面传过来的车牌号码、跟车电话、产权
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		if (bundle != null && bundle.containsKey("vehicleNo")) {
			vehicleNo = bundle.getString("vehicleNo");
		}

		if (bundle != null && bundle.containsKey("mVehicleType")) {
			mVehicleType = bundle.getInt("mVehicleType");
		}

		if (bundle != null && bundle.containsKey("vehicleCall")) {
			mVvehicleCall = bundle.getString("vehicleCall");
		}

		if (bundle != null && bundle.containsKey("vehicleID")) {
			mVehicleId = bundle.getInt("vehicleID");
		}

		// 请求当前车辆的详细信息
		getCurrentCar();

		mEtVehicleNo = (EditText) findViewById(R.id.et_newvehile_inputcarlicense);
		mIvWithcardlicense = (ImageView) findViewById(R.id.iv_newvehicle_withcardlicense);
		mIvDrivinglicense = (ImageView) findViewById(R.id.iv_newvehicle_drivinglicense);
		mIvInsuranceimg = (ImageView) findViewById(R.id.iv_newvehicle_insuranceimg);
		mBtnCommit = (Button) findViewById(R.id.bt_newvehile_commit);
		progressBarHandle = (ProgressBar) findViewById(R.id.pb_newvehicle_myProssBarhandle);
		myProssBarhandleText = (TextView) findViewById(R.id.tv_newvehicle_myProssBarhandleText);
		tvQuestion = (TextView) findViewById(R.id.tv_newvehicle_question);
		rbPersonal = (RadioButton) findViewById(R.id.rb_newvehile_personal);
		tvCertificateOne = (TextView) findViewById(R.id.tv_newvehicle_certificateOne);
		tv_newvehicle_xinxibiangeng = (TextView) findViewById(R.id.tv_newvehicle_xinxibiangeng);
		etNewVehileInputCarPhone = obtainView(R.id.et_newvehile_inputcarPhone);
		mIvWithcardStatus = obtainView(R.id.iv_newvehicle_withcard_status);
		mIvDrivingStatus = obtainView(R.id.iv_newvehicle_driving_status);
		mIvInsuranceStatus = obtainView(R.id.iv_newvehicle_insurance_status);

		// 设置车牌号码
		if (!TextUtils.isEmpty(vehicleNo) && vehicleNo != null) {
			mEtVehicleNo.setText(vehicleNo);
			// 设置输入车牌号码输入框不可获取焦点
			mEtVehicleNo.setFocusable(false);
		}

		// 设置跟车电话
		if (!TextUtils.isEmpty(mVvehicleCall) && mVvehicleCall != null) {
			etNewVehileInputCarPhone.setText(mVvehicleCall);
		}

		if (mVehicleType == 1) {
			// 个人产权
			rbPersonal.setText("个人产权车辆");
			tvCertificateOne.setText("身份证/驾驶证");
			mIvWithcardlicense.setImageResource(R.drawable.iv_withcard);
		} else if (mVehicleType == 2) {
			// 企业产权
			rbPersonal.setText("企业产权车辆");
			tvCertificateOne.setText("营业执照");
			tv_newvehicle_xinxibiangeng
					.setText(R.string.vehicle_addCompanyMsgOne);
			mIvWithcardlicense.setImageResource(R.drawable.iv_business);
		}

	}

	@Override
	protected void initData() {
	}

	// 获取当前车辆的信息
	private void getCurrentCar() {
		mGetVehicleDetailRequest = new GetVehicleDetailRequest(
				AddPersonalVehicleActivity.this, String.valueOf(mVehicleId));
		mGetVehicleDetailRequest.setRequestId(GET_VEHICLE_DETAIL_REQUEST);
		httpPost(mGetVehicleDetailRequest);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getCurrentCar();
	}

	@Override
	protected void setListener() {
		// 初始化点击事件
		initOnClick();
	}

	// 初始化titileBar的方法
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(
				R.string.tv_newvehicle_titlemsg));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				VehicleListActivity.invoke(AddPersonalVehicleActivity.this, "");
			}
		});

	}

	// 初始化点击事件的方法
	private void initOnClick() {
		mIvWithcardlicense.setOnClickListener(this);
		mIvDrivinglicense.setOnClickListener(this);
		mIvInsuranceimg.setOnClickListener(this);
		mBtnCommit.setOnClickListener(this);
		tvQuestion.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_newvehicle_withcardlicense:
			if (mVehicleType == 1) {
				// 上传手持身份证
				// showWithcardLicense();
				if (handIdCodeImgStatus != null
						|| !handIdCodeImgStatus.equals("2")) {
					// 跳转到上传照片页面
					UploadPictureActivity.invoke(
							AddPersonalVehicleActivity.this, "",
							ID_CODE_STATUS, mVehicleId);
				}
			} else if (mVehicleType == 2) {
				// 上传营业执照
				// showBusinessLicense();
				if (businessLicenseImgStatus != null
						|| !businessLicenseImgStatus.equals("2")) {
					// 跳转到上传照片页面
					UploadPictureActivity.invoke(
							AddPersonalVehicleActivity.this, "",
							BUSINESS_LICENSE_STATUS, mVehicleId);
				}
			}
			// setViewEnable(false);
			break;

		case R.id.iv_newvehicle_drivinglicense:
			// 上传行驶证
			// showDrivingLicense();
			if (vehicleLicenseImgStatus != null
					|| !vehicleLicenseImgStatus.equals("2")) {
				UploadPictureActivity.invoke(AddPersonalVehicleActivity.this,
						"", LICENSE_STATUS, mVehicleId);
			}
			// setViewEnable(false);
			break;

		case R.id.iv_newvehicle_insuranceimg:
			// 上传车辆照片
			// showInsuranceimg();
			if (vehicleImgStatus != null || !vehicleImgStatus.equals("2")) {
				UploadPictureActivity.invoke(AddPersonalVehicleActivity.this,
						"", VEHICLE_STATUS, mVehicleId);
			}
			// setViewEnable(false);
			break;

		case R.id.bt_newvehile_commit:
			// 申请车辆
			vehicleNo = mEtVehicleNo.getText().toString().trim();
			mVvehicleCall = etNewVehileInputCarPhone.getText().toString()
					.trim();

			Log.e("TAG", "跟车电话------------" + vehicleCall);

			// 上传个人产权
			if (TextUtils.isEmpty(vehicleNo)) {
				showToast("请输入车牌号");
				return;
			}

			if (!TextUtils.isEmpty(mVvehicleCall) && mVvehicleCall != null) {
				// 检测手机号码
				if (!mVvehicleCall.matches("^[1][3-8][0-9]{9}$")) {
					showToast("请输入正确的手机号码");
					return;
				}
			}

			if (vehicleOwnerType != null && vehicleOwnerType.equals("1")) {
				if (handIdCodeImgBase64 == null
						&& TextUtils.isEmpty(handIdCodeImgBase64)) {
					showToast("请上传手持身份证照片");
					return;
				}
			} else if (vehicleOwnerType != null && vehicleOwnerType.equals("2")) {
				if (businessLicenseImgBase64 == null
						&& TextUtils.isEmpty(businessLicenseImgBase64)) {
					showToast("请上传营业执照照片");
					return;
				}
			}

			if (vehicleLicenseImgUrl == null
					&& TextUtils.isEmpty(vehicleLicenseImgUrl)) {
				showToast("请上传行驶证照片");
				return;
			}
			if (vehicleImgBase64 == null && TextUtils.isEmpty(vehicleImgBase64)) {
				showToast("请上传车辆照片");
				return;
			}

			// 请求服务器申请审核车辆
			addVehicleAudit();

			setViewShenqingEnable(false);
			// addPersonalVehicle(vehicleNo, imgWithCardBase64,
			// withCardImgName, imgDrivingBase64, drivingImgName,
			// imgInsuranceBase64, vehicleImgName, mVvehicleCall);
			// } else if (mVehicleType == 2) {
			// // 上传企业产权
			// if (TextUtils.isEmpty(vehicleNo)) {
			// showToast("请输入车牌号");
			// return;
			// }
			//
			// if (!TextUtils.isEmpty(mVvehicleCall) && mVvehicleCall != null) {
			// // 检测手机号码
			// if (!mVvehicleCall.matches("^[1][3-8][0-9]{9}$")) {
			// showToast("请输入正确的手机号码");
			// return;
			// }
			// }
			//
			// if (TextUtils.isEmpty(businessLicenseImgBase64)) {
			// showToast("请上传营业执照照片");
			// return;
			// }
			// if (TextUtils.isEmpty(vehicleLicenseImgUrl)) {
			// showToast("请上传行驶证照片");
			// return;
			// }
			// if (TextUtils.isEmpty(vehicleImgBase64)) {
			// showToast("请上传车辆照片");
			// return;
			// }
			//
			// // addEnterpriseVehicle(vehicleNo, imgBusinessBase64,
			// // businessImgName, imgDrivingBase64, drivingImgName,
			// // imgInsuranceBase64, vehicleImgName, mVvehicleCall);
			//
			// }

			break;

		case R.id.tv_newvehicle_question:
			// 跳转到帮助页面
			HelpActivity.invoke(AddPersonalVehicleActivity.this, "iscom");

			break;

		default:
			break;
		}
	}

	/**
	 * @Description 提交车辆申请审核
	 * @Title:addVehicleAudit
	 * @return:void
	 * @throws
	 * @Create: 2016-5-30 上午10:29:04
	 * @Author : zhm
	 */
	private void addVehicleAudit() {
		mAddVehicleAuditRequest = new AddVehicleAuditRequest(
				AddPersonalVehicleActivity.this, mVehicleId);
		mAddVehicleAuditRequest.setRequestId(ADD_VEHICLE_AUDIT);
		httpPost(mAddVehicleAuditRequest);
	}

	// 请求服务器申请企业车辆认证
	// private void addEnterpriseVehicle(String vehicleNo,
	// String imgBusinessBase64, String businessImgName,
	// String imgDrivingBase64, String drivingImgName,
	// String imgInsuranceBase64, String vehicleImgName, String vehicleCall) {
	// mAddEnterpriseVehicleRequest = new AddEnterpriseVehicleRequest(
	// AddPersonalVehicleActivity.this, vehicleNo, imgBusinessBase64,
	// businessImgName, imgDrivingBase64, drivingImgName,
	// imgInsuranceBase64, vehicleImgName, vehicleCall);
	//
	// mAddEnterpriseVehicleRequest
	// .setRequestId(ADD_ENTERPRISEVEHICLE_REQUEST);
	// httpPost(mAddEnterpriseVehicleRequest);
	// // 设置按钮不可点击
	// setViewEnable(false);
	// // 显示进度条
	// progressBarHandle.setVisibility(View.VISIBLE);
	// myProssBarhandleText.setVisibility(View.VISIBLE);
	// progressBarHandle.setMax(MAX_PROGRESS);// 设置最大进度
	//
	// progress = (progress > 0) ? progress : 0;
	// progressBarHandle.setProgress(progress);
	// handler.sendEmptyMessage(PRO);// 发送空消息
	//
	// }

	// 请求服务器申请个人车辆认证
	// private void addPersonalVehicle(String vehicleNo,
	// String handIdCodeImgBase64, String handIdCodeImgName,
	// String vehicleLicenseImgUrl, String vehicleLicenseImgName,
	// String vehicleImgBase64, String vehicleImgName, String vehicleCall) {
	//
	// Log.e("TAG", "-----------------------" + handIdCodeImgBase64.toString());
	//
	// mAddVehicleRequest = new AddPersonalVehicleRequest(
	// AddPersonalVehicleActivity.this, vehicleNo,
	// handIdCodeImgBase64, handIdCodeImgName, vehicleLicenseImgUrl,
	// vehicleLicenseImgName, vehicleImgBase64, vehicleImgName,
	// vehicleCall);
	// mAddVehicleRequest.setRequestId(ADD_VEHICLE_REQUEST);
	// httpPost(mAddVehicleRequest);
	// // 设置按钮不可点击
	// setViewEnable(false);
	// // 显示进度条
	// progressBarHandle.setVisibility(View.VISIBLE);
	// myProssBarhandleText.setVisibility(View.VISIBLE);
	// progressBarHandle.setMax(MAX_PROGRESS);// 设置最大进度
	//
	// progress = (progress > 0) ? progress : 0;
	// progressBarHandle.setProgress(progress);
	// handler.sendEmptyMessage(PRO);// 发送空消息
	//
	// }

	// 设置按钮不可重复点击
	private void setViewEnable(boolean bEnable) {
		// if (bEnable) {
		// mBtnCommit.setText("提交");
		// } else {
		// mBtnCommit.setText("提交中 ...");
		// }
		// mBtnCommit.setEnabled(bEnable);
		mIvWithcardlicense.setEnabled(bEnable);
		mIvDrivinglicense.setEnabled(bEnable);
		mIvInsuranceimg.setEnabled(bEnable);

	}

	// 设置申请按钮不可点击
	private void setViewShenqingEnable(boolean bEnable) {
		if (bEnable) {
			mBtnCommit.setText("提交");
		} else {
			mBtnCommit.setText("提交中 ...");
		}
		mBtnCommit.setEnabled(bEnable);
	}

	// 营业执照的方法
	// private void showBusinessLicense() {
	// businessPopupWindow = new PopupWindow(AddPersonalVehicleActivity.this);
	// View businessPhoto = getLayoutInflater().inflate(
	// R.layout.item_businessphoto_popupwindows, null);
	// businessPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
	// businessPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
	// businessPopupWindow.setBackgroundDrawable(new BitmapDrawable());
	// businessPopupWindow.setFocusable(true);
	// businessPopupWindow.setOutsideTouchable(true);
	// businessPopupWindow.setContentView(businessPhoto);
	// businessPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
	//
	// Button business_photo = (Button) businessPhoto
	// .findViewById(R.id.item_popupwindows_businessphoto);
	// Button business_select = (Button) businessPhoto
	// .findViewById(R.id.item_popupwindows_businessselect);
	// Button business_cancel = (Button) businessPhoto
	// .findViewById(R.id.item_popupwindows_businesscancel);
	//
	// // 营业执照拍照
	// business_photo.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// PickImage.pickImageFromCamera(AddPersonalVehicleActivity.this,
	// personalVehicleTempFileDir + "Businesslicense.jpg", 7);
	// businessPopupWindow.dismiss();
	// }
	// });
	// // 营业执照图库选择
	// business_select.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// PickImage
	// .pickImageFromPhoto(AddPersonalVehicleActivity.this, 8);
	// businessPopupWindow.dismiss();
	// }
	// });
	// // 营业执照取消
	// business_cancel.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// businessPopupWindow.dismiss();
	// }
	// });
	// }
	//
	// // 上传车辆照片的方法
	// private void showInsuranceimg() {
	// insurancePopupWindow = new PopupWindow(AddPersonalVehicleActivity.this);
	// View insurancePhoto = getLayoutInflater().inflate(
	// R.layout.item_insuranceimgphoto_popupwindows, null);
	// insurancePopupWindow.setWidth(LayoutParams.MATCH_PARENT);
	// insurancePopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
	// insurancePopupWindow.setBackgroundDrawable(new BitmapDrawable());
	// insurancePopupWindow.setFocusable(true);
	// insurancePopupWindow.setOutsideTouchable(true);
	// insurancePopupWindow.setContentView(insurancePhoto);
	// insurancePopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
	//
	// Button insurance_photo = (Button) insurancePhoto
	// .findViewById(R.id.item_popupwindows_insuranceimgphoto);
	// Button insurance_select = (Button) insurancePhoto
	// .findViewById(R.id.item_popupwindows_insuranceimgselect);
	// Button insurance_cancel = (Button) insurancePhoto
	// .findViewById(R.id.item_popupwindows_insuranceimgcancel);
	//
	// // 车辆拍照
	// insurance_photo.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// PickImage.pickImageFromCamera(AddPersonalVehicleActivity.this,
	// personalVehicleTempFileDir + "Vehiclelicense.jpg", 5);
	// insurancePopupWindow.dismiss();
	// }
	// });
	//
	// // 车辆图库选图
	// insurance_select.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// PickImage
	// .pickImageFromPhoto(AddPersonalVehicleActivity.this, 6);
	// insurancePopupWindow.dismiss();
	// }
	// });
	//
	// // 车辆取消
	// insurance_cancel.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// insurancePopupWindow.dismiss();
	// }
	// });
	//
	// }
	//
	// // 上传行驶证的方法
	// private void showDrivingLicense() {
	// drivingPopupWindow = new PopupWindow(AddPersonalVehicleActivity.this);
	// View drivingPhoto = getLayoutInflater().inflate(
	// R.layout.item_drivingphoto_popupwindows, null);
	// drivingPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
	// drivingPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
	// drivingPopupWindow.setBackgroundDrawable(new BitmapDrawable());
	// drivingPopupWindow.setFocusable(true);
	// drivingPopupWindow.setOutsideTouchable(true);
	// drivingPopupWindow.setContentView(drivingPhoto);
	// drivingPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
	//
	// Button driving_photo = (Button) drivingPhoto
	// .findViewById(R.id.item_popupwindows_drivingphoto);
	// Button driving_select = (Button) drivingPhoto
	// .findViewById(R.id.item_popupwindows_drivingselect);
	// Button driving_cancel = (Button) drivingPhoto
	// .findViewById(R.id.item_popupwindows_drivingcancel);
	//
	// // 行驶证拍照
	// driving_photo.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// PickImage.pickImageFromCamera(AddPersonalVehicleActivity.this,
	// personalVehicleTempFileDir + "Drivinglicense.jpg", 3);
	// drivingPopupWindow.dismiss();
	// }
	// });
	//
	// // 行驶证选图
	// driving_select.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// PickImage
	// .pickImageFromPhoto(AddPersonalVehicleActivity.this, 4);
	// drivingPopupWindow.dismiss();
	// }
	// });
	//
	// // 行驶证取消
	// driving_cancel.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// drivingPopupWindow.dismiss();
	// }
	// });
	//
	// }
	//
	// // 上传手持身份证的方法
	// private void showWithcardLicense() {
	// withcardPopupWindow = new PopupWindow(AddPersonalVehicleActivity.this);
	// View withcardPhoto = getLayoutInflater().inflate(
	// R.layout.item_withcardphoto_popupwindows, null);
	// withcardPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
	// withcardPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
	// withcardPopupWindow.setBackgroundDrawable(new BitmapDrawable());
	// withcardPopupWindow.setFocusable(true);
	// withcardPopupWindow.setOutsideTouchable(true);
	// withcardPopupWindow.setContentView(withcardPhoto);
	// withcardPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
	//
	// Button withcard_photo = (Button) withcardPhoto
	// .findViewById(R.id.item_popupwindows_withcardphoto);
	// Button withcard_select = (Button) withcardPhoto
	// .findViewById(R.id.item_popupwindows_withcardselect);
	// Button withcard_cancel = (Button) withcardPhoto
	// .findViewById(R.id.item_popupwindows_withcardcancel);
	//
	// // 手持身份证拍照
	// withcard_photo.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// PickImage.pickImageFromCamera(AddPersonalVehicleActivity.this,
	// personalVehicleTempFileDir + "WithCardlicense.jpg", 1);
	// withcardPopupWindow.dismiss();
	// }
	// });
	// // 手持身份证图库选择
	// withcard_select.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// PickImage
	// .pickImageFromPhoto(AddPersonalVehicleActivity.this, 2);
	// withcardPopupWindow.dismiss();
	// }
	// });
	// // 手持身份证取消
	// withcard_cancel.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// withcardPopupWindow.dismiss();
	// }
	// });
	//
	// }
	//
	// // 接收返回结果
	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// switch (requestCode) {
	// case 1:
	// // 手持身份证拍照返回
	// if (resultCode == RESULT_OK) {
	// withCardimageFile = new File(personalVehicleTempFileDir
	// + "WithCardlicense.jpg");
	// }
	// break;
	//
	// case 2:
	// // 手持身份证图库返回
	// if (resultCode == RESULT_OK) {
	// Uri uri = data.getData();
	// String realPathFromURI = FileUtils
	// .getRealPathFromURI(uri, this);
	// if (TextUtils.isEmpty(realPathFromURI)) {
	// withCardimageFile = null;
	// } else {
	// withCardimageFile = new File(personalVehicleTempFileDir
	// + "WithCardlicense.jpg");
	// File temFile = new File(realPathFromURI);
	//
	// FileUtils.copyFile(temFile, withCardimageFile);
	// }
	// }
	// break;
	//
	// case 3:
	// // 行驶证拍照返回
	// if (resultCode == RESULT_OK) {
	// drivingimageFile = new File(personalVehicleTempFileDir
	// + "Drivinglicense.jpg");
	// }
	// break;
	//
	// case 4:
	// // 行驶证图库返回
	// if (resultCode == RESULT_OK) {
	// Uri uri = data.getData();
	// String realPathFromURI = FileUtils
	// .getRealPathFromURI(uri, this);
	// if (TextUtils.isEmpty(realPathFromURI)) {
	// drivingimageFile = null;
	// } else {
	// drivingimageFile = new File(personalVehicleTempFileDir
	// + "Drivinglicense.jpg");
	// File temFile = new File(realPathFromURI);
	//
	// FileUtils.copyFile(temFile, drivingimageFile);
	// }
	// }
	// break;
	//
	// case 5:
	// // 车辆照片拍照返回
	// if (resultCode == RESULT_OK) {
	// vehicleimageFile = new File(personalVehicleTempFileDir
	// + "Vehiclelicense.jpg");
	// }
	// break;
	//
	// case 6:
	// // 车辆照片图库返回
	// if (resultCode == RESULT_OK) {
	// Uri uri = data.getData();
	// String realPathFromURI = FileUtils
	// .getRealPathFromURI(uri, this);
	// if (TextUtils.isEmpty(realPathFromURI)) {
	// vehicleimageFile = null;
	// } else {
	// vehicleimageFile = new File(personalVehicleTempFileDir
	// + "Vehiclelicense.jpg");
	// File temFile = new File(realPathFromURI);
	//
	// FileUtils.copyFile(temFile, vehicleimageFile);
	// }
	// }
	// break;
	//
	// case 7:
	// // 营业执照拍照返回
	// if (resultCode == RESULT_OK) {
	// businessimageFile = new File(personalVehicleTempFileDir
	// + "Businesslicense.jpg");
	// }
	// break;
	//
	// case 8:
	// // 营业执照图库返回
	// if (resultCode == RESULT_OK) {
	// Uri uri = data.getData();
	// String realPathFromURI = FileUtils
	// .getRealPathFromURI(uri, this);
	// if (TextUtils.isEmpty(realPathFromURI)) {
	// businessimageFile = null;
	// } else {
	// businessimageFile = new File(personalVehicleTempFileDir
	// + "Businesslicense.jpg");
	// File temFile = new File(realPathFromURI);
	//
	// FileUtils.copyFile(temFile, businessimageFile);
	// }
	// }
	// break;
	//
	// default:
	// break;
	// }
	//
	// if (requestCode == 1 || requestCode == 2) {
	// // 判断本地文件是否存在
	// if (withCardimageFile == null) {
	// return;
	// }
	//
	// // TODO--
	// byte[] bytes = handleImage(withCardimageFile, 800, 800);
	//
	// if (bytes == null || bytes.length == 0) {
	// return;
	// }
	// int degree = ImageUtil.readPictureDegree(withCardimageFile
	// .getAbsolutePath());
	//
	// imgWithCardBase64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
	// Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	// ImageUtil.rotaingImageView(degree, bm);
	// // 显示拍照或选图后的手持身份证
	// if (bm != null) {
	// mIvWithcardlicense.setImageBitmap(bm);
	// }
	//
	// // base64解码
	// byte[] decode = Base64.decode(imgWithCardBase64, Base64.NO_WRAP);
	//
	// // TODO--将图片保存在SD卡
	// BufferedOutputStream bos = null;
	// FileOutputStream fos = null;
	// File file = new File(personalVehicleTempFileDir
	// + "imgBaseWitchCar.jpg");
	// try {
	// if (file.exists()) {
	// file.createNewFile();
	// }
	// fos = new FileOutputStream(file);
	// bos = new BufferedOutputStream(fos);
	// bos.write(decode);
	// fos.flush();
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// if (bos != null) {
	// try {
	// bos.close();
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	// }
	// if (fos != null) {
	// try {
	// fos.close();
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	// }
	// }
	//
	// }
	//
	// if (requestCode == 3 || requestCode == 4) {
	// // 判断本地文件是否存在
	// if (drivingimageFile == null) {
	// return;
	// }
	//
	// // TODO--
	// byte[] bytes = handleImage(drivingimageFile, 800, 800);
	//
	// if (bytes == null || bytes.length == 0) {
	// return;
	// }
	// int degree = ImageUtil.readPictureDegree(drivingimageFile
	// .getAbsolutePath());
	//
	// imgDrivingBase64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
	// Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	// ImageUtil.rotaingImageView(degree, bm);
	// // 显示拍照或选图后的行驶证
	// if (bm != null) {
	// mIvDrivinglicense.setImageBitmap(bm);
	// }
	//
	// // base64解码
	// byte[] decode = Base64.decode(imgDrivingBase64, Base64.NO_WRAP);
	//
	// // TODO--将图片保存在SD卡
	// BufferedOutputStream bos = null;
	// FileOutputStream fos = null;
	// File file = new File(personalVehicleTempFileDir
	// + "imgBaseDriving.jpg");
	// try {
	// if (file.exists()) {
	// file.createNewFile();
	// }
	// fos = new FileOutputStream(file);
	// bos = new BufferedOutputStream(fos);
	// bos.write(decode);
	// fos.flush();
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// if (bos != null) {
	// try {
	// bos.close();
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	// }
	// if (fos != null) {
	// try {
	// fos.close();
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	// }
	// }
	//
	// }
	//
	// if (requestCode == 5 || requestCode == 6) {
	// // 判断本地文件是否存在
	// if (vehicleimageFile == null) {
	// return;
	// }
	//
	// // TODO--
	// byte[] bytes = handleImage(vehicleimageFile, 800, 800);
	//
	// if (bytes == null || bytes.length == 0) {
	// return;
	// }
	// int degree = ImageUtil.readPictureDegree(vehicleimageFile
	// .getAbsolutePath());
	//
	// imgInsuranceBase64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
	// Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	// ImageUtil.rotaingImageView(degree, bm);
	//
	// // 显示拍照后选图后的车辆照片
	// if (bm != null) {
	// mIvInsuranceimg.setImageBitmap(bm);
	// }
	//
	// // base64解码
	// byte[] decode = Base64.decode(imgInsuranceBase64, Base64.NO_WRAP);
	//
	// // TODO--将图片保存在SD卡
	// BufferedOutputStream bos = null;
	// FileOutputStream fos = null;
	// File file = new File(personalVehicleTempFileDir
	// + "imgBaseInsurance.jpg");
	// try {
	// if (file.exists()) {
	// file.createNewFile();
	// }
	// fos = new FileOutputStream(file);
	// bos = new BufferedOutputStream(fos);
	// bos.write(decode);
	// fos.flush();
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// if (bos != null) {
	// try {
	// bos.close();
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	// }
	// if (fos != null) {
	// try {
	// fos.close();
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	// }
	// }
	//
	// }
	//
	// if (requestCode == 7 || requestCode == 8) {
	// // 判断本地文件是否存在
	// if (businessimageFile == null) {
	// return;
	// }
	//
	// // TODO--
	// byte[] bytes = handleImage(businessimageFile, 800, 800);
	//
	// if (bytes == null || bytes.length == 0) {
	// return;
	// }
	// int degree = ImageUtil.readPictureDegree(businessimageFile
	// .getAbsolutePath());
	//
	// imgBusinessBase64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
	// Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	// ImageUtil.rotaingImageView(degree, bm);
	//
	// // 显示拍照后选图后的营业执照照片
	// if (bm != null) {
	// mIvWithcardlicense.setImageBitmap(bm);
	// }
	//
	// // base64解码
	// byte[] decode = Base64.decode(imgBusinessBase64, Base64.NO_WRAP);
	//
	// // TODO--将图片保存在SD卡
	// BufferedOutputStream bos = null;
	// FileOutputStream fos = null;
	// File file = new File(personalVehicleTempFileDir
	// + "imgBaseBusiness.jpg");
	// try {
	// if (file.exists()) {
	// file.createNewFile();
	// }
	// fos = new FileOutputStream(file);
	// bos = new BufferedOutputStream(fos);
	// bos.write(decode);
	// fos.flush();
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// if (bos != null) {
	// try {
	// bos.close();
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	// }
	// if (fos != null) {
	// try {
	// fos.close();
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	// }
	// }
	//
	// }
	//
	// }

	/**
	 * 处理图片
	 * 
	 * @param avatarFile
	 * @return
	 */
	// private byte[] handleImage(File avatarFile, int out_Width, int
	// out_Height) {
	// if (avatarFile.exists()) { // 本地文件存在
	// BitmapFactory.Options options = new BitmapFactory.Options();
	// options.inJustDecodeBounds = true;
	// // 获取这个图片原始的宽和高 在outHeight 及 outWidth
	// Bitmap bm = BitmapFactory.decodeFile(avatarFile.getPath(), options);
	//
	// // 此时返回bm为空
	// // 我们要得到高及宽都不超过W H的缩略图
	// int zW = options.outWidth / out_Width;
	// int zH = options.outHeight / out_Height;
	// int be = zH;
	// if (zW > be)
	// be = zW;
	// if (be == 0)
	// be = 1;
	// options.inSampleSize = be;
	// options.inJustDecodeBounds = false;
	// bm = BitmapFactory.decodeFile(avatarFile.getPath(), options);
	//
	// ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	//
	// if (bm == null) {
	// return null;
	// }
	// bm.copy(Bitmap.Config.ARGB_8888, false);
	//
	// // TODO--
	// bm.compress(
	// avatarFile.getAbsolutePath().endsWith("jpg") ? Bitmap.CompressFormat.JPEG
	// : Bitmap.CompressFormat.PNG, 50, outputStream);
	//
	// return outputStream.toByteArray();
	//
	// }
	// return null;
	// }

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess;
		String message;
		switch (requestId) {
		case ADD_VEHICLE_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				// 新增个人车辆成功

				// 删除存放图片文件夹
				withCardimageFile = new File(personalVehicleTempFileDir
						+ "WithCardlicense.jpg");
				if (withCardimageFile.exists() && withCardimageFile.isFile()) {
					withCardimageFile.delete();
				}

				drivingimageFile = new File(personalVehicleTempFileDir
						+ "Drivinglicense.jpg");
				if (drivingimageFile.exists() && drivingimageFile.isFile()) {
					drivingimageFile.delete();
				}

				vehicleimageFile = new File(personalVehicleTempFileDir
						+ "Vehiclelicense.jpg");
				if (vehicleimageFile.exists() && vehicleimageFile.isFile()) {
					vehicleimageFile.delete();
				}

				handler.sendEmptyMessage(ALL);// 发送空消息

				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 关闭当前页面
				AddPersonalVehicleActivity.this.finish();
			} else {
				// 新增个人车辆失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				handler.sendEmptyMessage(FAIL);// 发送空消息
				// 设置按钮可点击
				setViewEnable(true);
			}

			break;
		case ADD_ENTERPRISEVEHICLE_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				// 新增企业车辆成功

				// 删除存放图片文件夹
				businessimageFile = new File(personalVehicleTempFileDir
						+ "Businesslicense.jpg");
				if (businessimageFile.exists() && businessimageFile.isFile()) {
					businessimageFile.delete();
				}

				drivingimageFile = new File(personalVehicleTempFileDir
						+ "Drivinglicense.jpg");
				if (drivingimageFile.exists() && drivingimageFile.isFile()) {
					drivingimageFile.delete();
				}

				vehicleimageFile = new File(personalVehicleTempFileDir
						+ "Vehiclelicense.jpg");
				if (vehicleimageFile.exists() && vehicleimageFile.isFile()) {
					vehicleimageFile.delete();
				}

				handler.sendEmptyMessage(ALL);// 发送空消息

				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 关闭当前页面
				AddPersonalVehicleActivity.this.finish();
			} else {
				// 新增企业车辆失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				handler.sendEmptyMessage(FAIL);// 发送空消息
				// 设置按钮可点击
				setViewEnable(true);
			}

			break;

		case GET_VEHICLE_DETAIL_REQUEST: // 获取当前车辆的详细信息
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				// 获取车辆信息成功
				VehicleDetailInfo carDetailInfo = (VehicleDetailInfo) response.singleData;
				mVehicleId = Integer.parseInt(carDetailInfo.id); // 车辆ID
				vehicleNo = carDetailInfo.vehicleNo;// 车牌号
				vehicleStatus = carDetailInfo.vehicleStatus;// 车辆状态显示值
				vehicleOwnerType = carDetailInfo.vehicleOwnerType;// 车辆所属人类型
				handIdCodeImgBase64 = carDetailInfo.handIdCodeImgBase64;// 手持身份证URL
				businessLicenseImgBase64 = carDetailInfo.businessLicenseImgBase64;// 营业执照URL
				vehicleLicenseImgUrl = carDetailInfo.vehicleLicenseImgUrl;// 行驶证图片URL
				vehicleImgBase64 = carDetailInfo.vehicleImgBase64;// 车辆图片URL

				handIdCodeImgStatus = carDetailInfo.handIdCodeImgStatus;// 手持身份证图片状态
				businessLicenseImgStatus = carDetailInfo.businessLicenseImgStatus;// 营业执照图片状态
				vehicleLicenseImgStatus = carDetailInfo.vehicleLicenseImgStatus;// 行驶证图片状态
				vehicleImgStatus = carDetailInfo.vehicleImgStatus;// 车辆图片状态

				if (vehicleOwnerType != null && vehicleOwnerType.equals("1")) {
					// 个人产权
					rbPersonal.setText("个人产权车辆");
					tvCertificateOne.setText("身份证/驾驶证");
					if (!TextUtils.isEmpty(handIdCodeImgBase64)
							&& handIdCodeImgBase64 != null) {
						if (handIdCodeImgStatus != null
								|| handIdCodeImgStatus.equals("1")) {
							ImageLoader.getInstance().displayImage(
									handIdCodeImgBase64, mIvWithcardlicense,
									ImageLoaderOptions.options);
							mIvWithcardStatus
									.setImageResource(R.drawable.short_dengdai);
						} else if (handIdCodeImgStatus != null
								|| handIdCodeImgStatus.equals("2")) {
							ImageLoader.getInstance().displayImage(
									handIdCodeImgBase64, mIvWithcardlicense,
									ImageLoaderOptions.options);
							mIvWithcardStatus
									.setImageResource(R.drawable.carbasic_min_ok);
						} else if (handIdCodeImgStatus != null
								|| handIdCodeImgStatus.equals("3")) {
							ImageLoader.getInstance().displayImage(
									handIdCodeImgBase64, mIvWithcardlicense,
									ImageLoaderOptions.options);
							mIvWithcardStatus
									.setImageResource(R.drawable.short_bohui);
						} else if (handIdCodeImgStatus != null
								|| handIdCodeImgStatus.equals("4")) {
							ImageLoader.getInstance().displayImage(
									handIdCodeImgBase64, mIvWithcardlicense,
									ImageLoaderOptions.options);
							mIvWithcardStatus
									.setImageResource(R.drawable.carbasic_min_overdue);
						}
					} else {
						mIvWithcardlicense
								.setImageResource(R.drawable.iv_withcard);
					}
				} else if (vehicleOwnerType != null
						&& vehicleOwnerType.equals("2")) {
					// 企业产权
					rbPersonal.setText("企业产权车辆");
					tvCertificateOne.setText("营业执照");
					tv_newvehicle_xinxibiangeng
							.setText(R.string.vehicle_addCompanyMsgOne);

					if (!TextUtils.isEmpty(businessLicenseImgBase64)
							&& businessLicenseImgBase64 != null) {
						if (businessLicenseImgStatus != null
								|| businessLicenseImgStatus.equals("1")) {
							ImageLoader.getInstance().displayImage(
									businessLicenseImgBase64,
									mIvWithcardlicense,
									ImageLoaderOptions.options);
							mIvWithcardStatus
									.setImageResource(R.drawable.short_dengdai);
						} else if (businessLicenseImgStatus != null
								|| businessLicenseImgStatus.equals("2")) {
							ImageLoader.getInstance().displayImage(
									businessLicenseImgBase64,
									mIvWithcardlicense,
									ImageLoaderOptions.options);
							mIvWithcardStatus
									.setImageResource(R.drawable.carbasic_min_ok);
						} else if (businessLicenseImgStatus != null
								|| businessLicenseImgStatus.equals("3")) {
							ImageLoader.getInstance().displayImage(
									businessLicenseImgBase64,
									mIvWithcardlicense,
									ImageLoaderOptions.options);
							mIvWithcardStatus
									.setImageResource(R.drawable.short_bohui);
						} else if (businessLicenseImgStatus != null
								|| businessLicenseImgStatus.equals("4")) {
							ImageLoader.getInstance().displayImage(
									businessLicenseImgBase64,
									mIvWithcardlicense,
									ImageLoaderOptions.options);
							mIvWithcardStatus
									.setImageResource(R.drawable.carbasic_min_overdue);
						}
					} else {
						mIvWithcardlicense
								.setImageResource(R.drawable.iv_business);
					}
				}
				if (!TextUtils.isEmpty(vehicleLicenseImgUrl)
						&& vehicleLicenseImgUrl != null) {
					if (vehicleLicenseImgStatus != null
							|| vehicleLicenseImgStatus.equals("1")) {
						ImageLoader.getInstance().displayImage(
								vehicleLicenseImgUrl, mIvDrivinglicense,
								ImageLoaderOptions.options);
						mIvDrivingStatus
								.setImageResource(R.drawable.short_dengdai);
					} else if (vehicleLicenseImgStatus != null
							|| vehicleLicenseImgStatus.equals("2")) {
						ImageLoader.getInstance().displayImage(
								vehicleLicenseImgUrl, mIvDrivinglicense,
								ImageLoaderOptions.options);
						mIvDrivingStatus
								.setImageResource(R.drawable.carbasic_min_ok);
					} else if (vehicleLicenseImgStatus != null
							|| vehicleLicenseImgStatus.equals("3")) {
						ImageLoader.getInstance().displayImage(
								vehicleLicenseImgUrl, mIvDrivinglicense,
								ImageLoaderOptions.options);
						mIvDrivingStatus
								.setImageResource(R.drawable.short_bohui);
					} else if (vehicleLicenseImgStatus != null
							|| vehicleLicenseImgStatus.equals("4")) {
						ImageLoader.getInstance().displayImage(
								vehicleLicenseImgUrl, mIvDrivinglicense,
								ImageLoaderOptions.options);
						mIvDrivingStatus
								.setImageResource(R.drawable.carbasic_min_overdue);
					}
				} else {
					mIvDrivinglicense
							.setImageResource(R.drawable.iv_driverlicense);
				}

				if (!TextUtils.isEmpty(vehicleImgBase64)
						&& vehicleImgBase64 != null) {
					if (vehicleImgStatus != null
							|| vehicleImgStatus.equals("1")) {
						ImageLoader.getInstance().displayImage(
								vehicleImgBase64, mIvInsuranceimg,
								ImageLoaderOptions.options);
						mIvInsuranceStatus
								.setImageResource(R.drawable.long_dengdai);
					} else if (vehicleImgStatus != null
							|| vehicleImgStatus.equals("2")) {
						ImageLoader.getInstance().displayImage(
								vehicleImgBase64, mIvInsuranceimg,
								ImageLoaderOptions.options);
						mIvInsuranceStatus
								.setImageResource(R.drawable.carbasic_max_ok);
					} else if (vehicleImgStatus != null
							|| vehicleImgStatus.equals("3")) {
						ImageLoader.getInstance().displayImage(
								vehicleImgBase64, mIvInsuranceimg,
								ImageLoaderOptions.options);
						mIvInsuranceStatus
								.setImageResource(R.drawable.long_bohui);
					} else if (vehicleImgStatus != null
							|| vehicleImgStatus.equals("4")) {
						ImageLoader.getInstance().displayImage(
								vehicleImgBase64, mIvInsuranceimg,
								ImageLoaderOptions.options);
						mIvInsuranceStatus
								.setImageResource(R.drawable.carbasic_max_overdue);
					}
				} else {
					mIvInsuranceimg
							.setImageResource(R.drawable.iv_insuranceimg);
				}
			}

			break;

		case ADD_VEHICLE_AUDIT: // 提交车辆申请审核
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				showToast(message);
				// 关闭当前页面
				VehicleListActivity.invoke(AddPersonalVehicleActivity.this, "");
			} else {
				showToast(message);
			}
			setViewEnable(true);
			break;

		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(getString(R.string.net_error_toast));
		// 设置申请按钮可点击
		setViewEnable(true);
		handler.sendEmptyMessage(FAIL);// 发送空消息
	}

	// 本界面的跳转方法
	public static void invoke(Context activity, String vehicleNo,
			int mVehicleType, String vehicleCall, int vehicleID) {
		Intent intent = new Intent();
		intent.setClass(activity, AddPersonalVehicleActivity.class);
		intent.putExtra("vehicleNo", vehicleNo);
		intent.putExtra("mVehicleType", mVehicleType);
		intent.putExtra("vehicleCall", vehicleCall);
		intent.putExtra("vehicleID", vehicleID);
		activity.startActivity(intent);
	}

	// 对返回键的监听
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			VehicleListActivity.invoke(AddPersonalVehicleActivity.this, null);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
