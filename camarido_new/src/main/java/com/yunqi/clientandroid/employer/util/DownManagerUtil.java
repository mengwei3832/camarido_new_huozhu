package com.yunqi.clientandroid.employer.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class DownManagerUtil {
	private static long id;

	public DownManagerUtil() {
		super();
	}

	public static void downManagerEx(Context context,
			DownloadManager downmanager, String url) {
		// 获取当前时间
		System.currentTimeMillis();

		String murl = null;
		String surl = null;
		try {
			murl = URLEncoder.encode(url, "utf-8").replaceAll("\\+", "%20");
			surl = murl.replaceAll("%3A", ":").replaceAll("%2F", "/");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String name = url.substring(url.lastIndexOf("/") + 1);

		DownloadManager.Request request = new DownloadManager.Request(
				Uri.parse(surl));
		request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		request.setTitle("统计报表");
		request.setAllowedOverRoaming(true);
		request.setVisibleInDownloadsUi(true);
		request.allowScanningByMediaScanner();
		request.setMimeType("application/vnd.ms-excel");
		request.setDestinationInExternalPublicDir(
				Environment.DIRECTORY_DOWNLOADS, name);
		id = downmanager.enqueue(request);

	}

}
