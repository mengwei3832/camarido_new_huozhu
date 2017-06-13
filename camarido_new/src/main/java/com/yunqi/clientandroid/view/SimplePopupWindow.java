package com.yunqi.clientandroid.view;

import java.util.List;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.util.UmengStatisticsUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @Description:
 * @ClassName: SimplePoupWindow
 * @author: chengtao
 * @date: Aug 13, 2016 11:47:12 AM
 * 
 */
@SuppressLint("InflateParams")
public class SimplePopupWindow extends PopupWindow {
	private Context context;
	private List<String> list;
	private int id;
	private OnSimplePopupWindowItemClickListener listener;

	private UmengStatisticsUtils mUmeng;

	private RelativeLayout rlMain;
	private ListView listView;
	private SimplePopupWindowAdapter adapter;

	public SimplePopupWindow(Context context, List<String> list, int id,
			OnSimplePopupWindowItemClickListener listener) {
		super();
		this.context = context;
		this.list = list;
		this.id = id;
		this.listener = listener;
		mUmeng = UmengStatisticsUtils.instance(context);
	}

	/**
	 * 
	 * @Description:创建弹窗
	 * @Title:createSimplePopupWindow
	 * @return:void
	 * @throws
	 * @Create: Aug 13, 2016 12:36:08 PM
	 * @Author : chengtao
	 */
	@SuppressWarnings("deprecation")
	private void createSimplePopupWindow() {
		if (list == null || context == null) {
			return;
		}
		View view = LayoutInflater.from(context).inflate(
				R.layout.employer_popupwindow_goods_type, null);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setBackgroundDrawable(new BitmapDrawable());
		this.setContentView(view);
		this.setOutsideTouchable(true);
		this.setFocusable(true);
		this.setTouchable(true); // 设置PopupWindow可触摸
		this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		listView = (ListView) view.findViewById(R.id.lv_pop_goods);
		rlMain = (RelativeLayout) view.findViewById(R.id.rl_pop_goods);
		adapter = new SimplePopupWindowAdapter();
		listView.setAdapter(adapter);
	}

	/**
	 * 
	 * @Description:显示弹窗
	 * @Title:showPop
	 * @return:void
	 * @throws
	 * @Create: Aug 13, 2016 12:46:09 PM
	 * @Author : chengtao
	 */
	@SuppressWarnings("unused")
	public void showPop(Activity activity) {
		createSimplePopupWindow();
		if (null != activity) {
			this.showAtLocation(activity.getWindow().getDecorView(),
					Gravity.BOTTOM, 0, 0);
		}
		rlMain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SimplePopupWindow.this.dismiss();
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String string = list.get(position);

				// Toast.makeText(context, string, Toast.LENGTH_SHORT).show();;

				//友盟统计首页
				mUmeng.setCalculateEvents("search_select_broker");

				// 接口回调
				listener.onSimplePopupWindowItemClick(
						SimplePopupWindow.this.id, string);

				// 关闭弹框
				SimplePopupWindow.this.dismiss();
			}
		});
	}

	public interface OnSimplePopupWindowItemClickListener {
		/**
		 * 
		 * @Description:
		 * @Title:onSimplePopupWindowItemClick
		 * @param id
		 *            弹窗id
		 * @param value
		 *            每个Item值
		 * @return:void
		 * @throws
		 * @Create: Aug 13, 2016 12:33:35 PM
		 * @Author : chengtao
		 */
		void onSimplePopupWindowItemClick(int id, String value);
	}

	class SimplePopupWindowAdapter extends BaseAdapter {

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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();

				convertView = LayoutInflater.from(context).inflate(
						R.layout.employer_pop_item_goods, null);

				viewHolder.tvName = (TextView) convertView
						.findViewById(R.id.tv_pop_item_goods);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.tvName.setText(list.get(position));

			return convertView;
		}

		class ViewHolder {
			TextView tvName;
		}

	}

}
