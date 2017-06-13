package com.yunqi.clientandroid.activity;

import java.util.ArrayList;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.adapter.RecommendAwardAdapter;
import com.yunqi.clientandroid.entity.REcommendSum;
import com.yunqi.clientandroid.entity.RecommendAward;
import com.yunqi.clientandroid.http.request.RecommendRequest;
import com.yunqi.clientandroid.http.request.RecommendSumRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.L;

/**
 * 
 * @Description:class 推荐奖励
 * @ClassName: RecommendActivity
 * @author: zhm
 * @date: 2016-4-6 下午6:53:51
 * 
 */
public class RecommendActivity extends BaseActivity implements
		OnScrollListener, View.OnClickListener {
	private TextView tv_recommed_marquee;// 滚马灯的控件
	private ListView lv_recommend_award; // listview的控件
	private PopupWindow recommendPopupWindow;
	private RelativeLayout rl_recommend_award;
	private LinearLayout ll_recommend_award;
	private RecommendAwardAdapter recommendAwardAdapter;
	private TextView tv_recommend_feixiang_anjian;// 规则和分享按键
	private ArrayList<RecommendAward> listRecommend = new ArrayList<RecommendAward>();
	private ArrayList<String> listUserPhone = new ArrayList<String>();
	private String sumMoney;

	private int pagenation = 1;
	private int pageSize = 6;
	private AlertDialog alertDialog = null;

	// 本页请求ID
	private final int GET_RECOMMEND_CONTENT = 1;
	private final int GET_RECOMMEND_SUM_MONEY = 2;

	private RecommendRequest recommendRequest;
	private RecommendSumRequest recommendSumRequest;

	// 保存使用SharedPreferences
	private SharedPreferences spf;
	private Editor editor;
	private boolean isFirst;
	private LinearLayout ll_recommend_guize;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_recommend_award;
	}

	@Override
	protected void initView() {
		initActionBar();

		tv_recommed_marquee = (TextView) findViewById(R.id.tv_recommed_marquee);
		lv_recommend_award = (ListView) findViewById(R.id.lv_recommend_award);

		tv_recommend_feixiang_anjian = (TextView) findViewById(R.id.tv_recommend_feixiang_anjian);
		ll_recommend_guize = (LinearLayout) findViewById(R.id.ll_recommend_guize);
	}

	// 初始化titileBar的方法
	private void initActionBar() {
		// 设置titileBar的标题
		setActionBarTitle(this.getResources().getString(
				R.string.recommend_title));
		// 设置左边的返回箭头
		setActionBarLeft(R.drawable.nav_back);
		// 给左边的返回箭头加监听
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 关闭当前的Activity页面
				RecommendActivity.this.finish();
			}
		});

	}

	@Override
	protected void initData() {
		// 进行判断是否第一次进入
		spf = getSharedPreferences("first", MODE_WORLD_WRITEABLE);
		isFirst = spf.getBoolean("isFirst", true);
		editor = spf.edit();
		if (isFirst) {
			L.e("TAG", "第一次进入-------------");

			// 弹出新手奖励的规则
			showRecommendPopup();

			editor.putBoolean("isFirst", false);
			editor.commit();
		} else {
			L.e("TAG", "不是第一次进入-------------");
		}

		// 获取推荐奖励的内容
		getRecommend();

		// 获取总价列表请求的内容
		getRecommendSum();

	}

	// 获取总价列表请求的内容
	private void getRecommendSum() {
		L.e("TAG", "jinlaile-------------获取总价列表请求的内容--------------------");
		recommendSumRequest = new RecommendSumRequest(RecommendActivity.this);
		recommendSumRequest.setRequestId(GET_RECOMMEND_SUM_MONEY);
		httpPost(recommendSumRequest);
	}

	// 获取推荐奖励的内容请求
	private void getRecommend() {
		L.e("TAG", "jinlaile---------------获取推荐奖励的内容请求------------------");
		recommendRequest = new RecommendRequest(RecommendActivity.this,
				pageSize, pagenation);
		recommendRequest.setRequestId(GET_RECOMMEND_CONTENT);
		httpPost(recommendRequest);
	}

	@Override
	public void onStart(int requestId) {
		L.e("TAG", "jinlaile---------------------------------");
		super.onStart(requestId);
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);

		boolean isSuccess;
		String message;

		switch (requestId) {
		case GET_RECOMMEND_CONTENT:// 获取推荐奖励的内容

			L.e("TAG", "jinlaile------------获取推荐奖励的内容---------------------");
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				ArrayList<RecommendAward> rList = response.data;

				L.e("TAG", "列表数据---------------------" + rList.toString());

				int money = 0;
				for (int i = 0; i < rList.size(); i++) {
					money = (int) rList.get(i).rewardRecordType;

					L.e("TAG", "qian钱---------------------" + money);
				}

				recommendAwardAdapter = new RecommendAwardAdapter(rList,
						RecommendActivity.this);
				lv_recommend_award.setAdapter(recommendAwardAdapter);

				recommendAwardAdapter.notifyDataSetChanged();// 刷新界面
			} else {
				showToast(message);
			}

			break;
		case GET_RECOMMEND_SUM_MONEY:// 获取总价列表
			isSuccess = response.isSuccess;
			message = response.message;
			L.e("TAG", "jinlaile----------------获取总价列表-----------------");
			if (isSuccess) {
				ArrayList<REcommendSum> sumlist = response.data;

				for (int i = 0; i < sumlist.size(); i++) {
					sumMoney = sumlist.get(i).userPhone;
					if (TextUtils.isEmpty(sumMoney) && sumMoney == null) {
						sumMoney = "";
					}
					listUserPhone.add(sumMoney);
				}

				sumMoney = "";

				for (int i = 0; i < sumlist.size(); i++) {
					sumMoney += "用户"
							+ listUserPhone.get(i)
							+ "获得<font color='#ffff00'>"
							+ sumlist.get(i).userPhoneMoney
							+ "元</font>现金券<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</sapn>";
				}

				if (!TextUtils.isEmpty(sumMoney) && sumMoney != null) {
					tv_recommed_marquee.setText(Html.fromHtml(sumMoney));
				}
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
		showToast(this.getResources().getString(R.string.net_error_toast));
	}

	// 弹出新手奖励的规则
	private void showRecommendPopup() {
		alertDialog = new AlertDialog.Builder(RecommendActivity.this).create();
		alertDialog.show();
		alertDialog.getWindow().setContentView(
				R.layout.popupwindow_recommend_award);
		alertDialog.getWindow().findViewById(R.id.rl_recommend_award)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						alertDialog.dismiss();
					}
				});

	}

	// 设置推荐奖励的规则
	// private void settingRecommend() {
	// recommendPopupWindow = new PopupWindow(RecommendActivity.this);
	// View recommend_view = RecommendActivity.this.getLayoutInflater()
	// .inflate(R.layout.popupwindow_task_award, null);
	//
	// recommendPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
	// recommendPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
	// recommendPopupWindow.setBackgroundDrawable(new BitmapDrawable());
	// recommendPopupWindow.setContentView(recommend_view);
	// recommendPopupWindow.setOutsideTouchable(true);
	// recommendPopupWindow.setFocusable(true);
	// recommendPopupWindow.setTouchable(true); // 设置PopupWindow可触摸
	// recommendPopupWindow
	// .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	// rl_recommend_award = (RelativeLayout) recommend_view
	// .findViewById(R.id.rl_recommend_award);
	//
	// }

	@Override
	protected void setListener() {
		lv_recommend_award.setOnScrollListener(this);
		tv_recommend_feixiang_anjian.setOnClickListener(this);
		ll_recommend_guize.setOnClickListener(this);
	}

	// 本界面的跳转方法
	public static void invoke(Activity activity) {
		Intent intent = new Intent(activity, RecommendActivity.class);
		activity.startActivity(intent);
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
			if (lv_recommend_award.getLastVisiblePosition() == (lv_recommend_award
					.getCount() - 1)) {
				pagenation += 1;
			}
			break;

		default:
			break;
		}

	}

	// 点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_recommend_feixiang_anjian:// 点击分享按键
			MyPromotionActivity.invoke(RecommendActivity.this);
			break;

		case R.id.ll_recommend_guize:// 点击规则
			showRecommendPopup();
			break;

		default:
			break;
		}
	}

}
