package in.bitcode.googlemapsdemo;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private Marker puneMarker, mumMarker;
    private int count = 0;
    private ArrayList<Marker> markers;
    private Circle circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        markers = new ArrayList<>();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        doSettings();
        addMarkers();
        addListeners();
        drawStuff();

        map.setInfoWindowAdapter( new MyInfoWindowAdapter() );

        Projection projection = map.getProjection();
        Point point = projection.toScreenLocation( mumMarker.getPosition() );
        LatLng geoLoc = projection.fromScreenLocation( new Point(100, 100 ));

        Geocoder gc = new Geocoder( MapsActivity.this, Locale.getDefault() );
        try {
            List<Address> results = gc.getFromLocationName("BitCode Pune", 50 );
            for( Address a : results ) {
                Log.e("tag", a.getAddressLine(0) + "\n" + a.getPhone() );
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        @Override
        public View getInfoContents(Marker marker) {

            View view = LayoutInflater.from( MapsActivity.this ).inflate( R.layout.lay_infowindow, null );

            ((ImageView)view.findViewById( R.id.imgInfoWindow ))
                    .setImageResource( R.mipmap.ic_launcher );
            ( (TextView )view.findViewById( R.id.txtInfoWindows ))
                    .setText( marker.getTitle() );

            return view;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }
    }


    private void drawStuff() {

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.radius(5000)
                .strokeColor(Color.RED)
                .center(puneMarker.getPosition())
                .clickable(true)
                .fillColor(Color.argb(80, 255, 0, 0));
        circle = map.addCircle(circleOptions);


        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.add(new LatLng(28.7041, 77.1025))
                .add(new LatLng(27.1767, 78.0081))
                .add(new LatLng(22.7196, 75.8577))
                .add(new LatLng(26.9124, 75.7873))
                .fillColor( Color.argb( 80, 255, 123, 45 ));
        Polygon polygon = map.addPolygon( polygonOptions );



    }

    private void addListeners() {

        map.setOnMapClickListener(new MyOnMapClickListener());
        map.setOnMarkerClickListener(new MyOnMarkerClickListener());
        map.setOnInfoWindowClickListener(new MyOnInfoWindowClickListener());
        map.setOnMarkerDragListener(new MyOnMarkerDragListener());
        map.setOnMyLocationClickListener(
                new GoogleMap.OnMyLocationClickListener() {
                    @Override
                    public void onMyLocationClick(@NonNull Location location) {

                    }
                }
        );

    }

    private class MyOnMarkerDragListener implements GoogleMap.OnMarkerDragListener {
        @Override
        public void onMarkerDragStart(Marker marker) {
            Log.e("tag", "Start " + marker.getTitle() + " " + marker.getPosition().latitude + " - " + marker.getPosition().longitude);
        }

        @Override
        public void onMarkerDrag(Marker marker) {
            Log.e("tag", "Drag " + marker.getTitle() + " " + marker.getPosition().latitude + " - " + marker.getPosition().longitude);
        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            Log.e("tag", "End " + marker.getTitle() + " " + marker.getPosition().latitude + " - " + marker.getPosition().longitude);
        }
    }

    private class MyOnInfoWindowClickListener implements GoogleMap.OnInfoWindowClickListener {
        @Override
        public void onInfoWindowClick(Marker marker) {
            mt("InfoWindow click : " + marker.getTitle());
        }
    }

    private class MyOnMarkerClickListener implements GoogleMap.OnMarkerClickListener {
        @Override
        public boolean onMarkerClick(Marker marker) {

            mt("Marker Click : " + marker.getTitle());

            return false;
        }
    }

    private class MyOnMapClickListener implements GoogleMap.OnMapClickListener {
        @Override
        public void onMapClick(LatLng latLng) {

            markers.add(
                    map.addMarker(
                            new MarkerOptions()
                                    .title("Some Title " + count)
                                    .snippet("Some Snippet " + count)
                                    .position(latLng)
                    )
            );

            //animate camera

            /*CameraUpdate cameraUpdate =
                    //CameraUpdateFactory.newLatLng( mumMarker.getPosition() );
                    CameraUpdateFactory.newLatLngZoom( mumMarker.getPosition(), 18 );
            */

            CameraPosition.Builder builder = new CameraPosition.Builder();
            CameraPosition cameraPosition = builder.zoom( 18 )
                    .bearing( 60 )
                    .tilt( 80 )
                    .target( mumMarker.getPosition() )
                    .build();

            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition( cameraPosition );

            map.animateCamera( cameraUpdate, 5000, null );

        }
    }

    private void addMarkers() {


        Bitmap bitmap = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.in_flag),
                200,
                150,
                false
        );
        BitmapDescriptor icon =
                //BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_ORANGE);
                //BitmapDescriptorFactory.fromResource( R.drawable.in_flag );
                BitmapDescriptorFactory.fromBitmap(bitmap);


        MarkerOptions pune = new MarkerOptions();
        pune.position(new LatLng(18.5204, 73.8567))
                .title("Pune")
                .snippet("This is PUne")
                .visible(true)
                .draggable(true)
                .anchor(0.5f, 0.5f)
                .icon(icon);

        puneMarker = map.addMarker(pune);


        mumMarker = map.addMarker(
                new MarkerOptions()
                        .position(new LatLng(19.0760, 72.8777))
                        .title("Mumbai")
                        .snippet("This is mumbai")

        );

    }

    @SuppressLint("MissingPermission")
    private void doSettings() {

        map.setMyLocationEnabled(true);
        map.setIndoorEnabled(true);
        map.setTrafficEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setBuildingsEnabled(true);


        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setZoomGesturesEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setRotateGesturesEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setCompassEnabled(true);

        //uiSettings.setAllGesturesEnabled( true );


    }

    private void mt(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
