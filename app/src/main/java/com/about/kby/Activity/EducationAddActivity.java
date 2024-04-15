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
import com.about.kby.Adapter.ExistingGroupAdapter;
import com.about.kby.Asession;
import com.about.kby.Bsession;
import com.about.kby.DateActivity;
import com.about.kby.ProductConfig;
import com.about.kby.R;
import com.about.kby.model.ChecklistModel;
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

public class EducationAddActivity extends AppCompatActivity {
    ImageView backimage,image;
    TextView txt_toolbar,add_name;
    EditText et_number;
    Button education_add;
    String group_id,memberId;
    RecyclerView rv_addlist;
    ProgressDialog progressDialog;
    List<ChecklistModel> namelist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_add);

        backimage = findViewById(R.id.backimage);
        image = findViewById(R.id.image);
        rv_addlist = findViewById(R.id.rv_addlist);
        add_name = findViewById(R.id.add_name);
        education_add = findViewById(R.id.education_add);
        et_number = findViewById(R.id.et_number);
        txt_toolbar = findViewById(R.id.txt_toolbar);
        add_name.setText(Bsession.getInstance().getUser_name(EducationAddActivity.this)+"( Admin )");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            group_id = extras.getString("grp_id");
            System.out.println("gropid="+group_id);
        }

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");

        name_list();
        education_add.setOnClickListener(new View.OnClickListener() {
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
                    String para2 = "?user_id=" + Bsession.getInstance().getUser_id(EducationAddActivity.this);
                    String para1 = "&grp_id=" + group_id;
                    String para3 = "&member=" + et_number.getText().toString().trim();
                    progressDialog.show();
                    String baseUrl = ProductConfig.education_grp_add + para2 +para1+ para3;

                    StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response.toString());
                            try {
                                progressDialog.dismiss();
                                JSONObject jsonResponse = new JSONObject(response);

                                if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                                    Toast.makeText(EducationAddActivity.this, "Your profile has been updated", Toast.LENGTH_LONG).show();
                                    et_number.getText().clear();
                                    name_list();
                                } else {
                                    Toast.makeText(EducationAddActivity.this, "Please check the details, account may not be created...", Toast.LENGTH_LONG).show();
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
                    RequestQueue requestQueue = Volley.newRequestQueue(EducationAddActivity.this);
                    requestQueue.add(jsObjRequest);

                }
            }
        });
        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EducationAddActivity.this, DateActivity.class);
                startActivity(intent);
            }
        });
        txt_toolbar.setText("Add/Remove");

    }

    private void addMobile() {

    }

    public void name_list() {
        final Map<String, String> params = new HashMap<>();
        String para_str = "?grp_id=" + group_id;
        String baseUrl = ProductConfig.education_grp_fetch+ para_str;
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
                            model.setName(jsonObject.getString("member_name"));

                            namelist.add(model);
                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(EducationAddActivity.this, LinearLayoutManager.VERTICAL, false);
                        rv_addlist.setLayoutManager(layoutManager);
                        EducationAddAdapter packageListAdapter = new EducationAddAdapter(EducationAddActivity.this, namelist);
                        rv_addlist.setAdapter(packageListAdapter);
                        rv_addlist.setHasFixedSize(true);

                    } else {
                        //Toast.makeText(EducationAddActivity.this, "Existing group not found", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(EducationAddActivity.this);
        requestQueue.add(jsObjRequest);

    }
}