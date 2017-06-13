package com.yunqi.clientandroid.http.response;

import java.util.ArrayList;

public class Response<T, E> {

	public int totalCount;
	public T singleData;
	public ArrayList<E> data;
	public String[] enumFields;
	public boolean isSuccess;
	public String message;
	public int ErrCode;

	// TotalCount": 1,
	// "Data": [
	// {
	// "TokenValue": "64753a80-aa0c-41ee-9b4d-2df041256bf3",
	// "TokenExpires": "2015-11-19T00:04:41.035409+08:00"
	// },
	// ],
	// "SingleData": {
	// "TokenValue": "64753a80-aa0c-41ee-9b4d-2df041256bf3",
	// "TokenExpires": "2015-11-19T00:04:41.035409+08:00"
	// },
	// "EnumFields": null,
	// "IsSuccess": true,
	// "Message": "sample string 3"
}
