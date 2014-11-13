Android-SimpleLayout
====================
Simple way use left top coordination to position android view.  
Use coordination on your design UI and it will transform to what size you want to display or device display size.


## Usage
---
###Programmatically
	SimpleLayout layout = new SimpleLayout(getApplication());
		setContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		ImageView img = new ImageView(getApplication());
		img.setImageResource(R.drawable.ic_launcher);
		SimpleLayout.LayoutParams param = new SimpleLayout.LayoutParams(144, 200);
		param.design_xPx = 300;
		param.design_yPx = 200;
		layout.addView(img, param);
###Xml
	<com.kai.android.simplelayout.SimpleLayout
		    android:id="@+id/view"
		    android:layout_width="300px"
		    android:layout_height="300px"
		    android:background="@android:color/holo_blue_bright"
		    app:design_height="400px"
		    app:design_width="400px"
		    app:display_keepRatio="true" >
	
			<Button
			    android:id="@+id/button1"
			    android:layout_width="wrap_content"
			    android:layout_height="wrap_content"
			    android:layout_alignParentLeft="true"
			    android:layout_alignParentTop="true"
			    app:design_x="100px"
			    app:design_y="100px"
			    android:text="Button" />
	
			<ImageView
			    android:id="@+id/imageView1"
			    android:layout_width="50px"
			    android:layout_height="100px"
			    android:scaleType="fitXY"
			    app:design_x="455px"
			    app:design_y="400px"
			    android:src="@drawable/ic_launcher" />
		   
		</com.kai.android.simplelayout.SimpleLayout>
		
License
-------

    Copyright 2014 Kai Chung

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
