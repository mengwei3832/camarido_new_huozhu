package com.yunqi.clientandroid.view.wheel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.util.UmengStatisticsUtils;
import com.yunqi.clientandroid.view.wheel.adapters.AbstractWheelTextAdapter;
import com.yunqi.clientandroid.view.wheel.views.OnWheelChangedListener;
import com.yunqi.clientandroid.view.wheel.views.OnWheelScrollListener;
import com.yunqi.clientandroid.view.wheel.views.WheelView;

/**
 * 
 * @Description:新版改变时间弹窗
 * @ClassName: NewChangeTimePopWin
 * @author: chengtao
 * @date: 2016年6月21日 下午2:57:14
 * 
 */
@SuppressLint("InflateParams")
public class NewChangeTimePopWin extends PopupWindow implements OnClickListener {
	// NewChangeTimePopWin布局界面
	private View contentView;
	// 上下文
	private Context context;
	// 起始时间
	private WheelView startWheelView;
	private List<String> startTimeList = new ArrayList<String>();
	private String startTime = "";
	// 结束时间
	private WheelView endWheelView;
	private List<String> endTimeList = new ArrayList<String>();
	private String endTime = "";

	// 开始时间Adapter
	changeTimeWheelAdapter startAdapter = null;
	// 结束时间Adapter
	changeTimeWheelAdapter endAdapter = null;

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
	// 接口
	private OnChangeTimeListener listener;
	// 用于计算时间列表
	private int year;
	private int month;
	private int day;
	private int days;
	// 列表最大长度
	private int maxDays = 200;
	// 是否能滚动以前的时间
	private boolean canScollBefore;
	//友盟统计
	private UmengStatisticsUtils mUmeng;

	public NewChangeTimePopWin(Context context, OnChangeTimeListener listener,
			boolean canScollBefore) {
		super();
		this.context = context;
		this.listener = listener;
		this.canScollBefore = canScollBefore;
		contentView = LayoutInflater.from(context).inflate(
				R.layout.send_package_time_poupwindow, null);
		Log.e("TAG", "NewChangeTimePopWin");
		initView();
		setListener();
		createStartTimeWheel();
		// createEndTimeWheel(3);
		mUmeng = UmengStatisticsUtils.instance(context);
	}

