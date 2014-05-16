package com.kai.android.simplelayout;


import java.security.spec.MGF1ParameterSpec;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
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
	
	int mGravity, cGravity = Gravity.START | Gravity.TOP;
	
	DisplayMetrics displayMetrics = null;
	int design_width = 1024;
	int design_height = 768;
	int display_width = 0;
	int display_height = 0;
	boolean display_keepRatio = false;
	
	/** These are used for computing child frames based on their gravity. */
    private final Rect mTmpContainerRect = new Rect();
    private final Rect mTmpChildRect = new Rect();

	public SimpleLayout(Context context) {
		super(context);
		init(context, null);
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
		mGravity = getGravity();
		displayMetrics = context.getResources().getDisplayMetrics();
		display_width = displayMetrics.widthPixels;
		display_height = displayMetrics.heightPixels;
		
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SimpleLayout);
		design_width = a.getDimensionPixelSize(R.styleable.SimpleLayout_design_width, 0);
		design_height = a.getDimensionPixelSize(R.styleable.SimpleLayout_design_height, 0);
		display_keepRatio = a.getBoolean(R.styleable.SimpleLayout_display_keepRatio, false);
		a.recycle();
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

     @Override
     protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
          super.onMeasure(widthMeasureSpec, heightMeasureSpec);
          LogUtil.d("widthMeasureSpec:"+widthMeasureSpec+", heightMeasureSpec:"+heightMeasureSpec);
          LogUtil.d("layout MeasuredWidth:"+getMeasuredWidth()+", MeasuredHeight:"+getMeasuredHeight());
     }    
     
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		display_width = getWidth();
		display_height = getHeight();
		if (design_width == 0 && design_height == 0) { //if design size not set default will be as display size.
			design_width = display_width;
			design_height = display_height; 
		}
		
		if (mGravity != cGravity) {
			super.onLayout(changed, left, top, right, bottom);
			
			return;
		}

		if (changed && display_keepRatio) {
			double scale = Math.min(getD2pScaleX(), getD2pScaleY());
			display_width = (int)(design_width*scale);
			display_height = (int)(design_height*scale);
			int r = left + display_width;
			int b = top + display_height;

			layout(left, top, r, b);
		}
		
        // These are the far left and right edges in which we are performing layout.
        int leftPos = getPaddingLeft();
        int rightPos = right - left - getPaddingRight();


        // These are the top and bottom edges in which we are performing layout.
        final int parentTop = getPaddingTop();
        final int parentBottom = bottom - top - getPaddingBottom();
        
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();

                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                if (design_width < width || design_height < height) {
                	// TODO fix if child size set "match_parent" or "fill_parent"
                	LogUtil.e("watch out! parent's design size should not smaller than child's design size!");
                }

                // Compute the frame in which we are placing this child.
                mTmpContainerRect.left = leftPos + d2pScaleX(lp.design_xPx);
                mTmpContainerRect.right = leftPos + d2pScaleX(width);
                mTmpContainerRect.top = parentTop + d2pScaleY(lp.design_yPx);
                mTmpContainerRect.bottom = parentBottom;

                // Use the child's gravity and size to determine its final
                // frame within its container.
                Gravity.apply(lp.gravity, d2pScaleX(width), d2pScaleY(height), mTmpContainerRect, mTmpChildRect);


                // Place the child.
                child.layout(mTmpChildRect.left, mTmpChildRect.top,
                        mTmpChildRect.right, mTmpChildRect.bottom);
                
                
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
        public int gravity = Gravity.TOP | Gravity.START;
        
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
            design_xPx = a.getDimensionPixelSize(R.styleable.SimpleLayoutLP_design_x, 0);
            design_yPx = a.getDimensionPixelSize(R.styleable.SimpleLayoutLP_design_y, 0);
            a.recycle();
		}
	}
}
