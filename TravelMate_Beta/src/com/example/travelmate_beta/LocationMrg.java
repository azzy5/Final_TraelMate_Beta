package com.example.travelmate_beta;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class LocationMrg extends Service {

	private final Context mContext;
	private boolean isGPSEnabled = false;
	private boolean isNetworkEnabled = false;
	private boolean isLocAvailable = false;
	private Location loc = null;
	boolean isWifiConn = false;
	boolean isMobileConn = false;
	private static final long UPDATE_DISTANCE = 10;
	private static final long UPDATE_TIME = 30000; //
	private static final String DEBUG_TAG = "Azzy_Debug";

	protected LocationManager lManager;

	public LocationMrg(Context context) {
		this.mContext = context;
		log("Intiated the Location manager class   kaaa .....LOL");
	}

	public Location getLocation() {
		try {
			lManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);

			isGPSEnabled = lManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			isNetworkEnabled = lManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			log("Requesting GPS Enalbe .....the network and gps lock is "
					+ isNetworkEnabled + " and " + isGPSEnabled);

			if (!isGPSEnabled && !isNetworkEnabled) {
				log("think that both locations are not availvabe haha .....the network and gps lock is "
						+ isNetworkEnabled + " and " + isGPSEnabled);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return loc;
	}



	public boolean isIiWIFI() {
		return this.isWifiConn;
	}

	public boolean isItMobile() {
		return this.isMobileConn;
	}

	public boolean isLocationAvailable() {
		boolean check = false;
		try {
			lManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);
			check = lManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return check;
	}

	public void requestGPSEnable() {

		try {
			lManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);
			boolean check = lManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			log("Requesting GPS Enalbe .....LOL" + check);
			if (!check) {

				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						mContext);

				alertDialog.setTitle("GPS is settings");
				alertDialog
						.setMessage("GPS is not enabled. Do you want to go to settings menu?");

				alertDialog.setPositiveButton("Settings",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								mContext.startActivity(intent);
							}
						});

				alertDialog.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});

				alertDialog.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private void log(String string) {
		Log.d(DEBUG_TAG, string);
	}

}