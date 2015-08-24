package com.example.launcherx;

//import android.app.ActionBar.LayoutParams;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout.LayoutParams;

public class AppTouchListener implements OnTouchListener
{
	/*
	 *In this class we add drag functionality to our app 
	 *in home screen 
	 */
	int iconSize;
	public AppTouchListener(int size)
	{
		iconSize=size;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
		// TODO Auto-generated method stub
		switch(event.getAction())
		{
		case MotionEvent.ACTION_MOVE:
			LayoutParams lp=new LayoutParams(iconSize,iconSize);
			lp.leftMargin=(int)event.getRawX()-iconSize/2;
			lp.topMargin=(int)event.getRawY()-iconSize/2;
			v.setLayoutParams(lp);
			//break;
		}
		return true;
	}

}
