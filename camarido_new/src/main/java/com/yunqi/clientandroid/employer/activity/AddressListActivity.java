package com.yunqi.clientandroid.employer.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.adapter.EmployerAddressListAdapter;
import com.yunqi.clientandroid.employer.entity.EmployerAddressInfo;
import com.yunqi.clientandroid.employer.request.GetEmployerAddressRequest;
import com.yunqi.clientandroid.http.response.Response;

/**
 * 
 * @Description:地址管理界面
 * @ClassName: AddressListActivity
 * @author: chengtao
 * @date: 2016年6月16日 下午1:28:00
 * 
 */
public class AddressListActivity extends BaseActivity implements
		OnRefreshListener2<ListView>, OnItemClickListener {
	private PullToRefreshListView lvAddress;
	private List<EmployerAddressInfo> list;
	private EmployerAddressListAdapter adapter;

	private int mPageIndex = 1;
	private final int PAGE_COUNT = 10;
	private boolean isloadingFinish = false;

	// 页面请求
	private GetEmployerAddressRequest getEmployerAddressRequest;
	// 请求ID
	private final static int GET_EMPLOYER_ADDRESS_REQUEST = 1;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_address_list;
	}

	@Override
	protected void initView() {
		lvAddress = obtainView(R.id.lv_address);
		list = new ArrayList<EmployerAddressInfo>();
		adapter = new EmployerAddressListAdapter(mContext, list);
		lvAddress.setAdapter(adapter);
		intiActionBar();
	}

	/**
	 * 
	 * @Description:初始化ActionBar
	 * @Title:intiActionBar
	 * @return:void
	 * @throws
	 * @Create: 2016年6月15日 下午5:08:25
	 * @Author : chengtao
	 */
	private void intiActionBar() {
		setActionBarTitle("地址管理");
		setActionBarLeft(R.drawable.fanhui);
		setOnActionBarLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		setActionBarRight(true, 0, "添加地址");
		setOnActionBarRightClickListener(false, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 跳转添加地址界面
//				AddAddressActiviy.invoke(mContext, "", "", "");
			}
		});
	}

	@Override
	protected void initData() {
		getAddressList();
	}

	private void getAddressList() {
		list.clear();
		adapter.notifyDataSetChanged();
		getEmployerAddressRequest = new GetEmployerAddressRequest(mContext,
				mPageIndex, PAGE_COUNT);
		getEmployerAddressRequest.setRequestId(GET_EMPLOYER_ADDRESS_REQUEST);
		httpPostJson(getEmployerAddressRequest);
	}

	@Override
	protected void setListener() {
		lvAddress.setOnRefreshListener(this);
		lvAddress.setOnItemClickListener(this);
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess = response.isSuccess;
		String message = response.message;
		switch (requestId) {
		case GET_EMPLOYER_ADDRESS_REQUEST:
			if (isSuccess) {
				int totalCount = response.totalCount;
				if (totalCount <= list.size()) {
					isloadingFinish = true;
				}
				ArrayList<EmployerAddressInfo> infos = response.data;
				if (infos != null) {
					list.addAll(infos);
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

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast("连接超时,请检查网络");
	}

	@Override
	protected void onResume() {
		super.onResume();
		getAddressList();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		mPageIndex = 1;
		getAddressList();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isloadingFinish) {
			mPageIndex++;
			getAddressList();
		} else {
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					lvAddress.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		EmployerAddressInfo info = list.get(position);
		String Longitude = null, Latitude = null, pcaName = null, addressName = null, companyName = null;
		if (info != null) {
			Longitude = info.Longitude;
			Latitude = info.Latitude;
			pcaName = info.provicename + info.cityname + info.areaname;
			companyName = info.TenantShortName;
			addressName = info.Addressdetail;
		}
		// 跳转地址预览界面
//		SeeAddressActiviy
//				.invoke(mContext, Double.parseDouble(Longitude),
//						Double.parseDouble(Latitude), pcaName, addressName,
//						companyName);
	}

	/**
	 * 
	 * @Description:地址管理界面跳转
	 * @Title:invoke
	 * @param activity
	 * @return:void
	 * @throws
	 * @Create: 2016年6月17日 下午3:30:59
	 * @Author : chengtao
	 */
	public static void invoke(Activity activity) {
		Intent intent = new Intent(activity, AddressListActivity.class);
		activity.startActivity(intent);
	}
}
