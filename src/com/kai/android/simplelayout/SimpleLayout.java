package com.kai.android.simplelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * Android Simple RelativeLayout
 * Simple way use left top coordination to position android view.  
 * Use coordination on your design UI and it will transform to what size you want to display or device display size.
 * @author "Jones"
 *
 */
public class SimpleLayout extends RelativeLayout {
	
	int mGravity = Gravity.START | Gravity.TOP;
	
	
	DisplayMetrics displayMetrics = null;
	int design_width = 1024;
	int design_height = 768;
	int display_width = design_width;
	int display_height = design_height;
	boolean isDisplayKeepRatio = false;
	
	/** These are used for computing child frames based on their gravity. */
    private final Rect mTmpContainerRect = new Rect();
    private final Rect mTmpChildRect = new Rect();

	public SimpleLayout(Context context) {
		super(context);
		init(context, null);
	}
	
	public SimpleLayout(Context context, boolean isDisplayKeepRatio) {
		super(context);
		init(context, null);
		setDesignWidthHeight(design_width, design_height, isDisplayKeepRatio);
	}
	
	public SimpleLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}
	
	public SimpleLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);

	}
	
	private void init(Context context, AttributeSet attrs) {
		display_width = getWidth();
 		display_height = getHeight();
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SimpleLayout);
		design_width = a.getDimensionPixelSize(R.styleable.SimpleLayout_design_width, design_width);
		design_height = a.getDimensionPixelSize(R.styleable.SimpleLayout_design_height, design_height);
		isDisplayKeepRatio = a.getBoolean(R.styleable.SimpleLayout_display_keepRatio, false);
		a.recycle();
		setDesignWidthHeight(design_width, design_height, isDisplayKeepRatio);
	}
	
	public void setDisplayWidthHeight(int display_width, int display_height) {
		this.display_width = display_width;
		this.display_height = display_height;
	}
	
	public void setDesignWidthHeight(int design_width, int design_height, boolean isScaleDesignAsDisplay) {
		this.design_width = design_width;
		this.design_height = design_height;
		this.isDisplayKeepRatio = isScaleDesignAsDisplay;
		
		if (isDisplayKeepRatio) {
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

     @Override
     protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	 int layoutWidthMeasureSpec = widthMeasureSpec;
         int layoutHeightMeasureSpec = heightMeasureSpec;
         
         int widthMode = MeasureSpec.getMode(widthMeasureSpec);
         int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    	 display_width = MeasureSpec.getSize(widthMeasureSpec);
    	 display_height = MeasureSpec.getSize(heightMeasureSpec);
    	 
    	 if (isDisplayKeepRatio) {
  			setDesignWidthHeight(design_width, design_height, isDisplayKeepRatio);
  			layoutWidthMeasureSpec = MeasureSpec.makeMeasureSpec(display_width, widthMode);
  			layoutHeightMeasureSpec = MeasureSpec.makeMeasureSpec(display_height, heightMode);
    	 }
    	     	 
    	 int childState = 0;
    	 int count = getChildCount();
    	 for (int i = 0; i < count; i++) {
             View child = getChildAt(i);
             if (GONE != child.getVisibility()) {
            	// Measure the child.
            	 child.measure(layoutWidthMeasureSpec, layoutHeightMeasureSpec);
                 childState = combineMeasuredStates(childState, child.getMeasuredState());
             }
    	 }
    	 
    	// Report our final dimensions.
         setMeasuredDimension(resolveSizeAndState(display_width, layoutWidthMeasureSpec, childState),
                 resolveSizeAndState(display_height, layoutHeightMeasureSpec,
                         childState << MEASURED_HEIGHT_STATE_SHIFT));
     }    
     
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    	int layoutWidthMeasureSpec = getMeasuredWidth();
    	int layoutHeightMeasureSpec = getMeasuredHeight();
    	
         // These are the far left and right edges in which we are performing layout.
         int layoutLeftPos = getPaddingLeft();
         int layoutRightPos = right - left - getPaddingRight();
         // These are the top and bottom edges in which we are performing layout.
         int layoutTopPos = getPaddingTop();
         int layoutBottomPos = bottom - top - getPaddingBottom();
        
         int count = getChildCount();
         for (int i = 0; i < count; i += 1) {
             final View child = getChildAt(i);
             if (GONE != child.getVisibility()) {
            	 int childOldWidthMeasureSpec = child.getMeasuredWidth();
                 int childOldHeightMeasureSpec = child.getMeasuredHeight();
                 
                 LayoutParams lp = (LayoutParams) child.getLayoutParams();
//                 LogUtil.d("child id:"+child.getId()+",lp width:"+lp.width+",height:"+lp.height);
                 int width = d2pScaleX(lp.width);
                 int height = d2pScaleY(lp.height);
                 if (LayoutParams.WRAP_CONTENT == lp.width) {
                     width = childOldWidthMeasureSpec;
                 } else if (LayoutParams.MATCH_PARENT == lp.width) {
                	 width = display_width; // if child layout is not position on origin, layout may out of container
                 } 
                 if (LayoutParams.WRAP_CONTENT == lp.height) {
                	 height = childOldHeightMeasureSpec;
                 } else if (LayoutParams.MATCH_PARENT == lp.height) {
                	 height = display_height; // if child layout is not position on origin, layout may out of container
                 }
                 if (child instanceof SimpleLayout) {
                	 SimpleLayout sc = (SimpleLayout) child;
                	 if (sc.isDisplayKeepRatio) {
	                	 double scale = Math.min(getD2pScaleX(), getD2pScaleY());
	                	 width = (int) (lp.width*scale);
	                	 height = (int) (lp.height*scale);
                	 }
                 }
                 
                 // Compute the frame in which we are placing this child.
                 int shiftX = d2pScaleX(lp.design_xPx); // default is Gravity.LEFT
                 int shiftY = d2pScaleY(lp.design_yPx); // default is Gravity.TOP
                 int gravity = lp.layout_gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
                 if (Gravity.CENTER_HORIZONTAL == gravity) {
                	 shiftX = (layoutWidthMeasureSpec - width) / 2;
                 }
                 gravity = lp.layout_gravity & Gravity.VERTICAL_GRAVITY_MASK;
                 if (Gravity.CENTER_VERTICAL == gravity) {
                	 shiftY = (layoutHeightMeasureSpec - height) / 2;
                 }
                 
                 mTmpContainerRect.left = layoutLeftPos + shiftX;
                 int tmpRight = mTmpContainerRect.left + width;
                 mTmpContainerRect.right = tmpRight > layoutRightPos ? layoutRightPos : tmpRight;
                 mTmpContainerRect.top = layoutTopPos + shiftY;
                 int tmpBottom = mTmpContainerRect.top + height;
                 mTmpContainerRect.bottom = tmpBottom > layoutBottomPos ? layoutBottomPos : tmpBottom;

                 // if not use specific width or height, fix child layout not out of it's container 
                 if (LayoutParams.MATCH_PARENT == lp.width) {
                	 width = mTmpContainerRect.right - mTmpContainerRect.left;
                 }
                 if (LayoutParams.MATCH_PARENT == lp.height) {
                	 height = mTmpContainerRect.bottom - mTmpContainerRect.top;
                 }
                 
                 // Use the child's gravity and size to determine its final frame within its container.
                 Gravity.apply(lp.layout_gravity, width, height, mTmpContainerRect, mTmpChildRect);
       
                 
                 // XXX If view width or height no change, not need to measure again. If still measure again,
                 // this will be slower. And if view is RelativeLayout, this will cause attribute "gravity" no use
                 // but view is LinearLayout won't. (still need to understand...)
                 if ((width != childOldWidthMeasureSpec) || (height != childOldHeightMeasureSpec)) {
                	// Measure the child.
                     int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST);
                     int heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
                	 child.measure(widthMeasureSpec, heightMeasureSpec);
                 }
                 
                 LogUtil.d("child layout position:"+mTmpChildRect.toString());
                 // Place the child.
                 child.layout(mTmpChildRect.left, mTmpChildRect.top, mTmpChildRect.right, mTmpChildRect.bottom);

             }
         }
 	}
	
	@Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new SimpleLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }
	
	public static class LayoutParams extends RelativeLayout.LayoutParams {
		/**
         * The gravity to apply with the View to which these layout parameters
         * are associated.
         */
        public int layout_gravity = Gravity.START | Gravity.TOP;
        
		public int design_xPx = 0;
		public int design_yPx = 0;

		public LayoutParams(int width, int height) {
			super(width, height);
		}
		
		public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
		
		public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.SimpleLayoutLP);
            layout_gravity = a.getInt(R.styleable.SimpleLayoutLP_android_layout_gravity, layout_gravity);
            design_xPx = a.getDimensionPixelSize(R.styleable.SimpleLayoutLP_design_x, design_xPx);
            design_yPx = a.getDimensionPixelSize(R.styleable.SimpleLayoutLP_design_y, design_yPx);
            a.recycle();
		}
		
	}
	
	/**
	 * It's for getGravity() android 2.2 compatibility
	 * @param context
	 * @return
	 */
	private int getScreenRotationGravity(Context context) {
		int gravity = Gravity.LEFT | Gravity.TOP;
	    final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	    switch (display.getRotation()) {
	        case Surface.ROTATION_0:
	        	LogUtil.i("SCREEN_ORIENTATION_REVERSE_PORTRAIT:ROTATION_0");
	        	gravity = Gravity.LEFT | Gravity.TOP;
	            break;

	        case Surface.ROTATION_90:
	        	LogUtil.i("SCREEN_ORIENTATION_LANDSCAPE:ROTATION_90");
	        	gravity = Gravity.LEFT | Gravity.TOP;
	            break;

	        case Surface.ROTATION_180:
	        	LogUtil.i("SCREEN_ORIENTATION_REVERSE_PORTRAIT:ROTATION_180");
	        	gravity = Gravity.RIGHT | Gravity.BOTTOM;
	            break;

	        case Surface.ROTATION_270:
	        	LogUtil.i("SCREEN_ORIENTATION_REVERSE_LANDSCAPE:ROTATION_270");
	        	gravity = Gravity.RIGHT | Gravity.BOTTOM;
	            break;
	    }
	    
	    return gravity;
	}
}
