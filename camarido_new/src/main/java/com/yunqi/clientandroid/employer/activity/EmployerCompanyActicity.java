package com.yunqi.clientandroid.employer.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.activity.LoginActicity;
import com.yunqi.clientandroid.employer.entity.GetCompanyContent;
import com.yunqi.clientandroid.employer.entity.PingTaiKeFu;
import com.yunqi.clientandroid.employer.request.GetCompanyContentRequest;
import com.yunqi.clientandroid.employer.request.GetEnterpriseInfoRequest;
import com.yunqi.clientandroid.employer.request.PingTaiKeFuRequest;
import com.yunqi.clientandroid.employer.request.SetEnterpriseInfoRequest;
import com.yunqi.clientandroid.employer.request.UploadCompanyUrlRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CommonUtil;
import com.yunqi.clientandroid.utils.ImageUtil;
import com.yunqi.clientandroid.utils.PreManager;
import com.yunqi.clientandroid.utils.StringUtils;
import com.yunqi.clientandroid.utils.UserUtil;
import com.yunqi.clientandroid.view.MMAlert;
import com.yunqi.clientandroid.view.RoundImageView;

/**
 * @Description:企业页面
 * @ClassName: EmployerCompanyActicity
 * @author: mengwei
 * @date: 2016-6-21 下午4:42:07
 * 
 */
