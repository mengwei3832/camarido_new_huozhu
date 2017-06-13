package com.yunqi.clientandroid.activity;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.util.UmengStatisticsUtils;
import com.yunqi.clientandroid.entity.LoginInfo;
import com.yunqi.clientandroid.http.AsyncHttp;
import com.yunqi.clientandroid.http.AsyncHttp.IHttpListener;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.PermissionsResultListener;
import com.yunqi.clientandroid.utils.PreManager;
import com.yunqi.clientandroid.utils.UserUtil;
import com.yunqi.clientandroid.view.LoadingDialog;

public abstract class BaseActivity extends FragmentActivity implements
		IHttpListener {
	protected Context mContext;
	private TextView tvTitle;
	private ImageButton ibLeftImage;
	public ImageButton ibRightImage;
	private TextView tvRight;
	private Dialog mLoadingDialog;
	protected Handler mHandler = new Handler();
	public View view;
	public View mainView;

	public UmengStatisticsUtils mUmeng;

	/**
	 * 图片加载
	 */
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	protected AsyncHttp mHttp = new AsyncHttp();

	protected AlertDialog builder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		initActionBar(null);
		mContext = this;
		if (getLayoutId() != 0) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mainView = inflater.inflate(getLayoutId(), null, true);
			setContentView(mainView);
		}
		// SDK在统计Fragment时，需要关闭Activity自带的页面统计，
		// 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
		MobclickAgent.openActivityDurationTrack(false);
		// 经过一段时间后再返回之前的应用,时间间隔
		MobclickAgent.setSessionContinueMillis(1000);

		mUmeng = UmengStatisticsUtils.instance(mContext);

		initView();
		setListener();
		initData();
	}

	/**
	 * 返回当前界面布局文件
	 * 
	 * @return
	 * @throws
	 * @Title:getLayoutId
	 * @Description:
	 * @return:int
	 * @Create: 2014年12月8日 下午2:40:17
	 * @Author : zhm
	 */
	protected abstract int getLayoutId();

	/**
	 * 此方法描述的是： 初始化所有view
	 * 
	 * @author: zhm
	 * @version: 2014-3-12 下午3:17:28
	 */
	protected abstract void initView();

	/**
	 * 此方法描述的是： 初始化所有数据的方法
	 * 
	 * @author: zhm
	 * @version: 2014-3-12 下午3:17:46
	 */
	protected abstract void initData();

	/**
	 * 此方法描述的是： 设置所有事件监听
	 * 
	 * @author: zhangwb
	 * @version: 2015-11-20 上午0:10:21
	 */
	protected abstract void setListener();

	@Override
	protected void onResume() {
		super.onResume();

		// umeng统计时间
		MobclickAgent.onResume(this);

	}

	@Override
	protected void onPause() {
		super.onPause();
		// umeng统计时间
		MobclickAgent.onPause(this);
		mHttp.cancelPost();
	}

	protected void httpPost(IRequest request) {
		mHttp.post(request, this);
	}

	protected void httpGet(IRequest request) {
		mHttp.get(request, this);
	}

	/**
	 * 显示等待弹窗
	 * @param message 弹窗显示的文本
	 */
	protected void showProgressDialog(String message){
		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		View net_view = layoutInflater.inflate(R.layout.dialog_wait_tishi, null);

		TextView tv = (TextView) net_view.findViewById(R.id.tv_dialog_wait);

		tv.setText(message);
        if (builder == null){
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            builder = dialog.create();
            builder.setView(net_view);
            builder.setCancelable(true);
            builder.setCanceledOnTouchOutside(false);
            builder.show();
        }
	}

	/**
	 * 隐藏等待弹窗
	 */
	protected void hideProgressDialog(){
		if (builder != null){
			builder.dismiss();
		}
	}

	/**
	 * 直接把JSON数据提交到服务器
	 * 
	 * @Description:
	 * @Title:httpPostJson
	 * @param request
	 * @return:void
	 * @throws
	 * @Create: 2015年11月22日 上午12:15:38
	 * @Author : zhm
	 */
	protected void httpPostJson(IRequest request) {
		mHttp.postJson(request, this);
	}

	@Override
	public void onStart(int requestId) {
		// TODO Auto-generated method stub
	}

	// ////////////////////Activity生命周期///////////////////////
	private String getClassName() {
		String canonicalName = this.getClass().getCanonicalName();
		String[] split = canonicalName.split("\\.");
		return split[split.length - 1];
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		if (response.isSuccess) {
			if (response.singleData instanceof LoginInfo) {
				LoginInfo loginInfo = (LoginInfo) response.singleData;
				PreManager.instance(this).setToken(loginInfo.tokenValue);
				UserUtil.setUserId(this, loginInfo.userId);
				PreManager.instance(this).setTokenExpires(
						loginInfo.tokenExpires);
			}
		} else {
			L.e("---BaseActivity----onSuccess----"+response.ErrCode);
			if (response.ErrCode == 10001) {
				// TODO 退出登录
				// 删除token过期时间
				PreManager.instance(mContext).removeTokenExpires();
				// 清空userId
				UserUtil.unSetUserId(mContext);
				// 跳转到登录界面
				LoginActicity.invoke((Activity) mContext);
				// 用户退出统计
				MobclickAgent.onProfileSignOff();
				// finish主界面
				((Activity) mContext).finish();
				CamaridoApp.destoryActivity("EmployerMainActivity");
			}
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		// TODO Auto-generated method stub
		L.e("---BaseActivity----onFailure----"+httpCode);
		if (httpCode == 401){
			// 删除token过期时间
			PreManager.instance(mContext).removeTokenExpires();
			// 清空userId
			UserUtil.unSetUserId(mContext);
			// 跳转到登录界面
			LoginActicity.invoke((Activity) mContext);
			// 用户退出统计
			MobclickAgent.onProfileSignOff();
			// finish主界面
			((Activity) mContext).finish();
			CamaridoApp.destoryActivity("EmployerMainActivity");
		}
	}

	public <T extends View> T obtainView(int resId) {
		return (T) findViewById(resId);
	}

	/**
	 * 设置ActionBar样式
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void initActionBar(View contentView) {

		// 1. 获取ActionBar Android 3.0 以上，使用 getActionBar
		// v7 包中的ActionBar 需要 ActionBarActivity 通过 getSupportActionBar()
		// 来获取
		ActionBar actionBar = this.getActionBar();

		if (actionBar == null) {
			return;
		}
		// 显示最左侧箭头
		ColorDrawable icon = new ColorDrawable(Color.TRANSPARENT);// 设置透明

		// 去掉应用程序的图标 , 有两种方法
		actionBar.setIcon(icon); // API 14
		actionBar.setLogo(icon); // API 14 才可以使用

		actionBar.setDisplayShowHomeEnabled(false);
		// 去掉标题
		actionBar.setTitle("");

		// 设置居中的标题
		actionBar.setDisplayShowCustomEnabled(true);

		if (contentView == null) {

			view = LayoutInflater.from(this).inflate(R.layout.action_bar_title,
					null);
			tvTitle = (TextView) view.findViewById(R.id.tv_title);
			ibLeftImage = (ImageButton) view.findViewById(R.id.ib_left);
			ibRightImage = (ImageButton) view.findViewById(R.id.ib_right);
			tvRight = (TextView) view.findViewById(R.id.tv_right);

			actionBar.setCustomView(view, new ActionBar.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT));
		} else {
			actionBar.setCustomView(contentView, new ActionBar.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT));
		}

	}

	/**
	 * 显示toast
	 * 
	 * @param resId
	 */
	public void showToast(final int resId) {
		showToast(getString(resId));
	}

	/**
	 * 显示toast
	 * 
	 * @param resStr
	 * @return Toast对象，便于控制toast的显示与关闭
	 */
	public Toast showToast(final String resStr) {

		if (TextUtils.isEmpty(resStr)) {
			return null;
		}

		Toast toast = null;

		mHandler.post(new Runnable() {
			@Override
			public void run() {
				Toast toast = Toast.makeText(BaseActivity.this, resStr,
						Toast.LENGTH_SHORT);
				toast.show();
			}
		});
		return toast;
	}

	public void showLoading(boolean show) {

		try {
			if (show) {
				// 由于这个dialog可能是由不同的activity唤起，所以每次都新建
				if (mLoadingDialog == null) {
					mLoadingDialog = new LoadingDialog(this,
							R.style.lodingDialog);
				}
				try {
					mLoadingDialog.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
					mLoadingDialog.cancel();
					mLoadingDialog = null;
				}
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 设置titlebar左边样式
	 * 
	 * @param resId
	 *            图片资源id
	 */
	public void setActionBarLeft(int resId) {
		if (ibLeftImage != null) {
			if (resId != 0) {
				ibLeftImage.setImageResource(resId);
				ibLeftImage.setVisibility(View.VISIBLE);
			} else {
				ibLeftImage.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 设置titlebar右边样式
	 * 
	 * @param isShow
	 *            是否显示 true 显示 false不显示
	 * @param imageResId
	 *            如果是图片，图片资源id 不是可以传0
	 * @param rightText
	 *            文本样式，要显示的文本
	 */
	public void setActionBarRight(boolean isShow, int imageResId,
			String rightText) {
		if (isShow) {
			if (imageResId != 0) {
				if (ibRightImage != null) {
					tvRight.setVisibility(View.GONE);
					ibRightImage.setImageResource(imageResId);
					ibRightImage.setVisibility(View.VISIBLE);
				}
			} else {
				tvRight.setVisibility(View.VISIBLE);
				ibRightImage.setVisibility(View.GONE);
				tvRight.setText(rightText);
			}
		} else {
			ibRightImage.setVisibility(View.GONE);
			tvRight.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setActionBarTitle(String title) {
		if (tvTitle != null) {
			tvTitle.setVisibility(View.VISIBLE);
			tvTitle.setText(title);
		}
	}

	/**
	 * 设置左边点击监听
	 * 
	 * @param leftClickListener
	 */
	public void setOnActionBarLeftClickListener(
			View.OnClickListener leftClickListener) {
		ibLeftImage.setOnClickListener(leftClickListener);
	}

	/**
	 * 设置右边点击监听
	 * 
	 * @param isImage
	 *            是否是图片
	 * @param rightClickListener
	 */
	public void setOnActionBarRightClickListener(boolean isImage,
			View.OnClickListener rightClickListener) {
		if (isImage) {
			ibRightImage.setOnClickListener(rightClickListener);
		} else {
			tvRight.setOnClickListener(rightClickListener);
		}
	}

	/**
	 * 
	 * @Description:给页面设置白板
	 * @Title:setBaiBan
	 * @param isNull
	 * @return:void
	 * @throws
	 * @Create: 2016年6月30日 下午4:48:43
	 * @Author : chengtao
	 */
	public void setBaiBan(boolean isNull) {
		if (isNull) {
			mainView.setBackgroundResource(R.drawable.baiban);
		} else {
			mainView.setBackgroundResource(0);
		}
	}

}
