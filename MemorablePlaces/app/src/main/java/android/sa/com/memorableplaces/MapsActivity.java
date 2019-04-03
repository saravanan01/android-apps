package android.sa.com.memorableplaces;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private int permCount = 0;
    private LatLng memPlace;
    private String memPlaceAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("lat", -1);
        double lng = intent.getDoubleExtra("lng", -1);
        String address = intent.getStringExtra("name");
        if(address != null) {
            memPlace = new LatLng(lat,
                    lng);
            memPlaceAddress = address;
        }else {
            memPlace = null;
            memPlaceAddress = null;
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 101 && permissions.length > 0 && grantResults.length > 1 && (grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED ) ){
            if (permCount < 3) {
                permCount++;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Log.i("perms", "got gps");
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                }
            } else {
                finish();
            }
        } else {
            Log.i("perms", "got gps on try "+permCount);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},101);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60*60*1000, 5000, this);
        mMap.setOnMapLongClickListener(this);

        mMap.clear();
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LatLng latLng = memPlace;
        String title = memPlaceAddress;
        if( memPlaceAddress == null && memPlace == null ) {
            latLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            title = "Your Location";
        }
        addMarker(latLng, title);
        updateAllmarkers();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));


    }

    private void updateAllmarkers() {
        for (Place p: MainActivity.getAllPlace() ) {
            if(!p.getAddress().equals(Constants.ADD_AN_ENTRY)){
                addMarker(new LatLng(p.getLatitude(),p.getLongitude()),p.getAddress());
            }
        }
    }

    private void addMarker(LatLng latLng, String title) {
        mMap.addMarker(new MarkerOptions().position(latLng).title(title));
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
            Log.i("long click", latLng.toString());
            try {
                List<Address> list = new Geocoder(getApplicationContext(), Locale.getDefault())
                        .getFromLocation(latLng.latitude, latLng.longitude, 1);
                if(list != null && list.size() > 0){
                    StringBuilder sb = new StringBuilder(100);
                    Address address = list.get(0);
                    String countryName = address.getCountryName();
                    String thoroughfare = address.getThoroughfare();
                    String thoroughfareSub = address.getSubThoroughfare();
                    if(thoroughfare != null) {
                        if(thoroughfareSub != null) {
                            sb.append(thoroughfareSub).append(", ");
                        }
                        sb.append(thoroughfare);

                    }

                    if (sb.toString().trim().length() < 1) sb.append(latLng.toString());
                    if (countryName != null) sb.append(", ").append(countryName);

                    saveNewMemorablePlace(latLng, sb.toString());
                } else {
                    saveNewMemorablePlace(latLng, latLng.toString());
                }
            } catch (Exception e) {
                Log.e("GeoDecode",e.getMessage(),e);
                Toast.makeText(this,"Error getting address ",Toast.LENGTH_SHORT).show();
            }

    }

    private void saveNewMemorablePlace(LatLng latLng, String sb) {
        MainActivity.addPlace(new Place(sb,latLng.latitude,latLng.longitude));
        Toast.makeText(this,"Saved "+sb,Toast.LENGTH_SHORT).show();
        updateAllmarkers();
    }

    @Override
    public void onLocationChanged(Location location) {
        mMap.clear();
        updateAllmarkers();
        if ( memPlaceAddress != null && memPlace != null) {
            mMap.addMarker(new MarkerOptions().position(memPlace).title(memPlaceAddress));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(memPlace,16));
        } else {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Your current location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
