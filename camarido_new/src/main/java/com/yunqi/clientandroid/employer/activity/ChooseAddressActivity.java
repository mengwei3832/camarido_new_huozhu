package com.yunqi.clientandroid.employer.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.adapter.ChooseAddressAdapter;
import com.yunqi.clientandroid.employer.adapter.ChooseAddressAdapter.onAddressCheckedListener;
import com.yunqi.clientandroid.employer.request.GetAddressRequest;
import com.yunqi.clientandroid.entity.ChooseAddressItem;
import com.yunqi.clientandroid.http.response.Response;

public class ChooseAddressActivity extends BaseActivity implements
		OnClickListener, PullToRefreshBase.OnRefreshListener2<ListView>,
		onAddressCheckedListener {
	private Button btnAddAddress;
	private PullToRefreshListView lvAddress;
	private List<ChooseAddressItem> list;
	private ChooseAddressAdapter adapter;
	public static int requestCode;

	private final int PAGE_COUNT = 10;
	private int mPageIndex = 1;
	private boolean isLoadingFinish = false;

	// 页面各种请求id
	private final static int GET_ADDRESS_LIST_REQUEST = 1;
	// 要传递的参数的key的名字
	public static String ADDRESS = "ADDRESS";
	public static String ADDRESS_ID = "ADDRESS_ID";
	public static String ADDRESS_CUSTOM_NAME = "ADDRESS_CUSTOM_NAME";

	@Override
	protected int getLayoutId() {
		return R.layout.activity_choose_address;
	}

	@Override
	protected void initView() {
		initActionBar();
		btnAddAddress = obtainView(R.id.btn_add_address);
		lvAddress = obtainView(R.id.lv_address);
		lvAddress.setMode(Mode.BOTH);
		list = new ArrayList<ChooseAddressItem>();
		adapter = new ChooseAddressAdapter(this, list, lvAddress, this);
		lvAddress.setAdapter(adapter);
	}

	/**
	 * 
	 * @Description:初始化ActionBar
	 * @Title:initActionBar
	 * @return:void
	 * @throws
	 * @Create: 2016年5月17日 下午8:33:23
	 * @Author : chengtao
	 */
	private void initActionBar() {
		setActionBarTitle("地址选择");
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, SendPackageActivity.class);
				intent.putExtra("address", adapter.getAddress());
				intent.putExtra("addressId", adapter.getAddressId());
				setResult(requestCode, intent);
				finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(mContext, SendPackageActivity.class);
			intent.putExtra("address", adapter.getAddress());
			intent.putExtra("addressId", adapter.getAddressId());
			setResult(requestCode, intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void initData() {
		getAddressList(PAGE_COUNT, mPageIndex);
	}

	/**
	 * 
	 * @Description:获取地址列表
	 * @Title:getAddressList
	 * @return:void
	 * @throws
	 * @Create: 2016年5月19日 下午4:28:22
	 * @Author : chengtao
	 */
	private void getAddressList(int pageSize, int pageIndex) {
		list.clear();
		adapter.notifyDataSetChanged();
		GetAddressRequest getAddressRequest = new GetAddressRequest(mContext,
				pageSize, pageIndex);
		getAddressRequest.setRequestId(GET_ADDRESS_LIST_REQUEST);
		httpPostJson(getAddressRequest);
	}

	@Override
	protected void setListener() {
		btnAddAddress.setOnClickListener(this);
		lvAddress.setOnRefreshListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add_address:
//			AddAddressActiviy.invoke(ChooseAddressActivity.this, "", "", "");
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * @Description:ChooseAddressActivity页面跳转
	 * @Title:invoke
//	 * @param context
//	 * @param requestCode
	 * @return:void
	 * @throws
	 * @Create: 2016年5月17日 下午8:40:22
	 * @Author : chengtao
	 */
	public static void invoke(Activity activity, int mRequestCode) {
		Intent intent = new Intent(activity, ChooseAddressActivity.class);
		requestCode = mRequestCode;
		activity.startActivityForResult(intent, mRequestCode);
	}

	/**
	 * 
	 * @Description:下拉刷新
	 * @Title:onPullDownToRefresh
	 * @param refreshView
	 * @throws
	 * @Create: 2016年5月19日 下午4:27:02
	 * @Author : zhm
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		mPageIndex = 1;
		getAddressList(PAGE_COUNT, mPageIndex);
	}

	/**
	 * 
	 * @Description:上拉加载
	 * @Title:onPullUpToRefresh
	 * @param refreshView
	 * @throws
	 * @Create: 2016年5月19日 下午4:27:07
	 * @Author : zhm
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		mPageIndex++;
		if (isLoadingFinish) {
			getAddressList(PAGE_COUNT, mPageIndex);
		} else {
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					lvAddress.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 200);
		}
	}

	/**
	 * 
	 * @Description:请求失败
	 * @Title:onFailure
	 * @param requestId
	 * @param httpCode
	 * @param error
	 * @throws
	 * @Create: 2016年5月19日 下午4:24:50
	 * @Author : zhm
	 */
	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
	}

	/**
	 * 
	 * @Description:请求成功
	 * @Title:onSuccess
	 * @param requestId
	 * @param response
	 * @throws
	 * @Create: 2016年5月19日 下午4:25:02
	 * @Author : zhm
	 */
	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess = response.isSuccess;
		String message = response.message;
		isLoadingFinish = false;
		switch (requestId) {
		case GET_ADDRESS_LIST_REQUEST:
			if (isSuccess) {
				list.clear();
				ArrayList<ChooseAddressItem> addressItems = response.data;
				if (addressItems != null) {
					list.addAll(addressItems);
				}
				if (response.totalCount < list.size()) {
					isLoadingFinish = true;
				}
				adapter.notifyDataSetChanged();
				lvAddress.onRefreshComplete();
			} else {
				showToast(message);
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * @Description:点击地址发生的事件
	 * @Title:onAddressChecked
	 * @param address
	 * @param addressId
	 * @param addressCustomName
	 * @throws
	 * @Create: 2016年6月22日 下午2:19:28
	 * @Author : chengtao
	 */
	@Override
	public void onAddressChecked(String address, int addressId,
			String addressCustomName) {
		Intent intent = new Intent(mContext, SendPackageActivity.class);
		intent.putExtra(ADDRESS, address);
		intent.putExtra(ADDRESS_ID, addressId);
		intent.putExtra(ADDRESS_CUSTOM_NAME, addressCustomName);
		setResult(requestCode, intent);
		finish();
	}
}
