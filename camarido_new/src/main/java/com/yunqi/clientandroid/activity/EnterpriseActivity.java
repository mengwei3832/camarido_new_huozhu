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
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
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
import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.CompanyRegisteredInfo;
import com.yunqi.clientandroid.http.request.GetSetCompanyRegisteredInfoRequest;
import com.yunqi.clientandroid.http.request.SetCompanyRegisteredInfoRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CommonUtil;
import com.yunqi.clientandroid.utils.FileUtils;
import com.yunqi.clientandroid.utils.ImageLoaderOptions;
import com.yunqi.clientandroid.utils.ImageUtil;
import com.yunqi.clientandroid.utils.PickImage;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 企业认证界面
 * @date 15/03/30
 */
public class EnterpriseActivity extends BaseActivity implements OnClickListener {

	private String name;// 姓名
	private String idCode;// 身份证号
	private String idCard;// 显示的隐藏身份证号码
	private String comStatus;// 企业认证状态
	private int imgStatus;// 判断是要上传哪种图片
	private String downLoadAttorney = "<a href='http://yqtms.com/pf/sqwts'>yqtms.com/pf/sqwts</a>";
	private View parentView;
	private TextView tvName;
	private TextView tvIdCard;
	private TextView tvDownload;
	private TextView tvCustomerPhone;
	private TextView tvQuestion;
	private ImageView ivDelete1;
	private ImageView ivBusiness;
	private ImageView ivBusinessfied;
	private ImageView ivAttorney;
	private ImageView ivAttorneyfied;
	private ImageView ivWithCard;
	private ImageView ivWithCardfied;
	private EditText etEnterpriseName;
	private Button btnSubmit;
	private LinearLayout llMsg;
	private LinearLayout llProssBar;
	private TextView myProssBarhandleText;
	private ProgressBar progressBarHandle;
	private LinearLayout mLlGlobal;
	private ProgressBar mProgress;
	private PopupWindow businessPopupWindow;
	private PopupWindow attorneyPopupWindow;
	private PopupWindow withCardPopupWindow;
	private String businessLicenseImgUrl;
	private String businessLicenseImgStatus;
	private String authorizationLetterImgUrl;
	private String authorizationLetterImgStatus;
	private String handIdCodeImgUrl;
	private String handIdCodeImgStatus;
	// 新增字段
	private String eBusinessLicenseImgUrl800;// 营业执照大图
	private String eAuthorizationLetterImgUrl800;// 授权书大图
	private String eHandIdCodeImgUrl800;// 手持身份证大图

	// 存放企业图片文件夹的统一路径
	private String enterpriseTempFileDir = Environment
			.getExternalStorageDirectory() + "/";
	// 营业执照存放图片文件夹的路径
	private String businessImgName = "Businesslicense.jpg";// 营业执照图片名
	private File businessimageFile = null;
	private String imgBusinessBase64;// 存放营业执照图片的字符串

	// 授权书存放图片文件夹的路径
	private String attorneyImgName = "Attorneylicense.jpg";// 授权书图片名
	private File attorneyimageFile = null;
	private String imgAttorneyBase64;// 存放授权书图片的字符串

	// 手持身份证存放图片文件夹的路径
	private String withCardImgName = "WithCardlicense.jpg";// 手持身份证图片名
	private File withCardimageFile = null;
	private String imgWithCardBase64;// 存放手持身份证图片的字符串

	private final int MAX_PROGRESS = 100;
	private final int NINE_PROGRESS = 96;
	private final int MIN_PROGRESS = 0;
	private final int PRO = 10;
	private final int ALL = 20;
	private final int FAIL = 30;
	private int progress = 1;

	// 本页请求的id
	private final int GET_SETCOMPANY_REGISTEREDINFO = 1;
	private final int SET_COMPANYREGISTEREDINFO_REQUEST = 2;

