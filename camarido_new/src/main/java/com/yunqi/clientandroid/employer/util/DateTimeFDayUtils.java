package com.yunqi.clientandroid.employer.util;

import com.yunqi.clientandroid.utils.L;

import android.content.Context;

public class DateTimeFDayUtils {
	private Context context;
	private int year;
	private int month;
	private int day;
	private int count;
	private int maxMonthTime;
	private int maxDayTime;
	
	public DateTimeFDayUtils(Context context, int year, int month, int day,
			int count) {
		super();
		this.context = context;
		this.year = year;
		this.month = month;
		this.day = day;
		this.count = count;
	}


	/**
	 * if (day > 31) {
			if (month == 1 || month == 3 || month == 5
					|| month == 7 || month == 8
					|| month == 10 || month == 12) {
				maxDayTime = day - 32;
			} else if (month == 4 || month == 6 || month == 9
					|| month == 11) {
				maxDayTime = day - 31;
			} else if (month == 2) {
				maxDayTime = day - 28;
			}
			maxMonthTime = month + 1;
		} else if (day < 31) {
			maxDayTime = day;
			maxMonthTime = month;
		}
		L
	 */
	public int getDayText(){
		if (count > 31) {
			if (month == 1 || month == 3 || month == 5
					|| month == 7 || month == 8
					|| month == 10 || month == 12) {
				maxDayTime = day - 32;
			} else if (month == 4 || month == 6 || month == 9
					|| month == 11) {
				maxDayTime = day - 31;
			} else if (month == 2) {
				maxDayTime = day - 28;
			}
			maxMonthTime = month + 1;
		} else if (day < 31 && day >28) {
			if (month == 1 || month == 3 || month == 5
					|| month == 7 || month == 8
					|| month == 10 || month == 12) {
				maxDayTime = day;
				maxMonthTime = month;
			} else if (month == 4 || month == 6 || month == 9
					|| month == 11) {
				maxDayTime = day;
				maxMonthTime = month;
			} else if (month == 2) {
				maxDayTime = day - 28;
				maxMonthTime = month+1;
			}
		} else if (day <= 28) {
			maxDayTime = day;
			maxMonthTime = month;
		}
		
		return 0;
	}
}
