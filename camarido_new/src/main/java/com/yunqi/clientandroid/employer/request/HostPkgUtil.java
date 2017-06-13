package com.yunqi.clientandroid.employer.request;

public class HostPkgUtil {

	public static HostType type = HostType.DEBUG_HOST;
//	 public static HostType type = HostType.PUBLIC_HOST;
//	 public static HostType type = HostType.DEMO_HOST;

	private static final String HOST_DEBUG = "http://qa.pkgapi.yqtms.com/";
	private static final String HOST_PUBLIC = "http://pkgapi.yqtms.com/";
	private static final String HOST_DEMO = "http://demo.pkgapi.yqtms.com/";

	private static final String WEB_HOST_DEBUG = "http:/qa.yqtms.com/";
	private static final String WEB_HOST_PUBLIC = "http://yqtms.com/";
	private static final String WEB_HOST_DEMO = "http://demo.yqtms.com/";

	public static String getWebHost() {

		switch (type) {
		case DEBUG_HOST:
			return WEB_HOST_DEBUG;
		case PUBLIC_HOST:
			return WEB_HOST_PUBLIC;
		case DEMO_HOST:
			return WEB_HOST_DEMO;
		default:
			break;
		}
		return null;
	}

	public static String getApiHost() {

		switch (type) {
		case DEBUG_HOST:
			return HOST_DEBUG;
		case PUBLIC_HOST:
			return HOST_PUBLIC;
		case DEMO_HOST:
			return HOST_DEMO;

		default:
			break;
		}
		return null;
	}

	public enum HostType {
		DEBUG_HOST, PUBLIC_HOST, DEMO_HOST
	}
}
