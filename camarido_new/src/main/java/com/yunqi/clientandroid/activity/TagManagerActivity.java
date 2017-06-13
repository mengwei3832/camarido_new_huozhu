package com.yunqi.clientandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.fragment.OrderSimpleFoucusListFragment;
import com.yunqi.clientandroid.fragment.TagManagerFragment;

/**
 * @author zhangwb
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 标签管理界面
 * @date 15/11/19
 */
public class TagManagerActivity extends BaseActivity implements
		RadioGroup.OnCheckedChangeListener {
	private RadioGroup rgTag;
	private ImageView ivPath;
	private ImageView ivManager;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_tag_manager;
	}

	@Override
	protected void initView() {
		// 初始化titileBar
		initActionBar();

		rgTag = obtainView(R.id.rg_tag);
		ivPath = obtainView(R.id.iv_path);
		ivManager = obtainView(R.id.iv_manager);

	}

	@Override
	protected void initData() {
		rgTag.check(R.id.rb_path);

	}

	@Override
	protected void setListener() {
		rgTag.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_path:
			// 当前选中的是路线
			ivPath.setVisibility(View.VISIBLE);
			ivManager.setVisibility(View.INVISIBLE);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fl_tag, new OrderSimpleFoucusListFragment())
					.commit();
			break;
		case R.id.rb_manager:
			ivPath.setVisibility(View.INVISIBLE);
			ivManager.setVisibility(View.VISIBLE);
			// 当前选中的是管理
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fl_tag, new TagManagerFragment()).commit();
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化titileBar
	 */
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(
				R.string.attention_manager));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TagManagerActivity.this.finish();
			}
		});
		setActionBarRight(false, 0, "");
		setOnActionBarRightClickListener(false, null);
	}

	/**
	 * 本界面跳转方法
	 */
	public static void invoke(Context context) {
		Intent intent = new Intent(context, TagManagerActivity.class);
		context.startActivity(intent);
	}
}
