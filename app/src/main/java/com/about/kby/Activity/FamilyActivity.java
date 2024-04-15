package com.about.kby.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.about.kby.Adapter.OpenGroupAdapter;
import com.about.kby.Adapter.PersonalCircleAdapter;
import com.about.kby.Bsession;
import com.about.kby.ProductConfig;
import com.about.kby.R;
import com.about.kby.model.ChecklistModel;
import com.about.kby.model.OpenModel;
import com.about.kby.model.SubCategoryModel;
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

public class FamilyActivity extends AppCompatActivity {
    ImageView fam_more;
    RecyclerView fam_reportchart;
    ImageView backimage;
    TextView txt_toolbar,team_name;
    String group_id,team_Name;
    Button btn_kby,btn_checklist,btn_education;
    RecyclerView rv_chartreport,educationlist,checklist_recycle;

    List<OpenModel> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);
        btn_kby = findViewById(R.id.btn_kby);
        rv_chartreport = findViewById(R.id.rv_chartreport);
        educationlist = findViewById(R.id.educationlist);
        checklist_recycle = findViewById(R.id.checklist_recycle);
        btn_checklist = findViewById(R.id.btn_checklist);
        team_name = findViewById(R.id.team_name);
        btn_education = findViewById(R.id.btn_education);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            group_id = extras.getString("grup_id");
            System.out.println("grup_id=,"+group_id);
            team_Name = extras.getString("team_name");
            team_name.setText(team_Name);
        }

        fam_more = findViewById(R.id.fam_more);
        fam_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(FamilyActivity.this,view);
                popupMenu.inflate(R.menu.education_popup);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.add_edu) {
                            Intent intent = new Intent(FamilyActivity.this, FamilyAddActivity.class);
                            intent.putExtra("grup_id",group_id);
                            startActivity(intent);
                        }
                        return false;

                    }
                });

            }
        });
        txt_toolbar = findViewById(R.id.txt_toolbar);

        backimage = findViewById(R.id.backimage);
        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FamilyActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        //txt_toolbar.setText("Family");

        btn_kby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_kby.setBackgroundResource(R.color.table);
                btn_education.setBackgroundResource(R.color.white);
                btn_checklist.setBackgroundResource(R.color.white);
                educationlist.setVisibility(View.GONE);
                checklist_recycle.setVisibility(View.GONE);
                openmentod();
                // Toast.makeText(OpenGroupActivity.this, "1111", Toast.LENGTH_SHORT).show();
            }
        });

        btn_education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_education.setBackgroundResource(R.color.table);
                btn_kby.setBackgroundResource(R.color.white);
                btn_checklist.setBackgroundResource(R.color.white);
                rv_chartreport.setVisibility(View.GONE);
                checklist_recycle.setVisibility(View.GONE);
                openmentods();
            }
        });

        btn_checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_checklist.setBackgroundResource(R.color.table);
                btn_kby.setBackgroundResource(R.color.white);
                btn_education.setBackgroundResource(R.color.white);
                rv_chartreport.setVisibility(View.GONE);
                educationlist.setVisibility(View.GONE);
                openmentodchecklist();
                // Toast.makeText(OpenGroupActivity.this, "3333", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openmentodchecklist() {
        final Map<String, String> params = new HashMap<>();
        checklist_recycle.setVisibility(View.VISIBLE);
        String baseUrl = ProductConfig.checklist_group_list+"?user_id="+ Bsession.getInstance().getUser_id(FamilyActivity.this)+"&group_id="+group_id;
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        itemList = new ArrayList<>();
                        JSONArray jsonlist = jsonResponse.getJSONArray("storeList");
                        for (int i = 0;i < jsonlist.length(); i++) {
                            JSONObject jsonObject1 = jsonlist.getJSONObject(i);
                            OpenModel model1 = new OpenModel();
                            model1.setUser_name(jsonObject1.getString("user_name"));
                            JSONArray jArrItemData = new JSONArray(jsonObject1.getString("list"));
                            List<SubCategoryModel> subCategoryModels = new ArrayList<>();

                            for (int j = 0; j < jArrItemData.length(); j++) {
                                JSONObject subCategoryObject = jArrItemData.getJSONObject(j);

                                SubCategoryModel subCategoryModel = new SubCategoryModel();
                                model1.setActive(subCategoryObject.getString("active"));
                                model1.setInactive(subCategoryObject.getString("inactive"));
                                model1.setDescription(subCategoryObject.getString("description"));
                                //subCategoryModels.add(subCategoryModel);

                            }
                            //model1.setSubcategory(subCategoryModels);
                            itemList.add(model1);
                            System.out.println("subCategoryModels=="+subCategoryModels);

                            PersonalCircleAdapter personalAdapter = new PersonalCircleAdapter(FamilyActivity.this, itemList);
                            checklist_recycle.setAdapter(personalAdapter);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(FamilyActivity.this);
                            checklist_recycle.setLayoutManager(layoutManager);

                           /* OpenGroupChecklistAdapter personalAdapter = new OpenGroupChecklistAdapter(FamilyActivity.this, itemList);
                            checklist_recycle.setAdapter(personalAdapter);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(FamilyActivity.this);
                            checklist_recycle.setLayoutManager(layoutManager);*/
                        }

                    } else {
                        Toast.makeText(FamilyActivity.this, "Activity not found", Toast.LENGTH_SHORT).show();
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
                        // Handle the error response here
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(FamilyActivity.this);
        requestQueue.add(jsObjRequest);
    }


    public void openmentod() {
        final Map<String, String> params = new HashMap<>();
        rv_chartreport.setVisibility(View.VISIBLE);
        String baseUrl = ProductConfig.kby_group_list+"?user_id="+Bsession.getInstance().getUser_id(FamilyActivity.this)+"&group_id="+group_id;
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        itemList = new ArrayList<>();
                        JSONArray jsonlist = jsonResponse.getJSONArray("storeList");
                        for (int i = 0;i < jsonlist.length(); i++) {
                            JSONObject jsonObject1 = jsonlist.getJSONObject(i);
                            OpenModel model1 = new OpenModel();
                            model1.setUser_name(jsonObject1.getString("user_name"));
                            model1.setDescription(jsonObject1.getString("description"));
                            JSONArray jArrItemData = new JSONArray(jsonObject1.getString("list"));
                            List<SubCategoryModel> subCategoryModels = new ArrayList<>();

                            for (int j = 0; j < jArrItemData.length(); j++) {
                                JSONObject subCategoryObject = jArrItemData.getJSONObject(j);

                                SubCategoryModel subCategoryModel = new SubCategoryModel();
                                subCategoryModel.setSub_category(subCategoryObject.getString("subcategory"));
                                subCategoryModel.setTime(subCategoryObject.getString("duriation"));
                                subCategoryModels.add(subCategoryModel);


                            }
                            model1.setSubcategory(subCategoryModels);
                            itemList.add(model1);
                            System.out.println("subCategoryModels=="+subCategoryModels);

                            OpenGroupAdapter personalAdapter = new OpenGroupAdapter(FamilyActivity.this, itemList);
                            rv_chartreport.setAdapter(personalAdapter);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(FamilyActivity.this);
                            rv_chartreport.setLayoutManager(layoutManager);
                        }

                    } else {
                        Toast.makeText(FamilyActivity.this, "Activity not found", Toast.LENGTH_SHORT).show();
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
                        // Handle the error response here
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(FamilyActivity.this);
        requestQueue.add(jsObjRequest);
    }
    public void openmentods() {
        final Map<String, String> params = new HashMap<>();
        educationlist.setVisibility(View.VISIBLE);
        String baseUrl = ProductConfig.education_group_list+
                "?user_id="+Bsession.getInstance().getUser_id(FamilyActivity.this)
                +"&group_id="+group_id;
        System.out.println("baseurl=="+baseUrl);
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        itemList = new ArrayList<>();
                        JSONArray jsonlist = jsonResponse.getJSONArray("storeList");
                        for (int i = 0;i < jsonlist.length(); i++) {
                            JSONObject jsonObject1 = jsonlist.getJSONObject(i);
                            OpenModel model1 = new OpenModel();
                            model1.setUser_name(jsonObject1.getString("user_name"));
                            model1.setDescription(jsonObject1.getString("description"));
                            JSONArray jArrItemData = new JSONArray(jsonObject1.getString("list"));
                            List<SubCategoryModel> subCategoryModels = new ArrayList<>();

                            for (int j = 0; j < jArrItemData.length(); j++) {
                                JSONObject subCategoryObject = jArrItemData.getJSONObject(j);

                                SubCategoryModel subCategoryModel = new SubCategoryModel();
                                subCategoryModel.setSub_category(subCategoryObject.getString("subject"));
                                subCategoryModel.setTime(subCategoryObject.getString("duriation"));
                                subCategoryModels.add(subCategoryModel);

                            }
                            model1.setSubcategory(subCategoryModels);
                            itemList.add(model1);
                            System.out.println("subCategoryModels=="+subCategoryModels);

                            OpenGroupAdapter personalAdapter = new OpenGroupAdapter(FamilyActivity.this, itemList);
                            educationlist.setAdapter(personalAdapter);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(FamilyActivity.this);
                            educationlist.setLayoutManager(layoutManager);
                        }

                    } else {
                        Toast.makeText(FamilyActivity.this, "Activity not found", Toast.LENGTH_SHORT).show();
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
                        // Handle the error response here
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(FamilyActivity.this);
        requestQueue.add(jsObjRequest);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FamilyActivity.this, PersonalExistingActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}