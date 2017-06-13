package com.yunqi.clientandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.fragment.CurrentFragment;
import com.yunqi.clientandroid.fragment.PerformFragment;
import com.yunqi.clientandroid.fragment.SettlementFragment;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 我的订单列表
 * @date 15/11/28
 */
public class MyTicketActivity extends BaseActivity implements
		RadioGroup.OnCheckedChangeListener {

	public RadioGroup mRadiogroup;
	public RadioButton mRbPerform;
	public RadioButton mRbCurrent;
	public RadioButton mRbSettlement;
	public PerformFragment mPerformFragment;
	public CurrentFragment mCurrentFragment;
	public SettlementFragment mSettlementFragment;
	private FragmentManager supportFragmentManager;
	private String flag;// 其他页面传过来的flag标记

	@Override
	protected int getLayoutId() {
		return R.layout.activity_myticket;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		mRadiogroup = (RadioGroup) findViewById(R.id.rg_myticket_radiogroup);
		mRbPerform = (RadioButton) findViewById(R.id.rb_myticket_perform);
		mRbCurrent = (RadioButton) findViewById(R.id.rb_myticket_current);
		mRbSettlement = (RadioButton) findViewById(R.id.rb_myticket_settlement);

	}

	@Override
	protected void initData() {
		mPerformFragment = new PerformFragment();
		mCurrentFragment = new CurrentFragment();
		mSettlementFragment = new SettlementFragment();

		// 接收从其他页面传过来的数据
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		if (bundle != null && bundle.containsKey("flag")) {
			flag = bundle.getString("flag");
		}

		supportFragmentManager = getSupportFragmentManager();
		if ("PERFORM".equals(flag)) {
			// 进入界面待执行运单默认选中颜色设置为蓝色
			mRbPerform.setChecked(true);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fl_myticket_container, mPerformFragment)
					.commit();
		} else if ("CURRENT".equals(flag)) {
			// 进入界面当前运单默认选中颜色设置为蓝色
			mRbCurrent.setChecked(true);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fl_myticket_container, mCurrentFragment)
					.commit();
		} else if ("COMPLETED".equals(flag)) {
			// 进入界面待执行运单默认选中颜色设置为蓝色
			mRbSettlement.setChecked(true);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fl_myticket_container, mSettlementFragment)
					.commit();
		}

	}

	@Override
	protected void setListener() {
		// 初始化radio选择
		initOnChecked();
	}

	/**
	 * 初始化titileBar
	 */
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(
				R.string.tv_leftmenu_order));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyTicketActivity.this.finish();
			}
		});

		setActionBarRight(true, 0,
				this.getResources().getString(R.string.myticket_history));
		setOnActionBarRightClickListener(false, new OnClickListener() {
			@Override
			public void onClick(View V) {
				// 跳转到订单历史列表界面
				HistoryTicketActivity.invoke(MyTicketActivity.this);
			}
		});
	}

	// 初始化点击事件
	private void initOnChecked() {
		mRadiogroup.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		FragmentTransaction beginTransaction = supportFragmentManager
				.beginTransaction();
		switch (checkedId) {
		case R.id.rb_myticket_perform:
			// 待执行订单
			beginTransaction.replace(R.id.fl_myticket_container,
					mPerformFragment).commit();
			break;

		case R.id.rb_myticket_current:
			// 当前订单
			beginTransaction.replace(R.id.fl_myticket_container,
					mCurrentFragment).commit();
			break;
		case R.id.rb_myticket_settlement:
			// 已结算订单
			beginTransaction.replace(R.id.fl_myticket_container,
					mSettlementFragment).commit();
			break;

		default:
			break;
		}

	}

	// 本界面的跳转方法
	public static void invoke(Context activity, String myTicketFlag) {
		Intent intent = new Intent(activity, MyTicketActivity.class);
		intent.putExtra("flag", myTicketFlag);
		activity.startActivity(intent);
	}

}
