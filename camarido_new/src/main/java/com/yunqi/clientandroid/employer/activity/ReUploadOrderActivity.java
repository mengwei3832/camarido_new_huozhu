package com.yunqi.clientandroid.employer.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.request.UploadCargoRolAgainRequest;
import com.yunqi.clientandroid.employer.request.UploadSignTicketAgainRequest;
import com.yunqi.clientandroid.employer.request.UploadTicketAgainRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.FileUtils;
import com.yunqi.clientandroid.utils.ImageUtil;
import com.yunqi.clientandroid.utils.PickImage;
import com.yunqi.zbarlibrary.CaptureActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 发包方当前运单下重新上传单据
 * @date 15/12/2
 */
public class ReUploadOrderActivity extends BaseActivity implements
		OnClickListener, CaptureActivity.IResultCallBack {

	private View parentView;
	private int ticketOperationType;// 执行状态
	private int ticketId;// 订单id
	private TextView tvDocument;
	private TextView tvCurrentTime;
	private EditText etTicketNumber;
	private ImageButton ibQrCode;
	private EditText etAlltotal;
	private ImageView ivPhotoimg;
	private String format;
	private Button btnSave;
	private PopupWindow documentPpw;
	// 提货单存放图片文件夹的路径
	private String driverTempFileDir = Environment
			.getExternalStorageDirectory() + "/";

	private File documentimageFile = null;
	private String documentImgName = "Document.jpg";// 保存拍照的文件名
	private String imgDocumentBase64;// 保存照片的字符串

	// 本页请求
	private UploadCargoRolAgainRequest mGetCargoRolAgainRequest;
	private UploadTicketAgainRequest mGetLoadTicketAgainRequest;
	private UploadSignTicketAgainRequest mGetSignTicketAgainRequest;

	// 本页请求id
	private final int GET_CARGOROLAGAIN_REQUEST = 1;
	private final int GET_LOADTICKETAGAIN_REQUEST = 2;
	private final int GET_SIGNTICKETAGAIN_REQUEST = 3;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_uploaddocument;
	}

	@Override
	protected void initView() {

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		// 获取从当前运单界面传过来的数据
		if (bundle != null && bundle.containsKey("ticketOperationType")) {
			ticketOperationType = bundle.getInt("ticketOperationType");
		}
		if (bundle != null && bundle.containsKey("ticketId")) {
			ticketId = bundle.getInt("ticketId");
		}

		// 初始化标题栏
		initActionBar();

		parentView = getLayoutInflater().inflate(
				R.layout.activity_uploaddocument, null);

		// 初始化时间
		initTime();

		tvDocument = (TextView) findViewById(R.id.tv_uploaddoc_document);
		tvCurrentTime = (TextView) findViewById(R.id.tv_uploaddoc_currentTime);
		etTicketNumber = (EditText) findViewById(R.id.et_uploaddoc_ticketcount);
		ibQrCode = (ImageButton) findViewById(R.id.ib_uploaddoc_qrcode);
		etAlltotal = (EditText) findViewById(R.id.et_uploaddoc_alltotal);
		ivPhotoimg = (ImageView) findViewById(R.id.iv_uploaddoc_photoimg);
		btnSave = (Button) findViewById(R.id.btn_uploaddoc_save);

		// 显示当前日期
		if (!TextUtils.isEmpty(format)) {
			tvCurrentTime.setText(format.split(" ")[0] + "");
		}

		// 根据执行状态显示需要上传的单据
		if (ticketOperationType == 20) {
			ivPhotoimg.setImageResource(R.drawable.uploaddoc_pickup);
		} else if (ticketOperationType == 30) {
			ivPhotoimg.setImageResource(R.drawable.uploaddoc_shipment);
		} else if (ticketOperationType == 40) {
			ivPhotoimg.setImageResource(R.drawable.uploaddoc_sign);
		}
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void setListener() {
		// 初始化点击事件
		initOnClick();
	}

	// 初始化标题栏
	private void initActionBar() {
		if (ticketOperationType == 20) {
			setActionBarTitle("待换票");
		} else if (ticketOperationType == 30) {
			setActionBarTitle("待装运");
		} else if (ticketOperationType == 40) {
			setActionBarTitle("待收货");
		}

		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ReUploadOrderActivity.this.finish();
			}
		});

	}

	// 初始化点击事件
	private void initOnClick() {
		ibQrCode.setOnClickListener(this);
		ivPhotoimg.setOnClickListener(this);
		btnSave.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_uploaddoc_qrcode:
			// 扫描二维码
			CaptureActivity.invoke(ReUploadOrderActivity.this,
					ReUploadOrderActivity.this);

			break;

		case R.id.iv_uploaddoc_photoimg:
			// 上传照片
			showDocumentPhotoPupopWindow();
			break;

		case R.id.btn_uploaddoc_save:
			// 保存资料
			String ticketRelationCode = etTicketNumber.getText().toString()
					.trim();
			String ticketWeight = etAlltotal.getText().toString().trim();

			if (TextUtils.isEmpty(ticketRelationCode)) {
				showToast("请输入单据号码");
				return;
			}

			if (TextUtils.isEmpty(ticketWeight)) {
				showToast("请输入总吨数");
				return;
			}

			if (TextUtils.isEmpty(imgDocumentBase64)) {
				showToast("请上传单据照片");
				return;
			}

			if (ticketOperationType == 20) {
				// 请求待换票接口
				GetCargoRolAgainRequest(ticketId, ticketRelationCode,
						ticketWeight, imgDocumentBase64, documentImgName);
			} else if (ticketOperationType == 30) {
				// 请求待装运接口
				GetLoadTicketAgainRequest(ticketId, ticketRelationCode,
						ticketWeight, imgDocumentBase64, documentImgName);
			} else if (ticketOperationType == 40) {
				// 请求待收货接口
				GetSignTicketAgainRequest(ticketId, ticketRelationCode,
						ticketWeight, imgDocumentBase64, documentImgName);

			}

			// 设置保存按钮不可点击
			btnSave.setEnabled(false);

			break;

		default:
			break;
		}
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		boolean isSuccess;
		String message;
		switch (requestId) {
		case GET_CARGOROLAGAIN_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				// 重新上传提货单据成功
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 关闭当前页面
				ReUploadOrderActivity.this.finish();
			} else {
				// 重新上传提货单据失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 设置保存按钮可点击
				btnSave.setEnabled(true);
			}
			break;

		case GET_LOADTICKETAGAIN_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				// 重新上传装运单据成功
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
				// 关闭当前页面
				ReUploadOrderActivity.this.finish();
			} else {
				// 重新上传装运单据失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 设置保存按钮可点击
				btnSave.setEnabled(true);
			}
			break;

		case GET_SIGNTICKETAGAIN_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				// 重新上传签收单据成功
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 关闭当前页面
				ReUploadOrderActivity.this.finish();
			} else {
				// 重新上传签收单据失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				// 设置保存按钮可点击
				btnSave.setEnabled(true);
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(this.getResources().getString(R.string.net_error_toast));
		// 设置保存按钮可点击
		btnSave.setEnabled(true);
	}

	// 上传签收单据的方法
	private void GetSignTicketAgainRequest(int ticketId,
			String ticketRelationCode, String ticketWeight,
			String ticketOperationPicUrl, String ticketOperationPicName) {
		mGetSignTicketAgainRequest = new UploadSignTicketAgainRequest(mContext,
				ticketId, ticketRelationCode, ticketWeight,
				ticketOperationPicUrl, ticketOperationPicName);
		mGetSignTicketAgainRequest.setRequestId(GET_SIGNTICKETAGAIN_REQUEST);
		httpPost(mGetSignTicketAgainRequest);
	}

	// 上传装货单据的方法
	private void GetLoadTicketAgainRequest(int ticketId,
			String ticketRelationCode, String ticketWeight,
			String ticketOperationPicUrl, String ticketOperationPicName) {
		mGetLoadTicketAgainRequest = new UploadTicketAgainRequest(mContext,
				ticketId, ticketRelationCode, ticketWeight,
				ticketOperationPicUrl, ticketOperationPicName);
		mGetLoadTicketAgainRequest.setRequestId(GET_LOADTICKETAGAIN_REQUEST);
		httpPost(mGetLoadTicketAgainRequest);
	}

	// 上传提货单据的方法
	private void GetCargoRolAgainRequest(int ticketId,
			String ticketRelationCode, String ticketWeight,
			String ticketOperationPicUrl, String ticketOperationPicName) {
		mGetCargoRolAgainRequest = new UploadCargoRolAgainRequest(mContext,
				ticketId, ticketRelationCode, ticketWeight,
				ticketOperationPicUrl, ticketOperationPicName);
		mGetCargoRolAgainRequest.setRequestId(GET_CARGOROLAGAIN_REQUEST);
		httpPost(mGetCargoRolAgainRequest);

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

	// 上传照片的PPW
	private void showDocumentPhotoPupopWindow() {
		documentPpw = new PopupWindow(ReUploadOrderActivity.this);
		View documentPhoto = getLayoutInflater().inflate(
				R.layout.item_documentphoto_popupwindows, null);
		documentPpw.setWidth(LayoutParams.MATCH_PARENT);
		documentPpw.setHeight(LayoutParams.WRAP_CONTENT);
		documentPpw.setBackgroundDrawable(new BitmapDrawable());
		documentPpw.setFocusable(true);
		documentPpw.setOutsideTouchable(true);
		documentPpw.setContentView(documentPhoto);
		documentPpw.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

		RelativeLayout rlParent = (RelativeLayout) documentPhoto
				.findViewById(R.id.rl_documentppw_parent);
		Button btnPhoto = (Button) documentPhoto
				.findViewById(R.id.btn_documentppw_photo);
		Button btnSelect = (Button) documentPhoto
				.findViewById(R.id.btn_documentppw_select);
		Button btnCancel = (Button) documentPhoto
				.findViewById(R.id.btn_documentppw_cancel);

		// TODO--根据执行状态显示拍照的背景图
		if (ticketOperationType == 20) {

		} else if (ticketOperationType == 30) {

		} else if (ticketOperationType == 40) {

		}

		// 拍照
		btnPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PickImage.pickImageFromCamera(ReUploadOrderActivity.this,
						driverTempFileDir + "Document.jpg", 1);
				documentPpw.dismiss();
			}
		});

		// 图库选图
		btnSelect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PickImage.pickImageFromPhoto(ReUploadOrderActivity.this, 2);
				documentPpw.dismiss();
			}
		});

		// 取消
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View V) {
				documentPpw.dismiss();
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

			bm.compress(
					avatarFile.getAbsolutePath().endsWith("jpg") ? Bitmap.CompressFormat.JPEG
							: Bitmap.CompressFormat.PNG, 100, outputStream);

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
				documentimageFile = new File(driverTempFileDir + "Document.jpg");
			}

			break;

		case 2:
			// 图库返回
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				String realPathFromURI = FileUtils
						.getRealPathFromURI(uri, this);
				if (TextUtils.isEmpty(realPathFromURI)) {
					documentimageFile = null;
				} else {
					documentimageFile = new File(driverTempFileDir
							+ "Document.jpg");
					File temFile = new File(realPathFromURI);

					FileUtils.copyFile(temFile, documentimageFile);
				}
			}
			break;

		default:
			break;
		}

		// 判断本地文件是否存在
		if (documentimageFile == null) {
			return;
		}

		byte[] bytes = handleImage(documentimageFile, 500, 400);

		if (bytes == null || bytes.length == 0) {
			return;
		}
		int degree = ImageUtil.readPictureDegree(documentimageFile
				.getAbsolutePath());

		// 替换后服务端接收图片错误
		// imgDriverBase64 = Base64.encodeToString(bytes,
		// Base64.NO_WRAP).replaceAll("[\\n]", "").replaceAll("=", "");
		imgDocumentBase64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
		Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		ImageUtil.rotaingImageView(degree, bm);

		// 显示图片
		if (bm != null) {
			ivPhotoimg.setImageBitmap(bm);
		}

	}

	/**
	 * 获取条形码的值
	 * 
	 * @param result
	 */
	@Override
	public void getResult(String result) {
		if (!TextUtils.isEmpty(result) && result != null) {
			etTicketNumber.setText(result);
		}
	}

	// TODO--当前运单列表界面跳转到上传单据的invoke
	public static void invoke(Context activity, int ticketId,
			int ticketOperationType) {
		Intent intent = new Intent();
		intent.setClass(activity, ReUploadOrderActivity.class);
		intent.putExtra("ticketId", ticketId);
		intent.putExtra("ticketOperationType", ticketOperationType);
		activity.startActivity(intent);
	}

}
