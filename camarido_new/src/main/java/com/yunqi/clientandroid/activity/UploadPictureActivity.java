package com.yunqi.clientandroid.activity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.http.request.AddBusinessLicenseRequest;
import com.yunqi.clientandroid.http.request.AddIdCodeRequest;
import com.yunqi.clientandroid.http.request.AddLicenseRequest;
import com.yunqi.clientandroid.http.request.AddVehicleRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.FileUtils;
import com.yunqi.clientandroid.utils.ImageLoaderOptions;
import com.yunqi.clientandroid.utils.ImageUtil;
import com.yunqi.clientandroid.utils.PickImage;

/**
 * @Description:上传图片的页面
 * @ClassName: UploadPictureActivity
 * @author: zhm
 * @date: 2016-5-26 下午5:43:07
 * 
 */
public class UploadPictureActivity extends BaseActivity implements
		OnClickListener {
	/* 进行判断的 */
	private int mStatus;
	private String mPicturePath;
	private int mVehicleId;

	/* 界面的控件对象 */
	private ImageView ivUploadPicture;
	private TextView tvUploadPictureText;
	private Button btUploadPicturePai;
	private Button btUploadPictureChoose;
	private ProgressBar progressBarHandle;
	private TextView myProssBarhandleText;

	private final int MAX_PROGRESS = 100;
	private final int NINE_PROGRESS = 96;
	private final int MIN_PROGRESS = 0;
	private final int PRO = 10;
	private final int ALL = 20;
	private final int FAIL = 30;
	private int progress = 1;

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

	// 请求ID
	private final int ADD_IDCODE_IMAGEVIEW = 1;
	private final int ADD_BUSINESS_LICENSE_IMAGEVIEW = 2;
	private final int ADD_LICENSE_IMAGEVIEW = 3;
	private final int ADD_VEHICLE_IMAGEVIEW = 4;

	// 请求类
	private AddIdCodeRequest mAddIdCodeRequest;
	private AddBusinessLicenseRequest mAddBusinessLicenseRequest;
	private AddLicenseRequest mAddLicenseRequest;
	private AddVehicleRequest mAddVehicleRequest;

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
		return R.layout.activity_upload_picture;
	}

	@Override
	protected void initView() {
		initActionBar();

		ivUploadPicture = obtainView(R.id.iv_upload_picture);
		tvUploadPictureText = obtainView(R.id.tv_upload_picture_text);
		btUploadPicturePai = obtainView(R.id.bt_upload_picture_pai);
		btUploadPictureChoose = obtainView(R.id.bt_upload_picture_choose);
		progressBarHandle = obtainView(R.id.pb_upload_picture_myProssBarhandle);
		myProssBarhandleText = obtainView(R.id.tv_upload_picture_myProssBarhandleText);

		// 获取传过来的值
		mStatus = getIntent().getIntExtra("mStatus", 0);
		mPicturePath = getIntent().getStringExtra("picturePath");
		mVehicleId = getIntent().getIntExtra("vehicleID", 0);
	}

	// 初始化标题栏的方法
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(
				R.string.tv_newvehicle_titlemsg));
		setActionBarLeft(R.drawable.nav_back);
		setActionBarRight(true, 0, "保存");
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UploadPictureActivity.this.finish();
			}
		});
		// 保存图片上传
		setOnActionBarRightClickListener(false, new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 判断类型，并上传图片
				addPicture();
			}
		});
	}

	/**
	 * @Description:判断类型，并上传图片
	 * @Title:addPicture
	 * @return:void
	 * @throws
	 * @Create: 2016-5-27 下午1:40:42
	 * @Author : zhm
	 */
	private void addPicture() {
		if (mStatus == 1) {
			mAddIdCodeRequest = new AddIdCodeRequest(
					UploadPictureActivity.this, mVehicleId, imgWithCardBase64,
					withCardImgName);
			mAddIdCodeRequest.setRequestId(ADD_IDCODE_IMAGEVIEW);
			httpPost(mAddIdCodeRequest);
		} else if (mStatus == 2) {
			mAddBusinessLicenseRequest = new AddBusinessLicenseRequest(
					UploadPictureActivity.this, mVehicleId, imgBusinessBase64,
					businessImgName);
			mAddBusinessLicenseRequest
					.setRequestId(ADD_BUSINESS_LICENSE_IMAGEVIEW);
			httpPost(mAddBusinessLicenseRequest);
		} else if (mStatus == 3) {
			mAddLicenseRequest = new AddLicenseRequest(
					UploadPictureActivity.this, mVehicleId, imgDrivingBase64,
					drivingImgName);
			mAddLicenseRequest.setRequestId(ADD_LICENSE_IMAGEVIEW);
			httpPost(mAddLicenseRequest);
		} else if (mStatus == 4) {
			mAddVehicleRequest = new AddVehicleRequest(
					UploadPictureActivity.this, mVehicleId, imgInsuranceBase64,
					vehicleImgName);
			mAddVehicleRequest.setRequestId(ADD_VEHICLE_IMAGEVIEW);
			httpPost(mAddVehicleRequest);
		}
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
		case ADD_IDCODE_IMAGEVIEW: // 手持身份证
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 删除存放图片文件夹
				withCardimageFile = new File(personalVehicleTempFileDir
						+ "WithCardlicense.jpg");
				if (withCardimageFile.exists() && withCardimageFile.isFile()) {
					withCardimageFile.delete();
				}

				handler.sendEmptyMessage(ALL);// 发送空消息

				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 关闭当前页面
				UploadPictureActivity.this.finish();
			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				handler.sendEmptyMessage(FAIL);// 发送空消息
				setViewEnable(true);
			}
			break;
		case ADD_BUSINESS_LICENSE_IMAGEVIEW: // 营业执照
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 删除存放图片文件夹
				businessimageFile = new File(personalVehicleTempFileDir
						+ "Businesslicense.jpg");
				if (businessimageFile.exists() && businessimageFile.isFile()) {
					businessimageFile.delete();
				}

				handler.sendEmptyMessage(ALL);// 发送空消息

				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 关闭当前页面
				UploadPictureActivity.this.finish();
			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				handler.sendEmptyMessage(FAIL);// 发送空消息
				setViewEnable(true);
			}
			break;
		case ADD_LICENSE_IMAGEVIEW: // 行驶证
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 删除存放图片文件夹
				drivingimageFile = new File(personalVehicleTempFileDir
						+ "Drivinglicense.jpg");
				if (drivingimageFile.exists() && drivingimageFile.isFile()) {
					drivingimageFile.delete();
				}

				handler.sendEmptyMessage(ALL);// 发送空消息

				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 关闭当前页面
				UploadPictureActivity.this.finish();
			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				handler.sendEmptyMessage(FAIL);// 发送空消息
				setViewEnable(true);
			}
			break;
		case ADD_VEHICLE_IMAGEVIEW: // 车辆照片
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 删除存放图片文件夹
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
				UploadPictureActivity.this.finish();
			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				handler.sendEmptyMessage(FAIL);// 发送空消息
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
	}

	@Override
	protected void initData() {
		// 判断传过来的值给相应控件赋值
		setKongJian();

	}

	@Override
	protected void setListener() {
		btUploadPicturePai.setOnClickListener(this);
		btUploadPictureChoose.setOnClickListener(this);

	}

	/**
	 * @Description:给控件赋值
	 * @Title:setIdCode
	 * @return:void
	 * @throws
	 * @Create: 2016-5-27 上午10:33:43
	 * @Author : zhm
	 */
	private void setKongJian() {
		Log.e("TAG", "----------点击的mStatus-------------" + mStatus);
		Log.e("TAG", "----------点击的mPicturePath-------------" + mPicturePath);
		Log.e("TAG", "----------点击的mStatus-------------" + mStatus);

		if (mStatus == 1) {
			if (mPicturePath == null || TextUtils.isEmpty(mPicturePath)) {

//				ivUploadPicture
//						.setImageResource(R.drawable.tushi_shenfenzhengpaizhao);
				tvUploadPictureText
						.setText(R.string.vehicle_upload_picture_shenfenzheng);
			} else {
				ImageLoader.getInstance().displayImage(mPicturePath,
						ivUploadPicture, ImageLoaderOptions.options);
				tvUploadPictureText.setText(R.string.vehicle_changePhoto_error);
				tvUploadPictureText.setTextColor(Color.RED);
			}
		} else if (mStatus == 2) {
			if (mPicturePath == null || TextUtils.isEmpty(mPicturePath)) {
//				ivUploadPicture
//						.setImageResource(R.drawable.tushi_yingyezhizhao);
				tvUploadPictureText
						.setText(R.string.vehicle_upload_picture_shenfenzheng);
			} else {
				ImageLoader.getInstance().displayImage(mPicturePath,
						ivUploadPicture, ImageLoaderOptions.options);
				tvUploadPictureText.setText(R.string.vehicle_changePhoto_error);
				tvUploadPictureText.setTextColor(Color.RED);
			}
		}
		if (mStatus == 3) {
			if (mPicturePath == null || TextUtils.isEmpty(mPicturePath)) {
//				ivUploadPicture.setImageResource(R.drawable.tushi_xingshizheng);
				tvUploadPictureText
						.setText(R.string.vehicle_upload_picture_shenfenzheng);
			} else {
				ImageLoader.getInstance().displayImage(mPicturePath,
						ivUploadPicture, ImageLoaderOptions.options);
				tvUploadPictureText.setText(R.string.vehicle_changePhoto_error);
				tvUploadPictureText.setTextColor(Color.RED);
			}
		}
		if (mStatus == 4) {
			if (mPicturePath == null || TextUtils.isEmpty(mPicturePath)) {
//				ivUploadPicture
//						.setImageResource(R.drawable.tushi_cheliangzhaopian);
				tvUploadPictureText
						.setText(R.string.vehicle_upload_picture_cheliang);
			} else {
				ImageLoader.getInstance().displayImage(mPicturePath,
						ivUploadPicture, ImageLoaderOptions.options);
				tvUploadPictureText.setText(R.string.vehicle_changePhoto_error);
				tvUploadPictureText.setTextColor(Color.RED);
			}
		}
	}

	/**
	 * 上传图片界面跳转
	 * 
	 * 本界面跳转方法
	 * 
	 * @param activity
	 */
	public static void invoke(Activity activity, String picturePath,
			int mStatus, int vehicleID) {
		Intent intent = new Intent(activity, UploadPictureActivity.class);
		intent.putExtra("picturePath", picturePath);
		intent.putExtra("mStatus", mStatus);
		intent.putExtra("vehicleID", vehicleID);
		activity.startActivity(intent);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_upload_picture_pai: // 拍摄图片
			if (mStatus == 1) {
				PickImage.pickImageFromCamera(UploadPictureActivity.this,
						personalVehicleTempFileDir + "WithCardlicense.jpg", 1); // 手持身份证拍照
			} else if (mStatus == 2) {
				PickImage.pickImageFromCamera(UploadPictureActivity.this,
						personalVehicleTempFileDir + "Businesslicense.jpg", 7); // 营业执照牌照
			} else if (mStatus == 3) {
				PickImage.pickImageFromCamera(UploadPictureActivity.this,
						personalVehicleTempFileDir + "Drivinglicense.jpg", 3); // 行驶证拍照
			} else if (mStatus == 4) {
				PickImage.pickImageFromCamera(UploadPictureActivity.this,
						personalVehicleTempFileDir + "Vehiclelicense.jpg", 5); // 车辆牌照
			}

			// setViewEnable(false);
			break;

		case R.id.bt_upload_picture_choose: // 选择相册
			if (mStatus == 1) {
				PickImage.pickImageFromPhoto(UploadPictureActivity.this, 2); // 手持身份图库选择
			} else if (mStatus == 2) {
				PickImage.pickImageFromPhoto(UploadPictureActivity.this, 8); // 营业执照图库选择
			} else if (mStatus == 3) {
				PickImage.pickImageFromPhoto(UploadPictureActivity.this, 4); // 行驶证选图
			} else if (mStatus == 4) {
				PickImage.pickImageFromPhoto(UploadPictureActivity.this, 6); // 车辆选图
			}

			// setViewEnable(false);
			break;

		default:
			break;
		}

	}

	private void setViewEnable(boolean enable) {
		btUploadPicturePai.setEnabled(enable);
		btUploadPictureChoose.setEnabled(enable);
	}

	// 接收返回的结果
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			// 手持身份证拍照返回
			if (resultCode == RESULT_OK) {
				withCardimageFile = new File(personalVehicleTempFileDir
						+ "WithCardlicense.jpg");
			}
			break;

		case 2:
			// 手持身份证图库返回
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				String realPathFromURI = FileUtils
						.getRealPathFromURI(uri, this);
				if (TextUtils.isEmpty(realPathFromURI)) {
					withCardimageFile = null;
				} else {
					withCardimageFile = new File(personalVehicleTempFileDir
							+ "WithCardlicense.jpg");
					File temFile = new File(realPathFromURI);

					FileUtils.copyFile(temFile, withCardimageFile);
				}
			}
			break;

		case 3:
			// 行驶证拍照返回
			if (resultCode == RESULT_OK) {
				drivingimageFile = new File(personalVehicleTempFileDir
						+ "Drivinglicense.jpg");
			}
			break;

		case 4:
			// 行驶证图库返回
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				String realPathFromURI = FileUtils
						.getRealPathFromURI(uri, this);
				if (TextUtils.isEmpty(realPathFromURI)) {
					drivingimageFile = null;
				} else {
					drivingimageFile = new File(personalVehicleTempFileDir
							+ "Drivinglicense.jpg");
					File temFile = new File(realPathFromURI);

					FileUtils.copyFile(temFile, drivingimageFile);
				}
			}
			break;

		case 5:
			// 车辆照片拍照返回
			if (resultCode == RESULT_OK) {
				vehicleimageFile = new File(personalVehicleTempFileDir
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
					vehicleimageFile = new File(personalVehicleTempFileDir
							+ "Vehiclelicense.jpg");
					File temFile = new File(realPathFromURI);

					FileUtils.copyFile(temFile, vehicleimageFile);
				}
			}
			break;

		case 7:
			// 营业执照拍照返回
			if (resultCode == RESULT_OK) {
				businessimageFile = new File(personalVehicleTempFileDir
						+ "Businesslicense.jpg");
			}
			break;

		case 8:
			// 营业执照图库返回
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				String realPathFromURI = FileUtils
						.getRealPathFromURI(uri, this);
				if (TextUtils.isEmpty(realPathFromURI)) {
					businessimageFile = null;
				} else {
					businessimageFile = new File(personalVehicleTempFileDir
							+ "Businesslicense.jpg");
					File temFile = new File(realPathFromURI);

					FileUtils.copyFile(temFile, businessimageFile);
				}
			}
			break;

		default:
			break;
		}

		if (requestCode == 1 || requestCode == 2) {
			// 判断本地文件是否存在
			if (withCardimageFile == null) {
				return;
			}

			// TODO--
			byte[] bytes = handleImage(withCardimageFile, 800, 800);

			if (bytes == null || bytes.length == 0) {
				return;
			}
			int degree = ImageUtil.readPictureDegree(withCardimageFile
					.getAbsolutePath());

			imgWithCardBase64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
			Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			ImageUtil.rotaingImageView(degree, bm);
			// 显示拍照或选图后的手持身份证
			if (bm != null) {
				ivUploadPicture.setImageBitmap(bm);
			}

			// base64解码
			byte[] decode = Base64.decode(imgWithCardBase64, Base64.NO_WRAP);

			// TODO--将图片保存在SD卡
			BufferedOutputStream bos = null;
			FileOutputStream fos = null;
			File file = new File(personalVehicleTempFileDir
					+ "imgBaseWitchCar.jpg");
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
				ivUploadPicture.setImageBitmap(bm);
			}

			// base64解码
			byte[] decode = Base64.decode(imgDrivingBase64, Base64.NO_WRAP);

			// TODO--将图片保存在SD卡
			BufferedOutputStream bos = null;
			FileOutputStream fos = null;
			File file = new File(personalVehicleTempFileDir
					+ "imgBaseDriving.jpg");
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
				ivUploadPicture.setImageBitmap(bm);
			}

			// base64解码
			byte[] decode = Base64.decode(imgInsuranceBase64, Base64.NO_WRAP);

			// TODO--将图片保存在SD卡
			BufferedOutputStream bos = null;
			FileOutputStream fos = null;
			File file = new File(personalVehicleTempFileDir
					+ "imgBaseInsurance.jpg");
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

		if (requestCode == 7 || requestCode == 8) {
			// 判断本地文件是否存在
			if (businessimageFile == null) {
				return;
			}

			// TODO--
			byte[] bytes = handleImage(businessimageFile, 800, 800);

			if (bytes == null || bytes.length == 0) {
				return;
			}
			int degree = ImageUtil.readPictureDegree(businessimageFile
					.getAbsolutePath());

			imgBusinessBase64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
			Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			ImageUtil.rotaingImageView(degree, bm);

			// 显示拍照后选图后的营业执照照片
			if (bm != null) {
				ivUploadPicture.setImageBitmap(bm);
			}

			// base64解码
			byte[] decode = Base64.decode(imgBusinessBase64, Base64.NO_WRAP);

			// TODO--将图片保存在SD卡
			BufferedOutputStream bos = null;
			FileOutputStream fos = null;
			File file = new File(personalVehicleTempFileDir
					+ "imgBaseBusiness.jpg");
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

}
