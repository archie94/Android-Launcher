package com.example.launcherx;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerAdapter extends BaseAdapter
{
	Context cDA;
	MainActivity.Pack packsDA[];
	public DrawerAdapter(Context c,MainActivity.Pack packs[])
	{
		cDA=c;
		packsDA=packs;
	}

	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return packsDA.length;
	}

	@Override
	public Object getItem(int arg0) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	
	static class ViewHolder // to make scroll in list view smooth
	{
		TextView text;
		ImageView icon;
	}
	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) 
	{
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		LayoutInflater li=(LayoutInflater)cDA.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if(convertView==null)
		{
			convertView=li.inflate(R.layout.item, null);
			
			viewHolder=new ViewHolder();
			viewHolder.text=(TextView)convertView.findViewById(R.id.icon_text);
			viewHolder.icon=(ImageView)convertView.findViewById(R.id.icon_image);
			
			convertView.setTag(viewHolder);
		}
		else
			viewHolder=(ViewHolder)convertView.getTag();
		
		
		viewHolder.text.setText(packsDA[pos].label);
		viewHolder.icon.setImageDrawable(packsDA[pos].icon);
		return convertView;
	}

}
