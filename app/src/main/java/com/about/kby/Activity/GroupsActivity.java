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
import android.os.Handler;
import android.text.Editable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.about.kby.Bsession;
import com.about.kby.DateActivity;
import com.about.kby.ProductConfig;
import com.about.kby.R;
import com.about.kby.model.EducationModel;
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

public class GroupsActivity extends AppCompatActivity {
    LinearLayout ll_personal,ll_opencircle;
    ImageView backimage,image;
    TextView txt_toolbar;
    HorizontalCalendar horizontalCalendar;
    private List<EducationModel> itemList = new ArrayList<>();

    Spinner fromcalantor;
    String selectedDateStr;
    Button today;
    TextView txt_date;
    static ProgressDialog progressDialog;
    EditText txt_note;
    LinearLayout addbutton,editbutton;
    RecyclerView recycleview;
    LinearLayout linearlayour;
    String currentNote;
    int currentNotePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        ll_opencircle = findViewById(R.id.ll_opencircle);
        recycleview = findViewById(R.id.recycleview);
        linearlayour = findViewById(R.id.linearlayour);
        linearlayour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_note.requestFocus();
            }
        });
        ll_personal = findViewById(R.id.ll_personal);
        addbutton = findViewById(R.id.addbutton);
        txt_toolbar = findViewById(R.id.txt_toolbar);
        backimage = findViewById(R.id.backimage);
        image = findViewById(R.id.image);
        txt_note = findViewById(R.id.txt_note);
        txt_date = findViewById(R.id.txt_date);
        today = findViewById(R.id.today);
        Calendar startDate = getInstance();
        startDate.add(Calendar.MONTH, -2);
        Calendar endDate = getInstance();
        endDate.add(Calendar.MONTH, 2);
        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");
        final Calendar defaultSelectedDate = getInstance();

        ll_opencircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupsActivity.this,OpenGroupActivity.class);
                startActivity(intent);
            }
        });
        ll_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupsActivity.this, PersonalExistingActivity.class);
                startActivity(intent);
            }
        });
        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupsActivity.this, DateActivity.class);
                startActivity(intent);
            }
        });
        txt_toolbar.setText("Notes");

        Drawable drawable = ContextCompat.getDrawable(GroupsActivity.this, R.drawable.buttonadds);

        horizontalCalendar = new HorizontalCalendar.Builder(GroupsActivity.this, R.id.calendarView)
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
                getnotereport(selectedDateStr);

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
                getnotereport(selectedDateStr);
                // Do something with today's date, for example, display it in a toast
                // Toast.makeText(getContext(), "Today's date: " + todayDateStr, Toast.LENGTH_SHORT).show();
            }
        });
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = txt_note.getText().toString();
                if (subject.isEmpty()) {
                    txt_note.requestFocus();
                    Toast.makeText(GroupsActivity.this, "Note is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                noteadd(subject);
                System.out.println("Subject added");
            }
        });
        getnotereport(selectedDateStr);

    }

    private void noteadd(String subject) {
        final Map<String, String> params = new HashMap<>();
        String customer_id = Bsession.getInstance().getUser_id(GroupsActivity.this);

        //progressDialog.show();
        StringRequest jsObjRequest = new StringRequest(0, ProductConfig.notesadd + ("?userid=" + customer_id) + ("&date=" + selectedDateStr)+("&subject="+subject), new Response.Listener<String>() {
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        // eduid = jsonResponse.getString("id");
                        recycleview.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Code to be executed after 5 seconds
                                // For example, you can put your code here
                                // or call a method to execute the desired functionality
                                getnotereport(selectedDateStr);

                            }
                        }, 1000);
                        txt_note.setText("");
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


    private void getnotereport(String selectedDateStr) {
        final Map<String, String> params = new HashMap<>();
        String para_str = "?userid=" + Bsession.getInstance().getUser_id(GroupsActivity.this);
        String para2 = "&date=" + selectedDateStr;
        System.out.println("onDateSelected==" + selectedDateStr);
        String baseUrl = ProductConfig.notesreport + para_str + para2;
        System.out.println("base=" + baseUrl);
        progressDialog.show();
        recycleview.setVisibility(View.VISIBLE);

        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        JSONArray jsonlist = jsonResponse.getJSONArray("list");
                        itemList.clear(); // Clear the list before adding new items
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject1 = jsonlist.getJSONObject(j);
                            EducationModel model1 = new EducationModel();
                            model1.setId(jsonObject1.getString("id"));
                            model1.setSubject(jsonObject1.getString("title"));
                            itemList.add(model1);
                        }
                        // Move adapter setup outside the loop
                        SubjecAdapter personalAdapter = new SubjecAdapter(GroupsActivity.this, itemList);
                        recycleview.setAdapter(personalAdapter);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(GroupsActivity.this);
                        recycleview.setLayoutManager(layoutManager);
                    } else {
                        recycleview.setVisibility(View.GONE);
                        //Toast.makeText(GroupsActivity.this, "Activity not found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss(); // Dismiss the dialog in case of exception
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                        progressDialog.dismiss(); // Dismiss the dialog in case of error
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(GroupsActivity.this);
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
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.educationadd, parent, false);
            return new SubjecAdapter.ItemViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull SubjecAdapter.ItemViewHolder holder, @SuppressLint("RecyclerView") int position) {

            EducationModel model = pesrsonalList.get(position);
            holder.txt_notes.setText(pesrsonalList.get(position).getSubject());
            holder.txt_notes.setOnClickListener(v -> {
            });
            editbutton = findViewById(R.id.editbutton);
            editbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    currentNote = pesrsonalList.get(position).getSubject();
                    currentNotePosition = position;
                    txt_note.setText(currentNote);

                }
            });

          /*  holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removem(pesrsonalList.get(position).getId());
                }
            });*/


        }



        @Override
        public int getItemCount() {
            return pesrsonalList.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView txt_notes;
            ImageView remove;

            public ItemViewHolder(View itemView) {
                super(itemView);

                remove = itemView.findViewById(R.id.remove);
                txt_notes = itemView.findViewById(R.id.txt_notes);


            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GroupsActivity.this, HomeActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}