	/**
	 * 
	 * @Description:创建开始时间弹窗
	 * @Title:createStartTimePoupWindow
	 * @return:void
	 * @throws @Create: 2016年6月21日 下午4:10:06
	 * @Author : chengtao
	 */
	private void createStartTimeWheel() {
		startAdapter = new changeTimeWheelAdapter(context, 0, maxTextSize,
				minTextSize, startTimeList);
		startWheelView.setVisibleItems(3);
		startWheelView.setViewAdapter(startAdapter);
		if (canScollBefore) {
			startWheelView.setCurrentItem(startTimeList.size() - 1);
		} else {
			startWheelView.setCurrentItem(0);
		}
		createEndTimeWheel(startWheelView.getCurrentItem() + 3);
		startTime = (String) startAdapter.getItemText(startWheelView
				.getCurrentItem());
		startWheelView.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String currentText = (String) startAdapter
						.getItemText(startWheelView.getCurrentItem());
				int currentIndex = startWheelView.getCurrentItem();
				startTime = currentText;
				setTextviewSize(currentText, startAdapter);
				createEndTimeWheel(currentIndex + 3);
			}
		});
		startWheelView.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				String currentText = (String) startAdapter
						.getItemText(startWheelView.getCurrentItem());
				startTime = currentText;
				setTextviewSize(currentText, startAdapter);
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String currentText = (String) startAdapter
						.getItemText(startWheelView.getCurrentItem());
				startTime = currentText;
				setTextviewSize(currentText, startAdapter);
			}
		});
	}

	/**
	 * 
	 * @Description:创建结束时间弹窗
	 * @Title:createEndTimeWheel
	 * @param currentIdex
	 * @return:void
	 * @throws @Create: 2016年6月21日 下午4:20:03
	 * @Author : chengtao
	 */
	private void createEndTimeWheel(int currentIdex) {
		endAdapter = new changeTimeWheelAdapter(context, 0, maxTextSize,
				minTextSize, endTimeList);
		endWheelView.setVisibleItems(3);
		endWheelView.setViewAdapter(endAdapter);
		endWheelView.setCurrentItem(currentIdex);
		endTime = (String) endAdapter
				.getItemText(endWheelView.getCurrentItem());
		endWheelView.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String currentText = (String) endAdapter
						.getItemText(endWheelView.getCurrentItem());
				endTime = currentText;
				setTextviewSize(currentText, endAdapter);
			}
		});
		endWheelView.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				String currentText = (String) endAdapter
						.getItemText(endWheelView.getCurrentItem());
				endTime = currentText;
				setTextviewSize(currentText, endAdapter);
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String currentText = (String) endAdapter
						.getItemText(endWheelView.getCurrentItem());
				endTime = currentText;
				setTextviewSize(currentText, endAdapter);
			}
		});
	}

	/**
	 * 
	 * @Description:设置监听
	 * @Title:setListener
	 * @return:void
	 * @throws @Create: 2016年6月21日 下午3:11:00
	 * @Author : chengtao
	 */
	private void setListener() {
		cancle.setOnClickListener(this);
		sure.setOnClickListener(this);
		blackView.setOnClickListener(this);
	}

	/**
	 * 
	 * @Description:初始化控件
	 * @Title:initView
	 * @return:void
	 * @throws @Create: 2016年6月21日 下午3:10:38
	 * @Author : chengtao
	 */
	private void initView() {
		startWheelView = obtainView(R.id.wv_start);
		endWheelView = obtainView(R.id.wv_end);
		cancle = obtainView(R.id.tv_cancle);
		sure = obtainView(R.id.tv_sure);
		blackView = obtainView(R.id.black_block);
		// 初始化时间列表
		iniTimeList(startTimeList, false);
		iniTimeList(endTimeList, true);
		
		Log.e("TAG", "--------startTimeList-----------"+startTimeList.toString());
		Log.e("TAG", "--------endTimeList-----------"+endTimeList.toString());
	}

	/**
	 * 获取控件
	 * 
	 * @Description:
	 * @Title:obtainView
	 * @param id
	 * @return
	 * @return:T
	 * @throws @Create: 2016年5月12日 下午6:56:05
	 * @Author : chengtao
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T obtainView(int id) {
		return (T) contentView.findViewById(id);
	}

	/**
	 * 
	 * @Description:适配器
	 * @ClassName: changeTimeWheelAdapter
	 * @author: chengtao
	 * @date: 2016年6月21日 下午3:10:00
	 * 
	 */
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
	 * @Description:弹窗接口
	 * @ClassName: OnChangeTimeListener
	 * @author: chengtao
	 * @date: 2016年6月21日 下午3:23:35
	 * 
	 */
	public interface OnChangeTimeListener {
		void onTime(String startTime, String endTime);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_cancle:
			//友盟统计时间弹窗取消
			mUmeng.setCalculateEvents("ship_select_date_cancel");

			this.dismiss();
			break;
		case R.id.tv_sure:
			//友盟统计时间弹窗确认
			mUmeng.setCalculateEvents("ship_select_date_sure");

			listener.onTime(startTime, endTime);
			this.dismiss();
			break;
		case R.id.black_block:
			this.dismiss();
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @Description:获取当前年份
	 * @Title:getCurrentYear
	 * @return
	 * @return:int
	 * @throws @Create: 2016年5月12日 下午7:21:08
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
	 * @throws @Create: 2016年5月12日 下午7:21:39
	 * @Author : chengtao
	 */
	public int getCurrentMonth() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MONTH) + 1;
	}

	/**
	 * 
	 * @Description:获取当前日期
	 * @Title:getCurrentDay
	 * @return
	 * @return:int
	 * @throws @Create: 2016年5月12日 下午7:22:00
	 * @Author : chengtao
	 */
	public int getCurrentDay() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.DATE);
	}

	/**
	 * 
	 * @Description:通过当前年份和月份计算天数
	 * @Title:calcualateDay
	 * @param cyear
	 * @param cmonth
	 * @return:void
	 * @throws @Create: 2016年5月12日 下午8:22:43
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
	 * @Description:显示弹窗
	 * @Title:showPopWin
	 * @param activity
	 * @return:void
	 * @throws @Create: 2016年6月21日 下午3:33:26
	 * @Author : chengtao
	 */
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

	/**
	 * 
	 * @Description:初始化时间列表
	 * @Title:iniTimeList
	 * @param list
	 * @return:void
	 * @throws @Create: 2016年6月21日 下午4:05:07
	 * @Author : chengtao
	 */
	private void iniTimeList(List<String> list, Boolean isEnd) {
		Log.e("TAG", "iniTimeList");
		year = getCurrentYear();
		month = getCurrentMonth();
		day = getCurrentDay();
		calcualateDay(year, month);
		
		Log.e("TAG", "+++++++year++++++++"+year);
		Log.e("TAG", "+++++++month++++++++"+month);
		Log.e("TAG", "+++++++day++++++++"+day);
		
		int i = 0;
		if (canScollBefore) {
			while (i < maxDays) {
				while (day >= 1) {
					if (month < 10) {
						if (day < 10) {
							list.add(0, year + "-0" + month + "-0" + day);
						} else {
							list.add(0, year + "-0" + month + "-" + day);
						}
					} else {
						if (day < 10) {
							list.add(0, year + "-" + month + "-0" + day);
						} else {
							list.add(0, year + "-" + month + "-" + day);
						}
					}
					day--;
					i++;
					if (i > maxDays) {
						break;
					}
				}
				month--;
				if (month < 1) {
					month = 12;
					year--;
				}
				calcualateDay(year, month);
				day = days;
			}
			if (isEnd) {
				Log.e("TAG", "-------isEnd-------" + "isEnd");
				year = getCurrentYear();
				month = getCurrentMonth();
				day = getCurrentDay() + 1;
				calcualateDay(year, month);
				i = 0;
				while (i < 30) {
					while (day <= days) {
						if (month < 10) {
							if (day < 10) {
								list.add(year + "-0" + month + "-0" + day);
							} else {
								list.add(year + "-0" + month + "-" + day);
							}
						} else {
							if (day < 10) {
								list.add(year + "-" + month + "-0" + day);
							} else {
								list.add(year + "-" + month + "-" + day);
							}
						}
						day++;
						i++;
						if (i > maxDays) {
							break;
						}
					}
					day = 1;
					month++;
					if (month > 12) {
						month = 1;
						year++;
					}
					calcualateDay(year, month);
					Log.e("TAG", "-------isEnd-------" + "isEnd");
				}
			}
		} else {
			while (i < maxDays) {
				while (day <= days) {
					if (month < 10) {
						if (day < 10) {
							list.add(year + "-0" + month + "-0" + day);
						} else {
							list.add(year + "-0" + month + "-" + day);
						}
					} else {
						if (day < 10) {
							list.add(year + "-" + month + "-0" + day);
						} else {
							list.add(year + "-" + month + "-" + day);
						}
					}
					day++;
					i++;
					if (i > maxDays) {
						break;
					}
				}
				day = 1;
				month++;
				if (month > 12) {
					month = 1;
					year++;
				}
				calcualateDay(year, month);
			}
		}
	}

}
