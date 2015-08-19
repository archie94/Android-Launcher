package com.example.launcherx;

public class SortApps 
{

	public void sort(MainActivity.Pack packs[])
	{
		//simple bubble sort for now 
		int i,j;
		MainActivity.Pack temp;
		
		for(i=0;i<packs.length-1;i++)
		{
			for(j=0;j<packs.length-i-1;j++)
			{
				if(packs[j].label.compareTo(packs[j+1].label)>0)
				{
					temp=packs[j];
					packs[j]=packs[j+1];
					packs[j+1]=temp;
				}
			}
		}
	}
}
