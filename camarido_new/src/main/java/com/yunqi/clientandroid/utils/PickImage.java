package com.yunqi.clientandroid.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.*;

/**
 * 图片选择类
 * 
 * @Description:
 * @ClassName: PickImage
 * @author: zhm
 * @date: 2015年8月19日 下午9:07:42
 * 
 */
public class PickImage {

	private final static String LOG_TAG = "log";

	/**
	 * 
	 * @Title:pickImageFromPhoto
	 * @Description:从相册选择图片
	 * @param activity
	 * @param requestCode
	 * @return:void
	 * @throws
	 * @Create: 2014-3-31 下午6:07:19
	 * @Author : zhm 邮箱：zhaomeng@baihe.com
	 */
	public static void pickImageFromPhoto(Activity activity, int requestCode) {
		if (!getSDCardStatus()) {
			commonToast(activity, "存储卡不可用");
			return;
		}

		try {
			Intent intent = new Intent(Intent.ACTION_PICK, null);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					"image/*");
			activity.startActivityForResult(intent, requestCode);
		} catch (Exception e) {
			commonToast(activity, "无法打开手机相册");
		}
	}

	/**
	 * 
	 * @Title:getSDCardStatus
	 * @Description:判断sd卡是可用
	 * @return
	 * @return:boolean
	 * @throws
	 * @Create: 2014-3-31 下午6:03:45
	 * @Author : zhm 邮箱：zhaomeng@baihe.com
	 */
	public static boolean getSDCardStatus() {

		String state = android.os.Environment.getExternalStorageState();
		if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {
			if (android.os.Environment.getExternalStorageDirectory().canWrite()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @Title:commonToast
	 * @Description:toast提示
	 * @param context
	 * @param resId
	 * @return:void
	 * @throws
	 * @Create: 2014-3-14 下午5:18:13
	 * @Author : zhm 邮箱：zhaomeng@baihe.com
	 */
	public static void commonToast(Context context, int resId) {
		Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}

	public static void commonToast(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 
	 * @Title:pickImageFromCamera
	 * @Description:相机拍照
	 * @param activity
	 * @param filepath
	 * @param requestCode
	 * @return:void
	 * @throws
	 * @Create: 2014-3-31 下午6:07:26
	 * @Author : zhm 邮箱：zhaomeng@baihe.com
	 */
	public static void pickImageFromCamera(Activity activity, String filepath,
			int requestCode) {
		if (!getSDCardStatus()) {
			commonToast(activity, "存储卡不可用");
			return;
		}

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(filepath);

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				Log.e(PickImage.class.getSimpleName(), "[pickImageFromCamera]",
						e);
			}
		}

		// 下面这句指定调用相机拍照后的照片存储的路径
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 
	 * @Title:getPathFromUri
	 * @Description:从媒体库中获取图片
	 * @param context
	 * @param uri
	 * @param needCrop
	 * @return
	 * @return:String
	 * @throws
	 * @Create: 2014-4-1 下午6:06:12
	 * @Author : zhm 邮箱：zhaomeng@baihe.com
	 */
	public static String getPathFromUri(Context context, Uri uri) {
		String img_path = null;
		if (uri.toString().contains("content")) {
			Cursor cursor = null;
			try {
				String[] proj = { MediaStore.Images.Media.DATA };
				android.support.v4.content.CursorLoader cursorLoader = new android.support.v4.content.CursorLoader(
						context, uri, proj, null, null, null);
				Cursor actualimagecursor = cursorLoader.loadInBackground();
				int actual_image_column_index = actualimagecursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

				actualimagecursor.moveToFirst();
				img_path = actualimagecursor
						.getString(actual_image_column_index);

			} catch (Exception e) {
				Log.e(LOG_TAG, "[getPathFromPhoto]", e);
			} finally {
				if (cursor != null) {
					try {
						cursor.close();
					} catch (Exception e) {
						Log.e(LOG_TAG, "[getPathFromPhoto]", e);
					}
				}
			}
		} else {
			img_path = uri.getPath();
		}
		return img_path;

	}

	/**
	 * 
	 * @Title:compressImage
	 * @Description:
	 * @param context
	 * @param filePath
	 * @param limitSize
	 * @param quality
	 * @return
	 * @return:String
	 * @throws
	 * @Create: 2014年11月18日 上午11:05:38
	 * @Author : zhm 邮箱：zhaomeng@baihe.com
	 */
	public static String compressImage(Context context, String filePath,
			String savePath, int limitSize, int quality) {
		if (filePath == null || !new File(filePath).exists()) {
			return filePath;
		}
		try {

			int degree = readPictureDegree(filePath);

			int scale = 1;
			Log.v("log", "limitSize:" + limitSize);
			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, option);

			if (option.outWidth > limitSize || option.outHeight > limitSize) {// 1
				scale = (int) Math.pow(
						2.0,
						(int) Math.round(Math.log(limitSize
								/ (double) Math.max(option.outHeight,
										option.outWidth))
								/ Math.log(0.5)));
			}
			option.inJustDecodeBounds = false;
			option.inSampleSize = scale;

			Bitmap bitmap = BitmapFactory.decodeFile(filePath, option);
			Bitmap bm = null;
			if (degree != 0) {
				bm = rotateBitmap(degree, bitmap);
			}
			if (!TextUtils.isEmpty(savePath)) {
				if (bm == null) {
					filePath = saveBitmapToFile(bitmap, savePath, quality);
				} else {
					filePath = saveBitmapToFile(bm, savePath, quality);
				}
			}

			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
			}
			if (bm != null && !bm.isRecycled()) {
				bm.recycle();
			}

		} catch (Exception e) {
			Log.e("log", "", e);
		} catch (OutOfMemoryError e) {
			Log.e("log", "", e);
		}
		return filePath;
	}

	/**
	 * 
	 * @Title:rotateBitmap
	 * @Description: 旋转图片
	 * @param angle
	 * @param bitmap
	 * @return
	 * @return:Bitmap
	 * @throws
	 * @Create: 2014-4-1 下午6:04:01
	 * @Author : zhm 邮箱：zhaomeng@baihe.com
	 */
	public static Bitmap rotateBitmap(int angle, Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return bm;
	}

	/**
	 * 
	 * @Title:readPictureDegree
	 * @Description:读取图片旋转角度
	 * @param imagePath
	 * @return
	 * @return:int
	 * @throws
	 * @Create: 2014-4-1 下午6:05:46
	 * @Author : zhm 邮箱：zhaomeng@baihe.com
	 */
	public static int readPictureDegree(String imagePath) {
		int imageDegree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(imagePath);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				imageDegree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				imageDegree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				imageDegree = 270;
				break;
			}
		} catch (IOException e) {
			Log.e(LOG_TAG, "readPictureDegree", e);
		}
		return imageDegree;
	}

	/**
	 * 
	 * @Title:saveBitmapToFile
	 * @Description:保存图片到文件中
	 * @param filePath
	 * @param bitmap
	 * @param quality
	 * @return:void
	 * @throws
	 * @Create: 2014-4-1 下午6:04:14
	 * @Author : zhm 邮箱：zhaomeng@baihe.com
	 */
	public static String saveBitmapToFile(Bitmap bitmap, String filePath,
			int quality) {
		if (bitmap == null) {
			return null;
		}
		BufferedOutputStream bos = null;
		File file = new File(filePath);
		if (!file.exists()) {
			File parentFile = file.getParentFile();
			if (!parentFile.exists()) {
				file.getParentFile().mkdirs();
			}
		} else {
			file.delete();
		}
		try {
			file.createNewFile();
			bos = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
		} catch (FileNotFoundException e) {
			Log.e(LOG_TAG, "[saveImageFile]", e);
		} catch (IOException e) {
			Log.e(LOG_TAG, "[saveImageFile]", e);
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					Log.e(LOG_TAG, "[saveImageFile]", e);
				}
			}
		}

		return filePath;
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public static void cropImage(Activity activity, String filePath,
			int requestCode) {
		try {
			Uri uri = Uri.fromFile(new File(filePath));
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
			intent.putExtra("crop", "true");
			// aspectX aspectY 是宽高的比例
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			// outputX outputY 是裁剪图片宽高
			intent.putExtra("outputX", 100);
			intent.putExtra("outputY", 100);
			intent.putExtra("return-data", true);
			activity.startActivityForResult(intent, requestCode);
		} catch (Exception e) {
			Toast.makeText(activity, "图片裁剪异常", Toast.LENGTH_SHORT).show();
		}
	}

	public static void saveCropImage(Intent picdata, String filePath) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			saveBitmapToFile(photo, filePath, 80);
		}
	}
}