	// 本页请求
	private GetSetCompanyRegisteredInfoRequest mGetSetCompanyRegisteredInfo;
	private SetCompanyRegisteredInfoRequest mSetCompanyRegisteredInfoRequest;

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
				llProssBar.setVisibility(View.GONE);
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (imgStatus == 1 || imgStatus == 2 || imgStatus == 3) {
			getSetCompanyRegisteredInfo();
		}
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_enterprise;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		parentView = getLayoutInflater().inflate(R.layout.activity_enterprise,
				null);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		// 获取从实名认证界面传过来的姓名和身份证号
		if (bundle != null && bundle.containsKey("name")) {
			name = bundle.getString("name");
		}

		if (bundle != null && bundle.containsKey("idCode")) {
			idCode = bundle.getString("idCode");
		}

		tvName = (TextView) findViewById(R.id.tv_enterprise_name);
		tvIdCard = (TextView) findViewById(R.id.tv_enterprise_idcard);
		tvDownload = (TextView) findViewById(R.id.tv_enterprise_download);
		tvCustomerPhone = (TextView) findViewById(R.id.tv_enterprise_customerPhone);
		tvQuestion = (TextView) findViewById(R.id.tv_enterprise_question);
		ivDelete1 = (ImageView) findViewById(R.id.iv_enterprise_delete1);
		ivBusiness = (ImageView) findViewById(R.id.iv_enterprise_business);
		ivBusinessfied = (ImageView) findViewById(R.id.iv_enterprise_businessfied);
		ivAttorney = (ImageView) findViewById(R.id.iv_enterprise_attorney);
		ivAttorneyfied = (ImageView) findViewById(R.id.iv_enterprise_attorneyfied);
		ivWithCard = (ImageView) findViewById(R.id.iv_enterprise_withCard);
		ivWithCardfied = (ImageView) findViewById(R.id.iv_enterprise_withCardfied);
		etEnterpriseName = (EditText) findViewById(R.id.et_enterprise_enterpriseName);
		btnSubmit = (Button) findViewById(R.id.bt_enterprise_submit);
		llMsg = (LinearLayout) findViewById(R.id.ll_enterprise_msg);
		llProssBar = (LinearLayout) findViewById(R.id.ll_enterprise_myProssBar);
		myProssBarhandleText = (TextView) findViewById(R.id.tv_enterprise_myProssBarhandleText);
		progressBarHandle = (ProgressBar) findViewById(R.id.pb_enterprise_myProssBarhandle);
		mLlGlobal = (LinearLayout) findViewById(R.id.ll_enterprise_global);
		mProgress = (ProgressBar) findViewById(R.id.pb_enterprise_progress);

		// 设置用户实名认证的姓名并不能修改
		if (!TextUtils.isEmpty(name) && name != null) {
			tvName.setText(name);
		}

		// 设置身份证号码并不能修改
		if (!TextUtils.isEmpty(idCode) && idCode != null) {
			if (idCode.length() == 15) {
				idCard = idCode.substring(0, 2) + "*********"
						+ idCode.substring(11, idCode.length());
			} else if (idCode.length() == 18) {
				idCard = idCode.substring(0, 2) + "************"
						+ idCode.substring(14, idCode.length());
			}
			tvIdCard.setText(idCard);
		}

		// 设置客服电话
		tvCustomerPhone.setText(Html.fromHtml("<u>" + "4006541756" + "</u>"));

