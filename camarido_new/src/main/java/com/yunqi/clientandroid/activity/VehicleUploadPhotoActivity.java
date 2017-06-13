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
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.http.request.ResetBusinessLicenseImgRequest;
import com.yunqi.clientandroid.http.request.ResetHandIdCodeImgRequest;
import com.yunqi.clientandroid.http.request.ResetLicenseImgRequest;
import com.yunqi.clientandroid.http.request.ResetVehicleImgImgRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.FileUtils;
import com.yunqi.clientandroid.utils.ImageLoaderOptions;
import com.yunqi.clientandroid.utils.ImageUtil;
import com.yunqi.clientandroid.utils.PickImage;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 重新上传新增车辆照片
 * @date 15/12/21
 */
public class VehicleUploadPhotoActivity extends BaseActivity implements
		View.OnClickListener {

	private ProgressBar progressBarHandle;
	private TextView myProssBarhandleText;
	private View parentView;
	private ImageView mVehiclePhoto;
	private Button mBtnChangePhoto;
	private Button mBtnSave;
	private String vehicleId;// 车辆id
	private int imgStatus;// 判断是要上传哪种图片 1是手持身份证、2是行驶证、3是车辆照片、4营业执照
	private String imgUrl;// 存放图片的URL
	private String imgName;// 存放图片的名称

	// 存放图片文件夹的路径
	private String driverTempFileDir = Environment
			.getExternalStorageDirectory() + "/";
	private File drivingimageFile = null;
	private String imgDrivingBase64;
	private PopupWindow certificatePopupWindow;

	private final int MAX_PROGRESS = 100;
	private final int NINE_PROGRESS = 96;
	private final int MIN_PROGRESS = 0;
	private final int PRO = 10;
	private final int ALL = 20;
	private final int FAIL = 30;
	private int progress = 1;

	// 本页请求
	private ResetHandIdCodeImgRequest mResetHandIdCodeImgRequest;
	private ResetLicenseImgRequest mResetLicenseImgRequest;
	private ResetVehicleImgImgRequest mResetVehicleImgImgRequest;
	private ResetBusinessLicenseImgRequest mResetBusinessLicenseImgRequest;

	// 本页请求id
	private final int SET_RESETHANDIDCODEIMG_REQUEST = 1;
	private final int SET_RESETLICENSEIMG_REQUEST = 2;
	private final int SET_RESETVEHICLEIMGIMG_REQUEST = 3;
	private final int SET_RESETBUSINESSLICENSEIMG_REQUEST = 4;

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
		return R.layout.activity_vehicle_uploadphoto;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		parentView = getLayoutInflater().inflate(
				R.layout.activity_vehicle_uploadphoto, null);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		// 获取从车辆详情页面传过来的数据
		if (bundle != null && bundle.containsKey("vehicleId")) {
			vehicleId = bundle.getString("vehicleId");
		}

		if (bundle != null && bundle.containsKey("imgStatus")) {
			imgStatus = bundle.getInt("imgStatus");
		}

		if (bundle != null && bundle.containsKey("imgUrl")) {
			imgUrl = bundle.getString("imgUrl");
		}
		if (bundle != null && bundle.containsKey("imgName")) {
			imgName = bundle.getString("imgName");
		}

		mVehiclePhoto = (ImageView) findViewById(R.id.iv_vehicleupload_photo);
		mBtnChangePhoto = (Button) findViewById(R.id.btn_vehicleupload_change);
		mBtnSave = (Button) findViewById(R.id.btn_vehicleupload_save);
		progressBarHandle = (ProgressBar) findViewById(R.id.pb_vehicleupload_myProssBarhandle);
		myProssBarhandleText = (TextView) findViewById(R.id.tv_vehicleupload_myProssBarhandleText);

		// 显示照片
		if (!TextUtils.isEmpty(imgUrl) && imgUrl != null) {
			ImageLoader.getInstance().displayImage(imgUrl, mVehiclePhoto,
					ImageLoaderOptions.options);
		}

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setListener() {
		// 初始化点击事件
		initOnClick();
	}

	// 初始化titileBar的方法
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(
				R.string.vehicle_changePhoto));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				VehicleUploadPhotoActivity.this.finish();
			}
		});

	}

	// 初始化点击事件的方法
	private void initOnClick() {
		mBtnChangePhoto.setOnClickListener(this);
		mBtnSave.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_vehicleupload_change:
			// 点击更改图片
			showCertificatePhoto();

			break;

		case R.id.btn_vehicleupload_save:
			// 点击上传图片
			if (TextUtils.isEmpty(imgDrivingBase64)) {
				showToast("请上传单据照片");
				return;
			}

			if (imgStatus == 1) {
				// 手持身份证
				if (!TextUtils.isEmpty(vehicleId) && vehicleId != null) {
					resetHandIdCodeImg(vehicleId, imgDrivingBase64, imgName);
				}
			} else if (imgStatus == 2) {
				// 行驶证
				if (!TextUtils.isEmpty(vehicleId) && vehicleId != null) {
					resetLicenseImg(vehicleId, imgDrivingBase64, imgName);
				}
			} else if (imgStatus == 3) {
				// 车辆
				if (!TextUtils.isEmpty(vehicleId) && vehicleId != null) {
					resetVehicleImgImg(vehicleId, imgDrivingBase64, imgName);
				}
			} else if (imgStatus == 4) {
				// 营业执照
				if (!TextUtils.isEmpty(vehicleId) && vehicleId != null) {
					resetBusinessLicenseImg(vehicleId, imgDrivingBase64,
							imgName);
				}
			}

			// 设置上传保存图片的按钮不可点击
			setViewEnable(false);
			// 显示进度条
			progressBarHandle.setVisibility(View.VISIBLE);
			myProssBarhandleText.setVisibility(View.VISIBLE);
			progressBarHandle.setMax(MAX_PROGRESS);// 设置最大进度

			progress = (progress > 0) ? progress : 0;
			progressBarHandle.setProgress(progress);
			handler.sendEmptyMessage(PRO);// 发送空消息

			break;

		default:
			break;
		}
	}

	// 设置上传保存图片的按钮不可重复点击
	private void setViewEnable(boolean bEnable) {
		if (bEnable) {
			mBtnSave.setText("保存");
		} else {
			mBtnSave.setText("保存中 ...");
		}
		mBtnSave.setEnabled(bEnable);
		mBtnChangePhoto.setEnabled(bEnable);

	}

	// 上传身份证
	private void resetHandIdCodeImg(String vehicleId, String imgBase64,
			String imgName) {
		mResetHandIdCodeImgRequest = new ResetHandIdCodeImgRequest(this,
				vehicleId, imgBase64, imgName);
		mResetHandIdCodeImgRequest.setRequestId(SET_RESETHANDIDCODEIMG_REQUEST);
		httpPost(mResetHandIdCodeImgRequest);
	}

	// 上传行驶证
	private void resetLicenseImg(String vehicleId, String imgBase64,
			String imgName) {
		mResetLicenseImgRequest = new ResetLicenseImgRequest(this, vehicleId,
				imgBase64, imgName);
		mResetLicenseImgRequest.setRequestId(SET_RESETLICENSEIMG_REQUEST);
		httpPost(mResetLicenseImgRequest);
	}

	// 上传车辆照片
	private void resetVehicleImgImg(String vehicleId, String imgBase64,
			String imgName) {
		mResetVehicleImgImgRequest = new ResetVehicleImgImgRequest(this,
				vehicleId, imgBase64, imgName);
		mResetVehicleImgImgRequest.setRequestId(SET_RESETVEHICLEIMGIMG_REQUEST);
		httpPost(mResetVehicleImgImgRequest);

	}

	// 上传营业执照
	private void resetBusinessLicenseImg(String vehicleId, String imgBase64,
			String imgName) {
		mResetBusinessLicenseImgRequest = new ResetBusinessLicenseImgRequest(
				this, vehicleId, imgBase64, imgName);
		mResetBusinessLicenseImgRequest
				.setRequestId(SET_RESETBUSINESSLICENSEIMG_REQUEST);
		httpPost(mResetBusinessLicenseImgRequest);

	}

	@Override
	public void onSuccess(int requestId, Response response) {
		boolean isSuccess;
		String message;
		switch (requestId) {
		case SET_RESETHANDIDCODEIMG_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				// 上传身份证成功
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 删除存放图片文件夹
				drivingimageFile = new File(driverTempFileDir + imgName);
				if (drivingimageFile.exists() && drivingimageFile.isFile()) {
					drivingimageFile.delete();
				}

				handler.sendEmptyMessage(ALL);// 发送空消息
				// 关闭当前页面
				VehicleUploadPhotoActivity.this.finish();
			} else {
				// 上传行驶证失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				handler.sendEmptyMessage(FAIL);// 发送空消息
			}
			// 设置上传保存图片的按钮可点击
			setViewEnable(true);
			break;

		case SET_RESETLICENSEIMG_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				// 上传行驶证成功
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 删除存放图片文件夹
				drivingimageFile = new File(driverTempFileDir + imgName);
				if (drivingimageFile.exists() && drivingimageFile.isFile()) {
					drivingimageFile.delete();
				}

				handler.sendEmptyMessage(ALL);// 发送空消息
				// 关闭当前页面
				VehicleUploadPhotoActivity.this.finish();
			} else {
				// 上传行驶证失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				handler.sendEmptyMessage(FAIL);// 发送空消息
			}

			// 设置上传保存图片的按钮可点击
			setViewEnable(true);
			break;

		case SET_RESETVEHICLEIMGIMG_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				// 上传车辆照片成功
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 删除存放图片文件夹
				drivingimageFile = new File(driverTempFileDir + imgName);
				if (drivingimageFile.exists() && drivingimageFile.isFile()) {
					drivingimageFile.delete();
				}

				handler.sendEmptyMessage(ALL);// 发送空消息
				// 关闭当前页面
				VehicleUploadPhotoActivity.this.finish();

			} else {
				// 上传车辆照片失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
				handler.sendEmptyMessage(FAIL);// 发送空消息
			}

			// 设置上传保存图片的按钮可点击
			setViewEnable(true);

			break;
		case SET_RESETBUSINESSLICENSEIMG_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				// 上传营业执照照片成功
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 删除存放图片文件夹
				drivingimageFile = new File(driverTempFileDir + imgName);
				if (drivingimageFile.exists() && drivingimageFile.isFile()) {
					drivingimageFile.delete();
				}

				handler.sendEmptyMessage(ALL);// 发送空消息
				// 关闭当前页面
				VehicleUploadPhotoActivity.this.finish();

			} else {
				// 上传营业执照照片失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
				handler.sendEmptyMessage(FAIL);// 发送空消息
			}

			// 设置上传保存图片的按钮可点击
			setViewEnable(true);

			break;

		default:
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		// 设置上传保存图片的按钮可点击
		setViewEnable(true);
		handler.sendEmptyMessage(FAIL);// 发送空消息
		showToast(this.getResources().getString(R.string.net_error_toast));

	}

	// 拍照的PopupWindow
	private void showCertificatePhoto() {
		certificatePopupWindow = new PopupWindow(
				VehicleUploadPhotoActivity.this);
		View drivingPhoto = getLayoutInflater().inflate(
				R.layout.item_drivingphoto_popupwindows, null);

		certificatePopupWindow.setWidth(LayoutParams.MATCH_PARENT);
		certificatePopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		certificatePopupWindow.setBackgroundDrawable(new BitmapDrawable());
		certificatePopupWindow.setFocusable(true);
		certificatePopupWindow.setOutsideTouchable(true);
		certificatePopupWindow.setContentView(drivingPhoto);
		certificatePopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

		RelativeLayout driving_parent = (RelativeLayout) drivingPhoto
				.findViewById(R.id.item_popupwindows_drivingparent);

		TextView driving_msg = (TextView) drivingPhoto
				.findViewById(R.id.item_popupwindows_drivingmsg);

		// if (imgStatus == 1) {
		// // 身份证
		// driving_parent.setBackgroundResource(R.drawable.withcardbg);
		// driving_msg.setText("请上传手持身份证");
		// } else if (imgStatus == 2) {
		// // 行驶证
		// driving_parent.setBackgroundResource(R.drawable.newdrivingbg);
		// driving_msg.setText("请上传行驶证");
		// } else if (imgStatus == 3) {
		// // 车辆
		// driving_parent.setBackgroundResource(R.drawable.newinsuranceimg);
		// driving_msg.setText("请上传车辆照片");
		// } else if (imgStatus == 4) {
		// // 营业执照
		// driving_parent.setBackgroundResource(R.drawable.businessbg);
		// driving_msg.setText("请上传营业执照");
		// }

		Button driving_photo = (Button) drivingPhoto
				.findViewById(R.id.item_popupwindows_drivingphoto);
		Button driving_select = (Button) drivingPhoto
				.findViewById(R.id.item_popupwindows_drivingselect);
		Button driving_cancel = (Button) drivingPhoto
				.findViewById(R.id.item_popupwindows_drivingcancel);

		// 拍照
		driving_photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PickImage.pickImageFromCamera(VehicleUploadPhotoActivity.this,
						driverTempFileDir + imgName, 1);
				certificatePopupWindow.dismiss();
			}
		});
		// 图库选择
		driving_select.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PickImage
						.pickImageFromPhoto(VehicleUploadPhotoActivity.this, 2);
				certificatePopupWindow.dismiss();
			}
		});
		// 取消
		driving_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				certificatePopupWindow.dismiss();
			}
		});

	}

	// 接收处理拍照或选图返回结果
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			// 行驶证拍照返回
			if (resultCode == RESULT_OK) {
				drivingimageFile = new File(driverTempFileDir + imgName);
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
					drivingimageFile = new File(driverTempFileDir + imgName);
					File temFile = new File(realPathFromURI);

					FileUtils.copyFile(temFile, drivingimageFile);
				}
			}
			break;

		default:
			break;
		}

		// 判断本地文件是否存在
		if (drivingimageFile == null) {
			return;
		}

		// TODO
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
			mVehiclePhoto.setImageBitmap(bm);
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

	// 本界面的跳转方法
	public static void invoke(Context activity, String vehicleId,
			String imgUrl, String imgName, int imgStatus) {
		Intent intent = new Intent();
		intent.setClass(activity, VehicleUploadPhotoActivity.class);
		intent.putExtra("vehicleId", vehicleId);
		intent.putExtra("imgUrl", imgUrl);
		intent.putExtra("imgName", imgName);
		intent.putExtra("imgStatus", imgStatus);
		activity.startActivity(intent);
	}

}
