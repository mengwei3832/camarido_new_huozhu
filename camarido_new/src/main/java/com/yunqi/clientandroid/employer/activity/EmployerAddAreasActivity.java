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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
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
 * @Description:class 添加区的界面
 * @ClassName: EmployerAddProvinceActivity
 * @author: chengtao
 * @date: 2016-6-7 下午7:38:02
 * 
 */
public class EmployerAddAreasActivity extends BaseActivity implements
		OnItemClickListener {
	private ListView lvAddAreas;
	private View pbAddProgress;
	private List<GetProvince> mAreasList = new ArrayList<>();
	private int provinceId, cityId;
	private String provinceName, cityName;
	private FilterManager filterManager;
	private EmployerAddressAdapter mEmployerAddressAdapter;
	private SpManager mSpManager;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_add_province;
	}

	@Override
	protected void initView() {
		initActionBar();

		// 获取传递过来的数据
		provinceId = getIntent().getIntExtra("provinceId",0);
		cityId = getIntent().getIntExtra("cityId",0);
		provinceName = getIntent().getStringExtra("provinceName");
		cityName = getIntent().getStringExtra("cityName");

		filterManager = FilterManager.instance(this);
		mSpManager = SpManager.instance(mContext);

		lvAddAreas = obtainView(R.id.lv_add_province);
		pbAddProgress = obtainView(R.id.pb_add_progress);

//		String districtStr = mSpManager.getDistrict(String.valueOf(cityId));

		mAreasList = SaveProvinceUtils.readObjectList(cityId+"");

		L.e("-----mAreasList-----"+mAreasList.toString());

		if (mAreasList.size() != 0){
//			try {
//				mAreasList = SaveSetUtils.String2SceneList(districtStr);
				mEmployerAddressAdapter = new EmployerAddressAdapter(mContext,
						mAreasList);
				lvAddAreas.setAdapter(mEmployerAddressAdapter);
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			}
		} else {
			mAreasList = AddressList.getCountryList(cityId);
			if (mAreasList.size() != 0){
				mEmployerAddressAdapter = new EmployerAddressAdapter(mContext,
						mAreasList);
				lvAddAreas.setAdapter(mEmployerAddressAdapter);
			} else {
				ShipAddressActivity.invokeNew(EmployerAddAreasActivity.this);
			}
//			pbAddProgress.setVisibility(View.VISIBLE);
//			mAreasList.clear();
//			mEmployerAddressAdapter.notifyDataSetChanged();
//			httpPost(new GetCityRequest(EmployerAddAreasActivity.this, cityId));
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
				EmployerAddAreasActivity.this.finish();
			}
		});
	}

	@Override
	protected void initData() {


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
			ArrayList<GetProvince> areasList = response.data;

			if (areasList != null) {
				mAreasList.addAll(areasList);
				try {
					String str = SaveSetUtils.SceneList2String(mAreasList);
//					mSpManager.setDistrict(cityId,str);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			Log.e("TAG", "---mAreasList----" + mAreasList.toString());

			// mEmployerAddressAdapter = new EmployerAddressAdapter(mContext,
			// mAreasList);
			// lvAddAreas.setAdapter(mEmployerAddressAdapter);

			showToast(message);

			mEmployerAddressAdapter.notifyDataSetChanged();

			pbAddProgress.setVisibility(View.INVISIBLE);
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
		lvAddAreas.setOnItemClickListener(this);
	}

	/**
	 * 本界面invoke跳转方法
	 */
	public static void invoke(Context context, int provinceId,
		int cityId, String provinceName, String cityName) {
		Intent intent = new Intent(context, EmployerAddAreasActivity.class);
		intent.putExtra("provinceId", provinceId);
		intent.putExtra("cityId", cityId);
		intent.putExtra("provinceName", provinceName);
		intent.putExtra("cityName", cityName);
		context.startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		GetProvince mGetProvince = mAreasList.get(position);

		int areasId = mGetProvince.id;

		String areasName = mGetProvince.name;

		Log.e("TAG", "----------选中的区或县------------" + areasName);
		Log.e("TAG", "----------选中的区或县ID------------" + areasId);
		filterManager.setProvince(provinceName);
		filterManager.setProvinceId(provinceId);
		filterManager.setCity(cityName);
		filterManager.setCityId(cityId);
		filterManager.setAreas(areasName);
		filterManager.setAreasId(areasId);
		filterManager.setEtEmpty(true);

		ShipAddressActivity.invokeNew(EmployerAddAreasActivity.this);
	}

}
