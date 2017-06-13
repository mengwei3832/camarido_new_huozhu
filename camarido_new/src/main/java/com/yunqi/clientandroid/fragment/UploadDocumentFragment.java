package com.yunqi.clientandroid.fragment;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.yunqi.clientandroid.activity.UploadAndModifyDocumentsActivity;
import com.yunqi.clientandroid.http.request.GetCargoRolRequest;
import com.yunqi.clientandroid.http.request.GetLoadTicketRequest;
import com.yunqi.clientandroid.http.request.GetSignTicketRequest;
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
 * @Description class 当前运单下上传单据的fragment
 * @date 15/12/16
 */
public class UploadDocumentFragment extends BaseFragment implements
		OnClickListener, CaptureActivity.IResultCallBack {

	private String ticketStatus;// 执行状态
	private String ticketId;// 订单id
	private String format;
	// 提货单存放图片文件夹的路径
	private String driverTempFileDir = Environment
			.getExternalStorageDirectory() + "/";
	private String extractImgName = "Extract.jpg";// 保存上传提货单的文件名
	private String shipmentImgName = "Shipment.jpg";// 保存上传装货单的文件名
	private String signReceivingImgName = "SignReceiving.jpg";// 保存上传签收单的文件名
	private String imgDocumentBase64;// 保存照片的字符串
	private Bitmap bm;// 存放图片的bitmap
	private File documentimageFile = null;
	private File file;

	@SuppressWarnings("unused")
	private ProgressBar progressBarHandle;
	private TextView myProssBarhandleText;
	private TextView tvDocument;
	private TextView tvCurrentTime;
	private View parentView;
	private EditText etTicketNumber;
	private EditText etAlltotal;
	private ImageButton ibQrCode;
	private ImageView ivPhotoimg;
	private Button btnSave;
	private PopupWindow documentPpw;
	private UploadAndModifyDocumentsActivity activity;
	private final int MAX_PROGRESS = 100;
	private final int NINE_PROGRESS = 96;
	private final int MIN_PROGRESS = 0;
	private final int PRO = 10;
	private final int ALL = 20;
	private final int FAIL = 30;
	private int progress = 1;
	private int barType = 2;// 是否显示进度条2 不显示 3 显示
	private TextView tvUploadBao;

	// 本页请求
	private GetCargoRolRequest mGetCargoRolRequest;
	private GetLoadTicketRequest mGetLoadTicketRequest;
	private GetSignTicketRequest mGetSignTicketRequest;

	// 本页请求id
	private final int GET_CARGOROL_REQUEST = 1;
	private final int GET_LOADTICKET_REQUEST = 2;
	private final int GET_SIGNTICKET_REQUEST = 3;

	// handler处理进度条
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
	protected void initData() {
		// TODO Auto-generated method stub
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_uploaddocument;
	}

	@Override
	protected void initView(View uploadDocumentView) {
		// 获取当前的activity
		activity = (UploadAndModifyDocumentsActivity) getActivity();

		ticketId = ((UploadAndModifyDocumentsActivity) getActivity())
				.getTicketId();
		ticketStatus = ((UploadAndModifyDocumentsActivity) getActivity())
				.getTicketStatus();

		parentView = LayoutInflater.from(this.getActivity()).inflate(
				R.layout.fragment_uploaddocument, null);

		// 初始化时间
		initTime();

		tvDocument = (TextView) uploadDocumentView
				.findViewById(R.id.tv_uploadfrag_document);
		tvCurrentTime = (TextView) uploadDocumentView
				.findViewById(R.id.tv_uploadfrag_currentTime);
		etTicketNumber = (EditText) uploadDocumentView
				.findViewById(R.id.et_uploadfrag_ticketcount);
		ibQrCode = (ImageButton) uploadDocumentView
				.findViewById(R.id.ib_uploadfrag_qrcode);
		etAlltotal = (EditText) uploadDocumentView
				.findViewById(R.id.et_uploadfrag_alltotal);
		ivPhotoimg = (ImageView) uploadDocumentView
				.findViewById(R.id.iv_uploadfrag_photoimg);
		btnSave = (Button) uploadDocumentView
				.findViewById(R.id.btn_uploadfrag_save);
		progressBarHandle = (ProgressBar) uploadDocumentView
				.findViewById(R.id.pb_uploadfrag_myProssBarhandle);
		myProssBarhandleText = (TextView) uploadDocumentView
				.findViewById(R.id.tv_uploadfrag_myProssBarhandleText);
		tvUploadBao = (TextView) uploadDocumentView
				.findViewById(R.id.tv_uploadfrag_baoxian_shuoming);

		// 显示当前日期
		if (!TextUtils.isEmpty(format) && format != null) {
			tvCurrentTime.setText(format.split(" ")[0]);
		}

		int insuranceType = UploadAndModifyDocumentsActivity.cInsuranceType;

		Log.e("TAG", "-------是否带保险---------" + insuranceType);

		// 根据执行状态显示需要上传的单据
		if (ticketStatus != null && ticketStatus.equals("2")) {
			ivPhotoimg.setImageResource(R.drawable.uploaddoc_pickup);
			tvUploadBao.setVisibility(View.GONE);
		} else if (ticketStatus != null && ticketStatus.equals("3")) {
			if (insuranceType == 0) {
				tvUploadBao.setVisibility(View.GONE);
			} else if (insuranceType == 1 || insuranceType == 2) {
				tvUploadBao.setVisibility(View.VISIBLE);
				tvUploadBao.setText("*本运单带保险，请准确填写矿发数据并上传清晰单据图片，以确保保单有效");
			}
			ivPhotoimg.setImageResource(R.drawable.uploaddoc_shipment);
		} else if (ticketStatus != null && ticketStatus.equals("4")) {
			tvUploadBao.setVisibility(View.GONE);
			ivPhotoimg.setImageResource(R.drawable.uploaddoc_sign);
		}

	}

	@Override
	public void onResume() {
		super.onResume();

		if (barType == 3) {
			// 只有上传图片才显示进度条
			if (bm != null) {
				handler.sendEmptyMessage(ALL);// 发送空消息
				progressBarHandle.setVisibility(View.VISIBLE);
				myProssBarhandleText.setVisibility(View.VISIBLE);

				ivPhotoimg.setImageBitmap(bm);
			}

			// 设置保存按钮不可点击
			ibQrCode.setEnabled(false);
			ivPhotoimg.setEnabled(false);
			btnSave.setEnabled(false);
		} else {
			progressBarHandle.setVisibility(View.GONE);
			myProssBarhandleText.setVisibility(View.GONE);
		}
	}

	@Override
	protected void setListener() {
		// 初始化点击事件
		initOnClick();
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
		case R.id.ib_uploadfrag_qrcode:
			// 扫描二维码
			CaptureActivity.invoke(getActivity(), this);
			break;

		case R.id.iv_uploadfrag_photoimg:
			// 上传照片
			showDocumentPhotoPupopWindow();
			break;

		case R.id.btn_uploadfrag_save:
			// 保存资料
			String ticketRelationCode = etTicketNumber.getText().toString()
					.trim();
			String ticketWeight = etAlltotal.getText().toString().trim();

			// 将单据号码和总吨数缓存
			CacheUtils.putString(getActivity(), "TICKET_CODE",
					ticketRelationCode);
			CacheUtils.putString(getActivity(), "TICKET_WEIGHT", ticketWeight);

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

			if (ticketStatus != null && ticketStatus.equals("2")) {
				// 请求待换票接口
				if (!TextUtils.isEmpty(ticketId) && ticketId != null) {
					GetCargoRolRequest(ticketId, ticketRelationCode,
							ticketWeight, imgDocumentBase64, extractImgName);
				}
			} else if (ticketStatus != null && ticketStatus.equals("3")) {
				// 请求待装运接口
				if (!TextUtils.isEmpty(ticketId) && ticketId != null) {
					GetLoadTicketRequest(ticketId, ticketRelationCode,
							ticketWeight, imgDocumentBase64, shipmentImgName);
				}
			} else if (ticketStatus != null && ticketStatus.equals("4")) {
				// 请求待收货接口
				if (!TextUtils.isEmpty(ticketId) && ticketId != null) {
					GetSignTicketRequest(ticketId, ticketRelationCode,
							ticketWeight, imgDocumentBase64,
							signReceivingImgName);
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

	// 上传签收单据的方法
	private void GetSignTicketRequest(String ticketId,
			String ticketRelationCode, String ticketWeight,
			String ticketOperationPicUrl, String ticketOperationPicName) {
		mGetSignTicketRequest = new GetSignTicketRequest(getActivity(),
				ticketId, ticketRelationCode, ticketWeight,
				ticketOperationPicUrl, ticketOperationPicName);
		mGetSignTicketRequest.setRequestId(GET_SIGNTICKET_REQUEST);
		httpPost(mGetSignTicketRequest);
		// 设置保存按钮不可点击
		setViewEnable(false);
		// 只有上传图片才显示进度条
		if (bm != null) {
			// 显示进度条
			showProgressBar();
		}
	}

	// 上传装货单据的方法
	private void GetLoadTicketRequest(String ticketId,
			String ticketRelationCode, String ticketWeight,
			String ticketOperationPicUrl, String ticketOperationPicName) {
		mGetLoadTicketRequest = new GetLoadTicketRequest(getActivity(),
				ticketId, ticketRelationCode, ticketWeight,
				ticketOperationPicUrl, ticketOperationPicName);
		mGetLoadTicketRequest.setRequestId(GET_LOADTICKET_REQUEST);
		httpPost(mGetLoadTicketRequest);
		// 设置保存按钮不可点击
		setViewEnable(false);
		// 只有上传图片才显示进度条
		if (bm != null) {
			// 显示进度条
			showProgressBar();
		}
	}

	// 上传提货单据的方法
	private void GetCargoRolRequest(String ticketId, String ticketRelationCode,
			String ticketWeight, String ticketOperationPicUrl,
			String ticketOperationPicName) {
		mGetCargoRolRequest = new GetCargoRolRequest(getActivity(), ticketId,
				ticketRelationCode, ticketWeight, ticketOperationPicUrl,
				ticketOperationPicName);
		mGetCargoRolRequest.setRequestId(GET_CARGOROL_REQUEST);
		httpPost(mGetCargoRolRequest);
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
		case GET_CARGOROL_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 上传提货单据成功
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				barType = 3;// 显示进度条
				handler.sendEmptyMessage(ALL);// 发送空消息

				// 删除存放图片文件夹
				documentimageFile = new File(driverTempFileDir + "Extract.jpg");
				if (documentimageFile.exists() && documentimageFile.isFile()) {
					documentimageFile.delete();
				}

				// 跳转到过程界面
				enterModifyDocument();
			} else {
				// 上传提货单据失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				barType = 2;// 不显示进度条
				handler.sendEmptyMessage(FAIL);// 发送空消息
				// 设置保存按钮可点击
				setViewEnable(true);

			}
			break;

		case GET_LOADTICKET_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				// 上传装货单据成功
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				barType = 3;// 显示进度条
				handler.sendEmptyMessage(ALL);// 发送空消息

				// 删除存放图片文件夹
				documentimageFile = new File(driverTempFileDir + "Shipment.jpg");
				if (documentimageFile.exists() && documentimageFile.isFile()) {
					documentimageFile.delete();
				}

				// 跳转到过程界面
				enterModifyDocument();

			} else {
				// 上传装货单据失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				barType = 2;// 不显示进度条
				handler.sendEmptyMessage(FAIL);// 发送空消息
				// 设置保存按钮可点击
				setViewEnable(true);

			}
			break;

		case GET_SIGNTICKET_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				// 上传签收单据成功
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				barType = 3;// 显示进度条
				handler.sendEmptyMessage(ALL);// 发送空消息

				// 删除存放图片文件夹
				documentimageFile = new File(driverTempFileDir
						+ "SignReceiving.jpg");
				if (documentimageFile.exists() && documentimageFile.isFile()) {
					documentimageFile.delete();
				}

				// 跳转到过程界面
				enterModifyDocument();
			} else {
				// 上传签收单据失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}

				barType = 2;// 不显示进度条
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
		showToast("连接超时,请检查网络");
		handler.sendEmptyMessage(FAIL);// 发送空消息
		// 设置保存按钮可点击
		setViewEnable(true);
	}

	// 切换到过程界面
	private void enterModifyDocument() {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction beginTransaction = fragmentManager
				.beginTransaction();

		// 当前按钮被选中
		activity.mModify.setChecked(true);
		// 切换到当前运单界面
		if (activity.modifyDocumentFragment == null) {
			activity.modifyDocumentFragment = new ModifyDocumentFragment();
		}
		beginTransaction.replace(R.id.fl_uploadmodify_container,
				activity.modifyDocumentFragment).commit();
	}

	// 上传照片的PPW
	private void showDocumentPhotoPupopWindow() {
		documentPpw = new PopupWindow(getActivity());
		View documentPhoto = LayoutInflater.from(this.getActivity()).inflate(
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
		if (ticketStatus != null && ticketStatus.equals("2")) {
			// 请求待换票接口

		} else if (ticketStatus != null && ticketStatus.equals("3")) {
			// 请求待装运接口

		} else if (ticketStatus != null && ticketStatus.equals("4")) {
			// 请求待收货接口

		}

		// 拍照
		btnPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				if (ticketStatus != null && ticketStatus.equals("2")) {
					// 请求待换票接口
					file = new File(driverTempFileDir + "Extract.jpg");
				} else if (ticketStatus != null && ticketStatus.equals("3")) {
					// 请求待装运接口
					file = new File(driverTempFileDir + "Shipment.jpg");
				} else if (ticketStatus != null && ticketStatus.equals("4")) {
					// 请求待收货接口
					file = new File(driverTempFileDir + "SignReceiving.jpg");
				}

				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						Log.e(PickImage.class.getSimpleName(),
								"[pickImageFromCamera]", e);
					}
				}

				// 下面这句指定调用相机拍照后的照片存储的路径
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				startActivityForResult(intent, 1);
				// 取消对话框
				documentPpw.dismiss();
			}
		});

		// 图库选图
		btnSelect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, 2);
				// 取消对话框
				documentPpw.dismiss();
			}
		});

		// 取消
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View V) {
				// 取消对话框
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			// 拍照返回
			if (resultCode == getActivity().RESULT_OK) {
				if (ticketStatus != null && ticketStatus.equals("2")) {
					// 请求待换票接口
					documentimageFile = new File(driverTempFileDir
							+ "Extract.jpg");
				} else if (ticketStatus != null && ticketStatus.equals("3")) {
					// 请求待装运接口
					documentimageFile = new File(driverTempFileDir
							+ "Shipment.jpg");
				} else if (ticketStatus != null && ticketStatus.equals("4")) {
					// 请求待收货接口
					documentimageFile = new File(driverTempFileDir
							+ "SignReceiving.jpg");
				}
			}

			break;

		case 2:
			// 图库返回
			if (resultCode == getActivity().RESULT_OK) {
				Uri uri = data.getData();
				String realPathFromURI = FileUtils.getRealPathFromURI(uri,
						getActivity());
				if (TextUtils.isEmpty(realPathFromURI)) {
					documentimageFile = null;
				} else {
					if (ticketStatus != null && ticketStatus.equals("2")) {
						// 请求待换票接口
						documentimageFile = new File(driverTempFileDir
								+ "Extract.jpg");
					} else if (ticketStatus != null && ticketStatus.equals("3")) {
						// 请求待装运接口
						documentimageFile = new File(driverTempFileDir
								+ "Shipment.jpg");
					} else if (ticketStatus != null && ticketStatus.equals("4")) {
						// 请求待收货接口
						documentimageFile = new File(driverTempFileDir
								+ "SignReceiving.jpg");
					}

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

		// TODO--调试图片大小和质量
		byte[] bytes = handleImage(documentimageFile, 800, 800);

		if (bytes == null || bytes.length == 0) {
			return;
		}
		int degree = ImageUtil.readPictureDegree(documentimageFile
				.getAbsolutePath());

		imgDocumentBase64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
		bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		ImageUtil.rotaingImageView(degree, bm);

		// 设置拍好的照片
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

}
