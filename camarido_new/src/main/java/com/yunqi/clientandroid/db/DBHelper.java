package com.yunqi.clientandroid.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DBHelper extends OrmLiteSqliteOpenHelper {

	private final static String LOG_TAG = DBHelper.class.getSimpleName();

	private static final String DATABASE_NAME = "hui_book.db";
	private static final int DATABASE_VERSION = 1;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		// try {
		// TableUtils.createTable(connectionSource, Book.class);
		// } catch (SQLException e) {
		// Log.e(LOG_TAG, "[onCreate]", e);
		// }
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		// try {
		// Log.v("log", "onUpdate");
		// TableUtils.dropTable(connectionSource, Book.class, true);
		// onCreate(db, connectionSource);
		// } catch (SQLException e) {
		// Log.e(LOG_TAG, "[onUpgrade]", e);
		// }
	}

	// public Dao<UserInfo, Long> getUserInfoDao() throws SQLException {
	// return DaoManager.createDao(connectionSource, UserInfo.class);
	// }

	public void clearTable(Class<?> clazz) {
		try {
			TableUtils.clearTable(connectionSource, clazz);
		} catch (SQLException e) {
			Log.e(LOG_TAG, "[clearTable]", e);
		}

	}
}
