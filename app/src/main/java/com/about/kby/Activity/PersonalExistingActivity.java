package com.about.kby.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.about.kby.Adapter.ExistingGroupAdapter;
import com.about.kby.Adapter.PersonalGroupAdapter;
import com.about.kby.Bsession;
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

public class PersonalExistingActivity extends AppCompatActivity {
    List<ChecklistModel> existinglist = new ArrayList<>();
    ImageView backimage;
    Button exist_group,new_group;
    TextView txt_toolbar;
    ProgressDialog progressDialog;
    RecyclerView rv_personal_exist;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_existing);
        rv_personal_exist = findViewById(R.id.rv_personal_exist);
        backimage = findViewById(R.id.backimage);
        new_group = findViewById(R.id.new_group);
        txt_toolbar = findViewById(R.id.txt_toolbar);
        exist_group = findViewById(R.id.exist_group);

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");

        new_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalExistingActivity.this, PersonalNewGroupActivity.class);
                startActivity(intent);
            }
        });

        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalExistingActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        txt_toolbar.setText("Personal");
        personal_fetch();
    }

    public void personal_fetch() {
        final Map<String, String> params = new HashMap<>();
        String para_str = "?user_id=" + Bsession.getInstance().getUser_id(PersonalExistingActivity.this);
        String para_str1 = "&user=" + Bsession.getInstance().getUser_mobile(PersonalExistingActivity.this);
        String baseUrl = ProductConfig.personal_exist+ para_str+para_str1;
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
                        JSONArray jsonlist = jsonResponse.getJSONArray("personal_grp_list");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject = jsonlist.getJSONObject(j);
                            ChecklistModel model = new ChecklistModel();
                            model.setName(jsonlist.getJSONObject(j).getString("grp_name"));
                            model.setId(jsonlist.getJSONObject(j).getString("grp_id"));

                            existinglist.add(model);
                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(PersonalExistingActivity.this, LinearLayoutManager.VERTICAL, false);
                        rv_personal_exist.setLayoutManager(layoutManager);
                        PersonalGroupAdapter packageListAdapter = new PersonalGroupAdapter(PersonalExistingActivity.this, existinglist);
                        rv_personal_exist.setAdapter(packageListAdapter);
                        rv_personal_exist.setHasFixedSize(true);

                    } else {
                        Toast.makeText(PersonalExistingActivity.this, "Personal group not found", Toast.LENGTH_SHORT).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(PersonalExistingActivity.this);
        requestQueue.add(jsObjRequest);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PersonalExistingActivity.this,HomeActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}