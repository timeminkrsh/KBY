package com.about.kby.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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

import com.about.kby.Adapter.ChecklistAdapter;
import com.about.kby.Adapter.EducationAddAdapter;
import com.about.kby.Bsession;
import com.about.kby.DateActivity;
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

public class ChecklistAddActivity extends AppCompatActivity {
    RecyclerView rv_checklist;
    ImageView backimage,image;
    TextView txt_toolbar;
    Button create_check;
    EditText check_name;
    ProgressDialog progressDialog;
    List<ChecklistModel> namelist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_add);
        rv_checklist = findViewById(R.id.rv_checklist);
        txt_toolbar = findViewById(R.id.txt_toolbar);
        create_check = findViewById(R.id.create_check);
        check_name = findViewById(R.id.check_name);
        backimage =  findViewById(R.id.backimage);
        image =  findViewById(R.id.image);

        //bmethod();
        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");

        create_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = check_name.getText().toString().trim();
                if (name.isEmpty()) {
                    check_name.setError("*Enter your name");
                    check_name.requestFocus();
                    return;
                }
                else {
                    final Map<String, String> params = new HashMap<>();
                    String para2 = "?user_id=" + Bsession.getInstance().getUser_id(ChecklistAddActivity.this);
                    String para3 = "&name=" + check_name.getText().toString().trim();
                    System.out.println("gggggg"+para3);
                    System.out.println("gggggg"+para2);
                    progressDialog.show();
                    String baseUrl = ProductConfig.checklist_add + para2 + para3;

                    StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response.toString());
                            try {
                                progressDialog.dismiss();
                                JSONObject jsonResponse = new JSONObject(response);
                                if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                                    Toast.makeText(ChecklistAddActivity.this, "Checklist task created successfully", Toast.LENGTH_LONG).show();
                                    name_list();
                                    check_name.getText().clear();
                                } else {
                                    Toast.makeText(ChecklistAddActivity.this, "Checklist not found", Toast.LENGTH_LONG).show();
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
                    RequestQueue requestQueue = Volley.newRequestQueue(ChecklistAddActivity.this);
                    requestQueue.add(jsObjRequest);

                }
            }
        });

        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChecklistAddActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChecklistAddActivity.this, DateActivity.class);
                startActivity(intent);
            }
        });
        name_list();
        txt_toolbar.setText("Checklist");
    }

    public void name_list() {
        final Map<String, String> params = new HashMap<>();
        String para_str = "?user_id=" + Bsession.getInstance().getUser_id(ChecklistAddActivity.this);
        String baseUrl = ProductConfig.checklist_fetch+ para_str;
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
                        JSONArray jsonlist = jsonResponse.getJSONArray("task_list");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject = jsonlist.getJSONObject(j);
                            ChecklistModel model = new ChecklistModel();
                            model.setId(jsonObject.getString("check_List_id"));
                            model.setName(jsonObject.getString("tack_name"));
                            namelist.add(model);
                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(ChecklistAddActivity.this, LinearLayoutManager.VERTICAL, false);
                        rv_checklist.setLayoutManager(layoutManager);
                        ChecklistAdapter packageListAdapter = new ChecklistAdapter(ChecklistAddActivity.this, namelist);
                        rv_checklist.setAdapter(packageListAdapter);
                        rv_checklist.setHasFixedSize(true);

                    } else {
                        Toast.makeText(ChecklistAddActivity.this, "Checklist group not found", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(ChecklistAddActivity.this);
        requestQueue.add(jsObjRequest);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChecklistAddActivity.this, HomeActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}