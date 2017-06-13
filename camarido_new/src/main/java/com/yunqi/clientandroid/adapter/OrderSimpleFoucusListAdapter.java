package com.yunqi.clientandroid.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.FocusPath;
import com.yunqi.clientandroid.entity.PackagePath;
import com.yunqi.clientandroid.fragment.OrderSimpleFoucusListFragment;

import java.util.List;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 标签管理关注Adapter
 * @date 15/11/28
 */
public class OrderSimpleFoucusListAdapter extends ArrayAdapter<FocusPath> {

	private Context mContext;
	private List<FocusPath> mList;
	private OrderSimpleFoucusListFragment mOrderSimpleFoucusListFragment;

	public OrderSimpleFoucusListAdapter(Context context, List<FocusPath> list,
			OrderSimpleFoucusListFragment orderSimpleFoucusListFragment) {
		super(context, 0, list);
		this.mContext = context;
		this.mList = list;
		this.mOrderSimpleFoucusListFragment = orderSimpleFoucusListFragment;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final FocusPath packagePath = mList.get(position);
		String beginProvinceText = packagePath.beginProvinceText;
		String endProvinceText = packagePath.endProvinceText;
		String beginCityText = packagePath.beginCityText;
		String endCityText = packagePath.endCityText;

		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_simaple_foucus, null);

			viewHolder.tvStartProvince = (TextView) convertView
					.findViewById(R.id.tv_simaple_foucus_start_province);
			viewHolder.tvEndProvince = (TextView) convertView
					.findViewById(R.id.tv_simaple_foucus_end_province);
			viewHolder.tvStartCity = (TextView) convertView
					.findViewById(R.id.tv_simaple_foucus_start_city);
			viewHolder.tvEndCity = (TextView) convertView
					.findViewById(R.id.tv_simaple_foucus_end_city);
			viewHolder.ibFocuson = (ImageView) convertView
					.findViewById(R.id.ib_simaple_foucus_packagelist_focuson);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (!TextUtils.isEmpty(beginProvinceText) && beginProvinceText != null) {
			viewHolder.tvStartProvince.setText(beginProvinceText);
		}

		if (!TextUtils.isEmpty(endProvinceText) && endProvinceText != null) {
			viewHolder.tvEndProvince.setText(endProvinceText);
		}

		if (!TextUtils.isEmpty(beginCityText) && beginCityText != null) {
			viewHolder.tvStartCity.setText(beginCityText);
		}

		if (!TextUtils.isEmpty(endCityText) && endCityText != null) {
			viewHolder.tvEndCity.setText(endCityText);
		}

		viewHolder.ibFocuson.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mOrderSimpleFoucusListFragment.cancelFocuson(packagePath);
			}
		});
		return convertView;
	}

	class ViewHolder {

		private TextView tvStartProvince;
		private TextView tvEndProvince;
		private TextView tvStartCity;
		private TextView tvEndCity;
		private ImageView ibFocuson;
	}

	/**
	 * 点击关注
	 */
	public interface IFocusonClick {
		/**
		 * 取消关注
		 */
		void cancelFocuson(FocusPath packagePath);
	}

}
