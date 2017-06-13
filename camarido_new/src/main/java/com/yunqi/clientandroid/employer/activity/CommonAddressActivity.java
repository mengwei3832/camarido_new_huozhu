package com.yunqi.clientandroid.employer.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.adapter.CommonAddressAdapter;
import com.yunqi.clientandroid.employer.request.GetAddressRequest;
import com.yunqi.clientandroid.entity.ChooseAddressItem;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.FilterManager;

/**
 * @Description:常用地址列表
 * @ClassName: CommonAddressActivity
 * @author: chengtao
 * @date: 2016-6-23 下午4:23:08
 * 
 */
public class CommonAddressActivity extends BaseActivity implements
		PullToRefreshBase.OnRefreshListener2<ListView>, OnItemClickListener {
	private PullToRefreshListView lvCommonView;
	private View pbCommonBar;
	private ImageView ivCommonBlank;
	/* 分页请求参数 */
	private int pageIndex = 1;// 起始页
	private int pageSize = 10;// 每页显示数量
	private int count;// 实际返回的数据数量
	private boolean isEnd = false;// 是否服务器无数据返回
	private Handler handler = new Handler();
	private FilterManager filterManager;

	// 保存地址的集合
	private ArrayList<ChooseAddressItem> commonList = new ArrayList<ChooseAddressItem>();
	private CommonAddressAdapter commonAddressAdapter;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_common_address;
	}

	@Override
	protected void initView() {
		filterManager = FilterManager.instance(mContext);

		initActionBar();

		pbCommonBar = obtainView(R.id.pb_common);
		ivCommonBlank = obtainView(R.id.iv_common_blank);
		lvCommonView = obtainView(R.id.lv_common_address);

		lvCommonView.setMode(PullToRefreshBase.Mode.BOTH);
		lvCommonView.setOnRefreshListener(this);

		commonAddressAdapter = new CommonAddressAdapter(commonList, mContext);
		lvCommonView.setAdapter(commonAddressAdapter);
	}

	/**
	 * 
	 * @Description:初始化ActionBar
	 * @Title:initActionBar
	 * @return:void
	 * @throws
	 * @Create: 2016年6月21日 下午2:37:45
	 * @Author : mengwei
	 */
	private void initActionBar() {
		setActionBarTitle("常用地址");
		setActionBarLeft(R.drawable.fanhui);
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CommonAddressActivity.this.finish();
			}
		});
	}

	@Override
	protected void initData() {
		// 获取地址内容
		getCommonAddress();
	}

	/**
	 * @Description:获取地址内容
	 * @Title:getCommonAddress
	 * @return:void
	 * @throws
	 * @Create: 2016-6-23 下午4:55:13
	 * @Author : mengwei
	 */
	private void getCommonAddress() {
		commonAddressAdapter.notifyDataSetChanged();
		pbCommonBar.setVisibility(View.VISIBLE);
		count = pageIndex * pageSize;
		httpPost(new GetAddressRequest(mContext, pageSize, pageIndex));
	}

	@Override
	public void onStart(int requestId) {
		super.onStart(requestId);
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess = response.isSuccess;
		String message = response.message;
		int totalCount = response.totalCount;
		if (isSuccess) {
			ArrayList<ChooseAddressItem> colis = response.data;

			if (colis != null) {
				commonList.addAll(colis);
			}

			if (colis.size() == 0) {
				showToast("暂无地址信息");
				ivCommonBlank.setVisibility(View.VISIBLE);
			} else {
				ivCommonBlank.setVisibility(View.GONE);
			}

			if (totalCount <= count) {
				isEnd = true;
			}

			commonAddressAdapter.notifyDataSetChanged();
			lvCommonView.onRefreshComplete();
			pbCommonBar.setVisibility(View.GONE);

		} else {
			showToast(message);
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(getString(R.string.net_error_toast));
		pbCommonBar.setVisibility(View.GONE);
		ivCommonBlank.setVisibility(View.VISIBLE);
	}

	@Override
	protected void setListener() {
		lvCommonView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		position--;
		ChooseAddressItem commonAddress = commonList.get(position);

		// 获取省市区ID、详细地址、别名
		int provinceId = commonAddress.ProvinceRegionId;
		int cityId = commonAddress.CityRegionId;
		int areaId = commonAddress.SubRegionId;
		String provinveName = commonAddress.Provicename;
		String cityName = commonAddress.Cityname;
		String areaName = commonAddress.Areaname;
		String detailAddress = commonAddress.Addressdetail;
		String companyName = commonAddress.AddressCustomName;

		filterManager.setProvince(provinveName);
		filterManager.setProvinceId(provinceId);
		filterManager.setCity(cityName);
		filterManager.setCityId(cityId);
		filterManager.setAreas(areaName);
		filterManager.setAreasId(areaId);
		filterManager.setAddressDetail(detailAddress);
		filterManager.setCompanyCustomName(companyName);

		Log.v("TAG", "provinceId--------" + provinceId);
		Log.v("TAG", "cityId--------" + cityId);
		Log.v("TAG", "areaId--------" + areaId);
		Log.v("TAG", "provinveName--------" + provinveName);
		Log.v("TAG", "cityName--------" + cityName);
		Log.v("TAG", "areaName--------" + areaName);
		Log.v("TAG", "detailAddress--------" + detailAddress);
		Log.v("TAG", "companyName--------" + companyName);

		CommonAddressActivity.this.finish();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// 清空存放当前订单列表的集合
		commonList.clear();
		commonAddressAdapter.notifyDataSetChanged();
		// 起始页置为1
		pageIndex = 1;
		// 请求服务器获取当前运单的数据列表
		getCommonAddress();

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isEnd) {
			++pageIndex;
			getCommonAddress();
		} else {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					lvCommonView.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
		}
	}

	/**
	 * 本界面invoke跳转方法
	 */
	public static void invoke(Activity activity) {
		Intent intent = new Intent(activity, CommonAddressActivity.class);
		activity.startActivity(intent);
	}

}
