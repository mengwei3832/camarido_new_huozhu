package com.yunqi.clientandroid.employer.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunqi.clientandroid.R;

public class KeyboardsAdapter extends BaseAdapter {
    private List<String> keyList;
    private LayoutInflater layoutInflater;

    public KeyboardsAdapter(List<String> keyList, Context context) {
        super();
        this.keyList = keyList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return keyList.size();
    }

    @Override
    public Object getItem(int position) {
        return keyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String str = keyList.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(
                    R.layout.dialog_gridview_item, parent, false);
            viewHolder.btnKey = (TextView) convertView.findViewById(R.id.tv_dialog_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (!str.equals("×")) {
            viewHolder.btnKey.setText(str);
        }
        return convertView;
    }

    class ViewHolder {
        TextView btnKey;
    }
}
