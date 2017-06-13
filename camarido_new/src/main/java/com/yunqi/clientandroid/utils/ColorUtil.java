package com.yunqi.clientandroid.utils;

import android.view.View;
import android.widget.ImageView;

import com.yunqi.clientandroid.R;

public class ColorUtil {

	private static final int TICKET_LOCATION_BG = 0;
	private static final int TICKET_LOCATION_BG1 = 1;
	private static final int TICKET_LOCATION_BG2 = 2;
	private static final int TICKET_LOCATION_BG3 = 3;
	private static final int TICKET_LOCATION_BG4 = 4;
	private static final int TICKET_LOCATION_BG5 = 5;

	public static void changeBackgroudStyle(int checkedCode, View view) {
		if (checkedCode == TICKET_LOCATION_BG) {
			view.setBackgroundResource(R.drawable.order_detail_location_bg);
		} else if (checkedCode == TICKET_LOCATION_BG1) {
			view.setBackgroundResource(R.drawable.order_detail_location_bg1);
		} else if (checkedCode == TICKET_LOCATION_BG2) {
			view.setBackgroundResource(R.drawable.order_detail_location_bg2);
		} else if (checkedCode == TICKET_LOCATION_BG3) {
			view.setBackgroundResource(R.drawable.order_detail_location_bg3);
		} else if (checkedCode == TICKET_LOCATION_BG4) {
			view.setBackgroundResource(R.drawable.order_detail_location_bg4);
		} else if (checkedCode == TICKET_LOCATION_BG5) {
			view.setBackgroundResource(R.drawable.order_detail_location_bg5);
		}

	}

	public static void changeImageViewStyle(int checkedCode, ImageView imageView) {
		if (checkedCode == TICKET_LOCATION_BG) {
			imageView.setImageResource(R.drawable.order_detail_location_bg);
		} else if (checkedCode == TICKET_LOCATION_BG1) {
			imageView.setImageResource(R.drawable.order_detail_location_bg1);
		} else if (checkedCode == TICKET_LOCATION_BG2) {
			imageView.setImageResource(R.drawable.order_detail_location_bg2);
		} else if (checkedCode == TICKET_LOCATION_BG3) {
			imageView.setImageResource(R.drawable.order_detail_location_bg3);
		} else if (checkedCode == TICKET_LOCATION_BG4) {
			imageView.setImageResource(R.drawable.order_detail_location_bg4);
		} else if (checkedCode == TICKET_LOCATION_BG5) {
			imageView.setImageResource(R.drawable.order_detail_location_bg5);
		}

	}

}
