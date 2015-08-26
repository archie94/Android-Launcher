package com.example.launcherx;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class DrawerLongClickListener implements OnItemLongClickListener 
{
	/* This class adds the functionality that on selecting the app for long in application
	 * drawer the app will be put in the home screen*/
	
	
	SlidingDrawer drawerForAdapter;
	RelativeLayout homeViewForAdapter;
	Context mContext;
	MainActivity.Pack packDLCL[];
	
	public DrawerLongClickListener(Context c,SlidingDrawer slidingDrawer,RelativeLayout homeView,MainActivity.Pack pack[])
	{
		mContext=c;
		drawerForAdapter=slidingDrawer;
		homeViewForAdapter=homeView;
		packDLCL=pack;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View item, int pos,long arg3) 
	{
		// TODO Auto-generated method stub
		//in this method we will bring an app to home screen when we long click the app in drawer 
		MainActivity.appLaunchable=false;
		
		LayoutParams lp=new LayoutParams(item.getWidth(),item.getHeight());
		lp.leftMargin=(int)item.getX();
		lp.topMargin=(int)item.getY();
		
		LayoutInflater li=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout ll=(LinearLayout)li.inflate(R.layout.item, null);
		
		
		((ImageView)ll.findViewById(R.id.icon_image)).setImageDrawable(((ImageView)item.findViewById(R.id.icon_image)).getDrawable());
		((TextView)ll.findViewById(R.id.icon_text)).setText(((TextView)item.findViewById(R.id.icon_text)).getText());
		
		//ll.setOnTouchListener(new AppTouchListener(item.getWidth()));//when we drag the app in homescreen
		ll.setOnClickListener(new AppClickListener(mContext,packDLCL));//when we click app in home screen 
		
		
		String data[]=new String[2];
		data[0]=packDLCL[pos].packageName;
		data[1]=packDLCL[pos].name;
		
		ll.setTag(data);
		
		
		homeViewForAdapter.addView(ll,lp);
		drawerForAdapter.animateClose();
		drawerForAdapter.bringToFront();//so that when we click our app drawer the app we put on home screen goes to background
		
		return false;
	}

}
