package com.yunqi.clientandroid.employer.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.util.UmengStatisticsUtils;

/**
 * @Description:备注页面
 * @ClassName: RemarkActivity
 * @author: mengwei
 * @date: 2016-6-23 下午2:00:31
 * 
 */
public class RemarkActivity extends BaseActivity implements OnClickListener {
	/* 界面控件对象 */
	private EditText etLoading;
	private EditText etUnLoading;
	private EditText etDemand;
	private Button btFinish;

	// 跳转传值
	public static int requestCode;
	private boolean isCopyPackage = false;
	private String loading;
	private String unLoading;
	private String demand;

	/* 友盟统计 */
	private UmengStatisticsUtils mUmeng;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_remark;
	}

	@Override
	protected void initView() {
		initActionBar();
		// 获取传过来的值
		getTransFerMessage();

		mUmeng = UmengStatisticsUtils.instance(mContext);

		// 控件对象
		etLoading = obtainView(R.id.et_remark_loading);
		etUnLoading = obtainView(R.id.et_remark_unloading);
		etDemand = obtainView(R.id.et_remark_demand);
		btFinish = obtainView(R.id.bt_remark_finish);
	}

	/**
	 * 初始化titileBar的方法
	 */
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(
				R.string.employer_activity_remark_title));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RemarkActivity.this.finish();
			}
		});
	}

	/**
	 * @Description:获取传过来的值
	 * @Title:getTransFerMessage
	 * @return:void
	 * @throws
	 * @Create: 2016-6-23 下午3:01:02
	 * @Author : mengwei
	 */
	private void getTransFerMessage() {
		loading = getIntent().getStringExtra("loading");
		unLoading = getIntent().getStringExtra("unLoading");
		demand = getIntent().getStringExtra("demand");
		Log.e("TAG", "----------------getTransFerMessage");
		Log.e("TAG", "----------------loading" + loading);
		Log.e("TAG", "----------------unLoading" + unLoading);
		Log.e("TAG", "----------------demand" + demand);
	}

	@Override
	protected void initData() {
		if (loading != null && !TextUtils.isEmpty(loading)) {
			etLoading.setText(loading);
		}
		if (unLoading != null && !TextUtils.isEmpty(unLoading)) {
			etUnLoading.setText(unLoading);
		}
		if (demand != null && !TextUtils.isEmpty(demand)) {
			etDemand.setText(demand);
		}
	}

	@Override
	protected void setListener() {
		btFinish.setOnClickListener(this);
	}

	/**
	 * @Description:备注页面跳转
	 * @Title:invoke
	 * @param activity
	 * @return:void
	 * @throws
	 * @Create: 2016年6月22日 下午2:01:38
	 * @Author : mengwei
	 */
	public static void invoke(Activity activity, int mRequestCode,
			String loading, String unLoading, String demand) {
		Intent intent = new Intent(activity, RemarkActivity.class);
		requestCode = mRequestCode;
		intent.putExtra("loading", loading);
		intent.putExtra("unLoading", unLoading);
		intent.putExtra("demand", demand);
		activity.startActivityForResult(intent, mRequestCode);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_remark_finish:
			// 获取输入框的值
			loading = etLoading.getText().toString().trim();
			unLoading = etUnLoading.getText().toString().trim();
			demand = etDemand.getText().toString().trim();

			//友盟统计
			mUmeng.setCalculateEvents("ship_click_remarks_finish");

			Intent intent = new Intent();
			intent.putExtra("loading", loading);
			intent.putExtra("unLoading", unLoading);
			intent.putExtra("demand", demand);
			setResult(requestCode, intent);
			RemarkActivity.this.finish();
			break;

		default:
			break;
		}
	}

}
