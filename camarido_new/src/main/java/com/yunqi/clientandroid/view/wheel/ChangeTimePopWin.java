package com.yunqi.clientandroid.view.wheel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.view.wheel.adapters.AbstractWheelTextAdapter;
import com.yunqi.clientandroid.view.wheel.views.OnWheelChangedListener;
import com.yunqi.clientandroid.view.wheel.views.OnWheelScrollListener;
import com.yunqi.clientandroid.view.wheel.views.WheelView;

public class ChangeTimePopWin extends PopupWindow implements OnClickListener {
	// ChangeTimePopWin布局界面
	private View contentView;
	// 上下文
	private Context context;
	// 当前年份
	private WheelView wvYear;
	private int currentYear;
	private String selectedYear;
	private List<String> yearList = new ArrayList<String>();
	// 当前月份
	private WheelView wvMonth;
	private int currentMonth;
	private String selectedMonth;
	private List<String> monthList = new ArrayList<String>();
	// 当前日期
	private WheelView wvDay;
	private int currentDay;
	private String selectedDay;
	private List<String> dayList = new ArrayList<String>();
	// 取消按钮
	private TextView cancle;
	// 确定按钮
	private TextView sure;
	// 阴影部分
	private View blackView;
	// 最大字体
	private int maxTextSize = 24;
	// 最小字体
	private int minTextSize = 12;

	// 获取日期天数
	private int days;

	// 年份Adapter
	changeTimeWheelAdapter yearAdapter = null;
	// 月份份Adapter
	changeTimeWheelAdapter monthAdapter = null;
	// 日期Adapter
	changeTimeWheelAdapter dayAdapter = null;

	// 接口
	private OnChangeTimePopWinListener listener;

	@SuppressLint("InflateParams")
	public ChangeTimePopWin(Context context) {
		super();
		this.context = context;
		contentView = LayoutInflater.from(context).inflate(
				R.layout.send_package_time_wheel_pop_window, null);
		init();
		setListenter();
		createChangeTimePoupWindow();
	}

	private void createChangeTimePoupWindow() {
		createYearWheel();
		createMonthWheel(currentMonth - 1);
		createDayWheel(currentDay - 1, dayList);
	}

