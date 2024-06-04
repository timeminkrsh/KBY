package com.about.kby.Activity;

import static java.util.Calendar.getInstance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.about.kby.Bsession;
import com.about.kby.DateActivity;
import com.about.kby.ProductConfig;
import com.about.kby.R;
import com.about.kby.model.EducationModel;
import com.about.kby.model.ExistingModel;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class EducationExistingActivity extends AppCompatActivity {
    Button ed_input,ed_insight;
    ImageView backimage,image,img_nogrp;
    static RecyclerView rv_group;
    private List<EducationModel> itemList = new ArrayList<>();
    static ProgressDialog progressDialog;
    static List<ExistingModel> existinglist = new ArrayList<>();
    TextView txt_toolbar;
    LinearLayout ll_nogrp,existig_grp;
    HorizontalCalendar horizontalCalendar;
    Spinner fromcalantor;
    public static  String selectedDateStr;
    Button today;
    LinearLayout addbutton;
    TextView txt_date;
    TableLayout tableLayout;
    RecyclerView recycleview;
    CheckBox checkbox;
    EditText edittext;
        String status="0";

    String subject;
    ImageView   remove;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_existing);

        ed_input = findViewById(R.id.ed_input);
        remove = findViewById(R.id.remove);
        //manic
        recycleview=findViewById(R.id.recycleview);
        tableLayout = findViewById(R.id.tableLayout);
        addbutton = findViewById(R.id.addbutton);
        checkbox = findViewById(R.id.checkbox);
        edittext = findViewById(R.id.edittext);

        backimage = findViewById(R.id.backimage);
        image = findViewById(R.id.image);
        txt_toolbar = findViewById(R.id.txt_toolbar);
        ed_insight = findViewById(R.id.ed_insight);
        rv_group = findViewById(R.id.rv_group);
        img_nogrp = findViewById(R.id.img_nogrp);
        ll_nogrp = findViewById(R.id.ll_nogrp);
        existig_grp = findViewById(R.id.existig_grp);
        fromcalantor = findViewById(R.id.fromcalantor);
        today = findViewById(R.id.today);
        txt_date = findViewById(R.id.txt_date);

        Calendar startDate = getInstance();
        startDate.add(Calendar.MONTH, -2);
        Calendar endDate = getInstance();
        endDate.add(Calendar.MONTH, 2);
        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");
        final Calendar defaultSelectedDate = getInstance();
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableLayout.setVisibility(View.GONE);
            }
        });
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableLayout.setVisibility(View.VISIBLE);
                subject = edittext.getText().toString();
                if (subject.isEmpty()) {
                    edittext.requestFocus();
                    return;
                }
                // Set status based on checkbox state
                status = checkbox.isChecked() ? "1" : "0";
                // Call subjectadd method
                subjectadd(subject, status);
            }
        });

        ed_insight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EducationExistingActivity.this, EducationNewActivity.class);
                startActivity(intent);
            }
        });
        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EducationExistingActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EducationExistingActivity.this, DateActivity.class);
                startActivity(intent);
            }
        });
        txt_toolbar.setText("To-Do List");
        //new
        Drawable drawable = ContextCompat.getDrawable(EducationExistingActivity.this, R.drawable.buttonadds);

        horizontalCalendar = new HorizontalCalendar.Builder(EducationExistingActivity.this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .configure()
                .formatTopText("")
                .formatMiddleText("dd")
                .formatBottomText("EEE")
                .showTopText(true)
                .showBottomText(true)
                .textColor(Color.WHITE, Color.WHITE)
                .colorTextMiddle(Color.WHITE, Color.parseColor("#000000"))
                .selectedDateBackground(drawable)
                .textSize(14, 14, 14)
                .end()
                .defaultSelectedDate(defaultSelectedDate)
                .build();

        Log.i("Default Date", DateFormat.format("EEE, MMM d, yyyy", defaultSelectedDate).toString());
        selectedDateStr = DateFormat.format("yyyy-MM-dd", defaultSelectedDate).toString();
        txt_date.setText(DateFormat.format("d-MMM-yyyy", defaultSelectedDate).toString());

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                selectedDateStr = DateFormat.format("yyyy-MM-dd", date).toString();
                //   Toast.makeText(getContext(), selectedDateStr + " selected!", Toast.LENGTH_SHORT).show();
                Log.i("onDateSelected", selectedDateStr + " - Position = " + position);
                txt_date.setText(DateFormat.format("d-MMM-yyyy", date).toString());
                //edittext.setText("");
                getSubject(selectedDateStr);

            }

        });

        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalCalendar.goToday(false);
                Calendar currentDate = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                selectedDateStr = dateFormat.format(currentDate.getTime());
                System.out.println("yyyy-M-d==" + selectedDateStr);
                txt_date.setText("Today");
                edittext.setText("");

                getSubject(selectedDateStr);

                // Do something with today's date, for example, display it in a toast
                // Toast.makeText(getContext(), "Today's date: " + todayDateStr, Toast.LENGTH_SHORT).show();
            }
        });
        getSubject(selectedDateStr);

        }
    private void subjectadd(String subject, String status) {
        final Map<String, String> params = new HashMap<>();
        String customer_id = Bsession.getInstance().getUser_id(EducationExistingActivity.this);

        //progressDialog.show();
        StringRequest jsObjRequest = new StringRequest(0, ProductConfig.subject_add + ("?userid=" + customer_id) + ("&date=" + selectedDateStr) + ("&subject=" + subject)+("&status="+status), new Response.Listener<String>() {
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        // eduid = jsonResponse.getString("id");

                        getSubject(selectedDateStr);
                        edittext.setText("");
                        tableLayout.setVisibility(View.VISIBLE);
                    } else {

                    }
                    System.out.println("Response==" + response);
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
    public void getSubject(String todayDateStr) {
        final Map<String, String> params = new HashMap<>();
        recycleview.setVisibility(View.VISIBLE);
        String para_str = "?userid=" + Bsession.getInstance().getUser_id(EducationExistingActivity.this);
        String para2 = "&date=" + todayDateStr;
        System.out.println("onDateSelected==" + todayDateStr);
        String baseUrl = ProductConfig.subjectlist + para_str + para2;
        System.out.println("base=" + baseUrl);
        progressDialog.show();
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        itemList = new ArrayList<>();
                        recycleview.setVisibility(View.VISIBLE);

                        JSONArray jsonlist = jsonResponse.getJSONArray("list");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject1 = jsonlist.getJSONObject(j);
                            EducationModel model1 = new EducationModel();
                            model1.setId(jsonObject1.getString("id"));
                            model1.setSubject(jsonObject1.getString("title"));
                            model1.setDuriation(jsonObject1.getString("status"));
                            itemList.add(model1);
                            SubjecAdapter personalAdapter = new SubjecAdapter(EducationExistingActivity.this, itemList);
                            recycleview.setAdapter(personalAdapter);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(EducationExistingActivity.this);
                            recycleview.setLayoutManager(layoutManager);
                        }
                    } else {

                        recycleview.setVisibility(View.GONE);
                        //Toast.makeText(getContext(), "Activity not found", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(EducationExistingActivity.this);
        requestQueue.add(jsObjRequest);

    }

    public class SubjecAdapter extends RecyclerView.Adapter<SubjecAdapter.ItemViewHolder> {
        private List<EducationModel> pesrsonalList;
        private Context mContext;
        String duriation = "", mParam3;
        String status="";
        public SubjecAdapter(Context mContext, List<EducationModel> pesrsonalList) {
            this.mContext = mContext;
            this.pesrsonalList = pesrsonalList;
        }


        @NonNull
        @Override
        public SubjecAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.education_list, parent, false);
            return new SubjecAdapter.ItemViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull SubjecAdapter.ItemViewHolder holder, @SuppressLint("RecyclerView") int position) {

            EducationModel model = pesrsonalList.get(position);
                holder.edittext.setText(pesrsonalList.get(position).getSubject());
             String checkboxval=   pesrsonalList.get(position).getDuriation();
                if (checkboxval.equalsIgnoreCase("0")){
                    holder.checkbox.setChecked(false);
                }else {
                    holder.checkbox.setChecked(true);
                }

            holder.remove.setOnClickListener(v ->  {
                    removem(pesrsonalList.get(position).getId());
            });

            holder.edittext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // This method is called to notify you that, within s, the count characters beginning at start are about to be replaced by new text with length after.
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // This method is called to notify you that, within s, the count characters beginning at start have just replaced old text that had length before.
                }

                @Override
                public void afterTextChanged(Editable s) {

                    // This method is called to notify you that, somewhere within s, the text has been changed.
                    dubjectupdate(pesrsonalList.get(position).getId(), holder.edittext.getText().toString(),status);

                }
            });
           holder.checkbox.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (holder.checkbox.isChecked()){
                       status="1";
                       System.out.println("111111check");
                   }else{
                       status="0";
                       System.out.println("22222222check");

                   }
                   dubjectupdate(pesrsonalList.get(position).getId(), holder.edittext.getText().toString(),status);

               }
           });

        }

        private void removem(String id) {

            final Map<String, String> params = new HashMap<>();
            String baseUrl = ProductConfig.removeeduca + "?subjectid=" + id;
            System.out.println("baseurl="+baseUrl);
            final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Response", response.toString());
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                            Toast.makeText(mContext, "Removed", Toast.LENGTH_SHORT).show();
                            EducationExistingActivity.this.getSubject(selectedDateStr);
                            String subject = edittext.getText().toString();
                            if (subject.isEmpty()){
                                tableLayout.setVisibility(View.GONE);

                            }

                        } else {
                          //  recycleview.setVisibility(View.GONE);
                          //  Toast.makeText(mContext, "Activity not found", Toast.LENGTH_SHORT).show();
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

            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(jsObjRequest);
        }

        private void dubjectupdate(String id, String duriation, String status) {
            final Map<String, String> params = new HashMap<>();
            String para2 = "?subjectid=" + id;
            String para3 = "&subject=" + duriation;
            String para4 = "&status=" + status;

            System.out.println("para2==" + para2);
            String baseUrl = ProductConfig.updatesubjectdurin + para2 + para3+para4;

            StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Response", response.toString());
                    try {
                        //progressDialog.dismiss();
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                            // Assuming that the TextView is inside the tableview layout
                        } else {
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub
                    Log.e("Error", error.toString());
                    // progressDialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(jsObjRequest);

        }


        @Override
        public int getItemCount() {
            return pesrsonalList.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            CheckBox checkbox;
            EditText edittext;
            ImageView remove;

            public ItemViewHolder(View itemView) {
                super(itemView);

                checkbox = itemView.findViewById(R.id.checkbox);
                remove = itemView.findViewById(R.id.remove);
                edittext = itemView.findViewById(R.id.edittext);


            }
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EducationExistingActivity.this, HomeActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}