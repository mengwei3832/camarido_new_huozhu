package com.yunqi.clientandroid.http;

import org.apache.http.Header;

import android.content.Context;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.LongSerializationPolicy;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonStreamerEntity;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yunqi.clientandroid.BuildConfig;
import com.yunqi.clientandroid.employer.util.SpManager;
import com.yunqi.clientandroid.http.request.IRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CacheUtils;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.PreManager;

public class AsyncHttp {

	private AsyncHttpClient mHttpClient = new AsyncHttpClient();
	private Gson mGson = null;

	public AsyncHttp() {
		mHttpClient.setTimeout(60 * 1000);
		mHttpClient.setConnectTimeout(60 * 1000);

		mGson = new GsonBuilder()
				.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
				.setLongSerializationPolicy(LongSerializationPolicy.DEFAULT)
				.create();
	}

	public void get(IRequest request, IHttpListener listener) {
		if (request != null) {
			addHeader(request.getContext());
			request.getParams().setUseJsonStreamer(true);
			mHttpClient.get(request.getUrl(), new HttpResponse(request,
					listener));
		} else {
			if (listener != null) {
				listener.onFailure(0, 404, new Exception("request is null"));
			}
		}
	}

	public void post(IRequest request, IHttpListener listener) {
		if (request != null) {
			addHeader(request.getContext());
			// RequestParams rp = request.getParams();
			// rp.setUseJsonStreamer(true);
			// rp
			if (!request.isUseFormRequest()) {
				request.getParams().setUseJsonStreamer(true);
			}
			mHttpClient.post(request.getContext(), request.getUrl(),
					request.getParams(), new HttpResponse(request, listener));
		} else {
			if (listener != null) {
				listener.onFailure(0, 404, new Exception("request is null"));
			}
		}

	}

	public void postJson(final IRequest request, final IHttpListener listener) {
		if (request != null) {
			addHeader(request.getContext());
			request.getParams().setUseJsonStreamer(true);
			mHttpClient.post(request.getContext(), request.getUrl(),
					request.getParams(), new HttpResponse(request, listener));
		} else {
			if (listener != null) {
				listener.onFailure(0, 404, new Exception("request is null"));
			}
		}

	}

	private void addHeader(Context context) {
		String token = PreManager.instance(context).getToken();
		
		L.e("----------当前Token-----------"+token);
		
		if (token != null) {
			mHttpClient.addHeader("YQ_API_TOKEN", token);
		}
	}

	public void cancelPost() {
		// client.cancelRequests(mContext, true);
		// client.cancelRequests(context, mayInterruptIfRunning);
	}

	public interface IHttpListener {
		void onStart(int requestId);

		void onSuccess(int requestId, Response response);

		void onFailure(int requestId, int httpStatus, Throwable error);

	}

	class HttpResponse extends TextHttpResponseHandler {

		private IHttpListener mHttpListener;
		private IRequest mRequest;

		public HttpResponse(IRequest request, IHttpListener httpListener) {
			mHttpListener = httpListener;
			mRequest = request;
		}

		@Override
		public void onStart() {
			if (mHttpListener != null) {
				mHttpListener.onStart(mRequest.getRequestId());
			}
		}

		@Override
		public void onFailure(int code, Header[] arg1, String arg2,
				Throwable arg3) {
			Log.e("log", "[onFailure]", arg3);
			if (mHttpListener != null) {
				mHttpListener.onFailure(mRequest.getRequestId(), code, arg3);
			}
		}

		@Override
		public void onSuccess(int code, Header[] headers, String content) {
			if (BuildConfig.DEBUG) {
				Log.v("log", "http url:" + mRequest.getUrl());
				Log.v("log", "http content:" + content);
			}
			
//			headers.

			if (mHttpListener != null && content != null) {
				try {
					mHttpListener.onSuccess(
							mRequest.getRequestId(),
							(Response) mGson.fromJson(content,
									mRequest.getParserType()));

				} catch (JsonSyntaxException e) {
					Log.v("TAG", e.toString());
					onFailure(code, headers, content, e);
				}
			}
		}
	}

}
