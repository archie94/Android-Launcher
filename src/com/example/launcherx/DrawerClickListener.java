package com.example.launcherx;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class DrawerClickListener implements OnItemClickListener
{
	/*
	 * This class opens up an application when it is clicked in 
	 * the application drawer 
	 */
	Context cDCL;
	MainActivity.Pack packDCL[];
	PackageManager pmDCL;
	
	public DrawerClickListener(Context c,MainActivity.Pack pack[],PackageManager pm)
	{
		cDCL=c;
		packDCL=pack;
		pmDCL=pm;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) 
	{
		if(MainActivity.appLaunchable==true)
		{
			Intent launchApp=pmDCL.getLaunchIntentForPackage(packDCL[pos].name);/*create intent with the package 
			//name of the application which is clicked*/
			cDCL.startActivity(launchApp);//launch the application
		}
	}

}
