package com.yunqi.clientandroid.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.LoginActicity;
import com.yunqi.clientandroid.employer.util.UmengStatisticsUtils;
import com.yunqi.clientandroid.http.AsyncHttp;
import com.yunqi.clientandroid.http.AsyncHttp.IHttpListener;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.PreManager;
import com.yunqi.clientandroid.utils.UserUtil;

public abstract class BaseFragment extends Fragment implements IHttpListener {
	/**
	 * 图片加载
	 */
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	protected AsyncHttp mHttp = new AsyncHttp();

	protected Handler mHandler = new Handler();

	protected View mRootView;

	public UmengStatisticsUtils mUmeng;
	private AlertDialog builder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (getLayoutId() != 0) {
			mRootView = inflater.inflate(getLayoutId(), container, false);
		}

		mUmeng = UmengStatisticsUtils.instance(getActivity());

		initView(mRootView);
		setListener();
		initData();
		return mRootView;
	}

	/**
	 * 显示等待弹窗
	 * @param message 弹窗显示的文本
	 */
	protected void showProgressDialog(String message){
		LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
		View net_view = layoutInflater.inflate(R.layout.dialog_wait_tishi, null);

		TextView tv = (TextView) net_view.findViewById(R.id.tv_dialog_wait);

		tv.setText(message);

		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		builder = dialog.create();
		builder.setView(net_view);
		builder.setCancelable(true);
		builder.setCanceledOnTouchOutside(false);
		builder.show();
	}

	/**
	 * 隐藏等待弹窗
	 */
	protected void hideProgressDialog(){
		builder.dismiss();
	}

	@Override
	public void onStart() {
		// initData();
		super.onStart();
	}

	/**
	 * 此方法描述的是： 初始化所有数据的方法
	 * 
	 * @author: zhm
	 * @version: 2014-3-12 下午3:17:46
	 */
	protected abstract void initData();

	/**
	 * 此方法描述的是： 获取布局
	 * 
	 * @author: zhangwb
	 * @version: 2015-11-20 上午0:10:30
	 */
	protected abstract int getLayoutId();

	/**
	 * 此方法描述的是： 初始化界面
	 * 
	 * @author: zhangwb
	 * @version: 2015-11-20 上午0:10:30
	 */
	protected abstract void initView(View _rootView);

	/**
	 * 
	 * @Description:获取控件对象
	 * @Title:obtainView
	 * @param id
	 * @return
	 * @return:T
	 * @throws
	 * @Create: 2016-5-10 下午6:25:33
	 * @Author : zhm
	 */
	protected <T extends View> T obtainView(int id) {
		return (T) mRootView.findViewById(id);
	}

	/**
	 * 此方法描述的是： 初始化界面
	 * 
	 * @author: zhangwb
	 * @version: 2015-11-20 下午13:10:30
	 */
	protected abstract void setListener();

	@Override
	public void onSuccess(int requestId, Response response) {
		if (!response.isSuccess) {
			L.e("---BaseFragment----onSuccess----"+response.ErrCode);
			if (response.ErrCode == 10001) {
				// TODO 退出登录
				// 删除token过期时间
				PreManager.instance(getActivity()).removeTokenExpires();
				// 清空userId
				UserUtil.unSetUserId(getActivity());
				// 跳转到登录界面
				LoginActicity.invoke(getActivity());
				// 用户退出统计
				MobclickAgent.onProfileSignOff();
				// finish主界面
				(getActivity()).finish();
				CamaridoApp.destoryActivity("EmployerMainActivity");
			}
		}

	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		// TODO Auto-generated method stub
		L.e("---BaseFragment----onFailure----"+httpCode);
		if (httpCode == 401){
			// 删除token过期时间
			PreManager.instance(getActivity()).removeTokenExpires();
			// 清空userId
			UserUtil.unSetUserId(getActivity());
			// 跳转到登录界面
			LoginActicity.invoke(getActivity());
			// 用户退出统计
			MobclickAgent.onProfileSignOff();
			// finish主界面
			(getActivity()).finish();
			CamaridoApp.destoryActivity("EmployerMainActivity");
		}
	}

	@Override
	public void onStart(int requestId) {

	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClassName()); // 统计页面
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClassName());// 统计页面
	}

	// //////////////////Fragment生命周期管理 结束//////////////////////////////////

	protected String getClassName() {
		return this.getClass().getSimpleName();
	}

	protected void httpPost(IRequest request) {
		mHttp.post(request, this);
	}

	protected void httpGet(IRequest request) {
		mHttp.get(request, this);
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

	/**
	 * 显示toast
	 * 
	 * @param resId
	 */
	public void showToast(final int resId) {
		showToast(getActivity().getString(resId));
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
				Toast toast = Toast.makeText(CamaridoApp.instance, resStr,
						Toast.LENGTH_SHORT);
				toast.show();
			}
		});
		return toast;
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
			mRootView.setBackgroundResource(R.drawable.baiban);
		} else {
			mRootView.setBackgroundResource(0);
		}
	}

}
