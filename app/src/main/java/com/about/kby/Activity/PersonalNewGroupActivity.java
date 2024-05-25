package com.about.kby.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.about.kby.Adapter.PersonalGroupAdapter;
import com.about.kby.Bsession;
import com.about.kby.ProductConfig;
import com.about.kby.R;
import com.about.kby.model.ChecklistModel;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalNewGroupActivity extends AppCompatActivity {

    ImageView backimage;
    TextView txt_toolbar;
    EditText group_name,mobile_num;
    Button btn_create;
    String accnum;
    String accname;
    ProgressDialog progressDialog;
    Button exist_group,new_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_newgroup);
        backimage = findViewById(R.id.backimage);
        txt_toolbar = findViewById(R.id.txt_toolbar);
        new_group = findViewById(R.id.new_group);
        group_name = findViewById(R.id.group_name);
        mobile_num = findViewById(R.id.mobile_num);
        btn_create = findViewById(R.id.btn_create);
        exist_group = findViewById(R.id.exist_group);

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Updating.....");

        exist_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalNewGroupActivity.this, PersonalExistingActivity.class);
                startActivity(intent);
            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_create.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
                PersonalNewGroupActivity registerActivity = PersonalNewGroupActivity.this;
                registerActivity.accname = registerActivity.group_name.getText().toString().trim();
                PersonalNewGroupActivity registerActivity2 = PersonalNewGroupActivity.this;
                registerActivity2.accnum = registerActivity2.mobile_num.getText().toString().trim();

                if (accname.isEmpty()) {
                    group_name.setError("*Enter your group name");
                    group_name.requestFocus();
                } else if (accnum.isEmpty()) {
                    mobile_num.setError("*Enter your Mobile number/Email ID");
                    mobile_num.requestFocus();
                } else if (accname == null || accname == "" || accnum == null || accnum == "" ) {
                    Toast.makeText(PersonalNewGroupActivity.this, "Please enter required information", Toast.LENGTH_SHORT).show();
                } else {
                    personal_group();
                }
            }
        });

        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalNewGroupActivity.this, PersonalNewGroupActivity.class);
                startActivity(intent);
            }
        });

        txt_toolbar.setText("Personal");
    }

    public void personal_group() {
        final Map<String, String> params = new HashMap<>();
        String customer_id = Bsession.getInstance().getUser_id(this);
        System.out.println("user=="+customer_id);
        this.accname = this.group_name.getText().toString().trim();
        this.accnum = this.mobile_num.getText().toString().trim();

        progressDialog.show();
        StringRequest jsObjRequest = new StringRequest(0, ProductConfig.personal_new + ("?user_id="+customer_id)+("&grp_name=" + this.accname) + ("&member=" + this.accnum)  , new Response.Listener<String>() {
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        String message = jsonResponse.getString("message");
                        Toast.makeText(getApplicationContext(), "Education group created successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(PersonalNewGroupActivity.this, PersonalExistingActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid user", Toast.LENGTH_LONG).show();

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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PersonalNewGroupActivity.this, GroupsActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}