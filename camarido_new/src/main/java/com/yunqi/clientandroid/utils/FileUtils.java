package com.yunqi.clientandroid.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @auther zhangwb 文件工具类
 * @date 2015/11/22.
 */
public class FileUtils {
	/**
	 * 处理文件 , 拷贝文件
	 * 
	 * @param fromFile
	 * @param toFile
	 */
	public static boolean copyFile(File fromFile, File toFile) {

		if (!fromFile.exists() || !fromFile.isFile()) {
			LogHelper.print("== fromFile not exsits");
			return false;
		}

		if (!toFile.exists()) {
			try {
				toFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		FileOutputStream outputStream = null;
		FileInputStream inputStream = null;
		try {
			outputStream = new FileOutputStream(toFile);

			inputStream = new FileInputStream(fromFile);

			int len = 0;
			byte[] buffer = new byte[512];

			while ((len = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, len);
			}

			return true;

		} catch (Exception e) {
			LogHelper.print("== fromFile not exsits" + e.toString());

		} finally {
			LogHelper.print("== fromFile not exsits  fuck");

			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return false;
	}

	/**
	 * 获取文件路径
	 * 
	 * @param contentUri
	 * @return
	 */
	public static String getRealPathFromURI(Uri contentUri, Context context) {

		try {
			String res = null;
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = context.getContentResolver().query(contentUri,
					proj, null, null, null);
			if (cursor.moveToFirst()) {
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				res = cursor.getString(column_index);
			}
			cursor.close();
			return res;
		} catch (Exception e) {

		}
		return null;

	}

}
