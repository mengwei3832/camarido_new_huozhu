package com.yunqi.clientandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.fragment.VehicleBasicmsgFragment;
import com.yunqi.clientandroid.fragment.VehicleDriverListFragment;

/**
 * @deprecated:车辆列表的item详情
 */
public class VehicleListDetailActivity extends BaseActivity implements
		RadioGroup.OnCheckedChangeListener {

	public String vehicleId;// 车辆ID
	private String vehicleNo;// 车牌号码
	private RadioGroup mDetailRg;
	private RadioButton mBasic;
	private RadioButton mDrivermsg;
	private VehicleBasicmsgFragment carBasicmsgFragment;
	private VehicleDriverListFragment carDrivermsgFragment;
	private FragmentManager fragmentManager;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_carlist_detail;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionbar();

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		// 获取从车辆列表传过来的车辆ID
		if (bundle != null && bundle.containsKey("vehicleId")) {
			vehicleId = bundle.getString("vehicleId");
		}
		if (bundle != null && bundle.containsKey("vehicleNo")) {
			vehicleNo = bundle.getString("vehicleNo");
		}
		mDetailRg = (RadioGroup) findViewById(R.id.carlist_detail_rg);
		mBasic = (RadioButton) findViewById(R.id.rb_carlist_basic);
		mDrivermsg = (RadioButton) findViewById(R.id.rb_carlist_drivermsg);

		// 进入界面基本信息默认选中颜色设置为蓝色
		mBasic.setChecked(true);
	}

	@Override
	protected void initData() {
		carBasicmsgFragment = new VehicleBasicmsgFragment();
		carDrivermsgFragment = new VehicleDriverListFragment();
		fragmentManager = getSupportFragmentManager();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fl_carlist_container, carBasicmsgFragment)
				.commit();
	}

	// 初始化titileBar的方法
	private void initActionbar() {
		setActionBarTitle(this.getResources().getString(R.string.vehicle_msg));
		setActionBarLeft(R.drawable.nav_back);
		// setActionBarRight(true, 0, "邀请码");
		setOnActionBarLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				VehicleListDetailActivity.this.finish();
			}
		});

		// setOnActionBarRightClickListener(false, new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // 跳转到发送邀请码界面
		// SendCodeActivity.invoke(VehicleListDetailActivity.this,
		// vehicleId);
		// }
		// });

	}

	@Override
	protected void setListener() {
		// 初始化radioGroup选择
		// initOnChecked();
	}

	// 初始化radioGroup点击事件
	private void initOnChecked() {
		mDetailRg.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		FragmentTransaction beginTransaction = fragmentManager
				.beginTransaction();
		switch (checkedId) {
		case R.id.rb_carlist_basic:
			beginTransaction.replace(R.id.fl_carlist_container,
					carBasicmsgFragment).commit();

			break;

		case R.id.rb_carlist_drivermsg:
			beginTransaction.replace(R.id.fl_carlist_container,
					carDrivermsgFragment).commit();
			break;

		default:
			break;
		}
	}

	// 车辆ID
	public String getId() {
		return vehicleId;
	}

	public void setId(String Id) {
		this.vehicleId = Id;
	}

	// 车牌号
	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	// 本界面的跳转方法
	public static void invoke(Context activity, String vehicleId,
			String vehicleNo) {
		Intent intent = new Intent();
		intent.setClass(activity, VehicleListDetailActivity.class);
		intent.putExtra("vehicleId", vehicleId);
		intent.putExtra("vehicleNo", vehicleNo);
		activity.startActivity(intent);
	}

}
