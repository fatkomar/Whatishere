package com.example.marcin.whatishere;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static java.lang.Double.valueOf;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView t1;
    TextView t2;
    TextView t3;
    TextView t4;


    private static final int MY_PERMISSION_REQUEST = 101;
    private static final int MY_PERMISSION_REQUEST2 = 102;

    Criteria criteria;
    Address address;
    LocationManager locationManager;
    Location location;
    String bestLocationProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Refresh();


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
        final Geocoder geocoder;
        geocoder = new Geocoder(this, Locale.getDefault());

        // Add a marker in Sydney and move the camera
        if ((!(location == null))) {

            LatLng currentLocation = new LatLng(valueOf(location.getLatitude()), valueOf(location.getLongitude()));
            mMap.addMarker(new MarkerOptions()
                    .position(currentLocation)
                    .title("You are here"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));

        }


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(point));
//                mMap.addMarker(new MarkerOptions()
//                        .position(point)
//                        .title(address)
//                );
                double location2 = point.latitude;
                double location3 = point.longitude;

                t3 = (TextView) findViewById(R.id.textViewa);
                t4 = (TextView) findViewById(R.id.textViewb);


                t4.setText(String.valueOf(location3));


                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(location2, location3, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                String address = addresses.get(0).getAddressLine(0);
                t3.setText(address);
                mMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title(address)
                );

                Toast.makeText(MapsActivity.this, address, Toast.LENGTH_SHORT).show();

            }

        });

    }


    private void Refresh() {

        t1 = (TextView) findViewById(R.id.textView1);
        t2 = (TextView) findViewById(R.id.textView2);


        criteria = new Criteria();
        address = new Address(Locale.US);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        bestLocationProvider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST);
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST2);
            return;
        }
        location = locationManager.getLastKnownLocation(bestLocationProvider);


        t1.setText(String.valueOf(location.getLatitude()));
        t2.setText(String.valueOf(location.getLongitude()));


    }


}
