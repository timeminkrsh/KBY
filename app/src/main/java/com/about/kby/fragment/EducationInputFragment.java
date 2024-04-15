package com.about.kby.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.about.kby.Activity.EducationAddActivity;
import com.about.kby.Activity.EducationExistingActivity;
import com.about.kby.Bsession;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EducationInputFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EducationInputFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2, mParam3;
    ImageView edu_more;
    ProgressDialog progressDialog;
    LinearLayout addbutton;
    String group_id;
    EditText simpleTimePicker, category;
    TextView sub_name, team_name;
    RecyclerView recycler_tablelist;
    TableLayout tableLayoutadd;
    EditText today;
    public static String todayDateStr;
    public String currentDateStr;

    private List<EducationModel> itemList = new ArrayList<>();
    String duriation = "";
    TextView txt_date;

    public String admin = "";
    String[] country = {"Add/Remove group", "Exit group", "Delete group"};

    public EducationInputFragment() {
        // Required empty public constructor
    }

    public static EducationInputFragment newInstance(String param1, String para2, String para3) {
        EducationInputFragment fragment = new EducationInputFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, para2);
        args.putString(ARG_PARAM3, para3);
        fragment.setArguments(args);
        return fragment;
    }

    private static EducationInputFragment instance;

    public static EducationInputFragment getInstances() {
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            // System.out.println("param3=="+mParam1);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_education_input, container, false);
        edu_more = view.findViewById(R.id.edu_more);
        team_name = view.findViewById(R.id.team_name);
        addbutton = view.findViewById(R.id.addbutton);
        today = view.findViewById(R.id.today);
        category = view.findViewById(R.id.category);
        simpleTimePicker = view.findViewById(R.id.simpleTimePicker);
        tableLayoutadd = view.findViewById(R.id.tableLayoutadd);
        txt_date = view.findViewById(R.id.txt_date);

        team_name.setText(mParam2);
        instance = this;
        admin = mParam3;
        if (mParam3.equalsIgnoreCase("1")) {
            addbutton.setVisibility(View.VISIBLE);

        } else {
            addbutton.setVisibility(View.GONE);

        }
        recycler_tablelist = view.findViewById(R.id.recycler_tablelist);
        System.out.println("mParam1" + mParam1);
        ProgressDialog progressDialog2 = new ProgressDialog(getContext());
        progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -2);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 2);
        final Calendar defaultSelectedDate = Calendar.getInstance();
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.buttonadds);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
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

        Log.i("Default Date", DateFormat.format("EEE, MM d, yyyy", defaultSelectedDate).toString());
        todayDateStr = DateFormat.format("MM/dd/yyyy", defaultSelectedDate).toString();
        System.out.println("onDateSelected==" + currentDateStr);

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                todayDateStr = DateFormat.format("MM/dd/yyyy", date).toString();
                //Toast.makeText(getContext(), selectedDateStr + " selected!", Toast.LENGTH_SHORT).show();
                Log.i("onDateSelected", todayDateStr + " - Position = " + position);
                System.out.println("onDateSelected==" + todayDateStr);
                txt_date.setText(DateFormat.format("d-MMM-yyyy", date).toString());

                // getSubjectDurations(date);
                getSubject(todayDateStr);

            }

        });

        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalCalendar.goToday(false);
                Calendar currentDate = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                todayDateStr = dateFormat.format(currentDate.getTime());
                txt_date.setText("Today");

                // today.setText(todayDateStr);
                // Do something with today's date, for example, display it in a toast
                // Toast.makeText(getContext(), "Today's date: " + todayDateStr, Toast.LENGTH_SHORT).show();
            }
        });

        simpleTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!category.getText().toString().trim().isEmpty()) {

                    final Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    // Display the selected time in HH:MM format
                                    String formattedTime = String.format("%02d:%02d", hourOfDay, minute);
                                    Toast.makeText(getContext(), "Selected time: " + formattedTime, Toast.LENGTH_SHORT).show();
                                    simpleTimePicker.setText(formattedTime);
                                    duriation = simpleTimePicker.getText().toString();
                                    System.out.println("Selected tim=" + duriation);
                                    subjectadd(duriation);
                                }
                            },
                            hour, minute, true);

                    timePickerDialog.show();
                } else {
                    Toast.makeText(getActivity(), "kindly enter the subject!", Toast.LENGTH_LONG).show();
                }
            }
        });


        getSubject(todayDateStr);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableLayoutadd.setVisibility(View.VISIBLE);
                String subjec = category.getText().toString();
                String tmen = simpleTimePicker.getText().toString();
                if (subjec.isEmpty()) {
                    category.setError("Enter Subject Name *");
                    category.requestFocus();
                } else if (tmen.isEmpty()) {
                    simpleTimePicker.setError("Enter Timer *");
                    simpleTimePicker.requestFocus();
                }

            }
        });
        edu_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(), view);
                popupMenu.inflate(R.menu.education_popup);
                MenuItem addEduMenuItem = popupMenu.getMenu().findItem(R.id.add_edu);
                MenuItem delMenuItem = popupMenu.getMenu().findItem(R.id.delete_edu);
                MenuItem exitMenuItem = popupMenu.getMenu().findItem(R.id.exit_edu);

                // Set visibility based on mParam3 value
                addEduMenuItem.setVisible(mParam3.equals("1"));
                delMenuItem.setVisible(mParam3.equals("1"));
                exitMenuItem.setVisible(!mParam3.equals("1"));

                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (mParam3.equals("1") && menuItem.getItemId() == R.id.add_edu) {
                            Intent intent = new Intent(getContext(), EducationAddActivity.class);
                            intent.putExtra("grp_id", mParam1);
                            startActivity(intent);
                            return true;
                        }
                        if (mParam3.equals("1") && menuItem.getItemId() == R.id.delete_edu) {
                            AlertDialog.Builder BackAlertDialog = new AlertDialog.Builder(getContext());
                            BackAlertDialog.setTitle((CharSequence) "Group Delete Alert");
                            BackAlertDialog.setMessage((CharSequence) "Are you sure want to exit ?");
                            BackAlertDialog.setIcon((int) R.drawable.logo);
                            BackAlertDialog.setPositiveButton((CharSequence) "NO", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            BackAlertDialog.setNegativeButton((CharSequence) "YES", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    delete_grp();
                                }

                            });
                            BackAlertDialog.show();

                        }
                        if (!mParam3.equals("1") && menuItem.getItemId() == R.id.exit_edu) {
                            AlertDialog.Builder BackAlertDialog = new AlertDialog.Builder(getContext());
                            BackAlertDialog.setTitle((CharSequence) "Group Exit Alert");
                            BackAlertDialog.setMessage((CharSequence) "Are you sure want to exit ?");
                            BackAlertDialog.setIcon((int) R.drawable.logo);
                            BackAlertDialog.setPositiveButton((CharSequence) "NO", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            BackAlertDialog.setNegativeButton((CharSequence) "YES", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    exit_grp();
                                }

                            });
                            BackAlertDialog.show();
                        }
                        return false;

                    }
                });

            }
        });
        return view;
    }

    private void exit_grp() {
        final Map<String, String> params = new HashMap<>();
        String baseUrl = ProductConfig.edu_exit_grp + "?grp_id=" + mParam1 + "&user_id=" + Bsession.getInstance().getUser_id(getContext());
        System.out.println("memberiddd==" + mParam1);
        System.out.println("memberiddd==" + Bsession.getInstance().getUser_id(getContext()));

        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        Toast.makeText(getContext(), "Exitedd", Toast.LENGTH_SHORT).show();
                        //((EducationExistingActivity) getContext()).packagelist();
                        Intent intent = new Intent(getContext(), EducationExistingActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "Not found", Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsObjRequest);
    }

    private void subjectadd(String duriation) {
        final Map<String, String> params = new HashMap<>();
        String customer_id = Bsession.getInstance().getUser_id(getContext());

        //progressDialog.show();
        StringRequest jsObjRequest = new StringRequest(0, ProductConfig.subject_add + ("?subject=" + category.getText().toString()) + ("&user_id=" + customer_id) + ("&grop_id=" + mParam1) + ("&duriation=" + duriation) + ("&date=" + todayDateStr), new Response.Listener<String>() {
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        // eduid = jsonResponse.getString("id");

                        getSubject(todayDateStr);
                        category.setText("");
                        simpleTimePicker.setText("");
                        tableLayoutadd.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getContext(), "Group not created...please check ", Toast.LENGTH_LONG).show();

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
        Volley.newRequestQueue(getContext()).add(jsObjRequest);
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));

    }

    private void delete_grp() {
        final Map<String, String> params = new HashMap<>();
        String para_str = "?user_id=" + Bsession.getInstance().getUser_id(getContext());
        String para1 = "&grp_id=" + mParam1;

        String baseUrl = ProductConfig.education_delete + para_str + para1;
        System.out.println("base=" + baseUrl);
        progressDialog.show();
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        Toast.makeText(getContext(), "Group Deleted", Toast.LENGTH_SHORT).show();
                        //((EducationExistingActivity) getContext()).packagelist();
                        Intent intent = new Intent(getContext(), EducationExistingActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "Not found", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsObjRequest);
    }

    public void getSubject(String todayDateStr) {
        final Map<String, String> params = new HashMap<>();
        recycler_tablelist.setVisibility(View.VISIBLE);
        String para_str = "?user_id=" + Bsession.getInstance().getUser_id(getContext());
        String para1 = "&grop_id=" + mParam1;
        String para2 = "&date=" + todayDateStr;
        System.out.println("onDateSelected==" + currentDateStr);
        String baseUrl = ProductConfig.subjectlist + para_str + para1 + para2;
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
                        JSONArray jsonlist = jsonResponse.getJSONArray("list");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject1 = jsonlist.getJSONObject(j);
                            EducationModel model1 = new EducationModel();
                            model1.setId(jsonObject1.getString("id"));
                            model1.setSubject(jsonObject1.getString("subject"));
                            model1.setDuriation(jsonObject1.getString("duriation"));
                            itemList.add(model1);
                            SubjecAdapter personalAdapter = new SubjecAdapter(getContext(), itemList, mParam3);
                            recycler_tablelist.setAdapter(personalAdapter);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                            recycler_tablelist.setLayoutManager(layoutManager);
                        }
                    } else {

                        recycler_tablelist.setVisibility(View.GONE);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsObjRequest);

    }

    public void getSubjectDurations(Calendar selectedDate) {
        final Map<String, String> params = new HashMap<>();
        String para_str = "?user_id=" + Bsession.getInstance().getUser_id(getContext());
        String para1 = "&grop_id=" + mParam1;
        String baseUrl = ProductConfig.subjectlist + para_str + para1;

        progressDialog.show();
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        JSONArray jsonlist = jsonResponse.getJSONArray("list");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject1 = jsonlist.getJSONObject(j);
                            EducationModel model1 = new EducationModel();
                            model1.setId(jsonObject1.getString("id"));
                            model1.setSubject(jsonObject1.getString("subject"));
                            model1.setDuriation(jsonObject1.getString("duriation"));
                            itemList.add(model1);
                        }

                        // Update the adapter and layout manager
                        SubjecAdapter personalAdapter = new SubjecAdapter(getContext(), itemList, mParam3);
                        recycler_tablelist.setAdapter(personalAdapter);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        recycler_tablelist.setLayoutManager(layoutManager);
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsObjRequest);
    }

    public static class SubjecAdapter extends RecyclerView.Adapter<SubjecAdapter.ItemViewHolder> {
        private List<EducationModel> pesrsonalList;
        private Context mContext;
        String duriation = "", mParam3;

        public SubjecAdapter(Context mContext, List<EducationModel> pesrsonalList, String mParam3) {
            this.mContext = mContext;
            this.pesrsonalList = pesrsonalList;
            this.mParam3 = mParam3;

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
            holder.category.setText(pesrsonalList.get(position).getSubject());

            System.out.println("mParam=" + mParam3);
            if (mParam3.equalsIgnoreCase("1")) {
                holder.remove.setVisibility(View.VISIBLE);
            } else {
                holder.remove.setVisibility(View.GONE);
            }

            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removem(pesrsonalList.get(position).getId());
                }
            });
            if (pesrsonalList.get(position).getDuriation() != null) {
                holder.simpleTimePicker.setText(pesrsonalList.get(position).getDuriation());
            }


            holder.simpleTimePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(mContext,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    // Display the selected time in HH:MM format
                                    String formattedTime = String.format("%02d:%02d", hourOfDay, minute);
                                    Toast.makeText(mContext, "Selected time: " + formattedTime, Toast.LENGTH_SHORT).show();
                                    holder.simpleTimePicker.setText(formattedTime);
                                    duriation = holder.simpleTimePicker.getText().toString();
                                    System.out.println("Selected tim=" + duriation);
                                    dubjectupdate(pesrsonalList.get(position).getId(), duriation);


                                }
                            },
                            hour, minute, true);

                    timePickerDialog.show();
                }
            });

        }

        private void removem(String id) {
            final Map<String, String> params = new HashMap<>();
            String baseUrl = ProductConfig.removeeduca + "?id=" + id;

            final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Response", response.toString());
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                            Toast.makeText(mContext, "Removed", Toast.LENGTH_SHORT).show();
                            EducationInputFragment.getInstances().getSubject(todayDateStr);
                        } else {
                            Toast.makeText(mContext, "Activity not found", Toast.LENGTH_SHORT).show();
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

        private void dubjectupdate(String id, String duriation) {
            final Map<String, String> params = new HashMap<>();
            String para2 = "?id=" + id;
            String para3 = "&duriation=" + duriation;
            System.out.println("para2==" + para2);
            String baseUrl = ProductConfig.updatesubjectdurin + para2 + para3;

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
                            Toast.makeText(mContext, " not found", Toast.LENGTH_SHORT).show();
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
            TextView category;
            EditText simpleTimePicker;
            ImageView remove;

            public ItemViewHolder(View itemView) {
                super(itemView);

                category = itemView.findViewById(R.id.category);
                remove = itemView.findViewById(R.id.remove);
                simpleTimePicker = itemView.findViewById(R.id.simpleTimePicker);


            }
        }
    }


}