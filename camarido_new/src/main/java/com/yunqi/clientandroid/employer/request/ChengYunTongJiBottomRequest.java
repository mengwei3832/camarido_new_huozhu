package com.yunqi.clientandroid.employer.request;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonStreamerEntity;
import com.yunqi.clientandroid.employer.entity.ChengYunTongJiBottom;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.NullObject;
import com.yunqi.clientandroid.http.response.Response;

public class ChengYunTongJiBottomRequest extends IRequest {

	public ChengYunTongJiBottomRequest(Context context, String packageId,
			String loadTime, String signTime, String vehicleNo,
			int departMentId, int ticketStatus, int todayCount) {
		super(context);

		Log.d("TAG", "--------xu------PackageId------" + packageId);
		Log.d("TAG", "--------xu------loadTime------" + loadTime);
		Log.d("TAG", "--------xu------signTime------" + signTime);
		Log.d("TAG", "--------xu------vehicleNo------" + vehicleNo);
		Log.d("TAG", "--------xu------departMentId------" + departMentId);
		Log.d("TAG", "--------xu------ticketStatus------" + ticketStatus);

		// JsonStreamerEntity entity = new JsonStreamerEntity(null, false);
		//
		// if (Environment.getExternalStorageState().equals(
		// Environment.MEDIA_MOUNTED)) {
		//
		// String path = "/sdcard/crasherb/";
		// File dir = new File(path);
		// if (!dir.exists()) {
		// dir.mkdirs();
		// }
		//
		// for (int i = 0; i < 100; i++) {
		// // 传参
		// try {
		// entity.addPart("PackageId", packageId);
		// entity.addPart("LoadTime", loadTime);
		// entity.addPart("SignTime", signTime);
		// entity.addPart("VehicleNo", vehicleNo);
		// entity.addPart("DepartMentId", departMentId);
		// entity.addPart("TicketStatus", ticketStatus);
		//
		// Log.d("TAG",
		// "----------entity----------" + entity.toString());
		//
		// FileOutputStream fos = new FileOutputStream(path
		// + "ceshi.txt",true);
		//
		// entity.writeTo(fos);
		// // fos.write(entity.toString().getBytes());
		// // fos.close();
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// }

		mParams.put("PackageId", packageId);
		mParams.put("LoadTime", loadTime);
		mParams.put("SignTime", signTime);
		mParams.put("VehicleNo", vehicleNo);
		mParams.put("DepartMentId", departMentId);
		mParams.put("TicketStatus", ticketStatus);
		mParams.put("IsTodayCount", todayCount);
	}

	@Override
	public String getUrl() {
		return getHost() + "pkgapi/Ticket/GetPackageTicketReportForPkg";
	}

	@Override
	public Type getParserType() {
		return new TypeToken<Response<ChengYunTongJiBottom, NullObject>>() {
		}.getType();
	}

}