public class EmployerCompanyActicity extends BaseActivity implements
		View.OnClickListener {
	private EmployerMainActivity activity;
	private View enterpriseView;
	private RelativeLayout enterprisewalletLayout;
	private RelativeLayout enterprisecarLayout;
	private RelativeLayout enterpriseaddressLayout;
	private RelativeLayout enterprisehelpLayout;
	private RelativeLayout enterpriseserviceLayout;
	private String servicenumberinputStr;
	private File profileImgFile;
	private GetEnterpriseInfoRequest mGetEnterpriseInfoRequest;
	private SetEnterpriseInfoRequest mSetEnterpriseInfoRequest;
	private DisplayImageOptions mOption;
	private RoundImageView ivHead;
	private Uri imageFileUri;
	private PopupWindow enterprisePopupWindow;
	private EditText enterprisenameEditText;
	private EditText enterprisemessagEditText;
	private TextView tvCancle;
	private TextView tvSure;
	private TextView tvEnterpriseCall;
	private RelativeLayout rlEnterpriseBack;
	private TextView tvEnterpriseNickname;
	private String extensionNumber;

	private PreManager mPreManager;
	private String pcsPhone;

	/* 请求ID */
	private final int GET_COMPANY_CONTENT = 1;
	private final int UPLOAD_LOGOURL = 2;
	private final int GET_PINGTAI_KEFU = 3;

	/* 请求类 */
	private GetCompanyContentRequest mGetCompanyContentRequest;
	private UploadCompanyUrlRequest mUploadCompanyUrlRequest;
	private PingTaiKeFuRequest pingTaiKeFuRequest;

	/* 请求的数据 */
	private String mTenantShortname; // 公司简称
	private String mLogoUrl; // 公司logo

	@Override
	protected int getLayoutId() {
		return R.layout.employer_fragment_enterprise_mine;
	}

	@Override
	protected void initView() {
		initActionBar();

		// TODO Auto-generated method stub
		enterpriseView = EmployerCompanyActicity.this.getLayoutInflater()
				.inflate(R.layout.employer_fragment_enterprise_mine, null);
		ivHead = obtainView(R.id.iv_enterprise_mine_avatar);
		tvEnterpriseNickname = obtainView(R.id.tv_enterprise_mine_nickName);
		enterprisewalletLayout = obtainView(R.id.rl_enterprise_mine_wallet);
		enterprisecarLayout = obtainView(R.id.rl_enterprise_mine_car);
		enterpriseaddressLayout = obtainView(R.id.rl_enterprise_mine_address);
		enterprisehelpLayout = obtainView(R.id.rl_enterprise_mine_help);
		enterpriseserviceLayout = obtainView(R.id.rl_enterprise_mine_service);
		// tvCancle = (TextView) enterpriseView
		// .findViewById(R.id.tv_enterprise_personal_cancle_mine);
		tvEnterpriseCall = obtainView(R.id.tv_enterprise_mine_call);
		rlEnterpriseBack = obtainView(R.id.rl_enterprise_mine_back);

		// PreManager类实例化
		mPreManager = PreManager.instance(EmployerCompanyActicity.this);

		// 对头像和公司名进行赋值
		setHeaderText();
	}

	// 初始化titileBar的方法
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(
				R.string.employer_activity_company));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EmployerCompanyActicity.this.finish();
			}
		});
	}

	/**
	 * 对头像和公司名进行赋值
	 */
	private void setHeaderText() {
		String mAvatar = mPreManager.getAvatar();
		String mCompanyName = mPreManager.getCompanyName();

		if ((mAvatar != null && !TextUtils.isEmpty(mAvatar))
				&& (mCompanyName != null && !TextUtils.isEmpty(mCompanyName))) {
			// 显示企业头像
			ImageLoader.getInstance().displayImage(mAvatar, ivHead, mOption);
			// 显示企业名字简称
			tvEnterpriseNickname.setText(mCompanyName);
		} else {
			// 获取企业信息
			getCompanyContentText();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// 获取企业信息
		getCompanyContentText();
		// 获取客服电话
		getPingTai();
	}

	private void getPingTai() {
		pingTaiKeFuRequest = new PingTaiKeFuRequest(mContext);
		pingTaiKeFuRequest.setRequestId(GET_PINGTAI_KEFU);
		httpGet(pingTaiKeFuRequest);
	}

	/**
	 * 获取企业信息
	 */
	private void getCompanyContentText() {
		mGetCompanyContentRequest = new GetCompanyContentRequest(
				EmployerCompanyActicity.this);
		mGetCompanyContentRequest.setRequestId(GET_COMPANY_CONTENT);
		httpPost(mGetCompanyContentRequest);
	}

	@Override
	protected void initData() {
		profileImgFile = new File(EmployerCompanyActicity.this.getCacheDir(),
				"headphoto_");
		// 设置头像
		mOption = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).showImageOnLoading(R.drawable.mine_avatar)
				.showImageForEmptyUri(R.drawable.mine_avatar)
				.showImageOnFail(R.drawable.mine_avatar).build();
	}

	@Override
	protected void setListener() {
		// 初始化点击事件
		initOnClick();
	}

	/**
	 * 初始化点击事件
	 */
	private void initOnClick() {
		ivHead.setOnClickListener(this);
		enterprisewalletLayout.setOnClickListener(this);
		enterprisecarLayout.setOnClickListener(this);
		enterpriseaddressLayout.setOnClickListener(this);
		enterprisehelpLayout.setOnClickListener(this);
		enterpriseserviceLayout.setOnClickListener(this);
		tvEnterpriseCall.setOnClickListener(this);
		rlEnterpriseBack.setOnClickListener(this);

	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);

		boolean isSuccess;
		String message;

		switch (requestId) {
		case GET_PINGTAI_KEFU:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				PingTaiKeFu pingTaiKeFu = (PingTaiKeFu) response.singleData;

				pcsPhone = pingTaiKeFu.PlatServicePhoneNum;
			}
			break;

		case GET_COMPANY_CONTENT: // 获取的企业信息
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				GetCompanyContent mGetCompanyContent = (GetCompanyContent) response.singleData;

				mTenantShortname = mGetCompanyContent.tenantShortname;
				mLogoUrl = mGetCompanyContent.logoUrl;
				extensionNumber = mGetCompanyContent.extensionNumber;// 客服电话

				if (mTenantShortname != null
						&& !TextUtils.isEmpty(mTenantShortname)) {
					mPreManager.setCompanyName(mTenantShortname);
					tvEnterpriseNickname.setText(mTenantShortname);
				}

				if (mLogoUrl != null && !TextUtils.isEmpty(mLogoUrl)) {
					mPreManager.setAvatar(mLogoUrl);
					ImageLoader.getInstance().displayImage(mLogoUrl, ivHead,
							mOption);
				} else {
					ivHead.setImageResource(R.drawable.company_touxiang);
				}

				if (extensionNumber != null
						&& !TextUtils.isEmpty(extensionNumber)) {
					tvEnterpriseCall.setText(extensionNumber);
				}
			}
			break;

		case UPLOAD_LOGOURL:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				showToast(message);
			} else {
				showToast(message);
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
	public void onStart(int requestId) {
		super.onStart(requestId);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_enterprise_mine_avatar:
			// 上传企业头像
			showPickDialog();
			break;
		// case R.id.rl_to_personal:
		// // 点击设置企业信息
		// showSettingenterprise();
		// break;
		case R.id.rl_enterprise_mine_wallet:
			// 跳转到企业钱包
			CompanyWalletActivity.invoke(EmployerCompanyActicity.this);
			break;

		case R.id.rl_enterprise_mine_car:
			// 跳转到车辆管理
			CarManagerAcitivity.invoke(EmployerCompanyActicity.this);
			break;

		case R.id.rl_enterprise_mine_address:
			// 跳转到地址管理
			AddressListActivity.invoke(EmployerCompanyActicity.this);
			break;
		case R.id.rl_enterprise_mine_help:
			// 跳转到系统帮助
			HelpPkgActivity.invoke(mContext);
			break;

		case R.id.rl_enterprise_mine_service:
			// 获取客服电话

			Log.e("TAG", "------pcsPhone------" + pcsPhone);

			if (pcsPhone != null && !TextUtils.isEmpty(pcsPhone)) {
				// 拨打客服电话
				CommonUtil.callPhoneIndirect(EmployerCompanyActicity.this,
						pcsPhone);
			}
			break;

		case R.id.rl_enterprise_mine_back: // 退出当前系统
			showLogoutDialog();
			break;

		}

	}

	/**
	 * 
	 * @Description:退出对话框
	 * @Title:showLogoutDialog
	 * @return:void
	 * @throws
	 * @Create: 2016年6月15日 下午4:30:37
	 * @Author : chengtao
	 */
	private void showLogoutDialog() {
		LayoutInflater inflater = LayoutInflater
				.from(EmployerCompanyActicity.this);
		View view = inflater.inflate(R.layout.logout_dialog, null);
		Button btnCancle = (Button) view.findViewById(R.id.btn_cancle);
		Button btnSure = (Button) view.findViewById(R.id.btn_sure);
		AlertDialog.Builder builder = new Builder(EmployerCompanyActicity.this);
		final AlertDialog dialog = builder.create();
		dialog.setView(view);
		dialog.setCancelable(true);
		dialog.show();
		btnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		btnSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 删除token过期时间
				PreManager.instance(EmployerCompanyActicity.this)
						.removeTokenExpires();
				// 清空userId
				UserUtil.unSetUserId(EmployerCompanyActicity.this);
				// 跳转到登录界面
				LoginActicity.invoke(EmployerCompanyActicity.this);
				// 用户退出统计
				MobclickAgent.onProfileSignOff();
				// finish主界面
				EmployerCompanyActicity.this.finish();
				CamaridoApp.destoryActivity("EmployerMainActivity");
			}
		});
	}

	public void showPickDialog() {
		Log.e("TAG", "-----------showPickDialog---------------");
		String shareDialogTitle = getString(R.string.pick_dialog_title);
		MMAlert.showAlert(EmployerCompanyActicity.this, shareDialogTitle,
				getResources().getStringArray(R.array.picks_item), null,
				new MMAlert.OnAlertSelectId() {
					@Override
					public void onClick(int whichButton) {
						switch (whichButton) {
						case 0: // 拍照
							try {
								Log.e("TAG", "-----------拍照---------------");
								imageFileUri = EmployerCompanyActicity.this
										.getContentResolver()
										.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
												new ContentValues());
								if (imageFileUri != null) {
									Intent i = new Intent(
											android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
									i.putExtra(
											android.provider.MediaStore.EXTRA_OUTPUT,
											imageFileUri);
									if (StringUtils.isIntentSafe(
											EmployerCompanyActicity.this, i)) {
										Log.e("TAG",
												"-----------startActivityForResult(i, 2)---------------");
										startActivityForResult(i, 2);
									} else {
										Toast.makeText(
												EmployerCompanyActicity.this,
												getString(R.string.dont_have_camera_app),
												Toast.LENGTH_SHORT).show();
									}
								} else {
									Toast.makeText(
											EmployerCompanyActicity.this,
											getString(R.string.cant_insert_album),
											Toast.LENGTH_SHORT).show();
								}
							} catch (Exception e) {
								Toast.makeText(EmployerCompanyActicity.this,
										getString(R.string.cant_insert_album),
										Toast.LENGTH_SHORT).show();
							}
							break;
						case 1: // 相册
							Log.e("TAG", "-----------相册---------------");
							Intent intent = new Intent(Intent.ACTION_PICK);
							intent.setDataAndType(
									android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
									"image/*");
							Log.e("TAG",
									"-----------startActivityForResult(intent, 1)---------------");
							startActivityForResult(intent, 1);
							break;
						default:
							break;
						}
					}

				});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case 1:// 如果是直接从相册获取
				if (data != null) {
					Log.e("TAG", "-----------直接从相册获取---------------");
					Uri uri = data.getData();
					Log.e("TAG",
							"-----------startPhotoZoom(uri)---------------");
					startPhotoZoom(uri);
				}
				break;
			case 2:// 如果是调用相机拍照时
				String picPath = null;
				if (imageFileUri != null) {
					Log.e("TAG", "-----------调用相机拍照---------------");
					picPath = ImageUtil.getPicPathFromUri(imageFileUri,
							EmployerCompanyActicity.this);
					int degree = 0;
					if (!StringUtils.isEmpty(picPath))
						degree = ImageUtil.readPictureDegree(picPath);
					Matrix matrix = new Matrix();
					if (degree != 0) {// 解决旋转问题
						matrix.preRotate(degree);
					}
					Uri uri = Uri.fromFile(new File(picPath));
					Log.e("TAG",
							"-----------startPhotoZoom(uri)---------------");
					startPhotoZoom(uri);
				} else {
					Toast.makeText(EmployerCompanyActicity.this, "图片获取异常",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case 3:// 取得裁剪后的图片
				if (data != null) {
					Log.e("TAG", "-----------setPicToView---------------");
					setPicToView(data);
				}
				break;
			}
		}
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		try {
			Log.e("TAG", "-----------startPhotoZoom(uri)---------------");
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
			intent.putExtra("crop", "true");
			// aspectX aspectY 是宽高的比例
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			// outputX outputY 是裁剪图片宽高
			intent.putExtra("outputX", 300);
			intent.putExtra("outputY", 300);
			intent.putExtra("return-data", true);
			Log.e("TAG",
					"-----------startActivityForResult(intent, 3)---------------");
			startActivityForResult(intent, 3);
		} catch (Exception e) {
			Toast.makeText(EmployerCompanyActicity.this, "图片裁剪异常",
					Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Log.e("TAG", "-----------setPicToView---------------");
		Bundle extras = picdata.getExtras();
		String headBase64 = "";
		String imageName = "";
		Log.e("TAG", "-----------extras---------------" + extras.toString());
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			int newWidth = 100;
			if (photo.getWidth() >= 100) {
				newWidth = photo.getWidth();
			}
			int newHeight = 100;
			if (photo.getHeight() >= 100) {
				newHeight = photo.getHeight();
			}
			if (photo.getWidth() < 100 || photo.getHeight() < 100) {
				photo = Bitmap.createScaledBitmap(photo, newWidth, newHeight,
						false);
			}
			try {
				photo.compress(Bitmap.CompressFormat.JPEG, 80,
						new FileOutputStream(profileImgFile));
				headBase64 = ImageUtil.bitmapToBase64(photo);
				imageName = profileImgFile.getName();
			} catch (FileNotFoundException e) {
				Log.e("TAG", "-----------e.printStackTrace()---------------");
				e.printStackTrace();
			}
			boolean exit = profileImgFile.exists();
			if (!exit) {
				Toast.makeText(EmployerCompanyActicity.this,
						R.string.upload_photo_fail, Toast.LENGTH_SHORT).show();
				return;
			}

			mUploadCompanyUrlRequest = new UploadCompanyUrlRequest(
					EmployerCompanyActicity.this, imageName, headBase64);
			mUploadCompanyUrlRequest.setRequestId(UPLOAD_LOGOURL);
			httpPost(mUploadCompanyUrlRequest);
		}
	}

	/**
	 * 注册界面跳转
	 * 
	 * @param activity
	 */
	public static void invoke(Activity activity) {
		Intent intent = new Intent(activity, EmployerCompanyActicity.class);
		activity.startActivity(intent);
	}
}
