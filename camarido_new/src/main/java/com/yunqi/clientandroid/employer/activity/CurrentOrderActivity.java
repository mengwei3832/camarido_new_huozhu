package com.yunqi.clientandroid.employer.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.activity.LoginActicity;
import com.yunqi.clientandroid.employer.fragment.CurrentOrderFragment;
import com.yunqi.clientandroid.employer.util.SpManager;
import com.yunqi.clientandroid.utils.PreManager;
import com.yunqi.clientandroid.utils.UserUtil;

/**
 * @author zhangwenbin zhangwb@zhongsou.com
 * @version version_code (e.g, V5.0.1)
 * @Copyright (c) 2016 zhongsou
 * @Description class description 发包方当前订单界面
 * @date 16/1/18
 */
public class CurrentOrderActivity extends BaseActivity implements
		View.OnClickListener, View.OnKeyListener {

	private CurrentOrderFragment mCurrentOrderFragment;
	private FrameLayout flCurrentOrder;
	private LinearLayout llStatusChoice;
	// 列表种类弹窗
	private PopupWindow mStatusChoiceWindow;
	private ImageView ivLine;
	private ImageView ivFilter;
	private ImageView ibSearch;
	private EditText etSearch;
	private TextView tvFilter;
	// 发包方状态选择SP
	private SpManager mSpManager;

	private boolean isExit = false;// 是否退出

	@Override
	protected int getLayoutId() {
		return R.layout.activity_current_order_employer;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();
		// 初始化列表种类弹窗
		createSortPopuwindow();

		flCurrentOrder = obtainView(R.id.fl_current_order_employer);
		llStatusChoice = obtainView(R.id.ll_filter_employer);
		ivLine = obtainView(R.id.iv_line_employer);
		ivFilter = obtainView(R.id.iv_filter_employer);
		etSearch = obtainView(R.id.et_search_employer);
		ibSearch = obtainView(R.id.ib_search_employer);
		tvFilter = obtainView(R.id.tv_filter_employer);
	}

	@Override
	protected void initData() {
		mSpManager = SpManager.instance(CurrentOrderActivity.this);
		mCurrentOrderFragment = new CurrentOrderFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fl_current_order_employer, mCurrentOrderFragment)
				.commit();
		// 设置初始列表类型
		setStatusText(mSpManager.getStatusType());
	}

	@Override
	protected void setListener() {
		llStatusChoice.setOnClickListener(this);
		mStatusChoiceWindow
				.setOnDismissListener(new PopupWindow.OnDismissListener() {
					@Override
					public void onDismiss() {
						ivFilter.setImageResource(R.drawable.order_triangle_nomal);
					}
				});

		ibSearch.setOnClickListener(this);
		etSearch.setOnKeyListener(this);
		// 搜索框是否获取焦点监听
		// etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
		// @Override
		// public void onFocusChange(View v, boolean hasFocus) {
		// String hint;
		// if (hasFocus) {
		// hint = etSearch.getHint().toString().trim();
		// etSearch.setTag(hint);
		// etSearch.setHint("");
		// } else {
		// hint = etSearch.getTag().toString().trim();
		// etSearch.setHint(hint);
		// }
		// }
		// });
	}

	/**
	 * 初始化titileBar的方法
	 */
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(
				R.string.employer_current_order));
		setActionBarLeft(0);
		setOnActionBarLeftClickListener(null);
		setActionBarRight(true, 0, "退出");
		setOnActionBarRightClickListener(false, new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 删除token过期时间
				PreManager.instance(CurrentOrderActivity.this)
						.removeTokenExpires();
				// 清空userId
				UserUtil.unSetUserId(CurrentOrderActivity.this);
				// 跳转到登录界面
				LoginActicity.invoke(CurrentOrderActivity.this);
				// 用户退出统计
				MobclickAgent.onProfileSignOff();
				// finish主界面
				CurrentOrderActivity.this.finish();
			}
		});
	}

	/**
	 * 创建排序Pupwindow
	 */
	private void createSortPopuwindow() {
		View popupView = getLayoutInflater().inflate(
				R.layout.current_order_popuwindow, null);

		mStatusChoiceWindow = new PopupWindow(popupView,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		mStatusChoiceWindow.setFocusable(true);
		mStatusChoiceWindow.setOutsideTouchable(true);
		mStatusChoiceWindow.setBackgroundDrawable(new BitmapDrawable());
		popupView.findViewById(R.id.tv_wait_change_ticket).setOnClickListener(
				this);
		popupView.findViewById(R.id.tv_wait_shipment).setOnClickListener(this);
		popupView.findViewById(R.id.tv_wait_receive).setOnClickListener(this);
		popupView.findViewById(R.id.tv_wait_check).setOnClickListener(this);
		popupView.findViewById(R.id.tv_wait_settlement)
				.setOnClickListener(this);
		popupView.findViewById(R.id.tv_all).setOnClickListener(this);
	}

	/**
	 * 弹出框消失
	 */
	private void dismissPupWindows() {
		if (null != mStatusChoiceWindow) {
			mStatusChoiceWindow.dismiss();
			ivFilter.setImageResource(R.drawable.order_triangle_nomal);
		}
	}

	/**
	 * 显示弹出框
	 */
	private void showPupWindow() {
		// 设置弹窗的显示位置
		mStatusChoiceWindow.showAsDropDown(ivLine, 0, 0);
		ivFilter.setImageResource(R.drawable.order_triangle_nomal_click);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_filter_employer:
			if (mStatusChoiceWindow.isShowing()) {
				dismissPupWindows();
			} else {
				showPupWindow();
			}
			break;
		case R.id.tv_wait_change_ticket:// 待换票
			dismissPupWindows();
			mCurrentOrderFragment.filterRequest(2, etSearch.getText()
					.toString().trim());
			setStatusText(2);
			break;
		case R.id.tv_wait_shipment:// 待装运
			dismissPupWindows();
			mCurrentOrderFragment.filterRequest(3, etSearch.getText()
					.toString().trim());
			setStatusText(3);
			break;
		case R.id.tv_wait_receive:// 待收货
			dismissPupWindows();
			mCurrentOrderFragment.filterRequest(4, etSearch.getText()
					.toString().trim());
			setStatusText(4);
			break;
		case R.id.tv_wait_check:// 待审核
			dismissPupWindows();
			mCurrentOrderFragment.filterRequest(5, etSearch.getText()
					.toString().trim());
			setStatusText(5);
			break;
		case R.id.tv_wait_settlement:// 已结算
			dismissPupWindows();
			mCurrentOrderFragment.filterRequest(8, etSearch.getText()
					.toString().trim());
			setStatusText(8);
			break;
		case R.id.tv_all:// 全部
			dismissPupWindows();
			mCurrentOrderFragment.filterRequest(-1, etSearch.getText()
					.toString().trim());
			setStatusText(-1);
			break;
		case R.id.ib_search_employer: // 搜索
			mCurrentOrderFragment.filterRequest(
					SpManager.instance(CurrentOrderActivity.this)
							.getStatusType(), etSearch.getText().toString()
							.trim());
			break;
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				mCurrentOrderFragment.filterRequest(
						SpManager.instance(CurrentOrderActivity.this)
								.getStatusType(), etSearch.getText().toString()
								.trim());
			}
			return true;
		}
		return false;
	}

	/**
	 * 刷新列表接口
	 */
	public interface IRequestOrder {
		void filterRequest(int ticketStatus, String keyWord);
	}

	/**
	 * 设置状态选择文字
	 * 
	 * @param status
	 */
	private void setStatusText(int status) {
		switch (status) {
		case -1:
			tvFilter.setText(this.getResources().getString(R.string.wait_all));
			break;
		case 2:
			tvFilter.setText(this.getResources().getString(
					R.string.wait_change_ticket));
			break;
		case 3:
			tvFilter.setText(this.getResources().getString(
					R.string.wait_shipment));
			break;
		case 4:
			tvFilter.setText(this.getResources().getString(
					R.string.wait_receive));
			break;
		case 5:
			tvFilter.setText(this.getResources().getString(R.string.wait_check));
			break;
		case 8:
			tvFilter.setText(this.getResources().getString(
					R.string.wait_settlement));
			break;
		}
	}

	// 监听手机返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 再按一次退出程序
			exit();
		}
		return false;
	}

	// 再按一次退出程序的方法
	private void exit() {
		if (!isExit) {
			isExit = true;
			showToast("再按一次退出程序");
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false;
				}
			}, 2000);
		} else {
			CurrentOrderActivity.this.finish();
		}
	}

	/**
	 * 本界面invoke跳转方法
	 */
	public static void invoke(Context context) {
		Intent intent = new Intent(context, CurrentOrderActivity.class);
		context.startActivity(intent);
	}

}
