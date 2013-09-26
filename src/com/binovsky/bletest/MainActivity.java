package com.binovsky.bletest;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity
{
	private final static int	INTENT_ID_REQUEST_ENABLE_BT	= 1;
	private final static int	INTENT_ID_DISCOVERABLE		= 2;
	private final static int	DISCOVERABLE_TIMEINTERVAL	= 999;

	private Handler				mHandler;
	private LeDeviceListAdapter	mLeDeviceListAdapter;
	private final Context		context						= this;

	private BluetoothAdapter	mBluetoothAdapter;
	private Button				btn;

	/**
	 * @return the mBluetoothAdapter
	 */
	public BluetoothAdapter getMBluetoothAdapter( )
	{
		return mBluetoothAdapter;
	}

	/**
	 * @param mBluetoothAdapter
	 *            the mBluetoothAdapter to set
	 */
	public void setMBluetoothAdapter( BluetoothAdapter mBluetoothAdapter )
	{
		this.mBluetoothAdapter = mBluetoothAdapter;
	}

	/**
	 * @return the btn
	 */
	public Button getBtn( )
	{
		return btn;
	}

	/**
	 * @param btn
	 *            the btn to set
	 */
	public void setBtn( Button btn )
	{
		this.btn = btn;
	}

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu )
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater( ).inflate( R.menu.main, menu );
		return true;
	}

	public void runBeacon( View view )
	{
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService( Context.BLUETOOTH_SERVICE );
		setMBluetoothAdapter( bluetoothManager.getAdapter( ) );

		setBtn( (Button) findViewById( R.id.run_beacon ) );

		if ( getBtn( ) == null )
			showFatalAlert( );

		if ( !getMBluetoothAdapter( ).isEnabled( ) )
		{
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE );
			startActivityForResult( enableBtIntent, INTENT_ID_REQUEST_ENABLE_BT );
			btn.setText( R.string.stop_beacon );
		}
		else
		{
			getMBluetoothAdapter( ).stopLeScan( lescanCallback );
			getMBluetoothAdapter( ).disable( );
			btn.setText( R.string.run_beacon );
		}
	}

	protected void onActivityResult( int requestCode, int resultCode,
			Intent data )
	{
		if ( requestCode == INTENT_ID_DISCOVERABLE )
		{
			if ( resultCode == RESULT_CANCELED )
			{
				getMBluetoothAdapter( ).disable( );
				btn.setText( R.string.run_beacon );

				return;
			}
			mHandler = new Handler( );
			mHandler.postDelayed( new Runnable( )
			{
				@Override
				public void run( )
				{
					getMBluetoothAdapter( ).stopLeScan( lescanCallback );
					getMBluetoothAdapter( ).disable( );
					btn.setText( R.string.run_beacon );
				}
			}, DISCOVERABLE_TIMEINTERVAL );

			if ( !getMBluetoothAdapter( ).startLeScan( lescanCallback ) )
			{
				showNoBLEAlert( );
				getMBluetoothAdapter( ).stopLeScan( lescanCallback );
				getMBluetoothAdapter( ).disable( );
				btn.setText( R.string.run_beacon );
			}
		}
		else if ( requestCode == INTENT_ID_REQUEST_ENABLE_BT )
		{
			if ( resultCode == RESULT_CANCELED )
			{
				getMBluetoothAdapter( ).disable( );
				btn.setText( R.string.run_beacon );

				return;
			}

			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE );
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
					DISCOVERABLE_TIMEINTERVAL );
			startActivityForResult( discoverableIntent, INTENT_ID_DISCOVERABLE );
		}
		else if ( resultCode == RESULT_CANCELED )
		{
			getMBluetoothAdapter( ).stopLeScan( lescanCallback );
			btn.setText( R.string.run_beacon );
		}
	}

	private void showFatalAlert( )
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context );

		// set title
		alertDialogBuilder.setTitle( "Fatal Error" );

		// set dialog message
		alertDialogBuilder.setMessage( "Fatal error, application will exit!" )
				.setCancelable( false )
				.setPositiveButton( "OK", new DialogInterface.OnClickListener( )
				{
					public void onClick( DialogInterface dialog, int id )
					{
						MainActivity.this.finish( );
					}
				} );

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create( );

		// show it
		alertDialog.show( );
	}

	private void showNoBLEAlert( )
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context );

		// set title
		alertDialogBuilder.setTitle( "Error" );

		// set dialog message
		alertDialogBuilder
				.setMessage(
						"NO Smart Bluetooth adapter presented in your device!" )
				.setCancelable( false )
				.setPositiveButton( "OK", new DialogInterface.OnClickListener( )
				{
					public void onClick( DialogInterface dialog, int id )
					{
						getMBluetoothAdapter( ).stopLeScan( lescanCallback );
						getMBluetoothAdapter( ).disable( );
						btn.setText( R.string.run_beacon );
					}
				} );

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create( );

		// show it
		alertDialog.show( );
	}

	private LeScanCallback	lescanCallback	= new LeScanCallback( )
											{
												@Override
												public void onLeScan(
														BluetoothDevice device,
														int rssi,
														byte[] scanRecord )
												{
													mLeDeviceListAdapter
															.addDevice( device );
													mLeDeviceListAdapter
															.notifyDataSetChanged( );

													Log.v( context
															.getString( R.string.TAG_DEVICES ),
															device.getName( )
																	+ " "
																	+ rssi );
												}
											};
}
