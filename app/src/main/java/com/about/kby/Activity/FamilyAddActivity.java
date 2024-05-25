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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.about.kby.Adapter.EducationAddAdapter;
import com.about.kby.Adapter.FamilyAddAdapter;
import com.about.kby.Bsession;
import com.about.kby.ProductConfig;
import com.about.kby.R;
import com.about.kby.model.ChecklistModel;
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

public class FamilyAddActivity extends AppCompatActivity {
    TextView txt_toolbar;
    EditText et_number;
    Button family_add;
    ImageView backimage;
    RecyclerView rv_familylist;
    String group_id;
    ProgressDialog progressDialog;
    List<ChecklistModel> namelist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_add);
        backimage =  findViewById(R.id.backimage);
        txt_toolbar = findViewById(R.id.txt_toolbar);
        et_number = findViewById(R.id.et_number);
        family_add = findViewById(R.id.family_add);
        rv_familylist = findViewById(R.id.rv_familylist);
        txt_toolbar.setText("");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            group_id = extras.getString("grup_id");
        }

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");

        name_list();
        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FamilyAddActivity.this, FamilyActivity.class);
                startActivity(intent);
            }
        });

        family_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_number.getText().toString().trim();
                if (name.isEmpty()) {
                    et_number.setError("*Enter your name");
                    et_number.requestFocus();
                    return;
                }
                else {
                    final Map<String, String> params = new HashMap<>();
                    String para2 = "?user_id=" + Bsession.getInstance().getUser_id(FamilyAddActivity.this);
                    String para1 = "&grp_id=" + group_id;
                    String para3 = "&user_name=" + et_number.getText().toString().trim();
                    progressDialog.show();
                    String baseUrl = ProductConfig.personal_grp_add + para2 +para1+ para3;

                    StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response.toString());
                            try {
                                progressDialog.dismiss();
                                JSONObject jsonResponse = new JSONObject(response);

                                if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {

                                    Toast.makeText(FamilyAddActivity.this, "Family member added successfully " , Toast.LENGTH_LONG).show();
                                    name_list();
                                } else {
                                    Toast.makeText(FamilyAddActivity.this, "Family member not added ", Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            Log.e("Error", error.toString());
                            progressDialog.dismiss();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(FamilyAddActivity.this);
                    requestQueue.add(jsObjRequest);
                }
            }
        });

    }

    public void name_list() {
        final Map<String, String> params = new HashMap<>();
        String para_str = "?grp_id=" + group_id;
        String baseUrl = ProductConfig.personal_grp_fetch+ para_str;
        namelist = new ArrayList<>();
        progressDialog.show();
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        JSONArray jsonlist = jsonResponse.getJSONArray("member_list");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject = jsonlist.getJSONObject(j);
                            ChecklistModel model = new ChecklistModel();
                            model.setId(jsonObject.getString("member_id"));
                            model.setName(jsonlist.getJSONObject(j).getString("member_name"));
                            namelist.add(model);
                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(FamilyAddActivity.this, LinearLayoutManager.VERTICAL, false);
                        rv_familylist.setLayoutManager(layoutManager);
                        FamilyAddAdapter packageListAdapter = new FamilyAddAdapter(FamilyAddActivity.this, namelist);
                        rv_familylist.setAdapter(packageListAdapter);
                        rv_familylist.setHasFixedSize(true);

                    } else {
                        Toast.makeText(FamilyAddActivity.this, "Family group not found", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(FamilyAddActivity.this);
        requestQueue.add(jsObjRequest);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FamilyAddActivity.this, FamilyActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}