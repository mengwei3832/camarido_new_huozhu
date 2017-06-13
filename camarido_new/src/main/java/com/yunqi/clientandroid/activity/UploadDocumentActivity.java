package com.yunqi.clientandroid.activity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.http.request.GetCargoRolAgainRequest;
import com.yunqi.clientandroid.http.request.GetLoadTicketAgainRequest;
import com.yunqi.clientandroid.http.request.GetSignTicketAgainRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CacheUtils;
import com.yunqi.clientandroid.utils.FileUtils;
import com.yunqi.clientandroid.utils.ImageUtil;
import com.yunqi.clientandroid.utils.PickImage;
import com.yunqi.zbarlibrary.CaptureActivity;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 当前运单下重新上传单据
 * @date 15/12/2
 */
public class UploadDocumentActivity extends BaseActivity implements
		OnClickListener, CaptureActivity.IResultCallBack {

	private ProgressBar progressBarHandle;
	private TextView myProssBarhandleText;
	private View parentView;
	private String ticketOperationType;// 执行状态
	private String ticketId;// 订单id
	private TextView tvDocument;
	private TextView tvCurrentTime;
	private EditText etTicketNumber;
	private EditText etAlltotal;
	private ImageButton ibQrCode;
	private ImageView ivPhotoimg;
	private Button btnSave;
	private String format;
	private PopupWindow documentPpw;
	private boolean isAgain;
	private TextView tvUploadbao;
	// 提货单存放图片文件夹的路径
	private String driverTempFileDir = Environment
			.getExternalStorageDirectory() + "/";

	private File documentimageFile = null;
	private String documentImgName = "Document.jpg";// 保存拍照的文件名
	private String imgDocumentBase64;// 保存照片的字符串
	private Bitmap bm;// 存放图片的Bitmap

	// 本页请求
	private GetCargoRolAgainRequest mGetCargoRolAgainRequest;
	private GetLoadTicketAgainRequest mGetLoadTicketAgainRequest;
	private GetSignTicketAgainRequest mGetSignTicketAgainRequest;

	private final int MAX_PROGRESS = 100;
	private final int NINE_PROGRESS = 96;
	private final int MIN_PROGRESS = 0;
	private final int PRO = 10;
	private final int ALL = 20;
	private final int FAIL = 30;
	private int progress = 1;

	// 本页请求id
	private final int GET_CARGOROLAGAIN_REQUEST = 1;
	private final int GET_LOADTICKETAGAIN_REQUEST = 2;
	private final int GET_SIGNTICKETAGAIN_REQUEST = 3;

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
		return R.layout.activity_uploaddocument;
	}

	@Override
	protected void initView() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		// 获取从当前运单界面传过来的数据
		if (bundle != null && bundle.containsKey("ticketOperationType")) {
			ticketOperationType = bundle.getString("ticketOperationType");
		}
		if (bundle != null && bundle.containsKey("ticketId")) {
			ticketId = bundle.getString("ticketId");
		}
		if (bundle != null && bundle.containsKey("isAgain")) {
			isAgain = bundle.getBoolean("isAgain");
		}

		// 初始化titileBar
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
		progressBarHandle = (ProgressBar) findViewById(R.id.pb_uploaddoc_myProssBarhandle);
		myProssBarhandleText = (TextView) findViewById(R.id.tv_uploaddoc_myProssBarhandleText);
		tvUploadbao = (TextView) findViewById(R.id.tv_uploaddoc_baoxian_shuoming);

		// 显示当前日期
		if (!TextUtils.isEmpty(format) && format != null) {
			tvCurrentTime.setText(format.split(" ")[0] + "");
		}

		// 获得缓存中的数据,并对输入框进行赋值
		String tTicketCode = CacheUtils.getString(UploadDocumentActivity.this,
				"TICKET_CODE", "");
		String tTicketWeight = CacheUtils.getString(
				UploadDocumentActivity.this, "TICKET_WEIGHT", "");

		etTicketNumber.setText(tTicketCode);
		etAlltotal.setText(tTicketWeight);

		// 获取保险
		int insuranceType = UploadAndModifyDocumentsActivity.cInsuranceType;

		Log.e("TAG", "---------是否有保险-----------" + insuranceType);

		// 根据执行状态显示需要上传的单据
		if (ticketOperationType != null && ticketOperationType.equals("20")) {
			ivPhotoimg.setImageResource(R.drawable.uploaddoc_pickup);
			tvUploadbao.setVisibility(View.GONE);
		} else if (ticketOperationType != null
				&& ticketOperationType.equals("30")) {
			if (insuranceType == 0) {
				tvUploadbao.setVisibility(View.GONE);
			} else if (insuranceType == 1 || insuranceType == 2) {
				tvUploadbao.setVisibility(View.VISIBLE);
				tvUploadbao
						.setText("*本运单已投保，修改发运数据，可能导致保单失效，如有修改必要建议在货物到达后修改发运数据");
			}
			ivPhotoimg.setImageResource(R.drawable.uploaddoc_shipment);
		} else if (ticketOperationType != null
				&& ticketOperationType.equals("40")) {
			tvUploadbao.setVisibility(View.GONE);
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

	// 初始化titileBar的方法
	private void initActionBar() {
		if (ticketOperationType != null && ticketOperationType.equals("20")) {
			setActionBarTitle("待换票");
		} else if (ticketOperationType != null
				&& ticketOperationType.equals("30")) {
			setActionBarTitle("待装运");
		} else if (ticketOperationType != null
				&& ticketOperationType.equals("40")) {
			setActionBarTitle("待收货");
		}

		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UploadDocumentActivity.this.finish();
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
			CaptureActivity.invoke(UploadDocumentActivity.this,
					UploadDocumentActivity.this);

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

			// if (TextUtils.isEmpty(imgDocumentBase64)) {
			// showToast("请上传单据照片");
			// return;
			// }

			if (ticketOperationType != null && ticketOperationType.equals("20")) {
				// 请求待换票接口
				if (!TextUtils.isEmpty(ticketId) && ticketId != null) {
					GetCargoRolAgainRequest(ticketId, ticketRelationCode,
							ticketWeight, imgDocumentBase64, documentImgName);
				}
			} else if (ticketOperationType != null
					&& ticketOperationType.equals("30")) {
				// 请求待装运接口
				if (!TextUtils.isEmpty(ticketId) && ticketId != null) {
					GetLoadTicketAgainRequest(ticketId, ticketRelationCode,
							ticketWeight, imgDocumentBase64, documentImgName);
				}
			} else if (ticketOperationType != null
					&& ticketOperationType.equals("40")) {
				// 请求待收货接口
				if (!TextUtils.isEmpty(ticketId) && ticketId != null) {
					GetSignTicketAgainRequest(ticketId, ticketRelationCode,
							ticketWeight, imgDocumentBase64, documentImgName);
				}

			}

			break;

		default:
			break;
		}
	}

	// 显示进度条的方法
	private void showProgressBar() {
		progressBarHandle.setVisibility(View.VISIBLE);
		myProssBarhandleText.setVisibility(View.VISIBLE);
		progressBarHandle.setMax(MAX_PROGRESS);// 设置最大进度

		progress = (progress > 0) ? progress : 0;
		progressBarHandle.setProgress(progress);
		handler.sendEmptyMessage(PRO);// 发送空消息
	}

	// 设置保存按钮不可重复点击
	private void setViewEnable(boolean bEnable) {
		if (bEnable) {
			btnSave.setText("保存");
		} else {
			btnSave.setText("保存中 ...");
		}
		btnSave.setEnabled(bEnable);
		ivPhotoimg.setEnabled(bEnable);
		ibQrCode.setEnabled(bEnable);
		etTicketNumber.setFocusable(bEnable);
		etAlltotal.setFocusable(bEnable);

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

				handler.sendEmptyMessage(ALL);// 发送空消息

				// 删除存放图片文件夹
				documentimageFile = new File(driverTempFileDir + "Document.jpg");
				if (documentimageFile.exists() && documentimageFile.isFile()) {
					documentimageFile.delete();
				}

				// 关闭当前页面
				UploadDocumentActivity.this.finish();
			} else {
				// 重新上传提货单据失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				handler.sendEmptyMessage(FAIL);// 发送空消息

				// 设置保存按钮可点击
				setViewEnable(true);
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

				handler.sendEmptyMessage(ALL);// 发送空消息

				// 删除存放图片文件夹
				documentimageFile = new File(driverTempFileDir + "Document.jpg");
				if (documentimageFile.exists() && documentimageFile.isFile()) {
					documentimageFile.delete();
				}

				// 关闭当前页面
				UploadDocumentActivity.this.finish();
			} else {
				// 重新上传装运单据失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				handler.sendEmptyMessage(FAIL);// 发送空消息
				// 设置保存按钮可点击
				setViewEnable(true);
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

				handler.sendEmptyMessage(ALL);// 发送空消息

				// 删除存放图片文件夹
				documentimageFile = new File(driverTempFileDir + "Document.jpg");
				if (documentimageFile.exists() && documentimageFile.isFile()) {
					documentimageFile.delete();
				}

				// 关闭当前页面
				UploadDocumentActivity.this.finish();
			} else {
				// 重新上传签收单据失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				handler.sendEmptyMessage(FAIL);// 发送空消息
				// 设置保存按钮可点击
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
		showToast(this.getResources().getString(R.string.net_error_toast));

		// 设置保存按钮可点击
		setViewEnable(true);
		handler.sendEmptyMessage(FAIL);// 发送空消息
	}

	// 上传签收单据的方法
	private void GetSignTicketAgainRequest(String ticketId,
			String ticketRelationCode, String ticketWeight,
			String ticketOperationPicUrl, String ticketOperationPicName) {
		mGetSignTicketAgainRequest = new GetSignTicketAgainRequest(mContext,
				ticketId, ticketRelationCode, ticketWeight,
				ticketOperationPicUrl, ticketOperationPicName);
		mGetSignTicketAgainRequest.setRequestId(GET_SIGNTICKETAGAIN_REQUEST);
		httpPost(mGetSignTicketAgainRequest);
		// 设置保存按钮不可点击
		setViewEnable(false);
		// 只有上传图片才显示进度条
		if (bm != null) {
			// 显示进度条
			showProgressBar();
		}
	}

	// 上传装货单据的方法
	private void GetLoadTicketAgainRequest(String ticketId,
			String ticketRelationCode, String ticketWeight,
			String ticketOperationPicUrl, String ticketOperationPicName) {
		mGetLoadTicketAgainRequest = new GetLoadTicketAgainRequest(mContext,
				ticketId, ticketRelationCode, ticketWeight,
				ticketOperationPicUrl, ticketOperationPicName);
		mGetLoadTicketAgainRequest.setRequestId(GET_LOADTICKETAGAIN_REQUEST);
		httpPost(mGetLoadTicketAgainRequest);
		// 设置保存按钮不可点击
		setViewEnable(false);
		// 只有上传图片才显示进度条
		if (bm != null) {
			// 显示进度条
			showProgressBar();
		}
	}

	// 上传提货单据的方法
	private void GetCargoRolAgainRequest(String ticketId,
			String ticketRelationCode, String ticketWeight,
			String ticketOperationPicUrl, String ticketOperationPicName) {
		mGetCargoRolAgainRequest = new GetCargoRolAgainRequest(mContext,
				ticketId, ticketRelationCode, ticketWeight,
				ticketOperationPicUrl, ticketOperationPicName);
		mGetCargoRolAgainRequest.setRequestId(GET_CARGOROLAGAIN_REQUEST);
		httpPost(mGetCargoRolAgainRequest);
		// 设置保存按钮不可点击
		setViewEnable(false);
		// 只有上传图片才显示进度条
		if (bm != null) {
			// 显示进度条
			showProgressBar();
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

	// 上传照片的PPW
	private void showDocumentPhotoPupopWindow() {
		documentPpw = new PopupWindow(UploadDocumentActivity.this);
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
		if (ticketOperationType != null && ticketOperationType.equals("20")) {

		} else if (ticketOperationType != null
				&& ticketOperationType.equals("30")) {

		} else if (ticketOperationType != null
				&& ticketOperationType.equals("40")) {

		}

		// 拍照
		btnPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PickImage.pickImageFromCamera(UploadDocumentActivity.this,
						driverTempFileDir + "Document.jpg", 1);
				documentPpw.dismiss();
			}
		});

		// 图库选图
		btnSelect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PickImage.pickImageFromPhoto(UploadDocumentActivity.this, 2);
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

		// TODO--图片调试
		byte[] bytes = handleImage(documentimageFile, 800, 800);

		if (bytes == null || bytes.length == 0) {
			return;
		}
		int degree = ImageUtil.readPictureDegree(documentimageFile
				.getAbsolutePath());

		imgDocumentBase64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
		bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		ImageUtil.rotaingImageView(degree, bm);

		// 显示图片
		if (bm != null) {
			ivPhotoimg.setImageBitmap(bm);
		}

		// base64解码
		byte[] decode = Base64.decode(imgDocumentBase64, Base64.NO_WRAP);

		// TODO--将图片保存在SD卡
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = new File(driverTempFileDir + "imgBaseDocument.jpg");
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

	// 当前界面的跳转方法
	public static void invoke(Context activity, String ticketId,
			String ticketOperationType) {
		Intent intent = new Intent();
		intent.setClass(activity, UploadDocumentActivity.class);
		intent.putExtra("ticketId", ticketId);
		intent.putExtra("ticketOperationType", ticketOperationType);
		activity.startActivity(intent);
	}

}
