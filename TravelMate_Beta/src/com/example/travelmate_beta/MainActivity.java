package com.example.travelmate_beta;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends Activity implements OnClickListener {

	private String DEBUG_TAG = "Azzy_Debug";
	private LocationMrg lManager;
	private ImageButton ibButtonOne, ibButtonTwo, ibButtonThree, ibButtonFour,
			ibButtonFive, ibButtonSix, ibButtonSeven, ibButtonEight,
			ibButtonNine;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);

		if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
			setContentView(R.layout.main_layout);
			log("inside the main ... UI is loaded hurrey ..");
			setupUI();
			log("Setup OK...");
			quickCheckUp();
			log("Chechup OK ...");
		} else {
			Toast.makeText(this, "Please Install play Sevices",
					Toast.LENGTH_SHORT).show();
			int errorCode = 10;
			((Dialog) GooglePlayServicesUtil
					.getErrorDialog(errorCode, this, 10)).show();
		}
	}

	private void setupUI() {

		ibButtonOne = (ImageButton) findViewById(R.id.ibAtms);
		ibButtonTwo = (ImageButton) findViewById(R.id.ibFuel);
		ibButtonThree = (ImageButton) findViewById(R.id.ibHotels);
		ibButtonFour = (ImageButton) findViewById(R.id.ibMonuments);
		ibButtonFive = (ImageButton) findViewById(R.id.ibMedic);
		ibButtonSix = (ImageButton) findViewById(R.id.ibMovies);
		ibButtonSeven = (ImageButton) findViewById(R.id.ibRestaurants);
		ibButtonEight = (ImageButton) findViewById(R.id.ibShopping);
		ibButtonNine = (ImageButton) findViewById(R.id.ibPolice);

		ibButtonOne.setOnClickListener(this);
		ibButtonTwo.setOnClickListener(this);
		ibButtonThree.setOnClickListener(this);
		ibButtonFour.setOnClickListener(this);
		ibButtonFive.setOnClickListener(this);
		ibButtonSix.setOnClickListener(this);
		ibButtonSeven.setOnClickListener(this);
		ibButtonEight.setOnClickListener(this);
		ibButtonNine.setOnClickListener(this);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	private void quickCheckUp() {
		log("Inside Quick Check Up");
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		boolean isWifiConn = networkInfo.isConnected();
		networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean isMobileConn = networkInfo.isConnected();
		if (!isWifiConn && !isMobileConn) {
			ToastMe("Network Connection not Avialable");
			showAlertDialog(MainActivity.this, "No Internet Connection",
					"Please eneble internet connection first", false);
		} else
			log("Internet Connection Available....");
		if (isWifiConn || isMobileConn) {
			lManager = new LocationMrg(this);
			lManager.requestGPSEnable();
		}
	}

	private void log(String string) {
		Log.d(DEBUG_TAG, string);
	}

	void ToastMe(String string) {
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	}

	@SuppressWarnings("deprecation")
	public void showAlertDialog(Context context, String title, String message,
			Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setIcon((status) ? R.drawable.common_ic_googleplayservices
				: R.drawable.common_ic_googleplayservices);
		alertDialog.setButton("Exit", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		alertDialog.show();
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		int id = v.getId();
		switch (id) {
		case R.id.ibAtms:
			intent = new Intent(this, MapsActivity.class);
			intent.putExtra("KEY_Search", "Search");
			intent.putExtra("KEY_Type", "atm");
			startActivity(intent);
			break;

		case R.id.ibFuel:
			intent = new Intent(this, MapsActivity.class);
			intent.putExtra("KEY_Search", "Search");
			intent.putExtra("KEY_Type", "gas_station");
			startActivity(intent);
			break;

		case R.id.ibHotels:
			intent = new Intent(this, MapsActivity.class);
			intent.putExtra("KEY_Search", "Search");
			intent.putExtra("KEY_Type", "lodging");
			startActivity(intent);
			break;

		case R.id.ibMedic:
			intent = new Intent(this, MapsActivity.class);
			intent.putExtra("KEY_Search", "Search");
			intent.putExtra("KEY_Type", "hospital");
			startActivity(intent);
			break;

		case R.id.ibMonuments:
			intent = new Intent(this, MapsActivity.class);
			intent.putExtra("KEY_Search", "Search");
			intent.putExtra("KEY_Type", "establishment");
			startActivity(intent);
			break;

		case R.id.ibMovies:
			intent = new Intent(this, MapsActivity.class);
			intent.putExtra("KEY_Search", "Search");
			intent.putExtra("KEY_Type", "movie_theater");
			startActivity(intent);
			break;

		case R.id.ibPolice:
			intent = new Intent(this, MapsActivity.class);
			intent.putExtra("KEY_Search", "Search");
			intent.putExtra("KEY_Type", "police");
			startActivity(intent);
			break;

		case R.id.ibShopping:
			intent = new Intent(this, MapsActivity.class);
			intent.putExtra("KEY_Search", "Search");
			intent.putExtra("KEY_Type", "shopping_mall");
			startActivity(intent);
			break;

		case R.id.ibRestaurants:
			intent = new Intent(this, MapsActivity.class);
			intent.putExtra("KEY_Search", "Search");
			intent.putExtra("KEY_Type", "restaurant");
			startActivity(intent);
			break;

		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
}