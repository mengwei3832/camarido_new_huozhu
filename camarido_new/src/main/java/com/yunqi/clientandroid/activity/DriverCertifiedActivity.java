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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.DriverVerifyInfo;
import com.yunqi.clientandroid.http.request.GetDriverInfoRequest;
import com.yunqi.clientandroid.http.request.SetDriverInfoRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CacheUtils;
import com.yunqi.clientandroid.utils.FileUtils;
import com.yunqi.clientandroid.utils.ImageLoaderOptions;
import com.yunqi.clientandroid.utils.ImageUtil;
import com.yunqi.clientandroid.utils.PickImage;

/**
 * @deprecated:司机认证
 */
public class DriverCertifiedActivity extends BaseActivity implements
		OnClickListener {

	private ProgressBar progressBarHandle;
	private TextView myProssBarhandleText;
	private View parentView;
	private String name;
	private String idCode;
	private ImageView mIvDelete1;
	private ImageView mIvPhotoImg;
	private Button mBtnDriverAgreed;
	private EditText mEtInputName;
	private LinearLayout mLlGlobal;
	private ProgressBar mProgress;
	private PopupWindow driverPopupWindow;
	// 架驶证存放图片文件夹的路径
	private String driverTempFileDir = Environment
			.getExternalStorageDirectory() + "/";
	private String LicenceImgName = "Driverlicense.jpg";
	private String imgDriverBase64;
	private String licenceImgUrl;
	private File driverimageFile = null;
	private String verifyRank;
	private String isDriver;
	private EditText mEtInputIdCard;
	private ImageView mIvDelete2;

	private final int MAX_PROGRESS = 100;
	private final int NINE_PROGRESS = 96;
	private final int MIN_PROGRESS = 0;
	private final int PRO = 10;
	private final int ALL = 20;
	private final int FAIL = 30;
	private int progress = 1;

	private final String DRIVERLICENSE = "DRIVERLICENSE";
	// 本页请求id
	private final int GET_DRIVER_INFO_REQUEST_ID = 1;
	private final int SET_DRIVER_INFO_REQUEST_ID = 2;
	// 本页请求
	private GetDriverInfoRequest mGetDriverInfoRequest;
	private SetDriverInfoRequest mSetDriverInfoRequest;

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
					// TODO--延迟发送消息
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
		return R.layout.activity_drivercertification;

	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		parentView = getLayoutInflater().inflate(
				R.layout.activity_drivercertification, null);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		// 获取从个人设置界面传过来的姓名
		if (bundle != null && bundle.containsKey("name")) {
			name = bundle.getString("name");
		}

		if (bundle != null && bundle.containsKey("idCode")) {
			idCode = bundle.getString("idCode");
		}

		if (bundle != null && bundle.containsKey("isDriver")) {
			isDriver = bundle.getString("isDriver");
		}

		mEtInputName = (EditText) findViewById(R.id.et_drivercertification_name);
		mEtInputIdCard = (EditText) findViewById(R.id.et_drivercertification_idCard);
		mIvDelete1 = (ImageView) findViewById(R.id.iv_drivercertification_delete1);
		mIvDelete2 = (ImageView) findViewById(R.id.iv_drivercertification_delete2);
		mBtnDriverAgreed = (Button) findViewById(R.id.bt_drivercertification_agreed);
		mIvPhotoImg = (ImageView) findViewById(R.id.iv_drivercertification_photoimg);
		mProgress = (ProgressBar) findViewById(R.id.pb_driver_progress);
		mLlGlobal = (LinearLayout) findViewById(R.id.ll_driver_global);
		progressBarHandle = (ProgressBar) findViewById(R.id.pb_drivercertification_myProssBarhandle);
		myProssBarhandleText = (TextView) findViewById(R.id.tv_drivercertification_myProssBarhandleText);

		// 是否司机认证：0：未认证，1：认证中，2：已认证，3：认证失败
		if (isDriver != null && !isDriver.equals("0")) {
			// 初始化界面时从服务器获取数据
			initDriverRequest();
		}

		// 设置用户实名认证的姓名并不能修改
		if (!TextUtils.isEmpty(name) && name != null) {
			mEtInputName.setText("姓名:" + name);
			mEtInputName.setFocusable(false);
		}

		// 设置身份证号码并不能修改
		if (!TextUtils.isEmpty(idCode) && idCode != null) {
			mEtInputIdCard.setText("身份证:" + idCode);
			mEtInputIdCard.setFocusable(false);
		}

	}

	@Override
	protected void initData() {

	}

	// 初始化titileBar的方法
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(R.string.driver));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DriverCertifiedActivity.this.finish();
			}
		});

	}

	@Override
	protected void setListener() {
		// 初始化点击事件
		initOnClick();
	}

	// 初始化点击事件的方法
	private void initOnClick() {
		mBtnDriverAgreed.setOnClickListener(this);
		mIvPhotoImg.setOnClickListener(this);
		mIvDelete1.setOnClickListener(this);
		mIvDelete2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_drivercertification_delete1:
			// 姓名输入框的删除按钮
			mEtInputName.setText("");
			break;

		case R.id.iv_drivercertification_delete2:
			// 身份证输入框的删除按钮
			mEtInputIdCard.setText("");
			break;

		case R.id.iv_drivercertification_photoimg:
			// 选择照片
			showDriverPhotoPupopWindow();
			break;

		case R.id.bt_drivercertification_agreed:
			// 点击认证按钮获取界面上的信息并上传到服务器
			name = mEtInputName.getText().toString().trim();
			idCode = mEtInputIdCard.getText().toString().trim();

			if (TextUtils.isEmpty(name)) {
				showToast("请输入姓名");
				return;
			}

			if (TextUtils.isEmpty(idCode)) {
				showToast("请输入身份证号");
				return;
			}

			if (TextUtils.isEmpty(imgDriverBase64)) {
				showToast("请上传驾驶证图片");
				return;
			}

			if (name.contains(":")) {
				String[] nameArray = name.split(":");
				name = nameArray[1];
			}

			if (idCode.contains(":")) {
				String[] idCodeArray = idCode.split(":");
				idCode = idCodeArray[1];
			}

			if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(idCode)
					&& !TextUtils.isEmpty(LicenceImgName)
					&& !TextUtils.isEmpty(imgDriverBase64)) {
				driverCertificationRequest(name, idCode, LicenceImgName,
						imgDriverBase64);
			}

			break;

		default:
			break;
		}
	}

	// 初始化界面时从服务器获取数据
	private void initDriverRequest() {
		mLlGlobal.setVisibility(View.INVISIBLE);
		mProgress.setVisibility(View.VISIBLE);

		mGetDriverInfoRequest = new GetDriverInfoRequest(
				DriverCertifiedActivity.this);
		mGetDriverInfoRequest.setRequestId(GET_DRIVER_INFO_REQUEST_ID);
		httpPost(mGetDriverInfoRequest);

	}

	// 上传司机认证资料的方法
	private void driverCertificationRequest(String userName, String idCode,
			String licenceImgName, String licenceImgUrl) {

		mSetDriverInfoRequest = new SetDriverInfoRequest(this, userName,
				idCode, licenceImgName, licenceImgUrl);
		mSetDriverInfoRequest.setRequestId(SET_DRIVER_INFO_REQUEST_ID);
		httpPost(mSetDriverInfoRequest);
		// 设置认证按钮不可点击和图片不可点击
		setViewEnable(false);
		mBtnDriverAgreed.setText("上传中...");
		// 显示进度条
		progressBarHandle.setVisibility(View.VISIBLE);
		myProssBarhandleText.setVisibility(View.VISIBLE);
		progressBarHandle.setMax(MAX_PROGRESS);// 设置最大进度

		progress = (progress > 0) ? progress : 0;
		progressBarHandle.setProgress(progress);
		handler.sendEmptyMessage(PRO);// 发送空消息
	}

	// 选择图片的对话框
	@SuppressWarnings("deprecation")
	private void showDriverPhotoPupopWindow() {
		driverPopupWindow = new PopupWindow(DriverCertifiedActivity.this);
		View driverphoto = getLayoutInflater().inflate(
				R.layout.item_driverphoto_popupwindows, null);

		driverPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
		driverPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		driverPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		driverPopupWindow.setFocusable(true);
		driverPopupWindow.setOutsideTouchable(true);
		driverPopupWindow.setContentView(driverphoto);
		driverPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

		Button photo = (Button) driverphoto
				.findViewById(R.id.item_popupwindows_driverphoto);
		Button select = (Button) driverphoto
				.findViewById(R.id.item_popupwindows_driverselect);
		Button cancel = (Button) driverphoto
				.findViewById(R.id.item_popupwindows_drivercancel);

		// 拍照
		photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PickImage.pickImageFromCamera(DriverCertifiedActivity.this,
						driverTempFileDir + "Driverlicense.jpg", 1);
				driverPopupWindow.dismiss();
			}
		});

		// 图库选图
		select.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PickImage.pickImageFromPhoto(DriverCertifiedActivity.this, 2);
				driverPopupWindow.dismiss();
			}
		});

		// 取消
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				driverPopupWindow.dismiss();
			}
		});

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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			// 拍照返回
			if (resultCode == RESULT_OK) {
				driverimageFile = new File(driverTempFileDir
						+ "Driverlicense.jpg");
			}

			break;

		case 2:
			// 图库返回
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				String realPathFromURI = FileUtils
						.getRealPathFromURI(uri, this);
				if (TextUtils.isEmpty(realPathFromURI)) {
					driverimageFile = null;
				} else {
					driverimageFile = new File(driverTempFileDir
							+ "Driverlicense.jpg");
					File temFile = new File(realPathFromURI);

					FileUtils.copyFile(temFile, driverimageFile);
				}
			}
			break;

		default:
			break;
		}

		// 判断本地文件是否存在
		if (driverimageFile == null) {
			return;
		}

		// TODO--
		byte[] bytes = handleImage(driverimageFile, 800, 800);

		if (bytes == null || bytes.length == 0) {
			return;
		}
		int degree = ImageUtil.readPictureDegree(driverimageFile
				.getAbsolutePath());

		imgDriverBase64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
		Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		ImageUtil.rotaingImageView(degree, bm);

		if (bm != null) {
			mIvPhotoImg.setImageBitmap(bm);
		}

		// base64解码
		byte[] decode = Base64.decode(imgDriverBase64, Base64.NO_WRAP);

		// TODO--将图片保存在SD卡
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = new File(driverTempFileDir + "imgBaseDriver.jpg");
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

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess;
		String message;
		switch (requestId) {
		case GET_DRIVER_INFO_REQUEST_ID:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				// 获取司机认证信息数据成功
				DriverVerifyInfo driverInfo = (DriverVerifyInfo) response.singleData;
				licenceImgUrl = driverInfo.licenceImgUrl;// 附件URL
				verifyRank = driverInfo.verifyRank;// 认证状态。0：未认证，1：认证中，2：已认证，3：认证失败

				if (!TextUtils.isEmpty(licenceImgUrl) && licenceImgUrl != null) {
					ImageLoader.getInstance().displayImage(licenceImgUrl,
							mIvPhotoImg, ImageLoaderOptions.options);
				}

				if (verifyRank != null && verifyRank.equals("2")) {
					// 说明已认证
					setViewEnable(false);// 设置认证按钮不可点击
					mBtnDriverAgreed.setText("已认证");
				} else if (verifyRank != null && verifyRank.equals("1")) {
					// 说明认证中
					setViewEnable(false);// 设置认证按钮不可点击
					mBtnDriverAgreed.setText("认证中");
				} else if (verifyRank != null && verifyRank.equals("3")) {
					// 说明认证失败
					setViewEnable(true);// 设置认证按钮可点击
					showToast("认证失败请重新认证");
				}

			} else {
				// 获取司机认证信息数据失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			mLlGlobal.setVisibility(View.VISIBLE);
			mProgress.setVisibility(View.INVISIBLE);
			break;
		case SET_DRIVER_INFO_REQUEST_ID:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 司机认证上传服务器成功
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				DriverVerifyInfo driverInfo = (DriverVerifyInfo) response.singleData;
				licenceImgUrl = driverInfo.licenceImgUrl;// 附件URL
				verifyRank = driverInfo.verifyRank;// 认证状态。0：未认证，1：认证中，2：已认证，3：认证失败

				if (verifyRank != null && verifyRank.equals("2")) {
					// 说明已认证
					CacheUtils.putBoolean(getApplicationContext(),
							DRIVERLICENSE, true);
					mBtnDriverAgreed.setText("已认证");
				} else if (verifyRank != null && verifyRank.equals("1")) {
					// 说明认证中
					CacheUtils.putBoolean(getApplicationContext(),
							DRIVERLICENSE, true);
					mBtnDriverAgreed.setText("认证中");
				} else if (verifyRank != null && verifyRank.equals("3")) {
					// 说明认证失败
					CacheUtils.putBoolean(getApplicationContext(),
							DRIVERLICENSE, false);
					// 设置认证按钮可点击
					setViewEnable(true);
					showToast("认证失败请重新认证");
				}

				// 删除存放图片文件夹
				driverimageFile = new File(driverTempFileDir
						+ "Driverlicense.jpg");
				if (driverimageFile.exists() && driverimageFile.isFile()) {
					driverimageFile.delete();
				}

				handler.sendEmptyMessage(ALL);// 发送空消息

			} else {
				// 司机认证上传服务器失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 设置认证按钮可点击
				setViewEnable(true);
				mBtnDriverAgreed.setText("认证");
				handler.sendEmptyMessage(FAIL);// 发送空消息
			}
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(this.getResources().getString(R.string.net_error_toast));
		switch (requestId) {
		case SET_DRIVER_INFO_REQUEST_ID:
			handler.sendEmptyMessage(FAIL);// 发送空消息
			// 设置认证按钮可点击
			setViewEnable(true);
			mBtnDriverAgreed.setText("认证");
			break;
		}
	}

	// 设置认证按钮不可重复点击
	private void setViewEnable(boolean bEnable) {
		mBtnDriverAgreed.setEnabled(bEnable);
		mIvPhotoImg.setEnabled(bEnable);
	}

	// 本界面的跳转的方法
	public static void invoke(Context activity, String name, String idCode,
			String isDriver) {
		Intent intent = new Intent();
		intent.setClass(activity, DriverCertifiedActivity.class);
		intent.putExtra("name", name);// 传真实姓名
		intent.putExtra("idCode", idCode);// 传身份证号码
		intent.putExtra("isDriver", isDriver);// 传司机认证状态
		activity.startActivity(intent);
	}

	public static void invokeTwo(Context activity, String isDriver) {
		Intent intent = new Intent();
		intent.setClass(activity, DriverCertifiedActivity.class);
		intent.putExtra("isDriver", isDriver);// 传司机认证状态
		activity.startActivity(intent);
	}

}