	/**
	 * 
	 * @Description:创建日期Wheel
	 * @Title:createDayWheel
	 * @param currentIndex
	 * @param dayList
	 * @return:void
	 * @throws
	 * @Create: 2016年5月12日 下午10:26:48
	 * @Author : chengtao
	 */
	private void createDayWheel(int currentIndex, List<String> dayList) {
		dayAdapter = new changeTimeWheelAdapter(context, currentIndex,
				maxTextSize, minTextSize, dayList);
		wvDay.setVisibleItems(5);
		wvDay.setViewAdapter(dayAdapter);
		wvDay.setCurrentItem(currentIndex);
		wvDay.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String curretText = (String) dayAdapter.getItemText(wheel
						.getCurrentItem());
				selectedDay = curretText;
				setTextviewSize(curretText, dayAdapter);
			}
		});
		wvDay.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String curretText = (String) dayAdapter.getItemText(wheel
						.getCurrentItem());
				selectedDay = curretText;
				setTextviewSize(curretText, dayAdapter);
			}
		});
	}

	/**
	 * 
	 * @Description:创建月份Wheel
	 * @Title:createMonthWheel
	 * @param currentIndex
	 * @return:void
	 * @throws
	 * @Create: 2016年5月12日 下午10:27:11
	 * @Author : chengtao
	 */
	private void createMonthWheel(int currentIndex) {
		monthAdapter = new changeTimeWheelAdapter(context, currentIndex,
				maxTextSize, minTextSize, monthList);
		wvMonth.setVisibleItems(5);
		wvMonth.setViewAdapter(monthAdapter);
		wvMonth.setCurrentItem(currentIndex);
		wvMonth.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String curretText = (String) monthAdapter.getItemText(wheel
						.getCurrentItem());
				selectedMonth = curretText;
				setTextviewSize(curretText, monthAdapter);
				if (Integer.parseInt(selectedYear) == currentYear
						&& Integer.parseInt(selectedMonth) == currentMonth) {
					calcualateDay(Integer.parseInt(selectedYear),
							Integer.parseInt(selectedMonth));
					initDayList(days);
					createDayWheel(currentDay - 1, dayList);
				} else {
					calcualateDay(Integer.parseInt(selectedYear),
							Integer.parseInt(selectedMonth));
					initDayList(days);
					createDayWheel(0, dayList);
				}
			}
		});
		wvMonth.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String curretText = (String) monthAdapter.getItemText(wheel
						.getCurrentItem());
				selectedMonth = curretText;
				setTextviewSize(curretText, monthAdapter);
			}
		});
	}

	/**
	 * 
	 * @Description:创建年份Wheel
	 * @Title:createYearWheel
	 * @return:void
	 * @throws
	 * @Create: 2016年5月12日 下午10:27:35
	 * @Author : chengtao
	 */
	private void createYearWheel() {
		yearAdapter = new changeTimeWheelAdapter(context, yearList.size() / 2,
				maxTextSize, minTextSize, yearList);
		wvYear.setVisibleItems(5);
		wvYear.setViewAdapter(yearAdapter);
		wvYear.setCurrentItem(yearList.size() / 2);
		wvYear.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String currentText = (String) yearAdapter.getItemText(wheel
						.getCurrentItem());
				selectedYear = currentText;
				setTextviewSize(currentText, yearAdapter);
				if (Integer.parseInt(selectedYear) != currentYear) {
					createMonthWheel(0);
				} else {
					createMonthWheel(currentMonth - 1);
				}
			}
		});
		wvYear.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String currentText = (String) yearAdapter.getItemText(wheel
						.getCurrentItem());
				selectedYear = currentText;
				setTextviewSize(currentText, yearAdapter);
			}
		});
	}

	private void setListenter() {
		cancle.setOnClickListener(this);
		sure.setOnClickListener(this);
		blackView.setOnClickListener(this);
	}

	/**
	 * 
	 * @Description:初始化
	 * @Title:init
	 * @return:void
	 * @throws
	 * @Create: 2016年5月12日 下午10:27:57
	 * @Author : chengtao
	 */
	private void init() {
		wvYear = obtainView(R.id.wv_year);
		wvMonth = obtainView(R.id.wv_month);
		wvDay = obtainView(R.id.wv_day);
		cancle = obtainView(R.id.tv_cancle);
		sure = obtainView(R.id.tv_sure);
		blackView = obtainView(R.id.black_lock);
		currentYear = getCurrentYear();
		currentMonth = getCurrentMonth();
		currentDay = getCurrenttDay();
		selectedYear = getCurrentYear() + "";
		selectedMonth = getCurrentMonth() + "";
		selectedDay = getCurrenttDay() + "";
		calcualateDay(currentYear, currentMonth);
		initYearList();
		initMonthList();
		initDayList(days);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_sure:
			if (listener != null) {
				listener.onTimePoupWindowListener(selectedYear, selectedMonth,
						selectedDay);
			}
			this.dismiss();
			break;
		case R.id.tv_cancle:
		case R.id.black_lock:
			this.dismiss();
			break;
		default:
			break;
		}
	}

	/**
	 * 获取控件
	 * 
	 * @Description:
	 * @Title:obtainView
	 * @param id
	 * @return
	 * @return:T
	 * @throws
	 * @Create: 2016年5月12日 下午6:56:05
	 * @Author : chengtao
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T obtainView(int id) {
		return (T) contentView.findViewById(id);
	}

	/**
	 * 
	 * @Description:接口
	 * @ClassName: OnChangeTimePopWinListener
	 * @author: chengtao
	 * @date: 2016年5月12日 下午7:38:51
	 * 
	 */
	public interface OnChangeTimePopWinListener {
		void onTimePoupWindowListener(String selectedYear,
				String selectedMonth, String selectedDay);
	}

	/**
	 * 
	 * @Description:设置接口
	 * @Title:setOnChangeTimePopWinListener
	 * @param listener
	 * @return:void
	 * @throws
	 * @Create: 2016年5月12日 下午7:39:02
	 * @Author : chengtao
	 */
	public void setOnChangeTimePopWinListener(
			OnChangeTimePopWinListener listener) {
		this.listener = listener;
	}

	private class changeTimeWheelAdapter extends AbstractWheelTextAdapter {
		private List<String> list;

		protected changeTimeWheelAdapter(Context context, int currentIndex,
				int maxsize, int minsize, List<String> list) {
			super(context, R.layout.item_send_package_wheel_text,
					R.id.tv_item_wheel, currentIndex, maxsize, minsize);
			this.list = list;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index);
		}
	}

	/**
	 * 
	 * @Description:获取当前年份
	 * @Title:getCurrentYear
	 * @return
	 * @return:int
	 * @throws
	 * @Create: 2016年5月12日 下午7:21:08
	 * @Author : chengtao
	 */
	public int getCurrentYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}

	/**
	 * 
	 * @Description:获取当前月份
	 * @Title:getCurrentMonth
	 * @return
	 * @return:int
	 * @throws
	 * @Create: 2016年5月12日 下午7:21:39
	 * @Author : chengtao
	 */
	public int getCurrentMonth() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MONTH) + 1;
	}

	/**
	 * 
	 * @Description:获取当前日期
	 * @Title:getCurrenttDay
	 * @return
	 * @return:int
	 * @throws
	 * @Create: 2016年5月12日 下午7:22:00
	 * @Author : chengtao
	 */
	public int getCurrenttDay() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.DATE);
	}

	/**
	 * 设置字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSize(String curriteItemText,
			AbstractWheelTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(maxTextSize);
			} else {
				textvew.setTextSize(minTextSize);
			}
		}
	}

	/**
	 * 
	 * @Description:初始化年份List
	 * @Title:initYearList
	 * @return:void
	 * @throws
	 * @Create: 2016年5月12日 下午8:44:37
	 * @Author : chengtao
	 */
	private void initYearList() {
		yearList.add((currentYear - 1) + "");
		yearList.add(currentYear + "");
		yearList.add((currentYear + 1) + "");
	}

	/**
	 * 
	 * @Description:初始化月份List
	 * @Title:initMonthList
	 * @return:void
	 * @throws
	 * @Create: 2016年5月12日 下午8:49:50
	 * @Author : chengtao
	 */
	private void initMonthList() {
		for (int i = 1; i <= 12; i++) {
			if (i < 10) {
				monthList.add("0" + i);
			} else {
				monthList.add(i + "");
			}
		}
	}

	/**
	 * 
	 * @Description:初始化日期List
	 * @Title:initDayList
	 * @param days
	 * @return:void
	 * @throws
	 * @Create: 2016年5月12日 下午8:50:07
	 * @Author : chengtao
	 */
	private void initDayList(int days) {
		dayList.clear();
		for (int i = 1; i <= days; i++) {
			if (i < 10) {
				dayList.add("0" + i);
			} else {
				dayList.add(i + "");
			}
		}
	}

	/**
	 * 
	 * @Description:通过当前年份和月份计算天数
	 * @Title:calcualateDay
	 * @param cyear
	 * @param cmonth
	 * @return:void
	 * @throws
	 * @Create: 2016年5月12日 下午8:22:43
	 * @Author : chengtao
	 */
	private void calcualateDay(int cyear, int cmonth) {
		boolean isLeapYear = false;
		if (cyear % 4 == 0 && cyear % 100 != 0) {
			isLeapYear = true;
		} else {
			isLeapYear = false;
		}
		switch (cmonth) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			days = 31;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			days = 30;
			break;
		case 2:
			if (isLeapYear) {
				days = 29;
			} else {
				days = 28;
			}
			break;
		default:
			break;
		}
	}

	public void showPopWin(Activity activity) {
		setTouchable(true);
		setFocusable(true);
		setAnimationStyle(R.style.FadeInPopWin);
		setContentView(contentView);
		setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
		if (null != activity) {
			showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM,
					0, 0);
		}
	}
}
