package com.example.launcherx;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.GridView;

public class MainActivity extends Activity 
{
	GridView drawGrid;
	DrawerAdapter drawerObj;
	class Pack
	{
		Drawable icon;
		String name,label;
	}
	Pack packs[];
	PackageManager pm;/*PackageManager class is used for retrieving informations related to the application 
	packages currently installed on the device*/
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawGrid=(GridView)findViewById(R.id.content);
        pm=getPackageManager();//retrieve information /*Return PackageManager instance to find global package information.*/
        set_packs();
        drawerObj=new DrawerAdapter(this,packs);
        drawGrid.setAdapter(drawerObj);
        drawGrid.setOnItemClickListener(new DrawerClickListener(this,packs,pm));
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
    }


}
