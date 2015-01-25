package com.example.travelmate_beta;

import java.util.ArrayList;
import java.util.Locale;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements LocationListener,
		OnMapReadyCallback, OnNavigationListener, OnMarkerClickListener {

	private static final String DEBUG_TAG = "Azzy_Debug ";
	private static final long UPDATE_TIME = 30000;
	private static final float UPDATE_DISTANCE = 10;
	private String provider;
	private Location uLocation = null;
	private GoogleMap gMap;
	private LocationManager lManager;
	private EditText etSearch;
	private String[] places;
	ActionBar actionbar;
	private TextToSpeech ttobj;
	private Button btSearch;
	private EditText etSearchInput;
	private String SearchSrting = null;
	boolean isFirstLoad = true;;
	private int index = 1;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		places = getResources().getStringArray(R.array.places);
		log(" "+places.length);
		actionbar = getActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionbar.setListNavigationCallbacks(ArrayAdapter.createFromResource(
				this, R.array.places, android.R.layout.simple_list_item_1),
				this);
		log("We Are in Maps Activity");
		setContentView(R.layout.activity_main);

		MapFragment fragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.map_Frag);
		fragment.getMapAsync(this);
		log("hurrey fragment sync ..");
		log("LOL");
		// SearchSrting = (String) getIntent().getStringExtra("KEY_Type");
		// etSearch.setHint(SearchSrting);
		// log(SearchSrting);
		btSearch = (Button) findViewById(R.id.btSearch);
		ttobj = new TextToSpeech(MapsActivity.this,
				new TextToSpeech.OnInitListener() {
					@Override
					public void onInit(int status) {
						if (status != TextToSpeech.ERROR)
							ttobj.setLanguage(Locale.US);
						ttobj.setSpeechRate((float) 1.0);
					}
				});

		btSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ttobj.speak("this functionality is not added yet",
						TextToSpeech.QUEUE_ADD, null);
			}
		});
	}

	private void log(String string) {
		Log.d(DEBUG_TAG, string);
	}

	void ToastMe(String string) {
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	}

	// @SuppressWarnings("deprecation"+places[].)
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
	public void onLocationChanged(Location location) {

		// log(places[actionbar.getSelectedNavigationIndex());
		uLocation = location;
		gMap.clear();

		log("Map Clear ...." + places[index].toLowerCase().replace("-", "_"));
		log("On Location Change  long : " + uLocation.getLongitude()
				+ " latitude  : " + uLocation.getLatitude());
		new GetPlaces(MapsActivity.this, places[index].toLowerCase().replace(
				"-", "_")).execute();

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.menu_sethybrid:
			gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			break;
		case R.id.menu_showtraffic:
			gMap.setTrafficEnabled(true);
			break;
		case R.id.menu_addmarker:

			break;
		case R.id.menu_getcurrentlocation:
			// ---get your current location and display a blue dot---
			gMap.setMyLocationEnabled(true);

			break;
		case R.id.menu_Stellite:
			gMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			break;

		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		gMap = googleMap;
		gMap.setMyLocationEnabled(true);
		lManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		// Getting the name of the best provider
		provider = lManager.getBestProvider(criteria, true);
		// Getting Current Location From GPS
		lManager.requestLocationUpdates(provider, UPDATE_TIME, UPDATE_DISTANCE,
				this);
		log("onMap IS OK ... ..");
		gMap.setOnMarkerClickListener(this);

	}

	private class GetPlaces extends AsyncTask<Void, Void, ArrayList<Place>> {

		private ProgressDialog dialog;
		private Context context;
		private String places;

		public GetPlaces(Context context, String places) {
			this.context = context;
			this.places = places;
			log("Get Places 1 ....");
		}

		@Override
		protected void onPostExecute(ArrayList<Place> result) {
			super.onPostExecute(result);
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			log("Get Places 2 ....");
			for (int i = 0; i < result.size(); i++) {
				gMap.addMarker(new MarkerOptions()
						.title(result.get(i).getName())
						.position(
								new LatLng(result.get(i).getLatitude(), result
										.get(i).getLongitude()))
						.icon(BitmapDescriptorFactory
								.fromResource(R.raw.pintwo))
						.snippet(result.get(i).getVicinity()));
			}
			log("Get Places 2 ....");

			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(result.get(0).getLatitude(), result.get(
							0).getLongitude())).zoom(14).tilt(30).build();
			log("Get Places 3 ....");
			gMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			log("Get Places 5 ....");
			dialog = new ProgressDialog(context);
			log("Get Places 6 ....");
			dialog.setCancelable(false);
			dialog.setMessage("Loading..");
			dialog.isIndeterminate();
			dialog.show();
			log("Get Places 7 ....");
		}

		@Override
		protected ArrayList<Place> doInBackground(Void... arg0) {
			log("Get Places 8 ....");
			ArrayList<Place> findPlaces = null;
			if (uLocation != null) {
				PlacesService service = new PlacesService(
						"AIzaSyD08FDnqrH7dl0vQCVVHMaZNlzkJTPQYfc");
				findPlaces = service.findPlaces(uLocation.getLatitude(),
						uLocation.getLongitude(), places);
				log("Get Places 9 ....");
				for (int i = 0; i < findPlaces.size(); i++) {
					Place placeDetail = findPlaces.get(i);
					log("Get Places 9 ....");
					Log.e(DEBUG_TAG, "places : " + placeDetail.getName());
					log("Get Places 10 ....");
				}
			} else
				ToastMe("Location is not obtained yet...");
			return findPlaces;
		}

	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		gMap.clear();
		log("onNavigation  IS started ... ..");
		if (uLocation != null) {
			log("On Location Change  long : " + uLocation.getLongitude()
					+ " latitude  : " + uLocation.getLatitude());
			new GetPlaces(MapsActivity.this, places[itemPosition].toLowerCase()
					.replace("-", "_").replace(" ", "_")).execute();
		} else
			ToastMe("Location is not obtained yet...");
		log("onNavigation  IS ok ... ..");
		return true;

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		ToastMe(arg0.getTitle());
		ToastMe(arg0.getSnippet());
		ttobj.speak(arg0.getTitle(), TextToSpeech.QUEUE_ADD, null);
		return false;
	}

}
