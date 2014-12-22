package com.kai.android.simplelayout;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// programmatically
		/*
		SimpleLayout layout = new SimpleLayout(getApplication());
		setContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		ImageView img = new ImageView(getApplication());
		img.setImageResource(R.drawable.ic_launcher);
		SimpleLayout.LayoutParams param = new SimpleLayout.LayoutParams(144, 200);
		param.design_xPx = 300;
		param.design_yPx = 200;
		layout.addView(img, param);
		*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
