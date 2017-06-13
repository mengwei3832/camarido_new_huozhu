package com.yunqi.clientandroid.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.adapter.TaskAwardAdapter;
import com.yunqi.clientandroid.entity.NewCarSum;
import com.yunqi.clientandroid.entity.TaskAward;
import com.yunqi.clientandroid.http.request.TaskAwardRequest;
import com.yunqi.clientandroid.http.request.TaskSumMoneyRquest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.view.TaskAwardListView;

/**
 * 
 * @Description:class 新手奖励
 * @ClassName: TaskAwardActivity
 * @author: zhm
 * @date: 2016-4-6 上午11:33:22
 * 
 */
public class TaskAwardActivity extends BaseActivity implements
		OnScrollListener, View.OnClickListener {
	private TaskAwardListView lv_task_award; // listview的
	private TextView tv_task_gain; // 获得收益
	private TextView tv_task_total; // 累计收益
	private PopupWindow pTaskPopouWindow;
	private RelativeLayout rl_task_award;
	private RelativeLayout ll_task_award;
	private TaskAwardAdapter taskAwardAdapter;
	private TaskAwardRequest taskAwardRequest;
	private TaskSumMoneyRquest taskSumMoneyRquest;
	private AlertDialog alertDialog = null;
	private int pagenation = 1;
	private int pageSize = 6;

	// 本页面请求Id
	public final int GET_TASK_CONTENT = 1;
	public final int GET_TASK_SUMMONEY = 2;
	private LinearLayout ll_task_tanchu;
	private LinearLayout ll_task_guize;

	// 保存使用SharedPreferences
	private SharedPreferences spf;
	private Editor editor;
	private boolean isOne;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_task_award;
	}

	@Override
	protected void initView() {
		initActionBar();
		lv_task_award = (TaskAwardListView) findViewById(R.id.lv_task_award);
		tv_task_gain = (TextView) findViewById(R.id.tv_task_gain);
		tv_task_total = (TextView) findViewById(R.id.tv_task_total);
		ll_task_award = (RelativeLayout) findViewById(R.id.ll_task_award);
		ll_task_tanchu = (LinearLayout) findViewById(R.id.ll_task_tanchu);
		ll_task_guize = (LinearLayout) findViewById(R.id.ll_task_guize);
	}

	// 初始化titileBar的方法
	private void initActionBar() {
		// 设置titileBar的标题
		setActionBarTitle(this.getResources().getString(R.string.task_title));
		// 设置左边的返回箭头
		setActionBarLeft(R.drawable.nav_back);
		// 给左边的返回箭头加监听
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 关闭当前的Activity页面
				TaskAwardActivity.this.finish();
			}
		});

	}

	@Override
	protected void initData() {
		L.e("TAG", "弹出规则----------------------------------------");

		// 进行判断是否第一次进入
		spf = getSharedPreferences("first", MODE_WORLD_WRITEABLE);
		isOne = spf.getBoolean("isOne", true);
		editor = spf.edit();
		if (isOne) {
			L.e("TAG", "第一次进入-------------");

			// 弹出新手奖励的规则
			showTaskPopup();

			editor.putBoolean("isOne", false);
			editor.commit();
		} else {
			L.e("TAG", "不是第一次进入-------------");
		}

		// 新手奖励内容的请求
		getTaskAward();

	}

	// 新手奖励内容的请求
	private void getTaskAward() {
		taskAwardRequest = new TaskAwardRequest(TaskAwardActivity.this,
				pageSize, pagenation);
		taskAwardRequest.setRequestId(GET_TASK_CONTENT);
		httpPost(taskAwardRequest);

		taskSumMoneyRquest = new TaskSumMoneyRquest(TaskAwardActivity.this);
		taskSumMoneyRquest.setRequestId(GET_TASK_SUMMONEY);
		httpPost(taskSumMoneyRquest);
	}

	@Override
	public void onStart(int requestId) {
		L.e("TAG", "jinlaile---------------------------------");
		super.onStart(requestId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);

		boolean isSuccess;
		String message;

		switch (requestId) {
		case GET_TASK_CONTENT: // 获取新手奖励的内容
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				ArrayList<TaskAward> taskList = response.data;

				taskAwardAdapter = new TaskAwardAdapter(taskList, this);
				lv_task_award.setAdapter(taskAwardAdapter);
			} else {
				showToast(message);
			}
			break;
		case GET_TASK_SUMMONEY: // 获取累计金额数
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				NewCarSum ncSum = (NewCarSum) response.singleData;
				double taskSumMoney = ncSum.newRewardRecordByUserSum;
				L.e("TAG", "累计金额总数：--------------------" + taskSumMoney);

				tv_task_total.setText(taskSumMoney + "元");
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
	}

	// 设置任务弹出框的信息
	// private void settingTask() {
	// pTaskPopouWindow = new PopupWindow(TaskAwardActivity.this);
	// View task_view = TaskAwardActivity.this.getLayoutInflater().inflate(
	// R.layout.popupwindow_task_award, null);
	//
	// pTaskPopouWindow.setWidth(LayoutParams.MATCH_PARENT);
	// pTaskPopouWindow.setHeight(LayoutParams.WRAP_CONTENT);
	// pTaskPopouWindow.setBackgroundDrawable(new BitmapDrawable());
	// pTaskPopouWindow.setContentView(task_view);
	// pTaskPopouWindow.setOutsideTouchable(true);
	// pTaskPopouWindow.setFocusable(true);
	// pTaskPopouWindow.setTouchable(true); // 设置PopupWindow可触摸
	// pTaskPopouWindow
	// .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	// rl_task_award = (RelativeLayout) task_view
	// .findViewById(R.id.rl_task_award);
	//
	// }

	// 弹出新手奖励的规则框
	private void showTaskPopup() {
		alertDialog = new AlertDialog.Builder(TaskAwardActivity.this).create();
		alertDialog.show();
		alertDialog.getWindow().setContentView(R.layout.popupwindow_task_award);
		alertDialog.getWindow().findViewById(R.id.rl_task_award)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						alertDialog.dismiss();
					}
				});
	}

	@Override
	protected void setListener() {
		// listview的滑动监听
		lv_task_award.setOnScrollListener(this);
		ll_task_guize.setOnClickListener(this);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		// 当不滚动时
		case OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
			// 判断滚动到底部
			if (lv_task_award.getLastVisiblePosition() == (lv_task_award
					.getCount() - 1)) {
				pagenation += 1;
			}
			break;

		default:
			break;
		}
	}

	// 本界面的跳转方法
	public static void invoke(Activity activity) {
		Intent intent = new Intent(activity, TaskAwardActivity.class);
		activity.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_task_guize:
			showTaskPopup();
			break;

		default:
			break;
		}
	}

}
