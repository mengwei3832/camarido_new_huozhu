package com.yunqi.clientandroid.employer.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.activity.SearchListActivity;
import com.yunqi.clientandroid.employer.adapter.ZiXunLieBiaoAdapter;
import com.yunqi.clientandroid.employer.entity.ZiXunLieBiaoBean;
import com.yunqi.clientandroid.employer.request.ZiXunLieBiaoRequest;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:class 行业资讯页面
 * @ClassName: DiscoverZiXunActivity
 * @author: mengwei
 * @date: 2016-6-7 下午4:49:08
 * 
 */
public class DiscoverZiXunActivity extends BaseActivity implements
		OnItemClickListener {
	/**
	 * 界面控件对象
	 */
	private PullToRefreshListView lvLieBiaoZiXun;
	private ProgressBar pbZixunBar;

	private ArrayList<ZiXunLieBiaoBean> lielist = new ArrayList<ZiXunLieBiaoBean>();
	private ZiXunLieBiaoAdapter ziXunLieBiaoAdapter;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_discover_zixun;
	}

	@Override
	protected void initView() {
		// 初始化控件对象
		initLayoutView();
	}

	/**
	 * 初始化界面控件对象
	 */
	private void initLayoutView() {
		lvLieBiaoZiXun = obtainView(R.id.lv_employer_zixun_liebiao);
		pbZixunBar = obtainView(R.id.pb_liebiao_bar);

		ziXunLieBiaoAdapter = new ZiXunLieBiaoAdapter(lielist, mContext);
		lvLieBiaoZiXun.setAdapter(ziXunLieBiaoAdapter);
		initActionBar();
	}

	private void initActionBar() {
		setActionBarTitle("行业资讯");
		setActionBarLeft(R.drawable.fanhui);
		setOnActionBarLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void initData() {
		pbZixunBar.setVisibility(View.VISIBLE);
		httpPost(new ZiXunLieBiaoRequest(mContext));
	}

	@Override
	public void onStart(int requestId) {
		super.onStart(requestId);
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess = response.isSuccess;
		String message = response.message;
		if (isSuccess) {
			ArrayList<ZiXunLieBiaoBean> zilist = response.data;

			if (zilist != null) {
				lielist.addAll(zilist);
			}
		}

		pbZixunBar.setVisibility(View.GONE);
		ziXunLieBiaoAdapter.notifyDataSetChanged();
		lvLieBiaoZiXun.onRefreshComplete();
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(getString(R.string.net_error_toast));
		lvLieBiaoZiXun.onRefreshComplete();
	}

	@Override
	protected void setListener() {
		// 初始化监听事件
		initOnClick();
	}

	/**
	 * 初始化监听事件
	 */
	private void initOnClick() {
		lvLieBiaoZiXun.setOnItemClickListener(this);
	}

	/**
	 * 本界面跳转方法
	 */
	public static void invoke(Context activity) {
		Intent intent = new Intent();
		intent.setClass(activity, DiscoverZiXunActivity.class);
		activity.startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ZiXunLieBiaoBean ziXunLieBiaoBean = lielist.get(position - 1);

		int tagId = ziXunLieBiaoBean.id;
		String title = ziXunLieBiaoBean.tagName;

		Log.d("TAG", "-------tagId--------" + tagId);
		Log.d("TAG", "-------title--------" + title);

		HangYeLieBiaoActivity.invoke(mContext, title, tagId);
	}

}
