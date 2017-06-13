package com.yunqi.clientandroid.employer.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.adapter.EmployerAddressAdapter;
import com.yunqi.clientandroid.employer.entity.GetProvince;
import com.yunqi.clientandroid.employer.request.GetCityRequest;
import com.yunqi.clientandroid.employer.util.AddressList;
import com.yunqi.clientandroid.employer.util.SaveProvinceUtils;
import com.yunqi.clientandroid.employer.util.SaveSetUtils;
import com.yunqi.clientandroid.employer.util.SpManager;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.FilterManager;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @Description:class 添加市的界面
 * @ClassName: EmployerAddProvinceActivity
 * @author: mengwei
 * @date: 2016-6-7 下午7:38:02
 * 
 */
public class EmployerAddCityActivity extends BaseActivity implements
		OnItemClickListener {
	private ListView lvAddCity;
	private View pbAddProgress;
	private List<GetProvince> mCityList = new ArrayList<>();
	private int provinceId;
	private String provinceName;
	private EmployerAddressAdapter mEmployerAddressAdapter;
	private SpManager mSpManager;
	private FilterManager mFilterManager;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_add_province;
	}

	@Override
	protected void initView() {
		initActionBar();

		mSpManager = SpManager.instance(mContext);
		mFilterManager = FilterManager.instance(mContext);

		// 获取传递过来的数据
		provinceId = getIntent().getIntExtra("provinceId",0);
		provinceName = getIntent().getStringExtra("provinceName");

		lvAddCity = obtainView(R.id.lv_add_province);
		pbAddProgress = obtainView(R.id.pb_add_progress);

//		String cityStr = mSpManager.getCity(String.valueOf(provinceId));

		mCityList = SaveProvinceUtils.readObjectList(provinceId+"");

		L.e("-----mCityList-----"+mCityList.toString());

		if (mCityList.size() != 0){
//			try {
//				mCityList = SaveSetUtils.String2SceneList(cityStr);
				mEmployerAddressAdapter = new EmployerAddressAdapter(
						EmployerAddCityActivity.this, mCityList);
				lvAddCity.setAdapter(mEmployerAddressAdapter);
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			}
		} else {
			mCityList = AddressList.getCityList(provinceId);
			if (mCityList.size() == 0){
				mFilterManager.setProvince(provinceName);
				mFilterManager.setProvinceId(provinceId);
				mFilterManager.setEtEmpty(true);

				ShipAddressActivity.invokeNew(EmployerAddCityActivity.this);
			} else {
				mEmployerAddressAdapter = new EmployerAddressAdapter(
						EmployerAddCityActivity.this, mCityList);
				lvAddCity.setAdapter(mEmployerAddressAdapter);
			}
//			pbAddProgress.setVisibility(View.VISIBLE);

//			mCityList.clear();
//			mEmployerAddressAdapter.notifyDataSetChanged();
//
//			// 市的请求
//			httpPost(new GetCityRequest(EmployerAddCityActivity.this, provinceId));
		}
	}

	// 初始化titileBar的方法
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(
				R.string.employer_address_tianjiadizhi));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EmployerAddCityActivity.this.finish();
			}
		});
	}

	@Override
	protected void initData() {

		// mEmployerAddressAdapter = new
		// EmployerAddressAdapter(EmployerAddCityActivity.this,
		// mCityList);
		// lvAddCity.setAdapter(mEmployerAddressAdapter);
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
		if (isSuccess) {
			ArrayList<GetProvince> cityList = response.data;

			if (cityList != null) {
				mCityList.addAll(cityList);
				try {
					String str = SaveSetUtils.SceneList2String(mCityList);
//					mSpManager.setCity(provinceId,str);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			showToast(message);

			pbAddProgress.setVisibility(View.INVISIBLE);

			mEmployerAddressAdapter.notifyDataSetChanged();
		} else {
			showToast(message);
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(getString(R.string.net_error_toast));
	}

	@Override
	protected void setListener() {
		lvAddCity.setOnItemClickListener(this);

	}

	/**
	 * 本界面invoke跳转方法
	 */
	public static void invoke(Context context, int provinceId,
			String provinceName) {
		Intent intent = new Intent(context, EmployerAddCityActivity.class);
		intent.putExtra("provinceId", provinceId);
		intent.putExtra("provinceName", provinceName);
		context.startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		GetProvince mGetProvince = mCityList.get(position);

		int cityId = mGetProvince.id;

		String cityName = mGetProvince.name;

		Log.e("TAG", "----------选中的市------------" + cityName);
		Log.e("TAG", "----------选中的市ID------------" + cityId);
		Log.e("TAG", "----------选中的shengID------------" + provinceId);

		// 跳转到区的页面
		EmployerAddAreasActivity.invoke(EmployerAddCityActivity.this,
				provinceId, cityId, provinceName, cityName);

	}

}
