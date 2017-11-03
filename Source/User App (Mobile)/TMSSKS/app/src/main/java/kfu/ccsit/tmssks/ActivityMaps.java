package kfu.ccsit.tmssks;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;
import java.util.TimerTask;

import kfu.ccsit.tmssks.data.DataMedium;
import kfu.ccsit.tmssks.data.Kid;
import kfu.ccsit.tmssks.data.MapPoint;
import kfu.ccsit.tmssks.data.network.NetworkUtils;

public class ActivityMaps extends ActivityBase implements OnMapReadyCallback {

    public static final String KEY_KID_ID = "kid_id";
    private static final int PERIOD = 500;

    private boolean mEnableCheck;
    private GoogleMap mMap;
    private Timer mTimer;
    private TimerTask mTask;
    private String mKidId;
    private Marker mMarker;
    private MarkerOptions mMarkerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Setting up toolbar
        setSupportActionBar(getToolbar());
        //noinspection ConstantConditions
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mKidId = getIntent().getExtras().getString(KEY_KID_ID, "-1");

        mMarkerOptions = new MarkerOptions()
                .anchor(0.5f, .97f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_bitmap));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEnableCheck = true;
        mTimer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {
                if (mEnableCheck) {
                    DataMedium.loadRoute(ActivityMaps.this, new NetworkUtils.OnRequestListener() {
                        @Override
                        public void onFinished(String response, boolean error) {
                            if (!error && mMap != null) {
                                updateMap(DataMedium.parseMapPoints(response));
                            } else {
                                LogUtils.d("loadRoute Error: " + response);
                            }
                            mEnableCheck = true;
                        }
                    }, mKidId);
                    mEnableCheck = false;
                }
            }
        };

        mTimer.scheduleAtFixedRate(mTask, 0, PERIOD);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mTimer.cancel();
            mTask.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Go back to the main activity when home (UP) button is clicked
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Kid k = DataMedium.getKid(mKidId);
        assert k != null;
        LatLng latlng = new LatLng(k.getLat(), k.getLng());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 10));
    }

    private void updateMap(MapPoint point) {
        if (point == null) {
            return;
        }

        mMap.clear();
        LatLng latlng = point.getLatLng();
        if (latlng != null) {
            mMarkerOptions.position(latlng);
            mMarker = mMap.addMarker(mMarkerOptions);
        } else {
            if (mMarker != null) mMarker.remove();
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            if (mMap != null && mMarker != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLng(mMarker.getPosition()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mMarker.getPosition(), 18));
            }
        }
    }
}
