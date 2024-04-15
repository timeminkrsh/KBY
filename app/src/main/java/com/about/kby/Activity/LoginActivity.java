package com.about.kby.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.about.kby.Bsession;
import com.about.kby.ProductConfig;
import com.about.kby.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Button login;
    EditText mobile;
    String moble;
    String phone;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Logging in.....");

        login = findViewById(R.id.login);
        mobile = findViewById(R.id.mobile);

        NetworkConnection();

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                login.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this.getApplicationContext(), R.anim.fade_in));
                LoginActivity loginActivity = LoginActivity.this;
                loginActivity.phone = loginActivity.mobile.getText().toString();
                if (phone.isEmpty()) {
                    mobile.setError("*Enter Mobile Number/Email ID");
                    mobile.requestFocus();
                }else if (phone == null || phone == "") {
                    Toast.makeText(LoginActivity.this, "Please enter Mobile Number/Email ID", Toast.LENGTH_SHORT).show();
                } else {
                    sendOTP();
                }
            }
        });
    }

    public void sendOTP() {
        String para1="?user_mobile="+mobile.getText().toString().trim();
        System.out.println("mobile=="+phone);
        final Map<String, String> params = new HashMap<>();
        progressDialog.show();
        Volley.newRequestQueue(this).add(new StringRequest(0, ProductConfig.userlogin + para1 , new Response.Listener<String>() {
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (!jsonResponse.has(FirebaseAnalytics.Param.SUCCESS) || !jsonResponse.getString(FirebaseAnalytics.Param.SUCCESS).equals("1")) {
                        Toast.makeText(LoginActivity.this.getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
                        return;
                    }
                    //ProductConfig.userModel = new UserModel(jsonResponse);
                    Bsession.getInstance().initialize(LoginActivity.this,"",
                            "",
                            jsonResponse.getString("user"),
                           "",
                            "");
                    phone = jsonResponse.getString("user");
                    Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, OTPActivity.class);
                    intent.putExtra("user_mobile", LoginActivity.this.moble);
                    Toast.makeText(LoginActivity.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                    System.out.println("Response=="+response);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoginActivity.this.progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
                progressDialog.dismiss();
            }
        }) {
            /* access modifiers changed from: protected */
            public Map<String, String> getParams() { return params; } });
    }

    private void NetworkConnection() {
        NetworkInfo info = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null) {
            Toast.makeText(this, "Please check your wireless connection and try again.", Toast.LENGTH_SHORT).show();
        } else if (!info.isConnected()) {
            Toast.makeText(this, "Please check your Network connection and try again.", Toast.LENGTH_SHORT).show();
        }
    }

}