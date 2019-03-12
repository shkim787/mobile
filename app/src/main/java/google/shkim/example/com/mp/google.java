package google.shkim.example.com.mp;

import android.content.Intent;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class google extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    PlaceAutocompleteFragment placeAutoComplete;
    private static final String DEBUG_TAG = "{LOG_ANDROID}";
    private Database dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initViews();
    }

    private void initViews() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete);

        Log.d(DEBUG_TAG, "a");

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                // TODO: Get info about the selected place.

                final LatLng location = place.getLatLng();
                Log.d(DEBUG_TAG, "name : " + place.getName());
                Log.d(DEBUG_TAG, "address : " + place.getAddress());
                Log.d(DEBUG_TAG, "id : " + place.getId());
                Log.d(DEBUG_TAG, "location : " + location.latitude + ", " + location.longitude);
                //세부정보 가져오기
                mMap.clear();
                int nameLength = place.getAddress().length();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(location));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, nameLength-1));
                MarkerOptions makerOptions = new MarkerOptions();
                makerOptions .position(location)
                        .title("검색 위치"); // 타이틀.

                // 2. 마커 생성 (마커를 나타냄)
                mMap.addMarker(makerOptions);

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("LOG", "An error occurred: " + status);
            }
        });
    }  //검색바에 장소 정보 적용

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        Button okbtn = (Button)findViewById(R.id.saveBtn);
        Button calcelbtn = (Button)findViewById(R.id.cancelBtn);
        dbHelper = new Database(getApplicationContext(), "Marker.db", null, 1);
        okbtn.setOnClickListener(new View.OnClickListener() //확인 버튼 클릭 이벤트
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(google.this, index.class);
                startActivity(intent);
                dbHelper.insert("insert into markerPoint(PlaceName, Lat, Lng) values('"+ place.getName() +"',"+ latLng.latitude + ", " + latLng.longitude + ");");
            }
        });
        calcelbtn.setOnClickListener(new View.OnClickListener() //취소 버튼 클릭 이벤트
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(google.this, index.class);
                startActivity(intent);
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() //꾹 누르고 있을때 마커 찍히는 이벤트
        {
            @Override
            public void onMapLongClick(final LatLng latLng)
            {
                MarkerOptions markerOptions = new MarkerOptions();
                mMap.addMarker(new MarkerOptions().position(latLng));
                    markerOptions.position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)); //마커위치설정
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));   // 마커생성위치로 이동
                    mMap.addMarker(markerOptions); //마커 생성

            }

        });
    }
}
