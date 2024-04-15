package com.about.kby.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.about.kby.Adapter.ExistingGroupAdapter;
import com.about.kby.Bsession;
import com.about.kby.DateActivity;
import com.about.kby.ProductConfig;
import com.about.kby.R;
import com.about.kby.fragment.PersonalInputFragment;
import com.about.kby.model.EducationModel;
import com.about.kby.model.ExistingModel;
import com.about.kby.model.PersonalModel;
import com.about.kby.model.PersonalPojo;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EducationNewActivity extends AppCompatActivity {
    Button ed_input,ed_insight,cretegroup;
    EditText edt_gname,edt_gphno,subject;
    ImageView backimage,image;
    Button crete;
    String accnum;
    String accname,subjects="";
    String bname;
    TextView txt_toolbar;
    ProgressDialog progressDialog;
    TableLayout tableLayout;
    String formattedDate="";
    RecyclerView recycler_table,recycler_tableaddrow;
    private List<EducationModel> itemList = new ArrayList<>();
    final ArrayList<String> idList = new ArrayList<>();
     String eduid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_new);

        ed_input =  findViewById(R.id.ed_input);
        cretegroup =  findViewById(R.id.cretegroup);
        backimage =  findViewById(R.id.backimage);
        image =  findViewById(R.id.image);
        edt_gphno =  findViewById(R.id.edt_gphno);
        edt_gname =  findViewById(R.id.edt_gname);
        subject =  findViewById(R.id.subject);
        crete =  findViewById(R.id.crete);
        txt_toolbar =  findViewById(R.id.txt_toolbar);
        tableLayout =  findViewById(R.id.tableLayout);
        ed_insight =  findViewById(R.id.ed_insight);
        recycler_table =  findViewById(R.id.recycler_table);

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Updating.....");

        ed_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EducationNewActivity.this, EducationExistingActivity.class);
                startActivity(intent);
            }
        });
        cretegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gropname = edt_gname.getText().toString();
                String mobile_email = edt_gphno.getText().toString();
                if (gropname.isEmpty()){
                    edt_gname.setError("Enter Group Name *");
                    edt_gname.requestFocus();
                    return;
                }
                education_group();

            }
        });
        Date currentDate = new Date();

        // Define the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

        // Format the current date according to the specified format
         formattedDate = dateFormat.format(currentDate);

        // Print or use the formatted date as needed
        Log.d("CurrentDate", "Formatted Date: " + formattedDate);
        crete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crete.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
                EducationNewActivity registerActivity = EducationNewActivity.this;
                registerActivity.accname = registerActivity.edt_gname.getText().toString().trim();
                EducationNewActivity registerActivity2 = EducationNewActivity.this;
                registerActivity2.accnum = registerActivity2.edt_gphno.getText().toString().trim();

                if (accname.isEmpty()) {
                    edt_gname.setError("*Enter your Group Name");
                    edt_gname.requestFocus();
                }else if (accname == null || accname == "") {
                    Toast.makeText(EducationNewActivity.this, "Please enter required information", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EducationNewActivity.this, "create", Toast.LENGTH_SHORT).show();
                    if (subject.getText().toString().isEmpty()){
                        Toast.makeText(EducationNewActivity.this, "Add Subject", Toast.LENGTH_SHORT).show();
                       return;
                    }else {
                        subjectadds(formattedDate);
                    }

                }
            }
        });

        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EducationNewActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EducationNewActivity.this, DateActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout addbutton = findViewById(R.id.addbutton);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gropname = edt_gname.getText().toString();
                String mobile_email = edt_gphno.getText().toString();
                if (gropname.isEmpty()){
                    edt_gname.setError("Enter Group Name *");
                    edt_gname.requestFocus();
                    return;
                }
                if (eduid.isEmpty()){
                    education_group();

                } else {
                    if (subject.getText().toString().isEmpty()){
                        Toast.makeText(EducationNewActivity.this, "Add Subject", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    subjectadd(formattedDate);

                }



            }
        });
        //education_group();
        txt_toolbar.setText("Education");
    }

    private void subjectadds( String date) {
        final Map<String, String> params = new HashMap<>();
        String customer_id = Bsession.getInstance().getUser_id(this);

        progressDialog.show();
        StringRequest jsObjRequest = new StringRequest(0, ProductConfig.subject_add + ("?subject="+subject.getText().toString())+("&user_id=" + customer_id) + ("&grop_id=" + eduid)+("&duriation="+"")+("&date="+date)  , new Response.Listener<String>() {
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        // eduid = jsonResponse.getString("id");
                        //  Toast.makeText(getApplicationContext(), "Education group created successfully", Toast.LENGTH_LONG).show();
                        subjectlist();
                        Intent intent=new Intent(EducationNewActivity.this,EducationExistingActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(), "Group not created...please check ", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    System.out.println("Response=="+response);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
                progressDialog.dismiss();
            }
        }) {
            /* access modifiers changed from: protected */
            public Map<String, String> getParams() {
                return params;
            }
        };
        Volley.newRequestQueue(this).add(jsObjRequest);
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));

    }

    private void subjectadd(String date) {
        final Map<String, String> params = new HashMap<>();
        String customer_id = Bsession.getInstance().getUser_id(this);

        progressDialog.show();
        StringRequest jsObjRequest = new StringRequest(0, ProductConfig.subject_add + ("?subject="+subject.getText().toString())+("&user_id=" + customer_id) + ("&grop_id=" + eduid)+("&date="+date)  , new Response.Listener<String>() {
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                       // eduid = jsonResponse.getString("id");
                        //  Toast.makeText(getApplicationContext(), "Education group created successfully", Toast.LENGTH_LONG).show();
                            subjectlist();

                    } else {
                        Toast.makeText(getApplicationContext(), "Group not created...please check ", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    System.out.println("Response=="+response);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
                progressDialog.dismiss();
            }
        }) {
            /* access modifiers changed from: protected */
            public Map<String, String> getParams() {
                return params;
            }
        };
        Volley.newRequestQueue(this).add(jsObjRequest);
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));

    }

    private void subjectlist() {
        final Map<String, String> params = new HashMap<>();
        String baseUrl = ProductConfig.subjectlist+"?user_id="+Bsession.getInstance().getUser_id(EducationNewActivity.this)+"&grop_id="+eduid+"&date="+formattedDate;
        System.out.println("base=="+baseUrl);
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        itemList = new ArrayList<>();
                        JSONArray jsonlist = jsonResponse.getJSONArray("list");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject1 = jsonlist.getJSONObject(j);
                            EducationModel model1 = new EducationModel();
                            model1.setId(jsonObject1.getString("id"));
                            model1.setSubject(jsonObject1.getString("subject"));
                            subject.setText("");


                            itemList.add(model1);
                            EducationAdapter personalAdapter = new EducationAdapter(EducationNewActivity.this, itemList);
                            recycler_table.setAdapter(personalAdapter);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(EducationNewActivity.this);
                            recycler_table.setLayoutManager(layoutManager);
                        }

                    } else {
                        Toast.makeText(EducationNewActivity.this, "Activity not found", Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(EducationNewActivity.this);
        requestQueue.add(jsObjRequest);
    }

    public void education_group() {
        final Map<String, String> params = new HashMap<>();
        String customer_id = Bsession.getInstance().getUser_id(this);
        this.accname = this.edt_gname.getText().toString().trim();
        this.accnum = this.edt_gphno.getText().toString().trim();


        progressDialog.show();
        StringRequest jsObjRequest = new StringRequest(0, ProductConfig.education_new + ("?user_id="+customer_id)+("&grp_name=" + this.accname)   , new Response.Listener<String>() {
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        eduid = jsonResponse.getString("id");
                        Toast.makeText(getApplicationContext(), "Education group created successfully", Toast.LENGTH_LONG).show();
                        subject.setVisibility(View.VISIBLE);

                    } else if (jsonResponse.has("faile") && jsonResponse.getString("faile").equals("0")){
                        Toast.makeText(getApplicationContext(), " Invalid user", Toast.LENGTH_LONG).show();
                        //finish();
                    }
                    System.out.println("Response=="+response);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
                progressDialog.dismiss();
            }
        }) {
            /* access modifiers changed from: protected */
            public Map<String, String> getParams() {
                return params;
            }
        };
        Volley.newRequestQueue(this).add(jsObjRequest);
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
    }

    public class EducationAdapter extends RecyclerView.Adapter<EducationNewActivity.EducationAdapter.ItemViewHolder> {
        private List<EducationModel> itemList;
        private Context mContext;

        public EducationAdapter(Context mContext, List<EducationModel> itemList) {
            this.mContext = mContext;
            this.itemList = itemList;

        }

        @NonNull
        @Override
        public EducationNewActivity.EducationAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.education_list, parent, false);
            return new EducationNewActivity.EducationAdapter.ItemViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull EducationNewActivity.EducationAdapter.ItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
            EducationModel model = itemList.get(position);
            holder.subjectw.setText(itemList.get(position).getSubject());

        }


        @Override
        public int getItemCount() {
            return itemList.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            EditText subjectw;

            public ItemViewHolder(View itemView) {
                super(itemView);
                subjectw=itemView.findViewById(R.id.subject);

            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EducationNewActivity.this, EducationExistingActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}