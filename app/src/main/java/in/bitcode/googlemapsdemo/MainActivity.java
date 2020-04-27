package in.bitcode.googlemapsdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView( R.layout.lay_mainactivity );

        SupportStreetViewPanoramaFragment svpFragment =
                (SupportStreetViewPanoramaFragment) getSupportFragmentManager().findFragmentById( R.id.svpFragment );
        svpFragment.getStreetViewPanoramaAsync(
                new MyOnSVPFragmentReadyCallback()
        );
    }

    class MyOnSVPFragmentReadyCallback implements OnStreetViewPanoramaReadyCallback {
        @Override
        public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {

            streetViewPanorama.setPosition( new LatLng(18.5620, 73.9168));
            streetViewPanorama.setZoomGesturesEnabled( true );
            streetViewPanorama.setPanningGesturesEnabled( true );
            streetViewPanorama.setStreetNamesEnabled( true );

        }
    }
}
