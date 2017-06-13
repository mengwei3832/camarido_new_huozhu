package com.yunqi.clientandroid.employer.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.fragment.EmploterBaoBiaoFragment;
import com.yunqi.clientandroid.employer.fragment.EmployerCompanyFragment;
import com.yunqi.clientandroid.employer.fragment.EmployerDiscoverFragment;
import com.yunqi.clientandroid.employer.fragment.EmployerHomeFragment;
import com.yunqi.clientandroid.employer.util.MessageRedUtils;
import com.yunqi.clientandroid.employer.util.UmengStatisticsUtils;
import com.yunqi.clientandroid.receiver.MiPushReceiver;
import com.yunqi.clientandroid.utils.FilterManager;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.PreManager;

/**
 * 
 * @Description:class 发包方App的主页面
 * @ClassName: EmployerMainActivity
 * @author: zhm
 * @date: 2016-5-11 上午10:00:01
 * 
 */
public class EmployerMainActivity extends BaseActivity implements
		RadioGroup.OnCheckedChangeListener, EmployerHomeFragment.IChangeMain {
	// 控件对象
	private RadioGroup rgMain;
	private ImageView ivRed;
	private ImageView ivRedZixun;

	private EmployerHomeFragment homeFragment;
	private EmploterBaoBiaoFragment baoFragment;
	private EmployerDiscoverFragment discoverFragment;
	private EmployerCompanyFragment companyFragment;

	private final int HOME_TAB = 1;
	private final int ORDER_TAB = 2;
	private final int DISCOVER_TAB = 3;
	private final int MINE_TAB = 4;
	private boolean isExit = false;// 是否退出
	private PreManager mPreManager;
	private FilterManager mFilterManager;
	private BubbleReceiver mBubbleReceiver;

	private String from = "";
	private static String FROM = "FROM";
	private FragmentManager manager;

	/** 判断发现页面资讯小红点的类 */
	private MessageRedUtils mMessageRedUtils;

	/* 友盟统计 */
	private UmengStatisticsUtils mUmeng;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_main;
	}

	@Override
	protected void initView() {
		rgMain = obtainView(R.id.rg_employer_main);
		ivRed = obtainView(R.id.iv_employer_red);
		ivRedZixun = obtainView(R.id.iv_employer_red_zixun);
		from = getIntent().getStringExtra(FROM) + "";
		Log.v("TAG", from);

		manager = getSupportFragmentManager();
		mUmeng = UmengStatisticsUtils.instance(mContext);

		mMessageRedUtils = new MessageRedUtils(mContext,ivRedZixun);
		new Thread(){
			@Override
			public void run() {
				mMessageRedUtils.get();
			}
		}.start();
	}

	@Override
	protected void initData() {
		setReceiver();
		CamaridoApp.addDestoryActivity(EmployerMainActivity.this,
				"EmployerMainActivity");
		mFilterManager = FilterManager.instance(this);
		// 初始化
		if (from.equals(EmployerPreViewActivity.FROM_PRE_VIEW)) {
			rgMain.check(R.id.rb_employer_order);
		} else {
			rgMain.check(R.id.rb_employer_home);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.e("TAG", "---------from-----------"+from);
		setIntent(intent);
		from = getIntent().getStringExtra(FROM) + "";
	}

	@Override
	protected void setListener() {
		rgMain.setOnCheckedChangeListener(this);

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_employer_home:
			//友盟统计首页
			mUmeng.setCalculateEvents("home_fragment");

			Log.e("TAG", "当前切换shouye页面-------------------");
			// 当前选中的是首页
			mFilterManager.clearSp();
			// getSupportFragmentManager()
			// .beginTransaction()
			// .replace(R.id.fl_employer_container,
			// new EmployerHomeFragment()).commit();
			showFragment(1);
			break;
		case R.id.rb_employer_order:
			//友盟统计首页
			mUmeng.setCalculateEvents("myorder_fragment");

			// 当前选中的是发包管理页面
			// getSupportFragmentManager().beginTransaction()
			// .replace(R.id.fl_employer_container, new
			// EmploterBaoBiaoFragment())
			// .commit();
			showFragment(2);
			break;
		case R.id.rb_employer_discover:
			//友盟统计首页
			mUmeng.setCalculateEvents("find_fragment");

			Log.e("TAG", "当前切换发现页面-------------------");
			// 当前选中的是发现
			mFilterManager.clearSp();
			ivRedZixun.setVisibility(View.GONE);
			// getSupportFragmentManager()
			// .beginTransaction()
			// .replace(R.id.fl_employer_container,
			// new EmployerDiscoverFragment()).commit();
			showFragment(3);
			break;
		case R.id.rb_employer_mine:
			//友盟统计首页
			mUmeng.setCalculateEvents("business_center_fragment");

			Log.e("TAG", "当前切换公路指数页面-------------------");
			// 当前选中的是企业
			mFilterManager.clearSp();
			// getSupportFragmentManager()
			// .beginTransaction()
			// .replace(R.id.fl_employer_container,
			// new EmployerCompanyFragment()).commit();
			showFragment(4);
			break;
		default:
			break;
		}

	}

	/**
	 * 
	 * @Description:显示或隐藏fragment
	 * @Title:showFragment
	 * @return:void
	 * @throws
	 * @Create: 2016-8-22 下午1:41:19
	 * @Author : mengwei
	 */
	private void showFragment(int checkId) {
		switch (checkId) {
		case 1:
			FragmentTransaction transaction1 = manager.beginTransaction();
			// 隐藏全部fragment
			hideAllFragment(transaction1);
			if (homeFragment == null) {
				homeFragment = new EmployerHomeFragment();
				transaction1.add(R.id.fl_employer_container, homeFragment);
			} else {
				transaction1.show(homeFragment);
			}
			transaction1.commit();
			break;
		case 2:
			FragmentTransaction transaction2 = manager.beginTransaction();
			// 隐藏全部fragment
			hideAllFragment(transaction2);
			if (baoFragment == null) {
				baoFragment = new EmploterBaoBiaoFragment();
				transaction2.add(R.id.fl_employer_container, baoFragment);
			} else {
				transaction2.show(baoFragment);
			}
			transaction2.commit();
			break;
		case 3:
			FragmentTransaction transaction3 = manager.beginTransaction();
			// 隐藏全部fragment
			hideAllFragment(transaction3);
			if (discoverFragment == null) {
				discoverFragment = new EmployerDiscoverFragment();
				transaction3.add(R.id.fl_employer_container, discoverFragment);
			} else {
				transaction3.show(discoverFragment);
			}
			transaction3.commit();
			break;
		case 4:
			FragmentTransaction transaction4 = manager.beginTransaction();
			// 隐藏全部fragment
			hideAllFragment(transaction4);
			if (companyFragment == null) {
				companyFragment = new EmployerCompanyFragment();
				transaction4.add(R.id.fl_employer_container, companyFragment);
			} else {
				transaction4.show(companyFragment);
			}
			transaction4.commit();
			break;

		default:
			break;
		}
	}

	/**
	 * @Description:隐藏全部的fragment
	 * @Title:hideAllFragment
	 * @param transaction
	 * @return:void
	 * @throws
	 * @Create: 2016-8-22 下午1:57:03
	 * @Author : mengwei
	 */
	private void hideAllFragment(FragmentTransaction transaction) {
		if (homeFragment != null) {
			transaction.hide(homeFragment);
		}
		if (baoFragment != null) {
			transaction.hide(baoFragment);
		}
		if (discoverFragment != null) {
			transaction.hide(discoverFragment);
		}
		if (companyFragment != null) {
			transaction.hide(companyFragment);
		}

	}

	@Override
	public void changeTab(int tabIndex) {
		switch (tabIndex) {
		case HOME_TAB:
			rgMain.check(R.id.rb_employer_home);
			break;
		case ORDER_TAB:
			rgMain.check(R.id.rb_employer_order);
			break;
		case DISCOVER_TAB:
			rgMain.check(R.id.rb_employer_discover);
			break;
		case MINE_TAB:
			rgMain.check(R.id.rb_employer_mine);
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
			EmployerMainActivity.this.finish();
			CamaridoApp.destoryActivity("EmployerCompanyRenZhengActivity");
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
		mPreManager = PreManager.instance(EmployerMainActivity.this);
		int msgCount = mPreManager.getMsgBubble();
		int attentionCount = mPreManager.getAttentionBubble();
		int activeCount = mPreManager.getActiveBubble();

		L.e("=========mPreManager.getShowRed()==========="+mPreManager.getShowRed());

		if (mPreManager.getShowRed()){
			ivRedZixun.setVisibility(View.VISIBLE);
		} else {
			ivRedZixun.setVisibility(View.GONE);
		}

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
		intent.setClass(activity, EmployerMainActivity.class);
		activity.startActivity(intent);
	}

	/**
	 * 
	 * @Description:新的跳转方法
	 * @Title:newInvoke
	 * @param activity
	 * @param from
	 * @return:void
	 * @throws
	 * @Create: 2016年7月7日 下午1:54:01
	 * @Author : chengtao
	 */
	public static void newInvoke(Context activity, String from) {
		Intent intent = new Intent();
		intent.setClass(activity, EmployerMainActivity.class);
		intent.putExtra(FROM, from);
		activity.startActivity(intent);
	}

}
