/**
 * 
 */
package com.binovsky.bletest;

import java.util.ArrayList;

import android.bluetooth.BluetoothDevice;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author mbinovsky
 *
 */
public class LeDeviceListAdapter extends BaseAdapter
{
	private ArrayList<BluetoothDevice> mDevices;

	/**
	 * 
	 */
	public LeDeviceListAdapter( )
	{
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the mDevices
	 */
	public ArrayList<BluetoothDevice> getMDevices( )
	{
		return mDevices;
	}

	/**
	 * @param mDevices the mDevices to set
	 */
	public void setMDevices( ArrayList<BluetoothDevice> mDevices )
	{
		this.mDevices = mDevices;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount( )
	{
		return getMDevices( ).size( );
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem( int position )
	{
		return getMDevices( ).get( position );
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId( int position )
	{
		return -1;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView( int position, View convertView, ViewGroup parent )
	{
		return null;
	}

	public void addDevice( BluetoothDevice device )
	{
		getMDevices( ).add( device );
	}
}
