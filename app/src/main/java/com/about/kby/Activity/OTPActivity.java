package com.about.kby.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.about.kby.Bsession;
import com.about.kby.ProductConfig;
import com.about.kby.R;
import com.about.kby.model.UserModel;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OTPActivity extends AppCompatActivity {
    Button verify;
    String phone;
    String message;
    String otp;
    EditText ot;
    PinView pinview;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);

        verify = findViewById(R.id.verify);
        pinview = findViewById(R.id.pinview);
        ot = findViewById(R.id.ot);

        ProgressDialog progressDialog = new ProgressDialog(OTPActivity.this);
        progressDialog.setMessage("Loading....");

        String mobile = Bsession.getInstance().getUser_mobile(OTPActivity.this);
        ot.setText(mobile);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpstr = String.valueOf(pinview.getText().toString());
                otp = otpstr;
                if (otpstr != null && otpstr != "" && otpstr.length() >= 4) {

                    phone = Bsession.getInstance().getUser_mobile(OTPActivity.this);
                    System.out.println("phone;;" + phone);
                    final Map<String, String> params = new HashMap<>();
                    progressDialog.show();

                    String para_str = "?user_mobile=" + phone;
                    String para_str1 = "&otp=" + pinview.getText().toString();
                    System.out.println("responee==" + pinview.getText().toString());
                    String baseUrl = ProductConfig.userotpverify + para_str + para_str1;

                    StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response.toString());
                            try {
                                progressDialog.dismiss();
                                JSONObject jsonResponse = new JSONObject(response);

                                if (jsonResponse.has("userstatus") && jsonResponse.getString("userstatus").equals("0")) {
                                    ProductConfig.userModel = new UserModel(jsonResponse);
                                    Bsession.getInstance().initialize(OTPActivity.this,
                                            jsonResponse.getString("user_id"), "",
                                            jsonResponse.getString("user"),
                                            "", "");
                                    phone = jsonResponse.getString("user");
                                    message = jsonResponse.getString("message");
                                    Toast.makeText(OTPActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(OTPActivity.this, HomeActivity.class);
                                    i.putExtra("user_mobile", phone);
                                    startActivity(i);
                                    finish();
                                } else if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")){
                                    Bsession.getInstance().initialize(OTPActivity.this,
                                            jsonResponse.getString("user_id"),
                                            jsonResponse.getString("user_name"),
                                            jsonResponse.getString("user"),
                                            "", "");
                                    phone = jsonResponse.getString("user");
                                    message = jsonResponse.getString("message");
                                    Toast.makeText(OTPActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(OTPActivity.this, HomeActivity.class);
                                    i.putExtra("user_mobile", phone);
                                    startActivity(i);
                                    finish();
                                } else if (jsonResponse.has("success") && jsonResponse.getString("success").equals("0")){
                                    Toast.makeText(OTPActivity.this, "Incorrect OTP entered", Toast.LENGTH_LONG).show();
                                    AlertDialog.Builder dialog2 = new AlertDialog.Builder(OTPActivity.this, R.style.AlertDialogTheme);
                                    dialog2.setTitle("' "  + OTPActivity.this.pinview.getText().toString().trim() + " ' Incorrect OTP !");
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("You have entered the wrong OTP. Please enter correct OTP");
                                    dialog2.setMessage(sb.toString()).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    }).show();
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
                    RequestQueue requestQueue = Volley.newRequestQueue(OTPActivity.this);
                    requestQueue.add(jsObjRequest);
                } else {
                    Toast.makeText(OTPActivity.this, "Invalid OTP..", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

}