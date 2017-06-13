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
import com.yunqi.clientandroid.http.request.ResetAttorneyRequest;
import com.yunqi.clientandroid.http.request.ResetBusinessImgRequest;
import com.yunqi.clientandroid.http.request.ResetWithCardRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.FileUtils;
import com.yunqi.clientandroid.utils.ImageLoaderOptions;
import com.yunqi.clientandroid.utils.ImageUtil;
import com.yunqi.clientandroid.utils.PickImage;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 企业认证界面重新上传
 * @date 15/03/30
 */
public class EnterpriseUploadPhotoActivity extends BaseActivity implements
		View.OnClickListener {

	private ProgressBar progressBarHandle;
	private TextView myProssBarhandleText;
	private View parentView;
	private int imgStatus;// 判断是要上传哪种图片 1是行驶证、2是运营证、3是车辆照片
	private String imgUrl;// 存放图片的URL
	private ImageView mEnterprisePhoto;
	private Button mBtnChangePhoto;
	private Button mBtnSave;

	// 存放图片文件夹的路径
	private String enterpriseTempFileDir = Environment
			.getExternalStorageDirectory() + "/";
	private String businessImgName = "Businesslicense.jpg";// 营业执照照片名
	private String attorneyImgName = "Attorneylicense.jpg";// 授权书照片名
	private String withCardImgName = "WithCardlicense.jpg";// 手持身份证照片名
	private File enterpriseimageFile = null;
	private String imgEnterpriseBase64;
	private PopupWindow enterprisePopupWindow;

	private final int MAX_PROGRESS = 100;
	private final int NINE_PROGRESS = 96;
	private final int MIN_PROGRESS = 0;
	private final int PRO = 10;
	private final int ALL = 20;
	private final int FAIL = 30;
	private int progress = 1;

	// 本页请求
	private ResetBusinessImgRequest mResetBusinessImgRequest;
	private ResetAttorneyRequest mResetAttorneyRequest;
	private ResetWithCardRequest mResetWithCardRequest;

	// 本页请求id
	private final int SET_RESETBUSINESSIMG_REQUEST = 1;
	private final int SET_RESETATTORNEYIMG_REQUEST = 2;
	private final int SET_RESETWITHCARDIMG_REQUEST = 3;

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
		return R.layout.activity_enterprise_uploadphoto;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		parentView = getLayoutInflater().inflate(
				R.layout.activity_enterprise_uploadphoto, null);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		// 获取从企业认证页面传过来的数据
		if (bundle != null && bundle.containsKey("imgStatus")) {
			imgStatus = bundle.getInt("imgStatus");
		}

		if (bundle != null && bundle.containsKey("imgUrl")) {
			imgUrl = bundle.getString("imgUrl");
		}

		mEnterprisePhoto = (ImageView) findViewById(R.id.iv_enterpriseupload_photo);
		mBtnChangePhoto = (Button) findViewById(R.id.btn_enterpriseupload_change);
		mBtnSave = (Button) findViewById(R.id.btn_enterpriseupload_save);
		progressBarHandle = (ProgressBar) findViewById(R.id.pb_enterpriseupload_myProssBarhandle);
		myProssBarhandleText = (TextView) findViewById(R.id.tv_enterpriseupload_myProssBarhandleText);

		// 显示照片
		if (!TextUtils.isEmpty(imgUrl) && imgUrl != null) {
			ImageLoader.getInstance().displayImage(imgUrl, mEnterprisePhoto,
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
				EnterpriseUploadPhotoActivity.this.finish();
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
		case R.id.btn_enterpriseupload_change:
			// 点击更改图片
			showCertificatePhoto();

			break;

		case R.id.btn_enterpriseupload_save:
			// 点击上传图片
			if (TextUtils.isEmpty(imgEnterpriseBase64)) {
				showToast("请上传单据照片");
				return;
			}

			if (imgStatus == 1) {
				// 营业执照
				resetBusinessImg(imgEnterpriseBase64, businessImgName);

			} else if (imgStatus == 2) {
				// 授权书
				resetAttorneyImg(imgEnterpriseBase64, attorneyImgName);

			} else if (imgStatus == 3) {
				// 手持身份证
				resetWithCardImg(imgEnterpriseBase64, withCardImgName);
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

	// 上传手持身份证
	private void resetWithCardImg(String imgEnterpriseBase64,
			String withCardImgName) {
		mResetWithCardRequest = new ResetWithCardRequest(
				EnterpriseUploadPhotoActivity.this, imgEnterpriseBase64,
				withCardImgName);
		mResetWithCardRequest.setRequestId(SET_RESETWITHCARDIMG_REQUEST);
		httpPost(mResetWithCardRequest);

	}

	// 上传授权书
	private void resetAttorneyImg(String imgEnterpriseBase64,
			String attorneyImgName) {
		mResetAttorneyRequest = new ResetAttorneyRequest(
				EnterpriseUploadPhotoActivity.this, imgEnterpriseBase64,
				attorneyImgName);
		mResetAttorneyRequest.setRequestId(SET_RESETATTORNEYIMG_REQUEST);
		httpPost(mResetAttorneyRequest);

	}

	// 上传营业执照
	private void resetBusinessImg(String imgEnterpriseBase64,
			String businessImgName) {
		mResetBusinessImgRequest = new ResetBusinessImgRequest(
				EnterpriseUploadPhotoActivity.this, imgEnterpriseBase64,
				businessImgName);
		mResetBusinessImgRequest.setRequestId(SET_RESETBUSINESSIMG_REQUEST);
		httpPost(mResetBusinessImgRequest);

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

	@Override
	public void onSuccess(int requestId, Response response) {
		boolean isSuccess;
		String message;

		switch (requestId) {
		case SET_RESETBUSINESSIMG_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 上传营业执照成功
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 删除存放图片文件夹
				enterpriseimageFile = new File(enterpriseTempFileDir
						+ "Businesslicense.jpg");
				if (enterpriseimageFile.exists()
						&& enterpriseimageFile.isFile()) {
					enterpriseimageFile.delete();
				}

				handler.sendEmptyMessage(ALL);// 发送空消息
				// 关闭当前页面
				EnterpriseUploadPhotoActivity.this.finish();
			} else {
				// 上传营业执照失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				handler.sendEmptyMessage(FAIL);// 发送空消息
			}
			// 设置上传保存图片的按钮可点击
			setViewEnable(true);

			break;

		case SET_RESETATTORNEYIMG_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				// 上传授权书成功
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 删除存放图片文件夹
				enterpriseimageFile = new File(enterpriseTempFileDir
						+ "Attorneylicense.jpg");
				if (enterpriseimageFile.exists()
						&& enterpriseimageFile.isFile()) {
					enterpriseimageFile.delete();
				}

				handler.sendEmptyMessage(ALL);// 发送空消息
				// 关闭当前页面
				EnterpriseUploadPhotoActivity.this.finish();
			} else {
				// 上传授权书失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				handler.sendEmptyMessage(FAIL);// 发送空消息
			}

			// 设置上传保存图片的按钮可点击
			setViewEnable(true);
			break;

		case SET_RESETWITHCARDIMG_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				// 上传手持身份证成功
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 删除存放图片文件夹
				enterpriseimageFile = new File(enterpriseTempFileDir
						+ "WithCardlicense.jpg");
				if (enterpriseimageFile.exists()
						&& enterpriseimageFile.isFile()) {
					enterpriseimageFile.delete();
				}

				handler.sendEmptyMessage(ALL);// 发送空消息
				// 关闭当前页面
				EnterpriseUploadPhotoActivity.this.finish();

			} else {
				// 上传手持身份证失败
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
		enterprisePopupWindow = new PopupWindow(
				EnterpriseUploadPhotoActivity.this);
		View enterprisePhoto = getLayoutInflater().inflate(
				R.layout.item_enterprisephoto_popupwindows, null);

		enterprisePopupWindow.setWidth(LayoutParams.MATCH_PARENT);
		enterprisePopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		enterprisePopupWindow.setBackgroundDrawable(new BitmapDrawable());
		enterprisePopupWindow.setFocusable(true);
		enterprisePopupWindow.setOutsideTouchable(true);
		enterprisePopupWindow.setContentView(enterprisePhoto);
		enterprisePopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

		RelativeLayout enterprise_parent = (RelativeLayout) enterprisePhoto
				.findViewById(R.id.item_popupwindows_enterpriseparent);

		TextView enterprise_msg = (TextView) enterprisePhoto
				.findViewById(R.id.item_popupwindows_enterprisemsg);

		if (imgStatus == 1) {
			// 营业执照
//			enterprise_parent.setBackgroundResource(R.drawable.businessbg);
			enterprise_msg.setText("请上传营业执照");
		} else if (imgStatus == 2) {
			// 授权书
//			enterprise_parent.setBackgroundResource(R.drawable.attorneybg);
			enterprise_msg.setText("请上传授权书");
		} else if (imgStatus == 3) {
			// 手持身份证
//			enterprise_parent.setBackgroundResource(R.drawable.withcardbg);
			enterprise_msg.setText("请上传手持身份证");
		}

		Button enterprise_photo = (Button) enterprisePhoto
				.findViewById(R.id.item_popupwindows_enterprisephoto);
		Button enterprise_select = (Button) enterprisePhoto
				.findViewById(R.id.item_popupwindows_enterpriseselect);
		Button enterprise_cancel = (Button) enterprisePhoto
				.findViewById(R.id.item_popupwindows_enterprisecancel);

		// 拍照
		enterprise_photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (imgStatus == 1) {
					// 营业执照
					PickImage.pickImageFromCamera(
							EnterpriseUploadPhotoActivity.this,
							enterpriseTempFileDir + "Businesslicense.jpg", 1);
				} else if (imgStatus == 2) {
					// 授权书
					PickImage.pickImageFromCamera(
							EnterpriseUploadPhotoActivity.this,
							enterpriseTempFileDir + "Attorneylicense.jpg", 1);
				} else if (imgStatus == 3) {
					// 手持身份证
					PickImage.pickImageFromCamera(
							EnterpriseUploadPhotoActivity.this,
							enterpriseTempFileDir + "WithCardlicense.jpg", 1);
				}
				enterprisePopupWindow.dismiss();
			}
		});
		// 图库选择
		enterprise_select.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PickImage.pickImageFromPhoto(
						EnterpriseUploadPhotoActivity.this, 2);
				enterprisePopupWindow.dismiss();
			}
		});
		// 取消
		enterprise_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				enterprisePopupWindow.dismiss();
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
				if (imgStatus == 1) {
					// 营业执照
					enterpriseimageFile = new File(enterpriseTempFileDir
							+ "Businesslicense.jpg");
				} else if (imgStatus == 2) {
					// 授权书
					enterpriseimageFile = new File(enterpriseTempFileDir
							+ "Attorneylicense.jpg");
				} else if (imgStatus == 3) {
					// 手持身份证
					enterpriseimageFile = new File(enterpriseTempFileDir
							+ "WithCardlicense.jpg");
				}
			}

			break;

		case 2:
			// 行驶证图库返回
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				String realPathFromURI = FileUtils
						.getRealPathFromURI(uri, this);
				if (TextUtils.isEmpty(realPathFromURI)) {
					enterpriseimageFile = null;
				} else {
					if (imgStatus == 1) {
						// 营业执照
						enterpriseimageFile = new File(enterpriseTempFileDir
								+ "Businesslicense.jpg");
					} else if (imgStatus == 2) {
						// 授权书
						enterpriseimageFile = new File(enterpriseTempFileDir
								+ "Attorneylicense.jpg");
					} else if (imgStatus == 3) {
						// 手持身份证
						enterpriseimageFile = new File(enterpriseTempFileDir
								+ "WithCardlicense.jpg");
					}
					File temFile = new File(realPathFromURI);

					FileUtils.copyFile(temFile, enterpriseimageFile);
				}
			}
			break;

		default:
			break;
		}

		// 判断本地文件是否存在
		if (enterpriseimageFile == null) {
			return;
		}

		// TODO
		byte[] bytes = handleImage(enterpriseimageFile, 800, 800);

		if (bytes == null || bytes.length == 0) {
			return;
		}
		int degree = ImageUtil.readPictureDegree(enterpriseimageFile
				.getAbsolutePath());

		imgEnterpriseBase64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
		Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		ImageUtil.rotaingImageView(degree, bm);
		// 显示拍照或选图后的行驶证
		if (bm != null) {
			mEnterprisePhoto.setImageBitmap(bm);
		}

		// base64解码
		byte[] decode = Base64.decode(imgEnterpriseBase64, Base64.NO_WRAP);

		// TODO--将图片保存在SD卡
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = new File(enterpriseTempFileDir + "imgBaseEnterprise.jpg");
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
	public static void invoke(Context activity, String imgUrl, int imgStatus) {
		Intent intent = new Intent();
		intent.setClass(activity, EnterpriseUploadPhotoActivity.class);
		intent.putExtra("imgUrl", imgUrl);
		intent.putExtra("imgStatus", imgStatus);
		activity.startActivity(intent);
	}

}
