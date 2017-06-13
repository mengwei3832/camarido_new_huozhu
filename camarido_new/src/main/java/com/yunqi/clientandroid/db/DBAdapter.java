package com.yunqi.clientandroid.db;

import android.content.Context;

import com.j256.ormlite.android.AndroidDatabaseConnection;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class DBAdapter {

	private final static String LOG_TAG = DBAdapter.class.getSimpleName();

	private static DBAdapter instance;
	protected DBHelper mDbHelper;
	AndroidDatabaseConnection mConnection;
	private Context context;

	protected DBAdapter(Context context) {
		this.context = context;
		mDbHelper = OpenHelperManager.getHelper(context, DBHelper.class);
		mConnection = new AndroidDatabaseConnection(
				mDbHelper.getWritableDatabase(), true);
	}

	public synchronized static DBAdapter instance(Context context) {

		if (instance == null) {
			instance = new DBAdapter(context);
		}
		return instance;
	}

	public void close() {
		if (mDbHelper != null) {
			OpenHelperManager.releaseHelper();
			mConnection.closeQuietly();
			mConnection = null;
			mDbHelper = null;
			instance = null;
		}
	}

	// public int insertBook(UserInfo userInfo) {
	// try {
	// Dao<UserInfo, Long> bookDao = mDbHelper.getUserInfoDao();
	// return bookDao.createOrUpdate(userInfo).getNumLinesChanged();
	// } catch (SQLException e) {
	// // FLog.e(LOG_TAG, e, "[insertMemberInfo]");
	// }
	// return -1;
	// }
	//
	// public int addFavorite(Book book) {
	// int result = -1;
	// Book temp = getBook(book.MaterialID);
	// if (temp != null) {
	// book.isCart = temp.isCart;
	// book.read = temp.read;
	// book.status = temp.status;
	// book.progress = temp.progress;
	// book.downUrl = temp.downUrl;
	// }
	//
	// book.curretUid = PreManager.instance(context).getUid();
	// try {
	// Dao<Book, String> bookDao = mDbHelper.getBookDao();
	// result = bookDao.createOrUpdate(book).getNumLinesChanged();
	// } catch (SQLException e) {
	// FLog.e(LOG_TAG, e, "[insertMemberInfo]");
	// }
	// FLog.v("log", "result result:" + result);
	// return result;
	// }
	//
	// public int deleteFavorite(Book book) {
	// try {
	// Dao<Book, String> bookDao = mDbHelper.getBookDao();
	// UpdateBuilder<Book, String> update = bookDao.updateBuilder();
	//
	// update.updateColumnValue("isFavorite", false).where()
	// .eq("curretUid", PreManager.instance(context).getUid())
	// .and().eq("MaterialID", book.MaterialID);
	// return update.update();
	// } catch (SQLException e) {
	// FLog.e(LOG_TAG, e, "[insertMemberInfo]");
	// }
	// return -1;
	// }
	//
	// public int addCart(Book book) {
	// Book temp = getBook(book.MaterialID);
	//
	// if (temp != null) {
	// book.isFavorite = temp.isFavorite;
	// book.read = temp.read;
	// book.status = temp.status;
	// book.progress = temp.progress;
	// book.downUrl = temp.downUrl;
	// }
	//
	// book.curretUid = PreManager.instance(context).getUid();
	// try {
	// Dao<Book, String> bookDao = mDbHelper.getBookDao();
	// return bookDao.createOrUpdate(book).getNumLinesChanged();
	// } catch (SQLException e) {
	// FLog.e(LOG_TAG, e, "[insertMemberInfo]");
	// }
	// return -1;
	// }

}
