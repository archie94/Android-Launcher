package com.example.launcherx;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity 
{
	GridView drawGrid;
	DrawerAdapter drawerObj;
	SlidingDrawer slidingDrawer;
	RelativeLayout homeView;
	class Pack
	{
		Drawable icon;
		String name,label;
	}
	Pack packs[];
	PackageManager pm;/*PackageManager class is used for retrieving informations related to the application 
	packages currently installed on the device*/
	
	static boolean appLaunchable=true;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawGrid=(GridView)findViewById(R.id.content);
        slidingDrawer=(SlidingDrawer)findViewById(R.id.drawer);
        homeView=(RelativeLayout)findViewById(R.id.home_view);
        pm=getPackageManager();//retrieve information /*Return PackageManager instance to find global package information.*/
        set_packs();
        
        //when we open drawer we want to launch app 
        slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener()
        {

			@Override
			public void onDrawerOpened() 
			{
				// TODO Auto-generated method stub
				appLaunchable=true;
				
			}
        	
        });
        
        //Intent filter is needed to add attributes to the actions we want to detect
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);//since we want our app drawer to refresh if a new app is added 
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);//if app is removed
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);//if app is modified 
        filter.addDataScheme("package");//need to know why this is required 
        registerReceiver(new PacReceiver(), filter);
        
    }

    public void set_packs()
    {
    	final Intent mainIntent = new Intent(Intent.ACTION_MAIN,null);
    	mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    	List<ResolveInfo> packsList=pm.queryIntentActivities(mainIntent, 0);
    	packs=new Pack[packsList.size()];
    	for(int i=0;i<packsList.size();i++)
    	{
    		packs[i]=new Pack();
    		packs[i].icon=packsList.get(i).loadIcon(pm);
    		packs[i].label=packsList.get(i).loadLabel(pm).toString();
    		packs[i].name=packsList.get(i).activityInfo.packageName;
    		
    	}
    	new SortApps().sort(packs);
    	
    	
    	drawerObj=new DrawerAdapter(this,packs);
        drawGrid.setAdapter(drawerObj);
        drawGrid.setOnItemClickListener(new DrawerClickListener(this,packs,pm));
        drawGrid.setOnItemLongClickListener(new DrawerLongClickListener(this,slidingDrawer,homeView));
    }
    
    
    //detect if an application is added or deleted 
    public class PacReceiver extends BroadcastReceiver
    {

		@Override
		public void onReceive(Context arg0, Intent arg1) 
		{
			// TODO Auto-generated method stub
			set_packs();//when onReceive() is called we ask for sorting which refreshes our app drawer 
		}
    	
    }

}
