package com.example.launcherx;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
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
	
	AppWidgetManager appWidgetManager;
	AppWidgetHost appWidgetHost;
	int REQUEST_CREATE_APPWIDGET=900;//random 
	
	
	class Pack
	{
		Drawable icon;
		String name,label,packageName;
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
        
        appWidgetManager=AppWidgetManager.getInstance(this);
        appWidgetHost=new AppWidgetHost(this,R.id.APP_WIDGET_HOST_ID);
        
        
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
        
        //On long pressing our homeView we want to open widget list
        homeView.setOnLongClickListener(new OnLongClickListener()
        {

			@Override
			public boolean onLongClick(View v) 
			{
				// TODO Auto-generated method stub
				selectWidget();
				return false;
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
    		packs[i].name=packsList.get(i).activityInfo.name;
    		packs[i].packageName=packsList.get(i).activityInfo.packageName;
    		
    	}
    	new SortApps().sort(packs);
    	
    	
    	drawerObj=new DrawerAdapter(this,packs);
        drawGrid.setAdapter(drawerObj);
        drawGrid.setOnItemClickListener(new DrawerClickListener(this,packs,pm));
        drawGrid.setOnItemLongClickListener(new DrawerLongClickListener(this,slidingDrawer,homeView,packs));
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
    
    //--------------------------------METHODS FOR WIDGET---------------------------------------------------------------------
    
    void selectWidget() 
    {
        int appWidgetId = this.appWidgetHost.allocateAppWidgetId();//allocate resources for a widget instance 
        
        //this activity will allow us to select which widget we want to add 
        Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        addEmptyData(pickIntent);
        startActivityForResult(pickIntent, R.id.REQUEST_PICK_APPWIDGET);
    }
    void addEmptyData(Intent pickIntent) 
    {
    	/*The Widget API supports that you merge custom widgets of your application with the installed ones. 
    	 * But if you don’t add anything, the Activity that shows the list of widgets to the user crashes 
    	 * with a NullPointerException. The addEmptyData() method above adds some dummy data to avoid this bug.
    	 */
        ArrayList customInfo = new ArrayList();
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo);
        ArrayList customExtras = new ArrayList();
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras);
    };
    
    
    /*If the user successfully selects a widget from the list (he didn’t pressed “back”), 
     * it will return an OK to you as an activity result. The data for this result contains the widget ID. 
     * Use it to retrieve the AppWidgetProviderInfo to check if the widget requires any configuration (some widgets does need). 
     * If it requires, you need to launch the activity to configure the widget.(non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
    */
    
    
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
        if (resultCode == RESULT_OK ) 
        {
            if (requestCode == R.id.REQUEST_PICK_APPWIDGET) 
            {
                configureWidget(data);
            }
            else if (requestCode == REQUEST_CREATE_APPWIDGET) 
            {
                createWidget(data);
            }
        }
        else if (resultCode == RESULT_CANCELED && data != null) 
        {
            int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
            if (appWidgetId != -1) 
            {
                appWidgetHost.deleteAppWidgetId(appWidgetId);
            }
        }
    }

    private void configureWidget(Intent data) 
    {
        Bundle extras = data.getExtras();
        int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo appWidgetInfo = appWidgetManager.getAppWidgetInfo(appWidgetId);
        if (appWidgetInfo.configure != null) 
        {
            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
            intent.setComponent(appWidgetInfo.configure);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            startActivityForResult(intent, REQUEST_CREATE_APPWIDGET);
        } 
        else 
        {
            createWidget(data);
        }
    }
    
    
    public void createWidget(Intent data) 
    {
        Bundle extras = data.getExtras();
        int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo appWidgetInfo = appWidgetManager.getAppWidgetInfo(appWidgetId);
        AppWidgetHostView hostView = appWidgetHost.createView(this, appWidgetId, appWidgetInfo);
        hostView.setAppWidget(appWidgetId, appWidgetInfo);
        homeView.addView(hostView);
    }
    
    /*The widget should be working now. But if you want to remove the widget, 
     * you need to ask to the AppWidgetHost to release it. If you do not release it, 
     * you’ll get a memory leak (your app will consume unnecessary memory). 
     * Finally, remove it from your LayoutView.
     */
    @Override
    protected void onStart() 
    {
        super.onStart();
        appWidgetHost.startListening();
    }
    @Override
    protected void onStop() 
    {
        super.onStop();
        appWidgetHost.stopListening();
    }
    
    /*The widget should be working now. But if you want to remove the widget, 
     * you need to ask to the AppWidgetHost to release it. If you do not release it, 
     * you’ll get a memory leak (your app will consume unnecessary memory). Finally, 
     * remove it from your LayoutView.
     */
    public void removeWidget(AppWidgetHostView hostView) 
    {
        appWidgetHost.deleteAppWidgetId(hostView.getAppWidgetId());
        homeView.removeView(hostView);
    }
    /*Note that the widget ID 
     * is also deleted during the onActivityResult() method 
     * if the user gave up selecting the widget.
     */

}
