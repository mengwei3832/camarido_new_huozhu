package com.yunqi.clientandroid.fragment;

import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.adapter.TagManagerAdapter;
import com.yunqi.clientandroid.entity.DiscoverListItem;
import com.yunqi.clientandroid.entity.Tag;
import com.yunqi.clientandroid.http.request.CancelTagRequest;
import com.yunqi.clientandroid.http.request.GetAllTagsRequest;
import com.yunqi.clientandroid.http.request.GetDiscoverListRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.MiPushUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author zhangwenbin
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 标签管理fragment
 * @date 15/12/20
 */
public class TagManagerFragment extends BaseFragment implements
		ExpandableListView.OnChildClickListener {

	private Tag mTag;
	private ExpandableListView elvTag;
	TagManagerAdapter mTagmanagerAdapter;
	List<DiscoverListItem> mHeaderList;
	LinkedHashMap<DiscoverListItem, List<Tag>> mChildMap;
	private FrameLayout flBlank;

	private GetAllTagsRequest mGetAllTagsRequest;
	private GetDiscoverListRequest mGetDiscoverListRequest;
	private CancelTagRequest mCancelTagRequest;

	private final int GET_ALL_TAGS_REQUEST = 1;
	private final int GET_DISCOVERLIST_REQUEST = 2;
	private final int CANCEL_TAG_REQUEST = 3;

	private int mGroupPosition;
	private int mChildPosition;

	@Override
	protected void initData() {
		mGetDiscoverListRequest = new GetDiscoverListRequest(getActivity());
		mGetDiscoverListRequest.setRequestId(GET_DISCOVERLIST_REQUEST);
		httpPost(mGetDiscoverListRequest);

		mHeaderList = new ArrayList<DiscoverListItem>();
		mChildMap = new LinkedHashMap<DiscoverListItem, List<Tag>>();

		Log.e("TAG", "---------mHeaderList------------" + mHeaderList.size());
		Log.e("TAG", "---------mChildMap------------" + mChildMap.size());

		if (mHeaderList.size() == 0) {
			showToast("暂无相关管理信息");
			flBlank.setVisibility(View.VISIBLE);
		} else {
			flBlank.setVisibility(View.GONE);
		}

		mTagmanagerAdapter = new TagManagerAdapter(getActivity(), mHeaderList,
				mChildMap);
		elvTag.setAdapter(mTagmanagerAdapter);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_tag_manager;
	}

	@Override
	protected void initView(View _rootView) {
		elvTag = (ExpandableListView) _rootView.findViewById(R.id.elv_tag);
		elvTag.setGroupIndicator(null);
		flBlank = (FrameLayout) _rootView.findViewById(R.id.fl_elv_tag_blank);

	}

	@Override
	protected void setListener() {
		elvTag.setOnChildClickListener(this);

	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		this.mGroupPosition = groupPosition;
		this.mChildPosition = childPosition;
		Tag tag = mChildMap.get(mHeaderList.get(groupPosition)).get(
				childPosition);
		mTag = tag;
		mCancelTagRequest = new CancelTagRequest(getActivity(), tag.id);
		mCancelTagRequest.setRequestId(CANCEL_TAG_REQUEST);
		httpPost(mCancelTagRequest);

		return false;
	}

	@Override
	public void onStart(int requestId) {
		super.onStart(requestId);
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess;
		String message;
		switch (requestId) {
		case GET_ALL_TAGS_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				List<Tag> tags = response.data;

				for (DiscoverListItem item : mHeaderList) {
					List<Tag> list = new ArrayList<Tag>();
					for (Tag tag : tags) {
						if (tag.pId == item.id) {
							list.add(tag);
						}
					}
					mChildMap.put(item, list);
				}

				mTagmanagerAdapter.setData(mHeaderList, mChildMap);
				mTagmanagerAdapter.notifyDataSetChanged();
				for (int i = 0; i < mTagmanagerAdapter.getGroupCount(); i++) {
					elvTag.expandGroup(i);
				}
			} else {

			}
			break;
		case GET_DISCOVERLIST_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				mHeaderList = response.data;

				// if (mHeaderList.size() == 0) {
				// showToast("暂无相关管理信息");
				// flBlank.setVisibility(View.VISIBLE);
				// } else {
				// flBlank.setVisibility(View.GONE);
				// }
				mGetAllTagsRequest = new GetAllTagsRequest(getActivity());
				mGetAllTagsRequest.setRequestId(GET_ALL_TAGS_REQUEST);
				httpPost(mGetAllTagsRequest);
			} else {

			}
			break;
		case CANCEL_TAG_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				MiPushUtil.setMiPushUnTopic(getActivity(), "T" + mTag.id);
				mChildMap.get(mHeaderList.get(mGroupPosition)).remove(
						mChildPosition);
				mTagmanagerAdapter.setData(mHeaderList, mChildMap);
				mTagmanagerAdapter.notifyDataSetChanged();
			}
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		switch (requestId) {
		case GET_DISCOVERLIST_REQUEST:
			showToast("连接超时,请检查网络");
			break;
		}
	}
}
