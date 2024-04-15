package com.about.kby.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.about.kby.Adapter.ExistingGroupAdapter;
import com.about.kby.Bsession;
import com.about.kby.DateActivity;
import com.about.kby.ProductConfig;
import com.about.kby.R;
import com.about.kby.model.ExistingModel;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EducationExistingActivity extends AppCompatActivity {
    Button ed_input,ed_insight;
    ImageView backimage,image,img_nogrp;
    static RecyclerView rv_group;
    static ProgressDialog progressDialog;
    static List<ExistingModel> existinglist = new ArrayList<>();
    TextView txt_toolbar;
    LinearLayout ll_nogrp,existig_grp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_existing);

        ed_input =  findViewById(R.id.ed_input);
        backimage =  findViewById(R.id.backimage);
        image =  findViewById(R.id.image);
        txt_toolbar =  findViewById(R.id.txt_toolbar);
        ed_insight =  findViewById(R.id.ed_insight);
        rv_group = findViewById(R.id.rv_group);
        img_nogrp = findViewById(R.id.img_nogrp);
        ll_nogrp = findViewById(R.id.ll_nogrp);
        existig_grp = findViewById(R.id.existig_grp);

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");

        ed_insight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EducationExistingActivity.this, EducationNewActivity.class);
                startActivity(intent);
            }
        });
        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EducationExistingActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EducationExistingActivity.this, DateActivity.class);
                startActivity(intent);
            }
        });
        txt_toolbar.setText("Education");
        packagelist();
    }

    public void packagelist() {
        final Map<String, String> params = new HashMap<>();
        String para_str = "?user_id=" + Bsession.getInstance().getUser_id(EducationExistingActivity.this);
        String baseUrl = ProductConfig.education_exist+ para_str;
        existinglist = new ArrayList<>();
        progressDialog.show();
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        JSONArray jsonlist = jsonResponse.getJSONArray("category_list");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject = jsonlist.getJSONObject(j);
                            ExistingModel model = new ExistingModel();
                            model.setGroup_id(jsonlist.getJSONObject(j).getString("grp_id"));
                            model.setGroup_name(jsonlist.getJSONObject(j).getString("grp_name"));
                            model.setAdmin(jsonlist.getJSONObject(j).getString("admin"));
                            model.setGroup_count(jsonlist.getJSONObject(j).getString("member_count"));
                            existinglist.add(model);

                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(EducationExistingActivity.this, LinearLayoutManager.VERTICAL, false);
                        rv_group.setLayoutManager(layoutManager);
                        ExistingGroupAdapter packageListAdapter = new ExistingGroupAdapter(EducationExistingActivity.this, existinglist);
                        rv_group.setAdapter(packageListAdapter);
                        rv_group.setHasFixedSize(true);

                    } else {
                        Toast.makeText(EducationExistingActivity.this, "Existing group not found", Toast.LENGTH_SHORT).show();
                        ll_nogrp.setVisibility(View.VISIBLE);
                        existig_grp.setVisibility(View.GONE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EducationExistingActivity.this);
        requestQueue.add(jsObjRequest);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EducationExistingActivity.this, HomeActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}