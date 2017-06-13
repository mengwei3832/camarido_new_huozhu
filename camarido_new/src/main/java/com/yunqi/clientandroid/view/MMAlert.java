package com.yunqi.clientandroid.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
//import com.baidu.android.common.util.Util;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class MMAlert {

	public interface OnAlertSelectId {
		void onClick(int whichButton);
	}

	private MMAlert() {

	}

	public static Dialog showAlert(final Context context, final String title,
			final String[] items, String exit, final OnAlertSelectId alertDo) {
		return showAlert(context, title, items, exit, alertDo, null);
	}

	/**
	 * @param context
	 *            Context.
	 * @param title
	 *            The title of this AlertDialog can be null .
	 * @param items
	 *            button name list.
	 * @param alertDo
	 *            methods call Id:Button + cancel_Button.
	 * @param exit
	 *            Name can be null.It will be Red Color
	 * @return A AlertDialog
	 */
	public static Dialog showAlert(final Context context, final String title,
			final String[] items, String exit, final OnAlertSelectId alertDo,
			OnCancelListener cancelListener) {
		String cancel = context.getString(R.string.dialog_cancel);
		final Dialog dlg = new Dialog(context, R.style.MMTheme_DataSheet);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.alert_dialog_menu_layout, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);
		final ListView list = (ListView) layout.findViewById(R.id.content_list);
		AlertAdapter adapter = new AlertAdapter(context, title, items, exit,
				cancel);
		list.setAdapter(adapter);
		list.setDividerHeight(0);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.v("FAN", "position=" + position);
				if (!(title == null || title.equals("")) && position - 1 >= 0) {
					alertDo.onClick(position - 1);
					dlg.dismiss();
					list.requestFocus();
				} else {
					alertDo.onClick(position);
					dlg.dismiss();
					list.requestFocus();
				}

			}
		});
		// set a large value put it in bottom
		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;// 改变显示位置
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);
		if (cancelListener != null) {
			dlg.setOnCancelListener(cancelListener);
		}
		dlg.setContentView(layout);
		dlg.show();
		return dlg;
	}
}

class AlertAdapter extends BaseAdapter {
	// private static final String TAG = "AlertAdapter";
	public static final int TYPE_BUTTON = 0;
	public static final int TYPE_TITLE = 1;
	public static final int TYPE_EXIT = 2;
	public static final int TYPE_CANCEL = 3;
	private List<String> items;
	private int[] types;
	// private boolean isSpecial = false;
	private boolean isTitle = false;
	// private boolean isExit = false;
	private Context context;

	public AlertAdapter(Context context, String title, String[] items,
			String exit, String cancel) {
		if (items == null || items.length == 0) {
			this.items = new ArrayList<String>();
		} else {
			List<String> list = Arrays.asList(items);
			this.items = new ArrayList<String>(list);
		}
		this.types = new int[this.items.size() + 3];
		this.context = context;
		if (title != null && !title.equals("")) {
			types[0] = TYPE_TITLE;
			this.isTitle = true;
			this.items.add(0, title);
		}

		if (exit != null && !exit.equals("")) {
			// this.isExit = true;
			types[this.items.size()] = TYPE_EXIT;
			this.items.add(exit);
		}

		if (cancel != null && !cancel.equals("")) {
			// this.isSpecial = true;
			types[this.items.size()] = TYPE_CANCEL;
			this.items.add(cancel);
		}
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public boolean isEnabled(int position) {
		if (position == 0 && isTitle) {
			return false;
		} else {
			return super.isEnabled(position);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final String textString = (String) getItem(position);
		ViewHolder holder;
		int type = types[position];
		if (convertView == null
				|| ((ViewHolder) convertView.getTag()).type != type) {
			holder = new ViewHolder();
			if (type == TYPE_CANCEL) {
				convertView = View.inflate(context,
						R.layout.alert_dialog_menu_list_layout_cancel, null);
			} else if (type == TYPE_BUTTON) {
				convertView = View.inflate(context,
						R.layout.alert_dialog_menu_list_layout, null);
			} else if (type == TYPE_TITLE) {
				convertView = View.inflate(context,
						R.layout.alert_dialog_menu_list_layout_title, null);
			} else if (type == TYPE_EXIT) {
				convertView = View.inflate(context,
						R.layout.alert_dialog_menu_list_layout_special, null);
			}

			// holder.view = (LinearLayout)
			// convertView.findViewById(R.id.popup_layout);
			holder.text = (TextView) convertView.findViewById(R.id.popup_text);
			holder.type = type;

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.text.setText(textString);
		return convertView;
	}

	static class ViewHolder {
		// LinearLayout view;
		TextView text;
		int type;
	}
}
