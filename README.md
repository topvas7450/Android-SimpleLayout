Android-SimpleLayout
====================
Simple way use left top coordination to position android view.  
Use coordination on your design UI and it will transform to what size you want to display or device display size.


## Usage

	SimpleLayout layout = new SimpleLayout(getApplication());
	setContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
	ImageView img = new ImageView(getApplication());
	img.setImageResource(R.drawable.ic_launcher);
	layout.addView(img, 200, 200, 30, 30, 0, 0);
