package com.yunqi.clientandroid.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.fragment.AllRoutesListFragment;
import com.yunqi.clientandroid.fragment.FocusonListFragment;

/**
 * @deprecated:所有路线列表
 */
public class SelectRouteActivity extends BaseActivity implements
		RadioGroup.OnCheckedChangeListener {

	private RadioGroup rg_selectroute;
	private RadioButton mAttention;
	private RadioButton mRoutes;
	private AllRoutesListFragment allRoutesListFragment;
	private FocusonListFragment focusonListFragment;
	private FragmentManager fragmentManager;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_selectroute;
	}

	@Override
	protected void initView() {
		initActionbar();
		rg_selectroute = (RadioGroup) findViewById(R.id.selectroute_list_rg);
		mAttention = (RadioButton) findViewById(R.id.rb_selectroute_attention);
		mRoutes = (RadioButton) findViewById(R.id.rb_selectroute_routes);
	}

	@Override
	protected void initData() {
		allRoutesListFragment = new AllRoutesListFragment();
		focusonListFragment = new FocusonListFragment();

		fragmentManager = getSupportFragmentManager();

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fl_selectroute_container, focusonListFragment)
				.commit();
	}

	@Override
	protected void setListener() {
		intiOnChecked();
	}

	// 初始化点击事件
	private void intiOnChecked() {
		rg_selectroute.setOnCheckedChangeListener(this);
	}

	private void initActionbar() {
		setActionBarTitle("选择路线");
		setActionBarLeft(R.drawable.nav_back);
		setActionBarRight(false, 0, "");
		setOnActionBarLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SelectRouteActivity.this.finish();
			}
		});

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		FragmentTransaction beginTransaction = fragmentManager
				.beginTransaction();
		switch (checkedId) {
		case R.id.rb_selectroute_attention:
			// 我的关注路线
			beginTransaction.replace(R.id.fl_selectroute_container,
					focusonListFragment).commit();
			break;

		case R.id.rb_selectroute_routes:
			// 所有路线
			beginTransaction.replace(R.id.fl_selectroute_container,
					allRoutesListFragment).commit();
			break;

		default:
			break;
		}

	}

}
