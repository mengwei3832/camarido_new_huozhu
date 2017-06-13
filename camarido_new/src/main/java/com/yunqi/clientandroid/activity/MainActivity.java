package com.yunqi.clientandroid.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.fragment.DiscoverFragment;
import com.yunqi.clientandroid.fragment.HomeFragment;
import com.yunqi.clientandroid.fragment.MineFragment;
import com.yunqi.clientandroid.fragment.OrderFragment;
import com.yunqi.clientandroid.http.HostUtil;
import com.yunqi.clientandroid.http.HostUtil.HostType;
import com.yunqi.clientandroid.receiver.MiPushReceiver;
import com.yunqi.clientandroid.utils.FilterManager;
import com.yunqi.clientandroid.utils.PreManager;

/**
 * @author zhangwb
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class app主界面
 * @date 15/11/19
 */
public class MainActivity extends BaseActivity implements
		RadioGroup.OnCheckedChangeListener, HomeFragment.IChangeMain {
	private RadioGroup rgMain;
	private ImageView ivRed;
	private final int HOME_TAB = 1;
	private final int ORDER_TAB = 2;
	private final int DISCOVER_TAB = 3;
	private final int MINE_TAB = 4;
	private boolean isExit = false;// 是否退出
	private PreManager mPreManager;
	private FilterManager mFilterManager;
	private BubbleReceiver mBubbleReceiver;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_main;
	}

	@Override
	protected void initView() {
		rgMain = obtainView(R.id.rg_main);
		ivRed = obtainView(R.id.iv_red);

		if (HostUtil.type == HostType.PUBLIC_HOST) {
//			/**
//			 * 友盟自动更新设置
//			 */
//			UmengUpdateAgent.setDefault();
//			// 每次检测更新的函数之前先恢复默认设置再设置参数，避免在其他地方设置的参数影响到这次更新
//			// 请在调用update,forceUpdate函数之前设置推广id，silentUpdate不支持此功能
//			// UmengUpdateAgent.setSlotId("54357");
//			// UmengUpdateAgent.setRichNotification(true);//通知栏显示暂停/取消按钮--默认为true
//			//
//			UmengUpdateAgent.setUpdateAutoPopup(true);// 自动弹出更新提示的对话框/通知栏--默认为true
//			UmengUpdateAgent.setUpdateOnlyWifi(false);
//			// 在任意网络环境下都进行更新自动提醒--默认为true
//			UmengUpdateAgent.update(MainActivity.this);// 调用umeng自动更新接口
//			// UmengUpdateAgent.setUpdateCheckConfig(false);//禁用集成检测--默认为true
//			UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
//
//				@Override
//				public void onClick(int status) {
//					switch (status) {
//					case UpdateStatus.Update:
//						break;
//					default:
//						// close the app
//						showToast("非常抱歉，您需要更新应用才能继续使用");
//						MainActivity.this.finish();
//					}
//				}
//			});
		}
	}

	@Override
	protected void initData() {
		setReceiver();
		CamaridoApp.addDestoryActivity(MainActivity.this, "MainActivity");
		mFilterManager = FilterManager.instance(this);
		rgMain.check(R.id.rb_home);

	}

	@Override
	protected void setListener() {
		rgMain.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_home:
			// 当前选中的是首页
			mFilterManager.clearSp();
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.fl_container, new HomeFragment(),
							"HOME_FRAGMENT").commit();
			break;
		case R.id.rb_order:
			// 当前选中的是订单
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fl_container, new OrderFragment()).commit();
			break;
		case R.id.rb_discover:
			// 当前选中的是发现
			mFilterManager.clearSp();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fl_container, new DiscoverFragment())
					.commit();
			break;
		case R.id.rb_mine:
			// 当前选中的是个人中心
			mFilterManager.clearSp();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fl_container, new MineFragment()).commit();
			break;
		default:
			break;
		}
	}

	@Override
	public void changeTab(int tabIndex) {
		switch (tabIndex) {
		case HOME_TAB:
			rgMain.check(R.id.rb_home);
			break;
		case ORDER_TAB:
			rgMain.check(R.id.rb_order);
			break;
		case DISCOVER_TAB:
			rgMain.check(R.id.rb_discover);
			break;
		case MINE_TAB:
			rgMain.check(R.id.rb_mine);
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
			MainActivity.this.finish();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 设置红色点
		setRedBubble();
	}

	/**
	 * 设置红色点
	 */
	private void setRedBubble() {
		mPreManager = PreManager.instance(MainActivity.this);
		int msgCount = mPreManager.getMsgBubble();
		int attentionCount = mPreManager.getAttentionBubble();
		int activeCount = mPreManager.getActiveBubble();

		if (msgCount > 0 || attentionCount > 0 || activeCount > 0) {
			ivRed.setVisibility(View.VISIBLE);
		} else {
			ivRed.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置广播
	 */
	private void setReceiver() {
		IntentFilter inf = new IntentFilter();
		inf.addAction(MiPushReceiver.MIPUSH_ACTION);
		mBubbleReceiver = new BubbleReceiver();
		registerReceiver(mBubbleReceiver, inf);
	}

	/**
	 * 气泡广播
	 */
	private class BubbleReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(MiPushReceiver.MIPUSH_ACTION)) {// 红点气泡
				setRedBubble();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mBubbleReceiver != null) {
			unregisterReceiver(mBubbleReceiver);
		}
	}

	/**
	 * 本界面跳转方法
	 */
	public static void invoke(Context activity) {
		Intent intent = new Intent();
		intent.setClass(activity, MainActivity.class);
		activity.startActivity(intent);
	}

}
