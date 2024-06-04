package com.about.kby;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.about.kby.Activity.AboutusActivity;
import com.about.kby.Activity.GroupsActivity;
import com.about.kby.Activity.HomeActivity;
import com.about.kby.Activity.PersonalNewGroupActivity;
import com.about.kby.Activity.SupportActivity;
import com.about.kby.DateActivity;
import com.about.kby.Interface.DateSelectionListener;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DateActivity extends AppCompatActivity {
    EditText et_name,et_phone,edt_address;
    Button sumbit;
    MeowBottomNavigation bottomNavigation;
    TextView txt_toolbar;
    TextView tvCurrentLocation;
    ImageView backimage;
    ProgressDialog progressDialog,progressDialog2;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        et_name = findViewById(R.id.et_name);
        edt_address = findViewById(R.id.edt_address);
        backimage = findViewById(R.id.backimage);
        et_phone = findViewById(R.id.et_phone);
        sumbit = findViewById(R.id.sumbit);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        txt_toolbar = findViewById(R.id.txt_toolbar);
        tvCurrentLocation = findViewById(R.id.tv_currentlocation);
        et_name.setText(Bsession.getInstance().getUser_name(DateActivity.this));
        txt_toolbar.setText("Profile");
        String name = Bsession.getInstance().getUser_id(DateActivity.this);
        System.out.println("name=="+name);
        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DateActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        et_name.setText(Bsession.getInstance().getUser_name(getApplicationContext()));
        et_phone.setText(Bsession.getInstance().getUser_mobile(getApplicationContext()));
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.infos));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.group));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.terms));
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                //initialize intent
                Intent intent = null;

                if (item.getId() == 4) {
                    //intent = new Intent(HomeActivity.this, DateActivity.class);
                } else if (item.getId() == 3) {
                    intent = new Intent(DateActivity.this, Group2Activity.class);
                } else if (item.getId() == 2) {
                    intent = new Intent(DateActivity.this, AboutusActivity.class);
                } else if (item.getId() == 1) {
                     intent = new Intent(DateActivity.this, HomeActivity.class);
                }

                if (intent != null) {
                    startActivity(intent);
                }
            }
        });
        tvCurrentLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tvCurrentLocation.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
                if (checkLocationPermission()) {
                    // Request location updates
                    requestLocationUpdates();
                } else {
                    // Request location permissions
                    ActivityCompat.requestPermissions(DateActivity.this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            1);
                }
            }
        });

        bottomNavigation.show(4, true);

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                //Toast.makeText(getApplicationContext()," You clicked "+ item.getId(), Toast.LENGTH_SHORT).show();
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

                //Toast.makeText(getApplicationContext()," You reselected "+ item.getId(), Toast.LENGTH_SHORT).show();
            }
        });

        sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString().trim();
                if (name.isEmpty()) {
                    et_name.setError("*Enter your name");
                    et_name.requestFocus();
                }
                else {
                    final Map<String, String> params = new HashMap<>();
                    String para2 = "?user_id=" + Bsession.getInstance().getUser_id(DateActivity.this);
                    String para3 = "&user_name=" + et_name.getText().toString().trim();
                    System.out.println("gggggg"+para3);
                    System.out.println("gggggg"+para2);

                    String baseUrl = ProductConfig.profile + para2 + para3;

                    StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response.toString());
                            try {

                                JSONObject jsonResponse = new JSONObject(response);

                                if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                                    Toast.makeText(DateActivity.this, "Your profile has been updated", Toast.LENGTH_LONG).show();
                                    Bsession.getInstance().initialize(DateActivity.this,
                                            jsonResponse.getString("user_id"),
                                            jsonResponse.getString("user_name"),
                                            Bsession.getInstance().getUser_mobile(DateActivity.this),
                                            "", "");
                                    Intent intent = new Intent(DateActivity.this, HomeActivity.class);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(DateActivity.this, "Something went wrong your profile ... Try it again", Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            Log.e("Error", error.toString());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(DateActivity.this);
                    requestQueue.add(jsObjRequest);

                }
            }
        });

    }
    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
    private void requestLocationUpdates() {
        ProgressDialog progressDialogg = new ProgressDialog(this);
        this.progressDialog2 = progressDialogg;
        progressDialog2.setMessage("Searching Location.....");
        progressDialog2.show();
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Use either NETWORK_PROVIDER or GPS_PROVIDER depending on your requirements
            String provider = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ?
                    LocationManager.NETWORK_PROVIDER :
                    LocationManager.GPS_PROVIDER;

            // Register the listener with the Location Manager to receive location updates
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                // ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                // public void onRequestPermissionsResult(int requestCode, String[] permissions,
                // int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestSingleUpdate(provider, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // Handle the received location data
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    // Convert latitude and longitude to address
                    Geocoder geocoder = new Geocoder(DateActivity.this, Locale.getDefault());
                    try {
                        progressDialog2.dismiss();
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        if (addresses != null && addresses.size() > 0) {
                            Address address = addresses.get(0);
                            String fullAddress = address.getAddressLine(0);
                            edt_address.setText(fullAddress);
                        } else {
                            Toast.makeText(DateActivity.this, "Unable to fetch address", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        progressDialog2.dismiss();
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
                    progressDialog2.dismiss();
                }
            }, null);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        bottomNavigation.show(1, true);
        super.onBackPressed();
    }


}