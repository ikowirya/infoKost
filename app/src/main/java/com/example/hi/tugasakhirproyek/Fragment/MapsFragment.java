package com.example.hi.tugasakhirproyek.Fragment;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hi.tugasakhirproyek.Model.kost;
import com.example.hi.tugasakhirproyek.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by Hi on 18/01/2018.
 */

public class MapsFragment extends Fragment implements OnMapReadyCallback,LocationListener,GoogleMap.OnMarkerClickListener{
    GoogleMap mMap;
    MapView mMapView;
    View mView;
    private String mPost_Key = null;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mAlamat;
    public  MapsFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_maps, container, false);
        mAlamat = FirebaseDatabase.getInstance().getReference("kost");
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView)mView.findViewById(R.id.map);

        if (mMapView != null)
        {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

    }

    @Override

    public void onMapReady(GoogleMap googleMap) {
//        MapsInitializer.initialize((getContext()));
//        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        googleMap.addMarker(new MarkerOptions().position((new LatLng(-7.952194000000002,112.613289))).title("Kost"));
//        CameraPosition lokasi = CameraPosition.builder().target(new LatLng(-7.952194000000002,112.613289)).zoom(15).bearing(0).tilt(0).build();
//        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(lokasi));
        mMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mAlamat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    kost kostAlamat = s.getValue(kost.class);
                    LatLng location = new LatLng(kostAlamat.latitude, kostAlamat.longitude);
                    mMap.addMarker(new MarkerOptions().position(location).title(Integer.toString(kostAlamat.biaya_sewa))).
                            setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13.0f));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        if ((ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED ||ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            mMap.setMyLocationEnabled(true);

//        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            final Marker[] mark= new Marker[1];
//            @Override
//            public void onMapClick(LatLng latLng) {
//                if (mark[0] != null) {
//                    mark[0].remove();
//                }
//                mark[0] = googleMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude,latLng.longitude)));
//
//            }
//        });

        }
    }

    @Override
    public void onLocationChanged(Location location) {

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

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}