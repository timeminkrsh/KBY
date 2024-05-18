package com.about.kby.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.about.kby.Bsession;
import com.about.kby.DateActivity;
import com.about.kby.Group2Activity;
import com.about.kby.ProductConfig;
import com.about.kby.R;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    LinearLayout education;
    LinearLayout personal;
    LinearLayout checklist;
    LinearLayout groups;
    Dialog bottomSheetDialog;
    LinearLayout account;
    DrawerLayout drawer;
    EditText et_name;
    MeowBottomNavigation bottomNavigation;
    ProgressDialog progressDialog;
    ImageView menu;

     @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_home);

         education = findViewById(R.id.education);
         checklist = findViewById(R.id.checklist);
         menu = findViewById(R.id.menu);
         drawer = findViewById(R.id.drawer_layout);
         bottomNavigation = findViewById(R.id.bottomNavigation);

         progressDialog = new ProgressDialog(this);
         progressDialog.setMessage("Updating.....");

         String profileUpdate = Bsession.getInstance().getUser_name(HomeActivity.this);

         if(profileUpdate.isEmpty()){
             AlertDialog.Builder builder = new AlertDialog.Builder(this);
             builder.setTitle("Profile Update")
                     .setMessage("Plaese update your profile to proceed?")
                     .setCancelable(false)
                     .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             Intent intent = new Intent(HomeActivity.this, DateActivity.class);
                             startActivity(intent);
                         }
                     })
                     .show();
         }

        education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, EducationExistingActivity.class);
                startActivity(intent);
            }
        });
        personal = findViewById(R.id.personal);
        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PersonalActivity.class);
                startActivity(intent);
            }
        });

        checklist = findViewById(R.id.checklist);
        checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ChecklistAddActivity.class);
                startActivity(intent);
            }
        });
        groups = findViewById(R.id.groups);
        groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, GroupsActivity.class);
                startActivity(intent);
            }
        });
         DrawerLayout drawer = findViewById(R.id.drawer_layout);
         NavigationView navigationView = findViewById(R.id.nav_view);
         ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                 this, drawer,  R.string.navigation_drawer_open, R.string.navigation_drawer_close);
         drawer.addDrawerListener(toggle);
         toggle.syncState();
         navigationView.setNavigationItemSelectedListener(this);
         menu.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 drawer.openDrawer(GravityCompat.START);
             }
         });


         bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.home));
         bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.infos));
         bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.group));
         bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.terms));
         bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
             @Override
             public void onShowItem(MeowBottomNavigation.Model item) {
                 //initialize intent
                 Intent intent = null;

                 if (item.getId() == 4) {
                     intent = new Intent(HomeActivity.this, DateActivity.class);
                 }else if (item.getId() == 3) {
                     intent = new Intent(HomeActivity.this, Group2Activity.class);
                 }else if (item.getId() == 2) {
                     intent = new Intent(HomeActivity.this, AboutusActivity.class);
                 } else if (item.getId() == 1) {
                    // intent = new Intent(HomeActivity.this, HomeActivity.class);
                 }

                 if (intent != null) {
                     startActivity(intent);
                 }
             }
         });

         bottomNavigation.show(1, true);

         bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
             @Override
             public void onClickItem(MeowBottomNavigation.Model item) {
                 //Toast.makeText(getApplicationContext()," You clicked "+ item.getId(), Toast.LENGTH_SHORT).show();
             }
         });

         bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
             @Override
             public void onReselectItem(MeowBottomNavigation.Model item) {

                 //Toast.makeText(getApplicationContext()," You reselected "+ item.getId(), Toast.LENGTH_SHORT).show();
             }
         });

     }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Intent intent = null;

        if (id == R.id.nav_home) {

            intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_support) {
            intent = new Intent(getApplicationContext(), FeedbackActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_about) {

            intent = new Intent(getApplicationContext(), AboutusActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            logoutAlert();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showBottomSheetDialog() {

        bottomSheetDialog = new Dialog(HomeActivity.this);
        bottomSheetDialog.setContentView(R.layout.profile_update);
         et_name = bottomSheetDialog.findViewById(R.id.et_name);
        EditText et_phone = bottomSheetDialog.findViewById(R.id.et_phone);

        Button sumbit = bottomSheetDialog.findViewById(R.id.sumbit);
        //et_name.setText(Bsession.getInstance().getUser_name(getApplicationContext()));
        et_phone.setText(Bsession.getInstance().getUser_mobile(getApplicationContext()));

        sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString().trim();
                if (name.isEmpty()) {
                    et_name.setError("*Enter your name");
                    et_name.requestFocus();             
                }
                else {
                    final Map<String, String> params = new HashMap<>();
                    String para2 = "?user_id=" + Bsession.getInstance().getUser_id(HomeActivity.this);
                    String para3 = "&user_name=" + et_name.getText().toString().trim();
                    System.out.println("gggggg"+para3);
                    System.out.println("gggggg"+para2);
                    progressDialog.show();
                    String baseUrl = ProductConfig.profile + para2 + para3;

                    StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response.toString());
                            try {
                                progressDialog.dismiss();
                                JSONObject jsonResponse = new JSONObject(response);

                                if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                                    Toast.makeText(HomeActivity.this, "Your profile has been updated", Toast.LENGTH_LONG).show();
                                    Bsession.getInstance().initialize(HomeActivity.this,
                                            jsonResponse.getString("user_id"),
                                            jsonResponse.getString("user_name"),
                                            Bsession.getInstance().getUser_mobile(HomeActivity.this),
                                            "", "");
                                    bottomSheetDialog.dismiss();
                                } else {
                                    Toast.makeText(HomeActivity.this, "Something went wrong your profile ... Try it again", Toast.LENGTH_LONG).show();
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
                    RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
                    requestQueue.add(jsObjRequest);

                }
            }
        });

       bottomSheetDialog.show();
    }



    private void loadFragment(Fragment fragment) {
        //replace the fragment
        getSupportFragmentManager()
                .beginTransaction()
               // .replace(R.id.nav_host_fragment_container,fragment, null)
                .commit();
    }

    private void logoutAlert() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setMessage(getApplicationContext().getResources().getString(R.string.alert_want_logout))
                .setCancelable(false)
                .setPositiveButton(getApplicationContext().getResources().getString(R.string.alert_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                                logout();
                            }
                        })
                .setNegativeButton(getApplicationContext().getResources().getString(R.string.alert_no),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });
        final android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen((int) GravityCompat.START)) {
            drawer.closeDrawer((int) GravityCompat.START);
            return;
        }
        AlertDialog.Builder BackAlertDialog = new AlertDialog.Builder(HomeActivity.this);
        BackAlertDialog.setTitle((CharSequence) "Activity Exit Alert");
        BackAlertDialog.setMessage((CharSequence) "Are you sure want to exit ?");
        BackAlertDialog.setIcon((int) R.drawable.logo);
        BackAlertDialog.setPositiveButton((CharSequence) "NO", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        BackAlertDialog.setNegativeButton((CharSequence) "YES", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
                finish();
            }

        });
        BackAlertDialog.show();
        super.onBackPressed();

    }
    private void logout() {

        Bsession.getInstance().destroy(HomeActivity.this);
        Toast.makeText(HomeActivity.this, getResources().getString(R.string.logout_success), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(HomeActivity.this, SplashScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish();
    }

    private void replace(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
       // transaction.replace(R.id.frame,fragment);
        transaction.commit();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}