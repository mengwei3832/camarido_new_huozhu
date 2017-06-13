package com.yunqi.clientandroid.employer.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.ChooseAddressItem;

public class ChooseAddressAdapter extends BaseAdapter implements
		OnItemClickListener {
	private LayoutInflater inflater;
	private List<ChooseAddressItem> list;
	private String address = "";
	private int addressId = 0;
	private String addressCustomName = "";
	private Map<String, Boolean> checkBoxstate;// 用于记录checkBox是否点击过
	private Map<String, Boolean> itemState;// 用于记录item是否点击过

	private onAddressCheckedListener listener;

	public ChooseAddressAdapter(Context context, List<ChooseAddressItem> list,
			PullToRefreshListView listView, onAddressCheckedListener listener) {
		super();
		this.list = list;
		inflater = LayoutInflater.from(context);
		this.listener = listener;
		checkBoxstate = new HashMap<String, Boolean>();
		itemState = new HashMap<String, Boolean>();
		listView.setOnItemClickListener(this);
	}

	class ViewHolder {
		TextView tvAddress;
		CheckBox cbAddress;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		ChooseAddressItem item = list.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_address, null);
			viewHolder = new ViewHolder();
			viewHolder.tvAddress = (TextView) convertView
					.findViewById(R.id.tv_address);
			viewHolder.cbAddress = (CheckBox) convertView
					.findViewById(R.id.cb_address);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (item != null) {
			String provicename = item.Provicename;
			String cityname = item.Cityname;
			String areaname = item.Areaname;
			String Addressdetail = item.Addressdetail;
			viewHolder.tvAddress.setText("");
			Log.v("TAG", "----provicename----" + provicename);
			if (provicename != null && !TextUtils.isEmpty(provicename)) {
				viewHolder.tvAddress.setText(viewHolder.tvAddress.getText()
						.toString() + provicename);
			}
			Log.v("TAG", "----cityname----" + cityname);
			if (cityname != null && !TextUtils.isEmpty(cityname)) {
				viewHolder.tvAddress.setText(viewHolder.tvAddress.getText()
						.toString() + cityname);
			}
			Log.v("TAG", "----areaname----" + areaname);
			if (areaname != null && !TextUtils.isEmpty(areaname)) {
				viewHolder.tvAddress.setText(viewHolder.tvAddress.getText()
						.toString() + areaname);
			}
			Log.v("TAG", "----Addressdetail----" + Addressdetail);
			if (Addressdetail != null && !TextUtils.isEmpty(Addressdetail)) {
				viewHolder.tvAddress.setText(viewHolder.tvAddress.getText()
						.toString() + Addressdetail);
			}
		} else {
			viewHolder.tvAddress.setText("");
		}
		// 向itemState添加数据
		if (itemState.get(String.valueOf(position)) == null) {
			itemState.put(String.valueOf(position), false);
		}
		Log.v("TAG", position + "");
		// 向checkBoxstate添加数据 并 判断判断checkBox是否选中
		if (checkBoxstate.get(String.valueOf(position)) == null
				|| checkBoxstate.get(String.valueOf(position)) == false) {
			checkBoxstate.put(String.valueOf(position), false);
			viewHolder.cbAddress.setChecked(false);
		} else {
			viewHolder.cbAddress.setChecked(true);
		}
		return convertView;
	}

	/**
	 * 
	 * @Description:获取地址信息接口
	 * @ClassName: onAddressCheckedLister
	 * @author: chengtao
	 * @date: 2016年6月22日 下午2:13:42
	 * 
	 */
	public interface onAddressCheckedListener {
		void onAddressChecked(String address, int addressId,
				String addressCustomName);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.v("TAG", "onItemClick--------" + position + "");
		position--;
		TextView tvAddress = (TextView) view.findViewById(R.id.tv_address);

		// 重置checkBoxstate的所有value
		for (String key : checkBoxstate.keySet()) {
			checkBoxstate.put(key, false);
		}

		if (itemState.get(String.valueOf(position))) {// 如果item点击过
			checkBoxstate.put(String.valueOf(position), false);
			itemState.put(String.valueOf(position), false);
			address = "";
			addressId = 0;
			addressCustomName = "";
		} else {// 如果item未点击过
			checkBoxstate.put(String.valueOf(position), true);
			for (String key : itemState.keySet()) {
				itemState.put(key, false);
			}
			itemState.put(String.valueOf(position), true);
			address = tvAddress.getText().toString();
			// addressId = list.get(position).SubRegionId;
			addressCustomName = list.get(position).AddressCustomName;
		}

		// 更新item
		ChooseAddressAdapter.this.notifyDataSetChanged();
		listener.onAddressChecked(address, addressId, addressCustomName);
	}

	/**
	 * 
	 * @Description:获取所选的地址
	 * @Title:getAddress
	 * @return
	 * @return:String
	 * @throws
	 * @Create: 2016年5月18日 上午11:57:33
	 * @Author : chengtao
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 
	 * @Description:返回地址ID
	 * @Title:getAddressId
	 * @return
	 * @return:int
	 * @throws
	 * @Create: 2016年6月18日 下午4:39:55
	 * @Author : chengtao
	 */
	public int getAddressId() {
		return addressId;
	}

	/**
	 * 
	 * @Description:获取地址别名
	 * @Title:AddressCustomName
	 * @return
	 * @return:String
	 * @throws
	 * @Create: 2016年6月22日 下午2:10:33
	 * @Author : chengtao
	 */
	public String AddressCustomName() {
		return addressCustomName;
	}

}
