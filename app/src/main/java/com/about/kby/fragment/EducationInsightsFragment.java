package com.about.kby.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.about.kby.Bsession;
import com.about.kby.ProductConfig;
import com.about.kby.R;
import com.about.kby.model.EducationModel;
import com.about.kby.model.PieEntryModel;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

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
 * Use the {@link EducationInsightsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EducationInsightsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2,mParam3;
    PieChart pieChart;
    TextView shares;
    TextView txts;
    final ArrayList<String> idList = new ArrayList<>();
    final ArrayList<String> duriation = new ArrayList<>();
    private List<PieEntry> entries = new ArrayList<>();
    ImageView out_share;
    RecyclerView recyclerView;
    private List<EducationModel> PojoLists = new ArrayList<>();
    String todayDateStr;
    EditText today;
    RecyclerView recycle;
    public EducationInsightsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EducationInsightsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EducationInsightsFragment newInstance(String param1, String param2,String para3) {
        EducationInsightsFragment fragment = new EducationInsightsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, para3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_education_insights, container, false);
        pieChart=view.findViewById(R.id.piechart);
        shares=view.findViewById(R.id.shares);
        recycle=view.findViewById(R.id.recycle);
        out_share=view.findViewById(R.id.out_share);
        txts=view.findViewById(R.id.txts);
        today=view.findViewById(R.id.today);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
// Get a reference to your ListView
        shares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //editText.setText("  Select Type");
                PopupMenu popupMenu = new PopupMenu(getContext(),view);
                popupMenu.inflate(R.menu.share_popup);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.open) {
                            View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog_layout, null);
                            EditText editText = dialogView.findViewById(R.id.edit_text_id);
                            Log.d("description=",editText.getText().toString());
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setView(dialogView)
                                    .setTitle("OpenCircle")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Handle OK button click
                                            String description=editText.getText().toString();
                                            Log.d("description=",editText.getText().toString());
                                            methods(idList,duriation,description);
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Handle Cancel button click
                                        }
                                    });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                        if (menuItem.getItemId() == R.id.pers) {
                            final Map<String, String> params = new HashMap<>();
                            String para_str = "?user_id=" + Bsession.getInstance().getUser_id(getContext());
                            String para_str1 = "&user=" + Bsession.getInstance().getUser_mobile(getContext());
                            String baseUrl = ProductConfig.personal_exist + para_str + para_str1;
                            final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e("Response", response.toString());
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                                            JSONArray jsonlist = jsonResponse.getJSONArray("personal_grp_list");
                                            PopupMenu newPopupMenu = new PopupMenu(getContext(), view);

                                            // Iterate over personalGroupList to create menu items dynamically
                                            for (int i = 0; i < jsonlist.length(); i++) {
                                                JSONObject group = jsonlist.getJSONObject(i);
                                                String groupName = group.getString("grp_name");
                                                final String groupId = group.getString("grp_id");
                                                newPopupMenu.getMenu().add(Menu.NONE, i, Menu.NONE, groupName);
                                            }

                                            newPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem item) {
                                                    int position = item.getItemId(); // Get the position of the clicked item
                                                    try {
                                                        if (position >= 0 && position < jsonlist.length()) {
                                                            JSONObject group = jsonlist.getJSONObject(position);
                                                            String groupId = group.getString("grp_id");
                                                            System.out.println("iddddd=" + groupId);
                                                            // Now you can use groupId as needed
                                                            View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog_layout, null);
                                                            EditText editText = dialogView.findViewById(R.id.edit_text_id);
                                                            Log.d("description=",editText.getText().toString());
                                                            String description=editText.getText().toString();
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                            builder.setView(dialogView)
                                                                    .setTitle("Personal")
                                                                    .setCancelable(false)
                                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            // Handle OK button click
                                                                           // insertgroup(groupId,idList,duriation,description);
                                                                        }
                                                                    })
                                                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            // Handle Cancel button click
                                                                        }
                                                                    });

                                                            AlertDialog alertDialog = builder.create();
                                                            alertDialog.show();
                                                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    // Retrieve the EditText value when the OK button is clicked
                                                                    String description = editText.getText().toString();
                                                                    Log.d("description=",editText.getText().toString());
                                                                    insertgroup(groupId,idList,duriation,description);
                                                                    alertDialog.dismiss(); // Dismiss the dialog after handling the click
                                                                }
                                                            });
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    return false;
                                                }
                                            });

                                            newPopupMenu.setGravity(Gravity.BOTTOM);
                                            newPopupMenu.show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // Handle error
                                }
                            });

                            // Add the request to the RequestQueue to execute it
                            Volley.newRequestQueue(getContext()).add(jsObjRequest);
                        }

                        return false;
                    }
                });
            }
        });
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
                .textSize(14,14,14)
                .end()
                .defaultSelectedDate(defaultSelectedDate)
                .build();

        Log.i("Default Date", DateFormat.format("EEE, MM d, yyyy", defaultSelectedDate).toString());
        todayDateStr = DateFormat.format("MM/dd/yyyy", defaultSelectedDate).toString();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override

            public void onDateSelected(Calendar date, int position) {
                todayDateStr = DateFormat.format("MM/dd/yyyy", date).toString();
                //Toast.makeText(getContext(), selectedDateStr + " selected!", Toast.LENGTH_SHORT).show();
                Log.i("onDateSelected", todayDateStr + " - Position = " + position);
                educationinsight(todayDateStr);
            }

        });

        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalCalendar.goToday(false);
                Calendar currentDate = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                todayDateStr = dateFormat.format(currentDate.getTime());

                // today.setText(todayDateStr);
                // Do something with today's date, for example, display it in a toast
                // Toast.makeText(getContext(), "Today's date: " + todayDateStr, Toast.LENGTH_SHORT).show();
            }
        });
        out_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "APP LINK : " + "https://play.google.com/store/apps/details?id=com.about.kby";
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
       // educationinsight();
        return view;
    }

    private void insertgroup(String description, ArrayList<String> groupId, ArrayList<String> idList, String duriation) {
        final Map<String, String> params = new HashMap<>();
        String idListcs = TextUtils.join(",", idList);
        String idListsubs = TextUtils.join(",", new String[]{duriation});
        String para_str2 = "&group_id=" + groupId;
        String para_str3 = "&subject=" + idListcs;
        String para_str5 = "&duriation=" +idListsubs ;
        String para_str6 = "&description=" +description ;
        String baseUrl = ProductConfig.education_group+"?user_id="+ Bsession.getInstance().getUser_id(getContext())+para_str2+para_str3+para_str5+para_str6;
        System.out.println("baseurl==="+baseUrl);
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {

                         Toast.makeText(getContext(), "Update On Open Circle", Toast.LENGTH_SHORT).show();

                    } else {
                        // Toast.makeText(getContext(), "Activity not found", Toast.LENGTH_SHORT).show();
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

    private void methods(ArrayList<String> idList, ArrayList<String> duriation, String description) {
        final Map<String, String> params = new HashMap<>();
        String idString = TextUtils.join(",", idList);
        String duriations = TextUtils.join(",", duriation);
        System.out.println("ID: " + idString);
        String para_str1 = "&type=" + "opencircle";
        String para_str2 = "&name=" + Bsession.getInstance().getUser_name(getContext());
        String para_str3 = "&sunject=" + idString;
        String para_str4 = "&duration=" + duriations;
        String para_str5 = "&description=" + description;
        String baseUrl = ProductConfig.subjectopencircle+"?user_id="+ Bsession.getInstance().getUser_id(getContext())+para_str1+para_str2+para_str3+para_str4+para_str5;
        System.out.println("baseurl==="+baseUrl);
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {

                        Toast.makeText(getContext(), "Update On Open Circle", Toast.LENGTH_SHORT).show();

                    } else {
                        // Toast.makeText(getContext(), "Activity not found", Toast.LENGTH_SHORT).show();
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


    public void educationinsight(String todayDateStr) {
        entries.clear();
        pieChart.clear();
        final Map<String, String> params = new HashMap<>();
        recycle.setVisibility(View.VISIBLE);
        String baseUrl = ProductConfig.subjectlist+"?user_id="+ Bsession.getInstance().getUser_id(getContext())+("&grop_id="+mParam1)+("&date="+todayDateStr);
        System.out.println("baseURL="+baseUrl);
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        PojoLists = new ArrayList<>();
                        JSONArray jsonlist = jsonResponse.getJSONArray("list");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject1 = jsonlist.getJSONObject(j);
                            EducationModel model1 = new EducationModel();
                            model1.setId(jsonObject1.getString("id"));
                            model1.setSubject(jsonObject1.getString("subject"));
                            model1.setDuriation(jsonObject1.getString("duriation"));
                            //  model1.setDate(jsonObject1.getString("date"));
                            String timeString = jsonObject1.getString("duriation"); // Assuming timeString is in the format HH:mm
                            String[] timeParts = timeString.split(":"); // Splitting hours and minutes
                            if (timeString.isEmpty()){
                                recycle.setVisibility(View.GONE);
                            }
                            idList.add(model1.getSubject());
                            duriation.add(model1.getDuriation());


                            PojoLists.add(model1);
                            if (!timeString.isEmpty()) {
                                entries.add(new PieEntry(
                                        Float.parseFloat(timeParts[0] + "." + timeParts[1])));

                            }
                        }
                        String[] idArray = idList.toArray(new String[0]);

                        for ( String id : idArray) {
                            System.out.println("ID: " + id);

                        }
                        ArrayList<Integer> colors = new ArrayList<>();

                        for (int color : ColorTemplate.MATERIAL_COLORS) {
                            colors.add(color);
                        }

                        for (int color : ColorTemplate.JOYFUL_COLORS) {
                            colors.add(color);
                        }

                        PieDataSet dataSet = new PieDataSet(entries, "");
                        dataSet.setColors(colors);
                        PieData data = new PieData(dataSet);
                        pieChart.setData(data);
                        dataSet.setValueFormatter(new PercentFormatter(pieChart));
                        dataSet.setValueTextSize(16f); // Set text size
                        dataSet.setValueTextColor(Color.BLACK);
                        pieChart.isDrawEntryLabelsEnabled();
                        pieChart.invalidate();
                        pieChart.animateXY(1000, 1000);

                        List<PieEntryModel> entryModels = new ArrayList<>();
                        for (int i = 0; i < PojoLists.size(); i++) {
                            EducationModel model = PojoLists.get(i);
                            int color = colors.get(i % colors.size()); // Assuming colors.size() > 0
                            PieEntryModel entryModel = new PieEntryModel(model.getSubject(), model.getDuriation(), color);
                            entryModels.add(entryModel);
                        }

                        recycle.setLayoutManager(new LinearLayoutManager(getContext()));
                        PieEntryAdapter adapter = new PieEntryAdapter(entryModels);
                        recycle.setAdapter(adapter);

                    } else {
                        recycle.setVisibility(View.GONE);
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
    public class PieEntryAdapter extends RecyclerView.Adapter<EducationInsightsFragment.PieEntryAdapter.ViewHolder> {

        private List<PieEntryModel> entryList;

        public PieEntryAdapter(List<PieEntryModel> entryList) {
            this.entryList = entryList;
        }

        @NonNull
        @Override
        public EducationInsightsFragment.PieEntryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pie_entry, parent, false);
            return new EducationInsightsFragment.PieEntryAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EducationInsightsFragment.PieEntryAdapter.ViewHolder holder, int position) {
            PieEntryModel entry = entryList.get(position);
            holder.colorBlock.setBackgroundColor(entry.getColor());
            String text = entry.getSubcategory() + " - " + entry.getDuriation();
            holder.tvEntryText.setText(text);
        }

        @Override
        public int getItemCount() {
            return entryList.size();
        }

        public  class ViewHolder extends RecyclerView.ViewHolder {
            View colorBlock;
            TextView tvEntryText;

            ViewHolder(View view) {
                super(view);
                colorBlock = view.findViewById(R.id.colorBlock);
                tvEntryText = view.findViewById(R.id.tvEntryText);
            }
        }
    }

}