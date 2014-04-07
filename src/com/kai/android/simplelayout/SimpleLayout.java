package com.kai.android.simplelayout;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.RelativeLayout;

/**
 * Android Simple RelativeLayout
 * Simple way use left top coordination to position android view.  
 * Use coordination on your design UI and it will transform to what size you want to display or device display size.
 * @author "Jones"
 *
 */
public class SimpleLayout extends RelativeLayout {
	
	DisplayMetrics displayMetrics = null;
	int design_width = 1024;
	int design_height = 768;
	int display_width = 0;
	int display_height = 0;

	public SimpleLayout(Context context) {
		super(context);
		displayMetrics = context.getResources().getDisplayMetrics();
		design_width = display_width = displayMetrics.widthPixels;
		design_height = display_height = displayMetrics.heightPixels;
	}
	
	public void setDisplayWidthHeight(int display_width, int display_height) {
		this.display_width = display_width;
		this.display_height = display_height;
	}
	
	public void setDesignWidthHeight(int design_width, int design_height, boolean isScaleDesignAsDisplay) {
		this.design_width = design_width;
		this.design_height = design_height;
		
		if (isScaleDesignAsDisplay) {
			double scale = Math.min(getD2pScaleX(), getD2pScaleY());
			this.display_width = (int)(design_width*scale);
			this.display_height = (int)(design_height*scale);
		}
	}

	public int getDisplayWidth() {
		return display_width;
	}

	public int getDisplayHeight() {
		return display_height;
	}
	
	public int getDesignWidth() {
		return design_width;
	}

	public int getDesignHeight() {
		return design_height;
	}

	/**
	 * get design x to display x scale
	 * @return
	 */
	public double getD2pScaleX() {
		return ((double)display_width/design_width);
	}
	
	/**
	 * get design y to display y scale
	 * @return
	 */
	public double getD2pScaleY() {
		return ((double)display_height/design_height);
	}
	
	/**
	 * get display x to design x scale
	 * @return
	 */
	public double getP2dScaleX() {
		return ((double)design_width/display_width);
	}
	
	/**
	 * get display y to design y scale
	 * @return
	 */
	public double getP2dScaleY() {
		return ((double)design_height/display_height);
	}
	
	/**
	 * get design x to display x
	 * @param xPixel
	 * @return
	 */
	public int d2pScaleX(int xPixel) {
		return (int) (xPixel*getD2pScaleX());
	}
	
	/**
	 * get design y to display y
	 * @param yPixel
	 * @return
	 */
	public int d2pScaleY(int yPixel) {
		return  (int) (yPixel*getD2pScaleY());
	}
	
	/**
	 * get display x to design x
	 * @param xPixel
	 * @return
	 */
	public int p2dScaleX(int xPixel) {
		return (int) (xPixel*getP2dScaleX());
	}
	
	/**
	 * get display y to design y
	 * @param yPixel
	 * @return
	 */
	public int p2dScaleY(int yPixel) {
		return  (int) (yPixel*getP2dScaleY());
	}

	public void addView(View child, int viewWidthPixel, int viewHeightPixel, int left, int top, int right, int bottom) {
		addView(child, -1, viewWidthPixel, viewHeightPixel, left, top, right, bottom);
	}
	
	public void addView(View child, int index, int viewWidthPixel, int viewHeightPixel, int left, int top, int right, int bottom) {
		update(child, viewWidthPixel, viewHeightPixel, left, top, right, bottom);
		
		addView(child, index);
	}
	
	void update(View child, int viewWidthPixel, int viewHeightPixel, int left, int top, int right, int bottom) {
		ViewGroup.LayoutParams params = child.getLayoutParams();
		if (params == null)
			params = generateDefaultLayoutParams();
		
		if (params instanceof RelativeLayout.LayoutParams) {
			RelativeLayout.LayoutParams rLayoutParams = (RelativeLayout.LayoutParams) params;
			rLayoutParams.alignWithParent = true;
			rLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
			rLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
			double scale = Math.min(getD2pScaleX(), getD2pScaleY());
			if (viewWidthPixel != -1)
				rLayoutParams.width = (int)(scale*viewWidthPixel);
			if (viewHeightPixel != -1)
				rLayoutParams.height = (int)(scale*viewHeightPixel);

			rLayoutParams.setMargins(d2pScaleX(left),
					d2pScaleY(top),
					d2pScaleX(right),
					d2pScaleY(bottom));
			child.setLayoutParams(rLayoutParams);
		} else {
			LogUtil.e("params is not instanceof RelativeLayout.LayoutParams!!");
		}
	}
	
}