		// 设置授权书下载地址
		tvDownload.setText(Html.fromHtml("授权书下载:" + "<font color='#ff4400'>"
				+ "<u>" + downLoadAttorney + "</u>" + "</font>"));
		tvDownload.setMovementMethod(LinkMovementMethod.getInstance());

	}

	@Override
	protected void initData() {
		// 获取企业认证信息
		getSetCompanyRegisteredInfo();

	}

	@Override
	protected void setListener() {
		// 初始化点击事件
		initOnClick();

	}

	// 初始化titileBar的方法
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(R.string.enterprise));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EnterpriseActivity.this.finish();
			}
		});

	}

	// 获取企业认证信息的方法
	private void getSetCompanyRegisteredInfo() {
		mLlGlobal.setVisibility(View.INVISIBLE);
		mProgress.setVisibility(View.VISIBLE);

		mGetSetCompanyRegisteredInfo = new GetSetCompanyRegisteredInfoRequest(
				EnterpriseActivity.this);
		mGetSetCompanyRegisteredInfo
				.setRequestId(GET_SETCOMPANY_REGISTEREDINFO);
		httpPost(mGetSetCompanyRegisteredInfo);

	}

	// 初始化点击事件的方法
	private void initOnClick() {
		tvQuestion.setOnClickListener(this);
		tvCustomerPhone.setOnClickListener(this);
		ivBusiness.setOnClickListener(this);
		ivAttorney.setOnClickListener(this);
		ivWithCard.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_enterprise_question:
			// 跳转到帮助页面
			HelpActivity.invoke(EnterpriseActivity.this, "iscom");
			break;

		case R.id.tv_enterprise_customerPhone:
			// 拨打客服电话
			String customerPhone = tvCustomerPhone.getText().toString().trim();

			if (!TextUtils.isEmpty(customerPhone)) {
				CommonUtil.callPhoneIndirect(this, customerPhone);
			}

			break;

		case R.id.iv_enterprise_business:
			// 营业执照

			if (businessLicenseImgStatus == null
					|| businessLicenseImgStatus.equals("0")) {
				// 点击拍照
				showBusinessLicense();

			} else if ((businessLicenseImgStatus != null && businessLicenseImgStatus
					.equals("1"))
					|| (businessLicenseImgStatus != null && businessLicenseImgStatus
							.equals("2"))) {
				// 营业执照图片审核中和审核通过--点击放大图片
				if (!TextUtils.isEmpty(eBusinessLicenseImgUrl800)
						&& eBusinessLicenseImgUrl800 != null) {
					ImageScaleActivity.invoke(EnterpriseActivity.this,
							eBusinessLicenseImgUrl800);
				}

			} else if ((businessLicenseImgStatus != null && businessLicenseImgStatus
					.equals("3"))
					|| (businessLicenseImgStatus != null && businessLicenseImgStatus
							.equals("4"))) {
				// 营业执照图片审核未通过和证件过期--重新上传
				imgStatus = 1;
				if (!TextUtils.isEmpty(businessLicenseImgUrl)
						&& businessLicenseImgUrl != null) {

					EnterpriseUploadPhotoActivity.invoke(
							EnterpriseActivity.this, businessLicenseImgUrl,
							imgStatus);

				}
			}

			break;

		case R.id.iv_enterprise_attorney:
			// 授权书

			if (authorizationLetterImgStatus == null
					|| authorizationLetterImgStatus.equals("0")) {
				// 点击拍照
				showAttorneyLicense();

			} else if ((authorizationLetterImgStatus != null && authorizationLetterImgStatus
					.equals("1"))
					|| (authorizationLetterImgStatus != null && authorizationLetterImgStatus
							.equals("2"))) {
				// 授权书图片审核中和审核通过--点击放大图片
				if (!TextUtils.isEmpty(eAuthorizationLetterImgUrl800)
						&& eAuthorizationLetterImgUrl800 != null) {
					ImageScaleActivity.invoke(EnterpriseActivity.this,
							eAuthorizationLetterImgUrl800);
				}

			} else if ((authorizationLetterImgStatus != null && authorizationLetterImgStatus
					.equals("3"))
					|| (authorizationLetterImgStatus != null && authorizationLetterImgStatus
							.equals("4"))) {
				// 授权书图片审核未通过和证件过期--重新上传
				imgStatus = 2;
				if (!TextUtils.isEmpty(authorizationLetterImgUrl)
						&& authorizationLetterImgUrl != null) {
					EnterpriseUploadPhotoActivity.invoke(
							EnterpriseActivity.this, authorizationLetterImgUrl,
							imgStatus);
				}
			}

			break;

		case R.id.iv_enterprise_withCard:
			// 手持身份证
			if (handIdCodeImgStatus == null || handIdCodeImgStatus.equals("0")) {
				// 点击拍照
				showWithCardLicense();

			} else if ((handIdCodeImgStatus != null && handIdCodeImgStatus
					.equals("1"))
					|| (handIdCodeImgStatus != null && handIdCodeImgStatus
							.equals("2"))) {
				// 手持身份证图片审核中和审核通过--点击放大图片
				if (!TextUtils.isEmpty(eHandIdCodeImgUrl800)
						&& eHandIdCodeImgUrl800 != null) {
					ImageScaleActivity.invoke(EnterpriseActivity.this,
							eHandIdCodeImgUrl800);
				}

			} else if ((handIdCodeImgStatus != null && handIdCodeImgStatus
					.equals("3"))
					|| (handIdCodeImgStatus != null && handIdCodeImgStatus
							.equals("4"))) {
				// 手持身份证图片审核未通过和证件过期--重新上传
				imgStatus = 3;

				if (!TextUtils.isEmpty(handIdCodeImgUrl)
						&& handIdCodeImgUrl != null) {
					EnterpriseUploadPhotoActivity.invoke(
							EnterpriseActivity.this, handIdCodeImgUrl,
							imgStatus);
				}
			}

			break;

		case R.id.bt_enterprise_submit:
			// TODO--提交企业信息

			String enterpriseName = etEnterpriseName.getText().toString()
					.trim();

			if (TextUtils.isEmpty(enterpriseName)) {
				showToast("请输入企业名称");
				return;
			}

			if (TextUtils.isEmpty(imgBusinessBase64)) {
				showToast("请上传营业执照照片");
				return;
			}
			if (TextUtils.isEmpty(imgAttorneyBase64)) {
				showToast("请上传授权书照片");
				return;
			}
			if (TextUtils.isEmpty(imgWithCardBase64)) {
				showToast("请上传手持身份证照片");
				return;
			}

			// 请求服务器提交企业认证

			setCompanyRegisteredInfo(name, idCode, enterpriseName,
					imgBusinessBase64, businessImgName, imgAttorneyBase64,
					attorneyImgName, imgWithCardBase64, withCardImgName);

			break;

		default:
			break;
		}

	}

	// 请求服务器提交企业认证的方法
	private void setCompanyRegisteredInfo(String name, String idCode,
			String enterpriseName, String imgBusinessBase64,
			String businessImgName, String imgAttorneyBase64,
			String attorneyImgName, String imgWithCardBase64,
			String withCardImgName) {

		mSetCompanyRegisteredInfoRequest = new SetCompanyRegisteredInfoRequest(
				EnterpriseActivity.this, name, idCode, enterpriseName,
				imgBusinessBase64, businessImgName, imgAttorneyBase64,
				attorneyImgName, imgWithCardBase64, withCardImgName);

		mSetCompanyRegisteredInfoRequest
				.setRequestId(SET_COMPANYREGISTEREDINFO_REQUEST);
		httpPost(mSetCompanyRegisteredInfoRequest);

		// 设置按钮不可点击
		setViewEnable(false);
		// 显示进度条
		llProssBar.setVisibility(View.VISIBLE);
		progressBarHandle.setMax(MAX_PROGRESS);// 设置最大进度

		progress = (progress > 0) ? progress : 0;
		progressBarHandle.setProgress(progress);
		handler.sendEmptyMessage(PRO);// 发送空消息

	}

	// 设置提交按钮不可重复点击
	private void setViewEnable(boolean bEnable) {
		if (bEnable) {
			btnSubmit.setText("提交");
		} else {
			btnSubmit.setText("提交中 ...");
		}
		btnSubmit.setEnabled(bEnable);
		ivBusiness.setEnabled(bEnable);
		ivAttorney.setEnabled(bEnable);
		ivWithCard.setEnabled(bEnable);

	}

	// 手持身份证的方法
	private void showWithCardLicense() {
		withCardPopupWindow = new PopupWindow(EnterpriseActivity.this);
		View withcardPhoto = getLayoutInflater().inflate(
				R.layout.item_withcardphoto_popupwindows, null);
		withCardPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
		withCardPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		withCardPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		withCardPopupWindow.setFocusable(true);
		withCardPopupWindow.setOutsideTouchable(true);
		withCardPopupWindow.setContentView(withcardPhoto);
		withCardPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

		Button withcard_photo = (Button) withcardPhoto
				.findViewById(R.id.item_popupwindows_withcardphoto);
		Button withcard_select = (Button) withcardPhoto
				.findViewById(R.id.item_popupwindows_withcardselect);
		Button withcard_cancel = (Button) withcardPhoto
				.findViewById(R.id.item_popupwindows_withcardcancel);

		// 手持身份证拍照
		withcard_photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PickImage.pickImageFromCamera(EnterpriseActivity.this,
						enterpriseTempFileDir + "WithCardlicense.jpg", 5);
				withCardPopupWindow.dismiss();
			}
		});

		// 手持身份证选图
		withcard_select.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PickImage.pickImageFromPhoto(EnterpriseActivity.this, 6);
				withCardPopupWindow.dismiss();
			}
		});

		// 手持身份证取消
		withcard_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				withCardPopupWindow.dismiss();
			}
		});

	}

	// 授权书的方法
	private void showAttorneyLicense() {
		attorneyPopupWindow = new PopupWindow(EnterpriseActivity.this);
		View attorneyPhoto = getLayoutInflater().inflate(
				R.layout.item_attorneyphoto_popupwindows, null);
		attorneyPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
		attorneyPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		attorneyPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		attorneyPopupWindow.setFocusable(true);
		attorneyPopupWindow.setOutsideTouchable(true);
		attorneyPopupWindow.setContentView(attorneyPhoto);
		attorneyPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

		Button attorney_photo = (Button) attorneyPhoto
				.findViewById(R.id.item_popupwindows_attorneyphoto);
		Button attorney_select = (Button) attorneyPhoto
				.findViewById(R.id.item_popupwindows_attorneyselect);
		Button attorney_cancel = (Button) attorneyPhoto
				.findViewById(R.id.item_popupwindows_attorneycancel);

		// 授权书拍照
		attorney_photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PickImage.pickImageFromCamera(EnterpriseActivity.this,
						enterpriseTempFileDir + "Attorneylicense.jpg", 3);
				attorneyPopupWindow.dismiss();
			}
		});

		// 授权书选图
		attorney_select.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PickImage.pickImageFromPhoto(EnterpriseActivity.this, 4);
				attorneyPopupWindow.dismiss();
			}
		});

		// 授权书取消
		attorney_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				attorneyPopupWindow.dismiss();
			}
		});

	}

	// 营业执照的方法
	private void showBusinessLicense() {
		businessPopupWindow = new PopupWindow(EnterpriseActivity.this);
		View businessPhoto = getLayoutInflater().inflate(
				R.layout.item_businessphoto_popupwindows, null);
		businessPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
		businessPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		businessPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		businessPopupWindow.setFocusable(true);
		businessPopupWindow.setOutsideTouchable(true);
		businessPopupWindow.setContentView(businessPhoto);
		businessPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

		Button business_photo = (Button) businessPhoto
				.findViewById(R.id.item_popupwindows_businessphoto);
		Button business_select = (Button) businessPhoto
				.findViewById(R.id.item_popupwindows_businessselect);
		Button business_cancel = (Button) businessPhoto
				.findViewById(R.id.item_popupwindows_businesscancel);

		// 营业执照拍照
		business_photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PickImage.pickImageFromCamera(EnterpriseActivity.this,
						enterpriseTempFileDir + "Businesslicense.jpg", 1);
				businessPopupWindow.dismiss();
			}
		});
		// 营业执照图库选择
		business_select.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PickImage.pickImageFromPhoto(EnterpriseActivity.this, 2);
				businessPopupWindow.dismiss();
			}
		});
		// 营业执照取消
		business_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				businessPopupWindow.dismiss();
			}
		});
	}

	// 接收返回结果
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			// 营业执照拍照返回
			if (resultCode == RESULT_OK) {
				businessimageFile = new File(enterpriseTempFileDir
						+ "Businesslicense.jpg");
			}
			break;

		case 2:
			// 营业执照图库返回
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				String realPathFromURI = FileUtils
						.getRealPathFromURI(uri, this);
				if (TextUtils.isEmpty(realPathFromURI)) {
					businessimageFile = null;
				} else {
					businessimageFile = new File(enterpriseTempFileDir
							+ "Businesslicense.jpg");
					File temFile = new File(realPathFromURI);

					FileUtils.copyFile(temFile, businessimageFile);
				}
			}
			break;

		case 3:
			// 授权书拍照返回
			if (resultCode == RESULT_OK) {
				attorneyimageFile = new File(enterpriseTempFileDir
						+ "Attorneylicense.jpg");
			}
			break;

		case 4:
			// 授权书图库返回
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				String realPathFromURI = FileUtils
						.getRealPathFromURI(uri, this);
				if (TextUtils.isEmpty(realPathFromURI)) {
					attorneyimageFile = null;
				} else {
					attorneyimageFile = new File(enterpriseTempFileDir
							+ "Attorneylicense.jpg");
					File temFile = new File(realPathFromURI);

					FileUtils.copyFile(temFile, attorneyimageFile);
				}
			}
			break;

		case 5:
			// 手持身份证拍照返回
			if (resultCode == RESULT_OK) {
				withCardimageFile = new File(enterpriseTempFileDir
						+ "WithCardlicense.jpg");
			}
			break;

		case 6:
			// 手持身份证图库返回
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				String realPathFromURI = FileUtils
						.getRealPathFromURI(uri, this);
				if (TextUtils.isEmpty(realPathFromURI)) {
					withCardimageFile = null;
				} else {
					withCardimageFile = new File(enterpriseTempFileDir
							+ "WithCardlicense.jpg");
					File temFile = new File(realPathFromURI);

					FileUtils.copyFile(temFile, withCardimageFile);
				}
			}
			break;

		default:
			break;
		}

		if (requestCode == 1 || requestCode == 2) {
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
			// 显示拍照或选图后的行驶证
			if (bm != null) {
				ivBusiness.setImageBitmap(bm);
			}

			// base64解码
			byte[] decode = Base64.decode(imgBusinessBase64, Base64.NO_WRAP);

			// TODO--将图片保存在SD卡
			BufferedOutputStream bos = null;
			FileOutputStream fos = null;
			File file = new File(enterpriseTempFileDir + "imgBaseBusiness.jpg");
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
			if (attorneyimageFile == null) {
				return;
			}

			// TODO--
			byte[] bytes = handleImage(attorneyimageFile, 800, 800);

			if (bytes == null || bytes.length == 0) {
				return;
			}
			int degree = ImageUtil.readPictureDegree(attorneyimageFile
					.getAbsolutePath());

			imgAttorneyBase64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
			Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			ImageUtil.rotaingImageView(degree, bm);
			// 显示拍照或选图后的运营证
			if (bm != null) {
				ivAttorney.setImageBitmap(bm);
			}

			// base64解码
			byte[] decode = Base64.decode(imgAttorneyBase64, Base64.NO_WRAP);

			// TODO--将图片保存在SD卡
			BufferedOutputStream bos = null;
			FileOutputStream fos = null;
			File file = new File(enterpriseTempFileDir + "imgBaseAttorney.jpg");
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

			// 显示拍照后选图后的车辆照片
			if (bm != null) {
				ivWithCard.setImageBitmap(bm);
			}

			// base64解码
			byte[] decode = Base64.decode(imgWithCardBase64, Base64.NO_WRAP);

			// TODO--将图片保存在SD卡
			BufferedOutputStream bos = null;
			FileOutputStream fos = null;
			File file = new File(enterpriseTempFileDir + "imgBaseWithCard.jpg");
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
		case GET_SETCOMPANY_REGISTEREDINFO:
			// 获取企业认证信息
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				CompanyRegisteredInfo companyRegisteredInfo = (CompanyRegisteredInfo) response.singleData;
				name = companyRegisteredInfo.name;// 姓名
				idCode = companyRegisteredInfo.idCode;// 身份证
				comStatus = companyRegisteredInfo.comStatus;// 是否企业认证：0：未认证，1：认证中，2：已认证，3：认证失败
				String comName = companyRegisteredInfo.comName;// 企业名称
				businessLicenseImgUrl = companyRegisteredInfo.businessLicenseImgUrl;// 营业执照路径
				businessLicenseImgStatus = companyRegisteredInfo.businessLicenseImgStatus;// 营业执照的状态1：待审核；2：审核通过；3：审核未通过；4：已过期
				authorizationLetterImgUrl = companyRegisteredInfo.authorizationLetterImgUrl;// 授权书路径
				authorizationLetterImgStatus = companyRegisteredInfo.authorizationLetterImgStatus;// 授权书的状态
				handIdCodeImgUrl = companyRegisteredInfo.handIdCodeImgUrl;// 手持身份证照路径
				handIdCodeImgStatus = companyRegisteredInfo.handIdCodeImgStatus;// 手持身份证照状态

				// 新增字段
				eBusinessLicenseImgUrl800 = companyRegisteredInfo.businessLicenseImgUrl800;// 营业执照大图
				eAuthorizationLetterImgUrl800 = companyRegisteredInfo.authorizationLetterImgUrl800;// 授权书大图
				eHandIdCodeImgUrl800 = companyRegisteredInfo.handIdCodeImgUrl800;// 手持身份证大图

				Log.e("TAG", "大图的地址营业执照--------" + eBusinessLicenseImgUrl800);
				Log.e("TAG", "大图的地址授权书大图--------"
						+ eAuthorizationLetterImgUrl800);
				Log.e("TAG", "大图的地址手持身份证--------" + eHandIdCodeImgUrl800);

				// 设置姓名
				if (!TextUtils.isEmpty(name) && name != null) {
					tvName.setText(name);
				}

				// 设置身份证号码并不能修改
				if (!TextUtils.isEmpty(idCode) && idCode != null) {
					if (idCode.length() == 15) {
						idCard = idCode.substring(0, 2) + "*********"
								+ idCode.substring(11, idCode.length());
					} else if (idCode.length() == 18) {
						idCard = idCode.substring(0, 2) + "************"
								+ idCode.substring(14, idCode.length());
					}
					tvIdCard.setText(idCard);
				}

				// TODO--设置企业名称
				if (!TextUtils.isEmpty(comName) && comName != null) {
					etEnterpriseName.setText(comName);
					etEnterpriseName.setFocusable(false);
				}

				// 显示营业执照照片
				if (!TextUtils.isEmpty(businessLicenseImgUrl)
						&& businessLicenseImgUrl != null) {
					ImageLoader.getInstance().displayImage(
							businessLicenseImgUrl, ivBusiness,
							ImageLoaderOptions.options);
				}
				// 显示授权书照片
				if (!TextUtils.isEmpty(authorizationLetterImgUrl)
						&& authorizationLetterImgUrl != null) {
					ImageLoader.getInstance().displayImage(
							authorizationLetterImgUrl, ivAttorney,
							ImageLoaderOptions.options);
				}
				// 显示手持身份证照片
				if (!TextUtils.isEmpty(handIdCodeImgUrl)
						&& handIdCodeImgUrl != null) {
					ImageLoader.getInstance().displayImage(handIdCodeImgUrl,
							ivWithCard, ImageLoaderOptions.options);
				}

				// 设置企业认证状态
				if (comStatus != null
						&& (comStatus.equals("0") || comStatus.equals("3"))) {
					// 未认证和认证失败
					btnSubmit.setText("提交");
					btnSubmit.setEnabled(true);

				} else if (comStatus != null && comStatus.equals("1")) {
					// 认证中
					btnSubmit.setText("认证中");
					btnSubmit.setEnabled(false);
				} else if (comStatus != null && comStatus.equals("2")) {
					// 认证通过
					btnSubmit.setText("认证通过");
					btnSubmit.setEnabled(false);
				}

				// 根据图片状态来决定是否要重新上传
				if (businessLicenseImgStatus != null
						&& businessLicenseImgStatus.equals("1")) {
					// 营业执照图片待审核
					ivBusinessfied
							.setImageResource(R.drawable.carbasic_min_perform);
				} else if (businessLicenseImgStatus != null
						&& businessLicenseImgStatus.equals("2")) {
					// 营业执照图片审核通过
					ivBusinessfied.setImageResource(R.drawable.carbasic_min_ok);
				} else if (businessLicenseImgStatus != null
						&& businessLicenseImgStatus.equals("3")) {
					// 营业执照图片审核未通过
					ivBusinessfied
							.setImageResource(R.drawable.carbasic_min_fail);
				} else if (businessLicenseImgStatus != null
						&& businessLicenseImgStatus.equals("4")) {
					// 营业执照图片已过期
					ivBusinessfied
							.setImageResource(R.drawable.carbasic_min_overdue);
				}

				if (authorizationLetterImgStatus != null
						&& authorizationLetterImgStatus.equals("1")) {
					// 授权书图片待审核
					ivAttorneyfied
							.setImageResource(R.drawable.carbasic_min_perform);
				} else if (authorizationLetterImgStatus != null
						&& authorizationLetterImgStatus.equals("2")) {
					// 授权书图片审核通过
					ivAttorneyfied.setImageResource(R.drawable.carbasic_min_ok);
				} else if (authorizationLetterImgStatus != null
						&& authorizationLetterImgStatus.equals("3")) {
					// 授权书图片审核未通过
					ivAttorneyfied
							.setImageResource(R.drawable.carbasic_min_fail);
				} else if (authorizationLetterImgStatus != null
						&& authorizationLetterImgStatus.equals("4")) {
					// 授权书图片已过期
					ivAttorneyfied
							.setImageResource(R.drawable.carbasic_min_overdue);
				}

				if (handIdCodeImgStatus != null
						&& handIdCodeImgStatus.equals("1")) {
					// 手持身份证图片待审核
					ivWithCardfied
							.setImageResource(R.drawable.carbasic_max_perform);
				} else if (handIdCodeImgStatus != null
						&& handIdCodeImgStatus.equals("2")) {
					// 手持身份证图片审核通过
					ivWithCardfied.setImageResource(R.drawable.carbasic_max_ok);
				} else if (handIdCodeImgStatus != null
						&& handIdCodeImgStatus.equals("3")) {
					// 手持身份证图片审核未通过
					ivWithCardfied
							.setImageResource(R.drawable.carbasic_max_fail);
				} else if (handIdCodeImgStatus != null
						&& handIdCodeImgStatus.equals("4")) {
					// 手持身份证图片已过期
					ivWithCardfied
							.setImageResource(R.drawable.carbasic_max_overdue);
				}

			} else {
				// 获取企业认证信息失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}

			mLlGlobal.setVisibility(View.VISIBLE);
			mProgress.setVisibility(View.INVISIBLE);
			break;

		case SET_COMPANYREGISTEREDINFO_REQUEST:
			// 提交企业认证
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {

				// 删除存放图片文件夹
				businessimageFile = new File(enterpriseTempFileDir
						+ "Businesslicense.jpg");
				if (businessimageFile.exists() && businessimageFile.isFile()) {
					businessimageFile.delete();
				}

				attorneyimageFile = new File(enterpriseTempFileDir
						+ "Attorneylicense.jpg");
				if (attorneyimageFile.exists() && attorneyimageFile.isFile()) {
					attorneyimageFile.delete();
				}

				withCardimageFile = new File(enterpriseTempFileDir
						+ "WithCardlicense.jpg");
				if (withCardimageFile.exists() && withCardimageFile.isFile()) {
					withCardimageFile.delete();
				}

				handler.sendEmptyMessage(ALL);// 发送空消息

				// if (!TextUtils.isEmpty(message) && message != null) {
				// showToast(message);
				// }

				// 关闭当前页面
				EnterpriseActivity.this.finish();
				// TODO --登录成功后关闭当前登录界面还需要关闭登录界面
				// CamaridoApp.destoryActivity("RealNameActivity");
			} else {
				// 提交企业失败
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
		case SET_COMPANYREGISTEREDINFO_REQUEST:
			// 设置提交按钮可点击
			setViewEnable(true);
			handler.sendEmptyMessage(FAIL);// 发送空消息
			break;

		}
	}

	// 本界面的跳转的方法
	public static void invoke(Context activity, String name, String idCode) {
		Intent intent = new Intent();
		intent.setClass(activity, EnterpriseActivity.class);
		intent.putExtra("name", name);// 传真实姓名
		intent.putExtra("idCode", idCode);// 传身份证号码
		activity.startActivity(intent);
	}

}
