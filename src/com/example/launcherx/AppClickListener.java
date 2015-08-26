package com.example.launcherx;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class AppClickListener implements OnClickListener 
{

	/*this class will be responsible for launching 
	 * our app which is in our home screen
	 */
	Context cACL;
	MainActivity.Pack packACL[];
	
	public AppClickListener(Context c,MainActivity.Pack pack[])
	{
		cACL=c;
		packACL=pack;
	}
	
	
	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		String data[];
		data=(String[])v.getTag();
		MainActivity.appLaunchable=true;
		if(MainActivity.appLaunchable==true)
		{
			//Intent launchApp=pmDCL.getLaunchIntentForPackage(packDCL[pos].name);/*create intent with the package
			Intent launchApp=new Intent(Intent.ACTION_MAIN);
			launchApp.addCategory(Intent.CATEGORY_LAUNCHER);//launch app in foreground 
			//name of the application which is clicked*/
			ComponentName cp=new ComponentName(data[0],data[1]);
			launchApp.setComponent(cp);
			cACL.startActivity(launchApp);//launch the application
		}
	}

}
