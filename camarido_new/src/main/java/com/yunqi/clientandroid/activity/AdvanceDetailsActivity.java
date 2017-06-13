package com.yunqi.clientandroid.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.utils.CacheUtils;
import com.yunqi.clientandroid.utils.L;

/**
 * 
 * @Description:class 提现详情页面
 * @ClassName: AdvanceDetailsActivity
 * @author: zhm
 * @date: 2016-4-14 上午9:00:00
 * 
 */
public class AdvanceDetailsActivity extends BaseActivity implements
		OnClickListener {
	private TextView tv_ad_rl_money;
	private TextView tv_ad_rl_cardType;
	private Button bt_ad_complete;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_advance_details;
	}

	@Override
	protected void initView() {
		initActionBar();

		tv_ad_rl_money = (TextView) findViewById(R.id.tv_ad_rl_money);
		tv_ad_rl_cardType = (TextView) findViewById(R.id.tv_ad_rl_cardType);
		bt_ad_complete = (Button) findViewById(R.id.bt_ad_complete);
	}

	// 初始化titileBar的方法
	private void initActionBar() {
		// 设置titileBar的标题
		setActionBarTitle(this.getResources().getString(
				R.string.my_safety_center));
		// 设置左边的返回箭头
		setActionBarLeft(R.drawable.nav_back);
		// 给左边的返回箭头加监听
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 关闭当前的Activity页面
				// AdvanceDetailsActivity.this.finish();
				MyWalletActivity
						.invoke(AdvanceDetailsActivity.this, null, null);
			}
		});

	}

	@Override
	protected void initData() {
		L.e("TAG", "fuzhile++++++++++++++++++++++++++++");

		// 获取穿过来的值
		String money = getIntent().getStringExtra("money");
		String cardNo = getIntent().getStringExtra("cardNo");
		String cardType = String
				.valueOf(getIntent().getIntExtra("cardType", 0));

		L.e("TAG", "huodezhi++++++" + money + "-------" + cardNo
				+ "---------" + cardType);

		// 获取银行卡号的后四位
		String str = cardNo.substring(15, 19).trim();

		L.e("TAG", "银行卡号：============================" + cardNo + "储蓄卡("
				+ str + ")");

		String sYIngHang = CacheUtils.getString(AdvanceDetailsActivity.this,
				"YINHANG", "");

		// 进行赋值
		tv_ad_rl_money.setText(money + "元");
		tv_ad_rl_cardType.setText(sYIngHang + "储蓄卡(" + str + ")");
	}

	@Override
	protected void setListener() {
		bt_ad_complete.setOnClickListener(this);
	}

	// 本界面的跳转方法
	public static void invoke(Activity activity, String money, String cardNo,
			int cardType) {
		Intent intent = new Intent(activity, AdvanceDetailsActivity.class);
		intent.putExtra("money", money);
		intent.putExtra("cardNo", cardNo);
		intent.putExtra("cardType", cardType);
		activity.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_ad_complete: // 完成
			MyWalletActivity.invoke(AdvanceDetailsActivity.this, null, null);
			break;

		default:
			break;
		}

	}

}
