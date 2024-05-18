package com.about.kby;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateActivity extends AppCompatActivity {
    EditText et_name,et_phone;
    Button sumbit;
    MeowBottomNavigation bottomNavigation;
    TextView txt_toolbar;
    ImageView backimage;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        et_name = findViewById(R.id.et_name);
        backimage = findViewById(R.id.backimage);
        et_phone = findViewById(R.id.et_phone);
        sumbit = findViewById(R.id.sumbit);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        txt_toolbar = findViewById(R.id.txt_toolbar);
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

    @Override
    public void onBackPressed() {
        finish();
        bottomNavigation.show(1, true);
        super.onBackPressed();
    }

}