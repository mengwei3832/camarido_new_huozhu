package com.yunqi.clientandroid.activity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.UserInfo;
import com.yunqi.clientandroid.entity.VehicleType;
import com.yunqi.clientandroid.http.request.AddVehicleFastRequest;
import com.yunqi.clientandroid.http.request.GetUserInfoRequest;
import com.yunqi.clientandroid.http.request.VehicleTypeRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.AllCapTransformationMethod;
import com.yunqi.clientandroid.utils.FileUtils;
import com.yunqi.clientandroid.utils.ImageUtil;
import com.yunqi.clientandroid.utils.PickImage;
import com.yunqi.clientandroid.utils.StringUtils;
import com.yunqi.clientandroid.view.wheel.adapters.AbstractWheelTextAdapter;
import com.yunqi.clientandroid.view.wheel.views.OnWheelChangedListener;
import com.yunqi.clientandroid.view.wheel.views.OnWheelScrollListener;
import com.yunqi.clientandroid.view.wheel.views.WheelView;

/**
 * @deprecated:快速新增车辆提交车辆信息
 */
public class FastAddVehicleActivity extends BaseActivity implements
		OnClickListener {

	private View parentView;
	private ProgressBar progressBarHandle;
	private TextView myProssBarhandleText;
	private Button mBtnApply;
	private EditText mEtVehicleNo;
	private EditText mEtVehicleEngineCode;
	private EditText mEtVehicleIdCard;
	private EditText mEtVehicleName;
	private TextView mTvVehicleType;
	private ImageView mIvDrivinglicense;
	private ImageView mIvOperatinglicense;
	private ImageView mIvInsuranceimg;
	private LinearLayout mLlGlobal;
	private ProgressBar mProgress;
	private PopupWindow drivingPopupWindow;
	private PopupWindow operatingPopupWindow;
	private PopupWindow insurancePopupWindow;

	// 存放图片文件夹的统一路径
	private String driverTempFileDir = Environment
			.getExternalStorageDirectory() + "/";
	// 行驶证存放图片文件夹的路径
	private String drivingImgName = "Drivinglicense.jpg";// 行驶证图片名
	private File drivingimageFile = null;
	private String imgDrivingBase64;// 存放行驶证图片的字符串

	// 运营证存放图片文件夹的路径
	private String operatingImgName = "Operatinglicense.jpg";// 运营证图片名
	private File operatingimageFile = null;
	private String imgOperatingBase64;// 存放运营证图片的字符串

	// 车辆存放图片文件夹的路径
	private String vehicleImgName = "Vehiclelicense.jpg";// 车辆图片名
	private File vehicleimageFile = null;
	private String imgInsuranceBase64;// 存放车辆图片的字符串

	private List<VehicleType> carTypeData;// 存放车辆类型的集合
	private ArrayList<String> listcarType = new ArrayList<String>();// 存放车辆类型字符串的集合
	private ArrayList<Integer> listcarTypeNum = new ArrayList<Integer>();// 存放车辆类型数字的集合
	private String vehicleTypeId = "500";// 访问车辆类型的参数为500
	private int mVehicleType;// 车辆类型的id
	private String vehicleNo;// 车牌号码
	private String vehicleEngineCode;// 发动机型号
	private String vehicleType;// 车辆类型

	private final int MAX_PROGRESS = 100;
	private final int NINE_PROGRESS = 96;
	private final int MIN_PROGRESS = 0;
	private final int PRO = 10;
	private final int ALL = 20;
	private final int FAIL = 30;
	private int progress = 1;
	// 字体大小
	private int maxsize = 24;
	private int minsize = 14;
	private String selectVehicleType;// 选中的车辆类型

	// 本页面请求
	private GetUserInfoRequest mGetUserInfoRequest;
	private VehicleTypeRequest mVehicleTypeRequest;
	private AddVehicleFastRequest mAddVehicleFastRequest;

	// 本页面请求id
	private final int GET_USER_INFO_REQUEST = 1;
	private final int VEHICLE_TYPE_REQUEST = 2;
	private final int ADD_VEHICLEFAST_REQUEST = 3;

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
		return R.layout.activity_fastaddvehicle;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		parentView = getLayoutInflater().inflate(
				R.layout.activity_fastaddvehicle, null);

		mLlGlobal = (LinearLayout) findViewById(R.id.ll_fastaddvehicle_global);
		mProgress = (ProgressBar) findViewById(R.id.pb_fastaddvehicle_progress);
		mEtVehicleName = (EditText) findViewById(R.id.et_fastaddvehicle_inputname);
		mEtVehicleIdCard = (EditText) findViewById(R.id.et_fastaddvehicle_inputidcard);
		mEtVehicleNo = (EditText) findViewById(R.id.et_fastaddvehicle_inputcarlicense);
		mTvVehicleType = (TextView) findViewById(R.id.tv_fastaddvehicle_inputcartype);
		mEtVehicleEngineCode = (EditText) findViewById(R.id.et_fastaddvehicle_inputengine);
		mIvDrivinglicense = (ImageView) findViewById(R.id.iv_fastaddvehicle_driverlicense);
		mIvOperatinglicense = (ImageView) findViewById(R.id.iv_fastaddvehicle_operatinglicense);
		mIvInsuranceimg = (ImageView) findViewById(R.id.iv_fastaddvehicle_insuranceimg);
		mBtnApply = (Button) findViewById(R.id.bt_fastaddvehicle_apply);

		progressBarHandle = (ProgressBar) findViewById(R.id.pb_fastaddvehicle_myProssBarhandle);
		myProssBarhandleText = (TextView) findViewById(R.id.tv_fastaddvehicle_myProssBarhandleText);

		// 设置输入框显示的多是大写
		mEtVehicleNo.setTransformationMethod(new AllCapTransformationMethod());

	}

	@Override
	protected void initData() {
		// 服务服务器获取姓名和身份证号码
		getUserInfo();
	}

	/**
	 * 获取用户信息
	 */
	private void getUserInfo() {
		mLlGlobal.setVisibility(View.INVISIBLE);
		mProgress.setVisibility(View.VISIBLE);

		// 获取用户信息
		mGetUserInfoRequest = new GetUserInfoRequest(
				FastAddVehicleActivity.this);
		mGetUserInfoRequest.setRequestId(GET_USER_INFO_REQUEST);
		httpPost(mGetUserInfoRequest);
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
				FastAddVehicleActivity.this.finish();
			}
		});

	}

	// 初始化点击事件的方法
	private void initOnClick() {
		mTvVehicleType.setOnClickListener(this);
		mIvDrivinglicense.setOnClickListener(this);
		mIvOperatinglicense.setOnClickListener(this);
		mIvInsuranceimg.setOnClickListener(this);
		mBtnApply.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_fastaddvehicle_driverlicense:
			// 上传行驶证
			showDrivingLicense();

			break;

		case R.id.iv_fastaddvehicle_operatinglicense:
			// 上传运营证
			showOperatingLicense();

			break;

		case R.id.iv_fastaddvehicle_insuranceimg:
			// 上传车辆照片
			showInsuranceimg();

			break;

		case R.id.tv_fastaddvehicle_inputcartype:
			// 选择车辆类型
			showVehileType(vehicleTypeId);

			break;

		case R.id.bt_fastaddvehicle_apply:
			// 申请车辆
			String name = mEtVehicleName.getText().toString().trim();
			String idCode = mEtVehicleIdCard.getText().toString().trim();
			vehicleNo = mEtVehicleNo.getText().toString().trim();
			vehicleType = mTvVehicleType.getText().toString().trim();
			vehicleEngineCode = mEtVehicleEngineCode.getText().toString()
					.trim();
			// 将字符串中的小写转换为大写
			vehicleNo = StringUtils.swapCase(vehicleNo);

			if (TextUtils.isEmpty(name)) {
				showToast("请输入姓名");
				return;
			}

			if (TextUtils.isEmpty(idCode)) {
				showToast("请输入身份证号");
				return;
			}

			// 正则校验身份证
			if (!idCode.matches("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])")) {
				showToast("请输入正确的身份证号码");
				return;
			}

			if (TextUtils.isEmpty(vehicleNo)) {
				showToast("请输入车牌号码");
				return;
			}
			// 正则校验车牌号码
			if (!vehicleNo.matches("[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}")) {
				showToast("请输入正确的车牌号码");
				return;
			}

			if (TextUtils.isEmpty(vehicleType)) {
				showToast("请输入车辆类型");
				return;
			}

			if (TextUtils.isEmpty(imgInsuranceBase64)) {
				showToast("请上传车辆照片");
				return;
			}
			if (TextUtils.isEmpty(imgDrivingBase64)) {
				showToast("请上传行驶证照片");
				return;
			}
			if (TextUtils.isEmpty(imgOperatingBase64)) {
				showToast("请上传运营证照片");
				return;
			}

			// 请求服务器申请验证车辆
			applyNewVehicle(name, idCode, vehicleNo, mVehicleType,
					vehicleEngineCode, imgInsuranceBase64, vehicleImgName,
					imgDrivingBase64, drivingImgName, imgOperatingBase64,
					operatingImgName);

			break;

		default:
			break;
		}

	}

	// 请求服务器申请车辆认证
	private void applyNewVehicle(String name, String idCode, String vehicleNo,
			int VehicleType, String vehicleEngineCode,
			String VehicleForceInsuImgUrl, String vehicleForceInsuImgName,
			String vehicleLicenseImgUrl, String vehicleLicenseImgName,
			String vehicleCertificateImgUrl, String vehicleCertificateImgName) {

		mAddVehicleFastRequest = new AddVehicleFastRequest(
				FastAddVehicleActivity.this, name, idCode, vehicleNo,
				VehicleType, vehicleEngineCode, VehicleForceInsuImgUrl,
				vehicleForceInsuImgName, vehicleLicenseImgUrl,
				vehicleLicenseImgName, vehicleCertificateImgUrl,
				vehicleCertificateImgName);
		mAddVehicleFastRequest.setRequestId(ADD_VEHICLEFAST_REQUEST);
		httpPost(mAddVehicleFastRequest);
		// 设置按钮不可点击
		setViewEnable(false);
		// 显示进度条
		progressBarHandle.setVisibility(View.VISIBLE);
		myProssBarhandleText.setVisibility(View.VISIBLE);
		progressBarHandle.setMax(MAX_PROGRESS);// 设置最大进度

		progress = (progress > 0) ? progress : 0;
		progressBarHandle.setProgress(progress);
		handler.sendEmptyMessage(PRO);// 发送空消息

	}

	// 设置申请按钮不可重复点击
	private void setViewEnable(boolean bEnable) {
		if (bEnable) {
			mBtnApply.setText("申请");
		} else {
			mBtnApply.setText("申请中 ...");
		}
		mBtnApply.setEnabled(bEnable);
		mTvVehicleType.setEnabled(bEnable);
		mIvDrivinglicense.setEnabled(bEnable);
		mIvOperatinglicense.setEnabled(bEnable);
		mIvInsuranceimg.setEnabled(bEnable);

	}

	// 选择车辆类型
	private void showVehileType(String id) {
		// 清空存放车辆类型中文的集合
		listcarType.clear();
		// 清空存放车辆类型数字的集合
		listcarTypeNum.clear();
		mVehicleTypeRequest = new VehicleTypeRequest(
				FastAddVehicleActivity.this, id);
		mVehicleTypeRequest.setRequestId(VEHICLE_TYPE_REQUEST);
		httpPost(mVehicleTypeRequest);
		// 设置选择车辆类型按钮不可点击
		setViewTypeEnable(false);
	}

	// 设置获取车辆类型按钮不可重复点击
	private void setViewTypeEnable(boolean bEnable) {
		mTvVehicleType.setEnabled(bEnable);
	}

	// 获取车辆类型
	private void showVehicleTypePop() {
		final PopupWindow typePopupWindow = new PopupWindow(
				FastAddVehicleActivity.this);
		View typeView = getLayoutInflater().inflate(
				R.layout.item_vehiletype_popupwindows, null);

		TextView tvCancle = (TextView) typeView
				.findViewById(R.id.tv_newvehicle_cancle);
		TextView tvSure = (TextView) typeView
				.findViewById(R.id.tv_newvehicle_sure);
		// 点击取消
		tvCancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				typePopupWindow.dismiss();
			}
		});

		// 点击确定
		tvSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 显示车辆类型
				if (selectVehicleType != null) {
					mTvVehicleType.setText(selectVehicleType);
				}
				typePopupWindow.dismiss();
			}
		});

		WheelView mvVehicleTypeList = (WheelView) typeView
				.findViewById(R.id.wv_newvehicle_type);

		// 将车辆类型添加进集合
		for (int i = 0; i < carTypeData.size(); i++) {
			listcarType.add(carTypeData.get(i).v);
			listcarTypeNum.add(carTypeData.get(i).k);
		}

		final VehicleTypeTextAdapter vehicleTypeTextAdapter = new VehicleTypeTextAdapter(
				this, listcarType, 0, maxsize, minsize);
		mvVehicleTypeList.setVisibleItems(5);
		mvVehicleTypeList.setViewAdapter(vehicleTypeTextAdapter);
		mvVehicleTypeList.setCurrentItem(0);
		mvVehicleTypeList.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String currentText = (String) vehicleTypeTextAdapter
						.getItemText(wheel.getCurrentItem());
				// 获取每一个车辆类型对应的id
				mVehicleType = listcarTypeNum.get(wheel.getCurrentItem());
				selectVehicleType = currentText;
				setTextviewSizeTwo(currentText, vehicleTypeTextAdapter);

			}
		});
		mvVehicleTypeList.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String currentText = (String) vehicleTypeTextAdapter
						.getItemText(wheel.getCurrentItem());
				setTextviewSizeTwo(currentText, vehicleTypeTextAdapter);
			}
		});

		typePopupWindow.setWidth(LayoutParams.MATCH_PARENT);
		typePopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		typePopupWindow.setBackgroundDrawable(new BitmapDrawable());
		typePopupWindow.setFocusable(true);
		typePopupWindow.setOutsideTouchable(true);
		typePopupWindow.setContentView(typeView);
		typePopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

	}

	class VehicleTypeTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected VehicleTypeTextAdapter(Context context,
				ArrayList<String> list, int currentItem, int maxsize,
				int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem,
					maxsize, minsize);
			this.list = list;
			selectVehicleType = list.get(0);// 初始化为集合的第一车辆类型
			mVehicleType = listcarTypeNum.get(0);// 初始化为集合的第一车辆类型的id
			setItemTextResource(R.id.tempValue);
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			// 返回车辆类型
			return list.get(index);
		}
	}

	/**
	 * 设置字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSizeTwo(String curriteItemText,
			VehicleTypeTextAdapter adapter) {
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

	// 上传车辆照片的方法
	private void showInsuranceimg() {
		insurancePopupWindow = new PopupWindow(FastAddVehicleActivity.this);
		View insurancePhoto = getLayoutInflater().inflate(
				R.layout.item_insuranceimgphoto_popupwindows, null);
		insurancePopupWindow.setWidth(LayoutParams.MATCH_PARENT);
		insurancePopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		insurancePopupWindow.setBackgroundDrawable(new BitmapDrawable());
		insurancePopupWindow.setFocusable(true);
		insurancePopupWindow.setOutsideTouchable(true);
		insurancePopupWindow.setContentView(insurancePhoto);
		insurancePopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

		Button insurance_photo = (Button) insurancePhoto
				.findViewById(R.id.item_popupwindows_insuranceimgphoto);
		Button insurance_select = (Button) insurancePhoto
				.findViewById(R.id.item_popupwindows_insuranceimgselect);
		Button insurance_cancel = (Button) insurancePhoto
				.findViewById(R.id.item_popupwindows_insuranceimgcancel);

		// 车辆拍照
		insurance_photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PickImage.pickImageFromCamera(FastAddVehicleActivity.this,
						driverTempFileDir + "Vehiclelicense.jpg", 5);
				insurancePopupWindow.dismiss();
			}
		});

		insurance_select.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PickImage.pickImageFromPhoto(FastAddVehicleActivity.this, 6);
				insurancePopupWindow.dismiss();
			}
		});

		insurance_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				insurancePopupWindow.dismiss();
			}
		});

	}

	// 上传运营证的方法
	private void showOperatingLicense() {
		operatingPopupWindow = new PopupWindow(FastAddVehicleActivity.this);
		View operatingPhoto = getLayoutInflater().inflate(
				R.layout.item_operatingphoto_popupwindows, null);
		operatingPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
		operatingPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		operatingPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		operatingPopupWindow.setFocusable(true);
		operatingPopupWindow.setOutsideTouchable(true);
		operatingPopupWindow.setContentView(operatingPhoto);
		operatingPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

		Button operating_photo = (Button) operatingPhoto
				.findViewById(R.id.item_popupwindows_operatingphoto);
		Button operating_select = (Button) operatingPhoto
				.findViewById(R.id.item_popupwindows_operatingselect);
		Button operating_cancel = (Button) operatingPhoto
				.findViewById(R.id.item_popupwindows_operatingcancel);

		// 运营证拍照
		operating_photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PickImage.pickImageFromCamera(FastAddVehicleActivity.this,
						driverTempFileDir + "Operatinglicense.jpg", 3);
				operatingPopupWindow.dismiss();
			}
		});

		// 运营证选图
		operating_select.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PickImage.pickImageFromPhoto(FastAddVehicleActivity.this, 4);
				operatingPopupWindow.dismiss();
			}
		});

		// 运营证取消
		operating_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				operatingPopupWindow.dismiss();
			}
		});

	}

	// 上传行驶证的方法
	private void showDrivingLicense() {
		drivingPopupWindow = new PopupWindow(FastAddVehicleActivity.this);
		View drivingPhoto = getLayoutInflater().inflate(
				R.layout.item_drivingphoto_popupwindows, null);

		drivingPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
		drivingPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		drivingPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		drivingPopupWindow.setFocusable(true);
		drivingPopupWindow.setOutsideTouchable(true);
		drivingPopupWindow.setContentView(drivingPhoto);
		drivingPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

		Button driving_photo = (Button) drivingPhoto
				.findViewById(R.id.item_popupwindows_drivingphoto);
		Button driving_select = (Button) drivingPhoto
				.findViewById(R.id.item_popupwindows_drivingselect);
		Button driving_cancel = (Button) drivingPhoto
				.findViewById(R.id.item_popupwindows_drivingcancel);

		// 行驶证拍照
		driving_photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PickImage.pickImageFromCamera(FastAddVehicleActivity.this,
						driverTempFileDir + "Drivinglicense.jpg", 1);
				drivingPopupWindow.dismiss();
			}
		});
		// 行驶证图库选择
		driving_select.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PickImage.pickImageFromPhoto(FastAddVehicleActivity.this, 2);
				drivingPopupWindow.dismiss();
			}
		});
		// 行驶证取消
		driving_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				drivingPopupWindow.dismiss();
			}
		});

	}

	// 接收返回结果
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			// 行驶证拍照返回
			if (resultCode == RESULT_OK) {
				drivingimageFile = new File(driverTempFileDir
						+ "Drivinglicense.jpg");
			}
			break;

		case 2:
			// 行驶证图库返回
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				String realPathFromURI = FileUtils
						.getRealPathFromURI(uri, this);
				if (TextUtils.isEmpty(realPathFromURI)) {
					drivingimageFile = null;
				} else {
					drivingimageFile = new File(driverTempFileDir
							+ "Drivinglicense.jpg");
					File temFile = new File(realPathFromURI);

					FileUtils.copyFile(temFile, drivingimageFile);
				}
			}
			break;

		case 3:
			// 运营证拍照返回
			if (resultCode == RESULT_OK) {
				operatingimageFile = new File(driverTempFileDir
						+ "Operatinglicense.jpg");
			}
			break;

		case 4:
			// 运营证图库返回
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				String realPathFromURI = FileUtils
						.getRealPathFromURI(uri, this);
				if (TextUtils.isEmpty(realPathFromURI)) {
					operatingimageFile = null;
				} else {
					operatingimageFile = new File(driverTempFileDir
							+ "Operatinglicense.jpg");
					File temFile = new File(realPathFromURI);

					FileUtils.copyFile(temFile, operatingimageFile);
				}
			}
			break;

		case 5:
			// 车辆照片拍照返回
			if (resultCode == RESULT_OK) {
				vehicleimageFile = new File(driverTempFileDir
						+ "Vehiclelicense.jpg");
			}
			break;

		case 6:
			// 车辆照片图库返回
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				String realPathFromURI = FileUtils
						.getRealPathFromURI(uri, this);
				if (TextUtils.isEmpty(realPathFromURI)) {
					vehicleimageFile = null;
				} else {
					vehicleimageFile = new File(driverTempFileDir
							+ "Vehiclelicense.jpg");
					File temFile = new File(realPathFromURI);

					FileUtils.copyFile(temFile, vehicleimageFile);
				}
			}
			break;

		default:
			break;
		}

		if (requestCode == 1 || requestCode == 2) {
			// 判断本地文件是否存在
			if (drivingimageFile == null) {
				return;
			}

			// TODO--
			byte[] bytes = handleImage(drivingimageFile, 800, 800);

			if (bytes == null || bytes.length == 0) {
				return;
			}
			int degree = ImageUtil.readPictureDegree(drivingimageFile
					.getAbsolutePath());

			imgDrivingBase64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
			Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			ImageUtil.rotaingImageView(degree, bm);
			// 显示拍照或选图后的行驶证
			if (bm != null) {
				mIvDrivinglicense.setImageBitmap(bm);
			}

			// base64解码
			byte[] decode = Base64.decode(imgDrivingBase64, Base64.NO_WRAP);

			// TODO--将图片保存在SD卡
			BufferedOutputStream bos = null;
			FileOutputStream fos = null;
			File file = new File(driverTempFileDir + "imgBaseDriving.jpg");
			try {
				if (file.exists()) {
					file.createNewFile();
				}
				fos = new FileOutputStream(file);
				bos = new BufferedOutputStream(fos);
				bos.write(decode);
				fos.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (bos != null) {
					try {
						bos.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}

		}

		if (requestCode == 3 || requestCode == 4) {
			// 判断本地文件是否存在
			if (operatingimageFile == null) {
				return;
			}

			// TODO--
			byte[] bytes = handleImage(operatingimageFile, 800, 800);

			if (bytes == null || bytes.length == 0) {
				return;
			}
			int degree = ImageUtil.readPictureDegree(operatingimageFile
					.getAbsolutePath());

			imgOperatingBase64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
			Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			ImageUtil.rotaingImageView(degree, bm);
			// 显示拍照或选图后的运营证
			if (bm != null) {
				mIvOperatinglicense.setImageBitmap(bm);
			}

			// base64解码
			byte[] decode = Base64.decode(imgOperatingBase64, Base64.NO_WRAP);

			// TODO--将图片保存在SD卡
			BufferedOutputStream bos = null;
			FileOutputStream fos = null;
			File file = new File(driverTempFileDir + "imgBaseOperating.jpg");
			try {
				if (file.exists()) {
					file.createNewFile();
				}
				fos = new FileOutputStream(file);
				bos = new BufferedOutputStream(fos);
				bos.write(decode);
				fos.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (bos != null) {
					try {
						bos.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}

		}

		if (requestCode == 5 || requestCode == 6) {
			// 判断本地文件是否存在
			if (vehicleimageFile == null) {
				return;
			}

			// TODO--
			byte[] bytes = handleImage(vehicleimageFile, 800, 800);

			if (bytes == null || bytes.length == 0) {
				return;
			}
			int degree = ImageUtil.readPictureDegree(vehicleimageFile
					.getAbsolutePath());

			imgInsuranceBase64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
			Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			ImageUtil.rotaingImageView(degree, bm);

			// 显示拍照后选图后的车辆照片
			if (bm != null) {
				mIvInsuranceimg.setImageBitmap(bm);
			}

			// base64解码
			byte[] decode = Base64.decode(imgInsuranceBase64, Base64.NO_WRAP);

			// TODO--将图片保存在SD卡
			BufferedOutputStream bos = null;
			FileOutputStream fos = null;
			File file = new File(driverTempFileDir + "imgBaseInsurance.jpg");
			try {
				if (file.exists()) {
					file.createNewFile();
				}
				fos = new FileOutputStream(file);
				bos = new BufferedOutputStream(fos);
				bos.write(decode);
				fos.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (bos != null) {
					try {
						bos.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}

		}

	}

	/**
	 * 处理图片
	 * 
	 * @param avatarFile
	 * @return
	 */
	private byte[] handleImage(File avatarFile, int out_Width, int out_Height) {
		if (avatarFile.exists()) { // 本地文件存在
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			// 获取这个图片原始的宽和高 在outHeight 及 outWidth
			Bitmap bm = BitmapFactory.decodeFile(avatarFile.getPath(), options);

			// 此时返回bm为空
			// 我们要得到高及宽都不超过W H的缩略图
			int zW = options.outWidth / out_Width;
			int zH = options.outHeight / out_Height;
			int be = zH;
			if (zW > be)
				be = zW;
			if (be == 0)
				be = 1;
			options.inSampleSize = be;
			options.inJustDecodeBounds = false;
			bm = BitmapFactory.decodeFile(avatarFile.getPath(), options);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			if (bm == null) {
				return null;
			}
			bm.copy(Bitmap.Config.ARGB_8888, false);

			// TODO--
			bm.compress(
					avatarFile.getAbsolutePath().endsWith("jpg") ? Bitmap.CompressFormat.JPEG
							: Bitmap.CompressFormat.PNG, 50, outputStream);

			return outputStream.toByteArray();

		}
		return null;
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess;
		String message;
		switch (requestId) {
		case GET_USER_INFO_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 获取用户信息成功
				UserInfo userInfo = (UserInfo) response.singleData;
				String name = userInfo.name;// 真实姓名
				String iDCard = userInfo.iDCode;// 身份证号码

				// 设置用户实名认证的姓名并不能修改
				if (!TextUtils.isEmpty(name) && name != null) {
					mEtVehicleName.setText(name);
					mEtVehicleName.setFocusable(false);
				}

				// 设置身份证号码并不能修改
				if (!TextUtils.isEmpty(iDCard) && iDCard != null) {
					mEtVehicleIdCard.setText(iDCard);
					mEtVehicleIdCard.setFocusable(false);
				}

			} else {
				// 获取用户信息失败
			}

			// 显示界面
			mLlGlobal.setVisibility(View.VISIBLE);
			mProgress.setVisibility(View.INVISIBLE);
			break;

		case VEHICLE_TYPE_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 获取信息成功
				carTypeData = response.data;
				// 显示车辆类型弹窗
				showVehicleTypePop();

			} else {
				// 获取信息失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			// 设置选择车辆类型按钮可点击
			setViewTypeEnable(true);

			break;

		case ADD_VEHICLEFAST_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				// 新增车辆成功

				// 删除存放图片文件夹
				drivingimageFile = new File(driverTempFileDir
						+ "Drivinglicense.jpg");
				if (drivingimageFile.exists() && drivingimageFile.isFile()) {
					drivingimageFile.delete();
				}

				operatingimageFile = new File(driverTempFileDir
						+ "Operatinglicense.jpg");
				if (operatingimageFile.exists() && operatingimageFile.isFile()) {
					operatingimageFile.delete();
				}

				vehicleimageFile = new File(driverTempFileDir
						+ "Vehiclelicense.jpg");
				if (vehicleimageFile.exists() && vehicleimageFile.isFile()) {
					vehicleimageFile.delete();
				}

				handler.sendEmptyMessage(ALL);// 发送空消息

				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 关闭当前页面
				FastAddVehicleActivity.this.finish();
			} else {
				// 新增车辆失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				handler.sendEmptyMessage(FAIL);// 发送空消息
				// 设置按钮可点击
				setViewEnable(true);
			}

			break;

		default:
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(getString(R.string.net_error_toast));
		switch (requestId) {
		case ADD_VEHICLEFAST_REQUEST:
			// 设置申请按钮可点击
			setViewEnable(true);
			handler.sendEmptyMessage(FAIL);// 发送空消息
			break;

		case VEHICLE_TYPE_REQUEST:
			// 设置选择车辆类型按钮可点击
			setViewTypeEnable(true);
			break;

		}
	}

	/**
	 * 本界面跳转方法
	 */
	public static void invoke(Context context) {
		Intent intent = new Intent(context, FastAddVehicleActivity.class);
		context.startActivity(intent);
	}

}
