package com.kai.android.simplelayout;

import android.util.Log;

/**
 * 
 * @author jones
 * 
 */
public class LogUtil {
	/**
	 * release時記得關閉isDebug, isSave2Locale
	 */
	public static boolean isDebug = true;
	

	public static void i(final String TAG, String msg) {
		if(isDebug) {
			String newTag = TAG+"("+new Throwable().getStackTrace()[1].getLineNumber()+")";
			Log.i(newTag, msg);
		}
	}
	
	public static void i(String msg) {
		if(isDebug) {
			Throwable t = new Throwable();
			String newTag = t.getStackTrace()[1].getFileName() + "("+t.getStackTrace()[1].getLineNumber()+")";
			Log.i(newTag, msg);
		}
	}
	
	public static void e(final String TAG, String msg) {
		if(isDebug) {
			String newTag = TAG+"("+new Throwable().getStackTrace()[1].getLineNumber()+")";
			Log.e(newTag, msg);
		}
	}
	
	public static void e(String msg) {
		if(isDebug) {
			Throwable t = new Throwable();
			String newTag = t.getStackTrace()[1].getFileName() + "("+t.getStackTrace()[1].getLineNumber()+")";
			Log.e(newTag, msg);
		}
	}
	
	public static void d(final String TAG, String msg) {
		if(isDebug) {
			String newTag = TAG+"("+new Throwable().getStackTrace()[1].getLineNumber()+")";
			Log.d(newTag, msg);
		}
	}
	
	public static void d(String msg) {
		if(isDebug) {
			Throwable t = new Throwable();
			String newTag = t.getStackTrace()[1].getFileName() + "("+t.getStackTrace()[1].getLineNumber()+")";
			Log.d(newTag, msg);
		}
	}
	
}
