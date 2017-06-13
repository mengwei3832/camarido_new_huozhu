package com.yunqi.clientandroid.adapter;

import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.entity.DiscoverListItem;
import com.yunqi.clientandroid.entity.Tag;

public class TagManagerAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private List<DiscoverListItem> mHeaderList; // header titles
	// child data in format of header title, child title
	private LinkedHashMap<DiscoverListItem, List<Tag>> mChildMap;
	private DisplayImageOptions option;

	public TagManagerAdapter(Context context,
			List<DiscoverListItem> listDataHeader,
			LinkedHashMap<DiscoverListItem, List<Tag>> listChildData) {
		this.mContext = context;
		this.mHeaderList = listDataHeader;
		this.mChildMap = listChildData;
		option = new DisplayImageOptions.Builder().cacheInMemory(true).build();
	}

	@Override
	public String getChild(int groupPosition, int childPosititon) {
		Log.d("ExpandAdapter", "in child get ");
		return this.mChildMap.get(this.mHeaderList.get(groupPosition)).get(
				childPosititon).name;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final String childText = getChild(groupPosition, childPosition);
		Log.d("Expandable List View", "Childview Displayed");
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(
					R.layout.adapter_tag_manager_child, null);
		}

		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.tv_child_name);

		if (!TextUtils.isEmpty(childText) && childText != null) {
			txtListChild.setText(childText);
		}

		return convertView;
	}

	public void setData(List<DiscoverListItem> listDataHeader,
			LinkedHashMap<DiscoverListItem, List<Tag>> listChildData) {
		this.mHeaderList = listDataHeader;
		this.mChildMap = listChildData;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		Log.d("expanding",
				"childview called"
						+ this.mChildMap.get(
								this.mHeaderList.get(groupPosition)).size());
		return this.mChildMap.get(this.mHeaderList.get(groupPosition)).size();
	}

	@Override
	public DiscoverListItem getGroup(int groupPosition) {
		Log.d("ExpandAdapter", "in grp ");
		return this.mHeaderList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		Log.d("ExpandAdapter", "in grp count");
		return this.mHeaderList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		Log.d("ExpandAdapter", "in grp Id");
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		DiscoverListItem discoverListItem = getGroup(groupPosition);
		String tagName = discoverListItem.tagName;
		String imgUrl = discoverListItem.imgUrl;

		if (convertView == null) {
			Log.d("ExpandAdapter", "in grp view");
			LayoutInflater infalInflater = (LayoutInflater) this.mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(
					R.layout.adapter_tag_manager_group, null);
		}

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.tv_discover_name);
		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.iv_discover_image);

		if (!TextUtils.isEmpty(tagName) && tagName != null) {
			lblListHeader.setText(tagName);
		}

		if (!TextUtils.isEmpty(imgUrl) && imgUrl != null) {
			ImageLoader.getInstance().displayImage(imgUrl, imageView, option);
		}

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}